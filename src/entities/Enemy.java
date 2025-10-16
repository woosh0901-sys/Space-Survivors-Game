package entities; // 이 파일이 com.mygame.entities 패키지에 속해 있음을 선언합니다.

import java.util.List;
import javafx.scene.canvas.GraphicsContext; // 캔버스에 그래픽을 그리기 위한 GraphicsContext 클래스를 가져옵니다.
import javafx.scene.image.Image; // 이미지를 다루기 위한 Image 클래스를 가져옵니다.
import javafx.scene.paint.Color; // 색상을 다루기 위한 Color 클래스를 가져옵니다.
import main.GameData;

public class Enemy { // 적 객체를 정의하는 Enemy 클래스입니다.

    public double x, y; // 적의 현재 x, y 좌표를 저장하는 변수입니다.
    private double speed = 150; // 적의 이동 속도를 설정합니다 (초당 150픽셀).
    private Image image; // 적의 이미지를 저장할 변수입니다.
    
    public boolean isOffScreen = false; // 적이 화면 밖으로 나갔는지 여부를 저장하는 변수입니다.
    public boolean isDestroyed = false; // 적이 파괴되었는지 여부를 저장하는 변수입니다.
    
    // ★★★ 체력 변수와 크기 변수 추가 ★★★ // 추가된 변수들에 대한 설명 주석입니다.
    private double health; // 적의 현재 체력을 저장하는 변수입니다.
    private double maxHealth; // 적의 최대 체력을 저장하는 변수입니다.
    private double width = 70; // 적 이미지의 너비를 설정합니다.
    private double height = 70; // 적 이미지의 높이를 설정합니다.
    
    private double enemyshootCooldown = 0;
    public double enemyattackSpeed = 2.0;
    
    public Enemy(double startX) { // Enemy 객체를 생성하는 생성자입니다. 시작 x좌표를 매개변수로 받습니다.
        this.x = startX; // x 좌표를 생성 시 받은 startX 값으로 초기화합니다.
        this.y = 0 - height; // y 좌표를 화면 바로 위에서 시작하도록 설정합니다.
        
        // GameData의 값으로 체력 초기화 // 체력 초기화에 대한 설명 주석입니다.
        this.maxHealth = GameData.enemyBaseHealth; // GameData에 저장된 값으로 최대 체력을 설정합니다.
        this.health = this.maxHealth; // 현재 체력을 최대 체력과 동일하게 설정합니다.
        this.speed = GameData.enemySpeed;
        
        this.enemyshootCooldown = Math.random() * enemyattackSpeed;
        
        try { // 예외가 발생할 수 있는 코드를 시도합니다.
            image = new Image(getClass().getResourceAsStream("/images/enemy.png")); //
        } catch (Exception e) { // 이미지 로딩 중 예외가 발생하면 실행됩니다.
            System.err.println("이미지 로딩 실패! resources/images/enemy.png 파일을 확인하세요."); // 콘솔에 오류 메시지를 출력합니다.
        }
    }
    
    // ★★★ 데미지를 받는 메소드 추가 ★★★ // 추가된 메소드에 대한 설명 주석입니다.
    public void takeDamage(double damage) { // 적이 데미지를 입었을 때 호출되는 메소드입니다.
        this.health -= damage; // 받은 데미지만큼 현재 체력을 감소시킵니다.
        if (this.health <= 0) { // 만약 체력이 0 이하가 되면,
            this.isDestroyed = true; // 파괴 상태를 true로 변경합니다.
        }
    }
    
    public void enemyshoot(List<EnemyBullet> enemyBullets) {
        	enemyBullets.add(new EnemyBullet(this.x, this.y)); // player.x 대신 this.x 사용
            this.enemyshootCooldown = this.enemyattackSpeed;        // player.attackSpeed 대신 this.attackSpeed 사용
    }
    
    public void update(double deltaTime, List<EnemyBullet> enemyBullets) { // 매 프레임마다 적의 상태를 갱신하는 메소드입니다.
        y += speed * deltaTime; // 속도와 프레임 시간을 곱한 만큼 y좌표를 증가시켜 아래로 이동시킵니다.
        if (y > 1080) { // 만약 적이 화면 아래(y=650)를 넘어가면,
            isOffScreen = true; // 화면 밖으로 나갔다고 표시합니다.
        }
        
        enemyshootCooldown -= deltaTime;
        // 쿨타임이 다 되면 발사
        if (enemyshootCooldown <= 0) {
            enemyshoot(enemyBullets);
        }
    }

    public void render(GraphicsContext gc) { // 화면에 적을 그리는 메소드입니다.
        if (image != null) { // 이미지가 성공적으로 로드되었는지 확인합니다.
            gc.drawImage(image, x - width / 2, y - height / 2, width, height); // (x, y)가 중심이 되도록 이미지를 그립니다.
            
            // ★★★ 적 체력 바 그리기 추가 ★★★ // 체력 바 그리기에 대한 설명 주석입니다.
            if (health < maxHealth) { // 체력이 최대 체력보다 적을 때만 (즉, 피해를 입었을 때만) 체력 바를 그립니다.
                // 체력 바 배경 // 체력 바의 배경 부분을 그립니다.
                gc.setFill(Color.DARKRED); // 그리기 색상을 어두운 빨간색으로 설정합니다.
                gc.fillRect(x - width / 2, y - height / 2 - 10, width, 5); // 적 이미지 위쪽에 체력 바 배경을 그립니다.
                // 현재 체력 // 현재 남은 체력을 표시하는 부분을 그립니다.
                double hpPercentage = health / maxHealth; // 현재 체력의 비율을 계산합니다 (0.0 ~ 1.0).
                gc.setFill(Color.RED); // 그리기 색상을 밝은 빨간색으로 설정합니다.
                gc.fillRect(x - width / 2, y - height / 2 - 10, width * hpPercentage, 5); // 체력 비율만큼 막대를 그립니다.
            }
        } else { // 이미지가 로드되지 않았을 경우,
            gc.setFill(Color.RED); // 그리기 색상을 빨간색으로 설정합니다.
            gc.fillRect(x - 15, y - 15, 30, 30); // 대체 도형으로 빨간 사각형을 그립니다.
        }
    }
    
    public double getRadius() { // 적의 충돌 반경을 반환하는 메소드입니다.
        return width / 2; // 적 너비의 절반을 반경으로 반환합니다.
    }
}