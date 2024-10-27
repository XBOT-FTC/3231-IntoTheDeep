package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Testing intake and claw", group="Linear Opmode")
public class Robot extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
//        AlanYuanMecanum drive = new AlanYuanMecanum();
        MecanumDrive mecanumDrive = new MecanumDrive(hardwareMap, telemetry);
        mecanumDrive.setPrecisionPower(0.25);

        Intaker intaker = new Intaker(hardwareMap, telemetry);

        Claw claw = new Claw(hardwareMap, telemetry);

        Climber climber = new Climber(hardwareMap, telemetry);
        climber.setClimbPosition(500, 1000, 1500);
        climber.setTicksRateOfChange(100);
        climber.setMotorPower(1.0);
        climber.setMaxExtensionTicks(20000);

        Lifter lifter = new Lifter(hardwareMap, telemetry);
        lifter.setClimbPosition(1000, 2000, 3000);
        lifter.setTicksRateOfChange(200);
        lifter.setMotorPower(0.5);
        lifter.setMaxExtensionTicks(10000);

        Slider slider = new Slider(hardwareMap, telemetry);
        slider.setClimbPosition(1000, 2000, 3000);
        slider.setTicksRateOfChange(5);
        slider.setMotorPower(0.5);
        slider.setMaxExtensionTicks(10000);

        waitForStart();

        while(opModeIsActive()) {
            intaker.startIntaker(gamepad2, telemetry);
            claw.powerServo(gamepad2, telemetry);
//            drive.runOpMode();
            climber.controls(gamepad1);
            lifter.controls(gamepad2);
            slider.controls(gamepad2);
        }
}}
