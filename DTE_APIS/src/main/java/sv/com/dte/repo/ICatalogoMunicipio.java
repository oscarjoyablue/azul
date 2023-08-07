package sv.com.dte.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import sv.com.dte.model.CatalogoMunicipio; 


public interface ICatalogoMunicipio extends JpaRepository<CatalogoMunicipio,Integer> {

	
	@Query("from CatalogoMunicipio munic where munic.descripcion like %:municipio% and munic.departamento= :departamento ")
	CatalogoMunicipio buscarPorNombre(@Param("municipio") String municipio, @Param("departamento") String departamento);
}
