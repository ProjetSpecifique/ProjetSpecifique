/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package splittercsvfiles;

/**
 * Object with the name of a output CSV file with the number of imageOK and NotOK
 * @author Gaetan
 */
public class OutputCSVFile {
    public String name;
    public int nbImageOK;
    public int nbImageNotOK;

    public OutputCSVFile(String name, int nbImageOK, int nbImageNotOK) {
        this.name = name;
        this.nbImageOK = nbImageOK;
        this.nbImageNotOK = nbImageNotOK;
    }
}
