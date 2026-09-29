package org.firstinspires.ftc.teamcode.Core;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;

import android.annotation.SuppressLint;

import com.bylazar.panels.Panels;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Vision.Limelight_Target_Scanner;
import org.firstinspires.ftc.teamcode.Vision.Limelight_Randomization_Scanner;

public class Robot {

    public DcMotorEx frontLeftDrive;
    public DcMotorEx frontRightDrive;
    public DcMotorEx backLeftDrive;
    public DcMotorEx backRightDrive;

    public VoltageSensor voltageSensor;

    public Telemetry telemetry;

    //init and declare war
    public OpMode opmode;
    public HardwareMap hardwareMap;

    public DriveMode controlMode;//STANDARD_ROBOT_CENTRIC;
    public IMU.Parameters imuParameters;
    public enum patternColors {PPG, GPP, PGP}
    public patternColors pattern;

    public enum Alliance {
        BLUE(2), RED(1);
        public final int limelightPipeline;
        Alliance(int limelightPipeline) {
            this.limelightPipeline = limelightPipeline;
        }
    }
    public Alliance alliance;

    public Pose robotPosition;

    public boolean callPartialPedro = true;

    public Limelight_Randomization_Scanner randomizationScanner;
    public Limelight_Target_Scanner targetScanner;


    public Panels panels;

    public static TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    public enum DriveMode {STANDARD_ROBOT_CENTRIC, PEDRO, LEGACY_FIELD_CENTRIC}
    public enum OpenClosed {OPEN,CLOSED}
    public enum MoveDirection {
        FORWARD, BACKWARD, LEFT, RIGHT,
        DIAGONAL_LEFT, DIAGONAL_RIGHT,
        TURN_LEFT, TURN_RIGHT
    }
    public enum UpDown {UP, DOWN}

    public boolean scanningForTargetTag = false;

    public int limelightSideOffsetAngle = 0;

    //Initialize motors and servos
    public Robot(HardwareMap hardwareMap, Telemetry telemetry, OpMode opmode) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.opmode = opmode;

