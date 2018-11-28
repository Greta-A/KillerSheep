package applications;

import java.util.Random;

import visual.dynamic.described.RuleBasedSprite;
import visual.statik.TransformableContent;

public class Paddock extends RuleBasedSprite
{
	protected double x, y, maxX;
	private static Random rng = new Random();
	private static final int INITIAL_LOCATION = 900;
	
	public Paddock(TransformableContent content) 
	{
		super(content);

		//this.x = x;
		//this.y = y;
		
		maxX = 900;
		x = rng.nextDouble() * maxX;
		
		//setLocation(x, y);

		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleTick(int arg0) 
	{
		if (x > (int)maxX)
		{
			x = INITIAL_LOCATION;
		}
		
		setLocation(x,y);
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
