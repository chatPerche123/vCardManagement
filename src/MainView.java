import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class MainView  extends JFrame {
    private final JButton imp;JButton create;JButton export;
    private final Model model;
    private final Controller controller;
    private final CreateView createView;
    private final ExportView exportView;
    private final ModifyView modifyView;
    private final AddNewCreateView addNewCreateView;
    //
    private final AddModifyView addModifyView;
    private final AddNewAddModifyView addNewAddModifyView;
    private final AddNewToProfilAddModifyView addNewToProfilAddModifyView;
    //
    private final AddModifyView2 addModifyView2;
    private final ModifyProfilView2 modifyProfilView2;
    //
    private final ModifyProfilView2AddressesForm modifyProfilView2AddressesForm;

    public MainView() throws IOException, URISyntaxException {
        // Create table model
        this.model = new Model(this);
        // Create Controller
        this.controller = new Controller(this);
        // Create Views
        this.createView = new CreateView(this);
        this.exportView = new ExportView(this);
        this.modifyView = new ModifyView(this);
        this.addNewCreateView = new AddNewCreateView(this);
        //
        this.addModifyView = new AddModifyView(this);
        this.addNewAddModifyView = new AddNewAddModifyView(this);
        this.addNewToProfilAddModifyView = new AddNewToProfilAddModifyView(this);
        //
        this.addModifyView2 = new AddModifyView2(this);
        this.modifyProfilView2 = new ModifyProfilView2(this);
        //
        this.modifyProfilView2AddressesForm = new ModifyProfilView2AddressesForm(this);
        //
        this.imp = new JButton("IMPORT");this.imp.setActionCommand("IMPORT");
        this.create = new JButton("CREATE");this.create.setActionCommand("CREATE");
        this.export = new JButton("EXPORT");this.export.setActionCommand("EXPORT");

        this.createAndShowGUI();
    }
    protected void createAndShowGUI() {
        add(imp);add(create);add(export);

        //controller implement 'ActionListener' + actionPerformed
        imp.addActionListener(controller);
        create.addActionListener(controller);
        export.addActionListener(controller);

        imp.setBounds(130,50,100, 20);
        imp.setSize(180, 150);
        imp.setFont(new Font("Courier", Font.BOLD,30));

        create.setBounds(130,200,100, 20);
        create.setSize(180, 150);
        create.setFont(new Font("Courier", Font.BOLD,30));

        export.setBounds(130,350,100, 20);
        export.setSize(180, 150);
        export.setFont(new Font("Courier", Font.BOLD,30));

        setTitle("Contact APP");
        setSize(440, 600); // Absolute size of the window
        setLayout(null);// no layout managers
        setVisible(true);// frame visible
        setResizable(false); //no resizable
        setLocationRelativeTo(null);// Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // App to close
    }
    //GETTERS
    protected Model getModel() {
        return this.model;
    }
    protected Controller getController() {
        return this.controller;
    }
    protected CreateView getCreateView() {
        return this.createView;
    }
    protected ExportView getExportView() {
        return this.exportView;
    }
    protected ModifyView getModifyView() {
        return this.modifyView;
    }
    protected AddNewCreateView getAddNewCreateView() {
        return this.addNewCreateView;
    }
    //
    protected AddModifyView getAddModifyView() {
        return this.addModifyView;
    }
    protected AddNewAddModifyView getAddNewAddModifyView() {
        return this.addNewAddModifyView;
    }
    protected AddNewToProfilAddModifyView getAddNewToProfilAddModifyView() {
        return this.addNewToProfilAddModifyView;
    }
    //
    protected AddModifyView2 getAddModifyView2() {
        return this.addModifyView2;
    }
    protected ModifyProfilView2 getModifyProfilView2() { return this.modifyProfilView2; }
    protected JFrame getMainViewJframe() {
        return this;
    }
    //
    protected ModifyProfilView2AddressesForm getModifyProfilView2AddressesForm() {
        return this.modifyProfilView2AddressesForm;
    }
}