package data.converter;

import api.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Helper class to convert Objects received from the DB into model objects that can
 * be returned to the client.
 *
 * @author mads
 */
public class DBObjConverter {
    private static final Logger LOGGER = Logger.getLogger(DBObjConverter.class);

    public static Tank tankFromDbObj(final DBObject dbObject) {
        //Read fields into HashMap for parsing
        Set<String> allKeys = dbObject.keySet();
        HashMap<String, String> fields = new HashMap();
        for (String key: allKeys) {
            fields.put(key, dbObject.get(key).toString());
        }

        Tank tank = new Tank(fields.get("id"));

        tank.setName(fields.get("name"));
        tank.setTread(parseDamage(fields.get("tread")));

        tank.setHealth((int)Float.parseFloat(fields.get("health")));

        try {
            ObjectMapper mapper = new ObjectMapper();

            Position pos = mapper.readValue(fields.get("position"), Position.class);
            tank.setPosition(pos);

        } catch (IOException ioe) {
            LOGGER.warn("Could not deserialize tank db-object.", ioe);
        }
        return tank;
    }

    public static Map mapFromDbObj(final DBObject dbObject) {
        //Read fields into HashMap for parsing
        Set<String> allKeys = dbObject.keySet();
        HashMap<String, String> fields = new HashMap();
        for (String key: allKeys) {
            fields.put(key, dbObject.get(key).toString());
        }

        Map map = new Map(fields.get("id"));

        //Parse as float due to how Mongo stores integers and cast to int
        map.setFieldX((int)Float.parseFloat(fields.get("fieldX")));
        map.setFieldY((int)Float.parseFloat(fields.get("fieldY")));

        try {
            ObjectMapper mapper = new ObjectMapper();

            Obstacle[] obstacles = mapper.readValue(fields.get("obstacles"), Obstacle[].class);
            map.setObstacles(new ArrayList<>(Arrays.asList(obstacles)));

            Position startPointA = mapper.readValue(fields.get("startPointA"), Position.class);
            map.setStartPointA(startPointA);

            Position startPointB = mapper.readValue(fields.get("startPointB"), Position.class);
            map.setStartPointB(startPointB);
        } catch (IOException ioe) {
            LOGGER.warn("Could not deserialize map db-object.", ioe);
        }
        return map;
    }

    public static Game gameFromDbObj(final DBObject dbObject) {
        //Read fields into HashMap for parsing
        Set<String> allKeys = dbObject.keySet();
        HashMap<String, String> fields = new HashMap();
        for (String key: allKeys) {
            fields.put(key, dbObject.get(key).toString());
        }

        Game game = new Game();
        game.setId(fields.get("id"));

        try {
            BattleParams params = new BattleParams();
            ObjectMapper mapper = new ObjectMapper();

            ArrayList<String> tankIds = mapper.readValue(fields.get("tankIds"), ArrayList.class);
            params.setTankIds(tankIds);
            params.setMapId(fields.get("mapId"));

            Score score = new Score();
            score.setTank1Points((int)Float.parseFloat(fields.get("scoreT1")));
            score.setTank2Points((int)Float.parseFloat(fields.get("scoreT2")));
            score.setWinner(fields.get("winner"));

            game.setBattleParams(params);
            game.setScore(score);
        } catch (IOException ioe) {
            LOGGER.warn("Could not deserialize game db-object.", ioe);
        }

        return game;
    }

    private static Damage parseDamage(final String dmgStr) {
        if (dmgStr == Damage.STABLE.toString()) {
            return Damage.STABLE;
        } else if (dmgStr == Damage.LOW.toString()) {
            return Damage.LOW;
        } else if (dmgStr == Damage.MODERATE.toString()) {
            return Damage.MODERATE;
        } else if (dmgStr == Damage.SEVERE.toString()) {
            return Damage.SEVERE;
        } else {
            return Damage.STABLE;
        }
    }
}
