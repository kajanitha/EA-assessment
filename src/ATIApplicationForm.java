import javax.swing.*; // Importing the Swing library for building graphical user interfaces (GUIs).
import java.awt.*; // Importing the Abstract Window Toolkit (AWT) for GUI components and layout management.
import java.awt.event.ActionEvent; // Importing ActionEvent class for handling action events.
import java.awt.event.ActionListener; // Importing ActionListener interface for receiving action events.
import java.sql.Connection; // Importing Connection interface for database connections.
import java.sql.DriverManager; // Importing DriverManager class for managing database connections.
import java.sql.PreparedStatement; // Importing PreparedStatement interface for executing parameterized SQL queries.
import java.sql.SQLException; // Importing SQLException class for handling SQL exceptions.
import java.text.SimpleDateFormat; // Importing SimpleDateFormat class for formatting dates.
import java.util.Date; // Importing Date class for handling date and time.
import de.wannawork.jcalendar.JCalendarComboBox; // Importing JCalendarComboBox for date selection in the GUI.

public class ATIApplicationForm extends JFrame { // Defining the ATIApplicationForm class that extends JFrame to create a window.

    private JTextField nameField, addressField, yearField; // Declaring text fields for user input.
    private JComboBox<String> courseComboBox; // Declaring a combo box for course selection.
    private JCalendarComboBox dobChooser; // Declaring a calendar combo box for date of birth selection.

    public ATIApplicationForm() { // Constructor for the ATIApplicationForm class.
        setTitle("ATI Application Form"); // Setting the title of the window.
        setSize(600, 500);
        setLayout(new GridLayout(6, 2)); // Setting the layout to a grid with 6 rows and 2 columns.

        add(new JLabel("Name:")); // Adding a label for the name field.
        nameField = new JTextField(); // Initializing the name text field.
        nameField.setPreferredSize(new Dimension(200, 30)); // Setting preferred size for name field.
        add(nameField); // Adding the name text field to the window.

        add(new JLabel("Address:")); // Adding a label for the address field.
        addressField = new JTextField(); // Initializing the address text field.
        addressField.setPreferredSize(new Dimension(200, 30)); // Setting preferred size for address field.
        add(addressField); // Adding the address text field to the window.

        add(new JLabel("Course:")); // Adding a label for the course selection.
        String[] courses = {"HNDIT", "HNDA", "HNDE"}; // Defining an array of course options.
        courseComboBox = new JComboBox<>(courses); // Initializing the combo box with course options.
        add(courseComboBox); // Adding the course combo box to the window.

        add(new JLabel("Year:")); // Adding a label for the year field.
        yearField = new JTextField(); // Initializing the year text field.
        yearField.setPreferredSize(new Dimension(200, 30)); // Setting preferred size for year field.
        add(yearField); // Adding the year text field to the window.

        add(new JLabel("Date of Birth:")); // Adding a label for the date of birth field.
        dobChooser = new JCalendarComboBox(); // Initializing the date of birth chooser.
        add(dobChooser); // Adding the date of birth chooser to the window.

        JButton submitButton = new JButton("Submit"); // Creating a submit button.
        submitButton.setPreferredSize(new Dimension(100, 30)); // Setting preferred size for submit button.
        submitButton.addActionListener(new ActionListener() { // Adding an action listener to handle button clicks.
            @Override
            public void actionPerformed(ActionEvent e) { // Overriding the actionPerformed method.
                submitApplication(); // Calling the method to submit the application when the button is clicked.
            }
        });
        add(submitButton); // Adding the submit button to the window.

        setVisible(true); // Making the window visible.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Setting the default close operation for the window.
    }

    private void submitApplication() { // Method to handle application submission.
        String name = nameField.getText(); // Retrieving the name from the text field.
        String address = addressField.getText(); // Retrieving the address from the text field.
        String course = (String) courseComboBox.getSelectedItem(); // Retrieving the selected course from the combo box.
        String year = yearField.getText(); // Retrieving the year from the text field.
        Date dob = dobChooser.getDate(); // Retrieving the selected date of birth.

        if (name.isEmpty() || year.isEmpty() || dob == null) { // Checking if any required fields are empty.
            JOptionPane.showMessageDialog(this, "Please fill in all fields."); // Showing an error message if fields are empty.
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
                JOptionPane.showMessageDialog(this, "Application submitted successfully."); // Showing success message.
                clearFields(); // Clearing the input fields after successful submission.
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit application."); // Showing error message if submission failed.
            }

            preparedStatement.close(); // Closing the prepared statement.
            connection.close(); // Closing the database connection.
        } catch (SQLException ex) { // Catching SQL exceptions.
            ex.printStackTrace(); // Printing the stack trace for debugging.
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage()); // Showing error message for database errors.
        }
    }

    private void clearFields() { // Method to clear input fields.
        nameField.setText(""); // Clearing the name field.
        addressField.setText(""); // Clearing the address field.
        yearField.setText(""); // Clearing the year field.
        dobChooser.setDate(null); // Clearing the date of birth chooser.
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