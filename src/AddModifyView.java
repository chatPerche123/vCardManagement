import ezvcard.VCard;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.*;
import java.util.List;

public class AddModifyView extends JDialog {
    protected JDialog jdialog;
    protected MainView mainView;

    protected JButton addDataVcard;
    protected JButton addDataProfil;
    protected JButton comeback;

    protected JPanel headerPanel;
    protected JPanel middlePanel;
    protected JPanel southPanel;
    protected JPanel middlePanelListContent;
    protected JPanel middlePanelResult;
    protected JPanel middlePanelListContentLabel;

    protected JScrollPane listVcardScrollpane;
    protected JScrollPane listProfilScrollpane;

    protected JLabel vCardListLabel;
    protected JLabel vCardProfilLabel;

    //LIST OF VCF FILES ON THE LEFT
    protected JList listVcardList;
    //PROFILS ON THE RIGHT FROM THE CLICKED FILE ON THE LEFT
    protected JList listProfilList;

    //Model from both left and right list
    protected DefaultListModel defaultListModelVcard;
    protected DefaultListModel defaultListModelProfil;

    //<filename, filename profil's list>
    protected Map<String, List<VCard>> mapFileProfils;
    //<Profil, infoProfil
    protected Map<VCard , String> mapProfilInfoProfils;
    //List<infoProfil>
    protected List<String> listInfoProfils;

    //Selected VCF File on the left
    protected File selectedVcard;
    //Selected profil from a specific file (selectedVcard) on the right
    protected VCard selectedProfil;
    //check if createAndShowGUI() have already run
    static boolean alreadyRun = false;

    public AddModifyView(MainView mainView) {
        this.jdialog = this;
        this.mainView = mainView;
        this.listVcardList = new JList();
        this.listProfilList = new JList();

        this.listInfoProfils = new ArrayList<>();
        this.mapFileProfils = new HashMap<>();
        this.mapProfilInfoProfils = new HashMap<>();

        this.addDataVcard = new JButton("ADD NEW TO VCARD");
        this.addDataProfil = new JButton("ADD NEW TO A PROFIL'S VCARD");
        this.comeback = new JButton("BACK TO MODIFY VIEW");

        this.headerPanel = new JPanel();
        this.middlePanel = new JPanel(new BorderLayout());
        this.southPanel = new JPanel();

        this.middlePanelListContent = new JPanel(new BorderLayout(70, 5));
        this.middlePanelResult = new JPanel();
        this.middlePanelListContentLabel = new JPanel(new BorderLayout());

        this.middlePanelListContentLabel.setBorder(BorderFactory.createEmptyBorder(10, 30, 0, 80));
        this.middlePanelListContent.setBorder(BorderFactory.createEmptyBorder(0, 30, 5, 80));

        this.listVcardScrollpane = new JScrollPane();
        this.listProfilScrollpane = new JScrollPane();

        this.vCardListLabel = new JLabel("<HTML><U>vCard list</U>: </HTML");
        this.vCardProfilLabel = new JLabel("<HTML><U>vCard's profils list</U>: </HTML");
        this.vCardListLabel.setFont(new Font("Serif", Font.PLAIN, 17));
        this.vCardProfilLabel.setFont(new Font("Serif", Font.PLAIN, 17));
    }

    protected void createAndShowGUI() {
        alreadyRun = true;

        this.middlePanelListContentLabel.setBackground(Color.lightGray);
        this.middlePanelListContent.setBackground(Color.lightGray);

        this.addDataVcard.addActionListener(mainView.getController());
        this.addDataProfil.addActionListener(mainView.getController());
        this.comeback.addActionListener(mainView.getController());

        this.addDataVcard.setEnabled(false);
        this.addDataProfil.setEnabled(false);

        this.addDataVcard.setActionCommand("ADDVCARDADDMODIFYVIEW");
        this.addDataProfil.setActionCommand("ADDPROFILADDMODIFYVIEW");
        this.comeback.setActionCommand("COMEBACKTOADDMODIFYVIEW");

        this.listVcardScrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.listVcardScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.listProfilScrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.listProfilScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        this.addDataVcard.setPreferredSize(new Dimension(250, 50));
        this.addDataProfil.setPreferredSize(new Dimension(250, 50));
        this.comeback.setPreferredSize(new Dimension(200, 50));

        this.vCardListLabel.setPreferredSize(new Dimension(150, 30));
        this.vCardProfilLabel.setPreferredSize(new Dimension(150, 30));

        this.listVcardScrollpane.setPreferredSize(new Dimension(200, 250));
        this.listProfilScrollpane.setPreferredSize(new Dimension(300, 250));

        //LIST OF FILE LIST
        this.listVcardList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getValueIsAdjusting())
                    return;
                //enable the corresponding button
                if(list.getSelectedValue()==null) {
                    addDataVcard.setEnabled(false);
                }else {
                    addDataVcard.setEnabled(true);

                    //retrieve filename from the jlist
                    String filename = list.getSelectedValue().toString();
                    //create a File object from this path+filename
                    selectedVcard = new File("import\\"+filename);

                    //récupère ma liste d'info de profil
                    listInfoProfils = mainView.getModel().retrieveProfilsFromFileName(filename, mapFileProfils);
                    mapProfilInfoProfils = mainView.getModel().map;
                    //convert this List<String> To a DefaultListModel Object
                    defaultListModelProfil = mainView.getModel().setModelFromList(listInfoProfils);
                    //so you can put it in the right Jlist profil list
                    listProfilList.setModel(defaultListModelProfil);
                }
            }
        });
        this.listProfilList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getValueIsAdjusting())
                    return;

                //enable the corresponding button
                if(list.getSelectedValue()==null) {
                    addDataProfil.setEnabled(false);
                }else {
                    addDataProfil.setEnabled(true);
                    //retrieve profil info from the jlist right
                    //1) [FamilyName]-[GivenName] [Phone Number]
                    String infoProfilSelected = list.getSelectedValue().toString();

                    for(HashMap.Entry<VCard, String> entry : mapProfilInfoProfils.entrySet()) {
                        VCard key = entry.getKey() ;//Profil associate to the infoProfil
                        String value = entry.getValue();//infoProfil

                        //correlation between ListInfoProfils and the string from mapProfilInfoProfils
                        if(value.equals(infoProfilSelected)) {
                            selectedProfil = key;
                        }
                    }
                }
            }
        });

        this.headerPanel.add(addDataVcard);
        this.headerPanel.add(addDataProfil);

        this.listVcardScrollpane.setViewportView(listVcardList);
        this.listProfilScrollpane.setViewportView(listProfilList);

        this.middlePanelListContentLabel.add(this.vCardListLabel, BorderLayout.WEST);
        this.middlePanelListContentLabel.add(this.vCardProfilLabel, BorderLayout.EAST);

        this.middlePanelListContent.add(middlePanelListContentLabel, BorderLayout.NORTH);
        this.middlePanelListContent.add(listVcardScrollpane, BorderLayout.WEST);
        this.middlePanelListContent.add(listProfilScrollpane, BorderLayout.EAST);

        this.middlePanel.add(middlePanelListContent, BorderLayout.CENTER);
        this.southPanel.add(comeback);

        add(headerPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        setSize(700, 800); // Absolute size of the window
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
