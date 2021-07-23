import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ModifyView extends JDialog {
    protected JDialog jDialog;
    protected JPanel mainJP;
    protected JButton buttonAdd;
    protected JButton buttonModify;
    protected JButton buttonBack;
    protected MainView mainView;
    static boolean alreadyRun = false;
    public ModifyView(MainView mainView) {
        this.mainView = mainView;
        this.jDialog = this;
        this.mainJP = new JPanel();
        this.mainJP.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));//default anyway
        this.buttonAdd = new JButton("ADD DATA OR PROFIL TO A VCARD");
        this.buttonAdd.setFont(this.buttonAdd.getFont().deriveFont(Font.BOLD, 16f));
        this.buttonModify = new JButton("MODIFY / DELETE PROFIL OR DATA");
        this.buttonModify.setFont(this.buttonModify.getFont().deriveFont(Font.BOLD, 16f));
        this.buttonBack = new JButton("COME BACK TO CREATE VIEW");
        this.buttonBack.setFont(this.buttonBack.getFont().deriveFont(Font.BOLD, 16f));
    }

    protected void createAndShowGUI() {
        this.alreadyRun = true;
        this.buttonAdd.setActionCommand("ADDMODIFYVIEW");
        this.buttonModify.setActionCommand("DELETEMODIFYVIEW");
        this.buttonBack.setActionCommand("BACKMODIFYVIEW");
        this.buttonAdd.addActionListener(mainView.getController());
        this.buttonModify.addActionListener(mainView.getController());
        this.buttonBack.addActionListener(mainView.getController());

        this.mainJP.setPreferredSize(new Dimension(500, 500));
        this.buttonAdd.setPreferredSize(new Dimension(350, 100));
        this.buttonModify.setPreferredSize(new Dimension(350, 100));
        this.buttonBack.setPreferredSize(new Dimension(350, 100));
        this.mainJP.add(buttonAdd);
        this.mainJP.add(buttonModify);
        this.mainJP.add(buttonBack);

        add(mainJP, BorderLayout.CENTER);
        setSize(500, 500); // Absolute size of the window
        setResizable(false);

        setVisible(true);// frame visible
        setLocationRelativeTo(null);// Center the window
        //CLOSE JDIALOG + APP
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
