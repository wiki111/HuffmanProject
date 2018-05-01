import java.awt.*;

public class Main {
    public static void main (String[] args){

        EventQueue.invokeLater(() -> {

            GuiConf gui = new GuiConf();
            gui.setVisible(true);

        });

    }
}
