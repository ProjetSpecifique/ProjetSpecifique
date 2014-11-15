package LearnImgCompMethForTags;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataBase.DataBaseMethods;
import DataBase.ImageDataBaseElement;
import ImageCompV.ImageDesc;


public class NewImageAddTags {
	
	private String newImagePath;
	private Map<String, Double> differencesMatrix;
	private String[] bestImageHitsIds;
	private Map<String,ImageDataBaseElement> dateBaseElements;
	private Map<String,Integer> statisticsForTags;
	
	public NewImageAddTags(String newImagePath) {
		super();
		this.newImagePath = newImagePath;
		differencesMatrix = new HashMap<String, Double>();
		bulidDifferenceMatrix();
	}
	
	private void bulidDifferenceMatrix(){
		dateBaseElements = new HashMap<String, ImageDataBaseElement>();
		dateBaseElements=DataBaseMethods.getElementsFromLocaleDataBase();
		System.out.println("Size: "+dateBaseElements.size());
		
		ImageDesc imgDescript = new ImageDesc();
		System.out.println("Building differenceMatrix");
		for (Map.Entry<String, ImageDataBaseElement> entry : dateBaseElements
				.entrySet()) {
			try {
				double diff=imgDescript.normalizationOfTwoSameTypeOfHistogram(0,newImagePath, entry.getValue().getPath());
				differencesMatrix.put(entry.getKey(), diff);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("The differenceMatrix");
		for (Map.Entry<String, Double> entry : differencesMatrix
				.entrySet()){
			System.out.println(entry.getValue());
		}
		System.out.println();
		
	}
	
	//parameter : the first 1..to lenght diffmatrix
	public void theBestHits(int bestHits){
		bestImageHitsIds = new String[bestHits];
		List<Map.Entry> a = new ArrayList<Map.Entry>(differencesMatrix.entrySet());
		Collections.sort(a,
		         new Comparator() {
		             public int compare(Object o1, Object o2) {
		                 Map.Entry e1 = (Map.Entry) o1;
		                 Map.Entry e2 = (Map.Entry) o2;
		                 return ((Comparable) e1.getValue()).compareTo(e2.getValue());
		             }
		         });

		int i=0;
		System.out.println("Best Elements");
		for (Map.Entry e : a) {
			if(i<bestHits){
				System.out.println(e.getKey() + " " + e.getValue());
				System.out.println(e.getKey().toString());
				bestImageHitsIds[i]=e.getKey().toString();
			}
			else{
				break;
			}
		    ++i;
		}
	}
	
	//patameter: how many tags
	public void calculateTheBestTagsForTheNewImage(int nrTags){
		System.out.println("Best Ids");
		for(int i=0;i<bestImageHitsIds.length;++i){
			System.out.print(bestImageHitsIds[i]+" ");
		}
		System.out.println();
		
		Map<String, List<String>> imageIdSAndTags = new HashMap<String, List<String>>();
		
		List<String> tempTagsList = new ArrayList<String>();
		String tagsForOneImg="";
		for(int i=0;i<bestImageHitsIds.length;++i){
			tagsForOneImg=dateBaseElements.get(bestImageHitsIds[i]).getTags();
			tempTagsList = new ArrayList<String>(Arrays.asList(tagsForOneImg.split(",")));
			imageIdSAndTags.put(bestImageHitsIds[i], tempTagsList);
			tagsForOneImg="";
		}
	
		System.out.println("Ids and List Of Tags");
		for (Map.Entry<String, List<String>> entry : imageIdSAndTags
				.entrySet()){
			System.out.println(entry.getKey()+":");
			for(int i=0;i<entry.getValue().size();++i){
				System.out.print(entry.getValue().get(i)+" , ");
			}
			System.out.println();
		}
		
		statisticsForTags = new HashMap<String,Integer>();
		
		System.out.println("Creat statistics for Tags");
		for (Map.Entry<String, List<String>> entry : imageIdSAndTags
				.entrySet()){
			//System.out.println(entry.getKey()+":");
			for(int i=0;i<entry.getValue().size();++i){
				if(statisticsForTags.containsKey(entry.getValue().get(i))){
					statisticsForTags.put(entry.getValue().get(i), statisticsForTags.get(entry.getValue().get(i))+1);
				}
				else{
					statisticsForTags.put(entry.getValue().get(i), 1);
				}
			}
			System.out.println();
		}
		
		/*System.out.println("The statistics for Tags");
		for (Map.Entry<String, Integer> entry : statisticsForTags
				.entrySet()){
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}*/
		
		List<Map.Entry> a = new ArrayList<Map.Entry>(statisticsForTags.entrySet());
		Collections.sort(a,
		         new Comparator() {
		             public int compare(Object o1, Object o2) {
		                 Map.Entry e1 = (Map.Entry) o1;
		                 Map.Entry e2 = (Map.Entry) o2;
		                 return ((Comparable) e2.getValue()).compareTo(e1.getValue());
		             }
		         });
		
		System.out.println();
		System.out.println("The tags");
		System.out.println();
		
		int i=0;
		for (Map.Entry e : a) {
			if(i<nrTags){
				System.out.println(e.getKey()+" -> "+e.getValue());
			}
			else{
				break;
			}
			++i;
			
		}
		
	}
	 
	
}
