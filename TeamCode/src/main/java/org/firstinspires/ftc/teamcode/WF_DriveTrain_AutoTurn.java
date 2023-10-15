/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.opencv.core.Core;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.opencv.core.Size;

@TeleOp(name="Omni TeleOp Auto-Turn", group="Linear Opmode")
//@Disabled
public class WF_DriveTrain_AutoTurn extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    Servo servo;
    private final ElapsedTime runtime = new ElapsedTime();
    private DrivingFunctions df = null;
    private void Initialize()
    {
        df = new DrivingFunctions(this);
    }

    @Override
    public void runOpMode() {
        Initialize();

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // Speed factor to slow down the robot, goes from 0.1 to 1.0
        servo = hardwareMap.get(Servo.class, "PixelReleaseServo");
        double speedFactor = 0.5;
        double position = 1;
        double kp = -0.033;
        boolean isAutoTurning = false;
        double autoTurningStart = 0.0;
        double autoTurningTarget = 0.0;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            previousGamepad1.copy(currentGamepad1);
            currentGamepad1.copy(gamepad1);

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double y = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double yaw = gamepad1.right_stick_x;

            if (gamepad1.back)
                df.ResetYaw();

            double botHeading = df.GetHeading();
            double rotatingSpeed = df.GetRotatingSpeed();
            // Field centric driving is activated when any of the hat buttons are pressed
            if (gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right) {
                x = gamepad1.dpad_left ? -1 : (gamepad1.dpad_right ? 1 : 0);
                y = gamepad1.dpad_up ? 1 : (gamepad1.dpad_down ? -1 : 0);
                double botHeadingRadians = Math.toRadians(botHeading);
                // Rotate the movement direction counter to the bot's rotation
                double newX = x * Math.cos(-botHeadingRadians) - y * Math.sin(-botHeadingRadians);
                double newY = x * Math.sin(-botHeadingRadians) + y * Math.cos(-botHeadingRadians);
                x = newX;
                y = newY;
            }

            if (!previousGamepad1.left_bumper && currentGamepad1.left_bumper)
                speedFactor = 0.5;
            if (!previousGamepad1.right_bumper && currentGamepad1.right_bumper)
                speedFactor = 1;
            if (currentGamepad1.start)
                df.PutPixelInBackBoard();

            if (currentGamepad1.right_trigger > 0.5)
                if (position <= 1) {
                    position += 0.01;
                }
            if (currentGamepad1.left_trigger > 0.5)
                if (position >= 0.5) {
                    position -= 0.01;
                }
            if (position > 0.5)
                servo.setPosition(position);
            if (previousGamepad1.left_trigger != 0 && currentGamepad1.left_trigger == 0 && speedFactor > 0.1)
                speedFactor -= 0.1;
            if (previousGamepad1.right_trigger != 0 && currentGamepad1.right_trigger == 0 && speedFactor < 1.0)
                speedFactor += 0.1;

            if (!isAutoTurning &&
                    ((!previousGamepad1.y && currentGamepad1.y) || (!previousGamepad1.x && currentGamepad1.x) ||
                            (!previousGamepad1.b && currentGamepad1.b) || (!previousGamepad1.a && currentGamepad1.a))) {
                isAutoTurning = true;
                autoTurningTarget = currentGamepad1.y ? 0.0 : currentGamepad1.x ? 90.0 : currentGamepad1.b ? -90.0 : 179.9;
                autoTurningStart = runtime.milliseconds();
            }

            if (isAutoTurning)
            {
                double currentTime = runtime.milliseconds();
                double deltaDegrees = (autoTurningTarget - botHeading + 540) % 360 - 180;
                if ((Math.abs(deltaDegrees) < 1.0 && Math.abs(rotatingSpeed) < 2.0) || (currentTime - autoTurningStart > 1300.0)) {
                    isAutoTurning = false;
                }
                else {
                    yaw = kp * deltaDegrees / speedFactor;
                }
            }

            df.MoveRobot(x, y, yaw, speedFactor);

            telemetry.addData("Speed Factor", "%1.1f", speedFactor);
            telemetry.addData("pos var", "%1.1f", position);
            telemetry.addData("position", "%1.1f", servo.getPosition());
            telemetry.addData("Bot Heading", "%4.2f", botHeading);
            telemetry.addData("X/Y/Yaw", "%4.2f, %4.2f, %4.2f", x, y, yaw);
            telemetry.update();
        }
    }
}
