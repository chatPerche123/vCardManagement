import ezvcard.VCard;
import ezvcard.property.Address;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ModifyProfilView2AddressesForm  extends JDialog {
    protected Address addressForm;
    protected Address addressFormCopy;

    protected GridBagConstraints constraints = new GridBagConstraints();
    protected VCard vCardProfilFormSelected;//selectedProfil
    HashMap<Object, Boolean> mapFormBool;
    protected JDialog jdialog;
    protected Object[] formListElement;

    protected JPanel buttonSouthPanel;
    protected JPanel insideFormPanel;
    protected JScrollPane middleFormPanel;
    protected JButton saveInformation;
    protected JButton leave;

    protected JLabel addressTitle;
    protected JLabel addressTypeLabel;
    protected JLabel streetNameLabel;
    protected JLabel cityNameLabel;
    protected JLabel regionNameLabel;
    protected JLabel postalCodeNameLabel;
    protected JLabel countryNameLabel;

    final JComboBox addressTypeComboBox;
    final JTextField streetNameTxt;
    final JTextField cityNameTxt;
    final JTextField regionNameTxt;
    final JTextField postalCodeNameTxt;
    final JTextField countryNameTxt;

    protected MainView mainView;
    protected File fileNameSelected;
    static boolean alreadyRun = false;

    public ModifyProfilView2AddressesForm(MainView mainView) {
        this.mainView = mainView;
        this.jdialog = new JDialog();

        this.buttonSouthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 80, 25));
        this.insideFormPanel = new JPanel(new GridBagLayout());//CONTAIN WHAT'S INSIDE MAIN FORM CONTAINER (2)

        this.middleFormPanel = new JScrollPane();//FORM MAIN CONTAINER (1)

        Border LoweredBevelBorder = BorderFactory.createLoweredBevelBorder();
        LineBorder border = new LineBorder(Color.BLACK, 2);
        this.middleFormPanel.setBorder(LoweredBevelBorder);
        this.middleFormPanel.setBorder(border);

        //this.addNewInformation = new JButton("SAVE");
        this.saveInformation = new JButton("SAVE");
        this.leave = new JButton("COME BACK");

        //
        this.addressTitle = new JLabel("----------------------------------------[ADDRESS INFORMATION]----------------------------------------");
        this.addressTitle.setFont(this.addressTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.addressTypeLabel = new JLabel("ADDRESS TYPE: ");
        this.streetNameLabel = new JLabel("STREET NAME: ");
        this.cityNameLabel = new JLabel("CITY NAME: ");
        this.regionNameLabel = new JLabel("REGION NAME: ");
        this.postalCodeNameLabel = new JLabel("POSTAL CODE: ");
        this.countryNameLabel = new JLabel("COUNTRY NAME: ");

        //
        String[] adresseTypeComboChoice = {"", "home", "work", "other"};

        this.addressTypeComboBox = new JComboBox(adresseTypeComboChoice);
        this.streetNameTxt = new JTextField(20);
        this.cityNameTxt = new JTextField(20);
        this.regionNameTxt = new JTextField(20);
        this.postalCodeNameTxt = new JTextField(20);
        this.countryNameTxt = new JTextField(20);
        //


        formListElement = new Object[]{addressTypeComboBox, streetNameTxt, cityNameTxt, regionNameTxt, postalCodeNameTxt, countryNameTxt};
        this.middleFormPanel.getViewport().setBackground(new java.awt.Color(102, 255, 255));
    }

    protected void createAndShowGUI() throws IOException {
        alreadyRun = true;
        setTitle("Addresses form: ");
        setVisible(true);//making the frame visible
        setResizable(false);//not resizable, fixed
        setSize(500, 450);
        setLocationRelativeTo(null);//center
        setLayout(new BorderLayout());//BorderLayout est déjà par défaut

        this.fileNameSelected = mainView.getAddModifyView2().selectedVcard;

        buttonSouthPanel.setPreferredSize(new Dimension(0, 100));

        saveInformation.setActionCommand("SAVEMODIFYPROFILVIEW2ADDRESSES");
        leave.setActionCommand("LEAVEMODIFYPROFILVIEW2ADDRESSES");
        saveInformation.addActionListener(mainView.getController());
        leave.addActionListener(mainView.getController());

        //GRIDBAGLAYOUT
        this.insideFormPanel.setBackground(new Color(255, 255, 255));
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 9, this.insideFormPanel, this.addressTypeLabel);
        doGridBagGUI(constraints, 1, 9, this.insideFormPanel, this.addressTypeComboBox);
        doGridBagGUI(constraints, 0, 10, this.insideFormPanel, this.streetNameLabel);
        doGridBagGUI(constraints, 1, 10, this.insideFormPanel, this.streetNameTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 11, this.insideFormPanel, this.cityNameLabel);
        doGridBagGUI(constraints, 1, 11, this.insideFormPanel, this.cityNameTxt);
        doGridBagGUI(constraints, 0, 12, this.insideFormPanel, this.regionNameLabel);
        doGridBagGUI(constraints, 1, 12, this.insideFormPanel, this.regionNameTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 13, this.insideFormPanel, this.postalCodeNameLabel);
        doGridBagGUI(constraints, 1, 13, this.insideFormPanel, this.postalCodeNameTxt);
        doGridBagGUI(constraints, 0, 14, this.insideFormPanel, this.countryNameLabel);
        doGridBagGUI(constraints, 1, 14, this.insideFormPanel, this.countryNameTxt, new Insets(2, 2, 2, 2));

        JPanel jp = new JPanel();
        jp.setPreferredSize(new Dimension(200, 30));
        jp.setBackground(new Color(255, 255, 255));
        doGridBagGUI(constraints, 1, 60, this.insideFormPanel, jp);
        this.middleFormPanel.setViewportView(insideFormPanel);//JScrollPane.setViewportView(component)

        this.buttonSouthPanel.add(saveInformation);
        this.buttonSouthPanel.add(leave);

        //addNewInformation.setPreferredSize(new Dimension(200, 80));
        saveInformation.setPreferredSize(new Dimension(150, 50));
        leave.setPreferredSize(new Dimension(150, 50));


        this.middleFormPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.middleFormPanel.getVerticalScrollBar().setUnitIncrement(16);//speed up scrollbar vertical

        add(middleFormPanel, BorderLayout.CENTER);
        add(buttonSouthPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    protected void checkInfo(Address addr) {
        addressForm = addr;
       // addressFormCopy = addr.copy();
        if(addr.getTypes().get(0) != null) {
            String typeAdd = addr.getTypes().get(0).toString();

            if (typeAdd.equals("home")) {
                addressTypeComboBox.setSelectedItem("home");
            } else if (typeAdd.equals("work")) {
                addressTypeComboBox.setSelectedItem("work");
            } else {
                //type personalisé ?
                addressTypeComboBox.setSelectedItem("other");
            }
        }
        if(addr.getStreetAddress() != null) {
            streetNameTxt.setText(addr.getStreetAddress());
        }
        if(addr.getLocality() != null) {
            cityNameTxt.setText(addr.getLocality());
        }
        if(addr.getRegion() != null) {
            regionNameTxt.setText(addr.getRegion());
        }
        if(addr.getPostalCode() != null) {
            postalCodeNameTxt.setText(addr.getPostalCode());
        }
        if(addr.getCountry() != null) {
            countryNameTxt.setText(addr.getCountry());
        }
    }


    protected void doGridBagGUI(GridBagConstraints constraints, int gridx, int gridy, JPanel jpanel, Object o, Insets... insets) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        if(insets.length > 0) {
            constraints.insets = insets[0];
        }
        jpanel.add((Component) o, constraints);
    }
}
