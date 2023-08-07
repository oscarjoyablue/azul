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
@Table(name = "CatalogoMunicipio")
public class CatalogoMunicipio implements Serializable {

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCatalogoMunicipio")
    private Integer idCatalogoMunicipio;
    @Basic(optional = false)
    @NotNull
    @Size(max = 2)
    @Column(name = "Codigo")
    private String codigo;
	@Basic(optional = false)
    @NotNull
    @Size(max = 150)
    @Column(name = "Descripcion")
    private String descripcion;
	@Basic(optional = false)
    @NotNull
    @Size(max = 2)
    @Column(name = "Departamento")
    private String departamento;
	
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
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	
}
