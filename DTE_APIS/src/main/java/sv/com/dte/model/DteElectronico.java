package sv.com.dte.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Size;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "DteElectronico")
public class DteElectronico implements Serializable  {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDteElectronico")
    private Integer idDteElectronico;
    
    @Basic(optional = true)
    @Column(name = "IdDteElectronicoRef",unique=true)
    private Integer dteElectronicoRef;
    
    @Basic(optional = false)
    @Column(name = "Version")
    private Integer version;
    
	@Basic(optional = false)
    @Size(max = 2)
    @Column(name = "Ambiente")
    private String ambiente;
	
	@Basic(optional = false)
    @Size(max = 2)
    @Column(name = "TipoDte")
    private String tipoDte;
	
	@Basic(optional = false)
    @Size(max = 31)
    @Column(name = "NumeroControl")
    private String numeroControl ;
	
	@Basic(optional = false)
    @Size(max = 36)
    @Column(name = "CodigoGeneracion")
    private String codigoGeneracion ;
	
	@Basic(optional = false)
    @Column(name = "TipoModelo")
    private Integer tipoModelo ;
	
	@Basic(optional = false)
    @Column(name = "TipoOperacion")
    private Integer tipoOperacion ;
	
	@Basic(optional = true)
    @Column(name = "TipoContigencia")
    private Integer tipoContigencia ;
	
	@Basic(optional = true)
    @Size(max = 500)
    @Column(name = "MotivoContin")
    private String motivoContin;
	
	@Basic(optional = false)
    @Size(max = 10)
    @Column(name = "FechaEmi")
    private String fechaEmi;
	
	@Basic(optional = false)
    @Size(max = 8)
    @Column(name = "HoraEmi")
    private String horaEmi;
	
	@Basic(optional = false)
    @Size(max = 3)
    @Column(name = "TipoMoneda")
    private String tipoMoneda;
	@Basic(optional = false)

    @Size(max = 30)
    @Column(name = "NiuCliente")
    private String niuCliente;
	
	@Basic(optional = false)
    @Size(max = 30)
    @Column(name = "ReferenciaT24")
    private String referenciaT24;
	
	@Basic(optional = true)
	@Column(name = "EstadoDTE")
	private Integer estadoDTE;
	
	@Basic(optional = true)
    @Size(max = 800)
    @Column(name = "FirmaElectronica")
    private String firmaElectronica;
	
	@Basic(optional = true)
    @Size(max = 300)
    @Column(name = "SelloRecibido")
    private String selloRecibido;

    @Size(max = 36)
    @Column(name = "CodGeneracionAnula")
    private String codigoAnulacion ;
	
    @Size(max = 10)
    @Column(name = "FechaAnulacion")
    private String fechaAnulacion;

    @Size(max = 8)
    @Column(name = "HoraAnulacion")
    private String horaAnulacion;
    
    @Size(max = 1)
    @Column(name = "TipoAnulacion")
    private String tipoAnulacion;

    @Size(max = 250)
    @Column(name = "MotivoAnulacion")
    private String motivoAnulacion;    
    
    @Size(max = 100)
    @Column(name = "NomSoliAnulacion")
    private String nomSoliAnulacion;
    
    @Size(max = 2)
    @Column(name = "TipoDocSoliAnulacion")
    private String tipoDocSoliAnulacion;
    
    @Size(max = 20)
    @Column(name = "NumDocSoliAnulacion")
    private String numDocSoliAnulacion;
    
    @Size(max = 100)
    @Column(name = "NomRespAnulacion")
    private String nomRespAnulacion;
    
    @Size(max = 2)
    @Column(name = "TipoDocRespAnulacion")
    private String tipoDocRespAnulacion;
    
    @Size(max = 20)
    @Column(name = "NumDocRespAnulacion")
    private String numDocRespAnulacion;
    
    @Size(max = 9)
    @Column(name = "CodAgencia")
    private String codAgencia;
    
    @OneToOne(mappedBy="dteElectronico" , cascade=CascadeType.ALL,optional=true)
    @Basic(optional = true)
    private ReceptorDte receptorDte;

    @OneToOne(mappedBy="dteElectronico" , cascade=CascadeType.ALL,optional=true)
    @Basic(optional = true)
    private ResumenDte resumenDte;
    
    @OneToMany(cascade=CascadeType.ALL)
    @Basic(optional = true)
    private List<DocumentosRelacionadosDte> documentoRelacionadosDte;

    @Column(name = "DTEEnviar")
    private String DTEEnviar;
    
    @Column(name = "DTEAnulacion")
    private String DTEAnulacion;
    
    @Column(name = "Respuesta")
    private String respuesta;
    
    @Column(name = "RespuestaAnulacion")
    private String respuestaAnulacion;
    
    @Column(name = "DocumentoBase64")
    private String documentoBase64;
    
    @Size(max = 30)
    @Column(name = "Cuenta")
    private String cuenta;
    
    @Size(max = 5)
    @Column(name = "TipoServicio")
    private String tipoServicio;

	public ReceptorDte getReceptorDte() {
		return receptorDte;
	}

	public void setReceptorDte(ReceptorDte receptorDte) {
		this.receptorDte = receptorDte;
	}

	public DteElectronico() {
		
	}
	
	public String getNiuCliente() {
		return niuCliente;
	}

	public void setNiuCliente(String niuCliente) {
		this.niuCliente = niuCliente;
	}

	public Integer getIdDteElectronico() {
		return idDteElectronico;
	}
	public void setIdDteElectronico(Integer idDteElectronico) {
		this.idDteElectronico = idDteElectronico;
	}

	public Integer getDteElectronicoRef() {
		return dteElectronicoRef;
	}

