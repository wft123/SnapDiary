package org.roxomi.roxy.snapdiary.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import org.roxomi.roxy.snapdiary.model.Photo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Roxy on 2017. 4. 19..
 */

public class PhotoInfo {

    private Context context;

    public PhotoInfo(Context context) {
        this.context = context;
    }

    private ArrayList<String> getPathOfAllImages()
    {
        ArrayList<String> result = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

        int lastIndex;
        while (cursor.moveToNext())
        {
            String absolutePathOfImage = cursor.getString(columnIndex);
            String nameOfFile = cursor.getString(columnDisplayname);
            lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
            lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;

            if (!TextUtils.isEmpty(absolutePathOfImage))
            {
                result.add(absolutePathOfImage);
            }
        }

//        while (cursor.moveToNext())
//        {
//            Log.i("getPathOfAllImages", "PATH:" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)) + "|");
//            Log.i("getPathOfAllImages", "DATE:" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)) + "|");
//            Log.i("getPathOfAllImages", "LATITUDE:" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE)) + "|");
//            Log.i("getPathOfAllImages", "LONGITUDE:" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE)) + "|");
//            Log.i("getPathOfAllImages", "--------------------------------------------------------");
//
//        }

        return result;
    }

    public ArrayList<Integer> getDays(int year, int month){
        ArrayList<Integer> result = new ArrayList<>();
        long fromDate, toDate;
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-2, 1,0,0,0);
        fromDate = calendar.getTimeInMillis();
        calendar.set(year, month-1, 1,0,0,0);
        toDate = calendar.getTimeInMillis();

        String selection = MediaStore.Images.ImageColumns.DATE_TAKEN + " >= "+ fromDate
                + " AND " + MediaStore.Images.ImageColumns.DATE_TAKEN + " < "+ toDate ;

        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);

        SimpleDateFormat df = new SimpleDateFormat("dd");

        while (cursor.moveToNext())
        {
            int date = Integer.parseInt(df.format(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN))));
            if(!result.contains(date)) result.add(date);
        }

        return result;
    }

    public ArrayList<Photo> getPathOfDays(int year, int month, int date){
        ArrayList<Photo> result = new ArrayList<>();

        long fromDate, toDate;
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, 0,0,0);
        fromDate = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, 1);
        toDate = calendar.getTimeInMillis();

        String selection = MediaStore.Images.ImageColumns.DATE_TAKEN + " >= "+ fromDate
                + " AND " + MediaStore.Images.ImageColumns.DATE_TAKEN + " < "+ toDate ;

        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        int lastIndex;

        while (cursor.moveToNext())
        {
            Photo photo = new Photo();
            photo.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)));
            photo.setDate(df.format(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN))));
            photo.setLatitude(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE)));
            photo.setLongitude(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE)));

            String absolutePathOfImage = cursor.getString(columnIndex);
            String nameOfFile = cursor.getString(columnDisplayname);
            lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
            lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;

            if (!TextUtils.isEmpty(absolutePathOfImage))
            {
                photo.setPath(absolutePathOfImage);
                result.add(photo);
            }
        }

        return result;
    }

    public ArrayList<Photo> getPathOfDays(long millis){
        ArrayList<Photo> result = new ArrayList<>();

        long fromDate, toDate;
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        fromDate = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, 1);
        toDate = calendar.getTimeInMillis();

        String selection = MediaStore.Images.ImageColumns.DATE_ADDED + " >= "+ fromDate
                + " AND " + MediaStore.Images.ImageColumns.DATE_ADDED + " < "+ toDate ;

        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        int lastIndex;

        while (cursor.moveToNext())
        {
            Photo photo = new Photo();
            photo.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)));
            photo.setDate(df.format(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED))));
            photo.setLatitude(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE)));
            photo.setLongitude(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE)));

            String absolutePathOfImage = cursor.getString(columnIndex);
            String nameOfFile = cursor.getString(columnDisplayname);
            lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
            lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;

            if (!TextUtils.isEmpty(absolutePathOfImage))
            {
                photo.setPath(absolutePathOfImage);
                result.add(photo);
            }
        }

        return result;
    }

}
