package applications;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.JApplication;
import auditory.sampled.BoomBox;
import auditory.sampled.BufferedSound;
import auditory.sampled.BufferedSoundFactory;
import io.ResourceFinder;
import visual.VisualizationView;
import visual.dynamic.described.Stage;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;
import visual.statik.sampled.ImageFactory;

/**
 * KillerSheep: main class for KillerSheep game.
 * @author Greta, Rain, Joelle
 *
 * We abide by the JMU Honor Code
 */

public class KillerSheep extends JApplication implements ActionListener
{
  // variable declarations
  private JPanel contentPane;
  private JButton start;
  private JButton replayButton;
  private JButton pause;
  private JButton resume;
  private JLabel instructions;
  private ResourceFinder finder;
  private ContentFactory factory;
  private Stage stage;
  private Content[] bernstein;
  private Content sheep;
  private Content paddock;
  private Content grass;
  private Content ksmenu;
  private Content youWin;
  private Content youLose;
  private Bernstein b;
  private Sheep sh;
  private Paddock p;
  private BoomBox darnbox;
  private BoomBox sheepBox;
  private ImageFactory imageFactory;
  private String sheepImage = "Sheep+Herd.png";

  /**
   * KillerSheep: constructor that takes in width/height of program
   *   and initializes audio variables.
   * @param width the width of the main screen
   * @param height the height of the main screen
   */
  
  public KillerSheep(int width, int height)
  {
    super(width, height);

    finder = ResourceFinder.createInstance(resources.Marker.class);
    factory = new ContentFactory(finder);
    BufferedSoundFactory sf = new BufferedSoundFactory(finder);
    imageFactory = new ImageFactory(finder);

    // Gosh Darn It Sound
    BufferedSound darnIt;
    BufferedSound baaing;
    try
    {
      AudioInputStream stream;
      stream = AudioSystem.getAudioInputStream(finder.findURL("goshdarnit.wav"));
      darnIt = sf.createBufferedSound(stream);
      auditory.sampled.Content gosh = darnIt;
      darnbox = new BoomBox(gosh);

      stream = AudioSystem.getAudioInputStream(finder.findURL("sheep-sound.wav"));
      baaing = sf.createBufferedSound(stream);

      auditory.sampled.Content sheepSound = baaing;
      sheepBox = new BoomBox(sheepSound);
    }
    catch (IOException | UnsupportedAudioFileException | NullPointerException e1)
    {
      e1.printStackTrace();
    }

  }
  
  /**
   * main entry point into KillerSheep program, sets up dimensions. 
   * @param args command line arguments
   */
  public static void main(String[] args)
  {
    KillerSheep app = new KillerSheep(1200, 700);
    invokeInEventDispatchThread(app);
  }

  /**
   * init: initialization method required for JApplication; sets up the stage and contentPane; 
   *   adds listeners to buttons.
   */
  @Override
  public void init()
  {
    layout();

    stage = new Stage(30);
    grass = factory.createContent("grass.png", 3, false);
    ksmenu = factory.createContent("ks_menu.png", 3, false);
    stage.add(ksmenu);
    VisualizationView view = stage.getView();
    view.setBounds(0, 0, this.width, 600);

    start.addActionListener(this);
    replayButton.addActionListener(this);
    pause.addActionListener(this);
    resume.addActionListener(this);

    contentPane.add(view);
    contentPane.add(instructions);
    contentPane.add(start);
    stage.start();
    stage.stop();
  }

  /**
   * layout: helper method to create Components of the contentPane.
   */
  public void layout()
  {
	  String serif = "Serif";
	  
    // content pane layout should be null
    contentPane = (JPanel) getContentPane();
    contentPane.setLayout(null);

    // Start Button
    start = new JButton("Start!");
    start.setFont(new Font(serif, Font.PLAIN, 20));
    start.setHorizontalAlignment(SwingConstants.CENTER);
    start.setSize(100, 60);
    start.setLocation(950, 615);

    // Replay Button
    replayButton = new JButton("Replay?");
    replayButton.setFont(new Font(serif, Font.PLAIN, 20));
    replayButton.setHorizontalAlignment(SwingConstants.CENTER);
    replayButton.setSize(100, 60);
    replayButton.setLocation(700, 615);

    // Pause button
    pause = new JButton("Pause");
    pause.setFont(new Font(serif, Font.PLAIN, 15));
    pause.setHorizontalAlignment(SwingConstants.CENTER);
    pause.setSize(70, 70);
    pause.setLocation(1000, 615);

    // Resume button
    resume = new JButton("Play");
    resume.setFont(new Font(serif, Font.PLAIN, 15));
    resume.setHorizontalAlignment(SwingConstants.CENTER);
    resume.setSize(70, 70);
    resume.setLocation(1100, 615);

    // Instructions panel
    instructions = new JLabel();
    instructions.setText("Use the arrow keys to direct Bernstein safely into the paddock! "
    		+ "Avoid the angry, killer sheep!");
    instructions.setFont(new Font(serif, Font.PLAIN, 20));
    instructions.setHorizontalAlignment(SwingConstants.CENTER);
    instructions.setSize(880, 85);
    instructions.setLocation(110, 610);

  }

