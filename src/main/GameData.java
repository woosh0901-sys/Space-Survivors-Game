package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameData {

    private static final String SAVE_FILE = "save_data.txt";

    // 재화 및 스탯 (영구 저장됨)
    public static int gold = 0;
    public static int playerMaxHp = 100;
    public static int playerDamage = 50;
    public static double playerAttackSpeed = 0.5;
    public static double playerMoveSpeed = 450;

    // 업그레이드 레벨 및 비용 (영구 저장됨)
    public static int hpLevel = 1;
    public static int damageLevel = 1;
    public static int attackSpeedLevel = 1;
    
    public static double hpUpgradeCost = 50;
    public static double damageUpgradeCost = 70;
    public static double attackSpeedUpgradeCost = 100;

    // 시간에 따라 변하는 게임 내 변수 (저장 안 함)
    public static double enemyBaseHealth = 50;
    public static double enemyBaseXP = 25;
    public static double enemyBaseGold = 10;
    public static double enemyDamage = 10;
    public static double enemySpeed = 150;
    
    public static void save() {
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            writer.write(gold + "\n");
            writer.write(playerMaxHp + "\n");
            writer.write(playerDamage + "\n");
            writer.write(playerAttackSpeed + "\n");
            writer.write(playerMoveSpeed + "\n"); // 이동속도 저장
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
            gold = Integer.parseInt(scanner.nextLine());
            playerMaxHp = Integer.parseInt(scanner.nextLine());
            playerDamage = Integer.parseInt(scanner.nextLine());
            playerAttackSpeed = Double.parseDouble(scanner.nextLine());
            playerMoveSpeed = Double.parseDouble(scanner.nextLine()); // 이동속도 불러오기
            hpLevel = Integer.parseInt(scanner.nextLine());
            damageLevel = Integer.parseInt(scanner.nextLine());
            attackSpeedLevel = Integer.parseInt(scanner.nextLine());
            hpUpgradeCost = Double.parseDouble(scanner.nextLine());
            damageUpgradeCost = Double.parseDouble(scanner.nextLine());
            attackSpeedUpgradeCost = Double.parseDouble(scanner.nextLine());
            System.out.println("게임 데이터를 불러왔습니다.");
        } catch (Exception e) {
            System.err.println("데이터 불러오기 오류. 파일을 삭제합니다.");
            file.delete();
        }
    }
    
    public static void reset() {
        gold = 0;
        playerMaxHp = 100;
        playerDamage = 50;
        playerAttackSpeed = 0.5;
        playerMoveSpeed = 600;
        hpLevel = 1;
        damageLevel = 1;
        attackSpeedLevel = 1;
        hpUpgradeCost = 50;
        damageUpgradeCost = 70;
        attackSpeedUpgradeCost = 100;
        enemyDamage = 10;
    }
    
    // 업그레이드 메소드들
    public static boolean upgradeMaxHp() { if (gold >= hpUpgradeCost) { gold -= hpUpgradeCost; playerMaxHp += 20; hpLevel++; hpUpgradeCost *= 1.25; return true; } return false; }
    public static boolean upgradeDamage() { if (gold >= damageUpgradeCost) { gold -= damageUpgradeCost; playerDamage += 5; damageLevel++; damageUpgradeCost *= 1.25; return true; } return false; }
    public static boolean upgradeAttackSpeed() { if (gold >= attackSpeedUpgradeCost && playerAttackSpeed > 0.1) { gold -= attackSpeedUpgradeCost; playerAttackSpeed *= 0.9; attackSpeedLevel++; attackSpeedUpgradeCost *= 1.25; return true; } return false; }
 
}