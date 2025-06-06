package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="SimepleMecanumDrive", group="Linear OpMode")
public class SimpleMecanumDrive extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app).
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "fl_drive"); // 1
        frontRightDrive = hardwareMap.get(DcMotor.class, "fr_drive"); // 3
        backLeftDrive = hardwareMap.get(DcMotor.class, "bl_drive"); // 0
        backRightDrive = hardwareMap.get(DcMotor.class, "br_drive"); // 2

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);


        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            double FLposition = frontLeftDrive.getCurrentPosition();
            double FRposition = frontRightDrive.getCurrentPosition();

            double BLposition = backLeftDrive.getCurrentPosition();
            double BRposition = backRightDrive.getCurrentPosition();

            // Setup a variable for each drive wheel to save power level for telemetry
            double frontLeftPower;
            double backLeftPower;
            double frontRightPower;
            double backRightPower;


            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx  =  gamepad1.right_stick_x;

            frontLeftDrive.setPower(y + x + rx);
            backLeftDrive.setPower(y - x + rx);
            frontRightDrive.setPower(y - x - rx);
            backRightDrive.setPower(y + x - rx);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Encoder", "frontLeft (%.2f), backLeft (%.2f), frontRight (%.2f), backRight (%.2f)", FLposition, FRposition, BLposition, BRposition);
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "frontLeft (%.2f), backLeft (%.2f), frontRight (%.2f), backRight (%.2f)", frontLeftDrive.getPower(), backLeftDrive.getPower(), frontRightDrive.getPower(), backRightDrive.getPower());
            telemetry.update();
        }
    }
}
