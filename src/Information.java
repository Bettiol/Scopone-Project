public class Information {

    private final static String BASE_URL = "https://api.github.com/";

    private String username;
    private String repo;
    private String url;

    public Information(String username, String repo) {
        this.username = username;
        this.repo = repo;
        this.url = createUrl(username, repo);
    }

    private String createUrl(String username, String reponame) {

        String completeurl = BASE_URL + "repos/" + username + "/" + reponame + "/releases";

        System.out.println(completeurl);
        return completeurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Information{" +
                "username='" + username + '\'' +
                ", repo='" + repo + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
