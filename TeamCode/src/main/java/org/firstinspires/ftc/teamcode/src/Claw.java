package org.firstinspires.ftc.teamcode.src;


import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Claw {
    Servo clawServo = null;
    boolean buttonPress;
    double closePosition = 1;
    double openPosition = 0.25;
    boolean grabMode = false;

    public Claw(HardwareMap hardwareMap, Telemetry telemetry) {
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawServo.setDirection(Servo.Direction.REVERSE);
        this.buttonPress = false;
    }

    public void powerServo(Gamepad gamepad, Telemetry telemetry) {
        controlServo(gamepad, telemetry);
    }

    public void controlServo(Gamepad gamepad, Telemetry telemetry) {
        if (gamepad.x) {
            if (!buttonPress) {
                buttonPress= true;
            }
        } else {
            if (buttonPress) {
                buttonPress = false;
                grabMode = !grabMode;
            }
        }
        this.setServo(telemetry);

    }

    public void setServo(Telemetry telemetry) {
        if (grabMode) {
            clawServo.setPosition(closePosition);
            telemetry.addLine("CLOSE");
        } else {
            clawServo.setPosition(openPosition);
            telemetry.addLine("OPEN");
        }

        telemetry.addData("Current pos Claw", clawServo.getPosition());

    }
    public void open() {
        clawServo.setPosition(openPosition);
    }

    public void close() {
        clawServo.setPosition(closePosition);
    }
}
