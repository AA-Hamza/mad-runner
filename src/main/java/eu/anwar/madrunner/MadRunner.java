package eu.anwar.madrunner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MadRunner extends Application
{

    static public MediaPlayer mainTheme;
    /* static vars */
    static public final double minScreenY = 500;
    static public final double minScreenX = 500;

    @Override public void start(Stage stage)
    {
        Group root = new Group();
        Scene theScene = new Scene(root);
        stage.setScene(theScene);

        stage.setResizable(true); // disable resizing the screen
        stage.setMinWidth(minScreenX);
        stage.setMinHeight(minScreenY);
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setMaximized(true);

        ResizableCanvas canvas = new ResizableCanvas();

        canvas.widthProperty().bind(stage.widthProperty());
        canvas.heightProperty().bind(stage.heightProperty());

        Media mainThemeMedia = new Media(getClass().getClassLoader().getResource("sounds/main_theme.wav").toString());
        mainTheme = new MediaPlayer(mainThemeMedia);
        mainTheme.setAutoPlay(true);
        mainTheme.setCycleCount(MediaPlayer.INDEFINITE);

        canvas.requestFocus();

        root.getChildren().add(canvas);

        AnimationTimer myGameLoop = new GameLoop(canvas);
        myGameLoop.start();

        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

    public class ResizableCanvas extends Canvas
    {
        public boolean isResizable()
        {
            return true;
        }
    }
}
