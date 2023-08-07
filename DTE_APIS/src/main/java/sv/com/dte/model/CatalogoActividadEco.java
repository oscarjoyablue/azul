package sv.com.dte.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "CatalogoActividadEco")
public class CatalogoActividadEco implements Serializable {

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCatalogoActividadEco")
    private Integer idCatalogoActividadEco;
    @Basic(optional = false)
    @NotNull
    @Size(max = 5)
    @Column(name = "Codigo")
    private String codigo;
	@Basic(optional = false)
    @NotNull
    @Size(max = 500)
    @Column(name = "Descripcion")
    private String descripcion;
	
	
	public CatalogoActividadEco() {
		
	}
	public Integer getIdCatalogoActividadEco() {
		return idCatalogoActividadEco;
	}
	public void setIdCatalogoActividadEco(Integer idCatalogoActividadEco) {
		this.idCatalogoActividadEco = idCatalogoActividadEco;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}