package api.model;

import lombok.Data;

@Data
public class Tank {
    private String id;
    private String name;

    private Damage tread;

    private int health;

    private Position position;

    public Tank(String id) {
        this.id = id;
    }
}
