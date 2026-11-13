package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {
    private Robot robot;

    public Launcher(Robot robot){
        this.robot = robot;
    }
    private double revStartTime = 0;

    private boolean waiting = false;

    public void update() {
        if(waiting && robot.opmode.getRuntime() - revStartTime >= 3){
            robot.transferServo1.setPower(1);
            robot.transferServo2.setPower(-1);
            robot.secondaryIntakeMotor.setPower(1);
            waiting = false;
        }
    }

    public void launch() {
        robot.launchMotor.setVelocity(-2150);
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
