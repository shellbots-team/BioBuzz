package org.firstinspires.ftc.teamcode.Core;

public class Intake {

    private Robot robot;
    public Intake(Robot robot){
        this.robot = robot;
    }

    public void runIntake() {
            robot.intakeMotor.setPower(1);
            robot.secondaryIntakeMotor.setPower(1);
    }

    public void runOuttake() {
        robot.intakeMotor.setPower(-1);
        robot.secondaryIntakeMotor.setPower(-1);
    }

    public void stopIntake() {
        robot.intakeMotor.setPower(0);
        robot.secondaryIntakeMotor.setPower(0);
    }

    public void flowerBarDown() {
        robot.flowerServo1.setPosition(0);
        robot.flowerServo2.setPosition(0);
    }

    public void flowerBarUp() {
        robot.flowerServo1.setPosition(1);
        robot.flowerServo2.setPosition(1);
    }
}
