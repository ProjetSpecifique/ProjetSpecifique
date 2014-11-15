package LearnImgCompMethForTags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PSQLException;

import DataBase.*;
import ImageCompV.ImageDesc;

public class BuildingMatrixEachImageCompMehtod {

	private Map<String, ImageDataBaseElement> dataBaseElements;
	private ImageDesc imgDesc;

	public BuildingMatrixEachImageCompMehtod(int numberOfImagesFromDataBase) {

		imgDesc = new ImageDesc();
		dataBaseElements = new HashMap<String, ImageDataBaseElement>();
		System.out.println("Loading Images From Locale DB");
		dataBaseElements = DataBaseMethods
				.getElementsFromLocaleDataBase(numberOfImagesFromDataBase);
		createTheDiffMatrixByEatchCompMethodToTheDataBase();

	}

	private void createTheDiffMatrixByEatchCompMethodToTheDataBase() {
		System.out
				.println("Create The Diff Matrix By Eatch Comp Method To The DataBase");

		try {
			DataBaseMethods.creatTablesMethodDistanceMatrix();
		} catch (Exception e) {
			if (e.getMessage().contains("already exists")) {
				System.out.println("Tables already exists");
			} else {
				System.out.println(e.getMessage() + "kjvgcvg");
				System.exit(1);
			}
		}

		
		/*//tul lassu azert van kikomentelve
		 * DataBaseMethods.insertDistanceMethodMatrixInDatabes(
				DataBaseMethods.method1DataBaseName, distanceMatrixBuilding(1));*/
		
		DataBaseMethods.insertDistanceMethodMatrixInDatabes(
				DataBaseMethods.method2DataBaseName, distanceMatrixBuilding(2));
		
		DataBaseMethods.insertDistanceMethodMatrixInDatabes(
				DataBaseMethods.method3DataBaseName, distanceMatrixBuilding(3));
		
		DataBaseMethods.insertDistanceMethodMatrixInDatabes(
				DataBaseMethods.method4DataBaseName, distanceMatrixBuilding(4));
		
		DataBaseMethods.insertDistanceMethodMatrixInDatabes(
				DataBaseMethods.method5DataBaseName, distanceMatrixBuilding(5));
		
		DataBaseMethods.insertDistanceMethodMatrixInDatabes(
				DataBaseMethods.method6DataBaseName, distanceMatrixBuilding(6));
		
		DataBaseMethods.insertDistanceMethodMatrixInDatabes(
				DataBaseMethods.method7DataBaseName, distanceMatrixBuilding(7));
		
		DataBaseMethods.insertDistanceMethodMatrixInDatabes(
				DataBaseMethods.method8DataBaseName, distanceMatrixBuilding(8));
		
		DataBaseMethods.insertDistanceMethodMatrixInDatabes(
				DataBaseMethods.method9DataBaseName, distanceMatrixBuilding(9));

	}

	// 1=EntrophyValueDist 2=GrayHistDist 3=HSB_HistDist 4=Blue_HistDist 5=Red_HistDist 6=Green_HistDist
	// 7=RGB_HistDist 8=InterestPointsDist 9=NumberOfFacesDist
	private List<DistanceMatrixDataBaseElement> distanceMatrixBuilding(
			int whichMatrix) {
		
		List<DistanceMatrixDataBaseElement> dList = new ArrayList<DistanceMatrixDataBaseElement>();
		System.out.println("Building distance");

		double[][] distanceValues = new double[dataBaseElements.size()][dataBaseElements
				.size()];
		String[] distanceIds = new String[dataBaseElements.size()];

		int i = 0;
		int j = 0;

		for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
				.entrySet()) {
			distanceIds[i] = entryI.getKey();
			++i;
		}

		i = 0;
		j = 0;

		switch (whichMatrix) {
		case 1:

			System.out.println("Building the EntrophyValueDist matrix");
			for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
					.entrySet()) {
				for (Map.Entry<String, ImageDataBaseElement> entryJ : dataBaseElements
						.entrySet()) {
					distanceValues[i][j] = imgDesc
							.theTwoImageEntorpyValueDistance(entryI.getValue()
									.getPath(), entryJ.getValue().getPath());
					++j;
				}
				++i;
				j = 0;
			}
			System.out.println("Building the matrix finished");

			break;

		case 2:

