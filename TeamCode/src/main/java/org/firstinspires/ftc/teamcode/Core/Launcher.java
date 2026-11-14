package org.firstinspires.ftc.teamcode.Core;

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
        SWITCH_TO_SECOND_BALL,
        SECOND_BALL,
        SWITCH_TO_THIRD_BALL,
        THIRD_BALL,
        SWITCH_TO_FOURTH_BALL,
        FOURTH_BALL,
        WAIT,
    }
    private LaunchState currentLaunchState = LaunchState.WAIT;

    public void update() {
        if(waiting && robot.opmode.getRuntime() - revStartTime >= 3) {
            robot.transferServo1.setPower(1);
            robot.transferServo2.setPower(-1);
            waiting = false;
        }
        //works as a timer so that the transfer servos wait until the launcher motor is up to speed

        switch (currentLaunchState) {
            case START:

                if (robot.opmode.getRuntime() - stateStallTime >= 5) {
                    robot.secondaryIntakeMotor.setPower(1);
                    switchLaunchBall();
                    currentLaunchState = LaunchState.SECOND_BALL;
                }
                break;
            case SWITCH_TO_SECOND_BALL:
                switchLaunchBall();
                if (switchState) {
                    launch();
                    currentLaunchState = LaunchState.SECOND_BALL;
                }
                break;
            case SECOND_BALL:
                checkIfLaunchDone();
                if (switchState) {

                    currentLaunchState = LaunchState.SWITCH_TO_THIRD_BALL;
                }
                break;
            case SWITCH_TO_THIRD_BALL:
                switchLaunchBall();
                if (switchState) {
                    launch();
                    currentLaunchState = LaunchState.THIRD_BALL;
                }
                break;
            case THIRD_BALL:
                checkIfLaunchDone();
                if (switchState) {
                    currentLaunchState = LaunchState.FOURTH_BALL;
                }
                break;
            case SWITCH_TO_FOURTH_BALL:
                switchLaunchBall();
                if (switchState) {
                    launch();
                    currentLaunchState = LaunchState.FOURTH_BALL;
                }
                break;
            case FOURTH_BALL:
                checkIfLaunchDone();
                if (switchState) {
                    startLaunch = false;
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

        robot.telemetry.addData("Launcher State", currentLaunchState);
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
    private void switchLaunchBall() {
        switchState = false;
        stateStallTime = robot.opmode.getRuntime();
        if (robot.opmode.getRuntime() - stateStallTime >= 0.2) {
            robot.secondaryIntakeMotor.setPower(0);
            switchState = true;
        }
    }

    /**
     *
     */
    private void checkIfLaunchDone() {
        if (robot.opmode.getRuntime() - stateStallTime >= 0.2) {
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

        switchState = false;
    }
}
