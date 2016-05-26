package calibrator;

public class InputProcessor {

	// Properties that have to be read in from xml
	private int xmlParameter_BoardSize_Width;											// The size of the board -> Number of items by width and height
	private int xmlParameter_BoardSize_Height;										    // The size of the board -> Number of items by width and height
	private float xmlParameter_Square_Size;												// The size of a square in your defined unit (point, millimeter,etc).
	private String xmlParameter_Calibrate_Pattern;										// One of the Chessboard, circles, or asymmetric circle pattern
	private int xmlParameter_Calibrate_NrOfFrameToUse;									// The number of frames to use from the input for calibration
	private boolean xmlParameter_Calibrate_FixAspectRatio;								// The aspect ratio
	private boolean xmlParameter_Calibrate_AssumeZeroTangentialDistortion;				// Assume zero tangential distortion
	private boolean xmlParameter_Calibrate_FixPrincipalPointAtTheCenter;				// Fix the principal point at the center
	private boolean xmlParameter_Write_DetectedFeaturePoints;							// Write detected feature points
	private boolean xmlParameter_Write_extrinsicParameters;								// Write extrinsic parameters
	private String xmlParameter_Write_outputFileName;									// The name of the file where to write
	private boolean xmlParameter_Show_UndistortedImage;									// Show undistorted images after calibration
	private boolean xmlParameter_Input_FlipAroundHorizontalAxis;						// Flip the captured images around the horizontal axis

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void getAndCheckInputs() {

		System.out.println(" InputProcessor: Initialising major parameters for calibration by hard coding");
		System.out.println(" InputProcessor: Ideally it would have to be read in from the xml file");
		System.out.println(" InputProcessor: For explanation of each parameter, look at xmlInput_try1 file in the images folder");
		xmlParameter_BoardSize_Width = 6;// 6 dots per row, in the c920 done by balaji
		xmlParameter_BoardSize_Width = 8;// 8 dots per row, in the shadow scan images
		xmlParameter_BoardSize_Height= 9; // 9 dots = corners per column
		xmlParameter_BoardSize_Height= 6; // 6 dots = corners per column in shadow scan
		xmlParameter_Square_Size= 0.019f; // assuming 19mm squares in the image
		xmlParameter_Square_Size= 0.030f; // assuming 19mm squares in the image
		xmlParameter_Calibrate_Pattern = "CHESSBOARD";
		xmlParameter_Calibrate_NrOfFrameToUse = 15;
		xmlParameter_Calibrate_NrOfFrameToUse = 10;// in shadow scan
		xmlParameter_Calibrate_FixAspectRatio = true;
		xmlParameter_Calibrate_AssumeZeroTangentialDistortion = true;
		xmlParameter_Calibrate_FixPrincipalPointAtTheCenter = true;
		xmlParameter_Write_DetectedFeaturePoints=true;
		xmlParameter_Write_extrinsicParameters = true;
		xmlParameter_Write_outputFileName = "./images/outputOfPreCalibrator_shadowScan.txt";
		xmlParameter_Show_UndistortedImage = true;
		xmlParameter_Input_FlipAroundHorizontalAxis = false;

		System.out.println(" InputProcessor:  Value of xmlParameter_BoardSize_Width                          = " + xmlParameter_BoardSize_Width							);
		System.out.println(" InputProcessor:  Value of xmlParameter_BoardSize_Height                         = " + xmlParameter_BoardSize_Height                         );
		System.out.println(" InputProcessor:  Value of xmlParameter_Square_Size                              = " + xmlParameter_Square_Size                              );
		System.out.println(" InputProcessor:  Value of xmlParameter_Calibrate_Pattern                        = " + xmlParameter_Calibrate_Pattern                        );
		System.out.println(" InputProcessor:  Value of xmlParameter_Calibrate_NrOfFrameToUse                 = " + xmlParameter_Calibrate_NrOfFrameToUse                 );
		System.out.println(" InputProcessor:  Value of xmlParameter_Calibrate_FixAspectRatio                 = " + xmlParameter_Calibrate_FixAspectRatio                 );
		System.out.println(" InputProcessor:  Value of xmlParameter_Calibrate_AssumeZeroTangentialDistortion = " + xmlParameter_Calibrate_AssumeZeroTangentialDistortion );
		System.out.println(" InputProcessor:  Value of xmlParameter_Calibrate_FixPrincipalPointAtTheCenter   = " + xmlParameter_Calibrate_FixPrincipalPointAtTheCenter   );
		System.out.println(" InputProcessor:  Value of xmlParameter_Write_DetectedFeaturePoints              = " + xmlParameter_Write_DetectedFeaturePoints              );
		System.out.println(" InputProcessor:  Value of xmlParameter_Write_extrinsicParameters                = " + xmlParameter_Write_extrinsicParameters                );
		System.out.println(" InputProcessor:  Value of xmlParameter_Write_outputFileName                     = " + xmlParameter_Write_outputFileName                     );
		System.out.println(" InputProcessor:  Value of xmlParameter_Show_UndistortedImage                    = " + xmlParameter_Show_UndistortedImage                    );
		System.out.println(" InputProcessor:  Value of xmlParameter_Input_FlipAroundHorizontalAxis           = " + xmlParameter_Input_FlipAroundHorizontalAxis           );


		// check the input parameters
		boolean goodInput = true;
		if (xmlParameter_BoardSize_Height <= 0 || xmlParameter_BoardSize_Width<= 0)
		{
			System.out.println(" InputProcessor:  Error here probably  vVVVVVVVVVVVVVVVVVVVVVVv");
			System.out.println(" InputProcessor:  Value of xmlParameter_BoardSize_Width looks wrong!" );
			goodInput = false;
		}
		if (xmlParameter_Square_Size<= 10e-6)
		{
			System.out.println(" InputProcessor:  Error here probably  vVVVVVVVVVVVVVVVVVVVVVVv");
			System.out.println(" InputProcessor:  Value of xmlParameter_Square_Size looks wrong!" );
			goodInput = false;
		}
		if (xmlParameter_Calibrate_NrOfFrameToUse <= 0)
		{
			System.out.println(" InputProcessor:  Error here probably  vVVVVVVVVVVVVVVVVVVVVVVv");
			System.out.println(" InputProcessor:  Value of xmlParameter_Calibrate_NrOfFramToUse looks wrong!" );
			goodInput = false;
		}
	}

	public int getRows()
	{
		return(xmlParameter_BoardSize_Height);
	}

	public int getCols()
	{
		return(xmlParameter_BoardSize_Width);
	}
	public float getSize()
	{
		return(xmlParameter_Square_Size);
	}
}
