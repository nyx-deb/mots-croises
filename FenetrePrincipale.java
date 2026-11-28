import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public final class FenetrePrincipale extends JFrame {

    private static final long serialVersionUID = 1L;

    private final transient Jeu jeu;
    private final PanneauGrille grille;
    private final PanneauDefinitions definitions;
    private final PanneauScore tableauScore;

    public FenetrePrincipale(Jeu jeu) {
        super("Mots mêlés");
        this.jeu = jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configurerCouleurs();

        grille = new PanneauGrille(jeu);
        definitions = new PanneauDefinitions(jeu);
        tableauScore = new PanneauScore(jeu);
        grille.setActionValidation(this::verifierSelection);

        JLabel titre = new JLabel("MOTS MÊLÉS");
        titre.setFont(Apparence.TITRE);
        titre.setForeground(Apparence.TEXTE);

        JPanel entete = new JPanel(new BorderLayout());
        entete.setBackground(Apparence.FOND);
        entete.add(titre, BorderLayout.WEST);
        entete.add(tableauScore, BorderLayout.EAST);

        JPanel colonneDroite = new JPanel(new BorderLayout(0, 12));
        colonneDroite.setBackground(Apparence.FOND);
        JLabel titreDefinitions = new JLabel("DÉFINITIONS");
        titreDefinitions.setFont(Apparence.PETIT);
        titreDefinitions.setForeground(Apparence.ATTENUE);
        colonneDroite.add(titreDefinitions, BorderLayout.NORTH);
        colonneDroite.add(definitions, BorderLayout.CENTER);

        JPanel contenu = new JPanel(new BorderLayout(32, 0));
        contenu.setBackground(Apparence.FOND);
        contenu.add(grille, BorderLayout.WEST);
        contenu.add(colonneDroite, BorderLayout.CENTER);

        JLabel aide = new JLabel("Glissez d'une lettre à l'autre pour sélectionner un mot.");
        aide.setFont(Apparence.PETIT);
        aide.setForeground(Apparence.ATTENUE);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(Apparence.FOND);
        JButton indice = Apparence.bouton("Indice");
        indice.addActionListener(e -> utiliserIndice());
        JButton recommencer = Apparence.bouton("Nouvelle partie");
        recommencer.addActionListener(e -> nouvellePartie());
        actions.add(indice);
        actions.add(recommencer);

        JPanel pied = new JPanel(new BorderLayout());
        pied.setBackground(Apparence.FOND);
        pied.add(aide, BorderLayout.WEST);
        pied.add(actions, BorderLayout.EAST);

        JPanel racine = new JPanel(new BorderLayout(0, 20));
        racine.setBackground(Apparence.FOND);
        racine.setBorder(BorderFactory.createEmptyBorder(28, 32, 24, 32));
        racine.add(entete, BorderLayout.NORTH);
        racine.add(contenu, BorderLayout.CENTER);
        racine.add(pied, BorderLayout.SOUTH);
        setContentPane(racine);

        definitions.selectionnerMot(jeu.prochainMotATrouver());
        pack();
        setLocationRelativeTo(null);
    }

    private void configurerCouleurs() {
        UIManager.put("Panel.background", Apparence.FOND);
        UIManager.put("OptionPane.background", Apparence.SURFACE);
        UIManager.put("OptionPane.messageForeground", Apparence.TEXTE);
        UIManager.put("Button.background", Apparence.SURFACE);
        UIManager.put("Button.foreground", Apparence.TEXTE);
        UIManager.put("List.background", Apparence.SURFACE);
        UIManager.put("List.foreground", Apparence.TEXTE);
        UIManager.put("Viewport.background", Apparence.SURFACE);
    }

    private void verifierSelection(Position debut, Position fin) {
        if (jeu.casesLigne(debut, fin).size() < 2) {
            return;
        }
        Mot trouve = jeu.chercherMot(debut, fin);
        if (trouve != null && jeu.validerMot(trouve)) {
            actualiserAffichage();
            definitions.selectionnerMot(jeu.prochainMotATrouver());
            JOptionPane.showMessageDialog(this, "Trouvé : " + trouve.mot + "\n" + trouve.definition,
                "Mot trouvé", JOptionPane.INFORMATION_MESSAGE);
            verifierFinDePartie();
            return;
        }
        if (trouve != null) {
            JOptionPane.showMessageDialog(this, "Ce mot est déjà trouvé.",
                "Mot trouvé", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        jeu.appliquerMalus();
        actualiserAffichage();
        JOptionPane.showMessageDialog(this, "Aucun mot sur cette ligne. -" + Jeu.PENALITE_ERREUR + " pts",
            "Essayez encore", JOptionPane.WARNING_MESSAGE);
    }

    private void utiliserIndice() {
        Mot mot = definitions.getMotSelectionne();
        if (mot == null) {
            mot = jeu.prochainMotATrouver();
        }
        Position position = jeu.donnerIndice(mot);
        actualiserAffichage();
        if (position == null) {
            JOptionPane.showMessageDialog(this, "Tous les mots sont trouvés.",
                "Indice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        grille.afficherIndice(position);
        JOptionPane.showMessageDialog(this, "Départ : ligne " + (position.getLigne() + 1)
            + ", colonne " + (position.getColonne() + 1) + ". -" + Jeu.POINTS_INDICE + " pts",
            "Indice", JOptionPane.INFORMATION_MESSAGE);
    }

    private void nouvellePartie() {
        jeu.reinitialiser();
        grille.reinitialiser();
        definitions.reinitialiser();
        definitions.selectionnerMot(jeu.prochainMotATrouver());
        actualiserAffichage();
    }

    private void verifierFinDePartie() {
        if (!jeu.estTermine()) {
            return;
        }
        int choix = JOptionPane.showConfirmDialog(this,
            "Terminé. Score : " + jeu.getScore() + " pts.\nRejouer ?",
            "Fin de partie", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (choix == JOptionPane.YES_OPTION) {
            nouvellePartie();
        }
    }

    private void actualiserAffichage() {
        grille.repaint();
        definitions.actualiser();
        tableauScore.actualiser();
    }
}
