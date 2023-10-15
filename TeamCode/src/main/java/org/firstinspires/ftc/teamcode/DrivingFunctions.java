package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class DrivingFunctions {
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private Servo pixelReleaseServo = null;
    private IMU imu = null;
    private LinearOpMode lom = null;
    private double headingError = 0.0;
    private final ElapsedTime runtime = new ElapsedTime();
    static final int TURN_TIMEOUT_MILLISECONDS = 3000;
    static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;   // Our motors are these: https://www.gobilda.com/5203-series-yellow-jacket-planetary-gear-motor-19-2-1-ratio-24mm-length-8mm-rex-shaft-312-rpm-3-3-5v-encoder/
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 3.78 ;     // These are our Mecanum wheels (96mm) - https://www.gobilda.com/96mm-mecanum-wheel-set-70a-durometer-bearing-supported-rollers/
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     HEADING_THRESHOLD       = 1.0 ;    // How close must the heading get to the target before moving to next step.
    static final double     P_TURN_GAIN            = 0.035;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_GAIN           = 0.03;     // Larger is more responsive, but also less stable
    static final int     SERVO_SMOOTH_MOVE_STEPS   = 20;     // Larger is smoother, but potentially slower
    static final double   PIXEL_RELESE_SERVO_INIT_POS   = 0.8;
    public DrivingFunctions(LinearOpMode l)
    {
        lom = l;
        Initialize();
    }
    private void Initialize()
    {
        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive  = lom.hardwareMap.get(DcMotor.class, "frontleft");
        leftBackDrive  = lom.hardwareMap.get(DcMotor.class, "backleft");
        rightFrontDrive = lom.hardwareMap.get(DcMotor.class, "frontright");
        rightBackDrive = lom.hardwareMap.get(DcMotor.class, "backright");
        pixelReleaseServo = lom.hardwareMap.get(Servo .class, "PixelReleaseServo");

        imu = lom.hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Ensure the robot is stationary.  Reset the encoders and set the motors to BRAKE mode
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        StopMotors();
        ResetYaw();

        pixelReleaseServo.setPosition(PIXEL_RELESE_SERVO_INIT_POS);
    }

    public void ResetYaw()
    {
        imu.resetYaw();
    }
    public void PutPixelInBackBoard()
    {
        MoveServoSmoothly(pixelReleaseServo, 1.0, 1500);
        lom.sleep(500);
        MoveServoSmoothly(pixelReleaseServo, PIXEL_RELESE_SERVO_INIT_POS, 500);
    }

    private void MoveServoSmoothly(Servo s, double endPosition, int timeInMilliseconds)
    {
        double stepSize = (endPosition - s.getPosition()) / SERVO_SMOOTH_MOVE_STEPS;
        long sleepTime = timeInMilliseconds / SERVO_SMOOTH_MOVE_STEPS;
        double position = s.getPosition();

        for (int i=0; i < SERVO_SMOOTH_MOVE_STEPS; i++)
        {
            position += stepSize;
            s.setPosition(position);
            lom.sleep(sleepTime);
        }
    }

    public void TestEncoders()
    {
        while(lom.opModeIsActive())
        {
            lom.telemetry.addData("Left Front Position: ",  "%7d", leftFrontDrive.getCurrentPosition());
            lom.telemetry.addData("Right Front Position: ",  "%7d", rightFrontDrive.getCurrentPosition());
            lom.telemetry.addData("Left Back Position: ",  "%7d", leftBackDrive.getCurrentPosition());
            lom.telemetry.addData("Right Back Position: ",  "%7d", rightBackDrive.getCurrentPosition());

            lom.telemetry.addData("Heading:", "%5.0f", GetHeading());
            lom.telemetry.update();
        }
    }

    /**
     *  Drive in a straight line, on a fixed compass heading (angle), based on encoder counts.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the desired position
     *  2) Driver stops the OpMode running.
     *
     * @param maxDriveSpeed MAX Speed for forward/rev motion (range 0 to +1.0) .
     * @param distance   Distance (in inches) to move from current position.  Negative distance means move backward.
     * @param heading      Absolute Heading Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from the current robotHeading.
     */
    public void DriveStraight(double maxDriveSpeed,
                              double distance,
                              double heading) {

        // Ensure that the OpMode is still active
        if (lom.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            int moveCounts = (int)(distance * COUNTS_PER_INCH);

            // Set Target FIRST, then turn on RUN_TO_POSITION
            leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + moveCounts);
            rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + moveCounts);
            leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + moveCounts);
            rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + moveCounts);

            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Set the required driving speed  (must be positive for RUN_TO_POSITION)
            // Start driving straight, and then enter the control loop
            maxDriveSpeed = Math.abs(maxDriveSpeed);
            MoveRobot(0, maxDriveSpeed, 0, 1.0);

            // keep looping while we are still active, and BOTH motors are running.
            while (lom.opModeIsActive() &&
                    (leftFrontDrive.isBusy() && rightFrontDrive.isBusy() && leftBackDrive.isBusy() && rightBackDrive.isBusy())) {
                // Determine required steering to keep on heading
                double turnSpeed = GetSteeringCorrection(heading, P_DRIVE_GAIN);

                // if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0)
                    turnSpeed *= -1.0;

                // Apply the turning correction to the current driving speed.
                MoveRobot(0, maxDriveSpeed, turnSpeed, 1.0);
            }
            StopMotors();
        }
    }
    private void StopMotors()
    {
        // Stop all motion & Turn off RUN_TO_POSITION
        MoveRobot(0, 0, 0, 1.0);
        // Set the encoders for closed loop speed control
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     *  Spin on the central axis to point in a new direction.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the heading (angle)
     *  2) Driver stops the OpMode running.
     *
     * @param maxTurnSpeed Desired MAX speed of turn. (range 0 to +1.0)
     * @param heading Absolute Heading Angle (in Degrees) relative to last gyro reset.
     *              0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *              If a relative angle is required, add/subtract from current heading.
     */
    public void TurnToHeading(double maxTurnSpeed, double heading) {

        double startTime = runtime.milliseconds();
        // Run getSteeringCorrection() once to pre-calculate the current error
        GetSteeringCorrection(heading, P_DRIVE_GAIN);

        // keep looping while we are still active, and not on heading.
        while ((runtime.milliseconds() - startTime) < TURN_TIMEOUT_MILLISECONDS &&
                lom.opModeIsActive() && (Math.abs(this.headingError) > HEADING_THRESHOLD)) {

            // Determine required steering to keep on heading
            double turnSpeed = GetSteeringCorrection(heading, P_TURN_GAIN);

            // Clip the speed to the maximum permitted value.
            turnSpeed = Range.clip(turnSpeed, -maxTurnSpeed, maxTurnSpeed);

            // Pivot in place by applying the turning correction
            MoveRobot(0, 0, turnSpeed, 1.0);
        }
        // Stop all motion;
        StopMotors();
    }

    // **********  LOW Level driving functions.  ********************

    /**
     * Use a Proportional Controller to determine how much steering correction is required.
     *
     * @param desiredHeading        The desired absolute heading (relative to last heading reset)
     * @param proportionalGain      Gain factor applied to heading error to obtain turning power.
     * @return                      Turning power needed to get to required heading.
     */
    public double GetSteeringCorrection(double desiredHeading, double proportionalGain) {
        // Determine the heading current error
        this.headingError = desiredHeading - GetHeading();

        // Normalize the error to be within +/- 180 degrees
        while (this.headingError > 180)  this.headingError -= 360;
        while (this.headingError <= -180) this.headingError += 360;

        // Multiply the error by the gain to determine the required steering correction/  Limit the result to +/- 1.0
        return Range.clip(this.headingError * proportionalGain, -1, 1);
    }
    public void MoveRobot(double x, double y, double yaw, double speedFactor)
    {
        double max;
        double leftFrontPower  = y + x + yaw;
        double rightFrontPower = y - x - yaw;
        double leftBackPower   = y - x + yaw;
        double rightBackPower  = y + x - yaw;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // Send calculated power to wheels
        leftFrontDrive.setPower(leftFrontPower * speedFactor);
        rightFrontDrive.setPower(rightFrontPower * speedFactor);
        leftBackDrive.setPower(leftBackPower * speedFactor);
        rightBackDrive.setPower(rightBackPower * speedFactor);
    }

    public double GetHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public double GetRotatingSpeed()
    {
        return imu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate;
    }

}
