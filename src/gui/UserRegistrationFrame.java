package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

//Class for both registering a user and editing user info
public class UserRegistrationFrame extends JFrame
{
    private JTextField nameTextField, surnameTextField, usernameTextField;
    private JPasswordField passwordField, passwordConfirmationField, currentPasswordField;
    private JPanel panel;
    private final DataBase dataBase;
    private HashMap<String, User> authentication;
    private Color primaryColor,textColor;
    private final User user;
    private final UserProfileFrame userProfileFrame;
    private boolean addUser=false;

    //This constructor is called to register a new subscriber
    UserRegistrationFrame(DataBase dataBase)
    {
        this.dataBase=dataBase;

        /*This HashMap correlates all usernames to their corresponding accounts.
        This way, when entering a username, the program can determine
        if it belongs to any user and, if so, which one*/
        authentication=new HashMap<>();
        authentication=dataBase.getAuthentication();
        user=null;
        userProfileFrame=null;

        /*Boolean parameter that determines whether to build a panel
        for registering (adding) a new user or for editing the details of an existing account*/
        addUser=true;
        buildFrame();
    }

    //This constructor is called to edit the details of an existing account
    UserRegistrationFrame(DataBase dataBase,User user,UserProfileFrame userProfileFrame)
    {
        this.dataBase=dataBase;
        this.authentication=new HashMap<>();
        this.authentication=dataBase.getAuthentication();
        this.user=user;
        this.userProfileFrame=userProfileFrame;
        buildFrame();
    }

