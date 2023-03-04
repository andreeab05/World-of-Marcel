abstract class Character extends Entity{
    String name;
    int xCoord, yCoord;
    Inventory inventory;
    int currentEXP;
    int level;
    double strength, charisma, dexterity;

    /* Metoda buyPotion verifica daca personajul are destui bani pentru a
    * cumpara potiunea si daca are suficient spatiu in inventar. Daca aceste
    * conditii sunt indeplinite, achizitia se poate face si metoda returneaza 1.
    * Altfel, returneaza 0. */
    public int buyPotion(Potion x){
        if(x.getPrice() > inventory.coins){
            System.out.println("Not enough coins, traveller! :(");
            return 0 ;
        }
        if(inventory.remainingInventoryWeight() < x.getInventoryWeight())
        {
            System.out.println("Not enough inventory space!");
            return 0;
        }

        System.out.println("Go ahead!Buy your potion, there's a dark road ahead waiting for you...");
        inventory.coins -= x.getPrice();
        return 1;
    }
}
