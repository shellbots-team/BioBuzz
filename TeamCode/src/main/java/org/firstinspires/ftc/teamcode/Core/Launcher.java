package org.firstinspires.ftc.teamcode.Core;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Configurable
public class Launcher {
    private Robot robot;

    public Launcher(Robot robot){
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, I, D, F);
        robot.launchMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        this.robot = robot;
    }
    private double revStartTime = 0;
    private double stateStallTime = 0;
    private boolean waiting = false;

    private boolean switchState = false;
    private boolean startLaunch = false;

    private static double P = 35;
    private static double I = 0;
    private static double D = 0;
    private static double F = 14.4;
    private static double secondSwitchTime = 0;

    public static double launcherVelocity = 1365;


    public enum LaunchState {
        START,
        OTHER_BALLS,
        WAIT,
    }
    private LaunchState currentLaunchState = LaunchState.WAIT;

    public void update() {
        if(waiting && robot.opmode.getRuntime() - revStartTime >= 1.5) {
            robot.transferServo1.setPower(1);
            robot.transferServo2.setPower(1);
            waiting = false;
        }
        //works as a timer so that the transfer servos wait until the launcher motor is up to speed

        switch (currentLaunchState) {
            case START:
                if (robot.opmode.getRuntime() - stateStallTime >= secondSwitchTime) {
                    robot.secondaryIntakeMotor.setPower(0.5);
                    stateStallTime = robot.opmode.getRuntime();
                    currentLaunchState = LaunchState.OTHER_BALLS;
                }
                break;
            case OTHER_BALLS:
                if (robot.opmode.getRuntime() - stateStallTime >= 6) {
                    startLaunch = false;
                    switchState = false;
                    currentLaunchState = LaunchState.WAIT;
                }
                break;
            case WAIT:
                stopLaunch();
                if (startLaunch) {
                    currentLaunchState = LaunchState.START;
                    stateStallTime = robot.opmode.getRuntime();
                    launch();
                }
                break;
        }

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, I, D, F);
        robot.launchMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        robot.telemetry.addData("Launcher State", currentLaunchState);
        robot.telemetry.addData("Switch State", switchState);
        robot.telemetry.addData("Start Launch", startLaunch);
        robot.telemetry.addData("State Stall Time", stateStallTime);
    }
    /*
    state machine creates time for new balls to launch
    uses launch() function
    launchTimeSpacer() is used as a timer to separate each case
     */


    public void switchStateSwitcher() {
        startLaunch = true;
    }
    //allows me to change the state machines from other classes because I couldn't figure out how to do it

    /**
     * sets a timer for the new ball to get pushed back to the transfer servos, then switches the state in the launcher state machine
     */
    //acts as a timer for the launching state machine

    public void launch() {
        robot.launchMotor.setVelocity(launcherVelocity);
        revStartTime = robot.opmode.getRuntime();
        waiting = true;
    }
    //this function works with the update function, it sets the motor speed, then allows the update function to go when enough time has passed

    public void stopLaunch() {
        robot.launchMotor.setPower(0);
        robot.transferServo1.setPower(0);
        robot.transferServo2.setPower(0);
        robot.secondaryIntakeMotor.setPower(0);
        switchState = false;
    }
}
