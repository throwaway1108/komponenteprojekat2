package com.raf.gaminggui.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.raf.gaminggui.util.ApiClient;

import javax.swing.*;
import java.awt.*;

public class SessionDetailsFrame extends JFrame {

    private Long sessionId;
    private Long userId;
    private JsonObject session;

    public SessionDetailsFrame(Long sessionId, Long userId) {
        this.sessionId = sessionId;
        this.userId = userId;

        setTitle("Session Details - ID: " + sessionId);
        setSize(600, 550); // Malo povećana visina zbog novog dugmeta
        setLocationRelativeTo(null);

        loadSession();
        initComponents();
    }

    private void loadSession() {
        try {
            String response = ApiClient.get("session", "/session/" + sessionId);
            session = ApiClient.getGson().fromJson(response, JsonObject.class);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Naslov i osnovni podaci
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel(session.get("sessionName").getAsString());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Game: " + session.getAsJsonObject("game").get("name").getAsString()), gbc);

        gbc.gridy = 2;
        panel.add(new JLabel("Type: " + session.get("sessionType").getAsString()), gbc);

        gbc.gridy = 3;
        panel.add(new JLabel("Players: " + session.get("currentPlayerCount").getAsInt() + "/" + session.get("maxPlayers").getAsInt()), gbc);

        gbc.gridy = 4;
        panel.add(new JLabel("Start Time: " + session.get("startDateTime").getAsString()), gbc);

        gbc.gridy = 5;
        panel.add(new JLabel("Status: " + session.get("status").getAsString()), gbc);

        gbc.gridy = 6;
        panel.add(new JLabel("Description: " + session.get("description").getAsString()), gbc);

        gbc.gridy = 7;
        panel.add(new JLabel("Organizer ID: " + session.get("organizerId").getAsLong()), gbc);

        // LOGIKA ZA DUGMIĆE
        long organizerId = session.get("organizerId").getAsLong();

        // Provera da li je ulogovani korisnik u listi učesnika
        JsonArray participants = session.getAsJsonArray("participantIds");
        boolean isParticipant = false;
        if (participants != null) {
            for (int i = 0; i < participants.size(); i++) {
                if (participants.get(i).getAsLong() == userId) {
                    isParticipant = true;
                    break;
                }
            }
        }

        // 1. Dugmići za ORGANIZATORA
        if (organizerId == userId) {
            JButton inviteButton = new JButton("Invite Player");
            inviteButton.addActionListener(e -> invitePlayer());

            JButton cancelButton = new JButton("Cancel Session");
            cancelButton.setBackground(new Color(255, 102, 102));
            cancelButton.addActionListener(e -> cancelSession());

            JButton completeButton = new JButton("Complete Session");
            completeButton.setBackground(new Color(102, 255, 102));
            completeButton.addActionListener(e -> completeSession());

            gbc.gridy = 8;
            panel.add(inviteButton, gbc);
            gbc.gridy = 9;
            panel.add(cancelButton, gbc);
            gbc.gridy = 10;
            panel.add(completeButton, gbc);
        }

        // 2. Dugme za NAPUŠTANJE (samo za učesnike koji nisu organizatori)
        else if (isParticipant) {
            JButton leaveButton = new JButton("Leave Session");
            leaveButton.setBackground(Color.ORANGE);
            leaveButton.addActionListener(e -> leaveSession());

            gbc.gridy = 8;
            panel.add(leaveButton, gbc);
        }

        add(panel);
    }

    private void invitePlayer() {
        String userIdStr = JOptionPane.showInputDialog(this, "Enter User ID to invite:");
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                JsonObject dto = new JsonObject();
                dto.addProperty("userId", Long.parseLong(userIdStr));

                ApiClient.post("session", "/session/" + sessionId + "/invite", dto);
                JOptionPane.showMessageDialog(this, "Invitation sent!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cancelSession() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this session?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                JsonObject dto = new JsonObject();
                dto.addProperty("userId", userId);

                ApiClient.post("session", "/session/" + sessionId + "/cancel", dto);
                JOptionPane.showMessageDialog(this, "Session cancelled!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void completeSession() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Mark this session as completed? Attendance will be updated for all participants.",
                "Complete Session", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                JsonObject dto = new JsonObject();
                dto.addProperty("organizerId", userId);

                // Poziv ka backendu koji vrši sinhronu komunikaciju sa User servisom
                ApiClient.post("session", "/session/" + sessionId + "/complete", dto);

                JOptionPane.showMessageDialog(this, "Session completed successfully!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void leaveSession() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to leave? This will be recorded as a penalty for your attendance percentage.",
                "Leave Session", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Pozivamo backend endpoint koji smo definisali za napuštanje
                ApiClient.post("session", "/session/" + sessionId + "/leave/" + userId, new JsonObject());

                JOptionPane.showMessageDialog(this, "You have successfully left the session.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}