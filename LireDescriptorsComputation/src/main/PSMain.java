package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

import descripteurs.AutoColorCorrelogramDescriptor;

/**
 * Super Main
 * @author AdeN
 *
 */
public class PSMain {
	
	/**
	 * args[0] - directory name for images
	 * args[1] - name for output csv file (without extension)
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		String workingDir = "";
		String csvPath = "";
        if (args[0] != null) {
            File f = new File(args[0]);
            if (f.exists() && f.isDirectory()) {
            	workingDir = args[0];
            }
        }
        
        // Fallback to current data dir (testing only)
        workingDir = workingDir == null ? System.getProperty("user.dir") + "/data/ferrari/" : workingDir;
        csvPath = csvPath == null? "data.csv": csvPath;
	    CSVWriter writer = new CSVWriter(new FileWriter(csvPath), 
	    		CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
	    // TODO : find sub-directories automatically
	    String[] subDirectories = { "red", "black", "white", "yellow" };
	    // Call descriptors
        AutoColorCorrelogramDescriptor.computeDescriptor(workingDir, writer, subDirectories);
    }
}
