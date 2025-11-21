package entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import main.GameConstants;

public class Player extends PlayableCharacter {

    private static final double DEFAULT_WIDTH = GameConstants.PLAYER_WIDTH;
    private static final double DEFAULT_HEIGHT = GameConstants.PLAYER_HEIGHT;
    private static final String IMAGE_PATH = "/images/park.png"; // Park 사진

    public Player(double startX, double startY) {
        super(startX, startY, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
        this.maxHp += 50; // 체력 보너스
        this.currentHp = this.maxHp;
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("Player 이미지 로딩 실패! " + IMAGE_PATH);
        }
    }

    @Override
    public List<Bullet> attack() {
        if (shootCooldown <= 0) {
            shootCooldown = this.attackSpeed;

            List<Bullet> bullets = new ArrayList<>();
            // ★ 수정된 부분: 마지막에 'false' (플레이어 총알 = 파란 공 이미지)
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