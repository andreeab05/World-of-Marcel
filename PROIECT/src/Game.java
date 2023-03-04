import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.json.simple.JSONArray;

public class Game {
    List<Account> accounts;
    Map<Cell.CellType, List<String>> stories;
    private Game(){
        accounts = new ArrayList<>();
        stories = new HashMap<>();
    }

    private static Game instance;

    /* Instantiere intarziata folosind sablonul Singleton.
     Astfel va exista mereu un singur obiect de clasa Game */
    public static Game getInstance() {
        if(instance == null)
            instance = new Game();
        return instance;
    }

    /* Metoda run porneste un joc nou . Apeleaza metodele de citire din
    * fisierele de intrare JSON care vor popula membrii clasei Game,
    * apoi apeleaza metoda de initializare a unui joc nou */
    public void run(String gameMode){
        // metode de citirea si incarcare a fisierelor JSON
        readAccountsJSON();
        readStoriesJSON();
        /* Metoda initializeNewGame primeste ca parametru un string ce
        * reprezinta modul de joc */
        initializeNewGame(gameMode);
    }

    /* Metoda ce initializeaza un joc nou. Daca se alege rularea jocului din terminal,
    * user-ul trebuie sa introduca de la tastatura caracterul P pentru a realiza toate actiunile
    * scenariului de testare hardcodat. Daca se alege GUI este pornita interfata grafica. */
    public void initializeNewGame(String gameMode) {
        Scanner scanner = new Scanner(System.in);
        /* Se verifica daca modul de joc este "terminal" sau "GUI" */
        if (gameMode.equals("terminal")) {
            /* Partea de logare */
            /* Utilizatorul trebuie sa introduca de la tastatura email-ul si parola
            * contului de pe care doreste sa joace */
        System.out.println("Fair well... you chose " + gameMode + ". Now, tell me, what brings you here? Adventure? Are in need of cash?");
        System.out.println("What's with that look on your face, ey? Alrighty, keep your secrets to yourself then!");
        System.out.println("But tell me one thing I really need to know to trust you, what's your email?");
        String email = scanner.next();
        int ok = 0;
            /* Se parcurge lista de conturi si se verifica daca exista email-ul introdus */
            /* Daca email-ul nu exista se afiseaza un mesaj si utilizatorul poate incerca din nou
            * introducerea unui email */
            Account current = new Account();
            while (ok == 0) {
                for (Account account : accounts) {
                    current = account;
                    /* Email-ul introdus corespunde cu o adresa de mail din lista de conturi */
                    if (current.playerInfo.getCredentials().getEmail().equals(email)) {
                        ok = 1;
                        break;
                    }
                }
                /* Email-ul introdus nu corespunde */
                if (ok == 0) {
                    System.out.println("Oh don't be shy! Tell me your real email!");
                    email = scanner.next();
                }
            }

            /* Introducerea de la tastatura a parolei */
            /* Userul are 3 incercari de a introduce o parola corecta, altfel
            * jocul se incheie */
            System.out.println("Great to meet you! Now let's get even more intimate...Tell me your password!");
            String password = scanner.next();
            ok = 0;
            int count = 0;
            while (ok == 0) {
                if (current.playerInfo.getCredentials().getPassword().equals(password)) {
                    ok = 1;
                    break;
                }
                if (count == 2) {
                    System.out.println("You are not very cooperative today. I hate liars... I am sorry but there's nothing left to do...");
                    System.out.println("GUAAARDS!!! GET THE INTRUDER!!! OFF WITH HIS HEAD!!!");
                    return;
                }
                System.out.println("I know it's a big question but how do you expect me to trust you? Try again");
                password = scanner.next();
                count++;
            }

            System.out.println("See, it wasn't that hard! ");
            System.out.println("Now, my good friend, " + current.playerInfo.getName() + ", the old witch felt there's a great power lyin' within you...");
            System.out.println("What kind of creature are you really?...");
            /* Alegere personaj: se apeleaza functia chooseCharacter ce returneaza
            * personajul ales */
            Character currCharacter = chooseCharacter(current);
            System.out.println("Oh, Lord... you are " + currCharacter.name + "... I thought you were just a myth!");
            System.out.println("Alrighty then... Wish you the best luck, but be careful, there are some dangerous beasts on these lands...");

            /* Generare harta hardcodata */
            Grid grid = Grid.createTestMap(5, 5, currCharacter);
            grid.get(0).get(0).visited = 1;
            System.out.println(grid);
                /* Se incearca prinderea exceptiei InvalidCommandException aruncata de
                * metoda Test.getCommand() */
                try {
                    /* Harta se parcurge atat timp cat personajul inca are viata sau pana cand acesta ajunge pe
                    * casuta de finish. Toate mutarile personajului sunt hardcodate. Dupa fiecare miscare este
                    * afisata harta actualizata */
                    while (currCharacter.currentRemainingLife > 0 && !(grid.currentCell.type.equals(Cell.CellType.FINISH))) {
                        int noOfMoves = 0;
                        /* Mutarea personajului pe harta cu 3 casute la dreapta */
                        while (noOfMoves != 3) {
                            Test.getCommand();
                            grid.goEast();
                            /* Metoda ce verifica daca pe casuta curenta se gasesc monede */
                            checkEmptyCellForMoney(currCharacter);
                            System.out.println(showStory(grid.currentCell));
                            /* La ultima mutare, personajul ajunge pe o celula de tip SHOP */
                            if (grid.currentCell.type.equals(Cell.CellType.SHOP)) {
                                Shop shop = (Shop) grid.currentCell.element;
                                System.out.println("New face! Haven't seen you around before! May I interest you with some of the best potions on the continent? Choose your favourites:");
                                /* Se cumpara doua potiuni */
                                int noOfPotions = 0;
                                while (noOfPotions != 2) {
                                    /* Se afiseaza potiunile pe care le poate cumpara jucatorul */
                                    System.out.println(shop);
                                    Test.getCommand();
                                    /* Se verifica daca se poate cumpara potiunea, apoi se adauga in inventar
                                    * dupa cumparare */
                                    if (currCharacter.buyPotion(shop.potions.get(0)) == 1) {
                                        currCharacter.inventory.addPotion(shop.buyPotion(0));
                                    }
                                    noOfPotions++;
                                }
                            }
                            if (!(grid.currentCell.type.equals(Cell.CellType.SHOP)) && !(grid.currentCell.type.equals(Cell.CellType.ENEMY)))
                                System.out.println(grid);
                            noOfMoves++;
                        }
                        /* Mutare la dreapta cu o casuta */
                        Test.getCommand();
                        grid.goEast();
                        System.out.println(showStory(grid.currentCell));
                        System.out.println(grid);

                        /* Mutare in jos cu 3 casute */
                        noOfMoves = 0;
                        while (noOfMoves != 3) {
                            Test.getCommand();
                            grid.goSouth();
                            checkEmptyCellForMoney(currCharacter);
                            System.out.println(showStory(grid.currentCell));
                            System.out.println(grid);
                            noOfMoves++;
                        }

                        /* In urma ultimei mutari,jucatorul ajunge pe o celula de tip ENEMY
                        * Se apeleaza metoda fight care returneaza 1 daca personajul castiga,
                        * respectiv 0 daca pierde */
                        if (grid.currentCell.type.equals(Cell.CellType.ENEMY))
                            if (fight(currCharacter, (Enemy) grid.currentCell.element) == 1) {
                                System.out.println("You won!");
                                /* Se apeleaza functia checkEnemyCellForMoney care verifica daca
                                * in urma victoriei, personajul primeste monede */
                                checkEnemyCellForMoney(currCharacter);
                                Test.getCommand();
                                /* Mutare cu o casuta in jos
                                * Se ajunge pe o celula de tip FINISH si jocul se incheie */
                                grid.goSouth();
                                System.out.println(showStory(grid.currentCell));
                                System.out.println(grid);
                                return;
                            }
                            /* Daca personajul pierde lupta, se afiseaza un mesaj si jocul se incheie */
                            else {
                                System.out.println("You lost, but remember! Next to a battle lost, the greatest misery is a battle gained");
                            }
                    }
                }
                catch (InvalidCommandException e){
                    e.printStackTrace();
                }
                /* Modul de joc ales este GUI */
            } else if (gameMode.equals("GUI")) {
                /* Generare randomizata a hartii si pornirea interfetei grafice */
                LogInWindow loginWindow = new LogInWindow(this);
                return;
            }
    }

