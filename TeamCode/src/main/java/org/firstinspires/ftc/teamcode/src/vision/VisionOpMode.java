package org.firstinspires.ftc.teamcode.src.vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp(name="Running with Vision assist", group="Linear OpMode")
public class VisionOpMode extends LinearOpMode {

    // VisionPortal later usage
    private VisionPortal visionPortal;
    private OpenCvWebcam visionCam;
    private boolean detectionStarted = false;
    private VisionPipelineColorDetection.Selected selected;


    public void runOpMode() throws InterruptedException {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        visionCam = OpenCvCameraFactory.getInstance().createWebcam(
                hardwareMap.get(WebcamName.class, "visionCam"), cameraMonitorViewId);

        waitForStart();

        visionCam.setPipeline(new VisionPipelineColorDetection());

        // While program is running
        while (opModeIsActive()) {
            if (gamepad1.a && !detectionStarted) {
                telemetry.addLine("Color object detection set to commence");
                startObjectDetection(telemetry);
                detectionStarted = true;
            } else {
                endObjectDetection(telemetry);
            }

            telemetry.addData("Frame Count", visionCam.getFrameCount());
            telemetry.addData("FPS", (int) (visionCam.getFps() + 0.5));
            telemetry.addData("Selected object color", selected.name());
            telemetry.update();
        }
    }

    // Start streaming
    public void startObjectDetection(Telemetry telemetry) {
        visionCam.startStreaming(800, 600, OpenCvCameraRotation.UPRIGHT);
        telemetry.addLine("Now starting object detection");
    }

    // End streaming
    public void endObjectDetection(Telemetry telemetry) {
        visionCam.stopStreaming();
        telemetry.addLine("Now ending object detection");
    }

//    public void getSelected() {
//       return selelctedfs
//    }
}
