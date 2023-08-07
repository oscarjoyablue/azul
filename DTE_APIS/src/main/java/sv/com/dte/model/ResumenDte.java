package sv.com.dte.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Size;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ResumenDte")
public class ResumenDte implements Serializable{

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdResumenDte")
    private Integer idResumenDte;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IdDteElectronico", referencedColumnName = "IdDteElectronico")
    private DteElectronico dteElectronico;
    @Basic(optional = false)
    @Column(name = "TotalNoSuj")
    private BigDecimal totalNoSuj;
    @Basic(optional = true)
    @Column(name = "TotalExenta")
    private BigDecimal totalExenta;
    @Basic(optional = true)
    @Column(name = "TotalGravada")
    private BigDecimal totalGravada;
    @Basic(optional = true)
    @Column(name = "SubTotalVentas")
    private BigDecimal subTotalVentas;
    @Basic(optional = true)
    @Column(name = "DescNoSuj")
    private BigDecimal descNoSuj;
    @Basic(optional = true)
    @Column(name = "DescuExenta")
    private BigDecimal descuExenta;
    @Basic(optional = true)
    @Column(name = "DescGravada")
    private BigDecimal descGravada;
    @Basic(optional = true)
    @Column(name = "PorcentajeDescuento")
    private BigDecimal porcentajeDescuento;
    @Basic(optional = true)
    @Column(name = "totalDescu")
    private BigDecimal TotalDescu;
    @Basic(optional = true)
    @Column(name = "SubTotal")
    private BigDecimal subTotal;
    @Basic(optional = true)
    @Column(name = "IvaPerci1")
    private BigDecimal ivaPerci1;
    @Basic(optional = true)
    @Column(name = "IvaRete1")
    private BigDecimal ivaRete1;
    @Basic(optional = true)
    @Column(name = "ReteRenta")
    private BigDecimal reteRenta;
    @Basic(optional = false)
    @Column(name = "MontoTotalOperacion")
    private BigDecimal montoTotalOperacion;
    @Basic(optional = true)
    @Column(name = "TotalNoGravado")
    private BigDecimal totalNoGravado;
    @Basic(optional = false)
    @Column(name = "totalPagar")
    private BigDecimal totalPagar;
    @Basic(optional = false) 
    @Size(max = 200)
    @Column(name = "TotalLetras")
    private String totalLetras;
    @Basic(optional = true)   
    @Column(name = "CondicionOperacion")
    private Integer condicionOperacion;
    
    @OneToMany()
    private List<TributosDte> tributos;
    
    @OneToMany()
    private List<CuerpoDte> cuerpos;
    
	public ResumenDte() {
		
	}


	public Integer getIdResumenDte() {
		return idResumenDte;
	}


	public void setIdResumenDte(Integer idResumenDte) {
		this.idResumenDte = idResumenDte;
	}


	public DteElectronico getDteElectronico() {
		return dteElectronico;
	}


	public void setDteElectronico(DteElectronico dteElectronico) {
		this.dteElectronico = dteElectronico;
	}


	public BigDecimal getTotalNoSuj() {
		return totalNoSuj;
	}


	public void setTotalNoSuj(BigDecimal totalNoSuj) {
		this.totalNoSuj = totalNoSuj;
	}


	public BigDecimal getTotalExenta() {
		return totalExenta;
	}


	public void setTotalExenta(BigDecimal totalExenta) {
		this.totalExenta = totalExenta;
	}


	public BigDecimal getTotalGravada() {
		return totalGravada;
	}


	public void setTotalGravada(BigDecimal totalGravada) {
		this.totalGravada = totalGravada;
	}


	public BigDecimal getSubTotalVentas() {
		return subTotalVentas;
	}


	public void setSubTotalVentas(BigDecimal subTotalVentas) {
		this.subTotalVentas = subTotalVentas;
	}


	public BigDecimal getDescNoSuj() {
		return descNoSuj;
	}


	public void setDescNoSuj(BigDecimal descNoSuj) {
		this.descNoSuj = descNoSuj;
	}


	public BigDecimal getDescuExenta() {
		return descuExenta;
	}


	public void setDescuExenta(BigDecimal descuExenta) {
		this.descuExenta = descuExenta;
	}


	public BigDecimal getDescGravada() {
		return descGravada;
	}


	public void setDescGravada(BigDecimal descGravada) {
		this.descGravada = descGravada;
	}


	public BigDecimal getPorcentajeDescuento() {
		return porcentajeDescuento;
	}


	public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}


	public BigDecimal getTotalDescu() {
		return TotalDescu;
	}


	public void setTotalDescu(BigDecimal totalDescu) {
		TotalDescu = totalDescu;
	}


	public BigDecimal getSubTotal() {
		return subTotal;
	}


	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}


	public BigDecimal getIvaPerci1() {
		return ivaPerci1;
	}


	public void setIvaPerci1(BigDecimal ivaPerci1) {
		this.ivaPerci1 = ivaPerci1;
	}


	public BigDecimal getIvaRete1() {
		return ivaRete1;
	}


	public void setIvaRete1(BigDecimal ivaRete1) {
		this.ivaRete1 = ivaRete1;
	}


	public BigDecimal getReteRenta() {
		return reteRenta;
	}


	public void setReteRenta(BigDecimal reteRenta) {
		this.reteRenta = reteRenta;
	}


	public BigDecimal getMontoTotalOperacion() {
		return montoTotalOperacion;
	}


	public void setMontoTotalOperacion(BigDecimal montoTotalOperacion) {
		this.montoTotalOperacion = montoTotalOperacion;
	}


	public BigDecimal getTotalNoGravado() {
		return totalNoGravado;
	}


	public void setTotalNoGravado(BigDecimal totalNoGravado) {
		this.totalNoGravado = totalNoGravado;
	}


	public BigDecimal getTotalPagar() {
		return totalPagar;
	}


	public void setTotalPagar(BigDecimal totalPagar) {
		this.totalPagar = totalPagar;
	}


	public String getTotalLetras() {
		return totalLetras;
	}


	public void setTotalLetras(String totalLetras) {
		this.totalLetras = totalLetras;
	}





	public Integer getCondicionOperacion() {
		return condicionOperacion;
	}


	public void setCondicionOperacion(Integer condicionOperacion) {
		this.condicionOperacion = condicionOperacion;
	}


	public List<TributosDte> getTributos() {
		return tributos;
	}


	public void setTributos(List<TributosDte> tributos) {
		this.tributos = tributos;
	}


	public List<CuerpoDte> getCuerpos() {
		return cuerpos;
	}


	public void setCuerpos(List<CuerpoDte> cuerpos) {
		this.cuerpos = cuerpos;
	}
	

    
}
