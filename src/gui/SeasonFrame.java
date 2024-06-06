package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


//Class for viewing a season's details
public class SeasonFrame extends JFrame
{
    private final SeriesFrame seriesFrame;
    private JPanel panel;
    private JPanel episodesInnerPanel;
    private JLabel numberOfEpisodesLabel,yearOfReleaseLabel,seasonNumberLabel;
    private Season season;
    private final User user;
    private final Series series;
    private Color primaryColor,textColor;

    //This constructor is called when viewing a series from the main page
    public SeasonFrame(Series series,Season season, User user,SeriesFrame seriesFrame)
    {
        this.season=season;
        this.user=user;
        this.series=series;
        this.seriesFrame=seriesFrame;
        buildFrame();
    }

    /*This constructor is called in the process of registering a new series
      or when editing an existing one,to avoid iteration-related runtime errors*/
    public SeasonFrame(Season season,Admin admin)
    {
        series=null;
        seriesFrame=null;
        this.user=admin;
        this.season=season;
        buildFrame();
    }

    public void buildFrame()
    {
        setTitle("Season " + season.getSeasonNumber());
        setSize(400,200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        primaryColor=new Color(85,0,0);
        textColor=Color.WHITE;

        panel=new JPanel();
        add(panel);
        panel.setBackground(primaryColor);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        buildSeasonInfoPanel();

        if(user instanceof Admin)
        {
            buildAdminPanelForSeason();
        }

        JPanel closeButtonPanel=new JPanel();
        panel.add(closeButtonPanel);
        closeButtonPanel.setLayout(new FlowLayout());
        closeButtonPanel.setBackground(primaryColor);
        closeButtonPanel.add(new JLabel(""));
        JButton closeButton=new JButton("Close");
        closeButton.setForeground(primaryColor);
        closeButton.setBackground(textColor);
        closeButton.addActionListener(e -> dispose());

        closeButtonPanel.add(closeButton);

        add(panel);
        setVisible(true);
    }

    //This method helps display the season's info
    public void buildSeasonInfoPanel()
    {
        JPanel seasonInfoPanel = new JPanel();
        panel.add(seasonInfoPanel);
        seasonInfoPanel.setBackground(primaryColor);
        seasonInfoPanel.setLayout(new BoxLayout(seasonInfoPanel,BoxLayout.Y_AXIS));

        numberOfEpisodesLabel=new JLabel("Episodes: " + season.getEpisodes().size());
        numberOfEpisodesLabel.setForeground(textColor);
        yearOfReleaseLabel=new JLabel("Year of release: " + season.getYearOfRelease());
        yearOfReleaseLabel.setForeground(textColor);
        seasonNumberLabel=new JLabel("Season: " + season.getSeasonNumber());
        seasonNumberLabel.setForeground(textColor);

        seasonInfoPanel.add(seasonNumberLabel);
        seasonInfoPanel.add(yearOfReleaseLabel);
        seasonInfoPanel.add(numberOfEpisodesLabel);

        JPanel episodesOuterPanel = new JPanel();
        episodesOuterPanel.setLayout(new BoxLayout(episodesOuterPanel,BoxLayout.Y_AXIS));
        episodesOuterPanel.setBackground(primaryColor);
        JScrollPane episodesScrollPane=new JScrollPane(episodesOuterPanel);
        seasonInfoPanel.add(episodesScrollPane);

        episodesInnerPanel=new JPanel();
        episodesInnerPanel.setBackground(primaryColor);
        episodesInnerPanel.setLayout(new BoxLayout(episodesInnerPanel,BoxLayout.Y_AXIS));

        JLabel clickToViewEpisodesLabel=new JLabel("Click here to view all episodes!");
        clickToViewEpisodesLabel.setOpaque(true);
        JLabel clickToHideEpisodesLabel=new JLabel("Click here to hide episodes");
        clickToHideEpisodesLabel.setOpaque(true);

        /*The episodesOuterPanel contains the scroll pane that will be used for displaying the episodes
         and the episodesInnerPanel that the scroll pane is wrapped around and that shows the needed info.
         With the labels clickToViewEpisodesLabel and clickToHideEpisodesLabel, the episodesOuterPanel
         handles when and how the episodesInnerPanel is displayed*/

        episodesOuterPanel.add(clickToViewEpisodesLabel);
        episodesOuterPanel.add(clickToHideEpisodesLabel);
        episodesOuterPanel.add(episodesInnerPanel);
        clickToHideEpisodesLabel.setVisible(false);

        clickToViewEpisodesLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                buildEpisodesInnerPanel(true);
                clickToHideEpisodesLabel.setVisible(true);
                clickToViewEpisodesLabel.setVisible(false);
            }
        });

        clickToHideEpisodesLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                buildEpisodesInnerPanel(false);
                clickToViewEpisodesLabel.setVisible(true);
                clickToHideEpisodesLabel.setVisible(false);
            }
        });
    }

    /*This method displays all episodes when the boolean parameter is true, and removes
    them from the panel when the boolean parameter is false. This is the basis for the
    clickToViewEpisodesLabel and the clickToHideEpisodesLabel, as well as for updating the
    panel after the episodes are edited*/
    public void buildEpisodesInnerPanel(boolean build)
    {
        numberOfEpisodesLabel.setText("Episodes: " + season.getEpisodes().size());
        JLabel clickOnAnEpisodeLabel=new JLabel("Click on an episode to view more details!");
        clickOnAnEpisodeLabel.setForeground(textColor);
        episodesInnerPanel.add(clickOnAnEpisodeLabel);

        final boolean[] breakFromIteration = {false};
        if(build)
        {
            for (Episode episode : season.getEpisodes())
            {
                JLabel episodeLabel = new JLabel("Episode");
                episodeLabel.setForeground(textColor);
                episodeLabel.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        new EpisodeFrame(season, episode, user, SeasonFrame.this);
                        breakFromIteration[0] =true;
                    }
                });
                if(breakFromIteration[0])
                {
                    break;
                }

                episodesInnerPanel.add(episodeLabel);
            }
            if(breakFromIteration[0])
            {
                updateDisplayedInfoForSeason();
            }
        }
        else
        {
            episodesInnerPanel.removeAll();
        }

        episodesInnerPanel.revalidate();
        episodesInnerPanel.repaint();
    }

    //Adding the components that are exclusive to admins
    public void buildAdminPanelForSeason()
    {
        JButton editSeasonButton =new JButton("Edit season details");
        editSeasonButton.setBackground(textColor);
        editSeasonButton.setForeground(primaryColor);

        editSeasonButton.addActionListener(e -> new EditSeasonFrame(season,(Admin) user,SeasonFrame.this));

        JButton deleteButton =new JButton("Delete season");
        deleteButton.setBackground(textColor);
        deleteButton.setForeground(primaryColor);

        /*Deleting a season when editing a series or registering a new one isn't possible,
         in order to avoid runtime errors. An admin can delete a season by removing it from the series
         in the register/edit season frame*/
        if(series!=null)
        {
            deleteButton.addActionListener(e -> {
                if(series.getSeasons().size()==1)
                {
                    JOptionPane.showMessageDialog(SeasonFrame.this,"Series must contain at lest one season!");
                }
                else {
                    int confirmResult = JOptionPane.showConfirmDialog(SeasonFrame.this, "Are you sure you want to delete this season?", "Confirm deletion", JOptionPane.YES_NO_OPTION);

                    if (confirmResult == JOptionPane.YES_OPTION) {
                        series.removeFromSeasons(season);
                        if (seriesFrame != null)
                        {
                            seriesFrame.updateDisplayedInfoForSeries();
                        }
                        dispose();
                    }
                }
            });
        }

        JPanel adminPanel=new JPanel();
        adminPanel.setLayout(new FlowLayout());
        adminPanel.setBackground(primaryColor);
        adminPanel.add(editSeasonButton);
        if(series!=null) {
            adminPanel.add(deleteButton);
        }
        panel.add(adminPanel);
    }


    //After editing a season, this method is called to ensure that the displayed info is up-to-date
    public void updateDisplayedInfoForSeason()
    {
        setTitle("Season " + season.getSeasonNumber());
        seasonNumberLabel.setText("Season: " + season.getSeasonNumber());
        numberOfEpisodesLabel.setText("Episodes: " + season.getEpisodes().size());
        yearOfReleaseLabel.setText("Year of release: " + season.getYearOfRelease());
        buildEpisodesInnerPanel(false);
        buildEpisodesInnerPanel(true);
        panel.revalidate();
        panel.repaint();

        if(seriesFrame!=null)
        {
            seriesFrame.updateDisplayedInfoForSeries();
        }
    }
}
