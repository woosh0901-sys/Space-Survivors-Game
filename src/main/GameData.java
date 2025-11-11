package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameData {

    private static final String SAVE_FILE = "save_data.txt";

    // 캐릭터 선택
    public static String selectedCharacter = CharacterType.DEFAULT.getId();

    // 재화 및 스탯 (영구)
    public static int gold = 0;
    public static int playerMaxHp = 100;
    public static int playerDamage = 50;
    public static double playerAttackSpeed = 0.8; // ★ 시작 값 (0.8)
    public static double playerMoveSpeed = 450; 

    // 업그레이드 레벨 및 비용
    public static int hpLevel = 1;
    public static int damageLevel = 1;
    public static int attackSpeedLevel = 1;
    
    public static double hpUpgradeCost = 50;
    public static double damageUpgradeCost = 70;
    public static double attackSpeedUpgradeCost = 100;
    
    public static final int MAX_UPGRADE_LEVEL = 30;

    // ★ 공격 속도 계산을 위한 새 상수 ★
    public static final double ATTACK_SPEED_START = 0.8;
    public static final double ATTACK_SPEED_END = 0.5;

    // 게임 내 변수 (게임 시작 시 초기화됨)
    public static double enemyBaseHealth = 50;
    public static double enemyBaseXP = 25;
    public static double enemyBaseGold = 10;
    public static double enemyDamage = 10;
    public static double enemySpeed = 150;
    
    public static void save() {
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            writer.write(selectedCharacter + "\n");
            writer.write(gold + "\n");
            writer.write(playerMaxHp + "\n");
            writer.write(playerDamage + "\n");
            writer.write(playerAttackSpeed + "\n"); // 현재 값 저장
            writer.write(playerMoveSpeed + "\n");
            writer.write(hpLevel + "\n");
            writer.write(damageLevel + "\n");
            writer.write(attackSpeedLevel + "\n");
            writer.write(hpUpgradeCost + "\n");
            writer.write(damageUpgradeCost + "\n");
            writer.write(attackSpeedUpgradeCost + "\n");
            System.out.println("게임 데이터가 저장되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void load() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("저장된 데이터가 없습니다.");
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            selectedCharacter = scanner.nextLine();
            gold = Integer.parseInt(scanner.nextLine());
            playerMaxHp = Integer.parseInt(scanner.nextLine());
            playerDamage = Integer.parseInt(scanner.nextLine());
            playerAttackSpeed = Double.parseDouble(scanner.nextLine()); // 일단 저장된 값 로드
            playerMoveSpeed = Double.parseDouble(scanner.nextLine());
            hpLevel = Integer.parseInt(scanner.nextLine());
            damageLevel = Integer.parseInt(scanner.nextLine());
            attackSpeedLevel = Integer.parseInt(scanner.nextLine());
            hpUpgradeCost = Double.parseDouble(scanner.nextLine());
            damageUpgradeCost = Double.parseDouble(scanner.nextLine());
            attackSpeedUpgradeCost = Double.parseDouble(scanner.nextLine());
            
            // ★ 중요: 로드된 레벨을 기반으로 공속 값을 강제 재계산
            // (이전 버전의 잘못된 공속 값을 덮어쓰기)
            playerAttackSpeed = calculateAttackSpeedByLevel(attackSpeedLevel);
            
            System.out.println("게임 데이터를 불러왔습니다. (캐릭터: " + selectedCharacter + ")");
        } catch (Exception e) {
            System.err.println("데이터 불러오기 오류. 파일을 삭제합니다.");
            file.delete();
            reset(); // 로드 실패 시 초기화
        }
    }
    
    public static void reset() {
        selectedCharacter = CharacterType.DEFAULT.getId();
        gold = 0;
        playerMaxHp = 100;
        playerDamage = 50;
        playerMoveSpeed = 450; 
        hpLevel = 1;
        damageLevel = 1;
        attackSpeedLevel = 1;
        hpUpgradeCost = 50;
        damageUpgradeCost = 70;
        attackSpeedUpgradeCost = 100;
        enemyDamage = 10;
        
        // ★ 중요: 리셋 시에도 재계산
        playerAttackSpeed = calculateAttackSpeedByLevel(attackSpeedLevel);
    }
    
    // --- 업그레이드 로직 ---
    
    private static boolean performUpgrade(double cost, Runnable upgradeAction) {
        if (gold >= cost) {
            gold -= (int)cost; 
            upgradeAction.run(); 
            return true;
        }
        return false;
    }

    public static boolean upgradeMaxHp() {
        if (hpLevel >= MAX_UPGRADE_LEVEL) {
            return false;
        }
        
        return performUpgrade(hpUpgradeCost, () -> {
            playerMaxHp += GameConstants.HP_UPGRADE_AMOUNT;
            hpLevel++;
            hpUpgradeCost *= GameConstants.UPGRADE_COST_MULTIPLIER;
        });
    }
    
    public static boolean upgradeDamage() {
        if (damageLevel >= MAX_UPGRADE_LEVEL) {
            return false;
        }
        
        return performUpgrade(damageUpgradeCost, () -> {
            playerDamage += GameConstants.DAMAGE_UPGRADE_AMOUNT;
            damageLevel++;
            damageUpgradeCost *= GameConstants.UPGRADE_COST_MULTIPLIER;
        });
    }
    
    /**
     * ★ 공격 속도 업그레이드 (선형 보간 로직 적용) ★
     */
    public static boolean upgradeAttackSpeed() {
        if (attackSpeedLevel >= MAX_UPGRADE_LEVEL) {
            return false; // 최대 레벨
        }
        
        return performUpgrade(attackSpeedUpgradeCost, () -> {
            attackSpeedLevel++; // 레벨 증가
            
            // ★ 핵심: 레벨에 맞춰 값을 '계산'하여 '덮어쓰기'
            playerAttackSpeed = calculateAttackSpeedByLevel(attackSpeedLevel); 
            
            attackSpeedUpgradeCost *= GameConstants.UPGRADE_COST_MULTIPLIER;
        });
    }

    /**
     * ★ 레벨에 따라 공격 속도를 계산하는 헬퍼 메서드 ★
     * (레벨 1: 0.8, 레벨 30: 0.5)
     */
    private static double calculateAttackSpeedByLevel(int level) {
        if (level <= 1) return ATTACK_SPEED_START;
        if (level >= MAX_UPGRADE_LEVEL) return ATTACK_SPEED_END;

        // (레벨 1~30 범위를 0.0~1.0 비율로 변환)
        // (level - 1) / (30 - 1)
        double progress = (double)(level - 1) / (double)(MAX_UPGRADE_LEVEL - 1); // (29)
        
        // 선형 보간: Start + (End - Start) * progress
        // 0.8 + (0.5 - 0.8) * progress
        // 0.8 + (-0.3) * progress
        double speed = ATTACK_SPEED_START + (ATTACK_SPEED_END - ATTACK_SPEED_START) * progress;
        
        return speed;
    }
}