import ezvcard.property.Address;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Controller  implements ActionListener {
    protected MainView mainView;
    protected String actionName;

    public Controller(MainView mainView)  {
        this.mainView = mainView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.actionName = e.getActionCommand();
        /////
        //MAIN VIEW//////////////////
        /////
        if(actionName.equals("IMPORT")) {
            mainView.getModel().doImport();
        }
        if(actionName.equals("CREATE")) {
            mainView.getMainViewJframe().setVisible(false);
            if(!CreateView.alreadyRun) {
                mainView.getCreateView().createAndShowGUI();
            }else {
                mainView.getCreateView().setVisible(true);
                try {
                    mainView.getModel().rebootList(mainView.getCreateView());
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }

        }
        if(actionName.equals("EXPORT")) {
            mainView.getMainViewJframe().setVisible(false);
            if(!ExportView.alreadyRun) {
                mainView.getExportView().createAndShowGUI();
            }else {
                mainView.getExportView().setVisible(true);
            }
        }
        /////
        //CREATE VIEW//////////////////
        /////
        if(actionName.equals("BACKEXPORTVIEW")) {
            //System.out.println("BACKEXPORTVIEW 4");
            mainView.getMainViewJframe().setVisible(true);
            mainView.getExportView().setVisible(false);
            mainView.getExportView().setDefaultCloseOperation(mainView.getExportView().DISPOSE_ON_CLOSE); //making the app to close
        }
        if(actionName.equals("ADDCREATEVIEW")) {
            //System.out.println("ADDCREATEVIEW 5");
            mainView.getCreateView().setVisible(false);
            if(!AddNewCreateView.alreadyRun) {
                mainView.getAddNewCreateView().createAndShowGUI();
            }else {
                try {
                    mainView.getModel().rebootList(mainView.getCreateView());
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
                mainView.getAddNewCreateView().setVisible(true);
            }
        }
        if(actionName.equals("BACKCREATEVIEW")) {
            //System.out.println("BACKCREATEVIEW 6");
            mainView.getMainViewJframe().setVisible(true);
            mainView.getCreateView().setVisible(false);
            mainView.getCreateView().setDefaultCloseOperation(mainView.getCreateView().DISPOSE_ON_CLOSE); //making the app to close
           // mainView.rebootCreateView();
        }
        if(actionName.equals("DELETECREATEVIEW")) {
            //System.out.println("DELETECREATEVIEW 7");
            mainView.getModel().doDeleteCreateView();
        }
        if(actionName.equals("MODIFYCREATEVIEW")) {
            //System.out.println("MODIFYCREATEVIEW 8");
            mainView.getCreateView().setVisible(false);
            if(!ModifyView.alreadyRun) {
                mainView.getModifyView().createAndShowGUI();
            }else {
                mainView.getModifyView().setVisible(true);
            }
        }
        /////
        //ADDNEWCREATE VIEW//////////////////
        /////
        //Build up the vcardList with new vCard
        if(actionName.equals("SAVEANDADDMOREADDNEWVIEW")) {
            //System.out.println("SAVEANDADDMOREADDNEWVIEW 9");
            try {
                mainView.getModel().save(1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        //Write down to a file the vcardListForm, if empty the vcardForm Object
        if(actionName.equals("SAVEADDNEWVIEW")) {
            //System.out.println("SAVEADDNEWVIEW 10");
            try {
                mainView.getModel().save(2);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if(actionName.equals("LEAVEADDNEWVIEW")) {
            //System.out.println("LEAVEADDNEWVIEW 11");
            mainView.getAddNewCreateView().setVisible(false);
            mainView.getCreateView().setVisible(true);
            mainView.getAddNewCreateView().setDefaultCloseOperation(mainView.getAddNewCreateView().DISPOSE_ON_CLOSE); //making the app to close

            try {
                mainView.getModel().rebootList(mainView.getCreateView());
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
            //restart ongoing operation if the user press "COME BACK" before "SAVE"
            mainView.getModel().restartFormLeaveCheck();
            //reset the form compenent list
            mainView.getModel().resetForm(mainView.getAddNewCreateView().formListElement);
        }
        if(actionName.equals("RESETADDNEWVIEW")) {
            //System.out.println("RESETADDNEWVIEW 12");
            mainView.getModel().resetForm(mainView.getAddNewCreateView().formListElement);
        }
        /////
        //MODIFY VIEW//////////////////
        /////
        if(actionName.equals("ADDMODIFYVIEW")) {
            //System.out.println("ADDMODIFYVIEW 13");
            mainView.getModifyView().setVisible(false);
            //createAndShowGUI() already loaded
            if(AddModifyView.alreadyRun) {
                //update the view if i added new file at the CreateView
                mainView.getModel().rebootAddModifyView(1);
                mainView.getAddModifyView().setVisible(true);
            }else {
                mainView.getAddModifyView().createAndShowGUI();
                //update the view if i added new file at the CreateView
                mainView.getModel().rebootAddModifyView(1);
               // mainView.getModel().rebootAddModifyView(1, 2);
                //mainView.getModel().rebootAddModifyView(1);
            }
        }
        if(actionName.equals("DELETEMODIFYVIEW")) {
            //System.out.println("DELETEMODIFYVIEW 14");
            mainView.getModifyView().setVisible(false);
            //createAndShowGUI() already loaded
            if(AddModifyView2.alreadyRun) {
                mainView.getModel().rebootAddModifyView2(1);
                mainView.getAddModifyView2().setVisible(true);
            }else {
                mainView.getAddModifyView2().createAndShowGUI();
                //update the view if i added new file at the CreateView
                mainView.getModel().rebootAddModifyView2(1);
                // mainView.getModel().rebootAddModifyView(1, 2);
                //mainView.getModel().rebootAddModifyView(1);
            }
        }
        if(actionName.equals("BACKMODIFYVIEW")) {
            //System.out.println("BACKMODIFYVIEW 15");
            mainView.getCreateView().jlistLeft.clearSelection();
            mainView.getCreateView().txtAreaRight.setText("");
            mainView.getModifyView().setVisible(false);
            mainView.getCreateView().setVisible(true);
        }
        /////
        //ADD MODIFY VIEW//////////////////
        /////
        //ADD NEW TO VCARD
        if(actionName.equals("ADDVCARDADDMODIFYVIEW")) {
            //System.out.println("ADDVCARDADDMODIFYVIEW 16");
            mainView.getAddModifyView().setVisible(false);
            if(!AddNewAddModifyView.alreadyRun) {
                try {
                    //initiate fileNameSelected = mainView.getAddModifyView().selectedVcard;
                    //this.setCombo() for form access element rule (2.1, 4.0 ...)
                    mainView.getAddNewAddModifyView().createAndShowGUI();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }else {
                mainView.getAddNewAddModifyView().setVisible(true);
                mainView.getAddNewAddModifyView().setSelectedVcard(mainView.getAddModifyView().selectedVcard);
                try {
                    mainView.getAddNewAddModifyView().setCombo();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        }
        if(actionName.equals("ADDPROFILADDMODIFYVIEW")) {
            //System.out.println("ADDPROFILADDMODIFYVIEW 17");
            mainView.getAddModifyView().setVisible(false);

            if(!AddNewToProfilAddModifyView.alreadyRun) {
                try {
                    mainView.getAddNewToProfilAddModifyView().createAndShowGUI();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }else {
                mainView.getAddNewToProfilAddModifyView().setVisible(true);
            }
            try {
                mainView.getAddNewToProfilAddModifyView().checkInfo();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        if(actionName.equals("COMEBACKTOADDMODIFYVIEW")) {
            //System.out.println("COMEBACKTOADDMODIFYVIEW 18");

            mainView.getModel().rebootAddModifyView(2);
            //no more selection in the addModifyView
            mainView.getAddModifyView().listProfilList.clearSelection();
            mainView.getAddModifyView().listVcardList.clearSelection();

            mainView.getCreateView().jlistLeft.clearSelection();
            mainView.getCreateView().txtAreaRight.setText("");

            mainView.getAddModifyView().setVisible(false);
            mainView.getCreateView().setVisible(true);
        }
        /////
        //ADD NEW ADD MODIFY VIEW//////////////////
        /////
        if(actionName.equals("LEAVEADDNEWADDMODIFYVIEW")) {
            //System.out.println("LEAVEADDNEWADDMODIFYVIEW 19");
            mainView.getAddNewAddModifyView().setVisible(false);
            mainView.getAddModifyView().setVisible(true);
            mainView.getModel().rebootAddModifyView(1, 2);
            mainView.getModel().resetForm(mainView.getAddNewAddModifyView().formListElement);
        }
        if(actionName.equals("SAVEADDNEWADDMODIFYVIEW")) {
            //System.out.println("SAVEADDNEWADDMODIFYVIEW 21");
            try {
                mainView.getModel().saveModifyView();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if(actionName.equals("RESETADDNEWADDMODIFYVIEW")) {
            //System.out.println("RESETADDNEWADDMODIFYVIEW 22");
            mainView.getModel().resetForm(mainView.getAddNewAddModifyView().formListElement);
        }
        /////
        //ADD NEW TO PROFIL ADD MODIFY VIEW////////////
        /////
        if(actionName.equals("SAVEADDNEWADDMODIFYVIEW2")) {
            //System.out.println("SAVEADDNEWADDMODIFYVIEW2 23");
            try {
                mainView.getModel().saveModifyView2();
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        }
        if(actionName.equals("LEAVEADDNEWADDMODIFYVIEW2")) {
            //System.out.println("LEAVEADDNEWADDMODIFYVIEW2 24");
            mainView.getAddNewToProfilAddModifyView().setVisible(false);
            mainView.getAddModifyView().setVisible(true);
            mainView.getModel().rebootAddModifyView(1, 2);
            mainView.getModel().resetForm(mainView.getAddNewToProfilAddModifyView().formListElement);
        }
        if(actionName.equals("RESETADDNEWADDMODIFYVIEW2")) {
            //System.out.println("RESETADDNEWADDMODIFYVIEW2 25");
            mainView.getModel().resetForm(mainView.getAddNewToProfilAddModifyView().formListElement);
        }

        ////ADDMODIFYVIEW2
        if(actionName.equals("COMEBACKTOADDMODIFYVIEW2")) {
            //System.out.println("COMEBACKTOADDMODIFYVIEW2 26");

            //no more selection in the addModifyView
            mainView.getAddModifyView2().listProfilList.clearSelection();
            mainView.getAddModifyView2().listVcardList.clearSelection();

            mainView.getModel().rebootAddModifyView2(2);

            mainView.getCreateView().jlistLeft.clearSelection();
            mainView.getCreateView().txtAreaRight.setText("");

            mainView.getAddModifyView2().setVisible(false);
            mainView.getCreateView().setVisible(true);
        }

        if(actionName.equals("MODIFYVCARDADDMODIFYVIEW2")) {
            //System.out.println("MODIFYVCARDADDMODIFYVIEW2 27");
            mainView.getAddModifyView2().setVisible(false);
            if(!ModifyProfilView2.alreadyRun) {
                try {
                    mainView.getModifyProfilView2().createAndShowGUI();//setCombo
                    mainView.getModel().resetForm(mainView.getModifyProfilView2().formListElement);

                    mainView.getModel().initiateModifyProfilView2Form();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }else {
                mainView.getModifyProfilView2().setVisible(true);
                mainView.getModifyProfilView2().setSelectedVcard(mainView.getAddModifyView2().selectedVcard);
                try {
                    mainView.getModifyProfilView2().setCombo();
                    mainView.getModel().resetForm(mainView.getModifyProfilView2().formListElement);
                    mainView.getModel().initiateModifyProfilView2Form();//checkinfo
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        if(actionName.equals("REMOVEPROFILADDMODIFYVIEW2")) {
            //System.out.println("REMOVEPROFILADDMODIFYVIEW2 28");
            mainView.getModifyProfilView2().setVisible(false);
            mainView.getAddModifyView2().setVisible(true);

            //réecriture du fichier
            try {
                mainView.getModel().reWriteMyFileDeleteProfil();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            mainView.getModel().rebootAddModifyView2(1, 2);

            //update list of vcard (file) on the right at the createView view
            try {
                mainView.getModel().rebootList(mainView.getCreateView());
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        }
        //MODIFYPROFILVIEW2
        //SAVEPROFILMODIFYVIEW2 LEAVEPROFILMODIFYVIEW2 RESETPROFILMODIFYVIEW2
        if(actionName.equals("SAVEPROFILMODIFYVIEW2")) {
            //System.out.println("SAVEPROFILMODIFYVIEW2 29");
            try {
                mainView.getModel().checkForm();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            mainView.getCreateView().jlistLeft.clearSelection();
            mainView.getCreateView().txtAreaRight.setText("");
            mainView.getAddModifyView2().listProfilList.clearSelection();
            mainView.getAddModifyView2().listVcardList.clearSelection();
            mainView.getModel().rebootAddModifyView2(1, 2);
            mainView.getModifyProfilView2().setVisible(false);
            mainView.getCreateView().setVisible(true);

        }

        if(actionName.equals("LEAVEPROFILMODIFYVIEW2")) {
            //System.out.println("LEAVEPROFILMODIFYVIEW2 30");
            mainView.getModifyProfilView2().setVisible(false);
            mainView.getAddModifyView2().setVisible(true);

            mainView.getModel().resetLists(mainView.getModifyProfilView2().arrList);
        }

        if(actionName.equals("RESETPROFILMODIFYVIEW2")) {
            //System.out.println("RESETPROFILMODIFYVIEW2 31");
            //mainView.getModel().resetForm(mainView.get .formListElement);
        }

        if(actionName.equals("SAVEMODIFYPROFILVIEW2ADDRESSES")) {
            //System.out.println("SAVEMODIFYPROFILVIEW2ADDRESSES");
            mainView.getModel().updateProfilAddresse();

            /*
            Je dois comparer les données présentes dans les JTextFields et l'objet pour voir s'il y a eu modification ou non.
            En cas de modification, je vais sans doute devoir modifier l'objet en accord avec les modifications.
            Puis mettre à jours la JList ainsi que le hashMap.
             */
        }
        if(actionName.equals("LEAVEMODIFYPROFILVIEW2ADDRESSES")) {
            //System.out.println("LEAVEMODIFYPROFILVIEW2ADDRESSES");
            mainView.getModifyProfilView2AddressesForm().setVisible(false);
            mainView.getModifyProfilView2().setVisible(true);
        }
    }


    public void setAddresses(String JlistName, Address addr) {
        //System.out.println("MODIFYPROFILVIEW2ADDRESSESFORM");
        if(JlistName.equals("modifyProfilView2Addresses")) {
            mainView.getModifyProfilView2().setVisible(false);
            mainView.getModifyProfilView2AddressesForm().setVisible(true);

            if(!mainView.getModifyProfilView2AddressesForm().alreadyRun) {
                try {
                    mainView.getModifyProfilView2AddressesForm().createAndShowGUI();
                    mainView.getModel().initiateModifyProfilView2AddressesForm(addr);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }else {
                mainView.getModifyProfilView2().setVisible(false);
                mainView.getModifyProfilView2AddressesForm().setVisible(true);
                mainView.getModel().initiateModifyProfilView2AddressesForm(addr);
            }
        }
    }
}
