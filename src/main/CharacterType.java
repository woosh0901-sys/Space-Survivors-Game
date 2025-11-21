package main;

public enum CharacterType {
    DEFAULT("박종화", "DEFAULT"), // 기본 (Player.java)
    WOO("우서현", "WOO"),         // 골드 (Player3.java)
    LEE("이정환", "LEE");         // 공격 (Player4.java)
    
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
    
    public static CharacterType fromId(String id) {
        for (CharacterType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return DEFAULT;
    }
}