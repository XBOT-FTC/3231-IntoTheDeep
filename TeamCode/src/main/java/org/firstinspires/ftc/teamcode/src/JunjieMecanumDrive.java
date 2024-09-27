package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(group = "MacanumDrive", name = "idk mode")
public class JunjieMecanumDrive extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;

    @Override
    public void runOpMode() throws InterruptedException {
        //TODO: change the device name compatible with our device.
        leftFrontDrive = hardwareMap.get(DcMotor.class, "lf");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rf");
        leftBackDrive = hardwareMap.get(DcMotor.class, "lb");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rb");

        //setting the directions
        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();
        runtime.reset();

        //cancels the while loop if something happens before hand
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y;
            //I am not gonna implement moving in x axis
            //double x = gamepad1.left_stick_x;

            leftFrontDrive.setPower(y);
            leftBackDrive.setPower(y);
            rightFrontDrive.setPower(-y);
            rightBackDrive.setPower(-y);
            Thread thread = new Thread(new Logger(y, telemetry));
            thread.start();
        }
    }
    //I am just trying out threads I might ruin the performance
    //It might also leak memory if there is a cleanup method for threads?
    public static class Logger implements Runnable {
        double power;
        Telemetry telemetry;
        public Logger(double power, Telemetry telemetry) {
            this.power = power;
            this.telemetry = telemetry;
        }
        @Override
        public void run() {
            this.telemetry.addData("Motor power", String.valueOf(this.power));
        }
    }
}