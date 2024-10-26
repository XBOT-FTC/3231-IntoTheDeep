package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Slider {
    private DcMotor slider = null;
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

    public Slider(HardwareMap hardwareMap, Telemetry telemetry) {
        this.slider = hardwareMap.get(DcMotor.class, "slider");
        this.slider.setDirection(DcMotorSimple.Direction.FORWARD);
        this.slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.telemetry = telemetry;
    }

    public void controls(Gamepad gamepad) {
        detectMode(gamepad);
        slide(gamepad);

        telemetry.addData("Manual Mode", manual);
        telemetry.addData("Climber Current Position", this.slider.getCurrentPosition());
        telemetry.addData("Climber Target Position", this.slider.getTargetPosition());
        telemetry.update();
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

    public void slide(Gamepad gamepad) {
        int targetPosition = this.slider.getCurrentPosition();
        if (manual) {
            // Manual mode
            if (gamepad.dpad_up) {
                targetPosition += ticksRateOfChange;
                targetPosition = Math.min(targetPosition, maxExtensionTicks);
            } else if (gamepad.dpad_down) {
                targetPosition -= ticksRateOfChange;
                targetPosition = Math.max(targetPosition, 0);
            }
        } else {
            // Auto Mode
            if (gamepad.dpad_up) {
                targetPosition = -levelTwo;
            } else if (gamepad.dpad_down) {
                targetPosition = 7000;
            }
        }
        this.slider.setTargetPosition(targetPosition);
        this.slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.slider.setPower(this.power);

        // If the target and current is within a threshold, set power to 0
        if (Math.abs(slider.getTargetPosition() -
                slider.getCurrentPosition()) < 20) {
            this.slider.setPower(0);
        }

        telemetry.addData("Current SLIDER POS: ", slider.getCurrentPosition());
        telemetry.addData("Current SLIDER TARGET: ", slider.getTargetPosition());
        telemetry.addData("Current SLIDER POWER:", slider.getPower());

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
