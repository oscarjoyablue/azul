package sv.com.dte.model;

import java.io.Serializable;
import java.math.BigDecimal;


import javax.validation.constraints.Size;

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

@Entity
@Table(name = "CuerpoDte")
public class CuerpoDte implements Serializable {

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCuerpoDte")
    private Integer idCuerpoDte;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IdDteElectronico", referencedColumnName = "IdDteElectronico")
    private DteElectronico dteElectronico;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IdResumenDte", referencedColumnName = "IdResumenDte")
    private ResumenDte resumenDte;
    @Basic(optional = true)
    
    @Column(name = "NumItem")
    private Integer numItem;
    @Basic(optional = true)
    
    @Column(name = "TipoItem")
    private Integer tipoItem;
    @Basic(optional = true)
    
    @Size(max = 36)
    @Column(name = "NumeroDocumento")
    private String numeroDocumento;
    @Basic(optional = true)
    
    @Size(max = 1000)
    @Column(name = "Descripcion")
    private String descripcion;
    @Basic(optional = true)
    
    @Column(name = "Cantidad")
    private BigDecimal cantidad;
    @Basic(optional = true)
    
    @Column(name = "UniMedida")
    private Integer uniMedida;
    @Basic(optional = true)
    
    @Column(name = "PrecioUni")
    private BigDecimal precioUni;
    @Basic(optional = true)
    
    @Column(name = "MontoDescu")
    private BigDecimal montoDescu;
    @Basic(optional = true)
    
    @Column(name = "VentaNoSuj")
    private BigDecimal ventaNoSuj;
    @Basic(optional = true)
    
    @Column(name = "VentaExenta")
    private BigDecimal ventaExenta;
    @Basic(optional = true)
    
    @Column(name = "VentaGravada")
    private BigDecimal ventaGravada;

    
    
	public CuerpoDte() {
		
	}


	public ResumenDte getResumenDte() {
		return resumenDte;
	}


	public void setResumenDte(ResumenDte resumenDte) {
		this.resumenDte = resumenDte;
	}


	public Integer getIdCuerpoDte() {
		return idCuerpoDte;
	}


	public void setIdCuerpoDte(Integer idCuerpoDte) {
		this.idCuerpoDte = idCuerpoDte;
	}


	public DteElectronico getDteElectronico() {
		return dteElectronico;
	}


	public void setDteElectronico(DteElectronico dteElectronico) {
		this.dteElectronico = dteElectronico;
	}


	public Integer getNumItem() {
		return numItem;
	}


	public void setNumItem(Integer numItem) {
		this.numItem = numItem;
	}


	public Integer getTipoItem() {
		return tipoItem;
	}


	public void setTipoItem(Integer tipoItem) {
		this.tipoItem = tipoItem;
	}


	public String getNumeroDocumento() {
		return numeroDocumento;
	}


	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public BigDecimal getCantidad() {
		return cantidad;
	}


	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}


	public Integer getUniMedida() {
		return uniMedida;
	}


	public void setUniMedida(Integer uniMedida) {
		this.uniMedida = uniMedida;
	}


	public BigDecimal getPrecioUni() {
		return precioUni;
	}


	public void setPrecioUni(BigDecimal precioUni) {
		this.precioUni = precioUni;
	}


	public BigDecimal getMontoDescu() {
		return montoDescu;
	}


	public void setMontoDescu(BigDecimal montoDescu) {
		this.montoDescu = montoDescu;
	}


	public BigDecimal getVentaNoSuj() {
		return ventaNoSuj;
	}


	public void setVentaNoSuj(BigDecimal ventaNoSuj) {
		this.ventaNoSuj = ventaNoSuj;
	}


	public BigDecimal getVentaExenta() {
		return ventaExenta;
	}


	public void setVentaExenta(BigDecimal ventaExenta) {
		this.ventaExenta = ventaExenta;
	}


	public BigDecimal getVentaGravada() {
		return ventaGravada;
	}


	public void setVentaGravada(BigDecimal ventaGravada) {
		this.ventaGravada = ventaGravada;
	}
	
    
    
    
}