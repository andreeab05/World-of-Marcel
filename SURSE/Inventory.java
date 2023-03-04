import java.util.ArrayList;
import java.util.List;

public class Inventory {
    List<Potion> ownedPotions;
    int maxWeight;
    int coins;

    public Inventory(int maxWeight, int coins){
        ownedPotions = new ArrayList<>();
        this.maxWeight = maxWeight;
        this.coins = coins;
    }

    /* Metoda de adaugare a unui obiect de clasa Potion la
    * lista de potiuni */
    public void addPotion(Potion newPotion){
        ownedPotions.add(newPotion);
    }

    /* Metoda de eliminare a unui obiect de clasa Potion din
    * lista de potiuni */
    public void removePotion(Potion potion){
        ownedPotions.remove(potion);
    }

    /* Metoda ce calculeaza greutatea ramasa a inventarului.
    * Calculeaza greutatea ocupata de potiunile existente si scade
    * aceasta valoare din greutatea maxima a inventarului.
    * Valoarea obtinuta in urma scaderii este intoarsa de metoda */
    public int remainingInventoryWeight(){
        int currentWeight = 0;
        for (Potion potion:ownedPotions) {
            currentWeight += potion.getInventoryWeight();
        }
        return maxWeight - currentWeight;
    }

    /* Metoda ce afiseaza sub forma de lista potiunile existente
    * in inventar. Se parcurge lista de potiuni element cu element
    * si se formeaza stringul ce va fi afisat la final de metoda */
    public void showPotions(){
        String res = new String();
        int i = 1;
        for (Object potion:ownedPotions) {
            res += "(" + i + ") " + ((Potion)potion).getClass().getName() + "\n";
            i++;
        }
        System.out.println(res);
    }
}
