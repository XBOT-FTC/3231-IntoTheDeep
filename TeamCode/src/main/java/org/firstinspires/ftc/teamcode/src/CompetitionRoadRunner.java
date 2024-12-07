package org.firstinspires.ftc.teamcode.src;

import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
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

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.lib.ModLinearSlide;
import org.firstinspires.ftc.teamcode.lib.ModSwivel;

@Autonomous(name="Basket Auto", group="Auto")
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

        Claw claw = new Claw(hardwareMap, telemetry);

        ModSwivel swivel = new ModSwivel(hardwareMap, DcMotorSimple.Direction.FORWARD);
        swivel.setSwivelPower(0.5);
        swivel.setMaxPosition(3000);

        ModLinearSlide linearSlide = new ModLinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
        linearSlide.setSlidePower(1);
        linearSlide.setMaxPosition(3680);
        linearSlide.setMaxPositionForDown(2775);
        linearSlide.setTickChange(60);
        linearSlide.setZeroPosition(0);
        linearSlide.setBasketPositionSlides(3600);
        linearSlide.setSpecimenPositionSlides(1440);
        linearSlide.setIntakeSubPositionSwivel(200);
        linearSlide.setSpecimenPositionSwivel(1400);
        linearSlide.setBasketPositionSwivel(1440);


        Pose2d initialPose = new Pose2d(24, 63, Math.toRadians(270)); // TODO: CHANGE THIS
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        Action tab = drive.actionBuilder(initialPose)
                .setTangent(Math.toRadians(270))

                .splineToLinearHeading(new Pose2d(56, 56, Math.toRadians(45)), Math.toRadians(91))

                .afterTime(0.5, new InstantAction(() -> {
                    linearSlide.scoreBasketPosition(swivel, telemetry);
                }))

                .afterTime(4, new InstantAction(() -> {
                    claw.open();
                }))

                .afterTime(1, new InstantAction(() -> {
                    claw.close();
                }))

                .afterTime(.5, new InstantAction(() -> {
                    linearSlide.setSwivelAndLinearSlidesDown(swivel, telemetry);
                })) //preloaded piece

                .splineToLinearHeading(new Pose2d(49, 40, Math.toRadians(270)), Math.toRadians(89))

                .afterTime(0, new InstantAction(() -> {
                    claw.open();
                }))

                .afterTime(1, new InstantAction(() -> {
                    claw.close(); //grab 1st piece
                }))


                .splineToLinearHeading(new Pose2d(56, 56, Math.toRadians(45)), Math.toRadians(89))
                .waitSeconds(.5)

                .afterTime(0.5, new InstantAction(() -> {
                    linearSlide.scoreBasketPosition(swivel, telemetry);
                }))

                .afterTime(4, new InstantAction(() -> {
                    claw.open();
                }))

                .afterTime(1, new InstantAction(() -> {
                    claw.close();
                }))

                .afterTime(.5, new InstantAction(() -> {
                    linearSlide.setSwivelAndLinearSlidesDown(swivel, telemetry);
                })) //score 1st piece

                .splineToLinearHeading(new Pose2d(58, 40, Math.toRadians(270)), Math.toRadians(89))
                .waitSeconds(.5)

                .afterTime(0, new InstantAction(() -> {
                    claw.open();
                }))

                .afterTime(1, new InstantAction(() -> {
                    claw.close();
                })) //grab 2nd piece

                .splineToLinearHeading(new Pose2d(56, 56, Math.toRadians(45)), Math.toRadians(89))
                .waitSeconds(.5)

                .afterTime(0.5, new InstantAction(() -> {
                    linearSlide.scoreBasketPosition(swivel, telemetry);
                }))

                .afterTime(4, new InstantAction(() -> {
                    claw.open();
                }))

                .afterTime(1, new InstantAction(() -> {
                    claw.close();
                }))

                .afterTime(.5, new InstantAction(() -> {
                    linearSlide.setSwivelAndLinearSlidesDown(swivel, telemetry);
                })) //score 2nd piece

                .turn(Math.toRadians(225))
                .waitSeconds(.5)
                .splineToLinearHeading(new Pose2d(26, 12, Math.toRadians(180)), Math.toRadians(89))
                .waitSeconds(.5) //park

                .build();


        // Initialization
        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", 1);
            telemetry.update();
        }

        if (isStopRequested()) return;

        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        tab
//                        Example
//                        trajectoryActionChosen,
//                        lift.liftUp(),
//                        claw.openClaw(),
//                        lift.liftDown(),
//                        trajectoryActionCloseOut
                )
        );
    }
}
