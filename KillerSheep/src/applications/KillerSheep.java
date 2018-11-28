package applications;

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
  private Content sheep1;
  private Content sheep2;
  private Content paddock;
  private Content grass;
  private Content ksmenu;
  private Bernstein b;
  private Sheep sh;
  private Sheep sh1;
  private Sheep sh2;
  private Paddock p;

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

    stage = new Stage(1);
    grass = factory.createContent("grass.png", 3, false);
    ksmenu = factory.createContent("KS_menu.jpg", 3, false);
    stage.add(ksmenu);
    VisualizationView view = stage.getView();
    view.setBounds(0, 0, this.width, 600);

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
    // start.setFont(new Font("Serif", Font.PLAIN, 20));
    start.setHorizontalAlignment(SwingConstants.CENTER);
    start.setSize(100, 60);
    start.setLocation(700, 615);

    // Replay Button
    replayButton = new JButton("Replay?");
    // replayButton.setFont(new Font("Serif", Font.PLAIN, 20));
    replayButton.setHorizontalAlignment(SwingConstants.CENTER);
    replayButton.setSize(100, 60);
    replayButton.setLocation(700, 615);

    // Pause button
    pause = new JButton("Pause");
    // pause.setFont(new Font("Serif", Font.PLAIN, 15));
    pause.setHorizontalAlignment(SwingConstants.CENTER);
    pause.setSize(70, 70);
    pause.setLocation(1000, 615);

    // Resume button
    resume = new JButton("Play");
    // resume.setFont(new Font("Serif", Font.PLAIN, 15));
    resume.setHorizontalAlignment(SwingConstants.CENTER);
    resume.setSize(70, 70);
    resume.setLocation(1100, 615);

    // create the instructions panel
    instructions = new JLabel();
    instructions.setText("Bernstein is only one click away! \u2192");
    // instructions.setFont(new Font("Serif", Font.PLAIN, 20));
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
    stage.remove(sh1);
    b = new Bernstein(bernstein, 3, 1000.0, 0.0, this);
    sheep = factory.createContent("Happysheep.png", 4, false);
    sh = new Sheep(sheep, 0, 950.0, 0.0, this, b);
    sh.setScale(0.5);

    // Add herd
    sheep1 = factory.createContent("rsz_sheep_png2190.png", 4, false);
    sh1 = new Sheep(sheep1, 0, 1090.0, 20, this, b);
    sh1.setScale(0.5);

    sh.setScale(0.5);
    stage.add(b);
    stage.add(sh);
    stage.add(sh1);

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
    x = b.getX();
    y = b.getY();
    stage.remove(b);
    bernstein = factory.createContent("flippedBernstein.png", 4, false);
    b = new Bernstein(bernstein, 3, x, y, this);
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

      stage.remove(ksmenu);
      stage.add(grass);

      // Add Bernstein
      bernstein = factory.createContent("b2.png", 4, false);
      b = new Bernstein(bernstein, 7, 370, 450.0, this);
      // stage.add(b);
      // Make bernstein movable
      contentPane.addKeyListener(b);

      // Fix start button issues
      contentPane.setFocusable(true);
      contentPane.requestFocus();
      contentPane.requestFocusInWindow();

      // Add killer sheep (single for now)
      sheep = factory.createContent("rsz_sheep.png", 4, false);
      sh = new Sheep(sheep, 0, 270.0, 450.0, this, b);
      sh.setScale(0.5);

      // Add herd
      sheep1 = factory.createContent("rsz_sheep_png2190.png", 4, false);
      sh1 = new Sheep(sheep1, 0, 150, 350.0, this, b);
      sh1.setScale(0.5);

      sheep2 = factory.createContent("rsz_sheep_png2190.png", 4, false);
      sh2 = new Sheep(sheep2, 0, 30, 450, this, b);
      sh2.setScale(0.5);

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
      stage.clear();
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
