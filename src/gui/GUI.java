package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import processor.*;
import processor.exceptions.*;
import sql.product.*;

public class GUI {
    public JFrame frame;
    public Processor processor;

    public GUI () {
        try {
            this.processor = new Processor();
        } catch (DatabaseErrorException ex) {
            JOptionPane.showMessageDialog(null, "Failed to start", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setGBCgrid(GridBagConstraints gbc, int x, int y, int width, int height) {
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
            public void windowClosing(WindowEvent ev) {
                logout();
                try {
                    processor.disconnect();
                } catch (DatabaseErrorException ex) {
                    //
                }
                frame.dispose();
            }
        });
        this.frame.pack();
        this.frame.setVisible(true);
    }

    private class ShowLoginPage extends MouseAdapter {
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
        gbc.insets = new Insets(5, 2, 5, 2);
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 1, 1);
        panel.add(passwordLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 2, 1);
        panel.add(registerLabel, gbc);

        this.setGBCgrid(gbc, 4, currRow, 1, 1);
        panel.add(loginButton, gbc);

        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        this.frame.getContentPane().add(panel, BorderLayout.CENTER);

        this.frame.revalidate();
        this.frame.repaint();
        this.frame.pack();
        if (!this.frame.isVisible()) this.frame.setVisible(true);
    }

