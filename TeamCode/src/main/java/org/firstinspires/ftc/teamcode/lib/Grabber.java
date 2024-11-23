package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/* CONTROLS
   X: OPEN/CLOSE GRABBER

   DPAD UP: SERVO INCREMENT
   DPAD DOWN: SERVO DECREMENT
*/
public class Grabber {

    private Servo servo = null;
    public int loop = 0;
    public boolean toggleButton = false;
    public double openPosition = 0.0;
    public double closePosition = 0.0;

    public Grabber(HardwareMap hardwareMap, Servo.Direction direction) {
        servo = hardwareMap.get(Servo.class, "servo");
        servo.setDirection(direction);
    }

    public void openClose(Gamepad gamepad, Telemetry telemetry) {
        if (gamepad.x) {
            if (loop == 0) {
                toggleButton = !toggleButton;
                loop++;
            }
        }
        else {
            loop = 0;
        }

        if (toggleButton) {
            servo.setPosition(closePosition);
        }
        else {
            servo.setPosition(openPosition);
        }

//        if (gamepad.dpad_up) {
//            position += 0.0001;
//            servo.setPosition(position);
//        }
//        else if (gamepad.dpad_down) {
//            position -= 0.0001;
//            servo.setPosition(position);
//        }
//        servo.setPosition(position);
        telemetry.addData("toggleButton", toggleButton);
        telemetry.addData("Servo", "Servo (%.2f)", servo.getPosition());
    }

    public void setOpenPosition(double position) {
        openPosition = position;
    }

    public void setClosePosition(double position) {
        closePosition = position;
    }
    public void open() {
        servo.setPosition(openPosition);
    }
    public void close() {
        servo.setPosition(closePosition);
    }

}