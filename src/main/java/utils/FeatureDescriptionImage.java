package utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.SIFT;

public class FeatureDescriptionImage {
    //Provided images
    FeatureExtractionImage img1, img2;
    //Mat of matched images
    Mat imgMatches;
    //Mat for decriptors of provided images
    Mat descriptors1, descriptors2;
    //Mat of matches between two images
    MatOfDMatch goodMatches;

    public FeatureDescriptionImage(FeatureExtractionImage img1, FeatureExtractionImage img2) {
        this.img1 = img1;
        this.img2 = img2;
        imgMatches = new Mat();

        descriptors1 = new Mat();
        descriptors2 = new Mat();

        goodMatches = new MatOfDMatch();

        describeFeature();

    }

    private void describeFeature() {
        SIFT descriptorExtractor = SIFT.create();

        // Compute descriptors
        descriptorExtractor.compute(img1.getMatImage(), img1.getKeyPoints(), descriptors1);
        descriptorExtractor.compute(img2.getMatImage(), img2.getKeyPoints(), descriptors2);

        // Match descriptor using FLANN matcher
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches);

        // Calculate max and min distances between keypoints
        double maxDist = 0;
        double minDist = 100;
        for (int i = 0; i < matches.rows(); i++) {
            double dist = matches.toArray()[i].distance;
            if (dist < minDist)
                minDist = dist;
            if (dist > maxDist)
                maxDist = dist;
        }
        System.out.println("Max dist : " + maxDist);
        System.out.println("Min dist : " + minDist);

        // Draw good matches (i.e. distance < 3*min_dist )
        List<DMatch> goodMatchesList = new ArrayList<DMatch>();
        for (int i = 0; i < matches.rows(); i++) {
            if (matches.toArray()[i].distance <= 3 * minDist)
                goodMatchesList.add(matches.toArray()[i]);
        }
        goodMatches.fromList(goodMatchesList);

        // Use RANSAC to remove outlier
        MatOfPoint2f ptMat1 = new MatOfPoint2f();
        MatOfPoint2f ptMat2 = new MatOfPoint2f();

        List<Point> ptList1 = new ArrayList<Point>();
        List<Point> ptList2 = new ArrayList<Point>();

        for (int i = 0; i < goodMatches.toArray().length; i++) {
            ptList1.add(img1.getKeyPoints().toArray()[goodMatches.toArray()[i].queryIdx].pt);
            ptList2.add(img2.getKeyPoints().toArray()[goodMatches.toArray()[i].trainIdx].pt);

        }

        ptMat1.fromList(ptList1);
        ptMat2.fromList(ptList2);

        Mat mask = new Mat();
        Mat H = Calib3d.findHomography(ptMat2, ptMat1, Calib3d.RANSAC, 5, mask);

        List<DMatch> goodMatchesListRANSAC = new ArrayList<DMatch>();
        MatOfDMatch goodMatchesRANSAC = new MatOfDMatch();
        for (int i = 0; i < goodMatches.toArray().length; i++) {
            if (mask.get(i, 0)[0] == 1) {
                goodMatchesListRANSAC.add(goodMatches.toArray()[i]);
            }
        }
        goodMatchesRANSAC.fromList(goodMatchesListRANSAC);

        MatOfByte matchesMask = new MatOfByte();
        Features2d.drawMatches(img1.getMatImage(), img1.getKeyPoints(), img2.getMatImage(), img2.getKeyPoints(),
                goodMatchesRANSAC, imgMatches, Scalar.all(-1), Scalar.all(-1), matchesMask,
                Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS);

        System.out.println(goodMatches.toArray().length);
        if (goodMatches.toArray().length == 0)
            System.out.println("No Good Match");

    }

    public Mat getMatImgMatches() {
        return imgMatches;
    }

    public MatOfDMatch getGoodMatches() {
        return goodMatches;
    }

    public BufferedImage getBufferedImgMatches() throws IOException {
        return FileService.matToBuffered(imgMatches);
    }
}
