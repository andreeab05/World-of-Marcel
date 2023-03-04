import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Entity implements CellElement{

    /* In constructorul clasei se instantiaza membrii mosteniti de la
    * clasa Entity */
    public Enemy(){
        Random random = new Random();
        int lowerbound = 50;
        int upperbound = 100;
        /* Generare aleatoare a valorilor pentru viata si mana(cu valori intre 0 si 100) */
        currentRemainingLife = random.nextInt(lowerbound, upperbound);
        currentRemainingMana = random.nextInt(lowerbound, upperbound);

        /* Generare aleatoare protectii la cele 3 spell-uri */
        fireProtection = random.nextBoolean();
        iceProtection = random.nextBoolean();
        earthProtection = random.nextBoolean();

        int origin = 1;
        int bound = 4;
        /* Generare aleatoare spell-uri si adaugarea lor in lista */
        abilities = new ArrayList<>();
        for(int i = 0 ; i < 3 ; i++){
            if(random.nextInt(origin, bound) == 1)
                abilities.add(new Ice());
            else if(random.nextInt(origin, bound) == 2)
                abilities.add(new Fire());
            else abilities.add(new Earth());
        }
    }

    /* Implementare a metodei toCharacter() din interfata CellElement*/
    @Override
    public char toCharacter() {
        return 'E';
    }

    /* Metoda care calculeaza damage-ul primit de enemy
    * Se genereaza aleator 0 sau 1; daca se genereaza 0,damage-ul este
    * evitat si viata nu este afectata,iar pentru 1 viata inamicului scade
    * cu damage(sansa de 50% de a evita damage-ul) */
    @Override
    public void receiveDamage(int damage) {
        Random randomDamage = new Random();
        if(randomDamage.nextInt(0,2) == 1)
            if(currentRemainingLife >= damage)
                currentRemainingLife -= damage;
            else currentRemainingLife = 0;
    }
    /* Metoda care calculeaza damage-ul dat de enemy
    * Enemy are sansa 50% de a da damage dublu. Valoarea
    * damage-ului este fixa. */
    @Override
    public int getDamage() {
        Random randomDamage = new Random();
        if(randomDamage.nextInt(0, 2) == 0) {
            return 20;
        }
        else{
            return 40;
        }
    }
}
