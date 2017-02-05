import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Updater implements Runnable{
    @Override
    public void run() {
        Window window = new Window();
        Connection connection = new Connection();
        TrayIcon icon;
        PopupMenu popup = new PopupMenu();
        MenuItem close = new MenuItem("Close"), open = new MenuItem("Open");
        window.create();
        window.fill(connection.update());
        open.addActionListener(e -> window.show());
        close.addActionListener(e -> System.exit(0));
        popup.add(open);
        popup.add(close);
        try {
            icon = new TrayIcon(ImageIO.read(getClass().getResource("/integral.png")), "Anúncios", popup);
            icon.setImageAutoSize(true);
            SystemTray tray = SystemTray.getSystemTray();
            tray.add(icon);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Thread.sleep(600000);
                window.fill(connection.update());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
