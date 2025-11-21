package entities;

import main.CharacterType;

public class CharacterFactory {
    
    public static PlayableCharacter createCharacter(CharacterType type, double x, double y) {
        switch (type) {
            case WOO:
                return new Player3(x, y); // 우서현 -> Player3
            case LEE:
                return new Player4(x, y); // 이정환 -> Player4
            case DEFAULT:
            default:
                return new Player(x, y);  // 박종화 -> Player
        }
    }
    
    public static PlayableCharacter createCharacter(String typeId, double x, double y) {
        CharacterType type = CharacterType.fromId(typeId);
        return createCharacter(type, x, y);
    }
    
    private CharacterFactory() {}
}