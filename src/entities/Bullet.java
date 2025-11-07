package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.GameMain; // GameMain의 화면 크기 참조

/**
 * 플레이어의 총알과 적의 총알을 모두 처리하는 통합 Bullet 클래스입니다.
 * Entity 클래스를 상속받습니다.
 */
public class Bullet extends Entity {

    // --- 상수로 정의 ---
    public static final double PLAYER_BULLET_SPEED = -1080.0; // 음수 = 위로 이동
    public static final double PLAYER_BULLET_SIZE = 40.0;
    public static final Color PLAYER_BULLET_COLOR = Color.YELLOW;

    public static final double ENEMY_BULLET_SPEED = 400.0; // 양수 = 아래로 이동
    public static final double ENEMY_BULLET_SIZE = 15.0;
    public static final Color ENEMY_BULLET_COLOR = Color.MAGENTA;

    // --- private 캡슐화 ---
    private double speed; // 이동 속도 (방향 포함)
    private Color color;
    private boolean isOffScreen = false;

    /**
     * 통합 총알 생성자
     * @param startX 시작 x 좌표
     * @param startY 시작 y 좌표
     * @param size 총알 크기 (지름)
     * @param speed 총알 속도 (음수면 위로, 양수면 아래로)
     * @param color 총알 색상
     */
    public Bullet(double startX, double startY, double size, double speed, Color color) {
        // 부모(Entity) 생성자 호출
        super(startX, startY, size, size); // width와 height가 동일 (원)
        
        this.speed = speed;
        this.color = color;
    }

    /**
     * 매 프레임마다 총알을 이동시킵니다.
     */
    @Override
    public void update(double deltaTime) {
        y += speed * deltaTime; // speed 값에 따라 위 또는 아래로 이동

        // 화면 밖으로 나갔는지 체크 (위쪽 또는 아래쪽)
        if (y < 0 - height || y > GameMain.HEIGHT + height) {
            isOffScreen = true;
        }
    }

    /**
     * 총알을 화면에 그립니다.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(this.color); // 생성자에서 받은 색상으로 설정
        gc.fillOval(x - width / 2, y - height / 2, width, height); // (x,y)가 중심이 되도록
    }

    /**
     * 총알이 화면 밖에 나갔는지 여부를 반환합니다. (Getter)
     */
    public boolean isOffScreen() {
        return this.isOffScreen;
    }
}