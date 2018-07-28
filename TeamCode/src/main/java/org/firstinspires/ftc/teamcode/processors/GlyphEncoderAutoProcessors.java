package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.BaseAutoTeleOp;
import org.firstinspires.ftc.teamcode.HardwarePushbot;
import org.firstinspires.ftc.teamcode.R;

import static java.lang.Boolean.FALSE;
import static java.lang.Math.abs;
import static org.firstinspires.ftc.teamcode.processors.GlyphMovementAutoProcessor.COUNTS_PER_INCH;

/**
 * Created by cisto on 6/23/2018.
 */

public class GlyphEncoderAutoProcessors extends BaseProcessor {
    final double TURN_MOTOR = 0.6;
    final double DRIVE_MOTOR = 0.8;
    private ElapsedTime runtime = new ElapsedTime();
    final double CRYPTO_BOX_DISTANCE = 10.0;
    final double TURN_DISTANCE = 12.0;
    boolean relicSide = FALSE;
    JewelSensorAutoArmProcessor.JewelColor baseColor = JewelSensorAutoArmProcessor.JewelColor.UNKOWN;
    int newLeftTarget;
    int newRightTarget;
    final double TURN_TIMEOUT = 4.0;
    public DcMotor leftDrive=getHardwareMap().get(DcMotor.class, "leftDrive");
    public DcMotor rightDrive=getHardwareMap().get(DcMotor.class, "rightDrive");


    public GlyphEncoderAutoProcessors(LinearOpMode opMode, JewelSensorAutoArmProcessor.JewelColor baseColor, boolean relicSide) {
        super(opMode);
        this.baseColor=baseColor;
        this.relicSide=relicSide;
    }

    @Override
    public void init() {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        getTelemetry().addData("Path2", "Running at %7d :%7d",
                leftDrive.getCurrentPosition(),
                rightDrive.getCurrentPosition());

        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void process() {
        getTelemetry().addData(" start move", "BaseColor %s  relic side %s", baseColor.name(), (relicSide) ? "true" : "false");
        if (JewelSensorAutoArmProcessor.JewelColor.RED.equals(baseColor)) {
            if (relicSide) {
                RedRelicCorner();
            } else {
                RedNonRelicCorner();
            }
        } else if (JewelSensorAutoArmProcessor.JewelColor.BLUE.equals(baseColor)){
            if (relicSide) {
                BlueRelicCorner();
            } else {
                BlueNonRelicCorner();
            }
        }
        releaseGlyph();
    }

    private void releaseGlyph() {
        GlyphClawAutoProcessor processor = (GlyphClawAutoProcessor) ((BaseAutoTeleOp) opMode).getProcessors(GlyphClawAutoProcessor.class);
        if (processor!=null){
            processor.drop();
        }
        else {
            getTelemetry().addData("processor is null: ", "GlyphClawAutoProcessor");
        }

    }


    void encoderDrive(boolean isTurning, double distance, double timeouts, String teleopfeedback) {
        double leftDistance;
        double rightDistance;
        if (isTurning) {
            leftDistance = distance;
            rightDistance = -distance;
        } else {
            leftDistance = distance;
            rightDistance = distance;
        }
        newLeftTarget = leftDrive.getCurrentPosition() + (int) (leftDistance * COUNTS_PER_INCH);
        newRightTarget = rightDrive.getCurrentPosition() + (int) (rightDistance * COUNTS_PER_INCH);
        leftDrive.setTargetPosition(newLeftTarget);
        rightDrive.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        if (isTurning) {
            leftDrive.setPower(abs(TURN_MOTOR));
            rightDrive.setPower(abs(TURN_MOTOR));
        } else {
            leftDrive.setPower(abs(DRIVE_MOTOR));
            rightDrive.setPower(abs(DRIVE_MOTOR));
        }

        while (opMode.opModeIsActive() &&
                (runtime.seconds() < timeouts) &&
                (leftDrive.isBusy() || rightDrive.isBusy())) {
            // Display it for the driver.
            getTelemetry().addData("directions:", " %s :%s", leftDrive.getDirection().toString(), rightDrive.getDirection().toString());
            getTelemetry().addData(" start move", "BaseColor %s  relic side %s", baseColor.name(), (relicSide) ? "true" : "false");
            getTelemetry().addData("Processing...", teleopfeedback);
            getTelemetry().addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
            getTelemetry().addData("Path2", "Running at %7d :%7d sync %s",
                    leftDrive.getCurrentPosition(),
                    rightDrive.getCurrentPosition(),
                    (abs(leftDrive.getCurrentPosition()-rightDrive.getCurrentPosition())>0.05*abs(leftDistance)) ? "OUTTA sync BUDDY BS" : "kiio 9 im synced bruh");

            if (newLeftTarget - leftDrive.getCurrentPosition()==0 && newRightTarget - rightDrive.getCurrentPosition()==0) {
                getTelemetry().addData("Done", ":)");
            }
            getTelemetry().update();
            sleep(30);
        }
        if (newLeftTarget - leftDrive.getCurrentPosition()==0 && newRightTarget - rightDrive.getCurrentPosition()==0) {
            leftDrive.setPower(0);
            rightDrive.setPower(0);
        }
        // Stop all motion;

        leftDrive.setPower(0);

        // Turn off RUN_TO_POSITION
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        sleep(250);

    }

    void Move(double distance) {
        double timeouts = 3.0 + abs(distance) /12;
        encoderDrive(false, distance, timeouts, "Moving " + distance + " inches.>:C");
        //getTelemetry().addData("time( %7d )", "out of timeouts( %7d )", runtime.seconds(), timeouts);
    }

    void turnRight() {
        encoderDrive(true, TURN_DISTANCE, TURN_TIMEOUT, "Turning right.=DThe circleoflie");
    }

    void turnLeft() {
        encoderDrive(true, -TURN_DISTANCE, TURN_TIMEOUT, "Tur" +
                "ning Left. ;)");
    }

    void RedRelicCorner() {
        Move(36.0);
        //turnRight();
        //Move(CRYPTO_BOX_DISTANCE);
    }

    void RedNonRelicCorner() {
        Move(24.0);
        turnLeft();
        Move(12.0);
        turnRight();
        Move(CRYPTO_BOX_DISTANCE);
    }

    void BlueRelicCorner() {
        Move(-36.0);
        turnRight();
        Move(CRYPTO_BOX_DISTANCE);
    }


    void BlueNonRelicCorner() {
        Move(-24.0);
        turnLeft();
        Move(12.0);
        turnLeft();
        Move(CRYPTO_BOX_DISTANCE);
    }

    private RelicRecoveryVuMark getVuMark() {
        PictographProcessor processor = (PictographProcessor) ((BaseAutoTeleOp) opMode).getProcessors(PictographProcessor.class);
        return processor.vuMark;
    }
}
