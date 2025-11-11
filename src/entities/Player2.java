package entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import main.GameConstants;

public class Player2 extends PlayableCharacter {

    private static final double TANK_WIDTH = GameConstants.TANK_WIDTH;
    private static final double TANK_HEIGHT = GameConstants.TANK_HEIGHT;
    private static final String IMAGE_PATH = "/images/tank_player.png";

    public Player2(double startX, double startY) {
        super(startX, startY, TANK_WIDTH, TANK_HEIGHT);

        this.maxHp += GameConstants.TANK_HP_BONUS;
        this.currentHp = this.maxHp;
        this.speed *= GameConstants.TANK_SPEED_MULTIPLIER;

        loadImage();
    }
    
    private void loadImage() {
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("탱크 이미지 로딩 실패! " + IMAGE_PATH + " 파일을 확인하세요.");
        }
    }

    @Override
    public List<Bullet> attack() {
        if (shootCooldown <= 0) {
            shootCooldown = this.attackSpeed * GameConstants.TANK_ATTACK_SPEED_MULTIPLIER;

            List<Bullet> bullets = new ArrayList<>();

            // 중앙 총알
            bullets.add(new Bullet(
                this.x,
                this.y,
                Bullet.PLAYER_BULLET_SIZE,
                Bullet.PLAYER_BULLET_SPEED,
                Bullet.PLAYER_BULLET_COLOR
            ));

            // 왼쪽 총알
            bullets.add(new Bullet(
                this.x - GameConstants.SHOTGUN_SPREAD_OFFSET,
                this.y,
                Bullet.PLAYER_BULLET_SIZE * GameConstants.SHOTGUN_BULLET_SIZE_MULTIPLIER,
                Bullet.PLAYER_BULLET_SPEED * GameConstants.SHOTGUN_BULLET_SPEED_MULTIPLIER,
                Bullet.PLAYER_BULLET_COLOR
            ));

            // 오른쪽 총알
            bullets.add(new Bullet(
                this.x + GameConstants.SHOTGUN_SPREAD_OFFSET,
                this.y,
                Bullet.PLAYER_BULLET_SIZE * GameConstants.SHOTGUN_BULLET_SIZE_MULTIPLIER,
                Bullet.PLAYER_BULLET_SPEED * GameConstants.SHOTGUN_BULLET_SPEED_MULTIPLIER,
                Bullet.PLAYER_BULLET_COLOR
            ));

            return bullets;
        }
        return null;
    }
}
