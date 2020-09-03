import javax.naming.PartialResultException;
import java.util.ArrayList;

public class Updater {

    private Information info;

    public Updater(String user, String repo) {
        this.info = new Information(user, repo);
    }

    private Release verifyLastRelease() {

        String response;
        ArrayList<Release> releases;
        Release lastrelease = null;
        ArrayList<Asset> assetslastrelease;

        response = Connection.connectionToApi(info.getUrl());

        if (response != null) {

            releases = Parser.parse(response);

            lastrelease = Parser.findLastRelease(releases);


            System.out.println("LAST RELEASE --> \n" + lastrelease);

        }
        
        return lastrelease;
    }

    public void start() {

        Release temp = verifyLastRelease();

        if (temp != null) {

            //TODO Controllo ultimo verione installata
            if (true) {
                Connection.downloadAsset(temp.getAssets());
            } else {

            }
        } else  {
            System.out.println("It's null");
        }

    }

}