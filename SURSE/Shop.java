import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shop implements CellElement{
    List<Potion> potions;

    /* In constructorul clasei se populeaza lista de potiuni in mod aleator.
    * Un magazin poate contine intre 2 si 4 potiuni(acest numar este ales de asemenea
    * in mod aleator) */
    public Shop(){
        potions = new ArrayList<>();
        Random random = new Random();
        int numberOfPotions = random.nextInt(2, 5);
        for(int i = 0 ; i < numberOfPotions ; i++)
        {
            if(random.nextInt(0,2) == 0)
                potions.add(new HealthPotion());
            else potions.add(new ManaPotion());
        }
    }

    @Override
    public char toCharacter() {
        return 'S';
    }

    /* Metoda de eliminare a potiunii de la pozitia index, care
    * este primit ca parametru. Potiunea de la pozitia index este
    * returnata de metoda */
    public Potion buyPotion(int index){
        return potions.remove(index);
    }

    /* Suprascriere a metodei toString() */
    @Override
    public String toString() {
        String res = new String();
        int i = 1;
        for (Object potion:potions) {
            res += "(" + i + ") " + ((Potion)potion).getClass().getName() + "\n";
            i++;
        }
        return res;
    }
}
