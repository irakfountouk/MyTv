package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

//Class for viewing the profile of the user currently logged in
public class UserProfileFrame extends JFrame
{
    private JLabel helloLabel;
    private JPanel panel,favoritesOuterPanel;
    private User user;
    private Color primaryColor,textColor;
    private DataBase dataBase;
    public UserProfileFrame(DataBase dataBase, User user)
    {
        this.dataBase=dataBase;
        this.user=user;
        buildFrame();
    }

    public void buildFrame()
    {
        setSize(300,300);
        setTitle("Your profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        primaryColor=new Color(85,0,0);
        textColor=Color.WHITE;

        panel =new JPanel();
        panel.setBackground(primaryColor);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        buildOptionsMenu();

        JPanel helloPanel=new JPanel();
        helloPanel.setLayout(new FlowLayout());
        helloPanel.setBackground(primaryColor);
        helloLabel=new JLabel("Hello, "+user.getName());
        helloLabel.setForeground(textColor);
        helloPanel.add(helloLabel);
        helloPanel.add(new JLabel(""));
        panel.add(helloPanel);
        panel.add(new JLabel("\n"));

        favoritesOuterPanel=new JPanel();
        favoritesOuterPanel.setLayout(new BoxLayout(favoritesOuterPanel,BoxLayout.Y_AXIS));
        favoritesOuterPanel.setBackground(primaryColor);
        if(user instanceof Subscriber)
        {
            JPanel favoritesDecorativePanel=new JPanel();
            favoritesDecorativePanel.setBackground(primaryColor);
            favoritesDecorativePanel.setLayout(new FlowLayout());
            JLabel favoritesLabel=new JLabel("Your Favorites:");
            favoritesLabel.setForeground(textColor);
            favoritesDecorativePanel.add(favoritesLabel);
            favoritesDecorativePanel.add(new JLabel(" "));
            panel.add(favoritesDecorativePanel);
            panel.add(favoritesOuterPanel);
            buildSubscriberFavoritesPanel();
        }

        JPanel returnButtonPanel=new JPanel();
        returnButtonPanel.setBackground(primaryColor);
        returnButtonPanel.setLayout(new FlowLayout());
        JButton returnButton=new JButton("Return to main page");
        returnButton.setBackground(textColor);
        returnButton.setForeground(primaryColor);
        returnButtonPanel.add(returnButton);
        returnButtonPanel.add(new JLabel("  "));
        panel.add(returnButtonPanel);

        returnButton.addActionListener(e -> {
            dispose();
            new MainPageFrame(dataBase,user);
        });


        add(panel);
        pack();
        this.setVisible(true);
    }

    public void buildSubscriberFavoritesPanel()
    {
        Subscriber subscriber=(Subscriber) user;
        if(subscriber.getFavorites().isEmpty())
        {
            JPanel emptyFavoritesPanel=new JPanel();
            emptyFavoritesPanel.setBackground(primaryColor);
            emptyFavoritesPanel.setLayout(new FlowLayout());
            JLabel emptyFavoritesLabel=new JLabel("Your favorites list is empty!");
            emptyFavoritesLabel.setForeground(textColor);
            emptyFavoritesPanel.add(emptyFavoritesLabel);
            favoritesOuterPanel.add(emptyFavoritesPanel);
        }
        else
        {
            JPanel favoritesInnerPanel=new JPanel();
            favoritesInnerPanel.setLayout(new BoxLayout(favoritesInnerPanel,BoxLayout.Y_AXIS));
            JScrollPane favoritesScrollPane=new JScrollPane(favoritesInnerPanel);
            favoritesOuterPanel.add(favoritesScrollPane);

            favoritesInnerPanel.add(new JLabel("Click on a title to view more details!"));

            ArrayList<Content> favoritesToBeRemoved = new ArrayList<>();

            JButton removeFromFavoritesButton = new JButton("Remove content from favorites");
            removeFromFavoritesButton.setBackground(textColor);
            removeFromFavoritesButton.setForeground(primaryColor);
            JButton removeSelectedFromFavoritesButton = new JButton("Remove selected from favorites");
            JButton removeAllFavoritesButton = new JButton("Remove all favorites");
            JButton cancelRemovalButton = new JButton("Cancel");

            JPanel removeFavoritesOptionsPanel = new JPanel();

            removeFavoritesOptionsPanel.setLayout(new FlowLayout());
            removeFavoritesOptionsPanel.setVisible(false);

            removeFavoritesOptionsPanel.add(removeSelectedFromFavoritesButton);
            removeFavoritesOptionsPanel.add(removeAllFavoritesButton);
            removeFavoritesOptionsPanel.add(cancelRemovalButton);

            removeFromFavoritesButton.addActionListener(e -> removeFavoritesOptionsPanel.setVisible(true));

            removeSelectedFromFavoritesButton.addActionListener(e -> {
                if (!favoritesToBeRemoved.isEmpty())
                {
                    int confirmation = JOptionPane.showConfirmDialog(favoritesOuterPanel, "Are you sure you want to remove all selected from favorites?", "Confirm removal", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION)
                    {
                        Iterator<Content> it=subscriber.getFavorites().iterator();
                        while(it.hasNext())
                        {
                            Content favorite=it.next();
                            if(favoritesToBeRemoved.contains(favorite))
                            {
                                it.remove();
                            }
                        }
                        updateSubscriberFavoritesPanel();
                    }
                }
            });

            removeAllFavoritesButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(favoritesOuterPanel, "Are you sure you want to remove all favorites?", "Confirm removal", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION)
                {
                    subscriber.deleteFavorites();
                    updateSubscriberFavoritesPanel();
                }
            });

            cancelRemovalButton.addActionListener(e -> {
                favoritesToBeRemoved.clear();
                removeFavoritesOptionsPanel.setVisible(false);
            });

            Iterator<Content> it=subscriber.getFavorites().iterator();
            while(it.hasNext())
            {
                Content favorite=it.next();
                JPanel favoritePanel = new JPanel();
                favoritesInnerPanel.add(favoritePanel);
                favoritePanel.setLayout(new FlowLayout());
                JCheckBox removeFavoriteCheckBox = new JCheckBox();
                removeFavoriteCheckBox.setVisible(false);
                favoritePanel.add(removeFavoriteCheckBox);
                JLabel favoriteLabel = new JLabel(favorite.getTitle());
                favoritePanel.add(favoriteLabel);

                favoriteLabel.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        if (!(favorite instanceof ViewableContent))
                        {
                            new ContentFrame(dataBase,favorite, subscriber,UserProfileFrame.this);
                        }
                        else if (favorite instanceof Movie movie)
                        {
                            new MovieFrame(dataBase,movie,subscriber,UserProfileFrame.this);
                        }
                        else if (favorite instanceof Series series)
                        {
                            new SeriesFrame(dataBase,series,subscriber,UserProfileFrame.this);
                        } else if (favorite instanceof ViewableContent viewableContent)
                        {
                            new ViewableContentFrame(dataBase,viewableContent, subscriber,UserProfileFrame.this);
                        }
                    }
                });

                removeFavoriteCheckBox.addItemListener(e -> {
                    int state = e.getStateChange();

                    if (state == ItemEvent.SELECTED)
                    {
                        favoritesToBeRemoved.add(favorite);
                    } else if (state == ItemEvent.DESELECTED) {
                        favoritesToBeRemoved.remove(favorite);
                    }
                });

                removeFromFavoritesButton.addActionListener(e -> removeFavoriteCheckBox.setVisible(true));

                cancelRemovalButton.addActionListener(e -> removeFavoriteCheckBox.setVisible(false));
                favoritesInnerPanel.add(favoritePanel);
            }

            favoritesInnerPanel.add(removeFavoritesOptionsPanel);

            JPanel removeFavoritesButtonPanel=new JPanel();
            removeFavoritesButtonPanel.setBackground(primaryColor);
            removeFavoritesButtonPanel.setLayout(new FlowLayout());
            removeFavoritesButtonPanel.add(removeFromFavoritesButton);
            favoritesOuterPanel.add(removeFavoritesButtonPanel);
        }
    }

    //Method for providing the user with options of how to handle their account(editing it, logging out, or deleting it)
    public void buildOptionsMenu()
    {
        JPanel optionsPanel=new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        optionsPanel.setBackground(primaryColor);

        Color menuForegroundColor=new Color(130,0,0);
        JMenuBar optionsBar=new JMenuBar();
        optionsBar.setLayout((new BoxLayout(optionsBar,BoxLayout.Y_AXIS)));
        optionsBar.setBackground(Color.BLACK);
        JMenu optionsMenu=new JMenu("Options");
        optionsMenu.setForeground(menuForegroundColor);
        JMenuItem editMenuItem=new JMenuItem("Edit account info");
        editMenuItem.setBackground(Color.BLACK);
        editMenuItem.setForeground(menuForegroundColor);
        JMenuItem logoutMenuItem=new JMenuItem("Logout");
        logoutMenuItem.setBackground(Color.BLACK);
        logoutMenuItem.setForeground(menuForegroundColor);
        JMenuItem deleteAccountMenuItem=new JMenuItem("Delete account");
        deleteAccountMenuItem.setBackground(Color.BLACK);
        deleteAccountMenuItem.setForeground(menuForegroundColor);

        optionsMenu.add(editMenuItem);
        optionsMenu.addSeparator();
        optionsMenu.add(logoutMenuItem);
        optionsMenu.addSeparator();
        optionsMenu.add(deleteAccountMenuItem);
        optionsBar.add(optionsMenu);

        editMenuItem.addActionListener(e -> new UserRegistrationFrame(dataBase,user,UserProfileFrame.this));


        logoutMenuItem.addActionListener(e -> {
            new LoginFrame(dataBase);
            dispose();
        });

        deleteAccountMenuItem.addActionListener(e -> {
            int confirmation=JOptionPane.showConfirmDialog(panel,"Are you sure you want to delete your account? All your information will be permanently lost","Confirm account deletion",JOptionPane.YES_NO_OPTION);
            if(confirmation==JOptionPane.YES_OPTION)
            {
                dataBase.removeFromUsers(user);
                new UserRegistrationFrame(dataBase);
                dispose();
            }
        });
        panel.add(optionsPanel);
        optionsPanel.add(new JLabel("           "));
        optionsPanel.add(new JLabel("           "));
        optionsPanel.add(new JLabel("           ")); //decorative labels
        optionsPanel.add(new JLabel("           "));
        optionsPanel.add(new JLabel("           "));
        optionsPanel.add(optionsBar);
    }

    public void updateSubscriberFavoritesPanel()
    {
        dataBase.editFiles();
        favoritesOuterPanel.removeAll();
        buildSubscriberFavoritesPanel();
        favoritesOuterPanel.revalidate();
        favoritesOuterPanel.repaint();
    }

    public void updateUserInfo()
    {
        dataBase.editFiles();
        helloLabel.setText("Hello, "+user.getName());
    }
}
