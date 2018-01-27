package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by cisto on 1/3/2018.
 */
@Autonomous()
public class ClawProcessor extends BaseProcessor {

    public Servo leftClaw = null;
    public Servo rightClaw = null;
    public double clawOffset = 0;
    public static final double MID_SERVO = 0.5;
    final double CLAW_SPEED = 0.05;

    public ClawProcessor(LinearOpMode opMode) {
        super(opMode);
    }

    @Override
    public void init() {
        // Define and initialize ALL installed servos.
        leftClaw = getHardwareMap().get(Servo.class, "leftClaw");
        rightClaw = getHardwareMap().get(Servo.class, "rightClaw" +
                "");
        leftClaw.setPosition(MID_SERVO);
        rightClaw.setPosition(MID_SERVO);

    }

    @Override
    public void process() {
        // Use gamepad left & right Bumpers to open and close the claw
        if (getGamepad(1).right_bumper)
            clawOffset += CLAW_SPEED;
        else if (getGamepad(1).left_bumper)
            clawOffset -= CLAW_SPEED;

        // Move both servos to new position.  Assume servos are mirror image of each other.
        clawOffset = Range.clip(clawOffset, -0.5, 0.5);
        leftClaw.setPosition(MID_SERVO + clawOffset);
        rightClaw.setPosition(MID_SERVO - clawOffset);
        getTelemetry().addData("claw",  "Offset = %.2f", clawOffset);
    }
}
