package sv.com.dte.service;

import sv.com.dte.model.DteAnulacionRequestDTE;
import sv.com.dte.model.DteCoreRequest;
import sv.com.dte.model.DteElectronico;


public interface IDteElectronicoService extends ICRUD<DteElectronico> {
	

	public void RegistrarDte( DteCoreRequest documentoElectronico) throws Exception;
	
	public void AnulacionDte( DteAnulacionRequestDTE anulacionDocumentoElectronico) throws Exception;
		
}
