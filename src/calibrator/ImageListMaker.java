package calibrator;

import java.io.File;

public class ImageListMaker {


	public String[] getImagesList(String directoryPath)
	{
		// Images identified are stored here;
		String[] imageNames;

		// The file path where the images are stored.
		File imageDirectory= new File(directoryPath);
		// Total number of images in the directory.. there may be other files also.. like xmls
		int numberOfImageFilesInImages=0;
		// array to hold file names
		String[] allFileInImagesDirList ;
		System.out.println(" ========================================================");
		System.out.println(" ImageListMaker: Getting a list of images in images folder");
		System.out.println(" ImageListMaker: You have to ensure manually the existence of 8x6 squares in the images.");
		System.out.println(" ImageListMaker: Checking if image directory  exists along side src folder and is not a file." );
		
		// Get all the reprojected images out
		
		if (imageDirectory.exists() == true && imageDirectory.isFile() != true) 
		{
			System.out.println(" ImageListMaker: Image directory images exists along side src folder and is not a file.");
			allFileInImagesDirList = imageDirectory.list();
			for (int i =0;i<allFileInImagesDirList.length;i++)
			{
				if(allFileInImagesDirList[i].contains("jpg"))
				{
					System.out.println(" ImageListMaker: File num " +i+" = "+allFileInImagesDirList[i]+" is a jpg image file; " + true);
					if(allFileInImagesDirList[i].contains("repro"))
					{
						System.out.println(" ImageListMaker: File num " +i+" = "+allFileInImagesDirList[i]+" is a reprojeccted jpg image file; " );
						try
						{
							System.out.println(" ImageListMaker: Attempting to delete file " + String.join("",directoryPath,allFileInImagesDirList[i]));
							File temp = new File(String.join("\\",directoryPath,allFileInImagesDirList[i]));
							temp.delete();
						}
						catch (Exception ne)
						{
							System.out.println(" ImageListMaker: Cannot deleted file num " +i+" = "+allFileInImagesDirList[i]+" is a reprojeccted jpg image file; " );
						}
					}
				
					
				}
			}
		}
		
		// After deleting the reprojected images, do the census again
		imageDirectory= new File(directoryPath);
		allFileInImagesDirList = imageDirectory.list();
		
		if (imageDirectory.exists() == true && imageDirectory.isFile() != true) 
		{
			System.out.println(" ImageListMaker: Image directory images exists along side src folder and is not a file.");
			allFileInImagesDirList = imageDirectory.list();
			for (int i =0;i<allFileInImagesDirList.length;i++)
			{
				if(allFileInImagesDirList[i].contains("jpg"))
				{
					numberOfImageFilesInImages++;
					System.out.println(" ImageListMaker: File num " +i+" = "+allFileInImagesDirList[i]+" is a jpg image file; " + true);
				}
				else
				{
					System.out.println(" ImageListMaker: File num " +i+" = "+allFileInImagesDirList[i]+" is not a jpg image file; " + false);
				}
			}
			System.out.println(" ImageListMaker: Total of "+ numberOfImageFilesInImages +" files are images in the directory");
			
			// Make a new array that holds only images.
			imageNames = new String[numberOfImageFilesInImages];
			System.out.println(" ImageListMaker: Putting all the image files in a separate string array");
			int counter = -1;
			for (int i =0;i<allFileInImagesDirList.length;i++)
			{
				if(allFileInImagesDirList[i].contains("jpg") ) // && allFileInImagesDirList[i].contains("imj")
				{
					counter ++;
					imageNames[counter] = allFileInImagesDirList[i]; 
				}
			}
			System.out.println(" ImageListMaker: Printing file names of only images of images directory");
			for (int i =0;i<imageNames.length;i++)
			{
				System.out.println(" ImageListMaker: Image number " +(i+1) + " = "+ imageNames[i]);
			}
		}
		else
		{
			System.out.println(" ImageListMaker: Error here probably  vVVVVVVVVVVVVVVVVVVVVVVv");
			System.out.println(" ImageListMaker: Do not forget to use \\\\ wherever \\ is required to escape backslash ");
			System.out.println(" ImageListMaker: Image directory images does not exist along side src folder or may be it is a file.");
			imageNames = new String[0];
		}
		
		return(imageNames);
	}


}
