package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//import org.firstinspires.ftc.teamcode.lib.LinearSlide;
//import org.firstinspires.ftc.teamcode.lib.Swivel;
import org.firstinspires.ftc.teamcode.lib.NewLinearSlide;
import org.firstinspires.ftc.teamcode.lib.ModLinearSlide;
import org.firstinspires.ftc.teamcode.lib.ModSwivel;
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

//        NewSwivel swivel = new NewSwivel(hardwareMap, DcMotorSimple.Direction.FORWARD);
//        swivel.setSwivelPower(0.5);
//        swivel.setTickChange(125);
//        swivel.setMaxPosition(3000);
//        swivel.setSpecimenPosition(1400);
//        swivel.setBasketPosition(1440);
//        swivel.setIntakeSubPosition(200);
//        swivel.setZeroPosition(0);

        ModSwivel swivel = new ModSwivel(hardwareMap, DcMotorSimple.Direction.FORWARD);
        swivel.setSwivelPower(0.5);
        swivel.setMaxPosition(3000);


//        NewLinearSlide linearSlide = new NewLinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
//        linearSlide.setSlidePower(1);
//        linearSlide.setMaxPosition(3680);
//        linearSlide.setTickChange(60);
//        linearSlide.setBasketPosition(3600);
//        linearSlide.setSpecimenPosition(1440);
//        linearSlide.setZeroPosition(0);
//        linearSlide.setMaxPositionForDown(2775);

        ModLinearSlide linearSlide = new ModLinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
        linearSlide.setSlidePower(1);
        linearSlide.setMaxPosition(3680);

        linearSlide.setBasketPositionSlides(3600);
        linearSlide.setSpecimenPositionSlides(1440);

        linearSlide.setIntakeSubPositionSwivel(200);
        linearSlide.setSpecimenPositionSwivel(1400);
        linearSlide.setBasketPositionSwivel(1440);

        linearSlide.setMaxPositionForDown(2775);
        linearSlide.setZeroPosition(0);

        linearSlide.setTickChange(60);


        waitForStart();

        while(opModeIsActive()) {
            claw.powerServo(gamepad1, telemetry);
            mecanumDrive.drive(gamepad1);
            linearSlide.slide(gamepad2, telemetry, swivel
                    .getSwivelIsZero(), swivel);
            telemetry.update();
        }
    }}