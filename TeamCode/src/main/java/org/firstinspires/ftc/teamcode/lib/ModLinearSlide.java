package org.firstinspires.ftc.teamcode.lib;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;
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

    public DcMotor linearSlideLeft = null;
    public DcMotor linearSlideRight = null;
    public int maxPosition = 0;
    public int maxPositionSwivel = 0;
    public double power = 0;
    public int positionPreset = 0;
    public int manualPositionSlides = 0;
    public int manualPositionSwivel = 0;

    public int specimenPositionSlides = 0;
    public int basketPositionSlides = 0;
    public int maxPositionDownSlides = 0;

    public int specimenPositionSwivel = 0;
    public int basketPositionSwivel = 0;
    public int intakeSubPositionSwivel = 0;
    public int zeroPosition = 0;

    public int thresholdUp = 110;
    public int thresholdDown = 300;
    public int tickChange = 0;
    public int tickChangeSwivel = 0;

    public boolean aPress = false;
    public boolean manualMode = false;
    private long lastUpdateTime = 0;
    private final long updateInterval = 50;

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
        toggleManualMode(gamepad);
        slideToPresetPosition(setScoringPosition(gamepad, swivel, telemetry), telemetry, swivelIsZero, gamepad, swivel);
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

    public void slidesManual(Gamepad gamepad, Telemetry telemetry, ModSwivel swivel) {
        if (gamepad.left_trigger > 0) {
            if (!swivel.getSwivelIsAtBasket(basketPositionSwivel, maxPositionSwivel)) {
                manualPositionSlides += tickChange;
                manualPositionSlides = Math.min(manualPositionSlides, maxPositionDownSlides);
            } else {
                telemetry.addLine("ELSE ELSE ELSE ELSE ELSE ELSE ELSE");
                manualPositionSlides += tickChange;
                manualPositionSlides = Math.min(manualPositionSlides, maxPosition);
            }
        } else if (gamepad.right_trigger > 0) {
            manualPositionSlides -= tickChange;
            if (!swivel.getSwivelIsAtBasket(basketPositionSwivel, maxPositionSwivel)) {
                manualPositionSlides = Math.max(maxPositionDownSlides, 0);
            } else {
                manualPositionSlides = Math.max(maxPosition, 0);
            }
        }

        linearSlideLeft.setTargetPosition(-manualPositionSlides);
        linearSlideRight.setTargetPosition(-manualPositionSlides);
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideLeft.setPower(power);
        linearSlideRight.setPower(power);

        if (manualPositionSlides == 0 &&
                Math.abs(linearSlideLeft.getCurrentPosition()) < 25 || Math.abs(linearSlideRight.getCurrentPosition()) < 25) {
            linearSlideLeft.setPower(0);
            linearSlideRight.setPower(0);
            telemetry.addLine("WITHIN 25 TICKS LINEAR SLIDES");
        }

        int currentPositionLeft = linearSlideLeft.getCurrentPosition();
        int currentPositionRight = linearSlideRight.getCurrentPosition();
        telemetry.addData("Swivel at Basket Position", swivel.getSwivelIsAtBasket(basketPositionSwivel, maxPositionSwivel));
        telemetry.addData("Current Left Slide Position", currentPositionLeft);
        telemetry.addData("Current Right Slide Position", currentPositionRight);
        telemetry.addData("Target Left Slide Position", linearSlideLeft.getTargetPosition());
        telemetry.addData("Target Right Slide Position", linearSlideRight.getTargetPosition());
        telemetry.addData("Slide Goal Position", manualPositionSlides);
        telemetry.addData("Linear Slide Power", power);
        telemetry.addData("Linear Slide left Actual Power", linearSlideLeft.getPower());
        telemetry.addData("Linear Slide right Actual Power", linearSlideRight.getPower());
    }

    public void swivelManual(Gamepad gamepad, ModSwivel swivel, Telemetry telemetry) {
        if (gamepad.left_bumper) {
            manualPositionSwivel = swivel.getSwivelPosition() + tickChangeSwivel;
            manualPositionSwivel = Math.min(manualPositionSwivel, maxPositionSwivel);
            telemetry.addLine("BROOOOOOOOOOOOO");
            telemetry.addData("MANUAL POSITION SWIVEL", manualPositionSwivel);
            swivel.swivelToPresetPosition(manualPositionSwivel, telemetry);

        } else if (gamepad.right_bumper) {
            manualPositionSwivel = swivel.getSwivelPosition() - tickChangeSwivel;
            manualPositionSwivel = Math.max(manualPositionSwivel, 0);
            telemetry.addData("MANUAL POSITION SWIVEL", manualPositionSwivel);
            swivel.swivelToPresetPosition(manualPositionSwivel, telemetry);

        }
        telemetry.update();
    }

//
////        if (gamepad.right_trigger > 0) {
////            positionManual += tickChange;
////            positionManual = Math.min(positionManual, maxPosition);
////        } else if (gamepad.left_trigger > 0) {
////            positionManual -= tickChange;
////            positionManual = Math.max(positionManual, 0);
////        }
////
////        if (Math.abs(swivel.getCurrentPosition()) < 25) {
////            swivel.setPower(0);
////            telemetry.addLine("WITHIN 25 ticks SWIVEL MANUAL");
////        }
//
////        swivel.actualSetTargetPosition(manualPositionSwivel, telemetry);
//
//    }

