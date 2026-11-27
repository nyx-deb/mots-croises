public class Position {
    int ligne;
    int colonne;
    //constructeur
    public Position(int ligne,int colonne){
        this.ligne=ligne;
        this.colonne=colonne;
    }
    public int getLigne(){
        return this.ligne;
    }
    public int getColonne(){
        return this.colonne;
    }
    public void setLigne(int ligne){
        this.ligne=ligne;
    }
    public void setColonne(int colonne){
this.colonne=colonne;
    }


}
