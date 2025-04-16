package idusw.soccerworld.repository;


import idusw.soccerworld.domain.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private  final SqlSessionTemplate sql;

    public List<PostDto> selectedCategory(Long categoryId) {
        return sql.selectList("postMapper.selectedCategory", categoryId);
    }

    public void create(PostDto postDTO) {
        sql.insert("postMapper.create", postDTO);
    }

    public void updateViewCount(Long postId) {
        sql.update("postMapper.updateViewCount", postId);
    }

    public PostDto findById(Long postId) {
        return sql.selectOne("postMapper.findById", postId);
    }

    public void updatePost(PostDto postDTO, Long postId) {
        postDTO.setPostId(postId);
        sql.update("postMapper.updatePost", postDTO);
    }

    public void deletePost(Long postId) {
        sql.delete("postMapper.deletePost", postId);
    }


    public void incrementUpCount(Long postId, int diff) {
        PostDto postDto = new PostDto();
        postDto.setPostId(postId);
        postDto.setUpCount(diff);
        sql.update("postMapper.incrementUpCount", postDto);
    }

    public void incrementDownCount(Long postId, int diff) {
        PostDto postDto = new PostDto();
        postDto.setPostId(postId);
        postDto.setDownCount(diff);
        sql.update("postMapper.incrementDownCount", postDto);
    }

    public void updatePicturePaths(Long postId, String picture) {
        Map<String,Object> param = new HashMap<>();
        param.put("postId", postId);
        param.put("picture", picture);
        sql.update("postMapper.updatePicturePaths", param);
    }

    public List<PostDto> findPagedByCategory(Long categoryId, int offset, int pageSize) {
        Map<String,Object> params = new HashMap<>();
        params.put("categoryId", categoryId);
        params.put("offset",     offset);
        params.put("pageSize",   pageSize);
        return sql.selectList("postMapper.findPagedByCategory", params);
    }

    public int countByCategory(Long categoryId) {
        return sql.selectOne("postMapper.countByCategory", categoryId);
    }

    public List<PostDto> searchByTitle(Long categoryId, String q, int offset, int size) {
        Map<String,Object> params = Map.of(
                "categoryId", categoryId,
                "q",          q,
                "offset",     offset,
                "pageSize",   size
        );
        return sql.selectList("postMapper.searchByTitle", params);
    }
    public int countByTitle(Long categoryId, String q) {
        return sql.selectOne("postMapper.countByTitle", Map.of("categoryId", categoryId, "q", q));
    }

    public List<PostDto> searchByAuthor(Long categoryId, String q, int offset, int size) {
        Map<String,Object> params = Map.of(
                "categoryId", categoryId,
                "q",          q,
                "offset",     offset,
                "pageSize",   size
        );
        return sql.selectList("postMapper.searchByAuthor", params);
    }
    public int countByAuthor(Long categoryId, String q) {
        return sql.selectOne("postMapper.countByAuthor", Map.of("categoryId", categoryId, "q", q));
    }

}