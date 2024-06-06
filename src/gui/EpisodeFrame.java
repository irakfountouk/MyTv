package gui;

import api.*;

import javax.swing.*;
import java.awt.*;


//Class for viewing an episode
public class EpisodeFrame extends JFrame
{
    private final SeasonFrame seasonFrame;
    private JLabel durationLabel;
    private JPanel panel;
    private final Season season;
    private Episode episode;
    private final User user;
    private Color primaryColor,textColor;
    public EpisodeFrame(Season season, Episode episode, User user,SeasonFrame seasonFrame)
    {
        this.season=season;
        this.episode=episode;
        this.user=user;
        this.seasonFrame=seasonFrame;
        buildFrame();
    }

    /*This constructor is called in the process of registering a new season
       or when editing an existing one to avoid iteration-related runtime errors*/
    public EpisodeFrame(Episode episode, Admin admin)
    {
        this.season=null;
        this.episode=episode;
        this.user=admin;
        this.seasonFrame=null;
        buildFrame();
    }

    public void buildFrame()
    {
        setTitle("Episode");
        setSize(200,250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        primaryColor=new Color(85,0,0);
        textColor=Color.WHITE;
        panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(primaryColor);

        durationLabel=new JLabel("Duration: " + episode.getDuration() + " min");
        durationLabel.setForeground(textColor);
        panel.add(durationLabel);

        if(user instanceof Admin)
        {
            buildAdminPanelForEpisode();
        }

        JPanel closeButtonPanel=new JPanel();
        panel.add(closeButtonPanel);
        closeButtonPanel.setLayout(new FlowLayout());
        closeButtonPanel.setBackground(primaryColor);
        closeButtonPanel.add(new JLabel(""));
        JButton closeButton=new JButton("Close");
        closeButton.setBackground(textColor);
        closeButton.setForeground(primaryColor);
        closeButton.addActionListener(e -> dispose());

        closeButtonPanel.add(closeButton);

        add(panel);
        setVisible(true);
    }
    public void updateEpisodeInfo()
    {
        durationLabel.setText("Duration: " + episode.getDuration()+ " min");
        panel.revalidate();
        panel.repaint();

        if(seasonFrame!=null)
        {
            seasonFrame.updateDisplayedInfoForSeason();
        }
    }

    //Adding the components that are exclusive to admins
    public void buildAdminPanelForEpisode()
    {
        JPanel adminPanel=new JPanel();
        panel.add(adminPanel);
        adminPanel.setLayout(new FlowLayout());
        adminPanel.setBackground(primaryColor);
        JButton editButton=new JButton("Edit episode details");
        editButton.setBackground(textColor);
        editButton.setForeground(primaryColor);
        JButton deleteButton=new JButton("Delete episode");
        deleteButton.setBackground(textColor);
        deleteButton.setForeground(primaryColor);
        adminPanel.add(editButton);

        /*Deleting an episode when editing a season or registering a new one isn't possible,
         in order to avoid runtime errors. An admin can delete an episode by removing it from the season
         in the register/edit season frame*/
        if(season!=null)
        {
            adminPanel.add(deleteButton);

            deleteButton.addActionListener(e -> {
                if(season.getEpisodes().size()==1)
                {JOptionPane.showMessageDialog(EpisodeFrame.this,"Season must contain at least one episode!");}
                else {
                    int confirmation = JOptionPane.showConfirmDialog(EpisodeFrame.this, "Are you sure you want to delete this episode?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION)
                    {
                        season.removeFromEpisodes(episode);
                        if(seasonFrame!=null)
                        {
                            seasonFrame.updateDisplayedInfoForSeason();
                        }
                        dispose();
                    }
                }
            });
        }

        editButton.addActionListener(e -> {
            new EditEpisodeFrame(episode, EpisodeFrame.this);
            if(seasonFrame!=null)
            {
                seasonFrame.updateDisplayedInfoForSeason();
            }
        });
    }
}