package main;

import javafx.scene.paint.Color;

public class GameConstants {
    // 화면 크기
    public static final int SCREEN_WIDTH = 1080;
    public static final int SCREEN_HEIGHT = 1440;
    
    // 플레이어 기본값
    public static final double PLAYER_WIDTH = 80.0;
    public static final double PLAYER_HEIGHT = 80.0;
    
    // 탱크 기본값
    public static final double TANK_WIDTH = 90.0;
    public static final double TANK_HEIGHT = 90.0;
    public static final double TANK_HP_BONUS = 50.0;
    public static final double TANK_SPEED_MULTIPLIER = 0.8;
    public static final double TANK_ATTACK_SPEED_MULTIPLIER = 1.5;
    
    // 적 기본값
    public static final double ENEMY_WIDTH = 70.0;
    public static final double ENEMY_HEIGHT = 70.0;
    public static final double ENEMY_ATTACK_SPEED = 2.0;
    public static final double HEALTH_BAR_HEIGHT = 5.0;
    public static final double HEALTH_BAR_Y_OFFSET = -10.0;
    
    // 총알 기본값
    public static final double PLAYER_BULLET_SPEED = -1080.0;
    public static final double PLAYER_BULLET_SIZE = 40.0;
    public static final Color PLAYER_BULLET_COLOR = Color.YELLOW;
    public static final double ENEMY_BULLET_SPEED = 400.0;
    public static final double ENEMY_BULLET_SIZE = 15.0;
    public static final Color ENEMY_BULLET_COLOR = Color.MAGENTA;
    
    // 게임 플레이
    public static final double INITIAL_SPAWN_INTERVAL = 1.0;
    public static final double MIN_SPAWN_INTERVAL = 0.2;
    public static final double INITIAL_REQUIRED_XP = 150.0;
    public static final double XP_MULTIPLIER = 1.6;
    public static final int DIFFICULTY_TIER_DURATION = 30;
    
    // ★ 보스 시스템
    public static final double BOSS_SPAWN_INTERVAL = 120.0; // 2분마다
    public static final double FINAL_BOSS_TIME = 600.0; // 10분
    public static final double BOSS_XP_REWARD = 500.0; // 보스 처치 시 경험치
    public static final double FINAL_BOSS_XP_REWARD = 2000.0; // 최종 보스 경험치
    
    // 난이도 증가 배율
    public static final double ENEMY_HEALTH_MULTIPLIER = 1.2;
    public static final double SPAWN_INTERVAL_DIVIDER = 1.1;
    public static final double ENEMY_GOLD_MULTIPLIER = 1.2;
    public static final double ENEMY_DAMAGE_MULTIPLIER = 1.15;
    public static final double ENEMY_SPEED_INCREMENT = 15.0;
    public static final double ENEMY_MAX_SPEED = 250.0;
    
    // 경험치 확률
    public static final double XP_TRIPLE_CHANCE = 0.01;
    public static final double XP_DOUBLE_CHANCE = 0.1;
    public static final int XP_TRIPLE_MULTIPLIER = 3;
    public static final int XP_DOUBLE_MULTIPLIER = 2;
    
    // 업그레이드
    public static final double UPGRADE_COST_MULTIPLIER = 1.25;
    public static final int HP_UPGRADE_AMOUNT = 20;
    public static final int DAMAGE_UPGRADE_AMOUNT = 5;
    
    // 레벨업 버프
    public static final double HP_BUFF_PERCENTAGE = 0.10;
    public static final int DAMAGE_BUFF_AMOUNT = 15;
    public static final double GOLD_BUFF_PERCENTAGE = 0.10;
    public static final double SPEED_BUFF_AMOUNT = 50.0;
    
    // 샷건 설정
    public static final double SHOTGUN_BULLET_SIZE_MULTIPLIER = 0.8;
    public static final double SHOTGUN_BULLET_SPEED_MULTIPLIER = 0.9;
    public static final double SHOTGUN_SPREAD_OFFSET = 20.0;
    
    private GameConstants() {}
}