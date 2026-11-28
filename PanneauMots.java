import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public final class PanneauMots extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient Jeu jeu;
    private final DefaultListModel<Mot> modele = new DefaultListModel<>();
    private final JList<Mot> liste;
    private transient Mot motSelectionne;
    private boolean synchronisation;

    public PanneauMots(Jeu jeu) {
        this.jeu = jeu;
        setLayout(new BorderLayout());
        setBackground(Apparence.FOND);

        remplirModele();
        liste = new JList<>(modele);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.setCellRenderer(new MotRenderer());
        liste.setBackground(Apparence.SURFACE);
        liste.setBorder(BorderFactory.createEmptyBorder());
        liste.addListSelectionListener(evenement -> {
            if (!evenement.getValueIsAdjusting() && !synchronisation) {
                motSelectionne = liste.getSelectedValue();
            }
        });

        JScrollPane defilement = new JScrollPane(liste);
        defilement.setBorder(BorderFactory.createLineBorder(Apparence.BORDURE));
        defilement.getViewport().setBackground(Apparence.SURFACE);
        add(defilement, BorderLayout.CENTER);
    }

    public Mot getMotSelectionne() {
        return motSelectionne;
    }

    public void selectionnerMot(Mot mot) {
        synchronisation = true;
        motSelectionne = mot;
        liste.setSelectedValue(mot, true);
        synchronisation = false;
    }

    public void reinitialiser() {
        synchronisation = true;
        modele.clear();
        remplirModele();
        liste.clearSelection();
        motSelectionne = null;
        synchronisation = false;
    }

    public void actualiser() {
        liste.repaint();
    }

    private void remplirModele() {
        for (Mot mot : jeu.getMots()) {
            modele.addElement(mot);
        }
    }

    private final class MotRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> liste, Object valeur, int index,
                boolean selectionne, boolean focus) {
            super.getListCellRendererComponent(liste, valeur, index, selectionne, focus);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            setFont(Apparence.CORPS);
            setBackground(selectionne ? Apparence.ACCENT_LEGER : Apparence.SURFACE);
            boolean trouve = valeur instanceof Mot mot && jeu.estTrouve(mot);
            setForeground(selectionne || trouve ? Apparence.ACCENT : Apparence.TEXTE);
            if (valeur instanceof Mot mot) {
                String marque = jeu.estTrouve(mot) ? "✓ " : "  ";
                setText(String.format("%02d   %s%-8s %d lettres", index + 1, marque,
                    mot.mot, mot.mot.length()));
            }
            return this;
        }
    }
}
