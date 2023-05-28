package eu.anwar.madrunner.objects;
import eu.anwar.madrunner.logic.Logic;
import eu.anwar.madrunner.objects.obstacles.*;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Player extends GameLaneObject
{
    /* Static public vars */
    // static public final double playerLength = 50.0d;
    static public final double playerLength = 64.0d;
    static public final int maxJumpFrames = 40;
    static public final int maxJumpLength = 2;
    static public final int maxRampFrames = (int)(TrailerWithRamp.rampSize / 2.0d);
    static public enum Level { LOW, HIGH }

    // This was in static block which makes better sense but it is here for
    // simplicity ¯\_(ツ)_/¯
    static public final double jumpFormulaATerm =
        (double)(-4 * maxJumpLength) / (double)(maxJumpFrames * maxJumpFrames);

    static public final double jumpFormulaBTerm = (double)(4 * maxJumpLength) / (double)maxJumpFrames;

    /* Private constants */
    // This color will be used in case the image doesn't exist
    private final Color playerColor = Color.DARKKHAKI;

    private MediaPlayer jumpMedia;
    private MediaPlayer deathMedia;

    /* Private vars */
    private Level level = Level.LOW;
    private boolean jumping = false;
    private boolean isClimbingRamp = false;
    private Boolean alive = true;
    private Boolean isDying = false;
    private ArrayList<Image> runFrames = null;
    private ArrayList<Image> deathFrames = null;
    // Animation frames counters
    private int currentJumpFrame = 0;
    private int currentRunningFrame = 0;
    private int currentDeathFrame = 0;
    private int currentClimbingFrame = 0;

    /* Constructors */
    public Player()
    {
        super(Block.Lane.CENTER, Logic.getBounderies().getPlayerY(), playerLength);
        this.setColor(playerColor);
        setAnimations();
        // setSounds();
    }

    /**
     * This function is responsible of loading player animations
     */
    private void setAnimations()
    {
        this.runFrames = new ArrayList<Image>(4);
        for (int i = 0; i < 4; i++)
        {
            this.runFrames.add(
                new Image(getClass().getClassLoader().getResourceAsStream("images/player/run_" + i + ".png"),
                          playerLength, playerLength, true, false));
        }

        this.deathFrames = new ArrayList<Image>(4);
        for (int i = 0; i < 4; i++)
        {
            this.deathFrames.add(
                new Image(getClass().getClassLoader().getResourceAsStream("images/player/death_" + i + ".png"),
                          playerLength, playerLength, true, false));
        }
    }

    // private void setSounds()
    // {
    //     Media jumpMediaSound = new Media(getClass().getClassLoader().getResource("sounds/jump.wav").toString());
    //
    //     this.jumpMedia = new MediaPlayer(jumpMediaSound);
    //     jumpMedia.setOnEndOfMedia(new Runnable() {
    //         public void run() {
    //             jumpMedia.seek(Duration.ZERO);
    //         }
    //     });
    //
    //     Media DeathMediaSound = new Media(getClass().getClassLoader().getResource("sounds/gameover.wav").toString());
    //     this.deathMedia = new MediaPlayer(DeathMediaSound);
    // }
    /* Getters */
    public Level getLevel()
    {
        return this.level;
    }

    /* Setters */
    public void setLevel(Level level)
    {
        this.level = level;
    }

    /* Utils */
    public void moveLeft()
    {
        switch (getLane())
        {
        case CENTER:
            this.setLane(Block.Lane.LEFT);
            break;
        case RIGHT:
            this.setLane(Block.Lane.CENTER);
            break;
        default:
            break;
        }
    }

    public void moveRight()
    {
        switch (getLane())
        {
        case CENTER:
            setLane(Block.Lane.RIGHT);
            break;
        case LEFT:
            setLane(Block.Lane.CENTER);
            break;
        default:
            break;
        }
    }

    private void playerLengthIncrease(double diff)
    {
        this.width += diff;
        this.length += diff;
    }

    private void playerLengthDecrease(double diff)
    {
        this.playerLengthIncrease(-1 * diff);
        if (this.width < Player.playerLength)
        { // Hard stop, if something is wrong in the logic
            this.width = Player.playerLength;
            this.length = Player.playerLength;
        }
    }

    public boolean isJumping()
    {
        return this.jumping;
    }
    public boolean isAlive()
    {
        return this.alive;
    }
    public boolean isDying()
    {
        return this.isDying;
    }
    public void die()
    {
        this.alive = false;

        // deathMedia.seek(deathMedia.getStartTime());
        // deathMedia.play();
    }
    public void rampClimb()
    {
        this.isClimbingRamp = true;
    }
    public void jump()
    {
        this.jumping = true;
        // jumpMedia.play();
    }
    public void setIsDying()
    {
        this.isDying = true;
    }

    /**
     * This is a simple square function that behaves a little bit like gravity
     * <a href="https://www.desmos.com/calculator/ih0rpsele9">Desmos Link</a>
     * The idea here is having a linear function make the jumping look weird
     * so a square function is a bit similar to real life (it still looks a bit
     * weird for a game). We are using length to as a z index
     * @param frame current frame (which should be smaller than maxJumpFrames)
     * @return the value of player increase in length all positive (it is smaller
     *     than maxJumpLength)
     */
    private double jumpCurrentValue(int frame)
    {
        return jumpFormulaATerm * frame * frame + jumpFormulaBTerm * frame;
    }

    /* Animation UTILS */
    public void jumpUpdate()
    {
        if (this.jumping == true)
        {
            if (this.currentJumpFrame <= Player.maxJumpFrames / 2)
            { // Up
                playerLengthIncrease(jumpCurrentValue(this.currentJumpFrame));
            }
            else
            { // down
                playerLengthDecrease(jumpCurrentValue(this.currentJumpFrame));
            }
            if (this.currentJumpFrame >= Player.maxJumpFrames)
            {
                this.currentJumpFrame = 0;
                this.jumping = false;
            }
            else
            {
                this.currentJumpFrame += 1;
            }
        }
    }

    public void setPlayerLevelLow()
    {
        // System.out.println("Player is low");
        this.level = Level.LOW;
        this.length = Player.playerLength;
        this.width = Player.playerLength;
    }

    public void setPlayerLevelHigh()
    {
        System.out.println("Player is high");
        this.level = Level.HIGH;
        this.length = Player.playerLength * 1.2;
        this.width = Player.playerLength * 1.2;
    }

    public void rampUpdate()
    {
        if (this.isClimbingRamp == true)
        {
            if (this.currentClimbingFrame == 0)
            { //  effect that the player got down
                setPlayerLevelLow();
            }
            if (this.currentClimbingFrame >= Player.maxRampFrames)
            {
                this.currentClimbingFrame = 0;
                this.isClimbingRamp = false;
                setPlayerLevelHigh();
            }
            else
            {
                playerLengthIncrease(0.5);
            }
            this.currentClimbingFrame += 1;
        }
    }

    private void updateRunningAnimation()
    {
        currentRunningFrame++;
        int index = (currentRunningFrame / 3) % runFrames.size();
        this.setImage(runFrames.get(index));
    }

    public void updateDeathAnimation()
    {
        currentDeathFrame++;
        int index = (currentDeathFrame / 10) % deathFrames.size();
        if (index == deathFrames.size() - 1)
        {
            alive = false;
            isDying = false;
        }
        this.setImage(deathFrames.get(index));
    }

    public void playerUpdateAnimation()
    {
        if (this.alive)
        {
            if (this.isDying)
            {
                updateDeathAnimation();
            }
            else if (this.jumping)
            {
                jumpUpdate();
            }
            else if (this.isClimbingRamp)
            {
                rampUpdate();
                updateRunningAnimation();
            }
            else
            {
                updateRunningAnimation();
            }
        }
    }
}
