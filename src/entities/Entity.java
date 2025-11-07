package entities;

import javafx.scene.canvas.GraphicsContext;

/**
 * 게임에 등장하는 모든 객체(플레이어, 적, 총알 등)의
 * 공통 속성과 기능을 정의하는 추상 클래스입니다.
 */
public abstract class Entity {

    // protected: 자식 클래스에서는 접근 가능, 외부에서는 직접 접근 불가
    protected double x, y;
    protected double width, height;
    protected boolean isDestroyed = false;

    /**
     * Entity 생성자
     * @param startX 시작 x 좌표
     * @param startY 시작 y 좌표
     * @param width 객체 너비
     * @param height 객체 높이
     */
    public Entity(double startX, double startY, double width, double height) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
    }

    // --- 추상 메소드 (자식 클래스가 반드시 구현) ---
    
    /**
     * 매 프레임마다 객체의 상태를 갱신합니다.
     * (예: 이동, 쿨다운 계산 등)
     */
    public abstract void update(double deltaTime);
    
    /**
     * 객체를 화면에 그립니다.
     */
    public abstract void render(GraphicsContext gc);

    
    // --- 공통 메소드 ---

    /**
     * 객체를 파괴 상태로 만듭니다.
     */
    public void destroy() {
        this.isDestroyed = true;
    }

    /**
     * 객체가 파괴되었는지 여부를 반환합니다.
     * @return 파괴되었으면 true
     */
    public boolean isDestroyed() {
        return this.isDestroyed;
    }

    /**
     * 다른 Entity와 충돌했는지 원형 기준으로 검사합니다.
     * @param other 충돌을 검사할 다른 Entity
     * @return 충돌했으면 true
     */
    public boolean collidesWith(Entity other) {
        // 두 객체 중심점 사이의 거리 계산
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // 두 객체의 반지름 합보다 거리가 가까우면 충돌
        return distance < this.getRadius() + other.getRadius();
    }

    /**
     * 객체의 충돌 반경을 반환합니다. (기본: 너비의 절반)
     */
    public double getRadius() {
        return width / 2;
    }
    
    // public getter (필요시 추가)
    public double getX() { return this.x; }
    public double getY() { return this.y; }
}