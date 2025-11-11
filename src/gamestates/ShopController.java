package gamestates;

import main.GameData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ShopController {

    private GameStateManager gsm;
    
    // --- FXML 연결 필드 ---
    @FXML private Label goldLabel;
    @FXML private Label hpLabel;
    @FXML private Button hpUpgradeButton;
    @FXML private Label damageLabel;
    @FXML private Button damageUpgradeButton;
    @FXML private Label attackSpeedLabel;
    @FXML private Button attackSpeedUpgradeButton;

    /**
     * FXML 파일이 로드된 후 자동으로 호출되는 초기화 메소드
     */
    @FXML
    public void initialize() {
        updateLabels();
    }
    
    /**
     * GameStateManager로부터 gsm 객체를 주입받습니다.
     */
    public void init(GameStateManager gsm) {
        this.gsm = gsm;
    }

    // --- FXML 연결 이벤트 핸들러 ---

    @FXML
    void handleHpUpgrade(ActionEvent event) {
        GameData.upgradeMaxHp(); // 업그레이드 시도
        updateLabels(); // UI 갱신
    }

    @FXML
    void handleDamageUpgrade(ActionEvent event) {
        GameData.upgradeDamage(); // 업그레이드 시도
        updateLabels(); // UI 갱신
    }

    @FXML
    void handleAttackSpeedUpgrade(ActionEvent event) {
        GameData.upgradeAttackSpeed(); // 업그레이드 시도
        updateLabels(); // UI 갱신
    }

    @FXML
    void handleBackButton(ActionEvent event) {
        if (gsm != null) {
            gsm.popState();
        }
    }

    /**
     * 현재 GameData를 기반으로 상점의 모든 UI 라벨과 버튼 상태를 갱신합니다.
     * (★ 최대 레벨 캡 UI 적용됨)
     */
    private void updateLabels() {
        // 골드 갱신
        goldLabel.setText("Gold: " + GameData.gold);
        
        // --- HP 업그레이드 갱신 ---
        hpLabel.setText(String.format("최대 체력 (Lv.%d): %d", GameData.hpLevel, GameData.playerMaxHp));

        if (GameData.hpLevel >= GameData.MAX_UPGRADE_LEVEL) {
            // A. 최대 레벨에 도달했을 때
            hpUpgradeButton.setText("MAX LEVEL");
            hpUpgradeButton.setDisable(true);
        } else {
            // B. 아직 업그레이드 가능할 때
            hpUpgradeButton.setText(String.format("Upgrade (Cost: %.0f G)", GameData.hpUpgradeCost));
            hpUpgradeButton.setDisable(GameData.gold < GameData.hpUpgradeCost); // 골드에 따라 비활성화
        }
        
        // --- 데미지 업그레이드 갱신 ---
        damageLabel.setText(String.format("공격력 (Lv.%d): %d", GameData.damageLevel, GameData.playerDamage));

        if (GameData.damageLevel >= GameData.MAX_UPGRADE_LEVEL) {
            damageUpgradeButton.setText("MAX LEVEL");
            damageUpgradeButton.setDisable(true);
        } else {
            damageUpgradeButton.setText(String.format("Upgrade (Cost: %.0f G)", GameData.damageUpgradeCost));
            damageUpgradeButton.setDisable(GameData.gold < GameData.damageUpgradeCost);
        }
        
        // --- 공격 속도 업그레이드 갱신 ---
        attackSpeedLabel.setText(String.format("공격 속도 (Lv.%d): %.2f초/발", GameData.attackSpeedLevel, GameData.playerAttackSpeed));

        if (GameData.attackSpeedLevel >= GameData.MAX_UPGRADE_LEVEL) {
            attackSpeedUpgradeButton.setText("MAX LEVEL");
            attackSpeedUpgradeButton.setDisable(true);
        } else {
            attackSpeedUpgradeButton.setText(String.format("Upgrade (Cost: %.0f G)", GameData.attackSpeedUpgradeCost));
            attackSpeedUpgradeButton.setDisable(GameData.gold < GameData.attackSpeedUpgradeCost);
        }
    }
}