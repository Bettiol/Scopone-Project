public class Main2 {

    public static void main(String[] args) {

        String user = "Bettiol";
        String repo = "TestApiConnectionRepo";

        Updater u = new Updater(user, repo);

        u.start();
    }
}
