package com.connectfourgui;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ConnectFourMainMenu extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                return new MyMainMenu();
            }
        });
    }

    public static class MyMainMenu extends FXGLMenu {

        private static final int SIZE = 150;

        private Animation<?> animation;

        public MyMainMenu() {
            super(MenuType.MAIN_MENU);

            getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
            getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);
            var btn1 = new Rectangle(0,0,SIZE * 2.2,SIZE - 100);
            btn1.setStrokeWidth(2.5);
            btn1.strokeProperty().bind(
                    Bindings.when(btn1.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK)
            );
            btn1.fillProperty().bind(
                    Bindings.when(btn1.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75))
            );
            btn1.setOnMouseClicked(e -> {
                ConnectFourGUI.alphaBeta = false;
                fireNewGame();
            });
            var btn2 = new Rectangle(0,btn1.getY() + 50,SIZE*2,SIZE-100);
            btn2.setStrokeWidth(2.5);
            btn2.strokeProperty().bind(
                    Bindings.when(btn2.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK)
            );
            btn2.fillProperty().bind(
                    Bindings.when(btn2.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75))
            );
            btn2.setOnMouseClicked(e -> fireNewGame());
            var exitBtn = new Rectangle(0,btn2.getY() + 50,SIZE,50);
            exitBtn.setStrokeWidth(2.5);
            exitBtn.strokeProperty().bind(
                    Bindings.when(exitBtn.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK)
            );
            exitBtn.fillProperty().bind(
                    Bindings.when(exitBtn.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75))
            );
            exitBtn.setOnMouseClicked(e -> fireExit());
            Text textResume = FXGL.getUIFactoryService().newText("RESUME", Color.WHITE, FontType.GAME, 24.0);
            textResume.setTranslateX(50);
            textResume.setTranslateY(100);
            textResume.setMouseTransparent(true);

            Text noAlphaBeta = FXGL.getUIFactoryService().newText("Mini Max without alpha beta pruning",Color.WHITE,FontType.GAME,24.0);
            noAlphaBeta.setTranslateY(btn1.getY() + btn1.getHeight()/2);
            noAlphaBeta.setTranslateX(btn1.getX());
            noAlphaBeta.setMouseTransparent(true);

            Text alphaBeta = FXGL.getUIFactoryService().newText("Mini Max with alpha beta pruning",Color.WHITE,FontType.GAME,24.0);
            alphaBeta.setTranslateY(btn2.getY() + btn2.getHeight() / 2);
            alphaBeta.setTranslateX(btn2.getX());
            alphaBeta.setMouseTransparent(true);

            Text textExit = FXGL.getUIFactoryService().newText("EXIT", Color.WHITE, FontType.GAME, 24.0);
            textExit.setTranslateX(exitBtn.getX());
            textExit.setTranslateY(exitBtn.getY() + exitBtn.getHeight() / 2);
            textExit.setMouseTransparent(true);

            getContentRoot().getChildren().addAll(btn1,btn2,exitBtn,textExit,noAlphaBeta,alphaBeta);

            getContentRoot().setScaleX(0);
            getContentRoot().setScaleY(0);

            animation = FXGL.animationBuilder()
                    .duration(Duration.seconds(0.66))
                    .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                    .scale(getContentRoot())
                    .from(new Point2D(0, 0))
                    .to(new Point2D(1, 1))
                    .build();
        }

        @Override
        public void onCreate() {
            animation.setOnFinished(EmptyRunnable.INSTANCE);
            animation.stop();
            animation.start();
        }

        @Override
        protected void onUpdate(double tpf) {
            animation.onUpdate(tpf);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}