package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IReadOnlyModel;
import cs3500.easyanimator.model.actions.ChangeColor;
import cs3500.easyanimator.model.actions.ChangeDimension;
import cs3500.easyanimator.model.actions.ChangePosition;
import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.actions.SynchronizedActionSetImpl;
import cs3500.easyanimator.model.shapes.Color;
import cs3500.easyanimator.model.shapes.Dimension;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Posn;
import cs3500.easyanimator.model.shapes.Rectangle;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This is the class that is in responsible for drawing and updating our visual view. It uses a
 * timer to set the speed and make repetitive changes.
 */
public class UpdateDrawingEdit extends JPanel implements ActionListener, IGraphicsEdit {

  private LinkedHashMap<String, IShape> shapeIdentifier;
  private LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList;
  private LinkedHashMap<String, IShape> copyShapeIdentifier;
  private LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> copyAnimationList;
  private int currentTick;
  private boolean isPlaying = false;
  private boolean looping = false;
  private int biggestTick;
  private int timeDelay;
  private int initialDelay;
  Timer timer;

  /**
   * This is the constructor for the class that handles updating the view as the time passes.
   *
   * @param animationList   The list of animations handled.
   * @param shapeIdentifier The shapes to be drawn.
   * @param secondsPerTick  The speed in which the animation runs.
   * @throws IllegalArgumentException if the given speed (secondsPerTick) is non-positive.
   */
  UpdateDrawingEdit(LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList,
      LinkedHashMap<String, IShape> shapeIdentifier, int secondsPerTick) {
    if (secondsPerTick <= 0) {
      throw new IllegalArgumentException("Speed must be a positive integer.");
    }
    this.shapeIdentifier = shapeIdentifier;
    this.animationList = animationList;
    this.copyShapeIdentifier = copyShapeList();
    this.copyAnimationList = copyAnimationList();
    this.currentTick = 0;

    this.biggestTick = 0;
    for (String key : copyAnimationList.keySet()) {
      ArrayList<ISynchronisedActionSet> tempList = copyAnimationList.get(key);
      if (tempList.size() > 0) {
        int test = tempList.get(tempList.size() - 1).getEndTick();
        if (test > biggestTick) {
          this.biggestTick = test;
        }
      }
    }
    timeDelay = 1000 / secondsPerTick;
    initialDelay = 10 / secondsPerTick;

    timer = new Timer(0, this);
    timer.setDelay(timeDelay);
    timer.setInitialDelay(10);
    timer.restart();
  }


  /**
   * An overridden paint method so that we are able to draw all shapes that need to be drawn.
   *
   * @param g The graphics that we are using to draw the shapes on.
   */
  @Override
  public void paint(Graphics g) {
    super.paintComponents(g);
    // paint all shapes
    for (String key : copyShapeIdentifier.keySet()) {
      IShape currentShape = copyShapeIdentifier.get(key);
      // Get all of the fields to easily call later
      if (currentShape.getPosn() != null) {
        Posn currentPosn = currentShape.getPosn();
        Dimension currentDimension = currentShape.getDimension();
        Color currentColor = currentShape.getColor();
        java.awt.Color awtColor = new java.awt.Color(currentColor.getR(), currentColor.getG(),
            currentColor.getB());
        switch (currentShape.officialShapeName()) {
          case ("rectangle"):
            g.setColor(awtColor);
            g.fillRect(currentPosn.getX(), currentPosn.getY(),
                currentDimension.getW(), currentDimension.getH());
            break;
          case ("oval"):
            g.setColor(awtColor);
            g.fillOval(currentPosn.getX(), currentPosn.getY(),
                currentDimension.getW(), currentDimension.getH());
            break;
          default:
            throw new IllegalArgumentException("Not a valid shape.");
        }
      }
    }
    timer.restart();
  }

  // All animation lists
  // each animation time 1 to time 2 in each animation list

  /**
   * The action listener for the class. This is called every time the timer moves one tick
   * duration.
   *
   * @param e The action event listener for the timer.
   */
  @Override
  public void actionPerformed(ActionEvent e) {

    timer.stop();
    if (isPlaying) {
      currentTick++;
      calculateNextState();
      repaint();
    }
    timer.start();
  }

  /**
   * Determines the next tick state of the shapes by applying the animations and then calling the
   * 'repaint' method.
   */
  private void calculateNextState() {
    for (String name : copyAnimationList.keySet()) {
      ArrayList<ISynchronisedActionSet> listOfActions = copyAnimationList.get(name);
      IShape currentShape = copyShapeIdentifier.get(name);
      for (ISynchronisedActionSet ai : listOfActions) {
        if (currentTick <= ai.getEndTick() && currentTick >= ai.getStartTick()) {
          ai.applyAnimation(currentShape);
          break;
        }
      }
    }
    if (!looping && currentTick >= biggestTick) {
      isPlaying = false;
      timer.stop();
    } else if (looping && currentTick >= biggestTick) {
      resetFields();
      isPlaying = true;
      timer.start();
    }
  }

