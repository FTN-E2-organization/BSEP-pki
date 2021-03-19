package rs.ac.uns.ftn.bsep.pki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.pki.model.Authority;
import rs.ac.uns.ftn.bsep.pki.repository.AuthorityRepository;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	  private AuthorityRepository authorityRepository;
		
	  @Autowired
	  public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
		  this.authorityRepository = authorityRepository;
	  }

	  @Override
	  public Authority findById(Long id) {
	    Authority auth = this.authorityRepository.getOne(id);
	    return auth;
	  }

	  @Override
	  public Authority findByname(String name) {
	    Authority auth = this.authorityRepository.findByName(name);
	    return auth;
	  }	
}
