import java.util.Scanner;

public class Test {
    /* Metoda ce preia comanda de la stdin si o returneaza.
    * Metoda arunca exceptia de tip InvalidCommandException daca
    * comanda data de utilizator este diferita de "terminal" sau
    * "GUI", necesare la alegerea modului de joc sau de "P" cu care
    * se trece la urmatoareamutare */
    public static String getCommand() throws InvalidCommandException{
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        if(!answer.equals("terminal") && !answer.equals("GUI") && !answer.equals("P"))
            throw new InvalidCommandException();
        return answer;
    }

    /* In metoda main se obtine un obiect de tip Game, apoi se apeleaza getCommand() si
    * utilizatorul alege modul de joc(terminal sau GUI), apoi raspunsul acestuia este trimis
    * ca parametru metodei run din clasa Game, care se si apeleaza. Deoarece se apeleaza functia
    * getCommand(), se incearca prinderea si tratarea exceptiei InvalidCommandException. */
    public static void main(String[] args) {
        Game game = Game.getInstance();
        System.out.println("Hello there, mighty hero, how would you like to play? Choose from terminal or GUI. Choices, choices...");
        try {
            String answer = Test.getCommand();
            game.run(answer);
        }
        catch (InvalidCommandException e){
            e.printStackTrace();
        }
    }
}
