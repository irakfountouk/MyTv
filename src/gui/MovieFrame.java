
package gui;

import api.*;

import javax.swing.*;

//Class for viewing a movie. It expands upon the class for viewing ViewableContent objects
public class MovieFrame extends ViewableContentFrame
{
    protected JLabel yearOfReleaseLabel,durationLabel;
    private Movie movie;

    public MovieFrame(DataBase dataBase,Movie movie, User user,MainPageFrame mainPageFrame,boolean isFoundFromSearchResults)
    {
        super(dataBase,movie,user,mainPageFrame,isFoundFromSearchResults);
        this.movie=movie;
        buildMovieFrame();
    }

    public MovieFrame(DataBase dataBase,Movie movie,User user)
    {
        super(dataBase,movie,user);
        this.movie=movie;
        buildMovieFrame();
    }

    public MovieFrame(DataBase dataBase,Movie movie,User user,UserProfileFrame userProfileFrame)
    {
        super(dataBase,movie,user,userProfileFrame);
        this.movie=movie;
        buildMovieFrame();
    }

    /*After filling the panel with the standard displayed info
    for ViewableContent objects, the frame is enhanced with movie-specific attributes*/
    public void buildMovieFrame()
    {
        setSize(700,600);
        buildMovieInfoPanel();

        if(user instanceof Admin)
        {
            buildAdminPanelForMovie();
        }

        panel.remove(returnButtonPanel);
        panel.add(returnButtonPanel);
        pack();

        if(!this.isVisible())
        {
            setVisible(true);
        }
    }

    //Adding all movie attributes
    public void buildMovieInfoPanel()
    {
        yearOfReleaseLabel=new JLabel("Year of release: " + movie.getYearOfRelease());
        yearOfReleaseLabel.setForeground(textColor);
        durationLabel=new JLabel("Duration: " + movie.getDuration() + " min");
        durationLabel.setForeground(textColor);

        int pos=contentInfoPanel.getComponentZOrder(peopleInvolvedLabel);
        contentInfoPanel.add(yearOfReleaseLabel,pos+1);
        contentInfoPanel.add(durationLabel,pos+2);

        contentInfoPanel.revalidate();
        contentInfoPanel.repaint();
    }

    //Calling the appropriate EditContentFrame
    public void buildAdminPanelForMovie()
    {
        buildAdminPanelForContent();

        editContentButton.addActionListener(e -> new EditContentFrame(dataBase,movie,(Admin) user,MovieFrame.this));
    }

    //Updating the extra displayed info after editing
    public void updateDisplayedContentInfoForMovie()
    {
        updateDisplayedContentInfoForViewable();

        yearOfReleaseLabel.setText("Year of release: " + movie.getYearOfRelease());
        durationLabel.setText("Duration: " + movie.getDuration() + " min");
        contentInfoPanel.revalidate();
        contentInfoPanel.repaint();
    }
}