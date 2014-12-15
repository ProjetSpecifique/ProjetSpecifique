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
import java.util.List;

/**
 *
 * @author Gaetan
 */
public class GeneratorDistancesTags implements GeneratorImageSet{

    /*
     Parameters
     */
    private static int nbTagsSimilar = 10;
    private static int nbTagsOpposite = 10;
    private static int distanceMaxSimilar = 200;
    private static int distanceMinOpposite = 10000;
    
    private Connection c;
    
    GeneratorDistancesTags(Connection c) {
        this.c = c;
    }

    @Override
    public List<String> getImagesOK(List<String> listTag, int number) throws Exception {
        List<String> listTagsSimilar = getSimilarTags(listTag.get(0), nbTagsSimilar);
        listTagsSimilar.add(listTag.get(0));
        return getImagesWithListTags(listTagsSimilar, number);
    }

    @Override
    public List<String> getImagesNotOK(List<String> listTag, int number) throws Exception {
        List<String> listTagsSimilar = getOppositeTags(listTag.get(0), nbTagsSimilar);
        return getImagesWithListTags(listTagsSimilar, number);
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
        System.out.println(query);
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            listTags.add(rs.getString(1));
        }
        //close stuff
        rs.close();
        st.close();

        if(listTags.isEmpty()) System.out.println("No tag similar found for " + tag);
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
        System.out.println(query);
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            listTags.add(rs.getString(1));
        }
        //close stuff
        rs.close();
        st.close();

        if(listTags.isEmpty()) System.out.println("No tag opposite found for " + tag);
        return listTags;
    }
    
    
    
    /**
     * Function to find images with a tag from the parameters list
     */
    private List<String> getImagesWithListTags(List<String> listTag, int number) throws Exception{
        if(listTag == null || listTag.size() <= 0 || number <= 0) throw new Exception("List tag empty");
        
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
}