        // This section turns the names of the pieces of hardware into variables that we can program with.
        // Make sure that the device name is the exact same thing you typed in on the configuration on the driver hub.
        frontRightDrive = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotorEx.class, "backRightDrive");

        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        imuParameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
        );

        // This section sets the direction of all of the motors. Depending on the motor, this may change later in the program.
        frontLeftDrive.setDirection(REVERSE);
        frontRightDrive.setDirection(FORWARD);
        backLeftDrive.setDirection(REVERSE);
        backRightDrive.setDirection(FORWARD);

        // This tells the motors to chill when we're not powering them.
        frontRightDrive.setZeroPowerBehavior(BRAKE);
        backLeftDrive.setZeroPowerBehavior(BRAKE);
        backRightDrive.setZeroPowerBehavior(BRAKE);
        frontLeftDrive.setZeroPowerBehavior(BRAKE);


        //This is new..
        telemetry.addData("Status", "Initialized");

        targetScanner = new Limelight_Target_Scanner(this);
        randomizationScanner = new Limelight_Randomization_Scanner(this);

        if (alliance == null) alliance = Alliance.BLUE;
    }

    /**
     * Updates the state of every part of the robot. Should be called once per loop.
     */
    public void update() {
        //TODO Make this function update the robot states
    }

    /**
     * Checks to see if the wheels are moving to a target position.
     * @return True, if any wheel is busy.
     */
    public boolean isWheelsBusy() {
        return backLeftDrive.isBusy() || frontLeftDrive.isBusy() || frontRightDrive.isBusy() || backRightDrive.isBusy();
    }

    /**
     * Sets the drive train motors' powers to zero.
     */
    public void stopDriveMotors() {
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    boolean startPointLocked = false;
    int frontLeftPos;
    int frontRightPos;
    int backLeftPos;
    int backRightPos;

    /**
     * Runs the drive train in a cardinal direction.
     * @param direction The direction, a MoveDirection enum
     * @param ticks The distance to move in motor ticks
     */
    public void setTargets(MoveDirection direction, int ticks, boolean updateStartPoint) {

        // So this is a weird looking block of code. Basically, if we want to lock the start point,
        // we want to update it once when it's initially locked and then keep it there. So, the
        // check to actually lock the start point is after the point where it updates, thus locking
        // the current position, but the check to unlock it happens before, so that it unlocks
        // immediately.
        if (updateStartPoint) {
            startPointLocked = false;
        }
        if (!startPointLocked) {
            frontLeftPos = frontLeftDrive.getCurrentPosition();
            frontRightPos = frontRightDrive.getCurrentPosition();
            backLeftPos = backLeftDrive.getCurrentPosition();
            backRightPos = backRightDrive.getCurrentPosition();
        }
        if (!updateStartPoint) {
            startPointLocked = true;
        }

        // This is all inverted (big sigh)

        switch (direction) {
            case RIGHT:
                frontLeftDrive.setTargetPosition(-ticks + frontLeftPos);
                frontRightDrive.setTargetPosition(ticks + frontRightPos);
                backLeftDrive.setTargetPosition(ticks + backLeftPos);
                backRightDrive.setTargetPosition(-ticks + backRightPos);
                break;
            case LEFT:
                frontLeftDrive.setTargetPosition(ticks + frontLeftPos);
                frontRightDrive.setTargetPosition(-ticks + frontRightPos);
                backLeftDrive.setTargetPosition(-ticks + backLeftPos);
                backRightDrive.setTargetPosition(ticks + backRightPos);
                break;
            case FORWARD:
                frontLeftDrive.setTargetPosition(-ticks + frontLeftPos);
                frontRightDrive.setTargetPosition(-ticks + frontRightPos);
                backLeftDrive.setTargetPosition(-ticks + backLeftPos);
                backRightDrive.setTargetPosition(-ticks + backRightPos);
                break;
            case BACKWARD:
                frontLeftDrive.setTargetPosition(ticks + frontLeftPos);
                frontRightDrive.setTargetPosition(ticks + frontRightPos);
                backLeftDrive.setTargetPosition(ticks + backLeftPos);
                backRightDrive.setTargetPosition(ticks + backRightPos);
                break;
            case TURN_RIGHT:
                frontLeftDrive.setTargetPosition(-ticks + frontLeftPos);
                frontRightDrive.setTargetPosition(ticks + frontRightPos);
                backLeftDrive.setTargetPosition(-ticks + backLeftPos);
                backRightDrive.setTargetPosition(ticks + backRightPos);
                break;
            case TURN_LEFT:
                frontLeftDrive.setTargetPosition(ticks + frontLeftPos);
                frontRightDrive.setTargetPosition(-ticks + frontRightPos);
                backLeftDrive.setTargetPosition(ticks + backLeftPos);
                backRightDrive.setTargetPosition(-ticks + backRightPos);
                break;
            case DIAGONAL_RIGHT:
                frontLeftDrive.setTargetPosition(-ticks + frontLeftPos);
                frontRightDrive.setPower(frontRightPos);
                backLeftDrive.setPower(backLeftPos);
                backRightDrive.setTargetPosition(-ticks + backRightPos);
                break;
            case DIAGONAL_LEFT:
                frontLeftDrive.setPower(frontLeftPos);
                frontRightDrive.setTargetPosition(-ticks + frontRightPos);
                backLeftDrive.setTargetPosition(-ticks + backLeftPos);
                backRightDrive.setPower(backRightPos);
                break;
        }
    }

    /**
     * Sets the drive motors to the specified RunMode
     * @param runMode DcMotor.RunMode enum
     */
    public void setDriveTrainRunMode(DcMotor.RunMode runMode) {
        frontLeftDrive.setMode(runMode);
        frontRightDrive.setMode(runMode);
        backLeftDrive.setMode(runMode);
        backRightDrive.setMode(runMode);
    }

    /**
     * Sets the drive motors to RUN_TO_POSITION. Enables usage of the DcMotor.setTargetPosition()
     * and Robot.setTargets() functions.
     */
    public void positionRunningMode() {
        setDriveTrainRunMode(RUN_TO_POSITION);
    }

    /**
     * Turns off the motor encoders, to run purely on power.
     */
    public void powerRunningMode() {
        setDriveTrainRunMode(RUN_WITHOUT_ENCODER);
    }

    /**
     * Sets the motors to run with encoder feedback.
     */
    public void encoderRunningMode(){
        setDriveTrainRunMode(RUN_USING_ENCODER);
    }

    /**
     * Sets the motors to the specified speed
     * @param speed A value from 0-1
     */
    public void powerSet(double speed) {
        frontLeftDrive.setPower(speed);
        frontRightDrive.setPower(speed);
        backLeftDrive.setPower(speed);
        backRightDrive.setPower(speed);
    }

    public void enableBrakes() {
        frontLeftDrive.setZeroPowerBehavior(BRAKE);
        frontRightDrive.setZeroPowerBehavior(BRAKE);
        backLeftDrive.setZeroPowerBehavior(BRAKE);
        backRightDrive.setZeroPowerBehavior(BRAKE);
    }

    /**
     * Stops the drive train and resets the encoder values to zero ticks.
     */
    public void resetDriveEncoders(){
        setDriveTrainRunMode(STOP_AND_RESET_ENCODER);
    }

    /**
     * Adds motor data to telemetry.
     */
    @SuppressLint("DefaultLocale")
    public void tellMotorOutput(){
        telemetry.addData("Control Mode", controlMode);
        telemetry.addData("Motors", String.format("FL Power(%.2f) FL Location (%d) FL Target (%d)", frontLeftDrive.getPower(), frontLeftDrive.getCurrentPosition(), frontLeftDrive.getTargetPosition()));
        telemetry.addData("Motors", String.format("FR Power(%.2f) FR Location (%d) FR Target (%d)", frontRightDrive.getPower(), frontRightDrive.getCurrentPosition(), frontRightDrive.getTargetPosition()));
        telemetry.addData("Motors", String.format("BL Power(%.2f) BL Location (%d) BL Target (%d)", backLeftDrive.getPower(), backLeftDrive.getCurrentPosition(), backLeftDrive.getTargetPosition()));
        telemetry.addData("Motors", String.format("BR Power(%.2f) BR Location (%d) BR Target (%d)", backRightDrive.getPower(), backRightDrive.getCurrentPosition(), backRightDrive.getTargetPosition()));
    }

    /**
     * Moves hardware to match-ready positions
     */
    public void readyHardware() {
        // TODO Get your hardware ready here
    }

    /**
     * Figures out if an integer is even.
     * @param x
     * @return True or false, for even or odd
     */
    public static boolean isEven(int x) {
        return x % 2 == 0;
    }

    /**
     * Finds the largest absolute value of a list of numbers
     * @param values A list of doubles
     * @return The largest absolute values
     */
    public static double getLargestAbsVal(double... values){
        // This function does some math!
        double max = 0;
        for (double val : values) {
            if (Math.abs(val) > max) {
                max = Math.abs(val);
            }
        }
        return max;
    }

    /**
     * Rounds a number to a specified level of precision.
     * @param input The number to be rounded
     * @param precision The degree of precision with which to round. For example, 0.001 will round
     *                  to three decimal places. Make sure this value is a multiple of 10, otherwise
     *                  some weird stuff will happen.
     * @return The rounded number.
     */
    public static double roundToDecimalPoint(double input, double precision) {
        return Math.round(input / precision) * precision;
    }
}
