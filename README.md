# FRC 2015 Position Computer Vision
This software uses footage from a camera aimed at the ground to track the robot's horizontal position. This years robot used Mecanum wheels (which can strafe sideways), so traditional optical encoder position tracking was not effective.

### Hardware:
This software runs on an Nvidia Jetson TK1 devkit that has an Axis IP camera attached to it. The Jetson is then connected through an ethernet switch to the NI RoboRIO (robot's controller).

### How does it work?
An algorithm called Optical Flow is used to compare each frame in a video stream to the one before it, and determine the amount of movement that occured in between. By pointing the camera at the ground, it is possible to derive a vector that represents the robot's horizontal motion between two frames.
