package sv.com.dte.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@Entity
@Table(name = "TributosDte")
public class TributosDte implements Serializable{

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTributosDte")
    private Integer idTributosDte;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IdResumenDte", referencedColumnName = "IdResumenDte")
    private ResumenDte resumenDte;
    @Basic(optional = false)
    
    @Size(max = 2)
    @Column(name = "Codigo")
    private String codigo;
	@Basic(optional = false)
    
    @Size(max = 200)
    @Column(name = "Descripcion")
    private String descripcion;
	@Basic(optional = false)
    
    @Column(name = "Valor")
    private BigDecimal valor;
	
	
	
	public TributosDte() {
		
	}
	public Integer getIdTributosDte() {
		return idTributosDte;
	}
	public void setIdTributosDte(Integer idTributosDte) {
		this.idTributosDte = idTributosDte;
	}

	public ResumenDte getResumenDte() {
		return resumenDte;
	}
	public void setResumenDte(ResumenDte resumenDte) {
		this.resumenDte = resumenDte;
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
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
}
