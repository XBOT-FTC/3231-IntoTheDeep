package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intaker {
    CRServo intakerServo = null;
    double spinPower = 1;
    double neutralPower = 0;
    double negativeSpinPower = -1;


    public Intaker(HardwareMap hardwareMap, Telemetry telemetry) {
        intakerServo = hardwareMap.get(CRServo.class, "intakerServo");
    }

    public void startIntaker(Gamepad gamepad, Telemetry telemetry) {
        setPowerToIntaker(gamepad, telemetry);
    }

    public void setPowerToIntaker(Gamepad gamepad, Telemetry telemetry) {
        if (gamepad.right_trigger > 0) {
            intakerServo.setPower(spinPower);
            telemetry.addLine("Intaker status is FORWARD");
        } else if (gamepad.left_trigger > 0) {
            intakerServo.setPower(negativeSpinPower);
            telemetry.addLine("Intaker status is BACKWARD");
        } else {
            intakerServo.setPower(neutralPower);
            telemetry.addLine("Intaker status is NEUTRAL NO POWER");
        }

    }
}
