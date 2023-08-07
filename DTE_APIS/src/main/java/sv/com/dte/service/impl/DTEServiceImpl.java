package sv.com.dte.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sv.com.dte.model.CatalogoActividadEco;
import sv.com.dte.model.CatalogoDepartamento;
import sv.com.dte.model.CatalogoMunicipio;
import sv.com.dte.model.CatalogoTributos;
import sv.com.dte.model.CuerpoDte;
import sv.com.dte.model.DatosComplementariosDTE;
import sv.com.dte.model.DocumentosRelacionadosDte;
import sv.com.dte.model.DteAnulacionRequestDTE;
import sv.com.dte.model.DteCoreRequest;
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
import sv.com.dte.model.ReceptorDte;
import sv.com.dte.model.ResumenDte;
import sv.com.dte.model.TributosDte;
import sv.com.dte.repo.ICatalogoActividadEco;
import sv.com.dte.repo.ICatalogoDepartamento;
import sv.com.dte.repo.ICatalogoMunicipio;
import sv.com.dte.repo.ICatalogoTributos;
import sv.com.dte.repo.ICuerpoDte;
import sv.com.dte.repo.IDocumentosRelacionadosDte;
import sv.com.dte.repo.IDteElectronicoRepo;
import sv.com.dte.repo.IReceptorDte;
import sv.com.dte.repo.IResumenDte;
import sv.com.dte.repo.ITributosDte;
import sv.com.dte.service.IDteElectronicoService;
import sv.com.dte.util.EstadosDTE;
import sv.com.dte.util.Generador;
import sv.com.dte.util.TipoAnulacion;
import sv.com.dte.util.TipoDTE;
import sv.com.dte.util.TipoModeloDTE;
import sv.com.dte.util.TipoTransmisionDTE;
import sv.com.dte.util.TributosCore;
import sv.com.dte.util.VersionDTE;
import com.google.gson.Gson;

@Service
public class DTEServiceImpl implements IDteElectronicoService {
	@Autowired
	private IDteElectronicoRepo repoDteElectronico;
	@Autowired
	private IReceptorDte repoReceptorDte;
	@Autowired
	private ICuerpoDte repoCuerpoDte;
	@Autowired
	private IResumenDte repoResumenDte;
	@Autowired
	private ITributosDte repoTributosDte;
	@Autowired
	private ICatalogoTributos repoCatalogoTributos;
	@Autowired
	private ICatalogoActividadEco repoActividadEconomica;
	@Autowired
	private ICatalogoDepartamento repoDeparmento;
	@Autowired
	private ICatalogoMunicipio repoMunicipio;
	@Autowired
	private IDocumentosRelacionadosDte repoDocumentosRelacionados;
	@Autowired
	private ServicioPBSImp servicioPBS;
	
	@Autowired
	private Generador generador;
	
	@Value("${Ambiente}")
	private String ambienteDte;
	@Value("${CodeEmisor}")
	private String codeEmisor;
	@Value("${NitEmisor}")
	private String nitEmisor;
	@Value("${NrcEmisor}")
	private String nrcEmisor;
	@Value("${NombreEmisor}")
	private String nombreEmisor;
	@Value("${TelefonoEmisor}")
	private String telefonoEmisor;
	
	private static final Logger logger = LoggerFactory.getLogger(DTEServiceImpl.class);
	
