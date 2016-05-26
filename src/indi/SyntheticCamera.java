package indi;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.CvType;   		// To give type of image
import org.opencv.imgcodecs.Imgcodecs; 	// To write image to png file

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import indi.India; // To use the covert function; mat to image;
import calibrator.PreCalibrator; // To use imagelist identifier and corner finder


public class SyntheticCamera {
	
	Random randy;
	List<Mat> imagePoints = new ArrayList<Mat>();
	Mat chessboard ;
	double transformedX,transformedY,transformedZ;
	int chessBoardWidth  ;// These get assigned when chessboard gets formed for the first time.   
	int chessBoardHeight ;// These get assigned when chessboard gets formed for the first time.   
	int numberOfBoxesInX ;// These get assigned when chessboard gets formed for the first time.   
	int numberOfBoxesInY ;// These get assigned when chessboard gets formed for the first time.   
	int numberOfImages   ;// Number of images to generate.
	double [][] t,r; // Translation and rotation vectors.
	double rotationAboutX_degrees=0;
	double rotationAboutY_degrees=0;
	double rotationAboutZ_degrees=0;
			
	
	
	// constructor
	public SyntheticCamera(int numberOfImages){
		
		this.numberOfImages = numberOfImages;
		t = new double[numberOfImages][3];
		r = new double[numberOfImages][3];
		System.out.println("\n From SyntheticCamera Constructor : ");
		
		// make a random number generator;
		randy = new Random();
		
	}
	
	public double[][] getRotationVectorsUsedInImageGeneration(){
		return r;
	}
	
	public double[][] getTranslationVectorsUsedInImageGeneration(){
		return t;
	}
	
