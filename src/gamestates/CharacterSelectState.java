package gamestates;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class CharacterSelectState extends GameState {

    public CharacterSelectState(GameStateManager gsm) {
        super(gsm);
        try {
            // 1. FXML 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterSelect.fxml"));
            this.rootNode = loader.load();
            
            // 2. 컨트롤러 가져오기
            CharacterSelectController controller = loader.getController();
            
            // 3. 컨트롤러에 gsm 주입
            controller.init(gsm);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void update(double deltaTime) {}
    @Override public void render() {}
}