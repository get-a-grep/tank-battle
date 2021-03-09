package business.map;

public enum Difficulty {
    EASY(80, 65, 5),
    NORMAL(100, 80, 25),
    HARD(120, 96, 50),
    INSANE(150, 120, 100);

    private final int maxX;
    private final int maxY;
    private final int obstacles;

    Difficulty(final int maxX, final int maxY, final int obstacles) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.obstacles = obstacles;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getObstacles() {
        return obstacles;
    }
}
