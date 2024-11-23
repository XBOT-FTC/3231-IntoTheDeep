package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

/* CONTROLS
   LEFT JOYSTICK: STRAFE
   RIGHT JOYSTICK: ROTATE
   DPAD UP: INCREMENT SPEED PERCENTAGE
   DPAD DOWN: DECREMENT SPEED PERCENTAGE
   A: PRECISION MODE
   LEFT BUMPER: ALIGN TO APRILTAG
*/

public class MecanumDrive {
    // drive stuff
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    public double speedModeLimiter = 0;
    public boolean precisionMode = false;
    private double speedChange = 0.0;
    private int count = 0;
    private int precisionLoop = 0;
    private int speedChangeLoop = 0;
    private double speedDecrement = 0;
    public double defaultPowerPercentage = 0.0;
    double speedGain  =  0.0; //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    double strafeGain =  0.0; //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    double turnGain   =  0.0; //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)
    double maxAutoSpeed = 0.0; //  Clip the approach speed to this max value
    double maxAutoStrafe = 0.0; //  Clip the approach speed to this max value
    double maxAutoTurn  = 0.0; //  Clip the turn speed to this max value
    double desiredDistance = 0.0; //  this is how close the camera should get to the target (inches)
    double desiredAutoDistance = 0.0;

    // camera stuff
    private static final boolean USE_WEBCAM = true; // Set true to use a webcam, or false for a phone camera
    private int desiredBlueTagID = 0; // Choose the tag you want to approach or set to -1 for ANY tag.
    private int desiredRedTagID = 0; // Choose the tag you want to approach or set to -1 for ANY tag.
    public VisionPortal visionPortal; // Used to manage the video source.
    private AprilTagProcessor aprilTag; // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null; // Used to hold the data for a detected AprilTag
    public boolean targetFound = false; // Set to true when an AprilTag target is detected
    public double drive  = 0; // Desired forward power/speed (-1 to +1)
    public double strafe = 0; // Desired strafe power/speed (-1 to +1)
    public double rotate = 0; // Desired turning power/speed (-1 to +1)
    public boolean loop = true;

    public MecanumDrive(HardwareMap hardwareMap, DcMotorSimple.Direction direction, Telemetry telemetry) {
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "lf_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "lb_drive");
        rightFrontDrive  = hardwareMap.get(DcMotor.class, "rf_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rb_drive");

        leftFrontDrive.setDirection(direction);
        leftBackDrive.setDirection(direction);
        rightFrontDrive.setDirection(direction.inverted());
        rightBackDrive.setDirection(direction.inverted());
    }

    public void drive(Gamepad gamepad, Telemetry telemetry) {
        targetFound = false;
        desiredTag = null;

        // Step through the list of detected tags and look for a matching tag
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            // Look to see if we have size info on this tag.
            if (detection.metadata != null) {
                //  Check to see if we want to track towards this tag.
                if ((desiredBlueTagID < 0) || (desiredRedTagID < 0) || (detection.id == desiredBlueTagID) || (detection.id == desiredRedTagID)) {
                    // Yes, we want to use this tag.
                    targetFound = true;
                    desiredTag = detection;
                    break;  // don't look any further.
                } else {
                    // This tag is in the library, but we do not want to track it right now.
                    telemetry.addData("Skipping", "Tag ID %d is not desired", detection.id);
                }
            } else {
                // This tag is NOT in the library, so we don't have enough information to track to it.
                telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
            }
        }

