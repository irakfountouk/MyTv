package gui;

import api.*;

import javax.swing.*;
import java.util.HashSet;

//Class for viewing ViewableContent objects. It expands upon the class for viewing all types of content
public class ViewableContentFrame extends ContentFrame
{
    protected JLabel synopsisLabel,genreLabel, suitabilityForMinorsLabel;
    private ViewableContent viewableContent;

    public ViewableContentFrame(DataBase dataBase, ViewableContent viewableContent, User user,MainPageFrame mainPageFrame,boolean isFoundFromSearchResults)
    {
        super(dataBase,viewableContent, user,mainPageFrame,isFoundFromSearchResults);
        this.viewableContent=viewableContent;
        buildViewableContentFrame();
    }

    public ViewableContentFrame(DataBase dataBase,ViewableContent viewableContent,User user)
    {
        super(dataBase,viewableContent,user);
        this.viewableContent=viewableContent;
        buildViewableContentFrame();
    }

    public ViewableContentFrame(DataBase dataBase, ViewableContent viewableContent, User user, UserProfileFrame userProfileFrame)
    {
        super(dataBase,viewableContent,user,userProfileFrame);
        this.viewableContent=viewableContent;
        buildViewableContentFrame();
    }

    /*After filling the panel with the standard displayed info
     for Content objects, the frame is enhanced with ViewableContent-specific attributes*/
    public void buildViewableContentFrame()
    {
        setSize(700,350);
        buildViewableContentInfoPanel();

        if(user instanceof Admin)
        {
            if(!(viewableContent instanceof Movie || viewableContent instanceof Series))
            {
                buildAdminPanelForViewableContent();
            }
        }

        /*Since the admin panel is added last in this class, the closeButtonPanel
         will be placed above it. It's best for it to be placed at the
         bottom, and removing it and re-adding it serves just this purpose*/
        panel.remove(returnButtonPanel);
        panel.add(returnButtonPanel);

        if(!this.isVisible())
        {
            setVisible(true);
        }
    }
    //Adding all ViewableContent attributes
    public void buildViewableContentInfoPanel()
    {
        synopsisLabel =new JLabel("<html><body style='width: 300px;'>Synopsis: "+ viewableContent.getSynopsis() + "</body></html>");
        synopsisLabel.setForeground(textColor);
        genreLabel=new JLabel("Genre: " + viewableContent.getGenre());
        genreLabel.setForeground(textColor);
        suitabilityForMinorsLabel =new JLabel("Suitable for minors: " + viewableContent.getIsSuitableForMinors());
        suitabilityForMinorsLabel.setForeground(textColor);
        peopleInvolvedLabel.setText("<html><body style='width: 300px;'>Actors: " + viewableContent.getPeopleInvolved() + "</body></html>");

        int pos=contentInfoPanel.getComponentZOrder(titleLabel);
        contentInfoPanel.add(genreLabel,pos+1);
        contentInfoPanel.add(synopsisLabel,pos+2);
        contentInfoPanel.add(suitabilityForMinorsLabel, pos+3);
        contentInfoPanel.revalidate();
        contentInfoPanel.repaint();
    }

    //Calling the appropriate EditContentFrame
    public void buildAdminPanelForViewableContent()
    {
        buildAdminPanelForContent();

        if(!(viewableContent instanceof Movie || viewableContent instanceof Series))
        {
            editContentButton.addActionListener(e -> new EditContentFrame(dataBase,viewableContent,(Admin) user,ViewableContentFrame.this));
        }
    }

    //Updating the extra displayed info after editing
    public void updateDisplayedContentInfoForViewable()
    {
        updateDisplayedContentInfo();

        peopleInvolvedLabel.setText("<html><body style='width: 300px;'>Actors: " + viewableContent.getPeopleInvolved() + "</body></html>");
        genreLabel.setText("Genre: " + viewableContent.getGenre());
        synopsisLabel.setText("<html><body style='width: 300px;'>Synopsis: " + viewableContent.getSynopsis() + "</body></html>");
        suitabilityForMinorsLabel.setText("Suitable for minors: " + viewableContent.getIsSuitableForMinors());

        contentInfoPanel.revalidate();
        contentInfoPanel.repaint();
    }
}
