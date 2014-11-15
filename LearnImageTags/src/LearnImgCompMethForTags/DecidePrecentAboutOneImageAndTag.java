package LearnImgCompMethForTags;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import DataBase.DataBaseMethods;
import DataBase.ImageDataBaseElement;
import ImageCompV.ImageDesc;

public class DecidePrecentAboutOneImageAndTag {

	private String imgPath;
	private Map<String, TagsAndMethodList> tags;// what we use
	private int theNrOfBigestVal = 3;
	private Map<String, Double[]> newPicHisgorams;
	private double probabilityAllInOne = 0;
	private int nrPicturesToDesc;
	private Map<ImageDataBaseElement, Map<String, Double[]>> imagesToDescSimiler; // <,<histname,value>>
	private Map<ImageDataBaseElement, Map<String, Double[]>> imagesToDescNotSimiler; // <path,<histname,value>>
	private Map<Integer,List<String>> xlsObj;
	private int rowsXLS=0;

	public DecidePrecentAboutOneImageAndTag(String imgP) {// tets one pic
		imgPath = imgP;
		tags = new HashMap<String, TagsAndMethodList>();
		tags = DataBaseMethods.loadTagsAndValuesFromDataBase(false);
		newPicHisgorams = new HashMap<String, Double[]>();
		calculatingHistogramForNewPic();
		calculatingForEachBestMethodAvgValues();
	}

	public DecidePrecentAboutOneImageAndTag(int nrOfPicsToDesc) { // test nr of
																	// pics are
																	// similar
																	// othes nr
																	// are
																	// different

		nrPicturesToDesc = nrOfPicsToDesc;
		tags = new HashMap<String, TagsAndMethodList>();
		tags = DataBaseMethods.loadTagsAndValuesFromDataBase(true);
		imagesToDescSimiler = new HashMap<ImageDataBaseElement, Map<String, Double[]>>();
		imagesToDescNotSimiler = new HashMap<ImageDataBaseElement, Map<String, Double[]>>();
		
		//xlsOutObj : <tag,<method+prob,pic desbid,similerornot>>
		xlsObj = new HashMap<Integer,List<String>>();

		List<ImageDataBaseElement> listNC = new ArrayList<ImageDataBaseElement>();
		listNC = DataBaseMethods
				.getImagesThatNOTSimilerWithGoodTags(nrOfPicsToDesc);

		List<ImageDataBaseElement> listC = new ArrayList<ImageDataBaseElement>();
		listC = DataBaseMethods
				.getImagesThatSimilerWithGoodTags(nrOfPicsToDesc);

		System.out.println("Size of Similer Pics Wich To Desc " + listC.size());
		System.out.println("Size of NONSimiler Pics Wich To Desc "
				+ listNC.size());

		for (int q = 0; q < listC.size(); ++q) {// vigyaz ha nem egyeznek a
												// meretek listC es listNC
			imagesToDescSimiler.put(listC.get(q),
					new HashMap<String, Double[]>());
			imagesToDescNotSimiler.put(listNC.get(q),
					new HashMap<String, Double[]>());
		}

		System.out.println("Calculating newSimilerPics Hisogram");
		for (Map.Entry<ImageDataBaseElement, Map<String, Double[]>> entry : imagesToDescSimiler
				.entrySet()) {
			imagesToDescSimiler.put(entry.getKey(),
					calculatingHistogramForNewPic(entry.getKey().getPath()));
		}
		System.out.println("Calculating newNotSimilerPics Hisogram");
		for (Map.Entry<ImageDataBaseElement, Map<String, Double[]>> entry : imagesToDescNotSimiler
				.entrySet()) {
			imagesToDescNotSimiler.put(entry.getKey(),
					calculatingHistogramForNewPic(entry.getKey().getPath()));
		}

		System.out.println("\nSimiler Pics\n");
		calculatingForEachBestMethodAvgValues(imagesToDescSimiler,true);

		System.out.println("\nNot Similer Pics\n");
		calculatingForEachBestMethodAvgValues(imagesToDescSimiler,false);
		
		writingToXLS();
		
	}

