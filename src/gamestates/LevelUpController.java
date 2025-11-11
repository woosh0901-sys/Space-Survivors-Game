package gamestates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.GameConstants;

public class LevelUpController {

    private GameStateManager gsm;
    private PlayingState playingState;

    @FXML
    private VBox buttonContainer;

    public void init(GameStateManager gsm, PlayingState playingState) {
        this.gsm = gsm;
        this.playingState = playingState;
        createUpgradeButtons();
    }

    private void createUpgradeButtons() {
        List<UpgradeOption> allUpgrades = createAllUpgradeOptions();
        Collections.shuffle(allUpgrades);

        int optionsToShow = Math.min(3, allUpgrades.size());
        for (int i = 0; i < optionsToShow; i++) {
            UpgradeOption option = allUpgrades.get(i);
            Button upgradeButton = createUpgradeButton(option);
            buttonContainer.getChildren().add(upgradeButton);
        }
    }
    
    private List<UpgradeOption> createAllUpgradeOptions() {
        List<UpgradeOption> upgrades = new ArrayList<>();
        
        upgrades.add(new UpgradeOption(
            "최대 체력 +10%", 
            () -> playingState.getPlayer().applyHpBuff(GameConstants.HP_BUFF_PERCENTAGE)
        ));
        
        upgrades.add(new UpgradeOption(
            "공격력 +5", 
            () -> playingState.getPlayer().applyDamageBuff(GameConstants.DAMAGE_BUFF_AMOUNT)
        ));
        	
        upgrades.add(new UpgradeOption(
            "획득골드 +10%", 
            () -> playingState.getPlayer().applyGoldBuff(GameConstants.GOLD_BUFF_PERCENTAGE)
        ));
        
        if (playingState.getPlayer().getSpeed() < playingState.getPlayer().getMaxSpeed()) {
            upgrades.add(new UpgradeOption(
                "이동 속도 +50", 
                () -> playingState.getPlayer().applySpeedBuff(GameConstants.SPEED_BUFF_AMOUNT)
            ));
        }
        
        return upgrades;
    }
    
    private Button createUpgradeButton(UpgradeOption option) {
        Button button = new Button(option.description);
        button.setFont(new Font("Arial", 20));
        button.setMinWidth(300);
        button.setOnAction(e -> {
            option.effect.run();
            gsm.popState();
        });
        return button;
    }
}