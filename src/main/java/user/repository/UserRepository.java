package user.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public Optional<User> getByUsername(String username);
}
