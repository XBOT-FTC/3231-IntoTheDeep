package org.firstinspires.ftc.teamcode.lib;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ModSwivel {

    private DcMotor swivel = null;
    public int maxPosition = 0;
    public double power = 0;

    public ModSwivel(HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
        swivel = hardwareMap.get(DcMotor.class, "swivel");
        swivel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        swivel.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn the motor back on when we are done
        swivel.setTargetPosition(0);
        swivel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        swivel.setDirection(direction);
    }

    public void swivelToPresetPosition(int scoringPosition, Telemetry telemetry, boolean downWard) {
        swivel.setTargetPosition(scoringPosition);
        swivel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        swivel.setPower(power);

//        if (Math.abs(swivel.getCurrentPosition()) < 70) {
//            swivel.setPower(0);
//            telemetry.addLine("WITHIN 70 ticks OF 0, SWIVEL");
//        }

        if (downWard && scoringPosition < 30 &&
                Math.abs(swivel.getCurrentPosition()) < 25) {
            swivel.setPower(0);
            telemetry.addLine("WITHIN 25 TICKS SWIVEL");
        }
        telemetry.addLine("RAHHHHHHHHHHH");
        int currentPosition = swivel.getCurrentPosition();
        telemetry.addData("Current Swivel Position", currentPosition);
        telemetry.addData("Swivel Goal Position", scoringPosition);
        telemetry.addData("Swivel Power", power);
        telemetry.addData("Actual Power", swivel.getPower());
    }

    public void swivelToPresetPositionAuto(int scoringPosition, Telemetry telemetry) {
        swivel.setTargetPosition(scoringPosition);
        swivel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        swivel.setPower(power);

//        if (Math.abs(swivel.getCurrentPosition()) < 70) {
//            swivel.setPower(0);
//            telemetry.addLine("WITHIN 70 ticks OF 0, SWIVEL");
//        }

        int currentPosition = swivel.getCurrentPosition();
        telemetry.addData("Current Swivel Position", currentPosition);
        telemetry.addData("Swivel Goal Position", scoringPosition);
        telemetry.addData("Swivel Power", power);
        telemetry.addData("Actual Power", swivel.getPower());
    }

    public void setSwivelPower(double power) {
        this.power = power;
    }

    public void setMaxPosition(int maxTicks) {
        this.maxPosition = maxTicks;
    }

    public boolean getSwivelIsZero() {
        return swivel.getCurrentPosition() < 50;
    }

    public int getSwivelPosition() {
        return swivel.getCurrentPosition();
    }

//    public void actualSetTargetPosition(int scoringPosition, Telemetry telemetry) {
//        swivel.setTargetPosition(scoringPosition);
//        swivel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        swivel.setPower(power);
//
//        int currentPosition = swivel.getCurrentPosition();
//        telemetry.addData("Current Swivel Position", currentPosition);
//        telemetry.addData("Swivel Goal Position", scoringPosition);
//        telemetry.addData("Swivel Power", power);
//        telemetry.addData("Actual Power", swivel.getPower());
//    }
}
