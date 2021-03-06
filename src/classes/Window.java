package classes;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    private JTextField field = new JTextField("URL to course page");
    private JLabel lastUpdate = new JLabel();
    private Preferences preferences = Preferences.userRoot().node("updaterIST");
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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scroll = new JScrollPane(table);

        lastUpdate.setOpaque(true);
        lastUpdate.setHorizontalAlignment(SwingConstants.CENTER);

        field.addFocusListener(urlInputed());
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
        try {
			frame.setIconImage(ImageIO.read(getClass().getResource("/resources/refresh.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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

    private String extract(String url) {
        String name = "";
        int i = 45;
        while (!Character.isDigit(url.charAt(i))) {
            name += url.charAt(i);
            i++;
        }
        return name;
    }

    private void remove() {
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

    private void add() {
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
        }
    }

    public void lastTimeUpdated() {
        lastUpdate.setText("Last updated at: " + simpleDF.format(new Date()) + ".");
    }
    
    private FocusListener urlInputed() {
    	FocusListener f = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (field.getText().equals("URL to course page"))
					field.setText("");
			}
		
			@Override
			public void focusLost(FocusEvent e) {
				if (field.getText().equals(""))
					field.setText("URL to course page");
			}
    	};
    	return f;
    }
}