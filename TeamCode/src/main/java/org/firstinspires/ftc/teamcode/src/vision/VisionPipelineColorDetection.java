package org.firstinspires.ftc.teamcode.src.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class VisionPipelineColorDetection extends OpenCvPipeline {
    Mat outputFrame = new Mat();
    Mat changedFrame = new Mat();

    @Override
    public Mat processFrame(Mat frame) {
        Mat modFrame = modifyFrame(frame);
        double frameHue = getAvgHue(modFrame);

        return determineColorSelection(frameHue);
    }

    public Selected determineColorSelection(double frameHue) {

        // Variables are all Hue values of HSV
        double redThresholdHValueStart = 355;
        double redThresholdHValueEnd = 360;

        double redThresholdHValueStartSecond = 2;
        double redThresholdHValueEndSecond = 8;

        double blueThresholdHValueStart = 228;
        double blueThresholdHValueEnd = 233;

        double yellowThresholdHValueStart = 39;
        double yellowThresholdHValueEnd = 43;

        if ((frameHue >= redThresholdHValueStart && frameHue <= redThresholdHValueEnd) ||
                (frameHue >= redThresholdHValueStartSecond && frameHue <= redThresholdHValueEndSecond)) {
            return Selected.RED;
        } else if (frameHue >= blueThresholdHValueStart && frameHue <= blueThresholdHValueEnd) {
            return Selected.BLUE;
        } else if (frameHue >= yellowThresholdHValueStart && frameHue <= yellowThresholdHValueEnd) {
            return Selected.NEUTRAL;
        }

        return Selected.NONE;
    }

    public double getAvgHue(Mat selectedFrame) {
        Scalar color = Core.mean(selectedFrame);
        return color.val[0] * (360.0 / 179.0);
    }

    public Mat modifyFrame(Mat frame) {
        // Region of interest to extract a certain part of the camera frame
        int roiStartX = frame.cols() / 4;
        int roiStartY = frame.rows() / 4;
        int roiWidth = frame.cols() / 2;
        int roiHeight = frame.rows() / 2;

        changedFrame = frame.submat(roiStartY, roiStartY + roiHeight, roiStartX, roiStartX + roiWidth);

        Imgproc.cvtColor(changedFrame, changedFrame, Imgproc.COLOR_RGB2HSV);


        return changedFrame;
    }

    public enum Selected {
        NONE,
        BLUE,
        RED,
        NEUTRAL
    }
}
