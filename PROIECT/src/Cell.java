public class Cell {
    int xCoord, yCoord;
    enum CellType{
        EMPTY,
        ENEMY,
        SHOP,
        FINISH
    }
    CellType type;
    CellElement element;
    int visited;

    public Cell(){
        type = null;
        visited = 0;
        element = null;
    }

    public Cell(CellType type){
        this.type = type;
        visited = 0;
        element = null;
    }

    /* Metoda ce verifica daca o celula a fost vizitata.
    * Daca a fost, returneaza true, altfel returneaza false */
    public boolean isVisited(){
        if(visited == 1)
            return true;
        else return false;
    }

    /* Metoda de setare a coordonatelor pe harta */
    public void setCoordinates(int x, int y){
        xCoord = x;
        yCoord = y;
    }

    /* Metoda ce instantiaza membrul de clasa CellEement in functie de tipul
    * primit ca parametru. Metoda returneaza obiectul de tip Cell actualizat */
    public Cell setElement(CellType type){
        if(type.equals(CellType.SHOP))
            element = new Shop();
        else if(type.equals(CellType.EMPTY))
            element = new Empty();
        else if(type.equals(CellType.ENEMY))
            element = new Enemy();
        else
            element = new Finish();

        return this;
    }

}
