package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by cisto on 1/3/2018.
 */
@Autonomous()
public class GlyphClawAutoProcessor extends BaseProcessor {

    public Servo leftClaw = null;
    public Servo rightClaw = null;
    public double clawOffset = 0;
    public static final double MID_SERVO = 0.5;
    final double CLAW_MOVE = 0.20;



    double zeropower=0;
    public DcMotor glyphArm = null;
    final int SLEEP_MS = 50;
    final double MIN_POWER = 0.10;
    final double MAX_POWER = 0.80;
    final double GLYPH_POWER = 0.40;
    final int GLYPH_MS = 1000;

    public void process() {
        pickUp();
    }


    public GlyphClawAutoProcessor(LinearOpMode opMode) {
        super(opMode);
    }

    @Override
    public void init() {
        // Define and initialize ALL installed servos.
        leftClaw = getHardwareMap().get(Servo.class, "leftClaw");
        rightClaw = getHardwareMap().get(Servo.class, "rightClaw");
        moveClaw(-CLAW_MOVE);
        // Define and initialize ALL installed servos.
        glyphArm = getHardwareMap().get(DcMotor.class, "glyphArm");
        glyphArm.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        glyphArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void pickUp() {
        //moveClaw(CLAW_MOVE);
        glyphArm.setPower(GLYPH_POWER);
        sleep(GLYPH_MS);
        glyphArm.setPower(0);
    }

    public void drop() {
        glyphArm.setPower(GLYPH_POWER);
        sleep(GLYPH_MS);
        glyphArm.setPower(0);
        moveClaw(CLAW_MOVE);
    }

    public void moveClaw(double clawOffset){
        clawOffset = Range.clip(clawOffset, -0.5, 0.5);
        leftClaw.setPosition(MID_SERVO + clawOffset);
        rightClaw.setPosition(MID_SERVO - clawOffset);
        sleep(SLEEP_MS);
    }


}
