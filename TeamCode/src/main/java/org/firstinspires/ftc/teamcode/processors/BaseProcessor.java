package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by cisto on 1/3/2018.
 */

public  abstract class BaseProcessor implements Processor{
    protected LinearOpMode opMode;

    BaseProcessor(LinearOpMode opMode){
        this.opMode =opMode;
    }

    protected Telemetry getTelemetry(){
        return opMode.telemetry;
    }

    protected HardwareMap getHardwareMap(){
        return opMode.hardwareMap;
    }

    protected Gamepad getGamepad( int i){
        return (i==1)? opMode.gamepad1: opMode.gamepad2;
    }

    protected JewelSensorAutoArmProcessor.JewelColor getBaseColor() {return JewelSensorAutoArmProcessor.baseColor;}

    protected void sleep(long msec){
        opMode.sleep(msec);
    }

    abstract public void init();
    abstract public void process();
}
