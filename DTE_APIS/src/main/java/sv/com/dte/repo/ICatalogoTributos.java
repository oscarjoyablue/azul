package sv.com.dte.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sv.com.dte.model.CatalogoTributos;



public interface  ICatalogoTributos extends JpaRepository<CatalogoTributos,Integer> {

	@Query("from CatalogoTributos tributos where tributos.codigo = :codigo")
	CatalogoTributos buscarPorCodigo(@Param("codigo") String codigo);
}
