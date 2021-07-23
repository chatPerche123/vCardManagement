import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class CreateView extends JDialog {
    protected JDialog jdialog;
    protected MainView mainView;
    protected JButton addNewButton;
    protected JButton modifyButton;
    protected JButton deleteButton;
    protected JButton leaveButton;
    protected JPanel footerPanel;
    protected JPanel contentPane;
    protected JPanel buttonsPanel;
    protected JPanel leftScroll;
    protected JPanel middleScroll;
    protected JScrollPane jpLeft;
    protected JLabel jlabelLeft;
    protected JTextPane txtAreaRight;
    protected JScrollPane jpRight;
    protected JLabel jlabelRight;
    protected JList jlistLeft;
    protected DefaultListModel defaultListModel;
    static boolean alreadyRun = false;

    public CreateView(MainView mainView) throws URISyntaxException, UnsupportedEncodingException {
        this.jlistLeft = new JList();
        this.jdialog = new JDialog();
        this.mainView = mainView;

        /*
        File jarPath=new File(CreateView.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String propertiesPath=jarPath.getParentFile().getAbsolutePath()+"\\import";
        System.out.println(" propertiesPath-"+propertiesPath);

         */

        this.defaultListModel = this.mainView.getModel().setModelFromFilePathFormats("import", "*.vcf", "*.txt");



        this.addNewButton = new JButton("CREATE");
        this.modifyButton = new JButton("MODIFY");
        this.deleteButton = new JButton("DELETE");
        this.footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        //JPanel contentPane = (JPanel)getContentPane(); //borderLayout by defaut
        this.contentPane = new JPanel(new BorderLayout()); //borderLayout by défaut
        //100 between elements (left-right)
        this.buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 20));
        this.leftScroll = new JPanel(new BorderLayout());
        //leftScroll.setBackground(Color.RED);
        this.middleScroll = new JPanel(new BorderLayout());
        this.leaveButton = new JButton("GOING BACK");
        this.jpLeft = new JScrollPane();
        this.jlabelLeft = new JLabel("LIST OF FILES : ");
        //
        this.txtAreaRight = new JTextPane();
        this.txtAreaRight.setContentType("text/html");
        //
        this.jpRight = new JScrollPane();
        this.jlabelRight = new JLabel("SELECTED CONTENT : ");
    }

    protected void createAndShowGUI() {
        alreadyRun = true;
        setVisible(true);//making the frame visible
        setResizable(false);//not resizable, fixed
        setSize(800, 800);
        setLocationRelativeTo(null);//center
        //CLOSE JDIALOG + APP
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //
        Color c = new Color(230, 230, 230);
        middleScroll.setBackground(c);
        //
        addNewButton.setActionCommand("ADDCREATEVIEW");
        modifyButton.setActionCommand("MODIFYCREATEVIEW");
        deleteButton.setActionCommand("DELETECREATEVIEW");
        leaveButton.setActionCommand("BACKCREATEVIEW");
        //
        leaveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        //
        //
        addNewButton.setPreferredSize(new Dimension(100, 50));
        modifyButton.setPreferredSize(new Dimension(100, 50));
        deleteButton.setPreferredSize(new Dimension(100, 50));
        leaveButton.setPreferredSize(new Dimension(120, 50));
        //
        //
        buttonsPanel.add(addNewButton);
        buttonsPanel.add(modifyButton);
        buttonsPanel.add(deleteButton);
        footerPanel.add(leaveButton);
        //
        //
        leaveButton.addActionListener(this.mainView.getController());
        addNewButton.addActionListener(this.mainView.getController());
        modifyButton.addActionListener(this.mainView.getController());
        deleteButton.addActionListener(this.mainView.getController());
        //
        //
        jlistLeft.setModel(defaultListModel);
        jpLeft.setViewportView(jlistLeft);
        //
        //
        jpLeft.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jpLeft.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jpLeft.setPreferredSize(new Dimension(200, 400));
        //
        //
        //mon jpanel de gauche qui ajoute mon jscrollpan de gauche
        leftScroll.add(jpLeft);
        //j'assigne le jlabel au jscrollpan
        jlabelLeft.setLabelFor(jpLeft);
        //mon jpanel ajoute le jlabel et le met en haut
        leftScroll.add(jlabelLeft, BorderLayout.NORTH);
        //
        //
        //Placer avant d'ajouter quelque chose dedans ...
        DefaultCaret caret = (DefaultCaret) txtAreaRight.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        //
        txtAreaRight.setEditable(false);
        //
        jpRight.setViewportView(txtAreaRight);
        jpRight.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jpRight.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jpRight.setPreferredSize(new Dimension(400, 400));
        jpRight.setViewportView(txtAreaRight);//remplace le add
        //
        //
        //mon jpanel de droite qui ajoute mon jscrollpan de droite
        middleScroll.add(jpRight);
        //j'assigne le jlabel au jscrollpan
        jlabelRight.setLabelFor(jpRight);
        //mon jpanel ajoute le jlabel et le met en haut
        middleScroll.add(jlabelRight, BorderLayout.NORTH);
        //
        //
        Border LoweredBevelBorder = BorderFactory.createLoweredBevelBorder();
        middleScroll.setBorder(LoweredBevelBorder);
        //
        //
        //After clicking on an element of the JList, with the name i retrieve the content of the associate VCard in
        //a formatted text to the txtArea on the right
        jlistLeft.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getValueIsAdjusting())
                    return;
                //retrieve the name of the value clicked (ex: abcd.vcf)
                if(list.getSelectedValue()==null) {
                //
                }else {
                    ArrayList<ArrayList<String>> arr = new ArrayList<>();
                    try {
                        arr.add(mainView.getModel().readAndShowVCardContent("import/" + list.getSelectedValue()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    StringBuilder arrContent = new StringBuilder();
                    for (ArrayList<String> s1 : arr) {
                        for (String s2 : s1) {
                            arrContent.append(s2);
                        }
                    }
                    txtAreaRight.setText(arrContent.toString());
                }
            }
        });
        //
        //
        contentPane.add(buttonsPanel, BorderLayout.NORTH);
        contentPane.add(leftScroll, BorderLayout.WEST);
        contentPane.add(middleScroll, BorderLayout.CENTER);
        contentPane.add(footerPanel, BorderLayout.SOUTH);
        //
        add(contentPane);
        //
        //https://stackoverflow.com/questions/34778965/how-to-remove-auto-focus-in-swing
        getContentPane().requestFocusInWindow(); //leave the default focus to the JFrame
    }
}