    private class Login implements ActionListener {
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
            } catch (EmptyInputException | EmailPatternException | IncorrectInputException | NoAccountException | DatabaseErrorException ex) {
                this.errorLabel.setText(ex.getMessage());
            }
        }
    }

    private class Logout implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logout();
        }
    }

    private void logout() {
        this.processor.logout();
        this.showLoginPage();
    }

    private class ShowRegisterPage extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            showRegisterPage();
        }
    }

    private void showRegisterPage() {
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
        gbc.insets = new Insets(5, 2, 5, 2);
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
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userNameLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(userNameField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 1, 1);
        panel.add(emailLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 1, 1);
        panel.add(passwordLabel, gbc);
        currRow++;

        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 4, 1);
        panel.add(passwordLabel2, gbc);
        currRow++;
        this.setGBCgrid(gbc, 0, currRow, 4, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField2, gbc);
        currRow += 2;

        this.setGBCgrid(gbc, 0, currRow, 2, 1);
        panel.add(loginLabel, gbc);

        this.setGBCgrid(gbc, 4, currRow, 1, 1);
        panel.add(registerButton, gbc);

        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        this.frame.getContentPane().add(panel, BorderLayout.CENTER);

        this.frame.revalidate();
        this.frame.repaint();
        this.frame.pack();
        if (!this.frame.isVisible()) this.frame.setVisible(true);
    }

    private class Register implements ActionListener {
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
                    showLoginPage();
                }
            } catch (EmptyInputException | EmailPatternException| AlreadyRegisteredException | DatabaseErrorException ex) {
                this.errorLabel.setText(ex.getMessage());
            }
        }
    }

    private class ShowProductPage implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showProductPage();
        }
    }

    private void showProductPage() {
        this.frame.getContentPane().removeAll();

        JPanel headerPanel = new JPanel(new BorderLayout());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton showRentalStatePageButton = new JButton("Show Rental State Page");
        showRentalStatePageButton.addActionListener(new ShowRentalStatePage());
        rightPanel.add(showRentalStatePageButton);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernamLabel = new JLabel("Username: " + processor.getCurrentUser().getUserName());
        JLabel userIdLabel = new JLabel("User ID: " + processor.getCurrentUser().getUserID());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new Logout());

        leftPanel.add(usernamLabel);
        leftPanel.add(userIdLabel);
        leftPanel.add(logoutButton);

        headerPanel.add(rightPanel, BorderLayout.EAST);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        JLabel errorLabel = new JLabel();

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

        JPanel resultPanel = new JPanel();

        JLabel minPriceLabel = new JLabel("Min Price:");
        JTextField minPriceField = new JTextField();

        JLabel maxPriceLabel = new JLabel("Max Price:");
        JTextField maxPriceField = new JTextField();

        JCheckBox immCheckBox = new JCheckBox("Available Immediately");

        JButton searchButton = new JButton("SEARCH");
        searchButton.addActionListener(new ShowProducts(errorLabel, minPriceField, maxPriceField, immCheckBox, resultPanel));

        searchPanel.add(minPriceLabel);
        searchPanel.add(minPriceField);
        
        searchPanel.add(maxPriceLabel);
        searchPanel.add(maxPriceField);

        searchPanel.add(immCheckBox);

        searchPanel.add(searchButton);

        showProducts(null, null, null, resultPanel, errorLabel);

        JScrollPane resultScrollPanel = new JScrollPane(resultPanel);
        resultScrollPanel.createVerticalScrollBar();
        resultScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(headerPanel);
        panel.add(searchPanel);
        panel.add(resultScrollPanel);

        panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        this.frame.getContentPane().add(panel, BorderLayout.CENTER);

        this.frame.revalidate();
        this.frame.repaint();
        this.frame.pack();
        if (!this.frame.isVisible()) this.frame.setVisible(true);
    }
    
    private class ShowProducts implements ActionListener {
        JLabel errorLabel;
        JTextField minPriceField;
        JTextField maxPriceField;
        JCheckBox immCheckBox;
        JPanel resultPanel;

        public ShowProducts(JLabel errorLabel, JTextField minPriceField, JTextField maxPriceField, JCheckBox immCheckBox, JPanel resultPanel) {
            this.errorLabel = errorLabel;
            this.minPriceField = minPriceField;
            this.maxPriceField = maxPriceField;
            this.immCheckBox = immCheckBox;
            this.resultPanel = resultPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String minPriceStr = this.minPriceField.getText().trim();
            Integer minPrice = (!minPriceStr.isEmpty()) ? Integer.parseInt(minPriceStr) : null;
            String maxPriceStr = this.maxPriceField.getText().trim();
            Integer maxPrice = (!maxPriceStr.isEmpty()) ? Integer.parseInt(maxPriceStr) : null;
            Boolean immediate = this.immCheckBox.isSelected();

            showProducts(minPrice, maxPrice, immediate, this.resultPanel, this.errorLabel);
        }
    }

    private void showProducts(Integer minPrice, Integer maxPrice, Boolean immediate, JPanel resultPanel, JLabel errorLabel) {
        resultPanel.removeAll();

        try {
            ArrayList<Product> searchResults = this.processor.getProducts(minPrice, maxPrice, immediate);
            resultPanel.setLayout(new GridLayout(0, 2, 10, 10));
            
            for (Product product : searchResults) {
                JPanel productPanel = new JPanel(new BorderLayout());

                ImageIcon imageIcon;
                File imageFile = new File(product.getImageURL());
                if (imageFile.exists()) imageIcon = new ImageIcon(product.getImageURL());
                else imageIcon = new ImageIcon("images/no_image.png");
                Image productImg = imageIcon.getImage();
                JLabel imgLabel = new JLabel(new ImageIcon(productImg));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

                JPanel upperRow = new JPanel(new BorderLayout());
                
                JLabel nameLabel = new JLabel(product.getProductName());
                JLabel rentalFeeLabel = new JLabel("\u00A5" + product.getRentalFee());
                rentalFeeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

                upperRow.add(nameLabel, BorderLayout.WEST);
                upperRow.add(rentalFeeLabel, BorderLayout.EAST);

                JPanel lowerRow = new JPanel(new BorderLayout());

                JLabel immLabel = new JLabel();
                immLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                if (product.getCurrentStock() == 0) {
                    LocalDate earliestRentalStart = product.getEarliestRentalStart();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd", Locale.ENGLISH);
                    immLabel.setText("Earliest Available Date: " + earliestRentalStart.format(formatter));
                } else immLabel.setText("<html><span style='color: orange;'>Available Immediately!</span></html>");

                lowerRow.add(immLabel, BorderLayout.EAST);

                infoPanel.add(upperRow);
                infoPanel.add(lowerRow);

                productPanel.add(imgLabel, BorderLayout.NORTH);
                productPanel.add(infoPanel, BorderLayout.SOUTH);

                productPanel.addMouseListener(new ShowProductDetail(product));

                productPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                resultPanel.add(productPanel);
            }

            resultPanel.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
        } catch (EmptyInputException | NoProductFoundException | DatabaseErrorException ex) {
            resultPanel.setLayout(new GridLayout(0, 1));
            errorLabel.setText(ex.getMessage());
            resultPanel.add(errorLabel);
            resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private class ShowProductDetail extends MouseAdapter {
        Product product;

        public ShowProductDetail(Product product) {
            this.product = product;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            showProductDetail(product);
        }
    }

    private void showProductDetail(Product product) {
        JDialog detailDialog = new JDialog(this.frame, "Product Detail", true);
        detailDialog.getContentPane().setLayout(new BoxLayout(detailDialog.getContentPane(), BoxLayout.Y_AXIS));

        ImageIcon imageIcon;
        File imageFile = new File(product.getImageURL());
        if (imageFile.exists()) imageIcon = new ImageIcon(product.getImageURL());
        else imageIcon = new ImageIcon("images/no_image.png");
        Image productImg = imageIcon.getImage();
        JLabel imgLabel = new JLabel(new ImageIcon(productImg));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel nameLabel = new JLabel("<html><h1>" + product.getProductName() + "</h1></html>");
        JLabel rentalFeeLabel = new JLabel("\u00A5" + product.getRentalFee());
        rentalFeeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(nameLabel, BorderLayout.WEST);
        headerPanel.add(rentalFeeLabel, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));

        JTextArea descriptionArea = new JTextArea(product.getDescription().replace(".", ".\n").replace(".\n ", ".\n"));
        descriptionArea.setEditable(false);

        detailPanel.add(descriptionArea);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        infoPanel.add(headerPanel);
        infoPanel.add(detailPanel);
        
        JPanel btnPanel = new JPanel(new BorderLayout());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(ev -> detailDialog.dispose());

        btnPanel.add(closeButton, BorderLayout.WEST);

        if (product.getCurrentStock() > 0) {
            JButton rentalButton = new JButton("RENTAL");
            rentalButton.addActionListener(new Rental(product));

            btnPanel.add(rentalButton, BorderLayout.EAST);
        } else {
            JLabel earliestRentalStartLabel = new JLabel();
            LocalDate earliestRentalStart = product.getEarliestRentalStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd", Locale.ENGLISH);
            earliestRentalStartLabel.setText("Earliest Available Date: " + earliestRentalStart.format(formatter));
            earliestRentalStartLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JButton reserveButton = new JButton("RESERVE");
            reserveButton.addActionListener(new Reserve(product));

            btnPanel.add(earliestRentalStartLabel, BorderLayout.CENTER);
            btnPanel.add(reserveButton, BorderLayout.EAST);
        }

        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        detailDialog.getContentPane().add(imgLabel);
        detailDialog.getContentPane().add(infoPanel);
        detailDialog.getContentPane().add(btnPanel);

        detailDialog.pack();
        detailDialog.setLocationRelativeTo(frame);
        detailDialog.setVisible(true);
    }

    private class Rental implements ActionListener {

        Product product;

        public Rental(Product product) {
            this.product = product;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            int check = JOptionPane.showConfirmDialog(frame, "Do you want to rent this product?", "Rental Confirmation", JOptionPane.YES_NO_OPTION);
            if (check == JOptionPane.YES_OPTION) {
                try{
                    Boolean success = processor.executeRental(product);
                    JDialog rentalDialog;

                    if (success) {
                        rentalDialog = new JDialog(frame, "Rental Success", true);
                        JLabel successLabel = new JLabel("Rental has been successfully completed.");
                        successLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                        rentalDialog.add(successLabel, BorderLayout.CENTER);
                        rentalDialog.setLocationRelativeTo(frame);
                        rentalDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        rentalDialog.pack();

                        rentalDialog.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                rentalDialog.dispose();
                                showProductPage();
                            }
                        });

                        rentalDialog.setVisible(true);
                    }

                } catch (NotYetLoginException | NoProductFoundException | DatabaseErrorException ex) {
                    JDialog errorDialog = new JDialog(frame, "Rental Failed due to an Error", true);
                    JLabel errorLabel = new JLabel(ex.getMessage());
                    errorLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                    errorDialog.add(errorLabel, BorderLayout.CENTER);
                    errorDialog.setLocationRelativeTo(frame);
                    errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    errorDialog.pack();
                    errorDialog.setVisible(true);
                }
            }
        }
    }

    private class Reserve implements ActionListener {

        Product product;

        public Reserve(Product product) {
            this.product = product;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                Boolean success = processor.executeReserve(product);
                JDialog reserveDialog;

                if (success) {
                    reserveDialog = new JDialog(frame, "Reservation Success", true);
                    JLabel successLabel = new JLabel("Reservation has been successfully completed.");
                    successLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                    reserveDialog.add(successLabel, BorderLayout.CENTER);
                    reserveDialog.setLocationRelativeTo(frame);
                    reserveDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    reserveDialog.pack();

                    reserveDialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            reserveDialog.dispose();
                            showProductPage();
                        }
                    });

                    reserveDialog.setVisible(true);
                }

            } catch (NotYetLoginException | NoProductFoundException | DatabaseErrorException ex) {
                JDialog errorDialog = new JDialog(frame, "Reservation Failed due to an Error", true);
                JLabel errorLabel = new JLabel(ex.getMessage());
                errorLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                errorDialog.add(errorLabel, BorderLayout.CENTER);
                errorDialog.setLocationRelativeTo(frame);
                errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                errorDialog.pack();
                errorDialog.setVisible(true);
            }
        }
    }

    private class ShowRentalStatePage implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showRentalStatePage();
        }
    }

    private void showRentalStatePage() {
        this.frame.getContentPane().removeAll();

        JPanel headerPanel = new JPanel(new BorderLayout());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton showProductsButton = new JButton("Show Product Page");
        showProductsButton.addActionListener(new ShowProductPage());
        rightPanel.add(showProductsButton);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Username: " + processor.getCurrentUser().getUserName());
        JLabel userIdLabel = new JLabel("User ID: " + processor.getCurrentUser().getUserID());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new Logout());

        leftPanel.add(logoutButton);
        leftPanel.add(usernameLabel);
        leftPanel.add(userIdLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        JPanel rentalProductPanel = new JPanel();
        this.showRentalProducts(rentalProductPanel);
        JScrollPane rentalScrollPanel = new JScrollPane(rentalProductPanel);
        rentalScrollPanel.createVerticalScrollBar();
        rentalScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel reservedProductPanel = new JPanel();
        this.showReservedProducts(reservedProductPanel);
        JScrollPane reservedScrollPanel = new JScrollPane(reservedProductPanel);
        reservedScrollPanel.createVerticalScrollBar();
        reservedScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        Dimension size1 = rentalScrollPanel.getPreferredSize();
        Dimension size2 = reservedScrollPanel.getPreferredSize();

        int maxWidth = Math.max(size1.width, size2.width);
        int maxHeight = Math.max(size1.height, size2.height);

        Dimension fixedSize = new Dimension(maxWidth, maxHeight);

        rentalScrollPanel.setPreferredSize(fixedSize);
        reservedScrollPanel.setPreferredSize(fixedSize);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        this.setGBCgrid(gbc, 0, 0, 1, 1);
        resultPanel.add(rentalScrollPanel, gbc);

        this.setGBCgrid(gbc, 1, 0, 1, 1);
        resultPanel.add(reservedScrollPanel, gbc);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(headerPanel);
        panel.add(resultPanel);

        panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        this.frame.getContentPane().add(panel, BorderLayout.CENTER);

        this.frame.revalidate();
        this.frame.repaint();
        this.frame.pack();
        if (!this.frame.isVisible()) this.frame.setVisible(true);
    }

    private void showRentalProducts(JPanel rentalProductPanel) {
        rentalProductPanel.removeAll();

        try {
            ArrayList<RentalProduct> rentalProducts = this.processor.getRentalProducts();
            rentalProductPanel.setLayout(new BoxLayout(rentalProductPanel, BoxLayout.Y_AXIS));

            for (RentalProduct rentalProduct : rentalProducts) {
                JPanel productPanel = new JPanel();
                productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.X_AXIS));

                ImageIcon imageIcon;
                File imageFile = new File(rentalProduct.getImageURL());
                if (imageFile.exists()) imageIcon = new ImageIcon(rentalProduct.getImageURL());
                else imageIcon = new ImageIcon("images/no_image.png");
                Image productImg = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(productImg));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                
                JLabel nameLabel = new JLabel();
                nameLabel.setText("<html><h1>" + rentalProduct.getProductName() + "</h1></html>");

                JLabel rentalDeadlineLabel = new JLabel();
                LocalDate rentalDeadline = rentalProduct.getRentalDeadLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd", Locale.ENGLISH);
                String rentalDeadlineStr = rentalDeadline.format(formatter);
                if (LocalDate.now().isAfter(rentalDeadline)) rentalDeadlineLabel.setText("<html><b><span style='color : red;'>" + rentalDeadlineStr + "</span></b></html>");
                else rentalDeadlineLabel.setText(rentalDeadlineStr);

                JButton returnButton = new JButton("RETURN");
                returnButton.addActionListener(new ShowReturnDialog(rentalProduct));

                productPanel.add(imgLabel);
                productPanel.add(nameLabel);
                productPanel.add(rentalDeadlineLabel);
                productPanel.add(returnButton);

                productPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                rentalProductPanel.add(productPanel);
            }

            rentalProductPanel.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
        } catch (NoProductFoundException | DatabaseErrorException ex) {
            rentalProductPanel.setLayout(new GridLayout(0, 1));
            JLabel errorLabel = new JLabel(ex.getMessage());
            rentalProductPanel.add(errorLabel);
            rentalProductPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        }

        rentalProductPanel.revalidate();
        rentalProductPanel.repaint();
    }

    private void showReservedProducts(JPanel reservedProductPanel) {
        reservedProductPanel.removeAll();

        try {
            ArrayList<ReservedProduct> reservedProducts = this.processor.getReservedProduct();
            reservedProductPanel.setLayout(new BoxLayout(reservedProductPanel, BoxLayout.Y_AXIS));

            for (ReservedProduct reservedProduct : reservedProducts) {
                JPanel productPanel = new JPanel();
                productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.X_AXIS));

                ImageIcon imageIcon;
                File imageFile = new File(reservedProduct.getImageURL());
                if (imageFile.exists()) imageIcon = new ImageIcon(reservedProduct.getImageURL());
                else imageIcon = new ImageIcon("images/no_image.png");
                Image productImg = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(productImg));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                
                JLabel nameLabel = new JLabel();
                nameLabel.setText("<html><h1>" + reservedProduct.getProductName() + "</h1></html>");

                JLabel earliestRentalStartLabel = new JLabel();
                LocalDate earliestRentalStart = reservedProduct.getEarliestRentalStart();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd", Locale.ENGLISH);
                String earliestRentalStartStr = earliestRentalStart.format(formatter);
                earliestRentalStartLabel.setText(earliestRentalStartStr);

                JButton returnButton = new JButton("RESERVE CANCEL");
                returnButton.addActionListener(new ShowReserveCancelDialog(reservedProduct));

                productPanel.add(imgLabel);
                productPanel.add(nameLabel);
                productPanel.add(earliestRentalStartLabel);
                productPanel.add(returnButton);

                productPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                reservedProductPanel.add(productPanel);
            }

            reservedProductPanel.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
        } catch (NoProductFoundException | DatabaseErrorException ex) {
            reservedProductPanel.setLayout(new GridLayout(0, 1));
            JLabel errorLabel = new JLabel(ex.getMessage());
            reservedProductPanel.add(errorLabel);
            reservedProductPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        }

        reservedProductPanel.revalidate();
        reservedProductPanel.repaint();
    }

    private class ShowReturnDialog implements ActionListener {
        RentalProduct rentalProduct;

        public ShowReturnDialog(RentalProduct rentalProduct) {
            this.rentalProduct = rentalProduct;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog returnDialog = new JDialog(frame, "Return Confirmation", true);
            returnDialog.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            ImageIcon imageIcon;
            File imageFile = new File(this.rentalProduct.getImageURL());
            if (imageFile.exists()) imageIcon = new ImageIcon(this.rentalProduct.getImageURL());
            else imageIcon = new ImageIcon("images/no_image.png");
            Image productImg = imageIcon.getImage();
            JLabel imgLabel = new JLabel(new ImageIcon(productImg));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel nameLabel = new JLabel("<html><h1>" + this.rentalProduct.getProductName() + "</h1></html>");

            infoPanel.add(imgLabel);
            infoPanel.add(nameLabel);

            if (rentalProduct.getRentalDeadLine().isBefore(LocalDate.now())) {
                JPanel overduePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JLabel overdueLabel = new JLabel("<html><span style='color: red;'>Overdue!</span></html>");
                overdueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                JLabel overdueFeeLabel = new JLabel("Overdue Fee: \u00A5" + (this.rentalProduct.getRentalFee() / 2));

                overduePanel.add(overdueLabel);
                overduePanel.add(overdueFeeLabel);

                infoPanel.add(overduePanel);
            }
            
            JPanel buttonPanel = new JPanel();
            JButton returnButton = new JButton("Return");
            returnButton.addActionListener(new ReturnProduct(this.rentalProduct, returnDialog));
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(ev -> returnDialog.dispose());

            buttonPanel.add(returnButton);
            buttonPanel.add(cancelButton);

            returnDialog.add(infoPanel, BorderLayout.CENTER);
            returnDialog.add(buttonPanel, BorderLayout.SOUTH);
            returnDialog.pack();
            returnDialog.setVisible(true);
        }
    }

    private class ReturnProduct implements ActionListener {
        RentalProduct rentalProduct;
        JDialog returnDialog;

        public ReturnProduct(RentalProduct rentalProduct, JDialog returnDialog) {
            this.rentalProduct = rentalProduct;
            this.returnDialog = returnDialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                if (processor.executeReturn(this.rentalProduct)) {
                    JDialog returnCompleteDialog = new JDialog(this.returnDialog, "Return Success", true);
                    JLabel successLabel = new JLabel("Return has been successfully completed.");
                    successLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                    returnCompleteDialog.add(successLabel, BorderLayout.CENTER);
                    returnCompleteDialog.setLocationRelativeTo(frame);
                    returnCompleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    returnCompleteDialog.pack();
                    returnCompleteDialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent ev) {
                            returnDialog.dispose();
                            showRentalStatePage();
                        }
                    });
                    returnCompleteDialog.setVisible(true);
                }
            } catch (NotYetLoginException | NoProductFoundException | DatabaseErrorException ex) {
                JDialog errorDialog = new JDialog(frame, "Return Failed by Error", true);
                JLabel errorLabel = new JLabel(ex.getMessage());
                errorLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                errorDialog.add(errorLabel, BorderLayout.CENTER);
                errorDialog.setLocationRelativeTo(frame);
                errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                errorDialog.pack();
                errorDialog.setVisible(true);
            } 
        }
    }

    private class ShowReserveCancelDialog implements ActionListener {
        ReservedProduct reservedProduct;

        public ShowReserveCancelDialog(ReservedProduct reservedProduct) {
            this.reservedProduct = reservedProduct;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog reserveCancelDialog = new JDialog(frame, "Reservation Cancel Confirmation", true);
            reserveCancelDialog.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            ImageIcon imageIcon;
            File imageFile = new File(this.reservedProduct.getImageURL());
            if (imageFile.exists()) imageIcon = new ImageIcon(this.reservedProduct.getImageURL());
            else imageIcon = new ImageIcon("images/no_image.png");
            Image productImg = imageIcon.getImage();
            JLabel imgLabel = new JLabel(new ImageIcon(productImg));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel nameLabel = new JLabel("<html><h1>" + this.reservedProduct.getProductName() + "</h1></html>");
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

            infoPanel.add(imgLabel);
            infoPanel.add(nameLabel);

            JPanel buttonPanel = new JPanel();
            JButton cancelButton = new JButton("Cancel Reservation");
            cancelButton.addActionListener(new ReserveCancel(this.reservedProduct, reserveCancelDialog));
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(ev -> reserveCancelDialog.dispose());

            buttonPanel.add(cancelButton);
            buttonPanel.add(closeButton);

            reserveCancelDialog.add(infoPanel, BorderLayout.CENTER);
            reserveCancelDialog.add(buttonPanel, BorderLayout.SOUTH);
            reserveCancelDialog.pack();
            reserveCancelDialog.setVisible(true);
        }
        
    }

    private class ReserveCancel implements ActionListener {
        ReservedProduct reservedProduct;
        JDialog reserveCancelDialog;

        public ReserveCancel(ReservedProduct reservedProduct, JDialog reserveCancelDialog) {
            this.reservedProduct = reservedProduct;
            this.reserveCancelDialog = reserveCancelDialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (processor.executeReserveCancel(this.reservedProduct)) {
                    JDialog cancelCompleteDialog = new JDialog(this.reserveCancelDialog, "Reservation Cancel Success", true);
                    JLabel successLabel = new JLabel("Reservation has been successfully canceled.");
                    successLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                    cancelCompleteDialog.add(successLabel, BorderLayout.CENTER);
                    cancelCompleteDialog.setLocationRelativeTo(frame);
                    cancelCompleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    cancelCompleteDialog.pack();
                    cancelCompleteDialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent ev) {
                            reserveCancelDialog.dispose();
                            showRentalStatePage();
                        }
                    });
                    cancelCompleteDialog.setVisible(true);
                }
            } catch (NotYetLoginException | NoProductFoundException | DatabaseErrorException ex) {
                JDialog errorDialog = new JDialog(frame, "Reservation Cancel Failed by Error", true);
                JLabel errorLabel = new JLabel(ex.getMessage());
                errorLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                errorDialog.add(errorLabel, BorderLayout.CENTER);
                errorDialog.setLocationRelativeTo(frame);
                errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                errorDialog.pack();
                errorDialog.setVisible(true);
            }
        }
    }
}
