package interfaces;

import java.util.List;
import java.util.Optional;

public interface CustomService<T> {
	public Optional<T> get(int id);
	public List<T> getAll();
	public void save(T t);
	public void delete(T t);
}
