package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Image; // To resize the images in icon.

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import javax.swing.ImageIcon; // To put images in the gallery panel of the center panel.
import javax.swing.JLabel;// To put images in the gallery panel of the center panel.
import javax.swing.JScrollPane ; // For scrolling through the images in gallery.

import calibrator.PreCalibrator;


public class OfflineCalibratorPanel {

	// Buttons and group of those buttons have to be in the center panel now.
	JButton auto 		         = new JButton( "Auto >");
	JButton readImages           = new JButton( "Read images >");
	JButton getParameters        = new JButton("Get parameters >");
	JButton identifyCorners      = new JButton("Identify corners >");
	JButton projectCorners       = new JButton("Project corner on image >");
	JButton generate2dWorldPoints= new JButton("Generate 2d world points >");
	JButton doCalibration        = new JButton("Calibrate >");
	JButton evaluateError        = new JButton("Evaluate projection error >");

	JPanel centerPanel;
	JPanel galleryPanel;
	JScrollPane galleryPanelScroll;
	JPanel buttonPanel;
	JTextArea hints;
	PreCalibrator calibratorKaaka;

	public OfflineCalibratorPanel  ( JTextArea hints, PreCalibrator calibratorKaaka)
	{
		this.hints = hints;
		this.calibratorKaaka = calibratorKaaka;
	}
	public JPanel makeButtonsAndGalleryAndPutInCenterPanel(){
		
		// have reference to the center panel
		centerPanel  = new JPanel();
		centerPanel.setBackground(Color.YELLOW);
		buttonPanel  = new JPanel(new FlowLayout()); // Make Buttons panel
		buttonPanel.setBackground(Color.GREEN);
		galleryPanel = new JPanel(new FlowLayout()); // Make gallery panel
		galleryPanel.setBackground(Color.RED);

		// have a reference to the hints so inner classes can use it
		this.hints = hints;

		// Automatically click everything in sequence..
		auto.setMnemonic(KeyEvent.VK_L);
		auto.setActionCommand("autoMode");
		
		// First read images.
		readImages.setMnemonic(KeyEvent.VK_R);
		readImages.setActionCommand("readImages");


		// Get parameters such as length and width of squares and their number
		getParameters.setMnemonic(KeyEvent.VK_G);
		getParameters.setActionCommand("getParameters");

		//identify the corners in the images
		identifyCorners.setMnemonic(KeyEvent.VK_C);
		identifyCorners.setActionCommand("IdentifyCorners");

		//project the the corners on the images
		projectCorners.setMnemonic(KeyEvent.VK_P);
		projectCorners.setActionCommand("projectCorners");

		//Generate 2D world points
		generate2dWorldPoints.setMnemonic(KeyEvent.VK_W);
		generate2dWorldPoints.setActionCommand("generate2dPoints");

		//Do calibration 
		doCalibration.setMnemonic(KeyEvent.VK_A);
		doCalibration.setActionCommand("doCalibration");

		//Evaluate error
		evaluateError.setMnemonic(KeyEvent.VK_E);
		evaluateError.setActionCommand("evaluateError");

		//Register a listener for the  buttons.
		auto.addActionListener(new Listener4autoMode());
		readImages.addActionListener(new Listener4readImages());
		identifyCorners.addActionListener(new Listener4identifyCorners());
		getParameters.addActionListener(new Listener4getParameters());
		projectCorners.addActionListener(new Listener4projectCorners());
		generate2dWorldPoints.addActionListener(new Listener4generateWorldPoints());
		doCalibration.addActionListener(new Listener4doCalibration());
		evaluateError.addActionListener(new Listener4evaluateError());
		
		
		// attach the buttons to button panel
		buttonPanel.add(readImages);
		buttonPanel.add(getParameters);
		buttonPanel.add(identifyCorners);
		buttonPanel.add(projectCorners);
		buttonPanel.add(generate2dWorldPoints);
		buttonPanel.add(doCalibration);
		buttonPanel.add(evaluateError);
		centerPanel.add(BorderLayout.NORTH,buttonPanel);
		
		// Gallery panel will be added after pressing the read images button.
		return(centerPanel);

	}

	class Listener4readImages implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			hints.setText("\n\n Starting off line calibration using these images. :)");
			calibratorKaaka.getImageList();
			String[] imageList = calibratorKaaka.getFullImageNames();
			hints.setText("");
			hints.append("\n Check console for a list of images read in : ");
			for (int i =0;i<imageList .length;i++){
				System.out.println(" From Gui : " + i + " Image " + imageList [i]);
			}
			
			
			// Put images of the read in files in the gallery panel
			JLabel lbl = new JLabel();
			
			for (int i=0;i<10;i++){
				
				lbl = new JLabel(" Image = " + i);
				
				// // Balaji tries to resize the image.
				// ImageIcon icon = new ImageIcon(imageList[i]);
				// lbl.setIcon(icon);
				// lbl.setSize(100,100);
				// lbl.setBounds(10,10,100,100);
				
				// Trick to resize image from http://stackoverflow.com/questions/16343098/resize-a-picture-to-fit-a-jlabel
				lbl.setIcon(new ImageIcon(new ImageIcon(imageList[i]).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
				
				galleryPanel.add(lbl);
			}
			galleryPanelScroll = new JScrollPane (galleryPanel);
			centerPanel.add(BorderLayout.SOUTH,galleryPanelScroll);
			centerPanel.revalidate();
			
			
		}
	}

	
	class Listener4autoMode implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			hints.setText("\n\n Doing everything in sequence. \n Pressing so many buttons is a lot of work.");
			calibratorKaaka.getImageList();
			calibratorKaaka.getParametersFromXml();
			calibratorKaaka.identifyCornersInImage();
			calibratorKaaka.drawIdentifiedCornersOnInputImages();
			calibratorKaaka.generateWorld2dCoordinates();
			calibratorKaaka.doCalibration();
			calibratorKaaka.evaluateError();

		}
	}

	class Listener4getParameters implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			hints.setText("\n\n Getting the parameters \n rows and columns :)");
			calibratorKaaka.getParametersFromXml();
			hints.append("\n Parameters have been read in from xml. ");
		}
	}

	class Listener4identifyCorners implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			hints.setText("\n Identifying the corners in the 15 images you have given me :)");
			calibratorKaaka.identifyCornersInImage();
		}
	}	
	class Listener4projectCorners implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			hints.setText("\n Project the corners back on the 15 images you have given me :)");
			calibratorKaaka.drawIdentifiedCornersOnInputImages();
		}
	}	
	class Listener4generateWorldPoints implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			hints.setText("\n Generating world points that will be correlated to identified image points");
			calibratorKaaka.generateWorld2dCoordinates();
		}
	}	
	class Listener4doCalibration implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			
			hints.setText("\n Doing calibration");
			calibratorKaaka.doCalibration();
		}
	}	
	class Listener4evaluateError implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			hints.setText("\n Evaluating re projection errors");
			calibratorKaaka.evaluateError();
		}
	}	

}
