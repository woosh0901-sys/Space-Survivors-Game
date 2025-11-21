package gamestates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Boss;
import entities.Bullet;
import entities.CharacterFactory;
import entities.Enemy;
import entities.PlayableCharacter;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import main.GameConstants;
import main.GameData;
import main.GameMain;

public class PlayingState extends GameState {
    
    private GraphicsContext gc;
    private PlayableCharacter player;
    
    private List<Bullet> playerBullets;
    private List<Enemy> enemies;
    private List<Boss> bosses; 
    private List<Bullet> enemyBullets;
    
    private int level;
    private double currentXp, requiredXp;
    
    private double spawnTimer;
    private double spawnInterval;
    private Random random;
    
    private double elapsedTime = 0;
    private int difficultyTier = 0;
    private GameUIController uiController;
    
    private double nextBossTime = GameConstants.BOSS_SPAWN_INTERVAL;
    private boolean finalBossSpawned = false;
    private boolean gameCleared = false;
    
    private Image backgroundImage;
    
    public PlayingState(GameStateManager gsm) {
        super(gsm);
        
        try {
            Canvas gameCanvas = new Canvas(GameMain.WIDTH, GameMain.HEIGHT);
            gc = gameCanvas.getGraphicsContext2D();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameUI.fxml"));
            Pane uiPane = loader.load();
            uiController = loader.getController();

            StackPane stack = new StackPane();
            stack.getChildren().addAll(gameCanvas, uiPane);
            this.rootNode = stack;

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        init();
    }

    public void init() {
        double startX = GameMain.WIDTH / 2;
        double startY = GameMain.HEIGHT - 50;
        
        player = CharacterFactory.createCharacter(GameData.selectedCharacter, startX, startY);
        System.out.println("캐릭터 로드: " + GameData.selectedCharacter);
        
        playerBullets = new ArrayList<>();
        enemies = new ArrayList<>();
        bosses = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        
        level = 1;
        currentXp = 0;
        requiredXp = GameConstants.INITIAL_REQUIRED_XP;
        spawnTimer = 0;
        spawnInterval = GameConstants.INITIAL_SPAWN_INTERVAL;
        random = new Random();
        elapsedTime = 0;
        difficultyTier = 0;
        
        nextBossTime = GameConstants.BOSS_SPAWN_INTERVAL;
        finalBossSpawned = false;
        gameCleared = false;
        
        resetGameStats();
        
        try {
            backgroundImage = new Image(getClass().getResourceAsStream("/images/background.png"));
        } catch (Exception e) {
            System.err.println("배경 이미지를 로드할 수 없습니다.");
        }
    }
    
    private void resetGameStats() {
        GameData.enemyBaseHealth = 50;
        GameData.enemyBaseXP = 25;
        GameData.enemyBaseGold = 10;
        GameData.enemyDamage = 10;
        GameData.enemySpeed = 150;
    }
    
    public void shoot() {
        List<Bullet> newBullets = player.attack();
        if (newBullets != null && !newBullets.isEmpty()) {
            playerBullets.addAll(newBullets);
        }
    }
       
    public PlayableCharacter getPlayer() {
        return player;
    }
    
    @Override
    public void update(double deltaTime) {
        if (gameCleared) return; 
        
        // ★ 스킬 사용 (E키)
        if (GameMain.getActiveKeys().contains(javafx.scene.input.KeyCode.E)) {
            player.tryUseSkill();
        }
        
        updateGameTime(deltaTime);
        updateEntities(deltaTime);
        handleCollisions();
        cleanupDestroyedEntities();
        checkGameState();
        handleSpawning(deltaTime);
        updateDifficulty(deltaTime);
        updateUI();
    }
    
    private void updateGameTime(double deltaTime) {
        elapsedTime += deltaTime;
    }
    
    private void updateEntities(double deltaTime) {
        player.update(deltaTime);
        player.handleInputAndMove(GameMain.getActiveKeys(), deltaTime);

        updateEnemies(deltaTime);
        updateBosses(deltaTime);

        playerBullets.forEach(b -> b.update(deltaTime));
        enemyBullets.forEach(b -> b.update(deltaTime));
    }
    
    private void updateEnemies(double deltaTime) {
        List<Bullet> newEnemyBullets = new ArrayList<>();
        for (Enemy enemy : enemies) {
            enemy.update(deltaTime);
            Bullet bullet = enemy.updateAI(deltaTime);
            if (bullet != null) newEnemyBullets.add(bullet);
        }
        enemyBullets.addAll(newEnemyBullets);
    }
    
    private void updateBosses(double deltaTime) {
        List<Bullet> newBossBullets = new ArrayList<>();
        for (Boss boss : bosses) {
            boss.update(deltaTime);
            List<Bullet> bullets = boss.updateAI(deltaTime);
            if (bullets != null) newBossBullets.addAll(bullets);
        }
        enemyBullets.addAll(newBossBullets);
    }
    
    private void handleCollisions() {
        handlePlayerBulletCollisions();
        handleEnemyBulletCollisions();
        handlePlayerEnemyCollisions();
        handlePlayerBossCollisions();
    }
    
    private void handlePlayerBulletCollisions() {
        for (Bullet bullet : playerBullets) {
            if (bullet.isDestroyed()) continue;
            for (Enemy enemy : enemies) {
                if (enemy.isDestroyed()) continue;
                if (bullet.collidesWith(enemy)) {
                    handleEnemyHit(bullet, enemy);
                    break;
                }
            }
        }
        for (Bullet bullet : playerBullets) {
            if (bullet.isDestroyed()) continue;
            for (Boss boss : bosses) {
                if (boss.isDestroyed()) continue;
                if (bullet.collidesWith(boss)) {
                    handleBossHit(bullet, boss);
                    break;
                }
            }
        }
    }
    
    private void handleEnemyHit(Bullet bullet, Enemy enemy) {
        bullet.destroy();
        enemy.takeDamage(player.damage);
        if (enemy.isDestroyed()) grantRewards();
    }
    
    private void handleBossHit(Bullet bullet, Boss boss) {
        bullet.destroy();
        boss.takeDamage(player.damage);
        if (boss.isDestroyed()) {
            grantBossRewards(boss);
            if (boss.isFinalBoss()) {
                gameCleared = true;
                gsm.setState(new GameClearState(gsm, elapsedTime));
            }
        }
    }
    
    private void grantRewards() {
        grantGold();
        grantExperience();
    }
    
    private void grantBossRewards(Boss boss) {
        int bossGold = boss.isFinalBoss() ? 
            (int)(GameData.enemyBaseGold * 100 * player.getGoldMultiplier()) :
            (int)(GameData.enemyBaseGold * 50 * player.getGoldMultiplier());
        GameData.gold += bossGold;
        
        double bossXp = boss.isFinalBoss() ? 
             GameConstants.FINAL_BOSS_XP_REWARD : 
             GameConstants.BOSS_XP_REWARD;
        currentXp += bossXp;
        System.out.println("보스 처치!");
    }
    
    private void grantGold() {
        int finalGold = (int)(GameData.enemyBaseGold * player.getGoldMultiplier());
        GameData.gold += finalGold;
    }
    
    private void grantExperience() {
        double xpGained = calculateXpGain();
        currentXp += xpGained;
    }
    
    private double calculateXpGain() {
        double luckRoll = random.nextDouble();
        if (luckRoll < GameConstants.XP_TRIPLE_CHANCE) return GameData.enemyBaseXP * GameConstants.XP_TRIPLE_MULTIPLIER;
        else if (luckRoll < GameConstants.XP_DOUBLE_CHANCE) return GameData.enemyBaseXP * GameConstants.XP_DOUBLE_MULTIPLIER;
        return GameData.enemyBaseXP;
    }
    
    private void handleEnemyBulletCollisions() {
        for (Bullet bullet : enemyBullets) {
            if (bullet.isDestroyed() || player.isDestroyed()) continue;
            if (player.collidesWith(bullet)) {
                player.takeDamage(GameData.enemyDamage);
                bullet.destroy();
            }
        }
    }
    
    private void handlePlayerEnemyCollisions() {
        for (Enemy enemy : enemies) {
            if (enemy.isDestroyed() || player.isDestroyed()) continue;
            if (player.collidesWith(enemy)) {
                player.takeDamage(GameData.enemyDamage);
                enemy.destroy();
            }
        }
    }
    
    private void handlePlayerBossCollisions() {
        for (Boss boss : bosses) {
            if (boss.isDestroyed() || player.isDestroyed()) continue;
            if (player.collidesWith(boss)) {
                player.takeDamage(GameData.enemyDamage * 2);
            }
        }
    }
    
    private void cleanupDestroyedEntities() {
        playerBullets.removeIf(b -> b.isOffScreen() || b.isDestroyed());
        enemies.removeIf(e -> e.isOffScreen() || e.isDestroyed());
        bosses.removeIf(b -> b.isOffScreen() || b.isDestroyed());
        enemyBullets.removeIf(b -> b.isOffScreen() || b.isDestroyed());
    }
    
    private void checkGameState() {
        if (player.isDestroyed()) {
            gsm.setState(new GameOverState(gsm));
            return;
        }
        if (currentXp >= requiredXp) {
            handleLevelUp();
        }
    }
    
    private void handleLevelUp() {
        level++;
        currentXp -= requiredXp;
        requiredXp *= GameConstants.XP_MULTIPLIER;
        gsm.pushState(new LevelUpState(gsm, this));
    }
    
    private void handleSpawning(double deltaTime) {
        if (elapsedTime >= nextBossTime && !finalBossSpawned) {
            spawnBoss();
        }
        if (elapsedTime < GameConstants.FINAL_BOSS_TIME) {
            handleEnemySpawning(deltaTime);
        }
    }
    
    private void handleEnemySpawning(double deltaTime) {
        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval) {
            spawnEnemy();
            spawnTimer = 0;
        }
    }
    
