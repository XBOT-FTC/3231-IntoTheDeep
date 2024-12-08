package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class UpdatedSpecimen {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(90), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-23, 63, Math.toRadians(-90))) // right here is where u plug the starting coordinates points
                        .splineTo(new Vector2d(0,37), Math.toRadians(-90))
                        .waitSeconds(1)
                        .forward(-5)
                        .splineTo(new Vector2d(-47, 47), Math.toRadians(90))
                        .waitSeconds(1)
                        .turn(Math.toRadians(180))
                        .forward(15)
                        .waitSeconds(1)
                        .splineTo(new Vector2d(0,37), Math.toRadians(-90))
                        .waitSeconds(1)
                        .splineTo(new Vector2d(-58, 47), Math.toRadians(-90))
                        .waitSeconds(1)
                        .splineTo(new Vector2d(0,37), Math.toRadians(-90))
                        .waitSeconds(1)
                        .splineTo(new Vector2d(-47,62), Math.toRadians(90))
                        .waitSeconds(1)
                        .splineTo(new Vector2d(-56, 27), Math.toRadians(180))
                        .waitSeconds(1)
                        .splineTo(new Vector2d(-47,62), Math.toRadians(0))
                        .waitSeconds(1)
                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}