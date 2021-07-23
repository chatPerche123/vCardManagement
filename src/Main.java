import javax.swing.*;
import javax.swing.UIManager.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String args[]) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    MainView mV = new MainView();
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
