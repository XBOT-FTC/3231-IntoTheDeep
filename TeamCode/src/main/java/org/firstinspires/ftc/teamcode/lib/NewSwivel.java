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
    public boolean precisionMode = false;
    public int position = 0;
    public int hangingPosition = 0;
    public int basketPosition = 0;
    public int intakeUpPosition = 0;
    public int intakeGroundPosition = 0;
    public boolean dpadUpPress = false;
    public boolean dpadDownPress = false;
    public boolean dpadLeftPress = false;
    public boolean dpadRightPress = false;


    public NewSwivel(HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
        swivel = hardwareMap.get(DcMotor.class, "swivel");
        swivel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        swivel.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn the motor back on when we are done
        swivel.setTargetPosition(0);
        swivel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        swivel.setDirection(direction);
    }

    public void swivel(Gamepad gamepad, Telemetry telemetry) {
        swivelToPosition(setScoringPosition(gamepad), telemetry);
    }

    public int setScoringPosition(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            if (!dpadUpPress) {
                dpadUpPress = true;
                position = hangingPosition;
            }
        } else {
            if (dpadUpPress) {
                dpadUpPress = false;
            }
        }

        if (gamepad.dpad_down) {
            if (!dpadDownPress) {
                dpadDownPress = true;
                position = intakeUpPosition;
            }
        } else {
            if (dpadDownPress) {
                dpadDownPress = false;
            }
        }

        if (gamepad.dpad_left) {
            if (!dpadLeftPress) {
                dpadLeftPress = true;
                position = basketPosition;
            }
        } else {
            if (dpadLeftPress) {
                dpadLeftPress = false;
            }
        }

        if (gamepad.dpad_right) {
            if (!dpadRightPress) {
                dpadRightPress = true;
                position = intakeGroundPosition;
            }
        } else {
            if (dpadRightPress) {
                dpadRightPress = false;
            }
        }

        return position;
    }

    public void swivelToPosition(int scoringPosition, Telemetry telemetry) {
        swivel.setTargetPosition(scoringPosition);
        swivel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        swivel.setPower(power); 

        int currentPosition = swivel.getCurrentPosition();
        telemetry.addData("Current Swivel Position", currentPosition);
        telemetry.addData("Swivel Goal Position", position);
        telemetry.addData("Swivel Precision Mode Status", precisionMode);
        telemetry.addData("Swivel Power", power);
        telemetry.addData("Actual Power", swivel.getPower());
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

    public void setHangingPosition(int position) {
        hangingPosition = position;
    }

    public void setBasketPosition(int position) {
        basketPosition = position;
    }

    public void setIntakeUpPosition(int position) {
        intakeUpPosition = position;
    }

    public void setIntakeGroundPosition(int position) {
        intakeGroundPosition = position;
    }
}
