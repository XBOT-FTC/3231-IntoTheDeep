//package org.firstinspires.ftc.teamcode.lib;
//
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.Gamepad;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
///* CONTROLS
//   A: PRECISION MODE
//   B: SCORING POSITION
//*/
//public class Swivel {
//
//    private DcMotor swivel = null;
//    public int maxPosition = 0;
//    public double power = 0;
//    public int tickChange = 0;
//    public boolean precisionMode = false;
//    public int precisionLoop = 0;
//    private double speedModeLimiter = 0;
//    public int position = 0;
//    public int scoringLoop = 0;
//    public boolean scoringBool = false;
//    public double defaultPowerPercentage = 0.0;
//    public int positionLoop = 0;
//    public int hangingPosition = 0;
//    public int basketPosition = 0;
//    public int intakeUpPosition = 0;
//    public int scoringCalc = 0;
//
//
//    public Swivel(HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
//        swivel = hardwareMap.get(DcMotor.class, "swivel");
//        swivel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
//        swivel.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn the motor back on when we are done
//        swivel.setTargetPosition(0);
//        swivel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        swivel.setDirection(direction);
//    }
//
//    public void swivel(Gamepad gamepad, Telemetry telemetry) {
//        precisionModeSwitch(gamepad);
//        scoringPosition(gamepad);
//
//        if (scoringBool) {
//            position = scoringPosition;
//        }
//        else {
//            position = 0;
//        }
//
//        swivel.setTargetPosition(position);
//        swivel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        power *= defaultPowerPercentage;
//
//        // TODO: need to be reviewed. logic for going back to zero for the swivel
//        if(position == 0) {
//            if (swivel.getCurrentPosition() > 15) {
//                swivel.setPower(0.8);
//            } else {
//                swivel.setPower(0.0);
//            }
//        }
//        else if (swivel.getCurrentPosition() < position) {
//            if (precisionMode) {
//                swivel.setPower(power *= speedModeLimiter);
//            }
//            else {
//                swivel.setPower(power);
//            }
//        }
//        else if (swivel.getCurrentPosition() > position) {
//            if (precisionMode) {
//                swivel.setPower(power *= -speedModeLimiter);
//            }
//            else {
//                swivel.setPower(-power);
//            }
//        }
//
//        int currentPosition = swivel.getCurrentPosition();
//        telemetry.addData("Current Swivel Position", currentPosition);
//        telemetry.addData("Swivel Goal Position", position);
//        telemetry.addData("Swivel Precision Mode Status", precisionMode);
//        telemetry.addData("Swivel Power", power);
//        telemetry.addData("Actual Power", swivel.getPower());
//    }
//
//    // switches precision mode on or off if press a
//    public void precisionModeSwitch(Gamepad gamepad) {
//        if (gamepad.a) {
//            if (precisionLoop == 0) {
//                precisionMode = !precisionMode;
//                precisionLoop++;
//            }
//        }
//        else {
//            precisionLoop = 0;
//        }
//    }
//
//    public void scoringPosition(Gamepad gamepad) {
////        if (gamepad.b) {
////            if (scoringLoop == 0) {
////                scoringBool = !scoringBool;
////                scoringLoop++;
////            }
////        }
////        else {
////            scoringLoop = 0;
////        }
//
//        if (gamepad.dpad_up) {
//            if (!buttonPress) {
//                buttonPress= true;
//            }
//        } else {
//            if (buttonPress) {
//                buttonPress = false;
//                grabMode = !grabMode;
//            }
//        }
//    }
//
//
//    public void swivelScore(Telemetry telemetry) {
//        int currentPosition = swivel.getCurrentPosition();
//        swivel.setTargetPosition(scoringPosition);
//        swivel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        swivel.setPower(power);
//        telemetry.addData("Current Swivel Power: ", power);
//        telemetry.addData("Current Swivel Pos: ", currentPosition);
//        telemetry.addData("Target Swivel Pos: ", swivel.getTargetPosition());
//        telemetry.update();
//
//    }
//    public void swivelDown(Telemetry telemetry) {
//        int currentPosition = swivel.getCurrentPosition();
//        swivel.setTargetPosition(0);
//        swivel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        swivel.setPower(0.25);
//        telemetry.addData("Current Swivel Power: ", power);
//        telemetry.addData("Current Swivel Pos: ", currentPosition);
//        telemetry.addData("Target Swivel Pos: ", swivel.getTargetPosition());
//        telemetry.update();
//    }
//
//    public void setSwivelPower(double power) {
//        this.power = power;
//    }
//
//    public void setTickChange(int tickChange) {
//        this.tickChange = tickChange;
//    }
//
//    public void setMaxPosition(int maxTicks) {
//        this.maxPosition = maxTicks;
//    }
//
//    public void setSpeedModeLimiter(double speedModeLimiter) {
//        this.speedModeLimiter = speedModeLimiter;
//    }
//
//    public void setDefaultPowerPercentage(double percentage) {
//        defaultPowerPercentage = percentage;
//    }
//
//    public void setHangingPosition(int position) {
//        hangingPosition = position;
//    }
//
//    public void setBasketPosition(int position) {
//        basketPosition = position;
//    }
//
//    public void setIntakeUpPosition(int position) {
//        intakeUpPosition = position;
//    }
//}
