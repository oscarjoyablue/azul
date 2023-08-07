package sv.com.dte.model;

import java.util.List;

public class JsonResumen{

	private Double totalExenta;
	private Double totalNoSuj;
	private Double totalGravada;
	private Double subTotalVentas;
	private Double descuNoSuj;
	private Double descuExenta;
	private Double descuGravada;
	private Double porcentajeDescuento;
	private Double totalDescu;
	private List<JsonTributos> tributos;
	private Double subTotal;
	private Double ivaPerci1;
	private Double ivaRete1;
	private Double reteRenta;
	private Double montoTotalOperacion;
	private Double totalNoGravado;
	private Double totalPagar;
	private String totalLetras;
	private Double saldoFavor;
	private Integer condicionOperacion;
	private JsonPagos pagos [];
	private String numPagoElectronico;
	private Double totalIva;
	private Double totalSujetoRetencion;
	private Double totalIVAretenido;
	private String totalIVAretenidoLetras;
	private Double totalCompra;
	private Double descu;
	private String observaciones;
	
	public Double getTotalExenta() {
		return totalExenta;
	}
	public void setTotalExenta(Double totalExenta) {
		this.totalExenta = totalExenta;
	}
	public Double getTotalNoSuj() {
		return totalNoSuj;
	}
	public void setTotalNoSuj(Double totalNoSuj) {
		this.totalNoSuj = totalNoSuj;
	}
	public Double getTotalGravada() {
		return totalGravada;
	}
	public void setTotalGravada(Double totalGravada) {
		this.totalGravada = totalGravada;
	}
	public Double getSubTotalVentas() {
		return subTotalVentas;
	}
	public void setSubTotalVentas(Double subTotalVentas) {
		this.subTotalVentas = subTotalVentas;
	}
	public Double getDescuNoSuj() {
		return descuNoSuj;
	}
	public void setDescuNoSuj(Double descuNoSuj) {
		this.descuNoSuj = descuNoSuj;
	}
	public Double getDescuExenta() {
		return descuExenta;
	}
	public void setDescuExenta(Double descuExenta) {
		this.descuExenta = descuExenta;
	}
	public Double getDescuGravada() {
		return descuGravada;
	}
	public void setDescuGravada(Double descuGravada) {
		this.descuGravada = descuGravada;
	}
	public Double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}
	public void setPorcentajeDescuento(Double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}
	public Double getTotalDescu() {
		return totalDescu;
	}
	public void setTotalDescu(Double totalDescu) {
		this.totalDescu = totalDescu;
	}
	public List<JsonTributos> getTributos() {
		return tributos;
	}
	public void setTributos(List<JsonTributos> tributos) {
		this.tributos = tributos;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public Double getIvaPerci1() {
		return ivaPerci1;
	}
	public void setIvaPerci1(Double ivaPerci1) {
		this.ivaPerci1 = ivaPerci1;
	}
	public Double getIvaRete1() {
		return ivaRete1;
	}
	public void setIvaRete1(Double ivaRete1) {
		this.ivaRete1 = ivaRete1;
	}
	public Double getReteRenta() {
		return reteRenta;
	}
	public void setReteRenta(Double reteRenta) {
		this.reteRenta = reteRenta;
	}
	public Double getMontoTotalOperacion() {
		return montoTotalOperacion;
	}
	public void setMontoTotalOperacion(Double montoTotalOperacion) {
		this.montoTotalOperacion = montoTotalOperacion;
	}
	public Double getTotalNoGravado() {
		return totalNoGravado;
	}
	public void setTotalNoGravado(Double totalNoGravado) {
		this.totalNoGravado = totalNoGravado;
	}
	public Double getTotalPagar() {
		return totalPagar;
	}
	public void setTotalPagar(Double totalPagar) {
		this.totalPagar = totalPagar;
	}
	public String getTotalLetras() {
		return totalLetras;
	}
	public void setTotalLetras(String totalLetras) {
		this.totalLetras = totalLetras;
	}
	public Double getSaldoFavor() {
		return saldoFavor;
	}
	public void setSaldoFavor(Double saldoFavor) {
		this.saldoFavor = saldoFavor;
	}
	public Integer getCondicionOperacion() {
		return condicionOperacion;
	}
	public void setCondicionOperacion(Integer condicionOperacion) {
		this.condicionOperacion = condicionOperacion;
	}
	public JsonPagos[] getPagos() {
		return pagos;
	}
	public void setPagos(JsonPagos[] pagos) {
		this.pagos = pagos;
	}
	public String getNumPagoElectronico() {
		return numPagoElectronico;
	}
	public void setNumPagoElectronico(String numPagoElectronico) {
		this.numPagoElectronico = numPagoElectronico;
	}
	public Double getTotalIva() {
		return totalIva;
	}
	public void setTotalIva(Double totalIva) {
		this.totalIva = totalIva;
	}
	public Double getTotalSujetoRetencion() {
		return totalSujetoRetencion;
	}
	public void setTotalSujetoRetencion(Double totalSujetoRetencion) {
		this.totalSujetoRetencion = totalSujetoRetencion;
	}
	public Double getTotalIVAretenido() {
		return totalIVAretenido;
	}
	public void setTotalIVAretenido(Double totalIVAretenido) {
		this.totalIVAretenido = totalIVAretenido;
	}
	public String getTotalIVAretenidoLetras() {
		return totalIVAretenidoLetras;
	}
	public void setTotalIVAretenidoLetras(String totalIVAretenidoLetras) {
		this.totalIVAretenidoLetras = totalIVAretenidoLetras;
	}
	public Double getTotalCompra() {
		return totalCompra;
	}
	public void setTotalCompra(Double totalCompra) {
		this.totalCompra = totalCompra;
	}
	public Double getDescu() {
		return descu;
	}
	public void setDescu(Double descu) {
		this.descu = descu;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
