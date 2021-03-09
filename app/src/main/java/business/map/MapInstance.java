package business.map;

import api.model.Map;
import api.model.Obstacle;
import api.model.Position;
import business.graph.Calculate;
import data.dao.MapDao;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The instance of a map to be played on. Currently a WIP as it will only have a use after
 * implementation of generateMap().
 *
 * @author mads
 */
@Data
public class MapInstance {
    private MapDao mapDao;

    private String id;

    private Position startPointA;
    private Position startPointB;

    private int fieldX;
    private int fieldY;

    private ArrayList<Obstacle> obstacles;
    private ArrayList<Position> invalidPositions;

    public MapInstance loadMap(String id) {
        this.id = id;
        MapDao mapDao = new MapDao();
        Map map = mapDao.getMapForId(id);
        fillMapInstance(map);

        return this;
    }

    public MapInstance generateMap(Difficulty difficulty) {
        if (difficulty == null) {
            difficulty = Difficulty.NORMAL;
        }
        mapDao = new MapDao();

        fieldX = difficulty.getMaxX();
        fieldY = difficulty.getMaxY();
        startPointA = findStartPos();
        startPointB = findStartPos();
        obstacles = generateObstacles(difficulty);

        id = mapDao.storeNewMap(this);

        return this;
    }

    private Position findStartPos() {
        Position retPos = new Position();
        Random random = new Random();
        int randomness = random.nextInt(fieldY) + 1;
        retPos.setY(randomness);
        //Set x opposite one another
        if (startPointA == null) {
            retPos.setX(1);
        } else {
            retPos.setX(fieldX);
        }
        return retPos;
    }

    private ArrayList<Obstacle> generateObstacles(Difficulty difficulty) {
        int i = difficulty.getObstacles();
        ArrayList<Obstacle> obsList = new ArrayList<>();

        genStartingAreas();

        while(i > 0) {
            obsList.add(genObstacle(difficulty, invalidPositions));
            i--;
        }
        return obsList;
    }

    private void fillMapInstance(Map map) {
        id = map.getId();
        startPointA = map.getStartPointA();
        startPointB = map.getStartPointB();
        fieldX = map.getFieldX();
        fieldY = map.getFieldY();
        obstacles = map.getObstacles();
    }

    private Obstacle genObstacle(Difficulty difficulty, ArrayList<Position> invalidPositions) {
        Obstacle obs = new Obstacle();

        int x = difficulty.getMaxX();
        int y = difficulty.getMaxY();

        Random random = new Random();

        int blockDecision = random.nextInt(2);

        int xDecision = random.nextInt(x) + 1;
        int yDecision = random.nextInt(y) + 1;

        for (Position pos : invalidPositions) {
            while (xDecision == pos.getX() || yDecision == pos.getY()) {
                xDecision = random.nextInt(x) + 1;
                yDecision = random.nextInt(y) + 1;
            }
        }

        Position obsPos = new Position();
        obsPos.setX(xDecision);
        obsPos.setY(yDecision);

        obs.setPosition(obsPos);

        switch (blockDecision) {
            case 1:
                obs.setBlocksMove(true);
                break;
            case 2:
                obs.setBlocksMove(false);
                break;
            default:
                obs.setBlocksMove(true);
        }

        return obs;
    }

    private void genStartingAreas() {
        ArrayList<Position> retPos = new ArrayList<>();
        retPos.add(startPointA);
        retPos.add(startPointB);

        retPos.addAll(Calculate.calculateArea(true, startPointA));
        retPos.addAll(Calculate.calculateArea(false, startPointB));

        invalidPositions = retPos;
    }
}
