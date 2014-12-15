/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generatecategoryimagesset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gaetan
 */
public class GenerateCategoryImagesSet {

    /*Parameters*/
    private static char separatorCSV = ';';
    private static String url = "jdbc:postgresql://localhost:5432/BDClean";
    private static String user = "utilisateur";
    private static String password = "utilisateur";
    private static int nbImageOK = 100;
    private static int nbImageNotOK = 100;
    private static String categoriesFilePath = "categories.csv";
    private static String outputPath = "output/";
    private static GeneratorImageSetFactory.GeneratorType generatorType = GeneratorImageSetFactory.GeneratorType.distancesTags;
    
    
    public static void main(String[] args) {
        //connect
        Connection c = null;
        try {
            
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, password);
            System.out.println("Opened database successfully");
            
            //Create the generator
            GeneratorImageSet genImageSet = GeneratorImageSetFactory.createGenerator(c, generatorType);
            
            //Load categories with tags from csv file
            List<List<String>> categories = loadCategories(categoriesFilePath);
            
            //For each category, find images OK and images not ok to write a CSV file
            Iterator<List<String>> iter = categories.iterator(); 
            while (iter.hasNext()) { 
                List<String> listTag = iter.next();
                printCSV(listTag.get(0), genImageSet.getImagesOK(listTag, nbImageOK), genImageSet.getImagesNotOK(listTag, nbImageNotOK));
                //System.out.println(listTag);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }  
    }
    
    /*
     * Function to write the CSV file with 2 list of image id, ok and not ok
     * ok have 1, not ok have 0 in the second column
     */
    public static void printCSV(String filename, List<String> listImageIdOK, List<String> listImageIdNotOK) throws FileNotFoundException{
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
    
    /**
     * Load the csv file with category
     * One category per lign, the name of the category is the first column and tags are in others columns
     */
    public static List<List<String>> loadCategories(String filepath) throws FileNotFoundException, IOException{
        List<List<String>> listCategories = new ArrayList<>();
        
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = br.readLine()) != null) {
            List<String> tags = new ArrayList(Arrays.asList(line.split(""+separatorCSV)));
            while(tags.remove("")); //Remove blanck tags
            listCategories.add(tags);
        }
        br.close();
        return listCategories;
    }
}
