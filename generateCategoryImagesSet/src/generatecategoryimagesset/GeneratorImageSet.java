/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generatecategoryimagesset;

import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Gaetan
 */
public interface GeneratorImageSet {
    public List<String> getImagesOK(List<String> listTag, int number) throws Exception;
    public List<String> getImagesNotOK(List<String> listTag, int number) throws Exception;
}
