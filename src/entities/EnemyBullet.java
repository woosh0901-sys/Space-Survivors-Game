package entities; // 이 파일이 com.mygame.entities 패키지에 속해 있음을 선언합니다.

import javafx.scene.canvas.GraphicsContext; // 캔버스에 그래픽을 그리기 위한 GraphicsContext 클래스를 가져옵니다.
import javafx.scene.paint.Color; // 색상을 다루기 위한 Color 클래스를 가져옵니다.
import main.GameMain;

public class EnemyBullet { // 총알 객체를 정의하는 Bullet 클래스입니다.

    public double x, y; // 총알의 현재 x, y 좌표를 저장하는 변수입니다.
    private double speed = 400; // 총알의 이동 속도를 설정합니다 (플레이어보다 빠르게).
    private double size = 15; // 총알의 지름(크기)을 설정합니다.
    
    // 총알이 화면 밖으로 나갔는지 확인하기 위한 변수 // 상태 변수에 대한 설명 주석입니다.
    public boolean isOffScreen = false; // 총알이 화면 밖으로 나갔는지 여부를 저장하는 변수입니다.
    public boolean isDestroyed = false; // 총알이 적과 충돌하여 파괴되었는지 여부를 저장하는 변수입니다.
    
    // 생성자: 총알이 생성될 위치를 받는다 // 생성자의 역할을 설명하는 주석입니다.
    public EnemyBullet(double startX, double startY) { // Bullet 객체를 생성하는 생성자입니다. 총알의 시작 위치를 매개변수로 받습니다.
        this.x = startX; // x 좌표를 생성 시 받은 startX 값으로 초기화합니다.
        this.y = startY; // y 좌표를 생성 시 받은 startY 값으로 초기화합니다.
    }

    // 매 프레임마다 호출되어 총알을 위로 움직인다 // update 메소드의 역할을 설명하는 주석입니다.
    public void update(double deltaTime) { // 매 프레임마다 총알의 상태를 갱신하는 메소드입니다.
        y += speed * deltaTime; // 속도와 프레임 시간을 곱한 만큼 y 좌표를 감소시켜 총알을 위로 이동시킵니다.
        
        // 총알이 화면 위쪽으로 완전히 사라졌는지 체크 // 화면 이탈 확인에 대한 설명 주석입니다.
        if (y > GameMain.HEIGHT + size) { // 총알의 y좌표가 화면 상단(-size)을 넘어갔는지 확인합니다.
            isOffScreen = true; // 화면 밖으로 나갔다고 표시합니다.
        }
    }

    // 화면에 총알을 그린다 // render 메소드의 역할을 설명하는 주석입니다.
    public void render(GraphicsContext gc) { // 화면에 총알을 그리는 메소드입니다.
        gc.setFill(Color.MAGENTA); // 그리기 색상을 노란색으로 설정합니다.
        gc.fillOval(x - size / 2, y - size / 2, size, size); // (x, y)가 중심이 되도록 원(총알)을 채워서 그립니다.
    }
    
    public double getRadius() { // 총알의 충돌 반경을 반환하는 메소드입니다.
        return size / 2; // 총알 크기(지름)의 절반을 반경으로 반환합니다.
    }
}