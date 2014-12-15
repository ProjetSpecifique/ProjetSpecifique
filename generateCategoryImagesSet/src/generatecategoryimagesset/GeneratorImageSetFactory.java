/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generatecategoryimagesset;

import java.sql.Connection;

/**
 *
 * @author Gaetan
 */
public class GeneratorImageSetFactory {
    public static enum GeneratorType { SQLRequest, distancesTags};
    
    public static GeneratorImageSet createGenerator(Connection c, GeneratorType t){
        switch(t){
            case SQLRequest:
                return new GeneratorSQLRequest(c);
                case distancesTags:
                return new GeneratorDistancesTags(c);
        }
        return new GeneratorSQLRequest(c);
    }
}