//    public void swivelManual(Gamepad gamepad, ModSwivel swivel, Telemetry telemetry) {
//        long currentTime = System.currentTimeMillis(); // Get the current time
//
//        if (currentTime - lastUpdateTime >= updateInterval) {
//            if (gamepad.left_bumper) {
//                manualPositionSwivel += tickChangeSwivel;
//                manualPositionSwivel = Math.min(manualPositionSwivel, maxPositionSwivel);
//                telemetry.addLine("Increasing Swivel");
//            } else if (gamepad.right_bumper) {
//                manualPositionSwivel -= tickChangeSwivel;
//                manualPositionSwivel = Math.max(manualPositionSwivel, 0);
//                telemetry.addLine("Decreasing Swivel");
//            }
//
//            telemetry.addData("MANUAL SWIVEL POSITION", manualPositionSwivel);
//
//            swivel.swivelToPresetPosition(manualPositionSwivel, telemetry);
//            // Update the last update time
//            lastUpdateTime = currentTime;
//            telemetry.update();
//        }
//
//        // Always send telemetry data for debugging
//
//    }

    public int setScoringPosition(Gamepad gamepad, ModSwivel swivel, Telemetry telemetry) {
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
        } else {
                if (!manualMode) {
                    telemetry.addLine("No button press ");
                    positionPreset = zeroPosition;
                    if (linearSlideLeft.getCurrentPosition() >= -thresholdDown ||
                            linearSlideRight.getCurrentPosition() >= -thresholdDown) {
                        swivel.swivelToPresetPosition(zeroPosition, telemetry);
                    }
                }
            }

        return positionPreset;
    }

    public void slideToPresetPosition(int scoringPosition, Telemetry telemetry, boolean isSwivelCheck, Gamepad gamepad, ModSwivel swivel) {
        if (manualMode) {
            slidesManual(gamepad, telemetry, swivel);
            swivelManual(gamepad, swivel, telemetry);
        } else {
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
    }

    public void slideToPresetPositionAuto(int scoringPosition, Telemetry telemetry) {
        linearSlideLeft.setTargetPosition(-scoringPosition);
        linearSlideRight.setTargetPosition(-scoringPosition);
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideLeft.setPower(power);
        linearSlideRight.setPower(power);

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

//    public void scoreBasketPosition(ModSwivel swivel, Telemetry telemetry, boolean isSwivelCheck, Gamepad gamepad) {
//        swivel.swivelToPresetPosition(basketPositionSwivel, telemetry);
//        if (swivel.getSwivelPosition() >= basketPositionSwivel - thresholdUp) {
//            slideToPresetPosition(basketPositionSlides, telemetry, isSwivelCheck, gamepad, swivel);
//        }
//    }

    public void scoreBasketPositionAuto(ModSwivel swivel, Telemetry telemetry) {
        swivel.swivelToPresetPosition(basketPositionSwivel, telemetry);
        if (swivel.getSwivelPosition() >= basketPositionSwivel - thresholdUp) {
            slideToPresetPositionAuto(basketPositionSlides, telemetry);
        }
    }

//    public void scoreSpecimenPosition(ModSwivel swivel, Telemetry telemetry, boolean isSwivelCheck, Gamepad gamepad) {
//        swivel.swivelToPresetPosition(specimenPositionSwivel, telemetry);
//        if (swivel.getSwivelPosition() >= specimenPositionSwivel - thresholdUp) {
//            slideToPresetPosition(specimenPositionSlides, telemetry, isSwivelCheck, gamepad, swivel);
//        }
//    }

    public void scoreSpecimenPositionAuto(ModSwivel swivel, Telemetry telemetry) {
        swivel.swivelToPresetPosition(specimenPositionSwivel, telemetry);
        if (swivel.getSwivelPosition() >= specimenPositionSwivel - thresholdUp) {
            slideToPresetPositionAuto(specimenPositionSlides, telemetry);
        }
    }
//
//    public void setSwivelAndLinearSlidesDown(ModSwivel swivel, Telemetry telemetry, boolean isSwivelCheck, Gamepad gamepad) {
//        slideToPresetPosition(zeroPosition, telemetry, isSwivelCheck, gamepad, swivel);
//        if (linearSlideLeft.getCurrentPosition() <= thresholdDown ||
//                linearSlideRight.getCurrentPosition() <= thresholdDown) {
//            swivel.swivelToPresetPosition(zeroPosition, telemetry);
//        }
//    }

    public void setSwivelAndLinearSlidesDownAuto(ModSwivel swivel, Telemetry telemetry) {
        slideToPresetPositionAuto(zeroPosition, telemetry);
        if (linearSlideLeft.getCurrentPosition() >= -thresholdDown ||
                linearSlideRight.getCurrentPosition() >= -thresholdDown) {
            swivel.swivelToPresetPosition(zeroPosition, telemetry);
        }
    }

}


