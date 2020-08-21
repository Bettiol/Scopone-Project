import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class Main {

    public final static int CONNECT_TIMEOUT = 5000;
    public final static int READ_TIMEOUT = 5000;
    public final static String API_URL = "https://api.github.com/repos/Bettiol/TestApiConnectionRepo/releases";

    public static void main(String[] args) {

        Release lastrel;
        ArrayList<Asset> lastassets;

        lastrel = verifyLastRelease();
        lastassets = lastrel.getAssets();

        downloadAsset(lastassets);

		/*
		 JAVA 11+

		 HttpClient client = HttpClient.newHttpClient();
		 HttpRequest request = HttpRequest.newBuilder()
		 	.uri(URI.create("https://api.github.com/repos/Bettiol/GitFlowTest/releases"))
		 	.build(); client.sendAsync(request,
		 HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
		 	.thenAccept(System.out::println).join();
		 */

    }

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
				/*
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
				 */
                System.out.println("Errore 404");
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();

            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseContent.toString();
    }

    public static ArrayList<Release> parse(String responseBody) {

        JSONArray informazioni = new JSONArray(responseBody);

        ArrayList<Asset> assets = null;

        ArrayList<Release> release = new ArrayList<Release>();


        for (int i = 0; i < informazioni.length(); i++) {

            assets = new ArrayList<Asset>();

            JSONObject info = informazioni.getJSONObject(i);

            long id = info.getLong("id");
            String tagname = info.getString("tag_name");
            String name = info.getString("name");
            String body = info.getString("body");

            String publishatstring = info.getString("published_at");
            LocalDateTime publishat = dateConverter(publishatstring);

            JSONArray asset = info.getJSONArray("assets");

            for (Object o : asset) {

                JSONObject jsonLineItem = (JSONObject) o;

                long idasset = jsonLineItem.getLong("id");
                String nameasset = jsonLineItem.getString("name");
                long size = jsonLineItem.getLong("size");
                String url = jsonLineItem.getString("browser_download_url");

                String createdatstring = jsonLineItem.getString("created_at");
                LocalDateTime createdat = dateConverter(createdatstring);

                boolean statusaddassets = assets.add(new Asset(idasset, nameasset, size, createdat, url));
                System.out.println("Status ADD assets --> " + statusaddassets);
            }

            boolean statusaddrelease = release.add(new Release(id, tagname, name, publishat, assets, body));
            System.out.println("Status ADD release --> " + statusaddrelease);
        }

        return release;

    }

    public static LocalDateTime dateConverter(String datastring) {

        Instant data = Instant.parse(datastring);

        LocalDateTime dataconvert = LocalDateTime.ofInstant(data, ZoneId.of(ZoneOffset.UTC.getId()));

        return dataconvert;
    }

    public static Release findLastRelease(ArrayList<Release> releases) {

        int position = 0;
        LocalDateTime last = dateConverter("0000-01-01T00:00:00Z");
        LocalDateTime temp;

        for (int i = 0; i < releases.size(); i++) {

            temp = releases.get(i).getPublishat();

            if (temp.isAfter(last) == true) {

                last = temp;
                position = i;

            }
        }

        return releases.get(position);

    }

    public static void printReleaseArrayList(ArrayList<Release> releases) {

        for (int i = 0; i < releases.size(); i++) {

            System.out.println(releases.get(i).toString());
            System.out.println("\n");

        }

    }

    public static Release verifyLastRelease() {

        String response;
        ArrayList<Release> releases;
        Release lastrelease;
        ArrayList<Asset> assetslastrelease;

        response = connectionToApi(API_URL);
        releases = parse(response);

        lastrelease = findLastRelease(releases);

        System.out.println("LAST RELEASE --> \n" + lastrelease);

        return lastrelease;
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
