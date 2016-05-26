package calibrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import ui.Plotter;
import indi.SyntheticCamera;

public class PreCalibrator {


	// Image name list
	String[] imageNames;
	String[] fullImageNames;
	String imageSet2use;
	String images2use_c920 = "c920";
	String images2use_shadowScan = "shadowScan";
	String images2use_synthetic = "synthetic";
	String directoryPath;

	// Matrices that store the detected points matrix and the camera caliberation matrix
	List<Mat> objectPoints = new ArrayList<Mat>();
	List<Mat> imagePoints = new ArrayList<Mat>();
	List<Mat> rvecs= new ArrayList<Mat>();
	List<Mat> tvecs= new ArrayList<Mat>();
	Mat cameraMatrix= new Mat();
	Mat distortionCoefficients= new Mat();

	int rows,cols;
	float squareSize;
	int imageWidth=0,imageHeight=0;
	double fxGuess,cxGuess,fyGuess,cyGuess;
	
	// Temporary float array to hold x and y coordinates
	float[] temp = new float[2];
	float[] tempWorld = new float[3];

	// Mat to hold the reprojection errors
	Mat perViewErrors= new Mat();
	
	// To take input from keyboard;
	Scanner sc ;


	public PreCalibrator (String imageSet)
	{
		imageSet2use = imageSet;
		// Either use our own c920 images or the images supplied by shadowScan or the synthetic images.
		if (images2use_c920.compareTo(imageSet2use)==0)
		{
			System.out.println("PreCalibrator: Values for shadow scan are hard coded in precalibrator.java. Their parameters will be chosen even if you chose to use your own images.");
			System.out.println("PreCalibrator: Sloppy coding.");
			directoryPath= ".//images//balaji_c920//";
			rows = 6;
			cols = 9;
			squareSize = 19; // C920 square size in 19 mm
		}
		else if (images2use_shadowScan.compareTo(imageSet2use)==0)
		{
			System.out.println("PreCalibrator: Values for shadow scan are hard coded in precalibrator.java. Their parameters will be chosen even if you chose to use your own images.");
			System.out.println("PreCalibrator: Sloppy coding.");
			directoryPath= ".//images//shadowScan//";
			rows = 8;
			cols = 6;
			squareSize = 30; // shadow scan square size in 30 mm
		}
		else if (images2use_synthetic.compareTo(imageSet2use)==0)
		{
			System.out.println("PreCalibrator: Values for Synthetic are hard coded in precalibrator.java." +
			"Their parameters will not change automatically if you chhange them only in generating image part.");
			directoryPath = ".//images//TransformationStudy//";
			rows = 9;
			cols = 14;
			squareSize = 50; // this is in mm.
			imageHeight = 840;
			imageWidth = 1020;
			fxGuess = 700.0;
			fyGuess = 650;
			cxGuess = imageWidth/2;
			cyGuess = imageHeight/2;
			
			// If using the synthetic images, also generate them.
			System.out.println("\n From PreCalibrator Constructor : All parameters of the fake chessboard are hardcoded in PreCalibrator Constructor ");
			SyntheticCamera synCam = new SyntheticCamera(0); // 0 is the debug flag.
			synCam.generateChessBoardPattern((int)squareSize,(int)squareSize,rows,cols);
			System.out.println("\n From PreCalibrator Constructor : All parameters of the fake camera and the transRots of each image are hard coded in the transformation code itself");
			synCam. applyIntrinsicExtrinsicTransformationsToTheChessBoardAndSaveImagesToSynthetic(imageHeight,imageWidth,fxGuess,fyGuess);

		}
		else
		{
			System.out.println("PreCalibrator: Wrong specification of image directory to use !" );
		}
		
		// To pause execution
		sc = new Scanner(System.in);
     
	}

	public void getImageList()
	{
		// Get list of images in the directory
		ImageListMaker imlistMaker = new ImageListMaker();
		imageNames = imlistMaker.getImagesList(directoryPath);
		fullImageNames = new String[imageNames.length];
		
		// Prepare full file path and returning the images
		for (int i =0;i<imageNames.length;i++){
			
			fullImageNames[i] = directoryPath + imageNames[i];
		}
		
		System.out.println("PreCalibrator: Number of images  = " + imageNames.length );
		
		// System.out.println("Press enter to continue");
		// sc.next();
	}
	public String[] getFullImageNames() {
		
		return fullImageNames;
	}

	public void identifyCornersInImage()
	{
		//  Get the image points approximate and then exact points from each image
		ImagePointsIdentifier imagePointsGetter= new ImagePointsIdentifier();
		imagePoints = imagePointsGetter.getAccurateImagePoints(imageNames, rows, cols, imagePoints,directoryPath);
		System.out.println("PreCalibrator: Obtained the accurate image points, size = " + imagePoints.size());
		// System.out.println("Press enter to continue");
		// sc.next();
	}

	public void drawIdentifiedCornersOnInputImages()
	{
		// Check image points found.
		DrawIdentifiedCornersOnInputImages checker = new DrawIdentifiedCornersOnInputImages(cols,rows);
		checker.checkIdentifiedImagePoint(imageNames,imagePoints,directoryPath);
		System.out.println("PreCalibrator: Finished reprojecting corners on images");
		
		// System.out.println("Press enter to continue");
		// sc.next();
	}

	public void generateWorld2dCoordinates()
	{
		// Get the object points
		GenerateChessBoardWorldCorners_offsetStart objectPointsGen = new GenerateChessBoardWorldCorners_offsetStart(rows, cols, squareSize, imageNames.length, objectPoints);
		System.out.println("PreCalibrator: Generating world points with rows =  " + rows);
		System.out.println("PreCalibrator: Generating world points with cols =  " + cols);
		System.out.println("PreCalibrator: Generating world points with size =  " + squareSize);
		objectPoints = objectPointsGen.getObjectPoints();
		System.out.println("PreCalibrator: Finished generating world points");
		// System.out.println("Press enter to continue");
		// sc.next();
	}


	public void  doCalibration()
	{
		// Run the calibrator
		ChessBoardCalibrator calibrator = new ChessBoardCalibrator(imageHeight,imageWidth);
		calibrator.runCalibrator(imagePoints, objectPoints, imageNames, tempWorld, temp, rows,cols, cameraMatrix, distortionCoefficients, rvecs, tvecs,fxGuess,cxGuess,fyGuess,cyGuess);
		System.out.println("PreCalibrator: Finished calibration");
	}

	public JPanel plotPoints()
	{
		Plotter pointsPlotter = new Plotter();
		System.out.println("PreCalibrator: Plotting given points.");
		return(pointsPlotter.plot());

	}
	public double evaluateError()
	{
		// Reproject the object points using the obtained matrices and recreate the photos
		Reprojector myReprojector = new Reprojector();
		double finalError = myReprojector.reprojectTheObjectPoints(objectPoints, imagePoints, distortionCoefficients, cameraMatrix, rvecs, tvecs, perViewErrors,imageNames,directoryPath, new Size(cols,rows));
		System.out.println("Press enter to continue");
		sc.next();
		return finalError;
	}
	public void getParametersFromXml()
	{
		// Major assignments
		if (images2use_synthetic.compareTo(imageSet2use)==0){
			
		}
		else{
			
		}
		System.out.println("\n PreCalibrator : Assigning rows = " + rows);
		System.out.println("\n PreCalibrator : Assigning cols = " + cols);
		System.out.println("\n PreCalibrator : Assigning squareSize = " + squareSize);
	}


	}

