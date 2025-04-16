package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.CategoryDto;
import idusw.soccerworld.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll();
    }
}
