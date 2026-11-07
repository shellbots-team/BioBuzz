package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {
    private Robot robot;

    public Launcher(Robot robot){
        this.robot = robot;
    }
    private double revStartTime = 0;

    public void launch() {
        robot.launchMotor.setPower(1);
        revStartTime = robot.opmode.getRuntime();
        if(robot.opmode.getRuntime() - revStartTime == 1){
            robot.transferServo1.setPower(1);
            robot.transferServo2.setPower(-1);
            robot.secondaryIntakeMotor.setPower(0.25);
        }


    }
    //TODO Tune the launcher
    public void stopLaunch() {
        robot.launchMotor.setPower(0);
        robot.transferServo1.setPower(0);
        robot.transferServo2.setPower(0);
        robot.secondaryIntakeMotor.setPower(0);
    }
}
