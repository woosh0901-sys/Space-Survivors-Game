package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.GameConstants;
import main.GameData;
import main.GameMain;

public class Boss extends Entity {

    private static final double BOSS_WIDTH = 120.0;
    private static final double BOSS_HEIGHT = 120.0;
    private static final double FINAL_BOSS_WIDTH = 180.0;
    private static final double FINAL_BOSS_HEIGHT = 180.0;
    
    // 기존 이미지 경로
    private static final String BOSS_IMAGE_PATH = "/images/boss.png";
    private static final String FINAL_BOSS_IMAGE_PATH = "/images/final_boss.png";
    
    // ★ 1. 피격(화난) 이미지 경로 추가
    private static final String HIT_IMAGE_PATH = "/images/boss_hit.png"; 

    private static final double HEALTH_BAR_HEIGHT = 10.0;
    private static final double HEALTH_BAR_Y_OFFSET = -15.0;

    private BossStats stats;
    private Image image;      // 평소 이미지
    private Image hitImage;   // ★ 2. 피격 이미지 변수
    
    private boolean isOffScreen = false;
    private boolean isFinalBoss;
    
    // ★ 3. 피격 효과 관련 변수
    private boolean isHit = false;       // 현재 맞았는가?
    private double hitTimer = 0;         // 피격 효과 지속 시간 타이머
    private final double HIT_DURATION = 0.15; // 0.15초 동안 화난 얼굴 유지
    
    private double shootCooldown = 0;
    private double attackSpeed = 1.0; 
    private double moveSpeed;
    private double moveDirection = 1; 

