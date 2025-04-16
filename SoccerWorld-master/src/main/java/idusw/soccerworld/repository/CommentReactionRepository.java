package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.CommentReactionDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CommentReactionRepository {

    private final SqlSessionTemplate sql;

    public void insert(CommentReactionDto dto) {
        sql.insert("commentReactionMapper.insertCommentReaction", dto);
    }

    public void delete(CommentReactionDto dto) {
        sql.delete("commentReactionMapper.deleteCommentReaction", dto);
    }

    public CommentReactionDto findByMember(Long commentId, Long memberId) {
        Map<String, Object> param = new HashMap<>();
        param.put("commentId", commentId);
        param.put("memberId", memberId);
        return sql.selectOne("commentReactionMapper.findCommentReactionByMember", param);
    }

    public Map<String, Integer> countReactions(Long commentId) {
        List<Map<String, Object>> resultList = sql.selectList("commentReactionMapper.countCommentReactions", commentId);
        Map<String, Integer> result = new HashMap<>();
        for (Map<String, Object> row : resultList) {
            result.put((String) row.get("reaction_type"), ((Long) row.get("count")).intValue());
        }
        return result;
    }
}
