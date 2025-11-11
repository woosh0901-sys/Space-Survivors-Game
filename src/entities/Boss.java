package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.GameConstants;
import main.GameData;
import main.GameMain;

/**
 * 보스 객체를 정의하는 클래스입니다.
 */
public class Boss extends Entity {

    private static final double BOSS_WIDTH = 120.0;
    private static final double BOSS_HEIGHT = 120.0;
    private static final double FINAL_BOSS_WIDTH = 180.0;
    private static final double FINAL_BOSS_HEIGHT = 180.0;
    private static final String BOSS_IMAGE_PATH = "/images/boss.png";
    private static final String FINAL_BOSS_IMAGE_PATH = "/images/final_boss.png";
    
    private static final double HEALTH_BAR_HEIGHT = 10.0;
    private static final double HEALTH_BAR_Y_OFFSET = -15.0;

    private BossStats stats;
    private Image image;
    private boolean isOffScreen = false;
    private boolean isFinalBoss;
    
    private double shootCooldown = 0;
    private double attackSpeed = 1.0; // 보스는 더 빠르게 공격
    private double moveSpeed;
    private double moveDirection = 1; // 1 = 오른쪽, -1 = 왼쪽

    /**
     * 보스 생성자
     * @param startX 시작 x 좌표
     * @param isFinalBoss 최종 보스 여부
     */
    public Boss(double startX, boolean isFinalBoss) {
        super(startX, -150, 
              isFinalBoss ? FINAL_BOSS_WIDTH : BOSS_WIDTH, 
              isFinalBoss ? FINAL_BOSS_HEIGHT : BOSS_HEIGHT);
        
        this.isFinalBoss = isFinalBoss;
        
        // 최종 보스는 훨씬 강함
        double health = isFinalBoss ? 
            GameData.enemyBaseHealth * 50 : // 최종 보스: 일반 적의 50배
            GameData.enemyBaseHealth * 20;   // 중간 보스: 일반 적의 20배
        
        this.moveSpeed = isFinalBoss ? 100.0 : 150.0;
        this.stats = new BossStats(health, this.moveSpeed);
        
        // 공격 쿨타임 무작위 설정
        this.shootCooldown = Math.random() * attackSpeed;
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            String imagePath = isFinalBoss ? FINAL_BOSS_IMAGE_PATH : BOSS_IMAGE_PATH;
            image = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            System.err.println("보스 이미지 로딩 실패!");
        }
    }
    
    public void takeDamage(double damage) {
        stats.reduceHealth(damage);
        if (stats.isDead()) {
            this.destroy();
        }
    }
    
    @Override
    public void update(double deltaTime) {
        // 보스가 화면에 완전히 들어올 때까지 아래로 이동
        if (y < 150) {
            y += 100 * deltaTime;
        } else {
            // 화면에 들어온 후에는 좌우로 이동
            x += moveDirection * stats.getSpeed() * deltaTime;
            
            // 화면 경계에 닿으면 방향 전환
            if (x <= width / 2) {
                x = width / 2;
                moveDirection = 1;
            } else if (x >= GameMain.WIDTH - width / 2) {
                x = GameMain.WIDTH - width / 2;
                moveDirection = -1;
            }
        }
    }
    
    /**
     * 보스의 공격 패턴 (여러 발 발사)
     */
    public java.util.List<Bullet> updateAI(double deltaTime) {
        shootCooldown -= deltaTime;
        
        if (shootCooldown <= 0) {
            shootCooldown = attackSpeed;
            
            java.util.List<Bullet> bullets = new java.util.ArrayList<>();
            
            if (isFinalBoss) {
                // 최종 보스: 5방향 발사
                for (int i = -2; i <= 2; i++) {
                    bullets.add(new Bullet(
                        this.x + (i * 40),
                        this.y,
                        Bullet.ENEMY_BULLET_SIZE * 1.5,
                        Bullet.ENEMY_BULLET_SPEED * 1.2,
                        Color.PURPLE
                    ));
                }
            } else {
                // 중간 보스: 3방향 발사
                bullets.add(new Bullet(this.x, this.y, 
                    Bullet.ENEMY_BULLET_SIZE * 1.3, Bullet.ENEMY_BULLET_SPEED, Color.RED));
                bullets.add(new Bullet(this.x - 30, this.y, 
                    Bullet.ENEMY_BULLET_SIZE * 1.3, Bullet.ENEMY_BULLET_SPEED, Color.RED));
                bullets.add(new Bullet(this.x + 30, this.y, 
                    Bullet.ENEMY_BULLET_SIZE * 1.3, Bullet.ENEMY_BULLET_SPEED, Color.RED));
            }
            
            return bullets;
        }
        return null;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x - width / 2, y - height / 2, width, height);
        } else {
            // 이미지 없을 때 대체 사각형
            gc.setFill(isFinalBoss ? Color.DARKVIOLET : Color.DARKRED);
            gc.fillRect(x - width / 2, y - height / 2, width, height);
        }
        
        // 체력 바 렌더링
        renderHealthBar(gc);
        
        // 보스 이름 표시
        renderBossName(gc);
    }
    
    private void renderHealthBar(GraphicsContext gc) {
        double barWidth = width * 1.5; // 보스 체력바는 더 넓게
        double hpBarY = y - height / 2 + HEALTH_BAR_Y_OFFSET;
        
        // 체력 바 배경
        gc.setFill(Color.DARKRED);
        gc.fillRect(x - barWidth / 2, hpBarY, barWidth, HEALTH_BAR_HEIGHT);
        
        // 현재 체력
        double hpPercentage = stats.getHealthPercentage();
        gc.setFill(isFinalBoss ? Color.PURPLE : Color.RED);
        gc.fillRect(x - barWidth / 2, hpBarY, barWidth * hpPercentage, HEALTH_BAR_HEIGHT);
        
        // 체력 텍스트
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 14));
        String hpText = String.format("%.0f / %.0f", stats.getHealth(), stats.getMaxHealth());
        gc.fillText(hpText, x - 30, hpBarY - 5);
    }
    
    private void renderBossName(GraphicsContext gc) {
        gc.setFill(isFinalBoss ? Color.GOLD : Color.ORANGE);
        gc.setFont(new Font("Arial Bold", 18));
        String name = isFinalBoss ? "FINAL BOSS" : "BOSS";
        gc.fillText(name, x - 50, y + height / 2 + 25);
    }
    
    public boolean isOffScreen() {
        return this.isOffScreen;
    }
    
    public boolean isFinalBoss() {
        return this.isFinalBoss;
    }
    
    // 내부 클래스로 보스 스탯 관리
    private static class BossStats {
        private double health;
        private final double maxHealth;
        private final double speed;
        
        BossStats(double health, double speed) {
            this.maxHealth = health;
            this.health = health;
            this.speed = speed;
        }
        
        void reduceHealth(double amount) {
            health = Math.max(0, health - amount);
        }
        
        boolean isDead() {
            return health <= 0;
        }
        
        double getHealthPercentage() {
            return health / maxHealth;
        }
        
        double getSpeed() {
            return speed;
        }
        
        double getHealth() {
            return health;
        }
        
        double getMaxHealth() {
            return maxHealth;
        }
    }
}