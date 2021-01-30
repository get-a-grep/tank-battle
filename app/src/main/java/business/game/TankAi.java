package business.game;

import api.model.Obstacle;
import api.model.Position;
import api.model.Tank;
import business.map.MapInstance;
import lombok.Data;
import org.apache.log4j.Logger;

import java.util.ArrayList;

@Data
public class TankAi {
    private static final Logger LOGGER = Logger.getLogger(TankAi.class);

    private Tank tankDef;

    private int curHealth;
    private Position curPos;
    private Direction curDirection;

    public TankAi (Tank tank) {
        this.tankDef = tank;
        curHealth = tank.getHealth();
    }

    public void doAction(MapInstance map, TankAi enemyTank) {
        ArrayList<Obstacle> obstacles = map.getObstacles();
        ArrayList<Direction> blockedDirections = new ArrayList<>();

        Position enemyPos = enemyTank.curPos;

        boolean enemyOnX = enemyPos.getX() == curPos.getX();
        boolean enemyOnY = enemyPos.getY() == curPos.getY();

        boolean clearLOS = true;
        for (Obstacle obstacle : obstacles) {
            if (enemyOnX) {
                if (obstacle.getPosition().getX() == curPos.getX() && obstacle.isBlocksMove()) {
                    clearLOS = false;
                } else {
                    continue;
                }
            } else if (enemyOnY) {
                if (obstacle.getPosition().getY() == curPos.getY() && obstacle.isBlocksMove()) {
                    clearLOS = false;
                } else {
                    continue;
                }
            }
            Direction block = findBlock(obstacle);
            if (block != null) {
                blockedDirections.add(block);
            }
        }

        if (clearLOS && isFacingEnemy(enemyPos)) {
            attack(enemyTank);
        } else if (clearLOS && !isFacingEnemy(enemyPos)) {
            doTurn(enemyPos);
        } else {
            move(enemyPos, blockedDirections);
        }
    }

    private boolean isFacingEnemy(Position enemyTank) {
        boolean enemyOnX = enemyTank.getX() == curPos.getX();
        boolean enemyOnY = enemyTank.getY() == curPos.getY();

        int enemyPosX = enemyTank.getX();
        int enemyPosY = enemyTank.getY();

        if (enemyOnX && curDirection == Direction.UP && enemyPosX > curPos.getX()) {
            return true;
        } else if (enemyOnX && curDirection == Direction.DOWN && enemyPosX < curPos.getX()) {
            return true;
        } else if (enemyOnY && curDirection == Direction.RIGHT && enemyPosY > curPos.getY()) {
            return true;
        } else if (enemyOnY && curDirection == Direction.LEFT && enemyPosY < curPos.getY()) {
            return true;
        } else {
            return false;
        }
    }

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

    private void doTurn(Position enemyPos) {
        if(enemyPos.getX() == curPos.getX()) {
            if (enemyPos.getX() > curPos.getX()) {
                curDirection = Direction.RIGHT;
            } else {
                curDirection = Direction.LEFT;
            }
        } else {
            if (enemyPos.getY() > curPos.getY()) {
                curDirection = Direction.UP;
            } else {
                curDirection = Direction.DOWN;
            }
        }
        LOGGER.info(tankDef.getName() + " turned " + curDirection.name());
    }

    private void move(Position enemyPos, ArrayList<Direction> blocking) {
        int enemyPosX = enemyPos.getX();
        int enemyPosY = enemyPos.getY();

        int diffPosX = enemyPosX > curPos.getX() ? enemyPosX - curPos.getX() :
                curPos.getX() - enemyPosX;
        int diffPosY = enemyPosY > curPos.getY() ? enemyPosY - curPos.getY() :
                curPos.getY() - enemyPosY;

        if (blocking.size() == 0) {
            if (diffPosX > diffPosY) {
                if (enemyPosX > curPos.getX()) {
                    curPos.setX(curPos.getX() + 1);
                    curDirection = Direction.UP;
                } else {
                    curPos.setX(curPos.getX() - 1);
                    curDirection = Direction.DOWN;
                }
            } else {
                if (enemyPosY > curPos.getY()) {
                    curPos.setY(curPos.getY() + 1);
                    curDirection = Direction.RIGHT;
                } else {
                    curPos.setY(curPos.getY() - 1);
                    curDirection = Direction.LEFT;
                }
            }
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
                if (enemyPosX > curPos.getX()) {
                    if (!isRightBlocked) {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.RIGHT;
                    } else if (!isDownBlocked) {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.DOWN;
                    } else if (!isUpBlocked) {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.UP;
                    } else {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.LEFT;
                    }
                } else {
                    if (!isLeftBlocked) {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.LEFT;
                    } else if (!isUpBlocked) {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.UP;
                    } else if (!isDownBlocked) {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.DOWN;
                    } else {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.RIGHT;
                    }
                }
            } else {
                if (enemyPosY > curPos.getY()) {
                    if (!isUpBlocked) {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.UP;
                    } else if (!isLeftBlocked) {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.LEFT;
                    } else if (!isRightBlocked) {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.RIGHT;
                    } else {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.DOWN;
                    }
                } else {
                    if (!isDownBlocked) {
                        curPos.setX(curPos.getX() - 1);
                        curDirection = Direction.DOWN;
                    } else if (!isLeftBlocked) {
                        curPos.setY(curPos.getY() - 1);
                        curDirection = Direction.LEFT;
                    } else if (!isRightBlocked) {
                        curPos.setY(curPos.getY() + 1);
                        curDirection = Direction.RIGHT;
                    } else {
                        curPos.setX(curPos.getX() + 1);
                        curDirection = Direction.UP;
                    }
                }
            }
        }
        LOGGER.info(tankDef.getName() + " moved to " + curPos.toString());
    }

    private void attack(TankAi tankAi) {
        tankAi.takeDamage();
        LOGGER.info(tankDef.getName() + " hit " + tankAi.getTankDef().getName() +
                " for 1 damage.");
    }

    private void takeDamage() {
        curHealth--;
    }
}
