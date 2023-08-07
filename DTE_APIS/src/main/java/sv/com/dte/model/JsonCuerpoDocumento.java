package sv.com.dte.model;

import java.util.List;

public class JsonCuerpoDocumento{
	
	private Integer EtiCuerpoDoctipoItem;
	private String EtiCuerpoDocnumeroDocumento;
	private String EtiCuerpoDoccodTributo;
	private String EtiCuerpoDocdescripcion;
	private Double EtiCuerpoDoccantidad;
	private Integer EtiCuerpoDocuniMedida;
	private Double EtiCuerpoDocprecioUni;
	private Double EtiCuerpoDocmontoDescu;
	private Double EtiCuerpoDocventaNoSuj;
	private Double EtiCuerpoDocventaExenta;
	private Double EtiCuerpoDocventaGravada;
	private List<String> EtiCuerpoDoctributos;
	private Double EtiCuerpoDocpsv;
	private Double EtiCuerpoDocnoGravado;
	private String EtiCuerpoDoccodigo;
	private Double EtiCuerpoDocivaItem;	
	private Double EtiCuerpoDocivaRetenido;
	private String EtiCuerpoDocfechaEmision;
	private String EtiCuerpoDoctipoDte;
	private Integer EtiCuerpoDoctipoDoc;
	private String EtiCuerpoDocnumDocumento;
	private Double EtiCuerpoDocmontoSujetoGrav;
	private String EtiCuerpoDoccodigoRetencionMH;
	private Double EtiCuerpoDoccompra;
		
	// ESTE ITEM SIEMPRE DEBE IR DE ULTIMO PARA EVITAR FINALIZACION DE JSON CON COMA 
	// CUANDO SE ELIMINE ATRIBUTOS QUE NO CORRESPONDEN AL TIPO DE FACTURA
	// -----------------------------------------------------------------------------
	private Integer numItem;	
	
