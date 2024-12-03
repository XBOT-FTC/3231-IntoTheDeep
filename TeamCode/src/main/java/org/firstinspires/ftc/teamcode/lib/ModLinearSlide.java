package org.firstinspires.ftc.teamcode.lib;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.NewSwivel;
/* CONTROLS
   A: PRECISION MODE
   Y: SCORING POSITION
*/
public class ModLinearSlide {

    private DcMotor linearSlideLeft = null;
    private DcMotor linearSlideRight = null;
    public int maxPosition = 0;
    public int maxPositionSwivel = 0;
    public double power = 0;
    public int positionPreset = 0;
    public int manualPosition = 0;

    public int specimenPositionSlides = 0;
    public int basketPositionSlides = 0;
    public int maxPositionDownSlides = 0;

    public int specimenPositionSwivel = 0;
    public int basketPositionSwivel = 0;
    public int intakeSubPositionSwivel = 0;
    public int zeroPosition = 0;

    public int thresholdUp = 110;
    public int thresholdDown = 30;
    public int tickChange = 0;
    public int tickChangeSwivel = 0;

    public boolean aPress = false;
    public boolean manualMode = false;

    public ModLinearSlide(HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
        linearSlideLeft = hardwareMap.get(DcMotor.class, "l_slide");
        linearSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn the motor back on when we are done
        linearSlideLeft.setTargetPosition(0);
        linearSlideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideLeft.setDirection(direction);

        linearSlideRight = hardwareMap.get(DcMotor.class, "r_slide");
        linearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn the motor back on when we are done
        linearSlideRight.setTargetPosition(0);
        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideRight.setDirection(direction.inverted());

    }

    public void slide(Gamepad gamepad, Telemetry telemetry, boolean swivelIsZero, ModSwivel swivel) {
        toggleManualMode(gamepad, swivel);
        slideToPresetPosition(setScoringPosition(gamepad, swivelIsZero, swivel, telemetry), telemetry);
    }

    public void toggleManualMode(Gamepad gamepad, ModSwivel swivel) {
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

        this.manualMode(gamepad, swivel);
    }

    public void manualMode(Gamepad gamepad, ModSwivel swivel) {
        if (manualMode) {
            if (gamepad.right_trigger > 0) {
                manualPosition += tickChangeSwivel;
                manualPosition = Math.min(manualPosition, maxPositionSwivel);
            } else {
                manualPosition = 0;
            }
        }

        swivel.swivelToPresetPosition(manualPosition, telemetry);
    }

    public int setScoringPosition(Gamepad gamepad, boolean swivelIsZero, ModSwivel swivel, Telemetry telemetry) {
        // ---- BASKET ----
        if (gamepad.dpad_up) {
            telemetry.addLine("D PAD UP ");
            swivel.swivelToPresetPosition(basketPositionSwivel, telemetry);
            if (swivel.getSwivelPosition() >= basketPositionSwivel - thresholdUp) {
                positionPreset = basketPositionSlides;
            }
        } else if (gamepad.dpad_left) {
            telemetry.addLine("D PAD LEFT ");
            swivel.swivelToPresetPosition(specimenPositionSwivel, telemetry);
            if (swivel.getSwivelPosition() >= specimenPositionSwivel - thresholdUp) {
                positionPreset = specimenPositionSlides;
            }
        } else if (gamepad.left_trigger > 0) {
            positionPreset += tickChange;
            positionPreset = Math.min(positionPreset, maxPosition);
        } else {
            telemetry.addLine("No button press ");
            positionPreset = zeroPosition;
            if (linearSlideLeft.getCurrentPosition() <= thresholdDown ||
                    linearSlideRight.getCurrentPosition() <= thresholdDown) {
                swivel.swivelToPresetPosition(zeroPosition, telemetry);
            }
        }

        return positionPreset;
    }

    public void slideToPresetPosition(int scoringPosition, Telemetry telemetry) {
        linearSlideLeft.setTargetPosition(-scoringPosition);
        linearSlideRight.setTargetPosition(-scoringPosition);
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideLeft.setPower(power);
        linearSlideRight.setPower(power);

        if (Math.abs(scoringPosition - linearSlideRight.getCurrentPosition()) < 25 || Math.abs(scoringPosition - linearSlideLeft.getCurrentPosition()) < 25) {
            linearSlideLeft.setPower(0);
            linearSlideRight.setPower(0);
            telemetry.addLine("WITHIN 25 TICKS LINEAR SLIDES");
        }

        int currentPositionLeft = linearSlideLeft.getCurrentPosition();
        int currentPositionRight = linearSlideRight.getCurrentPosition();
        telemetry.addData("Current Left Slide Position", currentPositionLeft);
        telemetry.addData("Current Right Slide Position", currentPositionRight);
        telemetry.addData("Target Left Slide Position", linearSlideLeft.getTargetPosition());
        telemetry.addData("Target Right Slide Position", linearSlideRight.getTargetPosition());
        telemetry.addData("Slide Goal Position", scoringPosition);
        telemetry.addData("Linear Slide Power", power);
        telemetry.addData("Linear Slide left Actual Power", linearSlideLeft.getPower());
        telemetry.addData("Linear Slide right Actual Power", linearSlideRight.getPower());
    }


    public void setSlidePower(double power) {
        this.power = power;
    }

    public void setMaxPosition(int maxTicks) {
        this.maxPosition = maxTicks;
    }

    public void setMaxPositionForDown(int maxTicks) {
        this.maxPositionDownSlides = maxTicks;
    }

    public void setMaxPositionSwivel(int maxTicks) {
        maxPositionSwivel = maxTicks;
    }

    public void setSpecimenPositionSlides(int position) {
        specimenPositionSlides = position;
    }

    public void setSpecimenPositionSwivel(int position) {
        specimenPositionSwivel = position;
    }

    public void setBasketPositionSlides(int position) {
        basketPositionSlides = position;
    }

    public void setBasketPositionSwivel(int position) {
        basketPositionSwivel = position;
    }

    public void setZeroPosition(int position) {
        zeroPosition = position;
    }

    public void setIntakeSubPositionSwivel(int position) {
        intakeSubPositionSwivel = position;
    }

    public void setTickChange(int change) {
        tickChange = change;
    }

    public void setTickChangeSwivel(int change) {
        tickChangeSwivel = change;
    }

    public void scoreBasketPosition(ModSwivel swivel, Telemetry telemetry) {
        swivel.swivelToPresetPosition(basketPositionSwivel, telemetry);
        if (swivel.getSwivelPosition() >= basketPositionSwivel - thresholdUp) {
            slideToPresetPosition(basketPositionSlides, telemetry);
        }
    }

    public void scoreSpecimenPosition(ModSwivel swivel, Telemetry telemetry) {
        swivel.swivelToPresetPosition(specimenPositionSwivel, telemetry);
        if (swivel.getSwivelPosition() >= specimenPositionSwivel - thresholdUp) {
            slideToPresetPosition(specimenPositionSlides, telemetry);
        }
    }

    public void setSwivelAndLinearSlidesDown(ModSwivel swivel, Telemetry telemetry) {
        slideToPresetPosition(zeroPosition, telemetry);
        if (linearSlideLeft.getCurrentPosition() <= thresholdDown ||
                linearSlideRight.getCurrentPosition() <= thresholdDown) {
            swivel.swivelToPresetPosition(zeroPosition, telemetry);
        }
    }

}


