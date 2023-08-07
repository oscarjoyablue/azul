package sv.com.dte.model;

import javax.validation.constraints.NotNull;

public class DteCoreRequest {
	
	@NotNull
	private String tipoDTE;
	@NotNull
	private String referencia;
	@NotNull
	private String niuClienteReceptor;
	@NotNull
	private String fechaGeneracion;
	@NotNull
	private String horaGeneracion;
	@NotNull
	private String tipoMoneda;
	@NotNull
	private String nitEmisor;
	@NotNull
	private String nrcEmisor;
	@NotNull
	private String codActividadEconomEmi;
	@NotNull
	private String actividadEconomEmi;
	@NotNull
	private String tipoEstablecimiento;
	@NotNull
	private String direccionDeptoEmi;
	@NotNull
	private String direccionMunicipioEmi;
	@NotNull
	private String direccionComplementoEmi;
	
	private String telefonoEmisor;
	@NotNull
	private String correoEmisor;
	
	private String nitReceptor;
	
	private String nrcReceptor;
	@NotNull
	private String nombreORazonSocial;
	@NotNull
	private String codActividadEconomRec;
	@NotNull
	private String actividadEconomRec;
	@NotNull
	private String direccionDeptoRec;
	@NotNull
	private String direccionMunicipioRec;
	@NotNull
	private String direccionComplementoRec;
	@NotNull
	private String correoRec;
	@NotNull
	private String tipoItem;
	
	private String esExento;
	@NotNull
	private String descripcionParaDTE;
	@NotNull
	private String monto;
	@NotNull
	private String montoLetras;
	
	private String codigoTributo;
	
	private String valorDeTributo;
	@NotNull
	private String codAgencia;
	
	private String cuentaCargo;
	
	private String tipoServicio;
	

	public String getTipoDTE() {
		return tipoDTE;
	}
	public void setTipoDTE(String tipoDTE) {
		this.tipoDTE = tipoDTE;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getNiuClienteReceptor() {
		return niuClienteReceptor;
	}
	public void setNiuClienteReceptor(String niuClienteReceptor) {
		this.niuClienteReceptor = niuClienteReceptor;
	}
	public String getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	public String getHoraGeneracion() {
		return horaGeneracion;
	}
	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getNitEmisor() {
		return nitEmisor;
	}
	public void setNitEmisor(String nitEmisor) {
		this.nitEmisor = nitEmisor;
	}
	public String getNrcEmisor() {
		return nrcEmisor;
	}
	public void setNrcEmisor(String nrcEmisor) {
		this.nrcEmisor = nrcEmisor;
	}
	public String getCodActividadEconomEmi() {
		return codActividadEconomEmi;
	}
	public void setCodActividadEconomEmi(String codActividadEconomEmi) {
		this.codActividadEconomEmi = codActividadEconomEmi;
	}
	public String getActividadEconomEmi() {
		return actividadEconomEmi;
	}
	public void setActividadEconomEmi(String actividadEconomEmi) {
		this.actividadEconomEmi = actividadEconomEmi;
	}
	public String getTipoEstablecimiento() {
		return tipoEstablecimiento;
	}
	public void setTipoEstablecimiento(String tipoEstablecimiento) {
		this.tipoEstablecimiento = tipoEstablecimiento;
	}
	public String getDireccionDeptoEmi() {
		return direccionDeptoEmi;
	}
	public void setDireccionDeptoEmi(String direccionDeptoEmi) {
		this.direccionDeptoEmi = direccionDeptoEmi;
	}
	public String getDireccionMunicipioEmi() {
		return direccionMunicipioEmi;
	}
	public void setDireccionMunicipioEmi(String direccionMunicipioEmi) {
		this.direccionMunicipioEmi = direccionMunicipioEmi;
	}
	public String getDireccionComplementoEmi() {
		return direccionComplementoEmi;
	}
	public void setDireccionComplementoEmi(String direccionComplementoEmi) {
		this.direccionComplementoEmi = direccionComplementoEmi;
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
	public String getNitReceptor() {
		return nitReceptor;
	}
	public void setNitReceptor(String nitReceptor) {
		this.nitReceptor = nitReceptor;
	}
	public String getNrcReceptor() {
		return nrcReceptor;
	}
	public void setNrcReceptor(String nrcReceptor) {
		this.nrcReceptor = nrcReceptor;
	}
	public String getNombreORazonSocial() {
		return nombreORazonSocial;
	}
	public void setNombreORazonSocial(String nombreORazonSocial) {
		this.nombreORazonSocial = nombreORazonSocial;
	}
	public String getCodActividadEconomRec() {
		return codActividadEconomRec;
	}
	public void setCodActividadEconomRec(String codActividadEconomRec) {
		this.codActividadEconomRec = codActividadEconomRec;
	}
	public String getActividadEconomRec() {
		return actividadEconomRec;
	}
	public void setActividadEconomRec(String actividadEconomRec) {
		this.actividadEconomRec = actividadEconomRec;
	}
	public String getDireccionDeptoRec() {
		return direccionDeptoRec;
	}
	public void setDireccionDeptoRec(String direccionDeptoRec) {
		this.direccionDeptoRec = direccionDeptoRec;
	}
	public String getDireccionMunicipioRec() {
		return direccionMunicipioRec;
	}
	public void setDireccionMunicipioRec(String direccionMunicipioRec) {
		this.direccionMunicipioRec = direccionMunicipioRec;
	}
	public String getDireccionComplementoRec() {
		return direccionComplementoRec;
	}
	public void setDireccionComplementoRec(String direccionComplementoRec) {
		this.direccionComplementoRec = direccionComplementoRec;
	}
	public String getCorreoRec() {
		return correoRec;
	}
	public void setCorreoRec(String correoRec) {
		this.correoRec = correoRec;
	}
	public String getTipoItem() {
		return tipoItem;
	}
	public void setTipoItem(String tipoItem) {
		this.tipoItem = tipoItem;
	}
	public String getEsExento() {
		return esExento;
	}
	public void setEsExento(String esExento) {
		this.esExento = esExento;
	}
	public String getDescripcionParaDTE() {
		return descripcionParaDTE;
	}
	public void setDescripcionParaDTE(String descripcionParaDTE) {
		this.descripcionParaDTE = descripcionParaDTE;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getMontoLetras() {
		return montoLetras;
	}
	public void setMontoLetras(String montoLetras) {
		this.montoLetras = montoLetras;
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
	public String getCodAgencia() {
		return codAgencia;
	}
	public void setCodAgencia(String codAgencia) {
		this.codAgencia = codAgencia;
	}
	public String getCuentaCargo() {
		return cuentaCargo;
	}
	public void setCuentaCargo(String cuentaCargo) {
		this.cuentaCargo = cuentaCargo;
	}
	public String getTipoServicio() {
		return tipoServicio;
	}
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	
	
	
	
	
}
