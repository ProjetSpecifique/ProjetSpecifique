package LearnImgCompMethForTags;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataBase.DataBaseMethods;
import DataBase.ImageDataBaseElement;
import ImageCompV.ImageDesc;

public class DescMethodAndData {
	private String descMethodName;
	private Double[] dataAvg;// histgoram
	private Map<String, Double[]> imgIdsAndDataValue;
	Map<String, ImageDataBaseElement> imageListFromDb;

	public DescMethodAndData(String name, List<String> imageIds) {
		imageListFromDb = DataBaseMethods.getElementsFromLocaleDataBase(7000);// dippends how many pics were used at the mathodAndvalue calculating at us just 20, at test use 100
		dataAvg = new Double[256];
		descMethodName = name;
		initialiseImgAndDataValueMap(imageIds);
		calculateAvgdata();
	}
	
	public Double[] getDataAvg() {
		return dataAvg;
	}
	public void setDataAvg(Double[] dataAvg) {
		this.dataAvg = dataAvg;
	}
	
	private void initialiseImgAndDataValueMap(List<String> imageIds) {
		for(int i=0;i<256;++i){
			dataAvg[i]=(double) 0;
		}
		imgIdsAndDataValue = new HashMap<String, Double[]>();
		for (int i = 0; i < imageIds.size(); ++i) {
			imgIdsAndDataValue.put(imageIds.get(i), new Double[256]);
		}

	}

	private void calculatingDataByMethod(int typeH){
		ImageDesc imgDes = new ImageDesc();
		for (Map.Entry<String, Double[]> entry : imgIdsAndDataValue
				.entrySet()) {
			try {
				//entry.setValue(imgDes.getNormHistogramForOneImage(imageListFromDb.get(entry.getKey()).getPath(), typeH));
				imgIdsAndDataValue.put(entry.getKey(), imgDes.getNormHistogramForOneImage(imageListFromDb.get(entry.getKey()).getPath(), typeH));
			} catch (IOException e) {
				System.out.println("Error at opening image");
				System.exit(1);
			}
		}
	}
	
	private void calculateAvgdata() {
		
		//System.out.println("Calculating Data befor make AvgData for one Method");
		
		// desm method
		switch (descMethodName) {
		
		case "GrayHistDist": // 0
			calculatingDataByMethod(0);
			break;

		case "HSB_HistDist": // 1
			calculatingDataByMethod(1);
			break;
		case "Blue_HistDist": // 2
			calculatingDataByMethod(2);
			break;
		case "Red_HistDist": // 4
			calculatingDataByMethod(4);
			break;
		case "Green_HistDist": // 3
			calculatingDataByMethod(3);
			break;
		case "RGB_HistDist": // 5
			calculatingDataByMethod(5);
			break;
		}
		
		//System.out.println("Calculating finished");
		
		//System.out.println("Calculating for dataAVG");
		
		for (Map.Entry<String, Double[]> entry : imgIdsAndDataValue
				.entrySet()) {
			Double[] val = entry.getValue();
			
			for(int i=0;i<entry.getValue().length;++i){
				dataAvg[i]+=val[i];
			}
			
		}
		
		for(int i=0;i<dataAvg.length;++i){
			dataAvg[i]/=imgIdsAndDataValue.size();			
		}
		
		//System.out.println("Calculating finished");

	}

}
