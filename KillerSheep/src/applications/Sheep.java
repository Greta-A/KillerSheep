package applications;

import java.util.Iterator;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.TransformableContent;

public class Sheep extends RuleBasedSprite
{

  protected double speed;
  protected double x, y;
  protected double sub = 250;

  public Sheep(TransformableContent content, double speed, double x, double y)
  {
    super(content);

    this.x = x;
    this.y = y;

    setLocation(x, y);
    this.speed = speed;
  }

  @Override
  public void handleTick(int arg0)
  {
    
    x = Bernstein.getX() - sub;
    y = Bernstein.getY();
    setLocation(x, y);
    
    
    Iterator<Sprite> i;
    Sprite bernstein;

    i = antagonists.iterator();
    while (i.hasNext())
    {
      
      bernstein = i.next();
      sub -= 0.025;

      if (intersects(bernstein))
      {
        KillerSheep.intersectWithBernstein();
      }
      
      //setLocation(x, y);
    }

  }

  public double getX()
  {
    return x;

  }
  
  public double getY() 
  {
    
    return y;
  }
  
  public void setX(double x)
  {
    this.x = x;

  }
  
  public void setY(double y) 
  {
    
    this.y = y;
  }

}
