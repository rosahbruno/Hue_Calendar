/*
Author: Blair Altland, Bruno Rosa, Nate DeCriscio, Kyle Bargo
Date: 5/2/2016

	Handles the landing page of an event.
	It pulls data from the calendar and displays it to provide more information about the event.
	Allows a user the ability to edit the event .

 */

package com.nillawaffer.hue.calendar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TextView;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;

import java.util.List;
import java.util.Map;


public class EventDetailActivity extends AppCompatActivity {

    //Instantiate buttons and strings for later usage
    Button doneButton;
    String tag = "0";
    String id = "0";
    String name = "0";
    String startTime = "0";
    String startMin= "0";
    String endTime = "0";
    String endMin = "0";

    ImageButton imagebutton1;
    ImageButton imagebutton2;
    ImageButton imagebutton3;
    ImageButton imagebutton4;
    ImageButton imagebutton5;
    ImageButton imagebutton6;

    //Instantiate all of the labels
    TextView label1;
    TextView label2;
    TextView label3;
    TextView label4;
    TextView label5;
    TextView label6;

    private PHHueSDK phHueSDK;
    private static final int MAX_HUE=65535;
    public static final String TAG = "Hue Calendar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        addListenerOnButton();
        setTextOnLabels();
        phHueSDK = PHHueSDK.create();
        instantiateObjects();

        //Create the labels
        setLabelsBlank();
        changeDataLabels();
        //setLabelText();
        setLabelsFromBridge();

        setImageButtonContent();

