package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.BaseAutoTeleOp;

import static java.lang.Math.abs;
import static org.firstinspires.ftc.teamcode.processors.GlyphMovementAutoProcessor.COUNTS_PER_INCH;

/**
 * Created by cisto on 6/23/2018.
 */

public class GlyphEncoderAutoProcessors extends BaseProcessor{
    DcMotor leftDrive;
    DcMotor rightDrive;
    final double TURN_MOTOR = 0.3;
    final double DRIVE_MOTOR = 0.4;
    private ElapsedTime runtime= new ElapsedTime();
    final double CRYPTO_BOX_DISTANCE=10.0;
    final double TURN_DISTANCE =12.0;
    boolean relicSide = false;
    JewelSensorAutoArmProcessor.JewelColor baseColor = JewelSensorAutoArmProcessor.JewelColor.UNKOWN;
    int newLeftTarget;
    int newRightTarget;
    final double TURN_TIMEOUT=4.0;

    public GlyphEncoderAutoProcessors(LinearOpMode opMode, JewelSensorAutoArmProcessor.JewelColor baseColor, boolean relicSide) {
        super(opMode);
    }

    @Override
    public void init() {
        leftDrive = getHardwareMap().get(DcMotor.class, "leftDrive");
        rightDrive = getHardwareMap().get(DcMotor.class, "rightDrive");
    }

    @Override
    public void process() {
        getTelemetry().addData(" start move", "BaseColor %s  relic side %s",  baseColor.name(), (relicSide)?"true" :"false" );
        getTelemetry().update();
        if (JewelSensorAutoArmProcessor.JewelColor.RED.equals(baseColor)){
            if (relicSide) {
                RedRelicCorner();
            } else{
                RedNonRelicCorner();
            }
        }else{
            if (relicSide) {
                BlueRelicCorner();
            } else{
                BlueNonRelicCorner();
            }
        }
        releaseGlyph();
    }

    private void releaseGlyph() {
        GlyphClawAutoProcessor processor = (GlyphClawAutoProcessor) ((BaseAutoTeleOp) opMode).getProcessors(GlyphClawAutoProcessor.class);
        processor.drop();
    }

    void encoderDrive(boolean isTurning, double distance, double timeouts){
        double leftDistance;
        double rightDistance;
        if(isTurning){
            leftDistance = distance;
            rightDistance = -distance;
        }
        else{
            leftDistance= distance;
            rightDistance = distance;
        }
        newLeftTarget = leftDrive.getCurrentPosition() + (int)(leftDistance * COUNTS_PER_INCH);
        newRightTarget = rightDrive.getCurrentPosition() + (int)(rightDistance * COUNTS_PER_INCH);
        leftDrive.setTargetPosition(newLeftTarget);
        rightDrive.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        leftDrive.setPower(abs(DRIVE_MOTOR));
        rightDrive.setPower(abs(DRIVE_MOTOR));
        while (opMode.opModeIsActive() &&
                (runtime.seconds() < timeouts) &&
                (leftDrive.isBusy() && rightDrive.isBusy())) {
            // Display it for the driver.
            getTelemetry().addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            getTelemetry().addData("Path2",  "Running at %7d :%7d",
                    leftDrive.getCurrentPosition(),
                    rightDrive.getCurrentPosition());
        }
    }
    void Move(double distance) {
        double timeouts=3.0+abs(distance)/24;
        encoderDrive(false, distance, timeouts);
    }

    void turnRight() {
        encoderDrive(true, TURN_DISTANCE, TURN_TIMEOUT);
    }

    void turnLeft() {
        encoderDrive(true, -TURN_DISTANCE, TURN_TIMEOUT);
    }
    void RedRelicCorner(){
        Move(36.0);
        turnRight();
        Move(CRYPTO_BOX_DISTANCE);
    }
    void RedNonRelicCorner(){
        Move(24.0);
        turnLeft();
        Move(12.0);
        turnRight();
        Move(CRYPTO_BOX_DISTANCE);
    }

    void BlueRelicCorner(){
        Move(-36.0);
        turnRight();
        Move(CRYPTO_BOX_DISTANCE);
    }

    void BlueNonRelicCorner(){
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
