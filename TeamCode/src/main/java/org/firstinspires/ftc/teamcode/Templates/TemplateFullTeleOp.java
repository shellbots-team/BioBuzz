package org.firstinspires.ftc.teamcode.Templates;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static org.firstinspires.ftc.teamcode.Core.Robot.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Core.Robot.Alliance.RED;
import static org.firstinspires.ftc.teamcode.Core.Robot.DriveMode.PEDRO;
import static org.firstinspires.ftc.teamcode.Core.Robot.DriveMode.STANDARD_ROBOT_CENTRIC;

import com.bylazar.panels.Panels;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.FramerateCalculator;
import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.Objects;

/**
 * This file is our iterative (Non-Linear) "OpMode" for TeleOp.
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is selected on the Robot Controller and executed.
 * This OpMode controls the functions of the robot during the driver-controlled period.
 * <p>
 * If the "@Disabled" line is not commented out, the program will not show up on the driver hub.
 * If you ever have problems with the program not showing up on the driver hub, it's probably because of that.
 * <p>
 */
@Disabled
@TeleOp(name = "Full TeleOp", group = "Templates")
public class TemplateFullTeleOp extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();
    private final FramerateCalculator fps = new FramerateCalculator(runtime);
    private double speed = 0.75;

    private boolean automatedDrive;

    Follower teleFollower;

    //Trackpad data
    public double trackpadXMax = 1920;
    public double trackpadXMin = 0;
    public double trackpadYMax = 1020;
    public double trackpadYMin = 0;
    public double trackpadCurrentX;
    public double trackpadCurrentY;
    public double stickerOffsetX = -0.01;
    public double stickerOffsetY = 0.01;
    public Pose trackTarget;


    int slot = 0; // temp for testing lol

    public Robot robot = null;
    public IMU imu;

    private boolean blackboardPositioningSucessful = true;


    public static final String ALLIANCE_KEY = "Alliance"; //For blackboard
    public static final String PATTERN_KEY = "Pattern";
    boolean killSwitchActivated = false;

    //static TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    public void init() {

        // Call the initialization protocol from the Robot class.
        robot = new Robot(hardwareMap, telemetry, this);
        teleFollower = Constants.createFollower(hardwareMap);
        robot.targetScanner.InitLimeLightTargeting(1, robot);
        robot.controlMode = STANDARD_ROBOT_CENTRIC;
        imu = hardwareMap.get(IMU.class, "imu");

        robot.panels = Panels.INSTANCE;

        getAllianceFromBlackboard(BLUE);

        Pose startingPose = grabStartPose();

        robot.robotPosition = new Pose(
                startingPose.getX(),
                startingPose.getY(),
                startingPose.getHeading()
        );

        teleFollower.setPose(startingPose);
        teleFollower.setHeading(startingPose.getHeading()); // TODO Redundant?

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    private Pose grabStartPose() {
        Object pedroXFromBB = blackboard.getOrDefault("PedroX", 72.0);
        Object pedroYFromBB = blackboard.getOrDefault("PedroY", 72.0);
        Object pedroHeadingFromBB =  blackboard.getOrDefault("PedroHeading", Math.PI / 2);

        double goodX = 72;
        double goodY = 72;
        double goodHeading = Math.PI / 2;

        if (pedroXFromBB instanceof Number) {
            goodX = (double) pedroXFromBB;
        } else {
            blackboardPositioningSucessful = false;
        }
        if (pedroYFromBB instanceof Number) {
            goodY = (double) pedroYFromBB;
        } else {
            blackboardPositioningSucessful = false;
        }
        if (pedroHeadingFromBB instanceof Number) {
            goodHeading = (double) pedroHeadingFromBB;
        } else {
            blackboardPositioningSucessful = false;
        }
        return new Pose(goodX, goodY, goodHeading);
    }

    private void getAllianceFromBlackboard(Robot.Alliance defaultAlliance) {
        if (Objects.equals(blackboard.get(ALLIANCE_KEY), "RED")) {
            robot.alliance = RED;
        } else if (Objects.equals(blackboard.get(ALLIANCE_KEY), "BLUE")) {
            robot.alliance = BLUE;
        } else {
            robot.alliance = defaultAlliance;
        }
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    public void init_loop() {
        telemetry.addData("HYPE", "ARE! YOU! READY?!?!?!?!");
        //doTelemetryStuff();
        telemetry.update();
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    public void start() {
        runtime.reset();
        telemetry.addData("HYPE", "Let's do this!!!");
        gamepad1.setLedColor(0, 0, 255, 10);
        gamepad2.setLedColor(0, 0, 255, 10);

        robot.enableBrakes();
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {

        robot.update();

        //So Begins the input chain. At least try a bit to organize by driver

        if (robot.controlMode == STANDARD_ROBOT_CENTRIC) {
            singleJoystickDrive();
        }
        else if (robot.controlMode == PEDRO) {

        }
        runTrackpadFunctions();

        if (gamepad2.right_stick_button) {
            doTelemetryStuff();
        }

        switchAlliance();
        controlMode();


        if (gamepad2.touchpad) {
            killSwitchActivated = true;
            requestOpModeStop();
        }
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {
        telemetry.addData("Status", "Robot Stopped");
        blackboard.put("PedroX", robot.robotPosition.getX());
        blackboard.put("PedroY", robot.robotPosition.getY());
        blackboard.put("PedroHeading", Math.toRadians(robot.robotPosition.getHeading()));
        if (killSwitchActivated){
            telemetry.addLine("Killswitch Hit!");
        }
    }


    /*
     * The holding cell for all of the random functions we call above.
     */

    /**
     * Actually moves the drive train, using values calculated by singleJoystickDrive().
     * @param motorPowers An array containing the powers for the following motors, in order:
     *                    frontLeftDrive, frontRightDrive, backLeftDrive, and backRightDrive
     */
    public void setIndividualPowers(double[] motorPowers) {
        // This function creates an array so that the function below works.
        // Don't mess with this function unless you know what you're doing.

        if (motorPowers.length != 4) {
            // This throws a runtime error. Make sure it has the right number of inputs!
            throw new Error("motorPowers does not have four items!");
        }
        robot.frontLeftDrive.setMode(RUN_WITHOUT_ENCODER);
        robot.frontRightDrive.setMode(RUN_WITHOUT_ENCODER);
        robot.backLeftDrive.setMode(RUN_WITHOUT_ENCODER);
        robot.backRightDrive.setMode(RUN_WITHOUT_ENCODER);

        robot.frontLeftDrive.setPower(motorPowers[0]);
        robot.frontRightDrive.setPower(motorPowers[1]);
        robot.backLeftDrive.setPower(motorPowers[2]);
        robot.backRightDrive.setPower(motorPowers[3]);
    }

    /**
     * Runs the basic robot-centric drive mode.
     */
    private void singleJoystickDrive() {
        // This function is very math-heavy.
        // Don't mess with it unless you REALLY know what you're doing.
        double leftY = -this.gamepad1.left_stick_y;
        double rightX = -this.gamepad1.right_stick_x;
        double leftX = -this.gamepad1.left_stick_x;

        double[] motorPowers = new double[4];

        motorPowers[0] = (leftY - leftX - rightX); // frontLeftDrive
        motorPowers[1] = (leftY + leftX + rightX); // frontRightDrive
        motorPowers[2] = (leftY + leftX - rightX); // backLeftDrive
        motorPowers[3] = (leftY - leftX + rightX); // backRightDrive

        setIndividualPowers(motorPowers);
    }

    private void controlMode() {
        if (gamepad1.back && !gamepad1.start) {
            if (robot.controlMode == STANDARD_ROBOT_CENTRIC) {
                robot.controlMode = PEDRO;
            } else if (robot.controlMode == PEDRO) {
                robot.controlMode = STANDARD_ROBOT_CENTRIC;
            }
        }

        if (gamepad1.options && robot.controlMode == PEDRO) {
            imu.resetYaw();
        }
    }

    private void driveSpeed() {
        if (gamepad1.dpad_up || gamepad1.right_trigger >= 0.25) {
            speed = 1;
        } else if (gamepad1.dpad_down) {
            speed = 0.25;
        } else if (gamepad1.dpad_left || gamepad1.left_trigger >= 0.25) {
            speed = 0.5;
        } else if (gamepad1.dpad_right) {
            speed = 0.75;
        }
    }

    private void switchAlliance() {
        if (gamepad2.start && gamepad2.shareWasPressed()) {
            if (robot.alliance == BLUE) {
                robot.alliance = RED;
                robot.targetScanner.InitLimeLightTargeting(1, robot);
            } else {
                robot.alliance = BLUE;
                robot.targetScanner.InitLimeLightTargeting(2, robot);
            }
        }
    }

    /**
     * Resets the robot location to a predetermined reset point.
     */
    private void resetPedroPosition() {
        final Pose blueResetPose = new Pose(133.28, 10.75, Math.PI / 2);
        final Pose redResetPose = new Pose(9.59, 9.67, Math.PI /2);
        if (gamepad1.start && gamepad1.shareWasPressed()) {
            if (robot.alliance == BLUE) {
                teleFollower.setPose(blueResetPose);
            }
            if (robot.alliance == RED) {
                teleFollower.setPose(redResetPose);
            }
        }
    }

    private void doTelemetryStuff() {
        // This little section updates the driver hub on the runtime and the motor powers.
        // It's mostly used for troubleshooting.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Alliance", robot.alliance);
        //telemetry.addData("Framerate (last 15 seconds)", fps.getFramerate(30) + " fps");

        telemetry.addLine();
        telemetry.addLine("Blackboard:");
        telemetry.addData("Last saved Alliance", blackboard.get(ALLIANCE_KEY));
        telemetry.addData("Position grab successful", blackboardPositioningSucessful);

        telemetry.update();
    }

    void runTrackpadFunctions() {
        if (gamepad1.touchpad_finger_1) {
            trackpadCurrentX = gamepad1.touchpad_finger_1_x;
            trackpadCurrentY = gamepad1.touchpad_finger_1_y; // Corrected for inversion
            trackTarget = translateTrackpad(trackpadCurrentX, trackpadCurrentY, ""); // Sets tracktarget to coords

            telemetry.addData("Finger 1 x detected val: ", gamepad1.touchpad_finger_1_x);
            telemetry.addData("Finger 1 y detected val: ", gamepad1.touchpad_finger_1_y);

            telemetry.addData("Finger 1 x adjusted: ", trackpadCurrentX);
            telemetry.addData("Finger 1 y adjusted: ", trackpadCurrentY);

            telemetry.addData("Pedro Target Position: ", trackTarget);
        } else if (trackTarget == null) {
            trackTarget = new Pose(72, 72, 0);
        }
    }

    private Pose translateTrackpad(double inX, double inY, String headingCheck) {
        //fix y axis inversion (top is 0 instead of bottom)
        //inY = Math.abs(inY - trackpadYMax);

        //if the heading check is tag rotate to point at target during path
        if (headingCheck == "tag") {
            return new Pose(((inX) * 72) + 72, ((inY) * 72) + 72);
        } else //or just keep current heading for same movement
        {
            return new Pose(((inX) * 72) + 72, ((inY) * 72) + 72);
        }
    }

    private PathChain makeDynamicPath(Pose targetPose, double targetHeadingDegrees) {
        return teleFollower.pathBuilder()
                .addPath(new BezierLine(teleFollower.getPose(), targetPose))
                .setLinearHeadingInterpolation(teleFollower.getHeading(), Math.toRadians(targetHeadingDegrees))
                .build();
        // Build the PathChain after adding all paths
    }

    private PathChain makeDynamicChain(Pose initalPose, Pose secondaryPose, double targetHeadingDegrees) {
        return teleFollower.pathBuilder()
                .addPath(new BezierLine(teleFollower.getPose(), initalPose))
                .setLinearHeadingInterpolation(teleFollower.getHeading(), Math.toRadians(targetHeadingDegrees))
                .addPath(new BezierLine(initalPose, secondaryPose))
                .setLinearHeadingInterpolation(targetHeadingDegrees, targetHeadingDegrees)
                .build(); // Build the PathChain after adding all paths
    }
}