    /* Metoda ce verifica daca pe o celula de tip EMPTY se gasesc monede
    * Exista o sansa de 20% de a se gasi monede. Daca se gasesc, metoda returneaza true,
    * altfel returneaza false */
    public boolean checkEmptyCellForMoney(Character character){
        if(new Random().nextInt(5) == 0){
            character.inventory.coins += 10;
            return true;
        }
        return false;
    }

    /* Metoda ce verifica daca pe o celula de tip ENEMy se gasesc monede
     * Exista o sansa de 80% de a se gasi monede. Daca se gasesc, metoda returneaza true,
     * altfel returneaza false */
    public void checkEnemyCellForMoney(Character character){
        if(new Random().nextInt(0, 5) != 0)
            character.inventory.coins += 30;
    }

    /* Metoda fight realizeaza lupta dintre hero si enemy */
    public int fight(Character hero, Enemy enemy) throws InvalidCommandException {
        System.out.println("It's time for a battle! ATTACK!");
        //int enemySpellIndex = 0;

        /* Variabila round retine al cui e randul la atac; cand round e par,
        * ataca hero, respectiv cand e impar ataca enemy */
        int round = 0;

        /* Lupta continua pana cand hero sau enemy ajunge fara viata */
        while((hero.currentRemainingLife != 0) && (enemy.currentRemainingLife != 0)){
            //System.out.println("choice " + round);
            /* Afisarea detaliilor legate de hero si enemy necesare luptei */
            System.out.println("Your life " + hero.currentRemainingLife + " and mana " + hero.currentRemainingMana);
            System.out.println("Enemy's life " + enemy.currentRemainingLife + " and mana " + enemy.currentRemainingMana);
            System.out.println("enemy fire protection " + enemy.fireProtection);
            System.out.println("enemy ice protection " + enemy.iceProtection);
            System.out.println("enemy earth protection " + enemy.earthProtection);

            if(round % 2 == 0) {
                /* Ataca hero */
                /* Se afiseaza actiunile posibile: atac normal, utilizare spell(se afiseaza
                * in paranteza numarul de spell-uri ramase) si utilizare potiune(se afiseaza
                * in paranteza numarul de potiuni disponibile) */
                System.out.println("Make your choice, hero! Attack, use your spells or drink a potion to become stronger!");
                System.out.println("You can use ATTACK, SPELL(" + hero.abilities.size() +
                        ") or POTION(" + hero.inventory.ownedPotions.size() + ")");
                /* Atac cu spell */
                Test.getCommand();
                if(hero.abilities.size() != 0) {
                    System.out.println("Use your powerful spells and destroy the beast!! Type the number of the spell:");
                    /* Cand se ataca cu spell, se afiseaza mereu lista spell-urilor disponibile */
                    hero.showAbilities();
                    Test.getCommand();
                    if (hero.useSpell(hero.abilities.get(0), enemy) == true)
                        hero.abilities.remove(0);
                    else enemy.receiveDamage(hero.getDamage());
                }
                /* Utilizare potiuni */
                else if(hero.inventory.ownedPotions.size() != 0) {
                    System.out.println("Which potion suits your needs? Type the number of the potion:");
                    /* Se afiseaza mereu lista potiunilor disponibile */
                    hero.inventory.showPotions();
                    Test.getCommand();
                    /* Utilizare potiune si eliminarea ei din inventar */
                    hero.inventory.ownedPotions.get(0).usePotion(hero);
                    hero.inventory.removePotion(hero.inventory.ownedPotions.get(0));
                }

                /* Atac normal(dupa ce s-a consumat toate spell-urile si potiunile */
                else{
                    enemy.receiveDamage(hero.getDamage());
                }
                round++;
            }
            else{
                /* Ataca enemy */
                System.out.println("Enemy's turn!");
                Random random = new Random();
                int chance = random.nextInt(4);
                /* Exista sansa de 25% de atac cu spell */
                if(chance == 0){
                    /* Se verifica daca enemy are spell-uri */
                    if(0 < enemy.abilities.size()) {
                        if(enemy.useSpell(enemy.abilities.get(0), hero) == true)
                            enemy.abilities.remove(0);
                        else hero.receiveDamage(enemy.getDamage());
                        //enemySpellIndex++;
                    }
                }
                /* Exista sansa de 75% de atac normal */
                else{
                    hero.receiveDamage(enemy.getDamage());
                }
                round++;
            }
        }
        if(hero.currentRemainingLife > 0)
            return 1;
        else return 0;
    }