	private void calculatingForEachBestMethodAvgValues() {

		// going through tags
		for (Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()) {
			// getMethAnValForHist()) here are the best values, and from here we
			// need the best 3
			System.out.print(entry.getKey() + ": ");
			Iterator it = entry.getValue().getMethAnValForHist().iterator();
			int i = 0;
			while (it.hasNext()) {
				if (i == theNrOfBigestVal) {
					System.out.println(probabilityAllInOne / theNrOfBigestVal);
					probabilityAllInOne = 0;
					break;
				}
				String[] pair = it.next().toString().split("=");
				String methodName = pair[0];// we got the method name
				// System.out.println("---------cont----------"+entry.getValue().getImgIdsThatContainsTag().size());
				// System.out.println("---------Notcont----------"+entry.getValue().getImgIdsThatNotContainsTag().size());
				DescMethodAndData describesTag = new DescMethodAndData(
						methodName, entry.getValue().getImgIdsThatContainsTag());
				DescMethodAndData notDescribesTag = new DescMethodAndData(
						methodName, entry.getValue()
								.getImgIdsThatNotContainsTag());

				calculatingProbabilityForATag(methodName,
						describesTag.getDataAvg(), notDescribesTag.getDataAvg());

				// System.out.println(methodName);
				++i;
			}
			System.out.println();
		}

	}

	private void calculatingForEachBestMethodAvgValues(
			Map<ImageDataBaseElement, Map<String, Double[]>> newPicsList,boolean simmilerORNot) {

		Map<String, List<DescMethodAndData>> methodAndData = new HashMap<String, List<DescMethodAndData>>();
		
		List<String> tmpObjXLS = new ArrayList<String>();
		
		// going through tags
		for (Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()) {
			// getMethAnValForHist()) here are the best values, and from here we
			// need the best 3
			System.out.print(entry.getKey() + ": ");
			
			for (Map.Entry<ImageDataBaseElement, Map<String, Double[]>> e : newPicsList
					.entrySet()) {
				for (Map.Entry<String, List<Double[]>> m : entry.getValue()
						.getElementsForDataBase().entrySet()) {
					calculatingProbabilityForATag(m.getKey(),
							m.getValue().get(0), m.getValue().get(1), e
									.getValue().get(m.getKey()));
				}
				tmpObjXLS = new ArrayList<String>();
				tmpObjXLS.add(entry.getKey());
				String p = (probabilityAllInOne/theNrOfBigestVal)+"";
				tmpObjXLS.add(p);
				tmpObjXLS.add(e.getKey().getId().toString());
				
				if(simmilerORNot){
					tmpObjXLS.add("Yes");
				}
				else{
					tmpObjXLS.add( "No" );
				}
				
				System.out.print("img: " + e.getKey().getId() + " p: "
						+ probabilityAllInOne / theNrOfBigestVal + " ");
				probabilityAllInOne = 0;
				xlsObj.put(rowsXLS, tmpObjXLS);
				++rowsXLS;
			}

			System.out.println();
		}

	}

