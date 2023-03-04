public class ManaPotion implements Potion{
    final private int price = 50;
    final private int weight = 2;
    final private int regenerationValue = 30;

    /* Metoda de utilizare a potiunii. Se apeleaza metoda manaRegeneration din clasa
    * Character care reface mana personajului cu o valoare data */
    @Override
    public void usePotion(Character character) {
        character.manaRegeneration(getRegeneration());
    }

    /* Metoda de preluare a pretului */
    @Override
    public int getPrice() {
        return price;
    }

    /* Metoda de preluare a valorii de regenerare */
    @Override
    public int getRegeneration() {
        return regenerationValue;
    }

    /* Metoda de preluare a greutatii ocupate de potiune
     * in inventar */
    @Override
    public int getInventoryWeight() {
        return weight;
    }
}
