/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generatecategoryimagesset;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gaetan
 */
public class GeneratorDistancesTags implements GeneratorImageSet{

    /*
     Parameters
     */
    private static int nbTagsSimilar = 40;
    private static int nbTagsOpposite = 20;
    private static int distanceMaxSimilar = 1000;
    private static int distanceMinOpposite = 10000;
    
    private Connection c;
    
    GeneratorDistancesTags(Connection c) {
        this.c = c;
    }

    @Override
    public List<String> getImagesOK(List<String> listTag, int number, List<String> listIdImageExclude) throws Exception {
        List<String> listTagsSimilar = getSimilarTags(listTag.get(0), nbTagsSimilar);
        listTagsSimilar.add(listTag.get(0));
        
        List<String> listImagesId = getImagesWithListTags(listTagsSimilar, number, listIdImageExclude);
        
        System.out.println(listImagesId.size() + " images found OK for '" + listTag.get(0) + "'");
        
        return listImagesId;
    }

    @Override
    public List<String> getImagesNotOK(List<String> listTag, int number, List<String> listIdImageExclude) throws Exception {
        List<String> listTagsSimilar = getOppositeTags(listTag.get(0), nbTagsSimilar);
        
        List<String> listImagesId = getImagesWithListTags(listTagsSimilar, number, listIdImageExclude);
        
        System.out.println(listImagesId.size() + " images found Not OK for '" + listTag.get(0) + "'");
        
        return listImagesId;
    }
    
    
    /**
     * Function to find tags similar to an other
     */
    private List<String> getSimilarTags(String tag, int number) throws SQLException{
        if(tag.trim().isEmpty() || number <= 0) return null;
        
        List<String> listTags = new ArrayList<>();

        //To have similar tags
        String query =  "SELECT CASE WHEN(tag1 ~ '(^|^.* )" + tag + "($| .*$)')" +
                        " THEN tag2 ELSE tag1 END tag, MIN(distance) minDistance" + 
                        " FROM distancestags2" +
                        " WHERE (tag1 ~ '(^|^.* )" + tag + "($| .*$)'" +
                        " OR tag2 ~ '(^|^.* )" + tag + "($| .*$)')" +
                        " AND distance < " +  distanceMaxSimilar +
                        " GROUP BY tag" + 
                        " ORDER BY minDistance ASC" +
                        " LIMIT " + number + ";";
        //System.out.println(query);
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            listTags.add(rs.getString(1));
        }
        //close stuff
        rs.close();
        st.close();

        if(listTags.isEmpty()) System.out.println("No tag similar found for " + tag);
        else System.out.println(listTags.size() + " tags similar found for " + tag);
        return listTags;
    }
    
    /**
     * Function to find tags opposite to an other
     */
    private List<String> getOppositeTags(String tag, int number) throws SQLException{
        if(tag.trim().isEmpty() || number <= 0) return null;
        
        List<String> listTags = new ArrayList<>();

        //To have opposite tags
        String query =  "SELECT CASE WHEN(tag1 ~ '(^|^.* )" + tag + "($| .*$)')" +
                        " THEN tag2 ELSE tag1 END tag, MAX(distance) maxDistance" + 
                        " FROM distancestags2" +
                        " WHERE (tag1 ~ '(^|^.* )" + tag + "($| .*$)'" +
                        " OR tag2 ~ '(^|^.* )" + tag + "($| .*$)')" +
                        " AND distance > " +  distanceMinOpposite +
                        " GROUP BY tag" + 
                        " ORDER BY maxDistance DESC" +
                        " LIMIT " + number + ";";
       // System.out.println(query);
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            listTags.add(rs.getString(1));
        }
        //close stuff
        rs.close();
        st.close();

        if(listTags.isEmpty()) System.out.println("No tag opposite found for " + tag);
        else System.out.println(listTags.size() + " tags opposite found for " + tag);
        
        return listTags;
    }
    
    
    
    /**
     * Function to find images with a tag from the parameters list
     */
    private List<String> getImagesWithListTags(List<String> listTag, int number, List<String> listIdImageExclude) throws Exception{
        if(listTag == null || listTag.size() <= 0 || number <= 0){
            System.out.println("List tag empty");
            return new ArrayList<>();
        }
        
        List<String> listImagesId = new ArrayList<>();
        
        StringBuilder query =  new StringBuilder();
        query.append("SELECT imageid\n");
        query.append("FROM \"imagetagfiltred\"\n");
        query.append("WHERE tag ~ '(^|^.* )");
        query.append(listTag.get(0));
        query.append("($| .*$)'\n");
        for(int i=1; i<listTag.size(); i++){
            query.append("UNION\n");
            query.append("	SELECT imageid\n");
            query.append("	FROM \"imagetagfiltred\"\n");
            query.append("	WHERE tag ~ '(^|^.* )");
            query.append(listTag.get(i));
            query.append("($| .*$)'\n");
        }
        
        //Add exclusion
        if(listIdImageExclude != null && listIdImageExclude.size() > 0){
         query.insert(0, "SELECT * FROM (");
         query.append(") AS U WHERE imageid NOT IN (");
         Iterator<String> iter = listIdImageExclude.iterator();
         while(iter.hasNext()){
             query.append('\'');
             query.append(iter.next());
             query.append('\'');
             if(iter.hasNext()){
                 query.append(',');
             }
         }
         query.append(")\n");
        }        
        
        //Add Limit
        query.append("LIMIT ");
        query.append(number);
        query.append(";");
        
        //System.out.println(query.toString());
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query.toString());
        while (rs.next()) {
            listImagesId.add(rs.getString(1));
        }
        //close stuff
        rs.close();
        st.close();

        return listImagesId;
    }
}
