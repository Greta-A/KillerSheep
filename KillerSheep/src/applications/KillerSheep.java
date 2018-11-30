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
  private JPanel contentPane;
  private JButton start;
  private JButton replayButton;
  private JButton pause;
  private JButton resume;
  private JLabel instructions;
  private ResourceFinder finder;
  private ContentFactory factory;
  private Stage stage;
  private Content bernstein;
  private Content sheep;
  private Content paddock;
  private Content grass;
  private Content ksmenu;
  private Bernstein b;
  private Sheep sh;
  private Paddock p;
  private BoomBox darnbox;
  private BoomBox sheepBox;

  public KillerSheep(int width, int height)
  {
    super(width, height);

    finder = ResourceFinder.createInstance(resources.Marker.class);
    BufferedSoundFactory sf = new BufferedSoundFactory(finder);
    factory = new ContentFactory(finder);

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

  }

  public void intersectWithPaddock()
  {
    instructions.setText("You Win!");
    start.setVisible(false);

    stage.stop();
    stage.remove(b);
    stage.remove(sh);
    b = new Bernstein(bernstein, 10, p.getX() + 25, 0.0, this);
    sheep = factory.createContent("Sheep+Herd.png", 4, false);
    sh = new Sheep(sheep, 0, p.getX() + 50, 0.0, this, b);
    sh.setScale(0.5);

    stage.add(sh);
    stage.add(b);

    pause.setVisible(false);
    resume.setVisible(false);
    contentPane.add(replayButton);
  }

  public void intersectWithBernstein()
  {
    double x, y;
    instructions.setText("You Lose!");
    start.setVisible(false);
    pause.setVisible(false);
    resume.setVisible(false);
    stage.stop();

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
    bernstein = factory.createContent("flippedBernstein.png", 4, false);
    b = new Bernstein(bernstein, 0, x, y, this);
    stage.add(b);
    contentPane.add(replayButton);

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == start)
    {

      stage.stop();
      stage.remove(ksmenu);
      stage.add(grass);

      try
      {
        sheepBox.start();
      }
      catch (LineUnavailableException e1)
      {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      // Add Bernstein
      bernstein = factory.createContent("b2.png", 4, false);
      b = new Bernstein(bernstein, 10, 370, 450, this);
      // stage.add(b);
      // Make bernstein movable
      contentPane.addKeyListener(b);

      // Fix start button issues
      contentPane.setFocusable(true);
      contentPane.requestFocus();
      contentPane.requestFocusInWindow();

      // Add killer sheep and herd
      sheep = factory.createContent("Sheep+Herd.png", 4, false);
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

      stage.addKeyListener(b);

      stage.start();
      instructions.setText(
          "Use the arrow keys to direct Bernstein safely into the paddock! Avoid the angry, killer sheep!");
      instructions.setSize(990, 85);
      start.setVisible(false);
      contentPane.add(pause);
      contentPane.add(resume);

    }

    if (e.getSource() == replayButton)
    {
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
