import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FenetrePrincipale(new Jeu()).setVisible(true));
    }
}
