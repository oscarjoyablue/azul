package sv.com.dte.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import sv.com.dte.model.DatosComplementariosDTE;
import sv.com.dte.model.DteElectronico;
import sv.com.dte.model.JsonCuerpoDocumento;
import sv.com.dte.model.JsonDatosDTE;
import sv.com.dte.model.JsonDireccion;
import sv.com.dte.model.JsonDocumento;
import sv.com.dte.model.JsonDocumentoRelacionado;
import sv.com.dte.model.JsonEmisor;
import sv.com.dte.model.JsonIdentificacion;
import sv.com.dte.model.JsonMotivo;
import sv.com.dte.model.JsonReceptor;
import sv.com.dte.model.JsonResumen;
import sv.com.dte.model.JsonSujetoExcluido;
import sv.com.dte.model.JsonTributos;
import sv.com.dte.repo.IDteElectronicoRepo;
import sv.com.dte.util.Utils;

@Service
public class ServicioPBSImp 
{
	private static final Logger logger = LoggerFactory.getLogger(DTEServiceImpl.class);
	
	@Autowired
	private DocuWareServiceImp docuWareServiceImp;
	
	@Autowired
	private IDteElectronicoRepo dteElectronicoRepo;
	
	@Value("${pbs.usuario}")
	private String pbsUsuario;
	
	@Value("${pbs.clave}")
	private String pbsClave;
	
	@Value("${pbs.direccion}")
	private String pbsDireccion;
	
	@Value("${reintentos.envio.documentos}")
	private Integer reintentosEnvioDocumentos;
	
	@Value("${correo.from}")
	private String correoFrom;
	
	@Value("${correo.to}")
	private String correoTo;
	
	@Value("${correo.subject}")
	private String correoSubject;
	
	@Value("${correo.body}")
	private String correoBody;

	@Value("${correo.direccion}")
	private String correodireccion;
	
	@Autowired
	private RestTemplate restTemplate;
			
	public String ObtenerToken() 
	{
		String strToken = "";
		
		logger.info("Se ingresa a metodo ObtenerToken()");
		
		try
		{
			// REALIZANDO CONSULTA DE TOKEN
			// ----------------------------
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			String jsonCredenciales = "{\"username\":\"" +  pbsUsuario +"\",\"password\":\"" + pbsClave + "\"}";
			HttpEntity<String> entity = new HttpEntity<String>(jsonCredenciales, headers);
		
			String response = restTemplate.postForObject(pbsDireccion + "/v1/security", entity, String.class);
			logger.info("Respuesta de generacion token: " + response);
			
			// TRANSFORMANDO RESPONSE EN JSON
			JsonObject jsonRespuesta = new Gson().fromJson(response, JsonObject.class);
			strToken = jsonRespuesta.get("token").toString().replace("\"","");
		}		
		catch (Exception ex) 
		{
			logger.error("Error al generar token: " + ex.toString());
			ex.printStackTrace();
		}
		
		return strToken;
	}
	
	public String EnviarDocumento(DatosComplementariosDTE datosComplementariosDTE, JsonDatosDTE documentoDTE)
	{
		String strRespuesta = "";
		Integer intContador = 0;
		
		logger.info("Se ingresa a metodo EnviarDocumento()");
		
		try
		{
			Boolean blnEnviar = true;
			
			while(blnEnviar == true)
			{				
				strRespuesta = EnviarDocumentoAProveedor(datosComplementariosDTE, documentoDTE);
				
				blnEnviar = false;
				intContador++;
				
				if	(intContador <= reintentosEnvioDocumentos && ErrorEnConexion(strRespuesta) == true)
				{
					blnEnviar = true;
					logger.info("Se hace reenvio numero: " + intContador.toString() + " de documento con numero de control: " + documentoDTE.getIdentificacion().getNumeroControl());
				}
			}
			
			// SE ENVIARA CORREO DE NOTIFICACION EN CASO QUE NO SE ENVIE DOCUMENTO A PROVEEDOR
			if	(ErrorEnConexion(strRespuesta) == true)
			{
				// ACTUALIZANDO ESTADO  DTE A 6 = PENDIENTE_PROVEEDOR
				dteElectronicoRepo.updateEstadoDTE(documentoDTE.getIdentificacion().getCodigoGeneracion(),6);

				EnviarCorreoNotificacion(datosComplementariosDTE, documentoDTE);
			}
			
			return strRespuesta; 
		}		
		catch (Exception ex) 
		{
			logger.error("Error al enviar DTE: " + ex.toString());
			strRespuesta = ex.toString();
			ex.printStackTrace();
		}
		
		return strRespuesta;
	}
	
	public boolean ErrorEnConexion(String cadena)
	{
		boolean errorEnConexion = false;
		
		if	(cadena.indexOf("HttpClientErrorException") >= 0 && (cadena.indexOf("404") >= 0 || cadena.indexOf("500") >= 0))
		{
			errorEnConexion = true;	
		}
		
		return errorEnConexion;
	}
	
	public String EnviarDocumentoAProveedor(DatosComplementariosDTE datosComplementariosDTE, JsonDatosDTE documentoDTE)
	{
		String strRespuesta = "";
		
		logger.info("Se ingresa a metodo EnviarDocumentoAProveedor()");
		
		try
		{
			// GENERANDO TOKEN
			// ---------------
			String strToken = ObtenerToken();
			
			// QUITANDO NODOS NO NECESARIOS
			// ----------------------------
			String jsonDTE = QuitarNodos(datosComplementariosDTE.getTipoDocumento(),documentoDTE);
			logger.info("JSON a enviar :" + jsonDTE);
			
			// HEADER PARA ENVIDO DE DOCUMENTO
			// -------------------------------
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(strToken);
			
			HttpEntity<String> entity = new HttpEntity<String>(jsonDTE, headers);
			
			// ENVIANDO ANULACIONES Y DTEs
			// ---------------------------
			if (datosComplementariosDTE.getTipoDocumento().equals("00"))
			{
				// GUARDANDO ANULACION A ENVIAR
				dteElectronicoRepo.updateDTEAnulacion(documentoDTE.getIdentificacion().getCodigoGeneracion(),jsonDTE);
				
				// ENVIANDO ANULACION
				strRespuesta = restTemplate.postForObject(pbsDireccion + "/v1/cancellation", entity, String.class);
				logger.info("Respuesta de envio DTE: " + strRespuesta);

				// GUARDANDO JSON DE RESPUESTA
				dteElectronicoRepo.updateRespuestaAnulacion(documentoDTE.getIdentificacion().getCodigoGeneracion(),strRespuesta);
				
				// ACTUALIZANDO ESTADO 2 = ENVIADO A PROVEEDOR
				dteElectronicoRepo.updateEstadoDTE(documentoDTE.getIdentificacion().getCodigoGeneracion(),2);
			}
			else 
			{
				// GUARDANDO DTE A ENVIAR
				dteElectronicoRepo.updateDTEEnviar(documentoDTE.getIdentificacion().getCodigoGeneracion(),jsonDTE);
				
				// ENVIANDO DTE
				strRespuesta = restTemplate.postForObject(pbsDireccion + "/v5/receptionBiller", entity, String.class);
				logger.info("Respuesta de envio DTE: " + strRespuesta);
				
				// GUARDANDO JSON DE RESPUESTA
				dteElectronicoRepo.updateRespuesta(documentoDTE.getIdentificacion().getCodigoGeneracion(),strRespuesta);
				
				// OBTENIENDO SELLO DE RECIBIDO Y DOCUMENTO BASE 64
				JsonObject jsonObjectRespuesta = new Gson().fromJson(strRespuesta, JsonObject.class);
				String strSelloRecibido = jsonObjectRespuesta.getAsJsonObject("responseMH").get("selloRecibido").toString().replace("\"","");
				String strDocumentoBase64 = jsonObjectRespuesta.get("digital").toString().replace("\"","");
				
				// ACTUALIZANDO CAMPOS
				if (strSelloRecibido.equals("null"))
				{
					// ACTUALIZANDO ESTADO 5 = RECEPCION CONTINGENCIA MH 
					dteElectronicoRepo.updateEstadoDTE(documentoDTE.getIdentificacion().getCodigoGeneracion(),5);
				}
				else
				{
					// ACTUALIZANDO SELLO DE RECIBIDO Y DOCUMENTO BASE 64
					dteElectronicoRepo.updateSelloDocumento(documentoDTE.getIdentificacion().getCodigoGeneracion(),strSelloRecibido, strDocumentoBase64);
					
					// ENVIANDO DOCUMENTO A DOCUWARE					
					Integer intEstado = docuWareServiceImp.EnviarDocumento(documentoDTE.getIdentificacion().getCodigoGeneracion(),datosComplementariosDTE.getNumeroDeTransaccion() + ".pdf", datosComplementariosDTE.getTipoDocumento(), datosComplementariosDTE.getNumeroDeTransaccion(), documentoDTE.getIdentificacion().getNumeroControl(), datosComplementariosDTE.getNumeroCuenta(), datosComplementariosDTE.getCodigoCliente(), documentoDTE.getIdentificacion().getFecEmi(), strDocumentoBase64);
					
					// ACTUALIZANDO ESTADO
					dteElectronicoRepo.updateEstadoDTE(documentoDTE.getIdentificacion().getCodigoGeneracion(),intEstado);
				}
			}		
		}		
		catch (Exception ex) 
		{
			logger.error("Error al enviar DTE: " + ex.toString());
			strRespuesta = ex.toString();
			ex.printStackTrace();
		}
		
		return strRespuesta;
	}
	
