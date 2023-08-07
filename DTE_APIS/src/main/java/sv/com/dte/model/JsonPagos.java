package sv.com.dte.model;

public class JsonPagos{

	private String codigo;
	private Double montoPago;
	private String referencia;
	private Double periodo;
	private String plazo;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Double getMontoPago() {
		return montoPago;
	}
	public void setMontoPago(Double montoPago) {
		this.montoPago = montoPago;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public Double getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Double periodo) {
		this.periodo = periodo;
	}
	public String getPlazo() {
		return plazo;
	}
	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}
}
