import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public final class PanneauDefinitions extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient Jeu jeu;
    private final DefaultListModel<Mot> modele = new DefaultListModel<>();
    private final JList<Mot> liste;
    private final JLabel detail;
    private transient Mot motSelectionne;
    private boolean synchronisation;

    public PanneauDefinitions(Jeu jeu) {
        this.jeu = jeu;
        setLayout(new BorderLayout(0, 12));
        setBackground(Apparence.SURFACE);
        setOpaque(true);

        JLabel titre = new JLabel("DÉFINITIONS");
        titre.setFont(Apparence.PETIT.deriveFont(Font.BOLD));
        titre.setForeground(Apparence.OR);
        add(titre, BorderLayout.NORTH);

        remplirModele();
        liste = new JList<>(modele);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.setCellRenderer(new MotRenderer());
        liste.setBackground(Apparence.SURFACE);
        liste.setBorder(BorderFactory.createEmptyBorder());
        liste.addListSelectionListener(evenement -> {
            if (evenement.getValueIsAdjusting() || synchronisation) {
                return;
            }
            motSelectionne = liste.getSelectedValue();
            afficherDetail();
        });
        JScrollPane defilement = new JScrollPane(liste);
        defilement.setBackground(Apparence.SURFACE);
        defilement.getViewport().setBackground(Apparence.SURFACE);
        defilement.setBorder(BorderFactory.createLineBorder(Apparence.BORDURE));
        add(defilement, BorderLayout.CENTER);

        detail = new JLabel();
        detail.setFont(Apparence.PETIT);
        detail.setForeground(Apparence.TEXTE_ATTENUE);
        detail.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        detail.setOpaque(true);
        detail.setBackground(Apparence.SURFACE_CLAIRE);
        add(detail, BorderLayout.SOUTH);
        afficherDetail();
    }

    public Mot getMotSelectionne() {
        return motSelectionne;
    }

    public void selectionnerMot(Mot mot) {
        synchronisation = true;
        motSelectionne = mot;
        liste.setSelectedValue(mot, true);
        synchronisation = false;
        afficherDetail();
    }

    public void reinitialiser() {
        synchronisation = true;
        modele.clear();
        remplirModele();
        liste.clearSelection();
        motSelectionne = null;
        synchronisation = false;
        afficherDetail();
    }

    public void actualiser() {
        liste.repaint();
        afficherDetail();
    }

    private void remplirModele() {
        for (Mot mot : jeu.getMots()) {
            modele.addElement(mot);
        }
    }

    private void afficherDetail() {
        if (motSelectionne == null) {
            detail.setText("Sélectionnez une définition, puis glissez sur la grille du début à la fin du mot.");
            return;
        }
        String sens = "horizontal".equals(motSelectionne.direction) ? "horizontale"
            : "vertical".equals(motSelectionne.direction) ? "verticale" : "oblique montante";
        String etat = jeu.estTrouve(motSelectionne) ? "Mot déjà trouvé : " + motSelectionne.mot : "Mot à trouver";
        detail.setText("<html><div style='width:230px;'><b>" + etat + "</b><br>"
            + motSelectionne.definition + "<br><span style='color:" + hex(Apparence.OR)
            + "'>" + sens + " · " + motSelectionne.mot.length() + " lettres</span></div></html>");
    }

    private static String hex(Color couleur) {
        return String.format("#%06X", couleur.getRGB() & 0xFFFFFF);
    }

    private final class MotRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> liste, Object valeur, int index,
                boolean selectionne, boolean focus) {
            super.getListCellRendererComponent(liste, valeur, index, selectionne, focus);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            setBackground(selectionne ? Apparence.OR : Apparence.SURFACE_CLAIRE);
            if (valeur instanceof Mot mot) {
                String sens = "horizontal".equals(mot.direction) ? "H"
                    : "vertical".equals(mot.direction) ? "V" : "O";
                String couleurMeta = selectionne ? hex(Apparence.FOND) : hex(Apparence.OR);
                String couleurTexte = selectionne ? hex(Apparence.FOND)
                    : jeu.estTrouve(mot) ? hex(Apparence.SUCCES) : hex(Apparence.TEXTE);
                String libelle = jeu.estTrouve(mot) ? "Trouvé : " + mot.mot + "<br>" + mot.definition
                    : mot.definition;
                setText("<html><div style='width:230px;'><span style='color:" + couleurMeta
                    + ";font-size:11px;font-weight:bold'>" + String.format("%02d", index + 1)
                    + " · " + sens + " · " + mot.mot.length() + " lettres</span><br><span style='color:"
                    + couleurTexte + ";font-size:13px'>" + libelle + "</span></div></html>");
            }
            return this;
        }
    }
}
