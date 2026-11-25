

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
            System.out.print("|   ");
        }
        System.out.println("|");
    }
    System.out.println("+---+---+---+---+---+");
}
}
