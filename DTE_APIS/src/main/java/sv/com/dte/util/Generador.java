package sv.com.dte.util;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sv.com.dte.repo.IDteElectronicoRepo;

@Component
public class Generador {

	
	
	@Autowired
	private IDteElectronicoRepo dteElectronicoRepo;
	
	//Genera un numero de control aleatorio con formato uuid v4
	public static String crearCodigoGeneracion ()
	{
		UUID uuid=UUID.randomUUID();
		String ramdomUUIDString=uuid.toString().toUpperCase();
		return ramdomUUIDString;
		
	}
	//Genera un numero de control no repetible que identifica los DTE
	
	public synchronized String generateNonRepeatableUUID()
	{
		String ramdomUUIDString;
		
		//Se crea el codigo de generacion en caso se repita , se genera uno aleatorio hasta que se pueda crear
		do
		{
			ramdomUUIDString=crearCodigoGeneracion();
		} while(!isUnique(ramdomUUIDString));
		
		return ramdomUUIDString;
	}
	
	//Verifica que el Codigo de generacion sea unico
	private  boolean isUnique(String uuid)
	{
		boolean variable;
		variable=(dteElectronicoRepo.ValidarCodigoGeneracion(uuid)<1)?  true :  false;
		return variable;
	}
	
	
	/*
	 * El Numero de Control estará compuesto de acuerdo al siguiente formato:
	 * DTE-TD-CAMAPUVE-CCCCCCCCCCCCCCC
	 * Donde:
	 * DTE: Siglas de DOcumento Tributario Electronico
	 * TD: Tipo de Documento a Emitir. Ejemplo 01 (Factura)
	 * CAMAPUVE: Casa Matriz + Punto de Venta donde se ha generado el documento, se enviara desde core codigo de agencia . Ejemplo: Se envia desde core: SV00010401, se tomara el 00010401
	 * CCCCCCCCCCCCCCC: Numero correlativo de 15 digitos , secuencial , este sera reiniciado cada año y no se puede repetir dentro de un año calendario.
	 * Ejemplo: 000000000000020
	 * Ejemplo completo: DTE-01-00010401-000000000000020
	 * 
	 */
	
	public synchronized String crearNumeroControl (String tipoDocumento , String casaMPuntoV,String fechaGeneracion)
	{
		//Se separa el codigo SV , ejemplo SV00010401
		//String codigoEntidad=casaMPuntoV.substring(2);
		String correlativo=String.format("%015d",dteElectronicoRepo.GetCorrelativoNumControl(fechaGeneracion));
		return "DTE-" + tipoDocumento+"-"+casaMPuntoV+"-"+correlativo;
	}
	
	
}
