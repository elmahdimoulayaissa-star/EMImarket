public class Authentification {

    public static User currentUser;

    private Authentification() {}

    public static boolean existe(String username) {
        for (User u : BD.users)
            if (u.getUsername().equals(username)) return true;
        return false;
    }

    public static boolean inscrire(String username, String password, String email) throws Exception {
        if (existe(username)) return false;
        User nouveau = new User(username, password, email, 100.0);
        if (username.equalsIgnoreCase("admin")) nouveau.setAdmin(true);
        BD.users.add(nouveau);
        BD.update();
        return true;
    }

    public static boolean login(String username, String password) {
        for (User u : BD.users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                return true;
            }
        }
        return false;
    }

    public static void logout() {
        currentUser = null;
    }
}
