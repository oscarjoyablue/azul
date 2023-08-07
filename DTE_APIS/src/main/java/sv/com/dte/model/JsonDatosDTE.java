package sv.com.dte.model;

import java.util.List;

public class JsonDatosDTE{
	
	public JsonDatosDTE() 
	{
		this.identificacion = new JsonIdentificacion();
		this.emisor = new JsonEmisor();
	}
	
	private JsonIdentificacion identificacion;
	private JsonEmisor emisor;
	private JsonReceptor receptor;
	private JsonSujetoExcluido sujetoExcluido;
	private String otrosDocumentos;
	private List<JsonDocumentoRelacionado> documentoRelacionado;
	private String ventaTercero;
	private List<JsonCuerpoDocumento> cuerpoDocumento;
	private JsonResumen resumen;
	private String extension;
	private String apendice;
	private JsonDocumento documento;
	private JsonMotivo motivo;
	
	public JsonIdentificacion getIdentificacion() {
		return identificacion;
	}
	public void setIdentificacion(JsonIdentificacion identificacion) {
		this.identificacion = identificacion;
	}
	public JsonEmisor getEmisor() {
		return emisor;
	}
	public void setEmisor(JsonEmisor emisor) {
		this.emisor = emisor;
	}
	public JsonReceptor getReceptor() {
		return receptor;
	}
	public void setReceptor(JsonReceptor receptor) {
		this.receptor = receptor;
	}
	public JsonSujetoExcluido getSujetoExcluido() {
		return sujetoExcluido;
	}
	public void setSujetoExcluido(JsonSujetoExcluido sujetoExcluido) {
		this.sujetoExcluido = sujetoExcluido;
	}
	public String getOtrosDocumentos() {
		return otrosDocumentos;
	}
	public void setOtrosDocumentos(String otrosDocumentos) {
		this.otrosDocumentos = otrosDocumentos;
	}
	public List<JsonDocumentoRelacionado> getDocumentoRelacionado() {
		return documentoRelacionado;
	}
	public void setDocumentoRelacionado(List<JsonDocumentoRelacionado> documentoRelacionado) {
		this.documentoRelacionado = documentoRelacionado;
	}
	public String getVentaTercero() {
		return ventaTercero;
	}
	public void setVentaTercero(String ventaTercero) {
		this.ventaTercero = ventaTercero;
	}
	public List<JsonCuerpoDocumento> getCuerpoDocumento() {
		return cuerpoDocumento;
	}
	public void setCuerpoDocumento(List<JsonCuerpoDocumento> cuerpoDocumento) {
		this.cuerpoDocumento = cuerpoDocumento;
	}
	public JsonResumen getResumen() {
		return resumen;
	}
	public void setResumen(JsonResumen resumen) {
		this.resumen = resumen;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getApendice() {
		return apendice;
	}
	public void setApendice(String apendice) {
		this.apendice = apendice;
	}
	public JsonDocumento getDocumento() {
		return documento;
	}
	public void setDocumento(JsonDocumento documento) {
		this.documento = documento;
	}
	public JsonMotivo getMotivo() {
		return motivo;
	}
	public void setMotivo(JsonMotivo motivo) {
		this.motivo = motivo;
	}	
}
