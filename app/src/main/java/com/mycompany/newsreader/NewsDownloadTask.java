package com.mycompany.newsreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Vara on 4/3/2015.
 */
public class NewsDownloadTask extends AsyncTask<Void, Void, Integer> {

    ArrayList<DataContainer> list;
    CustomAdapter adapter;
    ProgressDialog progressDialog;
    private Context context;
    private boolean showProgress;

    public NewsDownloadTask(Context context, boolean showProgress)
    {
        this.context = context;
        list = new ArrayList<DataContainer>();
        this.showProgress = showProgress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(showProgress)
            progressDialog = ProgressDialog.show(context, "Pactera Interview Test", "Loading...");
        else
            Toast.makeText(context, "Refreshing the content", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Integer doInBackground(Void... params) {

        JsonDataProvider dataProvider = new JsonDataProvider(list);

        return Integer.valueOf(dataProvider.getDataFromCloud());
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        ActionBarActivity activity = (ActionBarActivity)context;
        if(showProgress)
        {
            progressDialog.dismiss();
            activity.setTitle(JsonDataProvider.MAIN_TITLE);
        }
        ListView listView = (ListView)activity.findViewById(R.id.news_list);
        adapter = new CustomAdapter(context, R.layout.custom_row_layout, list);
        listView.setAdapter(adapter);
    }
}
