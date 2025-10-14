package entities;

import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import main.GameData;

public class Player {

    public double x, y;
    private Image image;
    private double displayWidth = 64, displayHeight = 64;
    
    // 게임 내에서만 사용되는 임시 스탯
    public double maxHp, currentHp;
    public int damage;
    public double attackSpeed;
    public double speed;
    private double goldMultiplier = 1.0;
    public double maxSpeed = 450; // 최대 이동 속도 한계

    public Player(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        
        // GameData의 영구 스탯으로 게임 내 임시 스탯을 초기화
        this.maxHp = GameData.playerMaxHp;
        this.currentHp = this.maxHp;
        this.damage = GameData.playerDamage;
        this.attackSpeed = GameData.playerAttackSpeed;
        this.speed = GameData.playerMoveSpeed;
        
        try {
            image = new Image(getClass().getResourceAsStream("/images/player.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 레벨업 버프 적용 메소드들
    public void applyHpBuff(double percentage) { 
    	this.maxHp *= (1 + percentage); this.currentHp = this.maxHp; 
    	System.out.println("최대 체력: " + (int)this.maxHp + "현재 체력: " + (int)this.currentHp);
    	}
    public void applyDamageBuff(int amount) { 
    	this.damage += amount; 
    	System.out.println("현재 공격력: " + this.damage);
    	}
    public void applyAttackSpeedBuff(double percentage) {
    	this.attackSpeed *= (1 - percentage); 
    	System.out.println("현재 공격속도: " + this.attackSpeed);
    	}
    public void applyGoldBuff(double percentage) { 
    	this.goldMultiplier += percentage;
    	System.out.println("골드획득양: " + this.goldMultiplier * 10);
    	}
    public void applySpeedBuff(double amount) {
        this.speed += amount;
        if (this.speed > this.maxSpeed) this.speed = this.maxSpeed;
        System.out.println("현재 이동속도: " + this.speed);
    }
    
    // Getter 메소드들
    public double getRadius() { return displayWidth / 2; }
    public double getGoldMultiplier() { return this.goldMultiplier; }
    public double getSpeed() { return this.speed; }
    public double getMaxSpeed() { return this.maxSpeed; }

    
    public void update(Set<KeyCode> activeKeys, double deltaTime) {
        if (activeKeys.contains(KeyCode.A)) x -= speed * deltaTime;
        if (activeKeys.contains(KeyCode.D)) x += speed * deltaTime;
        if (activeKeys.contains(KeyCode.W)) y -= speed * deltaTime;
        if (activeKeys.contains(KeyCode.S)) y += speed * deltaTime;
    }

    public void render(GraphicsContext gc) {
        if (image != null) {
        	 gc.drawImage(image, x - displayWidth / 2, y - displayHeight / 2, displayWidth,displayHeight);
        } else {
            gc.setFill(javafx.scene.paint.Color.BLUE);
            gc.fillRect(x - 15, y - 15, 30, 30);
        }
    }
}