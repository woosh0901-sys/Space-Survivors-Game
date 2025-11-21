package entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import main.GameConstants;

public class Player4 extends PlayableCharacter {

    private static final double WIDTH = GameConstants.PLAYER_WIDTH;
    private static final double HEIGHT = GameConstants.PLAYER_HEIGHT;
    private static final String IMAGE_PATH = "/images/lee.png"; // 이정환

    private int originalDamage; // 원래 데미지 저장용

    public Player4(double startX, double startY) {
        super(startX, startY, WIDTH, HEIGHT);
        
        // 기본 특성: 공격력 +20
        this.damage += 20;
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("Player4(Lee) 이미지 로딩 실패!");
        }
    }

    // ★ 스킬: 파워 업 (공격력 2배)
    // 쿨타임: 60초 / 지속시간: 5초
    @Override
    protected void activateSkill() {
        this.maxSkillCooldown = 60.0; // 지속시간이 기니까 쿨타임도 60초로 설정
        
        if (!isSkillActive) {
            originalDamage = this.damage;
            this.damage *= 200; // 데미지 2배 뻥튀기
            
            this.isSkillActive = true;
            this.skillActiveTimer = 5.0; // ★ 50초간 지속
            
            System.out.println("스킬 발동(이정환): 공격력 2배! (30초 지속)");
        }
    }
    
    @Override
    protected void deactivateSkill() {
        this.damage = originalDamage; // 원래 데미지로 복구
        System.out.println("파워 업 종료");
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