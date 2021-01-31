package business.game;

import api.model.Obstacle;
import api.model.Position;
import api.model.Tank;
import business.map.MapInstance;
import lombok.Data;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * The class containing the instance of a tank that is defined by a given schema.
 * Also contains all decision logic for movement, attacking, and turning.
 *
 * @author mads
 */
@Data
public class TankAi {
    private static final Logger LOGGER = Logger.getLogger(TankAi.class);

    private Tank tankDef;

    private int curHealth;
    private Position curPos;
    private Direction curDirection;
    private Direction lastDirection;

    /**
     * Constructor that sets health based on tank-schema on startup.
     *
     * @param tank the tank-schema
     */
    public TankAi (Tank tank) {
        this.tankDef = tank;
        curHealth = tank.getHealth();
    }

    /**
     * Method that decides whether the AI should move, turn, or attack.
     *
     * @param map
     *  the MapInstance that is being played on
     * @param enemyTank
     *  the enemy tank that is being played against
     */
    public void doAction(MapInstance map, TankAi enemyTank) {
        ArrayList<Obstacle> obstacles = map.getObstacles();
        ArrayList<Direction> blockedDirections = new ArrayList<>();

        Position enemyPos = enemyTank.curPos;

        boolean enemyOnX = enemyPos.getX() == curPos.getX();
        boolean enemyOnY = enemyPos.getY() == curPos.getY();

        //Find if our Line of Sight to the enemy tank is clear
        boolean clearLOS = false;
        for (Obstacle obstacle : obstacles) {
            int obstacleX = obstacle.getPosition().getX();
            int obstacleY = obstacle.getPosition().getY();

            boolean obstacleOnX = obstacleX == curPos.getX();
            boolean obstacleOnY = obstacleY == curPos.getY();

            if (enemyOnX && obstacleOnX) {
                if (enemyPos.getX() > curPos.getX()) {
                    if (obstacleX < curPos.getX()) {
                        clearLOS = true;
                    } else if (obstacleX > curPos.getX() && !obstacle.isBlocksMove()) {
                        clearLOS = true;
                    }
                } else {
                    if (obstacleX > curPos.getX()) {
                        clearLOS = true;
                    } else if (obstacleX < curPos.getX() && !obstacle.isBlocksMove()) {
                        clearLOS = true;
                    }
                }
            } else if (enemyOnX && !obstacleOnX) {
                clearLOS = true;
            } else if (enemyOnY && obstacleOnY) {
                if (enemyPos.getY() > curPos.getY()) {
                    if (obstacleY < curPos.getY()) {
                        clearLOS = true;
                    } else if (obstacleY > curPos.getY() && !obstacle.isBlocksMove()) {
                        clearLOS = true;
                    }
                } else {
                    if (obstacleY > curPos.getY()) {
                        clearLOS = true;
                    } else if (obstacleY < curPos.getY() && !obstacle.isBlocksMove()) {
                        clearLOS = true;
                    }
                }
            } else if (enemyOnY && !obstacleOnY) {
                clearLOS = true;
            }
            Direction block = findBlock(obstacle);
            if (block != null) {
                blockedDirections.add(block);
            }
        }

        // If the Line of Sight is clear and we are facing the enemy,
        // attack. Else turn to face enemy or move towards them
        if (clearLOS && isFacingEnemy(enemyPos)) {
            attack(enemyTank);
        } else if (clearLOS && !isFacingEnemy(enemyPos)) {
            doTurn(enemyPos);
        } else {
            move(enemyPos, blockedDirections);
        }
    }

