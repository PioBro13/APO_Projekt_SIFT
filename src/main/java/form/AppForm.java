package form;



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
    private JButton duplicateButton;
    private JButton saveFileButton;
    private JButton thresholdingButton;

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

    public int arithmeticValue(){
        int givenLevel = -1;
        try {
             givenLevel = Integer.parseInt(JOptionPane.showInputDialog("Enter integer value"));
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Incorrect data type!\nPlease enter integer number.",
                    "Wrong value", JOptionPane.ERROR_MESSAGE);
        }
        return givenLevel;
    }

    public String operationType(){
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please make a selection:"));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("Addition");
        model.addElement("Division");
        model.addElement("Multiplication");
        JComboBox comboBox = new JComboBox(model);
        panel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Flavor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        System.out.println(result);
        return comboBox.getSelectedItem().toString();
    }

    public String logicOperationType(){
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please make a selection:"));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("NOT");
        model.addElement("AND");
        model.addElement("OR");
        model.addElement("XOR");
        JComboBox comboBox = new JComboBox(model);
        panel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Flavor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        System.out.println(result);
        return comboBox.getSelectedItem().toString();
    }

}
