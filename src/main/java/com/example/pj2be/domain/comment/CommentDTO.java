package com.example.pj2be.domain.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Integer id; // PK
    private Integer board_id;
    private String member_id;
    private String comment;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private String nickname;
}