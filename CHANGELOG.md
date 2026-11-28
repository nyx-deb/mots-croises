# Changelog

## [Non publié] - 2026-11-28

### Ajouts
- `README.md` : présentation, prérequis, commandes de compilation, règles du jeu et structure du projet.
- `.gitignore` : exclusion des fichiers compilés, des répertoires de build et des fichiers d'IDE.
- `Jeu.java` : initialisation de la partie, saisie des réponses, vérification des mots et de la grille, indices, score, progression, fin de partie et réinitialisation.
- `FenetrePrincipale.java` : fenêtre Swing reliant la grille, les définitions, le score et les actions de jeu.
- `PanneauGrille.java` : grille 5x5 éditable, sélection des cases, navigation au clavier et surlignage des mots.
- `PanneauDefinitions.java` : liste des définitions avec sélection d'un mot et affichage de ses détails.
- `PanneauScore.java` : score, mots trouvés, progression et nombre d'indices utilisés.

### Modifications
- `Main.java` : lancement de l'interface Swing au lieu de l'affichage de démonstration de la grille.
- `PanneauGrille.java` et `Jeu.java` : clics répétés sur une case de croisement pour choisir le mot horizontal, vertical ou oblique voulu.
