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
    @FXML private ProgressBar skillBar;

    // update 메서드에 skillProgress 파라미터 추가
    public void update(int level, double currentXp, double requiredXp, double currentHp, double maxHp, double elapsedTime, double skillProgress) {
        // 레벨 및 경험치
        if (requiredXp > 0) {
            xpBar.setProgress(currentXp / requiredXp);
        }

        // 체력
        hpLabel.setText(String.format("HP: %.0f / %.0f", currentHp, maxHp));
        if (maxHp > 0) {
            hpBar.setProgress(currentHp / maxHp);
        }
        
        // ★ 스킬바 업데이트
        skillBar.setProgress(skillProgress);

        // 시간
        int minutes = (int) (elapsedTime / 60);
        int seconds = (int) (elapsedTime % 60);
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));

        // 골드
        goldLabel.setText("Gold: " + GameData.gold);
        
        LVLabel.setText("LV: " + level);
    }
}