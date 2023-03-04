import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid extends ArrayList<ArrayList<Cell>>{
    int width, height;
    Character character;
    Cell currentCell;

    private Grid(int width, int height, Character character){
        this.width = width;
        this.height = height;
        this.character = character;
    }

    /* Metoda de deplasare cu o casuta in sus pe harta. Se verifica daca
    * se poate face deplasarea; daca nu se poate, se afiseaza un mesaj */
    public void goNorth(){
        if(currentCell.xCoord > 0) {
            this.get(currentCell.xCoord).get(currentCell.yCoord).visited = 1;
            currentCell = this.get(currentCell.xCoord - 1).get(currentCell.yCoord);
        }
        else System.out.println("You shall not go there, traveller! There are unknown and dangerous roads that way!");
    }

    /* Metoda de deplasare cu o casuta in jos pe harta. Se verifica daca
     * se poate face deplasarea; daca nu se poate, se afiseaza un mesaj */
    public void goSouth(){
        if(currentCell.xCoord < height) {
            this.get(currentCell.xCoord).get(currentCell.yCoord).visited = 1;
            currentCell = this.get(currentCell.xCoord + 1).get(currentCell.yCoord);
        }
        else System.out.println("You shall not go there, traveller! There are unknown and dangerous roads that way!");
    }

    /* Metoda de deplasare cu o casuta la dreapta pe harta. Se verifica daca
     * se poate face deplasarea; daca nu se poate, se afiseaza un mesaj */
    public void goEast(){
        if(currentCell.yCoord < width) {
            this.get(currentCell.xCoord).get(currentCell.yCoord).visited = 1;
            currentCell = this.get(currentCell.xCoord).get(currentCell.yCoord + 1);
        }
        else System.out.println("You shall not go there, traveller! There are unknown and dangerous roads that way!");
    }

    /* Metoda de deplasare cu o casuta la stanga pe harta. Se verifica daca
     * se poate face deplasarea; daca nu se poate, se afiseaza un mesaj */
    public void goWest(){
        if(currentCell.yCoord > 0) {
            this.get(currentCell.xCoord).get(currentCell.yCoord).visited = 1;
            currentCell = this.get(currentCell.xCoord).get(currentCell.yCoord - 1);
        }
        else System.out.println("You shall not go there, traveller! There are unknown and dangerous roads that way!");
    }

    /* Metoda createTestMap genereaza harta hardcodata necesara testarii. Se instantiaza un obiect
    * de clasa Grid, apoi se instantiaza atatea liste cat este valoarea lui height(acestea reprezinta liniile).
    * Pentru fiecare linie se adauga casutele corespunzatoare hartii de testare. La final se seteaza casuta curenta
    * (de pe care porneste jucatorul) ca fiind cea de la coordonatele (0, 0). Metoda returneaza obiectul de clasa
    * Grid realizat. */
    public static Grid createTestMap(int width, int height, Character character){
        Grid grid = new Grid(width,height, character);
        for(int i = 0 ; i < height ; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                if ((i == 0 && j == 3) || (i == 1 && j == 3) || (i == 2 && j == 0)) {
                    row.add(j, new Cell(Cell.CellType.SHOP).setElement(Cell.CellType.SHOP));
                    row.get(j).setCoordinates(i, j);
                } else if ((i == 3 && j == 4) || (i == 3 && j == 0) || (i == 4 && j == 0) || (i == 1 && j == 0)){
                    row.add(j, new Cell(Cell.CellType.ENEMY).setElement(Cell.CellType.ENEMY));
                    row.get(j).setCoordinates(i, j);
                }
                else if(i == 4 && j == 4) {
                    row.add(j, new Cell(Cell.CellType.FINISH).setElement(Cell.CellType.FINISH));
                    row.get(j).setCoordinates(i, j);
                }

                else {
                    row.add(j, new Cell(Cell.CellType.EMPTY).setElement(Cell.CellType.EMPTY));
                    row.get(j).setCoordinates(i, j);
                }
            }
            grid.add(i, row);
        }
        grid.currentCell = new Cell(grid.get(0).get(0).type);
        return grid;
    }

    /* Metoda createMap creeaza harti randomizat. */
    public static Grid createMap(int width, int height, Character character){
        Random random = new Random();
        Grid grid = new Grid(width,height, character);
        for(int i = 0 ; i < height ; i++) {
            grid.add(i, new ArrayList<>());
            for(int j = 0 ; j < width ; j++)
                grid.get(i).add(j, new Cell());
        }

        /* Generare random de valori intre 0 si height, respectiv width, ce vor reprezenta
        * coordonatele x si y ale unei casute */
        int index1 = random.nextInt(height);
        int index2 = random.nextInt(width);
        /* Setarea coordonatelor randomizate ale casutei de tip FINISH */
        grid.get(index1).get(index2).setElement(Cell.CellType.FINISH);
        grid.get(index1).get(index2).type = Cell.CellType.FINISH;
        grid.get(index1).get(index2).setCoordinates(index1, index2);

        /* Setarea casutei de pe pozitia (0, 0) ca fiind de tip EMPTY
        (casuta de pe care porneste jucatorul) */
        grid.get(0).get(0).setElement(Cell.CellType.EMPTY);
        grid.get(0).get(0).type = Cell.CellType.EMPTY;
        grid.get(0).get(0).setCoordinates(0, 0);

        /* Numarul de inamici de pe harta este randomizat
        (pot exista minim 4, maxim 6 inamici pe harta) */
        int noOfEnemies = random.nextInt(4, 7);
        while(noOfEnemies != 0){
            index1 = random.nextInt(height);
            index2 = random.nextInt(width);
            /* Se verifica daca la coordonatele generate random este deja setat tipul casutei,
            * daca nu, se seteaza cu ENEMY, altfel se genereaza alte coordonate */
            if(grid.get(index1).get(index2).element == null)
            {
                grid.get(index1).get(index2).setElement(Cell.CellType.ENEMY);
                grid.get(index1).get(index2).type = Cell.CellType.ENEMY;
                grid.get(index1).get(index2).setCoordinates(index1, index2);
                noOfEnemies--;
            }
        }

        /* Numarul de magazine de pe harta este randomizat
        (pot exista minim 2, maxim 5 magazine pe harta) */
        int noOfShops = random.nextInt(2, 6);
        while(noOfShops != 0){
            index1 = random.nextInt(height);
            index2 = random.nextInt(width);
            /* Se verifica daca la coordonatele generate random este deja setat tipul casutei,
             * daca nu, se seteaza cu SHOP, altfel se genereaza alte coordonate */
            if(grid.get(index1).get(index2).element == null)
            {
                grid.get(index1).get(index2).setElement(Cell.CellType.SHOP);
                grid.get(index1).get(index2).type = Cell.CellType.SHOP;
                grid.get(index1).get(index2).setCoordinates(index1, index2);
                noOfShops--;
            }
        }

        /* Se parcurge harta si pentru fiecare casuta al carei tip nu a fost deja
        * setat, se seteaza tipul cu EMPTY */
        for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < width ; j++){
                if(grid.get(i).get(j).element == null){
                    grid.get(i).get(j).setElement(Cell.CellType.EMPTY);
                    grid.get(i).get(j).type = Cell.CellType.EMPTY;
                    grid.get(i).get(j).setCoordinates(i, j);
                }
            }
        }
        /* Se seteaza casuta curenta pe care se afla personajul cu casuta de la coordonatele
        * (0, 0) */
        grid.currentCell = new Cell(grid.get(0).get(0).type);
        return grid;
    }

    /* Suprascrierea metodei toString() pentru a afisa harta sub forma unei matrici */
    @Override
    public String toString() {
        String res = new String();
        for(int i = 0 ; i < height ; i++) {
            for (int j = 0; j < width; j++) {
                if(currentCell.xCoord == i && currentCell.yCoord == j)
                    res += "P ";
                else if(get(i).get(j).isVisited())
                    res += get(i).get(j).element.toCharacter() + " ";
                else
                    res += "? ";
            }
            res += "\n";
        }
    return res;
    }
}
