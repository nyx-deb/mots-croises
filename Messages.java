import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public final class Messages {

    private Messages() {
    }

    public static void information(Component parent, String titre, String message) {
        afficher(parent, titre, message, null);
    }

    public static boolean confirmer(Component parent, String titre, String message, String bouton) {
        return Boolean.TRUE.equals(afficher(parent, titre, message, bouton));
    }

    private static Boolean afficher(Component parent, String titre, String message, String boutonConfirmation) {
        Window fenetre = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialogue = new JDialog(fenetre, titre, JDialog.ModalityType.APPLICATION_MODAL);
        boolean[] reponse = {false};

        JPanel panneau = new JPanel(new BorderLayout(0, 20));
        panneau.setBackground(Apparence.SURFACE);
        panneau.setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

        JLabel titreLabel = new JLabel(titre);
        titreLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 15));
        titreLabel.setForeground(Apparence.ACCENT);

        String texte = message.replace("&", "&amp;").replace("<", "&lt;")
            .replace(">", "&gt;").replace("\n", "<br>");
        JLabel messageLabel = new JLabel("<html><div style='width:250px;color:#EDEDED;font-size:13px;'>"
            + texte + "</div></html>");

        JPanel contenu = new JPanel(new BorderLayout(0, 10));
        contenu.setBackground(Apparence.SURFACE);
        contenu.add(titreLabel, BorderLayout.NORTH);
        contenu.add(messageLabel, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(Apparence.SURFACE);
        boolean confirmation = boutonConfirmation != null;
        if (confirmation) {
            JButton annuler = Apparence.bouton("Annuler");
            annuler.addActionListener(e -> {
                reponse[0] = false;
                dialogue.dispose();
            });
            actions.add(annuler);
        }
        JButton principal = Apparence.bouton(confirmation ? boutonConfirmation : "OK");
        principal.setForeground(Apparence.ACCENT);
        principal.addActionListener(e -> {
            reponse[0] = true;
            dialogue.dispose();
        });
        actions.add(principal);

        panneau.add(contenu, BorderLayout.CENTER);
        panneau.add(actions, BorderLayout.SOUTH);
        panneau.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("ESCAPE"), "fermer");
        panneau.getActionMap().put("fermer", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evenement) {
                dialogue.dispose();
            }
        });

        dialogue.setContentPane(panneau);
        dialogue.getRootPane().setDefaultButton(principal);
        dialogue.pack();
        dialogue.setResizable(false);
        dialogue.setLocationRelativeTo(parent);
        dialogue.setVisible(true);
        return reponse[0];
    }
}
