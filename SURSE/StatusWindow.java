import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StatusWindow extends JFrame  implements ActionListener{
    int gainedEXP, newLevel, battlesWon, coins;
    JPanel status;
    JLabel expLabel, levelLabel, battlesLabel, coinsLabel, background, gameOverLabel, textLabel;
    JButton exitButton;

    public StatusWindow(int gainedEXP, int newLevel, int battlesWon, int coins){
        this.gainedEXP = gainedEXP;
        this.newLevel = newLevel;
        this.battlesWon = battlesWon;
        this.coins = coins;

        /* Setare dimensiuni fereastra. Fereastra nu este redimensionabila
        * si layout-ul este null pentru a avea control total asupra pozitionarii
        * componentelor grafice */
        setMinimumSize(new Dimension(972, 547));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        /* Setare imagine de background */
        background = new JLabel();
        background.setBounds(0,0, getWidth(), getHeight());
        background.setLayout(null);
        background.setIcon(getPicture(new File("final.jpg"), background.getWidth(), background.getHeight()));
        add(background);

        /* Setare label */
        gameOverLabel = new JLabel();
        gameOverLabel.setBounds(150, 50, 665, 129);
        gameOverLabel.setIcon(getPicture(new File("gameover.png"), gameOverLabel.getWidth(), gameOverLabel.getHeight()));
        background.add(gameOverLabel);

        /* Pentru a afisa progresul personajului pe parcursul jocului am folosit
        * un panel in care voi adauga 4 labeluri ce descriu progresul (monedele stranse,
        * numarul inamicilor invinsi, experienta acumulata, nivelul curent) si un label
        * pentru mesaj */
        status = new JPanel();
        status.setLayout(new GridLayout(5,1));
        status.setBounds(300, 185, 400, 125);
        status.setBackground(new Color(32,41,48,230));
        /* Setare label ce afiseaza mesaj */
        textLabel = new JLabel("Until next time...");
        textLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        textLabel.setForeground(new Color(180, 206, 211));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        status.add(textLabel);

        /* Setare label ce afiseaza experienta acumulata */
        expLabel = new JLabel("You have gained this much experience: " + gainedEXP + " EXP");
        expLabel.setFont(new Font("Helvetica", Font.BOLD, 15));
        expLabel.setForeground(new Color(188,109,76,255));
        expLabel.setHorizontalAlignment(JLabel.CENTER);
        status.add(expLabel);

        /* Setare label ce afiseaza nivelul curent */
        levelLabel = new JLabel("You are now level " + newLevel);
        levelLabel.setFont(new Font("Helvetica", Font.BOLD, 15));
        levelLabel.setForeground(new Color(188,109,76,255));
        levelLabel.setHorizontalAlignment(JLabel.CENTER);
        status.add(levelLabel);

        /* Setare label ce afiseaza numarul de inamici infranti */
        battlesLabel = new JLabel("You have defeated " + battlesWon + " atrocious enemie(s)");
        battlesLabel.setFont(new Font("Helvetica", Font.BOLD, 15));
        battlesLabel.setForeground(new Color(188,109,76,255));
        battlesLabel.setHorizontalAlignment(JLabel.CENTER);
        status.add(battlesLabel);

        /* Setare label ce afiseaza numarul de monede stranse */
        coinsLabel = new JLabel("You have won " + coins + " coins");
        coinsLabel.setFont(new Font("Helvetica", Font.BOLD, 15));
        coinsLabel.setForeground(new Color(188,109,76,255));
        coinsLabel.setHorizontalAlignment(JLabel.CENTER);
        status.add(coinsLabel);

        /* Am adaugat un buton de exit ce are un ascultator */
        exitButton = new JButton("EXIT");
        exitButton.setBounds(425, 325, 150, 40);
        exitButton.setBackground(new Color(14,21,31));
        exitButton.setForeground(new Color(254,198,142,255));
        exitButton.setFocusable(false);
        exitButton.addActionListener(this);
        /* Adaugare buton la background */
        background.add(exitButton);

        /* Adaugare panel de status la background */
        background.add(status);

        show();
        pack();
    }

    /* La apasarea butonului exitButton fereastra se inchide */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
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
