package project.br.useAuthentication.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.exception.InternalExceptionResult;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	//@Autowired
	//private PasswordEncoder encoder;

	public StatusResult<?> listAll() {
		try {
			List<UserJPA> result = this.userRepository.findAll();
			List<UserDTO> userlist = result.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
			return new StatusResult<List<UserDTO>>(HttpStatus.OK.value(), userlist);
		}
		catch(Exception e) {
			throw new InternalExceptionResult("Something Went Wrong!");
		}
	}
	
	public StatusResult<?> pesquisarPorID(Long id) {
		UserDTO user = new UserDTO(this.userRepository.findById(id).
				orElseThrow(() -> new NotFoundExceptionResult("The requested Id was not found."))); 
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), user);
	}
	
	@Transactional
	public StatusResult<?> inserirOuAtualizar(UserDTO user) {
		UserDTO u = new UserDTO(this.userRepository.save(new UserJPA(user)));
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), u);
	}
	
	public StatusResult<?> apagar(Long id) {
		try {
			this.userRepository.deleteById(id);
			return new StatusResult<HttpStatus>(HttpStatus.OK.value(), HttpStatus.OK);
		}
		catch(Exception e) {
			throw new NotFoundExceptionResult("The requested Id was not found.");
		}
	}
}
