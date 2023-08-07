package sv.com.dte.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sv.com.dte.model.CatalogoDepartamento;


public interface ICatalogoDepartamento extends JpaRepository<CatalogoDepartamento,Integer> {
	
	@Query("from CatalogoDepartamento depto where depto.descripcion like %:departamento%")
	CatalogoDepartamento buscarPorNombre(@Param("departamento") String departamento);

}
