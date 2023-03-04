public class Ice extends Spell{
    /* Constructorul clasei, in care se aleg valorile pentru membrii
    * damage si manaCost */
    public Ice(){
        damage = 30;
        manaCost = 20;
    }

    /* Metoda visit este folosita pentru realizarea design Patternului
    * Visitor; astfel este modelat efectul spell-ului Ice asupra unei entitati.
    * Daca enitatea nu are protectie la Ice, entitatea primeste damage-ul intreg pe
    * care il poate da spell-ul, altfel primeste un damage mai mic */
    public void visit(Entity entity){
        if(entity.iceProtection == false)
            entity.receiveDamage(damage);
        else entity.receiveDamage(damage - 20);
    }
}
