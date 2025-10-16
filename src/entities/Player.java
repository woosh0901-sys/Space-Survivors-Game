package entities;

import java.util.List; // List import 추가
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import main.GameData;
import main.GameMain;

public class Player {

    public double x, y;
    private Image image;
    private double displayWidth = 80, displayHeight = 80;
    private double shootCooldown = 0;
    
    // 게임 내 임시 스탯
    public double maxHp, currentHp;
    public int damage;
    public double attackSpeed;
    public double speed;
    private double goldMultiplier = 1.0;
    public double maxSpeed = 1050;

    public Player(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        
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
    
    public void shoot(List<Bullet> bullets) {
        if (shootCooldown <= 0) {
            bullets.add(new Bullet(this.x, this.y)); // player.x 대신 this.x 사용
            shootCooldown = this.attackSpeed;        // player.attackSpeed 대신 this.attackSpeed 사용
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
    	
    	if (shootCooldown > 0) {
            shootCooldown -= deltaTime;
        }
    	
    	double moveX = 0;
        double moveY = 0;

        // 2. 키 입력에 따라 방향 설정
        if (activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A)) {
            moveX -= 1;
        }
        if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D)) {
            moveX += 1;
        }
        if (activeKeys.contains(KeyCode.UP) || activeKeys.contains(KeyCode.W)) {
            moveY -= 1;
        }
        if (activeKeys.contains(KeyCode.DOWN) || activeKeys.contains(KeyCode.S)) {
            moveY += 1;
        }

        // 3. 벡터 정규화: 대각선 이동 시 속도가 빨라지는 것을 방지
        if (moveX != 0 && moveY != 0) {
            double magnitude = Math.sqrt(moveX * moveX + moveY * moveY);
            moveX /= magnitude;
            moveY /= magnitude;
        }

        // 4. 최종 위치 계산
        x += moveX * speed * deltaTime;
        y += moveY * speed * deltaTime;
        
        // --- 화면 밖으로 나가지 않도록 위치 보정 ---
        if (x < displayWidth / 2) x = displayWidth / 2;
        if (x > GameMain.WIDTH - displayWidth / 2) x = GameMain.WIDTH - displayWidth / 2;
        if (y < displayHeight / 2) y = displayHeight / 2;
        if (y > GameMain.HEIGHT - displayHeight / 2) y = GameMain.HEIGHT - displayHeight / 2;
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