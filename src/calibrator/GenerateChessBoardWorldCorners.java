package calibrator;

import java.util.List;
import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class GenerateChessBoardWorldCorners {

	float[] temp = new float [2];
	List <Mat> objectPoints = new ArrayList<Mat>();

	public List<Mat> getObjectPoints() {
		return objectPoints;
	}

	public GenerateChessBoardWorldCorners(int rows,int cols,float squareSize,int numImages,List<Mat> objectPoints)
	{

		System.out.println(" GenerateChessBoardWorldCorners :  ======================================================");
		System.out.println(" GenerateChessBoardWorldCorners :  Generating the positions of the corners of the squares in the world plane"); 
		System.out.println(" GenerateChessBoardWorldCorners :  The chess board corners are in a 2d plane , with each of size 3cm. ");
		System.out.println(" GenerateChessBoardWorldCorners :  Calculationg locations and putting them in an array first");
		System.out.println(" GenerateChessBoardWorldCorners :  Making a single column dummy matrix to hold value.");
		System.out.println(" GenerateChessBoardWorldCorners :  Number of rows = " + rows);
		System.out.println(" GenerateChessBoardWorldCorners :  Number of cols = " + cols);
		Mat c = Mat.zeros(rows*cols, 1, CvType.CV_32FC3);
		System.out.println(" GenerateChessBoardWorldCorners :  opencv-python-tutoroals.readthedocs.org says that the corners have to be like (0,0),(1,0) and so on.");
		System.out.println(" GenerateChessBoardWorldCorners :  For these images, origin is at top left. First is x co-ordinate, which goes towards right, and then y coordinate, which goes down.");
		double location[] = new double[3];
		int index;
		for (int i = 0; i < rows; i++) 
		{
			for (int j = 0; j < cols; j++)
			{
				// column location first then row location.
				index = (i*cols + j);
				location[0] = (j*squareSize);
				location[1] = (i*squareSize);
				location[2] = 0;
				System.out.println(" GenerateChessBoardWorldCorners :  Put loc [" +location[0] + "," + location[1] + "] at " + index);
				c.put(index, 0, location);
			}
		}

		System.out.println(" GenerateChessBoardWorldCorners :  Making multiple copies of the object points array. ");
		for(int i=0;i<numImages;i++)
		{
			System.out.println(" GenerateChessBoardWorldCorners :  Putting the mat into the page " + i + " of objectpoints list of mats");
			objectPoints.add(i ,c);
		}
		this.objectPoints = objectPoints;
	}



}
