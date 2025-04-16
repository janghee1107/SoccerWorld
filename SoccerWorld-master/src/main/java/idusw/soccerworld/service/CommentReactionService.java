package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.CommentDto;
import idusw.soccerworld.domain.dto.CommentReactionDto;
import idusw.soccerworld.repository.CommentReactionRepository;
import idusw.soccerworld.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentReactionService {

    private final CommentReactionRepository commentReactionRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    public void toggleReaction(CommentReactionDto dto) {
        Long commentId = dto.getCommentDto().getCommentId();
        Long memberId = dto.getMemberDto().getMemberId();

        CommentReactionDto existing = commentReactionRepository.findByMember(commentId, memberId);

        if (existing != null && existing.getReactionType().equals(dto.getReactionType())) {
            // 같은 반응 -> toggle off
            commentReactionRepository.delete(dto);
            updateCount(commentId, dto.getReactionType(), -1);
        } else {
            if (existing != null) {
                // 다른 반응이 있으면 삭제 후 count down
                updateCount(commentId, existing.getReactionType(), -1);
                commentReactionRepository.delete(existing);
            }
            commentReactionRepository.insert(dto); // 새 반응 추가
            updateCount(commentId, dto.getReactionType(), 1);
        }
    }

    private void updateCount(Long commentId, String reactionType, int diff) {
        if ("UP".equals(reactionType)) {
            commentRepository.incrementUpCount(commentId, diff);
        } else if ("DOWN".equals(reactionType)) {
            commentRepository.incrementDownCount(commentId, diff);
        }
    }

    //  좋아요 및 싫어요 개수 조회
    public Map<String, Integer> getReactionCounts(Long commentId) {
        return commentReactionRepository.countReactions(commentId);
    }

    public CommentReactionDto findByMember(Long commentId, Long memberId) {
        return commentReactionRepository.findByMember(commentId, memberId);
    }

    public Map<Long, String> findUserReactionsForPostComments(Long postId, Long memberId) {
        if (memberId == null) return Collections.emptyMap();
        List<CommentDto> comments = commentService.getCommentsByPost(postId);
        Map<Long, String> result = new HashMap<>();
        for (CommentDto c : comments) {
            CommentReactionDto cr = commentReactionRepository.findByMember(c.getCommentId(), memberId);
            result.put(c.getCommentId(), cr != null ? cr.getReactionType() : null);
        }
        return result;
    }
}
