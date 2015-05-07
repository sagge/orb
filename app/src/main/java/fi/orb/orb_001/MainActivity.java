package fi.orb.orb_001;

import java.lang.Math;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;


public class MainActivity extends Activity implements View.OnClickListener, LocationListener, SensorEventListener{

    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    private static final String PREF_MESSAGE = "message";
    private static final String HEIGHT = "height";
    SharedPreferences mSharedPreferences;

    public String own_id = "7";
    public String mess = "One fine orb over here";

    Location deviceLocation;
    String gender;
    Integer age;

    Integer orbs_in_list = 0;

    Activity activity = this;

    TextView mainTextView;
    TextView tvHeading;
    ImageButton optionsButton;
    ImageView orbButton;
    boolean debugDetails;
    ImageView orb_test;
    ImageView orb_test2;
    ImageView orb_test3;
    ImageView orb_test4;
    ImageView orb_test5;
    //ImageView orbasd;
    Orb orb1 = new Orb(60.1817256, 24.9272936, (float) 0.0, 21, "f", "65DD 165cm 53kg blond");
    Orb orb2 = new Orb(60.1825358, 24.9317418, (float) 0.0, 28, "m", "Cat loving pussydestroyer. Are you brave enough to find me? Red hair, blue jeans.");
    Orb orb3 = new Orb(60.2142283, 25.0467474, (float) 0.0, 18, "f", "Looking for dinner company :) Curly blond hair, beige jacket. Diehard escapist who loves books :) xoxo");
    Orb orb4 = new Orb(60.1899868, 24.8312963, (float) 0.0, 23, "f", "Nice conversations and maybe something else.. ;) Cat printed on my shirt and booty that you will notice ;)");
    Orb orb5 = new Orb(60.1715503, 24.9203406, (float) 0.0, 23, "f", "Nice conversations and maybe something else.. ;) Cat printed on my shirt and booty that you will notice ;)");


    ArrayList<Orb> orbs = new ArrayList();
    Orb orb_dummy = new Orb(86.5998692, 25.5093464, (float) 0.0, 21, "f", "dummy dummy");




    private TextView distanceField;
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;

    // define the display assembly compass picture
    private ImageView image;

    private RelativeLayout rotation_canvas;

    // record the compass picture angle turned
    private float compassCurrentDegree = 0f;
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    public class Orb{
        Location location;
        String id;
        float bearing;
        int age;
        String gender;
        String message;
        ImageView view;

        public Orb (double latitude, double longitude, float bearing, int age, String gender, String message){
            Location coordinates = new Location("dummyprovider");
            coordinates.setLatitude(latitude);
            coordinates.setLongitude(longitude);
            this.location = coordinates;
            this.bearing = bearing;
            this.age = age;
            this.gender = gender;
            this.message = message;
        }

        public int getAge(){
            return age;
        }

        public Location getLocation(){
            return location;
        }

        public void setLocation(double latitude, double longitude) {
            Location coordinates = new Location("dummyprovider");
            coordinates.setLatitude(latitude);
            coordinates.setLongitude(longitude);
            this.location = coordinates;
        }

        public String getGender(){
            return gender;
        }

        public String getMessage(){
            return message;
        }

        public float getBearing(){
            return bearing;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setBearing(float bearing) {
            this.bearing = bearing;
        }

        public void setView(ImageView view) {
            this.view = view;
        }
    }

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://orb.dy.fi/location";
    private static String url_put = "http://orb.dy.fi/user/";


    // JSON Node names
    private static final String TAG_ORBS = "json_list";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "nickname";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LON = "lon";
    private static final String TAG_NOTE = "note";

    // contacts JSONArray
    // JSONArray contacts = null;

    // Hashmap for ListView
    // ArrayList<HashMap<String, String>> contactList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        debugDetails = false;


        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String userName = mSharedPreferences.getString(PREF_NAME, "");


        //contactList = new ArrayList<HashMap<String, String>>();


        final ImageButton GetServerData = (ImageButton) findViewById(R.id.GetServerData);
        // On button click call this listener
        GetServerData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new GetContacts().execute();
                //Toast.makeText(getApplicationContext(), Integer.toString(orbs_in_list), Toast.LENGTH_LONG).show();

            }
        });