        // Tell the driver what we see, and what to do.
        if (targetFound) {
            telemetry.addData("\n>","HOLD Left-Bumper to Drive to Target\n");
            telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
            telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
            telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);
            telemetry.addData("Yaw","%3.0f degrees", desiredTag.ftcPose.yaw);
        } else {
            telemetry.addData("\n>","Drive using joysticks to find valid target\n");
        }

        // If Left Bumper is being pressed, AND we have found the desired target, Drive to target Automatically .
        if (gamepad.left_bumper && targetFound) {

            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            double rangeError   = (desiredTag.ftcPose.range - desiredDistance);
            double headingError = desiredTag.ftcPose.bearing;
            double yawError     = desiredTag.ftcPose.yaw;

            // Use the speed and turn "gains" to calculate how we want the robot to move.
            drive  = Range.clip(rangeError * speedGain, -maxAutoSpeed, maxAutoSpeed);
            rotate = Range.clip(headingError * turnGain, -maxAutoTurn, maxAutoTurn) ;
            strafe = Range.clip(-yawError * strafeGain, -maxAutoStrafe, maxAutoStrafe);

            telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, rotate);
        } else {
            // y and x are for moving, rotate is for rotating
            // drive using manual POV Joystick mode.  Slow things down to make the robot more controlable.
            drive  = -gamepad.left_stick_y * defaultPowerPercentage;  // Reduce drive rate to 50%.
            strafe = -gamepad.left_stick_x * defaultPowerPercentage;  // Reduce strafe rate to 50%.
            rotate = -gamepad.right_stick_x * defaultPowerPercentage;  // Reduce turn rate to 33%.
            telemetry.addData("Manual","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, rotate);
        }

        speedChange(gamepad);
        precisionModeSwitch(gamepad);

        moveRobot(drive, strafe, rotate, telemetry);
        telemetry.addData("Drive Precision Mode Status", precisionMode);
        telemetry.addData("Speed Change", speedChange);
        telemetry.addData("Default Speed Percentage", defaultPowerPercentage);
    }

    public void moveRobot(double x, double y, double yaw, Telemetry telemetry) {
        // Calculate wheel powers.
        double leftFrontPower  =  x - y - yaw;
        double rightFrontPower =  x + y + yaw;
        double leftBackPower   =  x + y - yaw;
        double rightBackPower  =  x - y + yaw;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower  /= max;
            rightFrontPower /= max;
            leftBackPower   /= max;
            rightBackPower  /= max;
        }

        // send power to the wheels
        if (precisionMode) {
            leftFrontDrive.setPower(leftFrontPower *= speedModeLimiter);
            leftBackDrive.setPower(leftBackPower *= speedModeLimiter);
            rightFrontDrive.setPower(rightFrontPower *= speedModeLimiter);
            rightBackDrive.setPower(rightBackPower *= speedModeLimiter);
        }
        else {
            leftFrontDrive.setPower(leftFrontPower *= speedChange);
            leftBackDrive.setPower(leftBackPower *= speedChange);
            rightFrontDrive.setPower(rightFrontPower *= speedChange);
            rightBackDrive.setPower(rightBackPower *= speedChange);
        }

        telemetry.addData("Front Motors", "Left Front (%.2f), Right Front (%.2f)", leftFrontPower, rightFrontPower);
        telemetry.addData("Back Motors", "Left Back (%.2f), Right Back (%.2f)", leftBackPower, rightBackPower);
    }

    // speed goes down or up in percentage of speedchange
    public void speedChange(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            if (count > 0 && speedChangeLoop == 0) {
                count--;
                speedChangeLoop++;
            }
        }
        else if (gamepad.dpad_down) {
            if (count < (1 / speedDecrement) - 1 && speedChangeLoop == 0) {
                count++;
                speedChangeLoop++;
            }
        }
        else {
            speedChangeLoop = 0;
        }
        speedChange = 1 - speedDecrement * count;
    }

    // switches precision mode on or off if press a
    public void precisionModeSwitch(Gamepad gamepad) {
        if (gamepad.a) {
            if (precisionLoop == 0) {
                precisionMode = !precisionMode;
                precisionLoop++;
            }
        }
        else {
            precisionLoop = 0;
        }
    }

    public void setPowers(double power) {
        leftFrontDrive.setPower(power);
        leftBackDrive.setPower(power);
        rightFrontDrive.setPower(power);
        rightBackDrive.setPower(power);
    }

    public void setSpeedModeLimiter(double speedModeLimiter) {
        this.speedModeLimiter = speedModeLimiter;
    }

    public void setSpeedDecrement(double speedDecrement) {
        this.speedDecrement = speedDecrement;
    }

    public void setDefaultPowerPercentage(double percentage) {
        defaultPowerPercentage = percentage;
    }

    public void setAutoMaxes(double maxAutoSpeed, double maxAutoStrafe, double maxAutoTurn) {
        this.maxAutoSpeed = maxAutoSpeed;
        this.maxAutoStrafe = maxAutoStrafe;
        this.maxAutoTurn = maxAutoTurn;
    }

    public void setGains(double speedGain, double strafeGain, double turnGain) {
        this.speedGain = speedGain;
        this.strafeGain = strafeGain;
        this.turnGain = turnGain;
    }

    public void setDesiredDistance(double desiredDistance) {
        this.desiredDistance = desiredDistance;
    }

    public void setDesiredAutoDistance(double desiredAutoDistance) {
        this.desiredAutoDistance = desiredAutoDistance;
    }

    public void setDesiredBlueTagID(int id) {
        desiredBlueTagID = id;
    }

    public void setDesiredRedTagID(int id) {
        desiredRedTagID = id;
    }

    // initialize AprilTag processor
    public void initAprilTag(HardwareMap hardwareMap) {
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(2);

        // Create the vision portal by using a builder.
        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTag)
                    .build();
        }
    }

    // TagID:
    // BLUE-
    // 3 aligns to middle
    // 2 aligns to left
    // RED-
    // 6 aligns to middle
    // 5 aligns to left
    public void moveToAprilTag(int aprilTagId) {
        while (loop) {
            targetFound = false;
            desiredTag = null;

            // Step through the list of detected tags and look for a matching tag
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                // Look to see if we have size info on this tag.
                if (detection.metadata != null) {
                    //  Check to see if we want to track towards this tag.
                    if ((detection.id == desiredBlueTagID) || (detection.id == desiredRedTagID)) {
                        // Yes, we want to use this tag.
                        targetFound = true;
                        desiredTag = detection;
                        break;  // don't look any further.
                    }
                }
            }

            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            double rangeError   = (desiredTag.ftcPose.range - desiredAutoDistance);
            double headingError = desiredTag.ftcPose.bearing;
            double yawError     = desiredTag.ftcPose.yaw;

            // Use the speed and turn "gains" to calculate how we want the robot to move.
            drive  = Range.clip(rangeError * speedGain, -maxAutoSpeed, maxAutoSpeed);
            rotate = Range.clip(headingError * turnGain, -maxAutoTurn, maxAutoTurn) ;
            strafe = Range.clip(-yawError * strafeGain, -maxAutoStrafe, maxAutoStrafe);

            // Calculate wheel powers.
            double leftFrontPower  = drive - strafe - rotate;
            double rightFrontPower = drive + strafe + rotate;
            double leftBackPower   = drive + strafe - rotate;
            double rightBackPower  = drive - strafe + rotate;

            // Normalize wheel powers to be less than 1.0
            double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            leftFrontDrive.setPower(leftFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightFrontDrive.setPower(rightFrontPower);
            rightBackDrive.setPower(rightBackPower);

            if (desiredTag == null) {
                loop = false;
            }
        }
    }
}