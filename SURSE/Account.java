import java.util.ArrayList;
/*import java.util.Collection;
import java.util.Collections;*/
import java.util.List;

public class Account {
    Information playerInfo;
    List<Character> characters;
    int noOfPlayedGames;

    public Account(){
        characters = new ArrayList<>();
    }

    /* Constructorul clasei Account ce populeaza membrul playerInfo folosind sablonul
    * Builder */
    public Account(Credentials credentials, String name, String country, ArrayList<String> favouriteGames){
        try {
            /* Constructia unui obiect de clasa Information prin intermediul unui obiect
            * de clasa InformationBuilder. Pentru fiecare membru care nu este obligatoriu
            * se apeleaza metoda de setare corespunzatoare (pentru country si favouriteGames).
            * La final se apeleaza metoda build ce returneaza obiectul de clasa Information cosntruit */
            playerInfo = new Information.InformationBuilder(credentials, name)
                    .country(country)
                    .favouriteGames(favouriteGames)
                    .build();
        }
        catch (InformationIncompleteException e)
        {
            e.getStackTrace();
        }
        characters = new ArrayList<>();
    }

    static class Information{
        private Credentials credentials;
        private String name;
        private String country;
        private ArrayList<String> favouriteGames;

        /* Clasa interna a clasei Information ce realizeaza sablonul Builder
        * cu ajutorul caruia se va construi un obiect de clasa Information element
        * cu element */
         public static class InformationBuilder{
            final private Credentials credentials;
            final private String name;
            private String country;
            private ArrayList<String> favouriteGames;

            /* Constructorul clasei instantiaza membrii care trebuie sa fie populati obligatoriu,
            * credentials si name (un obiect de clasa Information nu are voie sa existe daca acesti
            * membrii sunt null) */
            public InformationBuilder(Credentials credentials, String name){
                this.credentials = credentials;
                this.name = name;
            }

            /* Metoda de setare a membrului country */
            public InformationBuilder country(String country){
                this.country = country;
                return this;
            }

            /* Membru de setare a membrului favouriteGames(o lista de string-uri) */
            public InformationBuilder favouriteGames(ArrayList<String> favouriteGames){
                this.favouriteGames = favouriteGames;
                return this;
            }

            /* Metoda ce verifica daca membrii credentials si name ai obiectului
            * de tip InformationBuilder sunt null, caz in care se arunca o exceptie de tip
            * InformationIncompleteException, altfel se returneaza un obiect de clasa Information */
            public Information build() throws InformationIncompleteException{
                if(this.credentials.equals(null) || this.name.equals(null))
                    throw new InformationIncompleteException();
                return new Account.Information(this);
            }
        }

        /* Constructorul privat al clasei Information ce instantiaza membrii clasei
        * prin intermediul obiectului de tip InformationBuilder */
        private Information(InformationBuilder builder){
            credentials = builder.credentials;
            name = builder.name;;
            country = builder.country;;
            favouriteGames = builder.favouriteGames;
        }

        /* Metoda getter pentru obtinerea membrului name */
        public String getName() {
            return name;
        }

        /* Metoda getter pentru obtinerea membrului country */
        public String getCountry() {
            return country;
        }

        /* Metoda getter pentru obtinerea membrului favouriteGames */
        public ArrayList<String> getFavouriteGames() {
            return favouriteGames;
        }

        /* Metoda getter pentru obtinerea membrului credentials */
        public Credentials getCredentials() {
            return credentials;
        }
    }
}
