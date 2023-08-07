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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "DocumentoRelacionadosDte")
public class DocumentosRelacionadosDte implements Serializable{

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDocumentoRelacionadosDte")
    private Integer idDocumentoRelacionadosDte;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IdDteElectronico", referencedColumnName = "IdDteElectronico")
    private DteElectronico dteElectronico;
    @Basic(optional = false)
    
    @Size(max = 2)
    @Column(name = "TipoDocumento")
    private String tipoDocumento;
    @Basic(optional = false)
    
    @Column(name = "TipoGeneracion")
    private Integer tipoGeneracion;
	@Basic(optional = false)
    
    @Size(max = 36)
    @Column(name = "NumeroDocumento")
    private String NumeroDocumento;
	@Basic(optional = false)
    
    @Size(max = 10)
    @Column(name = "FechaEmision")
    private String fechaEmision;
	
	
	public DocumentosRelacionadosDte() {
		
	}
	public Integer getIdDocumentoRelacionadosDte() {
		return idDocumentoRelacionadosDte;
	}
	public void setIdDocumentoRelacionadosDte(Integer idDocumentoRelacionadosDte) {
		this.idDocumentoRelacionadosDte = idDocumentoRelacionadosDte;
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
	public Integer getTipoGeneracion() {
		return tipoGeneracion;
	}
	public void setTipoGeneracion(Integer tipoGeneracion) {
		this.tipoGeneracion = tipoGeneracion;
	}
	public String getNumeroDocumento() {
		return NumeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		NumeroDocumento = numeroDocumento;
	}
	public String getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}	
	
}