	public Integer getEtiCuerpoDoctipoItem() {
		return EtiCuerpoDoctipoItem;
	}
	public void setEtiCuerpoDoctipoItem(Integer etiCuerpoDoctipoItem) {
		EtiCuerpoDoctipoItem = etiCuerpoDoctipoItem;
	}
	public String getEtiCuerpoDocnumeroDocumento() {
		return EtiCuerpoDocnumeroDocumento;
	}
	public void setEtiCuerpoDocnumeroDocumento(String etiCuerpoDocnumeroDocumento) {
		EtiCuerpoDocnumeroDocumento = etiCuerpoDocnumeroDocumento;
	}
	public String getEtiCuerpoDoccodTributo() {
		return EtiCuerpoDoccodTributo;
	}
	public void setEtiCuerpoDoccodTributo(String etiCuerpoDoccodTributo) {
		EtiCuerpoDoccodTributo = etiCuerpoDoccodTributo;
	}
	public String getEtiCuerpoDocdescripcion() {
		return EtiCuerpoDocdescripcion;
	}
	public void setEtiCuerpoDocdescripcion(String etiCuerpoDocdescripcion) {
		EtiCuerpoDocdescripcion = etiCuerpoDocdescripcion;
	}
	public Double getEtiCuerpoDoccantidad() {
		return EtiCuerpoDoccantidad;
	}
	public void setEtiCuerpoDoccantidad(Double etiCuerpoDoccantidad) {
		EtiCuerpoDoccantidad = etiCuerpoDoccantidad;
	}
	public Integer getEtiCuerpoDocuniMedida() {
		return EtiCuerpoDocuniMedida;
	}
	public void setEtiCuerpoDocuniMedida(Integer etiCuerpoDocuniMedida) {
		EtiCuerpoDocuniMedida = etiCuerpoDocuniMedida;
	}
	public Double getEtiCuerpoDocprecioUni() {
		return EtiCuerpoDocprecioUni;
	}
	public void setEtiCuerpoDocprecioUni(Double etiCuerpoDocprecioUni) {
		EtiCuerpoDocprecioUni = etiCuerpoDocprecioUni;
	}
	public Double getEtiCuerpoDocmontoDescu() {
		return EtiCuerpoDocmontoDescu;
	}
	public void setEtiCuerpoDocmontoDescu(Double etiCuerpoDocmontoDescu) {
		EtiCuerpoDocmontoDescu = etiCuerpoDocmontoDescu;
	}
	public Double getEtiCuerpoDocventaNoSuj() {
		return EtiCuerpoDocventaNoSuj;
	}
	public void setEtiCuerpoDocventaNoSuj(Double etiCuerpoDocventaNoSuj) {
		EtiCuerpoDocventaNoSuj = etiCuerpoDocventaNoSuj;
	}
	public Double getEtiCuerpoDocventaExenta() {
		return EtiCuerpoDocventaExenta;
	}
	public void setEtiCuerpoDocventaExenta(Double etiCuerpoDocventaExenta) {
		EtiCuerpoDocventaExenta = etiCuerpoDocventaExenta;
	}
	public Double getEtiCuerpoDocventaGravada() {
		return EtiCuerpoDocventaGravada;
	}
	public void setEtiCuerpoDocventaGravada(Double etiCuerpoDocventaGravada) {
		EtiCuerpoDocventaGravada = etiCuerpoDocventaGravada;
	}
	public List<String> getEtiCuerpoDoctributos() {
		return EtiCuerpoDoctributos;
	}
	public void setEtiCuerpoDoctributos(List<String> etiCuerpoDoctributos) {
		EtiCuerpoDoctributos = etiCuerpoDoctributos;
	}
	public Double getEtiCuerpoDocpsv() {
		return EtiCuerpoDocpsv;
	}
	public void setEtiCuerpoDocpsv(Double etiCuerpoDocpsv) {
		EtiCuerpoDocpsv = etiCuerpoDocpsv;
	}
	public Double getEtiCuerpoDocnoGravado() {
		return EtiCuerpoDocnoGravado;
	}
	public void setEtiCuerpoDocnoGravado(Double etiCuerpoDocnoGravado) {
		EtiCuerpoDocnoGravado = etiCuerpoDocnoGravado;
	}
	public String getEtiCuerpoDoccodigo() {
		return EtiCuerpoDoccodigo;
	}
	public void setEtiCuerpoDoccodigo(String etiCuerpoDoccodigo) {
		EtiCuerpoDoccodigo = etiCuerpoDoccodigo;
	}
	public Double getEtiCuerpoDocivaItem() {
		return EtiCuerpoDocivaItem;
	}
	public void setEtiCuerpoDocivaItem(Double etiCuerpoDocivaItem) {
		EtiCuerpoDocivaItem = etiCuerpoDocivaItem;
	}
	public Double getEtiCuerpoDocivaRetenido() {
		return EtiCuerpoDocivaRetenido;
	}
	public void setEtiCuerpoDocivaRetenido(Double etiCuerpoDocivaRetenido) {
		EtiCuerpoDocivaRetenido = etiCuerpoDocivaRetenido;
	}
	public String getEtiCuerpoDocfechaEmision() {
		return EtiCuerpoDocfechaEmision;
	}
	public void setEtiCuerpoDocfechaEmision(String etiCuerpoDocfechaEmision) {
		EtiCuerpoDocfechaEmision = etiCuerpoDocfechaEmision;
	}
	public String getEtiCuerpoDoctipoDte() {
		return EtiCuerpoDoctipoDte;
	}
	public void setEtiCuerpoDoctipoDte(String etiCuerpoDoctipoDte) {
		EtiCuerpoDoctipoDte = etiCuerpoDoctipoDte;
	}
	public Integer getEtiCuerpoDoctipoDoc() {
		return EtiCuerpoDoctipoDoc;
	}
	public void setEtiCuerpoDoctipoDoc(Integer etiCuerpoDoctipoDoc) {
		EtiCuerpoDoctipoDoc = etiCuerpoDoctipoDoc;
	}
	public String getEtiCuerpoDocnumDocumento() {
		return EtiCuerpoDocnumDocumento;
	}
	public void setEtiCuerpoDocnumDocumento(String etiCuerpoDocnumDocumento) {
		EtiCuerpoDocnumDocumento = etiCuerpoDocnumDocumento;
	}
	public Double getEtiCuerpoDocmontoSujetoGrav() {
		return EtiCuerpoDocmontoSujetoGrav;
	}
	public void setEtiCuerpoDocmontoSujetoGrav(Double etiCuerpoDocmontoSujetoGrav) {
		EtiCuerpoDocmontoSujetoGrav = etiCuerpoDocmontoSujetoGrav;
	}
	public String getEtiCuerpoDoccodigoRetencionMH() {
		return EtiCuerpoDoccodigoRetencionMH;
	}
	public void setEtiCuerpoDoccodigoRetencionMH(String etiCuerpoDoccodigoRetencionMH) {
		EtiCuerpoDoccodigoRetencionMH = etiCuerpoDoccodigoRetencionMH;
	}
	public Double getEtiCuerpoDoccompra() {
		return EtiCuerpoDoccompra;
	}
	public void setEtiCuerpoDoccompra(Double etiCuerpoDoccompra) {
		EtiCuerpoDoccompra = etiCuerpoDoccompra;
	}
	public Integer getNumItem() {
		return numItem;
	}
	public void setNumItem(Integer numItem) {
		this.numItem = numItem;
	}
}
