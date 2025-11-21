package entities;

import java.util.List;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import main.GameData;
import main.GameMain;

public abstract class PlayableCharacter extends Entity {

    protected Image image;
    protected double shootCooldown = 0;
    
    public double maxHp, currentHp; 
    public int damage; 
    
    protected double attackSpeed;
    protected double speed;
    protected double goldMultiplier = 1.0;
    protected double maxSpeed = 1050.0; 

    // ★ 스킬 관련 변수
    protected double maxSkillCooldown = 10.0; // 기본값 (자식이 덮어씀)
    protected double currentSkillCooldown = 0; 
    protected boolean isSkillActive = false;   
    protected double skillActiveTimer = 0;     

    public PlayableCharacter(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
        
        this.maxHp = GameData.playerMaxHp;
        this.currentHp = this.maxHp;
        this.damage = GameData.playerDamage;
        this.attackSpeed = GameData.playerAttackSpeed;
        this.speed = GameData.playerMoveSpeed;
    }

    public abstract List<Bullet> attack();

    // ★ 스킬 사용 시도
    public void tryUseSkill() {
        if (currentSkillCooldown <= 0) {
            activateSkill(); // 자식이 구현한 스킬 발동
            currentSkillCooldown = maxSkillCooldown; // 쿨타임 리셋
        }
    }

    // 자식 클래스에서 구현할 추상 메서드
    protected abstract void activateSkill();
    
    // 버프 종료 (필요시 오버라이드)
    protected void deactivateSkill() {}

    // UI 표시용 (0.0 ~ 1.0)
    public double getSkillProgress() {
        if (currentSkillCooldown <= 0) return 1.0; 
        return 1.0 - (currentSkillCooldown / maxSkillCooldown); 
    }

    public void takeDamage(double damage) {
        this.currentHp -= damage;
        if (this.currentHp <= 0) {
            this.currentHp = 0;
            this.destroy(); 
        }
    }

    public void applyHpBuff(double percentage) { 
        double hpIncrease = this.maxHp * percentage;
        this.maxHp += hpIncrease;
        this.currentHp += hpIncrease; 
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
        
        // ★ 스킬 쿨타임 감소
        if (currentSkillCooldown > 0) {
            currentSkillCooldown -= deltaTime;
        }
        
        // ★ 버프 지속시간 감소 및 종료
        if (isSkillActive) {
            skillActiveTimer -= deltaTime;
            if (skillActiveTimer <= 0) {
                deactivateSkill(); 
                isSkillActive = false;
            }
        }
    }
    
    public void handleInputAndMove(Set<KeyCode> activeKeys, double deltaTime) {
        double moveX = 0;
        double moveY = 0;
        if (activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A)) moveX -= 1;
        if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D)) moveX += 1;
        if (activeKeys.contains(KeyCode.UP) || activeKeys.contains(KeyCode.W)) moveY -= 1;
        if (activeKeys.contains(KeyCode.DOWN) || activeKeys.contains(KeyCode.S)) moveY += 1; 

        if (moveX != 0 && moveY != 0) {
            double magnitude = Math.sqrt(2); 
            moveX /= magnitude;
            moveY /= magnitude;
        }
        x += moveX * speed * deltaTime;
        y += moveY * speed * deltaTime;
        
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