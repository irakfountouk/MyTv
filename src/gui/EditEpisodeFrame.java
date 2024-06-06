package gui;

import api.Episode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static api.Episode.durationIsValid;

//Class for both registering and editing episodes
public class EditEpisodeFrame extends JFrame
{
    private EditSeasonFrame editSeasonFrame;
    private ArrayList<Episode> episodes;
    private EpisodeFrame episodeFrame;
    private Episode episode;
    private Color primaryColor,textColor;
    private boolean addEpisode=false;
    private JTextField editDurationField;

    //This constructor is called when editing an existing episode
    public EditEpisodeFrame(Episode episode,EpisodeFrame episodeFrame)
    {
        episodes=new ArrayList<>();
        this.episodeFrame=episodeFrame;
        this.episode=episode;
        buildEditFrame();
    }

    //This constructor is called when registering a new episode
    public EditEpisodeFrame(ArrayList<Episode> episodes,EditSeasonFrame editSeasonFrame)
    {
        this.episodes=new ArrayList<>();
        this.editSeasonFrame=editSeasonFrame;
        this.episodes=episodes;
        this.episode=null;
        addEpisode=true;
        buildEditFrame();
    }
    public void buildEditFrame()
    {
        setSize(400,200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        if(!addEpisode) {
            setTitle("Edit episode details");
        }
        else
        {
            setTitle("Register Episode");
        }
        setLocationRelativeTo(null);

        primaryColor=new Color(85,0,0);
        textColor=Color.WHITE;

        JPanel editPanel=new JPanel();
        add(editPanel);
        editPanel.setLayout(new BoxLayout(editPanel,BoxLayout.Y_AXIS));
        editPanel.setBackground(primaryColor);

        JPanel editDurationPanel=new JPanel();
        editDurationPanel.setBackground(primaryColor);
        editPanel.add(editDurationPanel);
        editDurationPanel.setLayout(new FlowLayout());
        JLabel editDurationLabel=new JLabel("Duration (min): ");
        editDurationLabel.setForeground(textColor);

        editDurationField=new JTextField("   ");
        editDurationField.setSize(getSize());
        editDurationField.setEditable(true);

        if(!addEpisode)
        {
            editDurationField.setText(String.valueOf(episode.getDuration()));
        }

        editDurationField.setSize(editDurationLabel.getSize());
        editDurationPanel.add(editDurationLabel);
        editDurationPanel.add(editDurationField);

        JPanel savePanel=new JPanel();
        savePanel.setBackground(primaryColor);
        savePanel.setLayout(new FlowLayout());
        editPanel.add(savePanel);

        JButton saveButton=new JButton(" ");
        saveButton.setForeground(primaryColor);
        saveButton.setBackground(textColor);

        if(addEpisode)
        {
            saveButton.setText("Register episode");
        }
        else
        {
            saveButton.setText("Edit episode");
        }

        JButton cancelButton=new JButton("Cancel");
        cancelButton.setBackground(textColor);
        cancelButton.setForeground(primaryColor);
        savePanel.add(saveButton);
        savePanel.add(cancelButton);

        //Button for saving the changes made/registering the new episode
        saveButton.addActionListener(e -> {
            if(editDurationField.getText().replaceAll("\\s", "").isEmpty())
            {
                JOptionPane.showMessageDialog(editPanel,"All fields are mandatory");
            }
            else if(!durationIsValid(editDurationField.getText().trim()))
            {
                JOptionPane.showMessageDialog(editPanel,"Duration inserted isn't valid! Check for errors and try again");
            }
            else
            {
                int confirmation;
                if(addEpisode)
                {
                    confirmation=JOptionPane.showConfirmDialog(editPanel,"Register episode?","Confirm registration",JOptionPane.YES_NO_OPTION);
                }
                else {
                    confirmation = JOptionPane.showConfirmDialog(editPanel, "Save changes?", "Confirm edit", JOptionPane.YES_NO_OPTION);
                }
                if(confirmation==JOptionPane.YES_OPTION)
                {
                    if(addEpisode)
                    {
                        episode=new Episode(Integer.parseInt(editDurationField.getText().replaceAll("\\s", "")));
                        episodes.add(episode);
                        editSeasonFrame.updateEditEpisodesPanel();
                    }
                    else
                    {
                        episode.edit(Integer.parseInt(editDurationField.getText().replaceAll("\\s", "")));
                        episodeFrame.updateEpisodeInfo();

                    }
                    dispose();
                }
            }
        });

        //Button for discarding changes/process of registration
        cancelButton.addActionListener(e ->
        {
            int confirmation;
            if(addEpisode)
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
}
