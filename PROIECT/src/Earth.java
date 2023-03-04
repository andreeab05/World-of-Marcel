public class Earth extends Spell{
    /* Constructorul clasei, in care se aleg valorile pentru membrii
     * damage si manaCost */
    public Earth(){
        damage = 25;
        manaCost = 15;
    }

    /* Metoda visit este folosita pentru realizarea design patternului
     * Visitor; astfel este modelat efectul spell-ului Earth asupra unei entitati.
     * Daca enitatea nu are protectie la Earth, entitatea primeste damage-ul intreg pe
     * care il poate da spell-ul, altfel primeste un damage mai mic */
    public void visit(Entity entity){
        if(entity.earthProtection == false)
            entity.receiveDamage(damage);
        else entity.receiveDamage(damage - 10);
    }
}
