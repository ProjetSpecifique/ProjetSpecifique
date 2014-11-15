package DataBase;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.text.html.HTMLDocument.Iterator;

import LearnImgCompMethForTags.TagsAndMethodList;

import com.google.common.io.Files;

public class DataBaseMethods {

	public static String bigDataBaseName = "FLICKR_GETTY_2013";
	public static String localeDataBaseName = "Locale_FLICK_2013";
	public static String localDataBaseTableImage = "Image";
	public static String localDataBaseTableTagsAndValues = "TagsAndValues";
	public static String localDataBaseTableTagsAndValuesTest = "TagsAndValuesTest";
	public static String txtPathDataBaseSaved = "src/bigDbInTxt.txt";
	public static String userName = "postgres";
	public static String password = "123456";
	// query for url, id, tags, lon, lat, lang
	public static String query1Select = "SELECT \"Image\".\"downloadURL\", \"Image\".id, \"Image\".lon, \"Image\".lat, string_agg(\"Tag\".text,','), \"Tag\".language ";
	public static String query1From = " FROM public.\"Image\", public.\"imageTag\", public.\"Tag\" ";
	public static String query1Where = " WHERE  \"Image\".id = \"imageTag\".imageid AND  \"imageTag\".tag = \"Tag\".text AND  \"Tag\".gettytag = true  group by \"Image\".id, \"Tag\".language ; ";
	// query for locale flickr database all columns
	public static String query2Select = "SELECT  \"Image\".id,  \"Image\".tags,  \"Image\".url,  \"Image\".path,  \"Image\".lon,  \"Image\".lat,  \"Image\".language ";
	public static String query2From = "FROM public.\"Image\" ";
	public static String query2Where = "WHERE \"Image\".downloaded=true Limit 100;";
	// query for locale database for downaload
	public static String query3Select = "SELECT  \"Image\".id,  \"Image\".tags,  \"Image\".url,  \"Image\".path,  \"Image\".lon,  \"Image\".lat,  \"Image\".language, \"Image\".downloaded ";
	public static String query3From = "FROM public.\"Image\" ";
	public static String query3Where = "WHERE \"Image\".downloaded=false ;";
	// query for locale flickr database all columns with changing limit number
	public static String query4Select = "SELECT  \"Image\".id,  \"Image\".tags,  \"Image\".url,  \"Image\".path,  \"Image\".lon,  \"Image\".lat,  \"Image\".language ";
	public static String query4From = "FROM public.\"Image\" ";
	public static String query4Where = "WHERE \"Image\".downloaded=true ";
	// query for all rows tagsandvalues
	public static String query5 = "SELECT * FROM public.\"TagsAndValues\";";
	// query for all rows tagsandvaluesTest
	public static String query6 = "SELECT * FROM public.\"TagsAndValuesTest\";";

	public static String localePathForImages = "D:/Egyetem/Allamvizsga/KepekAdatbazis/";

	// methods matrix distancse databse name
	public static String method1DataBaseName = "EntrophyValueDist";
	public static String method2DataBaseName = "GrayHistDist";
	public static String method3DataBaseName = "HSB_HistDist";
	public static String method4DataBaseName = "Blue_HistDist";
	public static String method5DataBaseName = "Red_HistDist";
	public static String method6DataBaseName = "Green_HistDist";
	public static String method7DataBaseName = "RGB_HistDist";
	public static String method8DataBaseName = "InterestPointsDist";
	public static String method9DataBaseName = "NumberOfFacesDist";
	// all in one
	public static String[] methodDistanceDataBaseNames = { method1DataBaseName,
			method2DataBaseName, method3DataBaseName, method4DataBaseName,
			method5DataBaseName, method6DataBaseName, method7DataBaseName,
			method8DataBaseName, method9DataBaseName };

