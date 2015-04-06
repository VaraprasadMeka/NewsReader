package com.mycompany.newsreader;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vara on 4/3/2015.
 */
public class CustomAdapter extends ArrayAdapter<DataContainer> {

    private Context context;
    private int resource;
    private List<DataContainer> newsList;
    private int descriptionView_margin;

    public CustomAdapter(Context context, int resource, List<DataContainer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.newsList = objects;
        descriptionView_margin = convertDPtoPixels(100); // converting dp to px
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        ViewHolder viewHolder;

        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView)row.findViewById(R.id.news_title);
            viewHolder.descriptionView = (TextView)row.findViewById(R.id.news_description);
            viewHolder.imageView = (ImageView)row.findViewById(R.id.news_thumbnail);
            row.setTag(viewHolder);
        }
        else
        {
            row = convertView;
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.titleView.setText(newsList.get(position).title);
        if(!newsList.get(position).description.equalsIgnoreCase("null"))
            viewHolder.descriptionView.setText(newsList.get(position).description);
        else
            viewHolder.descriptionView.setText("");

        viewHolder.imageView.setTag(newsList.get(position).imageLocation);

        if(!newsList.get(position).imageLocation.equalsIgnoreCase("null"))
        {
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setImageBitmap(
                    BitmapFactory.decodeResource(context.getResources(),R.drawable.loading_image));

            ViewGroup.MarginLayoutParams params =
                    (ViewGroup.MarginLayoutParams)viewHolder.descriptionView.getLayoutParams();
            params.setMarginEnd(descriptionView_margin);

            //new ImageDownloadTask(viewHolder.imageView).execute(
            //        newsList.get(position).imageLocation);
            new ImageDownloadTask(context,viewHolder.imageView).executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR, newsList.get(position).imageLocation);
        }
        else
        {
            ViewGroup.MarginLayoutParams params =
                    (ViewGroup.MarginLayoutParams)viewHolder.descriptionView.getLayoutParams();
            params.setMarginEnd(0);

            viewHolder.imageView.setVisibility(View.GONE);
        }

        return row;
    }

    private static class ViewHolder
    {
        TextView titleView;
        TextView descriptionView;
        ImageView imageView;
    }

    private int convertDPtoPixels(int dp)
    {
        ActionBarActivity activity = (ActionBarActivity)context;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float density = metrics.density;
        int pixels = (int)Math.ceil(dp * density);

        return pixels;
    }
}
