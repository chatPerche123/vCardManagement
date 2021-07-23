
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.io.text.VCardReader;
import ezvcard.io.text.VCardWriter;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.*;
import ezvcard.util.PartialDate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

public class Model {
    protected ArrayList listValideFormat;
    protected MainView mainView;
    protected VCard profilModel;//intermediary object that contain the deepcopy of the updated selected profil
    protected HashMap<String, Email> mapEmail;
    protected HashMap<String, Url> mapURL;
    protected HashMap<String, Telephone> mapTEL;
    protected HashMap<String, Address> mapAdresses;
    protected VCard profilDeepCopyBeforeFinalSave;

    public Model(MainView mainView) {
        this.mainView = mainView;
        this.listValideFormat = new ArrayList();
        this.mapEmail = new HashMap<>();
        this.mapURL = new HashMap<>();
        this.mapTEL = new HashMap<>();
        this.mapAdresses = new HashMap<>();
        this.profilDeepCopyBeforeFinalSave = new VCard();
        addFormat("txt", "vcf");//valide file format
    }

    protected void doDeleteCreateView() {
        int index = mainView.getCreateView().jlistLeft.getSelectedIndex();
        if (index != -1) {
            String element = mainView.getCreateView().defaultListModel.getElementAt(index).toString();
            mainView.getCreateView().defaultListModel.remove(index);

            Path path = Paths.get("import/" + element);
            mainView.getModel().deleteFile(path);
        }
    }
    //The Import doesn't have a view but a function to make it work
    protected void doImport() {
        JFileChooser fileChooseBox = new JFileChooser();
        File src = null;
        File dest = new File("import/");

        //Filter JFileChooser suggestion (vcf only and by default)
        FileNameExtensionFilter filter = new FileNameExtensionFilter("vCard or txt", "vcf", "txt");
        fileChooseBox.addChoosableFileFilter(filter);//set a filter
        fileChooseBox.setFileFilter(filter);//set as default filter

        //Picked a File
        if (fileChooseBox.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            //selected file
            src = fileChooseBox.getSelectedFile();

            //Check if the file have 'cvf' extension
            boolean cvfExtTrue = this.checkFormat(src.toString());

            if (cvfExtTrue) {
                //Copy a src (file) to a Directory (dest)
                try {
                    FileUtils.copyFileToDirectory(src, dest);
                    showMessageDialog(null, "Your file have been added");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                showMessageDialog(null, "Choose a valide format file (vcf or txt)");
            }
        } else {
            //
        }
    }

    protected void rebuildInterest(String interestStr) {
        VCard profilDeepCopy2 = new VCard(mainView.getModifyProfilView2().vCardProfilFormSelected);

        int size = profilDeepCopy2.getInterests().size();

        for(int i=0; i<size; i++) {
            profilDeepCopy2.getInterests().remove(0);
        }

        String[] arrSplit = interestStr.split(";");
        Interest interest;
        for (String str : arrSplit) {
            interest = new Interest(str);
            profilDeepCopy2.addInterest(interest);
        }
        mainView.getModifyProfilView2().vCardProfilFormSelected = profilDeepCopy2;

        /*
        for(Interest inter : profilDeepCopy2.getInterests()) {
            System.out.println(inter.getValue());
        }
         */
    }

    protected void rebuildHobby(String hobbiesStr) {
        VCard profilDeepCopy2 = new VCard(mainView.getModifyProfilView2().vCardProfilFormSelected);

        int size = profilDeepCopy2.getHobbies().size();

        for(int i=0; i<size; i++) {
            profilDeepCopy2.getHobbies().remove(0);
        }

        String[] arrSplit = hobbiesStr.split(";");
        Hobby hobby;
        for (String str : arrSplit) {
            hobby = new Hobby(str);
            profilDeepCopy2.addHobby(hobby);
        }
        mainView.getModifyProfilView2().vCardProfilFormSelected = profilDeepCopy2;
        /*
        for(Hobby h : profilDeepCopy2.getHobbies()) {
            System.out.println(h.getValue());
        }
        */


    }
    //option | key to update | new key value
    protected void rebuildURL(int i, Url key, Url em) {
        profilDeepCopyBeforeFinalSave = new VCard(mainView.getModifyProfilView2().vCardProfilFormSelected);

        //EMPTY => DELETE
        if (i == 1) {
            for (Url url : mainView.getModifyProfilView2().vCardProfilFormSelected.getUrls()) {
                //if one doesn't exist in my map array then i add it
                if (url.equals(key)) {
                    profilDeepCopyBeforeFinalSave.getUrls().remove(key);
                    mainView.getModifyProfilView2().vCardProfilFormSelected = profilDeepCopyBeforeFinalSave;
                    break;
                }
            }
        }
        //DIFFERENT => UPDATE
        if (i == 2) {
            /*
            System.out.println("List of URL BEFORE");
            for (Url url1 : profilDeepCopyBeforeFinalSave.getUrls()) {
                System.out.println(url1.getValue());
            }
             */

            for (Url url : mainView.getModifyProfilView2().vCardProfilFormSelected.getUrls()) {
                //if one doesn't exist in my map array then i add it
                if (url.equals(key)) {
                    profilDeepCopyBeforeFinalSave.getUrls().remove(key);
                    profilDeepCopyBeforeFinalSave.getUrls().add(em);
                    mainView.getModifyProfilView2().vCardProfilFormSelected = profilDeepCopyBeforeFinalSave;
                    break;
                }
            }
        }
    }

    //option, oldkey, newkey
    protected void rebuildMail(int i, Email key, Email em) {
        profilDeepCopyBeforeFinalSave = new VCard(mainView.getModifyProfilView2().vCardProfilFormSelected);

        if (i == 1) {
            //récupère la première position de mon object "key" dans "getEmails"
            int index = profilDeepCopyBeforeFinalSave.getEmails().indexOf(key);
            //supprime cette occurence de mon tableau
            profilDeepCopyBeforeFinalSave.getEmails().remove(index);
            mainView.getModifyProfilView2().vCardProfilFormSelected = profilDeepCopyBeforeFinalSave;
        }
        if(i==2) {
            /*
            System.out.println("List of mails BEFORE MODIFICATION: ");
            for(Email email1 : profilDeepCopyBeforeFinalSave.getEmails()) {
                System.out.println(email1.getValue());
            }

             */

            //récupère la première position de mon object "key" dans "getEmails"
            int index = profilDeepCopyBeforeFinalSave.getEmails().indexOf(key);
            //supprime cette occurence de mon tableau
            profilDeepCopyBeforeFinalSave.getEmails().remove(index);
            //ajoute la nouvelle
            profilDeepCopyBeforeFinalSave.getEmails().add(em);
            //le tableau s'est réorganiser ...


            mainView.getModifyProfilView2().vCardProfilFormSelected = profilDeepCopyBeforeFinalSave;

            /*
            System.out.println("List of mails AFTER MODIFICATION: ");
            for(Email email2 :  mainView.getModifyProfilView2().vCardProfilFormSelected.getEmails()) {
                System.out.println(email2.getValue());
            }
            */
        }
    }
    protected void rebuildTel(int i, Telephone key, Telephone em) {
        profilDeepCopyBeforeFinalSave = new VCard(mainView.getModifyProfilView2().vCardProfilFormSelected);

        //EMPTY => DELETE
        if (i == 1) {
            for (Telephone tel : mainView.getModifyProfilView2().vCardProfilFormSelected.getTelephoneNumbers()) {
                //if one doesn't exist in my map array then i add it
                if (tel.equals(key)) {
                    //remove tel
                    profilDeepCopyBeforeFinalSave.getTelephoneNumbers().remove(key);
                    mainView.getModifyProfilView2().vCardProfilFormSelected = profilDeepCopyBeforeFinalSave;
                    break;
                }
            }
        }
        //DIFFERENT => UPDATE
        if (i == 2) {
            for (Telephone tel : mainView.getModifyProfilView2().vCardProfilFormSelected.getTelephoneNumbers()) {
                //if one doesn't exist in my map array then i add it
                if (tel.equals(key)) {
                    profilDeepCopyBeforeFinalSave.getTelephoneNumbers().remove(key);
                    profilDeepCopyBeforeFinalSave.getTelephoneNumbers().add(em);
                    mainView.getModifyProfilView2().vCardProfilFormSelected = profilDeepCopyBeforeFinalSave;
                    break;
                }
            }
        }
    }



    //Read a VCard file and extract 'some' valuable informations in a formatted way
    protected ArrayList<String> readAndShowVCardContent(String path) throws IOException {
        File file = new File(path);
        VCardReader reader = new VCardReader(file);

        VCard vcard = null;
        ArrayList<String> arr = new ArrayList<>();
        arr.add("<html>");
        while ((vcard = reader.readNext()) != null) {
            arr.add("<br><br><b><font size='+1'>[STRUCTURED NAME]</font></b>\n<br><br>");
            for (StructuredName sn : vcard.getStructuredNames()) {
                List<String> namesList = new ArrayList<>();

                if(sn.getFamily() != null) {
                    if (!sn.getFamily().equals("[]")) {
                        String familyName = "<b><font color ='#424949' size='+0.5'>FAMILY NAME:  </font></b>" + sn.getFamily() + "<br><br>";
                        namesList.add(familyName);
                    }
                }
                if(sn.getGiven() != null) {
                    if (!sn.getGiven().equals("[]")) {
                        String givenName = "<b><font color ='#424949' size='+0.5'>GIVEN NAME:    </font></b>" + sn.getGiven() + "<br><br>";
                        namesList.add(givenName);
                    }
                }

                if(sn.getPrefixes() != null) {
                    if (!sn.getPrefixes().toString().equals("[]")) {
                        String prefixes = "<b><font color ='#424949' size='+0.5'>PREFIXES:    </font></b>" + sn.getPrefixes().toString() + "<br><br>";
                        namesList.add(prefixes);
                    }
                }
                if (!sn.getSuffixes().toString().equals("[]")) {
                    String suffixes = "<b><font color ='#424949' size='+0.5'>SUFFIXES:    </font></b>" + sn.getSuffixes().toString() + "<br><br>";
                    namesList.add(suffixes);
                }
                if(!sn.getAdditionalNames().toString().equals("[]")) {
                    String additionalName = "<b><font color ='#424949' size='+0.5'>ADDITIONAL-NAME:    </font></b>" + sn.getAdditionalNames().toString() + "<br><br>";
                    namesList.add(additionalName);
                }

                if (vcard.getFormattedName() == null) {
                    //List<String> namesList = Arrays.asList(prefixes, familyName, givenName, suffixes, additionalName);
                    arr.addAll(namesList);
                } else {
                    String formatedName = "<b><font color ='#424949' size='+0.5'>FORMATED-NAME:    </font></b>" + vcard.getFormattedName().getValue() + "\n\n<br><br>";
                    namesList.add(formatedName);
                    //List<String> namesList = Arrays.asList(prefixes, familyName, givenName, suffixes, additionalName, formatedName);
                    arr.addAll(namesList);
                }

            }
            arr.add("<b><font size='+1'>[ADDRESSES]</font></b>\n<br><br>");
            for (Address adr : vcard.getAddresses()) {
                List<String> namesList = new ArrayList<>();
                if (!adr.getTypes().toString().equals("[]")) {
                    String adresseType = "<b><font color='#424949' size='+0.5'>"+adr.getTypes().toString().toUpperCase() + "    </font></b><br>\n";
                    namesList.add(adresseType);
                }
                if(adr.getStreetAddress() != null) {
                    String street = "<b><font color='#424949' size='+0.5'>STREET:    </font></b>" + adr.getStreetAddress() + "\n<br><br>";
                    namesList.add(street);
                }
                if(adr.getRegion() != null) {
                    String city = "<b><font color='#424949' size='+0.5'>CITY:  </font></b>" + adr.getLocality() + "\n<br><br>";
                    namesList.add(city);
                }
                if(adr.getPostalCode() != null) {
                    String postalCode = "<b><font color='#424949' size='+0.5'>POSTAL CODE   </font></b>:  " + adr.getPostalCode() + "\n<br><br>";
                    namesList.add(postalCode);
                }
                if( adr.getRegion() != null) {
                    String region = "<b><font color='#424949' size='+0.5'>REGION:    </font></b>" + adr.getRegion() + "\n<br><br>";
                    namesList.add(region);
                }
                if(adr.getCountry() != null) {
                    String country = "<b><font color='#424949' size='+0.5'>COUNTRY:    </font></b>" + adr.getCountry() + "\n\n<br><br>";
                    namesList.add(country);
                }
                arr.addAll(namesList);
            }
            //streetNameLabel, cityNameLabel, regionNameLabel, postalCodeNameLabel, countryNameLabel
            arr.add("<b><font size='+1'>[TELEPHONES]</font></b>\n<br><br>");
            for (Telephone telephone : vcard.getTelephoneNumbers()) {
                String typeTel = "<b><font color='#424949' size='+0.5'>"+telephone.getTypes().toString() + "   </font></b>:\n<br>";
                String tel = "";
                if(telephone.getText().equals("")) {
                    tel = " empty<br><br>";
                }else {
                    tel = telephone.getText() + "\n\n<br><br>";
                }
                List<String> namesList = Arrays.asList(typeTel, tel);
                arr.addAll(namesList);
            }
            arr.add("<b><font size='+1'>[EMAILS]</font></b>\n<br><br>");
            String workEmail = "";
            for (Email email : vcard.getEmails()) {
                if (email.getTypes().isEmpty()) {
                    workEmail = "<b><font color='#424949' size='+0.5'>[other]   </font></b>:\n<br>";
                } else {

                    workEmail = "<b><font color='#424949' size='+0.5'>"+email.getTypes().toString() + "   </font></b>:\n<br>";
                }
                String em = email.getValue() + "\n\n<br><br>";

                List<String> namesList = Arrays.asList(workEmail, em);
                arr.addAll(namesList);
            }

            arr.add("<b><font size='+1'>[ORGANISATION]</font></b>\n<br><br>");
            for (Organization org : vcard.getOrganizations()) {
                String orga = org.getValues().toString() + "\n\n<br><br>";

                List<String> namesList = Collections.singletonList(orga);
                arr.addAll(namesList);
            }
            arr.add("<b><font size='+1'>[TITLE]</font></b>\n<br><br>");
            //TITLE
            if (!vcard.getTitles().isEmpty()) {
                String title = vcard.getTitles().get(0).getValue() + "\n\n<br><br>";
                arr.add(title);
            }
            arr.add("<b><font size='+1'>[URL]</font></b>\n<br><br>");
            //MEDIA URL
            if (!vcard.getUrls().isEmpty()) {
                int k = 0;

                for (Url url : vcard.getUrls()) {
                    k++;
                    if (k == vcard.getUrls().size()) {
                        arr.add((url.getValue().toString() + "\n\n<br><br>"));
                    } else {
                        arr.add((url.getValue().toString() + "\n<br><br>"));
                    }
                }
            }
            arr.add("<b><font size='+1'>[NOTES]</font></b>\n<br><br>");
            if (!vcard.getNotes().isEmpty()) {
                String note = vcard.getNotes().get(0).getValue() + "\n\n<br><br>";
                arr.add(note);
            }
            Birthday bday = null;
            arr.add("<b><font size='+1'>[BIRTHDAY]</font></b>\n<br><br>");
            if (vcard.getBirthday() != null) {
                bday = vcard.getBirthday();

                if (bday.getDate() != null) {
                    Date date = bday.getDate();
                    //property value is a date
                    arr.add(date.toString() + "\n\n<br><br>");
                }

                PartialDate partialDate = bday.getPartialDate();
                if (partialDate != null) {
                    int year = partialDate.getYear();
                    int month = partialDate.getMonth();
                    arr.add(year + " / " + month + "\n\n<br><br>");
                }

                String text = bday.getText();
                if (text != null) {
                    //property value is plain text
                    arr.add(text + "\n\n<br><br>");
                }

            } else {
                arr.add("[BAD FORMAT / EMPTY]\n\n<br><br>");
            }
            //hobby interrest gender birthplace

            String hobbies = "";
            String interests = "";
            String gender = "";
            String birthPlace = "";
            if(vcard.getVersion().toString().equals("4.0")) {
                arr.add("<b><font size='+1'>[HOBBIES]</font></b>\n<br><br>");
                if(!vcard.getHobbies().isEmpty()) {
                    String str = "";
                    int hobbySize = vcard.getHobbies().size();
                    int k = 0;

                    for(Hobby hobby : vcard.getHobbies()) {
                        str+= hobby.getValue();
                        if(k < hobbySize-1) {//???
                            str+= ";";
                        }
                        k++;
                    }

                    hobbies = str; //vcard.getHobbies().get(0).getValue();
                    arr.add(hobbies + "\n\n<br><br>");
                }
                arr.add("<b><font size='+1'>[INTERESTS]</font></b>\n<br><br>");
                if(!vcard.getInterests().isEmpty()) {
                    String str = "";
                    int interestSize = vcard.getInterests().size();
                    int k = 0;

                    for(Interest interest : vcard.getInterests()) {
                        str+= interest.getValue();
                        if(k < interestSize-1) {//???
                            str+= ";";
                        }
                        k++;
                    }

                    interests = str;//vcard.getInterests().get(0).getValue();
                    arr.add(interests + "\n\n<br><br>");
                }
                arr.add("<b><font size='+1'>[GENDER]</font></b>\n<br><br>");
                if(vcard.getGender() != null) {
                    if (vcard.getGender().getGender() != null) {
                        gender = vcard.getGender().getGender();
                        arr.add(gender + "\n\n<br><br>");
                    }
                }
                arr.add("<b><font size='+1'>[BIRTHPLACE]</font></b>\n<br><br>");
                if(vcard.getBirthplace() != null) {
                    birthPlace = vcard.getBirthplace().getText();
                    arr.add(birthPlace + "\n\n<br><br>");
                }

            }

            arr.set(arr.size() - 1, arr.get(arr.size() - 1) + "__________________\n\n");
        }
        arr.add("</html>");
        reader.close();
        return arr;
    }
    /*
         SAVE AND ADD MORE => 1
         SAVE              => 2
     */
    public final void save(int i) throws IOException {
        //CHECK THE STRUCTUREDNAME PART OF THE FORM
        //If one element is not empty, we build up the n object (and so one...)
        StructuredName n = new StructuredName();
        boolean a = false;
        if (!mainView.getAddNewCreateView().familyNameTxt.getText().equals("")) {
            n.setFamily(mainView.getAddNewCreateView().familyNameTxt.getText());
            a = true;
        }
        if (!mainView.getAddNewCreateView().givenNameTxt.getText().equals("")) {
            n.setGiven(mainView.getAddNewCreateView().givenNameTxt.getText());
            a = true;
        }
        if (!mainView.getAddNewCreateView().prefixesTxt.getText().equals("")) {
            n.getPrefixes().add(mainView.getAddNewCreateView().prefixesTxt.getText());
            a = true;
        }
        if (!mainView.getAddNewCreateView().suffixesTxt.getText().equals("")) {
            n.getSuffixes().add(mainView.getAddNewCreateView().suffixesTxt.getText());
            a = true;
        }
        if (!mainView.getAddNewCreateView().additionalNameTxt.getText().equals("")) {
            n.getAdditionalNames().add(mainView.getAddNewCreateView().additionalNameTxt.getText());
            a = true;
        }
        if(a) {//new input by the form user
            mainView.getAddNewCreateView().vcardForm.setStructuredName(n);
        }
        //CHECK ADDRESS 1 PART OF THE FORM
        boolean b = false;
        Address adr = new Address();
        if (!mainView.getAddNewCreateView().streetNameTxt.getText().equals("")) {
            adr.setStreetAddress(mainView.getAddNewCreateView().streetNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().cityNameTxt.getText().equals("")) {
            adr.setLocality(mainView.getAddNewCreateView().cityNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().regionNameTxt.getText().equals("")) {
            adr.setRegion(mainView.getAddNewCreateView().regionNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().postalCodeNameTxt.getText().equals("")) {
            adr.setPostalCode(mainView.getAddNewCreateView().postalCodeNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().countryNameTxt.getText().equals("")) {
            adr.setCountry(mainView.getAddNewCreateView().countryNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().addressTypeComboBox.getSelectedItem().toString().equals("")) {
            String value = mainView.getAddNewCreateView().addressTypeComboBox.getSelectedItem().toString();
            if (value.equals("home")) {
                adr.getTypes().add(AddressType.HOME);
            } else if (value.equals("work")) {
                adr.getTypes().add(AddressType.WORK);
            } else {
                //type personalisé ?
                adr.getTypes().add(AddressType.get(value));
            }
        }

        if (b) {//new input by the form user
            mainView.getAddNewCreateView().vcardForm.addAddress(adr);
        }
        b = false;
        //CHECK ADDRESS 2 PART OF THE FORM
        Address adr2 = new Address();
        if (!mainView.getAddNewCreateView().streetNameTxt2.getText().equals("")) {
            adr2.setStreetAddress(mainView.getAddNewCreateView().streetNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().cityNameTxt2.getText().equals("")) {
            adr2.setLocality(mainView.getAddNewCreateView().cityNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().regionNameTxt2.getText().equals("")) {
            adr2.setRegion(mainView.getAddNewCreateView().regionNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().postalCodeNameTxt2.getText().equals("")) {
            adr2.setPostalCode(mainView.getAddNewCreateView().postalCodeNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().countryNameTxt2.getText().equals("")) {
            adr2.setCountry(mainView.getAddNewCreateView().countryNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().addressTypeComboBox2.getSelectedItem().toString().equals("")) {
            String value = mainView.getAddNewCreateView().addressTypeComboBox2.getSelectedItem().toString();
            if (value.equals("home")) {
                adr2.getTypes().add(AddressType.HOME);
            } else if (value.equals("work")) {
                adr2.getTypes().add(AddressType.WORK);
            } else {
                //type personalisé ?
                adr2.getTypes().add(AddressType.get(value));
            }
        }
        if (b) {//new input by the form user
            mainView.getAddNewCreateView().vcardForm.addAddress(adr2);
        }
        b = false;

        //CHECK ADDRESS 3 PART OF THE FORM
        Address adr3 = new Address();
        if (!mainView.getAddNewCreateView().streetNameTxt3.getText().equals("")) {
            adr3.setStreetAddress(mainView.getAddNewCreateView().streetNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().cityNameTxt3.getText().equals("")) {
            adr3.setLocality(mainView.getAddNewCreateView().cityNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().regionNameTxt3.getText().equals("")) {
            adr3.setRegion(mainView.getAddNewCreateView().regionNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().postalCodeNameTxt3.getText().equals("")) {
            adr3.setPostalCode(mainView.getAddNewCreateView().postalCodeNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().countryNameTxt3.getText().equals("")) {
            adr3.setCountry(mainView.getAddNewCreateView().countryNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewCreateView().addressTypeComboBox3.getSelectedItem().toString().equals("")) {
            String value = mainView.getAddNewCreateView().addressTypeComboBox3.getSelectedItem().toString();
            if (value.equals("home")) {
                adr3.getTypes().add(AddressType.HOME);
            } else if (value.equals("work")) {
                adr3.getTypes().add(AddressType.WORK);
            } else {
                //type personalisé ?
                adr3.getTypes().add(AddressType.get(value));
            }
        }
        if (b) {//new input by the form user
            mainView.getAddNewCreateView().vcardForm.addAddress(adr3);
        }
        b = false;

        //CHECK THE PHONE PART OF THE FORM
        Telephone tel;
        if (!mainView.getAddNewCreateView().mobilNumberTxt.getText().equals("")) {
            tel = new Telephone(mainView.getAddNewCreateView().mobilNumberTxt.getText());
            tel.getTypes().add(TelephoneType.CELL);
            mainView.getAddNewCreateView().vcardForm.addTelephoneNumber(tel);
        }
        if (!mainView.getAddNewCreateView().workNumberTxt.getText().equals("")) {
            tel = new Telephone(mainView.getAddNewCreateView().workNumberTxt.getText());
            tel.getTypes().add(TelephoneType.WORK);
            mainView.getAddNewCreateView().vcardForm.addTelephoneNumber(tel);
        }
        if (!mainView.getAddNewCreateView().homeNumberTxt.getText().equals("")) {
            tel = new Telephone(mainView.getAddNewCreateView().homeNumberTxt.getText());
            tel.getTypes().add(TelephoneType.HOME);
            mainView.getAddNewCreateView().vcardForm.addTelephoneNumber(tel);
        }

        //CHECK THE EMAIL PART OF THE FORM
        Email email;
        if (!mainView.getAddNewCreateView().personalMailTxt.getText().equals("")) {
            email = new Email(mainView.getAddNewCreateView().personalMailTxt.getText());
            email.getTypes().add(EmailType.HOME);
            mainView.getAddNewCreateView().vcardForm.addEmail(email);
        }
        if (!mainView.getAddNewCreateView().proMailTxt.getText().equals("")) {
            email = new Email(mainView.getAddNewCreateView().proMailTxt.getText());
            email.getTypes().add(EmailType.WORK);
            mainView.getAddNewCreateView().vcardForm.addEmail(email);
        }
        if (!mainView.getAddNewCreateView().otherMailTxt.getText().equals("")) {
            email = new Email(mainView.getAddNewCreateView().otherMailTxt.getText());
            email.getTypes().add(EmailType.get("other"));
            mainView.getAddNewCreateView().vcardForm.addEmail(email);
        }


        Organization org = new Organization();
        if (!mainView.getAddNewCreateView().organisationTxt.getText().equals("")) {
            org.getValues().add(mainView.getAddNewCreateView().organisationTxt.getText());
            mainView.getAddNewCreateView().vcardForm.setOrganization(org);
        }

        Title title = new Title(mainView.getAddNewCreateView().titleTxt.getText());
        if (!mainView.getAddNewCreateView().titleTxt.getText().equals("")) {
            mainView.getAddNewCreateView().vcardForm.addTitle(title);
        }

        Birthday bday = new Birthday(AddNewCreateView.selectedDate);
        if (bday.getDate() != null) {
            mainView.getAddNewCreateView().vcardForm.setBirthday(bday);
        }


        //FORMATED-NAME:  Titre Prenom Deuxieme Prenom Nom, Titre Poste Nominal
        /*"Dr. Gregory House M.D."*/
        /*
         */
        ArrayList<JTextField> arr = new ArrayList<>();

        if (!mainView.getAddNewCreateView().titleTxt.getText().equals("")) {
            arr.add(mainView.getAddNewCreateView().titleTxt);
        }
        if (!mainView.getAddNewCreateView().givenNameTxt.getText().equals("")) {
            arr.add(mainView.getAddNewCreateView().givenNameTxt);
        }
        if (!mainView.getAddNewCreateView().additionalNameTxt.getText().equals("")) {
            arr.add(mainView.getAddNewCreateView().additionalNameTxt);
        }
        if (!mainView.getAddNewCreateView().familyNameTxt.getText().equals("")) {
            arr.add(mainView.getAddNewCreateView().familyNameTxt);
        }
        if (!mainView.getAddNewCreateView().suffixesTxt.getText().equals("")) {
            arr.add(mainView.getAddNewCreateView().suffixesTxt);
        }
        if (!mainView.getAddNewCreateView().prefixesTxt.getText().equals("")) {
            arr.add(mainView.getAddNewCreateView().prefixesTxt);
        }
        if (!arr.isEmpty()) {
            StringBuilder str = new StringBuilder();
            int arrSize = arr.size();
            int arrK = 0;
            for (JTextField jt : arr) {
                arrK++;
                if (arrK == arrSize) {
                    str.append(jt.getText());
                } else {
                    str.append(jt.getText()).append(" ");
                }
            }
            FormattedName fn = new FormattedName(str.toString());
            mainView.getAddNewCreateView().vcardForm.setFormattedName(fn);
        }

        //String url = "[URL]\n" + vcard.getUrls().get(0).getValue() + "\n\n";
        Url url;
        if (!mainView.getAddNewCreateView().url1Txt.getText().equals("")) {
            url = new Url(mainView.getAddNewCreateView().url1Txt.getText());
            mainView.getAddNewCreateView().vcardForm.addUrl(url);
        }
        if (!mainView.getAddNewCreateView().url2Txt.getText().equals("")) {
            url = new Url(mainView.getAddNewCreateView().url2Txt.getText());
            mainView.getAddNewCreateView().vcardForm.addUrl(url);
        }
        if (!mainView.getAddNewCreateView().url3Txt.getText().equals("")) {
            url = new Url(mainView.getAddNewCreateView().url3Txt.getText());
            mainView.getAddNewCreateView().vcardForm.addUrl(url);
        }

        Note note;
        if (!mainView.getAddNewCreateView().noteTxt.getText().equals("")) {
            note = new Note(mainView.getAddNewCreateView().noteTxt.getText());
            mainView.getAddNewCreateView().vcardForm.addNote(note);
        }

        Hobby hobby;
        if (!mainView.getAddNewCreateView().hobbyTxt.getText().equals("")) {
            String hobbiesStr = mainView.getAddNewCreateView().hobbyTxt.getText();
            String[] arrSplit = hobbiesStr.split(";");
            for (String str : arrSplit) {
                hobby = new Hobby(str);
                mainView.getAddNewCreateView().vcardForm.addHobby(hobby);
            }
        }

        Interest interest;
        if (!mainView.getAddNewCreateView().interestTxt.getText().equals("")) {
            String interestStr = mainView.getAddNewCreateView().interestTxt.getText();
            String[] arrSplit = interestStr.split(";");
            for (String str : arrSplit) {
                interest = new Interest(str);
                mainView.getAddNewCreateView().vcardForm.addInterest(interest);
            }
        }

        Gender gender;
        String genderStr = mainView.getAddNewCreateView().genderTypeComboBox.getSelectedItem().toString();
        if (!genderStr.equals("")) {
            gender = new Gender(genderStr);
            mainView.getAddNewCreateView().vcardForm.setGender(gender);
        }

        Birthplace birthplace;
        String birthplaceStr = mainView.getAddNewCreateView().birthPlaceTxt.getText();
        if (!birthplaceStr.equals("")) {
            birthplace = new Birthplace(birthplaceStr);
            mainView.getAddNewCreateView().vcardForm.setBirthplace(birthplace);
        }

        //String vc = Ezvcard.write(vcard).version(VCardVersion.V2_1).go();
        /*
            (1) SAVE AND ADD MORE:
                if boolean is false (default value):
                  -Ask for the future file name and save it (ie var)
                  -Create the file
                  -Write on the file
                  -Change boolean to TRUE
                if boolean is true (already did at least one already step (1)):
                  -I write at the end of the existing file (don't ask for the name, use the existing one -ie var)
            ---------------------
            (2) SAVE AND EXIT:
                if boolean is TRUE (already did step (1)):
                  -I write at the end of the existing file (don't ask for the name, use the existing one -ie var)
                  -Change boolean to FALSE (restart the logic to default state)
                if boolean is FALSE:
                  -Ask for the future file name (saving the name is pointless)
                  -Create the file
                  -Write on the file
                  -Change boolean to FALSE (restart the logic to default state)

            We always start by creating the file then we either add more vCard or finich the work the this file
         */
        //SAVE AND ADD MORE
        if (i == 1) {
            //haveChanged(1)  =>   AddNewCreateView    formListElement
            //if i already wrote on the file (file exisit then) and there is something new to add, i wrote it down
            if (AddNewCreateView.onGoingOperation && mainView.getModel().haveChanged(1)) {
                File file = new File("import/" + AddNewCreateView.fileNameJOptionPane + ".vcf");
                Writer writer = new FileWriter(file, true);
                VCardWriter vcardWriter = new VCardWriter(writer, mainView.getModel().returnVcardVersion());

                //i get the last ongoing VCard to write it down at the end of the file that already contain informations
                vcardWriter.write(mainView.getAddNewCreateView().vcardForm);
                vcardWriter.close();
                writer.close();
                //reinitiate the form for future addition
                mainView.getModel().resetForm(mainView.getAddNewCreateView().formListElement);
                //restart the vcardForm variable for future addition
                mainView.getAddNewCreateView().vcardForm = new VCard();

                //Ask the user for the future name file to create it and be ready to write to it
            } else {
                //haveChanged(1)  =>   AddNewCreateView    formListElement
                if (mainView.getModel().haveChanged(1)) {
                    String s = JOptionPane.showInputDialog(
                            mainView.getAddNewCreateView(),
                            "File name: ",
                            "File name pop up",
                            JOptionPane.PLAIN_MESSAGE);
                    //check if filename is valide
                    if ((s != null) && (s.length() > 0)) {
                        //save file name
                        AddNewCreateView.fileNameJOptionPane = s;

                        String vc = Ezvcard.write(mainView.getAddNewCreateView().vcardForm).version(mainView.getModel().returnVcardVersion()).go();
                        try {
                            FileUtils.writeStringToFile(new File("import/" + s + ".vcf"), vc);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        mainView.getAddNewCreateView().vcardForm = new VCard();
                        AddNewCreateView.onGoingOperation = true;
                        mainView.getAddNewCreateView().vcardFormatCombo.setEnabled(false);
                        mainView.getModel().resetForm(mainView.getAddNewCreateView().formListElement);
                    }else {
                        //bad filename
                    }
                    //if the user finiched to save or just arrived to the form, the button don't do anything
                    //!AddNewCreateView.onGoingOperation && !mainView.getModel().haveChanged(1)
                } else {
                    //no change no save
                }

            }
            //SAVE AND FINICH IT
        } else if (i == 2) {
            //si true, je ne demande pas le nom du fichier et j'écris à la fin de celui ci
            if (AddNewCreateView.onGoingOperation && mainView.getModel().haveChanged(1)) {
                File file = new File("import/" + AddNewCreateView.fileNameJOptionPane + ".vcf");
                Writer writer = new FileWriter(file, true);
                VCardWriter vcardWriter = new VCardWriter(writer, mainView.getModel().returnVcardVersion());
                vcardWriter.write(mainView.getAddNewCreateView().vcardForm);
                vcardWriter.close();
                //Reset onGoingOperation for future operation
                AddNewCreateView.onGoingOperation = false;
                //enable again form vcard option
                mainView.getAddNewCreateView().vcardFormatCombo.setEnabled(true);
                //Reset form
                mainView.getModel().resetForm(mainView.getAddNewCreateView().formListElement);
                //Ask the user for the future name file to create it and be ready to write to it
            } else {
                //If user clicked on "SAVE AND ADD MORE", but don't add anything more and clic on "SAVE",
                //then we restart the form to his default state
                if (AddNewCreateView.onGoingOperation && !mainView.getModel().haveChanged(1)) {
                    mainView.getAddNewCreateView().vcardForm = new VCard();
                    AddNewCreateView.onGoingOperation = false;
                    mainView.getAddNewCreateView().vcardFormatCombo.setEnabled(true);
                    mainView.getModel().resetForm(mainView.getAddNewCreateView().formListElement);
                    //if there is no ongoing vcard creation AND the user completed the form
                    //then it mean he only want to save 1 Vcard to the file
                } else if (!AddNewCreateView.onGoingOperation && mainView.getModel().haveChanged(1)) {
                    //ask for a filename
                    String s = (String) JOptionPane.showInputDialog(
                            mainView.getAddNewCreateView(),
                            "File name: ",
                            "File name pop up",
                            JOptionPane.PLAIN_MESSAGE);
                    //Check if the filename is valide
                    if ((s != null) && (s.length() > 0)) {
                        //retrieve and write the information from the vCardForm to the file spefified by the user
                        String vc = Ezvcard.write(mainView.getAddNewCreateView().vcardForm).version(mainView.getModel().returnVcardVersion()).go();
                        try {
                            FileUtils.writeStringToFile(new File("import\\"+s + ".vcf"), vc);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        //restart
                        mainView.getAddNewCreateView().vcardForm = new VCard();
                        AddNewCreateView.onGoingOperation = false;
                        mainView.getAddNewCreateView().vcardFormatCombo.setEnabled(true);
                        //si nom fichier ok mais pas de changement
                        mainView.getModel().resetForm(mainView.getAddNewCreateView().formListElement);
                    } else {
                        //bad filename
                    }
                    //if the user finiched to save or just arrived to the form, the button don't do anything
                } else if (!AddNewCreateView.onGoingOperation && !mainView.getModel().haveChanged(1)) {
                    //
                }
            }
            /* Ezvcard.write(vcardList).go(file);*/
            mainView.getAddNewCreateView().vcardForm = new VCard();
        }
    }
    //ADD A NEW VCARD TO A FILE
    public final void saveModifyView() throws IOException {
        StructuredName n = new StructuredName();

        if (!mainView.getAddNewAddModifyView().familyNameTxt.getText().equals("")) {
            n.setFamily(mainView.getAddNewAddModifyView().familyNameTxt.getText());
        }
        if (!mainView.getAddNewAddModifyView().givenNameTxt.getText().equals("")) {
            n.setGiven(mainView.getAddNewAddModifyView().givenNameTxt.getText());
        }

        if (!mainView.getAddNewAddModifyView().prefixesTxt.getText().equals("")) {
            n.getPrefixes().add(mainView.getAddNewAddModifyView().prefixesTxt.getText());
        }
        if (!mainView.getAddNewAddModifyView().suffixesTxt.getText().equals("")) {
            n.getSuffixes().add(mainView.getAddNewAddModifyView().suffixesTxt.getText());
        }
        if (!mainView.getAddNewAddModifyView().additionalNameTxt.getText().equals("")) {
            n.getAdditionalNames().add(mainView.getAddNewAddModifyView().additionalNameTxt.getText());
        }

        mainView.getAddNewAddModifyView().vcardForm.setStructuredName(n);

        boolean b = false;
        Address adr = new Address();
        if (!mainView.getAddNewAddModifyView().streetNameTxt.getText().equals("")) {
            adr.setStreetAddress(mainView.getAddNewAddModifyView().streetNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().cityNameTxt.getText().equals("")) {
            adr.setLocality(mainView.getAddNewAddModifyView().cityNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().regionNameTxt.getText().equals("")) {
            adr.setRegion(mainView.getAddNewAddModifyView().regionNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().postalCodeNameTxt.getText().equals("")) {
            adr.setPostalCode(mainView.getAddNewAddModifyView().postalCodeNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().countryNameTxt.getText().equals("")) {
            adr.setCountry(mainView.getAddNewAddModifyView().countryNameTxt.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().addressTypeComboBox.getSelectedItem().toString().equals("")) {
            String value = mainView.getAddNewAddModifyView().addressTypeComboBox.getSelectedItem().toString();
            if (value.equals("home")) {
                adr.getTypes().add(AddressType.HOME);
            } else if (value.equals("work")) {
                adr.getTypes().add(AddressType.WORK);
            } else {
                //type personalisé ?
                adr.getTypes().add(AddressType.get(value));
            }
        }

        if (b) {
            mainView.getAddNewAddModifyView().vcardForm.addAddress(adr);
        }
        b = false;

        Address adr2 = new Address();
        if (!mainView.getAddNewAddModifyView().streetNameTxt2.getText().equals("")) {
            adr2.setStreetAddress(mainView.getAddNewAddModifyView().streetNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().cityNameTxt2.getText().equals("")) {
            adr2.setLocality(mainView.getAddNewAddModifyView().cityNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().regionNameTxt2.getText().equals("")) {
            adr2.setRegion(mainView.getAddNewAddModifyView().regionNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().postalCodeNameTxt2.getText().equals("")) {
            adr2.setPostalCode(mainView.getAddNewAddModifyView().postalCodeNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().countryNameTxt2.getText().equals("")) {
            adr2.setCountry(mainView.getAddNewAddModifyView().countryNameTxt2.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().addressTypeComboBox2.getSelectedItem().toString().equals("")) {
            String value = mainView.getAddNewAddModifyView().addressTypeComboBox2.getSelectedItem().toString();
            if (value.equals("home")) {
                adr2.getTypes().add(AddressType.HOME);
            } else if (value.equals("work")) {
                adr2.getTypes().add(AddressType.WORK);
            } else {
                //type personalisé ?
                adr2.getTypes().add(AddressType.get(value));
            }
        }
        if (b) {
            mainView.getAddNewAddModifyView().vcardForm.addAddress(adr2);
        }
        b = false;

        Address adr3 = new Address();
        if (!mainView.getAddNewAddModifyView().streetNameTxt3.getText().equals("")) {
            adr3.setStreetAddress(mainView.getAddNewAddModifyView().streetNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().cityNameTxt3.getText().equals("")) {
            adr3.setLocality(mainView.getAddNewAddModifyView().cityNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().regionNameTxt3.getText().equals("")) {
            adr3.setRegion(mainView.getAddNewAddModifyView().regionNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().postalCodeNameTxt3.getText().equals("")) {
            adr3.setPostalCode(mainView.getAddNewAddModifyView().postalCodeNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().countryNameTxt3.getText().equals("")) {
            adr3.setCountry(mainView.getAddNewAddModifyView().countryNameTxt3.getText());
            b = true;
        }
        if (!mainView.getAddNewAddModifyView().addressTypeComboBox3.getSelectedItem().toString().equals("")) {
            String value = mainView.getAddNewAddModifyView().addressTypeComboBox3.getSelectedItem().toString();
            if (value.equals("home")) {
                adr3.getTypes().add(AddressType.HOME);
            } else if (value.equals("work")) {
                adr3.getTypes().add(AddressType.WORK);
            } else {
                //type personalisé ?
                adr3.getTypes().add(AddressType.get(value));
            }
        }
        if (b) {
            mainView.getAddNewAddModifyView().vcardForm.addAddress(adr3);
        }
        b = false;


        Telephone tel;
        if (!mainView.getAddNewAddModifyView().mobilNumberTxt.getText().equals("")) {
            tel = new Telephone(mainView.getAddNewAddModifyView().mobilNumberTxt.getText());
            tel.getTypes().add(TelephoneType.CELL);
            mainView.getAddNewAddModifyView().vcardForm.addTelephoneNumber(tel);
        }
        if (!mainView.getAddNewAddModifyView().workNumberTxt.getText().equals("")) {
            tel = new Telephone(mainView.getAddNewAddModifyView().workNumberTxt.getText());
            tel.getTypes().add(TelephoneType.WORK);
            mainView.getAddNewAddModifyView().vcardForm.addTelephoneNumber(tel);
        }
        if (!mainView.getAddNewAddModifyView().homeNumberTxt.getText().equals("")) {
            tel = new Telephone(mainView.getAddNewAddModifyView().homeNumberTxt.getText());
            tel.getTypes().add(TelephoneType.HOME);
            mainView.getAddNewAddModifyView().vcardForm.addTelephoneNumber(tel);
        }


        Email email;
        if (!mainView.getAddNewAddModifyView().personalMailTxt.getText().equals("")) {
            email = new Email(mainView.getAddNewAddModifyView().personalMailTxt.getText());
            email.getTypes().add(EmailType.HOME);
            mainView.getAddNewAddModifyView().vcardForm.addEmail(email);
        }
        if (!mainView.getAddNewAddModifyView().proMailTxt.getText().equals("")) {
            email = new Email(mainView.getAddNewAddModifyView().proMailTxt.getText());
            email.getTypes().add(EmailType.WORK);
            mainView.getAddNewAddModifyView().vcardForm.addEmail(email);
        }
        if (!mainView.getAddNewAddModifyView().otherMailTxt.getText().equals("")) {
            email = new Email(mainView.getAddNewAddModifyView().otherMailTxt.getText());
            email.getTypes().add(EmailType.get("other"));
            mainView.getAddNewAddModifyView().vcardForm.addEmail(email);
        }


        Organization org = new Organization();
        if (!mainView.getAddNewAddModifyView().organisationTxt.getText().equals("")) {
            org.getValues().add(mainView.getAddNewAddModifyView().organisationTxt.getText());
            mainView.getAddNewAddModifyView().vcardForm.setOrganization(org);
        }

        Title title = new Title(mainView.getAddNewAddModifyView().titleTxt.getText());
        if (!mainView.getAddNewAddModifyView().titleTxt.getText().equals("")) {
            mainView.getAddNewAddModifyView().vcardForm.addTitle(title);
        }

        Birthday bday = new Birthday(AddNewAddModifyView.selectedDate);
        if (bday.getDate() != null) {
            mainView.getAddNewAddModifyView().vcardForm.setBirthday(bday);
        }


        //FORMATED-NAME:  Titre Prenom Deuxieme Prenom Nom, Titre Poste Nominal
        /*"Dr. Gregory House M.D."*/
        /*
         */
        ArrayList<JTextField> arr = new ArrayList<>();

        if (!mainView.getAddNewAddModifyView().titleTxt.getText().equals("")) {
            arr.add(mainView.getAddNewAddModifyView().titleTxt);
        }
        if (!mainView.getAddNewAddModifyView().givenNameTxt.getText().equals("")) {
            arr.add(mainView.getAddNewAddModifyView().givenNameTxt);
        }
        if (!mainView.getAddNewAddModifyView().additionalNameTxt.getText().equals("")) {
            arr.add(mainView.getAddNewAddModifyView().additionalNameTxt);
        }
        if (!mainView.getAddNewAddModifyView().familyNameTxt.getText().equals("")) {
            arr.add(mainView.getAddNewAddModifyView().familyNameTxt);
        }
        if (!mainView.getAddNewAddModifyView().suffixesTxt.getText().equals("")) {
            arr.add(mainView.getAddNewAddModifyView().suffixesTxt);
        }
        if (!mainView.getAddNewAddModifyView().prefixesTxt.getText().equals("")) {
            arr.add(mainView.getAddNewAddModifyView().prefixesTxt);
        }
        if (!arr.isEmpty()) {

            StringBuilder str = new StringBuilder();
            int arrSize = arr.size();
            int arrK = 0;
            for (JTextField jt : arr) {
                arrK++;
                if (arrK == arrSize) {
                    str.append(jt.getText());
                } else {
                    str.append(jt.getText()).append(" ");
                }
            }
            FormattedName fn = new FormattedName(str.toString());
            mainView.getAddNewAddModifyView().vcardForm.setFormattedName(fn);

        }

        //String url = "[URL]\n" + vcard.getUrls().get(0).getValue() + "\n\n";
        Url url;
        if (!mainView.getAddNewAddModifyView().url1Txt.getText().equals("")) {
            url = new Url(mainView.getAddNewAddModifyView().url1Txt.getText());
            mainView.getAddNewAddModifyView().vcardForm.addUrl(url);
        }
        if (!mainView.getAddNewAddModifyView().url2Txt.getText().equals("")) {
            url = new Url(mainView.getAddNewAddModifyView().url2Txt.getText());
            mainView.getAddNewAddModifyView().vcardForm.addUrl(url);
        }
        if (!mainView.getAddNewAddModifyView().url3Txt.getText().equals("")) {
            url = new Url(mainView.getAddNewAddModifyView().url3Txt.getText());
            mainView.getAddNewAddModifyView().vcardForm.addUrl(url);
        }

        Note note;
        if (!mainView.getAddNewAddModifyView().noteTxt.getText().equals("")) {
            note = new Note(mainView.getAddNewAddModifyView().noteTxt.getText());
            mainView.getAddNewAddModifyView().vcardForm.addNote(note);
        }

        Hobby hobby;
        if (!mainView.getAddNewAddModifyView().hobbyTxt.getText().equals("")) {
            String hobbiesStr = mainView.getAddNewAddModifyView().hobbyTxt.getText();
            String[] arrSplit = hobbiesStr.split(";");
            for (String str : arrSplit) {
                hobby = new Hobby(str);
                mainView.getAddNewAddModifyView().vcardForm.addHobby(hobby);
            }
        }

        Interest interest;
        if (!mainView.getAddNewAddModifyView().interestTxt.getText().equals("")) {
            String interestStr = mainView.getAddNewAddModifyView().interestTxt.getText();
            String[] arrSplit = interestStr.split(";");
            for (String str : arrSplit) {
                interest = new Interest(str);
                mainView.getAddNewAddModifyView().vcardForm.addInterest(interest);
            }
        }

        Gender gender;
        String genderStr = mainView.getAddNewAddModifyView().genderTypeComboBox.getSelectedItem().toString();
        if (!genderStr.equals("")) {
            gender = new Gender(genderStr);
            mainView.getAddNewAddModifyView().vcardForm.setGender(gender);
        }

        Birthplace birthplace;
        String birthplaceStr = mainView.getAddNewAddModifyView().birthPlaceTxt.getText();
        if (!birthplaceStr.equals("")) {
            birthplace = new Birthplace(birthplaceStr);
            mainView.getAddNewAddModifyView().vcardForm.setBirthplace(birthplace);
        }

        //MY FILE ALREADY EXIST ; IF MY FORM IS NOT EMPTY, i add up the new vcard to the end
        if (mainView.getModel().haveChanged(2)) {
            File file = new File("import/" + mainView.getAddNewAddModifyView().fileNameSelected.getName());
            Writer writer = new FileWriter(file, true);
            VCardWriter vcardWriter = null;
            vcardWriter = new VCardWriter(writer, mainView.getModel().returnVcardVersion(mainView.getAddNewAddModifyView().fileNameSelected));

            vcardWriter.write(mainView.getAddNewAddModifyView().vcardForm);//write my vCard
            vcardWriter.close();
            writer.close();
            mainView.getModel().resetForm(mainView.getAddNewAddModifyView().formListElement);
        }
    }

    //From the Map "mapFormBool" which contains the form component sorted by (true:not empty ; false:empty),
    //i check every possible already empty component that may have an input from the user.
    //If there is, i update a deepcopy object of the profil selected.
    //Then i attribute it to an intermiate VCard object "profilModel"
    public void saveModifyView2() throws Exception {

        //what i have access or not in my form accordingly to my profil
        HashMap<Object, Boolean> mapFormBool = mainView.getAddNewToProfilAddModifyView().mapFormBool;

        //DEEP COPY
        VCard profilModifyDeepCopy = new VCard(mainView.getAddNewToProfilAddModifyView().vCardProfilFormSelected);

        boolean ad1 = false;
        boolean ad2 = false;
        boolean ad3 = false;
        Telephone tel;
        Email email;
        Organization org = new Organization();
        Title title;
        Url url;
        Note note;
        Hobby hobby;
        Interest interest;
        Gender gender;
        Birthplace birthplace;
        Address adr = new Address();
        Address adr2 = new Address();
        Address adr3 = new Address();
        Birthday bday;

        for (HashMap.Entry<Object, Boolean> entry : mapFormBool.entrySet()) {
            Object key = entry.getKey();
            Boolean value = entry.getValue();

            //false => where the user can input anything to begin with
            if (!value) {
                if (key.getClass().toString().equals("class javax.swing.JComboBox")) {
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().addressTypeComboBox)) {
                        if (!mainView.getAddNewToProfilAddModifyView().addressTypeComboBox.getSelectedItem().equals("")) {
                            ad1 = true;
                            String type = mainView.getAddNewToProfilAddModifyView().addressTypeComboBox.getSelectedItem().toString();
                            if (type.equals("home")) {
                                adr.getTypes().add(AddressType.HOME);
                            } else if (type.equals("work")) {
                                adr.getTypes().add(AddressType.WORK);
                            } else {
                                //type personalisé ?
                                adr.getTypes().add(AddressType.get(type));
                            }
                        }else {
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().addressTypeComboBox2)) {
                        if (!mainView.getAddNewToProfilAddModifyView().addressTypeComboBox2.getSelectedItem().equals("")) {

                            ad2 = true;
                            String type = mainView.getAddNewToProfilAddModifyView().addressTypeComboBox2.getSelectedItem().toString();
                            if (type.equals("home")) {
                                adr2.getTypes().add(AddressType.HOME);
                            } else if (type.equals("work")) {
                                adr2.getTypes().add(AddressType.WORK);
                            } else {
                                //type personalisé ?
                                adr2.getTypes().add(AddressType.get(type));
                            }
                        }else {
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().addressTypeComboBox3)) {
                        if (!mainView.getAddNewToProfilAddModifyView().addressTypeComboBox3.getSelectedItem().equals("")) {
                            ad3 = true;
                            String type = mainView.getAddNewToProfilAddModifyView().addressTypeComboBox3.getSelectedItem().toString();
                            if (type.equals("home")) {
                                adr3.getTypes().add(AddressType.HOME);
                            } else if (type.equals("work")) {
                                adr3.getTypes().add(AddressType.WORK);
                            } else {
                                //type personalisé ?
                                adr3.getTypes().add(AddressType.get(type));
                            }
                        }else {
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().genderTypeComboBox)) {
                        if (!mainView.getAddNewToProfilAddModifyView().genderTypeComboBox.getSelectedItem().equals("")) {
                            String genderStr = mainView.getAddNewToProfilAddModifyView().genderTypeComboBox.getSelectedItem().toString();
                            if (!genderStr.equals("")) {
                                gender = new Gender(genderStr);
                                profilModifyDeepCopy.setGender(gender);
                            }
                        }

                    }

                } else if (key.getClass().toString().equals("class org.jdatepicker.impl.JDatePickerImpl")) {
                    if(AddNewToProfilAddModifyView.selectedDate != null) {
                        bday = new Birthday(AddNewToProfilAddModifyView.selectedDate);
                        profilModifyDeepCopy.setBirthday(bday);
                    }


                } else if (key.getClass().toString().equals("class javax.swing.JTextField")) {
                    JTextField familyName = mainView.getAddNewToProfilAddModifyView().familyNameTxt;
                    if (key.equals(familyName)) {
                        if (!familyName.getText().equals("")) {
                            profilModifyDeepCopy.getStructuredName().setFamily(familyName.getText());
                        }
                    }
                    JTextField givenName = mainView.getAddNewToProfilAddModifyView().givenNameTxt;
                    if (key.equals(givenName)) {
                        if (!givenName.getText().equals("")) {
                            profilModifyDeepCopy.getStructuredName().setGiven(givenName.getText());
                        }
                    }
                    JTextField prefixe = mainView.getAddNewToProfilAddModifyView().prefixesTxt;
                    if (key.equals(prefixe)) {
                        if (!prefixe.getText().equals("")) {
                            profilModifyDeepCopy.getStructuredName().getPrefixes().add(prefixe.getText());
                        }
                    }
                    JTextField suffixe = mainView.getAddNewToProfilAddModifyView().suffixesTxt;
                    if (key.equals(suffixe)) {
                        if (!suffixe.getText().equals("")) {
                            profilModifyDeepCopy.getStructuredName().getSuffixes().add(suffixe.getText());
                        }
                    }
                    JTextField additionalName = mainView.getAddNewToProfilAddModifyView().additionalNameTxt;
                    if (key.equals(additionalName)) {
                        if (!additionalName.getText().equals("")) {
                            profilModifyDeepCopy.getStructuredName().getAdditionalNames().add(additionalName.getText());
                        }
                    }

                    /* ADDRESS 1*/
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().streetNameTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().streetNameTxt.getText().equals("")) {
                            ad1 = true;
                            adr.setStreetAddress(mainView.getAddNewToProfilAddModifyView().streetNameTxt.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().cityNameTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().cityNameTxt.getText().equals("")) {
                            ad1 = true;
                            adr.setLocality(mainView.getAddNewToProfilAddModifyView().cityNameTxt.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().regionNameTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().regionNameTxt.getText().equals("")) {
                            ad1 = true;
                            adr.setRegion(mainView.getAddNewToProfilAddModifyView().regionNameTxt.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().postalCodeNameTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().postalCodeNameTxt.getText().equals("")) {
                            ad1 = true;
                            adr.setPostalCode(mainView.getAddNewToProfilAddModifyView().postalCodeNameTxt.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().countryNameTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().countryNameTxt.getText().equals("")) {
                            ad1 = true;
                            adr.setCountry(mainView.getAddNewToProfilAddModifyView().countryNameTxt.getText());
                        }
                    }
                    /* ADDRESS 2*/
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().streetNameTxt2)) {
                        if (!mainView.getAddNewToProfilAddModifyView().streetNameTxt2.getText().equals("")) {
                            ad2 = true;
                            adr2.setStreetAddress(mainView.getAddNewToProfilAddModifyView().streetNameTxt2.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().cityNameTxt2)) {
                        if (!mainView.getAddNewToProfilAddModifyView().cityNameTxt2.getText().equals("")) {
                            ad2 = true;
                            adr2.setLocality(mainView.getAddNewToProfilAddModifyView().cityNameTxt2.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().regionNameTxt2)) {
                        if (!mainView.getAddNewToProfilAddModifyView().regionNameTxt2.getText().equals("")) {
                            ad2 = true;
                            adr2.setRegion(mainView.getAddNewToProfilAddModifyView().regionNameTxt2.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().postalCodeNameTxt2)) {
                        if (!mainView.getAddNewToProfilAddModifyView().postalCodeNameTxt2.getText().equals("")) {
                            ad2 = true;
                            adr2.setPostalCode(mainView.getAddNewToProfilAddModifyView().postalCodeNameTxt2.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().countryNameTxt2)) {
                        if (!mainView.getAddNewToProfilAddModifyView().countryNameTxt2.getText().equals("")) {
                            ad2 = true;
                            adr2.setCountry(mainView.getAddNewToProfilAddModifyView().countryNameTxt2.getText());
                        }
                    }
                    /* ADDRESS 3*/
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().streetNameTxt3)) {
                        if (!mainView.getAddNewToProfilAddModifyView().streetNameTxt3.getText().equals("")) {
                            ad3 = true;
                            adr3.setStreetAddress(mainView.getAddNewToProfilAddModifyView().streetNameTxt3.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().cityNameTxt3)) {
                        if (!mainView.getAddNewToProfilAddModifyView().cityNameTxt3.getText().equals("")) {
                            ad3 = true;
                            adr3.setLocality(mainView.getAddNewToProfilAddModifyView().cityNameTxt3.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().regionNameTxt3)) {
                        if (!mainView.getAddNewToProfilAddModifyView().regionNameTxt3.getText().equals("")) {
                            ad3 = true;
                            adr3.setRegion(mainView.getAddNewToProfilAddModifyView().regionNameTxt3.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().postalCodeNameTxt3)) {
                        if (!mainView.getAddNewToProfilAddModifyView().postalCodeNameTxt3.getText().equals("")) {
                            ad3 = true;
                            adr3.setPostalCode(mainView.getAddNewToProfilAddModifyView().postalCodeNameTxt3.getText());
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().countryNameTxt3)) {
                        if (!mainView.getAddNewToProfilAddModifyView().countryNameTxt3.getText().equals("")) {
                            ad3 = true;
                            adr3.setCountry(mainView.getAddNewToProfilAddModifyView().countryNameTxt3.getText());
                        }
                    }
                    /////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////
                    /* TELEPHONE */
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().mobilNumberTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().mobilNumberTxt.getText().equals("")) {
                            tel = new Telephone(mainView.getAddNewToProfilAddModifyView().mobilNumberTxt.getText());
                            tel.getTypes().add(TelephoneType.CELL);
                            profilModifyDeepCopy.addTelephoneNumber(tel);
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().workNumberTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().workNumberTxt.getText().equals("")) {
                            tel = new Telephone(mainView.getAddNewToProfilAddModifyView().workNumberTxt.getText());
                            tel.getTypes().add(TelephoneType.WORK);
                            profilModifyDeepCopy.addTelephoneNumber(tel);
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().homeNumberTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().homeNumberTxt.getText().equals("")) {
                            tel = new Telephone(mainView.getAddNewToProfilAddModifyView().homeNumberTxt.getText());
                            tel.getTypes().add(TelephoneType.HOME);
                            profilModifyDeepCopy.addTelephoneNumber(tel);
                        }
                    }
                    /////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////
                    /* EMAIL */
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().personalMailTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().personalMailTxt.getText().equals("")) {
                            email = new Email(mainView.getAddNewToProfilAddModifyView().personalMailTxt.getText());
                            email.getTypes().add(EmailType.HOME);
                            profilModifyDeepCopy.addEmail(email);
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().proMailTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().proMailTxt.getText().equals("")) {
                            email = new Email(mainView.getAddNewToProfilAddModifyView().proMailTxt.getText());
                            email.getTypes().add(EmailType.WORK);
                            profilModifyDeepCopy.addEmail(email);
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().otherMailTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().otherMailTxt.getText().equals("")) {
                            email = new Email(mainView.getAddNewToProfilAddModifyView().otherMailTxt.getText());
                            email.getTypes().add(EmailType.get("other"));
                            profilModifyDeepCopy.addEmail(email);
                        }
                    }
                    /////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////
                    /* ORGANIZATION */
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().organisationTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().organisationTxt.getText().equals("")) {
                            org.getValues().add(mainView.getAddNewToProfilAddModifyView().organisationTxt.getText());
                            profilModifyDeepCopy.setOrganization(org);
                        }
                    }
                    /////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////
                    /* TITLE */
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().titleTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().titleTxt.getText().equals("")) {
                            title = new Title(mainView.getAddNewToProfilAddModifyView().titleTxt.getText());
                            if (!title.equals("")) {
                                profilModifyDeepCopy.addTitle(title);
                            }
                        }

                    }
                    /////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////
                    /* URL */
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().url1Txt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().url1Txt.getText().equals("")) {
                            url = new Url(mainView.getAddNewToProfilAddModifyView().url1Txt.getText());
                            profilModifyDeepCopy.addUrl(url);
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().url2Txt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().url2Txt.getText().equals("")) {
                            url = new Url(mainView.getAddNewToProfilAddModifyView().url2Txt.getText());
                            profilModifyDeepCopy.addUrl(url);
                        }
                    }
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().url3Txt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().url3Txt.getText().equals("")) {
                            url = new Url(mainView.getAddNewToProfilAddModifyView().url3Txt.getText());
                            profilModifyDeepCopy.addUrl(url);
                        }
                    }
                    /////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////
                    /* HOBBIES */
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().hobbyTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().hobbyTxt.getText().equals("")) {
                            String hobbiesStr = mainView.getAddNewToProfilAddModifyView().hobbyTxt.getText();
                            String[] arrSplit = hobbiesStr.split(";");
                            for (String str : arrSplit) {
                                hobby = new Hobby(str);
                                profilModifyDeepCopy.addHobby(hobby);
                            }
                        }
                    }
                    /////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////
                    /* INTERESTS */
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().interestTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().interestTxt.getText().equals("")) {
                            String interestStr = mainView.getAddNewToProfilAddModifyView().interestTxt.getText();
                            String[] arrSplit = interestStr.split(";");
                            for (String str : arrSplit) {
                                interest = new Interest(str);
                                profilModifyDeepCopy.addInterest(interest);
                            }
                        }
                    }
                    /////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////
                    /* BIRTHPLACE */
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().birthPlaceTxt)) {
                        String birthplaceStr = mainView.getAddNewToProfilAddModifyView().birthPlaceTxt.getText();
                        if (!birthplaceStr.equals("")) {
                            birthplace = new Birthplace(birthplaceStr);
                            profilModifyDeepCopy.setBirthplace(birthplace);
                        }
                    }
                    /////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////
                    /*  */

                    /* EMAIL */

                } else if (((key.getClass().toString().equals("class javax.swing.JTextArea")))) {
                    if (key.equals(mainView.getAddNewToProfilAddModifyView().noteTxt)) {
                        if (!mainView.getAddNewToProfilAddModifyView().noteTxt.getText().equals("")) {
                            note = new Note(mainView.getAddNewToProfilAddModifyView().noteTxt.getText());
                            profilModifyDeepCopy.addNote(note);
                        }
                    }
                }
            }//else is true

        }

        if (ad1) {
            profilModifyDeepCopy.addAddress(adr);
        }
        if (ad2) {
            profilModifyDeepCopy.addAddress(adr2);
        }
        if (ad3) {
            profilModifyDeepCopy.addAddress(adr3);
        }

        //RECREATE THE FormattedName////////////////////////////////////////////////////////////////////////////////////
        String d = "";
        String tle = "";
        if(!profilModifyDeepCopy.getTitles().isEmpty()) {
            tle = profilModifyDeepCopy.getTitles().get(0).getValue();
            d += tle +" ";
        }

        String givenName = "";
        if(profilModifyDeepCopy.getStructuredName() != null) {
            if(profilModifyDeepCopy.getStructuredName().getGiven() != null) {
                if (!profilModifyDeepCopy.getStructuredName().getGiven().equals("")) {
                    givenName = profilModifyDeepCopy.getStructuredName().getGiven();
                    d += givenName + " ";
                }
            }
        }
        String additionnalName = "";
        if(profilModifyDeepCopy.getStructuredName() != null) {
            if(!profilModifyDeepCopy.getStructuredName().equals("[]")) {
                if (!profilModifyDeepCopy.getStructuredName().getAdditionalNames().toString().equals("[]")) {
                    additionnalName = profilModifyDeepCopy.getStructuredName().getAdditionalNames().get(0);
                    d += additionnalName + " ";
                }
            }
        }
        String family = "";
        if(profilModifyDeepCopy.getStructuredName() != null) {
            if(profilModifyDeepCopy.getStructuredName().getFamily() != null) {
                if (!profilModifyDeepCopy.getStructuredName().getFamily().equals("")) {
                    family = profilModifyDeepCopy.getStructuredName().getFamily();
                    d += family + " ";
                }
            }
        }
        String suffixe = "";
        if(profilModifyDeepCopy.getStructuredName() != null) {
            if(!profilModifyDeepCopy.getStructuredName().equals("[]")) {
                if (!profilModifyDeepCopy.getStructuredName().getSuffixes().toString().equals("[]")) {
                    suffixe = profilModifyDeepCopy.getStructuredName().getSuffixes().get(0);
                    d += suffixe + " ";
                }
            }
        }
        String prefixe = "";
        if(profilModifyDeepCopy.getStructuredName() != null) {
            if(!profilModifyDeepCopy.getStructuredName().equals("[]")) {
                if (!profilModifyDeepCopy.getStructuredName().getPrefixes().toString().equals("[]")) {
                    prefixe = profilModifyDeepCopy.getStructuredName().getPrefixes().get(0);
                    d += prefixe;
                }
            }
        }
        if(!d.equals("")) {
            profilModifyDeepCopy.setFormattedName(d);
        }
        ////////////////////////////////////////////////////////////////////////////////////
        //intermediary
        this.profilModel = profilModifyDeepCopy;

        this.reWriteMyFile();
    }
    /*The function have to rewrite completely a vcf file
        HOW IS IT DOWN ?
    First, we save every profils from a file that is not the one we updated.
    For exemple, i have a file named "myFile.vcf" with 3 profils (A, B and C in it.
    If i update A, i will :
        save B and C
        write them back into the file
        write the new A at the end of the file

    From my mapFileProfils map (<fileName, List<Profil>) if my "SelectedVCard" name correspond to my Key,
    for every Profils in the value (List<Profil>), if it is different from my selectedProfil, i save it.

    If i had only 1 profil into my file then i just write it's updated version
     */
    public void reWriteMyFile() throws Exception {
        //SAVE EVERY VCARD EXCEPT THE ONE WE ARE MAKING CHANGE ATM
        List<VCard> myVcardExceptOne = new ArrayList<>();

        //<fileName, List<Profil>
        for (HashMap.Entry<String, List<VCard>> entry : mainView.getAddModifyView().mapFileProfils.entrySet()) {
            //<Key fileName , Value List<Profil>
            String key = entry.getKey();
            List<VCard> value = entry.getValue();
            //within my map if the key (filename) equals my selectedVCard
            if (key.equals(mainView.getAddModifyView().selectedVcard.getName())) {
                //I loop through all the profil from this file
                for (VCard v : value) {
                    //and save every profil different from the one i updated
                    if (!v.equals(mainView.getAddModifyView().selectedProfil)) {
                        myVcardExceptOne.add(v);
                    } else {
                        //fait rien
                    }
                }
            }
        }

        //EMPTY THE FILE FIRST
        //Get the file of the selectedVcard
        File file = new File(String.valueOf(mainView.getAddModifyView().selectedVcard));
        PrintWriter writer2 = new PrintWriter(file);
        //EMPTY IT
        writer2.print("");
        writer2.close();


        Writer writer = new FileWriter(file, true);
        VCardWriter vcardWriter = null;
        vcardWriter = new VCardWriter(writer, mainView.getModel().returnVcardVersion());
        boolean d = false;
        //Loop through my profil that are not my selectedVcard (0 = 1 profil)
        if(myVcardExceptOne.size()!=0) {
            for (VCard v : myVcardExceptOne) {
                //i write back the profils one by one
                vcardWriter.write(v);
            }
            //then my updated one
            vcardWriter.write(this.profilModel);

            //i have only 1 profil
        }else {
            //i write only my updated profil
            vcardWriter.write(this.profilModel);

        }
        vcardWriter.close();
        writer.close();

        //i instantiate/update my "selectedProfil" with my new updated profil "profilModel <= profilModifyDeepCopy"
        mainView.getAddModifyView().selectedProfil = new VCard(this.profilModel);

        //reset form
        mainView.getModel().resetForm(mainView.getAddNewToProfilAddModifyView().formListElement);

        //delete "X-PRODID:ez-vcard 0.11.2" dupplicated
        stripDuplicatesFromFile(mainView.getAddModifyView().selectedVcard.getPath());
        //update the form accordingly to the new updated selectedVcard
        mainView.getAddNewToProfilAddModifyView().checkInfo();
        this.rebootAddModifyView(1, 2);
    }

    public void updateMapFileProfils(File vcardFile, VCard v, VCard newV) {
        for (Map.Entry<String, List<VCard>> entry : mainView.getAddModifyView2().mapFileProfils.entrySet()) {
            String key = entry.getKey();
            List<VCard> value = entry.getValue();
            //mainView.getAddModifyView2().mapFileProfils.get(vcard.toString());
            if(vcardFile.getName().equals(key)) {
                value.remove(v);
                value.add(newV);
                //mainView.getAddModifyView2().mapFileProfils.put(key, value);
                break;
            }
        }
    }

    public void rewriteMyFileProfilTest2() throws IOException {
        //Get the file of the selectedVcard
        File file = new File(String.valueOf(mainView.getAddModifyView2().selectedVcard));
        PrintWriter writer2 = new PrintWriter(file);
        //EMPTY IT
        writer2.print("");
        writer2.close();

        Writer writer = new FileWriter(file, true);
        VCardWriter vcardWriter = null;
        vcardWriter = new VCardWriter(writer, mainView.getModel().returnVcardVersion());

        for (Map.Entry<String, List<VCard>> entry : mainView.getAddModifyView2().mapFileProfils.entrySet()) {
            String key = entry.getKey();
            List<VCard> value = entry.getValue();

            if(file.getName().equals(key)) {
                for(VCard v : value) {
                    vcardWriter.write(v);
                }
            }
        }

        vcardWriter.close();
        writer.close();

        //delete "X-PRODID:ez-vcard 0.11.2" dupplicated
        stripDuplicatesFromFile(mainView.getAddModifyView2().selectedVcard.getPath());
    }

    public void rewriteMyFileProfil() throws IOException {
        //Récupérer les profils de ma vCard sauf celui qui a été modifier
        //vider le fichier
        //réécrire les profils récupérés
        //réécrire le nouveau profil
        //SAVE EVERY VCARD EXCEPT THE ONE WE ARE MAKING CHANGE ATM
        List<VCard> myVcardExceptOne = new ArrayList<>();

        //<fileName, List<Profil>
        for (HashMap.Entry<String, List<VCard>> entry : mainView.getAddModifyView2().mapFileProfils.entrySet()) {
            //<Key fileName , Value List<Profil>
            String key = entry.getKey();
            List<VCard> value = entry.getValue();
            //within my map if the key (filename) equals my selectedVCard
            if (key.equals(mainView.getAddModifyView2().selectedVcard.getName())) {
                //I loop through all the profil from this file
                for (VCard v : value) {
                    //and save every profil different from the one i updated
                    if (!v.equals(mainView.getModifyProfilView2().copy)) {
                        myVcardExceptOne.add(v);
                    }
                }
            }
        }
        //EMPTY THE FILE FIRST
        //Get the file of the selectedVcard
        File file = new File(String.valueOf(mainView.getAddModifyView2().selectedVcard));
        PrintWriter writer2 = new PrintWriter(file);
        //EMPTY IT
        writer2.print("");
        writer2.close();

        Writer writer = new FileWriter(file, true);
        VCardWriter vcardWriter = null;
        vcardWriter = new VCardWriter(writer, mainView.getModel().returnVcardVersion());
        boolean d = false;
        //Loop through my profil that are not my selectedVcard (0 = 1 profil)
        boolean isEmpty = false;
        if(myVcardExceptOne.size()!=0) {
            for (VCard v : myVcardExceptOne) {
                //i write back the profils one by one
                vcardWriter.write(v);
            }
            //If i have none, it mean i had only 1 profil so the file is empty... Delete it then
        }
        vcardWriter.write(mainView.getAddModifyView2().selectedProfil);

        vcardWriter.close();
        writer.close();

        //delete "X-PRODID:ez-vcard 0.11.2" dupplicated
        stripDuplicatesFromFile(mainView.getAddModifyView2().selectedVcard.getPath());


    }


    public void reWriteMyFileDeleteProfil() throws Exception {
        //SAVE EVERY VCARD EXCEPT THE ONE WE ARE MAKING CHANGE ATM
        List<VCard> myVcardExceptOne = new ArrayList<>();

        //<fileName, List<Profil>
        for (HashMap.Entry<String, List<VCard>> entry : mainView.getAddModifyView2().mapFileProfils.entrySet()) {
            //<Key fileName , Value List<Profil>
            String key = entry.getKey();
            List<VCard> value = entry.getValue();
            //within my map if the key (filename) equals my selectedVCard
            if (key.equals(mainView.getAddModifyView2().selectedVcard.getName())) {
                //I loop through all the profil from this file
                for (VCard v : value) {
                    //and save every profil different from the one i updated
                    if (!v.equals(mainView.getAddModifyView2().selectedProfil)) {
                        myVcardExceptOne.add(v);
                    }
                }
            }
        }

        //EMPTY THE FILE FIRST
        //Get the file of the selectedVcard
        File file = new File(String.valueOf(mainView.getAddModifyView2().selectedVcard));
        PrintWriter writer2 = new PrintWriter(file);
        //EMPTY IT
        writer2.print("");
        writer2.close();

        Writer writer = new FileWriter(file, true);
        VCardWriter vcardWriter = null;
        vcardWriter = new VCardWriter(writer, mainView.getModel().returnVcardVersion());
        boolean d = false;
        //Loop through my profil that are not my selectedVcard (0 = 1 profil)
        boolean isEmpty = false;
        if(myVcardExceptOne.size()!=0) {
            for (VCard v : myVcardExceptOne) {
                //i write back the profils one by one
                vcardWriter.write(v);
            }
            //If i have none, it mean i had only 1 profil so the file is empty... Delete it then
        }else {
            isEmpty = true;
        }
        vcardWriter.close();
        writer.close();

        //reset form
        mainView.getModel().resetForm(mainView.getModifyProfilView2().formListElement);
        //mainView.getModel().rebootList(mainView.getCreateView());

        if(isEmpty) {
            if(file.delete()) {
                //System.out.println("FILE have been DELETED");
            }else {
                //System.out.println("FIle have not been deleted");
            }
        }else {

            //delete "X-PRODID:ez-vcard 0.11.2" dupplicated
            stripDuplicatesFromFile(mainView.getAddModifyView2().selectedVcard.getPath());
        }

    }


    public void rebootList(Object o) throws ClassNotFoundException {
        String className = o.getClass().getName();

        switch(className) {
            case "CreateView":
                mainView.getCreateView().defaultListModel = this.mainView.getModel().setModelFromFilePathFormats("import/", "*.vcf", "*.txt");
                mainView.getCreateView().jlistLeft.setModel(mainView.getCreateView().defaultListModel);
            break;
            case "AddModifyView":
                mainView.getAddModifyView().defaultListModelVcard = this.mainView.getModel().setModelFromFilePathFormats("import/", "*.vcf", "*.txt");
                mainView.getAddModifyView().listVcardList.setModel(mainView.getAddModifyView().defaultListModelVcard);
            break;
            case "AddModifyView2":
                mainView.getAddModifyView2().defaultListModelVcard = this.mainView.getModel().setModelFromFilePathFormats("import/", "*.vcf", "*.txt");
                mainView.getAddModifyView2().listVcardList.setModel(mainView.getAddModifyView2().defaultListModelVcard);
            break;

        }
    }


    //CREATE A DEFAULTLISTMODEL OBJECT FROM A LIST OF FILE IN AN ARRAY ( to construct/reconstruct a JList )
    //PRESENT AT A SPECIFIC PATH UNDER SPECIFIC FORMATS
    protected DefaultListModel setModelFromFilePathFormats(String path, String... formats) {
        File[] f = this.getListOfFiles(path, formats);

        DefaultListModel model = new DefaultListModel();
        model.ensureCapacity(200);

        for (File file : f) {
            model.addElement(file.getName());
        }
        return model;
    }
    //CREATE A DEFAULTLISTMODEL FROM A LIST OF STRING WITHIN THE FUNCTION PARAMETER
    protected DefaultListModel setModelFromList(List<String> list) {

        DefaultListModel model = new DefaultListModel();
        model.ensureCapacity(200);

        for (String s : list) {
            model.addElement(s);
        }
        return model;
    }

    protected DefaultListModel setModelModifyProfilView2EmailList(java.util.List<ezvcard.property.Email> o) {
        //maj of mapEmail

        if(!this.mapEmail.isEmpty()) {
            this.mapEmail.clear();
        }else {
            //System.out.println("  => mapEmail is empy (1st try)");
        }
        DefaultListModel model = new DefaultListModel();
        model.ensureCapacity(200);
        int k = 1;

        for (ezvcard.property.Email e : o) {
            String str = k+") "+e.getValue();
            model.addElement(str);
            this.mapEmail.put(str, e);
            k++;
        }

        return model;
    }
    protected DefaultListModel setModelModifyProfilView2UrlList(java.util.List<ezvcard.property.Url> o) {
        if(!this.mapURL.isEmpty()) {
            this.mapURL.clear();
        }else {
        }

        DefaultListModel model = new DefaultListModel();
        model.ensureCapacity(200);

        int k = 1;
        for (ezvcard.property.Url u : (java.util.List<ezvcard.property.Url>)o) {
            String str = k+") "+u.getValue();
            model.addElement(str);
            mapURL.put(str, u);
            k++;
        }

        return model;
    }

    protected DefaultListModel setModelModifyProfilView2TelList(java.util.List<ezvcard.property.Telephone> o) {
        if(!this.mapTEL.isEmpty()) {
            this.mapTEL.clear();
        }else {
        }
        DefaultListModel model = new DefaultListModel();
        model.ensureCapacity(200);

        int k = 1;
        for (ezvcard.property.Telephone u : (java.util.List<ezvcard.property.Telephone>)o) {
            String str = k+") "+u.getText();
            model.addElement(str);
            mapTEL.put(str, u);
            k++;
        }
        return model;
    }


    protected DefaultListModel setModelModifyAdressesView2List(java.util.List<ezvcard.property.Address> o) {
        if(!this.mapAdresses.isEmpty()) {
            this.mapAdresses.clear();
        }else {
        }



        DefaultListModel model = new DefaultListModel();
        model.ensureCapacity(200);
        int k = 1;
        for (ezvcard.property.Address u : (java.util.List<ezvcard.property.Address>)o) {
            String s = k+") ";
            if (!u.getTypes().toString().equals("[]")) {
                s += u.getTypes().toString().toUpperCase() + "  ";
            }
            if(u.getStreetAddress() != null) {
                s += u.getStreetAddress() + " ";
            }
            if(u.getRegion() != null) {
                s += u.getLocality() + " ";
            }
            if(u.getPostalCode() != null) {
                s += u.getPostalCode() + " ";
            }
            if( u.getRegion() != null) {
                s += u.getRegion() + " ";
            }
            if(u.getCountry() != null) {
                s += u.getCountry() + " ";
            }
            model.addElement(s);
            this.mapAdresses.put(s, u);
            k++;
        }

        return model;
    }

    //Create a hashmap like this <filename, List of vCard profil from this filename> by retrieving every file
    //from a specific PATH with approved formats

    protected HashMap<String, List<VCard>> buildFileProfilsMap(String path, String... formats) throws IOException {
        HashMap<String, List<VCard>> map = new HashMap<>();
        //for each file in the folder
        for (File f : this.getListOfFiles(path, formats)) {
            //make a list of it's different profiles
            List<VCard> vcards = Ezvcard.parse(f).all();
            //name of the file + what's inside
            map.put(f.getName(), vcards);
        }
        return map;
    }


    //Check a format accordingly to the array
    protected boolean checkFormat(String formatInput) {
        if (formatInput.contains(".")) {
            formatInput = formatInput.substring(formatInput.lastIndexOf(".") + 1);
            return listValideFormat.contains(formatInput);
        }
        return false;
    }

    //Valide format file list
    protected void addFormat(String... formats) {
        Collections.addAll(this.listValideFormat, formats);
    }


    //Retrieve the list of file from a path in parameter
    protected File[] getListOfFiles(String path, String... formats) {
        int nbOfFormat = formats.length;
        String[] typeFile = new String[nbOfFormat];
        int incr = 0;
        for (String format : formats) {
            typeFile[incr] = format;
            incr++;
        }
        File dir = new File(path);
        return dir.listFiles((FileFilter) new WildcardFileFilter(typeFile));
    }

    //Resize icon accordingly to the button size
    protected Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    //DELETE FILE FROM A PATH IN PARAM
    protected void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    //Delete a duplicata caused by the Ez-Vcard libraries itself (workaround)
    protected void stripDuplicatesFromFile(String filename) throws IOException {
        //First, retrieve every lines from a file
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        //Then, iterate through the list and write back to the file except the 'deleteLine'
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        String deleteLine = "X-PRODID:ez-vcard 0.11.2";

        for(int k =0; k<lines.size(); k++) {
            //if the element is 'deleteLine' ...
            if(lines.get(k).equals(deleteLine)) {
                //i write only the first one
                writer.write(lines.get(k));
                int d = k + 1;

                /*
                    EXEMPLE:
                    BEGIN:VCARD
                    VERSION:2.1
                    X-PRODID:ez-vcard 0.11.2
                    X-PRODID:ez-vcard 0.11.2
                 */
                    //à partir de la, tant que l'élément suivant est deletLine alors je le supprime
                    while (lines.get(d).equals(deleteLine) && d < lines.size()) {
                        lines.remove(d);
                        if(d+1 < k) {
                            d++;
                        }else {
                            break;
                        }
                    }
            }else {
                writer.write(lines.get(k));
            }
            //vérifier si l'élément suivant l'est aussi
            writer.newLine();
        }
        reader.close();
        writer.close();
    }
    //RESET AddNewToProfilAddModifyView(), AddNewAddModifyView() AND AddNewCreateView() FORMS
    protected final void resetForm(Object[] i) {
        ArrayList<Object[]> myElements = new ArrayList<>();
        myElements.add(i);
        for(Object o : myElements.get(0)) {
            switch (o.getClass().toString()) {
                case "class javax.swing.JComboBox" -> ((JComboBox) o).setSelectedIndex(0);
                case "class org.jdatepicker.impl.JDatePickerImpl" -> ((JDatePickerImpl) o).getJFormattedTextField().setText("");
                case "class javax.swing.JTextField" -> ((JTextField) o).setText("");
                case "class javax.swing.JTextArea" -> ((JTextArea) o).setText("");
            }
        }
    }
    //Check if change have been made by the user in the form section
    //It is useful to know if there is something to save or not
    /*
        haveChanged(1)  =>   AddNewCreateView    formListElement
        haveChanged(2)  =>   AddNewAddModifyView formListElement
     */
    protected boolean haveChanged(int i) {
        ArrayList<Object> everyTextField = new ArrayList<>();
        if(i==1) {
            everyTextField.addAll(Arrays.asList(mainView.getAddNewCreateView().formListElement));
        }if(i==2) {
            everyTextField.addAll(Arrays.asList(mainView.getAddNewAddModifyView().formListElement));
        }

        for (Object o : everyTextField) {
            if (o.getClass().toString().equals("class javax.swing.JComboBox")) {
                if (!((JComboBox) o).getSelectedItem().toString().equals("")) {
                    return true;
                }
            } else if (o.getClass().toString().equals("class org.jdatepicker.impl.JDatePickerImpl")) {
                if (!((JDatePickerImpl) o).getJFormattedTextField().getText().equals("")) {
                    return true;
                }
            } else if (o.getClass().toString().equals("class javax.swing.JTextField")) {
                if (!((JTextField) o).getText().equals("")) {
                    return true;
                }
            } else if (((o.getClass().toString().equals("class javax.swing.JTextArea")))) {
                if (!((JTextArea) o).getText().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

    //Return a VCardVersion Object from the user form input
    protected VCardVersion returnVcardVersion() {
        return switch (mainView.getAddNewCreateView().vcardFormatCombo.getSelectedItem().toString()) {
            case "2_1" -> VCardVersion.V2_1;
            case "3_0" -> VCardVersion.V3_0;
            case "4_0" -> VCardVersion.V4_0;
            default -> null;
        };
    }
    //Return a VCardVersion object directly from a file
    protected VCardVersion returnVcardVersion(File file) throws IOException {
        VCard vcard = Ezvcard.parse(file).first();
        return vcard.getVersion();
    }
    //restart a part of the form ongoing operation if the user press "COME BACK" while he didn't press "Save"
    protected void restartFormLeaveCheck() {
        if (AddNewCreateView.onGoingOperation) {
            mainView.getAddNewCreateView().vcardForm = new VCard();
            AddNewCreateView.onGoingOperation = false;
            mainView.getAddNewCreateView().vcardFormatCombo.setEnabled(true);
        }

    }

    //                                      associated with
    //retrieve from a filename, the vCard profil associated to it that are present in a hashmap
    // <vcCard file name, array of vcCard Profil in it>
    //in a String List format

    protected Map<VCard, String> map;
    static int x = 0;
    int k = 0;
    //Manage mapFileProfils and listInfoProfils<String>
    protected List<String> retrieveProfilsFromFileName(String fileName, Map<String, List<VCard>> mapFileProfils) {
        map = new HashMap<>();
        List<String> list = new ArrayList<>();
        //gather every profils from a file
        List<VCard> content = mapFileProfils.get(fileName);

        for (VCard vCard : content) {
            x++;

            String addInfo = "";
            k++;
            try {
                if (vCard.getStructuredName().getFamily() != null) {
                    addInfo = vCard.getStructuredName().getFamily();
                } else {
                    addInfo = "[FamilyName]";
                }
            } catch (Exception e) {
                addInfo = "[FamilyName]";
            }

            try {
                if (vCard.getStructuredName().getGiven() != null) {
                    addInfo += "-" + vCard.getStructuredName().getGiven();
                } else {
                    addInfo += "-[GivenName]";
                }
            } catch (Exception e) {
                addInfo = "-[GivenName]";
            }

            try {
                if (vCard.getTelephoneNumbers().get(0) != null) {
                    if (vCard.getTelephoneNumbers().get(0).getText().equals("")) {
                        addInfo += " [Phone Number]";
                    } else {
                        addInfo += " " + vCard.getTelephoneNumbers().get(0).getText();
                    }
                }
            } catch (Exception e) {
                addInfo += " [Phone Number]";
            }

            if (!addInfo.equals("[FamilyName]-[GivenName] [Phone Number]")) {
                list.add(x + ") " + addInfo);
                map.put(vCard, x + ") " + addInfo);
                //profil + his info
            } else {
                list.add(x + ") Profil");
                map.put(vCard, x + ") Profil");
            }


        }
        x = 0;
        return list;
    }


    public void rebootAddModifyView(int... i) {
        for (int myI : i) {
            if (myI == 1) {
                try {
                    //HashMap<nomFichier, List<VCard> soit les profils>
                    mainView.getAddModifyView().mapFileProfils = mainView.getModel().buildFileProfilsMap("import/", "*.vcf", "*.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainView.getAddModifyView().defaultListModelVcard = this.mainView.getModel().setModelFromFilePathFormats("import/", "*.vcf", "*.txt");
                mainView.getAddModifyView().listVcardList.setModel(mainView.getAddModifyView().defaultListModelVcard);
            }
            if (myI == 2) {
                try {
                    mainView.getAddModifyView().listInfoProfils = mainView.getModel().retrieveProfilsFromFileName(mainView.getAddModifyView().selectedVcard.getName(), mainView.getAddModifyView().mapFileProfils);
                    mainView.getAddModifyView().defaultListModelVcard = this.mainView.getModel().setModelFromFilePathFormats("import/", "*.vcf", "*.txt");
                    mainView.getAddModifyView().listVcardList.setModel(mainView.getAddModifyView().defaultListModelVcard);
                    DefaultListModel listModel = (DefaultListModel) mainView.getAddModifyView().listProfilList.getModel();
                    listModel.removeAllElements();
                    mainView.getAddModifyView().listProfilList.setModel(listModel);
                } catch (Exception e) {

                }
            }
        }
    }
    public void rebootAddModifyView2(int... i) {
        for (int myI : i) {
            if (myI == 1) {
                try {
                    //HashMap<nomFichier, List<VCard> soit les profils>
                    mainView.getAddModifyView2().mapFileProfils = mainView.getModel().buildFileProfilsMap("import", "*.vcf", "*.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainView.getAddModifyView2().defaultListModelVcard = this.mainView.getModel().setModelFromFilePathFormats("import", "*.vcf", "*.txt");
                mainView.getAddModifyView2().listVcardList.setModel(mainView.getAddModifyView2().defaultListModelVcard);
            }
            if (myI == 2) {
                try {
                    mainView.getAddModifyView2().listInfoProfils = mainView.getModel().retrieveProfilsFromFileName(mainView.getAddModifyView2().selectedVcard.getName(), mainView.getAddModifyView2().mapFileProfils);
                    mainView.getAddModifyView2().defaultListModelVcard = this.mainView.getModel().setModelFromFilePathFormats("import", "*.vcf", "*.txt");
                    mainView.getAddModifyView2().listVcardList.setModel(mainView.getAddModifyView2().defaultListModelVcard);
                    DefaultListModel listModel = (DefaultListModel) mainView.getAddModifyView2().listProfilList.getModel();
                    listModel.removeAllElements();
                    mainView.getAddModifyView2().listProfilList.setModel(listModel);
                } catch (Exception e) {

                }
            }
        }
    }
    //met à jours l'objet "Addresse" qui correspond au profil
    protected void updateProfilAddresse() {
        //TEXTFIELD
        String typeAddress = mainView.getModifyProfilView2AddressesForm().addressTypeComboBox.getSelectedItem().toString();
        String streetAddressForm = mainView.getModifyProfilView2AddressesForm().streetNameTxt.getText();
        String localityForm = mainView.getModifyProfilView2AddressesForm().cityNameTxt.getText();
        String regionForm = mainView.getModifyProfilView2AddressesForm().regionNameTxt.getText();
        String postalCodeForm = mainView.getModifyProfilView2AddressesForm().postalCodeNameTxt.getText();
        String countryForm = mainView.getModifyProfilView2AddressesForm().countryNameTxt.getText();
        boolean a = false;
        boolean b = false;
        boolean c = false;
        boolean d = false;
        boolean e = false;

        if(mainView.getModifyProfilView2AddressesForm().addressForm.getTypes() != null) {
            if(!mainView.getModifyProfilView2AddressesForm().addressForm.toString().equals("[]")) {
                switch(typeAddress) {
                    case "home":
                        mainView.getModifyProfilView2AddressesForm().addressForm.getTypes().set(0, AddressType.HOME);
                    case "work":
                        mainView.getModifyProfilView2AddressesForm().addressForm.getTypes().set(0, AddressType.WORK);
                    case "other":
                        mainView.getModifyProfilView2AddressesForm().addressForm.getTypes().set(0, AddressType.get(typeAddress));
                    default:

                }
            }
        }

        //OBJECT
        if(mainView.getModifyProfilView2AddressesForm().addressForm.getStreetAddress() != null) {
            String streetAddressObject = mainView.getModifyProfilView2AddressesForm().addressForm.getStreetAddress();

            if(streetAddressForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.getStreetAddresses().remove(0);
                a = true;
            }
            if(!streetAddressForm.equals(streetAddressObject) && !streetAddressForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.setStreetAddress(streetAddressForm);
            }

        }
        if(mainView.getModifyProfilView2AddressesForm().addressForm.getLocality() != null) {
            String localityObject = mainView.getModifyProfilView2AddressesForm().addressForm.getLocality();

            if(localityForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.getLocalities().remove(0);
                b = true;
            }
            if(!localityForm.equals(localityObject) && !localityForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.setLocality(localityForm);
            }
        }
        if(mainView.getModifyProfilView2AddressesForm().addressForm.getRegion() != null) {
            String regionObject = mainView.getModifyProfilView2AddressesForm().addressForm.getRegion();

            if(regionForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.getRegions().remove(0);
                c = true;
            }
            if(!regionForm.equals(regionObject) && !regionForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.setRegion(regionForm);
            }
        }
        if(mainView.getModifyProfilView2AddressesForm().addressForm.getPostalCode() != null) {
            String postalCodeObject = mainView.getModifyProfilView2AddressesForm().addressForm.getPostalCode();

            if(postalCodeForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.getPostalCodes().remove(0);
                d = true;
            }
            if(!postalCodeForm.equals(postalCodeObject) && !postalCodeForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.setPostalCode(postalCodeForm);
            }
        }
        if(mainView.getModifyProfilView2AddressesForm().addressForm.getCountry() != null) {
            String countryObject = mainView.getModifyProfilView2AddressesForm().addressForm.getCountry();

            if(countryForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.getCountries().remove(0);
                e = true;
            }
            if(!countryForm.equals(countryObject) && !countryForm.equals("")) {
                mainView.getModifyProfilView2AddressesForm().addressForm.setCountry(countryForm);
            }
        }

        if(a == true && b == true && c == true && d == true && e == true) {
            //delete yes(0) no(1)
            int result = JOptionPane.showOptionDialog(null,
                    "Do you want to delete this address ?",
                    "Delete address",
                    //JOptionPane yes no
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null, null, null);


            if(result == 0) {
                mainView.getModifyProfilView2().vCardProfilFormSelected.getAddresses().remove(mainView.getModifyProfilView2AddressesForm().addressForm);
                mainView.getModifyProfilView2AddressesForm().setVisible(false);
                mainView.getModifyProfilView2().setVisible(true);
            }else  {

            }
        }
        mainView.getModifyProfilView2().updateAddresses();
    }

    protected void checkForm() throws IOException {
        VCard vForm = mainView.getModifyProfilView2().vCardProfilFormSelected;
        mainView.getModifyProfilView2().copy = new VCard(vForm);
        ArrayList<String> arrayList = new ArrayList<>();
        String familyNameForm = mainView.getModifyProfilView2().familyNameTxt.getText();
        String givenNameForm = mainView.getModifyProfilView2().givenNameTxt.getText();
        String prefixesForm = mainView.getModifyProfilView2().prefixesTxt.getText();
        String suffixesForm = mainView.getModifyProfilView2().suffixesTxt.getText();
        String additionalNameForm = mainView.getModifyProfilView2().additionalNameTxt.getText();

        String orgaForm = mainView.getModifyProfilView2().organisationTxt.getText();
        String birthplaceForm = mainView.getModifyProfilView2().birthPlaceTxt.getText();
        String titleForm = mainView.getModifyProfilView2().titleTxt.getText();
        String genderForm = mainView.getModifyProfilView2().genderTypeComboBox.getSelectedItem().toString();
        String noteForm = mainView.getModifyProfilView2().noteTxt.getText();

        String hobbies = mainView.getModifyProfilView2().hobbyTxt.getText();

        if(hobbies.equals("")) {
            vForm.getHobbies().clear();
            /*
            for(int k=0; k< vForm.getHobbies().size(); k++) {
                vForm.getHobbies().remove(k);
            }
             */
        }

        if(!hobbies.equals("")) {
            String[] arrSplit = hobbies.split(";");
            List<Hobby> hobbyList = new ArrayList<>();
            for(int k=0; k< arrSplit.length; k++) {
                if(vForm.getHobbies().get(k).getValue().equals(arrSplit[k])) {
                    Hobby h = new Hobby(arrSplit[k]);
                    hobbyList.add(h);
                }
            }
            vForm.getHobbies().clear();
            for(Hobby h : hobbyList) {
                vForm.getHobbies().add(h);
            }
        }

        String vCardFamilyName = "";
        HashMap<String, String> map = new HashMap<>();


        String vCardGivenName = "";
        if(vForm.getStructuredName() != null) {
            if (vForm.getStructuredName().getGiven() != null) {
                vCardGivenName = vForm.getStructuredName().getGiven();
                map.put("given", vCardGivenName);
            } else {
                map.put("given", "");
            }
        }




        String vCardprefixes = "";
        if(vForm.getStructuredName() != null) {
            if (vForm.getStructuredName().getPrefixes().size() != 0) {
                vCardprefixes = vForm.getStructuredName().getPrefixes().get(0);
                map.put("prefixes", vCardprefixes);

            } else {
                map.put("prefixes", "");
            }
        }

        String vCardsuffixes = "";
        if(vForm.getStructuredName() != null) {
            if (vForm.getStructuredName().getSuffixes().size() != 0) {
                vCardsuffixes = vForm.getStructuredName().getSuffixes().get(0);
                map.put("suffixes", vCardsuffixes);

            } else {
                map.put("suffixes", "");
            }
        }

        if(vForm.getStructuredName() != null) {
            if (vForm.getStructuredName().getFamily() != null) {
                vCardFamilyName = vForm.getStructuredName().getFamily();
                map.put("family", vCardFamilyName);
            } else {
                map.put("family", "");
            }
        }

        String vCardAdditionalName = "";
        if(vForm.getStructuredName() != null) {
            if (vForm.getStructuredName().getAdditionalNames().size() != 0) {
                vCardAdditionalName = vForm.getStructuredName().getAdditionalNames().get(0);
                map.put("additionalname", vCardAdditionalName);

            } else {
                map.put("additionalname", "");
            }
        }

        String vCardNote = "";
        if(vForm.getNotes() != null && vForm.getNotes().size() != 0) {
            vCardNote = vForm.getNotes().get(0).getValue();
        }

        String vCardOrga = mainView.getModifyProfilView2().organisationTxt.getText();
        String vCardBirthplace;
        String vCardTitle;
        String vCardGender;

        if(vCardGivenName == null && vForm.getStructuredName() != null) {

        }else {

            if(!givenNameForm.equals(vCardGivenName) && !givenNameForm.equals("")) {
                if(vForm.getStructuredName() == null) {
                    StructuredName structedName = new StructuredName();
                    structedName.setGiven(givenNameForm);
                    vForm.setStructuredName(structedName);
                }else {
                    vForm.getStructuredName().setGiven(givenNameForm);
                }
                arrayList.add(vForm.getStructuredName().getGiven());
                map.put("given", vForm.getStructuredName().getGiven());

            }else if(givenNameForm.equals("") && vForm.getStructuredName() != null) {
                vForm.getStructuredName().setGiven("");
                if(!map.get("given").equals("")) {
                    map.put("given", "");
                }
            }
        }

        if(vCardAdditionalName == null && vForm.getStructuredName() != null) {

        }else {

            if(!additionalNameForm.equals(vCardAdditionalName) && !additionalNameForm.equals("")) {
                if(vForm.getStructuredName() == null) {
                    StructuredName structedName = new StructuredName();
                    structedName.getAdditionalNames().add(additionalNameForm);
                }else {
                    if(vForm.getStructuredName().getAdditionalNames().size() != 0) {
                        vForm.getStructuredName().getAdditionalNames().remove(vForm.getStructuredName().getAdditionalNames().get(0));
                    }
                    vForm.getStructuredName().getAdditionalNames().add(additionalNameForm);
                }
                arrayList.add(vForm.getStructuredName().getAdditionalNames().get(0));
                map.put("additionalname", vForm.getStructuredName().getAdditionalNames().get(0));

            }else if(vForm.getStructuredName() != null) {
               if(additionalNameForm.equals("") && vForm.getStructuredName().getAdditionalNames().size() != 0) {
                    vForm.getStructuredName().getAdditionalNames().remove(vForm.getStructuredName().getAdditionalNames().get(0));
                    if(!map.get("additionalname").equals("")) {
                        map.put("additionalname", "");
                    }
                }
            }


        }

        if(vCardFamilyName == null && vForm.getStructuredName() != null) {

        }else {

            if(!familyNameForm.equals(vCardFamilyName) && !familyNameForm.equals("")) {

                if(vForm.getStructuredName() == null) {
                    StructuredName structedName = new StructuredName();
                    structedName.setFamily(familyNameForm);
                    vForm.setStructuredName(structedName);
                }else {
                    vForm.getStructuredName().setFamily(familyNameForm);
                }
                arrayList.add(vForm.getStructuredName().getFamily());
                map.put("family", vForm.getStructuredName().getFamily());

            }else if(vForm.getStructuredName() != null) {
                if(familyNameForm.equals("")) {
                    vForm.getStructuredName().setFamily("");

                    if(!map.get("family").equals("")) {
                        map.put("family", "");
                    }
                }
            }

        }

        if(vCardsuffixes == null && vForm.getStructuredName() != null) {

        }else {

            if(!suffixesForm.equals(vCardsuffixes) && !suffixesForm.equals("")) {

                if(vForm.getStructuredName() == null) {
                    StructuredName structedName = new StructuredName();
                    structedName.getSuffixes().add(suffixesForm);
                    vForm.setStructuredName(structedName);
                }else {
                    if(vForm.getStructuredName().getSuffixes().size() != 0) {
                        vForm.getStructuredName().getSuffixes().remove(vForm.getStructuredName().getSuffixes().get(0));
                    }
                    vForm.getStructuredName().getSuffixes().add(suffixesForm);
                }
                arrayList.add(vForm.getStructuredName().getSuffixes().get(0));
                map.put("suffixes", vForm.getStructuredName().getSuffixes().get(0));

            }else if(vForm.getStructuredName() != null) {
                if(suffixesForm.equals("") && vForm.getStructuredName().getSuffixes().size() != 0) {
                    vForm.getStructuredName().getSuffixes().remove(vForm.getStructuredName().getSuffixes().get(0));
                    if(!map.get("suffixes").equals("")) {
                        map.put("suffixes", "");
                    }
                }
            }

        }

        if(vCardprefixes == null && vForm.getStructuredName() != null) {

        }else {

            if(!prefixesForm.equals(vCardprefixes) && !prefixesForm.equals("")) {

                if(vForm.getStructuredName() == null) {
                    StructuredName structedName = new StructuredName();
                    structedName.getSuffixes().add(prefixesForm);
                    vForm.setStructuredName(structedName);
                }else {
                    if(vForm.getStructuredName().getPrefixes().size() != 0) {
                        vForm.getStructuredName().getPrefixes().remove(vForm.getStructuredName().getPrefixes().get(0));
                    }
                    vForm.getStructuredName().getPrefixes().add(prefixesForm);
                }

                arrayList.add(vForm.getStructuredName().getPrefixes().get(0));
                map.put("prefixes", vForm.getStructuredName().getPrefixes().get(0));

            }else if(vForm.getStructuredName() != null) {
                if(prefixesForm.equals("") && vForm.getStructuredName().getPrefixes().size() != 0) {
                    vForm.getStructuredName().getPrefixes().remove(vForm.getStructuredName().getPrefixes().get(0));
                    if(!map.get("prefixes").equals("")) {
                        map.put("prefixes", "");
                    }
                }
            }
        }

        String s = "";
        int k = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            s+= value;
            if(k < map.size()) {
                s+= " ";
            }
            k++;
        }


        FormattedName fn = null;
        fn = new FormattedName(s);
        vForm.setFormattedName(fn);

        if(prefixesForm.equals("") && suffixesForm.equals("") && familyNameForm.equals("") && additionalNameForm.equals("") && givenNameForm.equals("")) {
            vForm.getFormattedNames().remove(vForm.getFormattedName());
            vForm.getStructuredNames().remove(vForm.getStructuredName());
        }


        if(vForm.getOrganizations() == null || vForm.getOrganizations().toString().equals("[]")) {
            //vForm.getOrganizations().remove(0);
            vCardOrga = "";
        }else {
            if(vForm.getOrganizations().size() != 0) {
                if(vForm.getOrganizations().get(0).getValues().size() !=0) {
                    vCardOrga = vForm.getOrganizations().get(0).getValues().get(0);

                    //si la case du formulaire !!devient!! (elle ne l'était pas avant) vide alors je supprime cette élément de la vcard
                    if(orgaForm.equals("")) {
                        vForm.getOrganizations().remove(0);
                    }

                    if (!orgaForm.equals(vCardOrga) && !orgaForm.equals("")) {
                        vForm.getOrganizations().remove(0);
                        Organization org = new Organization();
                        org.getValues().add(orgaForm);
                        vForm.getOrganizations().add(org);
                    }
                }
            }
        }

        if(vForm.getBirthplace() == null) {
            vCardBirthplace = "";
        }else {
            vCardBirthplace = vForm.getBirthplace().getText();
            if(!birthplaceForm.equals(vCardBirthplace)) {
                vForm.getBirthplaces().remove(vForm.getBirthplace());
                Birthplace birthplace = new Birthplace(birthplaceForm);
                vForm.getBirthplaces().add(birthplace);
            }
        }

        if(vForm.getTitles() == null || vForm.getTitles().toString().equals("[]")) {
            vCardTitle = "";
        }else {
            vCardTitle = vForm.getTitles().get(0).getValue();
            if(!titleForm.equals(vCardTitle)) {

                vForm.getTitles().remove(vForm.getTitles().get(0));
                Title title = new Title(titleForm);
                vForm.getTitles().add(title);
            }
        }

        if(vForm.getGender() == null) {
            vCardGender = "";
        }else {
            vCardGender = vForm.getGender().getGender();
            if(!genderForm.equals(vCardGender)) {
                vForm.getGender().setGender(genderForm);
            }
        }
        if(vForm.getNotes() == null || vForm.getNotes().size() == 0) {
            vCardNote = "";
        }else {
            vCardNote = vForm.getNotes().get(0).getValue();

            //si la case du formulaire !!devient!! (elle ne l'était pas avant) vide alors je supprime cette élément de la vcard
            if(noteForm.equals("")) {
                vForm.getNotes().remove(vForm.getNotes().get(0));
            }
            if(!noteForm.equals(vCardGender) && !noteForm.equals("")) {
                vForm.getNotes().remove(vForm.getNotes().get(0));
                Note note = new Note(noteForm);
                vForm.getNotes().add(note);
            }
        }

        if(vForm.getBirthday() == null) {
            String vCardBirthday = "";
        }else {

            Calendar selectedValue = null;
            Date selectedDate = null;
            if((Calendar) mainView.getModifyProfilView2().datePicker.getModel().getValue() != null) {
                selectedValue = (Calendar) mainView.getModifyProfilView2().datePicker.getModel().getValue();
                selectedDate = selectedValue.getTime();
            }

            if(selectedValue != null && selectedDate != null) {
                if (!selectedDate.toString().equals(vForm.getBirthday().getDate().toString()) && !selectedDate.toString().equals("")) {
                    vForm.getBirthday().setDate(selectedDate, false);
                }
            }else if(selectedDate == null && vForm.getBirthday() != null){
                vForm.getBirthdays().remove(vForm.getBirthday());
            }
        }

        mainView.getModel().updateMapFileProfils(mainView.getAddModifyView2().selectedVcard, mainView.getAddModifyView2().selectedProfil, new VCard(vForm));
        mainView.getAddModifyView2().selectedProfil =  new VCard(vForm);
        mainView.getModel().rewriteMyFileProfilTest2();
    }

    protected void resetLists(ArrayList<JList> arr) {
        for(JList list : arr) {
            try {
                DefaultListModel listModel = (DefaultListModel) list.getModel();
                listModel.removeAllElements();
            }catch(Exception e) {
                //
            }
        }
    }

    protected void initiateModifyProfilView2Form() {
        //dans un premier temps, je dois griser les informations qui sont vides
        mainView.getModifyProfilView2().checkInfo();

    }

    protected void test() {
        for (HashMap.Entry<Object, Boolean> entry : mainView.getModifyProfilView2().mapFormBool.entrySet()) {
            Object key = entry.getKey();
            Boolean value = entry.getValue();

            if(value.equals(false)) {

            }
        }
    }

    protected void initiateModifyProfilView2AddressesForm(Address addr) {
        mainView.getModifyProfilView2AddressesForm().checkInfo(addr);
    }




}
