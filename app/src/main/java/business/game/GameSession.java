package business.game;

import api.model.BattleParams;
import api.model.Score;
import business.map.MapInstance;
import data.dao.GameDao;
import data.dao.TankDao;
import org.apache.log4j.Logger;

public class GameSession {
    private static final Logger LOGGER = Logger.getLogger(GameSession.class);

    private String tankId1;
    private String tankId2;
    private String mapId;

    private MapInstance map;
    private TankAi tank1;
    private TankAi tank2;

    public String playSession(BattleParams battleParams) {
        //Setup for game
        setupSession(battleParams);

        //Play game
        while (tank1.getCurHealth() > 0 && tank2.getCurHealth() > 0) {
            doTurn();
        }

        //Set score
        Score score = new Score();
        score.setTank1Points(5 - tank2.getCurHealth());
        score.setTank2Points(5 - tank1.getCurHealth());

        //Store game
        GameDao gameDao = new GameDao();
        String gameId = gameDao.storeGameSession(battleParams, score);

        return gameId;
    }

    private void setupSession(BattleParams battleParams) {
        mapId = battleParams.getMapId();

        if (battleParams.getTankIds().size() != 2) {
            LOGGER.error("Interface only supports 2 tanks. Tanks given: " + battleParams.getTankIds().size());
        }
        tankId1 = battleParams.getTankIds().get(0);
        tankId2 = battleParams.getTankIds().get(1);

        TankDao tankDao = new TankDao();
        tank1 = new TankAi(tankDao.getTankForId(tankId1));
        tank2 = new TankAi(tankDao.getTankForId(tankId2));

        map = map.loadMap(mapId);

        tank1.setCurPos(map.getStartPointA());
        tank2.setCurPos(map.getStartPointB());
    }

    private void doTurn() {
        tank1.doAction(map, tank2);
        tank2.doAction(map, tank1);
    }
}