        addListenerOnButton();
    }

    //set the labels with the correct text
    public void setTextOnLabels(){

        Bundle extras = getIntent().getExtras();

        name = extras.getString("pushName");
        startTime = extras.getString("pushStartTime");
        startMin = extras.getString("pushStartMin");
        if (Integer.parseInt(startMin) >= 10){
            startMin = String.valueOf(startMin);
        } else {
            startMin = "0" + String.valueOf(startMin);
        }

        endTime = extras.getString("pushEndTime");
        endMin = extras.getString("pushEndMin");
        if (Integer.parseInt(endMin) >= 10){
            endMin = String.valueOf(endMin);
        } else {
            endMin = "0" + String.valueOf(endMin);
        }

        tag = extras.getString("pushTag");
        id = extras.getString("pushID");

        TextView nameField = (TextView) findViewById(R.id.name);
        nameField.setText(name);

        TextView startTimeField = (TextView) findViewById(R.id.startTime);
        startTimeField.setText(startTime);
        TextView startMinField = (TextView) findViewById(R.id.startMin);
        startMinField.setText(startMin);



        TextView endTimeField = (TextView) findViewById(R.id.endTime);
        endTimeField.setText(endTime);
        TextView endMinField = (TextView) findViewById(R.id.endMin);
        endMinField.setText(endMin);

        TextView tagField = (TextView) findViewById(R.id.busy);
        tagField.setText(tag);

    }

    public void addListenerOnButton() {

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        doneButton = (Button) findViewById(R.id.editButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                delete(Integer.parseInt(id));

                Intent intent = new Intent(EventDetailActivity.this, BasicActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setLabelsFromBridge() {

        //create array of all of the bulbs on the network
        //loop through the array of bulbs and fill them with images

        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        try {
            PHLight light1 = allLights.get(0);
            String label1Text = light1.getName();
            label1.setText(label1Text);
        } catch (IndexOutOfBoundsException e) {
            label1.setText("");
        }

        try {
            PHLight light2 = allLights.get(1);
            String label2Text = light2.getName();
            label2.setText(label2Text);
        } catch (IndexOutOfBoundsException e) {
            label2.setText("");
        }

        try {
            PHLight light3 = allLights.get(2);
            String label3Text = light3.getName();
            label3.setText(label3Text);
        } catch (IndexOutOfBoundsException e) {
            label3.setText("");
        }

        try {
            PHLight light4 = allLights.get(3);
            String label4Text = light4.getName();
            label4.setText(label4Text);
        } catch (IndexOutOfBoundsException e) {
            label4.setText("");
        }

        try {
            PHLight light5 = allLights.get(4);
            String label5Text = light5.getName();
            label5.setText(label5Text);
        } catch (IndexOutOfBoundsException e) {
            label5.setText("");
        }

        try {
            PHLight light6 = allLights.get(5);
            String label6Text = light6.getName();
            label6.setText(label6Text);
        } catch (IndexOutOfBoundsException e) {
            label6.setText("");
        }
    }

    public void setLabelsBlank() {

        label1 = (TextView) findViewById(R.id.label1);
        label1.setText("");

        label2 = (TextView) findViewById(R.id.label2);
        label2.setText("");

        label3 = (TextView) findViewById(R.id.label3);
        label3.setText("");

        label4 = (TextView) findViewById(R.id.label4);
        label4.setText("");

        label5 = (TextView) findViewById(R.id.label5);
        label5.setText("");

        label6 = (TextView) findViewById(R.id.label6);
        label6.setText("");
    }

    public void setImageButtonContent() {

        if (label1.getText().length() < 1) {
            imagebutton1.setVisibility(View.INVISIBLE);
        }

        if (label2.getText().length() < 1) {
            imagebutton2.setVisibility(View.INVISIBLE);
        }
        if (label3.getText().length() < 1) {
            imagebutton3.setVisibility(View.INVISIBLE);
        }
        if (label4.getText().length() < 1) {
            imagebutton4.setVisibility(View.INVISIBLE);
        }
        if (label5.getText().length() < 1) {
            imagebutton5.setVisibility(View.INVISIBLE);
        }
        if (label6.getText().length() < 1) {
            imagebutton6.setVisibility(View.INVISIBLE);
        }
    }

    public void instantiateObjects() {

        imagebutton1 = (ImageButton) findViewById(R.id.imageButton);
        imagebutton2 = (ImageButton) findViewById(R.id.imageButton2);
        imagebutton3 = (ImageButton) findViewById(R.id.imageButton3);
        imagebutton4 = (ImageButton) findViewById(R.id.imageButton4);
        imagebutton5 = (ImageButton) findViewById(R.id.imageButton5);
        imagebutton6 = (ImageButton) findViewById(R.id.imageButton6);

        label1 = (TextView) findViewById(R.id.label1);
        label1.setText("");

        label2 = (TextView) findViewById(R.id.label2);
        label2.setText("");

        label3 = (TextView) findViewById(R.id.label3);
        label3.setText("");

        label4 = (TextView) findViewById(R.id.label4);
        label4.setText("");

        label5 = (TextView) findViewById(R.id.label5);
        label5.setText("");

        label6 = (TextView) findViewById(R.id.label6);
        label6.setText("");
    }

    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
        }

        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };

    public void delete(int id) {

        EventDB myDbHelper = new EventDB(getApplicationContext());
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        /*
        myDbHelper.deleteinformation(search_name,sqLiteDatabase);
        Toast.makeText(getApplication(),"Deleted Content!", Toast.LENGTH_LONG).show();
        */

        db.execSQL("DELETE FROM EVENTS WHERE _ID = " + id);
        db.close();

    }

    public void changeDataLabels(){

        //if the current hour is <12, subtract 12
        TextView startTimeField = (TextView) findViewById(R.id.startTime);
        TextView startAMPMField = (TextView) findViewById(R.id.startAMPM);
        int startHour = Integer.parseInt(startTimeField.getText().toString());
        startAMPMField.setText("AM");
        if(startHour > 12) {

            int finalStartHour = startHour - 12;

            startTimeField.setText(String.valueOf(finalStartHour));
            startAMPMField.setText("PM");
        }

        //if the current hour is <12, subtract 12
        TextView endAMPMField = (TextView) findViewById(R.id.endAMPM);
        TextView endTimeField = (TextView) findViewById(R.id.endTime);
        int endHour = Integer.parseInt(endTimeField.getText().toString());
        endAMPMField.setText("AM");
        if(endHour > 12) {

            int finalEndHour = endHour - 12;

            endTimeField.setText(String.valueOf(finalEndHour));
            endAMPMField.setText("PM");

        }

    }

}