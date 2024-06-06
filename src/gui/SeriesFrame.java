package gui;

import api.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

//Class for viewing a series.It expands upon the class for viewing ViewableContent objects
public class SeriesFrame extends ViewableContentFrame
{
    protected JLabel numberOfSeasonsLabel;
    protected JPanel seasonsOuterPanel,seasonsInnerPanel;
    private Series series;
    private User user;
    public SeriesFrame(DataBase dataBase, Series series, User user,MainPageFrame mainPageFrame,boolean isFoundFromSearchResults)
    {
        super(dataBase,series,user,mainPageFrame,isFoundFromSearchResults);
        this.series=series;
        this.user=user;
        buildSeriesFrame();
    }

    public SeriesFrame(DataBase dataBase,Series series,User user)
    {
        super(dataBase,series,user);
        this.series=series;
        buildSeriesFrame();
    }

    public  SeriesFrame(DataBase dataBase,Series series,User user,UserProfileFrame userProfileFrame)
    {
        super(dataBase,series,user,userProfileFrame);
        this.series=series;
        buildSeriesFrame();
    }


    /*After filling the panel with the standard displayed info
    for ViewableContent objects, the frame is enhanced with series-specific attributes*/
    public void buildSeriesFrame()
    {
        buildSeriesInfoPanel();

        if(user instanceof Admin)
        {
            buildAdminPanelForSeries();
        }

        panel.remove(returnButtonPanel);
        panel.add(returnButtonPanel);
    }

    /*This method and the corresponding seasonsOuterPanel and seasonsInnerPanel
     * are responsible for displaying the seasons of the series*/
    public void buildSeriesInfoPanel()
    {
        int pos=contentInfoPanel.getComponentZOrder(synopsisLabel);
        numberOfSeasonsLabel=new JLabel("Seasons: " + series.getSeasons().size());
        numberOfSeasonsLabel.setForeground(textColor);
        contentInfoPanel.add(numberOfSeasonsLabel,pos+1);

        /*The seasonsOuterPanel contains the scroll pane that will be used for displaying the seasons
         and the seasonsInnerPanel that the scroll pane is wrapped around and that shows the needed info.
         With the labels clickToViewSeasonsLabel and clickToHideSeasonsLabel, the seasonsOuterPanel
         handles when and how the seasonsInnerPanel is displayed*/

        seasonsOuterPanel =new JPanel();
        seasonsOuterPanel.setLayout(new BoxLayout(seasonsOuterPanel,BoxLayout.Y_AXIS));
        seasonsOuterPanel.setBackground(primaryColor);
        JScrollPane seasonsScrollPane=new JScrollPane(seasonsOuterPanel);
        contentInfoPanel.add(seasonsScrollPane,pos+2);
        seasonsInnerPanel=new JPanel();
        seasonsInnerPanel.setBackground(primaryColor);
        seasonsInnerPanel.setLayout(new BoxLayout(seasonsInnerPanel,BoxLayout.Y_AXIS));

        JLabel clickToViewSeasonsLabel=new JLabel("Click here to view all seasons!");
        clickToViewSeasonsLabel.setOpaque(true);
        JLabel clickToHideSeasonsLabel=new JLabel("Click here to hide seasons");
        clickToHideSeasonsLabel.setOpaque(true);
        seasonsOuterPanel.add(clickToViewSeasonsLabel);
        seasonsOuterPanel.add(clickToHideSeasonsLabel);
        seasonsOuterPanel.add(seasonsInnerPanel);
        clickToHideSeasonsLabel.setVisible(false);

        clickToViewSeasonsLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                buildSeasonsInnerPanel(true);
                clickToHideSeasonsLabel.setVisible(true);
                clickToViewSeasonsLabel.setVisible(false);
            }
        });

        clickToHideSeasonsLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                buildSeasonsInnerPanel(false);
                clickToViewSeasonsLabel.setVisible(true);
                clickToHideSeasonsLabel.setVisible(false);
            }
        });
    }

    /*This method displays all seasons when the boolean parameter is true, and removes
    them from the panel when the boolean parameter is false. This is the basis for the
    clickToViewSeasonsLabel and the clickToHideSeasonsLabel, as well as for updating the
    panel after the seasons are edited*/
    public void buildSeasonsInnerPanel(boolean build)
    {
        if(build)
        {
            JLabel clickOnASeasonLabel=new JLabel("Click on a season to view more details!");
            clickOnASeasonLabel.setForeground(textColor);
            seasonsInnerPanel.add(clickOnASeasonLabel);

            for (Season season : series.getSeasons())
            {
                JLabel seasonLabel = new JLabel("Season: " + season.getSeasonNumber());
                seasonLabel.setForeground(textColor);
                seasonLabel.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        new SeasonFrame(series,season, user,SeriesFrame.this);
                    }
                });
                seasonsInnerPanel.add(seasonLabel);
            }
        }
        else
        {
            seasonsInnerPanel.removeAll();
        }
        seasonsInnerPanel.revalidate();
        seasonsInnerPanel.repaint();
    }

    //Calling the appropriate EditContentFrame
    public void buildAdminPanelForSeries()
    {
        buildAdminPanelForContent();

        editContentButton.addActionListener(e -> new EditContentFrame(dataBase,series,(Admin) user,SeriesFrame.this));
    }

    //Updating the extra displayed info after editing
    public void updateDisplayedInfoForSeries()
    {
        numberOfSeasonsLabel.setText("Seasons: " + series.getSeasons().size());
        updateDisplayedContentInfoForViewable();
        buildSeasonsInnerPanel(false);
        buildSeasonsInnerPanel(true);
    }
}

