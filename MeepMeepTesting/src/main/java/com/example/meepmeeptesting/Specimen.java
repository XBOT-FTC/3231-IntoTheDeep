package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Specimen {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(90), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-24, 63, Math.toRadians(180))) // right here is where u plug the starting coordinates points

                        .strafeTo(new Vector2d(-24,62))
                        .lineTo(new Vector2d(-37,63))
                        .waitSeconds(.5)
                        .strafeTo(new Vector2d(-37,12))
                        .waitSeconds(.5)
                        .strafeTo(new Vector2d(-47,12))
                        .waitSeconds(.5)
                        .strafeTo(new Vector2d(-47, 58))
                        .waitSeconds(.5)
                        .strafeTo(new Vector2d(-47,12))
                        .waitSeconds(.5)
                        .strafeTo(new Vector2d(-57,12))
                        .waitSeconds(.5)
                        .strafeTo(new Vector2d(-57,58))
                        .waitSeconds(.5)
                        .strafeTo(new Vector2d(-57,12))
                        .waitSeconds(.5)
                        .lineTo(new Vector2d(-60))
                        .waitSeconds(.5)
                        .strafeTo(new Vector2d(-61,58))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}