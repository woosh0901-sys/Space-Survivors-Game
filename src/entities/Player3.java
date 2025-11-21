package entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import main.GameConstants;

public class Player3 extends PlayableCharacter {

    private static final double WIDTH = GameConstants.PLAYER_WIDTH;
    private static final double HEIGHT = GameConstants.PLAYER_HEIGHT;
    private static final String IMAGE_PATH = "/images/woo.png"; // Woo 사진

    public Player3(double startX, double startY) {
        super(startX, startY, WIDTH, HEIGHT);
        
        this.goldMultiplier += 0.5; // 골드 보너스
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("Player3 이미지 로딩 실패! " + IMAGE_PATH);
        }
    }

    @Override
    public List<Bullet> attack() {
        if (shootCooldown <= 0) {
            shootCooldown = this.attackSpeed;

            List<Bullet> bullets = new ArrayList<>();
            // ★ 수정된 부분: 마지막에 'false'
            bullets.add(new Bullet(
                this.x,
                this.y,
                Bullet.PLAYER_BULLET_SIZE,
                Bullet.PLAYER_BULLET_SPEED,
                false
            ));
            return bullets;
        }
        return null;
    }
}