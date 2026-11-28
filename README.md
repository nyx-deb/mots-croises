# Mots croisés

Jeu de mots croisés en Java Swing : une grille 5x5, des définitions à consulter, des réponses à saisir et un score calculé en temps réel.

## Présentation

Le projet est divisé en deux parties :

- **Grille et modèle** : `Position.java`, `Mot.java`, `Terrain.java`. Ils gèrent les coordonnées, les mots, le placement dans la grille et le contrôle des croisements.
- **Jeu et interface** : `Jeu.java`, `Main.java`, `FenetrePrincipale.java`, `PanneauGrille.java`, `PanneauDefinitions.java`, `PanneauScore.java`. Ils gèrent la partie, les réponses, les indices, le score et l'affichage Swing.

La logique métier reste dans le modèle ; les classes Swing ne font que afficher et transmettre les actions de l'utilisateur.

## Prérequis

- JDK 21 ou supérieur (Swing est inclus dans le JDK).
- Aucun gestionnaire de dépendances ni serveur d'application n'est nécessaire.

## Compilation et lancement

Depuis la racine du dépôt :

```bash
mkdir -p build
javac -d build *.java
java -cp build Main
```

Vous pouvez aussi ouvrir le projet dans un IDE Java et lancer la classe `Main`, qui contient le point d'entrée `main`.

## Règles du jeu

- La grille contient des mots à trouver dans trois directions : horizontale, verticale et oblique montante.
- Les cases non occupées par un mot du dictionnaire reçoivent une lettre aléatoire, ce qui complète la grille.
- Sélectionnez une définition dans la liste (ou cliquez sur une case de la grille) pour choisir un mot à remplir. Sur un croisement, cliquez plusieurs fois sur la même case pour passer d'un mot à l'autre.
- Saisissez une lettre par case. La sélection avance automatiquement le long du mot choisi.
- « Vérifier le mot » valide uniquement le mot sélectionné, tandis que « Vérifier la grille » valide tous les mots déjà complets.
- Un indice révèle une lettre manquante du mot sélectionné.
- Une réponse correcte rapporte 10 points, un indice coûte 5 points et une mauvaise réponse coûte 2 points. Le score ne peut pas devenir négatif.
- La partie se termine lorsque tous les mots ont été trouvés ; une fenêtre propose alors de rejouer.

## Commandes utiles

| Action | Méthode |
| --- | --- |
| Choisir un mot | Clic sur une définition ou sur une case de la grille |
| Changer de mot sur un croisement | Clics répétés sur la même case |
| Saisir une lettre | Touche lettre au clavier |
| Effacer une case | Retour arrière |
| Vérifier le mot sélectionné | Touche Entrée ou bouton « Vérifier le mot » |
| Obtenir un indice | Bouton « Indice (-5) » |
| Recommencer | Bouton « Nouvelle partie » |

## Structure des fichiers

| Fichier | Rôle |
| --- | --- |
| `Main.java` | Point d'entrée, lance la fenêtre Swing. |
| `Jeu.java` | État de la partie, saisie, vérification, indices, score et progression. |
| `Terrain.java` | Grille 5x5, placement des mots et contrôle des limites et croisements. |
| `Mot.java` | Mot, définition, direction et position de départ. |
| `Position.java` | Coordonnées ligne/colonne et accesseurs. |
| `FenetrePrincipale.java` | Fenêtre principale, disposition et boutons d'action. |
| `PanneauGrille.java` | Grille interactive et sélection des cases. |
| `PanneauDefinitions.java` | Liste des définitions et détails du mot choisi. |
| `PanneauScore.java` | Score, progression et nombre d'indices. |
| `CHANGELOG.md` | Historique des changements. |

## Contribution

- Ne pas pousser directement sur `master` : créer une branche, puis ouvrir une Pull Request pour revue.
- Les fichiers du modèle (`Position.java`, `Mot.java`, `Terrain.java`) et ceux du jeu/interface sont attribués à deux personnes ; éviter de modifier la partie de l'autre sans concertation.
- Les fichiers `.class` et les répertoires de build sont ignorés via `.gitignore`.

Dépôt : <https://github.com/nyx-deb/mots-croises>
