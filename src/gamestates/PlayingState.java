package gamestates;

import java.util.*;

import entities.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import main.GameData;
import main.GameMain;

public class PlayingState extends GameState {
    
    private GraphicsContext gc;
    private Player player;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    
    private int level;
    private double currentXp, requiredXp;
    
    private double spawnTimer;
    private double spawnInterval;
    private Random random;
    
    private double elapsedTime = 0;
    private double shootCooldown = 0;
    
    public double playerdamage = 10;
    private int difficultyTier = 0;
    private int finalGold;
    public PlayingState(GameStateManager gsm) {
        super(gsm);
        Canvas canvas = new Canvas(GameMain.WIDTH, GameMain.HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.rootNode.getChildren().add(canvas);
        init(); 
    }

    public void init() {
        player = new Player(GameMain.WIDTH / 2, GameMain.HEIGHT - 50); 
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        level = 1;
        currentXp = 0;
        requiredXp = 100;
        spawnTimer = 0;
        spawnInterval = 1.0;
        random = new Random();
        elapsedTime = 0;
        shootCooldown = 0;
        difficultyTier = 0;
        // 시간에 따라 변하는 값들도 초기화
        GameData.enemyBaseHealth = 50;
        GameData.enemyBaseXP = 25;
        GameData.enemyBaseGold = 10;
        GameData.enemyDamage = 10;
    }
    
    public Player getPlayer() { return player; }
    
    public void shoot() {
         if (shootCooldown <= 0) {
             bullets.add(new Bullet(player.x, player.y));
             shootCooldown = player.attackSpeed; 
         }
    }
    
    @Override
    public void update(double deltaTime) {
        elapsedTime += deltaTime;
        
        if (shootCooldown > 0) shootCooldown -= deltaTime;
        player.update(GameMain.getActiveKeys(), deltaTime);
        
        // 충돌 감지: 총알 vs 적
        for (Bullet bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (bullet.isDestroyed || enemy.isDestroyed) continue;
                double dx = bullet.x - enemy.x;
                double dy = bullet.y - enemy.y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < bullet.getRadius() + enemy.getRadius()) {
                    bullet.isDestroyed = true;
                    enemy.takeDamage(player.damage);
                    if (enemy.isDestroyed) {
                        finalGold = (int)(GameData.enemyBaseGold * player.getGoldMultiplier());
                        GameData.gold += finalGold;
                        double xpGained = 0;
                        double luckRoll = random.nextDouble();
                        if (luckRoll < 0.01) xpGained = GameData.enemyBaseXP * 3;
                        else if (luckRoll < 0.1) xpGained = GameData.enemyBaseXP * 2;
                        else xpGained = GameData.enemyBaseXP;
                        currentXp += xpGained;
                    }
                    break; 
                }
            }
        }
        
        if (elapsedTime > (difficultyTier + 1) * 30) {
            difficultyTier++;
            GameData.enemyBaseHealth *= 1.2;
            spawnInterval /= 1.1; 
            GameData.enemyBaseXP *= 1.25;
            GameData.enemyBaseGold *= 1.2;
            GameData.enemyDamage *= 1.5;
            System.out.println(String.format("난이도 증가! [%d단계] 적 체력: %.0f, 생성 속도: %.2f초, 기본 경험치: %.1f, 획득 골드: %d,적 데미지 %d", 
                    difficultyTier, GameData.enemyBaseHealth, spawnInterval, GameData.enemyBaseXP, (int)(GameData.enemyBaseGold * player.getGoldMultiplier()),(int)GameData.enemyDamage));
        }
        
        // 충돌 감지: 플레이어 vs 적
        for (Enemy enemy : enemies) {
            if (enemy.isDestroyed) continue;
            
            double dx = player.x - enemy.x;
            double dy = player.y - enemy.y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance < player.getRadius() + enemy.getRadius()) {
            	player.currentHp -= (int)GameData.enemyDamage;
                System.out.println("Player HP: " + (int)player.currentHp);
                enemy.isDestroyed = true;
            }
        }
        
        if (player.currentHp <= 0) {
            gsm.setState(new GameOverState(gsm));
            return; 
        }
        
        bullets.removeIf(b -> b.isOffScreen || b.isDestroyed);
        enemies.removeIf(e -> e.isOffScreen || e.isDestroyed);

        if (currentXp >= requiredXp) {
            level++;
            currentXp -= requiredXp;
            requiredXp *= 1.5;
            gsm.pushState(new LevelUpState(gsm, this)); 
            return;
        }

        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval) {
            spawnEnemy();
            spawnTimer = 0;
        }
        
        for (Bullet b : bullets) b.update(deltaTime);
        for (Enemy e : enemies) e.update(deltaTime);
    }

    @Override
    public void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GameMain.WIDTH, GameMain.HEIGHT);
        player.render(gc);
        for (Bullet b : bullets) b.render(gc);
        for (Enemy e : enemies) e.render(gc);
        renderUI();
    }
    
    private void spawnEnemy() {
        double x = random.nextDouble() * (GameMain.WIDTH - 50) + 25;
        enemies.add(new Enemy(x));
    }

    private void renderUI() {
        // 경험치 바
        gc.setFill(Color.GRAY);
        gc.fillRect(10, 10, GameMain.WIDTH - 20, 20);
        double xpPercentage = (requiredXp > 0) ? currentXp / requiredXp : 0;
        double xpBarWidth = (GameMain.WIDTH - 20) * xpPercentage;
        gc.setFill(Color.CYAN);
        gc.fillRect(10, 10, xpBarWidth, 20);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 16));
        gc.fillText("Level: " + level, 15, 26);
        
        // 체력 바
        gc.setFill(Color.DARKRED);
        gc.fillRect(10, 40, GameMain.WIDTH - 20, 15);
        double hpPercentage = (player.maxHp > 0) ? player.currentHp / player.maxHp : 0;
        double hpBarWidth = (GameMain.WIDTH - 20) * hpPercentage;
        gc.setFill(Color.RED);
        gc.fillRect(10, 40, hpBarWidth, 15);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 12));
        gc.fillText("HP: " + (int)player.currentHp + " / " + (int)player.maxHp, 15, 52);
        
        // 폰트 설정 (시간과 골드에 공통으로 적용)
        gc.setFont(new Font("Arial", 20));
        
        // 시간 표시 (좌하단)
        String timeString = String.format("%02d:%02d", (int)(elapsedTime / 60), (int)(elapsedTime % 60));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(timeString, 15, GameMain.HEIGHT - 15);

        // 골드 표시 (우하단)
        String goldString = "Gold: " + (int)GameData.gold;
        gc.setFill(Color.GOLD);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText(goldString, GameMain.WIDTH - 15, GameMain.HEIGHT - 15);
        
        // 정렬을 다시 기본값으로 되돌림
        gc.setTextAlign(TextAlignment.LEFT);
    }
}