package api.model;

import lombok.Data;

@Data
public class Game {
    private String id;
    private BattleParams battleParams;
    private Score score;
}