    public Boss(double startX, boolean isFinalBoss) {
        super(startX, -150, 
              isFinalBoss ? FINAL_BOSS_WIDTH : BOSS_WIDTH, 
              isFinalBoss ? FINAL_BOSS_HEIGHT : BOSS_HEIGHT);
        
        this.isFinalBoss = isFinalBoss;
        
        double health = isFinalBoss ? 
            GameData.enemyBaseHealth * 50 : 
            GameData.enemyBaseHealth * 20;   
        
        this.moveSpeed = isFinalBoss ? 100.0 : 150.0;
        this.stats = new BossStats(health, this.moveSpeed);
        
        this.shootCooldown = Math.random() * attackSpeed;
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            String imagePath = isFinalBoss ? FINAL_BOSS_IMAGE_PATH : BOSS_IMAGE_PATH;
            // 평소 이미지 로드 (없으면 null)
            try { image = new Image(getClass().getResourceAsStream(imagePath)); } catch (Exception e) {}
            
            // ★ 4. 피격(화난) 이미지 로드
            hitImage = new Image(getClass().getResourceAsStream(HIT_IMAGE_PATH));
            
        } catch (Exception e) {
            System.err.println("보스 이미지 로딩 실패! (boss_hit.jpg 확인 필요)");
        }
    }
    
    public void takeDamage(double damage) {
        stats.reduceHealth(damage);
        
        // ★ 5. 맞으면 피격 상태 ON
        this.isHit = true;
        this.hitTimer = HIT_DURATION; // 타이머 0.15초 설정
        
        if (stats.isDead()) {
            this.destroy();
        }
    }
    
    @Override
    public void update(double deltaTime) {
        // ★ 6. 피격 타이머 체크
        if (isHit) {
            hitTimer -= deltaTime;
            if (hitTimer <= 0) {
                isHit = false; // 시간이 다 되면 다시 평온한 상태로
            }
        }

        // 기존 이동 로직
        if (y < 200) { // (이전에 수정한 200 위치)
            y += 100 * deltaTime;
        } else {
            x += moveDirection * stats.getSpeed() * deltaTime;
            
            if (x <= width / 2) {
                x = width / 2;
                moveDirection = 1;
            } else if (x >= GameMain.WIDTH - width / 2) {
                x = GameMain.WIDTH - width / 2;
                moveDirection = -1;
            }
        }
    }
    
    public java.util.List<Bullet> updateAI(double deltaTime) {
        shootCooldown -= deltaTime;
        
        if (shootCooldown <= 0) {
            shootCooldown = attackSpeed;
            
            java.util.List<Bullet> bullets = new java.util.ArrayList<>();
            
            if (isFinalBoss) {
                for (int i = -2; i <= 2; i++) {
                    bullets.add(new Bullet(this.x + (i * 40), this.y, Bullet.ENEMY_BULLET_SIZE * 1.5, Bullet.ENEMY_BULLET_SPEED * 1.2, Color.PURPLE));
                }
            } else {
                bullets.add(new Bullet(this.x, this.y, Bullet.ENEMY_BULLET_SIZE * 1.3, Bullet.ENEMY_BULLET_SPEED, Color.RED));
                bullets.add(new Bullet(this.x - 30, this.y, Bullet.ENEMY_BULLET_SIZE * 1.3, Bullet.ENEMY_BULLET_SPEED, Color.RED));
                bullets.add(new Bullet(this.x + 30, this.y, Bullet.ENEMY_BULLET_SIZE * 1.3, Bullet.ENEMY_BULLET_SPEED, Color.RED));
            }
            return bullets;
        }
        return null;
    }

    @Override
    public void render(GraphicsContext gc) {
        // ★ 7. 렌더링 로직 변경: 맞았으면 화난 얼굴, 아니면 평소 얼굴
        Image imageToDraw = (isHit && hitImage != null) ? hitImage : image;

        if (imageToDraw != null) {
            gc.drawImage(imageToDraw, x - width / 2, y - height / 2, width, height);
        } else {
            // 이미지 없을 때 대체 사각형
            gc.setFill(isHit ? Color.WHITE : (isFinalBoss ? Color.DARKVIOLET : Color.DARKRED)); // 맞으면 하얗게 번쩍
            gc.fillRect(x - width / 2, y - height / 2, width, height);
        }
        
        // 체력 바 렌더링 (발 밑으로)
        renderHealthBar(gc);
        
        // 보스 이름 표시
        renderBossName(gc);
    }
    
    private void renderHealthBar(GraphicsContext gc) {
        double barWidth = width * 1.5;
        double hpBarY = y + height / 2 + 20; // 발 밑 위치
        
        gc.setFill(Color.DARKRED);
        gc.fillRect(x - barWidth / 2, hpBarY, barWidth, HEALTH_BAR_HEIGHT);
        
        double hpPercentage = stats.getHealthPercentage();
        gc.setFill(isFinalBoss ? Color.PURPLE : Color.RED);
        gc.fillRect(x - barWidth / 2, hpBarY, barWidth * hpPercentage, HEALTH_BAR_HEIGHT);
        
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 14));
        String hpText = String.format("%.0f / %.0f", stats.getHealth(), stats.getMaxHealth());
        gc.fillText(hpText, x - 30, hpBarY + 25);
    }
    
    private void renderBossName(GraphicsContext gc) {
        gc.setFill(isFinalBoss ? Color.GOLD : Color.ORANGE);
        gc.setFont(new Font("Arial Bold", 18));
        String name = isFinalBoss ? "FINAL BOSS" : "BOSS";
        gc.fillText(name, x - 50, y - height / 2 - 15); // 이름은 머리 위로 올림
    }
    
    public boolean isOffScreen() { return this.isOffScreen; }
    public boolean isFinalBoss() { return this.isFinalBoss; }
    
    private static class BossStats {
        private double health;
        private final double maxHealth;
        private final double speed;
        
        BossStats(double health, double speed) {
            this.maxHealth = health;
            this.health = health;
            this.speed = speed;
        }
        void reduceHealth(double amount) { health = Math.max(0, health - amount); }
        boolean isDead() { return health <= 0; }
        double getHealthPercentage() { return health / maxHealth; }
        double getSpeed() { return speed; }
        double getHealth() { return health; }
        double getMaxHealth() { return maxHealth; }
    }
}