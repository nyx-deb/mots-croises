```text
M   M  OOO  TTTTT  SSSS       M   M EEEEE L     EEEEE  SSSS
MM MM O   O   T   S           MM MM E     L     E     S
M M M O   O   T    SSS        M M M EEEE  L     EEEE   SSS
M   M O   O   T       S       M   M E     L     E         S
M   M  OOO    T   SSSS        M   M EEEEE LLLLL EEEEE SSSS
```

![Java](https://img.shields.io/badge/Java-21-e25a5a?style=flat-square)
![Interface](https://img.shields.io/badge/UI-Swing-303033?style=flat-square)
![Dépendances](https://img.shields.io/badge/dependencies-none-303033?style=flat-square)

Jeu de mots mêlés en Java Swing. Une grille 5x5 remplie de lettres, cinq définitions, et des mots à retrouver en glissant d'une lettre à l'autre.

## Sommaire

- [Lancer le jeu](#lancer-le-jeu)
- [Jouer](#jouer)
- [Structure](#structure)
- [Contribution](#contribution)

## Lancer le jeu

JDK 21 ou supérieur, sans dépendance externe.

```bash
mkdir -p build
javac -d build *.java
java -cp build Main
```

<details>
<summary>Voir avec un IDE</summary>

Ouvrir le dossier dans un IDE Java, puis lancer la classe `Main` qui contient le point d'entrée `main`.

</details>

## Jouer

Glissez sur la grille du premier au dernier lettre d'un mot. Les mots se lisent en horizontal, en vertical ou en oblique, dans les deux sens. Les lettres qui ne composent pas un mot sont là pour égarer.

Un mot trouvé rapporte 10 points, une ligne sans mot en coûte 2, et un indice en coûte 5 en révélant la case de départ du mot sélectionné.

<details>
<summary>Commandes et interface</summary>

| Action | Méthode |
| --- | --- |
| Choisir une définition | Clic dans la liste de droite |
| Sélectionner un mot | Glisser d'une lettre à l'autre |
| Indice | Bouton « Indice », -5 points |
| Recommencer | Bouton « Nouvelle partie » |

L'interface est sombre, avec un seul accent rouge : sélection, lettres trouvées et score. Les mots validés passent dans la même teinte dans la grille et dans la liste.

</details>

<details>
<summary>À propos du modèle</summary>

`Position`, `Mot` et `Terrain` construisent la grille et placent les mots. `Jeu` recherche les mots sélectionnés, tient le score et la progression. Les classes Swing se limitent à l'affichage et aux événements.

</details>

## Structure

| Fichier | Rôle |
| --- | --- |
| `Main.java` | Point d'entrée |
| `Jeu.java` | Recherche, validation, score, progression |
| `Terrain.java` | Grille et placement des mots |
| `Mot.java` | Mot, définition, direction, départ |
| `Position.java` | Coordonnées |
| `FenetrePrincipale.java` | Fenêtre et disposition |
| `PanneauGrille.java` | Grille et sélection au glisser |
| `PanneauDefinitions.java` | Liste des définitions |
| `PanneauScore.java` | Score et progression |
| `Apparence.java` | Palette et typographie |
| `CHANGELOG.md` | Historique |

## Contribution

Créer une branche, puis ouvrir une Pull Request pour revue avant fusion. Les fichiers `.class` et les dossiers de compilation sont ignorés par `.gitignore`.
