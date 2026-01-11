package com.raf.gaminggui.gui;

import com.google.gson.JsonObject;
import com.raf.gaminggui.util.ApiClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Gaming Sessions - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this::handleLogin);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> openRegisterFrame());
        gbc.gridy = 4;
        panel.add(registerButton, gbc);

        add(panel);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim(); // Dodat trim() da uklonimo slučajne razmake
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Molimo popunite sva polja", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Kreiranje JSON-a za slanje
            JsonObject credentials = new JsonObject();
            credentials.addProperty("username", username);
            credentials.addProperty("password", password);

            // Pozivanje API-ja
            System.out.println("Pokušaj logina za korisnika: " + username);
            String response = ApiClient.post("user", "/user/login", credentials);

            if (response == null || response.isEmpty()) {
                throw new Exception("Server je vratio prazan odgovor (null). Proverite da li je nalog aktivan.");
            }

            JsonObject jsonResponse = ApiClient.getGson().fromJson(response, JsonObject.class);

            // PROVERA POLJA: Neki backend-i koriste "token", neki "jwt", neki "accessToken"
            String tokenKey = "token"; // Podrazumevano
            if (!jsonResponse.has("token") && jsonResponse.has("jwt")) {
                tokenKey = "jwt";
            } else if (!jsonResponse.has("token") && jsonResponse.has("accessToken")) {
                tokenKey = "accessToken";
            }

            if (!jsonResponse.has(tokenKey)) {
                throw new Exception("JSON odgovor ne sadrži token. Dobijeno: " + response);
            }

            String token = jsonResponse.get(tokenKey).getAsString();
            ApiClient.setToken(token);

            // Dekodiranje JWT-a da bismo izvukli ulogu i ID
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new Exception("Neispravan format tokena.");
            }

            String payload = new String(java.util.Base64.getDecoder().decode(parts[1]));

            JsonObject claims = ApiClient.getGson().fromJson(payload, JsonObject.class);

            // PROVERA ULOGE: Proveri da li se polje u tvom JWT-u zove "role" ili "roles" ili "claim"
            String role = "";
            if (claims.has("role")) {
                role = claims.get("role").getAsString();
            } else if (claims.has("roles")) {
                role = claims.get("roles").getAsString();
            }
            dispose();

            // Otvaranje odgovarajućeg prozora na osnovu uloge
            if (role.contains("ADMIN")) {
                new AdminFrame().setVisible(true);
            } else {
                long userId = claims.has("id") ? claims.get("id").getAsLong() : 0L;
                new PlayerFrame(userId).setVisible(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace(); // Veoma bitno: Ispisuje tačnu liniju gde je puklo u IntelliJ konzoli
            JOptionPane.showMessageDialog(this, "Login neuspešan: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegisterFrame() {
        new RegisterFrame().setVisible(true);
    }
}