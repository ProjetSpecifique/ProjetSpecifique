/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distancestags;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static distancestags.Distances.divergenceOfHistogrammes;
import java.sql.PreparedStatement;

/**
 *
 * @author Gaetan
 */
public class DistancesTags {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Connection to the DB
            Connection c = null;
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BDClean", "postgres", "admin");
            c.setAutoCommit(false);
            
            
            //Get the list of cooccurrences
            Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Getting list cooccurrences...");
            String theQuery = "SELECT \"tag1\", \"tag2\", \"cooccurrence\"\n" +
                            "  FROM \"cooccurrence\"\n" +
                            "  ORDER BY \"tag1\", \"tag2\";";
            ResultSet rs = stmt.executeQuery( theQuery );
            
            //Get the list of tag
            List<String> listTag = getListTag(rs);
            
            //Generate histogram for each tag
            List<List<Integer>> listHisto = createCooccurrenceHisto(rs, listTag);
            
            //Write CSV histogram file
            writeHistoCSV(listHisto, listTag, "tagHistograms.csv");
            
            //Compute distance between tags
            List<List<Double>> matrixDistance = createCooccurrenceHisto(listHisto);
            
            //Write CSV distance file
            writeDistanceMatrixCSV(matrixDistance, listTag, "tagDistances.csv");
            
            //Save in BD
            saveDistanceMatrixInBD(c, listTag, matrixDistance);
                    
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception ex) {
            System.err.println("exception: " + ex);
            ex.printStackTrace();
        }
    }
  
    public static List<String> getListTag(ResultSet rs) throws SQLException{
        String current = "";
        List<String> listTag = new ArrayList<String>();
        rs.beforeFirst();
        while (rs.next()) {
            if(!current.equals(rs.getString("tag1"))){
                current = rs.getString("tag1");
                listTag.add(current);
            }
        }
        
        return listTag;
    }
    
   
    /*
     * Create an histogramme for each tags
     */
    public static List<List<Integer>> createCooccurrenceHisto(ResultSet rs, List<String> listTag) throws SQLException{
        System.out.println("Generating histograms...");
        int nbTag = listTag.size();
        int currentTag1 = 0;
        int currentTag2 = 0;
        
        List<List<Integer>> listHistogrammes = new ArrayList<List<Integer>>();
        List<Integer> currentHisto = new ArrayList<Integer>();
        listHistogrammes.add(currentHisto);
        
        rs.beforeFirst();
        while (rs.next()) {
            while(!listTag.get(currentTag1).equals(rs.getString("tag1"))){
                //Complete lign with 0
                for(;currentTag2<nbTag; currentTag2++){
                    currentHisto.add(0);
                }
                currentHisto = new ArrayList<Integer>();
                listHistogrammes.add(currentHisto);
                
                currentTag1++;
                currentTag2 = 0;
                System.out.println((currentTag1+1) + "/" + nbTag);                
            }
            while(!listTag.get(currentTag2).equals(rs.getString("tag2"))){
                //Complete this pair with 0
                currentHisto.add(0);
                currentTag2++;
            }
            currentHisto.add(rs.getInt("cooccurrence"));
            currentTag2++;
        }
        System.out.println("Histograms generated.");
        return listHistogrammes;
    }

    public static List<List<Double>> createCooccurrenceHisto(List<List<Integer>> listHisto) {
        System.out.println("Compute distances...");
        List<List<Double>> matrixDistance = new ArrayList<List<Double>>();
        int nbTag = listHisto.size();
        for(int j=0; j<nbTag; j++){
            matrixDistance.add(new ArrayList<Double>());
            for(int i=0; i<nbTag; i++){
                if(i<j){
                    matrixDistance.get(j).add(matrixDistance.get(i).get(j));
                }
                else{
                    //matrixDistance.get(j).add(0.4); //To test without calulate distances
                    matrixDistance.get(j).add(divergenceOfHistogrammes((ArrayList<Integer>)listHisto.get(j),(ArrayList<Integer>)listHisto.get(i), null));
                }
            }
            System.out.println((j+1) + "/" + nbTag);
        }
        System.out.println("Distances computed.");
        return matrixDistance;
    }
    
    
    public static void writeHistoCSV(List<List<Integer>> listHisto, List<String> listTag, String fileName) throws IOException{
        System.out.println("Writting CSV...");
        int nbTag = listTag.size();
        
        FileWriter writer = null;
        writer = new FileWriter(fileName);
        //write tag column name
        for(int i=0; i<nbTag; i++){
            writer.write(";" + listTag.get(i));
        }
        
        for(int j=0; j<nbTag; j++){
            writer.write("\n" + listTag.get(j));
            for(int i=0; i<nbTag; i++){
                writer.write(";" + listHisto.get(j).get(i));
            }
            System.out.println((j+1) + "/" + nbTag);
        }
        
        writer.write("\n");
        writer.close();
        System.out.println("CSV written.");
    }
    
    public static void writeDistanceMatrixCSV(List<List<Double>> listHisto, List<String> listTag, String fileName) throws IOException{
        System.out.println("Writting CSV...");
        int nbTag = listTag.size();
        
        FileWriter writer = null;
        writer = new FileWriter(fileName);
        //write tag column name
        for(int i=0; i<nbTag; i++){
            writer.write(";" + listTag.get(i));
        }
        
        for(int j=0; j<nbTag; j++){
            writer.write("\n" + listTag.get(j));
            for(int i=0; i<nbTag; i++){
                writer.write(";" + listHisto.get(j).get(i));
            }
            System.out.println((j+1) + "/" + nbTag);
        }
        
        writer.write("\n");
        writer.close();
        System.out.println("CSV written.");
    }

    public static void saveDistanceMatrixInBD(Connection c, List<String> listTag, List<List<Double>> matrixDistance) throws SQLException{
        System.out.println("Saving in BD...");
        int nbTag = listTag.size();
        /*for(int j=0; j<nbTag; j++){
            for(int i=0; i<nbTag; i++){
                Statement st = null;
                st = c.createStatement();
                String query = "UPDATE \"cooccurrence\" " +
                                "SET \"distance\" = '" + matrixDistance.get(j).get(i) +
                                "' WHERE tag1 = '" + listTag.get(j) + "'" +
                                "AND tag2 = '" + listTag.get(i) + "'";
                st.executeUpdate(query);
                if (st != null) {
                    st.close();
                }
            }
            System.out.println((j+1) + "/" + nbTag);
        }*/
        
        /*String query = "UPDATE \"cooccurrence\" " +
                        "SET \"distance\" = ? WHERE tag1 = ? AND tag2 = ?";

        PreparedStatement pstmt = c.prepareStatement(query);
        for(int j=0; j<nbTag; j++){
            for(int i=0; i<nbTag; i++){
                pstmt.setDouble(1, matrixDistance.get(j).get(i));
                pstmt.setString(2, listTag.get(j));
                pstmt.setString(2, listTag.get(i));
                pstmt.addBatch();
            }
            System.out.println((j+1) + "/" + nbTag);
        }
        System.out.println("Executing request...");
        pstmt.executeBatch();
        */
        System.out.println("Saved in BD.");
        
        
    }
}

/*
 ALTER TABLE cooccurrence
   ADD COLUMN distance numeric;

 */