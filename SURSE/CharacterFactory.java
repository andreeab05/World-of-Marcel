public class CharacterFactory {
    /* Metoda getCharacter se foloseste pentru implementarea sablonului Factory.
    * Se verifica daca profesia primita ca parametru coincide cu Warrior, Rogue sau Mage, caz
    * in care se intoarce un obiect din clasa respectiva, altfel se returneaza null */
    public Character getCharacter(String profession, String name, int level, int experience ){
        if(profession.equalsIgnoreCase("Warrior"))
            return new Warrior(name, level, experience);

        else if(profession.equalsIgnoreCase("Mage"))
            return new Mage(name, level, experience);

        else if(profession.equalsIgnoreCase("Rogue"))
            return new Rogue(name, level, experience);

        return null;
    }
}
