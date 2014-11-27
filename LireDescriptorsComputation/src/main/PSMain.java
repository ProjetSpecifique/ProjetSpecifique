package main;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import descripteurs.MyDescriptor;
import descripteurs.MyDescriptorFactory;
import descripteurs.MyDescriptorType;

import net.semanticmetadata.lire.utils.FileUtils;

import utils.*;


/**
 * Super Main
 * @author AdeN
 *
 */
public class PSMain {
	
	private static final int PARTITIONS = 2;
	private static final int CLASS_X = 1;
	private static final int CLASS_NON_X = 2;
	
	/**
	 * args[0] - directory name for images
	 * args[1] - descriptor name
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		File workingDir = null;
		MyDescriptorType descriptorType = null;
		boolean pass = true;

		pass &= (args.length == 2);
		pass &= (args[0] != null);
		pass &= (args[1] != null);
		
        if (pass) {
            workingDir = new File(args[0]);
            pass &= workingDir.exists();
            pass &= workingDir.isDirectory();
            System.out.println("First pass ...");
        }
        
	    if (pass) {
	    	pass &= WriteCSV.initWriter(workingDir, args[1]);
	    	try {
	    		descriptorType = MyDescriptorType.valueOf(args[1]);
	    	} catch (IllegalArgumentException e) {
	    		e.printStackTrace();
	    		pass = false;
	    	}
	    	System.out.println("Second pass ...");
	    }
	    
	    if (pass) {
	    	ArrayList<File> allImages = FileUtils.getAllImageFiles(workingDir, true);
	    	RandomSelector.init();
	    	int classVal;
	    	for (Iterator<File> i = allImages.iterator(); i.hasNext(); ) {
	            File imgFile = i.next();
	            classVal = RandomSelector.getRandom(1, PARTITIONS); 
	            switch (classVal) {
	            
	            case CLASS_X:
	            case CLASS_NON_X:
	            	BufferedImage bufferedImgX = ImageUtils.openImage(imgFile);
	            	MyDescriptor descriptor = MyDescriptorFactory.buildDescriptor(descriptorType, bufferedImgX);
	            	double[] histogram = descriptor.computeHistogram();
	            	WriteCSV.writeLine(imgFile.getName(), histogram, classVal);
	            	break;
	            	
	            default:
	            	break;
	            }
	    	}
	    	pass &= WriteCSV.finish();
	    	System.out.println("Third pass & done ! Success : " + pass);
	    } else {
	    	System.out.println("Error !");
	    	System.exit(1);
	    }
    }
}
