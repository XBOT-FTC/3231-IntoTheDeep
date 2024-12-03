package org.firstinspires.ftc.teamcode.src;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.lib.NewLinearSlide;
import org.firstinspires.ftc.teamcode.lib.NewSwivel;
import org.firstinspires.ftc.teamcode.lib.Grabber;



@Autonomous(name="Specimen Auto", group="Linear OpMode")

public class SpecimenAuto extends LinearOpMode {    
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    public int moveForwardTime = 1000;
    NewSwivel swivel = new NewSwivel(hardwareMap, DcMotorSimple.Direction.FORWARD);
    NewLinearSlide linearSlide = new NewLinearSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);
    @Override
    public void runOpMode() throws InterruptedException{
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "fl_drive"); //0
        frontRightDrive = hardwareMap.get(DcMotor.class, "fr_drive"); //3
        backLeftDrive = hardwareMap.get(DcMotor.class, "bl_drive"); //1
        backRightDrive = hardwareMap.get(DcMotor.class, "br_drive"); //2
        

        waitForStart();

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //move forward
        moveLinear(0.5, moveForwardTime, "Forward");
        //rotate swivel
        //extend linear slide
        //retract
        //open grabber
        //retract fully
        //drive to park
        
    }
    public void moveLinear(double speed, int time, String direction) throws InterruptedException {
        if (direction.equalsIgnoreCase("Forward")) {
            setPower(speed, speed, speed, speed);
        }
        if (direction.equalsIgnoreCase("Backward")) {
            setPower(-speed, -speed, -speed, -speed);
        }
        if (direction.equalsIgnoreCase("Left")) {
            setPower(-speed, speed, speed, -speed);
        }
        if (direction.equalsIgnoreCase("Right")) {
            setPower(speed, -speed, -speed, speed);
        }
        Thread.sleep(time);
    }

}
