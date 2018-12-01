package applications;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import visual.dynamic.described.AbstractSprite;
import visual.statik.described.Content;
import visual.statik.described.TransformableContent;

/**
 * A character that falls from the top of the screen to the bottom (as in "The Matrix")
 *
 * Note: This is a simple rule-based Sprite. It does not use key-frames or tweening. Hence, it has
 * rules in handleTick().
 *
 * @author Prof. David Bernstein, James Madison University
 * @version 1.0
 */
public class FallingCharacter extends AbstractSprite
{
  private AlphaComposite alpha;
  private char[] chars;
  private Color color;
  private double baseline, left, maxX, maxY, speed;
  private Font font;
  private GlyphVector glyphs;
  private Rectangle2D defaultBounds;

  private static int COLUMNS = 16;
  private static Random rng = new Random();

  /**
   * Explicit Value Constructor
   *
   * @param stageWidth
   *          The width of the Stage
   * @param stageHeight
   *          The height of the Stage
   */
  public FallingCharacter(double stageWidth, double stageHeight)
  {
    super();
    maxX = stageWidth / (double) COLUMNS;
    maxY = stageHeight;

    defaultBounds = new Rectangle2D.Double(0.0, 0.0, 0.0, 0.0);

    font = new Font("Monospaced", Font.BOLD, 30);

    color = Color.white;// Color.GREEN;

    chars = new char[6];
    chars[0] = 'Y';
    chars[1] = 'O';
    chars[2] = 'U';
    chars[3] = 'W';
    chars[4] = 'I';
    chars[5] = 'N';

    left = rng.nextInt(COLUMNS) * maxX;
    baseline = rng.nextInt(12) * -10.0;
    // System.out.println(baseline);
    speed = rng.nextInt(6) + 2.0;

    // alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0);

    setVisible(true);
  }

  /**
   * Gets the (current) visual content for this Sprite (required by Sprite)
   *
   * Note: This method is not used since render() is overridden. It is included to honot the
   * contract specified in Sprite
   */
  public TransformableContent getContent()
  {
    Content content;
    Shape shape;

    content = null;
    if (glyphs != null)
    {
      shape = glyphs.getOutline();
      content = new Content(shape, color, color, null);

    }

    return content;
  }

  /**
   * Handle a tick event (generated by the Stage)
   *
   * @param time
   *          The current time (which is not used)
   */
  public void handleTick(int time)
  {
    // Update the location
    baseline += speed;

    if (baseline > maxY)
      baseline = rng.nextInt(12) * -10.0;
  }

  /**
   * Return the bounding box of the Sprite as it will be rendered (i.e., after being transformed).
   *
   * @return The bounding box
   */
  public Rectangle2D getBounds2D()
  {
    if (glyphs == null)
      return defaultBounds;
    else
      return glyphs.getVisualBounds();
  }

  /**
   * Render the Sprite in its current state
   *
   * Note: This class does not use AffineTransform objects to "move" the Sprite before rendering it
   * (which is slow). Instead, it simply renders the Sprite at the appropriate location (which is
   * much faster).
   *
   * @param g
   *          The rendering engine to use
   */
  public void render(Graphics g)
  {
    FontRenderContext frc;
    Graphics2D g2;

    g2 = (Graphics2D) g;
    if (glyphs == null)
    {
      // Setup the rendering engine
      g2.setFont(font);

      // Get the glyphs
      frc = g2.getFontRenderContext();
      glyphs = font.createGlyphVector(frc, chars);
    }

    // Set the color
    g2.setColor(color);

    // Use alpha blending
    // g2.setComposite(alpha);

    // Stroke, fill and compose
    g2.drawGlyphVector(glyphs, (float) left, (float) baseline);
  }
}
