import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MainWindow extends JFrame implements ActionListener {
    Grid grid;
    Game game;
    Character character;
    JTextArea story;
    JPanel map, narratorMenu, characterDetails;
    JButton north, south, east, west, start, exit;
    JLabel narratorPhoto, background;
    JLabel narratorSpeech, currentCoins, coinsImage, currentLife, lifeImage;
    /* Folosesc o matrice de butoane pentru a ascunde celulele nevizitate */
    JButton[][] buttons;
    /* Folosesc o matrice de label-uri pentru a modela celulele */
    JLabel[][] icons;
    //JTextField narratorSpeech;
    int moneyBagIndicator = 0, gainedExp = 0, newLevel, battlesWon = 0, coins = 0;

    public MainWindow(Character character, Game game){
        this.character = character;
        this.game = game;
        newLevel = character.level;
        /* Setare dimeniuni fereastra. Nu am folosit un layout
         * pentru a putea controla pozitia elementelor de fereastra.
         * Fereastra nu este redimensionabila */
        setMinimumSize(new Dimension(960, 540));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        /* Generare de harta random (dimensiunea maxima a hartii este 7x7)
        * Folosesc metoda de generare createMap din clasa Grid */
        int width = new Random().nextInt(5, 8);
        int height = new Random().nextInt(5, 8);
        grid = Grid.createMap(width, height, character);
        this.game = game;

        /* Setez imaginea de background */
        background = new JLabel();
        background.setBounds(0,0, getWidth(), getHeight());
        background.setLayout(null);
        background.setIcon(getPicture(new File("gamebackground.jpg"), background.getWidth(), background.getHeight()));
        add(background);

        /* Setez imaginea personajului narator */
        narratorPhoto = new JLabel();
        narratorPhoto.setBounds(0, 100, 350,400);
        narratorPhoto.setIcon(getPicture(new File("Narrator.png"), narratorPhoto.getWidth(), narratorPhoto.getHeight()));
        background.add(narratorPhoto);

        /* Setare panel ce modeleaza meniul afisat de catre personajul
        * narator */
        narratorMenu = new JPanel();
        narratorMenu.setLayout(null);
        narratorMenu.setBackground(new Color(26,20,0));
        narratorMenu.setBounds(200, 50, 300, 130);
        /* Setare label ce afiseaza indicatiile naratorului */
        narratorSpeech = new JLabel("Hello again, mighty hero! Let's start your journey...");
        narratorSpeech.setBounds(0, 0, 300, 20);
        narratorSpeech.setForeground(new Color(255, 224,102));
        narratorMenu.add(narratorSpeech);

        /* Setare buton de start */
        start = new JButton("START");
        start.setFocusable(false);
        start.setBackground(Color.black);
        //y=25
        start.setBounds(75, 25, 150, 20);
        start.setForeground(new Color(255, 224,102));
        start.addActionListener(this);
        narratorMenu.add(start);

        /* Setare butoane pentru deplasarea pe harta
        * Toate butoanele vor avea un ActionListener */
        /* Buton pentru deplasare in sus */
        north = new JButton("Go North");
        north.setIcon(getPicture(new File("arrow-up.png"), 24, 24));
        north.setFocusable(false);
        north.setBackground(Color.black);
        north.setForeground(new Color(255, 224,102));
        north.setBounds(75, 25, 150, 20);
        north.addActionListener(this);
        north.setVisible(false);

        /* Buton pentru deplasare in jos */
        south = new JButton("Go South");
        south.setIcon(getPicture(new File("arrow-down.png"), 24, 24));
        south.setFocusable(false);
        south.setBackground(Color.black);
        south.setForeground(new Color(255, 224,102));
        south.setBounds(75, 45, 150, 20);
        south.addActionListener(this);
        south.setVisible(false);

        /* Buton pentru deplasare la dreapta */
        east = new JButton("Go East");
        east.setIcon(getPicture(new File("arrow-right.png"), 24, 24));
        east.setFocusable(false);
        east.setBackground(Color.black);
        east.setForeground(new Color(255, 224,102));
        east.setBounds(75, 65, 150, 20);
        east.addActionListener(this);
        east.setVisible(false);

        /* Buton pentru deplasare la stanga */
        west = new JButton("Go West");
        west.setIcon(getPicture(new File("arrow-left.png"), 24, 24));
        west.setFocusable(false);
        west.setBackground(Color.black);
        west.setForeground(new Color(255, 224,102));
        west.setBounds(75, 85, 150, 20);
        west.addActionListener(this);
        west.setVisible(false);

        /* Buton exit (ce va aparea atunci cand se termina jocul) */
        exit = new JButton("Exit");
        exit.setFocusable(false);
        exit.setBackground(Color.black);
        exit.setForeground(new Color(255, 224,102));
        exit.setBounds(75, 105, 150, 20);
        exit.addActionListener(this);
        exit.setVisible(false);
        narratorMenu.add(north);narratorMenu.add(south);
        narratorMenu.add(west);narratorMenu.add(east);
        narratorMenu.add(exit);
        background.add(narratorMenu);

        /* Pentru modelarea hartii am folosit un panel in care voi adauga matricea
        * de label-uri icons, peste care voi adauga matricea de butoane */
        map = new JPanel();
        map.setLayout(new GridLayout(height, width));
        map.setBounds(400, 200, width * 50, height * 30);
        map.setBackground(new Color(102, 51, 0));
        buttons = new JButton[height][width];
        icons = new JLabel[height][width];
        for (int i = 0 ; i < grid.height ; i++){
            for(int j = 0 ; j < grid.width ; j++){
                icons[i][j] = new JLabel();
                /* Pentru celule de tip EMPTY, se verifica daca se gasesc monede
                * Daca se gasesc, pentru celula respectiva se seteaza un icon corespunzator */
                if(grid.get(i).get(j).type.equals(Cell.CellType.EMPTY))
                    if(new Random().nextInt(5) == 0)
                        icons[i][j].setIcon(getPicture(new File("moneybag.png"), 25, 25));
                icons[i][j].setLayout(new GridLayout(1,1));
                icons[i][j].setHorizontalAlignment(JLabel.CENTER);
                setColor(icons[i][j], i, j);
                icons[i][j].setOpaque(true);
                /* Adaugare celula la panel */
                map.add(icons[i][j]);
                /* Adaugare butoane pe toate celulele in afara de celula
                * de la coordonatele (0, 0) deoarece de acolo porneste jucatorul */
                if(i != 0 || j != 0){
                    buttons[i][j] = new JButton("?");
                    buttons[i][j].setFocusable(false);
                    buttons[i][j].setBackground(new Color(26,20,0));
                    buttons[i][j].setForeground(new Color(255, 196,0));
                    icons[i][j].add(buttons[i][j]);
                }
                /* Setare icon ce marcheaza pozitia jucatorului pe harta */
                else icons[i][i].setIcon(getPicture(new File("knight.png"), 25, 25));
            }
        }
        background.add(map);

        /* Setare text area pentru afisarea povestilor casutelor */
        story = new JTextArea(1,60);
        JScrollPane scrollPane = new JScrollPane(story);
        story.setEditable(false);
        story.setText(game.showStory(grid.currentCell));
        scrollPane.setBounds(350 ,450, 550, 50);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getHorizontalScrollBar().setBackground(new Color(26,20,0));
        story.setBackground(new Color(26,20,0));
        story.setForeground(new Color(255, 196,0));
        background.add(scrollPane);

        /* Setare panel ce va afisa viata curenta si monedele jucatorului  */
        characterDetails = new JPanel();
        characterDetails.setLayout(new GridLayout(1, 4));
        characterDetails.setBounds(340 + map.getWidth() / 2, 205 + map.getHeight(), 120, 30);
        /* Setare imagine pentru icon-ul de la viata */
        lifeImage = new JLabel();
        lifeImage.setOpaque(true);
        lifeImage.setBackground(new Color(26,20,0));
        lifeImage.setIcon(getPicture(new File("heart.png"), characterDetails.getWidth() / 4, characterDetails.getHeight()));
        characterDetails.add(lifeImage);

        /* Setare label ce afiseaza valoare vietii jucatorului */
        currentLife = new JLabel(String.valueOf(character.currentRemainingLife));
        currentLife.setOpaque(true);
        currentLife.setBackground(new Color(26,20,0));
        currentLife.setForeground(new Color(255, 196,0));
        characterDetails.add(currentLife);

        /* Setare imagine pentru icon-ul de la monede */
        coinsImage = new JLabel();
        coinsImage.setOpaque(true);
        coinsImage.setBackground(new Color(26,20,0));
        coinsImage.setIcon(getPicture(new File("coin.png"), characterDetails.getWidth() / 4, characterDetails.getHeight()));
        characterDetails.add(coinsImage);

        /* Setare label ce afiseaza numarul de monede pe care le detine jucatorul */
        currentCoins = new JLabel(String.valueOf(character.inventory.coins));
        currentCoins.setOpaque(true);
        currentCoins.setBackground(new Color(26,20,0));
        currentCoins.setForeground(new Color(255, 196,0));
        characterDetails.add(currentCoins);

        /* Adaugare panel la background */
        background.add(characterDetails);

        show();
        pack();
    }

    /* Metoda ce seteaza culoarea de fundal a unui label in functie de
    * pozitia sa pe harta (se obtine modelul de tabla de sah) */
    public void setColor(JLabel cellLabel, int i, int j){
        if(i % 2 == 0){
            if(j % 2 == 0)
                cellLabel.setBackground(new Color(0, 153, 51));
            else cellLabel.setBackground(new Color(0, 77, 26));
        }
        else {
            if (j % 2 == 0)
                cellLabel.setBackground(new Color(0, 77, 26));
            else
                cellLabel.setBackground(new Color(0, 153, 51));
        }
    }

    /* Metoda ce verifica care sunt directiile in care poate merge jucatorul
    * in functie de pozitia la care se afla pe harta. Daca jucatorul nu se poate
    * deplasa intr-o anumita directie, butonul ce realiza deplasarea in acea directie
    * este facut invizibil. Astfel se afiseaza doar miscarile posibile */
    public void showPossibleMove(Grid grid){
        narratorSpeech.setText("Choose where you want to go! Explore!");
        /* Verifica daca jucatorul se poate deplasa in jos */
        if(grid.currentCell.xCoord < grid.height - 1)
            south.setEnabled(true);
        else south.setEnabled(false);

        /* Verifica daca jucatorul se poate deplasa in sus */
        if(grid.currentCell.xCoord > 0)
            north.setEnabled(true);
        else north.setEnabled(false);

        /* Verifica daca jucatorul se poate deplasa la dreapta */
        if(grid.currentCell.yCoord < grid.width - 1)
            east.setEnabled(true);
        else east.setEnabled(false);

        /* Verifica daca jucatorul se poate deplasa la stanga */
        if(grid.currentCell.yCoord > 0)
            west.setEnabled(true);
        else west.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /* Dupa fiecare actiune, se actualizeaza viata ramasa si monedele
        * jucatorului */
        currentLife.setText(String.valueOf(character.currentRemainingLife));
        currentCoins.setText(String.valueOf(character.inventory.coins));
        /* Daca se apasa butonul exit, fereastra se inchide si se deschide
        * fereastra de final ce afiseaza statusul jocului */
        if(e.getSource().equals(exit)){
            StatusWindow status = new StatusWindow(gainedExp, newLevel, battlesWon, coins);
            dispose();
            //return;
        }
        /* Se verifica mereu daca jucatorul mai are viata*/
        /* Daca jucatorul mai are viata si celula curenta este de tip ENEMY
        * (deci tocmai a invins un inamic) se verifica daca primeste monede
        * si se actualizeaza valoarea experientei(si a nivelului daca este nevoie) */
        if(character.currentRemainingLife > 0) {
            if(grid.currentCell.type.equals(Cell.CellType.ENEMY)) {
                Random random = new Random();
                if (random.nextInt(0, 5) != 0) {
                    character.inventory.coins += 30;
                    coins += 30;
                }
                    if(character.currentEXP + 10 < 50) {
                        character.currentEXP += 10;
                    }
                    else{
                        character.level++;
                        newLevel = character.level;
                        character.currentEXP = 0;
                    }
                    gainedExp += 10;
                    battlesWon++;
            }
            if(grid.currentCell.type.equals(Cell.CellType.FINISH)) {
                narratorSpeech.setText("WELL DONE! YOU WON");
                north.setEnabled(false);
                south.setEnabled(false);
                east.setEnabled(false);
                west.setEnabled(false);
                return;
            }

            /* Daca se apasa butonul start, acesta devine invizibil si
             * se apeleaza metoda showPossibleMove */
            if(e.getSource().equals(start)){
                start.setVisible(false);
                north.setVisible(true);
                south.setVisible(true);
                west.setVisible(true);
                east.setVisible(true);
                showPossibleMove(grid);
            }

            /* Daca se apasa butonul south(deplasare in jos cu o casuta)*/
            if(e.getSource().equals(south)){
                /* Se afiseaza icon-ul de la casuta curenta */
                showIcon(grid.currentCell.xCoord, grid.currentCell.yCoord);
                /* Se face mutarea */
                grid.goSouth();
                /* Se verifica casuta pe care a ajuns jucatorul */
                checkCell(grid.currentCell);
                /* Se seteaza icon-ul celulei curente pentru a marca pozitia jucatorului pe harta */
                icons[grid.currentCell.xCoord][grid.currentCell.yCoord].setIcon(getPicture(new File("knight.png"), 25, 25));
                /* Devine invizibil butonul ce ascundea celula noua, daca celula nu era vizitata */
                if(!grid.get(grid.currentCell.xCoord).get(grid.currentCell.yCoord).isVisited())
                    buttons[grid.currentCell.xCoord][grid.currentCell.yCoord].setVisible(false);
            }

            /* Daca se apasa butonul north(deplasare in sus cu o casuta)*/
            if(e.getSource().equals(north)){
                /* Se afiseaza icon-ul de la casuta curenta */
                showIcon(grid.currentCell.xCoord, grid.currentCell.yCoord);
                /* Se face mutarea */
                grid.goNorth();
                /* Se verifica casuta pe care a ajuns jucatorul */
                checkCell(grid.currentCell);
                /* Se seteaza icon-ul celulei curente pentru a marca pozitia jucatorului pe harta */
                icons[grid.currentCell.xCoord][grid.currentCell.yCoord].setIcon(getPicture(new File("knight.png"), 25, 25));
                /* Devine invizibil butonul ce ascundea celula noua, daca celula nu era vizitata */
                if(!grid.get(grid.currentCell.xCoord).get(grid.currentCell.yCoord).isVisited())
                    buttons[grid.currentCell.xCoord][grid.currentCell.yCoord].setVisible(false);
            }

            /* Daca se apasa butonul east(deplasare la dreapta cu o casuta)*/
            if(e.getSource().equals(east)){
                /* Se afiseaza icon-ul de la casuta curenta */
                showIcon(grid.currentCell.xCoord, grid.currentCell.yCoord);
                /* Se face mutarea */
                grid.goEast();
                /* Se verifica casuta pe care a ajuns jucatorul */
                checkCell(grid.currentCell);
                /* Se seteaza icon-ul celulei curente pentru a marca pozitia jucatorului pe harta */
                icons[grid.currentCell.xCoord][grid.currentCell.yCoord].setIcon(getPicture(new File("knight.png"), 25, 25));
                /* Devine invizibil butonul ce ascundea celula noua, daca celula nu era vizitata */
                if(!grid.get(grid.currentCell.xCoord).get(grid.currentCell.yCoord).isVisited())
                    buttons[grid.currentCell.xCoord][grid.currentCell.yCoord].setVisible(false);
            }
            /* Daca se apasa butonul west(deplasare la stanga cu o casuta)*/
            if(e.getSource().equals(west)){
                /* Se afiseaza icon-ul de la casuta curenta */
                showIcon(grid.currentCell.xCoord, grid.currentCell.yCoord);
                /* Se face mutarea */
                grid.goWest();
                /* Se verifica casuta pe care a ajuns jucatorul */
                checkCell(grid.currentCell);
                /* Se seteaza icon-ul celulei curente pentru a marca pozitia jucatorului pe harta */
                icons[grid.currentCell.xCoord][grid.currentCell.yCoord].setIcon(getPicture(new File("knight.png"), 25, 25));
                /* Devine invizibil butonul ce ascundea celula noua, daca celula nu era vizitata */
                if(!grid.get(grid.currentCell.xCoord).get(grid.currentCell.yCoord).isVisited())
                    buttons[grid.currentCell.xCoord][grid.currentCell.yCoord].setVisible(false);
            }

            /* Daca celula curenta nu este vizitata, se afiseaza povestea pentru
             * celula respectiva */
            if(!grid.currentCell.isVisited() && !e.getSource().equals(start))
                story.setText(game.showStory(grid.currentCell));
            /* Se afiseaza mutarile posibile in functie de noua celula pe care
             * se afla personajul */
            showPossibleMove(grid);
        }
        /* Daca nu mai are viata, se ascund butoanele de deplasare pe harta
        * si se face vizibil butonul de exit */
        else {
            narratorSpeech.setText("Oops! Seems like you're dead!");
            north.setEnabled(false);
            south.setEnabled(false);
            east.setEnabled(false);
            west.setEnabled(false);
            exit.setVisible(true);
            return;
        }
    }

    /* Metoda showIcon primeste coordonatele unei celule si in functie de tipul ei,
    * seteaza icon-ul ce va fi afisat */
    public void showIcon(int i, int j){
        if(grid.get(i).get(j).type.equals(Cell.CellType.ENEMY)) {
            icons[i][j].setIcon(getPicture(new File("deadEnemyIcon.png"), 25, 25));
        }
        else if(grid.get(i).get(j).type.equals(Cell.CellType.SHOP)){
            icons[i][j].setIcon(getPicture(new File("shop.png"), 25, 25));
        }
        else if(grid.get(i).get(j).type.equals(Cell.CellType.EMPTY))
            if(moneyBagIndicator == 1) {
                icons[i][j].setIcon(getPicture(new File("moneybag.png"), 25, 25));
            }
            else icons[i][j].setIcon(null);
    }

    /* Metoda checkCell primeste ca parametru o celula din grid si in
    * functie de tipul ei, deschide fie o fereastra pentru shop(daca
    * celula are tipul SHOP), fie o fereastra de lupta (daca celula are
    * tipul ENEMY) */
    public void checkCell(Cell currentCell){
        /* Daca tipul celulei este SHOP, se deschide o fereastra pentru
        * magazin din care jucatorul isi va cumpara potiuni */
        if(currentCell.type.equals(Cell.CellType.SHOP)){
            Shop shop = (Shop) currentCell.element;
            ShopWindow shopWindow = new ShopWindow(shop, character);
        }

        /* Daca tipul celulei este ENEMY si jucatorul si enemy-ul de pe celula
        * mai au viata, atunci se deschide o fereastra in care jucatorul va incerca
        * sa invinga in lupta inamicul */
        if(currentCell.type.equals(Cell.CellType.ENEMY)){
            BattleWindow battleWindow;
            if(character.currentRemainingLife > 0 && ((Enemy)currentCell.element).currentRemainingLife > 0)
                 battleWindow = new BattleWindow(character, (Enemy) currentCell.element);
        }

        /* Daca tipul celulei este EMPTY si celula are un icon, inseamna ca acolo
        * se gasesc monede deci se actualizeaza si cantitatea de monede pe care o detine
        * personajul, dar si variabila coins care contorizeaza monedele castigate in timpul jocului */
        if(currentCell.type.equals(Cell.CellType.EMPTY)){
            if(icons[currentCell.xCoord][currentCell.yCoord].getIcon() != null) {
                character.inventory.coins += 15;
                coins += 15;
                moneyBagIndicator = 1;
            }
            else moneyBagIndicator = 0;
        }

        /* Daca tipul celulei este FINISH, butoanele de deplasare pe
        * harta devin invizibile si se va adauga la meniul naratorului
        * butonul de exit */
        if(currentCell.type.equals(Cell.CellType.FINISH)){
            exit.setVisible(true);
        }
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
