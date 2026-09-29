package org.firstinspires.ftc.teamcode.Templates;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static org.firstinspires.ftc.teamcode.Core.Robot.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Templates.TemplatePedroAuto.Steps.*;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Disabled
@Autonomous(name = "PedroPathing Auto Template", group = "Templates")
@Configurable // Panels
public class TemplatePedroAuto extends OpMode {

    public Robot robot;
    TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    int pathState; // Current autonomous path state (state machine)
    PathsForBack12Blue paths; // Paths defined in the Paths class

    public static final String ALLIANCE_KEY = "Alliance"; //For blackboard
    public static final String PATTERN_KEY = "Pattern";
    public ElapsedTime stallTimer;
    Pose startPose = new Pose(56.5, 9.200, Math.toRadians(180));
    // Make sure this is set HEREqa
    Timer pathTimer, actionTimer, opmodeTimer;

    @Override
    public void init() {

        robot = new Robot(hardwareMap, telemetry, this);
        follower = Constants.createFollower(hardwareMap);
        follower.setMaxPowerScaling(1);
        follower.setMaxPower(1);
        robot.callPartialPedro = false;
        panelsTelemetry = Robot.panelsTelemetry;
        opmodeTimer = new Timer();
        pathTimer = new Timer();
        actionTimer = new Timer();

        robot.randomizationScanner.InitLimeLight(0);
        blackboard.put(ALLIANCE_KEY, "BLUE");
        stallTimer = new ElapsedTime();

        follower.setPose(startPose);
        follower.setHeading(startPose.getHeading()); // TODO check if this is actually needed. I think it's redundant

        robot.alliance = BLUE;

        paths = new PathsForBack12Blue(follower); // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    public void init_loop() {
        return;
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    public void start() {
        //runtime.reset();
        opmodeTimer.resetTimer();
        telemetry.addData("HYPE", "Let's do this!!!");
        robot.readyHardware();
    }

    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        robot.update();
        pathState = autonomousPathUpdate(); // Update autonomous state machine

        // Log values to Panels and Driver Station
        panelsTelemetry.addLine("Telemetry goes here");
        panelsTelemetry.update(telemetry);
    }

    public static class PathsForBack12Blue {
        public PathChain template;

        public PathsForBack12Blue(Follower follower) {
            template = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.500, 9.200),
                                    new Pose(42.000, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();
        }
    }

    public enum Steps {
        STEP_1, STEP_2, STOPPED
    }

    private Steps currentStep = STEP_1; // Current autonomous path state (state machine)
    public int autonomousPathUpdate() {
        // Add your state machine Here
        // Access paths with paths.pathName
        // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine

        emergencyFinishIfNeeded(29.5);

        switch (currentStep) {
            case STEP_1:
                follower.followPath(paths.template);
                setCurrentStep(STEP_2);
                break;
            case STEP_2:
                if(!follower.isBusy())
                {
                    // do more stuff
                    setCurrentStep(STOPPED);
                }
                break;
            case STOPPED:
                return 0;
        }
        return 0;
    }

    /**
     * Moves to a different step and resets pathTimer.
     */
    void setCurrentStep(Steps nextStep) {
        currentStep = nextStep;
        pathTimer.resetTimer();
    }

    /**
     * Stops the drive train if finishTime is exceeded.
     * @param finishTime The time to stop at, in seconds
     */
    private void emergencyFinishIfNeeded(double finishTime) {
        if(opmodeTimer.getElapsedTimeSeconds() >= finishTime)
        {
            follower.breakFollowing();
            robot.frontRightDrive.setZeroPowerBehavior(BRAKE);
            robot.backLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.backRightDrive.setZeroPowerBehavior(BRAKE);
            robot.frontLeftDrive.setZeroPowerBehavior(BRAKE);

            robot.frontRightDrive.setPower(0);
            robot.backLeftDrive.setPower(0);
            robot.backRightDrive.setPower(0);
            robot.frontLeftDrive.setPower(0);

            setCurrentStep(STOPPED);
        }
    }
}
