import java.util.List;

abstract class Entity implements Element{
    List<Spell> abilities;
    double currentRemainingLife;
    final double maxLife = 100;
    double currentRemainingMana;
    final double maxMana = 100;
    boolean fireProtection, earthProtection, iceProtection;

    /* Metoda de regenerare a vietii personajului cu o valoare data.
    * Se adauga valoarea primita ca parametru la viata curenta a personajului.
    * Daca aceasta valoare depaseste valoarea maxima a vietii personajului,
    * valoarea curenta a vietii se reduce la valoarea maxima a vietii */
    public void lifeRegeneration(int extraLife){
        currentRemainingLife += extraLife;
        if(currentRemainingLife > maxLife)
            currentRemainingLife = maxLife;
    }

    /* Metoda de regenerare a manei personajului cu o valoare data.
     * Se adauga valoarea primita ca parametru la mana curenta a personajului.
     * Daca aceasta valoare depaseste valoarea maxima pe care o poate lua mana
     * personajului, valoarea curenta a manei se reduce la valoarea maxima a vietii*/
    public void manaRegeneration(int extraMana){
        currentRemainingMana += extraMana;
        if(currentRemainingMana > maxMana)
            currentRemainingMana = maxMana;
    }

    /* Metoda de accept ce "accepta" vizita unui visitor. Astfel,
    * visitor viziteaza entitatea curenta */
    public void accept(Visitor visitor){
        visitor.visit(this);
    }

    /* Metoda useSpell verifica daca entitatea care doreste sa foloseasca spell-ul
    are mana suficienta pentru a-l folosi; daca are, din mana curenta se scade mana
    necesara utilizarii spell-ului si se apeleaza metoda visit de catre entitatea
    asupra careia se foloseste spell-ul. Metoda returneaza true daca s-a putut folosi
    spell-ul, altfel returneaza false */
    public boolean useSpell(Spell spell, Entity enemy){
        if(currentRemainingMana >= spell.manaCost) {
            currentRemainingMana -= spell.manaCost;
            enemy.accept(spell);
            return true;
        }
        else return false;
    }

    /* Metoda showAbilities afiseaza abilitatile sub forma unei liste */
    public void showAbilities(){
        String res = new String();
        int i = 1;
        for (Spell spell:abilities) {
            res += "(" + i + ") " + spell.getClass().getName() + "\n";
            i++;
        }
        System.out.println(res);
    }

    /* Metode abstracte ce urmeaza sa fie implementate de clasele
    * ce mostenesc Entity */
    abstract void receiveDamage(int damage);
    abstract int getDamage();


}
