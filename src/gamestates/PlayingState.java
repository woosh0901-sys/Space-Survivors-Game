package gamestates;

import java.util.*;

import entities.*;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import main.GameData;
import main.GameMain;

public class PlayingState extends GameState {
    
    private GraphicsContext gc;
    private Player player;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<EnemyBullet> enemyBullets;
    
    private int level;
    private double currentXp, requiredXp;
    
    private double spawnTimer;
    private double spawnInterval;
    private Random random;
    
    private double elapsedTime = 0;
    
    public double playerdamage = 10;
    private int difficultyTier = 0;
    private GameUIController uiController;
    
    public PlayingState(GameStateManager gsm) {
        super(gsm);
        
        try {
            // 1. 게임 월드를 그릴 Canvas 생성
            Canvas gameCanvas = new Canvas(GameMain.WIDTH, GameMain.HEIGHT);
            gc = gameCanvas.getGraphicsContext2D();

            // 2. FXML로 만든 UI 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameUI.fxml"));
            Pane uiPane = loader.load();
            uiController = loader.getController(); // 컨트롤러 인스턴스 저장

            // 3. StackPane으로 Canvas 위에 UI Pane을 겹침
            StackPane stack = new StackPane();
            stack.getChildren().addAll(gameCanvas, uiPane);
            this.rootNode = stack;

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        init(); 
    }

    public void init() {
    	player = new Player(GameMain.WIDTH / 2, GameMain.HEIGHT - 50); 
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        level = 1;
        currentXp = 0;
        requiredXp = 150;
        spawnTimer = 0;
        spawnInterval = 1.0;
        random = new Random();
        elapsedTime = 0;
        difficultyTier = 0;
        
        GameData.enemyBaseHealth = 50;
        GameData.enemyBaseXP = 25;
        GameData.enemyBaseGold = 10;
        GameData.enemyDamage = 10;
        GameData.enemySpeed = 150;
        
        enemies = new ArrayList<>();
        enemyBullets = new ArrayList<>();
    }
    
    public void shoot() {
        player.shoot(bullets);
    }
       
    public Player getPlayer() { return player; }
    
    @Override
    public void update(double deltaTime) {
        // --- 1. 상태 업데이트 (시간, 쿨타임 등) ---
        elapsedTime += deltaTime;
        
        // --- 2. 객체 위치 및 상태 업데이트 ---
        player.update(GameMain.getActiveKeys(), deltaTime);
        for (Bullet b : bullets) b.update(deltaTime);
        for (Enemy e : enemies) e.update(deltaTime, enemyBullets);
        for (EnemyBullet eb : enemyBullets) eb.update(deltaTime);
        
        // --- 3. 충돌 감지 ---
        // 총알 vs 적
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
                        int finalGold = (int)(GameData.enemyBaseGold * player.getGoldMultiplier());
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
        
        // 적 총알 vs 플레이어
        for (EnemyBullet eb : enemyBullets) {
            if (eb.isDestroyed) continue;
            double dx = player.x - eb.x;
            double dy = player.y - eb.y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < player.getRadius() + eb.getRadius()) {
                player.currentHp -= GameData.enemyDamage;
                eb.isDestroyed = true;
            }
        }

        // 플레이어 vs 적
        for (Enemy enemy : enemies) {
            if (enemy.isDestroyed) continue;
            double dx = player.x - enemy.x;
            double dy = player.y - enemy.y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < player.getRadius() + enemy.getRadius()) {
                player.currentHp -= GameData.enemyDamage;
                enemy.isDestroyed = true;
            }
        }

        // --- 4. 객체 정리 (화면 이탈 및 파괴된 객체 제거) ---
        bullets.removeIf(b -> b.isOffScreen || b.isDestroyed);
        enemies.removeIf(e -> e.isOffScreen || e.isDestroyed);
        enemyBullets.removeIf(eb -> eb.isOffScreen || eb.isDestroyed);

        // --- 5. 게임 상태 변경 (레벨업, 게임 오버) ---
        if (player.currentHp <= 0) {
            gsm.setState(new GameOverState(gsm));
            return; 
        }
        
        if (currentXp >= requiredXp) {
            level++;
            currentXp -= requiredXp;
            requiredXp *= 1.6;
            gsm.pushState(new LevelUpState(gsm, this)); 
            return;
        }

        // --- 6. 새로운 객체 생성 (적 스폰) ---
        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval) {
            spawnEnemy();
            spawnTimer = 0;
        }
        
        // --- 7. 난이도 및 UI 업데이트 ---
        if (elapsedTime > (difficultyTier + 1) * 30) {
            difficultyTier++;
            GameData.enemyBaseHealth *= 1.2;
            if (spawnInterval > 0.2) spawnInterval /= 1.1;
            GameData.enemyBaseGold *= 1.2;
            GameData.enemyDamage *= 1.5;
            if(GameData.enemySpeed <= 250)
            GameData.enemySpeed +=15;
            System.out.println(String.format("난이도 증가! [%d단계] 적 체력: %.0f, 생성 속도: %.2f초, 기본 경험치: %.1f, 획득 골드: %d,적 데미지 %d", difficultyTier, GameData.enemyBaseHealth, spawnInterval, GameData.enemyBaseXP, (int)(GameData.enemyBaseGold * player.getGoldMultiplier()),(int)GameData.enemyDamage));
        }
        uiController.update(level, currentXp, requiredXp, player.currentHp, player.maxHp, elapsedTime);
    }

    @Override
    public void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GameMain.WIDTH, GameMain.HEIGHT);
        player.render(gc);
        for (Bullet b : bullets) b.render(gc);
        for (Enemy e : enemies) e.render(gc);
        for (EnemyBullet eb : enemyBullets) eb.render(gc);
    }
    
    private void spawnEnemy() {
        double x = random.nextDouble() * (GameMain.WIDTH - 50) + 25;
        enemies.add(new Enemy(x));
    }
}