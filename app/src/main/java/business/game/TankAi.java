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
    private Direction lastDirection;

    public TankAi (Tank tank) {
        this.tankDef = tank;
        curHealth = tank.getHealth();
    }

    /**
     * Method that decides whether the AI should move, turn, or attack.
     *
     * @param map
     * @param enemyTank
     */
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
        int enemyPosX = enemyTank.getX();
        int enemyPosY = enemyTank.getY();

        boolean enemyOnX = enemyPosX == curPos.getX();
        boolean enemyOnY = enemyPosY == curPos.getY();


        if (enemyOnX && curDirection == Direction.RIGHT && enemyPosX > curPos.getX()) {
            return true;
        } else if (enemyOnX && curDirection == Direction.LEFT && enemyPosX < curPos.getX()) {
            return true;
        } else if (enemyOnY && curDirection == Direction.UP && enemyPosY > curPos.getY()) {
            return true;
        } else if (enemyOnY && curDirection == Direction.DOWN && enemyPosY < curPos.getY()) {
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
        LOGGER.info(tankDef.getName() + " turned " + curDirection.name());
    }

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

    private void attack(TankAi tankAi) {
        tankAi.takeDamage();
        LOGGER.info(tankDef.getName() + " hit " + tankAi.getTankDef().getName() +
                " for 1 damage.");
    }

    private void takeDamage() {
        curHealth--;
    }
}
