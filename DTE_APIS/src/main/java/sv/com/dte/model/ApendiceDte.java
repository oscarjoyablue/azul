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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "ApendiceDte")
public class ApendiceDte  implements Serializable {

	  	private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "IdApendiceDte")
	    private Integer idApendiceDte;
	    @OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "IdDteElectronico", referencedColumnName = "IdDteElectronico")
	    private DteElectronico idDteElectronico;
	    @Basic(optional = false)
	    @NotNull
	    @Size(max = 25)
	    @Column(name = "Campo")
	    private String campo;
		@Basic(optional = false)
	    @NotNull
	    @Size(max = 50)
	    @Column(name = "Etiqueta")
	    private String etiqueta;
		@Basic(optional = false)
	    @NotNull
	    @Size(max = 150)
	    @Column(name = "Valor")
	    private String valor;
		
		public ApendiceDte() {
			super();
			// TODO Auto-generated constructor stub
		}

		public DteElectronico getIdDteElectronico() {
			return idDteElectronico;
		}

		public void setIdDteElectronico(DteElectronico idDteElectronico) {
			this.idDteElectronico = idDteElectronico;
		}

		public String getCampo() {
			return campo;
		}

		public void setCampo(String campo) {
			this.campo = campo;
		}

		public String getEtiqueta() {
			return etiqueta;
		}

		public void setEtiqueta(String etiqueta) {
			this.etiqueta = etiqueta;
		}

		public String getValor() {
			return valor;
		}

		public void setValor(String valor) {
			this.valor = valor;
		}	
	
		
}
