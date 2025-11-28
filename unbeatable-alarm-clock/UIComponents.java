import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// Utility class for creating styled UI components.
public class UIComponents {
    
    // Creates a styled button with custom colors and hover effects.
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(
            new LineBorder(bgColor.darker(), 2, true),
            new EmptyBorder(10, 20, 10, 20)
        ));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    // Shows a styled message dialog.
    public static void showStyledMessage(Component parent, String title, String message, int messageType) {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }
}