	public String QuitarNodos(String tipoDocumento, JsonDatosDTE documentoDTE)
	{
		// TRANSFORMANDO DOCUMENTO DTE A STRING CON FORMATO JSON
		// -----------------------------------------------------
		Gson gson = new GsonBuilder().serializeNulls().create();		
		String strJson = gson.toJson(documentoDTE);
		
		// TRANSFORMANDO STRING A OBJETO JSON
		// ----------------------------------
		JsonObject jsonObjectDTE = new Gson().fromJson(strJson, JsonObject.class);
		
		// VARIABLE PARA RETORNAR STRING JSON
		// ----------------------------------
		String strJsonFinal = "";
		
		// QUITANDO NODOS: 
		// 01 = FACTURA
		// 03 = CREDITO FISCAL
		// 05 = NOTA CREDITO
		// 07 = COMPROBANTE DE RETENCION
		// 14 = FACTURA SUJETO EXCLUIDO
		// -----------------------------
		if (tipoDocumento.equals("03"))
		{
			jsonObjectDTE.getAsJsonObject("identificacion").remove("fecAnula");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("horAnula");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigoMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigo");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVentaMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVenta");
			jsonObjectDTE.getAsJsonObject("emisor").remove("nomEstablecimiento");
			jsonObjectDTE.getAsJsonObject("receptor").remove("numDocumento");
			jsonObjectDTE.getAsJsonObject("receptor").remove("tipoDocumento");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIva");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalSujetoRetencion");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIVAretenido");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIVAretenidoLetras");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalCompra");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descu");
			jsonObjectDTE.getAsJsonObject("resumen").remove("observaciones");
			jsonObjectDTE.remove("sujetoExcluido");
			jsonObjectDTE.remove("documento");
			jsonObjectDTE.remove("motivo");
			
			strJsonFinal = gson.toJson(jsonObjectDTE);
			
			// QUITANDO ITEMS DE LA CLASE CUERPO DOCUMENTO
			// -------------------------------------------
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocivaItem\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocivaRetenido\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocfechaEmision\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctipoDte\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctipoDoc\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocnumDocumento\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocmontoSujetoGrav\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccodigoRetencionMH\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccompra\":null,","");
		}
		else if (tipoDocumento.equals("01"))
		{			
			jsonObjectDTE.getAsJsonObject("identificacion").remove("fecAnula");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("horAnula");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigoMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigo");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVentaMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVenta");
			jsonObjectDTE.getAsJsonObject("emisor").remove("nomEstablecimiento");		
			jsonObjectDTE.getAsJsonObject("receptor").remove("nombreComercial");
			jsonObjectDTE.getAsJsonObject("receptor").remove("nit");
			jsonObjectDTE.getAsJsonObject("resumen").remove("ivaPerci1");
			jsonObjectDTE.getAsJsonObject("resumen").remove("ivaPerci1");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalSujetoRetencion");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIVAretenido");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIVAretenidoLetras");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalCompra");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descu");
			jsonObjectDTE.getAsJsonObject("resumen").remove("observaciones");
			jsonObjectDTE.remove("sujetoExcluido");
			jsonObjectDTE.remove("documento");
			jsonObjectDTE.remove("motivo");
			
			strJsonFinal = gson.toJson(jsonObjectDTE);
			
			// QUITANDO ITEMS DE LA CLASE CUERPO DOCUMENTO
			// -------------------------------------------
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocivaRetenido\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocfechaEmision\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctipoDte\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctipoDoc\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocnumDocumento\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocmontoSujetoGrav\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccodigoRetencionMH\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccompra\":null,","");		
		}
		else if (tipoDocumento.equals("05"))
		{			
			jsonObjectDTE.getAsJsonObject("identificacion").remove("fecAnula");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("horAnula");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codPuntoVentaMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codPuntoVenta");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codEstableMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codEstable");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigoMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigo");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVentaMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVenta");
			jsonObjectDTE.getAsJsonObject("emisor").remove("nomEstablecimiento");
			jsonObjectDTE.getAsJsonObject("receptor").remove("numDocumento");
			jsonObjectDTE.getAsJsonObject("receptor").remove("tipoDocumento");
			jsonObjectDTE.getAsJsonObject("resumen").remove("porcentajeDescuento");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalNoGravado");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalPagar");
			jsonObjectDTE.getAsJsonObject("resumen").remove("saldoFavor");
			jsonObjectDTE.getAsJsonObject("resumen").remove("pagos");
			jsonObjectDTE.getAsJsonObject("resumen").remove("numPagoElectronico");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIva");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalSujetoRetencion");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIVAretenido");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIVAretenidoLetras");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalCompra");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descu");
			jsonObjectDTE.getAsJsonObject("resumen").remove("observaciones");
			jsonObjectDTE.remove("otrosDocumentos");
			jsonObjectDTE.remove("sujetoExcluido");
			jsonObjectDTE.remove("documento");
			jsonObjectDTE.remove("motivo");
			
			strJsonFinal = gson.toJson(jsonObjectDTE);
			
			// QUITANDO ITEMS DE LA CLASE CUERPO DOCUMENTO
			// -------------------------------------------
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocivaItem\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocpsv\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocnoGravado\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocivaRetenido\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocfechaEmision\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctipoDte\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctipoDoc\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocnumDocumento\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocmontoSujetoGrav\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccodigoRetencionMH\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccompra\":null,","");		
		}
		else if (tipoDocumento.equals("07"))
		{			
			jsonObjectDTE.getAsJsonObject("identificacion").remove("fecAnula");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("horAnula");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codPuntoVentaMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codPuntoVenta");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codEstableMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codEstable");
			jsonObjectDTE.getAsJsonObject("emisor").remove("nomEstablecimiento");
			jsonObjectDTE.getAsJsonObject("receptor").remove("nit");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalExenta");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalNoSuj");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalGravada");
			jsonObjectDTE.getAsJsonObject("resumen").remove("subTotalVenta");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descuNoSuj");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descuExenta");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descuGravada");
			jsonObjectDTE.getAsJsonObject("resumen").remove("porcentajeDescuento");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalDescu");
			jsonObjectDTE.getAsJsonObject("resumen").remove("tributos");
			jsonObjectDTE.getAsJsonObject("resumen").remove("subTotal");
			jsonObjectDTE.getAsJsonObject("resumen").remove("ivaPerci1");
			jsonObjectDTE.getAsJsonObject("resumen").remove("ivaRete1");
			jsonObjectDTE.getAsJsonObject("resumen").remove("reteRenta");
			jsonObjectDTE.getAsJsonObject("resumen").remove("montoTotalOperacion");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalNoGravado");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalPagar");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalLetras");
			jsonObjectDTE.getAsJsonObject("resumen").remove("saldoFavor");
			jsonObjectDTE.getAsJsonObject("resumen").remove("condicionOperacion");
			jsonObjectDTE.getAsJsonObject("resumen").remove("pagos");
			jsonObjectDTE.getAsJsonObject("resumen").remove("numPagoElectronico");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIva");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalCompra");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descu");
			jsonObjectDTE.getAsJsonObject("resumen").remove("observaciones");
			jsonObjectDTE.getAsJsonObject("resumen").remove("subTotalVentas");
			jsonObjectDTE.remove("sujetoExcluido");
			jsonObjectDTE.remove("otrosDocumentos");
			jsonObjectDTE.remove("ventaTercero");
			jsonObjectDTE.remove("documentoRelacionado");
			jsonObjectDTE.remove("documento");
			jsonObjectDTE.remove("motivo");
			
			strJsonFinal = gson.toJson(jsonObjectDTE);
			
			// QUITANDO ITEMS DE LA CLASE CUERPO DOCUMENTO
			// -------------------------------------------
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctipoItem\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocnumeroDocumento\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccodTributo\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccantidad\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocuniMedida\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocprecioUni\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocmontoDescu\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocventaNoSuj\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocventaExenta\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocventaGravada\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctributos\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocpsv\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocnoGravado\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccodigo\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocivaItem\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccompra\":null,","");
		}
		else if (tipoDocumento.equals("14"))
		{			
			jsonObjectDTE.getAsJsonObject("identificacion").remove("fecAnula");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("horAnula");			
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigoMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigo");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVentaMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVenta");
			jsonObjectDTE.getAsJsonObject("emisor").remove("nomEstablecimiento");
			jsonObjectDTE.getAsJsonObject("emisor").remove("tipoEstablecimiento");
			jsonObjectDTE.getAsJsonObject("emisor").remove("nombreComercial");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalExenta");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalNoSuj");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalGravada");
			jsonObjectDTE.getAsJsonObject("resumen").remove("subTotalVenta");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descuNoSuj");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descuExenta");
			jsonObjectDTE.getAsJsonObject("resumen").remove("descuGravada");
			jsonObjectDTE.getAsJsonObject("resumen").remove("porcentajeDescuento");
			jsonObjectDTE.getAsJsonObject("resumen").remove("tributos");
			jsonObjectDTE.getAsJsonObject("resumen").remove("ivaPerci1");
			jsonObjectDTE.getAsJsonObject("resumen").remove("montoTotalOperacion");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalNoGravado");
			jsonObjectDTE.getAsJsonObject("resumen").remove("saldoFavor");
			jsonObjectDTE.getAsJsonObject("resumen").remove("numPagoElectronico");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIva");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalSujetoRetencion");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIVAretenido");
			jsonObjectDTE.getAsJsonObject("resumen").remove("totalIVAretenidoLetras");
			jsonObjectDTE.getAsJsonObject("resumen").remove("subTotalVentas");
			jsonObjectDTE.remove("receptor");
			jsonObjectDTE.remove("documento");
			jsonObjectDTE.remove("motivo");
			jsonObjectDTE.remove("extension");
			jsonObjectDTE.remove("otrosDocumentos");
			jsonObjectDTE.remove("ventaTercero");
			jsonObjectDTE.remove("documentoRelacionado");
			
			strJsonFinal = gson.toJson(jsonObjectDTE);
			
			// QUITANDO ITEMS DE LA CLASE CUERPO DOCUMENTO
			// -------------------------------------------
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocnumeroDocumento\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccodTributo\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocventaNoSuj\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocventaExenta\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocventaGravada\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctributos\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocpsv\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocnoGravado\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocivaItem\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocivaRetenido\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocfechaEmision\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctipoDte\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoctipoDoc\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocnumDocumento\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDocmontoSujetoGrav\":null,","");
			strJsonFinal = strJsonFinal.replace("\"EtiCuerpoDoccodigoRetencionMH\":null,","");
		}
		else if (tipoDocumento.equals("00"))
		{			
			jsonObjectDTE.getAsJsonObject("identificacion").remove("tipoDte");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("numeroControl");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("tipoModelo");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("tipoOperacion");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("tipoContingencia");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("motivoContin");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("fecEmi");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("horEmi");
			jsonObjectDTE.getAsJsonObject("identificacion").remove("tipoMoneda");
			jsonObjectDTE.getAsJsonObject("emisor").remove("correo");
			jsonObjectDTE.getAsJsonObject("emisor").remove("nrc");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codActividad");
			jsonObjectDTE.getAsJsonObject("emisor").remove("descActividad");
			jsonObjectDTE.getAsJsonObject("emisor").remove("nombreComercial");
			jsonObjectDTE.getAsJsonObject("emisor").remove("direccion");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigoMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("codigo");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVentaMH");
			jsonObjectDTE.getAsJsonObject("emisor").remove("puntoVenta");
			jsonObjectDTE.remove("receptor");
			jsonObjectDTE.remove("cuerpoDocumento");
			jsonObjectDTE.remove("resumen");
			
			strJsonFinal = gson.toJson(jsonObjectDTE);
		}
		
		strJsonFinal = strJsonFinal.replace("EtiCuerpoDoc","");	
		
		return strJsonFinal;
	}
	
	public void EnviarCorreoNotificacion(DatosComplementariosDTE datosComplementariosDTE, JsonDatosDTE documentoDTE)
	{
		logger.info("Se ingresa a metodo EnviarCorreoNotificacio()");
		
		try
		{
			String strFecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date()); 
			
			String asuntoCorreo = correoSubject;
			asuntoCorreo = asuntoCorreo.replace("@{Fecha}", strFecha);
			
			Utils utils = new Utils();
			
			String cuerpoCorreo = correoBody;
			cuerpoCorreo = cuerpoCorreo.replace("@{NoTransaccion}", datosComplementariosDTE.getNumeroDeTransaccion());
			cuerpoCorreo = cuerpoCorreo.replace("@{CodigoCliente}", datosComplementariosDTE.getCodigoCliente());
			cuerpoCorreo = cuerpoCorreo.replace("@{NombreCliente}", datosComplementariosDTE.getNombreCliente());
			cuerpoCorreo = cuerpoCorreo.replace("@{TipoDTE}", utils.ObtenerDescripcionTipoDTE(datosComplementariosDTE.getTipoDocumento()));
			cuerpoCorreo = cuerpoCorreo.replace("@{NoDTE}", documentoDTE.getIdentificacion().getNumeroControl());
			cuerpoCorreo = cuerpoCorreo.replace("@{FechaDTE}", documentoDTE.getIdentificacion().getFecEmi());
			
			String jsonEnvioCorreo = 
					"{"
					+ "\"from\": \"" + correoFrom + "\","
					+ "\"to\":[{"
					+ "    \"to\": \""+ correoTo +"\",\r\n"
					+ "    \"subject\": \"" + asuntoCorreo + "\","
					+ "    \"body\": \"" + cuerpoCorreo + "\","
					+ "    \"file\": \"\""
					+ "    }]"
					+ "}";
			
			logger.info("JSON a enviar para notificar por correo error en envio de documento a proveedor: " + jsonEnvioCorreo);
			
			// ENVIANDO CORREO
			// ---------------
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
						
			HttpEntity<String> entity = new HttpEntity<String>(jsonEnvioCorreo, headers);
			
			String strRespuesta = restTemplate.postForObject(correodireccion, entity, String.class);
			logger.info("Respuesta de envio de correo: " + strRespuesta);
		
		}		
		catch (Exception ex) 
		{
			logger.error("Error al enviar correo: " + ex.toString());
			ex.printStackTrace();
		}
	}
	
	public String ReprocesarDocumentos()
	{
		String strRespuesta = "";
		logger.info("Se ingresa a metodo ReprocesarDocumentos()");
		
		try
		{
			// CONSULTANDO DOCUMENTOS A ENVIAR
			// -------------------------------
			List<DteElectronico> documentos = dteElectronicoRepo.buscarPorEstadoDTE(6);
			
			if (documentos.size() > 0)
			{
				// GENERANDO TOKEN
				// ---------------
				String strToken = ObtenerToken();
				
				for(DteElectronico documentoDTE : documentos) 
				{
					// ENVIANDO DOCUMENTO
					// ------------------
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.setBearerAuth(strToken);
					
					String strJsonEnviar = "";
					String urlEnvio = "";
					
					if (documentoDTE.getCodigoAnulacion() != null)
					{
						urlEnvio = pbsDireccion + "/v1/cancellation";
						strJsonEnviar = documentoDTE.getDTEAnulacion();
					}
					else
					{
						urlEnvio = pbsDireccion + "/v5/receptionBiller";
						strJsonEnviar = documentoDTE.getDTEEnviar();
					}
					
					HttpEntity<String> entity = new HttpEntity<String>(strJsonEnviar, headers);
					logger.info("JSON a enviar :" + strJsonEnviar);
					
					strRespuesta = restTemplate.postForObject(urlEnvio, entity, String.class);
					logger.info("Respuesta de envio DTE: " + strRespuesta);
					
					// GUARDANDO DATOS OBTENIDOS DE JSON RESPUESTA
					// -------------------------------------------
					if (documentoDTE.getCodigoAnulacion() != null)
					{
						documentoDTE.setRespuestaAnulacion(strRespuesta);
						documentoDTE.setEstadoDTE(2);
					}
					else 
					{
						documentoDTE.setRespuesta(strRespuesta);
						
						// OBTENIENDO SELLO DE RECIBIDO Y DOCUMENTO BASE 64
						JsonObject jsonObjectRespuesta = new Gson().fromJson(strRespuesta, JsonObject.class);
						String strSelloRecibido = jsonObjectRespuesta.getAsJsonObject("responseMH").get("selloRecibido").toString().replace("\"","");
						String strDocumentoBase64 = jsonObjectRespuesta.get("digital").toString().replace("\"","");
						
						// ACTUALIZANDO DATOS
						if (strSelloRecibido.equals("null"))
						{
							// ACTUALIZANDO ESTADO 5 = RECEPCION CONTINGENCIA MH 
							documentoDTE.setEstadoDTE(5);
						}
						else
						{
							documentoDTE.setSelloRecibido(strSelloRecibido);
							documentoDTE.setDocumentoBase64(strDocumentoBase64);
							
							// ENVIANDO DOCUMENTO A DOCUWARE
							String fechaEmision = documentoDTE.getFechaEmi() + "        ";
							fechaEmision = (fechaEmision.substring(0,4) + "-" + fechaEmision.substring(4,6) + "-" + fechaEmision.substring(6,8)).trim();

							Integer intEstado = docuWareServiceImp.EnviarDocumento(documentoDTE.getCodigoGeneracion(),documentoDTE.getReferenciaT24() + ".pdf", documentoDTE.getTipoDte(), documentoDTE.getReferenciaT24(), documentoDTE.getNumeroControl(), documentoDTE.getCuenta(), documentoDTE.getNiuCliente(), fechaEmision, strDocumentoBase64);
							documentoDTE.setEstadoDTE(intEstado);		
						}
					}					
					
					dteElectronicoRepo.save(documentoDTE);
				}
				
				strRespuesta = "Se finaliza con exito proceso de reenvio de DTEs, se proceso un total de " + documentos.size() + " documentos";
				logger.info(strRespuesta);
			}
			else 
			{
				strRespuesta = "No hay documentos para reprocesar";
				logger.info(strRespuesta);
			}
		}		
		catch (Exception ex) 
		{
			strRespuesta = "Error al enviar DTE: " + ex.toString();
			logger.error(strRespuesta);
			ex.printStackTrace();
		}		

		return strRespuesta;
	}
	
	public void ProbarEnvioDocumentos()
	{
		// DOCUMENTO 01
		// ------------
		String strDocumento01 = DocumentoEjemplo("01");
		JsonObject convertedObject01 = new Gson().fromJson(strDocumento01, JsonObject.class);
		
		// DOCUMENTO 03
		// ------------
		String strDocumento03 = DocumentoEjemplo("03");
		JsonObject convertedObject03 = new Gson().fromJson(strDocumento03, JsonObject.class);
		
		// DOCUMENTO 05
		// ------------		
		String strDocumento05 = DocumentoEjemplo("05");
		JsonObject convertedObject05 = new Gson().fromJson(strDocumento05, JsonObject.class);
		
		// DOCUMENTO 07
		// ------------		
		String strDocumento07 = DocumentoEjemplo("07");
		JsonObject convertedObject07 = new Gson().fromJson(strDocumento07, JsonObject.class);
		
		// DOCUMENTO 14
		// ------------
		String strDocumento14 = DocumentoEjemplo("14");
		JsonObject convertedObject14 = new Gson().fromJson(strDocumento14, JsonObject.class);
		
		// DOCUMENTO 00
		// ------------
		String strDocumento00 = DocumentoEjemplo("00");
		JsonObject convertedObject00 = new Gson().fromJson(strDocumento00, JsonObject.class);
		
		logger.info((convertedObject01.getAsJsonObject("responseMH").getAsJsonArray("observaciones").size() > 0) ? "Error documento 01" : "Exito Documento 01");
		logger.info((convertedObject03.getAsJsonObject("responseMH").getAsJsonArray("observaciones").size() > 0) ? "Error documento 03" : "Exito Documento 03");
		logger.info((convertedObject05.getAsJsonObject("responseMH").getAsJsonArray("observaciones").size() > 0) ? "Error documento 05" : "Exito Documento 05");
		logger.info((convertedObject07.getAsJsonObject("responseMH").getAsJsonArray("observaciones").size() > 0) ? "Error documento 07" : "Exito Documento 07");
		logger.info((convertedObject14.getAsJsonObject("responseMH").getAsJsonArray("observaciones").size() > 0) ? "Error documento 14" : "Exito Documento 14");
		logger.info((convertedObject00.getAsJsonObject("responseMH").getAsJsonArray("observaciones").size() > 0) ? "Error documento 00" : "Exito Documento 00");
	}
	
	public String DocumentoEjemplo(String tipoDocumento)
	{
		JsonDatosDTE jsonDatosDTE = new JsonDatosDTE();
		JsonIdentificacion jsonIdentificacion = new JsonIdentificacion();
		JsonEmisor jsonEmisor = new JsonEmisor();
		JsonDireccion jsonDireccion = new JsonDireccion();
		JsonReceptor jsonReceptor = new JsonReceptor();
		List<JsonDocumentoRelacionado> documentoRelacionadoLista = new ArrayList<>();		 
		JsonCuerpoDocumento jsonCuerpoDocumento = new JsonCuerpoDocumento();
		List<JsonCuerpoDocumento> jsonCuerpoDocumentoList = new ArrayList<>();
		JsonResumen jsonResumen = new JsonResumen();
		JsonSujetoExcluido jsonSujetoExcluido = new JsonSujetoExcluido();
		JsonDocumento jsonDocumento = new JsonDocumento();
		JsonMotivo jsonMotivo = new JsonMotivo();
						
		if (tipoDocumento.equals("01"))
		{
			jsonIdentificacion.setVersion(1);
			jsonIdentificacion.setAmbiente("00");
			jsonIdentificacion.setTipoDte("01");
			jsonIdentificacion.setNumeroControl("DTE-01-00010000-000000000000026");
			jsonIdentificacion.setCodigoGeneracion("710E7AF6-1C3B-4997-B0EA-42581245ED0A");
			jsonIdentificacion.setTipoModelo(1);
			jsonIdentificacion.setTipoOperacion(1);
			jsonIdentificacion.setFecEmi("2023-05-24");
			jsonIdentificacion.setHorEmi("08:00:48");
			jsonIdentificacion.setTipoMoneda("USD");
			
			jsonDireccion.setDepartamento("03");
			jsonDireccion.setMunicipio("09");
			jsonDireccion.setComplemento("AV. OLIMPICA");
			
			jsonEmisor.setCode("BANCOAZUL");
			jsonEmisor.setCorreo("contabilidad@bancoazul.com");
			jsonEmisor.setNit("06142309131046");
			jsonEmisor.setNrc("2279991");
			jsonEmisor.setNombre("BANCO AZUL S.A.");
			jsonEmisor.setCodActividad("64190");
			jsonEmisor.setDescActividad("BANCOS");		
			jsonEmisor.setTipoEstablecimiento("01");
			jsonEmisor.setTelefono("25558100");
			jsonEmisor.setDireccion(jsonDireccion);
						
			jsonReceptor.setNit("06141302911121");
			jsonReceptor.setNrc("1119891");
			jsonReceptor.setNombre("SUPERVENTAS S.A. DE C.V.");
			jsonReceptor.setCodActividad("80100");
			jsonReceptor.setDescActividad("DESCRIPCION");
			jsonReceptor.setTelefono("22398127");
			jsonReceptor.setCorreo("cliente@mail.com");
			jsonReceptor.setNumDocumento("282559998");
			jsonReceptor.setTipoDocumento("36");			
			jsonReceptor.setDireccion(jsonDireccion);
			
			jsonCuerpoDocumento.setEtiCuerpoDocivaItem(0.00);
			jsonCuerpoDocumento.setNumItem(1);
			jsonCuerpoDocumento.setEtiCuerpoDoctipoItem(2);
			jsonCuerpoDocumento.setEtiCuerpoDocdescripcion("COMISION POR PAGO DE PLANILLA");
			jsonCuerpoDocumento.setEtiCuerpoDoccantidad(1.00);
			jsonCuerpoDocumento.setEtiCuerpoDocuniMedida(59);
			jsonCuerpoDocumento.setEtiCuerpoDocprecioUni(10.00);
			jsonCuerpoDocumento.setEtiCuerpoDocmontoDescu(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocventaNoSuj(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocventaExenta(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocventaGravada(10.00);
			jsonCuerpoDocumento.setEtiCuerpoDocpsv(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocnoGravado(0.00);
			
			jsonCuerpoDocumentoList.add(jsonCuerpoDocumento);
			
			jsonResumen.setTotalExenta(0.00);
			jsonResumen.setTotalNoSuj(0.00);
			jsonResumen.setTotalGravada(10.00);
			jsonResumen.setSubTotalVentas(10.00);
			jsonResumen.setDescuNoSuj(0.00);
			jsonResumen.setDescuExenta(0.00);
			jsonResumen.setDescuGravada(0.00);
			jsonResumen.setPorcentajeDescuento(0.00);
			jsonResumen.setTotalDescu(0.00);	
			jsonResumen.setSubTotal(11.30);
			jsonResumen.setIvaPerci1(0.00);
			jsonResumen.setIvaRete1(0.00);
			jsonResumen.setReteRenta(0.00);
			jsonResumen.setMontoTotalOperacion(11.30);
			jsonResumen.setTotalNoGravado(0.00);
			jsonResumen.setTotalPagar(11.30);
			jsonResumen.setTotalLetras("ONCE 30/100 DOLARES");
			jsonResumen.setSaldoFavor(0.00);
			jsonResumen.setCondicionOperacion(1);
			jsonResumen.setTotalIva(0.00);
		}
		else if (tipoDocumento.equals("03"))
		{
			jsonIdentificacion.setVersion(3);
			jsonReceptor.setNumDocumento("282559998");
			jsonReceptor.setTipoDocumento("36");			
			
			jsonIdentificacion.setAmbiente("00");
			jsonIdentificacion.setTipoDte(tipoDocumento);
			jsonIdentificacion.setNumeroControl("DTE-"+tipoDocumento+"-00010000-000000000000026");
			jsonIdentificacion.setCodigoGeneracion("3CCD37F5-2E29-4560-BCA3-63DE9CCF1B4F");
			jsonIdentificacion.setTipoModelo(1);
			jsonIdentificacion.setTipoOperacion(1);
			jsonIdentificacion.setFecEmi("2023-05-24");
			jsonIdentificacion.setHorEmi("08:00:48");
			jsonIdentificacion.setTipoMoneda("USD");
			
			jsonDireccion.setDepartamento("02");
			jsonDireccion.setMunicipio("07");
			jsonDireccion.setComplemento("AV. OLIMPICA");
			
			jsonEmisor.setCode("BANCOAZUL");
			jsonEmisor.setCorreo("contabilidad@bancoazul.com");
			jsonEmisor.setNit("06142309131046");
			jsonEmisor.setNrc("2279991");
			jsonEmisor.setNombre("BANCO AZUL S.A.");
			jsonEmisor.setCodActividad("64190");
			jsonEmisor.setDescActividad("BANCOS");		
			jsonEmisor.setTipoEstablecimiento("01");
			jsonEmisor.setTelefono("25558100");
			jsonEmisor.setDireccion(jsonDireccion);
			
			jsonReceptor.setNit("06141302911121");
			jsonReceptor.setNrc("1119891");
			jsonReceptor.setNombre("SUPERVENTAS S.A. DE C.V.");
			jsonReceptor.setCodActividad("80100");
			jsonReceptor.setDescActividad("DESCRIPCION");
			jsonReceptor.setTelefono("22398127");
			jsonReceptor.setCorreo("cliente@mail.com");
			jsonReceptor.setDireccion(jsonDireccion);
			
			List<String> tributos = new ArrayList<>();
			tributos.add("20");		
			
			jsonCuerpoDocumento.setNumItem(1);
			jsonCuerpoDocumento.setEtiCuerpoDoctipoItem(1);
			jsonCuerpoDocumento.setEtiCuerpoDocdescripcion("COMISION POR PAGO DE PLANILLA");
			jsonCuerpoDocumento.setEtiCuerpoDoccantidad(1.00);
			jsonCuerpoDocumento.setEtiCuerpoDocuniMedida(59);
			jsonCuerpoDocumento.setEtiCuerpoDocprecioUni(10.00);
			jsonCuerpoDocumento.setEtiCuerpoDocmontoDescu(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocventaNoSuj(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocventaExenta(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocventaGravada(10.00);
			jsonCuerpoDocumento.setEtiCuerpoDocpsv(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocnoGravado(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDoctributos(tributos);
						
			jsonCuerpoDocumentoList.add(jsonCuerpoDocumento);
			
			JsonTributos jsonTributos = new JsonTributos();
			jsonTributos.setCodigo("20");
			jsonTributos.setDescripcion("Impuesto al Valor Agregado 13%");
			jsonTributos.setValor(1.30);
			
			List<JsonTributos> jsonTributosList = new ArrayList<>();
			jsonTributosList.add(jsonTributos);
			
			jsonResumen.setTotalExenta(0.00);
			jsonResumen.setTotalNoSuj(0.00);
			jsonResumen.setTotalGravada(10.00);
			jsonResumen.setSubTotalVentas(10.00);
			jsonResumen.setDescuNoSuj(0.00);
			jsonResumen.setDescuExenta(0.00);
			jsonResumen.setDescuGravada(0.00);
			jsonResumen.setPorcentajeDescuento(0.00);
			jsonResumen.setTotalDescu(0.00);	
			jsonResumen.setSubTotal(11.30);
			jsonResumen.setIvaPerci1(0.00);
			jsonResumen.setIvaRete1(0.00);
			jsonResumen.setReteRenta(0.00);
			jsonResumen.setMontoTotalOperacion(11.30);
			jsonResumen.setTotalNoGravado(0.00);
			jsonResumen.setTotalPagar(11.30);
			jsonResumen.setTotalLetras("ONCE 30/100 DOLARES");
			jsonResumen.setSaldoFavor(0.00);
			jsonResumen.setCondicionOperacion(1);
			jsonResumen.setTotalIva(0.00);			
			jsonResumen.setTributos(jsonTributosList);
			
		}
		else if (tipoDocumento.equals("05"))
		{
			jsonIdentificacion.setVersion(3);
			jsonIdentificacion.setAmbiente("00");
			jsonIdentificacion.setTipoDte("05");
			jsonIdentificacion.setNumeroControl("DTE-05-00010000-000000000000026");
			jsonIdentificacion.setCodigoGeneracion("123D3ADA-6F9B-46DD-A002-56AE850DE7AA");
			jsonIdentificacion.setTipoModelo(1);
			jsonIdentificacion.setTipoOperacion(1);
			jsonIdentificacion.setFecEmi("2023-05-24");
			jsonIdentificacion.setHorEmi("08:00:48");
			jsonIdentificacion.setTipoMoneda("USD");
			
			jsonDireccion.setDepartamento("03");
			jsonDireccion.setMunicipio("09");
			jsonDireccion.setComplemento("AV. OLIMPICA");
			
			jsonEmisor.setCode("BANCOAZUL");
			jsonEmisor.setCorreo("contabilidad@bancoazul.com");
			jsonEmisor.setNit("06142309131046");
			jsonEmisor.setNrc("2279991");
			jsonEmisor.setNombre("BANCO AZUL S.A.");
			jsonEmisor.setCodActividad("64190");
			jsonEmisor.setDescActividad("BANCOS");		
			jsonEmisor.setTipoEstablecimiento("01");
			jsonEmisor.setTelefono("25558100");
			jsonEmisor.setDireccion(jsonDireccion);
						
			jsonReceptor.setNit("06141302911121");
			jsonReceptor.setNrc("1119891");
			jsonReceptor.setNombre("SUPERVENTAS S.A. DE C.V.");
			jsonReceptor.setCodActividad("80100");
			jsonReceptor.setDescActividad("DESCRIPCION");
			jsonReceptor.setTelefono("22398127");
			jsonReceptor.setCorreo("cliente@mail.com");
			jsonReceptor.setNumDocumento("282559998");
			jsonReceptor.setTipoDocumento("36");			
			jsonReceptor.setDireccion(jsonDireccion);
			
			JsonDocumentoRelacionado jsonDocumentoRelacionado = new JsonDocumentoRelacionado();
			jsonDocumentoRelacionado.setTipoDocumento("03");
			jsonDocumentoRelacionado.setTipoGeneracion(1);
			jsonDocumentoRelacionado.setNumeroDocumento("123D3ADA");
			jsonDocumentoRelacionado.setFechaEmision("2023-05-24");
			
			documentoRelacionadoLista.add(jsonDocumentoRelacionado);
			
			List<String> tributos = new ArrayList<>();
			tributos.add("20");		
			
			jsonCuerpoDocumento.setNumItem(1);
			jsonCuerpoDocumento.setEtiCuerpoDoctipoItem(2);
			jsonCuerpoDocumento.setEtiCuerpoDocnumeroDocumento("123D3ADA-6F9B-46DD-A002-56AE850DE7AA");
			
			jsonCuerpoDocumento.setEtiCuerpoDocdescripcion("COMISION POR PAGO DE PLANILLA");
			jsonCuerpoDocumento.setEtiCuerpoDoccantidad(1.00);
			jsonCuerpoDocumento.setEtiCuerpoDocuniMedida(59);
			jsonCuerpoDocumento.setEtiCuerpoDocprecioUni(10.00);
			jsonCuerpoDocumento.setEtiCuerpoDocmontoDescu(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocventaNoSuj(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocventaExenta(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDocventaGravada(10.00);
			jsonCuerpoDocumento.setEtiCuerpoDoctributos(tributos);
			
			jsonCuerpoDocumentoList.add(jsonCuerpoDocumento);
			
			JsonTributos jsonTributos = new JsonTributos();
			jsonTributos.setCodigo("20");
			jsonTributos.setDescripcion("Impuesto al Valor Agregado 13%");
			jsonTributos.setValor(1.30);
			
			List<JsonTributos> jsonTributosList = new ArrayList<>();
			jsonTributosList.add(jsonTributos);
			
			jsonResumen.setTotalExenta(0.00);
			jsonResumen.setTotalNoSuj(0.00);
			jsonResumen.setTotalGravada(10.00);
			jsonResumen.setSubTotalVentas(10.00);
			jsonResumen.setDescuNoSuj(0.00);
			jsonResumen.setDescuExenta(0.00);
			jsonResumen.setDescuGravada(0.00);
			jsonResumen.setPorcentajeDescuento(0.00);
			jsonResumen.setTotalDescu(0.00);	
			jsonResumen.setSubTotal(11.30);
			jsonResumen.setIvaPerci1(0.00);
			jsonResumen.setIvaRete1(0.00);
			jsonResumen.setReteRenta(0.00);
			jsonResumen.setMontoTotalOperacion(11.30);
			jsonResumen.setTotalNoGravado(0.00);
			jsonResumen.setTotalPagar(11.30);
			jsonResumen.setTotalLetras("ONCE 30/100 DOLARES");
			jsonResumen.setSaldoFavor(0.00);
			jsonResumen.setCondicionOperacion(1);
			jsonResumen.setTotalIva(0.00);
			jsonResumen.setTributos(jsonTributosList);
		}
		else if (tipoDocumento.equals("07"))
		{
			jsonIdentificacion.setVersion(1);
			jsonIdentificacion.setAmbiente("00");
			jsonIdentificacion.setTipoDte("07");
			jsonIdentificacion.setNumeroControl("DTE-07-00010000-000000000000026");
			jsonIdentificacion.setCodigoGeneracion("123D3ADA-6F9B-46DD-A002-56AE850DE7AA");
			jsonIdentificacion.setTipoModelo(1);
			jsonIdentificacion.setTipoOperacion(1);
			jsonIdentificacion.setFecEmi("2023-05-24");
			jsonIdentificacion.setHorEmi("08:00:48");
			jsonIdentificacion.setTipoMoneda("USD");
			
			jsonDireccion.setDepartamento("03");
			jsonDireccion.setMunicipio("09");
			jsonDireccion.setComplemento("AV. OLIMPICA");
			
			jsonEmisor.setCode("BANCOAZUL");
			jsonEmisor.setCorreo("contabilidad@bancoazul.com");
			jsonEmisor.setNit("06142309131046");
			jsonEmisor.setNrc("2279991");
			jsonEmisor.setNombre("BANCO AZUL S.A.");
			jsonEmisor.setCodActividad("64190");
			jsonEmisor.setDescActividad("BANCOS");		
			jsonEmisor.setTipoEstablecimiento("01");
			jsonEmisor.setTelefono("25558100");
			jsonEmisor.setDireccion(jsonDireccion);
						
			jsonReceptor.setNit("06141302911121");
			jsonReceptor.setNrc("1119891");
			jsonReceptor.setNombre("SUPERVENTAS S.A. DE C.V.");
			jsonReceptor.setCodActividad("80100");
			jsonReceptor.setDescActividad("DESCRIPCION");
			jsonReceptor.setTelefono("22398127");
			jsonReceptor.setCorreo("cliente@mail.com");
			jsonReceptor.setNumDocumento("282559998");
			jsonReceptor.setTipoDocumento("36");			
			jsonReceptor.setDireccion(jsonDireccion);
			
			jsonCuerpoDocumento.setNumItem(1);
			jsonCuerpoDocumento.setEtiCuerpoDocdescripcion("DESCRIPCION");			
			jsonCuerpoDocumento.setEtiCuerpoDocivaRetenido(20.00);
			jsonCuerpoDocumento.setEtiCuerpoDocfechaEmision("2021-05-24");
			jsonCuerpoDocumento.setEtiCuerpoDoctipoDte("03");
			jsonCuerpoDocumento.setEtiCuerpoDoctipoDoc(1);
			jsonCuerpoDocumento.setEtiCuerpoDocnumDocumento("FF54E9DB-79C3-42CEB432-EC552C97EF33");
			jsonCuerpoDocumento.setEtiCuerpoDocmontoSujetoGrav(2000.00);
			jsonCuerpoDocumento.setEtiCuerpoDoccodigoRetencionMH("22");
			
			jsonCuerpoDocumentoList.add(jsonCuerpoDocumento);
			
			jsonResumen.setTotalSujetoRetencion(2000.00);
			jsonResumen.setTotalIVAretenido(20.00);
			jsonResumen.setTotalIVAretenidoLetras("VEINTE DOLARES");
		}
		else if (tipoDocumento.equals("14"))
		{
			jsonIdentificacion.setVersion(1);
			jsonIdentificacion.setAmbiente("00");
			jsonIdentificacion.setTipoDte("14");
			jsonIdentificacion.setNumeroControl("DTE-14-00010000-000000000000026");
			jsonIdentificacion.setCodigoGeneracion("123D3ADA-6F9B-46DD-A002-56AE850DE7AA");
			jsonIdentificacion.setTipoModelo(1);
			jsonIdentificacion.setTipoOperacion(1);
			jsonIdentificacion.setFecEmi("2023-05-24");
			jsonIdentificacion.setHorEmi("08:00:48");
			jsonIdentificacion.setTipoMoneda("USD");
			
			jsonDireccion.setDepartamento("03");
			jsonDireccion.setMunicipio("09");
			jsonDireccion.setComplemento("AV. OLIMPICA");
			
			jsonEmisor.setCode("BANCOAZUL");
			jsonEmisor.setCorreo("contabilidad@bancoazul.com");
			jsonEmisor.setNit("06142309131046");
			jsonEmisor.setNrc("2279991");
			jsonEmisor.setNombre("BANCO AZUL S.A.");
			jsonEmisor.setCodActividad("64190");
			jsonEmisor.setDescActividad("BANCOS");		
			jsonEmisor.setTipoEstablecimiento("01");
			jsonEmisor.setTelefono("25558100");
			jsonEmisor.setDireccion(jsonDireccion);
			
			jsonSujetoExcluido.setTipoDocumento("36");
			jsonSujetoExcluido.setNumDocumento("00000000000000");
			jsonSujetoExcluido.setNombre("CLIENTE S.A. DE C.V.");
			jsonSujetoExcluido.setCodActividad("80100");
			jsonSujetoExcluido.setDescActividad("DESCRIPCION");	
			jsonSujetoExcluido.setDireccion(jsonDireccion);
			jsonSujetoExcluido.setTelefono("22222222");
			jsonSujetoExcluido.setCorreo("correo@gmail.com");
			
			jsonCuerpoDocumento.setNumItem(1);
			jsonCuerpoDocumento.setEtiCuerpoDoctipoItem(2);
			jsonCuerpoDocumento.setEtiCuerpoDocdescripcion("DESCRIPCION");
			jsonCuerpoDocumento.setEtiCuerpoDoccantidad(1.00);
			jsonCuerpoDocumento.setEtiCuerpoDocuniMedida(99);
			jsonCuerpoDocumento.setEtiCuerpoDocprecioUni(300.00);
			jsonCuerpoDocumento.setEtiCuerpoDocmontoDescu(0.00);
			jsonCuerpoDocumento.setEtiCuerpoDoccompra(300.00);
			
			jsonCuerpoDocumentoList.add(jsonCuerpoDocumento);
			
			jsonResumen.setTotalDescu(0.00);
			jsonResumen.setSubTotal(11.30);
			jsonResumen.setIvaRete1(0.00);
			jsonResumen.setReteRenta(0.00);
			jsonResumen.setTotalPagar(11.30);
			jsonResumen.setTotalLetras("ONCE 30/100 DOLARES");
			jsonResumen.setCondicionOperacion(1);
			jsonResumen.setTotalCompra(300.00);
			jsonResumen.setDescu(0.00);
		}
		else if (tipoDocumento.equals("00"))
		{
			jsonIdentificacion.setVersion(1);
			jsonIdentificacion.setAmbiente("00");			
			jsonIdentificacion.setCodigoGeneracion("123D3ADA-6F9B-46DD-A002-56AE850DE7AA");
			jsonIdentificacion.setFecAnula("2023-05-23");
			jsonIdentificacion.setHorAnula("13:33:35");
			
			jsonDireccion.setDepartamento("03");
			jsonDireccion.setMunicipio("09");
			jsonDireccion.setComplemento("AV. OLIMPICA");
			
			jsonEmisor.setCode("BANCOAZUL");
			jsonEmisor.setNit("06142309131046");
			jsonEmisor.setNombre("BANCO AZUL S.A.");
			jsonEmisor.setTipoEstablecimiento("01");
			jsonEmisor.setTelefono("25558100");
			
			jsonDocumento.setTipoDte("01");
			jsonDocumento.setCodigoGeneracion("123D3ADA-6F9B-46DD-A002-56AE850DE7AA");
			jsonDocumento.setSelloRecibido("SelloDeRecepcion");
			jsonDocumento.setNumeroControl("DTE-01-00010000-000000000000026");
			jsonDocumento.setFecEmi("2023-05-23");
			jsonDocumento.setMontoIva(1.30);
			jsonDocumento.setCodigoGeneracionR("E35DEF20-E485-4563-822E-6C06FA5A9801");
			jsonDocumento.setTipoDocumento("36");
			jsonDocumento.setNumDocumento("00000000000000");
			jsonDocumento.setNombre("CLIENTE S.A. DE C.V.");
			jsonDocumento.setTelefono("22222222");
			jsonDocumento.setCorreo("correo@mail.com");
			
			jsonMotivo.setTipoAnulacion(1);
			jsonMotivo.setMotivoAnulacion("Descripcion de anulacion");
			jsonMotivo.setNombreResponsable("Nombre de autorizador");
			jsonMotivo.setTipDocResponsable("36");
			jsonMotivo.setNumDocResponsable("00000000000000");
			jsonMotivo.setNombreSolicita("Nombre de solicitante");
			jsonMotivo.setTipDocSolicita("36");
			jsonMotivo.setNumDocSolicita("00000000000000");
		}
		
		jsonDatosDTE.setIdentificacion(jsonIdentificacion);
		jsonDatosDTE.setEmisor(jsonEmisor);
		jsonDatosDTE.setReceptor(jsonReceptor);
		jsonDatosDTE.setCuerpoDocumento(jsonCuerpoDocumentoList);		
		jsonDatosDTE.setResumen(jsonResumen);
		jsonDatosDTE.setSujetoExcluido(jsonSujetoExcluido);
		jsonDatosDTE.setDocumento(jsonDocumento);
		jsonDatosDTE.setMotivo(jsonMotivo);
		
		if (documentoRelacionadoLista.size() > 0 )
		{
			jsonDatosDTE.setDocumentoRelacionado(documentoRelacionadoLista);
		}
		
		DatosComplementariosDTE datosComplementariosDTE = new DatosComplementariosDTE();
		datosComplementariosDTE.setTipoDocumento(tipoDocumento);
		datosComplementariosDTE.setNumeroDeTransaccion("FT2536FRTTTX");
		datosComplementariosDTE.setCodigoCliente("252501");
		datosComplementariosDTE.setNombreCliente("JUAN PEREZ");
		
		String respuesta = EnviarDocumento(datosComplementariosDTE, jsonDatosDTE);
		//EnviarCorreoNotificacion(datosComplementariosDTE, jsonDatosDTE);
			
		return respuesta;
	}

	public String ActualizarSelloDocumento()
	{
		String strRespuesta = "";
		Integer intProcesadosConExito = 0;
		
		logger.info("Ingreso a metodo ActualizarSelloDocumento()");
		
		try
		{
			// CONSULTANDO DOCUMENTOS A ENVIAR
			// -------------------------------
			List<DteElectronico> documentos = dteElectronicoRepo.buscarPorEstadoDTE(5);
			
			if (documentos.size() > 0)
			{
				// GENERANDO TOKEN
				// ---------------
				String strToken = ObtenerToken();
				
				for(DteElectronico documentoDTE : documentos) 
				{			
					// ------------------------------------
					// CONSULTANDO SELLO DE RECIBIDO PASO 1
					// ------------------------------------
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.setBearerAuth(strToken);					
					
					String strJsonEnviar = "{\"codigoGeneracion\": \"" + documentoDTE.getCodigoGeneracion() + "\"}";
					HttpEntity<String> entity = new HttpEntity<String>(strJsonEnviar, headers);
					logger.info("JSON a enviar para consulta de sello de recibido:" + strJsonEnviar);
					
					strRespuesta = restTemplate.postForObject(pbsDireccion + "/v4/receivedstamp", entity, String.class);
					logger.info("Respuesta consulta sello de recibido: " + strRespuesta);
					
					// OBTENIENDO SELLO DE RECIBIDO
					// ----------------------------
					JsonObject jsonObjectSello = new Gson().fromJson(strRespuesta, JsonObject.class);
					String strSelloRecibido = jsonObjectSello.getAsJsonObject("responseMH").get("selloRecibido").toString().replace("\"","");
					
					// ------------------------------------
					// CONSULTANDO DOCUMENTO BASE 64 PASO 2
					// ------------------------------------
					HttpHeaders headerDocumento = new HttpHeaders();
					headerDocumento.setContentType(MediaType.APPLICATION_JSON);
					headerDocumento.setBearerAuth(strToken);					
					
					String strJsonDocConsultar = "{\"codigoGeneracion\": \"" + documentoDTE.getCodigoGeneracion() + "\"}";
					HttpEntity<String> entDocumento = new HttpEntity<String>(strJsonDocConsultar, headerDocumento);
					logger.info("JSON a enviar para consulta de documento base 64:" + strJsonDocConsultar);
					
					strRespuesta = restTemplate.postForObject(pbsDireccion + "/v1/getpdf", entDocumento, String.class);
					logger.info("Respuesta consulta de documento base 64: " + strRespuesta);
					
					// OBTENIENDO DOCUMENTO BASE 64
					// ----------------------------
					JsonObject jsonObjectDocumento = new Gson().fromJson(strRespuesta, JsonObject.class);
					String strDocumentoBase64 = jsonObjectDocumento.get("pdfBase64").toString().replace("\"","");
					
					if (strSelloRecibido.equals("null") || strDocumentoBase64.equals("null"))
					{
						logger.info("Se encontraron valores nulos por lo que no se actualiza documento" );
					}
					else
					{
						//documentoDTE.setSelloRecibido(strSelloRecibido);
						documentoDTE.setDocumentoBase64(strDocumentoBase64);
						
						// ENVIANDO DOCUMENTO A DOCUWARE
						String fechaEmision = documentoDTE.getFechaEmi() + "        ";
						fechaEmision = (fechaEmision.substring(0,4) + "-" + fechaEmision.substring(4,6) + "-" + fechaEmision.substring(6,8)).trim();
						Integer intEstado = docuWareServiceImp.EnviarDocumento(documentoDTE.getCodigoGeneracion(),documentoDTE.getReferenciaT24() + ".pdf", documentoDTE.getTipoDte(), documentoDTE.getReferenciaT24(),documentoDTE.getNumeroControl(), documentoDTE.getCuenta(), documentoDTE.getNiuCliente(), fechaEmision, documentoDTE.getDocumentoBase64());

						documentoDTE.setEstadoDTE(intEstado);
						dteElectronicoRepo.save(documentoDTE);
						
						intProcesadosConExito++;
					}
				}
				
				strRespuesta = "Se finaliza proceso de actualizacion para sello de recibido y documentos base 64. Total de documentos procesados: " + documentos.size() + ", Total de documentos actualizados: " + intProcesadosConExito.toString();
				logger.info(strRespuesta);
			}
			else 
			{
				strRespuesta = "No hay documentos pendientes de actualizar";
				logger.info(strRespuesta);
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			logger.error(ex.getMessage().toString());
		}
		
		return strRespuesta;		
	}
}
