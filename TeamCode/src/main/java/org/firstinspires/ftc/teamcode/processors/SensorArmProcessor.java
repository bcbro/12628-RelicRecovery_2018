package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by cisto on 1/4/2018.
 */

public class SensorArmProcessor extends BaseProcessor{
    public SensorArmProcessor(LinearOpMode opMode) {
        super(opMode);
    }
    public Servo sensorArm;
    double position = 0;
    double INCREMENT=0.03125;
    @Override
    public void init() {
        sensorArm=getHardwareMap().get(Servo.class, "sensorArm");
        position=0.8;
        sensorArm.setPosition(position);
    }

    @Override
    public void process() {
        /*position=0.5;
        sensorArm.setPosition(position);
        while(position>=0.3419999) {
            position-=INCREMENT;
            sensorArm.setPosition(position);
            sleep(1500);
        }
*/
    }
}
