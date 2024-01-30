package user.core;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import interfaces.CustomService;
import user.model.User;
import user.repository.UserRepository;

@Service
public class UserService implements CustomService<User>{
	
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<User> get(int id) {
		return userRepository.findById(id);
	}

	@Override
	public void save(User t) {
		t.setPassword(passwordEncoder.encode(t.getPassword()));
		userRepository.save(t);
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public void delete(User t) {
		userRepository.delete(t);
	}
	
	
}
