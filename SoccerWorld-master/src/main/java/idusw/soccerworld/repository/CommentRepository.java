package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final SqlSessionTemplate sql;

    public void addComment(CommentDto commentDto) { sql.insert("commentMapper.addComment", commentDto);}

    public List<CommentDto> getCommentsByPost(Long postId) {
        return sql.selectList("commentMapper.getCommentsByPost", postId);
    }

    public void updateComment(CommentDto commentDto, Long commentId) {
        commentDto.setCommentId(commentId);
        sql.update("commentMapper.updateComment", commentDto);
    }

    public void deleteComment(Long commentId) {
        sql.delete("commentMapper.deleteComment", commentId);
    }

    public void incrementUpCount(Long commentId, int diff) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(commentId);
        commentDto.setUpCount(diff);
        sql.update("commentMapper.incrementUpCount", commentDto);
    }

    public void incrementDownCount(Long commentId, int diff) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(commentId);
        commentDto.setDownCount(diff);
        sql.update("commentMapper.incrementDownCount", commentDto);
    }
}
