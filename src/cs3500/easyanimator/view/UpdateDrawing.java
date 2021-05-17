package cs3500.easyanimator.view;

import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.shapes.Color;
import cs3500.easyanimator.model.shapes.Dimension;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Posn;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
public class UpdateDrawing extends JPanel implements ActionListener {

  LinkedHashMap<String, IShape> shapeIdentifier;
  LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList;
  int secondsPerTick;
  int ticks;

  /**
   * This is the constructor for the class that handles updating the view as the time passes.
   *
   * @param animationList   The list of animations handled.
   * @param shapeIdentifier The shapes to be drawn.
   * @param secondsPerTick  The speed in which the animation runs.
   * @throws IllegalArgumentException if the given speed (secondsPerTick) is non-positive.
   */
  UpdateDrawing(LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList,
      LinkedHashMap<String, IShape> shapeIdentifier, int secondsPerTick) {
    if (secondsPerTick <= 0) {
      throw new IllegalArgumentException("Speed must be a positive integer.");
    }
    this.secondsPerTick = secondsPerTick;
    this.shapeIdentifier = shapeIdentifier;
    this.animationList = animationList;
    this.ticks = 0;
  }

  /**
   * An overridden paint method so that we are able to draw all shapes that need to be drawn.
   *
   * @param g The graphics that we are using to draw the shapes on.
   */
  @Override
  public void paint(Graphics g) {
    super.paintComponents(g);
    //this.setPreferredSize(d);
    Graphics2D g2d = (Graphics2D) g;
    // paint all shapes
    for (String key : shapeIdentifier.keySet()) {
      IShape currentShape = shapeIdentifier.get(key);
      // Get all of the fields to easily call later
      Posn currentPosn = currentShape.getPosn();
      Dimension currentDimension = currentShape.getDimension();
      Color currentColor = currentShape.getColor();
      java.awt.Color awtColor = new java.awt.Color(currentColor.getR(), currentColor.getG(),
          currentColor.getB());

      switch (currentShape.officialShapeName()) {
        case ("rectangle"):
          g2d.setColor(awtColor);
          g2d.fillRect(currentPosn.getX(), currentPosn.getY(),
              currentDimension.getW(), currentDimension.getH());
          break;
        case ("oval"):
          g2d.setColor(awtColor);
          g2d.fillOval(currentPosn.getX(), currentPosn.getY(),
              currentDimension.getW(), currentDimension.getH());
          break;
        default:
          throw new IllegalArgumentException("Not a valid shape.");
      }
    }
    Timer timer = new Timer(1000 / secondsPerTick, this);
    ticks++;
    timer.start();
  }

  /**
   * The action listener for the class. This is called every time the timer moves one tick
   * duration.
   *
   * @param e The action event listener for the timer.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    calculateNext();
  }

  /**
   * Calculates the values and fields of the shapes during the next tick to be repainted.
   */
  private void calculateNext() {
    for (String name : animationList.keySet()) {
      ArrayList<ISynchronisedActionSet> listOfActions = animationList.get(name);
      IShape currentShape = shapeIdentifier.get(name);
      for (ISynchronisedActionSet ai : listOfActions) {
        if (ticks <= ai.getEndTick() && ticks >= ai.getStartTick()) {
          ai.applyAnimation(currentShape);
          break;
        }
      }
    }
    repaint();
  }

  /**
   * Sets the amoount of ticks for a currentanimation.
   *
   * @param ticks the amount of ticks to set an animation to.
   */
  public void setTicks(int ticks) {
    System.out.println("Current ticks are " + this.ticks + ". Setting ticks to: " + ticks);
    this.ticks = ticks;
    System.out.println("Ticks have been set: " + this.ticks);
  }
}
