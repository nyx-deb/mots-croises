import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

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

        JPanel entete = new JPanel(new BorderLayout(0, 6));
        entete.setOpaque(false);
        JLabel titre = new JLabel("MOTS MÊLÉS");
        titre.setFont(Apparence.TITRE);
        titre.setForeground(Apparence.OR);
        JLabel sousTitre = new JLabel("Trouvez les " + jeu.getMots().size()
            + " mots cachés dans la grille en vous aidant des définitions.");
        sousTitre.setFont(Apparence.SOUS_TITRE);
        sousTitre.setForeground(Apparence.TEXTE_ATTENUE);
        entete.add(titre, BorderLayout.NORTH);
        entete.add(sousTitre, BorderLayout.SOUTH);

        JPanel carteGrille = carte();
        carteGrille.setLayout(new BorderLayout(0, 12));
        JLabel titreGrille = new JLabel("GRILLE · glissez du premier au dernier lettre");
        titreGrille.setFont(Apparence.PETIT.deriveFont(Font.BOLD));
        titreGrille.setForeground(Apparence.OR);
        carteGrille.add(titreGrille, BorderLayout.NORTH);
        carteGrille.add(grille, BorderLayout.CENTER);

        JPanel carteDefinitions = carte();
        carteDefinitions.setLayout(new BorderLayout());
        carteDefinitions.add(definitions, BorderLayout.CENTER);

        JPanel carteScore = carte();
        carteScore.setLayout(new BorderLayout());
        carteScore.add(tableauScore, BorderLayout.CENTER);

        JPanel colonneDroite = new JPanel(new BorderLayout(0, 18));
        colonneDroite.setOpaque(false);
        colonneDroite.add(carteDefinitions, BorderLayout.CENTER);
        colonneDroite.add(carteScore, BorderLayout.SOUTH);

        JPanel contenu = new JPanel(new BorderLayout(20, 0));
        contenu.setOpaque(false);
        contenu.add(carteGrille, BorderLayout.CENTER);
        contenu.add(colonneDroite, BorderLayout.EAST);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        boutons.setOpaque(false);
        JButton regles = Apparence.bouton("Règles");
        regles.addActionListener(e -> afficherRegles());
        JButton indice = Apparence.bouton("Indice (-" + Jeu.POINTS_INDICE + ")");
        indice.addActionListener(e -> utiliserIndice());
        JButton nouvellePartie = Apparence.bouton("Nouvelle partie");
        nouvellePartie.addActionListener(e -> nouvellePartie());
        boutons.add(regles);
        boutons.add(indice);
        boutons.add(nouvellePartie);

        JPanel racine = new JPanel(new BorderLayout(0, 20));
        racine.setBackground(Apparence.FOND);
        racine.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        racine.add(entete, BorderLayout.NORTH);
        racine.add(contenu, BorderLayout.CENTER);
        racine.add(boutons, BorderLayout.SOUTH);
        setContentPane(racine);

        definitions.selectionnerMot(jeu.prochainMotATrouver());
        pack();
        setLocationRelativeTo(null);
    }

    private void configurerCouleurs() {
        UIManager.put("Panel.background", Apparence.FOND);
        UIManager.put("OptionPane.background", Apparence.SURFACE);
        UIManager.put("OptionPane.messageForeground", Apparence.TEXTE);
        UIManager.put("Button.background", Apparence.SURFACE_CLAIRE);
        UIManager.put("Button.foreground", Apparence.TEXTE);
        UIManager.put("List.background", Apparence.SURFACE);
        UIManager.put("List.foreground", Apparence.TEXTE);
        UIManager.put("Viewport.background", Apparence.SURFACE);
    }

    private JPanel carte() {
        JPanel panneau = new JPanel();
        panneau.setBackground(Apparence.SURFACE);
        panneau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Apparence.BORDURE),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        return panneau;
    }

    private void verifierSelection(Position debut, Position fin) {
        if (jeu.casesLigne(debut, fin).size() < 2) {
            return;
        }
        Mot trouve = jeu.chercherMot(debut, fin);
        if (trouve != null && jeu.validerMot(trouve)) {
            actualiserAffichage();
            definitions.selectionnerMot(jeu.prochainMotATrouver());
            JOptionPane.showMessageDialog(this, "Trouvé : " + trouve.mot + "\n\n" + trouve.definition,
                "Bravo !", JOptionPane.INFORMATION_MESSAGE);
            verifierFinDePartie();
            return;
        }
        if (trouve != null) {
            JOptionPane.showMessageDialog(this, "Ce mot est déjà trouvé.",
                "Mot validé", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        jeu.appliquerMalus();
        actualiserAffichage();
        JOptionPane.showMessageDialog(this, "Aucun mot ne correspond à cette ligne.",
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
            JOptionPane.showMessageDialog(this, "Tous les mots ont déjà été trouvés.",
                "Indice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        grille.afficherIndice(position);
        JOptionPane.showMessageDialog(this, "Le mot commence à la ligne " + (position.getLigne() + 1)
            + ", colonne " + (position.getColonne() + 1) + " (-" + Jeu.POINTS_INDICE + " points).",
            "Indice", JOptionPane.INFORMATION_MESSAGE);
    }

    private void nouvellePartie() {
        jeu.reinitialiser();
        grille.reinitialiser();
        definitions.reinitialiser();
        definitions.selectionnerMot(jeu.prochainMotATrouver());
        actualiserAffichage();
    }

    private void afficherRegles() {
        String regles = "<html><div style='width:360px;color:#E2E8F0;font-size:13px;'>"
            + "<b>Comment jouer ?</b><br><br>"
            + "1. Lisez une définition dans la colonne de droite.<br>"
            + "2. Dans la grille, glissez du premier au dernier lettre du mot.<br>"
            + "3. Les mots peuvent être horizontaux, verticaux ou obliques, et lus dans les deux sens.<br><br>"
            + "Un mot trouvé rapporte " + Jeu.POINTS_PAR_MOT + " points. Un indice en coûte "
            + Jeu.POINTS_INDICE + ", une erreur " + Jeu.PENALITE_ERREUR + ".<br><br>"
            + "Les lettres qui ne forment pas un mot sont là pour vous égarer.</div></html>";
        JOptionPane.showMessageDialog(this, regles, "Règles du jeu", JOptionPane.INFORMATION_MESSAGE);
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
        grille.repaint();
        definitions.actualiser();
        tableauScore.actualiser();
    }
}
