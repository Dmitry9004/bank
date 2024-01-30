package user.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import user.model.User;
import user.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService{

	private UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> user = userRepository.getByUsername(username);
		if(user.isEmpty())
			throw new UsernameNotFoundException(username);
		
		UserDetailsImpl userDetails = new UserDetailsImpl(user.get());
		
		List<GrantedAuthority> roles = new ArrayList<>();
		SimpleGrantedAuthority role = new SimpleGrantedAuthority(user.get().getRole().getRoleOfString());
		roles.add(role);
		userDetails.setAuthorities(roles);
		
		System.out.println(username + "////////////////////////////////////////////" + role);
		
		return userDetails;
	}
}
