package com.floral.floralfiessy.controller;

import com.floral.floralfiessy.dto.UserDto;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
        @Autowired
        private UserService userService;

        @PostMapping("/create")
        public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDetails) {
            UserDto createdUser = userService.create(userDetails);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }

        @GetMapping("/getAll")
        public ResponseEntity<List<UserDto>> getAllUsers() {
            return new ResponseEntity<>(userService.getAllUserDetails(), HttpStatus.OK);
        }

    @GetMapping("/getById/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        try {
            UserDto userDto = userService.getUserDetailsById(id);
            return ResponseEntity.ok(userDto);
        } catch (
                UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

//    @GetMapping("/getById/{id}")
//        public ResponseEntity<UserDto> getUserById(@PathVariable long id) throws UserNotFoundException {
//            return new ResponseEntity<>(userService.getUserDetailsById(id), HttpStatus.OK);
//        }

//        @PutMapping("/update/{id}")
//        public ResponseEntity<UserDto> updateUser(@PathVariable long id, @RequestBody @Valid UserDto userDetails) throws UserNotFoundException {
//            userService.getUserDetailsById(id); // Verify user exists
//            userDetails.setId(id); // Set the ID to ensure the correct user is updated
//            return new ResponseEntity<>(userService.saveUserDetails(userDetails), HttpStatus.ACCEPTED);
//        }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = userService.saveUserDetails(userDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUserDetails(id);
            return ResponseEntity.ok("Deleted user " + id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

//        @DeleteMapping("/delete/{id}")
//        public ResponseEntity<String> deleteUser(@PathVariable long id) throws UserNotFoundException {
//            userService.deleteUserDetails(id);
//            return ResponseEntity.ok("Deleted user " + id);
//        }
    }

