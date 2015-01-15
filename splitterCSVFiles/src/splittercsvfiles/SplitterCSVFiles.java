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
 * The number of file splitted is set by the number of OutputCSVFile object specified in 'filesResultsPart' with 
 * for each, the number of imageOK and NotOK
 * @author Gaetan
 */
public class SplitterCSVFiles {

    private static String sourceFolderPath = "input";
    private static String outputPath = "output/";
    private static String separatorCSV = ";";
    private static OutputCSVFile[] filesResultsPart = new OutputCSVFile[] {
        new OutputCSVFile("ToTrain", 100, 100),
        new OutputCSVFile("ToTest", 80, 120)
    };
    
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
        int nbFilesResult = filesResultsPart.length;
        List<List<String>> listOfListImageOk = new ArrayList<>();
        List<List<String>> listOfListImageNotOk = new ArrayList<>();
        int indexImageOK = 0;
        int indexImageNotOK = 0;
        
        for(int i=0; i<filesResultsPart.length; i++){
            if(indexImageOK + filesResultsPart[i].nbImageOK <= lignsImageOk.size()){
                listOfListImageOk.add(lignsImageOk.subList(indexImageOK, indexImageOK + filesResultsPart[i].nbImageOK));
                indexImageOK += filesResultsPart[i].nbImageOK;
            }
            else{
                System.out.println("Not enough imageOK for " + csvFile.getName() +
                                    " for the part " + filesResultsPart[i].name +
                                    " " + (lignsImageOk.size()-indexImageOK) + "/" + filesResultsPart[i].nbImageOK);
                listOfListImageOk.add(lignsImageOk.subList(indexImageOK, lignsImageOk.size()));
                indexImageOK = lignsImageOk.size();
            }
            if(indexImageNotOK + filesResultsPart[i].nbImageNotOK <= lignsImageNotOk.size()){
                listOfListImageNotOk.add(lignsImageNotOk.subList(indexImageNotOK, indexImageNotOK + filesResultsPart[i].nbImageNotOK));
                indexImageNotOK += filesResultsPart[i].nbImageNotOK;
            }
            else{
                System.out.println("Not enough imageNotOK for " + csvFile.getName() +
                                    " for the part " + filesResultsPart[i].name +
                                    " " + (lignsImageNotOk.size()-indexImageNotOK) + "/" + filesResultsPart[i].nbImageNotOK);
                listOfListImageNotOk.add(lignsImageNotOk.subList(indexImageNotOK, lignsImageNotOk.size()));
                indexImageNotOK = lignsImageNotOk.size();
            }
        }
     
        //Write all results files
        if(listOfListImageOk.size() != 0 && listOfListImageNotOk.size() != 0){
            String baseNameFile = csvFile.getName().substring(0, csvFile.getName().length()-4);
            for(int i=0; i<nbFilesResult; i++){
                String filePathName = outputPath + baseNameFile + filesResultsPart[i].name + ".csv";
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
        else{
            System.out.println("Not enough images to split " + csvFile.getName());
        }
    }
}
