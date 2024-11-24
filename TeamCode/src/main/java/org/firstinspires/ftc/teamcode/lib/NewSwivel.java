package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class NewSwivel {

    private DcMotor swivel = null;
    public int maxPosition = 0;
    public double power = 0;
    public int tickChange = 0;
    public int positionPreset = 0;
    public int positionManual = 0;
    public int specimenPosition = 0;
    public int basketPosition = 0;
    public int intakeUpPosition = 0;
    public int zeroPosition = 0;

    public boolean dpadUpPress = false;
    public boolean dpadDownPress = false;
    public boolean dpadLeftPress = false;
    public boolean dpadRightPress = false;

    public boolean manualMode = false;
    public boolean aPress = false;

    public NewSwivel(HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
        swivel = hardwareMap.get(DcMotor.class, "swivel");
        swivel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        swivel.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn the motor back on when we are done
        swivel.setTargetPosition(0);
        swivel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        swivel.setDirection(direction);

    }

    public void swivel(Gamepad gamepad, Telemetry telemetry) {
        toggleManualMode(gamepad);
        swivelToPresetPosition(setScoringPosition(gamepad), telemetry, gamepad);
    }

    public boolean toggleManualMode(Gamepad gamepad) {
        if (gamepad.a) {
            if (!aPress) {
                aPress = true;
            }
        } else {
            if (aPress) {
                aPress = false;
                manualMode = !manualMode;
            }
        }
        return manualMode;
    }

    public int setScoringPosition(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            if (!dpadUpPress) {
                dpadUpPress = true;
                positionPreset = basketPosition;
            }
        } else {
            if (dpadUpPress) {
                dpadUpPress = false;
            }
        }

        if (gamepad.dpad_down) {
            if (!dpadDownPress) {
                dpadDownPress = true;
                positionPreset = zeroPosition;
            }
        } else {
            if (dpadDownPress) {
                dpadDownPress = false;
            }
        }

        if (gamepad.dpad_left) {
            if (!dpadLeftPress) {
                dpadLeftPress = true;
                positionPreset = specimenPosition;
            }
        } else {
            if (dpadLeftPress) {
                dpadLeftPress = false;
            }
        }

        if (gamepad.dpad_right) {
            if (!dpadRightPress) {
                dpadRightPress = true;
                positionPreset = intakeUpPosition;
            }
        } else {
            if (dpadRightPress) {
                dpadRightPress = false;
            }
        }

        return positionPreset;
    }

    public void swivelToPresetPosition(int scoringPosition, Telemetry telemetry, Gamepad gamepad) {
        if (manualMode) {
            swivelToPositionManual(gamepad, telemetry);
        } else {
            swivel.setTargetPosition(scoringPosition);
            swivel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            swivel.setPower(power);
        }

        if (Math.abs(scoringPosition - swivel.getCurrentPosition()) < 25) {
            swivel.setPower(0);
            telemetry.addLine("WITHIN 25 ticks SWIVEL");
        }

        int currentPosition = swivel.getCurrentPosition();
        telemetry.addData("Current Swivel Position", currentPosition);
        telemetry.addData("Swivel Goal Position", scoringPosition);
        telemetry.addData("Swivel Power", power);
        telemetry.addData("Actual Power", swivel.getPower());
    }

    public void swivelToPositionManual(Gamepad gamepad, Telemetry telemetry) {
        if (gamepad.right_trigger > 0) {
            positionManual += tickChange;
            positionManual = Math.min(positionManual, maxPosition);
        } else if (gamepad.left_trigger > 0) {
            positionManual -= tickChange;
            positionManual = Math.max(positionManual, 0);
        }

        if (Math.abs(swivel.getCurrentPosition()) < 25) {
            swivel.setPower(0);
            telemetry.addLine("WITHIN 25 ticks SWIVEL MANUAL");
        }

        swivel.setTargetPosition(positionManual);
        swivel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        swivel.setPower(power);

        int currentPosition = swivel.getCurrentPosition();
        telemetry.addData("Current Swivel Position", currentPosition);
        telemetry.addData("Swivel Goal Position", positionManual);
        telemetry.addData("Swivel Power", power);
        telemetry.addData("Actual Power", swivel.getPower());
        telemetry.addData("Right trigger SWIVEL", gamepad.right_trigger);
        telemetry.addData("Left trigger SWIVEL", gamepad.left_trigger);
    }

    public void setSwivelPower(double power) {
        this.power = power;
    }

    public void setTickChange(int tickChange) {
        this.tickChange = tickChange;
    }

    public void setMaxPosition(int maxTicks) {
        this.maxPosition = maxTicks;
    }

    public void setSpecimenPosition(int position) {
        specimenPosition = position;
    }

    public void setBasketPosition(int position) {
        basketPosition = position;
    }

    public void setIntakeUpPosition(int position) {
        intakeUpPosition = position;
    }

    public void setZeroPosition(int position) {
        zeroPosition = position;
    }

    public boolean getSwivelIsZero() {
        return swivel.getCurrentPosition() < 25;
    }
}