    /* Metoda chooseCharacter parcurge element cu element lista de caractere a contului
    * trimis ca parametru si asteapta input de la user. Pentru a alege un personaj, userul
    * trebuie sa tasteze "yes" si pentru a-l refuza, "no". Metoda returneaza personajul
    * ales */
    public Character chooseCharacter(Account current){
        Scanner scanner = new Scanner(System.in);
        for (Character character: current.characters) {
            System.out.println("Are you a legendary " + character.getClass().getName() + "? Type yes or no");
            String ans = scanner.next();
            if(ans.equals("yes")){
                return character;
            }
            System.out.println("I see... Something even more powerful?");
        }
        return null;
    }

    /* Metoda showStory primeste ca parametru o celula si returneaza o poveste
    * in functie de tipul celulei, daca aceasta nu a fost deja vizitata */
    public String showStory(Cell cell){
        Random random = new Random();
        int storyIndex = random.nextInt(stories.get(cell.type).size());
        if(!cell.isVisited()){
            return stories.get(cell.type).get(storyIndex);
        }
        /* Returneaza null daca a fost vizitata deja */
        return null;
    }

    /* Metoda readAccountsJSON parseaza fisierul accounts.json si populeaza lista de counturi */
    private void readAccountsJSON(){
        JSONParser jsonParser = new JSONParser();
        try{
            FileReader reader = new FileReader(new File("accounts.json").getAbsolutePath());
            /* Obtinere obiect de clasa JSONObject */
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            /* Obtinere obiect de clasa JSONArray din obiectul JSONObject */
            JSONArray accounts = (JSONArray) obj.get("accounts");
            if(accounts != null){
                /* Se parcurge array-ul accounts si pentru fiecare obiect se creeaza un obiect
                * de clasa Account care se va adauga dupa popularea membrilor, la lista de conturi(membrul clasei Game) */
                for (Object account : accounts) {
                    /* Creare obiect nou din clasa Account */
                    Account newAccount;
                    /* Obtinere obiect de clasa JSONObject de la numele "credentials" */
                    JSONObject credentials = (JSONObject) ((JSONObject) account).get("credentials");
                    String email = credentials.get("email").toString();
                    String password = credentials.get("password").toString();

                    /* Creare obiect de clasa Credentials */
                    Credentials credential = new Credentials();
                    /* Setare membru email cu valoarea extrasa din fisier pentru email */
                    credential.setEmail(email);
                    /* Setare membru password cu valoarea extrasa din fisier pentru password */
                    credential.setPassword(password);

                    /* Obtinerea valorii de la numele "name" din fisierul JSON */
                    String name = ((JSONObject) account).get("name").toString();
                    /* Obtinerea valorii de la numele "country" din fisierul JSON */
                    String country = ((JSONObject) account).get("country").toString();

                    /* Se obtine un obiect de clasa JSONArray de la numele "favorite_games" */
                    JSONArray favouriteGames = (JSONArray) ((JSONObject)account).get("favorite_games");
                    /* Declararea si instantierea unui obiect de clasa ArrayList<String> in care vom adaga valorile
                    * din obiectul JSONArray */
                    ArrayList<String> favGames = new ArrayList<>();
                    if(favouriteGames != null)
                        /* Se parcurge element cu element array-ul obtinut si fiecare obiect
                        * este transformat in string(prin apelul metodei toString) si adaugat la lista favGames */
                        for (Object game: favouriteGames) {
                            String gameName = game.toString();
                            favGames.add(gameName);
                        }

                    /* Obtinerea valorii de la numele "maps_completed" din fisier */
                    String maps = ((JSONObject)account).get("maps_completed").toString();
                    int playedGames = Integer.parseInt(maps);
                    /* Instantierea obiectului de clasa Account declarat la inceput */
                    newAccount = new Account(credential, name, country, favGames);
                    newAccount.noOfPlayedGames = playedGames;

                    /* Obtinere obiect de clasa JSONArray de la numele "characters" din fisier */
                    JSONArray characters = (JSONArray) ((JSONObject)account).get("characters");
                    /* Instantiere obiect de clasa characterFactory pentru a instantia obiecte din
                    * clasele Warrior, Rogue si Mage (folosind Factory design pattern) */
                    CharacterFactory characterFactory = new CharacterFactory();
                    if(characters != null)
                        /* Parcurgerea obiectului characters */
                        for (Object character : characters) {
                            Character characterOwned;
                            /* Pentru fiecare obiect din JSONArray se obtin valorile de la numele "name",
                            * "profession", "level" si "experience" */
                            String characterName = ((JSONObject)character).get("name").toString();
                            String profession = ((JSONObject)character).get("profession").toString();
                            String levelStr = ((JSONObject)character).get("level").toString();
                            int level = Integer.parseInt(levelStr);
                            String experience = ((JSONObject)character).get("experience").toString();
                            int exp = Integer.parseInt(experience);
                            /* Instantiere obiect de clasa Character */
                            characterOwned = characterFactory.getCharacter(profession, characterName, level, exp);
                            /* Adaugare obiect characterOwned la lista de personaje din a contului curent */
                            newAccount.characters.add(characterOwned);
                        }
                    /* Adaugarea contului curent la lista de conturi din clasa Game */
                    this.accounts.add(newAccount);
                }
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }

    /* Metoda ce parseaza fisierul stories.json si populeaza dictionarul stories.
    * Tipul casutei va reprezenta cheia dictionarului si povestea(stringul) va
    * reprezenta valoarea */
    private void readStoriesJSON() {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(new File("stories.json").getAbsolutePath());
            /* Se obtine obiectul de clasa JSONObject ce contine un JSONArray */
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            /* Se obtine obiectul de clasa JSONArray din obj */
            JSONArray stories = (JSONArray) obj.get("stories");

            if(stories != null){
                /* Se parcurge element cu element obiectul de clasa JSONArray stories */
                for (Object element: stories) {
                    /* Pentru fiecare element, se iau povestea si tipul */
                    String story = ((JSONObject)element).get("value").toString();
                    String type = ((JSONObject)element).get("type").toString();
                    List<String> list;

                    Cell cell = new Cell();
                    /* Daca tipul celulei este EMPTY */
                    if(type.equals("EMPTY")) {
                        /* Se obtine o referinta la lista de povesti din dictionariul stories(tipul celulei este cheia) */
                        list = this.stories.get(Cell.CellType.EMPTY);
                        /* Daca lista este nula, se instanitiaza si se adauga String-ul story */
                        if(list == null) {
                            list = new ArrayList<String>();
                            list.add(story);
                            this.stories.put(Cell.CellType.EMPTY, list);
                        }
                        /* Daca lista nu este vida, doar se adauga String-ul story */
                        else list.add(story);
                    }
                    /* Daca tipul celulei este ENEMY */
                    else if(type.equals("ENEMY")){
                        /* Se obtine o referinta la lista de povesti din dictionariul stories(tipul celulei este cheia) */
                        list = this.stories.get(Cell.CellType.ENEMY);
                        /* Daca lista este nula, se instanitiaza si se adauga String-ul story */
                        if(list == null) {
                            list = new ArrayList<String>();
                            list.add(story);
                            this.stories.put(Cell.CellType.ENEMY, list);
                        }
                        /* Daca lista nu este vida, doar se adauga String-ul story */
                        else list.add(story);
                    }
                    /* Daca tipul celulei este SHOP */
                    else if(type.equals("SHOP")){
                        /* Se obtine o referinta la lista de povesti din dictionariul stories(tipul celulei este cheia) */
                        list = this.stories.get(Cell.CellType.SHOP);
                        /* Daca lista este nula, se instanitiaza si se adauga String-ul story */
                        if(list == null) {
                            list = new ArrayList<String>();
                            list.add(story);
                            this.stories.put(Cell.CellType.SHOP, list);
                        }
                        /* Daca lista nu este vida, doar se adauga String-ul story */
                        else list.add(story);
                    }
                    /* Daca tipul celulei este FINISH */
                    else {
                        /* Se obtine o referinta la lista de povesti din dictionariul stories(tipul celulei este cheia) */
                        list = this.stories.get(Cell.CellType.FINISH);
                        /* Daca lista este nula, se instanitiaza si se adauga String-ul story */
                        if(list == null) {
                            list = new ArrayList<String>();
                            list.add(story);
                            this.stories.put(Cell.CellType.FINISH, list);
                        }
                        /* Daca lista nu este vida, doar se adauga String-ul story */
                        else list.add(story);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }
}
