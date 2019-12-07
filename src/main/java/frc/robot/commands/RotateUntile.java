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

public class RotateUntile extends Command {
  Chassis chassis = Chassis.getInstance();
  double lastTimeOnT;
  double neededTime = 0.2;
  double deg;
  public RotateUntile() {
    requires(chassis);
  }

  @Override
  protected boolean isFinished() {
    if (!chassis.OnTargetValue())
      lastTimeOnT = Timer.getFPGATimestamp();
    return Timer.getFPGATimestamp() - lastTimeOnT < neededTime;
  }

  @Override
  protected void end() {
    chassis.DisableRotateValue();
  }

  @Override
  protected void interrupted() {
    chassis.DisableRotateValue();
  }
}
