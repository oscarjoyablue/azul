package sv.com.dte.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CatalogoTipoInvalidacion implements Serializable {


	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCatalogoTipoInvalidacion")
    private Integer idCatalogoTipoInvalidacion;
    @Basic(optional = false)
    @NotNull
    @Size(max = 2)
    @Column(name = "Codigo")
    private String codigo;
	@Basic(optional = false)
    @NotNull
    @Size(max = 300)
    @Column(name = "Descripcion")
    private String descripcion;

}
