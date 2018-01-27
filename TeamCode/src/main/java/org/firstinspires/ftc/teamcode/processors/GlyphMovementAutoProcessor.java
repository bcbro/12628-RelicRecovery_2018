package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.BaseAutoTeleOp;

/**
 * Created by cisto on 1/9/2018.
 */
@Autonomous(name="Pushbot: GlyphMovement", group="Pushbot")
public class GlyphMovementAutoProcessor extends BaseProcessor {
    boolean relicSide = false;
    JewelSensorAutoArmProcessor.JewelColor baseColor = JewelSensorAutoArmProcessor.JewelColor.UNKOWN;

    public GlyphMovementAutoProcessor(LinearOpMode opMode, JewelSensorAutoArmProcessor.JewelColor baseColor, boolean relicSide) {
        super(opMode);
        this.baseColor = baseColor;
        this.relicSide = relicSide;
    }


    public DcMotor leftDrive;
    public DcMotor rightDrive;

    final double DRIVE_MOTOR = 0.4;
    final int DRIVE_SLEEP_MS = 100;

    final double TURN_MOTOR = 0.3;
    final int TURN_SLEEP_MS = 4000;



    @Override
    public void init() {
        leftDrive = getHardwareMap().get(DcMotor.class, "leftDrive");
        rightDrive = getHardwareMap().get(DcMotor.class, "rightDrive");

    }

    @Override
    public void process() {

        //drive(DRIVE_MOTOR, DRIVE_1_SLEEP_MS, DcMotorSimple.Direction.REVERSE);
        //turn(TURN_MOTOR, TURN_SLEEP_MS, rightTurn ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        //drive(DRIVE_2_MOTOR, DRIVE_2_SLEEP_MS, DcMotorSimple.Direction.REVERSE);
        /*
        getTelemetry().addData(" start move", getVuMark());
        getTelemetry().update();        sleep(5000);
        drive(48.0, DcMotorSimple.Direction.FORWARD);
        getTelemetry().update();
*/
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
        releaseGylph();
    }

    public void releaseGylph(){
        GlyphClawAutoProcessor processor = (GlyphClawAutoProcessor) ((BaseAutoTeleOp) opMode).getProcessors(GlyphClawAutoProcessor.class);
        processor.drop();
    }


    public double getVuMarkDiff(){
        if (getVuMark().equals(RelicRecoveryVuMark.RIGHT)){
            return  VUMARK_DIFF;
        } else if (getVuMark().equals(RelicRecoveryVuMark.RIGHT)){
            return -1 * VUMARK_DIFF;
        }
        return 0;
    }


    static final double CRYPTO_MOVE = 4.0;
    static final double VUMARK_DIFF = 8.0;


    void RedRelicCorner(){
        drive(36.0 + getVuMarkDiff(), DcMotorSimple.Direction.FORWARD);
        turnRight();
        drive(CRYPTO_MOVE, DcMotorSimple.Direction.FORWARD);
    }

    void RedNonRelicCorner(){
        drive(24.0 + CRYPTO_MOVE , DcMotorSimple.Direction.FORWARD);
        turnLeft();
        drive(12.0 + getVuMarkDiff(), DcMotorSimple.Direction.FORWARD);
        //drive(CRYPTO_MOVE, DcMotorSimple.Direction.FORWARD);
    }

    void BlueRelicCorner(){
        drive(36.0 + getVuMarkDiff(), DcMotorSimple.Direction.REVERSE);
        turnRight();
        drive(CRYPTO_MOVE, DcMotorSimple.Direction.FORWARD);
    }

    void BlueNonRelicCorner(){
        drive(24.0 +CRYPTO_MOVE , DcMotorSimple.Direction.REVERSE);
        turnLeft();
        drive(12.0 + getVuMarkDiff(), DcMotorSimple.Direction.FORWARD);
        turnLeft();
        //drive(CRYPTO_MOVE, DcMotorSimple.Direction.FORWARD);
    }

    public void turnRight(){
        turn(TURN_MOTOR, TURN_SLEEP_MS,  DcMotorSimple.Direction.REVERSE);
    }

    public void turnLeft(){
        turn(TURN_MOTOR/2, 100,  DcMotorSimple.Direction.FORWARD);
        turn(TURN_MOTOR, TURN_SLEEP_MS,  DcMotorSimple.Direction.FORWARD);
    }

    public void drive(double distance, DcMotorSimple.Direction direction) {
        drive(DRIVE_MOTOR, (int) distance * DRIVE_SLEEP_MS, direction);
    }


    public void drive(double speed, int time, DcMotorSimple.Direction direction) {
        leftDrive.setDirection(direction.inverted());
        rightDrive.setDirection(direction);
        leftDrive.setPower(speed);
        rightDrive.setPower(speed);
        sleep(time);
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
    }

    //FYI forward makes it turn left bro and backward, right
    private void turn(double speed, int time, DcMotorSimple.Direction direction) {
        leftDrive.setDirection(direction.inverted());
        rightDrive.setDirection(direction.inverted());
        leftDrive.setPower(speed);
        rightDrive.setPower(speed);
        sleep(time);
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
    }

    private RelicRecoveryVuMark getVuMark() {
        PictographProcessor processor = (PictographProcessor) ((BaseAutoTeleOp) opMode).getProcessors(PictographProcessor.class);
        return processor.vuMark;
    }

}
