package rs.ac.uns.ftn.bsep.pki.service;

import rs.ac.uns.ftn.bsep.pki.model.Authority;

public interface AuthorityService {
	
	Authority findById(Long id);
	Authority findByname(String name);
}
