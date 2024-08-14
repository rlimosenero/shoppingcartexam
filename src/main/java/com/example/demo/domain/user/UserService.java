package com.example.demo.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return UserResponseDTO.builder().status("Success").message("test").build();
    }

    public boolean checkUserId(int pk) {
        return userInfoRepository.findById(pk).isPresent();
    }

    public List<UserInfo> getAllUser() {return userInfoRepository.findAll(); }
    public void deleteAllUsers() {userInfoRepository.deleteAll();}
    //ROLES
    public List<UserInfo> getAllUserByRole() {return userInfoRepository.findAllByRoles("ROLE_USER"); }
    public void deleteAllUsersByRole(String roles) {userInfoRepository.deleteByRoles(roles); }


    public boolean checkPk(Integer pk) {
        return userInfoRepository.findByPk(pk).isPresent();
    }

    public boolean checkUsername(String username) {
        return userInfoRepository.findByUsername(username).isPresent();
    }
}
