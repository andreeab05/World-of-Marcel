public class Credentials {
    private String email;
    private String password;

    /* Metoda setter pentru a modifica membrul email */
    public void setEmail(String email) {
        this.email = email;
    }

    /* Metoda setter pentru a modifica membrul password */
    public void setPassword(String password) {
        this.password = password;
    }

    /* Metoda getter pentru a obtine valoarea membrului privat email */
    public String getEmail() {
        return email;
    }

    /* Metoda getter pentru a obtine valoarea membrului privat password */
    public String getPassword() {
        return password;
    }
}
