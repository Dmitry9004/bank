package interfaces;

import java.util.List;

public interface DetailsService<T> {
	public List<T> getAllByUserId(int id);
}
