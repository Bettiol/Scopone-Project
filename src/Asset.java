import java.time.LocalDateTime;

public class Asset {

    private long id;
    private String name;
    private long size;
    private LocalDateTime createdat;
    private String url;

    public Asset(long id, String name, long size, LocalDateTime createdat, String url) {
        super();
        this.id = id;
        this.name = name;
        this.size = size;
        this.createdat = createdat;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getCreatedat() {
        return createdat;
    }

    public void setCreatedat(LocalDateTime createdat) {
        this.createdat = createdat;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Asset [id=" + id + ", name=" + name + ", size=" + size + ", createdat=" + createdat + ", url=" + url
                + "]";
    }

}
