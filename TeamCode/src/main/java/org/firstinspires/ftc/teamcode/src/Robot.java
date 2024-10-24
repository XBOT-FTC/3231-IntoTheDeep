package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Testing intake and claw", group="Linear Opmode")
public class Robot extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Intaker intaker = new Intaker(hardwareMap, telemetry);
        Claw claw = new Claw(hardwareMap, telemetry);


        waitForStart();

        while(opModeIsActive()) {
            intaker.startIntaker(gamepad1, telemetry);
            claw.powerServo(gamepad1, telemetry);
        }
}}
