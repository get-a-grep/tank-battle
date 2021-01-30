package api.model;

import lombok.Data;

@Data
public class Obstacle {

    private boolean blocksMove;
    private int damage;

    private Position position;
}
