package entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import main.GameConstants;

public class Player extends PlayableCharacter {

    private static final double DEFAULT_WIDTH = GameConstants.PLAYER_WIDTH;
    private static final double DEFAULT_HEIGHT = GameConstants.PLAYER_HEIGHT;
    private static final String IMAGE_PATH = "/images/park.png"; // 박종화

    private double originalAttackSpeed; // 원래 공격속도 저장용

    public Player(double startX, double startY) {
        super(startX, startY, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
        // 기본 특성: 체력 +50
        this.maxHp += 50; 
        this.currentHp = this.maxHp;
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("Player(Park) 이미지 로딩 실패!");
        }
    }

    // ★ 스킬: 광폭화 (공격속도 대폭 증가)
    // 쿨타임: 60초 / 지속시간: 5초
    @Override
    protected void activateSkill() {
        this.maxSkillCooldown = 60.0; // 쿨타임 1분
        
        if (!isSkillActive) {
            originalAttackSpeed = this.attackSpeed;
            this.attackSpeed = 0.1; // 0.1초마다 발사 (광속)
            
            this.isSkillActive = true;
            this.skillActiveTimer = 5.0; // 5초간 지속
            
            System.out.println("스킬 발동(박종화): 광폭화! (쿨타임 60초)");
        }
    }
    
    @Override
    protected void deactivateSkill() {
        this.attackSpeed = originalAttackSpeed; // 원래 속도로 복구
        System.out.println("광폭화 종료");
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
                false 
            ));
            return bullets;
        }
        return null;
    }
}