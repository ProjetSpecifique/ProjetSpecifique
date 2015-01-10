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
public class GeneratorSQLRequest implements GeneratorImageSet {

    private Connection c;
    
    GeneratorSQLRequest(Connection c) {
        this.c = c;
    }

    /**
     * Function to find images with a tag from the parameters list
     */
    public List<String> getImagesOK(List<String> listTag, int number) throws SQLException{
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

        System.out.println(listImagesId.size() + " images found OK for '" + listTag.get(0) + "'");
        return listImagesId;
    }
    
    /**
     * Function to have images without tags from the params list
     */
    public List<String> getImagesNotOK(List<String> listTag, int number) throws SQLException{
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

        System.out.println(listImagesId.size() + " images found Not OK for '" + listTag.get(0) + "'");
        return listImagesId;
    }
    
}
