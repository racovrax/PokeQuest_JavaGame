package Main;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static JFrame window;
    public static void main(String[] args) throws IOException {
        // Crearea ferestrei principale a aplicației
        window = new JFrame("PokeQuest"); // Crearea unei instanțe JFrame cu titlul "PokeQuest"
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Specificarea acțiunii la închiderea ferestrei (oprește aplicația)
        window.setResizable(false); // Interzicerea redimensionării ferestrei de către utilizator
        window.setTitle("PokeQuest"); // Setarea titlului ferestrei
        //window.setUndecorated(true); // Ascunderea barei de titlu a ferestrei

        // Crearea și adăugarea GamePanel-ului (panoul de joc) la fereastra principală
        GamePanel gamePanel = new GamePanel(); // Crearea unei instanțe GamePanel
        window.add(gamePanel); // Adăugarea GamePanel-ului la fereastra principală

        gamePanel.config.loadConfig();
        if(gamePanel.config.gp.fullScreenOn) {
            window.setUndecorated(true);
        }

        window.pack(); // Redimensionarea ferestrei pentru a se potrivi conținutului

        window.setLocationRelativeTo(null); // Centrarea ferestrei pe ecran
        window.setVisible(true); // Facerea ferestrei vizibile

        // Inițializarea jocului și pornirea firului de execuție pentru actualizarea și afișarea acestuia
        gamePanel.setupGame(); // Inițializarea jocului (setarea stării inițiale, a muzicii etc.)
        gamePanel.StartGameThread(); // Pornirea firului de execuție pentru actualizarea și afișarea jocului
    }
}
