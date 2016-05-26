package calibrator;

import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

public class Reprojector {

	// file path to store readin images
	String filePath;
	
	public Reprojector(){

	}
	public double reprojectTheObjectPoints(List<Mat> objectPoints,List<Mat> imagePoints,Mat distortionCoefficients,Mat cameraMatrix,
			List<Mat> rotationVectors, List<Mat> translationVectors, Mat perViewErrors, String[] imageNames,String directoryPath,Size cornerSize) 
	
	{
		System.out.println("Creating matrices that will hold the reprojected points.");
		System.out.println("3D object points are being reprojected in to 2D space using the given extrinsic and intrinsic parameters");
		MatOfPoint2f cornersReProjected = new MatOfPoint2f();
		System.out.println("Creating arrays that will hold the errors");
		double totalError = 0;
		double error;
		float viewErrors[] = new float[objectPoints.size()];

		// projectPoints expects the distortion coefficients in MatOfDouble form
		MatOfDouble distortionCoefficientsMatOfDouble = new MatOfDouble(distortionCoefficients);
		
		int totalPoints = 0;
		for (int i = 0; i < objectPoints.size(); i++) {
			MatOfPoint3f points = new MatOfPoint3f(objectPoints.get(i));
			Calib3d.projectPoints(points, rotationVectors.get(i), translationVectors.get(i),
					cameraMatrix, distortionCoefficientsMatOfDouble, cornersReProjected);
			
			System.out.println("Drawing the reprojected points on the original images;");
			filePath = directoryPath+ imageNames[i];
			System.out.println("Loading the first image = " + filePath);
			Mat img = Imgcodecs.imread(filePath);
			Calib3d.drawChessboardCorners(img, cornerSize, cornersReProjected, true);
			filePath = directoryPath + "reprojected_"+i+".jpg";
			Imgcodecs.imwrite(filePath,img);
			
			
			System.out.println("Computing the error now"); 
			error = Core.norm(imagePoints.get(i), cornersReProjected, Core.NORM_L2);

			int n = objectPoints.get(i).rows();
			viewErrors[i] = (float) Math.sqrt(error * error / n);
			totalError  += error * error;
			totalPoints += n;
		}
		perViewErrors.create(objectPoints.size(), 1, CvType.CV_32FC1);
		perViewErrors.put(0, 0, viewErrors);

		// Display error
		double rmsError =Math.sqrt(totalError / totalPoints); 
		System.out.println("Average rms error = " + rmsError);
		
		return rmsError;
	}

}
