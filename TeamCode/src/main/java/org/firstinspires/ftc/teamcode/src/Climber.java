package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Climber {
    private DcMotor climberMotor = null;
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

    public Climber(HardwareMap hardwareMap, Telemetry telemetry) {
        this.climberMotor = hardwareMap.get(DcMotor.class, "Climber");
        this.climberMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.climberMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.climberMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.climberMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.telemetry = telemetry;
    }

    public void detectMode(Gamepad gamepad) {
        // Toggles between manual and auto mode
        if (gamepad.a) {
            manualToggle = true;
        }
        if (!gamepad.a && manualToggle) {
            manual = !manual; // flips the mode
            manualToggle = false;
        }
    }

    public void climb(Gamepad gamepad) {
        int targetPosition = this.climberMotor.getCurrentPosition();
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
            if (gamepad.dpad_right) {
                targetPosition = levelOne;
            } else if (gamepad.dpad_up) {
                targetPosition = levelTwo;
            } else if (gamepad.dpad_left) {
                targetPosition = levelThree;
            } else if (gamepad.dpad_down) {
                targetPosition = this.startingPosition;
            }
        }
        this.climberMotor.setTargetPosition(targetPosition);
        this.climberMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // If the target and current is within a threshold, set power to 0
        if (Math.abs(climberMotor.getTargetPosition() -
                climberMotor.getCurrentPosition()) < 20) {
            this.climberMotor.setPower(0);
        }
    }

    public void setClimbPosition(int levelOne, int levelTwo, int levelThree) {
        this.levelOne = levelOne;
        this.levelTwo = levelTwo;
        this.levelThree = levelThree;
    }
}
