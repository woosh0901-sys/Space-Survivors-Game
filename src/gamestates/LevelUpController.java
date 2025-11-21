package gamestates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import main.GameConstants;

public class LevelUpController {

    private GameStateManager gsm;
    private PlayingState playingState;

    // ★ 중요: FXML 파일의 fx:id="optionsContainer" 와 이름을 똑같이 맞춰야 합니다.
    // (기존 buttonContainer -> optionsContainer 로 변경)
    @FXML
    private VBox optionsContainer;

    public void init(GameStateManager gsm, PlayingState playingState) {
        this.gsm = gsm;
        this.playingState = playingState;
        createUpgradeButtons();
    }

    private void createUpgradeButtons() {
        List<UpgradeOption> allUpgrades = createAllUpgradeOptions();
        Collections.shuffle(allUpgrades);

        int optionsToShow = Math.min(3, allUpgrades.size());
        
        // 기존 버튼들 초기화 (중복 방지)
        optionsContainer.getChildren().clear();
        
        for (int i = 0; i < optionsToShow; i++) {
            UpgradeOption option = allUpgrades.get(i);
            Button upgradeButton = createUpgradeButton(option);
            optionsContainer.getChildren().add(upgradeButton);
        }
    }
    
    private List<UpgradeOption> createAllUpgradeOptions() {
        List<UpgradeOption> upgrades = new ArrayList<>();
        
        upgrades.add(new UpgradeOption(
            "최대 체력 +10%", 
            () -> playingState.getPlayer().applyHpBuff(GameConstants.HP_BUFF_PERCENTAGE)
        ));
        
        upgrades.add(new UpgradeOption(
            "공격력 +15", 
            () -> playingState.getPlayer().applyDamageBuff(GameConstants.DAMAGE_BUFF_AMOUNT)
        ));
            
        upgrades.add(new UpgradeOption(
            "획득골드 +10%", 
            () -> playingState.getPlayer().applyGoldBuff(GameConstants.GOLD_BUFF_PERCENTAGE)
        ));
        
        if (playingState.getPlayer().getSpeed() < playingState.getPlayer().getMaxSpeed()) {
            upgrades.add(new UpgradeOption(
                "이동 속도 +50", 
                () -> playingState.getPlayer().applySpeedBuff(GameConstants.SPEED_BUFF_AMOUNT)
            ));
        }
        
        return upgrades;
    }
    
    // ★★★ 여기가 핵심 수정 부분입니다 (스타일 적용) ★★★
    private Button createUpgradeButton(UpgradeOption option) {
        Button button = new Button(option.description); // UpgradeOption의 필드명(description) 확인 필요
        
        // 버튼 크기 지정
        button.setPrefWidth(600);
        button.setPrefHeight(100);
        
        // 1. 기본 스타일 (반투명 주황 배경 + 테두리)
        String defaultStyle = 
            "-fx-background-color: rgba(255, 140, 0, 0.2);" + 
            "-fx-border-color: #FF8C00;" + 
            "-fx-border-width: 2;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;";
            
        // 2. 마우스 올렸을 때 스타일 (조금 더 밝게)
        String hoverStyle = 
            "-fx-background-color: rgba(255, 140, 0, 0.4);" + 
            "-fx-border-color: #FF8C00;" + 
            "-fx-border-width: 2;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;";

        // 초기 스타일 적용
        button.setStyle(defaultStyle);

        // 마우스 이벤트 리스너 추가
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));

        // 클릭 이벤트
        button.setOnAction(e -> {
            option.effect.run();
            gsm.popState();
        });
        
        return button;
    }
}