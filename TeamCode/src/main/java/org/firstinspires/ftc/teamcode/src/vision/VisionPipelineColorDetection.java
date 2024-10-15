package org.firstinspires.ftc.teamcode.src.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class VisionPipelineColorDetection extends OpenCvPipeline {

    Selected selection = Selected.NONE;
    Mat outputFrame = new Mat();

    public Selected getCurrentColorSelection() {
        return selection;
    }

    @Override
    public Mat processFrame(Mat frame) {
        Imgproc.cvtColor(frame, outputFrame, Imgproc.COLOR_RGB2HSV);

        double frameHue = getAvgHue(outputFrame);


        // Variables are all Hue values of HSV
        double redThresholdHValueStart = 355; // hsv(360, 92%, 60%)
        double redThresholdHValueEnd = 360;

        double redThresholdHValueStartSecond = 2;
        double redThresholdHValueEndSecond = 8;

        double blueThresholdHValueStart = 228;    //hsv(220, 92%, 23%)
        double blueThresholdHValueEnd = 233;

        double yellowThresholdHValueStart = 39; // hsv(44, 88%, 100%)
        double yellowThresholdHValueEnd = 43;

        if ((frameHue >= redThresholdHValueStart && frameHue <= redThresholdHValueEnd) ||
                (frameHue >= redThresholdHValueStartSecond && frameHue <= redThresholdHValueEndSecond)) {
            selection = Selected.RED;
        } else if (frameHue >= blueThresholdHValueStart && frameHue <= blueThresholdHValueEnd) {
            selection = Selected.BLUE;
        } else if (frameHue >= yellowThresholdHValueStart && frameHue <= yellowThresholdHValueEnd) {
            selection = Selected.NEUTRAL;
        }
            // Compares saturation to others to determine color of object
            // return the selected color currently return Selected.BLUE;
        return selection;
    }

    public double getAvgHue(Mat selectedFrame) {
        Scalar color = Core.mean(selectedFrame);
        return color.val[0] * (360.0 / 179.0);
    }

    public enum Selected {
        NONE,
        BLUE,
        RED,
        NEUTRAL
    }
}
