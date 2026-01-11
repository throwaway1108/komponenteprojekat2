package com.raf.gaminggui.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.raf.gaminggui.util.ApiClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public AdminFrame() {
        setTitle("Admin Panel");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Users", createUsersPanel());
        tabbedPane.addTab("Games", createGamesPanel());
        tabbedPane.addTab("Notification Types", createNotificationTypesPanel());

        add(tabbedPane);
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Username", "Email", "Active", "Blocked", "Attendance %"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadUsers(model));

        JButton blockButton = new JButton("Block/Unblock User");
        blockButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long userId = Long.parseLong(model.getValueAt(row, 0).toString());
                try {
                    // Šaljemo ID u URL-u: /user/{id}/block
                    ApiClient.put("user", "/user/" + userId + "/block", new JsonObject());
                    JOptionPane.showMessageDialog(this, "Status toggled for User ID: " + userId);
                    loadUsers(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user first.");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(blockButton);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadUsers(model);
        return panel;
    }

    private void loadUsers(DefaultTableModel model) {
        try {
            String response = ApiClient.get("user", "/user?size=100");
            if (response == null || response.trim().isEmpty() || response.equals("null")) return;

            model.setRowCount(0);
            JsonElement element = ApiClient.getGson().fromJson(response, JsonElement.class);
            JsonArray users = getArrayFromElement(element);

            if (users == null) return;

            for (int i = 0; i < users.size(); i++) {
                JsonObject user = users.get(i).getAsJsonObject();

                // Pokušavamo oba naziva polja jer Spring zna da skrati "isBlocked" u "blocked"
                boolean blocked = user.has("blocked") ? user.get("blocked").getAsBoolean() :
                        (user.has("isBlocked") && user.get("isBlocked").getAsBoolean());

                boolean active = user.has("active") ? user.get("active").getAsBoolean() :
                        (user.has("isActive") && user.get("isActive").getAsBoolean());

                model.addRow(new Object[]{
                        user.has("id") ? user.get("id").getAsLong() : 0L,
                        user.has("username") ? user.get("username").getAsString() : "N/A",
                        user.has("email") ? user.get("email").getAsString() : "N/A",
                        active,
                        blocked,
                        user.has("attendancePercentage") ? user.get("attendancePercentage").getAsDouble() : 0.0
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JPanel createGamesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Name", "Genre", "Description"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadGames(model));

        JButton addButton = new JButton("Add Game");
        addButton.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField genreField = new JTextField();
            JTextField descField = new JTextField();

            Object[] message = { "Name:", nameField, "Genre:", genreField, "Description:", descField };

            int option = JOptionPane.showConfirmDialog(this, message, "Add New Game", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    JsonObject game = new JsonObject();
                    game.addProperty("name", nameField.getText());
                    game.addProperty("genre", genreField.getText());
                    game.addProperty("description", descField.getText());

                    // MORAŠ DODATI TOKEN (ako ga čuvaš negde, npr. ApiClient.token)
                    ApiClient.post("session", "/game", game);
                    // ^ Ako tvoj ApiClient ne lepi automatski "Bearer <token>",
                    // kontroler će odbiti zahtev zbog @CheckSecurity.

                    loadGames(model);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton deleteButton = new JButton("Delete Game");
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long gameId = Long.parseLong(model.getValueAt(row, 0).toString());
                try {
                    ApiClient.delete("session", "/game/" + gameId);
                    loadGames(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadGames(model);
        return panel;
    }

    private void loadGames(DefaultTableModel model) {
        try {
            String response = ApiClient.get("session", "/game?size=100");
            if (response == null || response.trim().isEmpty()) return;

            model.setRowCount(0);
            JsonElement element = ApiClient.getGson().fromJson(response, JsonElement.class);
            JsonArray games = getArrayFromElement(element);

            if (games != null) {
                for (JsonElement g : games) {
                    JsonObject obj = g.getAsJsonObject();
                    model.addRow(new Object[]{
                            obj.get("id").getAsLong(),
                            obj.get("name").getAsString(),
                            obj.get("genre").getAsString(),
                            obj.get("description").getAsString()
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JPanel createNotificationTypesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Type Name", "Template"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadNotificationTypes(model));

        JButton addButton = new JButton("Add Type");
        addButton.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField templateField = new JTextField();
            Object[] message = { "Type Name:", nameField, "Template:", templateField };

            int option = JOptionPane.showConfirmDialog(this, message, "Add Notification Type", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    JsonObject type = new JsonObject();
                    type.addProperty("typeName", nameField.getText());
                    type.addProperty("template", templateField.getText());

                    ApiClient.post("notification", "/notification-type", type);
                    loadNotificationTypes(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadNotificationTypes(model);
        return panel;
    }

    private void loadNotificationTypes(DefaultTableModel model) {
        try {
            String response = ApiClient.get("notification", "/notification-type?size=100");
            if (response == null || response.trim().isEmpty()) return;

            model.setRowCount(0);
            JsonElement element = ApiClient.getGson().fromJson(response, JsonElement.class);
            JsonArray types = getArrayFromElement(element);

            if (types != null) {
                for (JsonElement t : types) {
                    JsonObject obj = t.getAsJsonObject();
                    model.addRow(new Object[]{
                            obj.get("id").getAsLong(),
                            obj.get("typeName").getAsString(),
                            obj.get("template").getAsString()
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JsonArray getArrayFromElement(JsonElement element) {
        if (element.isJsonObject() && element.getAsJsonObject().has("content")) {
            return element.getAsJsonObject().getAsJsonArray("content");
        } else if (element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        return null;
    }
}