import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Handles the wake-up challenges (math problem and click counter).

public class ChallengePanel extends JPanel {
    private static final int REQUIRED_CLICKS = 25;
    
    private Random random;
    private int mathAnswer;
    private int clickCount;
    private AlarmManager alarmManager;
    
    private JTextField mathField;
    private JLabel clickLabel;
    
    public ChallengePanel(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
        this.random = new Random();
        this.clickCount = 0;
        
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(180, 30, 30));
        setBorder(new LineBorder(Color.YELLOW, 4));
        setVisible(false);
    }
    
    // Sets up the challenge UI with math problem and click counter.

    public void setupChallenge(Runnable onSuccess) {
        removeAll();
        clickCount = 0;
        setVisible(true);
        setBackground(new Color(180, 30, 30));
        
        JPanel innerPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        innerPanel.setBackground(new Color(180, 30, 30));
        innerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("⚠️ WAKE UP CHALLENGE INITIATED! ⚠️");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.YELLOW);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(180, 30, 30));
        
        // Challenge 1: Math problem
        int num1 = random.nextInt(50) + 10;
        int num2 = random.nextInt(50) + 10;
        mathAnswer = num1 + num2;
        
        JLabel mathLabel = new JLabel("🧮 SOLVE: " + num1 + " + " + num2 + " = ?");
        mathLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        mathLabel.setForeground(Color.WHITE);
        mathLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mathLabel.setOpaque(true);
        mathLabel.setBackground(new Color(180, 30, 30));
        
        mathField = new JTextField(10);
        mathField.setFont(new Font("Segoe UI", Font.BOLD, 20));
        mathField.setHorizontalAlignment(SwingConstants.CENTER);
        mathField.setBackground(Color.WHITE);
        mathField.setForeground(Color.BLACK);
        mathField.setBorder(new LineBorder(Color.YELLOW, 3, true));
        
        // Challenge 2: Click counter
        clickLabel = new JLabel("👆 TAP " + REQUIRED_CLICKS + " TIMES: 0/" + REQUIRED_CLICKS);
        clickLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        clickLabel.setForeground(Color.WHITE);
        clickLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clickLabel.setOpaque(true);
        clickLabel.setBackground(new Color(180, 30, 30));
        
        JButton clickButton = new JButton("💪 I'M AWAKE!");
        clickButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        clickButton.setBackground(new Color(255, 100, 100));
        clickButton.setForeground(Color.WHITE);
        clickButton.setBorder(new CompoundBorder(
            new LineBorder(Color.YELLOW, 3, true),
            new EmptyBorder(15, 30, 15, 30)
        ));
        clickButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clickButton.setFocusPainted(false);
        
        clickButton.addActionListener(e -> handleChallenge(onSuccess));
        
        innerPanel.add(headerLabel);
        innerPanel.add(mathLabel);
        innerPanel.add(mathField);
        innerPanel.add(clickLabel);
        innerPanel.add(clickButton);
        
        add(innerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    // Handles challenge validation.

    private void handleChallenge(Runnable onSuccess) {
        clickCount++;
        clickLabel.setText("👆 TAP " + REQUIRED_CLICKS + " TIMES: " + clickCount + "/" + REQUIRED_CLICKS);
        
        if (clickCount >= REQUIRED_CLICKS) {
            try {
                int answer = Integer.parseInt(mathField.getText());
                if (answer == mathAnswer) {
                    // Record the successful dismissal
                    String dismissedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    String alarmTimeStr = alarmManager.getAlarmTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    
                    alarmManager.addToHistory(new AlarmRecord(
                        alarmTimeStr, 
                        alarmTimeStr, 
                        dismissedTime, 
                        clickCount
                    ));
                    
                    onSuccess.run();
                } else {
                    UIComponents.showStyledMessage(this, "❌ Wrong Answer!", 
                        "The correct answer is not " + answer + "!\nTry again and keep clicking!", 
                        JOptionPane.ERROR_MESSAGE);
                    resetChallenge();
                }
            } catch (NumberFormatException ex) {
                UIComponents.showStyledMessage(this, "⚠️ Missing Answer!", 
                    "Please solve the math problem first!", 
                    JOptionPane.WARNING_MESSAGE);
                resetChallenge();
            }
        }
    }
    
    // Resets the click counter.
    private void resetChallenge() {
        clickCount = 0;
        clickLabel.setText("👆 TAP " + REQUIRED_CLICKS + " TIMES: 0/" + REQUIRED_CLICKS);
    }
    
    // Gets the current click count (for testing/debugging).
    public int getClickCount() {
        return clickCount;
    }
}