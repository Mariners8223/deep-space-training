/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Chassis;

public class RotateGyro extends Command {
  Chassis chassis = Chassis.getInstance();
  double lastTimeOnT;
  double neededTime = 0.2;
  double deg;

  public RotateGyro(double deg) {
    requires(chassis);
    this.deg = deg;
  }

  @Override
  protected void initialize() {
    chassis.EnableRotate(deg);
  }

  @Override
  protected boolean isFinished() {
    if (!chassis.OnTarget())
      lastTimeOnT = Timer.getFPGATimestamp();
    return Timer.getFPGATimestamp() - lastTimeOnT < neededTime;
  }

  @Override
  protected void end() {
    chassis.DisableRotate();
  }

  @Override
  protected void interrupted() {
    chassis.DisableRotate();
  }
}
