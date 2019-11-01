/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.Chassis;


public class ChassisCommand extends Command {

  private double left, right;
  private Chassis chassis = Chassis.getInstance();

  public ChassisCommand(double Left, double Right) {
    this.left = Left;
    this.right = Right;
    requires(chassis);
  }

  @Override
  protected void initialize() {
    chassis.SetSpeed(left, right);
  }

  @Override
  protected boolean isFinished() {
    return true;
  }

}