/*

        final ImageButton GetServerData = (ImageButton) findViewById(R.id.GetServerData);
        // On button click call this listener
        GetServerData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getBaseContext(),
                        "Please wait, connecting to server.",
                        Toast.LENGTH_SHORT).show();


                // Create Inner Thread Class
                Thread background = new Thread(new Runnable() {

                    private final HttpClient Client = new DefaultHttpClient();
                    private String URL = "http://androidexample.com/media/webservice/getPage.php";

                    // After call for background.start this run method call
                    public void run() {
                        try {

                            String SetServerString = "";
                            HttpGet httpget = new HttpGet(URL);
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            SetServerString = Client.execute(httpget, responseHandler);
                            threadMsg(SetServerString);

                        } catch (Throwable t) {
                            // just end the background thread
                            Log.i("Animation", "Thread  exception " + t);
                        }
                    }

                    private void threadMsg(String msg) {

                        if (!msg.equals(null) && !msg.equals("")) {
                            Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message", msg);
                            msgObj.setData(b);
                            handler.sendMessage(msgObj);
                        }
                    }

                    // Define the Handler that receives messages from the thread and update the progress
                    private final Handler handler = new Handler() {

                        public void handleMessage(Message msg) {

                            String aResponse = msg.getData().getString("message");

                            if ((null != aResponse)) {

                                // ALERT MESSAGE
                                Toast.makeText(
                                        getBaseContext(),
                                        "Server Response: "+aResponse,
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                                // ALERT MESSAGE
                                Toast.makeText(
                                        getBaseContext(),
                                        "Not Got Response From Server.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    };

                });
                // Start Thread
                background.start();  //After call start method thread called run Method
            }
        });



*/






        mainTextView = (TextView) findViewById(R.id.fullscreen_content);
        mainTextView.setText(userName);

        rotation_canvas = (RelativeLayout) findViewById(R.id.rl);

        // Access the ImageButton defined in layout XML
        // and listen for it here
        optionsButton = (ImageButton) findViewById(R.id.options);
        optionsButton.setOnClickListener(this);

        orbButton = (ImageView) findViewById(R.id.logo);
        orbButton.setOnClickListener(this);
        //orbButton.setId(2);

        distanceField = (TextView) findViewById(R.id.Distance2);
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        //for dummy location while getting the actual location
        Location coordinates = new Location("dummyprovider");
        coordinates.setLatitude(60.181175);
        coordinates.setLongitude(24.883926);
        deviceLocation = coordinates;

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
        }


        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Read the user's name,
        // or an empty string if nothing found
        int height = mSharedPreferences.getInt(HEIGHT, 0);
        rotation_canvas.getLayoutParams().width = height;
        Toast.makeText(getApplicationContext(), "Welcome, " + height + "!", Toast.LENGTH_LONG).show();
        rotation_canvas.requestLayout();
        orb_test = new ImageView(this);
        orb_test.setImageResource(R.drawable.orbpallo);
        orb_test.setId(R.id.orb_t);

        orb_test2 = new ImageView(this);
        orb_test2.setImageResource(R.drawable.orbpallo);
        orb_test2.setId(R.id.orb_t2);

        orb_test3 = new ImageView(this);
        orb_test3.setImageResource(R.drawable.orbpallo);
        orb_test3.setId(R.id.orb_t3);

        orb_test4 = new ImageView(this);
        orb_test4.setImageResource(R.drawable.orbpallo);
        orb_test4.setId(R.id.orb_t4);

        orb_test5 = new ImageView(this);
        orb_test5.setImageResource(R.drawable.orbpallo);
        orb_test5.setId(R.id.orb_t5);

        rotation_canvas.addView(orb_test);
        rotation_canvas.addView(orb_test2);
        rotation_canvas.addView(orb_test3);
        rotation_canvas.addView(orb_test4);
        rotation_canvas.addView(orb_test5);

        orb_test.setOnClickListener(this);
        orb_test2.setOnClickListener(this);
        orb_test3.setOnClickListener(this);
        orb_test4.setOnClickListener(this);
        orb_test5.setOnClickListener(this);


        // Greet the user, or ask for their name if new
        displayWelcome();

        //initializeRotationCanvas();
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        // contacts JSONArray
        JSONArray contacts = null;

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*
            if (contactList.size() != orbs_in_list) {
                //Remove old orbs
                for (Orb orbi : orbs) {
                    orbi.view.setVisibility(View.GONE);
                }
                orbs.clear();
            }
            */
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_ORBS);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String nickname = c.getString(TAG_NAME);
                        String lat = c.getString(TAG_LAT);
                        String lon = c.getString(TAG_LON);
                        String note = c.getString(TAG_NOTE);


                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, nickname);
                        contact.put(TAG_LAT, lat);
                        contact.put(TAG_LON, lon);
                        contact.put(TAG_NOTE, note);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


            //Toast.makeText(getApplicationContext(), "Welcome, " + contactList.get(0).get(TAG_ID) + "!", Toast.LENGTH_LONG).show();

            //Orbs from json list

            if (contactList.size() != orbs_in_list) {

                //Remove old orbs
                for (Orb orbi : orbs) {
                    orbi.view.setVisibility(View.GONE);
                }
                orbs.clear();

                for (HashMap<String, String> orbi : contactList) {
                    final Orb orb_jas = new Orb(Double.parseDouble(orbi.get(TAG_LAT)), Double.parseDouble(orbi.get(TAG_LON)), (float) 0.0, 21, orbi.get(TAG_NAME), orbi.get(TAG_NOTE));
                    orb_jas.setId(orbi.get(TAG_ID));

                    ImageView temp = new ImageView(activity);
                    temp.setImageResource(R.drawable.orbpallo);
                    orb_jas.setView(temp);

                    orb_jas.setBearing(deviceLocation.bearingTo(orb_jas.getLocation()));
                    rotation_canvas.addView(orb_jas.view); //Päivitetään orb olion view
                    if (orbi.get(TAG_ID).equals(own_id)){
                        orb_jas.view.setVisibility(View.GONE);
                    }
                    orbs.add(orb_jas);


                    orb_jas.view.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            displayOrbInfo(orb_jas);
                        }
                    });
                }
                orbs_in_list = orbs.size();
            }
            else {
                for (int i = 0; i < orbs.size(); i++) {
                    orbs.get(i).setLocation(Double.parseDouble(contactList.get(i).get(TAG_LAT)), Double.parseDouble(contactList.get(i).get(TAG_LON)));
                    orbs.get(i).message = contactList.get(i).get(TAG_NOTE);
                }
            }

            Toast.makeText(getApplicationContext(), Integer.toString(orbs_in_list) + " ; " + Integer.toString(contactList.size()), Toast.LENGTH_LONG).show();

        }

    }

    private class UpdateMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String put_url = url_put + own_id;

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("note", mess.toString()));
            String jsonStr = sh.makeServiceCall(put_url, ServiceHandler.PUT, params);

            Log.d("Response: ", "> " + jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

    }

    private class AddUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("lat", Double.toString(deviceLocation.getLatitude())));
            params.add(new BasicNameValuePair("lon", Double.toString(deviceLocation.getLongitude())));
            params.add(new BasicNameValuePair("name", gender.toString()));
            params.add(new BasicNameValuePair("note", mess.toString()));
            String jsonStr = sh.makeServiceCall(url_put, ServiceHandler.POST, params);

            Log.d("Response: ", "> " + jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

    }

    private class UpdateLocationToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String put_url = url_put + own_id;

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("lat", Double.toString(deviceLocation.getLatitude())));
            params.add(new BasicNameValuePair("lon", Double.toString(deviceLocation.getLongitude())));
            String jsonStr = sh.makeServiceCall(put_url, ServiceHandler.PUT, params);

            Log.d("Response: ", "> " + jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            displayDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayWelcome() {
        // Access the device's key-value storage
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        // Read the user's name,
        // or an empty string if nothing found
        String name = mSharedPreferences.getString(PREF_NAME, "");

        /*if (name.length() > 0) {
            // If the name is valid, display a Toast welcoming them
            Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_LONG).show();
        }
        */

        if (name.length() == 0) {

            //Age dialog
            RelativeLayout linearLayout = new RelativeLayout(this);
            final NumberPicker aNumberPicker = new NumberPicker(this);
            aNumberPicker.setMaxValue(120);
            aNumberPicker.setMinValue(1);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
            RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            linearLayout.setLayoutParams(params);
            linearLayout.addView(aNumberPicker,numPicerParams);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Set your age");
            alertDialogBuilder.setView(linearLayout);
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Log.e("","Age set to: "+ aNumberPicker.getValue());
                                    age = aNumberPicker.getValue();

                                    // POST new user
                                    new AddUser().execute();


                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog ageDialog = alertDialogBuilder.create();


            // Gender dialog
            final AlertDialog.Builder choose_gender = new AlertDialog.Builder(this, 2);
            choose_gender.setTitle("Choose your gender");

            // Make an "OK" button to save the name
            choose_gender.setPositiveButton("Male", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    gender = "m";
                    ageDialog.show();

                    // OWN id
                    own_id = Integer.toString(orbs.size()+1);
                }
            });

            // Make a "Cancel" button
            // that simply dismisses the alert
            choose_gender.setNegativeButton("Female", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    gender = "f";
                    ageDialog.show();

                    // OWN id
                    own_id = Integer.toString(orbs.size()+1);
                }
            });


            // Name dialog
            // Show a dialog to ask for their name
            AlertDialog.Builder alert = new AlertDialog.Builder(this, 2);

            alert.setTitle("Hello!");
            alert.setMessage("What is your name?");

            // Create EditText for entry
            final EditText input = new EditText(this);
            alert.setView(input);


            // Make an "OK" button to save the name
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    // Grab the EditText's input
                    String inputName = input.getText().toString();

                    // Put it into memory (don't forget to commit!)
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.apply();
                    mainTextView.setText("\n" + inputName);

                    // Welcome the new user
                    Toast.makeText(getApplicationContext(), "Welcome, " + inputName + "!", Toast.LENGTH_LONG).show();
                    choose_gender.show();

                    // GET contacts for the first time
                    new GetContacts().execute();

                }
            });

            // Make a "Cancel" button
            // that simply dismisses the alert
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {}
            });

            alert.show();

        }
    }

    //Display dialog to change name
    public void displayDialog() {
        // Access the device's key-value storage
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        // Read the user's name,
        // or an empty string if nothing found
        String message = mSharedPreferences.getString(PREF_MESSAGE, "");

        AlertDialog.Builder alert = new AlertDialog.Builder(this, 2);
        alert.setTitle("Change your message?");
        alert.setMessage("Current message: " + message);

        // Create EditText for entry
        final EditText input = new EditText(this);
        alert.setView(input);

        // Make an "OK" button to save the name
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                // Grab the EditText's input
                String message = input.getText().toString();
                mess = message;

                // Put it into memory (don't forget to commit!)
                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.putString(PREF_MESSAGE, message);
                e.apply();
                new UpdateMessage().execute();
            }
        });

        // Make a "Cancel" button
        // that simply dismisses the alert
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        alert.show();
    }

    public void displayOrbInfo(Orb orb) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, 2);

        if (orb.getGender().equals("f")) {
            alert.setTitle("Female, " + orb.getAge());
        }
        else {
            alert.setTitle("Male, " + orb.getAge());
        }
        alert.setMessage(orb.getMessage());

        // Dummy OK button
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        // Make a "Cancel" button
        // that simply dismisses the alert
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        alert.show();
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(getApplicationContext(),"id" + v.getId(), Toast.LENGTH_LONG).show();
        //debug messages and structure to determine, which button is clicked

        switch (v.getId()){
            case R.id.options:
                startAnimation(v);
                displayDialog();
                Toast.makeText(getApplicationContext(), "Options clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.logo:
                //initializeRotationCanvas();
                if (debugDetails){
                    debugDetails = false;
                    findViewById(R.id.linearLayout0).setVisibility(View.INVISIBLE);
                    // Not working for some reason
                    // findViewById(R.id.imageViewCompass).setVisibility(View.INVISIBLE);
                }
                else{
                    debugDetails = true;
                    findViewById(R.id.linearLayout0).setVisibility(View.VISIBLE);
                    new UpdateLocationToServer().execute();
                    // Not working for some reason
                    // findViewById(R.id.imageViewCompass).setVisibility(View.VISIBLE);
                }
                break;
            case R.id.orb_t:
                displayOrbInfo(orb1);
                break;
            case R.id.orb_t2:
                displayOrbInfo(orb2);
                break;
            case R.id.orb_t3:
                displayOrbInfo(orb3);
                break;
            case R.id.orb_t4:
                displayOrbInfo(orb4);
                break;
            case R.id.orb_t5:
                displayOrbInfo(orb5);
                break;
        }
    }

    public void startAnimation(View v) {
        switch (v.getId()) {
            case R.id.options:
                RotateAnimation optAnim = new RotateAnimation(
                        (float) 0.0,
                        (float) 180.0,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                optAnim.setDuration(600);
                // set the animation after the end of the reservation status
                optAnim.setFillAfter(true);
                optionsButton.startAnimation(optAnim);
                break;
        }
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        deviceLocation = location;
        float lat = (float) (location.getLatitude());
        float lng = (float) (location.getLongitude());
        float dist = location.distanceTo(orb1.getLocation());

        orb1.bearing = location.bearingTo(orb1.getLocation());
        orb2.bearing = location.bearingTo(orb2.getLocation());
        orb3.bearing = location.bearingTo(orb3.getLocation());
        orb4.bearing = location.bearingTo(orb4.getLocation());
        orb5.bearing = location.bearingTo(orb5.getLocation());

        if (orbs.size() != 0) {
            for (Orb orbi : orbs) {
                orbi.setBearing(location.bearingTo(orbi.getLocation()));
            }
        }

        distanceField.setText(String.valueOf(dist));
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        tvHeading.setText("Heading: " + Float.toString(degree) + " " + orb1.getBearing() + " degrees");
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                compassCurrentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        float leveys = ((float) rotation_canvas.getWidth()/2) - ((float) orb_test.getWidth()/2);
        float korkeus = ((float) rotation_canvas.getHeight()/2) - ((float) orb_test.getHeight()/2);

        float orb1Distance = deviceLocation.distanceTo(orb1.getLocation());
        float orb2Distance = deviceLocation.distanceTo(orb2.getLocation());
        float orb3Distance = deviceLocation.distanceTo(orb3.getLocation());
        float orb4Distance = deviceLocation.distanceTo(orb4.getLocation());
        float orb5Distance = deviceLocation.distanceTo(orb5.getLocation());


        //list

        if (orbs.size() != 0) {
            for (Orb orbi : orbs) {
                float orbDistance = deviceLocation.distanceTo(orbi.getLocation());

                //orbi.view.setTranslationX(400);
                //orbi.view.setTranslationY(400);

                if (orbDistance > 150) {
                    orbi.view.setTranslationX(0);
                    orbi.view.setTranslationY(0);
                } else {
                    orbi.view.setTranslationX(leveys + (leveys * (float) Math.sin(Math.toRadians(orbi.getBearing() - currentDegree))) * (orbDistance / 150));
                    orbi.view.setTranslationY(korkeus - (korkeus * (float) Math.cos(Math.toRadians(orbi.getBearing() - currentDegree))) * (orbDistance / 150));
                }

            }
        }


        //constants

        if (orb1Distance > 150){
            orb_test.setTranslationX(0);
            orb_test.setTranslationY(0);
        }
        else {
            orb_test.setTranslationX(leveys + (leveys * (float) Math.sin(Math.toRadians(orb1.getBearing() - currentDegree)))*(orb1Distance/150));
            orb_test.setTranslationY(korkeus - (korkeus * (float) Math.cos(Math.toRadians(orb1.getBearing() - currentDegree)))*(orb1Distance/150));
        }

        if (orb2Distance > 150){
            orb_test2.setTranslationX(0);
            orb_test2.setTranslationY(0);
        }
        else {
            orb_test2.setTranslationX(leveys + (leveys * (float) Math.sin(Math.toRadians(orb2.getBearing() - currentDegree)))*(orb2Distance/150));
            orb_test2.setTranslationY(korkeus - (korkeus * (float) Math.cos(Math.toRadians(orb2.getBearing() - currentDegree)))*(orb2Distance/150));
        }

        if (orb3Distance > 150){
            orb_test3.setTranslationX(0);
            orb_test3.setTranslationY(0);
        }
        else {
            orb_test3.setTranslationX(leveys + (leveys * (float) Math.sin(Math.toRadians(orb3.getBearing() - currentDegree)))*(orb3Distance/150));
            orb_test3.setTranslationY(korkeus - (korkeus * (float) Math.cos(Math.toRadians(orb3.getBearing() - currentDegree)))*(orb3Distance/150));
        }

        if (orb4Distance > 150){
            orb_test4.setTranslationX(0);
            orb_test4.setTranslationY(0);
        }
        else {
            orb_test4.setTranslationX(leveys + (leveys * (float) Math.sin(Math.toRadians(orb4.getBearing() - currentDegree)))*(orb4Distance/150));
            orb_test4.setTranslationY(korkeus - (korkeus * (float) Math.cos(Math.toRadians(orb4.getBearing() - currentDegree)))*(orb4Distance/150));
        }

        if (orb5Distance > 150){
            orb_test5.setTranslationX(0);
            orb_test5.setTranslationY(0);
        }
        else {
            orb_test5.setTranslationX(leveys + (leveys * (float) Math.sin(Math.toRadians(orb5.getBearing() - currentDegree)))*(orb5Distance/150));
            orb_test5.setTranslationY(korkeus - (korkeus * (float) Math.cos(Math.toRadians(orb5.getBearing() - currentDegree)))*(orb5Distance/150));
        }

        image.startAnimation(ra);
        currentDegree = degree;
        compassCurrentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}