package entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import main.GameConstants;

public class Player3 extends PlayableCharacter {

    private static final double WIDTH = GameConstants.PLAYER_WIDTH;
    private static final double HEIGHT = GameConstants.PLAYER_HEIGHT;
    private static final String IMAGE_PATH = "/images/woo.png"; // 우서현

    public Player3(double startX, double startY) {
        super(startX, startY, WIDTH, HEIGHT);
        
        // 기본 특성: 골드 획득량 50% 증가
        this.goldMultiplier += 0.5; 
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("Player3(Woo) 이미지 로딩 실패!");
        }
    }

    // ★ 스킬: 긴급 회복
    // 쿨타임: 30초 / 즉시 발동
    @Override
    protected void activateSkill() {
        this.maxSkillCooldown = 30.0; // 쿨타임 30초
        
        // 잃은 체력의 50% 회복
        double missingHp = maxHp - currentHp;
        double healAmount = missingHp * 0.5;
        if (healAmount < 20) healAmount = 20; // 최소 20 회복 보장
        
        this.currentHp += healAmount;
        if (this.currentHp > maxHp) this.currentHp = maxHp;
        
        System.out.println("스킬 발동(우서현): 긴급 회복! (+" + (int)healAmount + " HP)");
    }
    
    // 즉시 발동형이라 deactivateSkill은 필요 없음

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
                false
            ));
            return bullets;
        }
        return null;
    }
}