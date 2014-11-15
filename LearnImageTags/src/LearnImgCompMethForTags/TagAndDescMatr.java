package LearnImgCompMethForTags;
import java.io.IOException;
import java.util.Map;

import ImageCompV.ImageDesc;


public class TagAndDescMatr {
	private String tag;
	private Map<String,String> descImgIds;
	private Map<String,String> notDescImgIds;
	private double[][] descMatrixl;
	
	public TagAndDescMatr(String tag,Map<String,String> descImgIds, Map<String,String> notDescImgIds){
		this.tag=tag;
		this.descImgIds=descImgIds;
		this.notDescImgIds=notDescImgIds;
		descMatrixl = new double[descImgIds.size()][notDescImgIds.size()];
		System.out.println("Tag: "+tag+" Size of descImgByTag: "+descImgIds.size()+" notDescImgbyTag: "+notDescImgIds.size());
		for(int i=0;i<6;++i){//6 diff types of histogram
			buildMatrix(i);
		}
	}
	
	private void buildMatrix(int typeHist){
		int i=0;
		int j=0;
		ImageDesc imageDesc = new ImageDesc();
		for(Map.Entry<String, String> entryI : descImgIds.entrySet()){
			for(Map.Entry<String, String> entryJ : notDescImgIds.entrySet()){
				try {
					descMatrixl[i][j]= imageDesc.normalizationOfTwoSameTypeOfHistogram(typeHist, entryI.getValue(), entryJ.getValue());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				++j;
			}
			j=0;
			++i;
		}
		
		double avg=0;
		for(int k=0;k<descMatrixl.length;++k){
			for(int l=0;l<descMatrixl[k].length;++l){
				avg+=descMatrixl[k][l];
			}
		}
		
		System.out.println("Average: "+avg/(descImgIds.size()*notDescImgIds.size())+"\n");
		
	}
	

	public void printMatrix(){
		
		System.out.println("The matrix for tag: "+tag+" -> by gray scale histogram");
		for(int i=0;i<descMatrixl.length;++i){
			for(int j=0;j<descMatrixl[i].length;++j){
				System.out.print(descMatrixl[i][j]+" ");
			}
			System.out.println();
		}
	}
	
}
