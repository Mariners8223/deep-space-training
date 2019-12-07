/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.RotateUntile;

/**
 * Add your docs here.
 */
public class Chassis extends Subsystem {
  ADXRS450_Gyro gyro;
  PIDController RotateGyroPID;
  PIDController RotateValuePID;
  public static final Timer m_timer = new Timer();

  private Spark m_frontLeft;
  private Spark m_backLeft;
  private SpeedControllerGroup m_left;

  private Spark m_frontRight;
  private Spark m_backRight;
  private SpeedControllerGroup m_right;

  private DifferentialDrive m_drive;

  private static Chassis instance;

  private class valueSorce implements PIDSource{
  @Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return null;
	}

	@Override
	public double pidGet() {
		return SmartDashboard.getNumber("ang", 1)*100.0;
	}
  }

  private valueSorce vs;

  private Chassis(){
    m_frontLeft = new Spark(RobotMap.LEFT_FRONT_MOTOR);
    m_backLeft = new Spark(RobotMap.LEFT_BACK_MOTOR);
    m_left = new SpeedControllerGroup(m_frontLeft, m_backLeft);

    m_frontRight = new Spark(RobotMap.RIGHT_FRONT_MOTOR);
    m_backRight = new Spark(RobotMap.RIGHT_BACK_MOTOR);
    m_right = new SpeedControllerGroup(m_frontRight, m_backRight);

    m_drive = new DifferentialDrive(m_left, m_right);

    gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
    RotateGyroPID = new PIDController(1, 1*0.52, 1*0.3, gyro, (double s)->{m_drive.tankDrive(s, -s);});
    RotateGyroPID.setOutputRange(-0.5, 0.5);
    RotateGyroPID.setPercentTolerance(100.0/180.0);

    vs = new valueSorce();
    RotateValuePID = new PIDController(1, 1*0.52, 1*0.3, vs, (double s)->{m_drive.tankDrive(0,0);});
    RotateValuePID.setOutputRange(-0.5, 0.5);
    RotateValuePID.setPercentTolerance(100.0/180.0);
    //m_timer.reset();
    //m_timer.start();
  }

  public static Chassis getInstance() {
    if(instance == null) instance = new Chassis();
    return instance;
  }

  @Override
  public void initDefaultCommand() {
    //setDefaultCommand(new ExampleCommand());
    setDefaultCommand(new RotateUntile());
  }

  /*static public void rotateValue(){
    double timeL = m_timer.get();
    double timeN;

    final double errorMax = 1; // ??
    final double maxS = 0.3;
    final double needCorrectTime = 0.3;
    final double errorRange = 0.1;
    double correctTime = 0;

    double errorL = SmartDashboard.getNumber("ang", 0);
    double errorN;

    double integralL = 0;

    double kP = 1;
    double kI = 0.52;
    double kD = 0.23;

    while(correctTime <= needCorrectTime){
      timeN = m_timer.get();
      errorN = SmartDashboard.getNumber("ang", 0);

      SmartDashboard.putNumber("error", errorN);

      double p = errorN;
      double i = integralL + errorN * (timeN - timeL);
      double d = (errorN - errorL) / (timeN - timeL);

      double ternS = (kP * p + kI * i + kD * d) * maxS / errorMax;
      if(Math.abs(ternS) > maxS){
        ternS = (Math.abs(ternS) / ternS) * maxS;
      }
      m_drive.tankDrive(ternS, -ternS);

      if(Math.abs(SmartDashboard.getNumber("ang", 0)) < errorRange){
        correctTime += timeN - timeL;
      } else {
        correctTime = 0;
      }

      timeL = timeN;
      errorL = errorN;
      integralL = i;
    }
    m_drive.stopMotor();
  }*/

  public void SetSpeed(Double Left, Double Right){
    m_drive.tankDrive(Left, Right);
  }

  public void EnableRotateGyro(double degree){
    RotateGyroPID.setSetpoint(degree);
    RotateGyroPID.enable();
  }

  public void DisableRotateGyro(){
    RotateGyroPID.disable();
  }

  public boolean OnTargetGyro(){
    return RotateValuePID.onTarget();
  }

  public void EnableRotateValue(){
    RotateValuePID.setSetpoint(0);
    RotateValuePID.enable();
  }

  public void DisableRotateValue(){
    RotateValuePID.disable();
  }

  public boolean OnTargetValue(){
    return RotateValuePID.onTarget();
  }
}
