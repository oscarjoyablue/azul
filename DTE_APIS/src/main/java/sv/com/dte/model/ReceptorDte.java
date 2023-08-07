package sv.com.dte.model;

import java.io.Serializable;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ReceptorDte")
public class ReceptorDte implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdReceptorDte")
    private Integer idReceptorDte;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IdDteElectronico", referencedColumnName = "IdDteElectronico")
    private DteElectronico dteElectronico;
    @Basic(optional = true)
    
    @Size(max = 2)
    @Column(name = "TipoDocumento")
    private String tipoDocumento;
    @Basic(optional = true)
    
    @Size(max = 30)
    @Column(name = "NumeroDocumento")
    private String numeroDocumento;
    @Basic(optional = true)
    
    @Size(max = 14)
    @Column(name = "NIT")
    private String nitReceptor;
    @Basic(optional = true)
    
    @Size(max = 14)
    @Column(name = "NRC")
    private String nrcReceptor;
    @Basic(optional = false)
    
    @Size(max = 250)
    @Column(name = "Nombre")
    private String nombreReceptor;
	@Basic(optional = false)
    
    @Size(max = 6)
    @Column(name = "CodActividad")
    private String codActividad;
	@Basic(optional = false)
    
    @Size(max = 150)
    @Column(name = "DescActividad")
    private String descActividad;
    @Basic(optional = false)
    
    @Size(max = 2)
    @Column(name = "Departamento")
    private String departamento;
	@Basic(optional = false)
    
    @Size(max = 2)
    @Column(name = "Municipio")
    private String municipio;
	@Basic(optional = false)
    
    @Size(max = 200)
    @Column(name = "Complemento")
    private String complemento;
	@Basic(optional = false)
    
    @Size(max = 100)
    @Column(name = "Correo")
    private String correo;
	
	public ReceptorDte() {
		
	}

	public Integer getIdReceptorDte() {
		return idReceptorDte;
	}


	public void setIdReceptorDte(Integer idReceptorDte) {
		this.idReceptorDte = idReceptorDte;
	}


	public DteElectronico getDteElectronico() {
		return dteElectronico;
	}


	public void setDteElectronico(DteElectronico dteElectronico) {
		this.dteElectronico = dteElectronico;
	}


	public String getTipoDocumento() {
		return tipoDocumento;
	}


	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}


	public String getNumeroDocumento() {
		return numeroDocumento;
	}


	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
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


	public String getNombreReceptor() {
		return nombreReceptor;
	}


	public void setNombreReceptor(String nombreReceptor) {
		this.nombreReceptor = nombreReceptor;
	}


	public String getCodActividad() {
		return codActividad;
	}


	public void setCodActividad(String codActividad) {
		this.codActividad = codActividad;
	}


	public String getDescActividad() {
		return descActividad;
	}


	public void setDescActividad(String descActividad) {
		this.descActividad = descActividad;
	}
	public String getDepartamento() {
		return departamento;
	}


	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}


	public String getMunicipio() {
		return municipio;
	}


	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}


	public String getComplemento() {
		return complemento;
	}


	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}


	public String getCorreo() {
		return correo;
	}


	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
}
