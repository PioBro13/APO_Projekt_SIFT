package main;

import form.AppForm;
import nu.pattern.OpenCV;
import org.opencv.features2d.SIFT;

import javax.swing.*;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        OpenCV.loadLocally();
        System.load(Paths.get(".").toAbsolutePath().normalize().toString() +"\\opencv\\build\\java\\x64\\opencv_java460.dll");
        SIFT sift = SIFT.create();
        JFrame form = new AppForm("APO Project");
        form.setVisible(true);
        form.setSize(700,500);

    }
}
