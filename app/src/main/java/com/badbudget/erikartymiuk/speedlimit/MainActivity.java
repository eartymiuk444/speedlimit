package com.badbudget.erikartymiuk.speedlimit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private Toast errorToast;

    private boolean isMiles = true;

    private static final Double METERS_PER_MILE = 1609.344;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            isMiles = savedInstanceState.getBoolean("isMiles");
        }
        else {
            isMiles = true;
        }

        setContentView(R.layout.activity_main);

    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isMiles", isMiles);
    }

    public void calculateClick(View view)
    {
        if (errorToast == null) {
            errorToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }

        EditText speedLimitText = (EditText) findViewById(R.id.speedLimit);
        EditText speedText = (EditText) findViewById(R.id.speed);
        EditText distanceText = (EditText) findViewById(R.id.distance);
        EditText timeMinutesText = (EditText) findViewById(R.id.timeMinutes);

        //Need at most one not filled in. If all filled in default to calc time diff
        boolean haveSpeedLimit = !speedLimitText.getText().toString().equals("");
        boolean haveSpeed = !speedText.getText().toString().equals("");
        boolean haveDistance = !distanceText.getText().toString().equals("");
        boolean haveTimeMinutes = !timeMinutesText.getText().toString().equals("");

        //Need 3/4 or 4/4 to be able to calculate something
        if (haveSpeedLimit && haveSpeed && haveDistance && !haveTimeMinutes) {
            Double speedLimit = Double.parseDouble(speedLimitText.getText().toString());
            Double speed = Double.parseDouble(speedText.getText().toString());
            Double distance = Double.parseDouble(distanceText.getText().toString());

            if (noZeros(speedLimit, speed, distance, null)) {
                timeMinutesText.setText(calculateTimeDiff(speedLimit, speed, distance));
            }
            else {
                showToastIfNotShowingWithMessage("Do not enter any zeros");
            }
        }
        else if (haveSpeedLimit && !haveSpeed && haveDistance && haveTimeMinutes) {
            Double speedLimit = Double.parseDouble(speedLimitText.getText().toString());
            Double distance = Double.parseDouble(distanceText.getText().toString());
            Double timeMinutes = Double.parseDouble(timeMinutesText.getText().toString());
            if (noZeros(speedLimit, null, distance, timeMinutes)) {
                speedText.setText(calculateSpeed(speedLimit, distance, timeMinutes));
            }
            else {
                showToastIfNotShowingWithMessage("Do not enter any zeros");
            }
        }
        else if (haveSpeedLimit && haveSpeed && !haveDistance && haveTimeMinutes) {
            Double speedLimit = Double.parseDouble(speedLimitText.getText().toString());
            Double speed = Double.parseDouble(speedText.getText().toString());
            Double timeMinutes = Double.parseDouble(timeMinutesText.getText().toString());
            if (noZeros(speedLimit, speed, null, timeMinutes)) {
                distanceText.setText(calculateDistance(speedLimit, speed, timeMinutes));
            }
            else {
                showToastIfNotShowingWithMessage("Do not enter any zeros");
            }
        }
        else if (haveSpeedLimit && haveSpeed && haveDistance && haveTimeMinutes) {

            Double speedLimit = Double.parseDouble(speedLimitText.getText().toString());
            Double speed = Double.parseDouble(speedText.getText().toString());
            Double distance = Double.parseDouble(distanceText.getText().toString());
            Double timeMinutes = Double.parseDouble(timeMinutesText.getText().toString());
            if (noZeros(speedLimit, speed, distance, timeMinutes)) {
                timeMinutesText.setText(calculateTimeDiff(speedLimit, speed, distance));
            }
            else {
                showToastIfNotShowingWithMessage("Do not enter any zeros");
            }
        }
        else if (!haveSpeedLimit && haveSpeed && haveDistance && haveTimeMinutes){
            Double speed = Double.parseDouble(speedText.getText().toString());
            Double distance = Double.parseDouble(distanceText.getText().toString());
            Double timeMinutes = Double.parseDouble(timeMinutesText.getText().toString());
            if (noZeros(null, speed, distance, timeMinutes)) {
                speedLimitText.setText(calculateSpeedLimit(speed, distance, timeMinutes));
            }
            else {
                showToastIfNotShowingWithMessage("Do not enter any zeros");
            }
        }
        else {
            //have less then 2/4 things filled in, need more info...
            showToastIfNotShowingWithMessage("Enter at least 3/4 fields");
        }

    }

    private void showToastIfNotShowingWithMessage(String message) {
        if (!errorToast.getView().isShown())
        {
            errorToast.setGravity(Gravity.CENTER, 0, 0);
            errorToast.setText(message);
            errorToast.show();
        }
    }

    private String calculateTimeDiff(Double speedLimit, Double speed, Double distance) {
        Double timeMinutesResult = 60 * distance * (1/(speedLimit) - 1/(speed));
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(timeMinutesResult);
    }

    private String calculateSpeed(Double speedLimit, Double distance, Double timeMinutes) {
        Double speedResult = 1/(1/speedLimit - timeMinutes/(60*distance));
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(speedResult);
    }

    private String calculateSpeedLimit(Double speed, Double distance, Double timeMinutes) {
        Double speedLimitResult = 1.0/(1.0/speed + timeMinutes/(60.0*distance));
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(speedLimitResult);
    }

    private String calculateDistance(Double speedLimit, Double speed, Double timeMinutes) {
        Double distanceResult = timeMinutes / (60 * (1/speedLimit - 1/speed));
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(distanceResult);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        TextView speedLabel = (TextView) findViewById(R.id.speedLabel);
        TextView speedLimitLabel = (TextView) findViewById(R.id.speedLimitLabel);
        TextView distanceLabel = (TextView) findViewById(R.id.distanceLabel);


        EditText speedLimitText = (EditText) findViewById(R.id.speedLimit);
        EditText speedText = (EditText) findViewById(R.id.speed);
        EditText distanceText = (EditText) findViewById(R.id.distance);

        //Need at most one not filled in. If all filled in default to calc time diff
        boolean haveSpeedLimit = !speedLimitText.getText().toString().equals("");
        boolean haveSpeed = !speedText.getText().toString().equals("");
        boolean haveDistance = !distanceText.getText().toString().equals("");

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.miles:
                if (checked) {
                    if (!isMiles) {
                        isMiles = true;
                        speedLabel.setText("MPH");
                        speedLimitLabel.setText("MPH");
                        distanceLabel.setText("MILES");


                        if (haveSpeedLimit) {
                            Double speedLimit = Double.parseDouble(speedLimitText.getText().toString());
                            speedLimitText.setText(kphToMph(speedLimit));
                        }
                        if (haveSpeed) {
                            Double speed = Double.parseDouble(speedText.getText().toString());
                            speedText.setText(kphToMph(speed));
                        }
                        if (haveDistance) {
                            Double distance = Double.parseDouble(distanceText.getText().toString());
                            distanceText.setText(kilometersToMiles(distance));
                        }

                    }
                }
                break;
            case R.id.kilometers:
                if (checked) {
                    if (isMiles) {
                        isMiles = false;
                        speedLabel.setText("km/h");
                        speedLimitLabel.setText("km/h");
                        distanceLabel.setText("km");

                        if (haveSpeedLimit) {
                            Double speedLimit = Double.parseDouble(speedLimitText.getText().toString());
                            speedLimitText.setText(mphToKph(speedLimit));
                        }
                        if (haveSpeed) {
                            Double speed = Double.parseDouble(speedText.getText().toString());
                            speedText.setText(mphToKph(speed));
                        }
                        if (haveDistance) {
                            Double distance = Double.parseDouble(distanceText.getText().toString());
                            distanceText.setText(milesToKilometers(distance));
                        }
                    }
                }
                break;
        }
    }

    private String milesToKilometers(Double miles) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(miles*(METERS_PER_MILE/1000));
    }

    private String kilometersToMiles(Double kilometers) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(kilometers/(METERS_PER_MILE/1000));    }

    private String mphToKph(Double mph) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(mph * (METERS_PER_MILE/1000));
    }

    private String kphToMph(Double kph) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(kph /(METERS_PER_MILE/1000));
    }

    private boolean noZeros(Double speedLimit, Double speed, Double distance, Double timeMinutes) {
        boolean noZeros = (speedLimit == null || speedLimit != 0) && (speed == null || speed != 0) &&
                (distance == null || distance != 0) && (timeMinutes == null || timeMinutes != 0);
        return noZeros;
    }
}


