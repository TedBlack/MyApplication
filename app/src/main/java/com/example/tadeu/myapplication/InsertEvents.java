package com.example.tadeu.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.property.Method;

import java.io.FileInputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Iterator;

/**
 * Created by Tadeu on 07/09/2016.
 */
public class InsertEvents {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    public static void insertEvents(Context context) throws Exception {
        String website = "https://calendar.google.com/calendar/ical/ijuh72fq0otnvo5edesmj72" +
                "fag%40group.calendar.google.com/public/basic.ics";

        DownloadFile dlFile = new DownloadFile(context);

        dlFile.doInBackground(website);


        FileInputStream calendarFile = new FileInputStream(context.getFilesDir() + "calendar.ics");
        CalendarBuilder builder = new CalendarBuilder();
        net.fortuna.ical4j.model.Calendar calendar = builder.build(calendarFile);

        TimeZoneRegistry registry = builder.getRegistry();
        TimeZone timeZone = registry.getTimeZone("Europe/Lisbon");

        ContentValues event = new ContentValues();
        ContentResolver cr = context.getContentResolver();

        for (Iterator events = calendar.getComponents().iterator(); events.hasNext(); ) {

            Component component = (Component) events.next();

            Long start = SDF.parse(component.getProperty("DTSTART").getValue()).getTime();
            Long end = SDF.parse(component.getProperty("DTEND").getValue()).getTime();
            String summary = component.getProperty("SUMMARY").getValue();
            String description = component.getProperty("DESCRIPTION").getValue();
            String UID = component.getProperty("UID").getValue();

            event.put("calendar_id", 1);
            event.put("title", summary);
            event.put("dtstart", start);
            event.put("dtend", end);
            event.put("description", description);
            event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            event.put(CalendarContract.Events.UID_2445, UID);
            event.put(CalendarContract.Events.ALL_DAY, true);
            event.put(CalendarContract.Events.HAS_ALARM, true);
            Uri uri;

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            if (verifyEvent(UID, context)) {
                //cr.insert(Uri.parse("content://com.android.calendar/events"), event);
                uri = cr.insert(CalendarContract.Events.CONTENT_URI, event);
            }
            else {
                uri = cr.insert(CalendarContract.Events.CONTENT_URI, event);
                //cr.insert(Uri.parse("content://com.android.calendar/events"), event);
            }

            long eventId = Long.parseLong(uri.getLastPathSegment());

            ContentValues reminders = new ContentValues();
            reminders.put(CalendarContract.Reminders.EVENT_ID, eventId);
            reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            reminders.put(CalendarContract.Reminders.MINUTES,5760-60*12);

            cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);
        }


    }

    protected static boolean verifyCon(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni!=null && ni.isConnectedOrConnecting();
    }

    private static boolean verifyEvent(String eventID, Context context){
        Uri uri = Uri.parse("content://com.android.calendar/events");
        Cursor cursor = context.getContentResolver().query( uri, null, null, null, null);
        cursor.moveToFirst();
        String uid;
        int id;
        for(int myCursor = 0; myCursor< cursor.getCount(); myCursor++) {
            uid = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.UID_2445));
            id = cursor.getInt(cursor.getColumnIndex(CalendarContract.Events._ID));
            if (uid!=null && uid.equals(eventID)) {
                Uri deleteUri = ContentUris.withAppendedId(uri, id);
                context.getContentResolver().delete(deleteUri, null, null);
                cursor.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return false;
    }

}
