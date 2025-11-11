package gamestates;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameClearController {

    private GameStateManager gsm;
    
    @FXML
    private Label timeLabel;

    public void init(GameStateManager gsm, double clearTime) {
        this.gsm = gsm;
        
        int minutes = (int) (clearTime / 60);
        int seconds = (int) (clearTime % 60);
        timeLabel.setText(String.format("클리어 타임: %02d:%02d", minutes, seconds));
    }

    @FXML
    void handleMenuButton(ActionEvent event) {
        gsm.setState(new MenuState(gsm));
    }
}