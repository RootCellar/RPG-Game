import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Terminal extends JFrame implements KeyListener {
    private JTextArea text = new JTextArea(30,30);
    private JTextField input;

    public Terminal() {
        this.setSize(100,100);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        text.setEditable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(1,1));
        panel.setBorder(BorderFactory.createEmptyBorder(20,30,10,30));

        JScrollPane scroll = new JScrollPane(text);
        panel.add(scroll, BorderLayout.CENTER);
        input = new JTextField();
        input.addKeyListener(this);
        panel.add(input, BorderLayout.PAGE_START);

        this.add(panel);
        this.setTitle("Terminal");
        this.pack();
        this.setVisible(true);
        text.append("Finished opening log!\n");
    }

    public void keyPressed(KeyEvent event) {
        String key = KeyEvent.getKeyText(event.getKeyCode());
        System.out.println(key);
        if(key.equals("Enter")) {
            //Client.send(input.getText());
            input.setText("");
        }
    }

    public void write(String w) {
        text.append(w+"\n");
        text.setCaretPosition(text.getDocument().getLength());
    }

    public void keyReleased(KeyEvent event) {

    }

    public void keyTyped(KeyEvent event) {

    }
}