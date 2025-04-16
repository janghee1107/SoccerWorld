package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.CommentDto;
import idusw.soccerworld.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void addComment(CommentDto commentDto) {
        commentRepository.addComment(commentDto);
    }

    public List<CommentDto> getCommentsByPost(Long postId) {
        return  commentRepository.getCommentsByPost(postId);
    }

    public void updateComment(CommentDto commentDto, Long commentId) {
        commentRepository.updateComment(commentDto, commentId);
    }

    public void deleteComment(Long commentId) { commentRepository.deleteComment(commentId);
    }
}
