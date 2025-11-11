package entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import main.GameConstants;

public class Player extends PlayableCharacter {

    private static final double DEFAULT_WIDTH = GameConstants.PLAYER_WIDTH;
    private static final double DEFAULT_HEIGHT = GameConstants.PLAYER_HEIGHT;
    private static final String IMAGE_PATH = "/images/player.png";

    public Player(double startX, double startY) {
        super(startX, startY, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        loadImage();
    }
    
    private void loadImage() {
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("플레이어 이미지 로딩 실패! " + IMAGE_PATH + " 파일을 확인하세요.");
        }
    }

    @Override
    public List<Bullet> attack() {
        if (shootCooldown <= 0) {
            shootCooldown = this.attackSpeed;

            List<Bullet> bullets = new ArrayList<>();
            bullets.add(new Bullet(
                this.x,
                this.y,
                Bullet.PLAYER_BULLET_SIZE,
                Bullet.PLAYER_BULLET_SPEED,
                Bullet.PLAYER_BULLET_COLOR
            ));
            return bullets;
        }
        return null;
    }
}
