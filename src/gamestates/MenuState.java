package gamestates;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class MenuState extends GameState {

    public MenuState(GameStateManager gsm) {
        super(gsm);
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            this.rootNode = loader.load();
            MenuController controller = loader.getController();
            controller.init(gsm);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(double deltaTime) {
        // FXML과 컨트롤러가 모든 것을 처리하므로 여기서는 할 일이 없습니다.
    }

    @Override
    public void render() {
        // FXML이 화면을 그리므로 여기서도 할 일이 없습니다.
    }
}