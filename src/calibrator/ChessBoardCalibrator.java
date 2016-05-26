package calibrator;

import java.util.List;
import java.util.Scanner;
import java.util.Random;

import javax.swing.JPanel;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

public class ChessBoardCalibrator {

	// Scanner to stop execution of code.
	Scanner sc;
	Random randy;
	
	// Parameters of the camera that will be estimated.
	Size imageSize;
	int imageWidth,imageHeight;
	
	public ChessBoardCalibrator(int imageHeight,int imageWidth){
		this.imageWidth  = imageWidth;
		this.imageHeight = imageHeight;
		imageSize        = new Size(imageWidth,imageHeight);
		
		sc               = new Scanner(System.in);
		randy            = new Random();
		
	}
	
	double apertureWidth ;
	double apertureHeight ;
	double[] fovx;
	double[] fovy;
	double[] focalLength;
	Point principalPoint = new Point();
	double[] aspectRatio;
	
	int mFlags = 0;

	public void runCalibrator(List<Mat> imagePoints,List<Mat> objectPoints,String[] imageNames,float[] tempWorld,float[] temp,int rows, int cols, Mat cameraMatrix, Mat distortionCoefficients, List<Mat> rvecs,List<Mat> tvecs,double  fxGuess,double  cxGuess,double  fyGuess,double  cyGuess ) 
	{
		System.out.println(" ChessBoardCalibrator :  ======================================================");
		System.out.println(" ChessBoardCalibrator :  Checking data for the calibrator.");
		System.out.println(" ChessBoardCalibrator :  Number of pages in the imagePoints mat array and the objectPoints mat array have to be same.");
		System.out.println(" ChessBoardCalibrator :  Size of imagePoints in z direction = " + imagePoints.size());
		System.out.println(" ChessBoardCalibrator :  Size of objectPoints in z direction = " + objectPoints.size());
		if (objectPoints.size() != imagePoints.size())
		{
			System.out.println(" ChessBoardCalibrator :  Error here vVVVVVVVVVVVVVVVVVVVVVVv");
			System.out.println(" ChessBoardCalibrator :  Size of objectPoints and imagePoints don't match! " );
			System.out.println(" ChessBoardCalibrator :  Press ctrl c " );
			sc.next();
			
		}
		System.out.println(" ChessBoardCalibrator :  Number of detected points in each image must be equal to number of object points for the chess board in that image.");
		for(int i =0;i<imageNames.length;i++)
		{
			System.out.println(" ChessBoardCalibrator : Number of points in each image is the height of the array: ");	
			System.out.println(" ChessBoardCalibrator : Number of points in image " +i+" in object side : "+ objectPoints.get(i).size().height );
			System.out.println(" ChessBoardCalibrator : Number of points in image " +i+" in image  side : "+ imagePoints.get(i).size().height);
			if ( objectPoints.get(i).size().height  !=  imagePoints.get(i).size().height){
				System.out.println(" ChessBoardCalibrator :  Press ctrl c " );
				sc.next();
			}
			System.out.println(" ChessBoardCalibrator : Points are arranged in 1d array, so width should be one. ");	
			System.out.println(" ChessBoardCalibrator : Width of object array for image "+ i + " is " + objectPoints.get(i).size().width);
			System.out.println(" ChessBoardCalibrator : Width of image  array for image "+ i + " is "+ imagePoints.get(i).size().width);
			if ( objectPoints.get(i).size().width  !=  imagePoints.get(i).size().width){
				System.out.println(" ChessBoardCalibrator :  Press ctrl c " );
				sc.next();
			}
			
		}
		System.out.println(" ChessBoardCalibrator :  Comparing the detected corners and object point locaion for first image.");
		System.out.println(" ChessBoardCalibrator :  Rows " + rows);
		System.out.println(" ChessBoardCalibrator :  Cols " + cols);
		
		for(int i =0;i<rows*cols;i++)
		{
			objectPoints.get(0).get(i,0, tempWorld);
			if (i % cols == 0)
			{
				System.out.format(" ChessBoardCalibrator : Row number = %2d point number = %d \n", (int)(i/cols),i);
			}
			System.out.println(" ChessBoardCalibrator : in row " +(int)(i/cols)+ ", col "+(int)(i%cols)+" obj x y z = ["+ tempWorld[0] +" , "+ tempWorld[1]+" , "+ tempWorld[2]+ "]" );
			imagePoints.get(0).get(i,0, temp);
			System.out.println(" ChessBoardCalibrator : in row " +(int)(i/cols)+ ", col "+(int)(i%cols)+" img x y  = ["+ temp[0] +" , "+ temp[1]+"]");
		}
		
		// The pattern size is the number of corners (9x6)
		System.out.println(" ChessBoardCalibrator :  Important *** Make sure size is column x row. not row x column");
		Size patternSize = new Size(cols, rows);
		
		System.out.println(" ChessBoardCalibrator :  Setting flags.");
		mFlags = 0;

		// Estimating principalPoint
		// mFlags = mFlags + Calib3d.CALIB_FIX_PRINCIPAL_POINT ; // ASK THE CODE NOT TO ESTIMATE PRINCIPAL POINT. Keep it at the initial guess value only

		// Provide first guess
		mFlags = mFlags + Calib3d.CALIB_USE_INTRINSIC_GUESS ; // ASKING CODE TO USE INTRINSIC GUESS. SO WE HAVE TO GIVE GUESS CAMERA MATRIX
		
		// Aspect ratio
		mFlags = mFlags + Calib3d.CALIB_FIX_ASPECT_RATIO ; // Asking to fix the ratio fx/fy as in the initial guess.
        
		// No distortions in the synthetics
		mFlags = mFlags + Calib3d.CALIB_FIX_K1 ; // Fix the value of K1 to initial guess or 0
		mFlags = mFlags + Calib3d.CALIB_FIX_K2 ; // Fix the value of K2 to initial guess or 0
		mFlags = mFlags + Calib3d.CALIB_FIX_K3 ; // Fix the value of K3 to initial guess or 0
		mFlags = mFlags + Calib3d.CALIB_FIX_K4 ; // Fix the value of K4 to initial guess or 0
		mFlags = mFlags + Calib3d.CALIB_FIX_K5;// Fix the value of K5 to initial guess or 0
        
		// No tangential distortions
		mFlags = mFlags + Calib3d.CALIB_ZERO_TANGENT_DIST; // Tangential distortion coefficient P1 and P2 are kept as 0 values.
		
        System.out.println(" ChessBoardCalibrator :  Giving a initial guess of the camera matrix");
        Mat.eye(3, 3, CvType.CV_64FC1).copyTo(cameraMatrix);
        // Convention in row,col. Matlab is supposedly col,row.
		// cameraMatrix.put(0, 0, fxGuess + randy.nextInt(40));// 615 is the estimated fx from matlab
        // cameraMatrix.put(1, 1, fyGuess + randy.nextInt(40));// 616.9 is the estimated fy from matlab
        // cameraMatrix.put(0, 2, cxGuess + randy.nextInt(40));// 325.38 is the estimated cx from matlab
        // cameraMatrix.put(1, 2, cyGuess + randy.nextInt(40));// 246.782 is the estimated cy from matlab
        // cameraMatrix.put(0, 0, fxGuess);// 615 is the estimated fx from matlab
        // cameraMatrix.put(1, 1, fyGuess);// 616.9 is the estimated fy from matlab
        // cameraMatrix.put(0, 2, cxGuess);// 325.38 is the estimated cx from matlab
        // cameraMatrix.put(1, 2, cyGuess);// 246.782 is the estimated cy from matlab
        cameraMatrix.put(0, 0, fxGuess + 100);// 615 is the estimated fx from matlab
        cameraMatrix.put(1, 1, fyGuess + 100);// 616.9 is the estimated fy from matlab
        cameraMatrix.put(0, 2, cxGuess + 100);// 325.38 is the estimated cx from matlab
        cameraMatrix.put(1, 2, cyGuess + 100);// 246.782 is the estimated cy from matlab
        System.out.println(" ChessBoardCalibrator :  Guess of camera matrix \n " + cameraMatrix.dump());
		
		
        System.out.println(" ChessBoardCalibrator :  Giving a initial guess of the distortion matrix");
        Mat.zeros(5, 1, CvType.CV_64FC1).copyTo(distortionCoefficients);
        System.out.println(" ChessBoardCalibrator :  Guess of distortion matrix \n " + distortionCoefficients.dump());
        
		System.out.println(" ChessBoardCalibrator :  Starting the calibrator.");
        Calib3d.calibrateCamera(objectPoints, imagePoints, imageSize, cameraMatrix, distortionCoefficients , rvecs, tvecs,mFlags);
		System.out.println(" ChessBoardCalibrator :  Calibration completed. ");
		System.out.println(" ChessBoardCalibrator :  Page size of rotation rotation matrices " + rvecs.size());
		System.out.println(" ChessBoardCalibrator :  Page size of translation vectors " + tvecs.size());
		System.out.println(" ChessBoardCalibrator :  First image name :\n" + imageNames[0]);
		System.out.println(" ChessBoardCalibrator :  First rotation matrix :\n" + rvecs.get(0).dump());
		System.out.println(" ChessBoardCalibrator :  First translation vector :\n" + tvecs.get(0).dump());
		System.out.println(" ChessBoardCalibrator :  Camera matrix :\n" + cameraMatrix.dump());
		System.out.println(" ChessBoardCalibrator :  Distortion coefficients  :\n" + distortionCoefficients.dump());
		
		System.out.println(" ChessBoardCalibrator :  Retrieving physical values from the camera matrix.");
		Calib3d.calibrationMatrixValues(cameraMatrix, imageSize, apertureWidth, apertureHeight, fovx, fovy, focalLength, principalPoint, aspectRatio);
		System.out.println(" ChessBoardCalibrator :  Input image height in pixels, taken from input data				" + imageSize.height 		);
		System.out.println(" ChessBoardCalibrator :  Input image width  in pixels, taken from input data				" + imageSize.width 		);
		System.out.println(" ChessBoardCalibrator :  Physical width in mm of the sensor.                                " + apertureWidth 	);
		System.out.println(" ChessBoardCalibrator :  Physical height in mm of the sensor.                               " + apertureHeight 	);
		System.out.println(" ChessBoardCalibrator :  Output field of view in degrees along the horizontal sensor axis.  " + fovx 			);
		System.out.println(" ChessBoardCalibrator :  Output field of view in degrees along the vertical sensor axis.    " + fovy 			);
		System.out.println(" ChessBoardCalibrator :  Focal length of the lens in mm.                                    " + focalLength 	);
		System.out.println(" ChessBoardCalibrator :  Principal point in mm.                                             " + principalPoint 	);
		System.out.println(" ChessBoardCalibrator :  f_y/f_x                                                            " + aspectRatio 	);
		

	}


}
