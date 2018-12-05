package com.polpid.springbootweb;

import com.polpid.springbootweb.domain.Board;
import com.polpid.springbootweb.domain.User;
import com.polpid.springbootweb.domain.enums.BoardType;
import com.polpid.springbootweb.repository.BoardRepository;
import com.polpid.springbootweb.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
public class SpringBootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }


    @Bean
    public CommandLineRunner runner(UserRepository userRepository, BoardRepository boardRepository) {
        return (args) -> {
            User user = userRepository.save(User.builder()
                    .name("havi")
                    .password("test")
                    .email("havi@gmail.com")
                    .createdDate(LocalDateTime.now())
                    .build());

            IntStream.rangeClosed(1, 200).forEach(index ->
                    boardRepository.save(Board.builder()
                            .title("게시글"+index)
                            .subTitle("순서"+index)
                            .content("컨텐츠")
                            .boardType(BoardType.free)
                            .createDate(LocalDateTime.now())
                            .updateDate(LocalDateTime.now())
                            .user(user).build())
            );
        };
    }
}
