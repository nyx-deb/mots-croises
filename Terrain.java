import  java.util.Random;

public class Terrain {

   
    
     char[][] solution =new char [5][5];
    

     public Terrain(){
          for (int ligne=0;ligne<5;ligne++){
               for(int colonne=0;colonne<5;colonne++){
                   solution[ligne][colonne]=' ';
               }
               
          }
     }
     public void afficher(){
          for(int ligne= 0;ligne<5;ligne++){
               for(int colonne=0;colonne<5;colonne++){
                  System.out.print(solution[ligne][colonne]);  
               }
               System.out.println();
          }
     }



public void afficherRectangle(){
    for (int ligne = 0; ligne < 5; ligne++) {
        System.out.println("+---+---+---+---+---+");
        for (int colonne = 0; colonne < 5; colonne++) {
            System.out.print("| "+ solution[ligne][colonne]+" ");
        }
        System.out.println("|");
    }
    System.out.println("+---+---+---+---+---+");
}
public void placerMot(Mot m){
    if(!peutPlacer(m)){
        return ;
    }

     if (m.direction.equals("horizontal")) {
          for(int k=0;k<m.mot.length();k++){
               int l=m.pos_depart.ligne;
               int c=m.pos_depart.colonne+k;
               solution[l][c]=m.mot.charAt(k);
          }
          
     }
     else if (m.direction.equals("vertical")) {
          for(int k=0;k<m.mot.length();k++){
               int l=m.pos_depart.ligne+k;
               int c=m.pos_depart.colonne;
               solution[l][c]=m.mot.charAt(k);
          }
          
     }//oblique montante
     else if (m.direction.equals("oblique")) {
          for(int k=0;k<m.mot.length();k++){
               int l=m.pos_depart.ligne-k;
               int c=m.pos_depart.colonne+k;
               solution[l][c]=m.mot.charAt(k);
          }
          
     }
}
//controle 
public boolean peutPlacer(Mot m) {
    for (int k = 0; k < m.mot.length(); k++) {
        int l = 0;
        int c = 0;

        if (m.direction.equals("horizontal")) {
            l = m.pos_depart.ligne;
            c = m.pos_depart.colonne + k;
        } else if (m.direction.equals("vertical")) {
            l = m.pos_depart.ligne + k;
            c = m.pos_depart.colonne;
        } else if (m.direction.equals("oblique")) {
            l = m.pos_depart.ligne - k;
            c = m.pos_depart.colonne + k;
        } else {
            return false;
        }

        // 1. Contrôle des limites de la grille (0 à 4)
        if (l < 0 || l >= 5 || c < 0 || c >= 5) {
            return false;
        }

        // 2. Contrôle des intersections (case vide ou même lettre)
        char caseActuelle = solution[l][c];
        char lettreMot = m.mot.charAt(k);
        if (caseActuelle != ' ' && caseActuelle != lettreMot) {
            return false;
        }
    }

    return true;
}

public boolean placerSiPossible(Mot m) {
    if (peutPlacer(m)) {
        placerMot(m);
        return true;
    } else {
        System.out.println("Impossible de placer le mot : " + m.mot);
        return false;
    }
}
public void remplirAvecDictionnaire(Mot[] dictionnaire) {
        if (dictionnaire == null) {
            return;
        }
        for (Mot m : dictionnaire) {
            if (m != null) {
                placerSiPossible(m);
            }
        }
    }

    public void remplirCasesVides() {
        Random random = new Random();
        for (int ligne = 0; ligne < 5; ligne++) {
            for (int colonne = 0; colonne < 5; colonne++) {
                if (solution[ligne][colonne] == ' ') {
                    solution[ligne][colonne] = (char) ('A' + random.nextInt(26));
                }
            }
        }
    }
    
}


