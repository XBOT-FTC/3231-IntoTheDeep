/* Copyright (c) 2017 FIRST. All rights reserved.
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

package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/*
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="AlanMecanum", group="Linear OpMode")
//@Disabled

public class MecanumDrive {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    boolean precisionToggle = false;
    boolean pressed = false;
    double precisionPower = 1;

    Telemetry telemetry;

    public MecanumDrive(HardwareMap hardwareMap, Telemetry telemetry) {
        this.frontLeftDrive  = hardwareMap.get(DcMotor.class, "fl_drive"); //0
        this.frontRightDrive = hardwareMap.get(DcMotor.class, "fr_drive"); //3
        this.backLeftDrive = hardwareMap.get(DcMotor.class, "bl_drive"); //1
        this.backRightDrive = hardwareMap.get(DcMotor.class, "br_drive"); //2

        this.frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        this.frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        this.backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        this.backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        this.telemetry = telemetry;
    }

    public void drive(Gamepad gamepad) {
        double FLposition = frontLeftDrive.getCurrentPosition();
        double FRposition = frontRightDrive.getCurrentPosition();

        double BLposition = backLeftDrive.getCurrentPosition();
        double BRposition = backRightDrive.getCurrentPosition();

        double y = -gamepad.left_stick_y;
        double x = gamepad.left_stick_x;
        double rx  =  gamepad.right_stick_x;

        if (gamepad.a == true && pressed == false) {
            precisionToggle = !precisionToggle;
            pressed = true;
        }
        else {
            pressed = false;
        }

        // Send calculated power to wheels
        if (precisionToggle) {
            frontLeftDrive.setPower((y + x + rx) * precisionPower);
            backLeftDrive.setPower((y - x + rx) * precisionPower);
            frontRightDrive.setPower((y - x - rx) * precisionPower);
            backRightDrive.setPower((y + x - rx) * precisionPower);
        }
        else {
            frontLeftDrive.setPower(y + x + rx);
            backLeftDrive.setPower(y - x + rx);
            frontRightDrive.setPower(y - x - rx);
            backRightDrive.setPower(y + x - rx);
        }

        // Show the elapsed game time and wheel power.
//        telemetry.addData("Encoder", "frontLeft (%.2f), backLeft (%.2f), frontRight (%.2f), backRight (%.2f)", FLposition, FRposition, BLposition, BRposition);
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "frontLeft (%.2f), backLeft (%.2f), frontRight (%.2f), backRight (%.2f)", frontLeftDrive.getPower(), backLeftDrive.getPower(), frontRightDrive.getPower(), backRightDrive.getPower());
        telemetry.update();

    }
    public void setPrecisionPower(double power) {
        this.precisionPower = power;
    }
}