	public void setDteElectronicoRef(Integer dteElectronicoRef) {
		this.dteElectronicoRef = dteElectronicoRef;
	}

	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getAmbiente() {
		return ambiente;
	}
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}
	public String getTipoDte() {
		return tipoDte;
	}
	public void setTipoDte(String tipoDte) {
		this.tipoDte = tipoDte;
	}
	public String getNumeroControl() {
		return numeroControl;
	}
	public void setNumeroControl(String numeroControl) {
		this.numeroControl = numeroControl;
	}
	public String getCodigoGeneracion() {
		return codigoGeneracion;
	}
	public void setCodigoGeneracion(String codigoGeneracion) {
		this.codigoGeneracion = codigoGeneracion;
	}
	public Integer getTipoModelo() {
		return tipoModelo;
	}
	public void setTipoModelo(Integer tipoModelo) {
		this.tipoModelo = tipoModelo;
	}
	public Integer getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(Integer tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public Integer getTipoContigencia() {
		return tipoContigencia;
	}
	public void setTipoContigencia(Integer tipoContigencia) {
		this.tipoContigencia = tipoContigencia;
	}
	public String getMotivoContin() {
		return motivoContin;
	}
	public void setMotivoContin(String motivoContin) {
		this.motivoContin = motivoContin;
	}
	public String getFechaEmi() {
		return fechaEmi;
	}
	public void setFechaEmi(String fechaEmi) {
		this.fechaEmi = fechaEmi;
	}
	public String getHoraEmi() {
		return horaEmi;
	}
	public void setHoraEmi(String horaEmi) {
		this.horaEmi = horaEmi;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getReferenciaT24() {
		return referenciaT24;
	}
	public void setReferenciaT24(String referenciaT24) {
		this.referenciaT24 = referenciaT24;
	}
	public String getFirmaElectronica() {
		return firmaElectronica;
	}
	public void setFirmaElectronica(String firmaElectronica) {
		this.firmaElectronica = firmaElectronica;
	}
	public String getSelloRecibido() {
		return selloRecibido;
	}
	public void setSelloRecibido(String selloRecibido) {
		this.selloRecibido = selloRecibido;
	}

	public Integer getEstadoDTE() {
		return estadoDTE;
	}

	public void setEstadoDTE(Integer estadoDTE) {
		this.estadoDTE = estadoDTE;
	}


	public String getCodigoAnulacion() {
		return codigoAnulacion;
	}

	public void setCodigoAnulacion(String codigoAnulacion) {
		this.codigoAnulacion = codigoAnulacion;
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

	public String getTipoAnulacion() {
		return tipoAnulacion;
	}

	public void setTipoAnulacion(String tipoAnulacion) {
		this.tipoAnulacion = tipoAnulacion;
	}

	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	public String getNomSoliAnulacion() {
		return nomSoliAnulacion;
	}

	public void setNomSoliAnulacion(String nomSoliAnulacion) {
		this.nomSoliAnulacion = nomSoliAnulacion;
	}

	public String getTipoDocSoliAnulacion() {
		return tipoDocSoliAnulacion;
	}

	public void setTipoDocSoliAnulacion(String tipoDocSoliAnulacion) {
		this.tipoDocSoliAnulacion = tipoDocSoliAnulacion;
	}

	public String getNumDocSoliAnulacion() {
		return numDocSoliAnulacion;
	}

	public void setNumDocSoliAnulacion(String numDocSoliAnulacion) {
		this.numDocSoliAnulacion = numDocSoliAnulacion;
	}

	public String getNomRespAnulacion() {
		return nomRespAnulacion;
	}

	public void setNomRespAnulacion(String nomRespAnulacion) {
		this.nomRespAnulacion = nomRespAnulacion;
	}

	public String getTipoDocRespAnulacion() {
		return tipoDocRespAnulacion;
	}

	public void setTipoDocRespAnulacion(String tipoDocRespAnulacion) {
		this.tipoDocRespAnulacion = tipoDocRespAnulacion;
	}

	public String getNumDocRespAnulacion() {
		return numDocRespAnulacion;
	}

	public void setNumDocRespAnulacion(String numDocRespAnulacion) {
		this.numDocRespAnulacion = numDocRespAnulacion;
	}


	public String getCodAgencia() {
		return codAgencia;
	}

	public void setCodAgencia(String codAgencia) {
		this.codAgencia = codAgencia;
	}

	public ResumenDte getResumenDte() {
		return resumenDte;
	}

	public void setResumenDte(ResumenDte resumenDte) {
		this.resumenDte = resumenDte;
	}

	public List<DocumentosRelacionadosDte> getDocumentoRelacionadosDte() {
		return documentoRelacionadosDte;
	}

	public void setDocumentoRelacionadosDte(List<DocumentosRelacionadosDte> documentoRelacionadosDte) {
		this.documentoRelacionadosDte = documentoRelacionadosDte;
	}

	public String getDTEEnviar() {
		return DTEEnviar;
	}

	public void setDTEEnviar(String dTEEnviar) {
		DTEEnviar = dTEEnviar;
	}

	public String getDTEAnulacion() {
		return DTEAnulacion;
	}

	public void setDTEAnulacion(String dTEAnulacion) {
		DTEAnulacion = dTEAnulacion;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getRespuestaAnulacion() {
		return respuestaAnulacion;
	}

	public void setRespuestaAnulacion(String respuestaAnulacion) {
		this.respuestaAnulacion = respuestaAnulacion;
	}

	public String getDocumentoBase64() {
		return documentoBase64;
	}

	public void setDocumentoBase64(String documentoBase64) {
		this.documentoBase64 = documentoBase64;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	
	
}
