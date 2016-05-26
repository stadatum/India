package calibrator;

import java.util.List;
import java.util.Scanner;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

public class DrawIdentifiedCornersOnInputImages {
	String filePath ;
	int rows,cols;
	Scanner sc;
	public DrawIdentifiedCornersOnInputImages(int cols, int rows)
	{
		sc = new Scanner (System.in);
		this.rows = rows;
		this.cols = cols;
		System.out.println(" DrawIdentifiedCornersOnInputImages : Cols = " + this.cols);
		System.out.println(" DrawIdentifiedCornersOnInputImages : Rows = " + this.rows);
		// sc.next();
			
	}

	public void checkIdentifiedImagePoint(String[] imageNames,List <Mat> imagePoints, String directoryPath)
	{
		for(int i=0;i<imageNames.length;i++)
		{
			System.out.println(" DrawIdentifiedCornersOnInputImages : ===============================================");
			System.out.println(" DrawIdentifiedCornersOnInputImages : Checking the identified image points on image " + i);
			filePath = directoryPath + imageNames[i];
			System.out.println(" DrawIdentifiedCornersOnInputImages : Loading the image = " + filePath);
			Mat img = Imgcodecs.imread(filePath);
			MatOfPoint2f corners = new MatOfPoint2f(imagePoints.get(i));
			System.out.println(" DrawIdentifiedCornersOnInputImages : Corners of the im " + filePath + " size = " + corners.size());
			Calib3d.drawChessboardCorners(img, new Size(cols,rows), corners, true);
			System.out.println(" DrawIdentifiedCornersOnInputImages : Completed drawing the detected corners on the  image");
			filePath = directoryPath + "identifiedCornersOnImage_"+i+".jpg";
			Imgcodecs.imwrite(filePath,img);
			System.out.println(" DrawIdentifiedCornersOnInputImages : Completed drawing the detected corners in"+filePath);
		}
	}

	public void checkCameraMatrices(Mat cameraMatrix,Mat distortionCoefficients){
		System.out.println(" DrawIdentifiedCornersOnInputImages : Check the matrices for NAN values");
		if (Core.checkRange(cameraMatrix))
		{
			System.out.println(" DrawIdentifiedCornersOnInputImages : Camera matrix doesnot have nan");
		}
		if (Core.checkRange(distortionCoefficients))
		{
			System.out.println(" DrawIdentifiedCornersOnInputImages : distortionCoefficients doesnot have nan");
		}
	}

}
