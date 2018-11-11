package applications;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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

public class KillerSheep extends JApplication implements ActionListener
{
  // variable declarations
  private static JPanel contentPane;
  private static JButton start;
  private static JButton replayButton;
  private static JButton pause;
  private static JButton resume;
  private static JLabel instructions;
  private JTextArea blank;
  private static ResourceFinder finder;
  private static ContentFactory factory;
  private static Stage stage;
  private static Content bernstein;
  private static Content sheep;
  private static Content sheep1;
  private static Content sheep2;
  private static Content sheep3;
  private static Content paddock;
  private static Content grass;
  private static Bernstein b;
  private static Sheep sh;
  private static Sheep sh1;
  private static Sheep sh2;
  private static Sheep sh3;
  private static Paddock p;

  public KillerSheep(int width, int height)
  {
    super(width, height);
  }

  @Override
  public void init()
  {
    layout();

    finder = ResourceFinder.createInstance(resources.Marker.class);
    factory = new ContentFactory(finder);

    // Set up the stage with the grass background
    stage = new Stage(1);
    grass = factory.createContent("grass.png", 3, false);
    stage.add(grass);

    // Add Bernstein
    bernstein = factory.createContent("b2.png", 4, false);
    b = new Bernstein(bernstein, 7, 200.0, 450.0);

    // Make bernstein movable
    contentPane.addKeyListener(b);
    
    // Fix start button issues
    contentPane.setFocusable(true);
    contentPane.requestFocus();
    contentPane.requestFocusInWindow();

    // Add killer sheep (single for now)
    sheep = factory.createContent("rsz_sheep.png", 4, false);
    sh = new Sheep(sheep, 0, 20.0, 450.0);
    sh.setScale(0.5);
    
    // Add herd
    sheep1 = factory.createContent("r_HtuULY.png", 4, false);
    sheep2 = factory.createContent("r_HtuULY.png", 4, false);
    sheep3 = factory.createContent("r_HtuULY.png", 4, false);
    sh1 = new Sheep(sheep1, 0, 3.0, 450.0);
    sh2 = new Sheep(sheep2, 0, 2.0, 475.0);
    sh3 = new Sheep(sheep3, 0, 1.0, 500.0);
    sh1.setScale(0.5);
    sh2.setScale(0.5);
    sh3.setScale(0.5);

    // Add paddock
    paddock = factory.createContent("paddock.png", 4, false);
    p = new Paddock(paddock, 900.0, 0.0);
    p.setScale(1.5);

    b.addAntagonist(p);
    sh.addAntagonist(b);

    stage.add(b);
    stage.add(sh);
    stage.add(p);
    stage.add(sh1);
    stage.add(sh2);
    stage.add(sh3);

    VisualizationView view = stage.getView();
    view.setBounds(0, 0, this.width, 600);
    contentPane.add(blank);
    contentPane.add(view);
    contentPane.add(instructions);
    contentPane.add(start);
    stage.start();
    stage.stop();
    start.addActionListener(this);
    replayButton.addActionListener(this);
    pause.addActionListener(this);
    resume.addActionListener(this);
  }

  public static void main(String[] args)
  {
    KillerSheep app = new KillerSheep(1200, 700);
    invokeInEventDispatchThread(app);
  }

  public void layout()
  {
    // content pane layout should be null
    contentPane = (JPanel) getContentPane();
    contentPane.setLayout(null);

    // Start Button
    start = new JButton("Start!");
    start.setFont(new Font("Serif", Font.PLAIN, 20));
    start.setHorizontalAlignment(SwingConstants.CENTER);
    start.setSize(100, 60);
    start.setLocation(700, 615);

    // Replay Button
    replayButton = new JButton("Replay?");
    replayButton.setFont(new Font("Serif", Font.PLAIN, 20));
    replayButton.setHorizontalAlignment(SwingConstants.CENTER);
    replayButton.setSize(100, 60);
    replayButton.setLocation(700, 615);
    
    // Pause button
    pause = new JButton("Pause");
    pause.setFont(new Font("Serif", Font.PLAIN, 15));
    pause.setHorizontalAlignment(SwingConstants.CENTER);
    pause.setSize(70, 70);
    pause.setLocation(1000, 615);
    
    // Resume button
    resume = new JButton("Play");
    resume.setFont(new Font("Serif", Font.PLAIN, 15));
    resume.setHorizontalAlignment(SwingConstants.CENTER);
    resume.setSize(70, 70);
    resume.setLocation(1100, 615);
    

    // create the instructions panel
    instructions = new JLabel();
    instructions.setText("Bernstein is only one click away! \u2192");
    instructions.setFont(new Font("Serif", Font.PLAIN, 20));
    instructions.setHorizontalAlignment(SwingConstants.CENTER);
    instructions.setSize(880, 85);
    instructions.setLocation(110, 610);

    blank = new JTextArea();
    blank.setSize(this.width, 600);
    blank.setEditable(false);
  }

  public static void intersectWithPaddock()
  {
    instructions.setText("You Win!");
    start.setVisible(false);
    stage.stop();
    stage.remove(b);
    stage.remove(sh);
    b = new Bernstein(bernstein, 3, 1000.0, 0.0);
    sheep = factory.createContent("Happysheep.png", 4, false);
    sh = new Sheep(sheep, 0, 950.0, 0.0);
    sh.setScale(0.5);
    stage.add(b);
    stage.add(sh);
    contentPane.add(replayButton);
  }

  public static void intersectWithBernstein()
  {
    double x, y;
    instructions.setText("You Lose!");
    start.setVisible(false);
    pause.setVisible(false);
    resume.setVisible(false);
    stage.stop();
    x = b.getX();
    y = b.getY();
    stage.remove(b);
    bernstein = factory.createContent("flippedBernstein.png", 4, false);
    b = new Bernstein(bernstein, 3, x, y);
    stage.add(b);
    contentPane.add(replayButton);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == start)
    {
      // Sheep Sound
      BufferedSoundFactory sf = new BufferedSoundFactory(finder);
      BufferedSound baaing;
      BoomBox box;

      try
      {
        AudioInputStream stream;
        stream = AudioSystem.getAudioInputStream(finder.findURL("sheep-sound.wav"));
        baaing = sf.createBufferedSound(stream);

        auditory.sampled.Content c = baaing;
        box = new BoomBox(c);
        box.start();

      }
      catch (IOException | UnsupportedAudioFileException | NullPointerException
          | LineUnavailableException e1)
      {
        e1.printStackTrace();
      }

      stage.start();
      instructions.setText(
          "Use the arrow keys to direct Bernstein safely into the paddock! Avoid the angry, killer sheep!");
      instructions.setSize(990, 85);
      start.setVisible(false);
      contentPane.add(pause);
      contentPane.add(resume);
      blank.setVisible(false);
    }

    if (e.getSource() == replayButton)
    {
    	contentPane.removeAll();
        init();
    }
    
    if (e.getSource() == pause)
    {
      stage.stop();     
    }
    
    if (e.getSource() == resume)
    { 
      stage.start();
    }
    
  }
}
