import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Connection {

    public final static int CONNECT_TIMEOUT = 5000;
    public final static int READ_TIMEOUT = 5000;

    public static String connectionToApi(String urlApi) {

        HttpURLConnection connection;

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        try {
            URL url = new URL(urlApi);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            int status = connection.getResponseCode();

            if (status == 404) {

                //TODO gestione degli altri codici HTTP
                /*
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
				*/

                System.out.println("Errore 404");

                return null;
                //System.exit(0);

            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();

                return responseContent.toString();
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    public static void downloadAsset(ArrayList<Asset> lastassets) {

        try {

            for (int i = 0; i < lastassets.size(); i++) {
                System.out.println("Informazioni FILE:");
                System.out.println("\t" + lastassets.get(i));
                FileUtils.copyURLToFile(new URL(lastassets.get(i).getUrl()), new File("DownloadFiles/" + lastassets.get(i).getName()), CONNECT_TIMEOUT, READ_TIMEOUT);
                System.out.println("Scaricato --> " + lastassets.get(i).getName());
            }

            System.out.println("SCARICATO TUTTO");

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