    /**
     * Decide whether this tank is facing the enemy tank
     *
     * @param enemyTank
     *  position of the enemy tank
     * @return
     *  true if facing the enemy tank
     */
    private boolean isFacingEnemy(Position enemyTank) {
        int enemyPosX = enemyTank.getX();
        int enemyPosY = enemyTank.getY();

        boolean enemyOnX = enemyPosX == curPos.getX();
        boolean enemyOnY = enemyPosY == curPos.getY();


        if (enemyOnY && curDirection == Direction.RIGHT && enemyPosX > curPos.getX()) {
            return true;
        } else if (enemyOnY && curDirection == Direction.LEFT && enemyPosX < curPos.getX()) {
            return true;
        } else if (enemyOnX && curDirection == Direction.UP && enemyPosY > curPos.getY()) {
            return true;
        } else if (enemyOnX && curDirection == Direction.DOWN && enemyPosY < curPos.getY()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Search the surrounding grids and return the direction of any blocking
     * obstacles. If there are none, return null, this will be handled above.
     *
     * @param obstacle
     *  the obstacle to be checked
     * @return
     *  a direction if it is blocking, null if not
     */
    private Direction findBlock(Obstacle obstacle) {
        int obsX = obstacle.getPosition().getX();
        int obsY = obstacle.getPosition().getY();

        if (obsX == curPos.getX()) {
            if (obsY == curPos.getY() + 1) {
                return Direction.UP;
            } else if (obsY == curPos.getY() - 1) {
                return Direction.DOWN;
            } else {
                return null;
            }
        } else if (obsY == curPos.getY()) {
            if (obsX == curPos.getX() + 1) {
                return Direction.RIGHT;
            } else if (obsX == curPos.getX() - 1) {
                return Direction.LEFT;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Turns the tank in the case that we aren't facing the enemy.
     *
     * @param enemyPos
     *  The position of the enemy tank
     */
    private void doTurn(Position enemyPos) {
        if (enemyPos.getX() == curPos.getX()) {
            if (enemyPos.getY() > curPos.getY()) {
                curDirection = Direction.UP;
            } else {
                curDirection = Direction.DOWN;
            }
        } else {
            if (enemyPos.getX() > curPos.getX()) {
                curDirection = Direction.RIGHT;
            } else {
                curDirection = Direction.LEFT;
            }
        }
        LOGGER.info(tankDef.getName() + " turned " + curDirection.name() + "on Position " +
                curPos.toString());
    }

    /**
     * Method containing decision logic on how to move based on where the enemy is,
     * where this tank moved last, and if there are any obstacles around.
     *
     * @param enemyPos
     *  The position of the enemy tank
     * @param blocking
     *  A list of directions that are blocked due to obstacles.
     */
    private void move(Position enemyPos, ArrayList<Direction> blocking) {
        int enemyPosX = enemyPos.getX();
        int enemyPosY = enemyPos.getY();

        boolean enemyIsRight = enemyPosX > curPos.getX();
        boolean enemyIsLeft = enemyPosX < curPos.getX();
        boolean enemyIsUp = enemyPosY > curPos.getY();
        boolean enemyIsDown = enemyPosY < curPos.getY();

        boolean lastMoveUp = lastDirection == Direction.UP;
        boolean lastMoveDown = lastDirection == Direction.DOWN;
        boolean lastMoveRight = lastDirection == Direction.RIGHT;
        boolean lastMoveLeft = lastDirection == Direction.LEFT;

        int diffPosX = enemyPosX > curPos.getX() ? enemyPosX - curPos.getX() :
                curPos.getX() - enemyPosX;
        int diffPosY = enemyPosY > curPos.getY() ? enemyPosY - curPos.getY() :
                curPos.getY() - enemyPosY;

        //there are no obstacles around us
        if (blocking.size() == 0) {
            if (diffPosX > diffPosY) {
                if (enemyIsLeft && !lastMoveRight) {
                    curPos.setX(curPos.getX() - 1);
                    curDirection = Direction.LEFT;
                } else if (enemyIsLeft && lastMoveRight) {
                    if (enemyIsUp) {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.UP;
                    } else {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.DOWN;
                    }
                } else if (enemyIsRight && !lastMoveLeft) {
                    curPos.setX(curPos.getX() + 1);
                    curDirection = Direction.RIGHT;
                } else if (enemyIsRight && lastMoveLeft) {
                    if (enemyIsUp) {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.UP;
                    } else {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.DOWN;
                    }
                }
            } else {
                if (enemyIsUp && !lastMoveDown) {
                    curPos.setY(curPos.getY() + 1);
                    curDirection = Direction.UP;
                } else if (enemyIsUp && lastMoveDown) {
                    if (enemyIsRight) {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.RIGHT;
                    } else {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.LEFT;
                    }
                } else if (enemyIsDown && !lastMoveUp) {
                    curPos.setY(curPos.getY() - 1);
                    curDirection = Direction.DOWN;
                } else if (enemyIsDown && lastMoveUp) {
                    if (enemyIsRight) {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.RIGHT;
                    } else {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.LEFT;
                    }
                }
            }
        //there are obstacles around us
        } else {
            boolean isLeftBlocked = false;
            boolean isRightBlocked = false;
            boolean isUpBlocked = false;
            boolean isDownBlocked = false;

            for (Direction direction : blocking) {
                switch (direction) {
                    case LEFT:
                        isLeftBlocked = true;
                        break;
                    case RIGHT:
                        isRightBlocked = true;
                        break;
                    case UP:
                        isUpBlocked = true;
                        break;
                    case DOWN:
                        isDownBlocked = true;
                        break;
                }
            }

            if (diffPosX > diffPosY) {
                if (enemyIsRight) {
                    if (!isRightBlocked && !lastMoveRight) {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.RIGHT;
                        lastDirection = Direction.RIGHT;
                    } else if (!isDownBlocked && !lastMoveDown) {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.DOWN;
                        lastDirection = Direction.DOWN;
                    } else if (!isUpBlocked && !lastMoveUp) {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.UP;
                        lastDirection = Direction.UP;
                    } else {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.LEFT;
                        lastDirection = Direction.LEFT;
                    }
                } else {
                    if (!isLeftBlocked && !lastMoveLeft) {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.LEFT;
                        lastDirection = Direction.LEFT;
                    } else if (!isUpBlocked && !lastMoveUp) {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.UP;
                        lastDirection = Direction.UP;
                    } else if (!isDownBlocked && !lastMoveDown) {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.DOWN;
                        lastDirection = Direction.DOWN;
                    } else {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.RIGHT;
                        lastDirection = Direction.RIGHT;
                    }
                }
            } else {
                if (enemyIsUp) {
                    if (!isUpBlocked && !lastMoveUp) {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.UP;
                        lastDirection = Direction.UP;
                    } else if (!isLeftBlocked && !lastMoveLeft) {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.LEFT;
                        lastDirection = Direction.LEFT;
                    } else if (!isRightBlocked && !lastMoveRight) {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.RIGHT;
                        lastDirection = Direction.RIGHT;
                    } else {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.DOWN;
                        lastDirection = Direction.DOWN;
                    }
                } else {
                    if (!isDownBlocked && lastDirection != Direction.DOWN) {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.DOWN;
                        lastDirection = Direction.DOWN;
                    } else if (!isLeftBlocked && lastDirection != Direction.LEFT) {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.LEFT;
                        lastDirection = Direction.LEFT;
                    } else if (!isRightBlocked && lastDirection != Direction.RIGHT) {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.RIGHT;
                        lastDirection = Direction.RIGHT;
                    } else {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.UP;
                        lastDirection = Direction.UP;
                    }
                }
            }
        }
        LOGGER.info(tankDef.getName() + " moved to " + curPos.toString());
    }

    /**
     * Method to attack the enemy tank by lowering its curHealth by 1;
     *
     * @param tankAi
     *  the tank to be attacked.
     */
    private void attack(TankAi tankAi) {
        tankAi.setCurHealth(tankAi.getCurHealth() - 1);
        LOGGER.info(tankDef.getName() + " hit " + tankAi.getTankDef().getName() +
                " for 1 damage.");
    }
}
