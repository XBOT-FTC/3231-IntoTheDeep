package org.firstinspires.ftc.teamcode.src;


import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Claw {
    Servo clawServo = null;
    boolean buttonPress;
    double openPosition = 1;
    double closePosition = 0;
    boolean grabMode = false;

    public Claw(HardwareMap hardwareMap) {
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        this.buttonPress = false;
    }

    public void powerServo(Gamepad gamepad, Telemetry telemetry) {
        controlServo(gamepad, telemetry);
    }

    public void controlServo(Gamepad gamepad, Telemetry telemetry) {
        if (gamepad.a) {
            if (!buttonPress) {
                buttonPress= true;
            }
        } else {
            if (buttonPress) {
                buttonPress = false;
                grabMode = !grabMode;
            }
        }
        this.setServo();
    }

    public void setServo() {
        if (grabMode) {
            clawServo.setPosition(openPosition);
        } else {
            clawServo.setPosition(closePosition);
        }
    }
}
