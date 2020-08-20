import java.util.List;

public class Release {
	
	private long id;
	private String tagname;
	private String name;
	private String publishat;
	private List<Asset> assets;
	private String body;
	
	public Release(long id, String tagname, String name, String publishat, List<Asset> assets, String body) {
		super();
		this.id = id;
		this.tagname = tagname;
		this.name = name;
		this.publishat = publishat;
		this.assets = assets;
		this.body = body;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublishat() {
		return publishat;
	}

	public void setPublishat(String publishat) {
		this.publishat = publishat;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Release [id=" + id + ", tagname=" + tagname + ", name=" + name + ", publishat=" + publishat
				+ ", assets=" + assets + ", body=" + body + "]";
	}
	
	

	
}