	@Override
	@Transactional
	public void RegistrarDte(DteCoreRequest docElectronico) throws Exception {
		//Creacion de Entidades
		DteElectronico documentoElectronico =new DteElectronico();
		ReceptorDte receptorDocumentoElectronico= new ReceptorDte();
		CuerpoDte cuerpoDocumentoElectronico = new CuerpoDte();
		ResumenDte resumenDocumentoElectronico= new ResumenDte();
		TributosDte tributosDocumentoElectronico= new TributosDte();
		
		Gson gson = new Gson();
		String json_core = gson.toJson(docElectronico);
		
		logger.info("DTE Recibido de Core para su Registro: \n " + json_core);
		
		try
		{
			
			BigDecimal MontoIva= docElectronico.getEsExento().equals("SI")?new BigDecimal(0): docElectronico.getValorDeTributo().equals("null")?new BigDecimal(0): new BigDecimal(docElectronico.getValorDeTributo());
			BigDecimal MontoSinIVA= new BigDecimal(docElectronico.getMonto());
			BigDecimal MontoTotal= MontoSinIVA.add(MontoIva);
			
			//Setamos datos de nodo Identificacion
			documentoElectronico.setVersion(docElectronico.getTipoDTE().equals(TipoDTE.FACTURA)?VersionDTE.VERSION_INICIAL:VersionDTE.VERSION_MODIFICADA);
			documentoElectronico.setAmbiente(ambienteDte);
			documentoElectronico.setTipoDte(docElectronico.getTipoDTE().equals(TipoDTE.FACTURA)?TipoDTE.FACTURA:TipoDTE.CREDITO_FISCAL);
			documentoElectronico.setNumeroControl(generador.crearNumeroControl(docElectronico.getTipoDTE(), String.format("%08d",Integer.parseInt(docElectronico.getCodAgencia().substring(2))),docElectronico.getFechaGeneracion()));
			documentoElectronico.setCodigoGeneracion(generador.generateNonRepeatableUUID());
			documentoElectronico.setTipoModelo(TipoModeloDTE.PREVIO);
			documentoElectronico.setTipoOperacion(TipoTransmisionDTE.NORMAL);
			documentoElectronico.setFechaEmi(docElectronico.getFechaGeneracion());
			documentoElectronico.setHoraEmi(docElectronico.getHoraGeneracion());
			documentoElectronico.setTipoMoneda(docElectronico.getTipoMoneda());
			documentoElectronico.setReferenciaT24(docElectronico.getReferencia());
			documentoElectronico.setNiuCliente(docElectronico.getNiuClienteReceptor());
			documentoElectronico.setEstadoDTE(EstadosDTE.REGISTRADO_API);
			documentoElectronico.setCodAgencia(docElectronico.getCodAgencia());
			documentoElectronico.setCuenta(docElectronico.getCuentaCargo().equals("null")?null:docElectronico.getCuentaCargo());
			documentoElectronico.setTipoServicio(docElectronico.getTipoServicio().equals("null")?null:docElectronico.getTipoServicio());
			//Setamos data del nodo receptor
			
			receptorDocumentoElectronico.setNumeroDocumento(
					documentoElectronico.getTipoDte().equals(TipoDTE.FACTURA)?docElectronico.getNitReceptor():null);
			// Segun catalogo-016 , Por el momento se esta dejando NIT
			receptorDocumentoElectronico.setTipoDocumento(
					documentoElectronico.getTipoDte().equals(TipoDTE.FACTURA)?"36":null);
			receptorDocumentoElectronico.setNitReceptor(
					documentoElectronico.getTipoDte().equals(TipoDTE.CREDITO_FISCAL)?docElectronico.getNitReceptor():null);
			
			receptorDocumentoElectronico.setNrcReceptor(docElectronico.getNrcReceptor().equals("null")?null:docElectronico.getNrcReceptor());
			receptorDocumentoElectronico.setNombreReceptor(docElectronico.getNombreORazonSocial());
			//Se obtiene la actividad economica
			CatalogoActividadEco catalogo= repoActividadEconomica.GetActividadEconomica(docElectronico.getCodActividadEconomRec());
			receptorDocumentoElectronico.setCodActividad(catalogo.getCodigo());
			receptorDocumentoElectronico.setDescActividad(catalogo.getDescripcion());
			receptorDocumentoElectronico.setDepartamento(docElectronico.getDireccionDeptoRec());
			receptorDocumentoElectronico.setMunicipio(docElectronico.getDireccionMunicipioRec());
			receptorDocumentoElectronico.setComplemento(docElectronico.getDireccionComplementoRec());
			receptorDocumentoElectronico.setCorreo(docElectronico.getCorreoRec());
			
			//Datos de Cuerpo
			//En caso de CCF y FCF solo sera 1 item
			cuerpoDocumentoElectronico.setNumItem(1);
			//Tipo item servicios catalogos-011, Tipo 2 servios 
			cuerpoDocumentoElectronico.setTipoItem( Integer.valueOf(docElectronico.getTipoItem()));
			//Cantidad sera solo 1 para CCF y FCF
			cuerpoDocumentoElectronico.setCantidad(new BigDecimal(1));
			// 59 por ser referente a catalogo-014 unidad de medida unidad
			cuerpoDocumentoElectronico.setUniMedida(59);
			cuerpoDocumentoElectronico.setDescripcion(docElectronico.getDescripcionParaDTE());
			cuerpoDocumentoElectronico.setPrecioUni(
					documentoElectronico.getTipoDte().equals(TipoDTE.CREDITO_FISCAL)?MontoSinIVA:MontoTotal);
			cuerpoDocumentoElectronico.setMontoDescu(new BigDecimal(0));
			cuerpoDocumentoElectronico.setVentaNoSuj(new BigDecimal(0));
			cuerpoDocumentoElectronico.setVentaExenta(
					docElectronico.getEsExento().equals("NO")?new BigDecimal(0):
					documentoElectronico.getTipoDte().equals(TipoDTE.CREDITO_FISCAL)?MontoSinIVA:MontoTotal);
			cuerpoDocumentoElectronico.setVentaGravada(
					docElectronico.getEsExento().equals("SI")?new BigDecimal(0):
					documentoElectronico.getTipoDte().equals(TipoDTE.CREDITO_FISCAL)?MontoSinIVA:MontoTotal);
			
			
			// Datos de Resumen
			resumenDocumentoElectronico.setTotalNoSuj(new BigDecimal(0));
			resumenDocumentoElectronico.setTotalExenta(
					docElectronico.getEsExento().equals("NO")?new BigDecimal(0):
					documentoElectronico.getTipoDte().equals(TipoDTE.CREDITO_FISCAL)?MontoSinIVA:MontoTotal);
			resumenDocumentoElectronico.setTotalGravada (
					docElectronico.getEsExento().equals("SI")?new BigDecimal(0):
					documentoElectronico.getTipoDte().equals(TipoDTE.CREDITO_FISCAL)?MontoSinIVA:MontoTotal);
			resumenDocumentoElectronico.setSubTotalVentas(
					documentoElectronico.getTipoDte().equals(TipoDTE.CREDITO_FISCAL)?MontoSinIVA:MontoTotal);
			
			resumenDocumentoElectronico.setDescNoSuj(new BigDecimal(0));
			resumenDocumentoElectronico.setDescuExenta(new BigDecimal(0));
			resumenDocumentoElectronico.setDescGravada(new BigDecimal(0));
			resumenDocumentoElectronico.setPorcentajeDescuento(new BigDecimal(0));
			resumenDocumentoElectronico.setTotalDescu(new BigDecimal(0));
			
			resumenDocumentoElectronico.setSubTotal(
					documentoElectronico.getTipoDte().equals(TipoDTE.CREDITO_FISCAL)?MontoSinIVA:MontoTotal);
			
			resumenDocumentoElectronico.setIvaPerci1(new BigDecimal(0));
			resumenDocumentoElectronico.setIvaRete1(new BigDecimal(0));
			resumenDocumentoElectronico.setReteRenta(new BigDecimal(0));
			
			resumenDocumentoElectronico.setMontoTotalOperacion(MontoTotal);
			resumenDocumentoElectronico.setTotalNoGravado(new BigDecimal(0));
			resumenDocumentoElectronico.setTotalPagar(MontoTotal);
			resumenDocumentoElectronico.setTotalLetras(docElectronico.getMontoLetras());
			//Condicion Operacion catalogo-016; 1-Contado
			resumenDocumentoElectronico.setCondicionOperacion(1);		
			
			if(docElectronico.getTipoDTE().equals(TipoDTE.CREDITO_FISCAL))
			{
				if(docElectronico.getEsExento().equals("NO"))
				{
					CatalogoTributos tributos;
					if(docElectronico.getCodigoTributo().equals(TributosCore.IVA))
					{
						
						tributos=repoCatalogoTributos.buscarPorCodigo("20");
						tributosDocumentoElectronico.setCodigo(tributos.getCodigo());
						tributosDocumentoElectronico.setDescripcion(tributos.getDescripcion());
						tributosDocumentoElectronico.setValor(MontoIva);
					}
				}
				
			}
			//Guardando las entidades
			repoDteElectronico.save(documentoElectronico);
			receptorDocumentoElectronico.setDteElectronico(documentoElectronico);
			resumenDocumentoElectronico.setDteElectronico(documentoElectronico);
			cuerpoDocumentoElectronico.setDteElectronico(documentoElectronico);
			cuerpoDocumentoElectronico.setResumenDte(resumenDocumentoElectronico);
			//Guardamos las demas entidades
			repoReceptorDte.save(receptorDocumentoElectronico);
			repoResumenDte.save(resumenDocumentoElectronico);
			//Seteamos el cuerpo
			repoCuerpoDte.save(cuerpoDocumentoElectronico);
			if(docElectronico.getTipoDTE().equals(TipoDTE.CREDITO_FISCAL) && docElectronico.getEsExento().equals("NO"))
				{
					tributosDocumentoElectronico.setResumenDte(resumenDocumentoElectronico);
					repoTributosDte.save(tributosDocumentoElectronico);
				}
			
			logger.info("DTE almacenado Correctamente en BD: " + documentoElectronico.getCodigoGeneracion());
			
			
			
			CatalogoDepartamento departamento=repoDeparmento.buscarPorNombre(docElectronico.getDireccionDeptoEmi());
			CatalogoMunicipio municipio=repoMunicipio.buscarPorNombre(docElectronico.getDireccionMunicipioEmi().toUpperCase(), departamento.getCodigo());
			
			docElectronico.setDireccionDeptoEmi(departamento.getCodigo());
			
			docElectronico.setDireccionMunicipioEmi(municipio.getCodigo());
			
					if (documentoElectronico.getTipoDte().equals(TipoDTE.FACTURA))
						EnvioFactura(documentoElectronico,receptorDocumentoElectronico,cuerpoDocumentoElectronico,resumenDocumentoElectronico,docElectronico);
					else
						EnvioCCF(documentoElectronico,receptorDocumentoElectronico,cuerpoDocumentoElectronico,resumenDocumentoElectronico,tributosDocumentoElectronico,docElectronico);
			
		}
		
		catch (Exception ex) {
			
			logger.error("Error en la Creacion del DteElectronico : " + ex.getMessage().toString());
			throw ex;
		}
		
	}

