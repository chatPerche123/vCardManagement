import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import ezvcard.VCard;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilCalendarModel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AddNewCreateView extends JDialog {
    protected JDialog jdialog;
    //List of element in the Form section
    Object[] formListElement;
    //is a file have already been created ? (from JOption)
    static boolean onGoingOperation = false;
    static String fileNameJOptionPane = "";

    //VCARD I AM CREATING IN THE FORM
    protected VCard vcardForm;

    protected JPanel buttonsNorthPanel;
    protected JPanel buttonSouthPanel;
    protected JPanel insideFormPanel;
    protected JScrollPane middleFormPanel;
    protected JButton addNewInformation;
    protected JButton saveInformation;
    protected JButton leave;
    protected JButton reset;
    //
    protected JLabel vcardFormatTitle;
    protected JLabel vcardFormatLabel;
    //
    protected JLabel personalInformationTitle;
    protected JLabel familyNameLabel;
    protected JLabel givenNameLabel;
    protected JLabel prefixesLabel;
    protected JLabel suffixesLabel;
    protected JLabel additionalNameLabel;
    //
    protected JLabel addressTitle;
    protected JLabel addressTypeLabel;
    protected JLabel streetNameLabel;
    protected JLabel cityNameLabel;
    protected JLabel regionNameLabel;
    protected JLabel postalCodeNameLabel;
    protected JLabel countryNameLabel;
    //
    protected JLabel addressTitle2;
    protected JLabel addressTypeLabel2;
    protected JLabel streetNameLabel2;
    protected JLabel cityNameLabel2;
    protected JLabel regionNameLabel2;
    protected JLabel postalCodeNameLabel2;
    protected JLabel countryNameLabel2;
    //
    protected JLabel addressTitle3;
    protected JLabel addressTypeLabel3;
    protected JLabel streetNameLabel3;
    protected JLabel cityNameLabel3;
    protected JLabel regionNameLabel3;
    protected JLabel postalCodeNameLabel3;
    protected JLabel countryNameLabel3;
    //


    protected JLabel numberInformationTitle;
    protected JLabel mobilNumberLabel;
    protected JLabel workNumberLabel;
    protected JLabel homeNumberLabel;
    //
    protected JLabel mailTitle;
    protected JLabel personalMailLabel;
    protected JLabel proMailLabel;
    protected JLabel otherMailLabel;
    //
    protected JLabel organisationTitle;
    protected JLabel organisationLabel;
    //
    protected JLabel titleTitre;
    protected JLabel titleLabel;
    //
    protected JLabel birthdayTitle;
    protected JLabel birthdayLabel;
    //
    protected JLabel urlTitle;
    protected JLabel url1;
    protected JLabel url2;
    protected JLabel url3;
    //
    protected JLabel noteTitre;
    protected JLabel noteLabel;
    //
    protected JLabel vcardFormatOnlyV4;
    //
    protected JLabel hobbyTitre;
    protected JLabel hobbyLabel;
    //
    protected JLabel interestTitre;
    protected JLabel interestLabel;
    //
    protected JLabel genderTitre;
    protected JLabel genderLabel;
    //
    protected JLabel birthplaceTitle;
    protected JLabel birthplaceLabel;


    final JComboBox vcardFormatCombo;
    //
    final JTextField familyNameTxt;
    final JTextField givenNameTxt;
    final JTextField prefixesTxt;
    final JTextField suffixesTxt;
    final JTextField additionalNameTxt;
    final JTextField formatedNameTxt;
    //
    final JComboBox addressTypeComboBox;
    final JTextField streetNameTxt;
    final JTextField cityNameTxt;
    final JTextField regionNameTxt;
    final JTextField postalCodeNameTxt;
    final JTextField countryNameTxt;
    //
    final JComboBox addressTypeComboBox2;
    final JTextField streetNameTxt2;
    final JTextField cityNameTxt2;
    final JTextField regionNameTxt2;
    final JTextField postalCodeNameTxt2;
    final JTextField countryNameTxt2;
    //
    final JComboBox addressTypeComboBox3;
    final JTextField streetNameTxt3;
    final JTextField cityNameTxt3;
    final JTextField regionNameTxt3;
    final JTextField postalCodeNameTxt3;
    final JTextField countryNameTxt3;
    //
    final JTextField mobilNumberTxt;
    final JTextField workNumberTxt;
    final JTextField homeNumberTxt;
    //
    final JTextField personalMailTxt;
    final JTextField proMailTxt;
    final JTextField otherMailTxt;
    //
    final JTextField organisationTxt;
    //
    final JTextField titleTxt;
    //
    JDatePanelImpl datePanel;
    JDatePickerImpl datePicker;
    static Date selectedDate;
    //
    final JTextField url1Txt;
    final JTextField url2Txt;
    final JTextField url3Txt;
    //
    protected JScrollPane noteJscrollpane;
    final JTextArea noteTxt;
    //
    final JTextField hobbyTxt;
    //
    final JTextField interestTxt;
    //
    final JComboBox genderTypeComboBox;
    //
    final JTextField birthPlaceTxt;
    protected MainView mainView;
    static boolean alreadyRun = false;

    public AddNewCreateView(MainView mainView) {
        this.mainView = mainView;
        this.jdialog = new JDialog();
        vcardForm = new VCard();

        this.buttonsNorthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 200, 25));
        this.buttonSouthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 80, 25));
        this.insideFormPanel = new JPanel(new GridBagLayout());//CONTAIN WHAT'S INSIDE MAIN FORM CONTAINER (2)

        this.middleFormPanel = new JScrollPane();//FORM MAIN CONTAINER (1)

        Border LoweredBevelBorder = BorderFactory.createLoweredBevelBorder();
        LineBorder border = new LineBorder(Color.BLACK, 2);
        this.middleFormPanel.setBorder(LoweredBevelBorder);
        this.middleFormPanel.setBorder(border);

        this.addNewInformation = new JButton("SAVE AND ADD MORE");
        this.saveInformation = new JButton("FINAL SAVE");
        this.leave = new JButton("COME BACK");
        this.reset = new JButton("RESET");
        //

        this.vcardFormatTitle = new JLabel("----------------------------------------[VCARD FORMAT]----------------------------------------");
        this.vcardFormatTitle.setFont(this.vcardFormatTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.vcardFormatLabel = new JLabel("CHOOSE VCARD FORMAT:");
        //
        this.personalInformationTitle = new JLabel("----------------------------------------[PERSONAL INFORMATION]----------------------------------------");
        this.personalInformationTitle.setFont(this.personalInformationTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.familyNameLabel = new JLabel("FAMILY NAME: ");
        this.givenNameLabel = new JLabel("GIVEN NAME: ");
        this.prefixesLabel = new JLabel("PREFIXES: ");
        this.suffixesLabel = new JLabel("SUFFIXES: ");
        this.additionalNameLabel = new JLabel("ADDITIONAL NAME: ");



        //
        this.addressTitle = new JLabel("----------------------------------------[ADDRESS INFORMATION 1]----------------------------------------");
        this.addressTitle.setFont(this.addressTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.addressTypeLabel = new JLabel("ADDRESS TYPE: ");
        this.streetNameLabel = new JLabel("STREET NAME: ");
        this.cityNameLabel = new JLabel("CITY NAME: ");
        this.regionNameLabel = new JLabel("REGION NAME: ");
        this.postalCodeNameLabel = new JLabel("POSTAL CODE: ");
        this.countryNameLabel = new JLabel("COUNTRY NAME: ");
        //
        this.addressTitle2 = new JLabel("----------------------------------------[ADDRESS INFORMATION 2]----------------------------------------");
        this.addressTitle2.setFont(this.addressTitle2.getFont().deriveFont(Font.BOLD, 14f));
        this.addressTypeLabel2 = new JLabel("ADDRESS TYPE: ");
        this.streetNameLabel2 = new JLabel("STREET NAME: ");
        this.cityNameLabel2 = new JLabel("CITY NAME: ");
        this.regionNameLabel2 = new JLabel("REGION NAME: ");
        this.postalCodeNameLabel2 = new JLabel("POSTAL CODE: ");
        this.countryNameLabel2 = new JLabel("COUNTRY NAME: ");
        //
        this.addressTitle3 = new JLabel("----------------------------------------[ADDRESS INFORMATION 3]----------------------------------------");
        this.addressTitle3.setFont(this.addressTitle3.getFont().deriveFont(Font.BOLD, 14f));
        this.addressTypeLabel3 = new JLabel("ADDRESS TYPE: ");
        this.streetNameLabel3 = new JLabel("STREET NAME: ");
        this.cityNameLabel3 = new JLabel("CITY NAME: ");
        this.regionNameLabel3 = new JLabel("REGION NAME: ");
        this.postalCodeNameLabel3 = new JLabel("POSTAL CODE: ");
        this.countryNameLabel3 = new JLabel("COUNTRY NAME: ");
        //



        //
        this.numberInformationTitle = new JLabel("----------------------------------------[PHONE INFORMATION]----------------------------------------");
        this.numberInformationTitle.setFont(this.numberInformationTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.mobilNumberLabel = new JLabel("MOBIL NUMBER: ");
        this.workNumberLabel = new JLabel("WORK NUMBER: ");
        this.homeNumberLabel = new JLabel("HOME NUMBER: ");
        //
        this.mailTitle = new JLabel("----------------------------------------[MAIL INFORMATION]----------------------------------------");
        this.mailTitle.setFont(this.mailTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.personalMailLabel = new JLabel("PERSONAL MAIL: ");
        this.proMailLabel = new JLabel("PRO MAIL: ");
        this.otherMailLabel = new JLabel("OTHER: ");
        //
        this.organisationTitle = new JLabel("----------------------------------------[ORGANISATION INFORMATION]----------------------------------------");
        this.organisationTitle.setFont(this.organisationTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.organisationLabel = new JLabel("ORGANISATION: ");
        //
        this.titleTitre = new JLabel("----------------------------------------[TITLE INFORMATION]----------------------------------------");
        this.titleTitre.setFont(this.titleTitre.getFont().deriveFont(Font.BOLD, 14f));
        this.titleLabel = new JLabel("TITLE: ");
        //
        this.birthdayTitle = new JLabel("----------------------------------------[BIRTHDAY INFORMATION]----------------------------------------");
        this.birthdayTitle.setFont(this.birthdayTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.birthdayLabel = new JLabel("BIRTHDAY: ");
        //
        this.urlTitle = new JLabel("----------------------------------------[URL]----------------------------------------");
        this.urlTitle.setFont(this.urlTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.url1 = new JLabel("URL1: ");
        this.url2 = new JLabel("URL2: ");
        this.url3 = new JLabel("URL3: ");
        //
        this.noteTitre = new JLabel("----------------------------------------[NOTE]----------------------------------------");
        this.noteTitre.setFont(this.noteTitre.getFont().deriveFont(Font.BOLD, 14f));

        this.noteLabel = new JLabel("NOTE(S): ");
        this.noteTxt = new JTextArea(5, 20);
        this.noteTxt.setLineWrap(true);
        this.noteTxt.setWrapStyleWord(true);
        this.noteJscrollpane = new JScrollPane(noteTxt);
        //
        this.vcardFormatOnlyV4 = new JLabel("----------------------------------------------/ / / /[ONLY V4_0] | | |----------------------------------------------");
        this.vcardFormatOnlyV4.setFont(this.vcardFormatOnlyV4.getFont().deriveFont(Font.BOLD, 15f));
        //
        this.hobbyTitre = new JLabel("----------------------------------------[HOBBY]----------------------------------------");
        this.hobbyTitre.setFont(this.hobbyTitre.getFont().deriveFont(Font.BOLD, 14f));
        this.hobbyLabel = new JLabel("HOBBY(IES): ");
        //
        this.interestTitre = new JLabel("----------------------------------------[INTEREST]----------------------------------------");
        this.interestTitre.setFont(this.interestTitre.getFont().deriveFont(Font.BOLD, 14f));
        this.interestLabel = new JLabel("INTEREST(S)");
        //
        this.genderTitre = new JLabel("----------------------------------------[GENDER]----------------------------------------");
        this.genderTitre.setFont(this.genderTitre.getFont().deriveFont(Font.BOLD, 14f));
        this.genderLabel = new JLabel("GENDER(S)");
        //
        this.birthplaceTitle = new JLabel("----------------------------------------[BIRTHPLACE]----------------------------------------");
        this.birthplaceTitle.setFont(this.birthplaceTitle.getFont().deriveFont(Font.BOLD, 14f));
        this.birthplaceLabel = new JLabel("BIRTHPLACE: ");


        //
        String[] vcardTypeComboChoice = {"2_1", "3_0", "4_0"};
        this.vcardFormatCombo = new JComboBox(vcardTypeComboChoice);
        //
        this.familyNameTxt = new JTextField(20);
        this.givenNameTxt = new JTextField(20);
        this.prefixesTxt = new JTextField(20);
        this.prefixesTxt.setToolTipText("ie: M.D");
        /*"Dr. Gregory House M.D."*/

        this.suffixesTxt = new JTextField(20);
        this.suffixesTxt.setToolTipText("ie: Dr.");
        this.additionalNameTxt = new JTextField(20);
        this.formatedNameTxt = new JTextField(20);


        //
        String[] adresseTypeComboChoice = {"", "home", "work", "other"};
        this.addressTypeComboBox = new JComboBox(adresseTypeComboChoice);
        this.streetNameTxt = new JTextField(20);
        this.cityNameTxt = new JTextField(20);
        this.regionNameTxt = new JTextField(20);
        this.postalCodeNameTxt = new JTextField(20);
        this.countryNameTxt = new JTextField(20);
        //
        this.addressTypeComboBox2 = new JComboBox(adresseTypeComboChoice);
        this.streetNameTxt2 = new JTextField(20);
        this.cityNameTxt2 = new JTextField(20);
        this.regionNameTxt2 = new JTextField(20);
        this.postalCodeNameTxt2 = new JTextField(20);
        this.countryNameTxt2 = new JTextField(20);
        //
        this.addressTypeComboBox3 = new JComboBox(adresseTypeComboChoice);
        this.streetNameTxt3 = new JTextField(20);
        this.cityNameTxt3 = new JTextField(20);
        this.regionNameTxt3 = new JTextField(20);
        this.postalCodeNameTxt3 = new JTextField(20);
        this.countryNameTxt3 = new JTextField(20);
        //



        //
        this.mobilNumberTxt = new JTextField(15);
        this.workNumberTxt = new JTextField(15);
        this.homeNumberTxt = new JTextField(15);
        //
        this.personalMailTxt = new JTextField(15);
        this.proMailTxt = new JTextField(15);
        this.otherMailTxt = new JTextField(15);
        //
        this.organisationTxt = new JTextField(15);
        //
        this.titleTxt = new JTextField(15);
        //

        UtilCalendarModel model = new UtilCalendarModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        //
        this.url1Txt = new JTextField(40);
        this.url2Txt = new JTextField(40);
        this.url3Txt = new JTextField(40);
        //
        this.hobbyTxt = new JTextField(20);
        this.hobbyTxt.setToolTipText("Format: hobby1;hobby2;hobby3;...");

        //
        this.interestTxt = new JTextField(20);
        this.interestTxt.setToolTipText("Format: interest1;interest2;interest3;...");
        //
        String[] genderTypeComboChoice = {"", "Male", "Female", "Other"};
        this.genderTypeComboBox = new JComboBox(genderTypeComboChoice);
        //
        this.birthPlaceTxt = new JTextField(20);

        //default choise 2_1
        this.hobbyTxt.setEditable(false);
        this.interestTxt.setEditable(false);
        this.genderTypeComboBox.setEnabled(false);
        this.birthPlaceTxt.setEditable(false);
        ToolTipManager.sharedInstance().setInitialDelay(0);//make the tooltip appear with no delay

        formListElement = new Object[]{familyNameTxt, givenNameTxt, prefixesTxt, suffixesTxt, additionalNameTxt,
                formatedNameTxt, addressTypeComboBox, streetNameTxt, cityNameTxt, regionNameTxt, postalCodeNameTxt, countryNameTxt, mobilNumberTxt,
                workNumberTxt, homeNumberTxt, personalMailTxt, proMailTxt, otherMailTxt, organisationTxt, titleTxt, url1Txt, url2Txt, url3Txt
                , interestTxt, hobbyTxt, birthPlaceTxt, datePicker, genderTypeComboBox, noteTxt,
                addressTypeComboBox2, streetNameTxt2, cityNameTxt2, regionNameTxt2, postalCodeNameTxt2, countryNameTxt2,
                addressTypeComboBox3, streetNameTxt3, cityNameTxt3, regionNameTxt3, postalCodeNameTxt3, countryNameTxt3};

        middleFormPanel.getViewport().setBackground(new java.awt.Color(102, 255, 255));
    }

    protected void createAndShowGUI() {
        alreadyRun = true;
        setVisible(true);//making the frame visible
        setResizable(false);//not resizable, fixed
        setSize(900, 900);
        setLocationRelativeTo(null);//center
        setLayout(new BorderLayout());//BorderLayout est déjà par défaut

        buttonsNorthPanel.setPreferredSize(new Dimension(0, 130));
        buttonSouthPanel.setPreferredSize(new Dimension(0, 130));

        addNewInformation.setActionCommand("SAVEANDADDMOREADDNEWVIEW");
        saveInformation.setActionCommand("SAVEADDNEWVIEW");
        leave.setActionCommand("LEAVEADDNEWVIEW");
        reset.setActionCommand("RESETADDNEWVIEW");

        addNewInformation.addActionListener(mainView.getController());
        saveInformation.addActionListener(mainView.getController());
        leave.addActionListener(mainView.getController());
        reset.addActionListener(mainView.getController());

        datePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar selectedValue = (Calendar) datePicker.getModel().getValue();
                selectedDate = selectedValue.getTime();
            }
        });

        homeNumberTxt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                homeNumberTxt.setEditable(ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyCode() == 8 || ke.getKeyCode() == 127 || ke.getKeyCode() == 37 || ke.getKeyCode() == 39);
            }
        });
        workNumberTxt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                workNumberTxt.setEditable(ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyCode() == 8 || ke.getKeyCode() == 127 || ke.getKeyCode() == 37 || ke.getKeyCode() == 39);
            }
        });
        mobilNumberTxt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                mobilNumberTxt.setEditable(ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyCode() == 8 || ke.getKeyCode() == 127 || ke.getKeyCode() == 37 || ke.getKeyCode() == 39);
            }
        });
        //when the vcard format is selected in the form (vcardFormatCombo), we make accessible the correct elements'form
        vcardFormatCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = (String) vcardFormatCombo.getSelectedItem();
                assert value != null;
                if (value.equals("2_1") || value.equals("3_0")) {
                    hobbyTxt.setText("");
                    hobbyTxt.setEditable(false);
                    interestTxt.setText("");
                    interestTxt.setEditable(false);
                    genderTypeComboBox.setSelectedIndex(0);
                    genderTypeComboBox.setEnabled(false);
                    birthPlaceTxt.setText("");
                    birthPlaceTxt.setEditable(false);
                } else {
                    //4_0
                    hobbyTxt.setEditable(true);
                    interestTxt.setEditable(true);
                    genderTypeComboBox.setEnabled(true);
                    genderTypeComboBox.setEditable(true);
                    birthPlaceTxt.setEditable(true);
                }
            }
        });

        //GRIDBAGLAYOUT
        this.insideFormPanel.setBackground(new Color(255, 255, 255));

        GridBagConstraints constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 1, 0, this.insideFormPanel, this.vcardFormatTitle, new Insets(15, 15, 15, 15));
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 1, this.insideFormPanel, this.vcardFormatLabel);
        doGridBagGUI(constraints, 1, 1, this.insideFormPanel, this.vcardFormatCombo);

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 1, 2, this.insideFormPanel, this.personalInformationTitle, new Insets(15, 15, 15, 15));

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 3, this.insideFormPanel, this.familyNameLabel);
        doGridBagGUI(constraints, 1, 3, this.insideFormPanel, this.familyNameTxt);
        doGridBagGUI(constraints, 0, 4, this.insideFormPanel, this.givenNameLabel);
        doGridBagGUI(constraints, 1, 4, this.insideFormPanel, this.givenNameTxt);
        doGridBagGUI(constraints, 0, 5, this.insideFormPanel, this.prefixesLabel);
        doGridBagGUI(constraints, 1, 5, this.insideFormPanel, this.prefixesTxt);
        doGridBagGUI(constraints, 0, 6, this.insideFormPanel, this.suffixesLabel);
        doGridBagGUI(constraints, 1, 6, this.insideFormPanel, this.suffixesTxt);
        doGridBagGUI(constraints, 0, 7, this.insideFormPanel, this.additionalNameLabel);
        doGridBagGUI(constraints, 1, 7, this.insideFormPanel, this.additionalNameTxt);
        doGridBagGUI(constraints, 1, 8, this.insideFormPanel, this.addressTitle, new Insets(15, 15, 15, 15));

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



        doGridBagGUI(constraints, 1, 15, this.insideFormPanel, this.addressTitle2, new Insets(15, 15, 15, 15));
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 16, this.insideFormPanel, this.addressTypeLabel2);
        doGridBagGUI(constraints, 1, 16, this.insideFormPanel, this.addressTypeComboBox2);
        doGridBagGUI(constraints, 0, 17, this.insideFormPanel, this.streetNameLabel2);
        doGridBagGUI(constraints, 1, 17, this.insideFormPanel, this.streetNameTxt2, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 18, this.insideFormPanel, this.cityNameLabel2);
        doGridBagGUI(constraints, 1, 18, this.insideFormPanel, this.cityNameTxt2);
        doGridBagGUI(constraints, 0, 19, this.insideFormPanel, this.regionNameLabel2);
        doGridBagGUI(constraints, 1, 19, this.insideFormPanel, this.regionNameTxt2, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 20, this.insideFormPanel, this.postalCodeNameLabel2);
        doGridBagGUI(constraints, 1, 20, this.insideFormPanel, this.postalCodeNameTxt2);
        doGridBagGUI(constraints, 0, 21, this.insideFormPanel, this.countryNameLabel2);
        doGridBagGUI(constraints, 1, 21, this.insideFormPanel, this.countryNameTxt2, new Insets(2, 2, 2, 2));



        doGridBagGUI(constraints, 1, 22, this.insideFormPanel, this.addressTitle3, new Insets(15, 15, 15, 15));
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 23, this.insideFormPanel, this.addressTypeLabel3);
        doGridBagGUI(constraints, 1, 23, this.insideFormPanel, this.addressTypeComboBox3);
        doGridBagGUI(constraints, 0, 24, this.insideFormPanel, this.streetNameLabel3);
        doGridBagGUI(constraints, 1, 24, this.insideFormPanel, this.streetNameTxt3, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 25, this.insideFormPanel, this.cityNameLabel3);
        doGridBagGUI(constraints, 1, 25, this.insideFormPanel, this.cityNameTxt3);
        doGridBagGUI(constraints, 0, 26, this.insideFormPanel, this.regionNameLabel3);
        doGridBagGUI(constraints, 1, 26, this.insideFormPanel, this.regionNameTxt3, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 27, this.insideFormPanel, this.postalCodeNameLabel3);
        doGridBagGUI(constraints, 1, 27, this.insideFormPanel, this.postalCodeNameTxt3);
        doGridBagGUI(constraints, 0, 28, this.insideFormPanel, this.countryNameLabel3);
        doGridBagGUI(constraints, 1, 28, this.insideFormPanel, this.countryNameTxt3, new Insets(2, 2, 2, 2));


        doGridBagGUI(constraints, 1, 29, this.insideFormPanel, this.numberInformationTitle, new Insets(15, 15, 15, 15));
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 30, this.insideFormPanel, this.mobilNumberLabel);
        doGridBagGUI(constraints, 1, 30, this.insideFormPanel, this.mobilNumberTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 31, this.insideFormPanel, this.workNumberLabel);
        doGridBagGUI(constraints, 1, 31, this.insideFormPanel, this.workNumberTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 32, this.insideFormPanel, this.homeNumberLabel);
        doGridBagGUI(constraints, 1, 32, this.insideFormPanel, this.homeNumberTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 33, this.insideFormPanel, this.mailTitle, new Insets(15, 15, 15, 15));

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 34, this.insideFormPanel, this.personalMailLabel);
        doGridBagGUI(constraints, 1, 34, this.insideFormPanel, this.personalMailTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 35, this.insideFormPanel, this.proMailLabel);
        doGridBagGUI(constraints, 1, 35, this.insideFormPanel, this.proMailTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 36, this.insideFormPanel, this.otherMailLabel);
        doGridBagGUI(constraints, 1, 36, this.insideFormPanel, this.otherMailTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 37, this.insideFormPanel, this.organisationTitle, new Insets(15, 15, 15, 15));

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 38, this.insideFormPanel, this.organisationLabel);
        doGridBagGUI(constraints, 1, 38, this.insideFormPanel, this.organisationTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 39, this.insideFormPanel, this.titleTitre, new Insets(15, 15, 15, 15));

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 40, this.insideFormPanel, this.titleLabel);
        doGridBagGUI(constraints, 1, 41, this.insideFormPanel, this.titleTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 42, this.insideFormPanel, this.birthdayTitle, new Insets(15, 15, 15, 15));

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 43, this.insideFormPanel, this.birthdayLabel, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 43, this.insideFormPanel, this.datePicker);
        doGridBagGUI(constraints, 1, 44, this.insideFormPanel, this.urlTitle, new Insets(15, 15, 15, 15));

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 45, this.insideFormPanel, this.url1, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 45, this.insideFormPanel, this.url1Txt);

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 46, this.insideFormPanel, this.url2, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 46, this.insideFormPanel, this.url2Txt);

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 47, this.insideFormPanel, this.url3, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 47, this.insideFormPanel, this.url3Txt);

        doGridBagGUI(constraints, 1, 48, this.insideFormPanel, this.noteTitre, new Insets(15, 15, 15, 15));

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 49, this.insideFormPanel, this.noteLabel, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 49, this.insideFormPanel, this.noteJscrollpane);
        doGridBagGUI(constraints, 1, 50, this.insideFormPanel, this.vcardFormatOnlyV4, new Insets(15, 15, 15, 15));
        doGridBagGUI(constraints, 1, 51, this.insideFormPanel, this.hobbyTitre, new Insets(15, 15, 15, 15));


        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 52, this.insideFormPanel, this.hobbyLabel, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 52, this.insideFormPanel, this.hobbyTxt);
        doGridBagGUI(constraints, 1, 53, this.insideFormPanel, this.interestTitre, new Insets(15, 15, 15, 15));
        doGridBagGUI(constraints, 0, 54, this.insideFormPanel, this.interestLabel, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 55, this.insideFormPanel, this.interestTxt);
        doGridBagGUI(constraints, 1, 56, this.insideFormPanel, this.genderTitre, new Insets(15, 15, 15, 15));

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 57, this.insideFormPanel, this.genderLabel, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 57, this.insideFormPanel, this.genderTypeComboBox);
        doGridBagGUI(constraints, 1, 58, this.insideFormPanel, this.birthplaceTitle, new Insets(15, 15, 15, 15));

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 59, this.insideFormPanel, this.birthplaceLabel, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 59, this.insideFormPanel, this.birthPlaceTxt);

        JPanel jp = new JPanel();
        jp.setPreferredSize(new Dimension(200, 30));
        jp.setBackground(new Color(255, 255, 255));
        doGridBagGUI(constraints, 1, 60, this.insideFormPanel, jp);

        this.middleFormPanel.setViewportView(insideFormPanel);//JScrollPane.setViewportView(component)

        this.buttonsNorthPanel.add(addNewInformation);
        this.buttonsNorthPanel.add(saveInformation);
        this.buttonSouthPanel.add(leave);
        this.buttonSouthPanel.add(reset);

        addNewInformation.setPreferredSize(new Dimension(200, 80));
        saveInformation.setPreferredSize(new Dimension(100, 80));
        leave.setPreferredSize(new Dimension(150, 50));
        reset.setPreferredSize(new Dimension(150, 50));


        this.middleFormPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.middleFormPanel.getVerticalScrollBar().setUnitIncrement(16);//speed up scrollbar vertical

        add(buttonsNorthPanel, BorderLayout.NORTH);
        add(middleFormPanel, BorderLayout.CENTER);
        add(buttonSouthPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

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

