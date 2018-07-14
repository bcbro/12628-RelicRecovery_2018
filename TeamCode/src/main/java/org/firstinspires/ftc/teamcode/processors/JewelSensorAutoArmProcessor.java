package org.firstinspires.ftc.teamcode.processors;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.BaseAutoTeleOp;

/**
 * Created by cisto on 1/4/2018.
 */

public class JewelSensorAutoArmProcessor extends BaseProcessor{
    public Servo sensorArm;


    final  double INITIAL_POSITION = 0.8;
    final  double START_POSITION = 0.34;
    final  double INCREMENT_POSITION = 0.01;
    final  double MIN_POSITION = 0.20;
    final int SLEEP_MS = 100;

    double position  = INITIAL_POSITION;


    final double JEWEL_MOTOR = 0.4;
    final int    JEWEL_SLEEP_MS = 300;

    ColorSensor sensorColor;
    DistanceSensor sensorDistance;

    static JewelColor baseColor;

    public JewelSensorAutoArmProcessor(LinearOpMode opMode, JewelColor baseColor){
        super(opMode);
        JewelSensorAutoArmProcessor.baseColor = baseColor;
    }


    View relativeLayout = null;

    public enum JewelColor{
        RED,
        BLUE,
        UNKOWN
    }


    JewelColor getJewelColor(final float values[]) {
        //the h range is 0 to 179 //the s range is 0 to 255 //the v range is 0 to 255
        //the values are stored as a list of min HSV //and a list of max HSV
        if (values.length > 0) {
            float hue = values[0];
            getTelemetry().addData("Jewel Arm",  " hue[%.2f] ", hue );
            if (hue >= 30.0 && hue <= 90.0) {
                // hsvMin.add(new Scalar( 60/2,  50, 150)); //green min
                // hsvMax.add(new Scalar(180/2, 255, 255)); //green max
                return JewelColor.UNKOWN;
            } else if (hue >= 190.0 && hue <= 290.0) {
                // hsvMin.add(new Scalar(180/2,  50, 150)); //blue min
                // hsvMax.add(new Scalar(300/2, 255, 255)); //blue max
                return JewelColor.BLUE;
            } else if ((hue >= 310.0 && hue <= 360.0) ||
                    (hue >= 0.0 && hue <= 40.0) ){
            // hsvMin.add(new Scalar(300/2,  50, 150)); //red min
            // hsvMax.add(new Scalar( 60/2, 255, 255)); //red max
            return JewelColor.RED;
            }
         }
        return JewelColor.UNKOWN;
    }


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

    private float[]  getHSV(){
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;
        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        // convert the RGB values to HSV values.
        // multiply by the SCALE_FACTOR.
        // then cast it back to int (SCALE_FACTOR is a double)
        Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                (int) (sensorColor.green() * SCALE_FACTOR),
                (int) (sensorColor.blue() * SCALE_FACTOR),
                hsvValues);

        // change the background color to match the color detected by the RGB sensor.
        // pass a reference to the hue, saturation, and value array as an argument
        // to the HSVToColor method.
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
        });
        return hsvValues;
    }

    @Override
    public void process() {
        position=START_POSITION;
        sensorArm.setPosition(position);
        JewelColor jewelColor = JewelColor.UNKOWN;
        double distanceCm = 0.0;
        while(position>=MIN_POSITION) {
            position -= INCREMENT_POSITION;
            sensorArm.setPosition(position);
            distanceCm = sensorDistance.getDistance(DistanceUnit.CM);
            if (distanceCm != Double.NaN && (distanceCm < 10.0)) {
                jewelColor = getJewelColor(getHSV());
            }else{
                jewelColor = JewelColor.UNKOWN;
            }
            getTelemetry().addData("Jewel Arm",  " pos[%.2f] dist[%.2f] jewel[%s] ",
                    sensorArm.getPosition(),distanceCm,  jewelColor.name()
                    );
            getTelemetry().update();
            if (jewelColor != JewelColor.UNKOWN) {
                break;
            }
            sleep(SLEEP_MS);
        }
        if (jewelColor != JewelColor.UNKOWN){
            getTelemetry().addData("Exiting Jewel Arm",  " pos[%.2f] dist[%.2f] jewel[%s] ",
                    sensorArm.getPosition(),distanceCm,  jewelColor.name()
            );
            getTelemetry().update();
            sensorArm.setPosition(MIN_POSITION);
            if (jewelColor != baseColor){
                // Move forward
                move(DcMotorSimple.Direction.FORWARD);
            }else{
                // Move backward
                move(DcMotorSimple.Direction.REVERSE);
            }
        }
        sensorArm.setPosition(INITIAL_POSITION);

    }

    public void  move(DcMotorSimple.Direction direction){
        MovementAutoProcessor processor = (MovementAutoProcessor) ((BaseAutoTeleOp)opMode).getProcessors(MovementAutoProcessor.class);
        processor.jewelMove(JEWEL_MOTOR, JEWEL_SLEEP_MS, direction);
        processor.jewelMove(JEWEL_MOTOR, JEWEL_SLEEP_MS, direction.inverted());
        sensorArm.setPosition(INITIAL_POSITION);
    }


}
