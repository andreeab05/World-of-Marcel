import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShopWindow extends JFrame implements ActionListener {
    Shop shop;
    Character currentCharacter;
    int potionIndex;
    JLabel background;
    JLabel shopPrompt, potionName, coinsLabel, coins;
    JButton buyButton, cancelButton, next, prev;
    JLabel potionImage;
    JPanel currentCoinsPanel;
    JFrame buyStatus;

    public ShopWindow(Shop shop, Character character){
        this.shop = shop;
        currentCharacter = character;
        potionIndex = 0;
        /* Setare dimeniuni fereastra. Nu am folosit un layout
         * pentru a putea controla pozitia elementelor de fereastra.
         * Fereastra nu este redimensionabila */
        setMinimumSize(new Dimension(412, 606));
        setResizable(false);
        setLayout(null);

        /* Setare imagine de background */
        background = new JLabel();
        background.setBounds(0,0, getWidth(), getHeight());
        background.setLayout(null);
        background.setIcon(getPicture(new File("shopBackground.jpg"), background.getWidth(), background.getHeight()));
        add(background);

        /* Setare label cu prompt */
        shopPrompt = new JLabel(" What potions would you like today?(" + shop.potions.size() + " left)");
        shopPrompt.setBounds(80, 100, 250, 50);
        shopPrompt.setHorizontalAlignment(JLabel.CENTER);
        shopPrompt.setOpaque(true);
        shopPrompt.setBackground(new Color(0, 0, 0));
        shopPrompt.setForeground(new Color(255, 204, 0));
        background.add(shopPrompt);

        /* Setare label ce afiseaza numele potiunii
        * Se afiseaza prima potiune din lista de potiuni */
        potionName = new JLabel(shop.potions.get(potionIndex).getClass().getName());
        potionName.setBounds(150, 460, 100, 30);
        potionName.setHorizontalAlignment(JLabel.CENTER);
        potionName.setOpaque(true);
        potionName.setBackground(new Color(0, 0, 0));
        potionName.setForeground(new Color(255, 204, 0));
        background.add(potionName);


        /* Setarea imaginii potiunii curente */
        potionImage = new JLabel();
        potionImage.setBounds(100, 200, 256,256);
        if(shop.potions.get(potionIndex).getClass().getName().equals("HealthPotion"))
            potionImage.setIcon(getPicture(new File("health_potion.png"), potionImage.getWidth(), potionImage.getHeight()));
        else if(shop.potions.get(potionIndex).getClass().getName().equals("ManaPotion"))
            potionImage.setIcon(getPicture(new File("mana_potion.png"), potionImage.getWidth(), potionImage.getHeight()));
        background.add(potionImage);

        /* Setare butoane next si prev
        * Ambele butoane au ascultator */
        next = new BasicArrowButton(BasicArrowButton.EAST);
        next.setBounds(330, 300, 50, 30);
        next.setBackground(new Color(90, 0, 0));
        next.setForeground(new Color(255, 204, 0));
        next.addActionListener(this);
        background.add(next);

        prev = new BasicArrowButton(BasicArrowButton.WEST);
        prev.setBounds(20, 300, 50, 30);
        prev.setBackground(new Color(90, 0, 0));
        prev.setForeground(new Color(255, 204, 0));
        prev.addActionListener(this);
        background.add(prev);

        /* Setare butoane buy si cancel
        * Ambele butoane au ascultator */
        buyButton = new JButton("BUY (" + shop.potions.get(potionIndex).getPrice() + ")");
        buyButton.setBounds(280, 500, 100, 50);
        buyButton.setBackground(new Color(90, 0, 0));
        buyButton.setForeground(new Color(255, 204, 0));
        buyButton.setFocusable(false);
        buyButton.addActionListener(this);
        background.add(buyButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(20, 500, 80, 50);
        cancelButton.setBackground(new Color(90, 0, 0));
        cancelButton.setForeground(new Color(255, 204, 0));
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(this);
        background.add(cancelButton);

        /* Pentru caseta care afiseaza numarul de monede am folosit
        * un panel ce va contine 2 labeluri(unul cu un icon pentru monede
        * si unul ce afiseaza numarul de monede) */
        currentCoinsPanel = new JPanel();
        currentCoinsPanel.setLayout(new GridLayout(1, 2));
        currentCoinsPanel.setBounds(165, 500, 70, 50);
        coinsLabel = new JLabel();
        coinsLabel.setBackground(new Color(60, 0, 0));
        coinsLabel.setOpaque(true);
        coinsLabel.setIcon(getPicture(new File("coin.png"), currentCoinsPanel.getWidth()/2, currentCoinsPanel.getHeight()/2));
        coins = new JLabel(String.valueOf(currentCharacter.inventory.coins));
        coins.setOpaque(true);
        coins.setBackground(new Color(60, 0, 0));
        coins.setForeground(new Color(255, 204, 0));
        currentCoinsPanel.add(coinsLabel);
        currentCoinsPanel.add(coins);
        background.add(currentCoinsPanel);

        show();
        pack();
    }

    /* Metoda actionPerformed verifica care este sursa
    * care a produs evenimentul(ce buton a fost apasat) */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(buyStatus != null)
            if(buyStatus.isVisible()) {
                buyStatus.dispose();
            }
        /* Daca se apasa butonul next se afiseaza urmatoarea potiune din
        * lista de potiuni a magazinului */
        if(e.getSource() == next){
            showNextPotion();
        }
        /* Daca se apasa butonul prev se afiseaza potiunea anterioara din
         * lista de potiuni a magazinului */
        else if(e.getSource() == prev){
            showPrevPotion();
        }
        /* Daca se apasa butonul buyButton se afiseaza o fereastra buyStatus ce anunta
        * daca s-a putut cumpara potiunea sau nu. Daca se poate cumpara, potiunea se scoate
        * din lista magazinului si se adauga in inventarul personajului */
        else if(e.getSource() == buyButton){
            buyStatus = new JFrame();
            buyStatus.setMinimumSize(new Dimension(200, 100));
            if(currentCharacter.buyPotion(shop.potions.get(potionIndex)) == 1) {
                /* Adaugare potiune in inventarul personajului */
                currentCharacter.inventory.addPotion(shop.buyPotion(potionIndex));
                buyStatus.add(new JLabel("Potion purchased!"));
                buyStatus.show();
                if(shop.potions.size() > 0)
                    showNextPotion();
                else setVisible(false);
                /* Actualizare prompt pentru potiuni(se modifica numarul potiunilor ramase) */
                String newPrompt = "What potions would you like today?(" + shop.potions.size() + " left)";
                shopPrompt.setText(newPrompt);
                /* Actualizare numar de monede ramase */
                coins.setText(String.valueOf(currentCharacter.inventory.coins));
            }
            else {
                buyStatus.add(new JLabel("Not enough money traveller! :("));
                buyStatus.show();
            }
        }
        /* Daca se apasa butonul cancelButton, fereastra se inchide */
        else if(e.getSource() == cancelButton)
            setVisible(false);
    }

    /* Metoda showNextPotion afiseaza urmatoarea potiune din lista de potiuni*/
    public void showNextPotion(){
        potionIndex++;
        /* Daca indexul potiunii se afla la finalul listei, indexul
         * devine 0 pentru a se lua lista de potiuni de la capat */
        if(potionIndex >= shop.potions.size())
            potionIndex = 0;
        /* Actualizarea imaginii potiunii si a labelului care afiseaza
         * numele potiunii */
        Potion potion = shop.potions.get(potionIndex);
        potionName.setText(potion.getClass().getName());
        if (potion.getClass().getName().equals("HealthPotion"))
            potionImage.setIcon(getPicture(new File("health_potion.png"), potionImage.getWidth(), potionImage.getHeight()));
        else if (potion.getClass().getName().equals("ManaPotion"))
            potionImage.setIcon(getPicture(new File("mana_potion.png"), potionImage.getWidth(), potionImage.getHeight()));
    }

    /* Metoda showPrevPotion afiseaza potiunea anterioara din lista de potiuni*/
    public void showPrevPotion(){
        potionIndex--;
        /* Daca indexul potiunii se afla la inceputul listei, indexul
         * devine shop.potions.size() - 1 (care este pozitia ultimei potiuni
         * din lista) pentru a se lua lista de potiuni de la coada */
        if(potionIndex < 0)
            potionIndex = shop.potions.size() - 1;

        /* Actualizarea imaginii potiunii si a labelului care afiseaza
        * numele potiunii */
        Potion potion = shop.potions.get(potionIndex);
        potionName.setText(potion.getClass().getName());
        if (potion.getClass().getName().equals("HealthPotion"))
            potionImage.setIcon(getPicture(new File("health_potion.png"), potionImage.getWidth(), potionImage.getHeight()));
        else if (potion.getClass().getName().equals("ManaPotion"))
            potionImage.setIcon(getPicture(new File("mana_potion.png"), potionImage.getWidth(), potionImage.getHeight()));
        buyButton = new JButton("BUY (" + shop.potions.get(potionIndex).getPrice() + ")");
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
