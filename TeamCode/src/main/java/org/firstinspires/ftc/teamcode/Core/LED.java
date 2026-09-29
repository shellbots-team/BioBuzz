package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.VIOLET;

import com.qualcomm.robotcore.hardware.Servo;

public class LED {
    private final Servo ledInterface;
    public LED(Servo ledFromHardwareMap) {
        ledInterface = ledFromHardwareMap;
    }

    public enum LEDColors {
        OFF(0),
        RED(0.28),
        ORANGE(0.333),
        YELLOW(0.388),
        SAGE(0.444),
        GREEN(0.5),
        AZURE(0.555),
        BLUE(0.611),
        INDIGO(0.666),
        VIOLET(0.722),
        WHITE(1);
        public final double colorOutputValue;

        LEDColors(double servoOutputValue) {
            this.colorOutputValue = servoOutputValue;
        }
    }
    public void setColor(LEDColors color) {
        ledInterface.setPosition(color.colorOutputValue);
    }

    public void setPosition(double colorOutputValue) {
        ledInterface.setPosition(colorOutputValue);
    }
}
