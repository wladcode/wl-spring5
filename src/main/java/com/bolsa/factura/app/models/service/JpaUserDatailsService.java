package com.bolsa.factura.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsa.factura.app.models.dao.IUserDao;
import com.bolsa.factura.app.models.entity.Rol;
import com.bolsa.factura.app.models.entity.User;

@Service("JpaUserDatailsService")
public class JpaUserDatailsService implements UserDetailsService {

    @Autowired
    private IUserDao userDao;
    
    private Logger logger = LoggerFactory.getLogger(JpaUserDatailsService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {

	User user = userDao.findByUsername(username);
	
	if(user == null) {
	    logger.error("Error en login: no existe el usuario: {}", username); 
	    throw new UsernameNotFoundException("Usuario no existe");
	}

	List<GrantedAuthority> authorities = new ArrayList<>();
	for (Rol rol : user.getRoles()) {
	    authorities.add(new SimpleGrantedAuthority(rol.getRolname()));
	}
	
	if(authorities.isEmpty()) {
	    logger.error("Error en login: el usuario: {}, no tiene roles asignados", username); 
	    throw new UsernameNotFoundException("El usuario no tiene roles asigados");
	}

	return new org.springframework.security.core.userdetails.User(username, user.getPassword(), user.getEnabled(),
		true, true, true, authorities);
    }

}
