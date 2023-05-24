package eu.anwar.madrunner;
import eu.anwar.madrunner.logic.Logic;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class GameLoop extends AnimationTimer
{
    /* static constants */
    static public final double fontSize = 30;
    static public final long FPS = 40;

    /* private vars */
    private GraphicsContext gc;
    private Logic gameLogic;
    private Font gameFont;
    private long last = 0;

    /* Constructors */
    public GameLoop(Canvas canvas)
    {
        gc = canvas.getGraphicsContext2D();
        this.gameFont =
            Font.loadFont(getClass().getClassLoader().getResourceAsStream("fonts/RussoOne-Regular.ttf"), fontSize);
        gc.setFont(this.gameFont);
        gameLogic = new Logic(gc);
        canvas.widthProperty().addListener(evt -> {
            Logic.pauseObject.enable();
            Logic.bounderiesOBJ = null;
            gameLogic.objectsSetUP();
        });
        canvas.heightProperty().addListener(evt -> {
            Logic.pauseObject.enable();
            Logic.bounderiesOBJ = null;
            gameLogic.objectsSetUP();
        });
        Logic.pauseObject.disable();
    }

    @Override public void handle(long now)
    {
        if ((now - last) > ((1_000 * 1_000_000 / FPS)))
        {
            gameLogic.tick(now);
            last = now;
        }
    }
}
