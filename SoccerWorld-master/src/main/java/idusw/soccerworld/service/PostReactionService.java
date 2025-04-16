package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.PostReactionDto;
import idusw.soccerworld.repository.PostReactionRepository;
import idusw.soccerworld.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostReactionService {

    private final PostReactionRepository postReactionRepository;
    private final PostRepository postRepository;

    public void toggleReaction(PostReactionDto dto) {
        Long postId = dto.getPostDto().getPostId();
        Long memberId = dto.getMemberDto().getMemberId();

        PostReactionDto existing = postReactionRepository.findByMember(postId, memberId);

        if (existing != null && existing.getReactionType().equals(dto.getReactionType())) {
            // 같은 반응 -> toggle off
            postReactionRepository.delete(dto);
            updateCount(postId, dto.getReactionType(), -1);
        } else {
            if (existing != null) {
                // 다른 반응이 있으면 삭제 후 count down
                updateCount(postId, existing.getReactionType(), -1);
                postReactionRepository.delete(existing);
            }
            postReactionRepository.insert(dto); // 새 반응 추가
            updateCount(postId, dto.getReactionType(), 1);
        }
    }

    private void updateCount(Long postId, String reactionType, int diff) {
        if ("UP".equals(reactionType)) {
            postRepository.incrementUpCount(postId, diff);
        } else if ("DOWN".equals(reactionType)) {
            postRepository.incrementDownCount(postId, diff);
        }
    }

    //  좋아요 및 싫어요 개수 조회
    public Map<String, Integer> getReactionCounts(Long postId) {
        return postReactionRepository.countReactions(postId);
    }

    public PostReactionDto findByMember(Long postId, Long memberId) {
        return postReactionRepository.findByMember(postId, memberId);
    }
}
