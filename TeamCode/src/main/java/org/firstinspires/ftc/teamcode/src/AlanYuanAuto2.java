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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.camera.delegating.DelegatingCaptureSequence;


/*
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an Fh * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@Autonomous(name="Ascent Zone: Forward", group="Linear OpMode")
//@Disabled
public class AlanYuanAuto2 extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "fl_drive"); //0
        frontRightDrive = hardwareMap.get(DcMotor.class, "fr_drive"); //3
        backLeftDrive = hardwareMap.get(DcMotor.class, "bl_drive"); //1
        backRightDrive = hardwareMap.get(DcMotor.class, "br_drive"); //2

        waitForStart();

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double FLposition = frontLeftDrive.getCurrentPosition();
        double FRposition = frontRightDrive.getCurrentPosition();
        double BLposition = backLeftDrive.getCurrentPosition();
        double BRposition = backRightDrive.getCurrentPosition();

        backRightDrive.setTargetPosition(1000);
        backLeftDrive.setTargetPosition(1000);
        frontRightDrive.setTargetPosition(1000);
        frontLeftDrive.setTargetPosition(1000);

        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRightDrive.setPower(.5);
        backLeftDrive.setPower(.5);
        frontRightDrive.setPower(.5);
        frontLeftDrive.setPower(.5);

        while(frontLeftDrive.isBusy() && frontRightDrive.isBusy() && backLeftDrive.isBusy() && backRightDrive.isBusy()) {
            telemetry.addData("Posititions", "frontLeft (%d), backLeft (%d), frontRight (%d), backRight (%d)", frontLeftDrive.getCurrentPosition(), frontRightDrive.getCurrentPosition(), backLeftDrive.getCurrentPosition(), backRightDrive.getCurrentPosition());
            telemetry.update();
        }

        moveStop();



        //CODE STARTS HERE


//        moveLinear(.3,2000, "forward");
//        moveRotation(3,2000, "right");
//        moveLinear(.3,2000, "forward");
//        moveLinear(.3,2000, "left");
//        moveLinear(.3,2000, "backward");
//        moveStop();





    }
    public void moveLinear(double speed, int time, String direction) throws InterruptedException {
        if (direction.equalsIgnoreCase("Forward")) {
            setPower(speed, speed, speed, speed);
        }
        if (direction.equalsIgnoreCase("Backward")) {
            setPower(-speed, -speed, -speed, -speed);
        }
        if (direction.equalsIgnoreCase("Left")) {
            setPower(-speed, speed, speed, -speed);
        }
        if (direction.equalsIgnoreCase("Right")) {
            setPower(speed, -speed, -speed, speed);
        }
        Thread.sleep(time);
    }

    public void setPower(double FLspeed, double FRspeed, double BLspeed, double BRspeed) {
        frontLeftDrive.setPower(FLspeed);
        frontRightDrive.setPower(FRspeed);
        backLeftDrive.setPower(BLspeed);
        backRightDrive.setPower(BRspeed);
    }


    public void moveStop() {
        setPower(0,0,0,0);
    }

    public void moveRotation(double speed, int time, String direction) throws InterruptedException{
        if (direction.equalsIgnoreCase("Left")) {
            setPower(-speed, speed, -speed, speed);
        }
        if (direction.equalsIgnoreCase("Right")) {
            setPower(speed, -speed, speed, -speed);
        }
        Thread.sleep(time);
    }

}
