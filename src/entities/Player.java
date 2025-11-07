package entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 * '기본' 플레이어 캐릭터입니다.
 * PlayableCharacter 클래스를 상속받습니다.
 */
public class Player extends PlayableCharacter { // ★ Entity 대신 PlayableCharacter 상속

    private static final double DEFAULT_WIDTH = 80.0;
    private static final double DEFAULT_HEIGHT = 80.0;
    private static final String IMAGE_PATH = "/images/player.png";

    public Player(double startX, double startY) {
        // 1. 부모 생성자 호출
        super(startX, startY, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // 2. 기본 스탯 적용 (GameData에서 이미 불러옴)
        
        try {
            image = new Image(getClass().getResourceAsStream(IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("플레이어 이미지 로딩 실패! " + IMAGE_PATH + " 파일을 확인하세요.");
        }
    }

    /**
     * '기본' 캐릭터의 공격 방식 (총알 1발 발사)
     */
    @Override
    public List<Bullet> attack() { // ★ 반환 타입 List<Bullet>
        if (shootCooldown <= 0) {
            shootCooldown = this.attackSpeed; 
            
            // 리스트를 생성해서 반환
            List<Bullet> bullets = new ArrayList<>();
            bullets.add(new Bullet(
                this.x, 
                this.y, 
                Bullet.PLAYER_BULLET_SIZE, 
                Bullet.PLAYER_BULLET_SPEED, 
                Bullet.PLAYER_BULLET_COLOR
            ));
            return bullets;
        }
        return null; // 쿨타임 중이므로 null 반환
    }
    
    // handleInputAndMove, update, render, takeDamage, applyBuff 등은
    // 모두 부모인 PlayableCharacter가 처리합니다.
}