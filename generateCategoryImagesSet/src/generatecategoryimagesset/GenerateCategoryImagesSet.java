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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    
    public static void main(String[] args) {
        //connect
        Connection c = null;
        try {
            
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, password);
            System.out.println("Opened database successfully");
            
            //Load categories with tags from csv file
            List<List<String>> categories = loadCategories(categoriesFilePath);
            
            //For each category, find images OK and images not ok to write a CSV file
            Iterator<List<String>> iter = categories.iterator(); 
            while (iter.hasNext()) { 
                List<String> listTag = iter.next();
                printCSV(listTag.get(0), getImagesOK(c, listTag, nbImageOK), getImagesNotOK(c, listTag, nbImageNotOK));
                //System.out.println(listTag);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }  
    }
    
    
    /**
     * Function to find images with a tag from the parameters list
     */
    public static List<String> getImagesOK(Connection c, List<String> listTag, int number) throws SQLException{
        if(listTag == null || listTag.size() <= 0 || number <= 0) return null;
        
        List<String> listImagesId = new ArrayList<>();

        String query =  "       SELECT imageid\n" +
                        "       FROM \"imagetagfiltred\"\n" +
                        "	WHERE tag ~ '(^|^.* )" + listTag.get(0) + "($| .*$)'\n";
        for(int i=1; i<listTag.size(); i++){
            query +=    "UNION\n" +
                        "	SELECT imageid\n" +
                        "	FROM \"imagetagfiltred\"\n" +
                        "	WHERE tag ~ '(^|^.* )" + listTag.get(i) + "($| .*$)'\n";
        }
        query += "LIMIT " + number + ";";
        System.out.println(query);
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            listImagesId.add(rs.getString(1));
        }
        //close stuff
        rs.close();
        st.close();

        return listImagesId;
    }
    
    /**
     * Function to have images without tags from the params list
     */
    public static List<String> getImagesNotOK(Connection c, List<String> listTag, int number) throws SQLException{
        if(listTag == null || listTag.size() <= 0 || number <= 0) return null;
        
        List<String> listImagesId = new ArrayList<>();
        
        String query =  "       SELECT imageid\n" +
                        "       FROM \"imagetagfiltred\"\n" +
                        "	WHERE tag ~ '(^|^.* )" + listTag.get(0) + "($| .*$)'\n";
        for(int i=1; i<listTag.size(); i++){
            query +=    "UNION\n" +
                        "	SELECT imageid\n" +
                        "	FROM \"imagetagfiltred\"\n" +
                        "	WHERE tag ~ '(^|^.* )" + listTag.get(i) + "($| .*$)'\n";
        }
        
        query = "SELECT i.id\n" +
                "FROM \"imagefiltred\" i\n" +
                "WHERE NOT EXISTS (\n" +
                "	SELECT NULL\n" +
                "	FROM \"imagetagfiltred\"\n" +
                "	WHERE i.id = imageId \n" +
                "	AND tag IN (SELECT DISTINCT tag\n" +
                "FROM \"imagetagfiltred\"\n" +
                "WHERE imageid IN (\n" + query +
                "	) EXCEPT(SELECT tag\n" +
                "           FROM \"imagetagfiltred\"\n" +
                "           GROUP BY tag\n" +
                "	HAVING count(imageid) > 10000\n" +
                "	)\n" +
                ")\n" +
                ") LIMIT " + number + ";";
        
        System.out.println(query);
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            listImagesId.add(rs.getString(1));
        }
        //close stuff
        rs.close();
        st.close();

        return listImagesId;
    }
    
    /**
     * Function to write the CSV file with 2 list of image id, ok and not ok
     * ok have 1, not ok have 0 in the second column
     */
    public static void printCSV(String filename, List<String> listImageIdOK, List<String> listImageIdNotOK) throws FileNotFoundException{
        PrintStream file = new PrintStream(new FileOutputStream(filename + ".csv", false));


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
