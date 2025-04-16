package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.PostReactionDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PostReactionRepository {

    private final SqlSessionTemplate sql;

    public void insert(PostReactionDto dto) {
        sql.insert("postReactionMapper.insertPostReaction", dto);
    }

    public void delete(PostReactionDto dto) {
        sql.delete("postReactionMapper.deletePostReaction", dto);
    }

    public PostReactionDto findByMember(Long postId, Long memberId) {
        Map<String, Object> param = new HashMap<>();
        param.put("postId", postId);
        param.put("memberId", memberId);
        return sql.selectOne("postReactionMapper.findPostReactionByMember", param);
    }

    public Map<String, Integer> countReactions(Long postId) {
        List<Map<String, Object>> resultList = sql.selectList("postReactionMapper.countPostReactions", postId);
        Map<String, Integer> result = new HashMap<>();
        for (Map<String, Object> row : resultList) {
            result.put((String) row.get("reaction_type"), ((Long) row.get("count")).intValue());
        }
        return result;
    }
}
