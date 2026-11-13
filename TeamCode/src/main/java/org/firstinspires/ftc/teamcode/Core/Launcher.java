package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {
    private Robot robot;

    public Launcher(Robot robot){
        this.robot = robot;
    }
    private double revStartTime = 0;

    private boolean waiting = false;

    private double stateStallTime = 0;

    private enum LaunchState {
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
            robot.secondaryIntakeMotor.setPower(1);
            waiting = false;
        }

        switch (currentLaunchState) {
            case START:
                launch();
                stateStallTime = robot.opmode.getRuntime();
                if ()
            case SECOND_BALL:

            case THIRD_BALL:
            case FOURTH_BALL:
            case WAIT:
                stopLaunch();
        }
    }





    public void launch() {
        robot.launchMotor.setVelocity(-2250);
        revStartTime = robot.opmode.getRuntime();
        waiting = true;
    }

    public void stopLaunch() {
        robot.launchMotor.setPower(0);
        robot.transferServo1.setPower(0);
        robot.transferServo2.setPower(0);
        robot.secondaryIntakeMotor.setPower(0);
    }
}
