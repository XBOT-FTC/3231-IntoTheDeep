package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.lib.NewLinearSlide;
import org.firstinspires.ftc.teamcode.lib.NewSwivel;

@Autonomous(name="District 2 Auto", group="group")
public class CompetitionRoadRunner extends LinearOpMode {
    /**
     * Override this method and place your code here.
     * <p>
     * Please do not catch {@link InterruptedException}s that are thrown in your OpMode
     * unless you are doing it to perform some brief cleanup, in which case you must exit
     * immediately afterward. Once the OpMode has been told to stop, your ability to
     * control hardware will be limited.
     *
     * @throws InterruptedException When the OpMode is stopped while calling a method
     *                              that can throw {@link InterruptedException}
     */
    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive = new MecanumDrive(hardwareMap, telemetry);
        Claw claw = new Claw(hardwareMap, telemetry);

        NewSwivel swivel = new NewSwivel(hardwareMap, DcMotorSimple.Direction.FORWARD);
        swivel.setSwivelPower(0.5);
        swivel.setTickChange(40);
        swivel.setMaxPosition(3000);
        swivel.setSpecimenPosition(1400);
        swivel.setBasketPosition(1440);
        swivel.setIntakeUpPosition(200);
        swivel.setZeroPosition(0);

        NewLinearSlide linearSlide = new NewLinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
        linearSlide.setSlidePower(1);
        linearSlide.setMaxPosition(3680);
        linearSlide.setTickChange(60);
        linearSlide.setBasketPosition(3600);
        linearSlide.setSpecimenPosition(1440);
        linearSlide.setZeroPosition(0);
        linearSlide.setMaxPositionForDown(2775);

        waitForStart();
    }
}
