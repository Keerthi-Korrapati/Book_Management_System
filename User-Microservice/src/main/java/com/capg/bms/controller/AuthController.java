package com.capg.bms.controller;


import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.capg.bms.entity.User;
import com.capg.bms.jwt.JwtUtils;
import com.capg.bms.repository.UserRepository;
import com.capg.bms.request.LoginRequest;
import com.capg.bms.request.SignupRequest;
import com.capg.bms.response.JwtResponse;
import com.capg.bms.response.MessageResponse;
import com.capg.bms.service.UserDetailsImpl;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/BookStore/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtUtils jwtUtils;
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(
			@Validated @RequestBody LoginRequest loginRequest) {
		
		//here i am authenticate username and password by using  authenticationManager
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);// sets authenticated user's details in the security context
		//  I am generating jwtToken to the authentication by using jwtutils
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		//here i am creating new jwt responses
		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail() 
												 ));
	}
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser( @RequestBody SignupRequest signUpRequest) {
		//here I am checking  username is present in Data Base if it is present it will give message
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		//here I am checking  email is present in Data Base if it is present it will give message
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
							 signUpRequest.getEmail(),
							 //encoder is used to encode the password
							 encoder.encode(signUpRequest.getPassword()));
		//saving  new user in database
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

}