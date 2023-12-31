package com.example.pj2be.service.commentservice;


import com.example.pj2be.domain.comment.CommentDTO;
import com.example.pj2be.mapper.commentmapper.CommentMapper;
import com.example.pj2be.mapper.commentmapper.ReplyCommentMapper;
import com.example.pj2be.mapper.likemapper.CommentLikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper mapper;
    private final ReplyCommentMapper replyCommentMapper;
    private final CommentLikeMapper commentLikeMapper;


    public void commentAdd(CommentDTO commentDTO, String member_id) {
        System.out.println("@@@@@@ 댓글 추가한 member_id = " + commentDTO.getMember_id() + "  게시판 :" + commentDTO.getBoard_id()+ "  @@@@@@") ;

        mapper.commentInsert(commentDTO);
    }


    public List<CommentDTO> commentList(Integer board_id, String member_id, CommentDTO commentDTO) {

            return mapper.commentSelectByBoard_id(board_id, member_id, commentDTO);

    }


    public void commentRemove(Integer comment_id) {
        System.out.println("@@@@@@@@" + comment_id + "번 댓글 삭제 @@@@@@@");

        // 대댓글 레코드 먼저 지우기
        replyCommentMapper.DeleteByCommentId(comment_id);

        // 댓글 좋아요 레코드 지우기
        commentLikeMapper.DeleteByCommentId(comment_id);

        mapper.commentDeleteById(comment_id);
    }

    public boolean commentUpdate(CommentDTO commentDTO) {
        System.out.println("@@@@@@@" + commentDTO.getId() + "번 댓글 수정 @@@@@@@@");
        return mapper.commentUpdate(commentDTO) == 1;
    }

    //    ================== 투표 게시판 댓글 ===================
    public void voteCommentAdd(CommentDTO commentDTO, String memberId) {

        System.out.println("@@@@@@ 댓글 추가한 member_id = " + commentDTO.getMember_id() + "  게시판 :" + commentDTO.getBoard_id()+ "  @@@@@@") ;

        commentDTO.setCode("C008");
        mapper.VoteCommentInsert(commentDTO);
    }

    public List<CommentDTO> VoteCommentList(CommentDTO commentDTO) {


            return mapper.voteCommentSelectByBoard_id(commentDTO);

    }
}
