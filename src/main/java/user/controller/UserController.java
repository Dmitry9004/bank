package user.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import user.core.UserDetailsImpl;
import user.core.UserService;
import user.dto.UserDTO;
import user.model.User;

@RestController
@RequestMapping("/user")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private UserService userService;
	private ModelMapper mapper;
	
	public UserController(UserService userService, ModelMapper mapper) {
		this.userService = userService;
		this.mapper = mapper;
	}
	
	@GetMapping
	public ResponseEntity<UserDTO> getUserProps(@AuthenticationPrincipal UserDetailsImpl principal) 
		throws ResponseStatusException{
		
		User user = userService.get(principal.getUserId()).get();
		UserDTO userDto = mapper.map(user, UserDTO.class);
		
		logger.info("UserController: getUserProprs, userId: " + principal.getUserId());
		
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
}
