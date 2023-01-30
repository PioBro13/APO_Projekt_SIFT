package utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.SIFT;
public class FeatureExtractionImage {

    private Mat image;
    private Mat imageKeypoint;

    private MatOfKeyPoint keypoints;

    public FeatureExtractionImage(Mat image) {
        this.image =  image;
        keypoints = new MatOfKeyPoint();
        imageKeypoint = new MatOfKeyPoint();
        detectKeypointSift();
    }

    private void detectKeypointSift() {
        SIFT detector = SIFT.create();
        detector.detect(image, keypoints);
        keypoints.toArray();
        KeyPoint[] m = keypoints.toArray();
        Features2d.drawKeypoints(image, keypoints, imageKeypoint);
    }

    public Mat getMatImage() {
        return image;
    }

    public Mat getMatImageKeypoint() {
        return imageKeypoint;
    }

    public BufferedImage getBufferedImage() throws IOException {
        return FileService.matToBuffered(image);
    }

    public BufferedImage getBufferedImageKeypoints() throws IOException {
        return FileService.matToBuffered(imageKeypoint);
    }

    public MatOfKeyPoint getKeyPoints() {
        return keypoints;
    }

}
