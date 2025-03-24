import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import de.wannawork.jcalendar.JCalendarComboBox;

public class ATIApplicationForm extends JFrame {

    private JTextField nameField, addressField, yearField;
    private JComboBox<String> courseComboBox;
    private JCalendarComboBox dobChooser;

    public ATIApplicationForm() {
        setTitle("Application Form for ATI");
        setSize(600, 500);
        setLayout(new GridBagLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make the heading span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the heading
        gbc.insets = new Insets(10, 10, 20, 10); // Add padding below the heading

        JLabel headingLabel = new JLabel("Application Form for ATI");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font and size
        mainPanel.add(headingLabel, gbc);

        gbc.gridy = 1; // Move to the next row
        gbc.gridwidth = 1; // Reset gridwidth
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        mainPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(250, 30));
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(250, 30));
        mainPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Course:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] courses = {"HNDIT", "HNDA", "HNDE"};
        courseComboBox = new JComboBox<>(courses);
        mainPanel.add(courseComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Year:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        yearField = new JTextField();
        yearField.setPreferredSize(new Dimension(250, 30));
        mainPanel.add(yearField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Date of Birth:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dobChooser = new JCalendarComboBox();
        mainPanel.add(dobChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(100, 30));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitApplication();
            }
        });
        mainPanel.add(submitButton, gbc);

        add(mainPanel, new GridBagConstraints());

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void submitApplication() { // Method to handle application submission.
        String name = nameField.getText();
        String address = addressField.getText();
        String course = (String) courseComboBox.getSelectedItem();
        String year = yearField.getText();
        Date dob = dobChooser.getDate();

        if (name.isEmpty() || year.isEmpty() || dob == null) { // Checking if any required fields are empty.
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return; // Exiting the method if validation fails.
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ati_applications", "root", ""); // Establishing a connection to the database.
            String query = "INSERT INTO applications (name, address, course, year, dob) VALUES (?, ?, ?, ?, ?)"; // SQL query to insert application data.
            PreparedStatement preparedStatement = connection.prepareStatement(query); // Preparing the SQL statement.
            preparedStatement.setString(1, name); // Setting the name parameter in the query.
            preparedStatement.setString(2, address); // Setting the address parameter in the query.
            preparedStatement.setString(3, course); // Setting the course parameter in the query.
            preparedStatement.setInt(4, Integer.parseInt(year)); // Setting the year parameter in the query.
            preparedStatement.setDate(5, new java.sql.Date(dob.getTime())); // Setting the date of birth parameter in the query.

            int rowsAffected = preparedStatement.executeUpdate(); // Executing the update and getting the number of affected rows.
            if (rowsAffected > 0) { // Checking if the insertion was successful.
                JOptionPane.showMessageDialog(this, "Application submitted successfully.");
                clearFields(); // Clearing the input fields after successful submission.
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit application.");
            }

            preparedStatement.close(); // Closing the prepared statement.
            connection.close(); // Closing the database connection.
        } catch (SQLException ex) { // Catching SQL exceptions.
            ex.printStackTrace(); // Printing the stack trace for debugging.
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage()); // Showing error message for database errors.
        }
    }

    private void clearFields() { // Method to clear input fields.
        nameField.setText("");
        addressField.setText("");
        yearField.setText("");
        dobChooser.setDate(null);
    }

    public static void main(String[] args) { // Main method to run the application.
        SwingUtilities.invokeLater(new Runnable() { // Using SwingUtilities to ensure GUI updates are on the Event Dispatch Thread.
            @Override
            public void run() { // Overriding the run method.
                new ATIApplicationForm(); // Creating an instance of the ATIApplicationForm class to display the GUI.
            }
        });
    }
}