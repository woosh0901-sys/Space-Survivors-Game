package gamestates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Bullet; 
import entities.Enemy;
import entities.PlayableCharacter; // ★ Player 대신 PlayableCharacter
import entities.Player;             // (DEFAULT 로드용)
import entities.Player2;         // (TANK 로드용)
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
    // --- ★ 플레이어 타입 변경 ---
    private PlayableCharacter player; 
    
    private List<Bullet> playerBullets; 
    private List<Enemy> enemies;
    private List<Bullet> enemyBullets; 
    
    private int level;
    private double currentXp, requiredXp;
    
    private double spawnTimer;
    private double spawnInterval;
    private Random random;
    
    private double elapsedTime = 0;
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
    	// --- ★ 캐릭터 로딩 부분 수정 ---
        double startX = GameMain.WIDTH / 2;
        double startY = GameMain.HEIGHT - 50;
        
        if (GameData.selectedCharacter.equals("TANK")) {
            player = new Player2(startX, startY);
            System.out.println("탱크 캐릭터로 시작합니다.");
        } else {
            // 기본값은 'DEFAULT' 캐릭터 (Player.java)
            player = new Player(startX, startY);
            System.out.println("기본 캐릭터로 시작합니다.");
        }
        
        playerBullets = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyBullets = new ArrayList<>(); 
        
        level = 1;
        currentXp = 0;
        requiredXp = 150;
        spawnTimer = 0;
        spawnInterval = 1.0;
        random = new Random();
        elapsedTime = 0;
        difficultyTier = 0;
        
        // 게임 시작 시 스탯 초기화
        GameData.enemyBaseHealth = 50;
        GameData.enemyBaseXP = 25;
        GameData.enemyBaseGold = 10;
        GameData.enemyDamage = 10;
        GameData.enemySpeed = 150;
    }
    
    /**
     * GameMain에서 스페이스바를 눌렀을 때 호출됩니다.
     * (공격 방식이 List<Bullet>로 변경됨에 따라 수정)
     */
    public void shoot() {
        // player.attack()이 이제 List<Bullet>을 반환합니다.
        List<Bullet> newBullets = player.attack(); // ★ player.shoot() -> player.attack()
        
        if (newBullets != null && !newBullets.isEmpty()) {
            playerBullets.addAll(newBullets); // ★ 리스트에 모두 추가
        }
    }
       
    public PlayableCharacter getPlayer() { return player; } // ★ 반환 타입 변경
    
    @Override
    public void update(double deltaTime) {
        // --- 1. 상태 업데이트 (시간, 쿨타임 등) ---
        elapsedTime += deltaTime;
        
        // --- 2. 객체 위치 및 상태 업데이트 (리팩토링된 방식) ---
        
        // 2a. 플레이어 업데이트 (쿨다운, 이동)
        player.update(deltaTime); // 쿨다운 갱신
        player.handleInputAndMove(GameMain.getActiveKeys(), deltaTime); // 키 입력 및 이동 처리
        // (수동 발사는 shoot() 메소드로 분리됨)

        // 2b. 적 업데이트 (이동, AI 발사)
        List<Bullet> newEnemyBullets = new ArrayList<>(); 
        for (Enemy e : enemies) {
            e.update(deltaTime); // 이동 처리
            Bullet newEnemyBullet = e.updateAI(deltaTime); // AI 및 발사 처리
            if (newEnemyBullet != null) {
                newEnemyBullets.add(newEnemyBullet);
            }
        }
        enemyBullets.addAll(newEnemyBullets); // 새로 발사된 총알을 메인 리스트에 추가

        // 2c. 총알 업데이트
        for (Bullet b : playerBullets) b.update(deltaTime);
        for (Bullet eb : enemyBullets) eb.update(deltaTime);
        
        
        // --- 3. 충돌 감지 (collidesWith 사용) ---
        
        // 3a. 플레이어 총알 vs 적
        for (Bullet bullet : playerBullets) {
            if (bullet.isDestroyed()) continue;
            for (Enemy enemy : enemies) {
                if (enemy.isDestroyed()) continue;
                
                if (bullet.collidesWith(enemy)) {
                    bullet.destroy(); 
                    enemy.takeDamage(player.damage); // ★ 캐릭터의 데미지
                    
                    if (enemy.isDestroyed()) {
                        // 경험치 및 골드 획득 로직
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
        
        // 3b. 적 총알 vs 플레이어
        for (Bullet eb : enemyBullets) { 
            if (eb.isDestroyed() || player.isDestroyed()) continue;
            
            if (player.collidesWith(eb)) { 
                player.takeDamage(GameData.enemyDamage); 
                eb.destroy(); 
            }
        }

        // 3c. 플레이어 vs 적
        for (Enemy enemy : enemies) {
            if (enemy.isDestroyed() || player.isDestroyed()) continue;

            if (player.collidesWith(enemy)) {
                player.takeDamage(GameData.enemyDamage); 
                enemy.destroy(); 
            }
        }

        // --- 4. 객체 정리 (Getter 사용) ---
        playerBullets.removeIf(b -> b.isOffScreen() || b.isDestroyed());
        enemies.removeIf(e -> e.isOffScreen() || e.isDestroyed());
        enemyBullets.removeIf(eb -> eb.isOffScreen() || eb.isDestroyed());

        // --- 5. 게임 상태 변경 (레벨업, 게임 오버) ---
        if (player.isDestroyed()) { 
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
        updateDifficulty(deltaTime);
        uiController.update(level, currentXp, requiredXp, player.currentHp, player.maxHp, elapsedTime);
    }
    
    private void updateDifficulty(double deltaTime) {
        if (elapsedTime > (difficultyTier + 1) * 30) {
            difficultyTier++;
            GameData.enemyBaseHealth *= 1.2;
            if (spawnInterval > 0.2) spawnInterval /= 1.1;
            GameData.enemyBaseGold *= 1.2;
            GameData.enemyDamage *= 1.5;
            if(GameData.enemySpeed <= 250)
            GameData.enemySpeed +=15;
            // System.out.println(String.format("난이도 증가! ..."));
        }
    }

    @Override
    public void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GameMain.WIDTH, GameMain.HEIGHT);
        
        player.render(gc);
        for (Bullet b : playerBullets) b.render(gc);
        for (Enemy e : enemies) e.render(gc);
        for (Bullet eb : enemyBullets) eb.render(gc); 
    }
    
    private void spawnEnemy() {
        double x = random.nextDouble() * (GameMain.WIDTH - 50) + 25;
        enemies.add(new Enemy(x));
    }
}