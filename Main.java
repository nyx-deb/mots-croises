public class Main {
    public static void main(String[] args) {
        Terrain terrain = new Terrain();

        // Dictionnaire de mots à trouver sur la grille 5x5 (indices de 0 à 4)
        Mot[] dictionnaire = {
            // Ligne 0 : J (0,1), E (0,2), U (0,3)
            new Mot("JEU", "horizontal", new Position(0, 1), "Activité ludique"),

            // Vertical depuis (0,3) : U (0,3) croise JEU, S (1,3)
            new Mot("US", "vertical", new Position(0, 3), "Abréviation courante"),

            // Oblique montante depuis (3,0) : S (3,0), O (2,1), L (1,2)
            new Mot("SOL", "oblique", new Position(3, 0), "Surface terrestre"),

            // Ligne 4 : D (4,0), A (4,1), N (4,2), S (4,3), E (4,4)
            new Mot("DANSE", "horizontal", new Position(4, 0), "Suite de mouvements rythmés"),

            // Vertical depuis (0,0) : P (0,0), A (1,0)
            new Mot("PA", "vertical", new Position(0, 0), "Début de mot"),

            // Exemple de mot qui sera refusé (dépasse la grille)
            new Mot("ABCDE", "horizontal", new Position(3, 3), "Test hors limites")
        };

        // 1. Remplir la grille avec les mots valides du dictionnaire
        terrain.remplirAvecDictionnaire(dictionnaire);

        System.out.println("=== Grille avec les mots à trouver ===");
        terrain.afficherRectangle();

        // 2. Remplir les cases vides (' ') avec des lettres aléatoires
        terrain.remplirCasesVides();

        System.out.println("=== Grille finale avec lettres de remplissage ===");
        terrain.afficherRectangle();

        // 3. Optionnel : afficher la liste des mots placés et leurs définitions
        System.out.println("=== Mots du dictionnaire ===");
        for (Mot m : dictionnaire) {
            System.out.println("- " + m.mot + " (" + m.direction + ") : " + m.definition);
        }
    }
}
