package com.polpid.springbootbatch.repository;

import com.polpid.springbootbatch.domain.User;
import com.polpid.springbootbatch.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUpdatedDateBeforeAndStatusEquals(LocalDateTime localDateTime, UserStatus active);
}
