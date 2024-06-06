package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import static api.Season.seasonNumberIsValid;

//Class for both registering and editing content
public class EditSeasonFrame extends JFrame
{
    private EditContentFrame editContentFrame;
    private ArrayList<Episode> editedEpisodes;
    private ArrayList<Season> seasons;
    private JPanel editEpisodesPanel;
    private JLabel editEpisodesLabel;
    private JTextField editSeasonNumberField,editYearOfReleaseField;
    private Season season;
    private final Admin admin;
    private SeasonFrame seasonFrame;
    private Color primaryColor,textColor;
    private boolean addSeason=false; //This field determines if a season will be registered (added) or edited

    //This constructor is called when editing an existing season
    public EditSeasonFrame(Season season,Admin admin,SeasonFrame seasonFrame)
    {
        editedEpisodes=new ArrayList<>();
        seasons=new ArrayList<>();
        editedEpisodes.addAll(season.getEpisodes());
        this.season=season;
        this.admin=admin;
        this.seasonFrame=seasonFrame;
        buildEditFrame();
    }

    //This constructor is called when registering a new season
    public EditSeasonFrame(ArrayList<Season> seasons, Admin admin, EditContentFrame editContentFrame)
    {
        this.editContentFrame=editContentFrame;
        editedEpisodes=new ArrayList<>();
        addSeason=true;
        this.admin=admin;
        this.seasons=new ArrayList<>();
        this.seasons=seasons;
        this.season=null;
        buildEditFrame();
    }

