import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public final class FenetrePrincipale extends JFrame {

    private static final long serialVersionUID = 1L;

    private final transient Jeu jeu;
    private final PanneauGrille grille;
    private final PanneauDefinitions definitions;
    private final PanneauScore tableauScore;

    public FenetrePrincipale(Jeu jeu) {
        super("Mots croisés");
        this.jeu = jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        grille = new PanneauGrille(jeu);
        definitions = new PanneauDefinitions(jeu, grille);
        tableauScore = new PanneauScore(jeu);
        grille.setActionVerification(this::verifierMotSelectionne);
        grille.setActionSelection(() -> definitions.selectionnerMot(grille.getMotSelectionne()));

        Mot premierMot = jeu.getMots().isEmpty() ? null : jeu.getMots().get(0);
        grille.selectionnerMot(premierMot);
        definitions.selectionnerMot(premierMot);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        JButton verifier = new JButton("Vérifier le mot");
        verifier.addActionListener(e -> verifierMotSelectionne());
        JButton verifierGrille = new JButton("Vérifier la grille");
        verifierGrille.addActionListener(e -> verifierLaGrille());
        JButton indice = new JButton("Indice (-" + Jeu.POINTS_INDICE + ")");
        indice.addActionListener(e -> utiliserIndice());
        JButton nouvellePartie = new JButton("Nouvelle partie");
        nouvellePartie.addActionListener(e -> nouvellePartie());
        boutons.add(verifier);
        boutons.add(verifierGrille);
        boutons.add(indice);
        boutons.add(nouvellePartie);

        JPanel droite = new JPanel(new BorderLayout(8, 8));
        droite.add(definitions, BorderLayout.CENTER);
        droite.add(tableauScore, BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new BorderLayout(12, 12));
        add(grille, BorderLayout.CENTER);
        add(droite, BorderLayout.EAST);
        add(boutons, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(820, 520));
        pack();
        setLocationRelativeTo(null);
    }

    private void verifierMotSelectionne() {
        Mot mot = grille.getMotSelectionne();
        if (mot == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez d'abord une définition.",
                "Aucun mot sélectionné", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (jeu.estTrouve(mot)) {
            JOptionPane.showMessageDialog(this, "Ce mot est déjà trouvé.",
                "Mot validé", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (jeu.verifier(mot)) {
            actualiserAffichage();
            JOptionPane.showMessageDialog(this, "Bravo, " + mot.mot + " est correct ! +"
                + Jeu.POINTS_PAR_MOT + " points.", "Bonne réponse", JOptionPane.INFORMATION_MESSAGE);
            verifierFinDePartie();
        } else {
            actualiserAffichage();
            JOptionPane.showMessageDialog(this, "Ce n'est pas la bonne réponse. Vérifiez les lettres.",
                "Réponse incorrecte", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void verifierLaGrille() {
        int ajoutes = jeu.verifierGrille();
        actualiserAffichage();
        if (ajoutes > 0) {
            JOptionPane.showMessageDialog(this, ajoutes + " mot(s) validé(s).",
                "Grille vérifiée", JOptionPane.INFORMATION_MESSAGE);
            verifierFinDePartie();
        } else {
            JOptionPane.showMessageDialog(this, "Aucun nouveau mot complet à valider.",
                "Grille vérifiée", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void utiliserIndice() {
        Mot mot = grille.getMotSelectionne();
        if (mot == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez d'abord une définition.",
                "Aucun mot sélectionné", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Position position = jeu.donnerIndice(mot);
        actualiserAffichage();
        if (position == null) {
            JOptionPane.showMessageDialog(this, "Aucune lettre à révéler pour ce mot.",
                "Indice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Lettre révélée en ligne " + (position.getLigne() + 1)
            + ", colonne " + (position.getColonne() + 1) + " (-" + Jeu.POINTS_INDICE + " points).",
            "Indice", JOptionPane.INFORMATION_MESSAGE);
    }

    private void nouvellePartie() {
        jeu.reinitialiser();
        definitions.reinitialiser();
        Mot premierMot = jeu.getMots().isEmpty() ? null : jeu.getMots().get(0);
        grille.selectionnerMot(premierMot);
        definitions.selectionnerMot(premierMot);
        actualiserAffichage();
    }

    private void verifierFinDePartie() {
        if (!jeu.estTermine()) {
            return;
        }
        int choix = JOptionPane.showConfirmDialog(this,
            "Partie terminée ! Score final : " + jeu.getScore() + " points.\nRejouer ?",
            "Fin de partie", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (choix == JOptionPane.YES_OPTION) {
            nouvellePartie();
        }
    }

    private void actualiserAffichage() {
        grille.actualiser();
        definitions.actualiser();
        tableauScore.actualiser();
    }
}
