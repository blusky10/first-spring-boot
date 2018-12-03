package com.polpid.springbootweb.repository;

import com.polpid.springbootweb.domain.Board;
import com.polpid.springbootweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByUser(User user);
}
