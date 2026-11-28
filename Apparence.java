import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public final class Apparence {

    public static final Color FOND = new Color(16, 16, 18);
    public static final Color SURFACE = new Color(26, 26, 29);
    public static final Color TEXTE = new Color(237, 237, 237);
    public static final Color ATTENUE = new Color(153, 153, 153);
    public static final Color BORDURE = new Color(48, 48, 51);
    public static final Color ACCENT = new Color(226, 90, 90);
    public static final Color ACCENT_LEGER = new Color(58, 34, 36);

    public static final Font TITRE = new Font("DejaVu Sans", Font.BOLD, 20);
    public static final Font CORPS = new Font("DejaVu Sans", Font.PLAIN, 14);
    public static final Font PETIT = new Font("DejaVu Sans", Font.PLAIN, 12);
    public static final Font LETTRE = new Font("DejaVu Sans Mono", Font.BOLD, 26);
    public static final Font CHIFFRE = new Font("DejaVu Sans", Font.BOLD, 22);

    private Apparence() {
    }

    public static JButton bouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(PETIT);
        bouton.setForeground(ATTENUE);
        bouton.setBackground(SURFACE);
        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setOpaque(true);
        bouton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evenement) {
                bouton.setForeground(ACCENT);
            }

            @Override
            public void mouseExited(MouseEvent evenement) {
                bouton.setForeground(ATTENUE);
            }
        });
        return bouton;
    }
}
