package LearnImgCompMethForTags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataBase.DataBaseMethods;
import DataBase.ImageDataBaseElement;

public class CreateMethodListForTags {
	
	//tag, and the object
	private Map<String,TagsAndMethodList> tags;
	//id and object
	private Map<String, ImageDataBaseElement> dataBaseElements;
	//id and words list
	private Map<String,List<String>> words;
	
	public CreateMethodListForTags(int numberOfPics){
		
		dataBaseElements = new HashMap<String, ImageDataBaseElement>();
		words = new HashMap<String, List<String>>();
		tags = new HashMap<String,TagsAndMethodList>();
		System.out.println("Loading Images From Locale DB");
		dataBaseElements = DataBaseMethods
				.getElementsFromLocaleDataBase(numberOfPics);

		buildWords();
	}
	
	private void buildWords(){
		
		System.out.println("Colecting words");
		for(Map.Entry<String, ImageDataBaseElement> entry : dataBaseElements.entrySet()){
			words.put(entry.getKey(),new ArrayList<String>(Arrays.asList(entry.getValue()
							.getTags().split(","))));
		}
			
		System.out.println("Loading words");
		for (Map.Entry<String, List<String>> entry : words.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); ++i) {
				if (!tags.containsKey(entry.getValue().get(i))) {
					tags.put(entry.getValue().get(i), new TagsAndMethodList(entry.getValue().get(i)));
					tags.get(entry.getValue().get(i)).setElemenContainsImgTag(entry.getKey());
				} 
				else{
					tags.get(entry.getValue().get(i)).setElemenContainsImgTag(entry.getKey());
				}
			}
		}
		
		System.out.println("Number of tags: "+tags.size());
				
		for (Map.Entry<String, List<String>> entry : words.entrySet()){
			for(Map.Entry<String, TagsAndMethodList> entryS : tags.entrySet()){
				if(!entryS.getValue().getImgIdsThatContainsTag().contains(entry.getKey())){//if ids not in containids
					entryS.getValue().setElementNotContainsImgTag(entry.getKey());
				}
			}
		}
		
		/*System.out.println("Tags statistics");
		for(Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()){
			System.out.println("Tag: "+entry.getValue().getTag() + " size idsin: "+entry.getValue().getImgIdsThatContainsTag().size()+" size idsnotin: "+entry.getValue().getImgIdsThatNotContainsTag().size());
		}*/
		
		for(Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()){
			entry.getValue().setMethodAndValueElement(DataBaseMethods.method2DataBaseName, DataBaseMethods.calculatingAverageValuesByMethods(DataBaseMethods.method2DataBaseName, entry.getValue().getImgIdsThatContainsTag(), entry.getValue().getImgIdsThatNotContainsTag()));
			entry.getValue().setMethodAndValueElement(DataBaseMethods.method3DataBaseName, DataBaseMethods.calculatingAverageValuesByMethods(DataBaseMethods.method3DataBaseName, entry.getValue().getImgIdsThatContainsTag(), entry.getValue().getImgIdsThatNotContainsTag()));
			entry.getValue().setMethodAndValueElement(DataBaseMethods.method4DataBaseName, DataBaseMethods.calculatingAverageValuesByMethods(DataBaseMethods.method4DataBaseName, entry.getValue().getImgIdsThatContainsTag(), entry.getValue().getImgIdsThatNotContainsTag()));
			entry.getValue().setMethodAndValueElement(DataBaseMethods.method5DataBaseName, DataBaseMethods.calculatingAverageValuesByMethods(DataBaseMethods.method5DataBaseName, entry.getValue().getImgIdsThatContainsTag(), entry.getValue().getImgIdsThatNotContainsTag()));
			entry.getValue().setMethodAndValueElement(DataBaseMethods.method6DataBaseName, DataBaseMethods.calculatingAverageValuesByMethods(DataBaseMethods.method6DataBaseName, entry.getValue().getImgIdsThatContainsTag(), entry.getValue().getImgIdsThatNotContainsTag()));
			entry.getValue().setMethodAndValueElement(DataBaseMethods.method7DataBaseName, DataBaseMethods.calculatingAverageValuesByMethods(DataBaseMethods.method7DataBaseName, entry.getValue().getImgIdsThatContainsTag(), entry.getValue().getImgIdsThatNotContainsTag()));
			entry.getValue().setMethodAndValueElement(DataBaseMethods.method8DataBaseName, DataBaseMethods.calculatingAverageValuesByMethods(DataBaseMethods.method8DataBaseName, entry.getValue().getImgIdsThatContainsTag(), entry.getValue().getImgIdsThatNotContainsTag()));
			entry.getValue().setMethodAndValueElement(DataBaseMethods.method9DataBaseName, DataBaseMethods.calculatingAverageValuesByMethods(DataBaseMethods.method9DataBaseName, entry.getValue().getImgIdsThatContainsTag(), entry.getValue().getImgIdsThatNotContainsTag()));
		}
		
		
		System.out.println("\nTags and their avg values by methods \n");
		for(Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()){
			System.out.println("Tag : "+entry.getValue().getTag());
			for(Map.Entry<String, Double> entry2 : entry.getValue().getMethodAndValue().entrySet()){
				System.out.println("MethodName: "+entry2.getKey()+" and avg value: "+entry2.getValue());
			}
			System.out.println();
		}
		
	}
	
}
