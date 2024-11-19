package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.lib.LinearSlide;
import org.firstinspires.ftc.teamcode.lib.Swivel;


@TeleOp(name="TESTING NEW!! :)", group="Linear Opmode")
public class Robot extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive mecanumDrive = new MecanumDrive(hardwareMap, telemetry);
        mecanumDrive.setPrecisionPower(0.75);
        mecanumDrive.setStrafeConstant(1.1);
        mecanumDrive.setDefaultSpeed(1.0);

        Claw claw = new Claw(hardwareMap, telemetry);
//
        Swivel swivel = new Swivel(hardwareMap, DcMotorSimple.Direction.FORWARD);
        swivel.setSwivelPower(1);
        swivel.setMaxPosition(3000);
        swivel.setTickChange(300);
        swivel.setSpeedModeLimiter(0.5);
        swivel.setScoringPosition(1200);
        swivel.setDefaultPowerPercentage(1);
//
        LinearSlide linearSlide = new LinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
        linearSlide.setSlidePower(1);
        linearSlide.setMaxPosition(3200);
        linearSlide.setTickChange(200);
        linearSlide.setSpeedModeLimiter(1);
        linearSlide.setScoringPosition(2000);
        linearSlide.setDefaultPowerPercentage(1.0);

        waitForStart();

        while(opModeIsActive()) {
            claw.powerServo(gamepad2, telemetry);
            mecanumDrive.drive(gamepad1);
            linearSlide.slide(gamepad2, telemetry);
            swivel.swivel(gamepad2, telemetry);
            telemetry.update();
        }
    }}