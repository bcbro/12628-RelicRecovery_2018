package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by cisto on 1/4/2018.
 */

public class MovementAutoProcessor extends MovementProcessor {
    public MovementAutoProcessor(LinearOpMode opMode) {
        super(opMode);
    }

    @Override
    public void process() {
    }

    public void jewelMove( double powerLevel, int sleepms, DcMotorSimple.Direction direction){
        leftDrive.setDirection(direction);
        rightDrive.setDirection(direction.inverted());
        leftDrive.setPower(powerLevel);
        rightDrive.setPower(powerLevel);
        sleep(sleepms);
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
    }

}
