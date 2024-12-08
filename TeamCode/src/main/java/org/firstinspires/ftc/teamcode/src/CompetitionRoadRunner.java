package org.firstinspires.ftc.teamcode.src;

import androidx.annotation.NonNull;

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

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

    public Claw claw;

    public ModSwivel swivel;

    public ModLinearSlide linearSlide;

    int slidePosition;

    int swivelPosition;

    @Override
    public void runOpMode() throws InterruptedException {

        slidePosition = 0;
        swivelPosition = 0;

        claw = new Claw(hardwareMap, telemetry);

        swivel = new ModSwivel(hardwareMap, DcMotorSimple.Direction.FORWARD);
        swivel.setSwivelPower(.5 );
        swivel.setMaxPosition(3000);

        linearSlide = new ModLinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
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


        Pose2d initialPose = new Pose2d(35.5, 63, Math.toRadians(360)); // TODO: CHANGE THIS
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        Action tab = drive.actionBuilder(initialPose)
                .setTangent(Math.toRadians(360))

                .strafeToLinearHeading(new Vector2d(35.5,62), Math.toRadians(360))

                .splineToLinearHeading(new Pose2d(54.8, 54.8, Math.toRadians(45)), Math.toRadians(91))

                .afterTime(.5, new InstantAction(() -> {
                    swivelPosition = 1440;
                }))
                .afterTime(2.5, new InstantAction(() -> {
                    slidePosition = 3600;
                }))

                .afterTime(5, new InstantAction(() -> {
                    claw.open();
                }))

                .afterTime(5.5, new InstantAction(() -> {
                    slidePosition = 0;
                }))

                .afterTime(7.9, new InstantAction(() -> {
                    swivelPosition = 0;
                })) // score preloaded piece

                .waitSeconds(8)

                .splineToLinearHeading(new Pose2d(50.75, 45, Math.toRadians(270)), Math.toRadians(89))

                .waitSeconds(.3)

                .lineToY(37.5)

                .afterTime(.5, new InstantAction(() -> {
                    claw.close(); // grab 1st piece
                }))

                .waitSeconds(.8)

                .lineToY(45)

                .waitSeconds(.3)

                .splineToLinearHeading(new Pose2d(54.8, 54.8, Math.toRadians(40)), Math.toRadians(89))

                .afterTime(.5, new InstantAction(() -> {
                    swivelPosition = 1440;
                }))
                .afterTime(2.5, new InstantAction(() -> {
                    slidePosition = 3600;
                }))

                .afterTime(5, new InstantAction(() -> {
                    claw.open();
                }))

                .afterTime(5.5, new InstantAction(() -> {
                    slidePosition = 0;
                }))

                .afterTime(7.9, new InstantAction(() -> {
                    swivelPosition = 0;
                })) // score 1st piece

                .waitSeconds(8)

                .splineToLinearHeading(new Pose2d(61.75, 45, Math.toRadians(270)), Math.toRadians(89))

                .lineToY(37.5)

                .afterTime(.5, new InstantAction(() -> {
                    claw.close();
                })) // grab 2nd piece

                .waitSeconds(.8)

                .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(45)), Math.toRadians(89))

                .afterTime(.5, new InstantAction(() -> {
                    swivelPosition = 1440;
                }))
                .afterTime(2.5, new InstantAction(() -> {
                    slidePosition = 3600;
                }))

                .afterTime(5, new InstantAction(() -> {
                    claw.open();
                }))

                .afterTime(5.5, new InstantAction(() -> {
                    claw.close();
                }))

                .afterTime(6, new InstantAction(() -> {
                    slidePosition = 0;
                }))

                .afterTime(8.4, new InstantAction(() -> {
                    swivelPosition = 0;
                }))  // score 2nd piece

                .waitSeconds(8.5)

                .turn(Math.toRadians(225))
                .waitSeconds(.5)
                .splineToLinearHeading(new Pose2d(26, 12, Math.toRadians(180)), Math.toRadians(89))
                .waitSeconds(.5) // park

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
                        initSystems(),
                        new ParallelAction(
                                tab,
                                slideMovement(telemetry),
                                swivelMovement(telemetry)
                        )
//                        Example
//                        trajectoryActionChosen,
//                        lift.liftUp(),
//                        claw.openClaw(),
//                        lift.liftDown(),
//                        trajectoryActionCloseOut
                )
        );
    }

    public Action slideMovement(Telemetry telemetry) {
        return new Action() {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    initialized = true;
                }

                linearSlide.slideToPresetPositionAuto(slidePosition, telemetry);
                telemetry.addData("Roadrunner, LinearSlide Current Position", linearSlide.linearSlideLeft.getCurrentPosition());
                telemetry.addData("Roadrunner, LinearSlide Target Position", slidePosition);
                telemetry.addData("Roadrunner, LinearSlide Power Position", linearSlide.linearSlideLeft.getPower());
//            if (Math.abs(linearSlide.linearSlideLeft.getCurrentPosition() - slidePosition) < 30
//                    || Math.abs(linearSlide.linearSlideRight.getCurrentPosition() - slidePosition) < 30) {
//                return false;
//            }
                return true;
            }
        };
    }

    public Action swivelMovement(Telemetry telemetry) {
        return new Action() {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    initialized = true;
                }

                swivel.swivelToPresetPosition(swivelPosition, telemetry);
                telemetry.addData("Roadrunner, Swivel Current Position", swivel.getSwivelPosition());
                telemetry.addData("Roadrunner, Swivel Target Position", swivelPosition);
                telemetry.addData("Roadrunner, Swivel Power Position", swivel.swivel.getPower());
//                if (Math.abs(swivel.swivel.getCurrentPosition() - swivelPosition) < 30) {
//                    return false;
//                }
                return true;
            }
        };
    }

    public Action initSystems() {
        return new SequentialAction(
                new InstantAction(() -> {
                    claw.close();
                })
        );
    }
}