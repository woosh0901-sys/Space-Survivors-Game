package entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 * '탱크' 플레이어 캐릭터입니다.
 * PlayableCharacter 클래스를 상속받습니다.
 * (체력은 높고, 이동속도는 느리며, 샷건을 쏩니다)
 */
public class Player2 extends PlayableCharacter {

    // 탱크 캐릭터의 고유 설정
    private static final double TANK_WIDTH = 90.0;  // 크기를 키움
    private static final double TANK_HEIGHT = 90.0;
    private static final String IMAGE_PATH = "/images/tank_player.png"; // (새 이미지 경로)

    public Player2(double startX, double startY) {
        // 1. 부모 생성자 호출
        super(startX, startY, TANK_WIDTH, TANK_HEIGHT);

        // 2. 기본 스탯 조정 (GameData의 영구 업그레이드 값에 추가 적용)
        this.maxHp += 50; // 기본 체력 +50
        this.currentHp = this.maxHp;
        this.speed *= 0.8; // 이동 속도 20% 감소
        
        try {
            // ★ 탱크 이미지 로드
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            // 이미지가 없으면 부모(PlayableCharacter)의 render가 파란 네모를 그려줌
            System.err.println("탱크 이미지 로딩 실패! " + IMAGE_PATH + " 파일을 확인하세요.");
        }
    }

    /**
     * '탱크' 캐릭터의 공격 방식 (총알 3발 샷건 발사)
     */
    @Override
    public List<Bullet> attack() {
        if (shootCooldown <= 0) {
            shootCooldown = this.attackSpeed * 1.5; // 공격 속도는 50% 느림
            
            List<Bullet> bullets = new ArrayList<>();
            
            // 중앙 총알 (기본 총알과 동일)
            bullets.add(new Bullet(
                this.x, 
                this.y, 
                Bullet.PLAYER_BULLET_SIZE, 
                Bullet.PLAYER_BULLET_SPEED, 
                Bullet.PLAYER_BULLET_COLOR
            ));
            
            // ★ 왼쪽 총알 (가짜 속도: 속도를 줄여서 대각선 구현)
            bullets.add(new Bullet(
                this.x - 20, // 살짝 왼쪽에서 시작
                this.y, 
                Bullet.PLAYER_BULLET_SIZE * 0.8, // 크기 80%
                Bullet.PLAYER_BULLET_SPEED * 0.9, // y속도 90%
                Bullet.PLAYER_BULLET_COLOR
            ));
            
            // ★ 오른쪽 총알
            bullets.add(new Bullet(
                this.x + 20, // 살짝 오른쪽에서 시작
                this.y, 
                Bullet.PLAYER_BULLET_SIZE * 0.8, 
                Bullet.PLAYER_BULLET_SPEED * 0.9,
                Bullet.PLAYER_BULLET_COLOR
            ));
            
            return bullets;
        }
        return null; // 쿨타임 중
    }
}