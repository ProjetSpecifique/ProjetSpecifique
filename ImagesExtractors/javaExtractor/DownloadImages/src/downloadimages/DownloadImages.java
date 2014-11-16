package downloadimages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
/**
 *
 * @author Atoka
 */
public class DownloadImages {

    private static String folder = "getty_images/";
    private static int number = 100;
    
    public static void main(String[] args) {
        String data = "";
        //connect
        Connection c = null;
        try {
            String url = "jdbc:postgresql://localhost:5432/imageSources";
            String user = "utilisateur";
            String password = "utilisateur";
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");

        Statement st = null;
        ResultSet rs = null;
        try {
            st = c.createStatement();
            String query = "SELECT id, \"downloadURL\" " +
                            "FROM \"Image\" " +
                            "WHERE NOT \"isDownloaded\" " +
                            "LIMIT " + number;
            System.out.println(query);
            rs = st.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getString(2));
                saveImage(rs.getString(2), rs.getString(1) + ".jpg");
                markDownloaded(c, rs.getString(1));
            }
            //close stuff
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (c != null) {
                    c.close();
                }

            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void saveImage(String imageUrl, String destinationFile) {
        try {
            File cfolder = new File(folder); 
            cfolder.mkdirs();
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(folder + destinationFile);

            byte[] b = new byte[4096];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public static void markDownloaded(Connection c, String idImage) throws Exception{
        Statement st = null;
        
        st = c.createStatement();
        String query = "UPDATE \"Image\" " +
                        "SET \"isDownloaded\" = true " +
                        "WHERE id = '" + idImage + "'";
        System.out.println(query);
        st.executeUpdate(query);
        if (st != null) {
            st.close();
        }
    }

}
