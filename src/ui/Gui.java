package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import calibrator.PreCalibrator;

public class Gui implements Runnable  
{
	// classes that have to be run from gui
	// first calibrator, that runs on images stored in the disk
	private PreCalibrator calibratorKaaka;


	// components
	JFrame rootWindowFrame;
	JPanel topRibbon,rightSidePanel,centerPanel;
	JPanel calibrationModeChoser;
	JButton onlineCameraCalibration;
	JButton offlineCameraCalibration;
	JButton projectorCalibrationButton;
	JTextArea hints ;
	JScrollPane scrollForHints;

	// listener for camera calibration button.
	projectorCalibrationButtonListener projectorCalListener = new projectorCalibrationButtonListener();


	public Gui (PreCalibrator input_calibrator) 
	{
		// Assign to local reference
		this.calibratorKaaka = input_calibrator;

	}
	
	public void run(){
		System.out.println("GUI: Starting to make interface");
		System.out.println("GUI: Adding camera caliberation button to window");
		// First make the buttons.
		this.makeOnlineCameraCalButton();
		this.makeOfflineCameraCalButton();
		this.makeProjectorCalibrationButton();
		// Make Ribbons after making all the buttons
		this.makeTopRibbon();
		this.makeRightSidePanel();
		this.makeCenterPanel();
		// Make frame after getting all the other ribbons
		this.makeFrame();
		
	}

	private void makeFrame()
	{
		System.out.println("GUI: Making a instance of the frame");
		rootWindowFrame  = new JFrame();

		System.out.println("GUI: Setting a icon for the main window");
		ImageIcon icon = new ImageIcon("res/root.jpg");
		Image iconForRoot = icon.getImage();
		rootWindowFrame.setIconImage(iconForRoot);
		rootWindowFrame.setTitle("STADATUM_3D");

		System.out.println("GUI: Add all panels to the window");
		rootWindowFrame.setBackground(Color.black);
		rootWindowFrame.getContentPane().add(BorderLayout.NORTH,topRibbon);
		rootWindowFrame.getContentPane().add(BorderLayout.CENTER,centerPanel);// By default, center panel is welcome message in yellow window
		rootWindowFrame.getContentPane().add(BorderLayout.EAST,rightSidePanel);
		
		System.out.println("GUI: Basic requirements");
		rootWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootWindowFrame.setSize(1000, 768);
		rootWindowFrame.setVisible(true);
	}

	private void makeCenterPanel()
	{
		centerPanel  = new JPanel();
		//centerPanel.setOpaque(true);
		//centerPanel.setBackground(Color.YELLOW);
		//centerPanel.setPreferredSize(new Dimension(200, 180));
	}

	private void makeTopRibbon()
	{
		topRibbon  = new JPanel();
		topRibbon.setBackground(Color.BLACK);
		topRibbon.setLayout(new FlowLayout());
		topRibbon.add(onlineCameraCalibration);
		topRibbon.add(offlineCameraCalibration);
		topRibbon.add(projectorCalibrationButton);
	}

	private void makeOnlineCameraCalButton()
	{
		System.out.println("GUI: Adding image to button");
		ImageIcon icon = new ImageIcon("res/camera.jpg");
		// TO DO :
		// resize icons 
		onlineCameraCalibration = new JButton("Online Cam-calibration",icon);
		System.out.println("GUI: Setting up the inner class as a listener");
		onlineCameraCalibration.addMouseListener(new OnlineCcameraCalibrationButtonListener());
		System.out.println("GUI: Setting size. This is only preferred size");
		onlineCameraCalibration.setSize(100, 100);
		System.out.println("GUI: Text to display when hovering on button");
		onlineCameraCalibration.setToolTipText("Not yet implemented. ");
		System.out.println("GUI: Setting up key board shortcut");
		onlineCameraCalibration.setMnemonic(KeyEvent.VK_N);
		onlineCameraCalibration.disable();

	}
	private void makeOfflineCameraCalButton()
	{
		System.out.println("GUI: Adding image to button");
		ImageIcon icon = new ImageIcon("res/camera.jpg");
		// TO DO :
		// resize icons 
		offlineCameraCalibration = new JButton("Offline Cam-calibration",icon);
		System.out.println("GUI: Setting up the inner class as a listener");
		offlineCameraCalibration.addMouseListener(new OfflineCameraCalibrationButtonListener());
		System.out.println("GUI: Setting size. This is only preferred size");
		offlineCameraCalibration.setSize(100, 100);
		System.out.println("GUI: Text to display when hovering on button");
		offlineCameraCalibration.setToolTipText("Use this for finding out\n intrinsic and extrinsic parameters\n of your camera.");
		System.out.println("GUI: Setting up key board shortcut");
		offlineCameraCalibration.setMnemonic(KeyEvent.VK_F);

	}

