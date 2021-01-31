package business.game;

import api.model.BattleParams;
import api.model.Score;
import business.map.MapInstance;
import data.dao.GameDao;
import data.dao.TankDao;
import org.apache.log4j.Logger;

/**
 * Class that plays a single game and stores that information in the MongoDB instance.
 * This class should only contain logic pertaining to gameplay and how a game is run.
 *
 * @author seanm
 */
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
        int turn = 0;
        while (tank1.getCurHealth() > 0 && tank2.getCurHealth() > 0) {
            doTurn();
            turn++;
            LOGGER.info("Tank 1 health: " + tank1.getCurHealth() +
                    " Tank 2 health: " + tank2.getCurHealth() + " on turn " + turn);
        }

        //Set score
        Score score = new Score();
        score.setTank1Points(tank1.getCurHealth());
        score.setTank2Points(tank2.getCurHealth());

        //Set winner
        if (tank1.getCurHealth() > tank2.getCurHealth()) {
            score.setWinner(tank1.getTankDef().getName());
        } else if (tank2.getCurHealth() > tank1.getCurHealth()) {
            score.setWinner(tank2.getTankDef().getName());
        } else {
            score.setWinner("TIE");
        }

        //Store game
        GameDao gameDao = new GameDao();
        String gameId = gameDao.storeGameSession(battleParams, score);

        return gameId;
    }

    private void setupSession(BattleParams battleParams) {
        mapId = battleParams.getMapId();

        if (battleParams.getTankIds().size() != 2) {
            LOGGER.error("Interface only supports 2 tanks. Tanks given: " + battleParams.getTankIds().size());
            System.exit(1);
        }
        tankId1 = battleParams.getTankIds().get(0);
        tankId2 = battleParams.getTankIds().get(1);

        TankDao tankDao = new TankDao();
        tank1 = new TankAi(tankDao.getTankForId(tankId1));
        tank2 = new TankAi(tankDao.getTankForId(tankId2));

        map = new MapInstance();
        map = map.loadMap(mapId);

        tank1.setCurPos(map.getStartPointA());
        tank2.setCurPos(map.getStartPointB());
    }

    private void doTurn() {
        tank1.doAction(map, tank2);

        if (tank2.getCurHealth() == 0) {
            return;
        } else {
            tank2.doAction(map, tank1);
        }

    }
}