	public void applyIntrinsicExtrinsicTransformationsToTheChessBoardAndSaveImagesToSynthetic(int imageHeight,int imageWidth, double fxGuess,double fyGuess){
		// Mat that will hold the transformed matrix
		Mat transformedChess_1=  new Mat();
		
		for (int i=0;i<numberOfImages;i=i+1){
			System.out.format("\n First generating translation and rot vector 2d arrays.");
			
			System.out.format("\n Generating image %d with extrinsic and intrinsic transformations. ",i);
			// X and Y are in mm in the real world. chess board is 150 mm wide, 100 mm tall
			// Chess board origin is at the top left corner.
			// Image origin is also at top left corner.
			// Chess board origin and cmos origin coincide in x and y
			
			// Then the cx and cy are given to move the origin of the board in the image to the center of the image
			 t[i][0]= -400+randy.nextInt(30);
			 t[i][1]= -400+randy.nextInt(30);
			 t[i][2]= 1000+randy.nextInt(20); // All images are between 0.9m and 1.0m
			// double [] t= {-200,-200, 1200+i}; // All images are between 0.9m and 1.0m
			
			
			// Oreilly learning open cv , page 379, 395/571
			// Rotation about various axis
			r[i][0] = randy.nextInt(10);
			r[i][1] = randy.nextInt(10);
			r[i][2] = randy.nextInt(10);
			
			rotationAboutX_degrees = r[i][0];
			rotationAboutY_degrees = r[i][1];
			rotationAboutZ_degrees = r[i][2];
				
			// if (i<2*5){
			// 	rotationAboutX_degrees = 1*i;//randy.nextInt(60);
			// 	rotationAboutY_degrees = 0;//randy.nextInt(60);
			// 	rotationAboutZ_degrees = 0;//randy.nextInt(60);
			// }
			// if (i>=2*5 && i<2*10){
			// 	rotationAboutX_degrees = 0;//randy.nextInt(60);
			// 	rotationAboutY_degrees = 1*(i-10);//randy.nextInt(60);
			// 	rotationAboutZ_degrees = 0;//randy.nextInt(60);
			// }
			// if (i>=2*10 && i<2*15){
			// 	rotationAboutX_degrees = 0;//randy.nextInt(60);
			// 	rotationAboutY_degrees = 0;//randy.nextInt(60);
			// 	rotationAboutZ_degrees = 1*(i-20);//randy.nextInt(60);
			// }
			
			// Intrinsics
			double fx = fxGuess;
			double fy = fyGuess;
			double cx = imageWidth/2;//chessBoardWidth*2;
			double cy = imageHeight/2;//chessBoardHeight*2;
			
			// Make the destination image all black, not fully black though.
			transformedChess_1 =  Mat.ones(imageHeight,imageWidth,CvType.CV_8U);
			// Make it gray now.
			for (int r=0;r<imageHeight;r++){
				for (int c=0;c<imageWidth;c++){
					transformedChess_1.put(r,c,122);
				}
			}
			
			double [][] Rx= {{1 , 0 , 0},{0 , Math.cos(Math.toRadians(rotationAboutX_degrees)) , Math.sin(Math.toRadians(rotationAboutX_degrees))},{0 , -Math.sin(Math.toRadians(rotationAboutX_degrees)) , Math.cos(Math.toRadians(rotationAboutX_degrees))}};
			double [][] Ry= {{Math.cos(Math.toRadians(rotationAboutY_degrees)) , 0 , -Math.sin(Math.toRadians(rotationAboutY_degrees))},{0 , 1 , 0},{Math.sin(Math.toRadians(rotationAboutY_degrees)) , 0 , Math.cos(Math.toRadians(rotationAboutY_degrees))}};
			double [][] Rz= {{Math.cos(Math.toRadians(rotationAboutZ_degrees)) , Math.sin(Math.toRadians(rotationAboutZ_degrees)) , 0},{-Math.sin(Math.toRadians(rotationAboutZ_degrees)) , Math.cos(Math.toRadians(rotationAboutZ_degrees)), 0},{0 , 0 , 1}};
			
			// Find the combined rotation matrix
			double [][] R = multiplyMatrices(Rx,(multiplyMatrices(Ry,Rz)));
			// Random initialisation of extrinsics
			double [][] extrinsic = {{0, 0 , 0 , 0},{0, 0 , 0 , 0},{0, 0 , 0 , 0}};
			
			// Initialisation of intrinsics
			double [][] intrinsic = {{fx, 0 , cx},{0, fy , cy},{0, 0 , 1}};
			
			// Apply the rotation to all elements of the chessBoard
			for (int pixelCol=0;pixelCol<chessBoardWidth ;pixelCol++){
				for (int pixelRow=0;pixelRow<chessBoardHeight ;pixelRow++){
					
					double [] XY1 = {pixelCol,pixelRow,10,1}; // all points are in plane z=0 and XY1 is in homogeneous coordinates. In this case if rotation about x is say 10 degrees, the bottom row of points are behind the chessboard in negative direction. Whne making homogeneous coordinates, the other two become negative. To avoid this give sufficient z coordinate in chessboard coordinate system itself.
					// When calculating the vector, first can be column.
					XY1[3] = 1; // Homogeneous part.
					
					// Form combined extrinsic from r1 and r2 of rotation and translation vector.
					extrinsic[0][0] = R[0][0];
					extrinsic[1][0] = R[1][0];
					extrinsic[2][0] = R[2][0];
					
					extrinsic[0][1] = R[0][1];
					extrinsic[1][1] = R[1][1];
					extrinsic[2][1] = R[2][1];
					
					extrinsic[0][2] = R[0][2];
					extrinsic[1][2] = R[1][2];
					extrinsic[2][2] = R[2][2];
					
					extrinsic[0][3] = t[i][0];
					extrinsic[1][3] = t[i][1];
					extrinsic[2][3] = t[i][2];
					
					// Apply transformation to the world vector;
					double [] temp=  multiplyMatrixVector(extrinsic, XY1);
					temp=  multiplyMatrixVector(intrinsic, temp);
					
					// When putting inside the mat, first should be row.
					transformedX = (temp[0]);
					transformedY = (temp[1]);
					transformedZ = (temp[2]);
					
					
					// Getting the z coordinate into 1, for making it a 2d rep of 3d body, and 2d rep is in homogeneous coordinate
					double homo_transformedX = (transformedX/transformedZ);
					double homo_transformedY = (transformedY/transformedZ);
					double homo_transformedZ = (transformedZ/transformedZ);
					
					//System.out.format("\n Original coordinates (r,c) " + 
					//" = %d , %d are mapped to (r,c) = %f %f %f ",pixelRow,pixelCol,transformedY,transformedX,transformedZ);
					//System.out.format("\n Original coordinates (r,c) " +
					//" = %d , %d are mapped to Homogeneous (r,c) = %f %f %f ",pixelRow,pixelCol,homo_transformedY,homo_transformedX,homo_transformedZ);
					//System.out.format("\n Original coordinates (r,c) " + 
					//"= %d , %d are mapped to Homogeneous ints (r,c) = %d %d %d ",pixelRow,pixelCol,(int)homo_transformedY,(int)homo_transformedX,(int)homo_transformedZ);
					
					// Put wants row first.
					transformedChess_1.put((int)(homo_transformedY),(int)(homo_transformedX),chessboard.get(pixelRow,pixelCol));
					
				}
			}
			Imgcodecs.imwrite(".//images//TransformationStudy//input" +i+"_tx="+t[i][0]+"ty="+t[i][1]+"tz="+t[i][2]+"Rx="+rotationAboutX_degrees+"Ry="+rotationAboutY_degrees+"Rz="+rotationAboutZ_degrees+".jpg",transformedChess_1);
		}
	}
	
