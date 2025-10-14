package gamestates;

import main.GameData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ShopController {

    private GameStateManager gsm;
    
    @FXML private Label goldLabel;
    @FXML private Label hpLabel;
    @FXML private Button hpUpgradeButton;
    @FXML private Label damageLabel;
    @FXML private Button damageUpgradeButton;
    @FXML private Label attackSpeedLabel;
    @FXML private Button attackSpeedUpgradeButton;

    // FXML 파일이 로드된 후 자동으로 호출되는 초기화 메소드
    @FXML
    public void initialize() {
        updateLabels();
    }
    
    public void init(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @FXML void handleHpUpgrade(ActionEvent event) { if (GameData.upgradeMaxHp()) updateLabels(); }
    @FXML void handleDamageUpgrade(ActionEvent event) { if (GameData.upgradeDamage()) updateLabels(); }
    @FXML void handleAttackSpeedUpgrade(ActionEvent event) { if (GameData.upgradeAttackSpeed()) updateLabels(); }
    @FXML void handleBackButton(ActionEvent event) { gsm.popState(); }

    private void updateLabels() {
        goldLabel.setText("Gold: " + GameData.gold);
        hpLabel.setText(String.format("최대 체력 (Lv.%d): %d", GameData.hpLevel, GameData.playerMaxHp));
        hpUpgradeButton.setText(String.format("Upgrade (Cost: %.0f G)", GameData.hpUpgradeCost));
        damageLabel.setText(String.format("공격력 (Lv.%d): %d", GameData.damageLevel, GameData.playerDamage));
        damageUpgradeButton.setText(String.format("Upgrade (Cost: %.0f G)", GameData.damageUpgradeCost));
        attackSpeedLabel.setText(String.format("공격 속도 (Lv.%d): %.2f초/발", GameData.attackSpeedLevel, GameData.playerAttackSpeed));
        attackSpeedUpgradeButton.setText(String.format("Upgrade (Cost: %.0f G)", GameData.attackSpeedUpgradeCost));
    }
}