import java.io.BufferedReader;
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

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

	private static HttpURLConnection connection;

	public static void main(String[] args) {

		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();

		try {
			URL url = new URL("https://api.github.com/repos/Bettiol/GitFlowTest/releases");
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

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

				parse(responseContent.toString());
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public static void parse(String responseBody) {

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
				String url = jsonLineItem.getString("url");

				String createdatstring = jsonLineItem.getString("created_at");
				LocalDateTime createdat = dateConverter(createdatstring);

				boolean statusaddassets = assets.add(new Asset(idasset, nameasset, size, createdat, url));
				System.out.println("Status ADD assets --> " + statusaddassets);
			}

			boolean statusaddrelease = release.add(new Release(id, tagname, name, publishat, assets, body));
			System.out.println("Status ADD release --> " + statusaddrelease);	 
		}

		for(int j = 0; j < release.size(); j++) {

			System.out.println(release.get(j).toString());
			System.out.println("\n");

		}


	}

	public static LocalDateTime dateConverter(String datastring) {

		Instant data = Instant.parse(datastring);

		LocalDateTime dataconvert = LocalDateTime.ofInstant(data, ZoneId.of(ZoneOffset.UTC.getId()));

		return dataconvert;
	}

	public static void dateCompare(LocalDateTime data1, LocalDateTime data2) {

		boolean isBefore = data1.isBefore(data2);
		boolean isAfter = data1.isAfter(data2);
		boolean isEqual = data1.isEqual(data2);

		System.out.println(isBefore);
		System.out.println(isAfter);
		System.out.println(isEqual);

	}

}
