package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {
    private Robot robot;

    public Launcher(Robot robot){
        this.robot = robot;
    }
    private double revStartTime = 0;
    private double stateStallTime = 0;

    private boolean waiting = false;

    private boolean switchState = false;
    private boolean startLaunch = false;

    public enum LaunchState {
        START,
        SECOND_BALL,
        THIRD_BALL,
        FOURTH_BALL,
        WAIT,
        GIVE_TIME;
    }
    private LaunchState currentLaunchState;

    public void update() {
        if(waiting && robot.opmode.getRuntime() - revStartTime >= 3){
            robot.transferServo1.setPower(1);
            robot.transferServo2.setPower(-1);
            waiting = false;
        }
        //works as a timer so that the transfer servos wait until the launcher motor is up to speed

        switch (currentLaunchState) {
            case START:
                launch();
                stateStallTime = robot.opmode.getRuntime();
                if (robot.opmode.getRuntime() - stateStallTime >= 0.5) {
                    robot.secondaryIntakeMotor.setPower(1);
                    switchLaunchBall();
                    currentLaunchState = LaunchState.SECOND_BALL;
                }
            case SECOND_BALL:
                launchNewBall();
                if (switchState) {
                    switchLaunchBall();
                    currentLaunchState = LaunchState.THIRD_BALL;
                }
            case THIRD_BALL:
               launchNewBall();
                if (switchState) {
                    currentLaunchState = LaunchState.FOURTH_BALL;
                }
            case FOURTH_BALL:
                launchNewBall();
                if (switchState) {
                    switchLaunchBall();
                    currentLaunchState = LaunchState.WAIT;
                }
            case WAIT:
                stopLaunch();
                if (startLaunch) {
                    currentLaunchState = LaunchState.START;
                }
        }
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

    private void switchLaunchBall() {
        switchState = false;
        stateStallTime = robot.opmode.getRuntime();
        if (robot.opmode.getRuntime() - stateStallTime >= 0.25) {
            robot.secondaryIntakeMotor.setPower(0);
        }
    }
    private void launchNewBall() {
        launch();
        stateStallTime = robot.opmode.getRuntime();
        if (robot.opmode.getRuntime() - stateStallTime >= 0.5) {
            robot.secondaryIntakeMotor.setPower(1);
            switchState = true;
        }
    }
    //acts as a timer for the launching state machine

    public void launch() {
        robot.launchMotor.setVelocity(-2250);
        revStartTime = robot.opmode.getRuntime();
        waiting = true;
    }
    //this function works with the update function, it sets the motor speed, then allows the update function to go when enough time has passed

    public void stopLaunch() {
        robot.launchMotor.setPower(0);
        robot.transferServo1.setPower(0);
        robot.transferServo2.setPower(0);
        robot.secondaryIntakeMotor.setPower(0);
        startLaunch = false;
        switchState = false;
    }
}
