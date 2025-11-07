package entities;

import java.util.List;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import main.GameData;
import main.GameMain;

/**
 * 모든 '조종 가능한' 캐릭터의 공통 부모 클래스입니다.
 * 이동, 버프 적용, 피격 등 공통 로직을 가집니다.
 */
public abstract class PlayableCharacter extends Entity {

    // --- 스탯 변수 ---
    protected Image image;
    protected double shootCooldown = 0;
    
    public double maxHp, currentHp; 
    public int damage; 
    
    protected double attackSpeed;
    protected double speed;
    protected double goldMultiplier = 1.0;
    protected double maxSpeed = 1050.0; // 최대 속도 제한

    /**
     * 조종 가능한 캐릭터의 생성자
     */
    public PlayableCharacter(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
        
        // GameData에서 '영구 업그레이드' 스탯을 가져옵니다.
        this.maxHp = GameData.playerMaxHp;
        this.currentHp = this.maxHp;
        this.damage = GameData.playerDamage;
        this.attackSpeed = GameData.playerAttackSpeed;
        this.speed = GameData.playerMoveSpeed;
    }

    // --- ★ 공격 메소드 (추상) ---
    /**
     * 캐릭터가 공격을 시도합니다. (쿨다운 확인 포함)
     * @return 쿨타임이 되어 발사한 총알 목록 (없으면 null)
     */
    public abstract List<Bullet> attack();

    
    // --- 공통 로직 ---

    public void takeDamage(double damage) {
        this.currentHp -= damage;
        if (this.currentHp <= 0) {
            this.currentHp = 0;
            this.destroy(); 
        }
    }

    // ★ HP 버프 수정된 버전
    public void applyHpBuff(double percentage) { 
        double hpIncrease = this.maxHp * percentage;
        this.maxHp += hpIncrease;
        this.currentHp += hpIncrease; // 증가량만큼만 더함
    }
    public void applyDamageBuff(int amount) { this.damage += amount; }
    public void applyAttackSpeedBuff(double percentage) { this.attackSpeed *= (1 - percentage); }
    public void applyGoldBuff(double percentage) { this.goldMultiplier += percentage; }
    public void applySpeedBuff(double amount) {
        this.speed += amount;
        if (this.speed > this.maxSpeed) this.speed = this.maxSpeed;
    }
    
    public double getGoldMultiplier() { return this.goldMultiplier; }
    public double getSpeed() { return this.speed; }
    public double getMaxSpeed() { return this.maxSpeed; }

    @Override
    public void update(double deltaTime) {
        if (shootCooldown > 0) {
            shootCooldown -= deltaTime;
        }
    }
    
    // ★ S키 버그 수정된 버전
    public void handleInputAndMove(Set<KeyCode> activeKeys, double deltaTime) {
        double moveX = 0;
        double moveY = 0;
        if (activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A)) moveX -= 1;
        if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D)) moveX += 1;
        if (activeKeys.contains(KeyCode.UP) || activeKeys.contains(KeyCode.W)) moveY -= 1;
        if (activeKeys.contains(KeyCode.DOWN) || activeKeys.contains(KeyCode.S)) moveY += 1; // 수정됨

        if (moveX != 0 && moveY != 0) {
            double magnitude = Math.sqrt(2); 
            moveX /= magnitude;
            moveY /= magnitude;
        }
        x += moveX * speed * deltaTime;
        y += moveY * speed * deltaTime;
        
        // 화면 밖으로 나가지 않도록 위치 보정
        if (x < width / 2) x = width / 2;
        if (x > GameMain.WIDTH - width / 2) x = GameMain.WIDTH - width / 2;
        if (y < height / 2) y = height / 2;
        if (y > GameMain.HEIGHT - height / 2) y = GameMain.HEIGHT - height / 2;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (image != null) {
             gc.drawImage(image, x - width / 2, y - height / 2, width, height);
        } else {
            gc.setFill(Color.BLUE);
            gc.fillRect(x - width / 2, y - height / 2, width, height);
        }
    }
}