package distancestags;

/* Database connection */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.DriverManager;

/* Collections */
import java.util.*;

/**
 * Class "Distances" that uses the flickr database to deliver a distance matrix for existing tags based on their
 * cooccurrence in the images. 
 * @author Patrick Sebastian John von Freyend & El�d Egyed-Zsigmond
 * @version 1.0
 */
public class Distances {
    /**
       Used to set minimum reference tag count and percentage
    */
    public static int MIN_REFERENCE_TAG_COUNT = 25;
    public static int REFERENCE_TAG_PERCENTAGE = 2;
    public static int PRINT = 0; // Set to 0 to not print any additional information, 1 to print the progress of the programm
    public static int TEST_POOL_SIZE_DIVISION = 1; // Set to 0 to not print any additional information, 1 to print the progress of the programm

    /**
     * Establishes a database connection (to a postgres database) and saves all image ids and their corresponding tags to a map (string => string list).
     * Please add an appropriate postgresql-driver that corresponds to your version of the JVM to the build path.
     *
     * @param url      URL to the database
     * @param user     Username for the database
     * @param password Password for the database
     * @return void
     */
    public static Map mapFromDatabase(String url, String user, String password) {

        Connection c = null;
        Statement stmt = null;
        Map imagesTags = new HashMap();

        try {
            /* Connect to database */
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, password);
            c.setAutoCommit(false);

            /* Successful! */
            if(PRINT == 1)
            System.out.println("Database connection successful. Creating map...\n");

            /* Read images (their ids) and their tags from the database and saves them to a map */
            stmt = c.createStatement();
            String theQuery = "SELECT \n" +
                    "  \"imagefiltred\".id, \n" +
                    "  \"imagetagfiltred\".tag \n" +
                    "FROM \n" +
                    "\"Tag\", \n" +
                    "\"imagefiltred\", \n" +
                    "\"imagetagfiltred\"\n" +
                    "WHERE \n" +
                    "  \"imagefiltred\".id = \"imagetagfiltred\".imageid AND\n" +
                    "  \"Tag\".text = \"imagetagfiltred\".tag AND\n" +
                    "  \"Tag\".gettytag = true AND\n" +
//                    "  \"Tag\".language = 'fr' AND\n" +
                    "  \"imagefiltred\".lat >= 50 AND \n" +
                    "  \"imagefiltred\".lat <= 55 AND \n" +
                    "  \"imagefiltred\".lon >= 5 AND \n" +
                    "  \"imagefiltred\".lon <= 15";
            ResultSet rs = stmt.executeQuery( theQuery );

            /* Write into the Map */
            while (rs.next()) {
                String id = rs.getString("id");
                String tag = rs.getString("tag");

                /* If the Map doesn't already contain the key */
                if (!imagesTags.containsKey(id)) {
                    /* add the key and create internal list */
                    imagesTags.put(id, new ArrayList());

                    /* add the first tag to the internal list */
                    List internal = (ArrayList) imagesTags.get(id);
                    internal.add(tag);
                } else {
                    /* Key already exists for the image; Search for tag in the internal list and add it */
                    List internal = (ArrayList) imagesTags.get(id);
                    if (!(internal.contains(tag))) {
                        internal.add(tag);
                    }
                }
            }

            if(PRINT == 1)
            System.out.println("The map was successfully created.\n");

            /* close reading */
            rs.close();
            stmt.close();

            /* close connection */
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + " - " + e.getMessage());
            System.exit(0);
        }

        return imagesTags;

    }

    /**
     * Prints the contents of a map to the console.
     *
     * @param map URL to the database
     * @return void
     */
    public static void printMap(Map map) {
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

    /**
     * Creates a tag pool (map with String (tag) => int) with all the different tags that exist in a map
     *
     * @param map Map with all the images and their corresponding tags
     * @return void
     */
    public static ArrayList<String> createTagPool(Map map) {

        if(PRINT == 1)
        System.out.println("Creating tagpool...\n");

        ArrayList<String> tagpool = new ArrayList<String>();

        /* Iterate through the map */
        Iterator itMap = map.entrySet().iterator();

        while (itMap.hasNext()) {
            Map.Entry pairFromMap = (Map.Entry) itMap.next();

             /* Select the list of tags in the pair (ArrayList) */
            ArrayList tagList = (ArrayList) pairFromMap.getValue();

            /* Iterate through that list and add to the tagpool with value 0 (counts) */
            for (String s : (ArrayList<String>) tagList) {
                /* If it doesn't already include the tag... */
                if (!(tagpool.contains(s))) {
                    tagpool.add(s);
                }
            }
        }

        if(PRINT == 1)
        System.out.println("Created tagpool.\n");

        return tagpool;
    }

    /**
     * Creates a tagpool and counts all the occurrences of each tag in imagesTags (map (string => string list))
     *
     * @param imagesTags Map with all the images and their corresponding tags
     * @return void
     */
    public static Map countOccurrences(Map imagesTags) {

        if(PRINT == 1)
            System.out.println("Creating tagpool and counting occurrences...\n");

        Map tagpool = new HashMap();

        /* Iterate through the map */
        Iterator itMap = imagesTags.entrySet().iterator();

        while (itMap.hasNext()) {
            Map.Entry pairFromMap = (Map.Entry) itMap.next();

             /* Select the list of tags in the pair (ArrayList) */
            ArrayList tagList = (ArrayList) pairFromMap.getValue();

            /* Iterate through that list and add to the tagpool with value 0 (counts) */
            for (String s : (ArrayList<String>) tagList) {
                /* If it doesn't already include the tag... */
                if (!(tagpool.containsKey(s))) {

                    tagpool.put(s, 0);

                    /* For every tag count through imagesTags, find each list and look for the search-tag, is it there: +1 in alltags */
                    Iterator countTags = imagesTags.entrySet().iterator();

                    while (countTags.hasNext()) {
                        Map.Entry countPair = (Map.Entry) countTags.next();

                        /* Find list in element */
                        ArrayList<String> arrayTags = (ArrayList<String>)countPair.getValue();

                        /* Is search in there? */
                        if(arrayTags.contains(s)) {
                            /* increment value by one in ALLTAGS */
                            tagpool.put(s, (Integer)tagpool.get(s) + 1);
                        }
                    }
                }
            }
        }

        if(PRINT == 1) {
            // System.out.println(alltags);
            System.out.println("Created tagpool, counting successful.\n");
        }

        return tagpool;
    }

    /**
     * Sorts a map with key => integer in descending order (switch o2 and o1 for ascending, if needed)
     * @param sort Map that is to be sorted
     * @return
     */
    private static Map<String, Integer> sortMapDescending(Map<String, Integer> sort) {

        /* Make a list out of map */
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(sort.entrySet());

        /* Sort it with a comparator */
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        /* Convert it to a map again */
        Map<String, Integer> sorted = new LinkedHashMap<String, Integer>();

        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            sorted.put(entry.getKey(), entry.getValue());
        }

        return sorted;
    }

    /**
     * This functions returns, given a minimum reference count of at least MIN_REFERENCE_TAG_COUNT but of a percentage of REFERENCE_TAG_PERCENTAGE (can be set above)
     * if higher, the set of representative tags as an array list from a map with the imageid => tags (string => string list)
     * @param imagesTags
     * @param percentage
     * @return
     */
    public static ArrayList getRepresentativeTags(Map imagesTags, int percentage) {

        ArrayList representativeTags = new ArrayList<String>();

        /*
            1. Create Map "alltags" tag => count (0)
            2. Count all occurrences and add to tag pool
            3. Choose "int count" REFERENCE_TAG_PERCENTAGE, minimum MIN_REFERENCE_TAG_COUNT
        */
        Map alltags = countOccurrences(imagesTags);

        if(PRINT == 1)
        System.out.println("Getting most representative tags...");

        /* Sort the tags in descending order */
        alltags = sortMapDescending(alltags);

        /* Check the percentage, if correct, proceed */
        if (percentage > 100 || percentage < 0) System.out.println("The given percentage has to be between 0 and 100.");
        else {
            /* Calculate number of tags, at least MIN_REFERENCE_TAG_COUNT, otherwise */
            double percentageCount = alltags.size() * percentage * 0.01;

            if (percentageCount > MIN_REFERENCE_TAG_COUNT) { double referenceCount = percentageCount; }
            else { double referenceCount = percentageCount; }

            /* Go through the map and find the number(percentageCount) highest-rated tags, add them to the list */
            Iterator iteratorMin = alltags.entrySet().iterator();
            while (iteratorMin.hasNext() && representativeTags.size() < percentageCount) {
                Map.Entry pair = (Map.Entry) iteratorMin.next();
                representativeTags.add(pair.getKey());
            }
        }

        return representativeTags;
    }

    /**
     * Counts the coocurrence of term1 and term2 in imagesTags
     * @param term
     * @param term2
     * @param imagesTags Map with the images and their corresponding tags (string => string list)
     * @return
     */
    public static int cooccurrenceBetweenTerms(String term, String term2, Map imagesTags) {
        /* Count of images that both term1 and term 2 are part of in maps */
        int cooccurrence = 0;

        Iterator iterator = imagesTags.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();

            /* For every picture, take the list */
            ArrayList tagList = (ArrayList) pair.getValue();

            /* Increment value by one, when both are tags for the same picture */
            if(tagList.contains(term) && tagList.contains(term2)) cooccurrence++;
        }

        // if(PRINT == 1)
        // System.out.println(cooccurrence + " is the cooccurrence for terms \"" + term +"\" and \"" + term2 + "\".");

        return cooccurrence;
    }

    /**
     * Creates a histogramm in counting, how often a term is used in the same photo in imagesTags with each of the frequent terms
     * (For a histogramm, take both the array frequent terms and the array with the count)
     * @param term Term for which the histogramm is supposed to be calculated
     * @param representativeTags Most frequent terms as calculcated by (String values) @getRepresentativeTags
     * @param imagesTags Map with the images and their corresponding tags (string => string list)
     * @return
     */
    public static ArrayList<Integer> calculateCooccurrences(String term, ArrayList<String> representativeTags, Map imagesTags) {
        ArrayList<Integer> cooccurrences = new ArrayList();

        /* Go through frequentTerms and get every single term */
        for (String term2 : representativeTags) {
            cooccurrences.add(cooccurrenceBetweenTerms(term, term2, imagesTags));
        }

        /* Print the corresponding counts and terms */
        if(PRINT == 1) {

            System.out.println("The term \"" + term + "\" is used simultaneously as follows:");
            for (int i = 0; i < representativeTags.size(); i++) {
                System.out.println(cooccurrences.get(i) + " times with the term \"" + representativeTags.get(i) + "\"");
            }

        }
        return cooccurrences;
    }

    /**
     * Calculates the divergence of two histogramms created by calculateCooccurences according to the Jenson-Shanon Divergence.
     * @param histogramm1
     * @param histogramm2
     * @param representativeTags
     * @return
     */
    public static double divergenceOfHistogrammes(ArrayList<Integer> histogramm1, ArrayList<Integer> histogramm2, ArrayList<String> representativeTags) {
        double divergence = 0;

        if(histogramm1.size() != histogramm2.size()) {

          System.out.println("The lists do not have the same amount of entries. Are you sure those are histogramms for the same frequent terms?");

        } else {

            double[] hist1 = new double[histogramm1.size()];
            double[] hist2 = new double[histogramm2.size()];

            for (int i = 0; i < histogramm1.size(); i++) {
                hist1[i] = histogramm1.get(i).doubleValue();
                hist2[i] = histogramm2.get(i).doubleValue();
            }

            divergence = JensonShanonDivergence.jensenShannonDivergence(hist1, hist2);
        }

        return divergence;
    }

    /**
     * Calculates a distance matrix for each tag in imagesTags and its representative tags (with the help of the Janson-Shanon-Divergence).
     * It then writes this matrix in a *.csv-file located in the main folder.
     * @param imagesTags
     * @param representativeTags
     * @return
     */
    public static double[][] calculcateDistanceMatrix(Map imagesTags, ArrayList<String> representativeTags) {

        /* Initialise matrix with amountTags * amountTags; right now only a tenth! */
        int amountTags = imagesTags.size();
        double[][] distanceMatrix = new double[amountTags/TEST_POOL_SIZE_DIVISION][amountTags/TEST_POOL_SIZE_DIVISION];

        /* Load all tags in map */
        ArrayList<String> tagpool = createTagPool(imagesTags);

        if(PRINT == 1)
        System.out.println(tagpool);

        int times = 0;
        

            /* Go through tagpool and calculate Jenson-Shanon-Divergence for each pair */
            for(int i = 0; i < amountTags/TEST_POOL_SIZE_DIVISION; i++) {
                System.out.print(i + ", ");
                for (int i2 = i+1; i2 < amountTags/TEST_POOL_SIZE_DIVISION; i2++) {
                    try {
                        ArrayList<Integer> cooccurrence1 = calculateCooccurrences(tagpool.get(i), representativeTags, imagesTags);
                        ArrayList<Integer> cooccurrence2 = calculateCooccurrences(tagpool.get(i2), representativeTags, imagesTags);
                        distanceMatrix[i][i2] = divergenceOfHistogrammes(cooccurrence1,cooccurrence2 , representativeTags);
                    }
                    catch (Exception e){
                        System.out.println(e);
                        System.out.println("i:"+i+"; i2:"+i2+"; tagpoolsize/TEST_POOL_SIZE_DIVISION :"+tagpool.size()/TEST_POOL_SIZE_DIVISION);
                        System.exit(-1);
                    }

                    
                    times++;
                    
                }
            }

     
        
        System.out.println(times);
        writeDistanceMatrixIntoFile(distanceMatrix,representativeTags );
        return distanceMatrix;

    }
    
    public static int writeDistanceMatrixIntoFile(double[][] theMatrix, ArrayList<String> representativeTags){
        try {

            /* Find source */
            String directory = (Distances.class.getProtectionDomain().getCodeSource().getLocation() + "").replace("file:", "");
            System.out.println(directory);

            /* Create file and empty if needed */
            PrintWriter writer = new PrintWriter(directory + "distancesFlickr.cvs");
            writer.print("");

//            /* Add representative Tags */
//            writer.append("[");
//            for(String s : representativeTags) {
//                writer.append(s + ", ");
//            }
//            writer.append("]\n\n");
//            writer.flush();

//            for(int i = 0; i < tagpool.size(); i++) {
//                String s = tagpool.get(i);
//
//                if(!(i == (tagpool.size()-1))) {
//                    writer.append(s + ", ");
//                } else {
//                    writer.append(s + "\n");
//                }
//            }
        int size = theMatrix.length;
        for (int j = 0; j < size; j++) {
            writer.append(j + ", ");
            for (int i = 0; i < size; i++) {
                writer.append(theMatrix[i][j] + "; ");

            }
            writer.append("\n");
        }

            
            
   } catch(IOException e) {
            e.printStackTrace();
            return -1;
        } 
        
        return 1;
    }

//    public static void main(String[] args) {
//        /* Create Map */
//        Map imagesTags = mapFromDatabase("jdbc:postgresql://localhost:5432/2014GettyClean", "postgres", "postgres");
//
//        /* Calculate representativeTags */
//        ArrayList representativeTags = getRepresentativeTags(imagesTags, REFERENCE_TAG_PERCENTAGE);
//
//        /* System.out.println(representativeTags);
//
//        // Testing histogramm function
//        ArrayList<Integer> histogramm1 = calculateCooccurrences("allemagne", representativeTags, imagesTags);
//        ArrayList<Integer> histogramm2 = calculateCooccurrences("france", representativeTags, imagesTags);
//        System.out.println(divergenceOfHistogrammes(histogramm1, histogramm2, representativeTags));
//        */
//
//        /* Distance Matrix */
//        calculcateDistanceMatrix(imagesTags, representativeTags);
//    }
}