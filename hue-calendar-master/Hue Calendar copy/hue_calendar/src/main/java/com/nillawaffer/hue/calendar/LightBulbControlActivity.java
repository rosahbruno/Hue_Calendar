/*
Author: Blair Altland, Bruno Rosa, Nate DeCriscio, Kyle Bargo
Date: 5/2/2016

	This activity allows for direct interaction with a specific bulb;
	if one wants to turn manually change the color brightness or another attribute of a bulb,
	this page allows for that.


 */

package com.nillawaffer.hue.calendar;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import org.w3c.dom.Text;

public class LightBulbControlActivity extends AppCompatActivity {

    private View root;
    private int currentBackgroundColor = 0xff76E6E2;
    private PHHueSDK phHueSDK;
    private static final int MAX_HUE=65535;
    public static final String TAG = "Hue Calendar";
    SeekBar seekBar;
    Button doneButton;
    //String bulbName = "Hue bloom 1";
    Switch onOffSwitch;

    String bulbName;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_bulb_detail);
        root = findViewById(R.id.color_screen);

        phHueSDK = PHHueSDK.create();

        setLabels();

        //Seek bar code
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(254);

        ImageButton bulbButton;
        bulbButton = (ImageButton) findViewById(R.id.image);

        onOffSwitch = (Switch) findViewById(R.id.switch1);

        //Set up code
        changeNameLabel(bulbName);
        setInitialBrightness();
        changeBackgroundColor(currentBackgroundColor);
        setSwitchPosition();
        
        addListenerOnButton();
        //On off switch code

        bulbButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setLightStatus(bulbName);
            }

        });


        onOffSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                setLightStatus(bulbName);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

                Toast.makeText(LightBulbControlActivity.this, "seek bar progress:" + progressChanged,
                        Toast.LENGTH_SHORT).show();

                PHBridge bridge = phHueSDK.getSelectedBridge();

                List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                for (PHLight light : allLights) {
                    PHLightState lightState = new PHLightState();

                    if (light.getName().contains(bulbName)) {

                        lightState.setBrightness(progressChanged);

                        bridge.updateLightState(light, lightState, listener);
                    }
                }

            }
        });

        findViewById(R.id.colorWheel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = LightBulbControlActivity.this;

                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("Choose color")
                        .initialColor(currentBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(7)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {

                                String newColor = Integer.toHexString(selectedColor);
                                String slimColor = newColor.substring(2);

                                int colorN = (int)Long.parseLong(slimColor, 16);
                                int r = (colorN >> 16) & 0xFF;
                                int g = (colorN >> 8) & 0xFF;
                                int b = (colorN >> 0) & 0xFF;

                                StringBuilder cComponents = new StringBuilder();
                                cComponents.append("rgb");
                                cComponents.append(":");
                                cComponents.append(r);
                                cComponents.append(":");
                                cComponents.append(g);
                                cComponents.append(":");
                                cComponents.append(b);

                                Log.w(TAG, cComponents.toString());

                                double doubleR = (double)r;
                                double doubleG = (double)g;
                                double doubleB = (double)b;

                                double red = doubleR / 255;
                                double green = doubleG / 255;
                                double blue = doubleB / 255;

                                StringBuilder colorComponents = new StringBuilder();
                                cComponents.append("redgreenblue");
                                cComponents.append(":");
                                colorComponents.append(red);
                                colorComponents.append(":");
                                colorComponents.append(green);
                                colorComponents.append(":");
                                colorComponents.append(blue);

                                Log.w(TAG, colorComponents.toString());

                                float X = (float)red * 0.664511f + (float)green * 0.154324f + (float)blue * 0.162028f;
                                float Y = (float)red * 0.283881f + (float)green * 0.668433f + (float)blue * 0.047685f;
                                float Z = (float)red * 0.000088f + (float)green * 0.072310f + (float)blue * 0.986039f;

                                float x = X / (X + Y + Z);
                                float y = Y / (X + Y + Z);

                                StringBuilder stringB = new StringBuilder();
                                stringB.append(red);
                                stringB.append(green);
                                stringB.append(blue);
                                Toast.makeText(getApplicationContext(), "Color Changed", Toast.LENGTH_SHORT).show();

                                PHBridge bridge = phHueSDK.getSelectedBridge();

                                List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                                for (PHLight light : allLights) {
                                    PHLightState lightState = new PHLightState();

                                    if (light.getName().contains(bulbName)) {

                                        lightState.setX(x);
                                        lightState.setY(y);

                                        bridge.updateLightState(light, lightState, listener);
                                    }
                                }

                                changeBackgroundColor(selectedColor);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Color List:");
                                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null)

                                        sb.append(":");
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });
    }

    //If lights are on, turn them off: if lights are off, turn them on
    public void setLightStatus(String name) {

        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();

            if (light.getName().contains(name)) {


                if (light.getLastKnownLightState().isOn()) {

                    lightState.setOn(false);

                } else {

                    lightState.setOn(true);
                }

                bridge.updateLightState(light, lightState, listener);
            }
        }
    }


    public void changeNameLabel(String name) {

        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();

            if (light.getName().contains(name)) {

                TextView nameLabel = (TextView) findViewById(R.id.nameLabel);
                nameLabel.setText(name);

            }
        }
    }

    public void setSwitchPosition() {

        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getName().contains(bulbName)) {

                if (light.getLastKnownLightState().isOn()) {

                    onOffSwitch.setChecked(true);
                }else{

                    onOffSwitch.setChecked(false);
                }
            }
        }
    }


    public void setInitialBrightness() {

        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getName().contains(bulbName)) {
                int currentBrightness = light.getLastKnownLightState().getBrightness();
                seekBar.setProgress(currentBrightness);
            }
        }
    }


    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
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

    public void addListenerOnButton() {

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setLabels() {

        Bundle extras = getIntent().getExtras();
        bulbName = extras.getString("pushName");

        TextView nameField = (TextView) findViewById(R.id.nameLabel);
        nameField.setText(bulbName);
    }
    private void changeBackgroundColor(int selectedColor) {
        currentBackgroundColor = selectedColor;
        root.setBackgroundColor(selectedColor);
    }

    private void toast(String text) {
    }


    @Override
    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {

            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }

            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }
}
