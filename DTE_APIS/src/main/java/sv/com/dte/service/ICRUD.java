package sv.com.dte.service;

public interface ICRUD<T> {
	T registrar(T obj);
	T modificar(T obj);
	T buscarPorId(Integer id);
}
