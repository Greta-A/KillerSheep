package applications;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.sampled.TransformableContent;

/**
 * Bernstein: Sprite class for the player (Bernstein).
 * 
 * @author Greta, Rain, Joelle
 *
 *         We abide by the JMU Honor Code
 */
public class Bernstein extends RuleBasedSprite implements KeyListener
{
  protected double speed;
  protected double x;
  protected double y;
  protected KillerSheep ks;
  protected int state, stateChange;
  protected TransformableContent[] contents;

  /**
   * Bernstein: constructor that creates the Bernstein object, its location and its state.
   * 
   * @param contents
   *          the array of image contents that comprise the Bernstein sprite
   * @param speed
   *          the speed of the Bernstein player
   * @param x
   *          the initial location on the x-axis
   * @param y
   *          the initial location on the y-axis
   * @param ks
   *          the KillerSheep class object which allows the method to access the KillerSheep
   *          variables
   */
  public Bernstein(TransformableContent contents[], double speed, double x, double y,
      KillerSheep ks)
  {
    super(contents[0]);

    this.contents = contents;
    this.x = x;
    this.y = y;

    setLocation(x, y);
    this.speed = speed;
    this.ks = ks;
    state = 0;
    stateChange = 1;
  }

  /**
   * Get the visual content associated with this Sprite (required by Sprite).
   * 
   * @return the array of content
   */
  public TransformableContent getContent()
  {
    return contents[state];
  }

  /**
   * keyPressed: required by KeyListener, updates Bernstein's location, state, and speed based on
   * user input.
   */
  @Override
  public void keyPressed(KeyEvent e)
  {

    switch (e.getKeyCode())
    {
      case KeyEvent.VK_UP:
        y -= speed;
        setLocation(x, y);
        break;
      case KeyEvent.VK_DOWN:
        y += speed;
        setLocation(x, y);
        break;
      case KeyEvent.VK_LEFT:
        x -= speed;
        setLocation(x, y);
        break;
      case KeyEvent.VK_RIGHT:
        x += speed;
        setLocation(x, y);
        break;
      default:
        break;
    }

    // disallow player from going out of bounds
    if (x < 0)
      x = 40;
    if (x > 1100)
      x = 1000;
    if (y < 0)
      y = 6.0;
    if (y > 455)
      y = 420;
  }

  /**
   * stateChange: helper method that changes the state of the image of the Bernstein player; called
   * during a KeyPress to avoid issues with metronome.
   */
  public void stateChange()
  {
    state += stateChange;
    if (state == 1)
    {
      stateChange = -1;
    }
    else
    {
      stateChange = 1;
    }
  }

  /**
   * handleTick: required by metronome; checks if intersecting with antagonist.
   */
  @Override
  public void handleTick(int arg0)
  {
    Iterator<Sprite> i;
    Sprite paddock;

    i = antagonists.iterator();
    while (i.hasNext())
    {
      speed += 0.5;
      paddock = i.next();

      if (intersects(paddock))
      {
        // invoke main class method to interact with contentPane and stage
        ks.intersectWithPaddock();
      }
    }
  }

  /**
   * keyTyped: required by KeyListener.
   */
  @Override
  public void keyTyped(KeyEvent e)
  {
  }

  /**
   * keyReleased: required by KeyListener.
   */
  @Override
  public void keyReleased(KeyEvent e)
  {
    switch (e.getKeyCode())
    {
      case KeyEvent.VK_UP:
        stateChange();
        break;
      case KeyEvent.VK_DOWN:
        stateChange();
        break;
      case KeyEvent.VK_LEFT:
        stateChange();
        break;
      case KeyEvent.VK_RIGHT:
        stateChange();
        break;
      default:
        break;
    }
  }

  /**
   * getX: getter method to retrive x-axis location of Bernstein player.
   * 
   * @return the value of the x location
   */
  public double getX()
  {
    return x;
  }

  /**
   * getY: getter method to retrieve y-axis location of Bernstein player.
   * 
   * @return the value of the y location
   */
  public double getY()
  {
    return y;
  }
}
