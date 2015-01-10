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
    private static String url = "jdbc:postgresql://localhost:5432/BDImageBig";
    private static String user = "utilisateur";
    private static String password = "utilisateur";
    private static int nbImageOK = 200;
    private static int nbImageNotOK = 200;
    private static String categoriesFilePath = "categories.csv";
    private static String outputPath = "output/";
    private static GeneratorImageSetFactory.GeneratorType generatorType = GeneratorImageSetFactory.GeneratorType.DistancesTags;
    
    
    public static void main(String[] args) {
        //connect
        Connection c = null;
        try {
            
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, password);
            System.out.println("Opened database successfully");
            
            //Create the generator
            GeneratorImageSet genImageSet = GeneratorImageSetFactory.createGenerator(c, generatorType);
            
            //Create the CSV printer
            CSVPrinter csvPrinter = new CSVPrinter(outputPath, separatorCSV);
            
            //Load categories with tags from csv file
            List<List<String>> categories = loadCategories(categoriesFilePath);
            
            //For each category, find images OK and images not ok to write a CSV file
            Iterator<List<String>> iter = categories.iterator(); 
            while (iter.hasNext()) { 
                List<String> listTag = iter.next();
                csvPrinter.printCSVWithTag(listTag.get(0), 
                        ImageWithTags.getListImageFromListString(c, genImageSet.getImagesOK(listTag, nbImageOK), true),
                        ImageWithTags.getListImageFromListString(c, genImageSet.getImagesNotOK(listTag, nbImageNotOK), false)
                        );
                //System.out.println(listTag);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }  
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
