package gamestates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
        List<UpgradeOption> allUpgrades = new ArrayList<>();
        allUpgrades.add(new UpgradeOption("최대 체력 +10%", () -> playingState.getPlayer().applyHpBuff(0.10)));
        allUpgrades.add(new UpgradeOption("공격력 +5", () -> playingState.getPlayer().applyDamageBuff(5)));
        allUpgrades.add(new UpgradeOption("공격 속도 +5%", () -> playingState.getPlayer().applyAttackSpeedBuff(0.05)));
        allUpgrades.add(new UpgradeOption("획득골드 +10%", () -> playingState.getPlayer().applyGoldBuff(0.10)));
        if (playingState.getPlayer().getSpeed() < playingState.getPlayer().getMaxSpeed()) {
             allUpgrades.add(new UpgradeOption("이동 속도 +30", () -> playingState.getPlayer().applySpeedBuff(30)));
        }
        
        Collections.shuffle(allUpgrades);
        
        int optionsToShow = Math.min(3, allUpgrades.size());
        for (int i = 0; i < optionsToShow; i++) {
            UpgradeOption option = allUpgrades.get(i);
            Button upgradeButton = new Button(option.description);
            upgradeButton.setFont(new Font("Arial", 20));
            upgradeButton.setMinWidth(300);
            upgradeButton.setOnAction(e -> {
                option.effect.run();
                gsm.popState();
            });
            buttonContainer.getChildren().add(upgradeButton);
        }
    }
}