package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/* CONTROLS
   A: PRECISION MODE
   Y: SCORING POSITION
*/
public class LinearSlide {

    private DcMotor linearSlideLeft = null;
    private DcMotor linearSlideRight = null;
    public int maxPosition = 0;
    public double power = 0;
    public int tickChange = 0;
    public boolean precisionMode = false;
    public int precisionLoop = 0;
    private double speedModeLimiter = 0;
    public int scoringLoop = 0;
    public boolean scoringBool = false;
    public int scoringPosition = 0;
    public int position = 0;
    public double tempPower = 0;
    public int zeroLoop = 0;
    public double defaultPowerPercentage = 0.0;


    public LinearSlide(HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
        // motor for left linear slide, sets up encoders
        linearSlideLeft = hardwareMap.get(DcMotor.class, "l_slide");
        linearSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn the motor back on when we are done
        linearSlideLeft.setTargetPosition(0);
        linearSlideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideLeft.setDirection(direction);

        // motor for right linear slide, sets up encoders
        linearSlideRight = hardwareMap.get(DcMotor.class, "r_slide");
        linearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn the motor back on when we are done
        linearSlideRight.setTargetPosition(0);
        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideRight.setDirection(direction.inverted());
    }

    public void slide(Gamepad gamepad, Telemetry telemetry) {
        slideDPad(gamepad, telemetry);
    }

    private void slideDPad(Gamepad gamepad, Telemetry telemetry) {
        precisionModeSwitch(gamepad);
        scoringPosition(gamepad);
        if (scoringBool) {
            position = scoringPosition;
            power = tempPower;
            zeroLoop = 0;
        }
        else {
            backToZero();
        }

        linearSlideLeft.setTargetPosition(-position);
        linearSlideRight.setTargetPosition(-position);
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // logic for toggle on-off of the linear slide to go back to zero.
        if (position == 0 && !scoringBool) {
            if (linearSlideLeft.getCurrentPosition() != 0) {
                // set power to get back to zero
                linearSlideLeft.setPower(power);
                linearSlideRight.setPower(power);
            } else {
                // if we're at zero, set power to zero to not stall the motors.
                linearSlideLeft.setPower(0.0);
                linearSlideRight.setPower(0.0);
            }

        }
        else if (linearSlideLeft.getCurrentPosition() < position) {
            linearSlideLeft.setPower(power);
            linearSlideRight.setPower(power);
        }
        else if (linearSlideLeft.getCurrentPosition() > position) {
            linearSlideLeft.setPower(-power);
            linearSlideRight.setPower(-power);
        }

        int currentPositionLeft = linearSlideLeft.getCurrentPosition();
        int currentPositionRight = linearSlideRight.getCurrentPosition();
        telemetry.addData("Current Left Slide Position", currentPositionLeft);
        telemetry.addData("Current Right Slide Position", currentPositionRight);
        telemetry.addData("Slide Goal Position", position);
        telemetry.addData("Slide Precision Mode Status", precisionMode);
        telemetry.addData("Linear Slide Power", power);
        telemetry.addData("Linear Slide Temp Power", tempPower);
        telemetry.addData("Scoring Position Boolean", scoringBool);
    }

    // switches precision mode on or off if press a
    public void precisionModeSwitch(Gamepad gamepad) {
        if (gamepad.a) {
            if (precisionLoop == 0) {
                precisionMode = !precisionMode;
                precisionLoop++;
            }
        }
        else {
            precisionLoop = 0;
        }
    }

    public void scoringPosition(Gamepad gamepad) {
        if (gamepad.y) {
            if (scoringLoop == 0) {
                scoringBool = !scoringBool;
                scoringLoop++;
            }
        }
        else {
            scoringLoop = 0;
        }
    }

    public void backToZero() {
        position = 0;
        if (zeroLoop == 0) {
            tempPower = power;
        }
        if (linearSlideLeft.getCurrentPosition() > -10 && linearSlideLeft.getCurrentPosition() < 20) {
            power = 0;
        }
        zeroLoop++;
    }

    public void setSlidePower(double power) {
        this.power = power;
    }

    public void setTickChange(int tickChange) {
        this.tickChange = tickChange;
    }

    public void setMaxPosition(int maxTicks) {
        this.maxPosition = maxTicks;
    }

    public void setSpeedModeLimiter(double speedModeLimiter) {
        this.speedModeLimiter = speedModeLimiter;
    }
    public void setScoringPosition(int position) {
        scoringPosition = position;
    }

    public void setDefaultPowerPercentage(double percentage) {
        defaultPowerPercentage = percentage;
    }

    public void slideScore(Telemetry telemetry) {
        int LScurrentPosition = linearSlideLeft.getCurrentPosition();
        linearSlideLeft.setTargetPosition(scoringPosition);
        linearSlideRight.setTargetPosition(scoringPosition);
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideLeft.setPower(power);
        linearSlideRight.setPower(power);
        telemetry.addData("Current Slide Power: ", power);
        telemetry.addData("Current Left Slide Pos: ", LScurrentPosition);
        telemetry.addData("Target Left Slide Pos: ", linearSlideLeft.getTargetPosition());
        telemetry.update();
    }

    public void slideDown(Telemetry telemetry) {
        int LScurrentPosition = linearSlideLeft.getCurrentPosition();
        linearSlideLeft.setTargetPosition(0);
        linearSlideRight.setTargetPosition(0);
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideLeft.setPower(power);
        linearSlideRight.setPower(power);
        telemetry.addData("Current Slide Power: ", power);
        telemetry.addData("Current Left Slide Pos: ", LScurrentPosition);
        telemetry.addData("Target Left Slide Pos: ", linearSlideLeft.getTargetPosition());
        telemetry.update();
    }
}