import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public final class Apparence {

    public static final Color FOND = new Color(15, 23, 42);
    public static final Color SURFACE = new Color(30, 41, 59);
    public static final Color SURFACE_CLAIRE = new Color(41, 55, 76);
    public static final Color BORDURE = new Color(71, 85, 105);
    public static final Color TEXTE = new Color(226, 232, 240);
    public static final Color TEXTE_ATTENUE = new Color(148, 163, 184);
    public static final Color OR = new Color(227, 179, 65);
    public static final Color SUCCES = new Color(52, 211, 153);
    public static final Color ERREUR = new Color(248, 113, 113);

    public static final Font TITRE = new Font("DejaVu Sans", Font.BOLD, 28);
    public static final Font SOUS_TITRE = new Font("DejaVu Sans", Font.PLAIN, 13);
    public static final Font CORPS = new Font("DejaVu Sans", Font.PLAIN, 14);
    public static final Font PETIT = new Font("DejaVu Sans", Font.PLAIN, 12);
    public static final Font LETTRE = new Font("DejaVu Sans Mono", Font.BOLD, 28);
    public static final Font CHIFFRE = new Font("DejaVu Sans", Font.BOLD, 34);

    private Apparence() {
    }

    public static JButton bouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(CORPS.deriveFont(Font.BOLD));
        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setOpaque(true);
        bouton.setBackground(SURFACE_CLAIRE);
        bouton.setForeground(TEXTE);
        bouton.setBorder(BorderFactory.createEmptyBorder(11, 18, 11, 18));
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evenement) {
                bouton.setBackground(BORDURE);
            }

            @Override
            public void mouseExited(MouseEvent evenement) {
                bouton.setBackground(SURFACE_CLAIRE);
            }
        });
        return bouton;
    }
}
