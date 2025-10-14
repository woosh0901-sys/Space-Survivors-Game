package gamestates;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class MenuState extends GameState {

    public MenuState(GameStateManager gsm) {
        super(gsm);
        
        try {
            // 1. FXML 로더를 생성합니다.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            
            // 2. FXML 파일을 로드하여 Pane(VBox)을 생성합니다.
            this.rootNode = loader.load();
            
            // 3. FXML 파일에 연결된 컨트롤러 인스턴스를 가져옵니다.
            MenuController controller = loader.getController();
            
            // 4. 컨트롤러에 GameStateManager를 전달하여 초기화합니다.
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