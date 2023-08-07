package sv.com.dte.model;

import javax.validation.constraints.NotNull;

public class DteAnulacionRequestDTE {


	@NotNull
	private String referencia;
	
	private String referenciaReemplazo;
	@NotNull
	private String codAgencia;
	@NotNull
	private String tipoDTEAnulacion;
	@NotNull
	private String niuClienteReceptor;
	
	private String tipoMotivoAnulacion;
	@NotNull
	private String descripcionAnulacion;
	@NotNull
	private String monto;
	
	private String codigoTributo;
	
	private String valorDeTributo;
	@NotNull
	private String nombreEmisor;
	@NotNull
	private String nitEmisor;
	@NotNull
	private String tipoEstablecimiento;
	
	private String telefonoEmisor;
	@NotNull
	private String correoEmisor;
	@NotNull
	private String nombreSolicita;
	
	private String tipoDocSolicita;
	
	private String numeroDocSolicita;
	@NotNull
	private String nombreResponsable;
	
	private String tipoDocResponsable;
	
	private String numeroDocResponsable;
	@NotNull
	private String fechaAnulacion;
	@NotNull
	private String horaAnulacion;
	
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getCodAgencia() {
		return codAgencia;
	}
	public void setCodAgencia(String codAgencia) {
		this.codAgencia = codAgencia;
	}

	public String getReferenciaReemplazo() {
		return referenciaReemplazo;
	}
	public void setReferenciaReemplazo(String referenciaReemplazo) {
		this.referenciaReemplazo = referenciaReemplazo;
	}
	public String getTipoDTEAnulacion() {
		return tipoDTEAnulacion;
	}
	public void setTipoDTEAnulacion(String tipoDTEAnulacion) {
		this.tipoDTEAnulacion = tipoDTEAnulacion;
	}
	public String getNiuClienteReceptor() {
		return niuClienteReceptor;
	}
	public void setNiuClienteReceptor(String niuClienteReceptor) {
		this.niuClienteReceptor = niuClienteReceptor;
	}
	public String getTipoMotivoAnulacion() {
		return tipoMotivoAnulacion;
	}
	public void setTipoMotivoAnulacion(String tipoMotivoAnulacion) {
		this.tipoMotivoAnulacion = tipoMotivoAnulacion;
	}
	public String getDescripcionAnulacion() {
		return descripcionAnulacion;
	}
	public void setDescripcionAnulacion(String descripcionAnulacion) {
		this.descripcionAnulacion = descripcionAnulacion;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getCodigoTributo() {
		return codigoTributo;
	}
	public void setCodigoTributo(String codigoTributo) {
		this.codigoTributo = codigoTributo;
	}
	public String getValorDeTributo() {
		return valorDeTributo;
	}
	public void setValorDeTributo(String valorDeTributo) {
		this.valorDeTributo = valorDeTributo;
	}
	public String getNombreEmisor() {
		return nombreEmisor;
	}
	public void setNombreEmisor(String nombreEmisor) {
		this.nombreEmisor = nombreEmisor;
	}
	public String getNitEmisor() {
		return nitEmisor;
	}
	public void setNitEmisor(String nitEmisor) {
		this.nitEmisor = nitEmisor;
	}
	public String getTipoEstablecimiento() {
		return tipoEstablecimiento;
	}
	public void setTipoEstablecimiento(String tipoEstablecimiento) {
		this.tipoEstablecimiento = tipoEstablecimiento;
	}
	public String getTelefonoEmisor() {
		return telefonoEmisor;
	}
	public void setTelefonoEmisor(String telefonoEmisor) {
		this.telefonoEmisor = telefonoEmisor;
	}
	public String getCorreoEmisor() {
		return correoEmisor;
	}
	public void setCorreoEmisor(String correoEmisor) {
		this.correoEmisor = correoEmisor;
	}
	public String getNombreSolicita() {
		return nombreSolicita;
	}
	public void setNombreSolicita(String nombreSolicita) {
		this.nombreSolicita = nombreSolicita;
	}
	public String getTipoDocSolicita() {
		return tipoDocSolicita;
	}
	public void setTipoDocSolicita(String tipoDocSolicita) {
		this.tipoDocSolicita = tipoDocSolicita;
	}
	public String getNumeroDocSolicita() {
		return numeroDocSolicita;
	}
	public void setNumeroDocSolicita(String numeroDocSolicita) {
		this.numeroDocSolicita = numeroDocSolicita;
	}
	public String getNombreResponsable() {
		return nombreResponsable;
	}
	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}
	public String getTipoDocResponsable() {
		return tipoDocResponsable;
	}
	public void setTipoDocResponsable(String tipoDocResponsable) {
		this.tipoDocResponsable = tipoDocResponsable;
	}
	public String getNumeroDocResponsable() {
		return numeroDocResponsable;
	}
	public void setNumeroDocResponsable(String numeroDocResponsable) {
		this.numeroDocResponsable = numeroDocResponsable;
	}
	public String getFechaAnulacion() {
		return fechaAnulacion;
	}
	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}
	public String getHoraAnulacion() {
		return horaAnulacion;
	}
	public void setHoraAnulacion(String horaAnulacion) {
		this.horaAnulacion = horaAnulacion;
	}
	 

	

	
	
}
