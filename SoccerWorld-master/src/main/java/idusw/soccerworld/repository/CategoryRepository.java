package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
    private final SqlSessionTemplate sql;

    public List<CategoryDto> findAll() {
        return sql.selectList("CategoryMapper.findAll");
    }
}