	// creating tables for methoddist - insert table after part1 and part2
	public static String crtTableDistancePart1 = "CREATE TABLE \"";
	public static String crtTableDistancePart2 = "\" ( id1 character varying(15) NOT NULL, id2 character varying(15) NOT NULL, distance double precision NOT NULL,  CONSTRAINT \"";
	public static String crtTableDistancePart3 = "_pkey\" PRIMARY KEY (id1,id2) );";

	// Limit 100
	// parameter if its 1 writes out for output if its 2 inserts in new locale
	// database

	public static String[] goodTagsToTest = { "river", "tower", "sunlight",
			"beauty in nature", "cold", "beach", "cloud", "india", "grass",
			"tree", "city", "fog", "shadow", "people", "house", "walking",
			"photography", "car", "lake", "forest", "capital cities", "uk",
			"colour image", "wildlife", "clear sky", "cityscape", "night",
			"skyscraper", "dome", "indoors", "moon", "bridge", "cathedral",
			"dusk", "sky", "road", "plant", "branch", "hill", "rock",
			"sunrise", "pond", "landscape", "footpath", "tranquility", "day",
			"illuminated", "sunset", "building exterior", "history", "italy",
			"sweden", "sand", "dawn", "riverbank", "fish", "arch" };

	// public static String[] goodTagsToTest ={"branch"};

	public static void queryForIdTagsLonLanLang(int param) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ bigDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out.println("Opened " + bigDataBaseName + " successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query1Select + query1From
					+ query1Where);
			if (param == 1) {
				while (rs.next()) {
					String url = rs.getString(1);
					String id = rs.getString(2);
					double lon = rs.getDouble(3);
					double lat = rs.getDouble(4);
					String tags = rs.getString(5);
					String language = rs.getString(6);
					System.out.println("URL = " + url);
					System.out.println("ID = " + id);
					System.out.println("LON = " + lon);
					System.out.println("LAT = " + lat);
					System.out.println("TAGS = " + tags);
					System.out.println("LANGUAGE = " + language);
					System.out.println();
				}
			} else if (param == 2) {
				// ArrayList<ImageDataBaseElement> imageDataBaseElements = new
				// ArrayList<ImageDataBaseElement>();
				Map<String, ImageDataBaseElement> imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
				while (rs.next()) {
					String url = rs.getString(1);
					String id = rs.getString(2);
					double lon = rs.getDouble(3);
					double lat = rs.getDouble(4);
					String tags = rs.getString(5);
					String language = rs.getString(6);
					// imageDataBaseElements.add(new ImageDataBaseElement(id,
					// url, tags, lon, lat, language));
					if (!url.isEmpty()) {
						if (!imageDataBaseElements.containsKey(id)) {
							imageDataBaseElements.put(id,
									new ImageDataBaseElement(id, url, tags,
											lon, lat, language, false));
						}
					}
				}
				insertionWithOutDownloadInLocaleDB(imageDataBaseElements);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Operation done successfully");
	}

