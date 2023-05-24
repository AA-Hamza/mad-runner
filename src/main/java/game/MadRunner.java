package game;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class MadRunner extends Application {
  /* static vars */
  static public final double minScreenY = 500;
  static public final double minScreenX = 500;

  // static public final double screenY;
  // static public final double screenX;

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene theScene = new Scene(root);
    stage.setScene(theScene);

    stage.setResizable(true); // disable resizing the screen
    stage.setMinWidth(minScreenX);
    stage.setMinHeight(minScreenY);
    stage.setWidth(1024);
    stage.setHeight(768);
    stage.setMaximized(true);

    // Canvas canvas = new Canvas(
    //     stage.getWidth(),
    //     stage.getHeight()); // We are only using canvas to draw objects
    ResizableCanvas canvas = new ResizableCanvas();

    canvas.widthProperty().bind(stage.widthProperty());
    canvas.heightProperty().bind(stage.heightProperty());

    canvas.requestFocus();

    root.getChildren().add(canvas);

    AnimationTimer myGameLoop = new GameLoop(canvas);
    myGameLoop.start();

    stage.show();
  }

  public static void main(String[] args) { launch(); }

  public class ResizableCanvas extends Canvas {
    public boolean isResizable() { return true; }
  }
}
