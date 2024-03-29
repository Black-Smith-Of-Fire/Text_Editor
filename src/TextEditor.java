import java.awt.event.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Scanner;

import static javax.swing.ScrollPaneConstants.*;

public class TextEditor extends JFrame implements ActionListener{
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane =new JScrollPane(textArea);
    JSpinner fontSizeSpinner= new JSpinner();
    JLabel fontLabel= new JLabel("Font : ");
    JButton fontColourButton = new JButton("Colour");
    JComboBox fontBox ;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    TextEditor(){
        //Physical Properties
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(500,500);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);


        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial",Font.PLAIN,20));

        //Adding everything
        scrollPane.setPreferredSize(new Dimension(450,450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        fontSizeSpinner.setPreferredSize(new Dimension(50,25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int)fontSizeSpinner.getValue()));
            }
        });
        fontColourButton.addActionListener(this);

        String [] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox  = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");

        // menubar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);


        this.setJMenuBar(menuBar);
        this.add(fontSizeSpinner);
        this.add(fontColourButton);
        this.add(scrollPane);
        this.add(fontLabel);
        //  this.add(textArea);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
           if (e.getSource()==fontColourButton){
               JColorChooser colorChooser =new JColorChooser();

               Color color = colorChooser.showDialog(null,"Choose a color",Color.BLACK);
                       textArea.setForeground(color);
           }
           if(e.getSource()==fontBox){
            textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
           }

           if(e.getSource()==openItem){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files","txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION){
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;
                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()){
                        while(fileIn.hasNextLine()){
                            String line = fileIn.nextLine()+"/n";
                            textArea.append(line);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                finally{
                    fileIn.close();
                }

            }
           }
           
           if(e.getSource()==saveItem){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response =fileChooser.showSaveDialog(null);

            if(response == JFileChooser.APPROVE_OPTION){
                File file;
                PrintWriter fileOut=null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try{
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                }catch(FileNotFoundException e1){
                    e1.printStackTrace();
                }
                finally{
                    fileOut.close();
                }
            }
           }

           if(e.getSource()==exitItem){
            System.exit(0);
           }
    }
}
