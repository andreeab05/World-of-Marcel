import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BattleWindow extends JFrame implements ActionListener {
    Character hero;
    Enemy enemy;
    JLabel heroImage, enemyImage, background, logo, inventoryLabel, levelLabel, expLabel;
    JLabel heroLife, enemyLife, heroMana, enemyMana, fireProtection, iceProtection, earthProtection;
    JPanel inventoryPanel, potionsMenuPanel, spellMenuPanel, spellsPanel, heroPanel, enemyPanel;
    JButton healthPotionButton, manaPotionButton, fireButton, iceButton, earthButton, attackButton,
    victoryStatus;
    int noOfHealthPotions, noOfManaPotions, noOfFireSpells, noOfIceSpells, noOfEarthSpells;

    public BattleWindow(Character hero, Enemy enemy){
        this.hero = hero;
        this.enemy = enemy;
        noOfHealthPotions = 0;
        noOfManaPotions = 0;
        noOfFireSpells = 0;
        noOfEarthSpells = 0;
        noOfIceSpells = 0;

        /* Numarare potiuni pentru a afisa numarul lor in interfata */
        for (Potion potion : hero.inventory.ownedPotions) {
            if(potion.getClass().getName().equals("HealthPotion")){
                noOfHealthPotions++;
            }
            else noOfManaPotions++;
        }

        /* Numarare abilitati pentru a afisa numarul de spell-uri disponibile
        * din fiecare clasa de spell-uri */
        for (Spell spell : hero.abilities) {
            if(spell.getClass().getName().equals("Fire")){
                noOfFireSpells++;
            }
            else if(spell.getClass().getName().equals("Ice")){
                noOfIceSpells++;
            }
            else noOfEarthSpells++;
        }

        /* Setare dimeniuni fereastra. Nu am folosit un layout
         * pentru a putea controla pozitia elementelor de fereastra.
         * Fereastra nu este redimensionabila */
        setMinimumSize(new Dimension(980, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        /* Setare imagine de background */
        background = new JLabel();
        background.setBounds(0,0, getWidth(), getHeight());
        background.setLayout(null);
        background.setIcon(getPicture(new File("battleBackground.jpg"), background.getWidth(), background.getHeight()));
        add(background);

        /* Setarea imaginii eroului(va fi pozitionata in
        * partea stanga a ferestrei) */
        heroImage = new JLabel();
        heroImage.setBounds(40, 50, 220, 305);
        heroImage.setBackground(new Color(85, 100, 130, 200));
        heroImage.setOpaque(true);
        if(hero.getClass().getName().equals("Rogue"))
            heroImage.setIcon(getPicture(new File("Rogue.png"), heroImage.getWidth(), heroImage.getHeight()));
        else if(hero.getClass().getName().equals("Mage"))
            heroImage.setIcon(getPicture(new File("Mage.png"), heroImage.getWidth(), heroImage.getHeight()));
        else if(hero.getClass().getName().equals("Warrior"))
            heroImage.setIcon(getPicture(new File("Warrior.png"), heroImage.getWidth(), heroImage.getHeight()));
        background.add(heroImage);

        /* Pentru a afisa detlaiile despre erou(viata, mana, nivel si experienta)
        * am folosit un panel in care am pus 4 labeluri ce vor indica valorile pentru
        * caracteristicile personajului */
        heroPanel = new JPanel();
        heroPanel.setLayout(new GridLayout(4,1));
        heroPanel.setBounds(40, 360, 220, 100);
        /* Setare label ce afiseaza viata eroului */
        heroLife = new JLabel("Life: " + hero.currentRemainingLife + " / " + hero.maxLife);
        heroLife.setOpaque(true);
        heroLife.setBackground(new Color(26,20,0));
        heroLife.setForeground(new Color(255, 196,0));
        heroLife.setHorizontalAlignment(JLabel.CENTER);
        /* Setare label ce afiseaza mana eroului */
        heroMana = new JLabel("Mana: " + hero.currentRemainingMana + " / " + hero.maxMana);
        heroMana.setOpaque(true);
        heroMana.setBackground(new Color(26,20,0));
        heroMana.setForeground(new Color(255, 196,0));
        heroMana.setHorizontalAlignment(JLabel.CENTER);
        /* Setare label ce afiseaza experienta curenta a eroului */
        expLabel = new JLabel("EXP: " + hero.currentEXP);
        expLabel.setOpaque(true);
        expLabel.setBackground(new Color(26,20,0));
        expLabel.setForeground(new Color(255, 196,0));
        expLabel.setHorizontalAlignment(JLabel.CENTER);
        /* Setare label ce afiseaza nivelul curent al eroului */
        levelLabel = new JLabel("Level: " + hero.level);
        levelLabel.setOpaque(true);
        levelLabel.setBackground(new Color(26,20,0));
        levelLabel.setForeground(new Color(255, 196,0));
        levelLabel.setHorizontalAlignment(JLabel.CENTER);
        /* Adaugare label-urilor in panel */
        heroPanel.add(heroLife); heroPanel.add(heroMana);
        heroPanel.add(expLabel); heroPanel.add(levelLabel);
        /* Adaugare panel la bacground */
        background.add(heroPanel);

        /* Setarea imaginii inamicului(va fi pozitionata in
         * partea dreapta a ferestrei) */
        enemyImage = new JLabel();
        enemyImage.setBounds(700, 50, 220, 305);
        enemyImage.setBackground(new Color(85, 100, 130, 200));
        enemyImage.setOpaque(true);
        enemyImage.setIcon(getPicture(new File("enemy.png"), enemyImage.getWidth(), enemyImage.getHeight()));
        background.add(enemyImage);

        /* Pentru a afisa detlaiile despre inamic(viata, mana, cele 3 protectii la spell-uri)
         * am folosit un panel in care am pus 5 labeluri ce vor indica valorile pentru
         * caracteristicile personajului */
        enemyPanel = new JPanel();
        enemyPanel.setLayout(new GridLayout(5, 1));
        enemyPanel.setBounds(700, 360, 220, 100);
        enemyPanel.setBackground(new Color(26,20,0));
        /* Setare label ce va afisa viata ramasa a inamicului */
        enemyLife = new JLabel("Life: " + enemy.currentRemainingLife + " / " + enemy.maxLife);
        enemyLife.setForeground(new Color(255, 196,0));
        enemyLife.setHorizontalAlignment(JLabel.CENTER);
        /* Setare label ce va afisa mana ramasa a inamicului */
        enemyMana = new JLabel("Mana: " + enemy.currentRemainingMana + " / " + enemy.maxMana);
        enemyMana.setForeground(new Color(255, 196,0));
        enemyMana.setHorizontalAlignment(JLabel.CENTER);
        /* Setare label ce afiseaza daca inamicul are protectie la Fire */
        fireProtection = new JLabel("Fire Spell Protection: " + enemy.fireProtection);
        fireProtection.setForeground(Color.red);
        fireProtection.setHorizontalAlignment(JLabel.CENTER);
        /* Setare label ce afiseaza daca inamicul are protectie la Ice */
        iceProtection = new JLabel("Ice Spell Protection: " + enemy.iceProtection);
        iceProtection.setForeground(Color.CYAN);
        iceProtection.setHorizontalAlignment(JLabel.CENTER);
        /* Setare label ce afiseaza daca inamicul are protectie la Earth */
        earthProtection = new JLabel("Earth Spell Protection: " + enemy.earthProtection);
        earthProtection.setForeground(Color.orange);
        earthProtection.setHorizontalAlignment(JLabel.CENTER);
        /* Adaugare label-urilor in panel */
        enemyPanel.add(enemyLife); enemyPanel.add(enemyMana);
        enemyPanel.add(fireProtection); enemyPanel.add(iceProtection); enemyPanel.add(earthProtection);
        /* Adaugare panel la background */
        background.add(enemyPanel);

        /* Setare label ce va afisa o imagine(logo-ul jocului) */
        logo = new JLabel();
        logo.setBounds(290, 0, 400, 200);
        logo.setIcon(getPicture(new File("wow.png"), logo.getWidth(), logo.getHeight()));
        background.add(logo);

        /* Pentru a afisa continutul inventarului eroului sub forma unui meniu
        * am folosit un panel in care voi pune un label ce va afisa numele meniului
        * (INVENTORY) si un panel in care voi pune potiunile */
        inventoryPanel = new JPanel();
        inventoryPanel.setBounds(320, 490, 235, 60);
        inventoryPanel.setLayout(new GridLayout(2, 1));
        /* Setare label pentru titlul meniului */
        inventoryLabel = new JLabel("INVENTORY");
        inventoryLabel.setOpaque(true);
        inventoryLabel.setBackground(new Color(77, 59, 0));
        inventoryLabel.setForeground(new Color(255, 196,0));
        inventoryLabel.setHorizontalAlignment(JLabel.CENTER);
        inventoryPanel.add(inventoryLabel);
        /* Setare panel pentru potiuni in care voi adauga 2 butoane
        * (corespondente celor 2 tipuri de potiuni). Fiecare buton va afisa
        * cate potiuni de tipul respectiv mai sunt in inventar. Apasarea butonului duce
        * la consumarea unei potiuni
        * Ambele butoane au ascultator */
        potionsMenuPanel = new JPanel();
        potionsMenuPanel.setLayout(new GridLayout(1, 2));
        /* Setare buton pentru potiunea de viata */
        healthPotionButton = new JButton("(" + noOfHealthPotions + ")");
        healthPotionButton.setBackground(new Color(26,20,0));
        healthPotionButton.setForeground(new Color(255, 196,0));
        healthPotionButton.setFocusable(false);
        healthPotionButton.setIcon(getPicture(new File("healthIcon.png"), 20, 30));
        /* Daca nu exista potiuni de viata in inventar, butonul devine inactiv */
        if(noOfHealthPotions == 0)
            healthPotionButton.setEnabled(false);
        healthPotionButton.addActionListener(this);
        /* Setare buton pentru potiunea de mana */
        manaPotionButton = new JButton("(" + noOfManaPotions + ")");
        manaPotionButton.setBackground(new Color(26,20,0));
        manaPotionButton.setForeground(new Color(255, 196,0));
        manaPotionButton.setFocusable(false);
        manaPotionButton.setIcon(getPicture(new File("manaIcon.png"), 30, 30));
        /* Daca nu exista potiuni de viata in inventar, butonul devine inactiv */
        if(noOfManaPotions == 0)
            manaPotionButton.setEnabled(false);
        manaPotionButton.addActionListener(this);
        /* Adaugare butoanelor in panelul pentru potiuni */
        potionsMenuPanel.add(healthPotionButton); potionsMenuPanel.add(manaPotionButton);
        inventoryPanel.add(potionsMenuPanel);
        background.add(inventoryPanel);

        /* Pentru a afisa spell-uri de care dispune eroul sub forma unui meniu
         * am folosit un panel in care voi pune un label ce va afisa numele meniului
         * (SPELLS) si un panel pentru butoanele corespondente spell-urilor */
        spellMenuPanel = new JPanel();
        spellMenuPanel.setBounds(560, 490, 235, 60);
        spellMenuPanel.setLayout(new GridLayout(2, 1));
        /* Setare label pentru titlul meniului */
        JLabel spellMenu = new JLabel("SPELLS");
        spellMenu.setOpaque(true);
        spellMenu.setBackground(new Color(77, 59, 0));
        spellMenu.setForeground(new Color(255, 196,0));
        spellMenu.setHorizontalAlignment(JLabel.CENTER);
        spellMenuPanel.add(spellMenu);
        /* Setare panel pentru spell-uri in care voi adauga 3 butoane
         * (corespondente celor 3 tipuri de spell-uri). Fiecare buton va afisa
         * cate spell-uri de tipul respectiv mai sunt disponibile. Apasarea butonului duce
         * la utilizarea unui spell
         * Cele 3 butoane au ascultator */
        spellsPanel = new JPanel();
        spellsPanel.setLayout(new GridLayout(1, 3));

        /* Setare buton pentru spell-ul Fire */
        fireButton = new JButton("(" + noOfFireSpells + ")");
        fireButton.setBackground(new Color(26,20,0));
        fireButton.setForeground(new Color(255, 196,0));
        fireButton.setFocusable(false);
        fireButton.setIcon(getPicture(new File("fireIcon.png"), 20, 20));
        /* Daca personajul nu are spell-uri de tip Fire, butonul devine inactiv */
        if(noOfFireSpells == 0)
            fireButton.setEnabled(false);
        fireButton.addActionListener(this);

        /* Setare buton pentru spell-ul Ice */
        iceButton = new JButton("(" + noOfIceSpells + ")");
        iceButton.setBackground(new Color(26,20,0));
        iceButton.setForeground(new Color(255, 196,0));
        iceButton.setFocusable(false);
        iceButton.setIcon(getPicture(new File("iceIcon.png"), 20, 20));
        /* Daca personajul nu are spell-uri de tip Ice, butonul devine inactiv */
        if(noOfIceSpells == 0)
            iceButton.setEnabled(false);
        iceButton.addActionListener(this);

        /* Setare buton pentru spell-ul Earth */
        earthButton = new JButton("(" + noOfEarthSpells + ")");
        earthButton.setBackground(new Color(26,20,0));
        earthButton.setForeground(new Color(255, 196,0));
        earthButton.setFocusable(false);
        earthButton.setIcon(getPicture(new File("earthIcon.png"), 25, 20));
        /* Daca personajul nu are spell-uri de tip Earth, butonul devine inactiv */
        if(noOfEarthSpells == 0)
            earthButton.setEnabled(false);
        earthButton.addActionListener(this);
        /* Adaugarea butoanelor la panelul pentru spell-uri */
        spellsPanel.add(fireButton);spellsPanel.add(iceButton);
        spellsPanel.add(earthButton);
        /* Adaugare panel pentru spell-uri la panelul meniului */
        spellMenuPanel.add(spellsPanel);
        background.add(spellMenuPanel);

        /* Setare buton pentru atac normal
        * Butonul are ascultator */
        attackButton = new JButton("Normal Attack");
        attackButton.setBackground(new Color(26,20,0));
        attackButton.setForeground(Color.red);
        attackButton.setBounds(165, 490,150,  60);
        attackButton.setFocusable(false);
        attackButton.addActionListener(this);
        background.add(attackButton);

        /* Setare buton ce afiseaza statusul bataliei
        * Butonul are ascultator si este invizibil pana la finalul
        * bataliei */
        victoryStatus = new JButton();
        victoryStatus.setBounds(490, 300, 100, 30);
        victoryStatus.setBackground(new Color(26,20,0));
        victoryStatus.setForeground(Color.red);
        victoryStatus.setFocusable(false);
        background.add(victoryStatus);
        victoryStatus.setVisible(false);
        victoryStatus.addActionListener(this);

        show();
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /* Daca jucatorul apasa butonul victoryStatus, fereastra se inchide */
        if(e.getSource().equals(victoryStatus))
            dispose();

        /* Daca jucatorul apasa butonul de atac normal, enemy primeste damage conform
        * metodei receiveDamage si se actualizeaza labeulul ce afiseaza viata ianmicului */
        if(e.getSource().equals(attackButton)){
            enemy.receiveDamage(hero.getDamage());
            enemyLife.setText("Life: " + enemy.currentRemainingLife + " / " + enemy.maxLife);
        }
        /* Daca utilizatorul apasa unul din cele 3 butoane pentru atac cu spell, eroul
        * foloseste abilitatea asupra inamicului. */
        else if(e.getSource().equals(fireButton)){
            for(int i = 0 ; i < hero.abilities.size() ; i++)
                if(hero.abilities.get(i).getClass().getName().equals("Fire")){
                    /* Se verifica daca eroul are suficienta mana ca sa foloseasca spell-ul*/
                    if(hero.useSpell(hero.abilities.get(i), enemy) == true){
                        noOfFireSpells--;
                        fireButton.setText("(" + noOfFireSpells + ")");
                        hero.abilities.remove(i);
                        heroMana.setText("Mana: " + hero.currentRemainingMana + " / " + hero.maxMana);
                        enemyLife.setText("Life: " + enemy.currentRemainingLife + " / " + enemy.maxLife);
                    }
                    /* Daca nu are suficienta mana, va ataca normal */
                    else {
                        enemy.receiveDamage(hero.getDamage());
                        enemyLife.setText("Life: " + enemy.currentRemainingLife + " / " + enemy.maxLife);
                    }
                }
            /*Daca dupa utilizarea unui spell, nu mai
            * exista spell-uri de acel tip, butonul devine inactiv */
            if(noOfFireSpells == 0)
                fireButton.setEnabled(false);
        }
        else if(e.getSource().equals(iceButton)){
            for(int i = 0 ; i < hero.abilities.size() ; i++)
                if(hero.abilities.get(i).getClass().getName().equals("Ice")){
                    /* Se verifica daca eroul are suficienta mana ca sa foloseasca spell-ul*/
                    if(hero.useSpell(hero.abilities.get(i), enemy) == true){
                        noOfIceSpells--;
                        iceButton.setText("(" + noOfIceSpells + ")");
                        hero.abilities.remove(i);
                        heroMana.setText("Mana: " + hero.currentRemainingMana + " / " + hero.maxMana);
                        enemyLife.setText("Life: " + enemy.currentRemainingLife + " / " + enemy.maxLife);
                    }
                    /* Daca nu are suficienta mana, va ataca normal */
                    else {
                        enemy.receiveDamage(hero.getDamage());
                        enemyLife.setText("Life: " + enemy.currentRemainingLife + " / " + enemy.maxLife);
                    }
                }
            /*Daca dupa utilizarea unui spell, nu mai
             * exista spell-uri de acel tip, butonul devine inactiv */
            if(noOfIceSpells == 0)
                iceButton.setEnabled(false);
        }
        else if(e.getSource().equals(earthButton)){
            for(int i = 0 ; i < hero.abilities.size() ; i++)
                if(hero.abilities.get(i).getClass().getName().equals("Earth")){
                    /* Se verifica daca eroul are suficienta mana ca sa foloseasca spell-ul*/
                    if(hero.useSpell(hero.abilities.get(i), enemy) == true){
                        noOfEarthSpells--;
                        earthButton.setText("(" + noOfEarthSpells + ")");
                        hero.abilities.remove(i);
                        heroMana.setText("Mana: " + hero.currentRemainingMana + " / " + hero.maxMana);
                        enemyLife.setText("Life: " + enemy.currentRemainingLife + " / " + enemy.maxLife);
                    }
                    /* Daca nu are suficienta mana, va ataca normal */
                    else {
                        enemy.receiveDamage(hero.getDamage());
                        enemyLife.setText("Life: " + enemy.currentRemainingLife + " / " + enemy.maxLife);
                    }
                }
            /* Daca dupa utilizarea unui spell, nu mai
             * exista spell-uri de acel tip, butonul devine inactiv */
            if(noOfEarthSpells == 0)
                earthButton.setEnabled(false);
        }
        /* Daca utilizatorul apasa unul din cele doua butoane, acesta va folosi potiunea si se
        * va actualiza numarul potiunilor de acel tip si caracteristica modificata in urma potiunii
        * (viata sau mana) */
        else if(e.getSource().equals(healthPotionButton)){
            /* Se cauta in lista de potiuni o potiune de tip HealthPotion */
            for(int i = 0 ; i < hero.inventory.ownedPotions.size() ; i++){
                /* Daca se gaseste potiune, se scade numarul de potiuni si se actualizeaza
                * labelul ce afiseaza viata eroului */
                if(hero.inventory.ownedPotions.get(i).getClass().getName().equals("HealthPotion")){
                    noOfHealthPotions--;
                    healthPotionButton.setText("(" + noOfHealthPotions + ")");
                    heroLife.setText("Life: " + hero.currentRemainingLife + " / " + hero.maxLife);
                    /* Folosire potiune */
                    hero.inventory.ownedPotions.get(i).usePotion(hero);
                    /* Eliminare potiune din lista */
                    hero.inventory.removePotion(hero.inventory.ownedPotions.get(i));
                }
            }
            /* Daca eroul ramane fara potiuni, butonul devine inactiv */
            if(noOfHealthPotions == 0)
                healthPotionButton.setEnabled(false);
        }
        else if(e.getSource().equals(manaPotionButton)){
            /* Se cauta in lista de potiuni o potiune de tip HealthPotion */
            for(int i = 0 ; i < hero.inventory.ownedPotions.size() ; i++){
                /* Daca se gaseste potiune, se scade numarul de potiuni si se actualizeaza
                 * labelul ce afiseaza viata eroului */
                if(hero.inventory.ownedPotions.get(i).getClass().getName().equals("ManaPotion")){
                    noOfManaPotions--;
                    manaPotionButton.setText("(" + noOfManaPotions + ")");
                    heroMana.setText("Mana: " + hero.currentRemainingMana + " / " + hero.maxMana);
                    /* Folosire potiune */
                    hero.inventory.ownedPotions.get(i).usePotion(hero);
                    /* Eliminare potiune din lista */
                    hero.inventory.removePotion(hero.inventory.ownedPotions.get(i));
                }
            }
            /* Daca dupa utilizarea unui spell, nu mai
             * exista spell-uri de acel tip, butonul devine inactiv */
            if(noOfManaPotions == 0)
                manaPotionButton.setEnabled(false);
        }
        /* Daca hero si enemy inca mai au viata, dupa atacul eroului ataca inamicul */
        if(hero.currentRemainingLife != 0 && enemy.currentRemainingLife != 0)
            enemyAttack();
        /* Altfel se verifica cine a ramas fara viata si se afiseaza butonul
        * victoryStatus */
        else{
            if(hero.currentRemainingLife > 0)
                victoryStatus.setText("YOU WON");
            else
                victoryStatus.setText("YOU LOST");
            victoryStatus.setVisible(true);
        }
    }

    /* Metoda enemyAttack modeleaza atacul inamicului */
    public void enemyAttack(){
        Random random = new Random();
        int chance = random.nextInt(4);
        /* Sansa de 25% de atac cu spell */
        if (chance == 1) {
            if (0 < enemy.abilities.size()) {
                /* Se verifica daca inamicul are mana suficienta pentru a utiliza
                * un spell */
                if (enemy.useSpell(enemy.abilities.get(0), hero) == true) {
                    /* Eliminare spell din lista de spell-uri a inamicului */
                    enemy.abilities.remove(0);
                    enemyMana.setText("Mana: " + enemy.currentRemainingMana + " / " + enemy.maxMana);
                }
                /* Daca nu are, ataca normal */
                else hero.receiveDamage(enemy.getDamage());
            }
        }
        /* Sansa de 75% atac normal */
        else {
            hero.receiveDamage(enemy.getDamage());
        }
        /* Actualizare label ce afiseaza viata eroului */
        heroLife.setText("Life: " + hero.currentRemainingLife + " / " + hero.maxLife);
    }

    public ImageIcon getPicture(File file, int width, int height) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image image = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
