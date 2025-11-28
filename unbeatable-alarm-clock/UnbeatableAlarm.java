import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Main application class that creates the alarm clock GUI.

public class UnbeatableAlarm extends JFrame {
    private JLabel timeLabel, dateLabel;
    private JTextField hourField, minuteField;
    private JButton setAlarmButton, viewHistoryButton, clearHistoryButton;
    private Timer clockTimer, alarmCheckTimer;
    
    private AlarmManager alarmManager;
    private ChallengePanel challengePanel;
    
    public UnbeatableAlarm() {
        this.alarmManager = new AlarmManager();
        
        setTitle("⏰ Unbeatable Alarm Clock");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(20, 20, 30));
        
        setupUI();
        startClock();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // Sets up the main user interface.

    private void setupUI() {
        // Top panel - Current time and date display
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - Alarm settings
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Challenge panel (hidden initially)
        challengePanel = new ChallengePanel(alarmManager);
        add(challengePanel, BorderLayout.SOUTH);
    }
    
    // Creates the top panel with clock display.

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(25, 25, 40));
        topPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(0, 255, 128), 3, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel timePanel = new JPanel(new GridLayout(2, 1));
        timePanel.setBackground(new Color(25, 25, 40));
        
        timeLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 72));
        timeLabel.setForeground(new Color(0, 255, 128));
        
        dateLabel = new JLabel("", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        dateLabel.setForeground(new Color(150, 150, 200));
        
        timePanel.add(timeLabel);
        timePanel.add(dateLabel);
        topPanel.add(timePanel, BorderLayout.CENTER);
        
        return topPanel;
    }
    
   // Creates the center panel with alarm settings.

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(20, 20, 30));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel setLabel = new JLabel("⏰ Configure Your Wake-Up Time");
        setLabel.setForeground(new Color(255, 200, 0));
        setLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        centerPanel.add(setLabel, gbc);
        
        // Time input panel
        JPanel timeInputPanel = createTimeInputPanel();
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 5;
        centerPanel.add(timeInputPanel, gbc);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 5;
        centerPanel.add(buttonPanel, gbc);
        
        return centerPanel;
    }
    
    // Creates the time input panel.

    private JPanel createTimeInputPanel() {
        JPanel timeInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        timeInputPanel.setBackground(new Color(20, 20, 30));
        
        JLabel hourLabel = new JLabel("Hour:");
        hourLabel.setForeground(Color.WHITE);
        hourLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        timeInputPanel.add(hourLabel);
        
        hourField = createTimeField();
        timeInputPanel.add(hourField);
        
        JLabel colonLabel = new JLabel(":");
        colonLabel.setForeground(new Color(255, 200, 0));
        colonLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        timeInputPanel.add(colonLabel);
        
        JLabel minuteLabel = new JLabel("Minute:");
        minuteLabel.setForeground(Color.WHITE);
        minuteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        timeInputPanel.add(minuteLabel);
        
        minuteField = createTimeField();
        timeInputPanel.add(minuteField);
        
        return timeInputPanel;
    }
    
   // Creates a styled time input field.

    private JTextField createTimeField() {
        JTextField field = new JTextField(5);
        field.setFont(new Font("Arial", Font.BOLD, 32));
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(0, 200, 100), 3, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setPreferredSize(new Dimension(100, 60));
        return field;
    }
    
    // Creates the button panel.

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(20, 20, 30));
        
        setAlarmButton = UIComponents.createStyledButton("🔔 ACTIVATE ALARM", new Color(0, 200, 100));
        setAlarmButton.addActionListener(e -> setAlarm());
        buttonPanel.add(setAlarmButton);
        
        viewHistoryButton = UIComponents.createStyledButton("📜 VIEW HISTORY", new Color(100, 150, 255));
        viewHistoryButton.addActionListener(e -> showHistory());
        buttonPanel.add(viewHistoryButton);
        
        clearHistoryButton = UIComponents.createStyledButton("🗑️ CLEAR HISTORY", new Color(200, 100, 100));
        clearHistoryButton.addActionListener(e -> clearHistory());
        buttonPanel.add(clearHistoryButton);
        
        return buttonPanel;
    }
    
   // Starts the clock and alarm checking timers.

    private void startClock() {
        clockTimer = new Timer(1000, e -> updateTime());
        clockTimer.start();
        updateTime();
        
        alarmCheckTimer = new Timer(1000, e -> checkAlarm());
        alarmCheckTimer.start();
    }
    
    // Updates the displayed time and date.

    private void updateTime() {
        LocalTime now = LocalTime.now();
        timeLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        
        java.time.LocalDate date = java.time.LocalDate.now();
        dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
    }
    
   // Sets the alarm based on user input.

    private void setAlarm() {
        try {
            int hour = Integer.parseInt(hourField.getText());
            int minute = Integer.parseInt(minuteField.getText());
            
            if (!alarmManager.setAlarm(hour, minute)) {
                UIComponents.showStyledMessage(this, "⚠️ Invalid Time Format!", 
                    "Please use 24-hour format:\nHours: 00-23\nMinutes: 00-59", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalTime alarmTime = alarmManager.getAlarmTime();
            setAlarmButton.setText("✓ ALARM ACTIVE: " + alarmTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            setAlarmButton.setEnabled(false);
            setAlarmButton.setBackground(new Color(255, 150, 0));
            
            UIComponents.showStyledMessage(this, "✅ Alarm Set Successfully!", 
                "Your alarm is now scheduled for:\n" + 
                alarmTime.format(DateTimeFormatter.ofPattern("HH:mm")) + 
                "\n\nGet ready to wake up!", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
            UIComponents.showStyledMessage(this, "❌ Input Error", 
                "Please enter valid numbers for hours and minutes!", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    

     // Checks if alarm should trigger.

    private void checkAlarm() {
        if (alarmManager.shouldTriggerAlarm()) {
            triggerAlarm();
        }
    }
    
    
     // Triggers the alarm with visual and audio effects.

    private void triggerAlarm() {
        alarmManager.setAlarmRinging(true);
        
        // Make window always on top
        setAlwaysOnTop(true);
        toFront();
        requestFocus();
        
        // Start beeping
        Timer beepTimer = new Timer(500, e -> Toolkit.getDefaultToolkit().beep());
        beepTimer.start();
        
        // Flash the main content pane
        Timer flashTimer = new Timer(300, new ActionListener() {
            boolean red = true;
            public void actionPerformed(ActionEvent e) {
                if (alarmManager.isAlarmRinging()) {
                    Component[] components = getContentPane().getComponents();
                    for (Component comp : components) {
                        if (comp != challengePanel) {
                            comp.setBackground(red ? new Color(220, 20, 20) : new Color(255, 220, 0));
                        }
                    }
                    getContentPane().setBackground(red ? new Color(220, 20, 20) : new Color(255, 220, 0));
                    red = !red;
                }
            }
        });
        flashTimer.start();
        
        // Setup challenges
        challengePanel.setupChallenge(() -> stopAlarm(beepTimer, flashTimer));
    }
    
 
     
    // Stops the alarm and resets the UI.

    private void stopAlarm(Timer beepTimer, Timer flashTimer) {
        beepTimer.stop();
        flashTimer.stop();
        alarmManager.resetAlarm();
        
        setAlwaysOnTop(false);
        setExtendedState(JFrame.NORMAL);
        
        // Reset all backgrounds to default
        resetBackgrounds();
        
        challengePanel.setVisible(false);
        setAlarmButton.setText("🔔 ACTIVATE ALARM");
        setAlarmButton.setEnabled(true);
        setAlarmButton.setBackground(new Color(0, 200, 100));
        hourField.setText("");
        minuteField.setText("");
        
        getContentPane().revalidate();
        getContentPane().repaint();
        
        UIComponents.showStyledMessage(this, "☀️ Good Morning!", 
            "Congratulations! You're officially awake!\n\n" +
            "Challenge completed successfully.\nHave a wonderful day! 😊", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    
     // Resets all component backgrounds to default.

    private void resetBackgrounds() {
        getContentPane().setBackground(new Color(20, 20, 30));
        
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel && comp != challengePanel) {
                comp.setBackground(new Color(20, 20, 30));
                if (comp instanceof JPanel) {
                    for (Component nested : ((JPanel) comp).getComponents()) {
                        if (nested instanceof JPanel) {
                            nested.setBackground(new Color(20, 20, 30));
                        }
                    }
                }
            }
        }
    }
    
   
     //Shows the alarm history.

    private void showHistory() {
        if (alarmManager.getHistory().isEmpty()) {
            UIComponents.showStyledMessage(this, "📜 Alarm History", 
                "No alarm history yet.\n\nSet an alarm and complete the challenge\nto see it appear here!", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder history = new StringBuilder("📜 ALARM HISTORY (LinkedList)\n");
        history.append("═══════════════════════════════════════\n\n");
        
        int count = 1;
        for (AlarmRecord record : alarmManager.getHistory()) {
            history.append(count++).append(". ").append(record.toString()).append("\n\n");
        }
        
        JTextArea textArea = new JTextArea(history.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "📊 Your Alarm History", JOptionPane.INFORMATION_MESSAGE);
    }
    
   
     // Clears the alarm history.

    private void clearHistory() {
        if (alarmManager.getHistory().isEmpty()) {
            UIComponents.showStyledMessage(this, "🗑️ Clear History", 
                "History is already empty!", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear all alarm history?\n\n" +
            "This will remove " + alarmManager.getHistory().size() + " record(s).",
            "⚠️ Confirm Clear History",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            alarmManager.clearHistory();
            UIComponents.showStyledMessage(this, "✅ History Cleared", 
                "All alarm history has been deleted successfully!", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
     //Main entry point of the application.

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new UnbeatableAlarm());
    }
}