package sv.com.dte.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sv.com.dte.model.DteElectronico;

public interface IDteElectronicoRepo extends JpaRepository<DteElectronico,Integer> {

	@Query(value="EXEC ValidarCodigoGeneracion @CodigoGeneracion=:CodigoGeneracion",nativeQuery = true)
	 int ValidarCodigoGeneracion(@Param("CodigoGeneracion") String codigoGeneracion);
	
	@Query(value="EXEC GetCorrelativoNumControl @Fecha=:Fecha",nativeQuery = true)
	 int GetCorrelativoNumControl(@Param("Fecha") String fecha);
	/*
	@Query("select count(i.idDteElectronico) + 1 from DteElectronico i where  i.numeroControl is not null and YEAR(fechaEmi)=YEAR(:fecha)")
	long ObtenerCorrelativo(@Param("fecha") String fecha);
	
	@Query("select count(i.idDteElectronico) from DteElectronico i where i.codigoGeneracion = :codigoGeneracion or i.codigoAnulacion=:codigoGeneracion ")
	long ConsultarPorCodigoGeneracion(@Param("codigoGeneracion") String codigoGeneracion);
	*/
	@Query("from DteElectronico documento where documento.referenciaT24 = :referencia ")
	DteElectronico buscarPorReferencia(@Param("referencia") String referencia);
	
	@Query("from DteElectronico i where i.codigoGeneracion = :codigoGeneracion ")
	DteElectronico buscarPorCodigoGeneracion(@Param("codigoGeneracion") String codigoGeneracion);
	
	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update DteElectronico set EstadoDTE = :estadoDTE where CodigoGeneracion = :codigoGeneracion or CodGeneracionAnula = :codigoGeneracion", nativeQuery = true)
	public int updateEstadoDTE(@Param("codigoGeneracion") String codigoGeneracion,@Param("estadoDTE") Integer estadoDTE);
	
	@Query("from DteElectronico i where i.estadoDTE = :estadoDTE ")
	List<DteElectronico> buscarPorEstadoDTE(@Param("estadoDTE") Integer estadoDTE);
	
	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update DteElectronico set DTEEnviar = :json where CodigoGeneracion = :codigoGeneracion", nativeQuery = true)
	public int updateDTEEnviar(@Param("codigoGeneracion") String codigoGeneracion,@Param("json") String json);
	
	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update DteElectronico set DTEAnulacion = :json where CodGeneracionAnula = :codigoGeneracion", nativeQuery = true)
	public int updateDTEAnulacion(@Param("codigoGeneracion") String codigoGeneracion,@Param("json") String json);
	
	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update DteElectronico set Respuesta = :json where CodigoGeneracion = :codigoGeneracion", nativeQuery = true)
	public int updateRespuesta(@Param("codigoGeneracion") String codigoGeneracion,@Param("json") String json);
	
	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update DteElectronico set RespuestaAnulacion = :json where CodGeneracionAnula = :codigoGeneracion", nativeQuery = true)
	public int updateRespuestaAnulacion(@Param("codigoGeneracion") String codigoGeneracion,@Param("json") String json);
	
	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update DteElectronico set SelloRecibido = :sello, DocumentoBase64 = :documento where CodigoGeneracion = :codigoGeneracion", nativeQuery = true)
	public int updateSelloDocumento(@Param("codigoGeneracion") String codigoGeneracion,@Param("sello") String sello, @Param("documento") String documento);
}