    public void buildFrame()
    {
        if(addUser) {
            this.setTitle("Sign up Form");
        }
        else
        {
            setTitle("Edit profile information");
        }
        this.setLocationRelativeTo(null);
        this.setSize(400,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        primaryColor=new Color(85, 0, 0);
        textColor=Color.WHITE;

        panel=new JPanel();
        panel.setBackground(primaryColor);

        nameTextField =new JTextField("");
        if(!addUser)
        {
            nameTextField.setText(user.getName());
        }

        JLabel nameLabel=new JLabel("Name");
        JPanel namePanel= buildBasicInfoPanel(nameTextField,nameLabel);

        surnameTextField =new JTextField("");
        if(!addUser)
        {
            surnameTextField.setText(user.getSurname());
        }

        JLabel surnameLabel =new JLabel("Surname");
        JPanel surnamePanel=buildBasicInfoPanel(surnameTextField, surnameLabel);

        usernameTextField =new JTextField("");
        if(!addUser)
        {
            usernameTextField.setText(user.getUsername());
        }

        JLabel usernameLabel=new JLabel("Username");
        JPanel usernamePanel= buildBasicInfoPanel(usernameTextField,usernameLabel);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(namePanel);
        panel.add(surnamePanel);
        panel.add(usernamePanel);
        buildPasswordPanel();
        buildPasswordConfirmationPanel();
        buildSavePanel();

        usernameTextField.setToolTipText("Username must begin with a latin character and must only contain latin characters and (optionally) numbers");
        usernameTextField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                //Checking if username inserted can be used
                if(!usernameTextField.getText().trim().isEmpty())
                {
                    if((addUser || ( user!=null && !usernameTextField.getText().trim().equals(user.getUsername())))
                            && authentication.containsKey(usernameTextField.getText().trim()))
                    {
                        JOptionPane.showMessageDialog(usernamePanel, "This username already exists");
                    }
                }
            }
        });

        //nameTextField.requestFocusInWindow();
        add(panel);
        pack();
        this.setVisible(true);
    }

    public void buildPasswordPanel()
    {
        passwordField=new JPasswordField("");
        JLabel passwordLabel=new JLabel("Password");
        passwordLabel.setForeground(textColor);

        JCheckBox showPasswordCheckBox =new JCheckBox("Show password",false);
        showPasswordCheckBox.setBackground(Color.BLACK);
        showPasswordCheckBox.setForeground(textColor);
        JPanel passwordPanel= new JPanel();
        passwordPanel.setBackground(primaryColor);
        passwordPanel.setLayout(new BoxLayout(passwordPanel,BoxLayout.Y_AXIS));

        if(!addUser)
        {
            currentPasswordField =new JPasswordField("");
            JLabel currentPasswordLabel=new JLabel("Current password");
            currentPasswordLabel.setForeground(textColor);
            JCheckBox showCurrentPasswordCheckBox=new JCheckBox("Show password",false);
            showCurrentPasswordCheckBox.setBackground(Color.BLACK);
            showCurrentPasswordCheckBox.setForeground(textColor);

            showCurrentPasswordCheckBox.addItemListener(e -> {
                int state = e.getStateChange();
                currentPasswordField.setEchoChar((state == ItemEvent.SELECTED) ? '\0' : '*');
            });

            passwordPanel.add(currentPasswordLabel);
            passwordPanel.add(currentPasswordField);
            passwordPanel.add(showCurrentPasswordCheckBox);

            passwordLabel.setText("New password (optional)");
        }

        passwordField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                String password=new String(passwordField.getPassword()).trim();
                if(!password.isEmpty() && password.length()<8)
                {
                    JOptionPane.showMessageDialog(passwordPanel, "Password must be at least 8 characters long");
                }
                else if(!password.isEmpty() && !passwordIsValid())
                {
                    JOptionPane.showMessageDialog(passwordPanel,"Password must contain latin characters, numbers and can optionally contain special characters(@,!,(,),-,[,],_,#,$,%,^,+,=,*,&,/ )");
                }
            }
        });

        showPasswordCheckBox.addItemListener(e -> {
            int state = e.getStateChange();
            passwordField.setEchoChar((state == ItemEvent.SELECTED) ? '\0' : '*');
        });
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.add(showPasswordCheckBox);
        panel.add(passwordPanel);
    }

    //Panel for confirming the (new) password inserted
    public void buildPasswordConfirmationPanel()
    {
        passwordConfirmationField=new JPasswordField("");
        JLabel passwordLabel=new JLabel("Confirm password");
        if(!addUser)
        {
            passwordLabel.setText("Confirm new password");
        }

        passwordLabel.setForeground(textColor);
        JCheckBox showPasswordCheckBox =new JCheckBox("Show password",false);
        showPasswordCheckBox.setBackground(Color.BLACK);
        showPasswordCheckBox.setForeground(textColor);
        JPanel passwordPanel=new JPanel();
        passwordPanel.setBackground(primaryColor);
        passwordPanel.setLayout(new BoxLayout(passwordPanel,BoxLayout.Y_AXIS));

        passwordConfirmationField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                String passwordConfirmation=new String(passwordConfirmationField.getPassword()).trim();
                String password=new String(passwordField.getPassword()).trim();
                if (!passwordConfirmation.isEmpty() && !password.equals(passwordConfirmation))
                {
                    JOptionPane.showMessageDialog(passwordPanel, "Confirmation of password doesn't match password");
                }
            }
        });

        showPasswordCheckBox.addItemListener(e -> {
            int state = e.getStateChange();
            passwordConfirmationField.setEchoChar((state == ItemEvent.SELECTED) ? '\0' : '*');
        });
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordConfirmationField);
        passwordPanel.add(showPasswordCheckBox);
        panel.add(passwordPanel);
    }

    /*This method, savePanel and saveButton are responsible for registering
    a new user in the system or saving the edits made by an existing one*/
    public void buildSavePanel()
    {
        JPanel savePanel=new JPanel();
        savePanel.setLayout(new FlowLayout());
        savePanel.setBackground(primaryColor);

        panel.add(savePanel);

        JButton saveButton =new JButton("Save changes");
        savePanel.add(saveButton);
        if(addUser)
        {
            saveButton.setText("Register");
        }
        else
        {
            JButton cancelButton=new JButton("Cancel");
            cancelButton.setBackground(textColor);
            cancelButton.setForeground(primaryColor);

            savePanel.add(cancelButton);
            cancelButton.addActionListener(e -> {
                int confirmation=JOptionPane.showConfirmDialog(panel,"Cancel changes?","Confirm cancellation",JOptionPane.YES_NO_OPTION);
                {
                    if(confirmation==JOptionPane.YES_OPTION)
                    {
                        dispose();
                    }
                }
            });
        }

        saveButton.setBackground(textColor);
        saveButton.setForeground(primaryColor);

        saveButton.addActionListener(e -> {
            String password=new String(passwordField.getPassword()).trim();
            String passwordConfirmation=new String(passwordConfirmationField.getPassword()).trim();
            String username=usernameTextField.getText().trim();
            String surname=surnameTextField.getText().trim();
            String name=nameTextField.getText().trim();

            if(name.isEmpty()|| surname.isEmpty()|| username.isEmpty())
            {
                if(addUser) {
                    JOptionPane.showMessageDialog(panel, "All fields are mandatory");
                }
                else
                {
                    JOptionPane.showMessageDialog(panel,"All fields are mandatory (new password and confirmation of new password excluded)");
                }
            }
            else if(addUser && (password.isEmpty() || passwordConfirmation.isEmpty()))
            {
                JOptionPane.showMessageDialog(panel,"All fields are mandatory");
            }
            else if(!addUser && new String(currentPasswordField.getPassword()).trim().isEmpty())
            {
                JOptionPane.showMessageDialog(panel,"Inserting current password is mandatory");
            }
            else if(!addUser && !user.validatePassword(new String(currentPasswordField.getPassword()).trim()))
            {
                JOptionPane.showMessageDialog(panel,"Wrong current password inserted!");
            }
            else if(!usernameIsValid())
            {
                JOptionPane.showMessageDialog(panel, "Username must begin with a latin character and must only contain latin characters and (optionally) numbers");
            }
            else if(!password.isEmpty() && !passwordIsValid())
            {
                JOptionPane.showMessageDialog(panel,"Password must be at least 8 characters long and contain latin characters, numbers and optionally special characters (@,!,(,),-,[,],_,#,$,%,^,+,=,*,&,/)");
            }
            else if (!password.equals(passwordConfirmation))
            {
                JOptionPane.showMessageDialog(panel, "Confirmation of password doesn't match password");
            }
            else if((addUser || (!username.equals(user.getUsername()))) && authentication.containsKey(usernameTextField.getText().trim()))
            {
                JOptionPane.showMessageDialog(panel,"This username already exists");
            }
            else //All fields are correct
            {
                if(addUser) //Register new user
                {
                    JOptionPane.showMessageDialog(panel, "Successful sign up!");
                    Subscriber subscriber = new Subscriber(username, password, name, surname);
                    dataBase.addToUsers(subscriber);
                    new MainPageFrame(dataBase,subscriber);
                }
                else //Edit account details
                {
                    int confirmation=JOptionPane.showConfirmDialog(panel,"Save changes?","Confirm edits",JOptionPane.YES_NO_OPTION);
                    if(confirmation==JOptionPane.YES_OPTION)
                    {
                        if (password.isEmpty())
                        {
                            //If the user didn't insert a new password, the password will remain the same
                            user.edit(username, new String(currentPasswordField.getPassword()).trim(), name, surname,authentication);
                        } else
                        {
                            user.edit(username, password, name, surname,authentication);
                        }
                        userProfileFrame.updateUserInfo();
                    }
                    dataBase.editFiles();
                }
                dispose();
            }
        });
    }

    /*Username must follow the given pattern (first character is a latin character
    and only acceptable characters are latin characters and numbers). This method
    is defined in the gui and not the api package, so that the valid pattern can
    be updated without the api being altered*/
    public boolean usernameIsValid()
    {
        String acceptableUsernameCharacters="^[a-zA-Z]+[0-9]*$";

        return usernameTextField.getText().trim().matches(acceptableUsernameCharacters);
    }

    /*Password must follow the given pattern (length is at least 8 characters,
    latin characters and numbers are mandatory, and the special characters
    @,!,(,),-,[,],_,#,$,%,^,+,=,*,&,/ can be included for extra security). This method
    is defined in the gui and not the api package, so that the valid pattern can
    be updated without the api being altered*/
    public boolean passwordIsValid()
    {
        String password=new String(passwordField.getPassword()).trim();
        String acceptablePasswordCharacters="^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@!()\\-\\[\\]_#$%^+=*&/]+$";

        return password.matches(acceptablePasswordCharacters) && password.length()>=8;
    }

    /*The panels for name,surname and username fields all follow the same pattern
    so a generic method can be extracted*/
    public JPanel buildBasicInfoPanel(JTextField text, JLabel label)
    {
        JPanel panel=new JPanel();
        label.setForeground(textColor);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(primaryColor);
        panel.add(label);
        panel.add(text);

        return panel;
    }
}

