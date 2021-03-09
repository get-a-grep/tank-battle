package business.graph;

import api.model.Position;

import java.util.ArrayList;

public class Calculate {

    public static ArrayList<Position> calculateArea(boolean xLimited, Position center) {
        ArrayList<Position> surroundingPos = new ArrayList<>();
        business.graph.Position centerPos = new business.graph.Position(center);

        if (xLimited) {
            //Row 1
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateRight()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateRight().modulateRight()));

            //Row 2
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateUp()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateUp().modulateRight()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateUp().modulateRight().modulateRight()));

            //Middle Row
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateRight()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateRight().modulateRight()));

            //Row 4
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateRight()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateRight().modulateRight()));

            //Row 5
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateDown()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateDown().modulateRight()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateDown().modulateRight().modulateRight()));
        } else {
            //Row 1
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateLeft()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateLeft().modulateLeft()));

            //Row 2
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateUp()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateUp().modulateLeft()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateUp().modulateUp().modulateLeft().modulateLeft()));

            //Middle Row
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateLeft()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateLeft().modulateLeft()));

            //Row 3
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateLeft()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateLeft().modulateLeft()));

            //Row 4
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateDown()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateDown().modulateLeft()));
            surroundingPos.add(business.graph.Position.toModel(centerPos.modulateDown().modulateDown().modulateLeft().modulateLeft()));

        }

        return surroundingPos;
    }
}
