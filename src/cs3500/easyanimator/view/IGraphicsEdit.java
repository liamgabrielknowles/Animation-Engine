package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IReadOnlyModel;

/**
 * An interface to represent the public commands that are involved in re-painting the animations.
 * This interface allows for changes to be made in a backwards-compatible nature.
 */
public interface IGraphicsEdit {

  /**
   * Changes the speed of the animation based on the input from the slider.
   *
   * @param secondsPerTick the speed of the animation based on the milliseconds of the Timer.
   */
  void setTickSpeed(int secondsPerTick);

  /**
   * Starts playing the animation by setting the play boolean to true if given true. Else pauses the
   * animation.
   *
   * @param play a boolean representing whether the animation is playing or not
   */
  void play(boolean play);


  /**
   * Sets the curernt amount of ticks passed in the animation.
   *
   * @param currentTick the current amount of ticks we want to set the animation to.
   */
  void setCurrentTick(int currentTick);

  /**
   * Resets the fields of the animation to its original state for playback. Useful for restarting.
   */
  void resetFields();

  /**
   * Getter method that rerturns the amount of ticks passed in the animation.
   *
   * @return the amount of ticks passed in the animation.
   */
  int getCurrentTick();

  /**
   * Getter method to extract the current loop state of an animation.
   *
   * @return the boolean of whether or not it is looping.
   */
  boolean isLooping();

  /**
   * Set the looping state of an animation to whatever boolean is given.
   *
   * @param l the looping state to be set to.
   */
  void setLooping(boolean l);

  /**
   * Updates the read only copy of the animation and assigns the copy's to be displayed.
   *
   * @param m the read only model that is read by the view.
   */
  void updateReadOnly(IReadOnlyModel m);


}
