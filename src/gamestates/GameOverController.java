package gamestates;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameOverController {
    
    private GameStateManager gsm;

    public void init(GameStateManager gsm) {
        this.gsm = gsm;
    }
    
    @FXML
    void handleRestartButton(ActionEvent event) {
        gsm.setState(new PlayingState(gsm));
    }
    
    @FXML
    void handleMenuButton(ActionEvent event) {
        gsm.setState(new MenuState(gsm));
    }
}