	private double[][] multiplyMatrices(double[][] a, double [][] b){
	   int rowsInA = a.length;
       int columnsInA = a[0].length; // same as rows in B
       int columnsInB = b[0].length;
       double [][] c = new double[rowsInA][columnsInB];
       for (int i = 0; i < rowsInA; i++) {
           for (int j = 0; j < columnsInB; j++) {
               for (int k = 0; k < columnsInA; k++) {
                   c[i][j] = c[i][j] + a[i][k] * b[k][j];
               }
           }
       }
       return c;
   }

	private double[] multiplyMatrixVector(double[][] a, double []b){
	   int rowsInA = a.length;
       int columnsInA = a[0].length;
       int rowsInB = b.length; // same as cols in a
       double [] c = new double[rowsInA];
       for (int i = 0; i < rowsInA; i++) {
            for (int k = 0; k < columnsInA; k++) {
                c[i] = c[i] + a[i][k] * b[k];
            }
       }
       return c;
   }

   public void generateChessBoardPattern(int boxWidth ,int boxHeight ,int numberOfCornersInY,int numberOfCornersInX){
		
		this.numberOfBoxesInX = numberOfCornersInX + 1;
		this.numberOfBoxesInY = numberOfCornersInY + 1;
		
		this.chessBoardWidth  = numberOfBoxesInX * boxWidth ;  
		this.chessBoardHeight = numberOfBoxesInY * boxHeight;  
		
		// create a matrix
		Mat gridMatrix = Mat.zeros(chessBoardHeight,chessBoardWidth,CvType.CV_8U);
		
		// Make alternate columns white, first is white.
			for (int gridCol=0;gridCol<numberOfBoxesInX;gridCol++){
				for (int gridRow=0;gridRow<numberOfBoxesInY;gridRow++){
					if ((gridCol+gridRow)%2 == 1){
						for (int pixelCol=gridCol*boxWidth;pixelCol<gridCol*(boxWidth)+boxWidth ;pixelCol++){
							for (int pixelRow=gridRow*boxHeight;pixelRow<gridRow*(boxHeight)+boxHeight ;pixelRow++){
								gridMatrix.put(pixelRow, pixelCol, 255); 	
							}
						}
					}
				}
			}
		
		
		// Convert the matrix into image
		// Image im = India.convertMat2Image( gridMatrix);
		// Imgcodecs.imwrite(".//images//TransformationStudy//chessBoard.png",gridMatrix);
		this.chessboard =  gridMatrix;
		
	
	}
}
