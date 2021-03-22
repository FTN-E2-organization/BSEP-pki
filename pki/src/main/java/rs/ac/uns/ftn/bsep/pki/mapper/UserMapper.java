package rs.ac.uns.ftn.bsep.pki.mapper;

import java.util.ArrayList;
import java.util.Collection;

import rs.ac.uns.ftn.bsep.pki.dto.UserDTO;
import rs.ac.uns.ftn.bsep.pki.model.User;

public class UserMapper {
	
	public static UserDTO toUserDTO(User user) {
		return new UserDTO(user.getId(), user.getUsername());
	}

	public static Collection<UserDTO> toUserDTOs(Collection<User> users){
		Collection<UserDTO> userDTOs = new ArrayList<UserDTO>();
		for(User user:users) {
			userDTOs.add(toUserDTO(user));
		}
		return userDTOs;
	}
}
