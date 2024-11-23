package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lifter {
    private DcMotor lifter = null;
    private int ticksRateOfChange = 0;

    private int startingPosition = 0;

    private int levelOne = 0;
    private int levelTwo = 0;
    private int levelThree = 0;
    private int maxExtensionTicks = 0;

    private boolean manualToggle = false;
    private boolean manual = false;

    private double power = 0;

    Telemetry telemetry = null;

    public Lifter(HardwareMap hardwareMap, Telemetry telemetry) {
        this.lifter = hardwareMap.get(DcMotor.class, "lifter");
        this.lifter.setDirection(DcMotorSimple.Direction.FORWARD);
        this.lifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.lifter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.lifter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.telemetry = telemetry;
    }

    public void controls(Gamepad gamepad) {
        detectMode(gamepad);
        lift(gamepad);
    }

    public void detectMode(Gamepad gamepad) {
        // Toggles between manual and auto mode
        if (gamepad.a && !manualToggle) {
            manualToggle = true;
        }
        if (!gamepad.a && manualToggle) {
            manual = !manual; // flips the mode
            manualToggle = false;
        }
    }

    public void lift(Gamepad gamepad) {
        int targetPosition = this.lifter.getCurrentPosition();
        if (manual) {
            // Manual mode
            if (gamepad.left_bumper) {
                targetPosition += ticksRateOfChange;
                targetPosition = Math.min(targetPosition, maxExtensionTicks);
            } else if (gamepad.right_bumper) {
                targetPosition -= ticksRateOfChange;
                targetPosition = Math.max(targetPosition, 0);
            }
        } else {
            // Auto Mode
            if (gamepad.left_bumper) {
                targetPosition = -levelThree;
            } else if (gamepad.right_bumper) {
                targetPosition = 5000;
            }
        }
        this.lifter.setTargetPosition(targetPosition);
        this.lifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.lifter.setPower(this.power);

        // If the target and current is within a threshold, set power to 0

        telemetry.addData("Current LIFTER POS: ", lifter.getCurrentPosition());
        telemetry.addData("Current LIFTER TARGET: ", lifter.getTargetPosition());
        telemetry.addData("Current LIFTER POWER:", lifter.getPower());

        telemetry.update();

    }

    public void setClimbPosition(int levelOne, int levelTwo, int levelThree) {
        this.levelOne = levelOne;
        this.levelTwo = levelTwo;
        this.levelThree = levelThree;
    }

    public void setMotorPower(double power) {
        this.power = power;
    }

    public void setTicksRateOfChange(int ticks) {
        this.ticksRateOfChange = ticks;
    }

    public void setMaxExtensionTicks(int maxTicks) {
        this.maxExtensionTicks = maxTicks;
    }
}
