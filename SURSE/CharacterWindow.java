import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CharacterWindow extends JFrame implements ActionListener {
    Account account;
    Game game;
    /* Variabila ce retine pozitia in lista a ultimului caracter afisat */
    int indexOfCharacter;
    JButton next, prev, play;
    JTextField name, profession, level, experience;
    JLabel nameLabel, professionLabel, levelLabel, experienceLabel;
    JLabel background, characterPhoto, prompt;

    public CharacterWindow(Account account, Game game){
        this.account = account;
        this.game = game;
        indexOfCharacter = 0;
        /* Setare dimeniuni fereastra. Nu am folosit un layout
         * pentru a putea controla pozitia elementelor de fereastra.
         * Fereastra nu se poate redimensiona */
        setMinimumSize(new Dimension(728, 410));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        /* Setare imagine de background */
        background = new JLabel();
        background.setBounds(0,0, getWidth(), getHeight());
        background.setLayout(null);
        background.setIcon(getPicture(new File("chooseCharacter.jpg"), background.getWidth(), background.getHeight()));

        /* Setare prompt */
        prompt = new JLabel();
        prompt.setBounds(140, 15, 407, 51);
        prompt.setIcon(getPicture(new File("choose1.png"), prompt.getWidth(), prompt.getHeight()));
        background.add(prompt);

        Character character = this.account.characters.get(indexOfCharacter);

        /* Setare panel ce modeleaza caseta in care apar datele despre personajul curent */
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.setBounds(30,90, 250, 150);
        panel.setBackground(new Color(96,32,96, 40));

        /* Setare label si text field pentru afisarea numelui personajului curent */
        nameLabel = new JLabel("Character Name");
        nameLabel.setForeground(new Color(249,236,249));
        name = new JTextField(30);
        name.setText(character.name);
        name.setHorizontalAlignment(JTextField.CENTER);
        name.setBackground(new Color(38,77,0));
        name.setForeground(new Color(242,255,230));

        /* Setare label si text field pentru afisarea profesiei personajului curent */
        professionLabel = new JLabel("Profession");
        professionLabel.setForeground(new Color(249,236,249));
        profession = new JTextField(10);
        profession.setHorizontalAlignment(JTextField.CENTER);
        profession.setText(character.getClass().getName());
        profession.setBackground(new Color(38,77,0));
        profession.setForeground(new Color(242,255,230));

        /* Setare label si text field pentru afisarea nivelului personajului curent */
        levelLabel = new JLabel("Level");
        levelLabel.setForeground(new Color(249,236,249));
        level = new JTextField(3);
        level.setHorizontalAlignment(JTextField.CENTER);
        level.setText(String.valueOf(character.level));
        level.setBackground(new Color(38,77,0));
        level.setForeground(new Color(242,255,230));

        /* Setare label si text field pentru afisarea experientei personajului curent */
        experienceLabel = new JLabel("EXP");
        experienceLabel.setForeground(new Color(249,236,249));
        experience = new JTextField(3);
        experience.setHorizontalAlignment(JTextField.CENTER);
        experience.setText(String.valueOf(character.currentEXP));
        experience.setBackground(new Color(38,77,0));
        experience.setForeground(new Color(242,255,230));

        /* Adaugarea componentelor in panel */
        panel.add(nameLabel); panel.add(name);
        panel.add(professionLabel); panel.add(profession);
        panel.add(levelLabel); panel.add(level);
        panel.add(experienceLabel); panel.add(experience);

        background.add(panel);

        /* Setare label ce afiseaza imaginea personajului curent */
        characterPhoto = new JLabel();
        characterPhoto.setBounds(400, 40, 220, 305);

        if(character.getClass().getName().equals("Rogue"))
            characterPhoto.setIcon(getPicture(new File("Rogue.png"), characterPhoto.getWidth(), characterPhoto.getHeight()));
        else if(character.getClass().getName().equals("Mage"))
            characterPhoto.setIcon(getPicture(new File("Mage.png"), characterPhoto.getWidth(), characterPhoto.getHeight()));
        else if(character.getClass().getName().equals("Warrior"))
            characterPhoto.setIcon(getPicture(new File("Warrior.png"), characterPhoto.getWidth(), characterPhoto.getHeight()));

        background.add(characterPhoto);

        /* Am adaugat un ActionListener pe toate butoanele enumerate mai jos */
        /* Setare buton next*/
        next = new JButton("Next");
        next.setBounds(600,320, 70, 30);
        next.setBackground(new Color(98,30,98));
        next.setForeground(new Color(249,236,249));
        next.setFocusable(false);
        background.add(next);
        next.addActionListener(this);

        /* Setare buton previuos */
        prev = new JButton("Prev");
        prev.setBounds(40, 320, 70, 30);
        prev.setBackground(new Color(98,30,98));
        prev.setForeground(new Color(249,236,249));
        prev.setFocusable(false);
        background.add(prev);
        prev.addActionListener(this);

        /* Setare buton de play */
        play = new JButton("PLAY");
        play.setBounds(314, 320, 100, 40);
        play.setBackground(new Color(51,0,51));
        play.setForeground(new Color(249,236,249));
        play.setFocusable(false);
        background.add(play);
        play.addActionListener(this);

        add(background);
        show();
        pack();
    }

    /* Metoda actionPerformed verifica care buton este sursa evenimentului */
    @Override
    public void actionPerformed(ActionEvent e) {
        /* Daca se apasa butonul next, se apeleaza metoda showNextCharacter
        * ce afiseaza urmatorul personaj din lista */
        if(e.getSource() == next){
            showNextCharacter();
        }
        /* Daca se apasa butonul prev, se apeleaza metoda showPrevCharacter
         * ce afiseaza personajul anterior din lista de personaje*/
        else if(e.getSource() == prev){
            showPrevCharacter();
        }
        /* Daca se apasa play, se porneste jocul cu ultimul personaj afisat */
        else if(e.getSource() == play){
            MainWindow gamePage = new MainWindow(account.characters.get(indexOfCharacter), game);
            dispose();
        }
    }

    /* Metoda showNextCharacter afiseaza pe ecran personajul urmator din lista
    * de personaje a contului ales */
    public void showNextCharacter(){
        indexOfCharacter++;
        /* Daca indexul personajului se afla la finalul listei, indexul
        * devine 0 pentru a se lua lista de personaje de la capat */
        if(indexOfCharacter >= account.characters.size())
            indexOfCharacter = 0;

        /* Se retine caracterul curent afisat */
        Character character = account.characters.get(indexOfCharacter);
        /* Se actualizeaza campurile ce afiseaza datele despre personaj */
        name.setText(character.name);
        profession.setText(character.getClass().getName());
        level.setText(String.valueOf(character.level));
        experience.setText(String.valueOf(character.currentEXP));
        /* Se actualizeaza imaginea cu personajul */
        if (character.getClass().getName().equals("Rogue"))
            characterPhoto.setIcon(getPicture(new File("Rogue.png"), characterPhoto.getWidth(), characterPhoto.getHeight()));
        else if (character.getClass().getName().equals("Mage"))
            characterPhoto.setIcon(getPicture(new File("Mage.png"), characterPhoto.getWidth(), characterPhoto.getHeight()));
        else if (character.getClass().getName().equals("Warrior"))
            characterPhoto.setIcon(getPicture(new File("Warrior.png"), characterPhoto.getWidth(), characterPhoto.getHeight()));
    }

    /* Metoda showPrevCharacter afiseaza pe ecran personajul anterior din lista
     * de personaje a contului ales */
    public void showPrevCharacter(){
        indexOfCharacter--;
        /* Daca indexul personajului se afla la inceputul listei, indexul
         * devine account.characters.size() - 1 (care este pozitia ultimului
         *  personaj din lista) pentru a se lua lista de personaje de la coada */
        if(indexOfCharacter < 0)
            indexOfCharacter = account.characters.size() - 1;

        /* Se retine caracterul curent afisat */
        Character character = account.characters.get(indexOfCharacter);
        /* Se actualizeaza campurile ce afiseaza datele despre personaj */
        name.setText(character.name);
        profession.setText(character.getClass().getName());
        level.setText(String.valueOf(character.level));
        experience.setText(String.valueOf(character.currentEXP));
        /* Se actualizeaza imaginea cu personajul */
        if (character.getClass().getName().equals("Rogue"))
            characterPhoto.setIcon(getPicture(new File("Rogue.png"), characterPhoto.getWidth(), characterPhoto.getHeight()));
        else if (character.getClass().getName().equals("Mage"))
            characterPhoto.setIcon(getPicture(new File("Mage.png"), characterPhoto.getWidth(), characterPhoto.getHeight()));
        else if (character.getClass().getName().equals("Warrior"))
            characterPhoto.setIcon(getPicture(new File("Warrior.png"), characterPhoto.getWidth(), characterPhoto.getHeight()));
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