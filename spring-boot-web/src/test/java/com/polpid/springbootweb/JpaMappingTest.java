package com.polpid.springbootweb;

import com.polpid.springbootweb.domain.Board;
import com.polpid.springbootweb.domain.User;
import com.polpid.springbootweb.domain.enums.BoardType;
import com.polpid.springbootweb.repository.BoardRepository;
import com.polpid.springbootweb.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaMappingTest {

    private final String boardTitle = "TEST";
    private final String email = "test@email.com";

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Before
    public void init(){
        User user = userRepository.save(User.builder()
                    .name("winter")
                    .password("test")
                    .email(email)
                    .createdDate(LocalDateTime.now())
                    .build()
        );

        boardRepository.save(Board.builder()
                .title(boardTitle)
                .subTitle("SubTitle")
                .content("contents")
                .boardType(BoardType.free)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .user(user)
                .build()
        );
    }

    @Test
    public void 생성_테스트(){
        User user = userRepository.findByEmail(email);

        assertThat(user.getName(), is("winter"));
        assertThat(user.getPassword(), is("test"));

        Board board = boardRepository.findByUser(user);

        assertThat(board.getTitle(), is(boardTitle));
    }

}
