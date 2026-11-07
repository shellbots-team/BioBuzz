package org.firstinspires.ftc.teamcode.Core;

public class Intake {

    private Robot robot;
    public Intake(Robot robot){
        this.robot = robot;
    }

    private boolean FULL = false;

    private enum inOut {
        INTAKE,
        OUTTAKE,
        FLOWERINTAKE,
        WAIT;
    }

    public void runIntake() {
        if (!FULL) {
            robot.intakeMotor.setPower(1);
            robot.secondaryIntakeMotor.setPower(1);
        }
    }

    public void runOuttake() {
        robot.intakeMotor.setPower(-1);
        robot.secondaryIntakeMotor.setPower(-1);
    }

    public void stopIntake() {
        robot.intakeMotor.setPower(0);
        robot.secondaryIntakeMotor.setPower(0);
    }
}
