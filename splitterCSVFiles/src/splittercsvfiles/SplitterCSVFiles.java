/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package splittercsvfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Take all CSV files from a folder and split them in 2 other csv files with a part of image OK and a part of not OK
 * The number of file splitted is set by the number of names specified in 'nameFilesResultsPart'
 * @author Gaetan
 */
public class SplitterCSVFiles {

    private static String sourceFolderPath = "input";
    private static String outputPath = "output/";
    private static String[] nameFilesResultsPart = new String[] {"ToTrain", "ToTest"};
    private static String separatorCSV = ";";
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
       //List csv files from the source folder
       List<File> listCsv = listCSVFilesInFolder(sourceFolderPath);
       
       //For each csv file, split it
       Iterator<File> iter = listCsv.iterator();
       while(iter.hasNext()){
           splitCSVFile(iter.next());
       }
       
    }
    
    
    /**
     * List all CSV file from a folder (not recursive)
     * @param folderPath
     * @return 
     */
    public static List<File> listCSVFilesInFolder(String folderPath){
        final File folder = new File(sourceFolderPath);
        if (folder.isDirectory()) {
             List<File> res = new ArrayList<>();
             for (final File fileEntry : folder.listFiles()) {
                 if(fileEntry.isFile() && fileEntry.getName().lastIndexOf(".csv")==fileEntry.getName().length()-4){
                     res.add(fileEntry);
                 }
             }
             return res;
        }
        else{
            //The folderpath is a CSV file
            List<File> res = new ArrayList<>();
            res.add(folder);
            return res;
        }
    }
    
    /**
     * Split a CSV file
     * @param file 
     */
    public static void splitCSVFile(File csvFile) throws IOException{
        
        List<String> lignsImageOk = new ArrayList<>();
        List<String> lignsImageNotOk = new ArrayList<>();
        
        //Read CSV file and separate OK and not OK images
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String line = "";
        while ((line = br.readLine()) != null) {
            String isOk = line.split(separatorCSV)[1];
            if(isOk.equals("1")){
                lignsImageOk.add(line);
            }
            else if(isOk.equals("0")){
                lignsImageNotOk.add(line);
            }
        }
        
        //Split list of imageOK and list of imageNotOK
        int nbFilesResult = nameFilesResultsPart.length;
        List<List<String>> listOfListImageOk = new ArrayList<>();
        List<List<String>> listOfListImageNotOk = new ArrayList<>();
        int nbImageOkPerList = lignsImageOk.size() / nbFilesResult;
        int nbImageNotOkPerList = lignsImageNotOk.size() / nbFilesResult;
        
        System.out.println(csvFile.getName() + "\t " + nbImageOkPerList + " images OK\t " + nbImageNotOkPerList + " images Non Ok par fichiers");
        
        for(int i=0; i<nbFilesResult-1; i++){
            listOfListImageOk.add(lignsImageOk.subList(i*nbImageOkPerList, (i+1)*nbImageOkPerList));
            listOfListImageNotOk.add(lignsImageNotOk.subList(i*nbImageNotOkPerList, (i+1)*nbImageNotOkPerList));
        }
        
        //for the last file, add the rest of images
        listOfListImageOk.add(lignsImageOk.subList((nbFilesResult-1)*nbImageOkPerList, lignsImageOk.size()));
        listOfListImageNotOk.add(lignsImageNotOk.subList((nbFilesResult-1)*nbImageNotOkPerList, lignsImageNotOk.size()));
        
        
        //Write all results files
        String baseNameFile = csvFile.getName().substring(0, csvFile.getName().length()-4);
        for(int i=0; i<nbFilesResult; i++){
            String filePathName = outputPath + baseNameFile + nameFilesResultsPart[i] + ".csv";
            PrintStream file = new PrintStream(new FileOutputStream(filePathName, false));

            //Write lign of imageOK
            Iterator<String> iter = listOfListImageOk.get(i).iterator(); 
            while (iter.hasNext()) {
                file.println(iter.next());
            }
            //Write lign of imageNotOK
            iter = listOfListImageNotOk.get(i).iterator(); 
            while (iter.hasNext()) {
                file.println(iter.next());
            }
            file.close();
        }
    }
}
