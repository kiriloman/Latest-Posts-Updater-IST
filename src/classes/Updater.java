package classes;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Updater implements Runnable{

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
            icon = new TrayIcon(ImageIO.read(getClass().getResource("/resources/refresh.png")), "Anúncios", popup);
            icon.setImageAutoSize(true);
            SystemTray tray = SystemTray.getSystemTray();
            tray.add(icon);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
        
        while (true) {
            try {
                window.lastTimeUpdated();
                Thread.sleep(300000);
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
        if (before.size() < after.size()) {
            popUp();
            return;
        }
        for (String beforeKey : before.keySet()) {
            if (after.keySet().contains(beforeKey) && before.get(beforeKey)[0][0] != null && !before.get(beforeKey)[0][0].equals(after.get(beforeKey)[0][0])) {
                	popUp();
                    break;
            }
        }
    }

    public void popUp() {
        try {
            String basePath = new File("").getAbsolutePath();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(basePath.concat("/src/resources/spin.wav")));
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            Clip clip = (Clip)AudioSystem.getLine(info);
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(new JFrame(), "New Posts!");
    }
}
