package com.example.demo.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByUsername(String username);

    Optional<UserInfo> findByPk(Integer pk);

    void deleteByRoles(String roles);

    List<UserInfo> findAllByRoles(String roles);
}
