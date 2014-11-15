
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataBase.DataBaseMethods;
import LearnImgCompMethForTags.BuildingMatrixEachImageCompMehtod;
import LearnImgCompMethForTags.CreateMethodListForTags;
import LearnImgCompMethForTags.DecidePrecentAboutOneImageAndTag;
import LearnImgCompMethForTags.OptByTagDistanceForDistanceCalculatingEachImgMethod;

public class Main {

	public static void main(String[] args) {
		
		//DataBaseMethods.saveMyBigFLickrDataBaseToTxt();
		//DataBaseMethods.insertionWithOutDownloadInLocaleDBFromTXT();
		//DataBaseMethods.queryForIdTagsLonLanLang(2);
		//DataBaseMethods.downloadImagesToYourPC();
		
		/*String fileAbsolutePath = "src/newimg.jpg";
		NewImageAddTags newImageAddTags = new NewImageAddTags(fileAbsolutePath);
		newImageAddTags.theBestHits(7);
		newImageAddTags.calculateTheBestTagsForTheNewImage(5);*/
		
		/*CrtBMethodLsToTags crtBMethodLsToTags = new CrtBMethodLsToTags();
		crtBMethodLsToTags.selectingTagsAndCreat(2);*/
		
		//param: for how many images to make comparematrix
		//T_j building
		//BuildingMatrixEachImageCompMehtod bMatrixEachImageCompMehtod = new BuildingMatrixEachImageCompMehtod(50);
		
		//Atltag
		//CreateMethodListForTags crMethodListForTags = new CreateMethodListForTags(100);
		
		//tagsAndValue : method and value 
		OptByTagDistanceForDistanceCalculatingEachImgMethod optByTgDFrDisCalEachMe = new OptByTagDistanceForDistanceCalculatingEachImgMethod(1000);
		optByTgDFrDisCalEachMe.selectTheBigestNrFromTagDiffMatrixAndCalculatingOptimDistance(10);
		
		//those it for each tag
		DecidePrecentAboutOneImageAndTag aboutOneImageAndTag = new DecidePrecentAboutOneImageAndTag(20);
		
		
	}

}
