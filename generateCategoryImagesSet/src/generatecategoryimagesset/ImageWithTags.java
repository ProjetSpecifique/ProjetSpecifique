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
public class ImageWithTags {
    private String id;
    private boolean isOk;
    private ArrayList<String> tags;

    public ImageWithTags(String id, boolean isOk) {
        this.id = id;
        this.isOk = isOk;
        this.tags = new ArrayList<String>();
    }
    
    public void loadTagsFromBD(Connection c) throws SQLException{
        
        String query =  "SELECT tag\n" +
                        "FROM \"imagetagfiltred\"\n" +
                        "WHERE imageid = '" + this.id + "';";
       
        //System.out.println(query);
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query);
        
        this.tags.clear();
        while (rs.next()) {
            this.tags.add(rs.getString(1));
        }
        //close stuff
        rs.close();
        st.close();
    }
    
    public String toCSV(char separatorCSV){
        StringBuilder csvText = new StringBuilder();
        csvText.append(this.id);
        csvText.append(separatorCSV);
        csvText.append((this.isOk?'1':'0'));
        
        Iterator<String> iter = this.tags.iterator();
        while(iter.hasNext()){
            csvText.append(separatorCSV);
            csvText.append(iter.next());
        }
        csvText.append(separatorCSV);
        return csvText.toString();
    }
    
    
    public static List<ImageWithTags> getListImageFromListString(Connection c, List<String> listString, boolean isOk) throws SQLException{
        List<ImageWithTags> listImage = new ArrayList<>();
        Iterator<String> iter = listString.iterator();
        while(iter.hasNext()){
            ImageWithTags image = new ImageWithTags(iter.next(), isOk);
            image.loadTagsFromBD(c);
            listImage.add(image);
        }
        return listImage;
    }
}
