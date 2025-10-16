package gamestates;

import main.GameData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class GameUIController {

    @FXML private ProgressBar xpBar;
    @FXML private Label levelLabel;
    @FXML private ProgressBar hpBar;
    @FXML private Label hpLabel;
    @FXML private Label timeLabel;
    @FXML private Label goldLabel;
    @FXML private Label LVLabel;

    // PlayingState로부터 게임 데이터를 받아와 UI를 갱신하는 메소드
    public void update(int level, double currentXp, double requiredXp, double currentHp, double maxHp, double elapsedTime) {
        // 레벨 및 경험치
        if (requiredXp > 0) {
            xpBar.setProgress(currentXp / requiredXp);
        }

        // 체력
        hpLabel.setText(String.format("HP: %.0f / %.0f", currentHp, maxHp));
        if (maxHp > 0) {
            hpBar.setProgress(currentHp / maxHp);
        }

        // 시간
        int minutes = (int) (elapsedTime / 60);
        int seconds = (int) (elapsedTime % 60);
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));

        // 골드
        goldLabel.setText("Gold: " + GameData.gold);
        
        LVLabel.setText("LV: " + level);
    }
}