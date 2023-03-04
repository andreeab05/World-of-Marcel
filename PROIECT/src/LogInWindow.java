import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class LogInWindow extends JFrame implements ActionListener {
    Account account;
    public JButton button;
    public JTextField email;
    public JPasswordField password;
    public JLabel emailLabel, passLabel, background, logo, errorMessage;
    Game game;

    public LogInWindow(Game game){
        this.game = game;
        /* Setare dimeniuni fereastra de login. Nu am folosit un layout
        * pentru a putea controla pozitia elementelor de fereastra.
        * Fereastra nu este redimensionabila */
        setMinimumSize(new Dimension(960, 526));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        /* Setare imagine de background */
        background = new JLabel();
        background.setBounds(0,0, getWidth(), getHeight());
        background.setIcon(getPicture(new File("background.jpg"), background.getWidth(), background.getHeight()));
        add(background);

        /* Setare imagine cu logo-ul jocului */
        logo = new JLabel();
        logo.setBounds(240, 0, 430, 220);
        logo.setIcon(getPicture(new File("wow.png"), logo.getWidth(), logo.getHeight()));
        background.add(logo);

        /* Creare caseta de login */
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));
        panel.setBounds(350, 180, 200, 200);
        panel.setBackground(new Color(0, 0, 0, 0));
        /* Setare label si text field pentru introducerea email-ului */
        emailLabel = new JLabel("Account email");
        emailLabel.setForeground(new Color(204, 255, 255));
        email = new JTextField(30);
        email.setCaretColor(new Color(204, 255, 255));
        email.setBackground(new Color(0, 77, 77));
        email.setForeground(new Color(204, 255, 255));
        panel.add(emailLabel);panel.add(email);

        /* Setare label si text field pentru introducerea parolei */
        passLabel = new JLabel("Password");
        passLabel.setForeground(new Color(204, 255, 255));
        password = new JPasswordField(30);
        password.setCaretColor(new Color(204, 255, 255));
        password.setBackground(new Color(0, 77, 77));
        password.setForeground(new Color(204, 255, 255));

        /* Setare buton de log in */
        button = new JButton("Log In");
        button.setBackground(new Color(0, 51, 51));
        button.setForeground(new Color(204, 255, 255));
        button.setFocusable(false);
        /* Adaug un actionListener pe buton */
        button.addActionListener(this);

        /* Adaugare componente in panel */
        panel.add(passLabel);
        panel.add(password);
        panel.add(new JLabel());
        panel.add(button);
        errorMessage = new JLabel("*Incorrect email or password");
        errorMessage.setForeground(Color.red);
        errorMessage.setBackground(new Color(0, 77, 77, 50));
        panel.add(errorMessage);
        errorMessage.setVisible(false);
        /* Adaugare panel in background */
        background.add(panel);

        show();
        pack();
    }

    /* Metoda actionPerformed verifica daca email-ul si parola introduse
    * de utilizator in casetele de text corespund unui cont. Daca datele introduse
    * nu corespund unui cont, cele doua campuri de text se golesc si se afiseaza
    * un mesaj de eroare. Utilizatorul poate reintroduce date noi */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton){
            /* Daca datele sunt corecte, se porneste o fereastra in care utilizatorul
            * va alege personajul cu care vrea sa joace */
            if(isExistingAccount(game, email.getText(), password.getText()) == true) {
                CharacterWindow characterWindow = new CharacterWindow(account, game);
                dispose();
            }
            else {
                email.setText("");
                password.setText("");
                errorMessage.setVisible(true);
                isExistingAccount(game, email.getText(), password.getText());
            }
        }
    }


    /* Metoda isExistingAccount parcurge lista de conturi din obiectul de clasa Game si verifica daca
    * email-ul si parola corespund vreunui cont. Daca corespund, metoda returneaza true, altfel returneaza
    * false */
    public boolean isExistingAccount(Game game, String email, String password){
        for (Account account:game.accounts) {
            if(account.playerInfo.getCredentials().getEmail().equals(email)){
                if(account.playerInfo.getCredentials().getPassword().equals(password)) {
                    /* Daca contul exista, membrul account primeste o referinta la contul
                    * gasit */
                    this.account = account;
                    return true;
                }
            }
        }
        return false;
    }

    /* Metoda ce intoarce un obiect de clasa ImageIcon. Metoda primeste ca parametru
    * un fisier(ce contine o imagine) pe care il citeste, apoi il scaleaza la dimensiunea
    * apeland metoda getScaledInstance */
    public ImageIcon getPicture(File file, int width, int height) {
        BufferedImage img = null;
        try {
            /* Citire fisier */
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Metoda ce scaleaza imaginea citita la dimensiunile dorite */
        Image image = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
