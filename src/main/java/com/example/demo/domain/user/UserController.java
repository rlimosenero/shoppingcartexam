package com.example.demo.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody UserInfo userInfo) {
        log.info("postUser is called");

        Map<String, Object> map = new LinkedHashMap<>();

        try {
//            if (!userService.checkUserId(userInfo.getPk())) {
//                map.put("status", "Fail");
//                map.put("message", "Record Saving failed!");
//
//                throw new ArrayIndexOutOfBoundsException();
//            }
            userService.addUser(userInfo);

        } catch (ArrayIndexOutOfBoundsException ex) {
            log.error("There is an error inserting data");
            return new ResponseEntity<>(
                    new UserResponseDTO("Fail", "There is an error  adding user!"), HttpStatus.BAD_REQUEST);

        }
        log.info("Successfully Inserted");
        return new ResponseEntity<>(new UserResponseDTO("Success", "User successfully inserted!"), HttpStatus.CREATED);

    }


    @GetMapping()
    public List<UserInfo> getUsers(){
        return userService.getAllUser();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUsers(){
         userService.deleteAllUsers();
         return new ResponseEntity<>(
                 new UserResponseDTO("Success", "All users deleted!"), HttpStatus.OK);
    }
}
