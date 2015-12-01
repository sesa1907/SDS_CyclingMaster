package innoworld.cyclingmaster;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.round;

public class Record extends AppCompatActivity {
    Button btnShowLocation;

    GPSTracker gps;

    TextView textviewCurrentLocation;

    Boolean recording = false;

    Button btnRecord;

    ArrayList<GPSPoint> record = new ArrayList<GPSPoint>();

    Thread recordthread;

    Double recordDistance = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        btnShowLocation = (Button) findViewById(R.id.show_location);
        btnRecord = (Button) findViewById(R.id.record_startstop);
        textviewCurrentLocation = (TextView) findViewById(R.id.textview_CurrentLocation);

        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gps = new GPSTracker(Record.this);

                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    double altitude = gps.getAltitude();

                    String locationText =
                                    "Your location is:" +
                                    "\nLatitude: " + latitude +
                                    "\nLongitude: " + longitude +
                                    "\nAltitude: " + altitude +
                                    "\nTime: " + getCurrentTimeStamp();

                    textviewCurrentLocation.setText(locationText);
                } else {
                    gps.showSettingsAlert();
                }
            }
        });

        Runnable runnable = new Runnable() {
            public void run() {
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {*/
                        while (true) {
                            if(recording) {
                                addGPSPoint();
                                int size = record.size();
                                if(size>1) {
                                    recordDistance += distance(record.get(size-2).getLattitude()
                                            ,record.get(size-2).getLongitude()
                                            ,record.get(size-1).getLattitude()
                                            ,record.get(size-1).getLongitude());
                                }
                            }
                            SystemClock.sleep(1000);
                        }
                    /*}
                });*/
            }
        };
        recordthread = new Thread(runnable);
        recordthread.start();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER )) {
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
            startActivity(myIntent);
        }
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat dmyhms = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String strDate = dmyhms.format(date);
        return strDate;
    }

    public void recordStartStop(View v) {
        if(recording) {
            recording = false;

            SimpleDateFormat dmyhms = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            saveRecord();

            record = new ArrayList<GPSPoint>();

            btnRecord.setText("Start recording");
        } else {
            recording = true;
            btnRecord.setText("Stop recording");
        }
    }

    private void addGPSPoint() {
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            double altitude = gps.getAltitude();
            Date date = new Date();

            GPSPoint gpspoint = new GPSPoint(latitude, longitude, altitude, date);

            record.add(gpspoint);
        } else {
            gps.showSettingsAlert();
        }
    }

    private void saveRecord() {
        SimpleDateFormat dmyhms = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String beginTime = dmyhms.format(record.get(0).getDate());
        String endTime = dmyhms.format(record.get(record.size()-1).getDate());

        String filename = beginTime + ".gpx";
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "data" + File.separator + "innoworld.cyclingmaster" + File.separator + filename);

        String fileContent = getFileContent();

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(fileContent.getBytes());
            out.close();

            String text = "File saved at " + file.getAbsolutePath() + "\n" +
                          "for a ride between " + beginTime + " and " + endTime + "\n" +
                          "with a distance driven of " + round(recordDistance, 2) + "km";
            textviewCurrentLocation.setText(text);
        }
        catch (Exception e) {
            Toast.makeText(this, "Saving the file failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String getFileContent() {
        String fileContent = "";
        String beginTime = getTime(record.get(0).getDate());

        fileContent +=
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<gpx creator=\"Cycling Master Android\" version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd\" xmlns:gpxtpx=\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1\" xmlns:gpxx=\"http://www.garmin.com/xmlschemas/GpxExtensions/v3\">\n" +
                "\t<metadata>\n" +
                "\t\t<time>" + beginTime + "</time>\n" +
                "\t</metadata>\n" +
                "\t<trk>\n" +
                "\t\t<name>Cycling Master @ " + beginTime + "</name>\n" +
                "\t\t<trkseg>\n";

        for(GPSPoint gpspoint : record) {
            fileContent += "\t\t\t<trkpt lat=\""+gpspoint.getLattitude() + "\" lon=\""+gpspoint.getLongitude()+"\">\n";
            fileContent += "\t\t\t\t<ele>" + gpspoint.getElevation() + "</ele>\n";
            fileContent += "\t\t\t\t<time>" + getTime(gpspoint.getDate()) + "</time>\n";
            fileContent += "\t\t\t</trkpt>\n";
        }
        fileContent +=
                "\t\t</trkseg>\n" +
                "\t</trk>\n" +
                "</gpx>";

        return fileContent;
    }

    private String getTime(Date date) {
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");

        return ymd.format(date) + "T" + hms.format(date) + "Z";
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0; // kilometers (or 6371.0 3958.75)
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
