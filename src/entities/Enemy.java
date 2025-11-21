package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.GameConstants;
import main.GameData;
import main.GameMain;

public class Enemy extends Entity {

    private static final double DEFAULT_WIDTH = GameConstants.ENEMY_WIDTH;
    private static final double DEFAULT_HEIGHT = GameConstants.ENEMY_HEIGHT;
    private static final String IMAGE_PATH = "/images/enemy.png";
    
    private static final double HEALTH_BAR_HEIGHT = GameConstants.HEALTH_BAR_HEIGHT;
    private static final double HEALTH_BAR_Y_OFFSET = GameConstants.HEALTH_BAR_Y_OFFSET;

    private EnemyStats stats;
    private Image image;
    private boolean isOffScreen = false;
    
    private double enemyShootCooldown = 0;
    private double enemyAttackSpeed = GameConstants.ENEMY_ATTACK_SPEED;

    public Enemy(double startX) {
        super(startX, 0 - DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
        this.stats = new EnemyStats(GameData.enemyBaseHealth, GameData.enemySpeed);
        this.enemyShootCooldown = Math.random() * enemyAttackSpeed;
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("적 이미지 로딩 실패! " + IMAGE_PATH + " 파일을 확인하세요.");
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
        y += stats.getSpeed() * deltaTime;
        
        if (y > GameMain.HEIGHT + height) {
            isOffScreen = true;
        }
    }
    
    public Bullet updateAI(double deltaTime) {
        enemyShootCooldown -= deltaTime;
        
        if (enemyShootCooldown <= 0) {
            enemyShootCooldown = enemyAttackSpeed;
            
            return new Bullet(
                this.x, 
                this.y, 
                Bullet.ENEMY_BULLET_SIZE, 
                Bullet.ENEMY_BULLET_SPEED, 
                Bullet.ENEMY_BULLET_COLOR
            );
        }
        return null;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x - width / 2, y - height / 2, width, height);
            
            if (stats.getHealth() < stats.getMaxHealth()) {
                renderHealthBar(gc);
            }
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(x - width / 2, y - height / 2, width, height);
        }
    }
    
    private void renderHealthBar(GraphicsContext gc) {
        double hpBarY = y - height / 2 + HEALTH_BAR_Y_OFFSET;
        
        gc.setFill(Color.DARKRED);
        gc.fillRect(x - width / 2, hpBarY, width, HEALTH_BAR_HEIGHT);
        
        double hpPercentage = stats.getHealthPercentage();
        gc.setFill(Color.RED);
        gc.fillRect(x - width / 2, hpBarY, width * hpPercentage, HEALTH_BAR_HEIGHT);
    }
    
    public boolean isOffScreen() {
        return this.isOffScreen;
    }
    
    // 내부 클래스로 스탯 관리
    private static class EnemyStats {
        private double health;
        private final double maxHealth;
        private final double speed;
        
        EnemyStats(double health, double speed) {
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
