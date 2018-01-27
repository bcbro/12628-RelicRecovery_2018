package org.firstinspires.ftc.teamcode.processors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static com.sun.tools.javac.util.Constants.format;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;

/**
 * Created by cisto on 1/6/2018.
 */

public class PictographProcessor extends BaseProcessor {

    RelicRecoveryVuMark vuMark =  RelicRecoveryVuMark.UNKNOWN;
    public PictographProcessor(LinearOpMode opMode) {
        super(opMode);
    }

    public static final String TAG = "Vuforia VuMark Sample";

    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    VuforiaTrackables relicTrackables =null;
    VuforiaTrackable relicTemplate =null;

    public void init() {
        int cameraMonitorViewId = getHardwareMap().appContext.getResources().getIdentifier("cameraMonitorViewId", "id", getHardwareMap().appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AcFh9if/////AAAAmRVY/EX1tERXlbmB1+kgtEh+xGvelsMTvi7epSNWzFAwtOtTPraNIArANwBoakhG9rDyuTxS4wnnKtDgc76Ha7dgxXGY2+a1Lwz/V+jVh47IHyxpN1AGaOpiLxjl6BoLAhgpRJGUrOeyZfg/WHkf1CzjgzkNE8mDLPscu3v87IV8g2Sac2j0BUCfEem4fLqgCZGddBVnJ3E+cwmawyRjUW+FETw4lG3kPrO9p4sg/w906TFiikvPFABR1DBDd2qAfFnXClQFfykE8ZaLrvDXCETeYkgZCGQ7axUmk2cTTBGrWLeRIzDc1euHkV1padp9VYq54x9XpyQ+EnJfpE9BA/ISXnuDfZvbzT22ul3WAIFD";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);

        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        getTelemetry().addData(">", "Press Play to start");

        relicTrackables.activate();

        }

    @Override
    public void process() {
        int i= 0;
        while((i++<5 ) && (vuMark == RelicRecoveryVuMark.UNKNOWN)) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
                /*
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
                getTelemetry().addData("Pose", format(pose));
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }
                */
            getTelemetry().addData("VuMark", vuMark);

            getTelemetry().update();
            sleep(200);
        }
    }
}