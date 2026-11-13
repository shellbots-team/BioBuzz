package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(group = "Core", name = "BadAuto")
public class TemplateLegacyLinearAuto extends LinearOpMode {
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

        auto.speed = 0.5;

        //The code to run once play has been pressed goes here.
        auto.moveAndStall(Robot.MoveDirection.RIGHT, 150, 0);
        auto.moveAndStall(Robot.MoveDirection.FORWARD, 700, 0);
    }
}

