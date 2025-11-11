package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.GameConstants;
import main.GameMain;

public class Bullet extends Entity {

    public static final double PLAYER_BULLET_SPEED = GameConstants.PLAYER_BULLET_SPEED;
    public static final double PLAYER_BULLET_SIZE = GameConstants.PLAYER_BULLET_SIZE;
    public static final Color PLAYER_BULLET_COLOR = GameConstants.PLAYER_BULLET_COLOR;

    public static final double ENEMY_BULLET_SPEED = GameConstants.ENEMY_BULLET_SPEED;
    public static final double ENEMY_BULLET_SIZE = GameConstants.ENEMY_BULLET_SIZE;
    public static final Color ENEMY_BULLET_COLOR = GameConstants.ENEMY_BULLET_COLOR;

    private double speed;
    private Color color;
    private boolean isOffScreen = false;

    public Bullet(double startX, double startY, double size, double speed, Color color) {
        super(startX, startY, size, size);
        this.speed = speed;
        this.color = color;
    }

    @Override
    public void update(double deltaTime) {
        y += speed * deltaTime;

        if (y < 0 - height || y > GameMain.HEIGHT + height) {
            isOffScreen = true;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(this.color);
        gc.fillOval(x - width / 2, y - height / 2, width, height);
    }

    public boolean isOffScreen() {
        return this.isOffScreen;
    }
}