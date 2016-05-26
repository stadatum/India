package patternGenerator;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.CvType;   		// To give type of image
import org.opencv.imgcodecs.Imgcodecs; 	// To write image to png file

import indi.India; // To access function the converts mat to image.


public class BinaryPatternGenerator{
	
	// Reference to the main function.
	India indi;
	String [] binaryPattern = {"0000","0001","0010","0011","0100"};
	Mat mat;
	Size imageSize;
	
	// default constructor.
	public BinaryPatternGenerator(India indi){
		this.indi = indi;
		makeBinaryPattern();
	}
	
	private void makeBinaryPattern(){
		// Make the canvas image mat
		mat = Mat.zeros(80, 80, CvType.CV_8UC1);
		imageSize = mat.size();
		int bandLocationOfCurrentImageColumn, numberOfColumnsOfImagePerBit;
		numberOfColumnsOfImagePerBit = (int)(imageSize.width/binaryPattern[0].length());
		System.out.println("\n From BinaryPatternGenerator : ");
        for (int imageNumber = 0;imageNumber<binaryPattern.length;imageNumber++){
			mat = Mat.zeros(80, 80, CvType.CV_8UC1);
			System.out.println("\n Creating image number " + imageNumber);
				for (int r=0;r<imageSize.height;r++){
					for (int c=0;c<imageSize.width;c++){
						// System.out.println("\n Processing column number " + c);
						bandLocationOfCurrentImageColumn = c/numberOfColumnsOfImagePerBit;
						// System.out.println("\n Quotient " + bandLocationOfCurrentImageColumn);
						if (binaryPattern[imageNumber].charAt(bandLocationOfCurrentImageColumn)=='1'){
                            mat.put(r,c,254);                    
                        }
					}
				}
			// System.out.println("Image to be formed from this identity mat = " + mat.dump());
			// Image img = indi.convertMat2Image(mat);
			Imgcodecs.imwrite(".//images//TransformationStudy//binary_" + imageNumber + ".jpg",mat);
		}
	}
	
	
	
}