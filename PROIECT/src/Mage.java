import java.util.ArrayList;
import java.util.Random;

public class Mage extends Character{
    /* Constructorul public al clasei in care se populeaza
     * membrii clasei (mosteniti de la clasa Character) */
    public Mage(String name, int level, int experience){
        this.name = name;
        this.level = level;
        currentEXP = experience;
        /* Mage are protectie la Ice, dar nu si la celelalte
         * 2 spell-uri */
        fireProtection = false;
        iceProtection = true;
        earthProtection = false;
        currentRemainingLife = maxLife;
        currentRemainingMana = maxMana;

        /* Calcularea atributelor personajului in functie de nivelul
         * acestuia. Atributul principal al Mage este charisma deci
         * are valoarea cea mai mare */
        if(level < 10) {
            charisma = level * 0.5;
            strength = level * 0.25;
            dexterity = level * 0.25;
        }
        else{
            charisma = level * 0.6;
            strength = level * 0.35;
            dexterity = level * 0.35;
        }
        /* Setare greutate maxima inventar si a banilor initiali.
        * Mage are cea mai mica greutate de inventar */
        inventory = new Inventory(5, 100);

        Random random = new Random();
        int origin = 1;
        int bound = 4;
        /* Generare aleatoare abilitati(4 abilitati) */
        abilities = new ArrayList<>();
        for(int i = 0 ; i < 4 ; i++){
            if(random.nextInt(origin, bound) == 1)
                abilities.add(new Ice());
            else if(random.nextInt(origin, bound) == 2)
                abilities.add(new Fire());
            else abilities.add(new Earth());
        }
    }

    @Override
    public String toString() {
        return "name: " + name + "level: " + level + "exp: " + currentEXP;
    }

    /* Implementarea metodei care calculeaza damage-ul primit de Warrior,
     * mostenita de la clasa Character care o mostenteste de la Entity */
    @Override
    void receiveDamage(int damage) {
        double damageReduction;
        Random random = new Random();
        if(random.nextInt(4) == 0)
        {
            damageReduction = (strength + dexterity) * 1.5;
            /* Sanse 25% sa primeasca damage injumatatit*/
            damage -= damageReduction;
            if(currentRemainingLife >= damage)
                currentRemainingLife -= damage;
            else currentRemainingLife = 0;
        }
        else{
            /* Sanse 75% sa primeasca damage normal */
            if(currentRemainingLife >= damage) {
                damageReduction = (strength + dexterity) * 0.75;
                damage -= damageReduction;
                currentRemainingLife -= damage;
            }
            else currentRemainingLife = 0;
        }
    }

    /* Implementarea metodei care calculeaza damage-ul dat de Mage,
     * mostenita de la clasa Character care o mostenteste de la Entity.
     * Metoda intoarce valoarea damage-ului */
    @Override
    public int getDamage() {
        int damage;
        Random random = new Random();
        if(level < 10)
            damage = 35;
        else damage = 30;
        if(random.nextInt(4) == 0)
        {
            /* Sanse 25% sa dea damage dublu */
            damage += (2.5 * charisma);
        }
        else{
            /* Sanse 75% sa dea damage normal */
            damage += (1.25 * charisma);
        }
        return damage;
    }
}