	private void makeProjectorCalibrationButton()
	{
		System.out.println("GUI: Adding projector image to button");
		ImageIcon icon = new ImageIcon("res/projector.jpg");
		projectorCalibrationButton = new JButton("Projector Calibration",icon);
		System.out.println("GUI: Setting up the inner class as a listener");
		projectorCalibrationButton.addMouseListener(projectorCalListener);
		System.out.println("GUI: Setting size. It doesn't work");
		projectorCalibrationButton.setSize(100, 100);
		System.out.println("GUI: Text to display when hovering on button");
		projectorCalibrationButton.setToolTipText("Use this for finding out intrinsic and extrinsic parameters of your projector, using your camera.");
		System.out.println("GUI: Setting up key board shortcut");
		projectorCalibrationButton.setMnemonic(KeyEvent.VK_P);
	}

	private void makeRightSidePanel()
	{
		hints = new JTextArea(140,20);
		hints.setLineWrap(true);
		hints.setFont(new Font("Serif", Font.PLAIN, 14));
		hints.setBackground(Color.BLACK);
		hints.setForeground(Color.WHITE);
		hints.setText("Help");
		
		scrollForHints = new JScrollPane(hints);
		
		// Text area > scroll pane > jpanel  
		
		rightSidePanel  = new JPanel();
		rightSidePanel.setBackground(Color.GRAY);
		rightSidePanel.setLayout(new FlowLayout());
		rightSidePanel.add(scrollForHints);
	}

	// Listener for camera calibration button, offf line
	class OfflineCameraCalibrationButtonListener implements MouseListener 
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {

			OfflineCalibratorPanel myCalibrationModeChoser = new OfflineCalibratorPanel(hints,calibratorKaaka);
			// while doing camera calibration set center panel to the mode choser
			centerPanel= myCalibrationModeChoser.makeButtonsAndGalleryAndPutInCenterPanel();
			rootWindowFrame.getContentPane().add(BorderLayout.CENTER,centerPanel);
			centerPanel.setBackground(Color.WHITE);
			centerPanel.setOpaque(true);
			centerPanel.revalidate();
			System.out.println("GUI: Added center panel for offline caliberation ");
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			hints.setText("");			
			hints.append(" \n Use camera calibration feature to find out the intrinsic and extrinsic matrices of the camera.");
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			hints.setText("");			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}
	// Listener for camera calibration button, offf line
	class OnlineCcameraCalibrationButtonListener implements MouseListener 
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			hints.append(" \nAm telling na");
			hints.append(" \nYet to be implemented .");
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			hints.setText("");			
			hints.append(" \n This feature is yet to be implemented .");
			hints.append(" \n Use off line calibration mode");
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			hints.setText("");			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

	// Listener for projector calibration button
	class projectorCalibrationButtonListener implements MouseListener 
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			projectorCalibrationButton.setText("Doing projector Calibration");
		}

		@Override
		public void mouseEntered(MouseEvent arg0) 
		{
			hints.setText("");			
			hints.append(" \n Use projector calibration feature to find out the intrinsic and extrinsic matrices of the projector. \n \n Do this after "
					+ "doing camera calibration. Camera's calibration values are required to do projector calibration.");
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			hints.setText("");			

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	// Center panel as subclass 
	class DisplayPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public void paintcomponent(Graphics g)
		{
			g.setColor(Color.BLUE);
		}

	}



}
