package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.GameMain;

public class Bullet extends Entity {

    // 상수
    public static final double PLAYER_BULLET_SIZE = 45;
    public static final double PLAYER_BULLET_SPEED = 600;
    
    public static final double ENEMY_BULLET_SIZE = 20;
    public static final double ENEMY_BULLET_SPEED = 400;
    
    // ★ 적 총알 색상 (원래대로)
    public static final Color ENEMY_BULLET_COLOR = Color.RED; 

    // ★ 플레이어 총알만 이미지 사용
    private static final String IMAGE_PATH = "/images/bullet.png";
    private static Image bulletImage;

    private double dx, dy;
    private boolean isEnemy; // 적 총알인지 여부

    // 이미지 로딩 (플레이어용만 로드)
    static {
        try {
            bulletImage = new Image(Bullet.class.getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("플레이어 총알 이미지 로딩 실패! 경로를 확인하세요.");
        }
    }

    public Bullet(double x, double y, double size, double speed, boolean isEnemy) {
        super(x, y, size, size);
        this.isEnemy = isEnemy;

        // 적이면 아래로(+), 플레이어면 위로(-) 날아감
        if (isEnemy) {
            this.dy = speed; 
        } else {
            this.dy = -speed;
        }
        this.dx = 0;
    }
    
    // (보스용 호환 생성자) Color를 받아도 무조건 적으로 취급
    public Bullet(double x, double y, double size, double speed, Color color) {
        this(x, y, size, speed, true); 
    }

    @Override
    public void update(double deltaTime) {
        x += dx * deltaTime;
        y += dy * deltaTime;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isEnemy) {
            // ★ 적 총알은 원래대로 '빨간색 원' 그리기
            gc.setFill(ENEMY_BULLET_COLOR);
            gc.fillOval(x, y, width, height);
        } else {
            // ★ 플레이어 총알은 '이미지' 그리기
            if (bulletImage != null) {
                gc.drawImage(bulletImage, x, y, width, height);
            } else {
                // 이미지가 없으면 노란색 원 (비상용)
                gc.setFill(Color.YELLOW);
                gc.fillOval(x, y, width, height);
            }
        }
    }
    
    public boolean isOffScreen() {
        return y + height < 0 || y > GameMain.HEIGHT || x + width < 0 || x > GameMain.WIDTH;
    }
}