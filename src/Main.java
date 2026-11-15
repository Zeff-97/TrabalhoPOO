import database.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.initialize();
            Menu.Show();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
