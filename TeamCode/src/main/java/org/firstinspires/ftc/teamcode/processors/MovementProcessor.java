package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by cisto on 1/4/2018.
 */

public class MovementProcessor extends BaseProcessor {
    public MovementProcessor(LinearOpMode opMode) {
        super(opMode);
    }
    double left;
    double right;
    double drive;
    double turn;
    double max;
    public DcMotor leftDrive;
    public DcMotor rightDrive;

    @Override
    public void init() {
        leftDrive=getHardwareMap().get(DcMotor.class, "leftDrive");
        rightDrive=getHardwareMap().get(DcMotor.class, "rightDrive");
    }



    @Override
    public void process() {

        // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
        // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
        // This way it's also easy to just drive straight, or just turn.

        drive = getGamepad(2).left_stick_x;
        turn  = getGamepad(2).right_stick_y;

        left  = drive + turn;
        right = drive - turn;
        max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0)
        {
            left /= max;
            right /= max;
        }

        leftDrive.setPower(left);
        rightDrive.setPower(right);
        getTelemetry().addData("Move",  " left[%.2f] right[%.2f]", left,right);
    }

}
