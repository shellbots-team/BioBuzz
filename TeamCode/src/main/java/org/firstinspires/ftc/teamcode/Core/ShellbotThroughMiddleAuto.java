package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Robot.MoveDirection.FORWARD;
import static org.firstinspires.ftc.teamcode.Core.Robot.MoveDirection.LEFT;
import static org.firstinspires.ftc.teamcode.Core.Robot.MoveDirection.TURN_LEFT;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(group = "Core", name = "ShellbotMiddleAuto")
public class ShellbotThroughMiddleAuto extends LinearOpMode {
    private Robot robot;
    private AutonomousPLUS auto;

    public static final String ALLIANCE_KEY = "Alliance";
    public static final String PATTERN_KEY = "Pattern";

    private boolean launching = false;
    private double launchStartTime = 0;


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
        auto.moveAndStall(FORWARD, 700, 0);
        launching = true;
        robot.launcher.switchStateSwitcher();
        waitWhileLaunching();
        robot.launcher.stopLaunch();
        auto.moveAndStall(TURN_LEFT, 25, 0);
        auto.moveAndStall(FORWARD, 2700, 0);
        auto.moveAndStall(LEFT, 1400, 0);
    }

    private void waitWhileLaunching() {
        launchStartTime = getRuntime();
        while (launching) {
            robot.launcher.update();
            if (getRuntime() - launchStartTime >= 10) {
                launching = false;
            }
            sleep(1);
        }
    }
}