	private void writingToXLS() {
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		data.put("1", new Object[] { "Tag", "P", "ImgName","SimiLerOrNot"});

		System.out.println("Writing elements to exel file!");

		int ind = 2;
		for (Map.Entry<Integer, List<String>> entry : xlsObj.entrySet()) {
			String namet = entry.getValue().get(0);
			String d[] = { entry.getValue().get(1), entry.getValue().get(2), entry.getValue().get(3)};
			data.put(ind + "", new Object[] { namet, d[0], d[1], d[2] });
			++ind;
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");

		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof Date)
					cell.setCellValue((Date) obj);
				else if (obj instanceof Boolean)
					cell.setCellValue((Boolean) obj);
				else if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Double)
					cell.setCellValue((Double) obj);
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(new File(
					"src/probabiltyByPics.xls"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Writing completed!");
	}

	private double calculatingSimpleDistanceBetweenTwoMethodVal(Double[] ar1,
			Double[] ar2) {
		double sum = 0;
		for (int i = 0; i < ar1.length; ++i) {
			sum += Math.abs(ar1[i] - ar2[i]);
		}
		return sum;
	}

	private void calculatingProbabilityForATag(String methodName,
			Double[] descAr, Double[] notDescAr) {
		double distDescAndNew = calculatingSimpleDistanceBetweenTwoMethodVal(
				descAr, newPicHisgorams.get(methodName));
		double distNotDescAndNew = calculatingSimpleDistanceBetweenTwoMethodVal(
				notDescAr, newPicHisgorams.get(methodName));
		double disDescAndNotDesc = calculatingSimpleDistanceBetweenTwoMethodVal(
				descAr, notDescAr);
		double probability = distDescAndNew
				/ (distNotDescAndNew + distDescAndNew);
		probabilityAllInOne += probability;
		// System.out.print(" by "+methodName+" : "+probability+" ");
	}

	private void calculatingProbabilityForATag(String methodName,
			Double[] descAr, Double[] notDescAr, Double[] newPicDescAr) {
		double distDescAndNew = calculatingSimpleDistanceBetweenTwoMethodVal(
				descAr, newPicDescAr);
		double distNotDescAndNew = calculatingSimpleDistanceBetweenTwoMethodVal(
				notDescAr, newPicDescAr);
		double disDescAndNotDesc = calculatingSimpleDistanceBetweenTwoMethodVal(
				descAr, notDescAr);
		double probability = distDescAndNew
				/ (distNotDescAndNew + distDescAndNew);
		probabilityAllInOne += probability;
		// System.out.print(" by "+methodName+" : "+probability+" ");
	}

	private Map<String, Double[]> calculatingHistogramForNewPic(String pathPic) {
		// System.out.println("Calculating Hists for new Pic");

		Map<String, Double[]> calcH = new HashMap<String, Double[]>();

		ImageDesc imgDesc = new ImageDesc();
		try {
			calcH.put("GrayHistDist", new Double[256]);
			calcH.put("HSB_HistDist", new Double[256]);
			calcH.put("Blue_HistDist", new Double[256]);
			calcH.put("Red_HistDist", new Double[256]);
			calcH.put("Green_HistDist", new Double[256]);
			calcH.put("RGB_HistDist", new Double[256]);

			calcH.put("GrayHistDist",
					imgDesc.getNormHistogramForOneImage(pathPic, 0));
			calcH.put("HSB_HistDist",
					imgDesc.getNormHistogramForOneImage(pathPic, 1));
			calcH.put("Blue_HistDist",
					imgDesc.getNormHistogramForOneImage(pathPic, 2));
			calcH.put("Red_HistDist",
					imgDesc.getNormHistogramForOneImage(pathPic, 4));
			calcH.put("Green_HistDist",
					imgDesc.getNormHistogramForOneImage(pathPic, 3));
			calcH.put("RGB_HistDist",
					imgDesc.getNormHistogramForOneImage(pathPic, 5));

			// System.out.println("Calculate finished!");

			return calcH;
		} catch (IOException e) {
			System.out.println("Image File not Found!");
			System.exit(1);
		}

		return calcH;

	}

	private void calculatingHistogramForNewPic() {
		System.out.println("Calculating Hists for new Pic");
		ImageDesc imgDesc = new ImageDesc();
		try {
			newPicHisgorams.put("GrayHistDist", new Double[256]);
			newPicHisgorams.put("HSB_HistDist", new Double[256]);
			newPicHisgorams.put("Blue_HistDist", new Double[256]);
			newPicHisgorams.put("Red_HistDist", new Double[256]);
			newPicHisgorams.put("Green_HistDist", new Double[256]);
			newPicHisgorams.put("RGB_HistDist", new Double[256]);

			newPicHisgorams.put("GrayHistDist",
					imgDesc.getNormHistogramForOneImage(imgPath, 0));
			newPicHisgorams.put("HSB_HistDist",
					imgDesc.getNormHistogramForOneImage(imgPath, 1));
			newPicHisgorams.put("Blue_HistDist",
					imgDesc.getNormHistogramForOneImage(imgPath, 2));
			newPicHisgorams.put("Red_HistDist",
					imgDesc.getNormHistogramForOneImage(imgPath, 4));
			newPicHisgorams.put("Green_HistDist",
					imgDesc.getNormHistogramForOneImage(imgPath, 3));
			newPicHisgorams.put("RGB_HistDist",
					imgDesc.getNormHistogramForOneImage(imgPath, 5));
		} catch (IOException e) {
			System.out.println("Image File not Found!");
			System.exit(1);
		}

		System.out.println("Calculate finished!");
	}

}
