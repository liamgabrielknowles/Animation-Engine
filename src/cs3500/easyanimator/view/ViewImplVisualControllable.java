package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.IReadOnlyModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A editable view for the animation. Has custom stop, start, restart buttons and gives user the
 * ability to edit keyframes and shapes.
 */
