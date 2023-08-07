package sv.com.dte.service.impl;

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
import sv.com.dte.model.DteElectronico;
import sv.com.dte.repo.IDteElectronicoRepo;

@Service
public class DocuWareServiceImp 
{
	private static final Logger logger = LoggerFactory.getLogger(DTEServiceImpl.class);
	
	@Autowired
	private IDteElectronicoRepo dteElectronicoRepo;
	
	@Value("${docuware.usuario}")
	private String docuwareUsuario;
	
	@Value("${docuware.clave}")
	private String docuwareClave;
	
	@Value("${docuware.direccion}")
	private String docuWareDireccion;
	
	@Value("${docuware.archivador}")
	private String docuwareArchivador;
	
	@Autowired
	private RestTemplate restTemplate;
			
	public Integer EnviarDocumento(String strCodigoGeneracion,String strNombreArchivo, String strCodigoDocumento, String strReferencia, String strNumeroControl, String strCuenta, String strCliente, String strFecha, String strDocumentoBase64) 
	{		
		logger.info("Se ingresa a metodo EnviarDocumento()");
		
		// ACTUALIZANDO ESTADO 9 = PENDIENTE DOCUWARE
		Integer intEstado = 9;
				
		try
		{
			// CADENA CON DATOS A ENVIAR
			// -------------------------
			String strDatos = 
					"{"
					+ "\"idArchivador\":\"@{IdArchivador}\","
					+ "\"nombreArchivo\":\"@{NombreArchivo}\","
					+ "\"indices\": ["
					+ "		{"
					+ "			\"index\":\"CODIGO_DE_DOCUMENTO\","
					+ "			\"value\":\"@{CodigoDocumento}\""
					+ "		},"
					+ "		{"
					+ "			\"index\":\"REFERENCIA\","
					+ "			\"value\":\"@{Referencia}\""
					+ "		},"
					+ "		{"
					+ "			\"index\":\"NUMERO_DE_CONTROL\","
					+ "			\"value\":\"@{NumeroControl}\""
					+ "		},"
					+ "		{"
					+ "			\"index\":\"CUENTA\","
					+ "			\"value\":\"@{Cuenta}\""
					+ "		},"
					+ "		{"
					+ "			\"index\":\"CLIENTE\","
					+ "			\"value\":\"@{Cliente}\""
					+ "		},"
					+ "		{"
					+ "			\"index\":\"FECHA\","
					+ "			\"value\":\"@{Fecha}\""
					+ "		}		"
					+ "	],"
					+ "\"documentoBase64\":\"@{DocumentoBase64}\""
					+ "}";
			
			strCuenta = (strCuenta == null) ? "" : strCuenta;
			strCliente = (strCliente == null) ? "" : strCliente;
			
			strDatos = strDatos.replace("@{IdArchivador}", docuwareArchivador);
			strDatos = strDatos.replace("@{NombreArchivo}", strNombreArchivo);
			strDatos = strDatos.replace("@{CodigoDocumento}", CodigoTipoDocumento(strCodigoDocumento));
			strDatos = strDatos.replace("@{Referencia}", strReferencia);
			strDatos = strDatos.replace("@{NumeroControl}", strNumeroControl);
			strDatos = strDatos.replace("@{Cuenta}", strCuenta);
			strDatos = strDatos.replace("@{Cliente}", strCliente);
			strDatos = strDatos.replace("@{Fecha}", strFecha);
			strDatos = strDatos.replace("@{DocumentoBase64}", strDocumentoBase64);
			
			logger.info("Datos a enviar a DocuWare: " + strDatos);
			
			// ENVIANDO DOCUMENTO
			// ------------------
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBasicAuth(docuwareUsuario, docuwareClave, null);
			
			HttpEntity<String> entity = new HttpEntity<String>(strDatos, headers);
		
			String response = restTemplate.postForObject(docuWareDireccion + "/IndexarDocumento", entity, String.class);
			logger.info("Respuesta de envio documento a DocuWare: " + response);
			
			// EVALUANDO SI DOCUWARE RECIBIO DEL DOCUMENTO
			if (response.indexOf("mensaje\\\":\\\"OK\\\"}\"") >= 0)
			{
				// ACTUALIZANDO ESTADO 8 = ENVIADO A DOCUWARE
				intEstado = 8;
			}
		}		
		catch (Exception ex) 
		{
			logger.error("Error al enviar documento a DocuWare: " + ex.toString());
			ex.printStackTrace();
		}
		
		return intEstado;
	}
	
	public String CodigoTipoDocumento(String tipoDocumento)
	{
		String strTipoDocumento = tipoDocumento;
		
		if (strTipoDocumento.equals("01"))
		{
			strTipoDocumento = "FE001";
		}
		else if (strTipoDocumento.equals("03"))
		{
			strTipoDocumento = "FE002";
		}
		else if (strTipoDocumento.equals("05"))
		{
			strTipoDocumento = "FE003";
		}
		else if (strTipoDocumento.equals("14"))
		{
			strTipoDocumento = "FE004";
		}
		else if (strTipoDocumento.equals("07"))
		{
			strTipoDocumento = "FE005";
		}
		else
		{
			strTipoDocumento = "FEXXX";
		}
		
		return strTipoDocumento;		
	}
	
	public String ReprocesarEnvioDocumentos()
	{
		String strRespuesta = "";
		Integer intProcesadosConExito = 0;
		
		logger.info("Ingreso a metodo ReprocesarEnvioDocumentos()");
		
		try
		{
			// CONSULTANDO DOCUMENTOS A ENVIAR
			// -------------------------------
			List<DteElectronico> documentos = dteElectronicoRepo.buscarPorEstadoDTE(9);
			
			if (documentos.size() > 0)
			{
				for(DteElectronico documentoDTE : documentos) 
				{
					// ENVIANDO DOCUMENTO A DOCUWARE
					String fechaEmision = documentoDTE.getFechaEmi() + "        ";
					fechaEmision = (fechaEmision.substring(0,4) + "-" + fechaEmision.substring(4,6) + "-" + fechaEmision.substring(6,8)).trim();
					Integer intEstado = EnviarDocumento(documentoDTE.getCodigoGeneracion(),documentoDTE.getReferenciaT24() + ".pdf", documentoDTE.getTipoDte(), documentoDTE.getReferenciaT24(),documentoDTE.getNumeroControl(), documentoDTE.getCuenta(), documentoDTE.getNiuCliente(), fechaEmision, documentoDTE.getDocumentoBase64());

					documentoDTE.setEstadoDTE(intEstado);
					dteElectronicoRepo.save(documentoDTE);
					
					if (intEstado == 8)
					{
						intProcesadosConExito++;
					}
				}
				
				strRespuesta = "Se finaliza proceso de reenvio de documentos a DocuWare, Total de documentos enviados: " + documentos.size() + ", Total de documentos cargados: " + intProcesadosConExito.toString();
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
			ex.printStackTrace();
			logger.error(ex.getMessage().toString());
		}
		
		return strRespuesta;		
	}
}
