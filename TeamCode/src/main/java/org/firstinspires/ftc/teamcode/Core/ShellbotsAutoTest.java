package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled // TODO remove this if you ever want to run the program
@Autonomous(group = "Core", name = "Shellbots Auto Test")
public class ShellbotsAutoTest extends LinearOpMode {
    private Robot robot;
    private AutonomousPLUS auto;

    public static final String ALLIANCE_KEY = "Alliance";
    public static final String PATTERN_KEY = "Pattern";

    public void runOpMode() {

        robot = new Robot(hardwareMap, telemetry, this);
        auto = new AutonomousPLUS(robot);

        while (opModeInInit()) {
            // This is the equivalent of init_loop(). It will repeat until the play button is
            // pressed.
            sleep(1);
        }


        robot.readyHardware();
        blackboard.put(ALLIANCE_KEY, "RED");

        waitForStart();

        //The code to run once play has been pressed goes here.
        robot.intakeMotor.setPower(0.75);
        auto.moveAndStall(Robot.MoveDirection.FORWARD, 800, 0);
        auto.moveAndStall(Robot.MoveDirection.RIGHT, 800, 0);
        robot.intakeMotor.setPower(0);
    }
}

