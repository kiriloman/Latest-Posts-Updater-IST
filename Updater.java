import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Updater implements Runnable{

    @Override
    public void run() {
        Window window = new Window();
        Connection connection = new Connection();
        TrayIcon icon;
        PopupMenu popup = new PopupMenu();
        MenuItem close = new MenuItem("Close"), open = new MenuItem("Open");
        HashMap<String, String[][]> before = connection.update(), after = new HashMap<>();
        window.create();
        window.fill(before);
        open.addActionListener(e -> window.show());
        close.addActionListener(e -> System.exit(0));
        popup.add(open);
        popup.add(close);
        try {
            icon = new TrayIcon(ImageIO.read(getClass().getResource("/refresh.png")), "Anúncios", popup);
            icon.setImageAutoSize(true);
            SystemTray tray = SystemTray.getSystemTray();
            tray.add(icon);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
//                HashMap<String, String[][]> test = new HashMap<>(), test1 = new HashMap<>();
//                test.put("ask", new String[][]{{"kk", "kk"}});
//                test1.put("ask", new String[][]{{"k", "kk"}});
                window.lastTimeUpdated();
//                newPost(test, test1);
                Thread.sleep(600000);
                after = connection.update();
                window.fill(after);
                newPost(before, after);
                before = after;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void newPost(HashMap<String, String[][]> before, HashMap<String, String[][]> after) {
        for (String beforeKey : before.keySet()) {
            if (after.keySet().contains(beforeKey)) {
                if (before.get(beforeKey)[0][0] != null || after.get(beforeKey)[0][0] != null) {
                    if (!before.get(beforeKey)[0][0].equals(after.get(beforeKey)[0][0]) || !before.get(beforeKey)[0][1].equals(after.get(beforeKey)[0][1])) {
                        try {
                            InputStream input = getClass().getResourceAsStream("blop.wav");
                            AudioStream audioStream = new AudioStream(input);
                            AudioPlayer.player.start(audioStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(before.get(beforeKey)[0][0] + "  " + after.get(beforeKey)[0][0]);
                        System.out.println(before.get(beforeKey)[0][1] + "  " + after.get(beforeKey)[0][1]);
                        JOptionPane.showMessageDialog(new JFrame(), "New Posts!");
                        break;
                    }
                }
            }
        }
    }
}
