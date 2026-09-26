import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OnlineReservation extends JFrame {

    static Map<Integer, Booking> bookings = new HashMap<>();
    static int nextPNR = 10001;

    JTextField usernameField;
    JPasswordField passwordField;

    public OnlineReservation() {
        setTitle("Online Reservation System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("LOGIN");
        panel.add(new JLabel());
        panel.add(loginButton);

        loginButton.addActionListener(e -> login());

        add(panel);
        setVisible(true);
    }

    void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("1234")) {
            dispose();
            new ReservationWindow();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Username or Password"
            );
        }
    }

    public static void main(String[] args) {
        new OnlineReservation();
    }
}

class Booking {

    int pnr;
    String passengerName;
    String trainNumber;
    String trainName;
    String classType;
    String journeyDate;
    String source;
    String destination;

    Booking(
            int pnr,
            String passengerName,
            String trainNumber,
            String trainName,
            String classType,
            String journeyDate,
            String source,
            String destination) {

        this.pnr = pnr;
        this.passengerName = passengerName;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.classType = classType;
        this.journeyDate = journeyDate;
        this.source = source;
        this.destination = destination;
    }
}

class ReservationWindow extends JFrame {

    JTextField passengerField;
    JTextField trainNumberField;
    JTextField trainNameField;
    JTextField dateField;
    JTextField sourceField;
    JTextField destinationField;

    JComboBox<String> classBox;

    ReservationWindow() {

        setTitle("Train Reservation");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );

        panel.add(new JLabel("Passenger Name:"));
        passengerField = new JTextField();
        panel.add(passengerField);

        panel.add(new JLabel("Train Number:"));
        trainNumberField = new JTextField();
        panel.add(trainNumberField);

        panel.add(new JLabel("Train Name:"));
        trainNameField = new JTextField();
        panel.add(trainNameField);

        panel.add(new JLabel("Class Type:"));

        String[] classes = {
                "AC",
                "Sleeper",
                "General"
        };

        classBox = new JComboBox<>(classes);
        panel.add(classBox);

        panel.add(new JLabel("Journey Date:"));
        dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Source:"));
        sourceField = new JTextField();
        panel.add(sourceField);

        panel.add(new JLabel("Destination:"));
        destinationField = new JTextField();
        panel.add(destinationField);

        JButton bookButton = new JButton("BOOK TICKET");
        JButton cancelButton = new JButton("CANCEL TICKET");

        panel.add(bookButton);
        panel.add(cancelButton);

        bookButton.addActionListener(e -> bookTicket());

        cancelButton.addActionListener(e -> {
            new CancellationWindow();
        });

        add(panel);
        setVisible(true);
    }

    void bookTicket() {

        String passenger = passengerField.getText();
        String trainNumber = trainNumberField.getText();
        String trainName = trainNameField.getText();
        String classType = (String) classBox.getSelectedItem();
        String date = dateField.getText();
        String source = sourceField.getText();
        String destination = destinationField.getText();

        if (passenger.isEmpty() ||
                trainNumber.isEmpty() ||
                trainName.isEmpty() ||
                date.isEmpty() ||
                source.isEmpty() ||
                destination.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required fields"
            );

            return;
        }

        try {
            Integer.parseInt(trainNumber);
        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Train number must be numeric"
            );

            return;
        }

        int pnr = OnlineReservation.nextPNR++;

        Booking booking = new Booking(
                pnr,
                passenger,
                trainNumber,
                trainName,
                classType,
                date,
                source,
                destination
        );

        OnlineReservation.bookings.put(pnr, booking);

        JOptionPane.showMessageDialog(
                this,
                "BOOKING SUCCESSFUL!\n\n" +
                "PNR Number : " + pnr + "\n" +
                "Passenger  : " + passenger + "\n" +
                "Train      : " + trainName + "\n" +
                "Class      : " + classType + "\n" +
                "Date       : " + date + "\n" +
                "From       : " + source + "\n" +
                "To         : " + destination
        );

        clearFields();
    }

    void clearFields() {

        passengerField.setText("");
        trainNumberField.setText("");
        trainNameField.setText("");
        dateField.setText("");
        sourceField.setText("");
        destinationField.setText("");
    }
}

class CancellationWindow extends JFrame {

    JTextField pnrField;
    JTextArea detailsArea;

    CancellationWindow() {

        setTitle("Cancel Ticket");
        setSize(450, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel top = new JPanel();

        top.add(new JLabel("Enter PNR:"));

        pnrField = new JTextField(10);
        top.add(pnrField);

        JButton fetchButton = new JButton("FETCH");
        top.add(fetchButton);

        panel.add(top, BorderLayout.NORTH);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);

        panel.add(
                new JScrollPane(detailsArea),
                BorderLayout.CENTER
        );

        JButton cancelButton = new JButton("CANCEL TICKET");

        panel.add(cancelButton, BorderLayout.SOUTH);

        fetchButton.addActionListener(e -> fetchBooking());

        cancelButton.addActionListener(e -> cancelBooking());

        add(panel);
        setVisible(true);
    }

    void fetchBooking() {

        try {

            int pnr = Integer.parseInt(
                    pnrField.getText()
            );

            Booking b =
                    OnlineReservation.bookings.get(pnr);

            if (b == null) {

                detailsArea.setText(
                        "PNR not found!"
                );

                return;
            }

            detailsArea.setText(
                    "PNR           : " + b.pnr + "\n" +
                    "Passenger     : " + b.passengerName + "\n" +
                    "Train Number  : " + b.trainNumber + "\n" +
                    "Train Name    : " + b.trainName + "\n" +
                    "Class         : " + b.classType + "\n" +
                    "Journey Date  : " + b.journeyDate + "\n" +
                    "Source        : " + b.source + "\n" +
                    "Destination   : " + b.destination
            );

        } catch (NumberFormatException e) {

            detailsArea.setText(
                    "Enter a valid PNR number"
            );
        }
    }

    void cancelBooking() {

        try {

            int pnr = Integer.parseInt(
                    pnrField.getText()
            );

            Booking b =
                    OnlineReservation.bookings.get(pnr);

            if (b == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "PNR not found!"
                );

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to cancel?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {

                OnlineReservation.bookings.remove(pnr);

                JOptionPane.showMessageDialog(
                        this,
                        "Ticket Cancelled Successfully!"
                );

                detailsArea.setText("");
                pnrField.setText("");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid PNR"
            );
        }
    }
}