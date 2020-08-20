import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

			if (status > 299) {

				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();

			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			}
			//System.out.println(responseContent.toString());

			parse(responseContent.toString());

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
		
		List<Asset> assets = new ArrayList<Asset>();
		
		List<Release> release = new ArrayList<Release>();
		
		System.out.println(informazioni.length());
		
		for (int i = 0; i < informazioni.length(); i++) {
			
			JSONObject info = informazioni.getJSONObject(i);
			
			
			String publishat = info.getString("published_at");
			long id = info.getLong("id");
			String tagname = info.getString("tag_name");
			String name = info.getString("name");
			String body = info.getString("body");
			
			
			//System.out.println(publicata);
			
			
			
			JSONArray asset = info.getJSONArray("assets");
			
			for (Object o : asset) {
			 
				JSONObject jsonLineItem = (JSONObject) o; 
				
				long idasset = jsonLineItem.getLong("id"); 
				String nameasset = jsonLineItem.getString("name");
				long size = jsonLineItem.getLong("size"); 
				String createdat = jsonLineItem.getString("created_at");
				String url = jsonLineItem.getString("url");
				
				boolean retu = assets.add(new Asset(idasset, nameasset, size, createdat, url));
				//System.out.println(assets);
			}
			
			
			boolean re = release.add(new Release(id, tagname, name, publishat, assets, body));
			//System.out.println(assets);
			
			assets.clear();
			
			//System.out.println(assets);
			
					 
		}
		
		System.out.println(release);
		/*for(int j = 0; j < release.size(); j++) {
            System.out.println(release.get(j).toString());
            System.out.println("\n");
        }*/
		
		
	}

}
