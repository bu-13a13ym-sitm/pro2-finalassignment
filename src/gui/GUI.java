package gui;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import processor.*;
import processor.exceptions.*;
import sql.*;
import sql.account.*;
import sql.product.*;
import sql.exception.*;

public class GUI {
    public JFrame frame;
    public Processor processor;

    public GUI () {
        try {
            this.processor = new Processor();
        } catch (ConnectionFailedException e) {
            JOptionPane.showMessageDialog(null, "Failed to start", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setGBCgrid(GridBagConstraints gbc, int x, int y, int width, int height) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
    }

    public void createFrame() {
        this.frame = new JFrame("Clothes Rental System");
        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logout();
                processor.disconnect();
                frame.dispose();
            }
        });
        this.frame.pack();
        this.frame.setVisible(true);
    }

    class ShowLoginPage extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            showLoginPage();
        }
    }

    public void showLoginPage() {
        this.frame.getContentPane().removeAll();

        JLabel titleLabel = new JLabel("<html><h1>Clothes Rental System</h1></html>");
        JLabel infoLabel = new JLabel("Fulfill the following fields to login the system.");
        JLabel errorLabel = new JLabel();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("password:");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setEchoChar('*');

        JLabel registerLabel = new JLabel("<html><u><font color='blue'>Register new account.</font></u></html>");
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new ShowRegisterPage());

        JButton loginButton = new JButton("LOGIN");
        loginButton.addActionListener(new Login(emailField, passwordField, errorLabel));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 3, 2, 3);
        int currRow = 0;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        panel.add(titleLabel, gbc);
        currRow += 3;

        this.setGBCgrid(gbc, 0, currRow, 4, 1);
        panel.add(infoLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 1);
        panel.add(errorLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 1, 1);
        panel.add(emailLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        panel.add(emailField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 1, 1);
        panel.add(passwordLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        panel.add(passwordField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 2, 1);
        panel.add(registerLabel, gbc);

        this.setGBCgrid(gbc, 4, currRow, 1, 1);
        panel.add(loginButton, gbc);

        panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        this.frame.getContentPane().add(panel, BorderLayout.CENTER);

        this.frame.revalidate();
        this.frame.repaint();
        this.frame.pack();
        if (!this.frame.isVisible()) this.frame.setVisible(true);
    }

    class Login implements ActionListener {
        private final JTextField emailField;
        private final JPasswordField passwordField;
        private final JLabel errorLabel;

        public Login(JTextField emailField, JPasswordField passwordField, JLabel errorLabel) {
            this.emailField = emailField;
            this.passwordField = passwordField;
            this.errorLabel = errorLabel;
        }

        public void actionPerformed(ActionEvent e) {
            String emailStr = this.emailField.getText().trim();
            String passwordStr = new String(this.passwordField.getPassword()).trim();
            try {
                if (processor.loginCheck(emailStr, passwordStr)) showProductPage();
            } catch (Exception ex) {
                this.errorLabel.setText(ex.getMessage());
            }
        }
    }

    class Logout implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logout();
        }
    }

    public void logout() {
        this.processor.logout();
        this.showLoginPage();
    }

    class ShowRegisterPage extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            showRegisterPage();
        }
    }

    public void showRegisterPage() {
        this.frame.getContentPane().removeAll();

        JLabel titleLabel = new JLabel("<html><h2>Register new Account</h2></html>");
        JLabel infoLabel = new JLabel("Fulfill the following fields to register your account.");
        JLabel errorLabel = new JLabel();

        JLabel userNameLabel = new JLabel("user name");
        JTextField userNameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("password");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setEchoChar('*');

        JLabel passwordLabel2 = new JLabel("Enter your password again.");
        JPasswordField passwordField2 = new JPasswordField();
        passwordField2.setEchoChar('*');

        JLabel loginLabel = new JLabel("<html><u><font color='blue'>Back to the login page.</font></u></html>");
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new ShowLoginPage());

        JButton registerButton = new JButton("REGISTER");
        registerButton.addActionListener(new Register(userNameField, emailField, passwordField, passwordField2, errorLabel));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 3, 2, 3);
        int currRow = 0;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        panel.add(titleLabel, gbc);
        currRow += 3;

        this.setGBCgrid(gbc, 0, currRow, 4, 1);
        panel.add(infoLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 1);
        panel.add(errorLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 1);
        panel.add(userNameLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        panel.add(userNameField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 1, 1);
        panel.add(emailLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        panel.add(emailField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 1, 1);
        panel.add(passwordLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        panel.add(passwordField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 4, 1);
        panel.add(passwordLabel2, gbc);
        currRow++;
        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        panel.add(passwordField2, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 2, 1);
        panel.add(loginLabel, gbc);

        this.setGBCgrid(gbc, 4, currRow, 1, 1);
        panel.add(registerButton, gbc);

        panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        this.frame.getContentPane().add(panel, BorderLayout.CENTER);

        this.frame.revalidate();
        this.frame.repaint();
        this.frame.pack();
        if (!this.frame.isVisible()) this.frame.setVisible(true);
    }

    class Register implements ActionListener {
        JTextField userNameField;
        JTextField emailField;
        JPasswordField passwordField;
        JPasswordField passwordField2;
        JLabel errorLabel;

        public Register(JTextField userNameField, JTextField emailField, JPasswordField passwordField, JPasswordField passwordField2, JLabel errorLabel) {
            this.userNameField = userNameField;
            this.emailField = emailField;
            this.passwordField = passwordField;
            this.passwordField2 = passwordField2;
            this.errorLabel = errorLabel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String userNameStr = this.userNameField.getText().trim();
            String emailStr = this.emailField.getText().trim();
            String passwordStr = new String(this.passwordField.getPassword()).trim();
            String passwordStr2 = new String(this.passwordField2.getPassword()).trim();
            if (!passwordStr.equals(passwordStr2)) {
                this.errorLabel.setText("passwords not match.");
                return;
            }

            try {
                if (processor.newCustomer(userNameStr, emailStr, passwordStr)) {
                    JOptionPane.showMessageDialog(frame, "Register completed.", "Register Completed", JOptionPane.INFORMATION_MESSAGE);
                    showProductPage();
                }
            } catch (Exception ex) {
                this.errorLabel.setText(ex.getMessage());
            }
        }
    }

    public void showProductPage() {
        //
    }

    public void showProducts() {
        //
    }

    public void showProductDetail() {
        //
    }

    public void rental() {
        //
    }

    public void reserve() {
        //
    }

    public void showRentalStatePage() {
        //
    }

    public void showReturnDialog() {
        //
    }

    public void returnProduct() {
        //
    }

    public void showReserveCancelDialog() {
        //
    }

    public void reserveCancel() {
        //
    }
}
