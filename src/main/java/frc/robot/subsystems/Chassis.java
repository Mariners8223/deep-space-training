/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.commands.ExampleCommand;

/**
 * Add your docs here.
 */
public class Chassis extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private Spark m_frontLeft;
  private Spark m_backLeft;
  private SpeedControllerGroup m_left;

  private Spark m_frontRight;
  private Spark m_backRight;
  private SpeedControllerGroup m_right;

  private DifferentialDrive m_drive;

  private static Chassis instance;

  private Chassis(){
    m_frontLeft = new Spark(RobotMap.LEFT_FRONT_MOTOR);
    m_backLeft = new Spark(RobotMap.LEFT_BACK_MOTOR);
    m_left = new SpeedControllerGroup(m_frontLeft, m_backLeft);

    m_frontRight = new Spark(RobotMap.RIGHT_FRONT_MOTOR);
    m_backRight = new Spark(RobotMap.RIGHT_BACK_MOTOR);
    m_right = new SpeedControllerGroup(m_frontRight, m_backRight);

    m_drive = new DifferentialDrive(m_left, m_right);
  }

  public static Chassis getInstance() {
    if(instance == null) instance = new Chassis();
    return instance;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ExampleCommand();
  }

  public void SetSpeed(Double Left, Double Right){
    m_drive.tankDrive(Left, Right);
  }
}
