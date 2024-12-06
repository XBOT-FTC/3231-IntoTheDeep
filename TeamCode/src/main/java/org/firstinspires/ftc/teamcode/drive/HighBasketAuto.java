//package org.firstinspires.ftc.teamcode.drive;
//
//import com.acmerobotics.roadrunner.Pose2d;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.MecanumDrive;
//import org.firstinspires.ftc.teamcode.lib.Grabber;
//import org.firstinspires.ftc.teamcode.lib.LinearSlide;
//import org.firstinspires.ftc.teamcode.lib.Swivel;
//import org.firstinspires.ftc.teamcode.src.Claw;
//
//@Disabled
//@Autonomous(name = "Basket Auto", group = "Linear OpMode")
//public class HighBasketAuto extends LinearOpMode {
//    @Override
//    public void runOpMode() {
//
//        Claw claw = new Claw(hardwareMap, telemetry);
//        LinearSlide slide = new LinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
//        Swivel swivel = new Swivel(hardwareMap,DcMotorSimple.Direction.FORWARD);
//        Pose2d startingPos = new Pose2d(-24,63, Math.toRadians(-90));
//
//        MecanumDrive drive = new MecanumDrive(hardwareMap,startingPos);
//    }
//}
