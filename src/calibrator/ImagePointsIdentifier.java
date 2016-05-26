package calibrator;

import java.util.List;
import java.util.Scanner;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImagePointsIdentifier {

	public List<Mat> getApproximateImagePoints(String[] imageNames,int rows, int cols, List<Mat> imagePoints,String directoryPath) 
	{
		
		System.out.println(" ======================================================");
		System.out.println(" ImagePointsIdentifier: Get the corners from each image, estimate Approximately");
		String filePath;
		
		// The pattern size is the number of corners (9x6)
		System.out.println(" Important *** Make sure size is column x row. not row x column");
		Size patternSize = new Size(cols,rows);
		System.out.println(" ImagePointsIdentifier: Current setup W = " + patternSize.width + "H = " + patternSize.height);
		
		
		for(int i=0;i<imageNames.length;i++)
		{
			filePath =  directoryPath+ imageNames[i];
			Mat inputImageGray = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_GRAYSCALE);
			System.out.println(" Getting approximate estimation of image points in image : "+filePath);
			// Check if corners were found
			// Place to put found corners
			MatOfPoint2f foundCorners = new MatOfPoint2f();
			Calib3d.findChessboardCorners(inputImageGray,patternSize, foundCorners);
			imagePoints.add(i,foundCorners);
		}
		for (int i=0;i<imagePoints.size();i++)
		{
			System.out.println(" ImagePointsIdentifier:  Image number " + i + " has " + imagePoints.get(i).size() +" number of points");
		}
		return(imagePoints);
	}

	public List<Mat> getAccurateImagePoints(String[] imageNames,int rows, int cols, List<Mat> imagePoints, String directoryPath) 
	{
		
		System.out.println(" ======================================================");
		System.out.println(" ImagePointsIdentifier: Get the corners from each image, estimated Accurately");
		String filePath;
		// The pattern size is the number of corners (9x6)
		System.out.println(" ImagePointsIdentifier: Important *** Make sure size is column x row. not row x column");
		Size patternSize = new Size(cols,rows);		
		System.out.println(" ImagePointsIdentifier: Using the columns = " + cols);
		System.out.println(" ImagePointsIdentifier: Using the rows = " + rows);
		System.out.println(" ImagePointsIdentifier: Using the size " + patternSize);
		Size searchWindowSize = new Size(11,11);
		System.out.println(" ImagePointsIdentifier: Search window size in pixels " + searchWindowSize);
		Size omitWindowSize = new Size(-1,-1);
		System.out.println(" ImagePointsIdentifier: Omit window size in pixels " + omitWindowSize);
		// corner search stop termination criteria
		TermCriteria terminatingCondition = new TermCriteria(TermCriteria.COUNT+TermCriteria.MAX_ITER, 100, 1e-3);
		// Place to put found corners
		MatOfPoint2f foundCorners;
				
		for(int i=0;i<imageNames.length;i++)
		{
			filePath =  directoryPath + imageNames[i];
			Mat inputImageGray = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_GRAYSCALE);
			System.out.println(" ImagePointsIdentifier:  Total number of images to process : "+imageNames.length);
			System.out.println(" ImagePointsIdentifier:  Current image being processed =  : "+i);
			System.out.println(" ImagePointsIdentifier:  Getting first the approximate image points in image : "+filePath);
			foundCorners = new MatOfPoint2f();
			Calib3d.findChessboardCorners(inputImageGray,patternSize, foundCorners);
			
			// for (int j=0;j<foundCorners.total() ;j++){
			// 	double [] corn = foundCorners.get(1,i);
			// 	System.out.println(" ImagePointsIdentifier:  Printing the point number  foundCorners(1," +j + ") of the image " + 0 +" = "+corn[0] + " y = " + corn[1]);
			// }
			
			System.out.println(" ImagePointsIdentifier:  Size of the array of found corners = " + foundCorners.size());
			System.out.println(" ImagePointsIdentifier:  Getting the accurate corners from image index "+i+", upto subpixel resolution");
			Imgproc.cornerSubPix(inputImageGray, foundCorners, searchWindowSize, omitWindowSize,terminatingCondition);
			System.out.println("  ImagePointsIdentifier: Got the corners from image, in the foundCorners array itself.");
			imagePoints.add(i,foundCorners);
		}
		
		for (int i=0;i<imagePoints.size();i++)
		{
			System.out.println(" ImagePointsIdentifier:  Image number " + i + " has " + imagePoints.get(i).size() +" number of points");
		}
		
		// System.out.println(" ImagePointsIdentifier:  Please enter the image for which identified corners have to be printed.");
		// System.out.println(" ImagePointsIdentifier:  waiting for human input: ____");
		// Scanner sc = new Scanner(System.in);
		// int imNum = sc.nextInt();
		// System.out.println(" ImagePointsIdentifier:  Printing the identified corners of image number  " + imNum);
		// double [] corn ;// Temporarily to store the output of the get command
		// for (int i=0;i<imagePoints.get(imNum).total();i++){
		// 	corn = imagePoints.get(imNum).get(i,0);
		// 	System.out.println(" ImagePointsIdentifier:  Printing the point number  (1," +i + ") of the image " + imNum +" x= "+corn[0]+" y= " +corn[1] );
		// }
		
		return(imagePoints);
	}

}
