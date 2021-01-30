package api.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Map {
    private String id;

    private Position startPointA;
    private Position startPointB;

    private int fieldX;
    private int fieldY;

    private ArrayList<Obstacle> obstacles;

    public Map(String id) {
        this.id = id;
    }
}
