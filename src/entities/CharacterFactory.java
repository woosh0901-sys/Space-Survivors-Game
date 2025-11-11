package entities;

import main.CharacterType;

/**
 * 캐릭터 생성을 담당하는 Factory 클래스입니다.
 */
public class CharacterFactory {
    
    /**
     * 캐릭터 타입에 따라 적절한 PlayableCharacter 인스턴스를 생성합니다.
     * @param type 캐릭터 타입
     * @param x 시작 x 좌표
     * @param y 시작 y 좌표
     * @return 생성된 PlayableCharacter 인스턴스
     */
    public static PlayableCharacter createCharacter(CharacterType type, double x, double y) {
        switch (type) {
            case TANK:
                return new Player2(x, y);
            case DEFAULT:
            default:
                return new Player(x, y);
        }
    }
    
    /**
     * 문자열 ID로부터 캐릭터를 생성합니다.
     * @param typeId 캐릭터 타입 ID
     * @param x 시작 x 좌표
     * @param y 시작 y 좌표
     * @return 생성된 PlayableCharacter 인스턴스
     */
    public static PlayableCharacter createCharacter(String typeId, double x, double y) {
        CharacterType type = CharacterType.fromId(typeId);
        return createCharacter(type, x, y);
    }
    
    private CharacterFactory() {
        // 인스턴스 생성 방지
    }
}