	@Override
	@Transactional
	public void AnulacionDte(DteAnulacionRequestDTE documentoAnulacion) throws Exception {
		
		
		Gson gson = new Gson();
		String json_core = gson.toJson(documentoAnulacion);
		
		logger.info("Json recibido para Anulacion: " + json_core);
		
		try
		{
			
			if (documentoAnulacion.getTipoDTEAnulacion().equals(TipoDTE.CREDITO_FISCAL))
			{
				//Generacion de Nota de Credito
				DteElectronico documentoElectronico =new DteElectronico();
				ReceptorDte receptorDocumentoElectronico= new ReceptorDte();
				CuerpoDte cuerpoDocumentoElectronico = new CuerpoDte();
				ResumenDte resumenDocumentoElectronico= new ResumenDte();
				TributosDte tributosDocumentoElectronico= new TributosDte();
				DocumentosRelacionadosDte documentoRelacionado= new DocumentosRelacionadosDte();
				logger.info(" Json Recibido de Core para creacion de Nota de Credito: " + json_core);
				
				//Documento relacionado del cual se otendran los datos de receptor.
				DteElectronico documentoInvalidar=repoDteElectronico.buscarPorReferencia(documentoAnulacion.getReferencia());
				
				//Nodo Identificacion y creacion de DteElectronico
				documentoElectronico.setVersion(VersionDTE.VERSION_MODIFICADA);
				documentoElectronico.setAmbiente(ambienteDte);
				documentoElectronico.setTipoDte(TipoDTE.NOTA_CREDITO);
				documentoElectronico.setNumeroControl(generador.crearNumeroControl(TipoDTE.NOTA_CREDITO, String.format("%08d",Integer.parseInt(documentoAnulacion.getCodAgencia().substring(2))),documentoAnulacion.getFechaAnulacion()));
				documentoElectronico.setCodigoGeneracion(generador.generateNonRepeatableUUID());
				documentoElectronico.setTipoModelo(TipoModeloDTE.PREVIO);
				documentoElectronico.setTipoOperacion(TipoTransmisionDTE.NORMAL);
				documentoElectronico.setFechaEmi(documentoAnulacion.getFechaAnulacion());
				documentoElectronico.setHoraEmi(documentoAnulacion.getHoraAnulacion());
				documentoElectronico.setTipoMoneda(documentoInvalidar.getTipoMoneda());
				//Referencia Reemplazo es la referencia de dela Nota de Credito
				documentoElectronico.setReferenciaT24(documentoAnulacion.getReferenciaReemplazo());
				documentoElectronico.setNiuCliente(documentoAnulacion.getNiuClienteReceptor());
				documentoElectronico.setEstadoDTE(EstadosDTE.REGISTRADO_API);
				documentoElectronico.setCodAgencia(documentoAnulacion.getCodAgencia());
				documentoElectronico.setDteElectronicoRef(documentoInvalidar.getIdDteElectronico());
				
				// Guardamos la nota de credito
				repoDteElectronico.save(documentoElectronico);
				
				//Creamos el documento relacionado
				documentoRelacionado.setNumeroDocumento(documentoInvalidar.getCodigoGeneracion());
				documentoRelacionado.setFechaEmision(documentoInvalidar.getFechaEmi());
				documentoRelacionado.setTipoDocumento(documentoInvalidar.getTipoDte());
				documentoRelacionado.setDteElectronico(documentoInvalidar);
				//Segun CAT-007- 1-Fisico 2-Electronico
				documentoRelacionado.setTipoGeneracion(2);
				//Guardamos el documento relacionado
				repoDocumentosRelacionados.save(documentoRelacionado);
				receptorDocumentoElectronico.setNitReceptor(documentoInvalidar.getReceptorDte().getNitReceptor());
				receptorDocumentoElectronico.setNrcReceptor(documentoInvalidar.getReceptorDte().getNrcReceptor());
				receptorDocumentoElectronico.setNombreReceptor(documentoInvalidar.getReceptorDte().getNombreReceptor());
				receptorDocumentoElectronico.setCodActividad(documentoInvalidar.getReceptorDte().getCodActividad());
				receptorDocumentoElectronico.setDescActividad(documentoInvalidar.getReceptorDte().getDescActividad());
				receptorDocumentoElectronico.setDepartamento(documentoInvalidar.getReceptorDte().getDepartamento());
				receptorDocumentoElectronico.setMunicipio(documentoInvalidar.getReceptorDte().getMunicipio());
				receptorDocumentoElectronico.setComplemento(documentoInvalidar.getReceptorDte().getComplemento());;
				receptorDocumentoElectronico.setCorreo(documentoInvalidar.getReceptorDte().getCorreo());
				receptorDocumentoElectronico.setDteElectronico(documentoElectronico);
				//Guardamos la data del Receptor
				repoReceptorDte.save(receptorDocumentoElectronico);
				
				
				BigDecimal MontoIva= new BigDecimal(documentoAnulacion.getValorDeTributo());
				BigDecimal MontoSinIVA= new BigDecimal(documentoAnulacion.getMonto());
				BigDecimal MontoTotal= MontoSinIVA.add(MontoIva);
				
				cuerpoDocumentoElectronico.setNumItem(1);
				cuerpoDocumentoElectronico.setTipoItem(2);
				cuerpoDocumentoElectronico.setNumeroDocumento(documentoInvalidar.getCodigoGeneracion());
				cuerpoDocumentoElectronico.setDescripcion(documentoAnulacion.getDescripcionAnulacion());
				cuerpoDocumentoElectronico.setCantidad(new BigDecimal(1));
				cuerpoDocumentoElectronico.setUniMedida(59);
				cuerpoDocumentoElectronico.setPrecioUni(MontoSinIVA);
				cuerpoDocumentoElectronico.setMontoDescu(new BigDecimal(0));
				cuerpoDocumentoElectronico.setVentaNoSuj(new BigDecimal(0));
				cuerpoDocumentoElectronico.setVentaExenta(new BigDecimal(0));
				cuerpoDocumentoElectronico.setVentaGravada(MontoSinIVA);
				
				// Datos de Resumen
				resumenDocumentoElectronico.setTotalNoSuj(new BigDecimal(0));
				resumenDocumentoElectronico.setTotalExenta(new BigDecimal(0));
				resumenDocumentoElectronico.setTotalGravada (MontoSinIVA);
				resumenDocumentoElectronico.setSubTotalVentas(MontoSinIVA);
				
				resumenDocumentoElectronico.setDescNoSuj(new BigDecimal(0));
				resumenDocumentoElectronico.setDescuExenta(new BigDecimal(0));
				resumenDocumentoElectronico.setDescGravada(new BigDecimal(0));
				resumenDocumentoElectronico.setPorcentajeDescuento(new BigDecimal(0));
				resumenDocumentoElectronico.setTotalDescu(new BigDecimal(0));
				
				resumenDocumentoElectronico.setSubTotal(MontoSinIVA);
				
				resumenDocumentoElectronico.setIvaPerci1(new BigDecimal(0));
				resumenDocumentoElectronico.setIvaRete1(new BigDecimal(0));
				resumenDocumentoElectronico.setReteRenta(new BigDecimal(0));
				
				
				resumenDocumentoElectronico.setMontoTotalOperacion(MontoTotal);
				resumenDocumentoElectronico.setTotalNoGravado(new BigDecimal(0));
				resumenDocumentoElectronico.setTotalPagar(MontoTotal);
				resumenDocumentoElectronico.setTotalLetras("MONTO LETRAS 00/100 DOLARES");
				//Condicion Operacion catalogo-016; 1-Contado
				resumenDocumentoElectronico.setCondicionOperacion(1);
				
				//Datos de Tributos
				CatalogoTributos tributos=repoCatalogoTributos.buscarPorCodigo("20");
				tributosDocumentoElectronico.setCodigo(tributos.getCodigo());
				tributosDocumentoElectronico.setDescripcion(tributos.getDescripcion());
				tributosDocumentoElectronico.setValor(new BigDecimal(documentoAnulacion.getValorDeTributo()));
				
				//Guardando las entidades de resumen, cuerpo y  tributos
				resumenDocumentoElectronico.setDteElectronico(documentoElectronico);
				repoResumenDte.save(resumenDocumentoElectronico);
				cuerpoDocumentoElectronico.setDteElectronico(documentoElectronico);
				cuerpoDocumentoElectronico.setResumenDte(resumenDocumentoElectronico);
				repoCuerpoDte.save(cuerpoDocumentoElectronico);
				tributosDocumentoElectronico.setResumenDte(resumenDocumentoElectronico);
				repoTributosDte.save(tributosDocumentoElectronico);
				
				//Llamando a metodo de envio a PBS
				
				EnvioNotaCredito(documentoElectronico,receptorDocumentoElectronico,cuerpoDocumentoElectronico,
						resumenDocumentoElectronico,tributosDocumentoElectronico,documentoRelacionado,documentoAnulacion);
				
			}
			
			else
			{
			
				//En caso evento de invalidacion sea resindir de operacion
				if ( documentoAnulacion.getTipoMotivoAnulacion().equals(TipoAnulacion.RESINDIR) )
				{
					logger.info("Anulacion Tipo 2 resindir de Operacion");
					
					//Se busca el documento que reemplaza y al documento a anular
					
					DteElectronico documentoInvalidar=repoDteElectronico.buscarPorReferencia(documentoAnulacion.getReferencia());
					
					logger.info("Documento a Invalidar: " + documentoInvalidar.getCodigoGeneracion());
					//Se crea la referencia del documento a Anular
					DteElectronico documentoInvalidado =new DteElectronico();
					//Seteando data del documento original.
					documentoInvalidado.setAmbiente(documentoInvalidar.getAmbiente());
					documentoInvalidado.setVersion(VersionDTE.VERSION_ANULACION);
					documentoInvalidado.setTipoDte(documentoInvalidar.getTipoDte());
					documentoInvalidado.setNumeroControl(documentoInvalidar.getNumeroControl());
					documentoInvalidado.setCodigoGeneracion(documentoInvalidar.getCodigoGeneracion());
					documentoInvalidado.setTipoModelo(documentoInvalidar.getTipoModelo());
					documentoInvalidado.setTipoOperacion(documentoInvalidar.getTipoModelo());
					documentoInvalidado.setFechaEmi(documentoInvalidar.getFechaEmi());
					documentoInvalidado.setHoraEmi(documentoInvalidar.getHoraEmi());
					documentoInvalidado.setTipoMoneda(documentoInvalidar.getTipoMoneda());
					documentoInvalidado.setReferenciaT24(documentoAnulacion.getReferenciaReemplazo());
					documentoInvalidado.setNiuCliente(documentoAnulacion.getNiuClienteReceptor());
					documentoInvalidado.setCodAgencia(documentoAnulacion.getCodAgencia());
					
					
					//Datos a llenar al anular el registro
					documentoInvalidado.setCodigoAnulacion(generador.generateNonRepeatableUUID());
					documentoInvalidado.setFechaAnulacion(documentoAnulacion.getFechaAnulacion());
					documentoInvalidado.setHoraAnulacion(documentoAnulacion.getHoraAnulacion());
					documentoInvalidado.setDteElectronicoRef(documentoInvalidar.getIdDteElectronico());
					documentoInvalidado.setTipoAnulacion(TipoAnulacion.RESINDIR);
					documentoInvalidado.setMotivoAnulacion(documentoAnulacion.getDescripcionAnulacion());
					documentoInvalidado.setNomSoliAnulacion(documentoAnulacion.getNombreSolicita());
					//Se setea dui de momento con data dummy
					documentoInvalidado.setTipoDocSoliAnulacion("36");
					documentoInvalidado.setNumDocSoliAnulacion("05113264-8");
					documentoInvalidado.setNomRespAnulacion(documentoAnulacion.getNombreResponsable());
					documentoInvalidado.setTipoDocRespAnulacion("36");
					documentoInvalidado.setNumDocRespAnulacion("05113264-8");
					
					//Actualizamos el estado de documento a anular a anulaod en proceso
					documentoInvalidado.setEstadoDTE(EstadosDTE.REGISTRADO_ANULACION);
					
					repoDteElectronico.save(documentoInvalidado);
					
					ReceptorDte receptor= documentoInvalidar.getReceptorDte();
					
					logger.info("Envio Anulacion PBS------------>");
					
					EnvioAnulacion(documentoInvalidado,documentoInvalidar,receptor,documentoAnulacion);
					
					
				}
				
				else
				{
					//Se busca el documento que reemplaza y al documento a anular
					DteElectronico documentoReemplaza=repoDteElectronico.buscarPorReferencia(documentoAnulacion.getReferenciaReemplazo());
					logger.info("Documento que reemplaza: " + documentoReemplaza.getCodigoGeneracion());
					DteElectronico documentoAnulado=repoDteElectronico.buscarPorReferencia(documentoAnulacion.getReferencia());
					logger.info("Documento a Anular: " + documentoAnulado.getCodigoGeneracion());
					documentoReemplaza.setCodigoAnulacion(generador.generateNonRepeatableUUID());
					documentoReemplaza.setFechaAnulacion(documentoAnulacion.getFechaAnulacion());
					documentoReemplaza.setHoraAnulacion(documentoAnulacion.getHoraAnulacion());
					documentoReemplaza.setDteElectronicoRef(documentoAnulado.getIdDteElectronico());
					documentoReemplaza.setTipoAnulacion(documentoAnulacion.getTipoMotivoAnulacion());
					documentoReemplaza.setMotivoAnulacion(documentoAnulacion.getDescripcionAnulacion());
					documentoReemplaza.setNomSoliAnulacion(documentoAnulacion.getNombreSolicita());
					//Se setea dui de momento con data dummy
					documentoReemplaza.setTipoDocSoliAnulacion("36");
					documentoReemplaza.setNumDocSoliAnulacion("05113264-8");
					documentoReemplaza.setNomRespAnulacion(documentoAnulacion.getNombreResponsable());
					documentoReemplaza.setTipoDocRespAnulacion("36");
					documentoReemplaza.setNumDocRespAnulacion("05113264-8");
					
					//Actualizamos el estado de documento a anular a anulado en proceso
					documentoAnulado.setEstadoDTE(EstadosDTE.REGISTRADO_ANULACION);
					
					repoDteElectronico.save(documentoReemplaza);
					
					repoDteElectronico.save(documentoAnulado);
					
					ReceptorDte receptor= documentoAnulado.getReceptorDte();
					
					logger.info("Envio Anulacion PBS------------>");
					
					EnvioAnulacion(documentoReemplaza,documentoAnulado,receptor,documentoAnulacion);
				}
				
			}
			
			
		}
		catch (Exception ex) {
			
			logger.error("Error en el proceso de Anulación : " +ex.getMessage().toString());
			throw ex;
		}
		
		
	}
	