    public void buildEditFrame()
    {
        setSize(600,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if(season!=null) {
            setTitle("Edit season details");
        }
        else
        {
            setTitle("Register season");
        }
        setLocationRelativeTo(null);

        primaryColor=new Color(85,0,0);
        textColor=Color.WHITE;

        JPanel editPanel=new JPanel();
        add(editPanel);
        editPanel.setLayout(new GridLayout(4,2));
        editPanel.setBackground(primaryColor);

        JLabel editSeasonNumberLabel=new JLabel("Season number:");
        editSeasonNumberLabel.setForeground(textColor);

        editSeasonNumberField=new JTextField("    ");
        editYearOfReleaseField=new JTextField("   ");
        editEpisodesLabel=new JLabel("Episodes:" + editedEpisodes.size());

        if(!addSeason)
        {
            editSeasonNumberField.setText(String.valueOf(season.getSeasonNumber()));
            editYearOfReleaseField.setText(String.valueOf(season.getYearOfRelease()));
        }

        editPanel.add(editSeasonNumberLabel);
        editPanel.add(editSeasonNumberField);

        JLabel editYearOfReleaseLabel=new JLabel("Year of release:");
        editYearOfReleaseLabel.setForeground(textColor);

        editPanel.add(editYearOfReleaseLabel);
        editPanel.add(editYearOfReleaseField);

        editEpisodesLabel.setForeground(textColor);
        editPanel.add(editEpisodesLabel);
        editEpisodesPanel=new JPanel();
        JScrollPane editEpisodesScrollPane=new JScrollPane(editEpisodesPanel);
        editPanel.add(editEpisodesScrollPane);
        editEpisodesPanel.setLayout(new BoxLayout(editEpisodesPanel,BoxLayout.Y_AXIS));
        buildEditEpisodesPanel();

        editPanel.add(new JLabel("     "));

        JPanel savePanel=new JPanel();
        savePanel.setBackground(primaryColor);
        savePanel.setLayout(new FlowLayout());
        editPanel.add(savePanel);

        JButton saveButton=new JButton(" ");
        saveButton.setBackground(textColor);
        saveButton.setForeground(primaryColor);

        if(addSeason)
        {
            saveButton.setText("Register season");
        }
        else
        {
            saveButton.setText("Save changes");
        }

        JButton cancelButton=new JButton("Cancel");
        cancelButton.setForeground(primaryColor);
        cancelButton.setBackground(textColor);
        savePanel.add(saveButton);
        savePanel.add(cancelButton);

        saveButton.addActionListener(e -> {
            if(editSeasonNumberField.getText().replaceAll("\\s", "").isEmpty() || editYearOfReleaseField.getText().replaceAll("\\s", "").isEmpty() || editedEpisodes.isEmpty())
            {
                JOptionPane.showMessageDialog(editPanel,"All fields are mandatory");
            }
            else if(!seasonNumberIsValid(editSeasonNumberField.getText().trim()))
            {
                JOptionPane.showMessageDialog(editPanel,"Season number inserted isn't valid! Check for errors and try again");
            }
            else if(!Season.yearOfReleaseIsValid(editYearOfReleaseField.getText().trim()))
            {
                JOptionPane.showMessageDialog(editPanel,"Year of release inserted isn't valid! Check for errors and try again");
            }
            else
            {
                int confirmation;
                if(addSeason)
                {
                    confirmation=JOptionPane.showConfirmDialog(editPanel,"Register season?","Confirm registration",JOptionPane.YES_NO_OPTION);
                }
                else {
                    confirmation = JOptionPane.showConfirmDialog(editPanel, "Save changes?", "Confirm edit", JOptionPane.YES_NO_OPTION);
                }
                if(confirmation==JOptionPane.YES_OPTION)
                {
                    if(addSeason)
                    {
                        season = new Season(Integer.parseInt(editSeasonNumberField.getText().replaceAll("\\s", "")), Integer.parseInt(editYearOfReleaseField.getText().replaceAll("\\s", "")), editedEpisodes);
                        seasons.add(season);
                        editContentFrame.updateEditSeasonsPanel();
                    }
                    else
                    {
                        season.edit(Integer.parseInt(editSeasonNumberField.getText().replaceAll("\\s", "")), Integer.parseInt(editYearOfReleaseField.getText().replaceAll("\\s", "")), editedEpisodes);
                        seasonFrame.updateDisplayedInfoForSeason();
                    }

                    dispose();
                }
            }
        });

        //Button for discarding all changes made/process of registration
        cancelButton.addActionListener(e -> {
            int confirmation;
            if(addSeason)
            {
                confirmation=JOptionPane.showConfirmDialog(editPanel,"Cancel registration?","Confirm cancellation",JOptionPane.YES_NO_OPTION);
            }
            else {
                confirmation = JOptionPane.showConfirmDialog(editPanel, "Cancel changes?", "Confirm cancellation", JOptionPane.YES_NO_OPTION);
            }
            if(confirmation==JOptionPane.YES_OPTION)
            {
                dispose();
            }
        });

        setVisible(true);
    }

    public void buildEditEpisodesPanel()
    {
        editEpisodesLabel.setText("Episodes: " + editedEpisodes.size());
        if (!editedEpisodes.isEmpty())
        {
            editEpisodesPanel.add(new JLabel("Click on an episode to view more details!"));
        }

        JPanel handleEpisodesPanel = new JPanel();
        handleEpisodesPanel.setLayout(new FlowLayout());

        JButton addToEpisodesButton = new JButton("Add episode");
        addToEpisodesButton.setForeground(primaryColor);
        addToEpisodesButton.setBackground(textColor);

        addToEpisodesButton.addActionListener(e -> new EditEpisodeFrame(editedEpisodes,EditSeasonFrame.this));

        handleEpisodesPanel.add(addToEpisodesButton);

        if (!editedEpisodes.isEmpty())
        {
            buildRemoveEpisodesPanel(handleEpisodesPanel);
        }

        editEpisodesPanel.add(handleEpisodesPanel);
    }

    /*This method gives the admin the options
     of removing selected or all registered episodes
     of the season, or cancelling the removal process*/
    public void buildRemoveEpisodesPanel(JPanel handleEpisodesPanel)
    {
        JButton removeFromEpisodesButton = new JButton("Remove episodes");
        removeFromEpisodesButton.setBackground(textColor);
        removeFromEpisodesButton.setForeground(primaryColor);
        JButton removeSelectedFromEpisodesButton = new JButton("Remove selected from episodes");
        removeSelectedFromEpisodesButton.setForeground(primaryColor);
        removeSelectedFromEpisodesButton.setBackground(textColor);
        JButton removeAllEpisodesButton = new JButton("Remove all episodes");
        removeAllEpisodesButton.setBackground(textColor);
        removeAllEpisodesButton.setForeground(primaryColor);
        JButton cancelRemovalButton = new JButton("Cancel");
        cancelRemovalButton.setForeground(primaryColor);
        cancelRemovalButton.setBackground(textColor);

        handleEpisodesPanel.add(removeFromEpisodesButton);

        JPanel removeEpisodesOptionsPanel = new JPanel();

        removeEpisodesOptionsPanel.setLayout(new FlowLayout());
        removeEpisodesOptionsPanel.setVisible(false);

        ArrayList<Episode> episodesToBeRemoved = new ArrayList<>();

        removeEpisodesOptionsPanel.add(removeSelectedFromEpisodesButton);
        removeEpisodesOptionsPanel.add(removeAllEpisodesButton);
        removeEpisodesOptionsPanel.add(cancelRemovalButton);

        removeFromEpisodesButton.addActionListener(e -> removeEpisodesOptionsPanel.setVisible(true));

        removeSelectedFromEpisodesButton.addActionListener(e -> {
            if (!episodesToBeRemoved.isEmpty())
            {
                int confirmation = JOptionPane.showConfirmDialog(editEpisodesPanel, "Are you sure you want to remove all selected from episodes?", "Confirm removal", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION)
                {
                    Iterator<Episode> it=editedEpisodes.iterator();
                    while(it.hasNext())
                    {
                        Episode episode=it.next();
                        if(episodesToBeRemoved.contains(episode))
                        {
                            it.remove();
                        }
                    }
                    updateEditEpisodesPanel();
                }
            }
        });

        removeAllEpisodesButton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(editEpisodesPanel, "Are you sure you want to remove all episodes?", "Confirm removal", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                int doubleConfirmation = JOptionPane.showConfirmDialog(editEpisodesPanel, "The episode section can't be empty, so you will have to add at least one episode before saving the changes you made", "Confirm removal", JOptionPane.OK_CANCEL_OPTION);
                if (doubleConfirmation == JOptionPane.OK_OPTION) {
                    editedEpisodes.clear();
                    updateEditEpisodesPanel();
                }
            }
        });

