import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;


public class ExportView extends JDialog {
    protected MainView mainView;
    protected JLabel myLabel;
    protected JButton button2;
    protected JScrollPane scrollPane;
    protected ImageIcon leftArrow;
    protected JDialog jDialog;
    static boolean alreadyRun = false;

    public ExportView(MainView mainView) {
        this.jDialog = new JDialog();
        this.myLabel = new JLabel("Double-clic to export : ");
        this.leftArrow = new ImageIcon("images/leftArrow.png", "go back");
        this.button2 = new JButton();
        this.mainView = mainView;
    }

    protected void createAndShowGUI() {
        alreadyRun = true;
        setLayout(new FlowLayout());
        //list of file in the folder
        File[] f = this.mainView.getModel().getListOfFiles("import/", "*.vcf", "*.txt");
        DefaultListModel model = new DefaultListModel();
        model.ensureCapacity(300);//arbitraty
        //Building up the model object for the JList
        for (File file : f) {
            model.addElement(file.getName());
        }
        JList list = new JList(model);
        //select the first element by default
        list.setSelectedIndex(0);

        JLabel myLabel = new JLabel("Double-clic to export : ");
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    //Create a File from the list names
                    File file = new File("src/import/" + list.getSelectedValue());

                    JFileChooser jfileChooser = new JFileChooser();
                    jfileChooser.setApproveButtonText("Save");
                    jfileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    jfileChooser.setAcceptAllFileFilterUsed(false);

                    //Picked a File
                    if (jfileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        //Choose the destination
                        File dest = new File(jfileChooser.getSelectedFile().getAbsolutePath());
                        try {
                            //operate the transfer
                            FileUtils.copyFileToDirectory(file, dest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        scrollPane = new JScrollPane(list);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(200, 400));

        JButton button2 = new JButton();
        button2.setActionCommand("BACKEXPORTVIEW");
        button2.addActionListener(this.mainView.getController());

        button2.setSize(150, 50);
        int offset = button2.getInsets().left;
        button2.setIcon(this.mainView.getModel().resizeIcon(leftArrow, button2.getWidth() - offset, button2.getHeight() - offset));

        //CLOSE JDIALOG + APP
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        add(myLabel);
        add(scrollPane);
        add(button2);
        setSize(300, 550); //BEFORE setLocationRelativeTo
        setLocationRelativeTo(null); //CENTER ON THE SCREEN
        setVisible(true);
        setResizable(false);
    }
}
