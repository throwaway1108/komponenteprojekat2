package com.raf.gaminggui.gui;

import com.google.gson.JsonObject;
import com.raf.gaminggui.util.ApiClient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class RegisterFrame extends JFrame {

    private JTextField emailField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField dateOfBirthField;

    public RegisterFrame() {
        setTitle("Register");
        setSize(400, 400);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Register New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        addField(panel, gbc, "Email:", emailField = new JTextField(20), 1);
        addField(panel, gbc, "First Name:", firstNameField = new JTextField(20), 2);
        addField(panel, gbc, "Last Name:", lastNameField = new JTextField(20), 3);
        addField(panel, gbc, "Username:", usernameField = new JTextField(20), 4);
        addField(panel, gbc, "Password:", passwordField = new JPasswordField(20), 5);
        addField(panel, gbc, "Date of Birth (YYYY-MM-DD):", dateOfBirthField = new JTextField(20), 6);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        gbc.gridy = 8;
        panel.add(cancelButton, gbc);

        add(panel);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void handleRegister() {
        try {
            String email = emailField.getText();

            JsonObject user = new JsonObject();
            user.addProperty("email", email);
            user.addProperty("firstName", firstNameField.getText());
            user.addProperty("lastName", lastNameField.getText());
            user.addProperty("username", usernameField.getText());
            user.addProperty("password", new String(passwordField.getPassword()));
            user.addProperty("dateOfBirth", dateOfBirthField.getText());

            ApiClient.post("user", "/user", user);

            JsonObject activationDto = new JsonObject();
            activationDto.addProperty("email", email);

            ApiClient.post("user", "/user/activate", activationDto);

            JOptionPane.showMessageDialog(this,
                    "Registration successful and account activated! You can now login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}