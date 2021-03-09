package api.model;

import business.map.Difficulty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class BattleParams {
    private ArrayList<String> tankIds;
    private String mapId;
    private Difficulty difficulty;

    public BattleParams() {
        super();
    }
}
