package form;



import gui.SIFTgui;
import utils.FeatureMatching;
import utils.FileService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AppForm extends JFrame{
    private JPanel mainPanel;
    private JButton openFileButton;
    private JButton SIFTImageButton;

    private BufferedImage lastOpenedFile;

    public AppForm(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        SIFTImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File openedFile = openFile();
                    lastOpenedFile = FileService.imageToBuffered(openedFile);
                    FileService.openImage(openedFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        SIFTImageButton.addComponentListener(new ComponentAdapter() {
        });
        SIFTImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new SIFTgui(openFile(),openFile());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public File openFile(){
        JFileChooser fc = new JFileChooser("src/main/resources");
        int returnVal = fc.showOpenDialog(this.mainPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            System.out.println("Opened file: " + file.getAbsolutePath());
            return file;
        } else {
            System.out.println("File not found");
            return null;
        }
    }

}
