package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.PostDto;
import idusw.soccerworld.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<PostDto> selectedCategory(Long categoryId) {
        return postRepository.selectedCategory(categoryId);
    }

    public void create(PostDto postDTO) {
        postRepository.create(postDTO);
    }

    public void updateViewCount(Long postId) {
        postRepository.updateViewCount(postId);
    }

    public PostDto findById(Long postId) {
        return postRepository.findById(postId);
    }

    public void updatePost(PostDto postDTO, Long postId) {
        postRepository.updatePost(postDTO, postId);
    }

    public void deletePost(Long postId) {
        postRepository.deletePost(postId);
    }

    public void updatePicturePaths(Long postId, String picture) {
        postRepository.updatePicturePaths(postId, picture);
    }

    public List<PostDto> findPagedByCategory(Long categoryId, int offset, int pageSize) {
        return postRepository.findPagedByCategory(categoryId, offset, pageSize);
    }

    public int countByCategory(Long categoryId) {
        return postRepository.countByCategory(categoryId);
    }

    public List<PostDto> searchByTitle(Long categoryId, String q, int offset, int size) {
        return postRepository.searchByTitle(categoryId, q, offset, size);
    }
    public int countByTitle(Long categoryId, String q) {
        return postRepository.countByTitle(categoryId, q);
    }

    public List<PostDto> searchByAuthor(Long categoryId, String q, int offset, int size) {
        return postRepository.searchByAuthor(categoryId, q, offset, size);
    }
    public int countByAuthor(Long categoryId, String q) {
        return postRepository.countByAuthor(categoryId, q);
    }

}