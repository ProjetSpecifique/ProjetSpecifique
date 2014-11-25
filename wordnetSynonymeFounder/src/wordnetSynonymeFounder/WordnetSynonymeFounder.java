/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordnetSynonymeFounder;

import edu.smu.tspell.wordnet.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gaetan
 */
public class WordnetSynonymeFounder {
    
    private enum Output {
        DISPLAY,CSV;	
    }

    private static String wordSearched = "boat";
    private static Boolean userSelectsSence = false;
    private static String wordnetInstallationDir = "D:\\Program Files (x86)\\WordNet\\dict\\";
    private static Output outputMethod = Output.DISPLAY;
    private static char separatorCSV = ';';
    
    
    public static void main(String[] args) {
        
        //Set the folder where is installed wordnet dictionnary
        System.setProperty("wordnet.database.dir", wordnetInstallationDir);
        
        NounSynset nounSynset;

        WordNetDatabase database = WordNetDatabase.getFileInstance(); 
        Synset[] synsets = database.getSynsets(wordSearched, SynsetType.NOUN);
        
        if(synsets.length <= 0){ //Word not found
            System.out.println("The word '" + wordSearched + "' doesn't have sence");
            return;
        }
        int senceSelected = 0;
        //If option 'selectSence' set, ask the user the sence to select else select the first
        if(userSelectsSence && synsets.length > 1){
            senceSelected = askUserSelectsSence(synsets);
        }
        
        nounSynset = (NounSynset) synsets[senceSelected];
        
        //List hyponymes words
        NounSynset[] listHypoSynset = nounSynset.getHyponyms();
        List<String> listHypo = new ArrayList<String>();
        for (int i = 0; i < listHypoSynset.length; i++) {
            listHypo.addAll(Arrays.asList(listHypoSynset[i].getWordForms()));
        }
        
        //Output result
        switch(outputMethod){
            case DISPLAY:
                displayListWord(listHypo);
            break;
            case CSV:
                printCSV(wordSearched + "_synonymes", listHypo);
            break;
            default:
                displayListWord(listHypo);
        }
        
    }
    
    /*
     * Function to ask the user which sence he wants
     * param le list of possible word with sences
     * return the index of sence selected
    */
    private static int askUserSelectsSence(Synset[] synsets){
        int senceSelect = 0;
        do{
            //Display all sences
            for (int i = 0; i < synsets.length; i++) { 
                System.out.println( (i+1) + ") " + ((NounSynset)(synsets[i])).getDefinition());
            }

            //Ask user choise
            System.out.print("Select the sence of '" + wordSearched + "': ");
            try{
                Scanner in = new Scanner(System.in);
                senceSelect = in.nextInt();
            }catch(Exception e){ //input value not an integer
                senceSelect = -1;
            }
            if(senceSelect<1 || senceSelect>synsets.length){
                senceSelect = -1;
                System.out.println("Unvalid selection");
            }
            else{
                senceSelect--;
                System.out.println("Sence of '" + wordSearched + "' selected: " + synsets[senceSelect].getDefinition());
            }             
        }while(senceSelect == -1);
        
        return senceSelect;
    }
    
    /*
     * Function to display in the console a list of word
    */
    private static void displayListWord(List<String> listWord){
        Iterator<String> iter = listWord.iterator(); 
        while (iter.hasNext()) { 
            String element = iter.next();
            System.out.println(element);
        }
    }
    
    /*
     * Function to create a CSV file with a list of word
    */
    private static void printCSV(String filename, List<String> listWord){
        try {
            PrintStream file = new PrintStream(new FileOutputStream(filename + ".csv", true));
       
        
            Iterator<String> iter = listWord.iterator(); 
            while (iter.hasNext()) { 
                String element = iter.next();
                
                file.println(element + separatorCSV);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WordnetSynonymeFounder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
