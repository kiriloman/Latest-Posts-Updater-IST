import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Connection {
    private Preferences conPreferences = Preferences.userRoot().node("updaterIST");

    public HashMap<String, String[][]> update() {
        org.jsoup.nodes.Document doc;
        org.jsoup.nodes.Document parsedDocSmall;
        org.jsoup.nodes.Document parsedDocH5;
        org.jsoup.select.Elements elementsSmall;
        org.jsoup.select.Elements elementsH5;
        org.jsoup.select.Elements date;
        org.jsoup.select.Elements content;
        
        String stringSmall, stringH5;
        String[] partsSmall, partsH5;
        String[][] contentPerKey;
        HashMap<String, String[][]> info = new HashMap<>();
        
        try {
            for (int i = 0; i < conPreferences.keys().length; i++) {
                System.out.println(conPreferences.keys()[i]);
                
                doc = Jsoup.connect(conPreferences.get(conPreferences.keys()[i], conPreferences.keys()[i])).get();
                
                elementsSmall = doc.getElementsByClass("small");
                elementsH5 = doc.getElementsByTag("h5");
                
                stringSmall = elementsSmall.toString();
                stringH5 = elementsH5.toString();
                
                parsedDocSmall = Jsoup.parseBodyFragment(stringSmall);
                parsedDocH5 = Jsoup.parseBodyFragment(stringH5);
                
                date = parsedDocSmall.select("span");
                content = parsedDocH5.select("a");
                
                partsSmall = date.toString().split("\n");
                partsH5 = content.toString().split("\n");
                
                contentPerKey = new String[1][2];
                
                if (partsH5[0].length() != 0 && partsSmall[0].length() != 0) {
                    for (int j = 0; j < partsSmall.length; j++) {
                        if (Character.getNumericValue(partsSmall[j].charAt(6)) < 10) {
                            contentPerKey[0] = new String[]{partsSmall[j].substring(6, partsSmall[j].lastIndexOf("<") - 1), partsH5[0].substring(partsH5[0].indexOf(">") + 1, partsH5[0].indexOf("<", partsH5[0].indexOf("<") + 1))};
                            System.out.println(partsSmall[j].substring(6, partsSmall[j].lastIndexOf("<") - 1) + " " + partsH5[0].substring(partsH5[0].indexOf(">") + 1, partsH5[0].indexOf("<", partsH5[0].indexOf("<") + 1)));
                            break;
                        }
                    }
                }
                
                info.put(conPreferences.keys()[i], contentPerKey);
            }
        } catch (BackingStoreException | IOException e) {
            e.printStackTrace();
        }
        return info;
    }
}