	public static void saveMyBigFLickrDataBaseToTxt() {

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ bigDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out.println("Opened " + bigDataBaseName + " successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query1Select + query1From
					+ query1Where);

			Map<String, ImageDataBaseElement> imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
			while (rs.next()) {
				String url = rs.getString(1);
				String id = rs.getString(2);
				double lon = rs.getDouble(3);
				double lat = rs.getDouble(4);
				String tags = rs.getString(5);
				String language = rs.getString(6);
				if (!url.isEmpty()) {
					if (!imageDataBaseElements.containsKey(id)) {
						imageDataBaseElements.put(id, new ImageDataBaseElement(
								id, url, tags, lon, lat, language, false));
					}
				}
			}

			// saving to txt
			System.out.println("Saving to txt big database");
			try {

				StringBuffer stringBuffer = new StringBuffer();
				String content = "";

				for (Map.Entry<String, ImageDataBaseElement> entry : imageDataBaseElements
						.entrySet()) {
					stringBuffer.append(entry.getValue().getId() + " "
							+ entry.getValue().getUrl() + " "
							+ entry.getValue().getTags() + " "
							+ entry.getValue().getPath() + " "
							+ entry.getValue().getLon() + " "
							+ entry.getValue().getLat() + " "
							+ entry.getValue().getLanguage() + " "
							+ entry.getValue().isDownloaded() + "\n");
				}

				content = stringBuffer.toString();

				File file = new File(txtPathDataBaseSaved);

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();

				System.out.println("Saving to txt done successfully");

			} catch (IOException e) {
				e.printStackTrace();
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Operation done successfully ");

	}

	public static void insertionWithOutDownloadInLocaleDBFromTXT() {

		Map<String, ImageDataBaseElement> imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();

		System.out.println("Loading from txt");

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(txtPathDataBaseSaved));

			while ((sCurrentLine = br.readLine()) != null) {
				String id = sCurrentLine.subSequence(0,
						sCurrentLine.indexOf(" ")).toString();
				sCurrentLine = sCurrentLine
						.substring(sCurrentLine.indexOf(" ") + 1);

				String url = sCurrentLine.subSequence(0,
						sCurrentLine.indexOf(" ")).toString();
				sCurrentLine = sCurrentLine
						.substring(sCurrentLine.indexOf(" ") + 1);

				String tags = sCurrentLine.subSequence(0,
						sCurrentLine.indexOf(" null")).toString();
				sCurrentLine = sCurrentLine.substring(sCurrentLine
						.indexOf("null "));
				sCurrentLine = sCurrentLine
						.substring(sCurrentLine.indexOf(" ") + 1);

				String path = "";
				// System.out.println(sCurrentLine);
				double lon = Double.parseDouble(sCurrentLine.subSequence(0,
						sCurrentLine.indexOf(" ")).toString());
				sCurrentLine = sCurrentLine
						.substring(sCurrentLine.indexOf(" ") + 1);

				double lat = Double.parseDouble(sCurrentLine.subSequence(0,
						sCurrentLine.indexOf(" ")).toString());
				sCurrentLine = sCurrentLine
						.substring(sCurrentLine.indexOf(" ") + 1);

				String language = sCurrentLine.subSequence(0,
						sCurrentLine.indexOf(" ")).toString();
				sCurrentLine = sCurrentLine
						.substring(sCurrentLine.indexOf(" ") + 1);

				boolean downloaded = Boolean.parseBoolean(sCurrentLine
						.substring(0));
				System.out.println("-" + id + "-" + url + "-" + tags + "-"
						+ lon + "-" + lat + "-" + language + "-" + downloaded);

				imageDataBaseElements.put(id, new ImageDataBaseElement(id, url,
						tags, lon, lat, language, false));

				// System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		System.out.println("Size: " + imageDataBaseElements.size());
		System.out.println("Inserting");

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out
					.println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			String sql = "";
			for (Map.Entry<String, ImageDataBaseElement> entry : imageDataBaseElements
					.entrySet()) {

				entry.getValue().setPath("");

				sql = "INSERT INTO \"" + localDataBaseTableImage
						+ "\" (id,tags,url,path,lon,lat,language,downloaded) "
						+ "VALUES ('" + entry.getValue().getId() + "', '"
						+ entry.getValue().getTags() + "', '"
						+ entry.getValue().getUrl() + "', '"
						+ entry.getValue().getPath() + "', "
						+ entry.getValue().getLon() + ", "
						+ entry.getValue().getLat() + ", '"
						+ entry.getValue().getLanguage() + "' ,"
						+ entry.getValue().isDownloaded() + " );";
				stmt.executeUpdate(sql);
				sql = "";

			}

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out
				.println("Records without downloaded path inserted successfully");
	}

	private static void insertionWithOutDownloadInLocaleDB(
			Map<String, ImageDataBaseElement> imageDataBaseElements) {
		System.out.println("Size: " + imageDataBaseElements.size());
		System.out.println("Inserting");

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out
					.println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			String sql = "";
			for (Map.Entry<String, ImageDataBaseElement> entry : imageDataBaseElements
					.entrySet()) {

				entry.getValue().setPath("");

				sql = "INSERT INTO \"" + localDataBaseTableImage
						+ "\" (id,tags,url,path,lon,lat,language,downloaded) "
						+ "VALUES ('" + entry.getValue().getId() + "', '"
						+ entry.getValue().getTags() + "', '"
						+ entry.getValue().getUrl() + "', '"
						+ entry.getValue().getPath() + "', "
						+ entry.getValue().getLon() + ", "
						+ entry.getValue().getLat() + ", '"
						+ entry.getValue().getLanguage() + "' ,"
						+ entry.getValue().isDownloaded() + " );";
				stmt.executeUpdate(sql);
				sql = "";

			}

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out
				.println("Records without downloaded path inserted successfully");

	}

	public static void downloadImagesToYourPC() {
		Connection c = null;
		Statement stmt = null;
		Map<String, ImageDataBaseElement> imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
		;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out
					.println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query3Select + query3From
					+ query3Where);

			while (rs.next()) {
				String id = rs.getString(1);
				String tags = rs.getString(2);
				String url = rs.getString(3);
				String path = rs.getString(4);
				double lon = rs.getDouble(5);
				double lat = rs.getDouble(6);
				String language = rs.getString(7);
				boolean downloaded = rs.getBoolean(8);
				imageDataBaseElements.put(id, new ImageDataBaseElement(id, url,
						tags, lon, lat, language, downloaded));
			}

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Elements listed from localeDB successfully");

		System.out.println("Downloading images");

		c = null;
		stmt = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(true);
			System.out
					.println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			String sql = "";
			String returnSaveImage;
			for (Map.Entry<String, ImageDataBaseElement> entry : imageDataBaseElements
					.entrySet()) {

				returnSaveImage = saveImage(entry.getValue().getUrl(),
						entry.getKey());
				if (returnSaveImage != "") {
					entry.getValue().setPath(returnSaveImage);
					sql = "UPDATE \"Image\" set downloaded=true , path='"
							+ entry.getValue().getPath()
							+ "' WHERE \"Image\".id='"
							+ entry.getValue().getId() + "' ;";
					stmt.executeUpdate(sql);
					sql = "";
					System.out.println("Row where id = "
							+ entry.getValue().getId() + ", updated");
				}

			}

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("All of images downloaded successfully");

	}

