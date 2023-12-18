package NikitaLSG;

public class Main {
    public static void main(String[] args) {
        String host = "10.242.100.15";
        String service = "dblab.miit.ru";
        String user = "your_username";
        String password = "your_password";

        ConnectDB connectDB = new ConnectDB(host, service, user, password);
        connectDB.createUI();
    }
}
