import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class Parser {

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
}
