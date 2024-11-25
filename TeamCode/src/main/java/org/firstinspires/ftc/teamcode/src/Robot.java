package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//import org.firstinspires.ftc.teamcode.lib.LinearSlide;
//import org.firstinspires.ftc.teamcode.lib.Swivel;
import org.firstinspires.ftc.teamcode.lib.NewLinearSlide;
import org.firstinspires.ftc.teamcode.lib.NewSwivel;


@TeleOp(name="TESTING NEW!! :)", group="Linear Opmode")
public class Robot extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive mecanumDrive = new MecanumDrive(hardwareMap, telemetry);
        mecanumDrive.setPrecisionPower(0.3);
        mecanumDrive.setStrafeConstant(1.1);
        mecanumDrive.setDefaultSpeed(1.0);

        Claw claw = new Claw(hardwareMap, telemetry);
//
//        Swivel swivel = new Swivel(hardwareMap, DcMotorSimple.Direction.FORWARD);
//        swivel.setSwivelPower(1);
//        swivel.setMaxPosition(3000);
//        swivel.setTickChange(300);
//        swivel.setSpeedModeLimiter(0.5);
//        swivel.setDefaultPowerPercentage(1);
//
//        swivel.setHangingPosition(1000);
//        swivel.setBasketPosition(1750);
//        swivel.setIntakeUpPosition(300);

        NewSwivel swivel = new NewSwivel(hardwareMap, DcMotorSimple.Direction.FORWARD);
        swivel.setSwivelPower(0.5);
        swivel.setTickChange(125);
        swivel.setMaxPosition(3000);
        swivel.setSpecimenPosition(1400);
        swivel.setBasketPosition(1440);
        swivel.setIntakeSubPosition(200);
        swivel.setZeroPosition(0);

//
//        LinearSlide linearSlide = new LinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
//        linearSlide.setSlidePower(1);
//        linearSlide.setMaxPosition(3200);
//        linearSlide.setTickChange(200);
//        linearSlide.setSpeedModeLimiter(1);
//        linearSlide.setScoringPosition(3000);
//        linearSlide.setDefaultPowerPercentage(1.0);

        NewLinearSlide linearSlide = new NewLinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
        linearSlide.setSlidePower(1);
        linearSlide.setMaxPosition(3680);
        linearSlide.setTickChange(60);
        linearSlide.setBasketPosition(3600);
        linearSlide.setSpecimenPosition(1440);
        linearSlide.setZeroPosition(0);
        linearSlide.setMaxPositionForDown(2775);

        waitForStart();

        while(opModeIsActive()) {
            claw.powerServo(gamepad2, telemetry);
            mecanumDrive.drive(gamepad1);
            linearSlide.slide(gamepad1, telemetry, swivel
                    .getSwivelIsZero());
            swivel.swivel(gamepad2, telemetry);
            telemetry.update();
        }
    }}