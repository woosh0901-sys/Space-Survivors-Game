package gamestates; // 이 파일이 com.mygame.gamestates 패키지에 속해 있음을 선언합니다.

import javafx.scene.layout.Pane; // UI 요소들을 담을 컨테이너인 Pane 클래스를 가져옵니다.

public abstract class GameState { // 모든 게임 상태(메뉴, 플레이 중 등)의 기본이 되는 추상 클래스 GameState입니다.

    protected GameStateManager gsm; // 게임 상태를 관리하는 GameStateManager에 대한 참조 변수입니다.
    protected Pane rootNode; // 이 게임 상태의 모든 UI 요소를 담을 루트 컨테이너(Pane)입니다.

    public GameState(GameStateManager gsm) { // GameState 객체를 생성하는 생성자입니다.
        this.gsm = gsm; // GameStateManager 참조를 초기화합니다.
        this.rootNode = new Pane(); // 이 상태에서 사용할 새로운 Pane 객체를 생성합니다.
    }

    public Pane getRootNode() { // 이 게임 상태의 루트 Pane을 반환하는 메소드입니다.
        return rootNode; // rootNode 변수를 반환합니다.
    }

    // 매 프레임마다 게임 로직을 갱신하는 추상 메소드입니다. 하위 클래스에서 반드시 구현해야 합니다.
    public abstract void update(double deltaTime);
    
    // 화면에 그래픽을 그리는 추상 메소드입니다. 하위 클래스에서 반드시 구현해야 합니다.
    public abstract void render();
}