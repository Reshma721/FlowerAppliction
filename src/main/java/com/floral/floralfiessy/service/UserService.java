package com.floral.floralfiessy.service;

import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.dto.UserDto;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public User dtoToEntity(UserDto user){
        User userEntity=new User();
        BeanUtils.copyProperties(user,userEntity);
        return userEntity;
    }

    public UserDto entityToDto(User userEntity){
        UserDto userDto=new UserDto();
        BeanUtils.copyProperties(userEntity,userDto);
        return userDto;
    }
    public List<UserDto> getAllUserDetails() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::entityToDto).collect(Collectors.toList());
    }
//    public List<UserDto> getAllUserDetails() {
//        List<UserDto> user=new ArrayList<>();
//        for(User busDetailsEntity: loginRepository.findAll()){
//            user.add(entityToDto(busDetailsEntity));
//        }
//        return user;
//    }
public UserDto getUserDetailsById(Long id) throws UserNotFoundException {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    return entityToDto(user);
}
//    public UserDto getUserDetailsById(long id) throws UserNotFoundException {
//        User user = loginRepository.findById(id).orElse(null);
//        if (user != null) {
//            return entityToDto(user);
//        } else
//            throw new UserNotFoundException("UserId Not Found");
//
//    }
public UserDto saveUserDetails(UserDto userDto) throws UserNotFoundException {
    User user = dtoToEntity(userDto);
    User savedUser = userRepository.save(user);
    return entityToDto(savedUser);
}

//    public UserDto saveUserDetails(UserDto userDetails) {
//        User user=dtoToEntity(userDetails);
//        return entityToDto(loginRepository.save(user));
//    }

    public UserDto create(UserDto userDetails) {
        return saveUserDetails(userDetails);
    }

    public void deleteUserDetails(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }
//    public void deleteUserDetails(long id) throws UserNotFoundException {
//        User user = loginRepository.findById(id).orElse(null);
//        if (user != null) {
//            loginRepository.deleteById(id);
//        } else
//            throw new UserNotFoundException("UserId not found");
//    }
}
