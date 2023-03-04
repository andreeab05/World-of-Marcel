import java.util.ArrayList;
import java.util.Random;

public class Rogue extends Character{
    /* Constructorul public al clasei in care se populeaza
     * membrii clasei (mosteniti de la clasa Character) */
    public Rogue(String name, int level, int experience){
        this.name = name;
        this.level = level;
        currentEXP = experience;
        /* Rogue are protectie la Earth, dar nu si la celelalte
         * 2 spell-uri */
        fireProtection = false;
        iceProtection = false;
        earthProtection = true;
        currentRemainingLife = maxLife;
        currentRemainingMana = maxMana;

        /* Calcularea atributelor personajului in functie de nivelul
         * acestuia. Atributul principal al Rogue este dexterity deci
         * are valoarea cea mai mare */
        if(level < 10) {
            charisma = level * 0.25;
            strength = level * 0.25;
            dexterity = level * 0.5;
        }
        else{
            charisma = level * 0.35;
            strength = level * 0.35;
            dexterity = level * 0.6;
        }
        /* Setare greutate maxima inventar si a banilor initiali.
        * Rogue are o greuttate medie de inventar */
        inventory = new Inventory(10, 100);

        Random random = new Random();
        int origin = 1;
        int bound = 4;
        /* Generare aleatoare abilitati(4 abilitati) */
        abilities = new ArrayList<>();
        for(int i = 0 ; i < 4 ; i++) {
            if (random.nextInt(origin, bound) == 1)
                abilities.add(new Ice());
            else if (random.nextInt(origin, bound) == 2)
                abilities.add(new Fire());
            else abilities.add(new Earth());
        }
    }

    public String toString() {
        return "name: " + name + "level: " + level + "exp: " + currentEXP;
    }

    /* Implementarea metodei care calculeaza damage-ul primit de Rogue,
     * mostenita de la clasa Character care o mostenteste de la Entity */
    @Override
    void receiveDamage(int damage) {
        double damageReduction;
        Random random = new Random();
        if(random.nextInt(4) == 0)
        {
            damageReduction = (charisma  + strength) * 1.5;
            /* Sanse 25% sa primeasca damage injumatatit*/
            damage -= damageReduction;
            if(currentRemainingLife >= damage)
                currentRemainingLife -= damage;
            else currentRemainingLife = 0;
        }
        else{
            damageReduction = (strength + charisma) * 0.75;
            /* Sanse 75% sa primeasca damage normal */
            if(currentRemainingLife >= damage) {
                damage -= damageReduction;
                currentRemainingLife -= damage;
            }
            else currentRemainingLife = 0;

        }
    }

    /* Implementarea metodei care calculeaza damage-ul dat de Rogue,
     * mostenita de la clasa Character care o mostenteste de la Entity.
     * Metoda intoarce valoarea damage-ului */
    @Override
    int getDamage() {
        int damage;
        Random random = new Random();
        if(level < 10)
            damage = 35;
        else damage = 30;
        if(random.nextInt(4) == 0)
        {
            // sanse 25% sa dea damage dublu
            damage += (3 * dexterity);
        }
        else{
            // sanse 75% sa dea damage normal
            damage += (1.5 * dexterity);
        }
        return damage;
    }
}
