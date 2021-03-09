package business.graph;

public class Position {

    private int x;
    private int y;

    public Position(api.model.Position position) {
        x = position.getX();
        y = position.getY();
    }

    public Position modulateUp () {
        x++;
        return this;
    }

    public Position modulateDown () {
        x--;
        return this;
    }

    public Position modulateLeft () {
        y++;
        return this;
    }

    public Position modulateRight () {
        y--;
        return this;
    }

    public static api.model.Position toModel(Position position) {
        api.model.Position retPos = new api.model.Position();

        retPos.setX(position.x);
        retPos.setY(position.y);

        return retPos;
    }
}
