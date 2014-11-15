package DataBase;

public class ImageDataBaseElement {
	private String id;
	private String url;
	private String path;
	private String tags;
	private double lon;
	private double lat;
	private String language;
	private boolean downloaded;
		
	public ImageDataBaseElement(String id, String url, String tags, double lon,
			double lat, String language, boolean downloaded) {
		super();
		this.id = id;
		this.url = url;
		this.tags = tags;
		this.lon = lon;
		this.lat = lat;
		this.language = language;
		this.downloaded=downloaded;
	}
	
	public ImageDataBaseElement(String id, String url,String path, String tags, double lon,
			double lat, String language) {
		super();
		this.id = id;
		this.url = url;
		this.path=path;
		this.tags = tags;
		this.lon = lon;
		this.lat = lat;
		this.language = language;
	}
	
	public boolean isDownloaded() {
		return downloaded;
	}
	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	
}
