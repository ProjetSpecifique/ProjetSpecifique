package LearnImgCompMethForTags;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import DataBase.DataBaseMethods;
import DataBase.ImageDataBaseElement;
import ImageCompV.ImageDesc;

public class OptByTagDistanceForDistanceCalculatingEachImgMethod {

	// id and object
	private Map<String, ImageDataBaseElement> imageListFromDb;
	private Map<String, List<String>> idsAndTags;
	private Map<String, List<String>> tagsAndIdsList;
	// tag, and the object
	private Map<String, TagsAndMethodList> tags;
	private Map<String, ImageDataBaseElement> imageListFromDbWithPathKey;
	private Map<String, Map<Integer, double[]>> imgPathAndHistogramArray;
	private Set<String> goodTags;
	//number of bigeszt diszkriminans method
	private int theNrOfBigestVal=3;

	public OptByTagDistanceForDistanceCalculatingEachImgMethod(int nrPics) {
		imageListFromDb = DataBaseMethods.getElementsFromLocaleDataBase(nrPics);
		imgPathAndHistogramArray = new HashMap<String, Map<Integer, double[]>>();
		// atalakitani hogy path alapjan tudjunk keresni
		// path and obj
		imageListFromDbWithPathKey = new HashMap<String, ImageDataBaseElement>();
		for (Map.Entry<String, ImageDataBaseElement> entry : imageListFromDb
				.entrySet()) {
			imageListFromDbWithPathKey.put(entry.getValue().getPath(),
					entry.getValue());
		}
		idsAndTags = new HashMap<String, List<String>>();
		tagsAndIdsList = new HashMap<String, List<String>>();
		tags = new HashMap<String, TagsAndMethodList>();

		// szures best tags
		goodTags = new HashSet<String>();
		for (int q = 0; q < DataBaseMethods.goodTagsToTest.length; ++q) {
			goodTags.add(DataBaseMethods.goodTagsToTest[q]);
		}

		loadTags();
		buildingDistanceByTags();
	}