        cancelRemovalButton.addActionListener(e -> {
            episodesToBeRemoved.clear();
            removeEpisodesOptionsPanel.setVisible(false);
        });


        /*Iterating through all episodes that are currently in the episode section
        of the edit panel, so that each episode's details can be viewed, and
        check boxes for choosing the ones to be removed are added next to every one*/

        Iterator<Episode> it=editedEpisodes.iterator();
        final boolean[] breakFromIteration = {false};
        while(it.hasNext())
        {
            Episode episode=it.next();
            JPanel episodePanel = new JPanel();
            editEpisodesPanel.add(episodePanel);
            episodePanel.setLayout(new FlowLayout());
            JCheckBox removeEpisodeCheckBox = new JCheckBox();
            removeEpisodeCheckBox.setVisible(false);
            episodePanel.add(removeEpisodeCheckBox);
            JLabel episodeLabel = new JLabel("Episode ");
            episodePanel.add(episodeLabel);

            episodeLabel.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    new EpisodeFrame(episode, admin);
                    breakFromIteration[0] =true;
                }
            });
            if(breakFromIteration[0])
            {
                break;
            }

            removeEpisodeCheckBox.addItemListener(e -> {
                int state = e.getStateChange();

                if (state == ItemEvent.SELECTED) {
                    episodesToBeRemoved.add(episode);
                } else if (state == ItemEvent.DESELECTED) {
                    episodesToBeRemoved.remove(episode);
                }
            });

            removeFromEpisodesButton.addActionListener(e -> removeEpisodeCheckBox.setVisible(true));

            cancelRemovalButton.addActionListener(e -> removeEpisodeCheckBox.setVisible(false));
        }
        if(breakFromIteration[0])
        {
            updateEditEpisodesPanel();
        }
        editEpisodesPanel.add(removeEpisodesOptionsPanel);
    }

    /*This method is called after removing or adding episodes,
    so that the episodes section can be updated appropriately*/
    public void updateEditEpisodesPanel()
    {
        editEpisodesPanel.removeAll();
        buildEditEpisodesPanel();
        editEpisodesPanel.revalidate();
        editEpisodesPanel.repaint();
    }
}