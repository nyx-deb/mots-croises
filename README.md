# Mots mêlés

Jeu de mots mêlés en Java Swing : une grille 5x5 remplie de lettres, des définitions à consulter et des mots à retrouver en glissant d'une lettre à l'autre.

## Présentation

Le projet garde la séparation en deux parties définie dans le document de répartition :

- **Grille et modèle** : `Position.java`, `Mot.java`, `Terrain.java`. Ils gèrent les coordonnées, les mots, leur placement et le contrôle des croisements.
- **Jeu et interface** : `Jeu.java`, `Main.java`, `FenetrePrincipale.java`, `PanneauGrille.java`, `PanneauDefinitions.java`, `PanneauScore.java`, `Apparence.java`. Ils gèrent la partie, la recherche des mots, les indices, le score et l'affichage Swing.

La logique métier reste dans le modèle ; les classes Swing ne font qu'afficher la grille et transmettre les actions du joueur.

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

- La grille affiche toutes les lettres : celles des mots cachés et des lettres aléatoires qui servent à égarer le joueur.
- Cinq mots sont dissimulés dans la grille, en horizontal, en vertical ou en oblique montant.
- Pour valider un mot, glissez le curseur depuis sa première lettre jusqu'à sa dernière lettre. Les mots sont acceptés dans les deux sens de lecture.
- Les définitions sont listées à droite. Un mot trouvé passe au vert dans la liste et dans la grille, et la sélection passe automatiquement au mot suivant non trouvé.
- Un mot correct rapporte **10 points**. Une sélection qui ne correspond à aucun mot coûte **2 points**. Le score ne peut pas devenir négatif.
- Le bouton **Indice (-5)** révèle, avec un contour doré, la case de départ du mot sélectionné.
- La partie se termine lorsque tous les mots ont été trouvés ; une fenêtre propose alors de rejouer.

## Commandes utiles

| Action | Méthode |
| --- | --- |
| Choisir la définition ciblée | Clic sur une définition |
| Sélectionner un mot | Glisser d'une lettre à l'autre dans la grille |
| Obtenir un indice | Bouton « Indice (-5) » |
| Consulter les règles | Bouton « Règles » |
| Recommencer | Bouton « Nouvelle partie » |

## Interface

L'interface utilise un thème sombre : fond ardoise, surfaces légèrement plus claires, accent doré et vert pour les mots trouvés. La typographie `DejaVu Sans` (et `DejaVu Sans Mono` pour les lettres de la grille) garde une hiérarchie lisible, avec des contrastes conformes à WCAG AA. Chaque état est visible : survol des cases, sélection en cours, mot trouvé, indice en pointillés et boutons au survol.

## Structure des fichiers

| Fichier | Rôle |
| --- | --- |
| `Main.java` | Point d'entrée, lance la fenêtre Swing. |
| `Jeu.java` | État de la partie, recherche des mots, validation, indices, score, progression et fin de partie. |
| `Terrain.java` | Grille 5x5, placement des mots et contrôle des limites et croisements. |
| `Mot.java` | Mot, définition, direction et position de départ. |
| `Position.java` | Coordonnées ligne/colonne et accesseurs. |
| `FenetrePrincipale.java` | Fenêtre principale, disposition et boutons d'action. |
| `PanneauGrille.java` | Grille interactive, peinture des cases et sélection par glisser-déposer. |
| `PanneauDefinitions.java` | Liste des définitions et détails du mot choisi. |
| `PanneauScore.java` | Score, progression et nombre d'indices. |
| `Apparence.java` | Palette, typographie et boutons communs à l'interface. |
| `CHANGELOG.md` | Historique des changements. |

## Contribution

- Ne pas pousser directement sur `master` : créer une branche, puis ouvrir une Pull Request pour revue.
- Éviter de modifier la partie de l'autre sans concertation.
- Les fichiers `.class` et les répertoires de build sont ignorés via `.gitignore`.

Dépôt : <https://github.com/nyx-deb/mots-croises>
