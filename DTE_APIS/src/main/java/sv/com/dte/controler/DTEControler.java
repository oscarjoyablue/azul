package sv.com.dte.controler;

import javax.annotation.ManagedBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sv.com.dte.exception.CustomerException;
import sv.com.dte.model.DteAnulacionRequestDTE;
import sv.com.dte.model.DteCoreRequest;
import sv.com.dte.service.IDteElectronicoService;
import sv.com.dte.service.impl.DocuWareServiceImp;
import sv.com.dte.service.impl.ServicioPBSImp;

@Component
@RestController
@ManagedBean
@RequestMapping("/dte")
public class DTEControler {

	private static final Logger logger = LoggerFactory.getLogger(DTEControler.class);
	@Autowired
	private IDteElectronicoService dteElectronicoService;
	
	@Autowired
	private ServicioPBSImp servicioPBSImp;
	
	@Autowired
	private DocuWareServiceImp docuWareServiceImp;
	
	@RequestMapping(value = "/core", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> RecepcionDte(@RequestBody DteCoreRequest dteRequest) throws CustomerException {
		try
		{
			dteElectronicoService.RegistrarDte(dteRequest);
			return ResponseEntity.ok(" 1|Petición procesada correctamente");
		}
		catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "3|Error en la peticion: " + ex.toString());

		}
		
	}
	
	@RequestMapping(value = "/core/anulacion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<String> AnulacionDte(@RequestBody DteAnulacionRequestDTE dteAnulacionRequest) throws CustomerException {
		try
		{	dteElectronicoService.AnulacionDte(dteAnulacionRequest);
			return ResponseEntity.ok("1|Petición procesada correctamente");
		}
		catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("3|Error en la peticion: " + ex.toString());

		}
		
	}
	
	@RequestMapping(value = "/reprocesardte", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String ReprocesoDTE()
	{
		String respuesta = "";
			
		try
		{
			respuesta = servicioPBSImp.ReprocesarDocumentos();
		}
		catch (Exception ex) 
		{
			logger.error(ex.getMessage().toString());
			ex.printStackTrace();
		}
		
		return respuesta;
	}
	
	@RequestMapping(value = "/actualizarsellodocumento", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String ActualizarSelloDocumento()
	{
		String respuesta = "";
			
		try
		{
			respuesta = servicioPBSImp.ActualizarSelloDocumento();
		}
		catch (Exception ex) 
		{
			logger.error(ex.getMessage().toString());
			ex.printStackTrace();
		}
		
		return respuesta;
	}
	
	@RequestMapping(value = "/reprocesardocuware", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String ReprocesarDocuware()
	{
		String respuesta = "";
			
		try
		{
			respuesta = docuWareServiceImp.ReprocesarEnvioDocumentos();
		}
		catch (Exception ex) 
		{
			logger.error(ex.getMessage().toString());
			ex.printStackTrace();
		}
		
		return respuesta;
	}
}
