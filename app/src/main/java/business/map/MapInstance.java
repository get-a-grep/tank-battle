package business.map;

import api.model.Map;
import api.model.Obstacle;
import api.model.Position;
import data.dao.MapDao;
import lombok.Data;

import java.util.ArrayList;

@Data
public class MapInstance {
    private String id;

    private Position startPointA;
    private Position startPointB;

    private int fieldX;
    private int fieldY;

    private ArrayList<Obstacle> obstacles;

    public MapInstance loadMap(String id) {
        this.id = id;
        MapDao mapDao = new MapDao();
        Map map = mapDao.getMapForId(id);
        fillMapInstance(map);

        return this;
    }

    public MapInstance generateMap() {
        //WIP
        return null;
    }

    private void fillMapInstance(Map map) {
        id = map.getId();
        startPointA = map.getStartPointA();
        startPointB = map.getStartPointB();
        fieldX = map.getFieldX();
        fieldY = map.getFieldY();
        obstacles = map.getObstacles();
    }
}
