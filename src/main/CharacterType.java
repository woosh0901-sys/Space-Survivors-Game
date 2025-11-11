package main;

/**
 * 캐릭터 타입을 정의하는 Enum입니다.
 */
public enum CharacterType {
    DEFAULT("기본 캐릭터", "DEFAULT"),
    TANK("탱크 캐릭터", "TANK");
    
    private final String displayName;
    private final String id;
    
    CharacterType(String displayName, String id) {
        this.displayName = displayName;
        this.id = id;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getId() {
        return id;
    }
    
    /**
     * ID 문자열로부터 CharacterType을 찾습니다.
     * @param id 캐릭터 ID
     * @return 해당하는 CharacterType (없으면 DEFAULT)
     */
    public static CharacterType fromId(String id) {
        for (CharacterType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return DEFAULT;
    }
}
