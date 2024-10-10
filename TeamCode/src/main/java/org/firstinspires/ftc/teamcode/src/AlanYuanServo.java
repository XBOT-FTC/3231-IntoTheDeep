package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@TeleOp(name="Basic: Linear OpMode", group="Linear OpMode")
public class AlanYuanServo {
    private Servo servo = null;
    public int loop = 0;
    public boolean buttonPressed = false;
    public double openPosition = 0.33;
    public double closedPosition = 0.31;
    private double position = 0.0;

    public AlanYuanServo(HardwareMap hardwareMap, Servo.Direction direction) {
        servo = hardwareMap.get(Servo.class, "servo");
        servo.setDirection(direction);
    }

    public void openClose(Gamepad gamepad, Telemetry telemetry) {
        if (gamepad.x) {
            if (loop == 0) {
                buttonPressed = !buttonPressed;
                loop++;
            }
        }
        else {
            loop = 0;
        }

        if (buttonPressed) {
            servo.setPosition(closedPosition);
        }
        else {
            servo.setPosition(openPosition);
        }

        if (gamepad.right_bumper) {
        position += 0.0001;
        servo.setPosition(position);
        }

        if (gamepad.right_trigger > 0.5) {
            position -= 0.0001;
            servo.setPosition(position);
        }
        telemetry.addData("buttonPressed", buttonPressed);
        telemetry.addData("Servo", "Servo (%.2f)", servo.getPosition());
    }
}

