package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.TeamDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamRepository {
    SqlSessionTemplate sessionTemplate;

    public TeamRepository(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public int insertTeams(List teamList) {
        return sessionTemplate.insert("TeamMapper.insertTeams", teamList);
    }

    public int insertTeam(List<TeamDto> teamDtoList){
        int result = sessionTemplate.insert("TeamMapper.insertTeam",teamDtoList);
        return result;
    }

    public List<TeamDto> selectAll() {
        return sessionTemplate.selectList("TeamMapper.selectAll");
    }

    public TeamDto selectOneByPk(Long teamId) {
        return sessionTemplate.selectOne("TeamMapper.selectOneByPk", teamId);
    }
}
