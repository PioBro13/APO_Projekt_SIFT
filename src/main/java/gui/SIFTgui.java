package gui;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SIFTgui {
    private static final String[] ELEMENT_TYPE = { "Blur", "Gaussian" };
    private static final String[] BORDERS = { "Constant", "Reflect", "Wrap"};
    private int borderType;

    private Mat matImgFirst;
    private Mat matImgSecond;
    private Mat matFeaturesMached;
    private Mat matImgProbability = new Mat();
    private int kernelSize = 0;
    private int smoothingType;
    private JFrame frame;
    private JLabel imgLabel;
    private JLabel imgLabelProbability = new JLabel();
    private JButton thresholdProbabilityButton = new JButton("Threshold probability image");
    private JLabel imgLabelProbabilityText = new JLabel("Probability image:");
    public SIFTgui(File firstImage,File secondImage) throws IOException {
        matImgFirst = FileService.BufferedImage2Mat(FileService.imageToBuffered(firstImage));
        matImgSecond = FileService.BufferedImage2Mat(FileService.imageToBuffered(secondImage));
        matFeaturesMached  = FeatureMatching.match(firstImage, secondImage);
        // Create and set up the window.
        frame = new JFrame("SIFT GUI");
        // Set up the content pane.
        Image imgMatched = HighGui.toBufferedImage(matFeaturesMached);
        addComponentsToPane(frame.getContentPane(), imgMatched);
        // Use the content pane's default BorderLayout. No need for
        // setLayout(new BorderLayout());
        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    private void addComponentsToPane(Container pane, Image img) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.add(new JLabel("Matched features using SIFT"));

        pane.add(sliderPanel, BorderLayout.PAGE_START);
        imgLabel = new JLabel(new ImageIcon(img));
        sliderPanel.add(imgLabel, BorderLayout.CENTER);
        JButton generateProbabilityButton = new JButton("Generate probability image");
        sliderPanel.add(generateProbabilityButton);
        generateProbabilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                imgLabelProbabilityText.setVisible(true);
                thresholdProbabilityButton.setVisible(true);
            }
        });
        imgLabelProbabilityText.setVisible(false);
        sliderPanel.add(imgLabelProbabilityText);
        sliderPanel.add(imgLabelProbability);
        thresholdProbabilityButton.setVisible(false);
        sliderPanel.add(thresholdProbabilityButton);
    }
    private void update() {
        FeatureExtractionImage imageFirst = new FeatureExtractionImage(matImgFirst);
        FeatureExtractionImage imageSecond = new FeatureExtractionImage(matImgSecond);

        FeatureDescriptionImage desc = new FeatureDescriptionImage(imageFirst,imageSecond);

        ProbabilityImage probabilityImage = new ProbabilityImage(imageFirst,imageSecond,desc);
        matImgProbability = probabilityImage.getMatPanoramaImage();
        Image img = HighGui.toBufferedImage(matImgProbability);
        imgLabelProbability.setIcon(new ImageIcon(img));
        frame.repaint();
    }
}