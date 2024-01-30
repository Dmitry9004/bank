package interfaces;

import java.util.List;

public interface DetailsRepository<T> {
	
	public List<T> getAllByUserId(int id);
}
