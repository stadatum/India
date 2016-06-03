package indi;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.lang.Process ; // For running python from java
import java.io.IOException;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import calibrator.*;
// import indi.IndiaGUI1;
import ui.Gui;
import patternGenerator.BinaryPatternGenerator;

public class India {
	
	public static int debugFlag;
	
	
    public static void main(String[] args) {
        India pers = new India();
		
		// Parse commandline args
		if (args.length > 0) {
			try {
				debugFlag = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Argument" + args[0] + " must be an integer.");
				System.exit(1);
			}
		}
    }

    public India() {
        System.out.println("\n From Indi Constructor : ");
        System.out.println("\n Loading core library");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		
		System.out.println("\n From India Constructor : ");
        System.out.println("\n From India Constructor : Making a synthetic chessboard first");

		// System.out.println("Making a calibrator object from India ");
        PreCalibrator kaka = new PreCalibrator("synthetic");
		
		
		kaka.getImageList();
		// kaka.getParametersFromXml();
		kaka.identifyCornersInImage();
		kaka.drawIdentifiedCornersOnInputImages();
		kaka.generateWorld2dCoordinates();
		kaka.doCalibration();
		kaka.evaluateError();
		kaka.compareInputAndOutputTranslationsAndRotations();
		
		// Python plot
		try{
			Process p = Runtime.getRuntime().exec ("python plotInputOutputTranslations.py");
		}
		catch (IOException e){
			System.err.println("Problem in running python code from java");
			System.exit(1);
			
		}
		
		
		// // Generate binary pattern for a start.
		// System.out.println("\n From India Constructor : Generating binary patterns ");
		// BinaryPatternGenerator binaryPatternGenerator = new BinaryPatternGenerator (this);
		
		// // Big Gui part
		// System.out.println("\n From Indi Constructor : ");
        // System.out.println("Creating GUI object");
        // Gui gui = new Gui(kaka);
		// Thread guiThread = new Thread(gui, "Gui for the India code.");
        // guiThread.start();
        
		
		
		// Small Gui part
		// Mat mat = Mat.eye(5, 5, CvType.CV_8UC1);
        // System.out.println("\n From Indi Constructor : ");
        // System.out.println("Creating a simple identity matrix ");
        // System.out.println("Image to be formed from this identity mat = " + mat.dump());
        // convertMat2Image(mat);
        // 
        // System.out.println("\n From Indi Constructor : ");
        // System.out.println("Creating GUI object");
        // IndiaGUI1 gui = new IndiaGUI1();
        // System.out.println("Setting the image in the GUI ");
        // gui.setImg2display(convertMat2Image(mat));
        // 
		// System.out.println("\n From Indi Constructor : ");
        // System.out.println("Displaying the identity matrix as an image.");
        // Thread guiThread = new Thread(gui, "Class to display images from Mat.");
        // guiThread.start();
        
        
    }
	
	
    public static Image convertMat2Image(Mat inMat) {

        // source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
        // Fastest code
        
        // Buffered image;
        // Get the type of the buffered image to create.
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (inMat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        
        // Get the size of the inmat to get the array size of the byte.
        int bufferSize = inMat.channels() * inMat.cols() * inMat.rows();
		// Create a byte array of the size of the inMat.
        byte[] b = new byte[bufferSize];
		// Put the data from the inMat into the byte array b
        inMat.get(0, 0, b); // get all the pixels
        
        BufferedImage image = new BufferedImage(inMat.cols(), inMat.rows(), type);
        // creating reference to the data of the buffered image.
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        // putting data from the b byte array into the obtained reference.
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

}
