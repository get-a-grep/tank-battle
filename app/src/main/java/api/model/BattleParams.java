package api.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class BattleParams {
    private ArrayList<String> tankIds;
    private String mapId;

    public BattleParams() {
        super();
    }
}
