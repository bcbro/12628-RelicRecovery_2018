package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by cisto on 1/3/2018.
 */

public class GlyphArmProcessor extends BaseProcessor {

    double zeropower=0;

    public DcMotor glyphArm = null;
    final int SLEEP_MS = 50;
    final double MIN_POWER = 0.10;
    final double MAX_POWER = 0.80;

    private boolean lastTriggerLeft = true;
    private boolean triggerOn = true;

    public GlyphArmProcessor(LinearOpMode opMode) {
        super(opMode);
    }

    @Override
    public void init() {
        // Define and initialize ALL installed servos.
        glyphArm = getHardwareMap().get(DcMotor.class, "glyph_arm");
        glyphArm.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        glyphArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    protected void stopArm() {
        glyphArm.setPower(0);
        sleep(SLEEP_MS);
    }

    protected double getPower( double triggerVal){

        if (triggerVal<=0) return 0.0;
        return MIN_POWER + triggerVal* (MAX_POWER-MIN_POWER);
    }

    @Override
    public void process() {


        if (getGamepad(1).x){
            triggerOn =true;
        }else if (getGamepad(1).y){
            triggerOn =false;
            glyphArm.setPower(zeropower);
        }

        if (triggerOn) {
            // Use gamepad left & right trigger  to move the arm up and down
            if (getGamepad(1).left_trigger > 0.0) {
                if (!lastTriggerLeft) {
                    stopArm();
                    lastTriggerLeft = true;
                }
                glyphArm.setPower(getPower(getGamepad(1).left_trigger));
                glyphArm.setDirection(DcMotor.Direction.REVERSE);
            } else if (getGamepad(1).right_trigger > 0.0) {
                if (lastTriggerLeft) {
                    stopArm();
                    lastTriggerLeft = false;
                }
                glyphArm.setPower(getPower(getGamepad(1).right_trigger));
                glyphArm.setDirection(DcMotor.Direction.FORWARD);
            }else{
                glyphArm.setPower(zeropower);
            }
            getTelemetry().addData("Trigger",  " left[%.2f] right[%.2f] power [%.2f] [%s]",
                    getGamepad(1).left_trigger, getGamepad(1).right_trigger,
                    glyphArm.getPower(), glyphArm.getDirection().name());

        }
    }
}