    private void spawnBoss() {
        double x = GameMain.WIDTH / 2;
        if (elapsedTime >= GameConstants.FINAL_BOSS_TIME) {
            bosses.add(new Boss(x, true));
            finalBossSpawned = true;
            System.out.println("최종 보스 출현!");
        } else {
            bosses.add(new Boss(x, false));
            nextBossTime += GameConstants.BOSS_SPAWN_INTERVAL;
            System.out.println("보스 출현!");
        }
    }
    
    private void updateDifficulty(double deltaTime) {
        if (elapsedTime >= GameConstants.FINAL_BOSS_TIME) return;
        if (elapsedTime > (difficultyTier + 1) * GameConstants.DIFFICULTY_TIER_DURATION) {
            difficultyTier++;
            GameData.enemyBaseHealth *= GameConstants.ENEMY_HEALTH_MULTIPLIER;
            if (spawnInterval > GameConstants.MIN_SPAWN_INTERVAL) spawnInterval /= GameConstants.SPAWN_INTERVAL_DIVIDER;
            GameData.enemyBaseGold *= GameConstants.ENEMY_GOLD_MULTIPLIER;
            GameData.enemyDamage *= GameConstants.ENEMY_DAMAGE_MULTIPLIER;
            if (GameData.enemySpeed <= GameConstants.ENEMY_MAX_SPEED) GameData.enemySpeed += GameConstants.ENEMY_SPEED_INCREMENT;
        }
    }
    
    private void updateUI() {
        // ★ skillProgress 추가됨
        uiController.update(level, currentXp, requiredXp, 
                          player.currentHp, player.maxHp, elapsedTime,
                          player.getSkillProgress());
    }

    @Override
    public void render() {
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, GameMain.WIDTH, GameMain.HEIGHT);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, GameMain.WIDTH, GameMain.HEIGHT);
        }
        
        player.render(gc);
        playerBullets.forEach(b -> b.render(gc));
        enemies.forEach(e -> e.render(gc));
        bosses.forEach(b -> b.render(gc));
        enemyBullets.forEach(b -> b.render(gc));
    }
    
    private void spawnEnemy() {
        double x = random.nextDouble() * (GameMain.WIDTH - 50) + 25;
        enemies.add(new Enemy(x));
    }
}