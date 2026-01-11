package com.raf.gaminggui.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.raf.gaminggui.util.ApiClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PlayerFrame extends JFrame {

    private Long userId;
    private JTabbedPane tabbedPane;
    private DefaultTableModel allSessionsModel;
    private DefaultTableModel mySessionsModel;

    // Čuvamo poslednje učitane podatke o korisniku radi lakšeg popunjavanja Edit dijaloga
    private JsonObject lastLoadedUser;

    public PlayerFrame(Long userId) {
        this.userId = userId;
        setTitle("Player Panel - User ID: " + userId);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("My Profile", createProfilePanel());
        tabbedPane.addTab("Sessions", createSessionsPanel());
        tabbedPane.addTab("Create Session", createCreateSessionPanel());
        tabbedPane.addTab("My Sessions", createMySessionsPanel());
        tabbedPane.addTab("Notifications", createNotificationsPanel());

        // Automatsko osvežavanje profila kada se klikne na prvi tab
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                refreshProfileUI();
            }
        });
        add(tabbedPane);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        try {
            // Dohvatamo sve korisnike da bismo našli trenutnog (prema tvom kodu)
            String response = ApiClient.get("user", "/user?size=1000");
            if (response != null && !response.trim().isEmpty()) {
                JsonElement element = ApiClient.getGson().fromJson(response, JsonElement.class);
                JsonArray users = getArrayFromElement(element);

                if (users != null) {
                    for (int i = 0; i < users.size(); i++) {
                        JsonObject user = users.get(i).getAsJsonObject();
                        if (user.has("id") && user.get("id").getAsLong() == userId) {
                            lastLoadedUser = user;
                            break;
                        }
                    }
                }
            }

            if (lastLoadedUser != null) {
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Username: " + getString(lastLoadedUser, "username")), gbc);
                gbc.gridy = 1;
                panel.add(new JLabel("Email: " + getString(lastLoadedUser, "email")), gbc);
                gbc.gridy = 2;
                panel.add(new JLabel("Name: " + getString(lastLoadedUser, "firstName") + " " + getString(lastLoadedUser, "lastName")), gbc);
                gbc.gridy = 3;
                panel.add(new JLabel("Registered: " + getInt(lastLoadedUser, "totalRegisteredSessions")), gbc);
                gbc.gridy = 4;
                panel.add(new JLabel("Attended: " + getInt(lastLoadedUser, "totalAttendedSessions")), gbc);
                gbc.gridy = 5;
                panel.add(new JLabel("Abandoned: " + getInt(lastLoadedUser, "totalAbandonedSessions")), gbc);
                gbc.gridy = 6;
                double attPct = lastLoadedUser.has("attendancePercentage") ? lastLoadedUser.get("attendancePercentage").getAsDouble() : 0.0;
                panel.add(new JLabel("Attendance %: " + String.format("%.2f", attPct) + "%"), gbc);
                gbc.gridy = 7;
                panel.add(new JLabel("Organized: " + getInt(lastLoadedUser, "successfullyOrganizedSessions")), gbc);
                gbc.gridy = 8;
                String title = getString(lastLoadedUser, "organizerTitle").equals("N/A") ? "Novice" : getString(lastLoadedUser, "organizerTitle");
                JLabel titleLabel = new JLabel("Title: " + title);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
                panel.add(titleLabel, gbc);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        // DUGMAD ZA AKCIJE
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton refreshProfileBtn = new JButton("Refresh Profile Data");
        refreshProfileBtn.addActionListener(e -> refreshProfileUI());

        JButton editProfileBtn = new JButton("Edit Profile");
        editProfileBtn.addActionListener(e -> openEditProfileDialog());

        buttonPanel.add(refreshProfileBtn);
        buttonPanel.add(editProfileBtn);

        gbc.gridy = 9; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void openEditProfileDialog() {
        if (lastLoadedUser == null) return;

        JTextField firstNameField = new JTextField(getString(lastLoadedUser, "firstName"));
        JTextField lastNameField = new JTextField(getString(lastLoadedUser, "lastName"));
        JTextField emailField = new JTextField(getString(lastLoadedUser, "email"));
        JTextField usernameField = new JTextField(getString(lastLoadedUser, "username"));

        Object[] message = {
                "First Name:", firstNameField,
                "Last Name:", lastNameField,
                "Email:", emailField,
                "Username:", usernameField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Personal Data", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                JsonObject updateDto = new JsonObject();
                updateDto.addProperty("firstName", firstNameField.getText());
                updateDto.addProperty("lastName", lastNameField.getText());
                updateDto.addProperty("email", emailField.getText());
                updateDto.addProperty("username", usernameField.getText());

                // PUT zahtev na User servis: /user/{id}
                ApiClient.put("user", "/user/" + userId, updateDto);

                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                refreshProfileUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
            }
        }
    }

    private void refreshProfileUI() {
        tabbedPane.setComponentAt(0, createProfilePanel());
    }

    // --- Ostatak koda (Sessions, Create, My Sessions, Notifications) ---

    private JPanel createSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Name", "Game", "Type", "Max Players", "Current", "Start Time", "Status"};
        allSessionsModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(allSessionsModel);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField gameIdField = new JTextField(5);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"", "OPEN", "CLOSED"});

        JButton searchButton = new JButton("Search / Refresh");
        searchButton.addActionListener(e -> {
            StringBuilder query = new StringBuilder("/session?size=100");
            if (!gameIdField.getText().isEmpty()) query.append("&gameId=").append(gameIdField.getText());
            if (typeCombo.getSelectedIndex() > 0) query.append("&sessionType=").append(typeCombo.getSelectedItem());
            loadSessions(allSessionsModel, query.toString());
        });

        JButton joinButton = new JButton("Join Session");
        joinButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long sessionId = Long.parseLong(allSessionsModel.getValueAt(row, 0).toString());
                String type = allSessionsModel.getValueAt(row, 3).toString();
                String token = "CLOSED".equals(type) ? JOptionPane.showInputDialog(this, "Enter token:") : null;
                try {
                    JsonObject dto = new JsonObject();
                    dto.addProperty("userId", userId);
                    if (token != null) dto.addProperty("invitationToken", token);
                    ApiClient.post("session", "/session/" + sessionId + "/join", dto);
                    JOptionPane.showMessageDialog(this, "Joined!");
                    searchButton.doClick();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });

        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long sid = Long.parseLong(allSessionsModel.getValueAt(row, 0).toString());
                SessionDetailsFrame df = new SessionDetailsFrame(sid, userId);
                df.addWindowListener(new WindowAdapter() {
                    @Override public void windowClosed(WindowEvent e) { searchButton.doClick(); }
                });
                df.setVisible(true);
            }
        });

        filterPanel.add(new JLabel("Game ID:")); filterPanel.add(gameIdField);
        filterPanel.add(new JLabel("Type:")); filterPanel.add(typeCombo);
        filterPanel.add(searchButton);
        JPanel bp = new JPanel(); bp.add(joinButton); bp.add(viewButton);
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bp, BorderLayout.SOUTH);
        loadSessions(allSessionsModel, "/session?size=100");
        return panel;
    }

    private void loadSessions(DefaultTableModel model, String endpoint) {
        try {
            model.setRowCount(0);
            String response = ApiClient.get("session", endpoint);
            if (response == null || response.isEmpty()) return;
            JsonArray sessions = getArrayFromElement(ApiClient.getGson().fromJson(response, JsonElement.class));
            if (sessions == null) return;
            for (int i = 0; i < sessions.size(); i++) {
                JsonObject s = sessions.get(i).getAsJsonObject();
                JsonObject g = s.getAsJsonObject("game");
                model.addRow(new Object[]{s.get("id").getAsLong(), s.get("sessionName").getAsString(), g.get("name").getAsString(), s.get("sessionType").getAsString(), s.get("maxPlayers").getAsInt(), s.get("currentPlayerCount").getAsInt(), s.get("startDateTime").getAsString(), s.get("status").getAsString()});
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private JPanel createCreateSessionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField gameIdField = new JTextField(20);
        JTextField maxPlayersField = new JTextField(20);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"OPEN", "CLOSED"});
        JTextField startDateTimeField = new JTextField(20);
        JTextArea descArea = new JTextArea(5, 20);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Session Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Game ID:"), gbc);
        gbc.gridx = 1; panel.add(gameIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Max Players:"), gbc);
        gbc.gridx = 1; panel.add(maxPlayersField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; panel.add(typeCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1; panel.add(startDateTimeField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(descArea), gbc);

        JButton createButton = new JButton("Create Session");
        createButton.addActionListener(e -> {
            try {
                JsonObject session = new JsonObject();
                session.addProperty("sessionName", nameField.getText());
                session.addProperty("gameId", Long.parseLong(gameIdField.getText()));
                session.addProperty("maxPlayers", Integer.parseInt(maxPlayersField.getText()));
                session.addProperty("sessionType", typeCombo.getSelectedItem().toString());
                session.addProperty("startDateTime", startDateTimeField.getText());
                session.addProperty("description", descArea.getText());
                session.addProperty("organizerId", userId);

                ApiClient.post("session", "/session", session);
                JOptionPane.showMessageDialog(this, "Session created!");
                tabbedPane.setSelectedIndex(3);
                loadMySessions(mySessionsModel);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(createButton, gbc);
        return panel;
    }

    private JPanel createMySessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Name", "Game", "Type", "Players", "Start Time", "Status"};
        mySessionsModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(mySessionsModel);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadMySessions(mySessionsModel));

        JButton viewBtn = new JButton("View Details");
        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long sid = Long.parseLong(mySessionsModel.getValueAt(row, 0).toString());
                SessionDetailsFrame df = new SessionDetailsFrame(sid, userId);
                df.addWindowListener(new WindowAdapter() {
                    @Override public void windowClosed(WindowEvent e) { loadMySessions(mySessionsModel); }
                });
                df.setVisible(true);
            }
        });

        JPanel bp = new JPanel(); bp.add(refreshBtn); bp.add(viewBtn);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bp, BorderLayout.SOUTH);
        loadMySessions(mySessionsModel);
        return panel;
    }

    private void loadMySessions(DefaultTableModel model) {
        try {
            if (model == null) return;
            model.setRowCount(0);
            String response = ApiClient.get("session", "/session?userId=" + userId + "&size=100");
            if (response == null || response.isEmpty()) return;
            JsonArray sessions = getArrayFromElement(ApiClient.getGson().fromJson(response, JsonElement.class));
            if (sessions == null) return;
            for (int i = 0; i < sessions.size(); i++) {
                JsonObject s = sessions.get(i).getAsJsonObject();
                String gn = s.has("game") && !s.get("game").isJsonNull() ? s.getAsJsonObject("game").get("name").getAsString() : "Unknown";
                model.addRow(new Object[]{s.get("id").getAsLong(), s.get("sessionName").getAsString(), gn, s.get("sessionType").getAsString(), s.get("currentPlayerCount").getAsInt() + "/" + s.get("maxPlayers").getAsInt(), s.get("startDateTime").getAsString(), s.get("status").getAsString()});
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private JPanel createNotificationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Type", "Content", "Sent At"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadNotifications(model));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        loadNotifications(model);
        return panel;
    }

    private void loadNotifications(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            String response = ApiClient.get("notification", "/notification?userId=" + userId + "&size=100");
            if (response == null || response.isEmpty()) return;
            JsonArray arr = getArrayFromElement(ApiClient.getGson().fromJson(response, JsonElement.class));
            if (arr != null) {
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject n = arr.get(i).getAsJsonObject();
                    String type = n.has("notificationType") ? n.getAsJsonObject("notificationType").get("typeName").getAsString() : "N/A";
                    model.addRow(new Object[]{n.get("id").getAsLong(), type, n.get("content").getAsString(), n.get("sentAt").getAsString()});
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    // Helper metode
    private JsonArray getArrayFromElement(JsonElement element) {
        if (element.isJsonObject() && element.getAsJsonObject().has("content")) return element.getAsJsonObject().getAsJsonArray("content");
        else if (element.isJsonArray()) return element.getAsJsonArray();
        return null;
    }

    private String getString(JsonObject o, String key) {
        return o.has(key) && !o.get(key).isJsonNull() ? o.get(key).getAsString() : "N/A";
    }

    private int getInt(JsonObject o, String key) {
        return o.has(key) && !o.get(key).isJsonNull() ? o.get(key).getAsInt() : 0;
    }
}