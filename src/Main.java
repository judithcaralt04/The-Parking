import Business.BusinessLogicSimulation;
import Persistance.dao.SQLConnector;
import Presentation.MainController;

import javax.swing.JOptionPane;

/**
 * Application entry point that starts the parking management program.
 */
public class Main {

    /**
     * Starts the application.
     *
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args) {

        SQLConnector connector = SQLConnector.getInstance();

        if (!connector.isConnected()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se ha podido conectar a la base de datos.\n\n" +
                            "URL: " + connector.getUrl() + "\n\n" +
                            "Detalle: " + connector.getLastError() + "\n\n" +
                            "Comprueba que:\n" +
                            "  - El servidor MySQL está arrancado.\n" +
                            "  - Los datos del fichero config.json son correctos.\n" +
                            "  - La base de datos parking_db existe.\n\n" +
                            "El programa se cerrará.",
                    "Error de conexión a la base de datos",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }

        new BusinessLogicSimulation().start();
        new MainController();
    }
}
