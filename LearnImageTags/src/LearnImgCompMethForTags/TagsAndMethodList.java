package LearnImgCompMethForTags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.tree.TreeModel;

import org.postgresql.jdbc2.ArrayAssistantRegistry;

public class TagsAndMethodList {
	
	private String tag;
	private Map<String,Double> methodAndValue;//values for database
	private List<String> imgIdsThatContainsTag;
	private List<String> imgIdsThatNotContainsTag;
	private double[][] tagsDiffereceMatrix;//T_sz_ksz = f_tksz
	private TreeMap<Double,List<String>> tagDiffMatrixOrderByValue;//List contains 2 ids at pos 0 and 1
	private SortedSet<Map.Entry<String,Double>> methAnValForHist; // it is normalized, when is sorted
	private List<String> imgsDescmethodAndValue;
	private List<String> imgsNotDescmethodAndValue;
	private Map<String,List<Double[]>> elementToDatabase;//<methodname, <0:good hists 1: badhists>>
	
	public TagsAndMethodList(String tag){
		this.tag=tag;
		methodAndValue = new HashMap<String, Double>();
		imgIdsThatContainsTag = new ArrayList<String>();
		imgIdsThatNotContainsTag = new ArrayList<String>();
	}
		
	public void setMethodAndValueElement(String method, double value){
		methodAndValue.put(method, value);
	}
	
	public void setElemenContainsImgTag(String el){
		if(!imgIdsThatContainsTag.contains(el)){
			imgIdsThatContainsTag.add(el);
		}
	}
	
	public void setElementNotContainsImgTag(String el){
		if(!imgIdsThatNotContainsTag.contains(el)){
			imgIdsThatNotContainsTag.add(el);
		}
	}
	
	public SortedSet<Map.Entry<String, Double>> getMethAnValForHist() {
		return methAnValForHist;
	}
	
	public void setMethAnValForHist(
			SortedSet<Map.Entry<String, Double>> methAnValForHist) {
		this.methAnValForHist = methAnValForHist;
	}
	
	public List<String> getImgIdsThatContainsTag() {
		return imgIdsThatContainsTag;
	}
	
	public void setImgIdsThatContainsTag(List<String> imgIdsThatContainsTag) {
		this.imgIdsThatContainsTag = imgIdsThatContainsTag;
	}
	
	public List<String> getImgIdsThatNotContainsTag() {
		return imgIdsThatNotContainsTag;
	}
	
	public void setImgIdsThatNotContainsTag(List<String> imgIdsThatNotContainsTag) {
		this.imgIdsThatNotContainsTag = imgIdsThatNotContainsTag;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}

	public Map<String, Double> getMethodAndValue() {
		return methodAndValue;
	}
	
	public void setMethodAndValue(Map<String, Double> methodAndValue) {
		this.methodAndValue = methodAndValue;
	}
	/*public void setMethodAndValueAndSortsIt(Map<String, Double> methodAndValue) {
		this.methodAndValue = (Map<String, Double>) entriesSortedByValues(methodAndValue);
	}*/
	
	public double[][] getTagsDiffereceMatrix() {
		return tagsDiffereceMatrix;
	}
	
	public void setTagsDiffereceMatrix(double[][] tagsDiffereceMatrix) {
		this.tagsDiffereceMatrix = tagsDiffereceMatrix;
	}
	
	public TreeMap<Double, List<String>> getTagDiffMatrixOrderByValue() {
		return tagDiffMatrixOrderByValue;
	}
	
	public void calculatingAverageValues(){
				
	}
	
	public void createDistanceTagMatrix(){
		tagsDiffereceMatrix = new double[imgIdsThatContainsTag.size()][imgIdsThatNotContainsTag.size()];
		//System.out.println("createDistanceTagMatrix: "+imgIdsThatContainsTag.size()+" "+imgIdsThatNotContainsTag.size());
	}
	
	public void setElementForTagsDifferenceMatrix(int i,int j, double value){
		tagsDiffereceMatrix[i][j]=value;
	}
	
	public void calculatingTagDiffValuesOrderByDistanceValue(){
		tagDiffMatrixOrderByValue = new TreeMap<>(new MyComp());
		for(int y=0;y<tagsDiffereceMatrix.length;++y){
			for(int x=0;x<tagsDiffereceMatrix[y].length;++x){
				List<String> ids = new ArrayList<String>();
				ids.add(imgIdsThatContainsTag.get(y));
				ids.add(imgIdsThatNotContainsTag.get(x));
				tagDiffMatrixOrderByValue.put(tagsDiffereceMatrix[y][x], ids);
			}
		}
		
		//System.out.println("TagDiffValueOrderByValue");
		/*for(Map.Entry<Double, List<String>> entry: tagDiffMatrixOrderByValue.entrySet() ){
			System.out.print(entry.getKey() + " : "+entry.getValue().get(0)+" - "+entry.getValue().get(1)+ " , ");
		}
		System.out.println();*/
	}
	
