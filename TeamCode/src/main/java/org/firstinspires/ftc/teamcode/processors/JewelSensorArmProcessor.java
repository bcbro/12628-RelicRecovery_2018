package org.firstinspires.ftc.teamcode.processors;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

/**
 * Created by cisto on 1/4/2018.
 */

public class JewelSensorArmProcessor extends BaseProcessor{
    public JewelSensorArmProcessor(LinearOpMode opMode) {
        super(opMode);
    }
    public Servo sensorArm;


    final  double INITIAL_POSITION = 0.8;
    final  double START_POSITION = 0.45;
    final  double INCREMENT_POSITION = 0.03;
    final  double MIN_POSITION = 0.30;
    final int SLEEP_MS =100;

    double position  = INITIAL_POSITION;


    ColorSensor sensorColor;
    DistanceSensor sensorDistance;


    View relativeLayout = null;




    @Override
    public void init() {
        sensorArm=getHardwareMap().get(Servo.class, "sensorArm");
        position=INITIAL_POSITION;
        sensorArm.setPosition(position);

        // get a reference to the color sensor.
        sensorColor = getHardwareMap().get(ColorSensor.class, "sensorColorDistance");
        // get a reference to the distance sensor that shares the same name.
        sensorDistance = getHardwareMap().get(DistanceSensor.class, "sensorColorDistance");

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = getHardwareMap().appContext.getResources().getIdentifier("RelativeLayout", "id",
                getHardwareMap().appContext.getPackageName());
        relativeLayout = ((Activity) getHardwareMap().appContext).findViewById(relativeLayoutId);

    }


    @Override
    public void process() {
     }
}