			System.out.println("Building the GrayHistDist matrix");
			for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
					.entrySet()) {
				for (Map.Entry<String, ImageDataBaseElement> entryJ : dataBaseElements
						.entrySet()) {
					try {
						distanceValues[i][j] = imgDesc
								.normalizationOfTwoSameTypeOfHistogram(0,
										entryI.getValue().getPath(), entryJ
												.getValue().getPath());
					} catch (IOException e) {
						distanceValues[i][j] = -1024;
						e.printStackTrace();
					}
					++j;
				}
				++i;
				j = 0;
			}
			System.out.println("Building the matrix finished");

			break;

		case 3:

			System.out.println("Building the HSB_HistDist matrix");
			for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
					.entrySet()) {
				for (Map.Entry<String, ImageDataBaseElement> entryJ : dataBaseElements
						.entrySet()) {
					try {
						distanceValues[i][j] = imgDesc
								.normalizationOfTwoSameTypeOfHistogram(1,
										entryI.getValue().getPath(), entryJ
												.getValue().getPath());
					} catch (IOException e) {
						distanceValues[i][j] = -1024;
						e.printStackTrace();
					}
					++j;
				}
				++i;
				j = 0;
			}
			System.out.println("Building the matrix finished");

			break;
		case 4:

			System.out.println("Building the Blue_HistDist matrix");
			for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
					.entrySet()) {
				for (Map.Entry<String, ImageDataBaseElement> entryJ : dataBaseElements
						.entrySet()) {
					try {
						distanceValues[i][j] = imgDesc
								.normalizationOfTwoSameTypeOfHistogram(2,
										entryI.getValue().getPath(), entryJ
												.getValue().getPath());
					} catch (IOException e) {
						distanceValues[i][j] = -1024;
						e.printStackTrace();
					}
					++j;
				}
				++i;
				j = 0;
			}
			System.out.println("Building the matrix finished");

			break;
		case 5:

			System.out.println("Building the Red_HistDist matrix");
			for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
					.entrySet()) {
				for (Map.Entry<String, ImageDataBaseElement> entryJ : dataBaseElements
						.entrySet()) {
					try {
						distanceValues[i][j] = imgDesc
								.normalizationOfTwoSameTypeOfHistogram(4,
										entryI.getValue().getPath(), entryJ
												.getValue().getPath());
					} catch (IOException e) {
						distanceValues[i][j] = -1024;
						e.printStackTrace();
					}
					++j;
				}
				++i;
				j = 0;
			}
			System.out.println("Building the matrix finished");

			break;
		case 6:

			System.out.println("Building the Green_HistDist matrix");
			for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
					.entrySet()) {
				for (Map.Entry<String, ImageDataBaseElement> entryJ : dataBaseElements
						.entrySet()) {
					try {
						distanceValues[i][j] = imgDesc
								.normalizationOfTwoSameTypeOfHistogram(3,
										entryI.getValue().getPath(), entryJ
												.getValue().getPath());
					} catch (IOException e) {
						distanceValues[i][j] = -1024;
						e.printStackTrace();
					}
					++j;
				}
				++i;
				j = 0;
			}
			System.out.println("Building the matrix finished");

			break;
		case 7:

			System.out.println("Building the RGB_HistDist matrix");
			for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
					.entrySet()) {
				for (Map.Entry<String, ImageDataBaseElement> entryJ : dataBaseElements
						.entrySet()) {
					try {
						distanceValues[i][j] = imgDesc
								.normalizationOfTwoSameTypeOfHistogram(5,
										entryI.getValue().getPath(), entryJ
												.getValue().getPath());
					} catch (IOException e) {
						distanceValues[i][j] = -1024;
						e.printStackTrace();
					}
					++j;
				}
				++i;
				j = 0;
			}
			System.out.println("Building the matrix finished");

			break;
			
		case 8:

			System.out.println("Building the InterestPointsDist matrix");
			for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
					.entrySet()) {
				for (Map.Entry<String, ImageDataBaseElement> entryJ : dataBaseElements
						.entrySet()) {
					distanceValues[i][j] = imgDesc
							.get2ImageCommunInterestPoint(entryI.getValue()
									.getPath(), entryJ.getValue().getPath());
					++j;
				}
				++i;
				j = 0;
			}
			System.out.println("Building the matrix finished");

			break;
			
		case 9:

			System.out.println("Building the NumberOfFacesDist matrix");
			for (Map.Entry<String, ImageDataBaseElement> entryI : dataBaseElements
					.entrySet()) {
				for (Map.Entry<String, ImageDataBaseElement> entryJ : dataBaseElements
						.entrySet()) {
					distanceValues[i][j] = imgDesc
							.getNumberOfFacesOnTwoImage(entryI.getValue()
									.getPath(), entryJ.getValue().getPath());
					++j;
				}
				++i;
				j = 0;
			}
			System.out.println("Building the matrix finished");

			break;
		}

		i = 0;
		j = 0;

		System.out.println("Calculating under main diagonal and listing it");
		for (i = 0; i < distanceValues.length; ++i) {
			for (j = 0; j < i; ++j) {
				System.out.print(distanceValues[i][j]+" ");
				dList.add(new DistanceMatrixDataBaseElement(distanceIds[i],
						distanceIds[j], distanceValues[i][j]));
			}
			System.out.println();
		}

		return dList;

	}

	
	
}
