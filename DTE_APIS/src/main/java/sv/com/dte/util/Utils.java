package sv.com.dte.util;

public class Utils {
	
	public String ObtenerDescripcionTipoDTE(String tipoDTE)
	{
		String strDescripcion  =  "";
		
		if (tipoDTE.equals("01"))
		{
			strDescripcion  =  "FE";
		}
		else if (tipoDTE.equals("03"))
		{
			strDescripcion  =  "CCFE";
		}
		else if (tipoDTE.equals("05"))
		{
			strDescripcion  =  "NCE";
		}
		else if (tipoDTE.equals("07"))
		{
			strDescripcion  =  "CRE";
		}
		else if (tipoDTE.equals("14"))
		{
			strDescripcion  =  "FSEE";
		}
		else if (tipoDTE.equals("00"))
		{
			strDescripcion  =  "ANULACION";
		}
		else
		{
			strDescripcion  =  "Tipo Desconocido";
		}
		
		return strDescripcion;
	}
}
