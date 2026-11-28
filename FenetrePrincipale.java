import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public final class FenetrePrincipale extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String ASTUCE = "Glissez d'une lettre à l'autre pour sélectionner un mot.";

    private final transient Jeu jeu;
    private final PanneauGrille grille;
    private final PanneauMots mots;
    private final PanneauScore tableauScore;
    private final JLabel statut;

    public FenetrePrincipale(Jeu jeu) {
        super("Mots mêlés");
        this.jeu = jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configurerCouleurs();

        grille = new PanneauGrille(jeu);
        mots = new PanneauMots(jeu);
        tableauScore = new PanneauScore(jeu);
        statut = new JLabel(ASTUCE);
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
        JLabel titreMots = new JLabel("MOTS À TROUVER");
        titreMots.setFont(Apparence.PETIT);
        titreMots.setForeground(Apparence.ATTENUE);
        colonneDroite.add(titreMots, BorderLayout.NORTH);
        colonneDroite.add(mots, BorderLayout.CENTER);

        JPanel contenu = new JPanel(new BorderLayout(28, 0));
        contenu.setBackground(Apparence.FOND);
        contenu.add(grille, BorderLayout.WEST);
        contenu.add(colonneDroite, BorderLayout.CENTER);

        statut.setFont(Apparence.PETIT);
        statut.setForeground(Apparence.ATTENUE);

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
        pied.add(statut, BorderLayout.WEST);
        pied.add(actions, BorderLayout.EAST);

        JPanel racine = new JPanel(new BorderLayout(0, 20));
        racine.setBackground(Apparence.FOND);
        racine.setBorder(BorderFactory.createEmptyBorder(26, 28, 22, 28));
        racine.add(entete, BorderLayout.NORTH);
        racine.add(contenu, BorderLayout.CENTER);
        racine.add(pied, BorderLayout.SOUTH);
        setContentPane(racine);

        mots.selectionnerMot(jeu.prochainMotATrouver());
        pack();
        setLocationRelativeTo(null);
    }

    private void configurerCouleurs() {
        UIManager.put("Panel.background", Apparence.FOND);
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
            mots.selectionnerMot(jeu.prochainMotATrouver());
            statut.setText("Trouvé : " + trouve.mot + " · +" + Jeu.POINTS_PAR_MOT + " pts");
            statut.setForeground(Apparence.ACCENT);
            verifierFinDePartie();
            return;
        }
        if (trouve != null) {
            statut.setText("Ce mot est déjà trouvé.");
            statut.setForeground(Apparence.ATTENUE);
            return;
        }
        jeu.appliquerMalus();
        actualiserAffichage();
        statut.setText("Aucun mot sur cette ligne · -" + Jeu.PENALITE_ERREUR + " pts");
        statut.setForeground(Apparence.ATTENUE);
    }

    private void utiliserIndice() {
        Mot mot = mots.getMotSelectionne();
        if (mot == null) {
            mot = jeu.prochainMotATrouver();
        }
        Position position = jeu.donnerIndice(mot);
        actualiserAffichage();
        if (position == null) {
            Messages.information(this, "Indice", "Tous les mots sont déjà trouvés.");
            return;
        }
        grille.afficherIndice(position);
        String texte = "Le mot commence à la ligne " + (position.getLigne() + 1)
            + ", colonne " + (position.getColonne() + 1) + ". -" + Jeu.POINTS_INDICE + " pts";
        statut.setText(texte);
        statut.setForeground(Apparence.ACCENT);
        Messages.information(this, "Indice", texte);
    }

    private void nouvellePartie() {
        jeu.reinitialiser();
        grille.reinitialiser();
        mots.reinitialiser();
        mots.selectionnerMot(jeu.prochainMotATrouver());
        actualiserAffichage();
        statut.setText(ASTUCE);
        statut.setForeground(Apparence.ATTENUE);
    }

    private void verifierFinDePartie() {
        if (!jeu.estTermine()) {
            return;
        }
        boolean rejouer = Messages.confirmer(this, "Fin de partie",
            "Tous les mots sont trouvés.\nScore : " + jeu.getScore() + " pts.", "Rejouer");
        if (rejouer) {
            nouvellePartie();
        }
    }

    private void actualiserAffichage() {
        grille.repaint();
        mots.actualiser();
        tableauScore.actualiser();
    }
}