	private void loadTags() {

		for (Map.Entry<String, ImageDataBaseElement> entry : imageListFromDb
				.entrySet()) {
			idsAndTags.put(
					entry.getKey(),
					new ArrayList<String>(Arrays.asList(entry.getValue()
							.getTags().split(","))));
		}

		System.out.println("Loading tags");
		for (Map.Entry<String, List<String>> entry : idsAndTags.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); ++i) {
				if (!tags.containsKey(entry.getValue().get(i))) {// nincs benne
					tags.put(entry.getValue().get(i), new TagsAndMethodList(
							entry.getValue().get(i)));
					tags.get(entry.getValue().get(i)).setElemenContainsImgTag(
							entry.getKey());
				} else {// benne van
					tags.get(entry.getValue().get(i)).setElemenContainsImgTag(
							entry.getKey());
				}
			}
		}

		System.out.println("Number of tags: " + tags.size());

		for (Map.Entry<String, List<String>> entry : idsAndTags.entrySet()) {
			for (Map.Entry<String, TagsAndMethodList> entryS : tags.entrySet()) {
				if (!entryS.getValue().getImgIdsThatContainsTag()
						.contains(entry.getKey())) {// if ids not in containids
					entryS.getValue().setElementNotContainsImgTag(
							entry.getKey());
				}
			}
		}

		System.out.println("Tags statistics");
		for (Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()) {
			System.out.println("Tag: '" + entry.getValue().getTag()
					+ "' size idsin: "
					+ entry.getValue().getImgIdsThatContainsTag().size()
					+ " size idsnotin: "
					+ entry.getValue().getImgIdsThatNotContainsTag().size());
		}

		for (Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()) {
			tagsAndIdsList.put(entry.getValue().getTag(), entry.getValue()
					.getImgIdsThatContainsTag());
		}
	}

	private void buildingDistanceByTags() {
		System.out.println("Building Distance by Tag...");

		/*
		 * System.out.println("Tags and ids:"); for (Map.Entry<String,
		 * List<String>> entry : tagsAndIdsList.entrySet()) {
		 * System.out.print(entry.getKey()+" : "); for (int i = 0; i <
		 * entry.getValue().size(); ++i) {
		 * System.out.print(entry.getValue().get(i)+" "); }
		 * System.out.println(""); }
		 */

		System.out
				.println("Calculating the T_sz_ksz matrix ( tag differnece matrix )");

		for (Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()) {// tag
																			// alapjan
			entry.getValue().createDistanceTagMatrix();
			// System.out.println("Tag: " + entry.getValue().getTag());
			for (int i = 0; i < entry.getValue().getImgIdsThatContainsTag()
					.size(); ++i) {
				for (int j = 0; j < entry.getValue()
						.getImgIdsThatNotContainsTag().size(); ++j) {
					int comunTagsNr = getComunTags(entry.getValue()
							.getImgIdsThatContainsTag().get(i), entry
							.getValue().getImgIdsThatNotContainsTag().get(j));
					int notComunTagsNr = idsAndTags.get(
							entry.getValue().getImgIdsThatContainsTag().get(i))
							.size()
							+ idsAndTags.get(
									entry.getValue()
											.getImgIdsThatNotContainsTag()
											.get(j)).size() - comunTagsNr;
					// System.out.println("Tag: "+entry.getValue().getTag()+" ids: "+entry.getValue().getImgIdsThatContainsTag().get(i)+" - "+
					// entry.getValue().getImgIdsThatNotContainsTag().get(j)
					// +" commun tags: "+comunTagsNr+" not commun tags: "+notComunTagsNr
					// );

					// egyik es masik kepnek hany kulcsaszava van
					double the2PicTagLength = Math.abs(idsAndTags.get(
							entry.getValue().getImgIdsThatContainsTag().get(i))
							.size()
							- idsAndTags.get(
									entry.getValue()
											.getImgIdsThatNotContainsTag()
											.get(j)).size());
					double diff = 0;
					if (the2PicTagLength != 0) {
						diff = (comunTagsNr - notComunTagsNr)
								/ the2PicTagLength;
					} else {
						diff = (comunTagsNr - notComunTagsNr);
					}
					entry.getValue().setElementForTagsDifferenceMatrix(i, j,
							diff);
				}
			}

			/*
			 * System.out.println("The tagDiffMatrix: "); double [][]diffMat=
			 * entry.getValue().getTagsDiffereceMatrix(); for(int
			 * y=0;y<diffMat.length;++y){ for(int x=0;x<diffMat[y].length;++x){
			 * System.out.print(diffMat[y][x] +" "); } System.out.println(); }
			 * System.out.println();
			 */
			entry.getValue().calculatingTagDiffValuesOrderByDistanceValue();
			// itt lehet a huba
		}

		System.out.println("Calculating finished");
	}

	// the firs o bigest diff pics if o is to big Than allOfThem
	public void selectTheBigestNrFromTagDiffMatrixAndCalculatingOptimDistance(
			int oL) {

		TreeMap<Double, List<String>> theDiffValues;
		int index = 0;
		List<String> idsThatDescr;
		List<String> idsThatNotDescr;// this are ids
		System.out.println("Start building");

		for (Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()) {

			if (goodTags.contains(entry.getValue().getTag())) {

				theDiffValues = new TreeMap<Double, List<String>>();
				index = 0;
				System.out.println("Tag: " + entry.getValue().getTag());
				for (Map.Entry<Double, List<String>> entryIn : entry.getValue()
						.getTagDiffMatrixOrderByValue().entrySet()) {
					// System.out.println(" Dist: "+entryIn.getKey()+" : "+entryIn.getValue().get(0)+"-"+entryIn.getValue().get(1));
					if (index >= oL) {//javitsd oL
						// System.out.println(index);
						break;
					}
					theDiffValues.put(entryIn.getKey(), entryIn.getValue());
					++index;
				}

				idsThatDescr = new ArrayList<String>();
				idsThatNotDescr = new ArrayList<String>();

				for (Map.Entry<Double, List<String>> ent : theDiffValues
						.entrySet()) {
					idsThatDescr.add(imageListFromDbWithPathKey.get(
							imageListFromDb.get(ent.getValue().get(0))
									.getPath()).getId());
					idsThatNotDescr.add(imageListFromDbWithPathKey.get(
							imageListFromDb.get(ent.getValue().get(1))
									.getPath()).getId());
				}
				tags.get(entry.getKey()).setImgsDescMetAndVal(idsThatDescr);
				tags.get(entry.getKey()).setImgsNotDescMetAndVal(
						idsThatNotDescr);

			}
		}

		System.out.println("Build Finished For All tags");

		System.out.println("Creating hisograms");
		creatingHistograms();

		/*for(Map.Entry<String, Map<Integer, double[]>> e : imgPathAndHistogramArray.entrySet()){
			System.out.println(e.getKey());
			for(Map.Entry<Integer, double[]> ei : e.getValue().entrySet()){
				System.out.println("Meth: "+ei.getKey()+" size: "+ei.getValue().length);
			}
		}*/
		
		// creating elements for databse
		for (Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()) {

			if (goodTags.contains(entry.getValue().getTag())) {

				entry.getValue().setMethodAndValue(
						buildMatrixAvgByMethodOptByTags(entry
								.getValue().getTag()));				
			}
		}
		
		for (Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()) {

			if (goodTags.contains(entry.getValue().getTag())) {

				entry.getValue().setMethAnValForHistAndSortIt();//diszkriminans modszerek
				Iterator it = entry.getValue().getMethAnValForHist().iterator();
				Map<String,List<Double[]>> elms = new HashMap<String,List<Double[]>>();
				List<Double[]> arrays = new ArrayList<Double[]>();
				int i = 0;
				while(it.hasNext()){
					if (i == theNrOfBigestVal) {			
						break;
					}
					String[] pair = it.next().toString().split("=");
					String methodName = pair[0];
					
					//makeking to avg arrays(one good, other bad)
					DescMethodAndData goodArrayObj = new DescMethodAndData(methodName, entry.getValue().getImgsThatDesc());
					DescMethodAndData badArrayObj = new DescMethodAndData(methodName, entry.getValue().getImgsThatNotDesc());
					arrays.add(goodArrayObj.getDataAvg());
					arrays.add(badArrayObj.getDataAvg());
					elms.put(methodName, arrays);
					++i;
				}	
				entry.getValue().createElementsForDataBase(elms);
			}
		}
		
		// write to
		DataBaseMethods.insertTagsAndValuesToDataBaseTest(tags);

	}

	private Map<String, Double> buildMatrixAvgByMethodOptByTags(String tagName) {

		Map<String, Double> distanceAvgByMethodNameAndValue = new HashMap<String, Double>();

		double size = tags.get(tagName).getImgsThatDesc().size()* tags.get(tagName).getImgsThatNotDesc().size();

		double grayHist = 0;
		double hsbHist = 0;
		double blueHist = 0;
		double redHist = 0;
		double greenHist = 0;
		double rgbHist = 0;
		ImageDesc imgDesc = new ImageDesc();

		/*System.out
				.println("Building Matrix Avg By Method Opt By Tags the GrayHistDist, HSB_HistDist, Blue_HistDist, Red_HistDist, Green_HistDist, RGB_HistDist, InterestPointsDist, NumberOfFacesDist matrix ");*/

		List<String> idsThatDescr = new ArrayList<String>();
		idsThatDescr = tags.get(tagName).getImgsThatDesc();
		List<String> idsThatNotDescr = new ArrayList<String>();
		idsThatNotDescr = tags.get(tagName).getImgsThatNotDesc();
		
		List<String> pathsThatDescr = new ArrayList<String>();
		List<String> pathsThatNotDescr = new ArrayList<String>();

		for(int i=0;i<idsThatDescr.size();++i){
			pathsThatDescr.add(imageListFromDb.get(idsThatDescr.get(i)).getPath());
		}
		
		for(int i=0;i<idsThatNotDescr.size();++i){
			pathsThatNotDescr.add(imageListFromDb.get(idsThatNotDescr.get(i)).getPath());
		}

		for (int i = 0; i < pathsThatDescr.size(); ++i) {
			for (int j = 0; j < pathsThatNotDescr.size(); ++j) {
				try {
					grayHist += imgDesc.normalizationOfTwoSameTypeOfHistogram(
							0, imgPathAndHistogramArray.get(pathsThatDescr.get(i)).get(0), imgPathAndHistogramArray.get(pathsThatNotDescr.get(j)).get(0));
				} catch (IOException e) {
					grayHist += 0;
					e.printStackTrace();
				}

				try {
					hsbHist += imgDesc.normalizationOfTwoSameTypeOfHistogram(
							1, imgPathAndHistogramArray.get(pathsThatDescr.get(i)).get(1), imgPathAndHistogramArray.get(pathsThatNotDescr.get(j)).get(1));
				} catch (IOException e) {
					hsbHist += 0;
					e.printStackTrace();
				}

				try {
					blueHist += imgDesc.normalizationOfTwoSameTypeOfHistogram(
							2, imgPathAndHistogramArray.get(pathsThatDescr.get(i)).get(2), imgPathAndHistogramArray.get(pathsThatNotDescr.get(j)).get(2));
				} catch (IOException e) {
					blueHist += 0;
					e.printStackTrace();
				}

				try {
					redHist += imgDesc.normalizationOfTwoSameTypeOfHistogram(
							4, imgPathAndHistogramArray.get(pathsThatDescr.get(i)).get(4), imgPathAndHistogramArray.get(pathsThatNotDescr.get(j)).get(4));
				} catch (IOException e) {
					redHist += 0;
					e.printStackTrace();
				}

				try {
					greenHist += imgDesc.normalizationOfTwoSameTypeOfHistogram(
							3, imgPathAndHistogramArray.get(pathsThatDescr.get(i)).get(3), imgPathAndHistogramArray.get(pathsThatNotDescr.get(j)).get(3));
				} catch (IOException e) {
					greenHist += 0;
					e.printStackTrace();
				}

				try {
					rgbHist += imgDesc.normalizationOfTwoSameTypeOfHistogram(
							5, imgPathAndHistogramArray.get(pathsThatDescr.get(i)).get(5), imgPathAndHistogramArray.get(pathsThatNotDescr.get(j)).get(5));
				} catch (IOException e) {
					rgbHist += 0;
					e.printStackTrace();
				}

			}
		}


		System.out.println("Building the matrixs finished for tag: "+tagName);
		distanceAvgByMethodNameAndValue.put(
				DataBaseMethods.method2DataBaseName, grayHist / size);
		distanceAvgByMethodNameAndValue.put(
				DataBaseMethods.method3DataBaseName, hsbHist / size);
		distanceAvgByMethodNameAndValue.put(
				DataBaseMethods.method4DataBaseName, blueHist / size);
		distanceAvgByMethodNameAndValue.put(
				DataBaseMethods.method5DataBaseName, redHist / size);
		distanceAvgByMethodNameAndValue.put(
				DataBaseMethods.method6DataBaseName, greenHist / size);
		distanceAvgByMethodNameAndValue.put(
				DataBaseMethods.method7DataBaseName, rgbHist / size);
		
		System.out.println("Gray " + grayHist/size + ", hsb " + hsbHist/size + ", blue "
				+ blueHist + ", red " + redHist/size + ", green " + greenHist/size
				+ ", rgb " + rgbHist/size + ", size " + size);

		return distanceAvgByMethodNameAndValue;

	}

	
	private void creatingHistograms() {

		List<String> idsThatDescr;
		List<String> idsThatNotDescr;
		ImageDesc imgDesc = new ImageDesc();
		Map<Integer, double[]> hist;

		for (Map.Entry<String, TagsAndMethodList> entry : tags.entrySet()) {

			if (goodTags.contains(entry.getValue().getTag())) {
				idsThatDescr = new ArrayList<String>();
				idsThatNotDescr = new ArrayList<String>();
				idsThatDescr = entry.getValue().getImgsThatDesc();
				idsThatNotDescr = entry.getValue().getImgsThatNotDesc();

				/*System.out.println("Tag: " + entry.getKey()
						+ " Size desc and notdesc imgs: " + idsThatDescr.size()
						+ " " + idsThatNotDescr.size());*/

				for (int i = 0; i < idsThatDescr.size(); ++i) {
					if (!imgPathAndHistogramArray.containsKey(imageListFromDb
							.get(idsThatDescr.get(i)).getPath())) {// if it isnt
																	// calculated
						
						imgPathAndHistogramArray.put(imageListFromDb
							.get(idsThatDescr.get(i)).getPath(), new HashMap<Integer, double[]>());
						
						hist = new HashMap<Integer, double[]>();
						try {
							hist.put(0, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatDescr.get(i))
											.getPath(), 0));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(1, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatDescr.get(i))
											.getPath(), 1));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(2, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatDescr.get(i))
											.getPath(), 2));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(4, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatDescr.get(i))
											.getPath(), 4));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(3, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatDescr.get(i))
											.getPath(), 3));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(5, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatDescr.get(i))
											.getPath(), 5));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						imgPathAndHistogramArray.put(
								imageListFromDb.get(idsThatDescr.get(i))
										.getPath(), hist);

					}
				}

				for (int i = 0; i < idsThatNotDescr.size(); ++i) {
					if (!imgPathAndHistogramArray.containsKey(imageListFromDb
							.get(idsThatNotDescr.get(i)).getPath())) {// if it
																		// isnt
																		// calculated
						
						imgPathAndHistogramArray.put(imageListFromDb
								.get(idsThatNotDescr.get(i)).getPath(), new HashMap<Integer, double[]>());
						
						hist = new HashMap<Integer, double[]>();
						try {
							hist.put(0, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatNotDescr.get(i))
											.getPath(), 0));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(1, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatNotDescr.get(i))
											.getPath(), 1));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(2, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatNotDescr.get(i))
											.getPath(), 2));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(4, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatNotDescr.get(i))
											.getPath(), 4));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(3, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatNotDescr.get(i))
											.getPath(), 3));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						try {
							hist.put(5, imgDesc.getHistogramByType(
									imageListFromDb.get(idsThatNotDescr.get(i))
											.getPath(), 5));
						} catch (IOException e) {
							System.out.println("Error histcreating");
							e.printStackTrace();
						}

						imgPathAndHistogramArray.put(
								imageListFromDb.get(idsThatNotDescr.get(i))
										.getPath(), hist);
					}
				}

			}
		}

		System.out.println("Histogram Arrray calculate finished!");

	}

	private int getComunTags(String pic1Id, String pic2Id) {
		int i = 0;
		Map<String, Integer> staticsForKeyWords = new HashMap<String, Integer>();

		for (int k = 0; k < idsAndTags.get(pic1Id).size(); ++k) {// for pic1
			staticsForKeyWords.put(idsAndTags.get(pic1Id).get(k), 1);
		}

		for (int k = 0; k < idsAndTags.get(pic2Id).size(); ++k) {// for pic2
			if (staticsForKeyWords.containsKey(idsAndTags.get(pic2Id).get(k))) {
				staticsForKeyWords
						.put(idsAndTags.get(pic2Id).get(k), staticsForKeyWords
								.get(idsAndTags.get(pic2Id).get(k)) + 1);
			}
		}

		for (Map.Entry<String, Integer> entry : staticsForKeyWords.entrySet()) {
			if (entry.getValue() >= 2) {
				++i;
			}
		}

		return i;
	}

}