	// do not use it
	private static void insertionInNewDatabase(
			Map<String, ImageDataBaseElement> imageDataBaseElements) {

		System.out.println("Size: " + imageDataBaseElements.size());
		System.out.println("Saving Images");

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(true);
			System.out
					.println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			String sql = "";
			String returnSaveImage;
			for (Map.Entry<String, ImageDataBaseElement> entry : imageDataBaseElements
					.entrySet()) {

				returnSaveImage = saveImage(entry.getValue().getUrl(),
						entry.getKey());
				if (returnSaveImage != "") {
					entry.getValue().setPath(returnSaveImage);
					sql = "INSERT INTO \"" + localDataBaseTableImage
							+ "\" (id,tags,url,path,lon,lat,language) "
							+ "VALUES ('" + entry.getValue().getId() + "', '"
							+ entry.getValue().getTags() + "', '"
							+ entry.getValue().getUrl() + "', '"
							+ entry.getValue().getPath() + "', "
							+ entry.getValue().getLon() + ", "
							+ entry.getValue().getLat() + ", '"
							+ entry.getValue().getLanguage() + "' );";
					stmt.executeUpdate(sql);
					sql = "";
				}

			}

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");

	}

	private static String saveImage(String imgUrl, String namePicId) {
		BufferedImage image = null;
		try {

			URL url = new URL(imgUrl);
			// read the url
			System.out.println(url.toString());
			image = ImageIO.read(url);

			// for jpg
			ImageIO.write(image, "jpg", new File(localePathForImages
					+ namePicId + ".jpg"));

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return localePathForImages + namePicId + ".jpg";
	}

	public static Map<String, ImageDataBaseElement> getElementsFromLocaleDataBase(
			int limitForPics) {
		Connection c = null;
		Statement stmt = null;
		// <id,Row From DB>
		Map<String, ImageDataBaseElement> imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			// System.out
			// .println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			ResultSet rs = null;
			if (limitForPics == 0) {
				rs = stmt.executeQuery(query4Select + query4From + query4Where
						+ ";");
			} else {
				rs = stmt.executeQuery(query4Select + query4From + query4Where
						+ "Limit " + limitForPics + " ;");
			}

			imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
			while (rs.next()) {
				String id = rs.getString(1);
				String tags = rs.getString(2);
				String url = rs.getString(3);
				String path = rs.getString(4);
				double lon = rs.getDouble(5);
				double lat = rs.getDouble(6);
				String language = rs.getString(7);
				imageDataBaseElements.put(id, new ImageDataBaseElement(id, url,
						path, tags, lon, lat, language));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		// System.out.println("Operation done successfully");

		return imageDataBaseElements;
	}

	public static Map<String, ImageDataBaseElement> getElementsFromLocaleDataBase() {
		Connection c = null;
		Statement stmt = null;
		Map<String, ImageDataBaseElement> imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out
					.println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query2Select + query2From
					+ query2Where);

			imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
			while (rs.next()) {
				String id = rs.getString(1);
				String tags = rs.getString(2);
				String url = rs.getString(3);
				String path = rs.getString(4);
				double lon = rs.getDouble(5);
				double lat = rs.getDouble(6);
				String language = rs.getString(7);
				imageDataBaseElements.put(id, new ImageDataBaseElement(id, url,
						path, tags, lon, lat, language));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Operation done successfully");

		return imageDataBaseElements;
	}

	public static void creatTablesMethodDistanceMatrix() throws Exception {
		Connection c = null;
		Statement stmt = null;
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
				+ localeDataBaseName, userName, password);
		System.out.println("Opened " + localeDataBaseName + " successfully");

		String sql;

		for (int i = 0; i < methodDistanceDataBaseNames.length; ++i) {
			stmt = c.createStatement();
			sql = crtTableDistancePart1 + methodDistanceDataBaseNames[i]
					+ crtTableDistancePart2 + methodDistanceDataBaseNames[i]
					+ crtTableDistancePart3;
			stmt.executeUpdate(sql);
			sql = "";
			System.out.println("Table : " + methodDistanceDataBaseNames[i]
					+ " created successfully");
		}

		stmt.close();
		c.close();
		System.out
				.println("All Tables for methods distance created successfully");
	}

	public static void insertDistanceMethodMatrixInDatabes(String tableName,
			List<DistanceMatrixDataBaseElement> dList) {

		Connection c = null;
		Statement stmt = null;

		System.out.println("\nInsertingin " + tableName + "\n");

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out
					.println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			String sql = "";
			for (DistanceMatrixDataBaseElement entry : dList) {

				sql = "INSERT INTO \"" + tableName
						+ "\" (id1,id2,distance) VALUES ('" + entry.getId1()
						+ "','" + entry.getId2() + "'," + entry.getDistance()
						+ ");";
				stmt.executeUpdate(sql);
				sql = "";

			}

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Recordsinserted successfully");

	}

	public static double calculatingAverageValuesByMethods(String methodName,
			List<String> imgIdsThatContainsTag,
			List<String> imgIdsThatNotContainsTag) {

		System.out.println("Calculating averageValue for " + methodName
				+ " method");

		double[][] distanceMatrix = new double[imgIdsThatContainsTag.size()][imgIdsThatNotContainsTag
				.size()];

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out.println("Opened " + localeDataBaseName
					+ " database successfully");

			stmt = c.createStatement();
			ResultSet rs = null;
			double distance = 0;

			for (int i = 0; i < imgIdsThatContainsTag.size(); ++i) {
				for (int j = 0; j < imgIdsThatNotContainsTag.size(); ++j) {
					rs = stmt.executeQuery("SELECT \"" + methodName
							+ "\".distance FROM public.\"" + methodName
							+ "\" WHERE (\"" + methodName + "\".id1='"
							+ imgIdsThatContainsTag.get(i) + "' AND \""
							+ methodName + "\".id2='"
							+ imgIdsThatNotContainsTag.get(j) + "') OR (\""
							+ methodName + "\".id1='"
							+ imgIdsThatNotContainsTag.get(j) + "' AND \""
							+ methodName + "\".id2='"
							+ imgIdsThatContainsTag.get(i) + "') ;");
					while (rs.next()) {
						distance = rs.getDouble(1);
					}
					distanceMatrix[i][j] = distance;
				}
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Distance Matrix build successfully");

		double avg = 0;
		for (int i = 0; i < imgIdsThatContainsTag.size(); ++i) {
			for (int j = 0; j < imgIdsThatNotContainsTag.size(); ++j) {
				avg += distanceMatrix[i][j];
			}
		}

		return avg
				/ (imgIdsThatContainsTag.size() * imgIdsThatNotContainsTag
						.size());

	}

	public static void insertTagsAndValuesToDataBaseTest(
			Map<String, TagsAndMethodList> elements) {
		System.out.println("Inserting to Database TagsAndValuesTest");
		System.out.println("Size of list: " + elements.size());
		System.out.println("Inserting");

		Connection c = null;
		Statement stmt = null;

		// szures best tags
		Set<String> goodTags = new HashSet<String>();
		for (int q = 0; q < DataBaseMethods.goodTagsToTest.length; ++q) {
			goodTags.add(DataBaseMethods.goodTagsToTest[q]);
		}

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out.println("Opened " + localDataBaseTableTagsAndValuesTest
					+ " successfully");

			stmt = c.createStatement();
			String sql = "";
			;
			for (Map.Entry<String, TagsAndMethodList> entry : elements
					.entrySet()) {

				if (goodTags.contains(entry.getValue().getTag())) {
					sql = "INSERT INTO \""
							+ localDataBaseTableTagsAndValuesTest
							+ "\" (id,method1,array0,array1,method2,array2,array3,method3,array4,array5) "
							+ "VALUES ('"
							+ entry.getValue().getTag()
							+ "', '"
							+ entry.getValue()
									.dataElementsToString().get(0)
							+ "', '"
							+ entry.getValue()
									.dataElementsToString().get(1)
							+ "', '"
							+ entry.getValue()
									.dataElementsToString().get(2)
							+ "', '"
							+ entry.getValue()
									.dataElementsToString().get(3)
							+ "', '"
							+ entry.getValue()
									.dataElementsToString().get(4)
							+ "', '"
							+ entry.getValue()
									.dataElementsToString().get(5)
							+ "', '"
							+ entry.getValue()
									.dataElementsToString().get(6)
							+ "', '"
							+ entry.getValue()
									.dataElementsToString().get(7)
							+ "', '"
							+ entry.getValue()
									.dataElementsToString().get(8)
							+ "' );";
					stmt.executeUpdate(sql);
					sql = "";
				}
			}

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records method and values inserted successfully");

	}

	public static void insertTagsAndValuesToDataBase(
			Map<String, TagsAndMethodList> elements) {
		System.out.println("Inserting to Database TagsAndValues");
		System.out.println("Size of list: " + elements.size());
		System.out.println("Inserting");

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out.println("Opened " + localDataBaseTableTagsAndValues
					+ " successfully");

			stmt = c.createStatement();
			String sql = "";
			;
			for (Map.Entry<String, TagsAndMethodList> entry : elements
					.entrySet()) {
				sql = "INSERT INTO \""
						+ localDataBaseTableTagsAndValues
						+ "\" (id,methodandvalue,imgidsthatcontainstag,imgidsthatnotcontainstag) "
						+ "VALUES ('"
						+ entry.getValue().getTag()
						+ "', '"
						+ entry.getValue().convertToStringMethodAndValueMap()
						+ "', '"
						+ entry.getValue()
								.convertToStringImgIdsThatContainsTag()
						+ "', '"
						+ entry.getValue()
								.convertToStringImgIdsThatNotContainsTag()
						+ "' );";
				stmt.executeUpdate(sql);
				sql = "";
			}

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records method and values inserted successfully");

	}

	private static Double[] convertStringToDoubleArray(String ar){
		List<String> a = new ArrayList<String>(
				Arrays.asList(ar.split(", ")));
		Double[] mar= new Double[a.size()];
		
		for(int i=0;i<mar.length;++i){
			mar[i] = Double.parseDouble(a.get(i));
		}
		
		return mar;
	}
	
	public static Map<String, TagsAndMethodList> loadTagsAndValuesFromDataBase(
			boolean ifIsTest) {
		Map<String, TagsAndMethodList> elements = new HashMap<String, TagsAndMethodList>();

		System.out.println("Loading tags and values from database!");

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			System.out
					.println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			ResultSet rs;
			if (ifIsTest) {// working with test database
				rs = stmt.executeQuery(query6);// !!!!
			} else {
				rs = stmt.executeQuery(query5); // it will give error working with just 20 pics
			}

			while (rs.next()) {

				String tag = rs.getString(1);
				String method1 = rs.getString(2);
				String ar1 = rs.getString(3);
				String ar2 = rs.getString(4);
				String method2 = rs.getString(5);
				String ar3 = rs.getString(6);
				String ar4 = rs.getString(7);
				String method3 = rs.getString(8);
				String ar5 = rs.getString(9);
				String ar6 = rs.getString(10);
				TagsAndMethodList tgAM = new TagsAndMethodList(tag);

				Map<String,List<Double[]>> el = new HashMap<String,List<Double[]>>();
				List<Double[]> arrays= new ArrayList<Double[]>();
				
				//dateElements
				//1
				arrays.add(convertStringToDoubleArray(ar1));
				arrays.add(convertStringToDoubleArray(ar2));
				el.put(method1, arrays);
				//2
				arrays= new ArrayList<Double[]>();
				arrays.add(convertStringToDoubleArray(ar3));
				arrays.add(convertStringToDoubleArray(ar4));
				el.put(method2, arrays);
				//3
				arrays= new ArrayList<Double[]>();
				arrays.add(convertStringToDoubleArray(ar5));
				arrays.add(convertStringToDoubleArray(ar6));
				el.put(method3, arrays);
				
				tgAM.createElementsForDataBase(el);
				
				// methodAndValues
				/*List<String> methAnV = new ArrayList<String>(
						Arrays.asList(methodAndValue.split(", ")));
				Map<String, Double> mapAV = new HashMap<String, Double>();
				String help = "";
				for (int i = 0; i < methAnV.size(); ++i) {
					help = methAnV.get(i);
					String[] mv = help.split("=");
					mapAV.put(mv[0], Double.parseDouble(mv[1]));
				}
				tgAM.setMethodAndValue(mapAV);
				// tgAM.setMethodAndValueAndSortsIt(mapAV);
				tgAM.setMethAnValForHistAndSortIt();

				// imgThatContainstag
				List<String> imgThCoTag = new ArrayList<String>(
						Arrays.asList(imgsThatContainstag.split(", ")));
				tgAM.setImgIdsThatContainsTag(imgThCoTag);

				// imgsThatNotContainstag
				List<String> imgThNotCoTag = new ArrayList<String>(
						Arrays.asList(imgsThatNotContainstag.split(", ")));
				tgAM.setImgIdsThatNotContainsTag(imgThNotCoTag);*/

				elements.put(tgAM.getTag(), tgAM);

				/*
				 * System.out.println("Tag = " + tag);
				 * System.out.println("methodAndValue = " + methodAndValue);
				 * System.out.println("imgsThatContainstag = " +
				 * imgsThatContainstag);
				 * System.out.println("imgsThatNotContainstag = " +
				 * imgsThatNotContainstag); System.out.println();
				 */
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Operation done successfully");

		return elements;
	}

	public static List<ImageDataBaseElement> getImagesThatSimilerWithGoodTags(
			int nr) {
		List<ImageDataBaseElement> similerElements = new ArrayList<ImageDataBaseElement>();

		System.out.println("Loading images from database!");

		Connection c = null;
		Statement stmt = null;

		// <id,Row From DB>
		Map<String, ImageDataBaseElement> imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			// System.out
			// .println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(query4Select + query4From + query4Where
					+ ";");

			imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
			while (rs.next()) {
				String id = rs.getString(1);
				String tags = rs.getString(2);
				String url = rs.getString(3);
				String path = rs.getString(4);
				double lon = rs.getDouble(5);
				double lat = rs.getDouble(6);
				String language = rs.getString(7);
				imageDataBaseElements.put(id, new ImageDataBaseElement(id, url,
						path, tags, lon, lat, language));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		int i = 0;
		int picNr = 0;
		for (Map.Entry<String, ImageDataBaseElement> entry : imageDataBaseElements
				.entrySet()) {
			if (i > 100) {
				if (picNr < nr) {
					for (int k = 0; k < goodTagsToTest.length; ++k) {
						if (entry.getValue().getTags()
								.contains(goodTagsToTest[k])) {
							similerElements.add(entry.getValue());
							break;
						}
					}
					++picNr;
				}
				else{
					break;
				}

			}
			++i;
		}

		return similerElements;
	}

	public static List<ImageDataBaseElement> getImagesThatNOTSimilerWithGoodTags(
			int nr) {
		List<ImageDataBaseElement> nonSimilerElements = new ArrayList<ImageDataBaseElement>();

		System.out.println("Loading images from database!");

		Connection c = null;
		Statement stmt = null;

		// <id,Row From DB>
		Map<String, ImageDataBaseElement> imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"
					+ localeDataBaseName, userName, password);
			c.setAutoCommit(false);
			// System.out
			// .println("Opened " + localeDataBaseName + " successfully");

			stmt = c.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(query4Select + query4From + query4Where
					+ ";");

			imageDataBaseElements = new HashMap<String, ImageDataBaseElement>();
			while (rs.next()) {
				String id = rs.getString(1);
				String tags = rs.getString(2);
				String url = rs.getString(3);
				String path = rs.getString(4);
				double lon = rs.getDouble(5);
				double lat = rs.getDouble(6);
				String language = rs.getString(7);
				imageDataBaseElements.put(id, new ImageDataBaseElement(id, url,
						path, tags, lon, lat, language));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		int i = 0;
		int picNr = 0;
		boolean itCont=false;
		for (Map.Entry<String, ImageDataBaseElement> entry : imageDataBaseElements
				.entrySet()) {
			if (i > 100) {
				if (picNr < nr) {
					for (int k = 0; k < goodTagsToTest.length; ++k) {
						if (entry.getValue().getTags()
								.contains(goodTagsToTest[k])) {
							itCont=true;
						}
					}
					if(!itCont){//nics benne
						nonSimilerElements.add(entry.getValue());
						++picNr;
					}
					itCont=false;
				}
				else{
					break;
				}

			}
			++i;
		}

		return nonSimilerElements;
	}
	
}
