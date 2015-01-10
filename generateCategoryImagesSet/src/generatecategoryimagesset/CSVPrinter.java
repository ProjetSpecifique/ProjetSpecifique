/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generatecategoryimagesset;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

/**
 * Class to write a CSV File
 * @author Gaetan
 */
public class CSVPrinter {
    
    private String outputPath;
    private char separatorCSV;

    public CSVPrinter(String outputPath, char separatorCSV) {
        this.outputPath = outputPath;
        this.separatorCSV = separatorCSV;
    }
    
    
    /*
     * Function to write the CSV file with 2 list of image id, ok and not ok
     * ok have 1, not ok have 0 in the second column
     */
    public void printCSV(String filename, List<String> listImageIdOK, List<String> listImageIdNotOK) throws FileNotFoundException{
        PrintStream file = new PrintStream(new FileOutputStream(outputPath + filename + ".csv", false));


        Iterator<String> iter = listImageIdOK.iterator(); 
        while (iter.hasNext()) { 
            String element = iter.next();

            file.println(element + separatorCSV + '1' + separatorCSV);
        }
        
        iter = listImageIdNotOK.iterator(); 
        while (iter.hasNext()) { 
            String element = iter.next();

            file.println(element + separatorCSV + '0' + separatorCSV);
        }
        file.close();
    }
    
    /*
     * Function to write the CSV file with 2 list of ImageWithTags, ok and not ok
     * ok have 1, not ok have 0 in the second column
     */
    public void printCSVWithTag(String filename, List<ImageWithTags> listImageIdOK, List<ImageWithTags> listImageIdNotOK) throws FileNotFoundException{
        PrintStream file = new PrintStream(new FileOutputStream(outputPath + filename + ".csv", false));


        Iterator<ImageWithTags> iter = listImageIdOK.iterator(); 
        while (iter.hasNext()) {
            file.println(iter.next().toCSV(separatorCSV));
        }
        
        iter = listImageIdNotOK.iterator(); 
        while (iter.hasNext()) { 
            file.println(iter.next().toCSV(separatorCSV));
        }
        file.close();
    }
}
