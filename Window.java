import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class Window {
    private JFrame frame = new JFrame("Últimos Anúncios");
    private JPanel panelOptions = new JPanel();
    private JPanel panelTable = new JPanel();
    private JPanel panelMain = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private JButton plus = new JButton("Plus");
    private JButton minus = new JButton("Minus");
    private JTextField field = new JTextField();
    private Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

    public void create() {
        String[] columnNames = {"Cadeira", "Data", "Anúncio"};
        table = new JTable(new DefaultTableModel(null, columnNames));
        model = (DefaultTableModel) table.getModel();
        JScrollPane scroll = new JScrollPane(table);
        plus.addActionListener(e -> add());
        minus.addActionListener(e -> remove());
        panelOptions.setLayout(new BoxLayout(panelOptions, BoxLayout.X_AXIS));
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelOptions.add(plus);
        panelOptions.add(minus);
        panelOptions.add(field);
        panelTable.add(scroll);
        panelMain.add(panelOptions);
        panelMain.add(panelTable);
        frame.add(panelMain);
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
        if (table.getSelectedRow() != -1) {
            String name = (String) model.getValueAt(table.getSelectedRow(), 0);
            boolean delete = true;
            for (int i = 0; i < table.getRowCount(); i++) {
                if (name.equals(model.getValueAt(i, 0)) && i != table.getSelectedRow()) {
                    delete = false;
                    break;
                }
            }
            if (delete)
                preferences.remove(name);
            model.removeRow(table.getSelectedRow());
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
}