  /**
   * Changes the speed of the animation based on the input from the slider.
   *
   * @param secondsPerTick the speed of the animation based on the milliseconds of the Timer.
   */
  public void setTickSpeed(int secondsPerTick) {
    timer.setDelay(timeDelay);
    timeDelay = 1000 / secondsPerTick;
    initialDelay = 10 / secondsPerTick;
  }

  /**
   * Starts playing the animation by setting the play boolean to true.
   */
  public void play(boolean play) {
    if (play) {
      isPlaying = true;
      timer.start();
    } else {
      isPlaying = false;
    }
  }


  /**
   * Sets the current amount of ticks passed in the animation.
   *
   * @param currentTick the current amount of ticks we want to set the animation to.
   */
  public void setCurrentTick(int currentTick) {
    this.currentTick = currentTick;
  }

  /**
   * Resets the fields of the animation to its original state for playback. Useful for restarting.
   */
  public void resetFields() {
    timer.stop();
    isPlaying = false;
    this.copyAnimationList = copyAnimationList();
    this.copyShapeIdentifier = copyShapeList();
    this.currentTick = 0;
  }

  /**
   * Getter method that returns the amount of ticks passed in the animation.
   *
   * @return the amount of ticks passed in the animation.
   */
  public int getCurrentTick() {
    return this.currentTick;
  }

  /**
   * Getter method to extract the current loop state of an animation.
   *
   * @return the boolean of whether or not it is looping.
   */
  public boolean isLooping() {
    return this.looping;
  }

  /**
   * Set the looping state of an animation to whatever boolean is given.
   *
   * @param l the looping state to be set to.
   */
  public void setLooping(boolean l) {
    this.looping = l;
  }

  /**
   * Updates the read only copy of the animation and assigns the copy's to be displayed.
   *
   * @param m the read only model that is read by the view.
   */
  public void updateReadOnly(IReadOnlyModel m) {
    this.isPlaying = false;
    this.shapeIdentifier = m.getShapeIdentifier();
    this.animationList = m.getAnimationList();
    this.copyShapeIdentifier = this.copyShapeList();
    this.copyAnimationList = this.copyAnimationList();
  }

  // copys the animation list from the read copy to be displayed
  private LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> copyAnimationList() {
    LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> newList = new LinkedHashMap<>();
    LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> oldList = animationList;
    for (String key : oldList.keySet()) {
      ArrayList<ISynchronisedActionSet> oldISAL = oldList.get(key);
      ArrayList<ISynchronisedActionSet> newISAL = new ArrayList<>();
      for (ISynchronisedActionSet isa : oldISAL) {
        ArrayList<IActionCommand> oldCommandList = isa.getCommandList();
        ArrayList<IActionCommand> newCommandList = new ArrayList<>();
        for (IActionCommand ac : oldCommandList) {
          int[] fields = ac.getFieldValues();

          switch (ac.officialName()) {
            case ("color"):
              newCommandList
                  .add(new ChangeColor(fields[0], fields[1], fields[2], ac.getTicksLeft()));
              break;
            case ("position"):
              newCommandList.add(new ChangePosition(fields[0], fields[1], ac.getTicksLeft()));
              break;
            case ("dimension"):
              newCommandList.add(new ChangeDimension(fields[0], fields[1], ac.getTicksLeft()));
              break;
            default:
              throw new IllegalArgumentException("Cannot determine command.");
          }
        }
        newISAL.add(new SynchronizedActionSetImpl(isa.getStartTick(), isa.getEndTick(),
            newCommandList));

      }
      newList.put(key, newISAL);
    }
    return newList;
  }

  // copys the list of shapes from the read copy to be displayed by the view
  private LinkedHashMap<String, IShape> copyShapeList() {
    LinkedHashMap<String, IShape> newList = new LinkedHashMap<>();
    LinkedHashMap<String, IShape> oldList = shapeIdentifier;
    for (String key : oldList.keySet()) {
      IShape oldShape = oldList.get(key);
      Color newColor;
      Posn newPosn;
      Dimension newDimension;
      if (oldShape.getColor() == null) {
        newColor = null;
      } else {
        newColor = new Color(oldShape.getColor().getR(), oldShape.getColor().getG(),
            oldShape.getColor().getB());
      }
      if (oldShape.getPosn() == null) {
        newPosn = null;
      } else {
        newPosn = new Posn(oldShape.getPosn().getX(), oldShape.getPosn().getY());
      }
      if (oldShape.getDimension() == null) {
        newDimension = null;
      } else {
        newDimension = new Dimension(oldShape.getDimension().getW(),
            oldShape.getDimension().getH());
      }
      if (oldList.get(key).officialShapeName().equalsIgnoreCase("oval")) {
        newList.put(key, new Oval(newPosn, newDimension, newColor));
      } else if (oldList.get(key).officialShapeName().equalsIgnoreCase("rectangle")) {
        newList.put(key, new Rectangle(newPosn, newDimension, newColor));
      }
    }
    return newList;
  }
}