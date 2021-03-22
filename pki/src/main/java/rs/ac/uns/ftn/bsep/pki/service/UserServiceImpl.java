package rs.ac.uns.ftn.bsep.pki.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.pki.model.User;
import rs.ac.uns.ftn.bsep.pki.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Collection<User> getAllSubjects() {
		return userRepository.getAllSubjects();
		//return userRepository.findAll();
	}
}