	@Override
	public DteElectronico registrar(DteElectronico obj) {
		// TODO Auto-generated method stub
		return repoDteElectronico.save(obj);
	}

	@Override
	public DteElectronico modificar(DteElectronico obj) {
		// TODO Auto-generated method stub
		return repoDteElectronico.save(obj);
	}

	@Override
	public DteElectronico buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return repoDteElectronico.findById(id).get();
	}
	
	public String EnvioFactura(DteElectronico dteElectronico,ReceptorDte receptorDte,CuerpoDte cuerpoDte, ResumenDte resumenDte,DteCoreRequest request) {
	
		JsonDatosDTE jsonDatosDTE = new JsonDatosDTE();
		JsonIdentificacion jsonIdentificacion = new JsonIdentificacion();
		JsonEmisor jsonEmisor = new JsonEmisor();
		JsonDireccion jsonDireccionEmisor = new JsonDireccion();
		JsonDireccion jsonDireccionReceptor = new JsonDireccion();
		JsonReceptor jsonReceptor = new JsonReceptor();
		JsonCuerpoDocumento jsonCuerpoDocumento = new JsonCuerpoDocumento();
		List<JsonCuerpoDocumento> jsonCuerpoDocumentoList = new ArrayList<>();
		JsonResumen jsonResumen = new JsonResumen();
		
		jsonIdentificacion.setVersion(dteElectronico.getVersion());
		jsonIdentificacion.setAmbiente(dteElectronico.getAmbiente());
		jsonIdentificacion.setTipoDte(dteElectronico.getTipoDte());
		jsonIdentificacion.setNumeroControl(dteElectronico.getNumeroControl());
		jsonIdentificacion.setCodigoGeneracion(dteElectronico.getCodigoGeneracion());
		jsonIdentificacion.setTipoModelo(dteElectronico.getTipoModelo());
		jsonIdentificacion.setTipoOperacion(dteElectronico.getTipoOperacion());
		jsonIdentificacion.setFecEmi(dteElectronico.getFechaEmi().substring(0,4) +"-"+dteElectronico.getFechaEmi().substring(4,6)+"-"+dteElectronico.getFechaEmi().substring(6,8));
		jsonIdentificacion.setHorEmi(dteElectronico.getHoraEmi());
		jsonIdentificacion.setTipoMoneda(dteElectronico.getTipoMoneda());
		
		// Direccion Emisor
		jsonDireccionEmisor.setDepartamento(request.getDireccionDeptoEmi());
		jsonDireccionEmisor.setMunicipio(request.getDireccionMunicipioEmi());
		jsonDireccionEmisor.setComplemento(request.getDireccionComplementoEmi());
		
		
		// Direccion Receptor
		jsonDireccionReceptor.setDepartamento(receptorDte.getDepartamento());
		jsonDireccionReceptor.setMunicipio(receptorDte.getMunicipio());
		jsonDireccionReceptor.setComplemento(receptorDte.getComplemento());
		
		//Direccion Receptor
		
		jsonEmisor.setCode(codeEmisor);
		jsonEmisor.setCorreo(request.getCorreoEmisor());
		jsonEmisor.setNit(nitEmisor);
		jsonEmisor.setNrc(nrcEmisor);
		jsonEmisor.setNombre("BANCO AZUL S.A.");
		jsonEmisor.setCodActividad("64190");
		jsonEmisor.setDescActividad("BANCOS");		
		jsonEmisor.setTipoEstablecimiento(request.getTipoEstablecimiento());
		jsonEmisor.setTelefono("25558100");
		jsonEmisor.setDireccion(jsonDireccionEmisor);
		//Data del receptor			
		jsonReceptor.setNit(null);
		jsonReceptor.setNrc(receptorDte.getNrcReceptor());
		jsonReceptor.setNombre(receptorDte.getNombreReceptor());
		jsonReceptor.setCodActividad(receptorDte.getCodActividad());
		jsonReceptor.setDescActividad(receptorDte.getDescActividad());
		jsonReceptor.setTelefono(null);
		jsonReceptor.setCorreo(receptorDte.getCorreo());
		jsonReceptor.setNumDocumento(receptorDte.getNumeroDocumento());
		jsonReceptor.setTipoDocumento(receptorDte.getTipoDocumento());			
		jsonReceptor.setDireccion(jsonDireccionReceptor);
		
		jsonCuerpoDocumento.setEtiCuerpoDocivaItem(0.00);
		jsonCuerpoDocumento.setNumItem(1);
		jsonCuerpoDocumento.setEtiCuerpoDoctipoItem(cuerpoDte.getTipoItem());
		jsonCuerpoDocumento.setEtiCuerpoDocdescripcion(cuerpoDte.getDescripcion());
		jsonCuerpoDocumento.setEtiCuerpoDoccantidad(cuerpoDte.getCantidad().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocuniMedida(cuerpoDte.getUniMedida());
		jsonCuerpoDocumento.setEtiCuerpoDocprecioUni(cuerpoDte.getPrecioUni().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocmontoDescu(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDocventaNoSuj(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDocventaExenta(cuerpoDte.getVentaExenta().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocventaGravada(cuerpoDte.getVentaGravada().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocpsv(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDocnoGravado(0.00);
		
		jsonCuerpoDocumentoList.add(jsonCuerpoDocumento);
		
		jsonResumen.setTotalExenta(resumenDte.getTotalExenta().doubleValue());
		jsonResumen.setTotalNoSuj(0.00);
		jsonResumen.setTotalGravada(resumenDte.getTotalGravada().doubleValue());
		jsonResumen.setSubTotalVentas(resumenDte.getSubTotalVentas().doubleValue());
		jsonResumen.setDescuNoSuj(0.00);
		jsonResumen.setDescuExenta(0.00);
		jsonResumen.setDescuGravada(0.00);
		jsonResumen.setPorcentajeDescuento(0.00);
		jsonResumen.setTotalDescu(0.00);	
		jsonResumen.setSubTotal(resumenDte.getSubTotal().doubleValue());
		jsonResumen.setIvaPerci1(0.00);
		jsonResumen.setIvaRete1(0.00);
		jsonResumen.setReteRenta(0.00);
		jsonResumen.setMontoTotalOperacion(resumenDte.getMontoTotalOperacion().doubleValue());
		jsonResumen.setTotalNoGravado(0.00);
		jsonResumen.setTotalPagar(resumenDte.getMontoTotalOperacion().doubleValue());
		jsonResumen.setTotalLetras(resumenDte.getTotalLetras());
		jsonResumen.setSaldoFavor(0.00);
		jsonResumen.setCondicionOperacion(1);
		jsonResumen.setTotalIva(0.00);
		
		jsonDatosDTE.setIdentificacion(jsonIdentificacion);
		jsonDatosDTE.setEmisor(jsonEmisor);
		jsonDatosDTE.setReceptor(jsonReceptor);
		jsonDatosDTE.setCuerpoDocumento(jsonCuerpoDocumentoList);		
		jsonDatosDTE.setResumen(jsonResumen);
		
		DatosComplementariosDTE datosComplementarios=new DatosComplementariosDTE();
		datosComplementarios.setTipoDocumento(dteElectronico.getTipoDte());
		datosComplementarios.setNombreCliente(receptorDte.getNombreReceptor());
		datosComplementarios.setCodigoCliente(dteElectronico.getNiuCliente());
		datosComplementarios.setNumeroDeTransaccion(dteElectronico.getReferenciaT24());
		datosComplementarios.setNumeroCuenta(dteElectronico.getCuenta()==null?"":dteElectronico.getCuenta());
		
		String result = servicioPBS.EnviarDocumento(datosComplementarios, jsonDatosDTE);
		
		
		return result;
	}
	
	public String EnvioCCF(DteElectronico dteElectronico,ReceptorDte receptorDte,CuerpoDte cuerpoDte, ResumenDte resumenDte,TributosDte tributosDte,DteCoreRequest request) {

		
		
		JsonDatosDTE jsonDatosDTE = new JsonDatosDTE();
		JsonIdentificacion jsonIdentificacion = new JsonIdentificacion();
		JsonEmisor jsonEmisor = new JsonEmisor();
		JsonDireccion jsonDireccionEmisor = new JsonDireccion();
		JsonDireccion jsonDireccionReceptor = new JsonDireccion();
		JsonReceptor jsonReceptor = new JsonReceptor();
		JsonCuerpoDocumento jsonCuerpoDocumento = new JsonCuerpoDocumento();
		List<JsonCuerpoDocumento> jsonCuerpoDocumentoList = new ArrayList<>();
		JsonResumen jsonResumen = new JsonResumen();
		
		jsonIdentificacion.setVersion(dteElectronico.getVersion());
		jsonIdentificacion.setAmbiente(dteElectronico.getAmbiente());
		jsonIdentificacion.setTipoDte(dteElectronico.getTipoDte());
		jsonIdentificacion.setNumeroControl(dteElectronico.getNumeroControl());
		jsonIdentificacion.setCodigoGeneracion(dteElectronico.getCodigoGeneracion());
		jsonIdentificacion.setTipoModelo(dteElectronico.getTipoModelo());
		jsonIdentificacion.setTipoOperacion(dteElectronico.getTipoOperacion());
		jsonIdentificacion.setFecEmi(dteElectronico.getFechaEmi().substring(0,4) +"-"+dteElectronico.getFechaEmi().substring(4,6)+"-"+dteElectronico.getFechaEmi().substring(6,8));
		jsonIdentificacion.setHorEmi(dteElectronico.getHoraEmi());
		jsonIdentificacion.setTipoMoneda(dteElectronico.getTipoMoneda());
		
		// Direccion Emisor
		jsonDireccionEmisor.setDepartamento(request.getDireccionDeptoEmi());
		jsonDireccionEmisor.setMunicipio(request.getDireccionMunicipioEmi());
		jsonDireccionEmisor.setComplemento(request.getDireccionComplementoEmi());
		
		
		// Direccion Receptor
		jsonDireccionReceptor.setDepartamento(receptorDte.getDepartamento());
		jsonDireccionReceptor.setMunicipio(receptorDte.getMunicipio());
		jsonDireccionReceptor.setComplemento(receptorDte.getComplemento());
		
		//Direccion Receptor
		
		jsonEmisor.setCode(codeEmisor);
		jsonEmisor.setCorreo(request.getCorreoEmisor());
		jsonEmisor.setNit(nitEmisor);
		jsonEmisor.setNrc(nrcEmisor);
		jsonEmisor.setNombre("BANCO AZUL S.A.");
		jsonEmisor.setCodActividad("64190");
		jsonEmisor.setDescActividad("BANCOS");		
		jsonEmisor.setTipoEstablecimiento(request.getTipoEstablecimiento());
		jsonEmisor.setTelefono("25558100");
		jsonEmisor.setDireccion(jsonDireccionEmisor);
		
		//Data del receptor			
		jsonReceptor.setNit(receptorDte.getNitReceptor());
		jsonReceptor.setNrc(receptorDte.getNrcReceptor());
		jsonReceptor.setNombre(receptorDte.getNombreReceptor());
		jsonReceptor.setCodActividad(receptorDte.getCodActividad());
		jsonReceptor.setDescActividad(receptorDte.getDescActividad());
		jsonReceptor.setTelefono(null);
		jsonReceptor.setCorreo(receptorDte.getCorreo());
		jsonReceptor.setDireccion(jsonDireccionReceptor);
		
		List<String> tributos = new ArrayList<>();
		if(request.getEsExento().equals("SI"))
			tributos=null;
		else
			tributos.add("20");
		
		
		jsonCuerpoDocumento.setNumItem(1);
		jsonCuerpoDocumento.setEtiCuerpoDoctipoItem(cuerpoDte.getTipoItem());
		jsonCuerpoDocumento.setEtiCuerpoDocdescripcion(cuerpoDte.getDescripcion());
		jsonCuerpoDocumento.setEtiCuerpoDoccantidad(cuerpoDte.getCantidad().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocuniMedida(cuerpoDte.getUniMedida());
		jsonCuerpoDocumento.setEtiCuerpoDocprecioUni(cuerpoDte.getPrecioUni().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocmontoDescu(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDocventaNoSuj(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDocventaExenta(cuerpoDte.getVentaExenta().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocventaGravada(cuerpoDte.getVentaGravada().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocpsv(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDocnoGravado(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDoctributos(tributos);
		
		jsonCuerpoDocumentoList.add(jsonCuerpoDocumento);
		
		JsonTributos jsonTributos = new JsonTributos();
		if(request.getEsExento().equals("SI"))
			jsonTributos=null;
		else
			{
			jsonTributos.setCodigo(tributosDte.getCodigo());
			jsonTributos.setDescripcion(tributosDte.getDescripcion());
			jsonTributos.setValor(tributosDte.getValor().doubleValue());
			}
		
		List<JsonTributos> jsonTributosList = new ArrayList<>();
		jsonTributosList.add(jsonTributos);
		
		jsonResumen.setTotalExenta(resumenDte.getTotalExenta().doubleValue());
		jsonResumen.setTotalNoSuj(0.00);
		jsonResumen.setTotalGravada(resumenDte.getTotalGravada().doubleValue());
		jsonResumen.setSubTotalVentas(resumenDte.getSubTotalVentas().doubleValue());
		jsonResumen.setDescuNoSuj(0.00);
		jsonResumen.setDescuExenta(0.00);
		jsonResumen.setDescuGravada(0.00);
		jsonResumen.setPorcentajeDescuento(0.00);
		jsonResumen.setTotalDescu(0.00);	
		jsonResumen.setSubTotal(resumenDte.getSubTotal().doubleValue());
		jsonResumen.setIvaPerci1(0.00);
		jsonResumen.setIvaRete1(0.00);
		jsonResumen.setReteRenta(0.00);
		jsonResumen.setMontoTotalOperacion(resumenDte.getMontoTotalOperacion().doubleValue());
		jsonResumen.setTotalNoGravado(0.00);
		jsonResumen.setTotalPagar(resumenDte.getMontoTotalOperacion().doubleValue());
		jsonResumen.setTotalLetras(resumenDte.getTotalLetras());
		jsonResumen.setSaldoFavor(0.00);
		jsonResumen.setCondicionOperacion(1);
		jsonResumen.setTributos(jsonTributosList);
		
		jsonDatosDTE.setIdentificacion(jsonIdentificacion);
		jsonDatosDTE.setEmisor(jsonEmisor);
		jsonDatosDTE.setReceptor(jsonReceptor);
		jsonDatosDTE.setCuerpoDocumento(jsonCuerpoDocumentoList);		
		jsonDatosDTE.setResumen(jsonResumen);
		
		DatosComplementariosDTE datosComplementarios=new DatosComplementariosDTE();
		datosComplementarios.setTipoDocumento(dteElectronico.getTipoDte());
		datosComplementarios.setNombreCliente(receptorDte.getNombreReceptor());
		datosComplementarios.setCodigoCliente(dteElectronico.getNiuCliente());
		datosComplementarios.setNumeroDeTransaccion(dteElectronico.getReferenciaT24());
		datosComplementarios.setNumeroCuenta(dteElectronico.getCuenta()==null?"":dteElectronico.getCuenta());
		
		String result = servicioPBS.EnviarDocumento(datosComplementarios, jsonDatosDTE);
		
		
		return result;
	}

	public String EnvioAnulacion(DteElectronico dteElectronico,DteElectronico dteOriginalAnular,ReceptorDte receptorDte,DteAnulacionRequestDTE request) {
	
		JsonDatosDTE jsonDatosDTE = new JsonDatosDTE();
		JsonIdentificacion jsonIdentificacion = new JsonIdentificacion();
		JsonEmisor jsonEmisor = new JsonEmisor();
		JsonDocumento jsonDocumento = new JsonDocumento();
		JsonMotivo jsonMotivo = new JsonMotivo();
		
		jsonIdentificacion.setVersion(VersionDTE.VERSION_ANULACION);
		jsonIdentificacion.setAmbiente(dteElectronico.getAmbiente());			
		jsonIdentificacion.setCodigoGeneracion(dteElectronico.getCodigoAnulacion());
		jsonIdentificacion.setFecAnula(dteElectronico.getFechaAnulacion().substring(0,4) +"-"+dteElectronico.getFechaAnulacion().substring(4,6)+"-"+dteElectronico.getFechaAnulacion().substring(6,8));
		jsonIdentificacion.setHorAnula(dteElectronico.getHoraAnulacion());
		

		
		jsonEmisor.setCode(codeEmisor);
		jsonEmisor.setNit(nitEmisor);
		jsonEmisor.setNombre("BANCO AZUL S.A.");
		jsonEmisor.setTipoEstablecimiento(request.getTipoEstablecimiento());
		jsonEmisor.setTelefono("25558100");
		
		jsonDocumento.setTipoDte(dteOriginalAnular.getTipoDte());
		jsonDocumento.setCodigoGeneracion(dteOriginalAnular.getCodigoGeneracion());
		jsonDocumento.setSelloRecibido("SelloDeRecepcion");
		jsonDocumento.setNumeroControl(dteOriginalAnular.getNumeroControl());
		jsonDocumento.setFecEmi(dteOriginalAnular.getFechaEmi().substring(0,4) +"-"+dteOriginalAnular.getFechaEmi().substring(4,6)+"-"+dteOriginalAnular.getFechaEmi().substring(6,8));
		jsonDocumento.setMontoIva(0.00);
		jsonDocumento.setCodigoGeneracionR(dteElectronico.getTipoAnulacion().equals("2")? null: dteElectronico.getCodigoGeneracion());
		jsonDocumento.setTipoDocumento(receptorDte.getTipoDocumento());
		jsonDocumento.setNumDocumento(receptorDte.getNumeroDocumento());
		jsonDocumento.setNombre(receptorDte.getNombreReceptor());
		jsonDocumento.setTelefono(null);
		jsonDocumento.setCorreo(receptorDte.getCorreo());
		
		jsonMotivo.setTipoAnulacion(Integer.valueOf(dteElectronico.getTipoAnulacion()));
		jsonMotivo.setMotivoAnulacion(dteElectronico.getMotivoAnulacion());
		jsonMotivo.setNombreResponsable(dteElectronico.getNomRespAnulacion());
		jsonMotivo.setTipDocResponsable(dteElectronico.getTipoDocRespAnulacion());
		jsonMotivo.setNumDocResponsable(dteElectronico.getNumDocRespAnulacion());
		jsonMotivo.setNombreSolicita(dteElectronico.getNomSoliAnulacion());
		jsonMotivo.setTipDocSolicita(dteElectronico.getTipoDocSoliAnulacion());
		jsonMotivo.setNumDocSolicita(dteElectronico.getNumDocSoliAnulacion());
		
		jsonDatosDTE.setIdentificacion(jsonIdentificacion);
		jsonDatosDTE.setEmisor(jsonEmisor);
		jsonDatosDTE.setDocumento(jsonDocumento);
		jsonDatosDTE.setMotivo(jsonMotivo);
		
		DatosComplementariosDTE datosComplementarios=new DatosComplementariosDTE();
		datosComplementarios.setTipoDocumento("00");
		datosComplementarios.setNombreCliente(receptorDte.getNombreReceptor());
		datosComplementarios.setCodigoCliente(dteElectronico.getNiuCliente());
		datosComplementarios.setNumeroDeTransaccion(dteElectronico.getReferenciaT24());
		datosComplementarios.setNumeroCuenta(dteOriginalAnular.getCuenta()==null?"":dteOriginalAnular.getCuenta());
		
		String result = servicioPBS.EnviarDocumento(datosComplementarios, jsonDatosDTE);
		
		return result;
	}
	
	public String EnvioNotaCredito(DteElectronico dteElectronico,ReceptorDte receptorDte,CuerpoDte cuerpoDte, ResumenDte resumenDte,TributosDte tributosDte,DocumentosRelacionadosDte documentoRelacionado, DteAnulacionRequestDTE request) {
		
		JsonDatosDTE jsonDatosDTE = new JsonDatosDTE();
		JsonIdentificacion jsonIdentificacion = new JsonIdentificacion();
		List<JsonDocumentoRelacionado> documentoRelacionadoLista = new ArrayList<>();
		JsonEmisor jsonEmisor = new JsonEmisor();
		JsonDireccion jsonDireccionEmisor = new JsonDireccion();
		JsonDireccion jsonDireccionReceptor = new JsonDireccion();
		JsonReceptor jsonReceptor = new JsonReceptor();
		JsonCuerpoDocumento jsonCuerpoDocumento = new JsonCuerpoDocumento();
		List<JsonCuerpoDocumento> jsonCuerpoDocumentoList = new ArrayList<>();
		JsonResumen jsonResumen = new JsonResumen();
		
		jsonIdentificacion.setVersion(dteElectronico.getVersion());
		jsonIdentificacion.setAmbiente(dteElectronico.getAmbiente());
		jsonIdentificacion.setTipoDte(dteElectronico.getTipoDte());
		jsonIdentificacion.setNumeroControl(dteElectronico.getNumeroControl());
		jsonIdentificacion.setCodigoGeneracion(dteElectronico.getCodigoGeneracion());
		jsonIdentificacion.setTipoModelo(dteElectronico.getTipoModelo());
		jsonIdentificacion.setTipoOperacion(dteElectronico.getTipoOperacion());
		jsonIdentificacion.setFecEmi(dteElectronico.getFechaEmi().substring(0,4) +"-"+dteElectronico.getFechaEmi().substring(4,6)+"-"+dteElectronico.getFechaEmi().substring(6,8));
		jsonIdentificacion.setHorEmi(dteElectronico.getHoraEmi());
		jsonIdentificacion.setTipoMoneda(dteElectronico.getTipoMoneda());
		
		JsonDocumentoRelacionado jsonDocumentoRelacionado = new JsonDocumentoRelacionado();
		jsonDocumentoRelacionado.setTipoDocumento(documentoRelacionado.getTipoDocumento());
		jsonDocumentoRelacionado.setTipoGeneracion(documentoRelacionado.getTipoGeneracion());
		jsonDocumentoRelacionado.setNumeroDocumento(documentoRelacionado.getNumeroDocumento());
		jsonDocumentoRelacionado.setFechaEmision(documentoRelacionado.getFechaEmision().substring(0,4) +"-"+documentoRelacionado.getFechaEmision().substring(4,6)+"-"+documentoRelacionado.getFechaEmision().substring(6,8));
		
		documentoRelacionadoLista.add(jsonDocumentoRelacionado);
		
		// Direccion Emisor
		jsonDireccionEmisor.setDepartamento("06");
		jsonDireccionEmisor.setMunicipio("14");
		jsonDireccionEmisor.setComplemento("Avenida Olímpica y Alameda Manuel Enrique Araujo No. 3553");
		
		
		// Direccion Receptor
		jsonDireccionReceptor.setDepartamento(receptorDte.getDepartamento());
		jsonDireccionReceptor.setMunicipio(receptorDte.getMunicipio());
		jsonDireccionReceptor.setComplemento(receptorDte.getComplemento());
		
		
		
		jsonEmisor.setCode(codeEmisor);
		jsonEmisor.setCorreo(request.getCorreoEmisor());
		jsonEmisor.setNit(nitEmisor);
		jsonEmisor.setNrc(nrcEmisor);
		jsonEmisor.setNombre("BANCO AZUL S.A.");
		jsonEmisor.setCodActividad("64190");
		jsonEmisor.setDescActividad("BANCOS");		
		jsonEmisor.setTipoEstablecimiento(request.getTipoEstablecimiento());
		jsonEmisor.setTelefono("25558100");
		jsonEmisor.setDireccion(jsonDireccionEmisor);
		
		//Data del receptor			
		jsonReceptor.setNit(receptorDte.getNitReceptor());
		jsonReceptor.setNrc(receptorDte.getNrcReceptor());
		jsonReceptor.setNombre(receptorDte.getNombreReceptor());
		jsonReceptor.setCodActividad(receptorDte.getCodActividad());
		jsonReceptor.setDescActividad(receptorDte.getDescActividad());
		jsonReceptor.setTelefono(null);
		jsonReceptor.setCorreo(receptorDte.getCorreo());
		jsonReceptor.setDireccion(jsonDireccionReceptor);
		
		List<String> tributos = new ArrayList<>();
		tributos.add("20");
		
		
		jsonCuerpoDocumento.setNumItem(1);
		jsonCuerpoDocumento.setEtiCuerpoDoctipoItem(cuerpoDte.getTipoItem());
		jsonCuerpoDocumento.setEtiCuerpoDocnumeroDocumento(cuerpoDte.getNumeroDocumento());
		jsonCuerpoDocumento.setEtiCuerpoDocdescripcion(cuerpoDte.getDescripcion());
		jsonCuerpoDocumento.setEtiCuerpoDoccantidad(cuerpoDte.getCantidad().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocuniMedida(cuerpoDte.getUniMedida());
		jsonCuerpoDocumento.setEtiCuerpoDocprecioUni(cuerpoDte.getPrecioUni().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDocmontoDescu(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDocventaNoSuj(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDocventaExenta(0.00);
		jsonCuerpoDocumento.setEtiCuerpoDocventaGravada(cuerpoDte.getVentaGravada().doubleValue());
		jsonCuerpoDocumento.setEtiCuerpoDoctributos(tributos);
		
		jsonCuerpoDocumentoList.add(jsonCuerpoDocumento);
		
		JsonTributos jsonTributos = new JsonTributos();
		jsonTributos.setCodigo("20");
		jsonTributos.setDescripcion("Impuesto al Valor Agregado 13%");
		jsonTributos.setValor(tributosDte.getValor().doubleValue());
		
		List<JsonTributos> jsonTributosList = new ArrayList<>();
		jsonTributosList.add(jsonTributos);
		
		jsonResumen.setTotalExenta(0.00);
		jsonResumen.setTotalNoSuj(0.00);
		jsonResumen.setTotalGravada(resumenDte.getTotalGravada().doubleValue());
		jsonResumen.setSubTotalVentas(resumenDte.getSubTotalVentas().doubleValue());
		jsonResumen.setDescuNoSuj(0.00);
		jsonResumen.setDescuExenta(0.00);
		jsonResumen.setDescuGravada(0.00);
		jsonResumen.setPorcentajeDescuento(0.00);
		jsonResumen.setTotalDescu(0.00);	
		jsonResumen.setSubTotal(resumenDte.getSubTotal().doubleValue());
		jsonResumen.setIvaPerci1(0.00);
		jsonResumen.setIvaRete1(0.00);
		jsonResumen.setReteRenta(0.00);
		jsonResumen.setMontoTotalOperacion(resumenDte.getMontoTotalOperacion().doubleValue());
		jsonResumen.setTotalNoGravado(0.00);
		jsonResumen.setTotalPagar(resumenDte.getMontoTotalOperacion().doubleValue());
		jsonResumen.setTotalLetras(resumenDte.getTotalLetras());
		jsonResumen.setSaldoFavor(0.00);
		jsonResumen.setCondicionOperacion(1);
		jsonResumen.setTributos(jsonTributosList);
		
		jsonDatosDTE.setIdentificacion(jsonIdentificacion);
		jsonDatosDTE.setEmisor(jsonEmisor);
		jsonDatosDTE.setReceptor(jsonReceptor);
		jsonDatosDTE.setCuerpoDocumento(jsonCuerpoDocumentoList);		
		jsonDatosDTE.setResumen(jsonResumen);
		jsonDatosDTE.setDocumentoRelacionado(documentoRelacionadoLista);
		
		DatosComplementariosDTE datosComplementarios=new DatosComplementariosDTE();
		datosComplementarios.setTipoDocumento(dteElectronico.getTipoDte());
		datosComplementarios.setNombreCliente(receptorDte.getNombreReceptor());
		datosComplementarios.setCodigoCliente(dteElectronico.getNiuCliente());
		datosComplementarios.setNumeroDeTransaccion(dteElectronico.getReferenciaT24());
		datosComplementarios.setNumeroCuenta(dteElectronico.getCuenta()==null?"":dteElectronico.getCuenta());
		
		String result = servicioPBS.EnviarDocumento(datosComplementarios, jsonDatosDTE);
		
		
		
		return result;
	}
			
}
