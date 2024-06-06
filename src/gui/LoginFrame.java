
package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//Class for logging a user in
public class LoginFrame extends JFrame
{
    private JTextField username;
    private JPasswordField password;
    private JPanel panel;
    private DataBase dataBase;
    private Color primaryColor,textColor;
    private HashMap<String,User> authentication;

    public LoginFrame(DataBase dataBase)
    {
        this.dataBase=dataBase;

        /*This HashMap correlates all usernames to their corresponding accounts.
         This way, when entering a username, the program can determine
         if it belongs to any user and, if so, which one*/
        authentication=new HashMap<>();
        authentication=dataBase.getAuthentication();
        buildFrame();
    }

    public void buildFrame()
    {
        setTitle("Login");
        setLocationRelativeTo(null);
        setSize(500,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        primaryColor=new Color(85,0,0);
        textColor=Color.WHITE;

        panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(primaryColor);

        buildUsernamePanel();
        buildPasswordPanel();
        buildLoginOptionsPanel();

        add(panel);
        this.setVisible(true);
    }

    public void buildUsernamePanel()
    {

        username=new JTextField("");
        JLabel usernameLabel=new JLabel("username");
        usernameLabel.setForeground(textColor);
        JPanel usernamePanel=new JPanel();
        usernamePanel.setBackground(primaryColor);
        usernamePanel.setLayout(new BoxLayout(usernamePanel,BoxLayout.Y_AXIS));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(username);
        panel.add(usernamePanel);
    }

    public void buildPasswordPanel()
    {
        password=new JPasswordField();
        JLabel passwordLabel=new JLabel("password");
        passwordLabel.setForeground(textColor);

        JCheckBox showPasswordCheckBox =new JCheckBox("Show password",false);
        showPasswordCheckBox.setBackground(primaryColor);
        showPasswordCheckBox.setForeground(textColor);

        JPanel passwordPanel=new JPanel();
        passwordPanel.setBackground(primaryColor);
        passwordPanel.setLayout(new BoxLayout(passwordPanel,BoxLayout.Y_AXIS));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(password);
        passwordPanel.add(showPasswordCheckBox);

        showPasswordCheckBox.addItemListener(e -> {
            int state = e.getStateChange();
            password.setEchoChar((state == ItemEvent.SELECTED) ? '\0' : '*');
        });

        panel.add(passwordPanel);
    }

    public void buildLoginOptionsPanel()
    {

        JPanel loginOptionsPanel =new JPanel();
        loginOptionsPanel.setBackground(primaryColor);
        FlowLayout customLayout = new FlowLayout();
        customLayout.setHgap(20);
        loginOptionsPanel.setLayout(customLayout);
        JButton loginButton =new JButton("Login");
        loginButton.setBackground(textColor);
        loginButton.setForeground(primaryColor);
        loginOptionsPanel.add(loginButton);

        loginOptionsPanel.add(buildRegisterPanel());

        loginButton.addActionListener(e -> {
            //Checking if all fields are filled
            if(username.getText().trim().isEmpty() || Arrays.toString(password.getPassword()).trim().isEmpty())
            {
                JOptionPane.showMessageDialog(panel, "All fields are mandatory");
            }
            //Checking if the username exists and if the password corresponds to the given username
            else if(!authentication.containsKey(username.getText().trim()) || !authentication.get(username.getText().trim()).validatePassword(new String(password.getPassword()).trim()))
            {
                JOptionPane.showMessageDialog(panel,"Wrong username or password");
            }
            else //Username and password given are correct
            {
                User user=authentication.get(username.getText().trim());
                new MainPageFrame(dataBase,user);
                dispose();
            }
        });

        panel.add(loginOptionsPanel);
    }

    //Gives a non-registered user the option to register
    public JPanel buildRegisterPanel()
    {

        JPanel registerPanel=new JPanel();
        registerPanel.setLayout(new FlowLayout());
        registerPanel.setBackground(Color.BLACK);
        JLabel registerLabel=new JLabel("Not registered?");
        registerLabel.setForeground(textColor);
        JButton registerButton =new JButton("Sign up");
        registerPanel.add(registerLabel);
        registerPanel.add(registerButton);

        registerButton.addActionListener(e -> {
            dispose();
            new UserRegistrationFrame(dataBase);
        });

        return registerPanel;
    }
}