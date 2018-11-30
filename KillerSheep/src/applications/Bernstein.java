package applications;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.sampled.TransformableContent;

public class Bernstein extends RuleBasedSprite implements KeyListener
{

  protected double speed;
  protected double x;
  protected double y;
  protected KillerSheep ks;

  public Bernstein(TransformableContent content, double speed, double x, double y, KillerSheep ks)
  {
    super(content);

    this.x = x;
    this.y = y;

    setLocation(x, y);
    this.speed = speed;
    this.ks = ks;
  }

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
    }

    if (x < 0)
    {
      x = 30;
    }
    if (x > 1100)
    {
      x = 1100;
    }
    if (y < 0)
    {
      y = 4.0;
    }
    if (y > 455)
    {
      y = 455;
    }

  }

  @Override
  public void handleTick(int arg0)
  {
    Iterator<Sprite> i;
    Sprite paddock;

    i = antagonists.iterator();
    while (i.hasNext())
    {
      paddock = i.next();

      if (intersects(paddock))
      {
        ks.intersectWithPaddock();
      }
    }
    setLocation(x, y);
  }

  @Override
  public void keyTyped(KeyEvent e)
  {
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
  }

  public double getX()
  {
    return x;
  }

  public double getY()
  {
    return y;
  }

}
