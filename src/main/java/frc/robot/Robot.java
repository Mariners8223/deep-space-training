package frc.robot;

import edu.wpi.first.wpilibj.Controller;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Robot extends TimedRobot {
  private float div = 2;
  private final Spark m_frontLeft = new Spark(1);
  private final Spark m_rearLeft = new Spark(2);
  private final SpeedControllerGroup m_left = new SpeedControllerGroup(m_frontLeft, m_rearLeft);

  private final Spark m_frontRight = new Spark(3);
  private final Spark m_rearRight = new Spark(4);
  private final SpeedControllerGroup m_right = new SpeedControllerGroup(m_frontRight, m_rearRight);

  private final DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);
  private final Encoder encLeft = new Encoder(1, 2, false, Encoder.EncodingType.k4X);
  private final Encoder encRight = new Encoder(3, 4, false, Encoder.EncodingType.k4X);

  private final Spark m_rollingThing = new Spark(5);

  // private final Spark m_4motot1 = new Spark(6);
  private final Spark m_4motot = new Spark(7);
  // private final SpeedControllerGroup m_4motot = new
  // SpeedControllerGroup(m_4motot1, m_4motot2);

  private final Timer m_timer = new Timer();
  private final Joystick m_joystick = new Joystick(0);
  private final ADXRS450_Gyro m_gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);

  private volatile Float dist;
  private volatile Float ang;

  private volatile String[] p;
  private volatile String[] p_pros;

  @Override
  public void robotInit() {
    SmartDashboard.putString("time: ", "17:48");
    NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("tb");

    networkTable.addEntryListener("distance", (table, key, entry, value, flags) -> {
      dist = (Float) value.getValue();
      SmartDashboard.putNumber("dist:", dist);
      System.out.print(dist);
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    networkTable.addEntryListener("angle", (table, key, entry, value, flags) -> {
      dist = (Float) value.getValue();
      SmartDashboard.putNumber("ang:", dist);
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

   

  }

  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
    encLeft.reset();
    encRight.reset();
  }

  @Override
  public void autonomousPeriodic() {
    if (m_joystick.getRawButton(4)) {
      div -= 0.1;
    }
    if (m_joystick.getRawButton(1)) {
      div += 0.1;
    }
    if (div < 1) {
      div = 1;
    }
    if (!m_joystick.getRawButton(2)) {
      m_drive.arcadeDrive(m_joystick.getRawAxis(1) / div, m_joystick.getRawAxis(0) / div);

      // m_4motot.set((m_joystick.getRawAxis(3) - m_joystick.getRawAxis(2)) / 2);
      // m_4motot1.set((m_joystick.getRawAxis(3)) / 2);
      m_4motot.set((m_joystick.getRawAxis(3) - m_joystick.getRawAxis(2)) * 0.5);

      if (m_joystick.getRawButton(6)) {
        m_rollingThing.set(0.4);
      } else if (m_joystick.getRawButton(5)) {
        m_rollingThing.set(-0.4);
      } else {
        m_rollingThing.set(0);
      }

    } else {
      m_drive.stopMotor();
    }

  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testPeriodic() {
  }
}