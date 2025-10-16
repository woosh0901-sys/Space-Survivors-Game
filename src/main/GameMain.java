package main; // 이 파일이 main 패키지에 속해 있음을 선언합니다.

import java.util.HashSet; // HashSet 클래스를 가져옵니다. 중복되지 않는 요소들의 집합을 구현하는 데 사용됩니다.
import java.util.Set; // Set 인터페이스를 가져옵니다. 집합의 동작을 정의합니다.

import gamestates.GameStateManager;
import gamestates.MenuState;
import gamestates.PlayingState;
import javafx.animation.AnimationTimer; // 애니메이션 타이머를 생성하기 위한 AnimationTimer 클래스를 가져옵니다. 게임 루프에 사용됩니다.
import javafx.application.Application; // JavaFX 애플리케이션의 생명주기를 관리하는 Application 클래스를 가져옵니다.
import javafx.scene.Scene; // 애플리케이션의 모든 콘텐츠를 담는 컨테이너인 Scene 클래스를 가져옵니다.
import javafx.scene.input.KeyCode; // 키보드 키를 나타내는 열거형인 KeyCode를 가져옵니다.
import javafx.scene.layout.Pane; // 자식 노드를 자유롭게 배치할 수 있는 기본 레이아웃 컨테이너인 Pane 클래스를 가져옵니다.
import javafx.scene.layout.StackPane; // 자식 노드를 겹겹이 쌓는 레이아웃 컨테이너인 StackPane 클래스를 가져옵니다.
import javafx.stage.Stage; // 애플리케이션의 최상위 창을 나타내는 Stage 클래스를 가져옵니다.

public class GameMain extends Application { // JavaFX 애플리케이션의 진입점인 GameMain 클래스를 선언하고 Application 클래스를 상속받습니다.

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    private GameStateManager gsm; // 게임 상태 관리자(GameStateManager) 객체를 저장할 변수입니다.
    private static Set<KeyCode> activeKeys = new HashSet<>(); // 현재 눌려있는 키들을 저장하는 Set입니다.
    private StackPane mainPane; // 게임의 모든 UI 요소를 담을 메인 컨테이너인 StackPane입니다.

    @Override // 부모 클래스(Application)의 start 메소드를 재정의합니다.
    public void start(Stage primaryStage) throws Exception { // 애플리케이션이 시작될 때 호출되는 메소드입니다. primaryStage는 기본 창입니다.
        // ★★★ 게임 시작 시 데이터 불러오기 ★★★ // 게임 데이터를 불러오는 부분임을 나타내는 주석입니다.
        GameData.load(); // GameData 클래스의 load 메소드를 호출하여 저장된 게임 데이터를 불러옵니다.

        primaryStage.setTitle("Space Survivors"); // 게임 창의 제목을 "Space Survivors"로 설정합니다.
        mainPane = new StackPane(); // UI 요소들을 겹쳐서 표시할 StackPane 객체를 생성합니다.
        Scene scene = new Scene(mainPane, WIDTH, HEIGHT); // StackPane을 루트로 하고 지정된 너비와 높이를 가진 Scene 객체를 생성합니다.
        
        primaryStage.setScene(scene); // 생성한 Scene을 기본 창(primaryStage)에 설정합니다.
        primaryStage.show(); // 게임 창을 화면에 표시합니다.

        // ★★★ 게임 창을 닫을 때 데이터 저장 ★★★ // 게임 데이터를 저장하는 부분임을 나타내는 주석입니다.
        primaryStage.setOnCloseRequest(e -> { // 사용자가 창을 닫으려고 할 때 실행될 이벤트를 설정합니다.
            GameData.save(); // GameData 클래스의 save 메소드를 호출하여 현재 게임 데이터를 저장합니다.
        });

        gsm = new GameStateManager(this); // 게임 상태 관리자(GameStateManager) 객체를 생성합니다.
        gsm.setState(new MenuState(gsm)); // 게임의 초기 상태를 메뉴 상태(MenuState)로 설정합니다.

        scene.setOnKeyPressed(event -> { // 키가 눌렸을 때 실행될 이벤트를 설정합니다.
            activeKeys.add(event.getCode()); // 눌린 키의 코드를 activeKeys 집합에 추가합니다.
            
            if (gsm.getCurrentState() instanceof PlayingState) { // 현재 게임 상태가 플레이 중(PlayingState)인지 확인합니다.
                if (event.getCode() == KeyCode.SPACE) { // 눌린 키가 스페이스바인지 확인합니다.
                    ((PlayingState) gsm.getCurrentState()).shoot(); // 현재 상태를 PlayingState로 형변환하고 shoot 메소드를 호출하여 발사합니다.
                }
            }
        });
        
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode())); // 키에서 손을 뗐을 때 실행될 이벤트를 설정하고, 해당 키를 activeKeys 집합에서 제거합니다.
        
        AnimationTimer gameLoop = new AnimationTimer() { // 게임 루프를 생성하기 위한 AnimationTimer 객체를 익명 클래스로 만듭니다.
            private long lastTimestamp = 0; // 마지막으로 handle 메소드가 호출된 시간을 저장할 변수입니다.
            @Override // AnimationTimer의 handle 메소드를 재정의합니다.
            public void handle(long now) { // 매 프레임마다 호출되는 메소드입니다. 'now'는 현재 시간(나노초)입니다.
                if (lastTimestamp == 0) { // 첫 프레임인 경우,
                    lastTimestamp = now; // lastTimestamp를 현재 시간으로 설정하고,
                    return; // 메소드를 종료합니다.
                }
                double deltaTime = (now - lastTimestamp) / 1_000_000_000.0; // 이전 프레임과 현재 프레임 사이의 시간 차이(초 단위)를 계산합니다.
                lastTimestamp = now; // lastTimestamp를 현재 시간으로 업데이트합니다.

                gsm.update(deltaTime); // 게임 상태 관리자의 update 메소드를 호출하여 게임 로직을 업데이트합니다.
                gsm.render(); // 게임 상태 관리자의 render 메소드를 호출하여 화면을 다시 그립니다.
            }
        };
        gameLoop.start(); // 게임 루프(AnimationTimer)를 시작합니다.
    }
    
    public void setRoot(Pane pane) { // 메인 컨테이너(mainPane)의 자식 노드를 변경하는 메소드입니다.
        mainPane.getChildren().clear(); // mainPane의 모든 자식 노드를 제거합니다.
        mainPane.getChildren().add(pane); // 새로운 Pane을 mainPane의 자식으로 추가합니다.
    }
    
    public static Set<KeyCode> getActiveKeys() { // 현재 눌려있는 키들의 집합을 반환하는 정적 메소드입니다.
        return activeKeys; // activeKeys 집합을 반환합니다.
    }

    public static void main(String[] args) { // 프로그램의 주 진입점인 main 메소드입니다.
        launch(args); // JavaFX 애플리케이션을 시작합니다.
    }
}