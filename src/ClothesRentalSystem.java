import java.util.Locale;
import javax.swing.UIManager;

import gui.*;

public class ClothesRentalSystem {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        Locale.setDefault(Locale.US);
        GUI gui = new GUI();
        gui.createFrame();
        gui.showLoginPage();
    }
}
