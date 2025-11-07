package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.GameData;
import main.GameMain; // 화면 높이 참조

/**
 * 적(Enemy) 객체를 정의하는 클래스입니다.
 * Entity 클래스를 상속받습니다.
 */
public class Enemy extends Entity {

    // --- 상수로 정의 ---
    private static final double DEFAULT_WIDTH = 70.0;
    private static final double DEFAULT_HEIGHT = 70.0;
    private static final String IMAGE_PATH = "/images/enemy.png";
    
    // 체력 바 관련 상수
    private static final double HEALTH_BAR_HEIGHT = 5.0; // 체력 바 높이
    private static final double HEALTH_BAR_Y_OFFSET = -10.0; // 적 이미지 상단으로부터의 y간격

    // --- private 캡슐화 ---
    private double speed;
    private Image image;
    private boolean isOffScreen = false;
    
    private double health;
    private double maxHealth;
    
    private double enemyshootCooldown = 0;
    private double enemyattackSpeed = 2.0; // (이 값도 GameData에서 관리하는 것을 권장)

    /**
     * 적 생성자
     * @param startX 시작 x 좌표 (y 좌표는 화면 위로 고정)
     */
    public Enemy(double startX) {
        // 부모(Entity) 생성자 호출. y좌표는 화면 바로 위(-height)
        super(startX, 0 - DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
        // GameData의 값으로 스탯 초기화
        this.maxHealth = GameData.enemyBaseHealth;
        this.health = this.maxHealth;
        this.speed = GameData.enemySpeed;
        
        // 공격 쿨타임 무작위 설정 (등장하자마자 동시에 쏘는 것 방지)
        this.enemyshootCooldown = Math.random() * enemyattackSpeed;
        
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("적 이미지 로딩 실패! " + IMAGE_PATH + " 파일을 확인하세요.");
        }
    }
    
    /**
     * 적이 데미지를 입었을 때 호출됩니다.
     * @param damage 입은 데미지 양
     */
    public void takeDamage(double damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            this.destroy(); // 부모 Entity의 destroy() 메소드 호출
        }
    }
    
    /**
     * 매 프레임마다 적의 상태(이동)를 갱신합니다.
     */
    @Override
    public void update(double deltaTime) {
        y += speed * deltaTime; // 아래로 이동
        
        // 화면 아래로 완전히 사라졌는지 체크
        if (y > GameMain.HEIGHT + height) { // 1080 대신 GameMain.HEIGHT 사용
            isOffScreen = true;
        }
    }
    
    /**
     * 적의 AI(총알 발사) 로직을 처리합니다.
     * @param deltaTime 프레임 시간
     * @return 발사된 총알 (발사 안하면 null)
     */
    public Bullet updateAI(double deltaTime) {
        enemyshootCooldown -= deltaTime;
        
        // 쿨타임이 다 되면 발사
        if (enemyshootCooldown <= 0) {
            enemyshootCooldown = enemyattackSpeed; // 쿨타임 초기화
            
            // 통합 Bullet 클래스의 생성자 사용
            return new Bullet(
                this.x, 
                this.y, 
                Bullet.ENEMY_BULLET_SIZE, 
                Bullet.ENEMY_BULLET_SPEED, 
                Bullet.ENEMY_BULLET_COLOR
            );
        }
        return null; // 총알을 발사하지 않음
    }

    /**
     * 적을 화면에 그립니다.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (image != null) {
            // (x, y)가 중심이 되도록 이미지 그리기
            gc.drawImage(image, x - width / 2, y - height / 2, width, height);
            
            // 체력이 닳았을 때만 체력 바 그리기
            if (health < maxHealth) {
                double hpBarY = y - height / 2 + HEALTH_BAR_Y_OFFSET;
                
                // 체력 바 배경 (어두운 빨강)
                gc.setFill(Color.DARKRED);
                gc.fillRect(x - width / 2, hpBarY, width, HEALTH_BAR_HEIGHT);
                
                // 현재 체력 (밝은 빨강)
                double hpPercentage = health / maxHealth;
                gc.setFill(Color.RED);
                gc.fillRect(x - width / 2, hpBarY, width * hpPercentage, HEALTH_BAR_HEIGHT);
            }
        } else {
            // 이미지 로딩 실패 시 대체 사각형 (크기를 상수에 맞게 수정)
            gc.setFill(Color.RED);
            gc.fillRect(x - width / 2, y - height / 2, width, height);
        }
    }
    
    /**
     * 화면 밖에 나갔는지 여부를 반환합니다. (Getter)
     */
    public boolean isOffScreen() {
        return this.isOffScreen;
    }
}