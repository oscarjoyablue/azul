package sv.com.dte.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import sv.com.dte.model.CatalogoActividadEco;

public interface ICatalogoActividadEco extends JpaRepository<CatalogoActividadEco,Integer> {

	@Procedure
	CatalogoActividadEco GetActividadEconomica(@Param("CodigoT24") String codigo);
}
