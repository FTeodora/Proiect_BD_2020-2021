package view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame{

    private final JLabel loginLabel;
    private final JPanel loginFieldsPanel;
    private final JLabel usernameLabel;
    private final ImageIcon usernameIcon;
    private final JTextField usernameField;
    private final JLabel passwordLabel;
    private final ImageIcon passwordIcon;
    private final JTextField passwordField;
    private final JButton loginButton;
    private final JPanel loginPanel;
    private final JButton cancelButton;
    private final JPanel upPanel;
    private final JPanel downPanel;



    public static void ErrorMessage(){
        ImageIcon errorlogo=new ImageIcon("errorlogo1.jpg");

        JFrame errorFrame = new JFrame("ERROR!");
        errorFrame.setSize(300,80);
        errorFrame.setLocationRelativeTo(null);
        errorFrame.setIconImage(errorlogo.getImage());
        JLabel errorText = new JLabel("Invalid username or password!");
        errorText.setFont(new Font("Tahoma",Font.BOLD,12));


        JPanel errorPanel = new JPanel();

        errorPanel.add(errorText);
        errorFrame.setContentPane(errorPanel);
        errorFrame.setVisible(true);
    }

    public LoginView() {
        super("Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        loginLabel=new JLabel("LOGIN");
        loginLabel.setForeground(Color.WHITE);

        loginFieldsPanel = new JPanel();
        loginFieldsPanel.setBackground(new Color(70,130,180));
        loginFieldsPanel.setLayout(new GridLayout(3,3,6,6));

        usernameIcon=new ImageIcon(LoginView.class.getResource("\\icons\\usericon.png"));
        usernameLabel = new JLabel("USERNAME:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Tahoma",Font.BOLD,12));
        usernameLabel.setIcon(usernameIcon );
        usernameField= new JTextField(1);

        passwordIcon=new ImageIcon(LoginView.class.getResource("icons\\passwordicon.png"));
        passwordLabel = new JLabel("PASSWORD:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Tahoma",Font.BOLD,12));
        passwordLabel.setIcon(passwordIcon);
        passwordField= new JPasswordField(1);

        loginButton = new JButton("LOGIN");
        loginButton.setBackground(new Color(70,130,180));
        loginButton.setForeground(Color.WHITE);

        cancelButton = new JButton("CANCEL");
        cancelButton.setBackground(new Color(70,130,180));
        cancelButton.setForeground(Color.WHITE);

        loginFieldsPanel.add(loginLabel);
        loginFieldsPanel.add(usernameLabel);
        loginFieldsPanel.add(usernameField);
        loginFieldsPanel.add(passwordLabel);
        loginFieldsPanel.add(passwordField);

        loginPanel = new JPanel();
        loginPanel.setBackground(new Color(70,130,180));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));

        upPanel=new JPanel();
        upPanel.setLayout(new FlowLayout());
        upPanel.setBackground(new Color(70,130,180));
        upPanel.add(loginLabel);

        downPanel=new JPanel();
        downPanel.setLayout(new FlowLayout());
        downPanel.add(loginButton);
        downPanel.add(cancelButton);
        upPanel.setBackground(new Color(70,130,180));

        loginPanel.add(upPanel);
        loginPanel.add(loginFieldsPanel);
        loginPanel.add(downPanel);
        this.setContentPane(loginPanel);
        setVisible(true);

    }

    public String getUsernameFieldText(){
        return usernameField.getText();
    }

    public String getPasswordFieldText(){
        return passwordField.getText();
    }

    public void setUsernameFieldText(String text){
        usernameField.setText(text);
    }

    public void setPasswordFieldText(String text){
        passwordField.setText(text);
    }

    public void addLoginButtonListener(ActionListener listener){
        loginButton.addActionListener(listener);
    }

    public void addCancelButtonListener(ActionListener listener){
        cancelButton.addActionListener(listener);
    }


}
