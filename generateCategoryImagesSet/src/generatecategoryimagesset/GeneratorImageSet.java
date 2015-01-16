/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generatecategoryimagesset;

import java.util.List;

/**
 *
 * @author Gaetan
 */
public interface GeneratorImageSet {
    public List<String> getImagesOK(List<String> listTag, int number, List<String> listIdImageExclude) throws Exception;
    public List<String> getImagesNotOK(List<String> listTag, int number, List<String> listIdImageExclude) throws Exception;
}
