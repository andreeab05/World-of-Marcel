public class Fire extends Spell{
    /* Constructorul clasei, in care se aleg valorile pentru membrii
     * damage si manaCost */
    public Fire(){
        damage = 35;
        manaCost = 25;
    }

    /* Metoda visit este folosita pentru realizarea design patternului
     * Visitor; astfel este modelat efectul spell-ului Fire asupra unei entitati.
     * Daca enitatea nu are protectie la Fire, entitatea primeste damage-ul intreg pe
     * care il poate da spell-ul, altfel primeste un damage mai mic */
    public void visit(Entity entity){
        if(entity.fireProtection == false)
            entity.receiveDamage(damage);
        else entity.receiveDamage(damage - 20);
    }
}