	public String convertToStringImgIdsThatContainsTag(){
		String l = imgIdsThatContainsTag.toString();
		l=l.substring(1);
		l=l.substring(0, l.length()-1);
		return l;
	}
	
	public String convertToStringImgIdsThatNotContainsTag(){
		String l = imgIdsThatNotContainsTag.toString();
		l=l.substring(1);
		l=l.substring(0, l.length()-1);
		return l;
	}
	
	public String convertToStringMethodAndValueMap(){
		String l = methodAndValue.toString();
		l=l.substring(1);
		l=l.substring(0, l.length()-1);
		return l;
	}
	
	public void setImgsDescMetAndVal(List<String> idsThatDescr){
		imgsDescmethodAndValue = new ArrayList<String>();
		imgsDescmethodAndValue = idsThatDescr;
	}
	
	public void setImgsNotDescMetAndVal(List<String> idsThatNotDescr){
		imgsNotDescmethodAndValue = new ArrayList<String>();
		imgsNotDescmethodAndValue = idsThatNotDescr;
	}
	
	public String convertToStringImgsDescMetAndVal(){
		String l = imgsDescmethodAndValue.toString();
		l=l.substring(1);
		l=l.substring(0, l.length()-1);
		return l;
	}
	
	public String convertToStringImgsNotDescMetAndVal(){
		String l = imgsNotDescmethodAndValue.toString();
		l=l.substring(1);
		l=l.substring(0, l.length()-1);
		return l;
	}
	
	public List<String> getImgsThatDesc(){
		return imgsDescmethodAndValue;
	}
	
	public List<String> getImgsThatNotDesc(){
		return imgsNotDescmethodAndValue;
	}
	
	public void createElementsForDataBase(Map<String,List<Double[]>> el){
		elementToDatabase = new HashMap<String,List<Double[]>>();
		elementToDatabase = el;
		//System.out.println("Created "+elementToDatabase.size());
		System.out.println("Element for DB for tag: "+tag+" created ");
		/*for(Map.Entry<String,List<Double[]>> ent : elementToDatabase.entrySet()){
			System.out.println("MethodNAme :"+ent.getKey());
			System.out.println("Good Array: " + ent.getValue().get(0).toString() );
			System.out.println("Bad Array: " + ent.getValue().get(0).toString() );
		}*/
	}
	
	public Map<String,List<Double[]>> getElementsForDataBase(){
		return elementToDatabase;
	}
	
	public List<String> dataElementsToString(){
		List<String> list = new ArrayList<String>();
		for(Map.Entry<String,List<Double[]>> ent : elementToDatabase.entrySet()){
			list.add(ent.getKey());
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i< ent.getValue().get(0).length;++i ){
				sb.append(ent.getValue().get(0)[i]+", ");
			}
			System.out.println(sb.toString());
			list.add(sb.toString());
			sb = new StringBuilder();
			for(int i = 0; i< ent.getValue().get(1).length;++i ){
				sb.append(ent.getValue().get(1)[i]+", ");
			}
			list.add(sb.toString());
			System.out.println(sb);
		}
		return list;
	}
	
	//sorted DESC
	public void setMethAnValForHistAndSortIt(){
		Map<String,Double> helpMap = new HashMap<String,Double>();
		for(Map.Entry<String, Double> ent : methodAndValue.entrySet()){
			helpMap.put(ent.getKey(), ent.getValue());
		}
		helpMap=normalizeMethodAndValues(helpMap);
		methAnValForHist = entriesSortedByValuesDesc(helpMap);
	}
	
	private Map<String,Double> normalizeMethodAndValues(Map<String,Double> mapWithMethodAndVals){
		double max=0;
		double min=Double.MAX_VALUE;
		for(Map.Entry<String, Double> ent : mapWithMethodAndVals.entrySet()){
			if(ent.getValue()>max){
				max=ent.getValue();
			}
			if(ent.getValue()<min){
				min=ent.getValue();
			}
		}
		for(Map.Entry<String, Double> ent : mapWithMethodAndVals.entrySet()){
			mapWithMethodAndVals.put(ent.getKey(), ent.getValue()/(max-min));
		}
		return mapWithMethodAndVals;
	}
	
	class MyComp implements Comparator<Double>{
		 
	    @Override
	    public int compare(Double str1, Double str2) {
	        return str2.compareTo(str1);
	    }
	     
	}
	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValuesDesc(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e2.getValue().compareTo(e1.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValuesAsc(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e1.getValue().compareTo(e2.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
	
}
