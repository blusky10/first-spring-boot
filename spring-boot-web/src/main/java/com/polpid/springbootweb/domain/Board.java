package com.polpid.springbootweb.domain;

import com.polpid.springbootweb.domain.enums.BoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table
@Entity
@Getter
@NoArgsConstructor
public class Board implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String title;
    @Column
    private String subTitle;
    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Column
    private LocalDateTime createDate;
    @Column
    private LocalDateTime updateDate;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Board(String title, String subTitle, String content, BoardType boardType, LocalDateTime createDate, LocalDateTime updateDate, User user) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardType = boardType;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.user = user;
    }
}
