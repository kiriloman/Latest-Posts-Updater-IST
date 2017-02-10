import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.Preferences;

public class Window {
    private JFrame frame = new JFrame("Last Posts");
    private JPanel panelOptions = new JPanel();
    private JPanel panelTable = new JPanel();
    private JPanel panelMain = new JPanel();
    private JTable table;
    private JScrollPane scroll;
    private DefaultTableModel model;
    private JButton plus = new JButton("+");
    private JButton minus = new JButton("-");
    private JTextField field = new JTextField();
    private JLabel lastUpdate = new JLabel();
    private Preferences preferences = Preferences.userRoot().node(this.getClass().getName());
    private SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm:ss");

    public void create() {
        model = new DefaultTableModel(null, new String[] {"Class", "Date", "Post"}) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(0).setMinWidth(50);

        scroll = new JScrollPane(table);

        lastUpdate.setOpaque(true);
        lastUpdate.setHorizontalAlignment(SwingConstants.CENTER);

        plus.addActionListener(e -> add());
        minus.addActionListener(e -> remove());

        panelOptions.setLayout(new BoxLayout(panelOptions, BoxLayout.X_AXIS));
        panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.Y_AXIS));
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelOptions.add(plus);
        panelOptions.add(minus);
        panelOptions.add(field);
        panelTable.add(scroll);
        panelTable.add(lastUpdate);
        panelMain.add(panelOptions);
        panelMain.add(panelTable);

        frame.add(panelMain);
        frame.setIconImage(new ImageIcon(getClass().getClass().getResource("/refresh.png")).getImage());
        frame.setPreferredSize(new Dimension(370, 370));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    public void fill(HashMap<String, String[][]> data) {
        model.setRowCount(0);
        for (String name : data.keySet()) {
            if (data.get(name)[0][0] != null) {
                for (int i = 0; i < data.get(name).length; i++) {
                    model.addRow(new String[]{name, data.get(name)[i][0], data.get(name)[i][1]});
                }
            }
            else {
                model.addRow(new String[]{name, "", ""});
            }
        }
        frame.pack();
    }

    public void show() {
        frame.setVisible(true);
    }

    public String extract(String url) {
        String name = "";
        int i = 45;
        while (!Character.isDigit(url.charAt(i))) {
            name += url.charAt(i);
            i++;
        }
        return name;
    }

    public void remove() {
        if (table.getSelectedRows().length != 0) {
            String name;
            for (int i = 0; i < table.getSelectedRows().length; i++) {
                name = (String) model.getValueAt(table.getSelectedRows()[i], 0);
                for (int j = 0; j < table.getRowCount(); j++) {
                    if (name.equals(model.getValueAt(j, 0))) {
                        model.removeRow(j);
                        j--;
                    }
                }
                preferences.remove(name);
            }
        }
    }

    public void add() {
        if (field.getText().startsWith("https://fenix.tecnico.ulisboa.pt/disciplinas/")) {
            boolean insert = true;
            String name = extract(field.getText());
            for (int i = 0; i < table.getRowCount(); i++) {
                if (name.equals(model.getValueAt(i, 0))) {
                    insert = false;
                    break;
                }
            }
            if (insert) {
                preferences.put(extract(field.getText()), field.getText());
                model.addRow(new String[]{extract(field.getText()), "", ""});
            }
            else {
                //Warning message "Os anúncios desta cadeira já estão a ser seguidos..."
            }
        }
    }

    public void lastTimeUpdated() {
        lastUpdate.setText("Last updated at: " + simpleDF.format(new Date()) + ".");
    }
}
