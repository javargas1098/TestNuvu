package com.test.service;

import com.test.config.MapperUtil;
import com.test.dtos.ResponseGenericDTO;
import com.test.dtos.UserDetailDto;
import com.test.dtos.UserDto;
import com.test.exceptions.UserRuntimeException;
import com.test.exceptions.UsernameException;
import com.test.model.User;
import com.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    public ResponseGenericDTO save(UserDto user) {
        try {
            user.setPassword(bcryptEncoder.encode(user.getPassword()));
            userRepository.save(MapperUtil.convertToEntity(user, User.class));
            return new ResponseGenericDTO("Usuario creado con exito", true, Integer.toString(HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseGenericDTO(null, false, Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseGenericDTO update(UserDetailDto user) {
        try {
            if (userRepository.findById(user.getId()).isPresent()) {
                user.setPassword(bcryptEncoder.encode(user.getPassword()));
                userRepository.save(MapperUtil.convertToEntity(user, User.class));
            } else {
                return new ResponseGenericDTO("El usuario no existe", false, Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseGenericDTO("Usuario actualizado con exito", true, Integer.toString(HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseGenericDTO(null, false, Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseGenericDTO getUsers(long id) {
        try {
            List<UserDto> dto = new ArrayList<>();
            List<User> listResult = null;

            if (id == 0) {
                listResult = userRepository.findAll();
            } else {
                listResult = userRepository.findUserById(id);
            }

            listResult.forEach((user) -> {
                dto.add(MapperUtil.convertToEntity(user, UserDto.class));
            });
            if (dto.isEmpty()) {
                return new ResponseGenericDTO(null, false, "No se encontraron Usuarios", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseGenericDTO(dto, true, Integer.toString(HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseGenericDTO(null, false, Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseGenericDTO deleteUserById(long id) {
        try {
            User existed = userRepository.findById(id).orElseThrow(UserRuntimeException::new);
            userRepository.delete(existed);
            return new ResponseGenericDTO("Usuario eliminado", true, Integer.toString(HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseGenericDTO(null, false, Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}