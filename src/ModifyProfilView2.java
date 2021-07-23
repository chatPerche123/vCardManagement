import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilCalendarModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ModifyProfilView2 extends JDialog {
    protected GridBagConstraints constraints = new GridBagConstraints();

    protected VCard vCardProfilFormSelected;//selectedProfil
    HashMap<Object, Boolean> mapFormBool;
    protected JDialog jdialog;
    protected Object[] formListElement;
    //
    protected VCard vcardForm;
    protected ArrayList<VCard> vcardList;
    //
    protected JPanel buttonsNorthPanel;
    protected JPanel buttonSouthPanel;
    protected JPanel insideFormPanel;
    protected JScrollPane middleFormPanel;
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
    //
    protected JComboBox vcardFormatCombo;
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
    protected JDatePanelImpl datePanel;
    protected JDatePickerImpl datePicker;
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
    //
    protected MainView mainView;
    //
    protected File fileNameSelected;
    static boolean alreadyRun = false;

    //

    protected VCard copy;

    protected JScrollPane jScrollPaneInfoURL = new JScrollPane();
    protected JList infoURLList = new JList();
    protected JScrollPane jScrollPaneInfoMail = new JScrollPane();
    protected JList infoEmailList = new JList();
    protected JScrollPane jScrollPaneInfoTel = new JScrollPane();
    protected JList infoTelList = new JList();
    protected JScrollPane jscrollInfoAdresses = new JScrollPane();
    protected JList infoAddressesList = new JList();

    ArrayList<JList> arrList = new ArrayList<>();


    public ModifyProfilView2(MainView mainView) {
        arrList.add(infoEmailList);
        arrList.add(infoURLList);
        arrList.add(infoTelList);
        arrList.add(infoAddressesList);

        this.mainView = mainView;
        this.jdialog = new JDialog();
        vcardForm = new VCard();
        this.vcardList = new ArrayList<>();
        this.buttonsNorthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 200, 25));
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
        this.addressTitle = new JLabel("----------------------------------------[ADDRESS INFORMATION]----------------------------------------");
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

        this.vcardFormatCombo = new JComboBox();

        formListElement = new Object[]{familyNameTxt, givenNameTxt, prefixesTxt, suffixesTxt, additionalNameTxt,
                formatedNameTxt, addressTypeComboBox, streetNameTxt, cityNameTxt, regionNameTxt, postalCodeNameTxt, countryNameTxt, mobilNumberTxt,
                workNumberTxt, homeNumberTxt, personalMailTxt, proMailTxt, otherMailTxt, organisationTxt, titleTxt, url1Txt, url2Txt, url3Txt
                , interestTxt, hobbyTxt, birthPlaceTxt, datePicker, genderTypeComboBox, noteTxt,
                addressTypeComboBox2, streetNameTxt2, cityNameTxt2, regionNameTxt2, postalCodeNameTxt2, countryNameTxt2,
                addressTypeComboBox3, streetNameTxt3, cityNameTxt3, regionNameTxt3, postalCodeNameTxt3, countryNameTxt3};

        middleFormPanel.getViewport().setBackground(new java.awt.Color(102, 255, 255));

    }

    protected void createAndShowGUI() throws IOException {

        alreadyRun = true;
        setVisible(true);//making the frame visible
        setResizable(false);//not resizable, fixed
        setSize(900, 900);
        setLocationRelativeTo(null);//center
        setLayout(new BorderLayout());//BorderLayout est déjà par défaut

        this.fileNameSelected = mainView.getAddModifyView2().selectedVcard;
        this.setCombo();

        buttonsNorthPanel.setPreferredSize(new Dimension(0, 130));
        buttonSouthPanel.setPreferredSize(new Dimension(0, 130));

        //addNewInformation.setActionCommand("SAVEADDNEWADDMODIFYVIEW");
        saveInformation.setActionCommand("SAVEPROFILMODIFYVIEW2");
        leave.setActionCommand("LEAVEPROFILMODIFYVIEW2");
        reset.setActionCommand("RESETPROFILMODIFYVIEW2");
        //addNewInformation.addActionListener(mainView.getController());
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

        interestTxt.setEditable(false);
        interestTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String originalText = interestTxt.getText();

                    Object[] options = {"Modify", "Cancel"};
                    JPanel panel = new JPanel();
                    panel.add(new JLabel("Value can be : same (no save), different (save) or empty (removed): "));
                    JTextField textField = new JTextField(10);
                    textField.setText(interestTxt.getText());
                    panel.add(textField);

                    int result = JOptionPane.showOptionDialog(null, panel, "Update interest value",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, null);

                    String s = textField.getText();

                    if(originalText.equals(s)) {
                    }
                    if(originalText.isEmpty()) {
                        int size = mainView.getModifyProfilView2().vCardProfilFormSelected.getInterests().size();
                        /*
                        System.out.println("List of interests BEFORE");
                        System.out.println(mainView.getModifyProfilView2().vCardProfilFormSelected.getInterests().toString());
                        */
                        int x = 0;
                        for(int k = 0;k<size; k++) {
                            mainView.getModifyProfilView2().vCardProfilFormSelected.getInterests().remove(x);
                        }
                        /*
                        System.out.println("List of interests AFTER");
                        System.out.println(mainView.getModifyProfilView2().vCardProfilFormSelected.getInterests().toString());
                        */
                    }

                    if(!originalText.equals(s)) {
                        String newString = "";
                        for(int k = 0;k<s.length();k++) {
                            if(s.charAt(k) != ';') {
                                newString += s.charAt(k);
                            }else if(s.charAt(k) == ';') {
                                if(k+1 < s.length()) {

                                    if(s.charAt(k+1) != ';') {
                                        newString += s.charAt(k);
                                    }else {
                                        // ;; /!\
                                    }
                                }
                            }
                        }
                        interestTxt.setText(newString);
                        mainView.getModel().rebuildInterest(newString);
                    }
                    //quelqu'un peut supprimer (ex: h1;h2;h3) h2 et laisser le ';' ...
                    //si j'ai deux ';' à suivre alors je supprime le deuxième
                }
            }
        });


        hobbyTxt.setEditable(false);
        hobbyTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String originalText = hobbyTxt.getText();

                    Object[] options = {"Modify", "Cancel"};
                    JPanel panel = new JPanel();
                    panel.add(new JLabel("Value can be : same (no save), different (save) or empty (removed): "));
                    JTextField textField = new JTextField(10);
                    textField.setText(hobbyTxt.getText());
                    panel.add(textField);

                    int result = JOptionPane.showOptionDialog(null, panel, "Update hobby value",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, null);

                    String s = textField.getText();

                    if(originalText.equals(s)) {
                    }
                    if(originalText.isEmpty()) {

                    }
                    if(!originalText.equals(s)) {
                        String newString = "";
                        for(int k = 0;k<s.length();k++) {
                            if(s.charAt(k) != ';') {
                                newString += s.charAt(k);
                            }else if(s.charAt(k) == ';') {
                                if(k+1 < s.length()) {

                                    if(s.charAt(k+1) != ';') {
                                        newString += s.charAt(k);
                                    }else {
                                        // ;; /!\
                                    }
                                }
                            }
                        }
                        hobbyTxt.setText(newString);
                        mainView.getModel().rebuildHobby(newString);
                    }
                    //quelqu'un peut supprimer (ex: h1;h2;h3) h2 et laisser le ';' ...
                    //si j'ai deux ';' à suivre alors je supprime le deuxième
                }
            }
        });

        infoEmailList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    //boolean d = false;
                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    //1) email1, 2) email2 ....
                    String o = (String) list.getModel().getElementAt(index);

                    for (Map.Entry<String, Email> e : mainView.getModel().mapEmail.entrySet()) {
                        String key = e.getKey(); //"1) email1", "2) email2" ....

                        //Si la valeur de mon tableau est la même que celle sur laquelle j'ai cliquée, alors
                        //je récupère la clef
                        if(key.equals(o)) {
                            Email email = e.getValue();
                            Object[] options = {"Modify", "Cancel"};
                            JPanel panel = new JPanel();
                            panel.add(new JLabel("Value can be : same (no save), different (save) or empty (removed): "));
                            JTextField textField = new JTextField(10);
                            textField.setText(email.getValue());

                            panel.add(textField);

                            int result = JOptionPane.showOptionDialog(null, panel, "Update email value",
                                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                                    null, options, null);

                            if (result == JOptionPane.CLOSED_OPTION || result == JOptionPane.NO_OPTION) {
                                //
                            }
                            if (result == JOptionPane.OK_OPTION) {
                                String newUserInput = textField.getText();
                                //si la nouvelle donnée (ex: on passe de "@1" à "@2")
                                //est-ce-que "@2" est différent de @1" ?
                                //(On compare le résultat de l'utilisateur ("@2") avec celui de l'object ("@1")
                                //et non pas l'élément de la liste ("1) email1 ...) et le String du hashmap)

                                if(newUserInput.isEmpty()) {
                                    mainView.getModel().rebuildMail(1, email, null);
                                    updateMail();
                                    break;
                                }

                                if (!newUserInput.equals(key) && !newUserInput.isEmpty()) {
                                    //nouveau mail
                                    Email em = new Email(newUserInput);
                                    em.getTypes().add(email.getTypes().get(0));
                                    /*
                                    System.out.println("Create a new mail: " + newUserInput);
                                    System.out.println("Initiate of rebuildMail which ... :");
                                    System.out.println("-will look to delete the value: " + email.getValue());
                                    System.out.println("-and remplace it by: " + em.getValue());

                                     */
                                    mainView.getModel().rebuildMail(2, email, em);
                                    //je mets à jours le model et mapMail
                                    updateMail();
                                    break;
                                }

                            } else {

                            }
                        }
                    }

                }
            }
        });

        infoURLList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    //boolean d = false;
                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    //1) email1, 2) email2 ....

                    String o = (String) list.getModel().getElementAt(index);

                    for (Map.Entry<String, Url> e : mainView.getModel().mapURL.entrySet()) {
                        String key = e.getKey();

                        Url value = e.getValue();
                    }

                    for (Map.Entry<String, Url> e : mainView.getModel().mapURL.entrySet()) {
                        String key = e.getKey(); //"1) email1", "2) email2" ....

                        //Si la valeur de mon tableau est la même que celle sur laquelle j'ai cliquée, alors
                        //je récupère la clef
                        if (key.equals(o)) {
                            Url url = e.getValue();

                            Object[] options = {"Modify", "Cancel"};
                            JPanel panel = new JPanel();
                            panel.add(new JLabel("Value can be : same (no save), different (save) or empty (removed): "));
                            JTextField textField = new JTextField(10);
                            textField.setText(url.getValue());

                            panel.add(textField);

                            int result = JOptionPane.showOptionDialog(null, panel, "Update email value",
                                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                                    null, options, null);

                            if (result == JOptionPane.CLOSED_OPTION || result == JOptionPane.NO_OPTION) {
                                //
                            }
                            if (result == JOptionPane.OK_OPTION) {
                                String newUserInput = textField.getText();

                                if(newUserInput.isEmpty()) {
                                    mainView.getModel().rebuildURL(1, url, null);
                                    updateUrl();
                                    break;
                                }

                                if (!newUserInput.equals(key)) {
                                    //nouveau mail
                                    Url em = new Url(newUserInput);

                                    mainView.getModel().rebuildURL(2, url, em);
                                    //je mets à jours le model et mapMail
                                    updateUrl();
                                    break;
                                }
                            } else {
                            }
                        }
                    }
                }
            }
        });

        infoTelList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    //boolean d = false;
                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    //1) email1, 2) email2 ....

                    String o = (String) list.getModel().getElementAt(index);



                    for (Map.Entry<String, Telephone> e : mainView.getModel().mapTEL.entrySet()) {
                        String key = e.getKey(); //"1) email1", "2) email2" ....

                        //Si la valeur de mon tableau est la même que celle sur laquelle j'ai cliquée, alors
                        //je récupère la clef
                        if (key.equals(o)) {
                            Telephone tel = e.getValue();

                            Object[] options = {"Modify", "Cancel"};
                            JPanel panel = new JPanel();
                            panel.add(new JLabel("Value can be : same (no save), different (save) or empty (removed): "));
                            JTextField textField = new JTextField(10);
                            textField.setText(tel.getText());

                            panel.add(textField);

                            int result = JOptionPane.showOptionDialog(null, panel, "Update email value",
                                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                                    null, options, null);

                            if (result == JOptionPane.CLOSED_OPTION || result == JOptionPane.NO_OPTION) {
                                //
                            }
                            if (result == JOptionPane.OK_OPTION) {
                                String newUserInput = textField.getText();

                                if(newUserInput.isEmpty()) {
                                    mainView.getModel().rebuildTel(1, tel, null);
                                    updateTel();
                                    break;
                                }

                                if (!newUserInput.equals(key)) {
                                    //nouveau mail
                                    Telephone em = new Telephone(newUserInput);
                                    em.getTypes().add(tel.getTypes().get(0));

                                    mainView.getModel().rebuildTel(2, tel, em);
                                    //je mets à jours le model et mapMail
                                    updateTel();
                                    break;
                                }
                            } else {
                            }
                        }
                    }
                }
            }
        });

        infoAddressesList.setName("modifyProfilView2Addresses");
        infoAddressesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    //boolean d = false;
                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    String o = (String) list.getModel().getElementAt(index);

                    for (Map.Entry<String, Address> e : mainView.getModel().mapAdresses.entrySet()) {
                        String key = e.getKey();

                        //Si la valeur de mon tableau est la même que celle sur laquelle j'ai cliquée, alors
                        //je récupère la clef
                        if (key.equals(o)) {
                            Address addr = e.getValue();
                            mainView.getController().setAddresses(infoAddressesList.getName(), addr);
                            break;
                        } else {
                        }
                        }
                    }
                }
        });





        //GRIDBAGLAYOUT
        this.insideFormPanel.setBackground(new Color(255, 255, 255));


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

        jscrollInfoAdresses.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jscrollInfoAdresses.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jscrollInfoAdresses.setViewportView(infoAddressesList);
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 1, 9, this.insideFormPanel, jscrollInfoAdresses);

        /*
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 9, this.insideFormPanel, this.addressTypeLabel);
        //doGridBagGUI(constraints, 1, 9, this.insideFormPanel, this.addressTypeComboBox);
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
        */

        jScrollPaneInfoTel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneInfoTel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneInfoTel.setViewportView(infoTelList);
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 1, 29, this.insideFormPanel, this.numberInformationTitle, new Insets(15, 15, 15, 15));
        doGridBagGUI(constraints, 1, 30, this.insideFormPanel, jScrollPaneInfoTel);

        /*
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 30, this.insideFormPanel, this.mobilNumberLabel);
        doGridBagGUI(constraints, 1, 30, this.insideFormPanel, this.mobilNumberTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 31, this.insideFormPanel, this.workNumberLabel);
        doGridBagGUI(constraints, 1, 31, this.insideFormPanel, this.workNumberTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 32, this.insideFormPanel, this.homeNumberLabel);
        doGridBagGUI(constraints, 1, 32, this.insideFormPanel, this.homeNumberTxt, new Insets(2, 2, 2, 2));

         */

        jScrollPaneInfoMail.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneInfoMail.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneInfoMail.setViewportView(infoEmailList);
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 1, 33, this.insideFormPanel, this.mailTitle, new Insets(15, 15, 15, 15));
        doGridBagGUI(constraints, 1, 34, this.insideFormPanel, jScrollPaneInfoMail);

        /*
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 34, this.insideFormPanel, this.personalMailLabel);
        doGridBagGUI(constraints, 1, 34, this.insideFormPanel, this.personalMailTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 35, this.insideFormPanel, this.proMailLabel);
        doGridBagGUI(constraints, 1, 35, this.insideFormPanel, this.proMailTxt, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 0, 36, this.insideFormPanel, this.otherMailLabel);
        doGridBagGUI(constraints, 1, 36, this.insideFormPanel, this.otherMailTxt, new Insets(2, 2, 2, 2));
         */

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 1, 37, this.insideFormPanel, this.organisationTitle, new Insets(15, 15, 15, 15));
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

        /*
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 45, this.insideFormPanel, this.url1, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 45, this.insideFormPanel, this.url1Txt);
         */

        jScrollPaneInfoURL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneInfoURL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneInfoURL.setViewportView(infoURLList);
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 1, 45, this.insideFormPanel, this.urlTitle, new Insets(15, 15, 15, 15));
        doGridBagGUI(constraints, 1, 46, this.insideFormPanel, jScrollPaneInfoURL);

        /*
        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 46, this.insideFormPanel, this.url2, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 46, this.insideFormPanel, this.url2Txt);

        constraints = new GridBagConstraints();
        doGridBagGUI(constraints, 0, 47, this.insideFormPanel, this.url3, new Insets(2, 2, 2, 2));
        doGridBagGUI(constraints, 1, 47, this.insideFormPanel, this.url3Txt);
         */
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

        //this.buttonsNorthPanel.add(addNewInformation);
        this.buttonsNorthPanel.add(saveInformation);
        this.buttonSouthPanel.add(leave);
        this.buttonSouthPanel.add(reset);

        //addNewInformation.setPreferredSize(new Dimension(200, 80));
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
    //filenameselected setter
    protected void setSelectedVcard(File file) {
        this.fileNameSelected = file;
    }
    //update the form access accordingly to the type of VCard (2.1, ...)
    protected void setCombo() throws IOException {
        VCardVersion vCardVersion = mainView.getModel().returnVcardVersion(this.fileNameSelected);
        String[] a =  {vCardVersion.getVersion()};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(a);
        this.vcardFormatCombo.setModel(model);
        this.vcardFormatCombo.setEnabled(false);

        if (vCardVersion.getVersion().equals("2.1") || vCardVersion.getVersion().equals("3.0")) {
            hobbyTxt.setText("");
            hobbyTxt.setEditable(false);
            interestTxt.setText("");
            interestTxt.setEditable(false);
            genderTypeComboBox.setSelectedIndex(0);
            genderTypeComboBox.setEnabled(false);
            birthPlaceTxt.setText("");
            birthPlaceTxt.setEditable(false);

            hobbyTxt.setBackground(Color.lightGray);
            interestTxt.setBackground(Color.lightGray);
            birthPlaceTxt.setBackground(Color.lightGray);

        } else {
            hobbyTxt.setEditable(true);
            interestTxt.setEditable(true);
            genderTypeComboBox.setEnabled(true);
            genderTypeComboBox.setEditable(true);
            birthPlaceTxt.setEditable(true);

            hobbyTxt.setBackground(Color.WHITE);
            interestTxt.setBackground(Color.WHITE);
            birthPlaceTxt.setBackground(Color.WHITE);
        }
    }
    //false => already have the information
    protected void checkInfo() {
        //make a copy of the vCard profil selected
        vCardProfilFormSelected = new VCard(mainView.getAddModifyView2().selectedProfil);
        mapFormBool = new HashMap<>();
        mapFormBool.clear();
        //STRUCTURED NAME//

        if (!vCardProfilFormSelected.getStructuredNames().isEmpty()) {
            //NO STRUCTURED NAME
            for (StructuredName sn : vCardProfilFormSelected.getStructuredNames()) {
                List<String> namesList = new ArrayList<>();

                if (sn.getFamily() != null) {
                    if (!sn.getFamily().equals("[]")) {
                        String familyName = sn.getFamily();
                        this.familyNameTxt.setText(familyName);
                        mapFormBool.put(familyNameTxt, true);
                    } else if(sn.getFamily().equals("")){
                        mapFormBool.put(familyNameTxt, false);
                    }
                } else {
                    mapFormBool.put(familyNameTxt, false);
                }


                if (sn.getGiven() != null) {
                    if (!sn.getGiven().equals("[]")) {
                        String givenName = sn.getGiven();
                        this.givenNameTxt.setText(givenName);
                        mapFormBool.put(givenNameTxt, true);
                    } else if(sn.getGiven().equals("")){
                        mapFormBool.put(givenNameTxt, false);
                    }
                } else {
                    mapFormBool.put(givenNameTxt, false);
                }

                if (sn.getPrefixes() != null) {
                    if (sn.getPrefixes().size() != 0) {
                        if (!sn.getPrefixes().toString().equals("[]")) {
                            String prefixes = sn.getPrefixes().get(0);
                            this.prefixesTxt.setText(prefixes);
                            mapFormBool.put(prefixesTxt, true);
                        } else if(sn.getPrefixes().get(0).equals("")){
                            mapFormBool.put(prefixesTxt, false);
                        }
                    } else {
                        mapFormBool.put(prefixesTxt, false);
                    }
                } else {
                    mapFormBool.put(prefixesTxt, false);
                }
                if (sn.getSuffixes() != null) {
                    if (sn.getPrefixes().size() != 0) {
                        if (!sn.getSuffixes().toString().equals("[]")) {
                            String suffixes = sn.getSuffixes().get(0);
                            this.suffixesTxt.setText(suffixes);
                            mapFormBool.put(suffixesTxt, true);
                        } else if(sn.getSuffixes().get(0).equals("")){
                            mapFormBool.put(suffixesTxt, false);
                        }
                    } else {
                        mapFormBool.put(suffixesTxt, false);
                    }
                } else {
                    mapFormBool.put(suffixesTxt, false);
                }
                if (sn.getAdditionalNames() != null) {
                    if (sn.getAdditionalNames().size() != 0) {
                        if (!sn.getAdditionalNames().toString().equals("[]")) {
                            String additionalName = sn.getAdditionalNames().get(0);
                            this.additionalNameTxt.setText(additionalName);
                            mapFormBool.put(additionalNameTxt, true);
                        } else if(sn.getAdditionalNames().get(0).equals("")){
                            mapFormBool.put(additionalNameTxt, false);
                        }
                    } else {
                        mapFormBool.put(additionalNameTxt, false);
                    }
                } else {
                    mapFormBool.put(additionalNameTxt, false);
                }
            }
        }else {
            mapFormBool.put(prefixesTxt, false);
            mapFormBool.put(suffixesTxt, false);
            mapFormBool.put(additionalNameTxt, false);
            mapFormBool.put(familyNameTxt, false);
            mapFormBool.put(givenNameTxt, false);
        }

            if(vCardProfilFormSelected.getOrganizations() != null) {
                if(vCardProfilFormSelected.getOrganizations().size() != 0) {
                    if (!vCardProfilFormSelected.getOrganizations().toString().equals("[]") || vCardProfilFormSelected.getOrganizations().size() != 0) {
                        if(vCardProfilFormSelected.getOrganizations().get(0).getValues().size() != 0) {
                            String orga = vCardProfilFormSelected.getOrganizations().get(0).getValues().get(0);
                            this.organisationTxt.setText(orga);
                            mapFormBool.put(organisationTxt, true);
                        }
                    } else {
                        mapFormBool.put(organisationTxt, false);
                    }
                }else {
                    mapFormBool.put(organisationTxt, false);
                }
            }else {
                mapFormBool.put(organisationTxt, false);
            }

            if(vCardProfilFormSelected.getTitles().size() !=0) {
                if (!vCardProfilFormSelected.getTitles().toString().equals("[]")) {
                    String title = vCardProfilFormSelected.getTitles().get(0).getValue();
                    this.titleTxt.setText(title);
                    mapFormBool.put(titleTxt, true);
                } else {

                }
            }else {
                mapFormBool.put(titleTxt, false);
            }


            if(vCardProfilFormSelected.getNotes().size() != 0) {
                if (!vCardProfilFormSelected.getNotes().toString().equals("[]")) {
                    String notes = vCardProfilFormSelected.getNotes().get(0).getValue();
                    this.noteTxt.setText(notes);
                    mapFormBool.put(noteTxt, true);
                } else {
                    mapFormBool.put(noteTxt, false);
                }
            }else {
                mapFormBool.put(noteTxt, false);
            }

        if (vCardProfilFormSelected.getBirthday() != null) {
            datePicker.getComponent(1).setEnabled(true);
        }else {
            datePicker.getComponent(1).setEnabled(false);
        }


            /*
            if (vCardProfilFormSelected.getBirthday() != null) {
                Birthday bday = vCardProfilFormSelected.getBirthday();
                Date date = bday.getDate();
                System.out.println(bday.getDate());
                this.selectedDate = date;
                UtilCalendarModel model = new UtilCalendarModel();
                Properties p = new Properties();
                p.put("text.today", "Today");
                p.put("text.month", "Month");
                p.put("text.year", "Year");
                model.setDate(bday.getDate().getYear(), bday.getDate().getMonth(), bday.getDate().getDay());
                //model.setDate(1, 1, 1);
                model.setSelected(true);

                datePanel = new JDatePanelImpl(model, p);
                datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

                mapFormBool.put(datePicker, false);
            } else {
                mapFormBool.put(datePicker, true);
            }
            */

            //if there are MAILS
            if(!vCardProfilFormSelected.getEmails().isEmpty()) {
                //Update the visual
                DefaultListModel defaultListModel = null;
                defaultListModel = this.mainView.getModel().setModelModifyProfilView2EmailList(vCardProfilFormSelected.getEmails());
                infoEmailList.setModel(defaultListModel);
                jScrollPaneInfoMail.setViewportView(infoEmailList);
                jScrollPaneInfoMail.setPreferredSize(new Dimension(200, 150));
            }else {
                mapFormBool.put(infoEmailList, false);
            }

            //if there are URL
            if(!vCardProfilFormSelected.getUrls().isEmpty()) {
                DefaultListModel defaultListModel = null;
                defaultListModel = this.mainView.getModel().setModelModifyProfilView2UrlList(vCardProfilFormSelected.getUrls());
                infoURLList.setModel(defaultListModel);
                jScrollPaneInfoURL.setViewportView(infoURLList);
                jScrollPaneInfoURL.setPreferredSize(new Dimension(200, 150));
            }else {
                mapFormBool.put(infoURLList, false);
            }

            //if there are MAILS
            if(!vCardProfilFormSelected.getTelephoneNumbers().isEmpty()) {
                DefaultListModel defaultListModel = null;
                defaultListModel = this.mainView.getModel().setModelModifyProfilView2TelList(vCardProfilFormSelected.getTelephoneNumbers());
                infoTelList.setModel(defaultListModel);
                jScrollPaneInfoTel.setViewportView(infoTelList);
                jScrollPaneInfoTel.setPreferredSize(new Dimension(200, 150));
            }else {
                mapFormBool.put(infoTelList, false);
            }

            if(!vCardProfilFormSelected.getAddresses().isEmpty()) {
                DefaultListModel defaultListModel = null;
                defaultListModel = this.mainView.getModel().setModelModifyAdressesView2List(vCardProfilFormSelected.getAddresses());
                infoAddressesList.setModel(defaultListModel);
                jscrollInfoAdresses.setViewportView(infoAddressesList);
                jscrollInfoAdresses.setPreferredSize(new Dimension(200, 150));

            }else {
                mapFormBool.put(infoAddressesList, false);
            }

            if (vCardProfilFormSelected.getVersion().toString().equals("4.0")) {
                Birthplace b = vCardProfilFormSelected.getBirthplace();
                if (b != null) {
                    this.birthPlaceTxt.setText(b.getText());
                } else {
                    mapFormBool.put(birthPlaceTxt, false);
                }
                //FEMALE, MALE, OTHER

                if (vCardProfilFormSelected.getGender() != null) {
                    if(vCardProfilFormSelected.getGender().getGender() != null) {
                        String gender = vCardProfilFormSelected.getGender().getGender();
                        this.genderTypeComboBox.setSelectedItem(gender);
                    }
                } else {
                    mapFormBool.put(genderTypeComboBox, false);
                }

                if (!vCardProfilFormSelected.getInterests().isEmpty()) {
                    String interest = "";
                    for(Interest i : vCardProfilFormSelected.getInterests()) {
                        interest += i.getValue() + ";";
                    }
                    this.interestTxt.setText(interest.substring(0, interest.length()-1));
                } else {
                    mapFormBool.put(interestTxt, false);
                }

                if (!vCardProfilFormSelected.getHobbies().isEmpty()) {
                    String hobbies = "";
                    for(Hobby h: vCardProfilFormSelected.getHobbies()) {
                        hobbies += h.getValue() + ";";
                    }
                    this.hobbyTxt.setText(hobbies.substring(0, hobbies.length()-1));
                } else {
                    mapFormBool.put(hobbyTxt, false);
                }
            }

            accessForm();
            // this.accessForm();

    }
    //Update the form accordingly to what the user has access or not ( mapFormBool edited by check() )
    protected void accessForm() {
        for (HashMap.Entry<Object, Boolean> entry : this.mapFormBool.entrySet()) {
            Object key = entry.getKey();
            boolean value = entry.getValue();
            //if true => hide form case
            if(!value) {
                switch (key.getClass().toString()) {
                    case "class javax.swing.JComboBox" -> ((JComboBox) key).setEnabled(false);
                    case "class org.jdatepicker.impl.JDatePickerImpl" -> ((JDatePickerImpl) key).getComponent(1).setEnabled(false);
                    case "class javax.swing.JTextField" -> ((JTextField) key).setEnabled(false);
                    case "class javax.swing.JTextArea" -> ((JTextArea) key).setEnabled(false);
                }
            }else {
                switch (key.getClass().toString()) {
                    case "class javax.swing.JComboBox" -> ((JComboBox) key).setEnabled(true);
                    case "class org.jdatepicker.impl.JDatePickerImpl" -> ((JDatePickerImpl) key).getComponent(1).setEnabled(true);
                    case "class javax.swing.JTextField" -> ((JTextField) key).setEnabled(true);
                    case "class javax.swing.JTextArea" -> ((JTextArea) key).setEnabled(true);
                }
            }
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


    protected void updateMail() {
        DefaultListModel defaultListModel = null;
        defaultListModel = this.mainView.getModel().setModelModifyProfilView2EmailList(vCardProfilFormSelected.getEmails());
        infoEmailList.setModel(defaultListModel);
        jScrollPaneInfoMail.setViewportView(infoEmailList);
    }
    protected void updateUrl() {
        DefaultListModel defaultListModel = null;
        defaultListModel = this.mainView.getModel().setModelModifyProfilView2UrlList((vCardProfilFormSelected.getUrls()));
        infoURLList.setModel(defaultListModel);
        jScrollPaneInfoURL.setViewportView(infoURLList);
    }
    protected void updateTel() {
        DefaultListModel defaultListModel = null;
        defaultListModel = this.mainView.getModel().setModelModifyProfilView2TelList((vCardProfilFormSelected.getTelephoneNumbers()));
        infoTelList.setModel(defaultListModel);
        jScrollPaneInfoTel.setViewportView(infoTelList);
    }
    protected void updateAddresses() {
        DefaultListModel defaultListModel = null;
        defaultListModel = this.mainView.getModel().setModelModifyAdressesView2List((vCardProfilFormSelected.getAddresses()));
        infoAddressesList.setModel(defaultListModel);
        jscrollInfoAdresses.setViewportView(infoAddressesList);
    }
}
