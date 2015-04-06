package com.mycompany.newsreader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Vara on 4/3/2015.
 */
public class JsonDataProvider {

    private static final String SERVER_URL = "https://dl.dropboxusercontent.com/u/746330/facts.json";
    private ArrayList<DataContainer> list;
    public static String MAIN_TITLE;

    public JsonDataProvider(ArrayList<DataContainer> list)
    {
        this.list = list;
    }
    public int getDataFromCloud()
    {
        int returnCode = -1;

        try
        {
            URL url = new URL(SERVER_URL);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setConnectTimeout(5000);
            httpConnection.connect();

            int responseCode = httpConnection.getResponseCode();

            if(responseCode == 200)
            {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line+"\n");
                }
                bufferedReader.close();
                returnCode = parseJsonMessage(stringBuilder.toString());
            }
            httpConnection.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return returnCode;
    }

    private int parseJsonMessage(String jsonString)
    {
        int returnCode = -1;

        try
        {
            JSONObject jsonObj = new JSONObject(jsonString);
            MAIN_TITLE = jsonObj.getString("title");

            JSONArray articlesArray = jsonObj.getJSONArray("rows");
            for(int i=0; i<articlesArray.length(); i++)
            {
                JSONObject article = articlesArray.getJSONObject(i);
                String title = article.getString("title").trim();

                if(title.equalsIgnoreCase("null"))
                    continue;

                String description = article.getString("description").trim();
                String imageLocation = article.getString("imageHref");

                list.add(new DataContainer(title, description, imageLocation));
            }
            returnCode = 0;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return returnCode;
    }
}