  /**
   * intersectWithPaddock: helper method to win the game; transports Bernstein into the 
   *  paddock with the sheep.
   */
  public void intersectWithPaddock()
  {
    instructions.setText("You Win!");
    start.setVisible(false);

    //stop the stage, prevent user from moving player
    stage.stop();

    stage.remove(b);
    stage.remove(sh);
    b = new Bernstein(bernstein, 0, p.getX() + 25, 0.0, this);
    sheep = factory.createContent(sheepImage, 4, false);
    sh = new Sheep(sheep, 0, p.getX() + 50, 0.0, this, b);
    sh.setScale(0.5);
    
    stage.add(sh);
    stage.add(b);
    
    youWin = factory.createContent("YouWin.png", 4, false);
    youWin.setLocation(450, 250);
    stage.add(youWin);

    pause.setVisible(false);
    resume.setVisible(false);

    contentPane.add(replayButton);
  }

  /**
   * intersectWithBernstein: loss state, changes player to dead Bernstein.
   */
  public void intersectWithBernstein()
  {
    double x, y;
    instructions.setText("You Lose!");
    start.setVisible(false);
    pause.setVisible(false);
    resume.setVisible(false);
    stage.stop();

    // play the "Gosh Darn It" audio
    try
    {
      darnbox.start();
    }
    catch (LineUnavailableException e)
    {
      e.printStackTrace();
    }

    x = b.getX();
    y = b.getY();
    stage.remove(b);

    BufferedImage[] images = imageFactory.createBufferedImages("flippedBernstein.png", 1, 4);
    Content[] b2 = new Content[1];
    b2[0] = factory.createContent(images[0]);

    b = new Bernstein(b2, 0, x, y, this);

    stage.add(b);
    youLose = factory.createContent("YouLose.png", 4, false);
    youLose.setLocation(450, 250);
    stage.add(youLose);


    contentPane.add(replayButton);
  }

  /**
   * actionPerformed: required for JButtons, performs events for start, play, pause, and resume
   *   buttons.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == start)
    {
      stage.stop();
      stage.remove(ksmenu);
      stage.add(grass);

      // play Sheep sound
      try
      {
        sheepBox.start();
      }
      catch (LineUnavailableException e1)
      {
        e1.printStackTrace();
      }

      // create images of Bernstein walking as player
      BufferedImage[] images = imageFactory.createBufferedImages("bernsteinWalking.png", 2, 4);
      bernstein = new Content[2];

      // Add Bernstein
      bernstein[0] = factory.createContent(images[0]);
      bernstein[1] = factory.createContent(images[1]);
      b = new Bernstein(bernstein, 10, 370, 450, this);
      
      // Fix start button issues
      contentPane.setFocusable(true);
      contentPane.requestFocus();
      contentPane.requestFocusInWindow();

      // Add killer sheep and herd
      sheep = factory.createContent(sheepImage, 4, false);
      sh = new Sheep(sheep, 5, 0.0, 100.0, this, b);
      sh.setScale(0.5);

      // Add paddock
      paddock = factory.createContent("paddock.png", 4, false);
      p = new Paddock(paddock);
      p.setScale(1.5);

      b.addAntagonist(p);
      sh.addAntagonist(b);
      
      stage.add(b);
      stage.add(sh);
      stage.add(p);

      contentPane.addKeyListener(b);

      start.setVisible(false);
      contentPane.add(pause);
      contentPane.add(resume);
      stage.start();
    }

    if (e.getSource() == replayButton)
    {
    	// remove everything from stage and call init to restart application
      stage.remove(b);
      contentPane.setVisible(false);
      contentPane.removeAll();
      contentPane.setVisible(true);
      init();
    }

    if (e.getSource() == pause)
    {
      stage.getMetronome().stop();
    }

    if (e.getSource() == resume)
    {
      contentPane.setFocusable(true);
      contentPane.requestFocus();
      contentPane.requestFocusInWindow();
      stage.getMetronome().start();
    }
  }
}
