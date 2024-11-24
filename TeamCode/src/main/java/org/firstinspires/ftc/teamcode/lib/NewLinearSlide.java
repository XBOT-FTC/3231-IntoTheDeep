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
public class NewLinearSlide {

    private DcMotor linearSlideLeft = null;
    private DcMotor linearSlideRight = null;
    public int maxPosition = 0;
    public double power = 0;
    public int tickChange = 0;
    public int positionPreset = 0;
    public int positionManual = 0;
    public int scoringPosition = 0;
    public int specimenPosition = 0;
    public int basketPosition = 0;
    public int zeroPosition = 0;

    public boolean dpadUpPress = false;
    public boolean dpadLeftPress = false;
    public boolean dpadDownPress = false;

    public boolean manualMode = false;
    public boolean aPress = false;

    public NewLinearSlide(HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
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
        toggleManualMode(gamepad);
        slideToPresetPosition(setScoringPosition(gamepad), telemetry, gamepad);
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

        return positionPreset;
    }

    public void slideToPresetPosition(int scoringPosition, Telemetry telemetry, Gamepad gamepad) {
        if (manualMode) {
            slideToPositionManual(gamepad, telemetry);
        } else {
            linearSlideLeft.setTargetPosition(-scoringPosition);
            linearSlideRight.setTargetPosition(-scoringPosition);
            linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearSlideLeft.setPower(power);
            linearSlideRight.setPower(power);

            if (Math.abs(scoringPosition - linearSlideRight.getCurrentPosition()) < 25) {
                if (Math.abs(scoringPosition - linearSlideLeft.getCurrentPosition()) < 25) {
                    linearSlideLeft.setPower(0);
                    linearSlideRight.setPower(0);
                    telemetry.addLine("WITHIN 25 ticks LINEAR SLIDES");
                }
            }

            int currentPositionLeft = linearSlideLeft.getCurrentPosition();
            int currentPositionRight = linearSlideRight.getCurrentPosition();
            telemetry.addData("Current Left Slide Position", currentPositionLeft);
            telemetry.addData("Current Right Slide Position", currentPositionRight);
            telemetry.addData("Slide Goal Position", scoringPosition);
            telemetry.addData("Linear Slide Power", power);
            telemetry.addData("Linear Slide left Actual Power", linearSlideLeft.getPower());
            telemetry.addData("Linear Slide right Actual Power", linearSlideRight.getPower());
            telemetry.addData("Manual Mode status", manualMode);
        }
    }

    public void slideToPositionManual(Gamepad gamepad, Telemetry telemetry) {
        if (gamepad.right_trigger > 0) {
            positionManual += tickChange;
            positionManual = Math.min(positionManual, maxPosition);
        } else if (gamepad.left_trigger > 0) {
            positionManual -= tickChange;
            positionManual = Math.max(positionManual, 0);
        }

        linearSlideLeft.setTargetPosition(-positionManual);
        linearSlideRight.setTargetPosition(-positionManual);
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideLeft.setPower(power);
        linearSlideRight.setPower(power);

        int currentPositionLeft = linearSlideLeft.getCurrentPosition();
        int currentPositionRight = linearSlideRight.getCurrentPosition();
        telemetry.addData("Current Left Slide Position", currentPositionLeft);
        telemetry.addData("Current Right Slide Position", currentPositionRight);
        telemetry.addData("Slide Goal Position", scoringPosition);
        telemetry.addData("Linear Slide Power", power);
        telemetry.addData("Right trigger SLIDES", gamepad.right_trigger);
        telemetry.addData("Left trigger SLIDES", gamepad.left_trigger);
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

    public void setPosition(int position) {
        scoringPosition = position;
    }

    public void setSpecimenPosition(int position) {
        specimenPosition = position;
    }

    public void setBasketPosition(int position) {
        basketPosition = position;
    }

    public void setZeroPosition(int position) {
        zeroPosition = position;
    }

}