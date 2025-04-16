package idusw.soccerworld.repository;

import idusw.soccerworld.domain.dto.GameDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameRepository {
    SqlSessionTemplate sessionTemplate;

    public GameRepository(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public List<GameDto> selectAll() {
        return sessionTemplate.selectList("GameName.selectAll");
    }

    public List<GameDto> selectByDate(GameDto gameDto){
        return sessionTemplate.selectList("GameName.selectByDate",gameDto);
    }
    public GameDto selectByGameId(int GameId){
        return sessionTemplate.selectOne("GameName.selectByGameId",GameId);
    }

    public int insertGames(List<GameDto> gameDtoList){
        int result = sessionTemplate.insert("GameName.insertGames",gameDtoList);
        return result;
    }

    public List<GameDto> selectMore(GameDto gameDto) {
        return sessionTemplate.selectList("GameName.selectMore", gameDto);
    }

    public List<GameDto> selectByWeek(GameDto gameDto){
        return sessionTemplate.selectList("GameName.selectBytWeek", gameDto);
    }
}
