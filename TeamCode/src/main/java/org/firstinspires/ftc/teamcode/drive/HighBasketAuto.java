package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.src.Claw;
import org.firstinspires.ftc.teamcode.lib.ModLinearSlide;
import org.firstinspires.ftc.teamcode.lib.ModSwivel;
import org.firstinspires.ftc.teamcode.src.Claw;

@Disabled
@Autonomous(name = "Basket Auto", group = "Linear OpMode")
public class HighBasketAuto extends LinearOpMode {
    @Override
    public void runOpMode() {

        Claw claw = new Claw(hardwareMap, telemetry);
        ModLinearSlide slide = new ModLinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
        ModSwivel swivel = new ModSwivel(hardwareMap,DcMotorSimple.Direction.FORWARD);
        Pose2d startingPos = new Pose2d(-24,63, Math.toRadians(-90));
//                .splineTo(new Vector2d(0,37), Math.toRadians(-90))
//                .waitSeconds(1)
//                .forward(-5)
//                .splineTo(new Vector2d(-47, 47), Math.toRadians(90))
//                .waitSeconds(1)
//                .turn(Math.toRadians(180))
//                .forward(15)
//                .waitSeconds(1)
//                .splineTo(new Vector2d(0,37), Math.toRadians(-90))
//                .waitSeconds(1)
//                .splineTo(new Vector2d(-58, 47), Math.toRadians(-90))
//                .waitSeconds(1)
//                .splineTo(new Vector2d(0,37), Math.toRadians(-90))
//                .waitSeconds(1)
//                .splineTo(new Vector2d(-47,62), Math.toRadians(90))
//                .waitSeconds(1)
//                .splineTo(new Vector2d(-56, 27), Math.toRadians(180))
//                .waitSeconds(1)
//                .splineTo(new Vector2d(-47,62), Math.toRadians(0))
//                .waitSeconds(1);
        MecanumDrive drive = new MecanumDrive(hardwareMap,startingPos);
    }
}