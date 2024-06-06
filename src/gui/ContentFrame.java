package gui;

import api.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Class for viewing content details
public class ContentFrame extends JFrame
{
    protected JPanel panel, contentInfoPanel,adminPanel,relatedContentPanel, subscriberPanel, returnButtonPanel;
    protected ReviewsPanel reviewsPanel;
    protected JLabel titleLabel, ratingLabel, peopleInvolvedLabel, reviewsLabel,
            relatedContentLabel;
    protected JButton editContentButton, deleteContentButton,addReviewButton,favoritesButton;
    protected JScrollPane relatedContentScrollPane;
    protected Color primaryColor,textColor;
    protected DataBase dataBase;
    private Content content;
    protected final User user;
    protected MainPageFrame mainPageFrame;
    protected UserProfileFrame userProfileFrame;
    protected boolean isAccessedFromAnotherContent=false,isFoundFromSearchResults=false;

    /*This constructor is called when the current user is
    an admin accessing content details via the main page,
    so that the main page can be updated if any changes are made*/
    public ContentFrame(DataBase dataBase,Content content,User user,MainPageFrame mainPageFrame,boolean isFoundFromSearchResults)
    {
        userProfileFrame=null;
        this.mainPageFrame=mainPageFrame;
        this.dataBase=dataBase;
        this.content=content;
        this.user=user; //user who's currently logged in
        this.isFoundFromSearchResults=isFoundFromSearchResults;
        buildContentFrame();
    }

    /*This constructor is called when the current user is a subscriber or
    when an admin is accessing content details via a different content frame,
    in order not to unnecessarily pass the main page as a parameter*/

    public ContentFrame(DataBase dataBase,Content content,User user)
    {
        userProfileFrame=null;
        this.mainPageFrame=null;
        this.dataBase=dataBase;
        this.content=content;
        this.user=user;

        /*Content deletion and editing are possible only by directly
        accessing the content, to avoid iteration-related
        runtime errors.This boolean parameter helps with deciding
        whether these options will be provided to the admin*/
        isAccessedFromAnotherContent=true;
        buildContentFrame();
    }

    /*This constructor is called when a user is accessing content details via
     their profile, so sections like the favorites section can be properly updated*/
    public ContentFrame(DataBase dataBase,Content content,User user,UserProfileFrame userProfileFrame)
    {
        this.userProfileFrame=userProfileFrame;
        mainPageFrame=null;
        this.dataBase=dataBase;
        this.content=content;
        this.user=user;
        buildContentFrame();
    }

    protected void buildContentFrame()
    {
        setTitle(content.getTitle());
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        primaryColor=new Color(85,0,0);
        textColor=Color.WHITE;
        setBackground(primaryColor);

        panel=new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(primaryColor);

        buildContentInfoPanel();

        panel.add(contentInfoPanel);

        /*Determining the type of user so that the options provided for this content
        (e.g. leaving a review if the user is a subscriber or editing the content's details
        if the user is an admin) are appropriate*/

        if (user instanceof Admin)
        {
            if(!(content instanceof ViewableContent))
            {
                buildAdminPanelForContent();
            }
        }
        else
        {
            buildSubscriberPanel(content);
        }

        //creating a panel for the close button to more flexibly handle the way appears
        returnButtonPanel =new JPanel();
        returnButtonPanel.setLayout(new FlowLayout());
        returnButtonPanel.setBackground(primaryColor);
        returnButtonPanel.add(new JLabel(" "));
        JButton returnButton=new JButton("Return");

        returnButton.setAlignmentX(Button.CENTER_ALIGNMENT);
        returnButton.setForeground(primaryColor);
        returnButton.setBackground(textColor);
        returnButtonPanel.add(returnButton);

        returnButton.addActionListener(e -> dispose());

        panel.add(returnButtonPanel);
        add(panel);

        pack();

        setVisible(true);
    }

    /*This method and the corresponding panel (contentInfoPanel)
    handle the display of the info of the given content */
    public void buildContentInfoPanel()
    {
        contentInfoPanel =new JPanel();
        contentInfoPanel.setSize(getPreferredSize());
        contentInfoPanel.setLayout(new BoxLayout(contentInfoPanel, BoxLayout.Y_AXIS));
        contentInfoPanel.setBackground(primaryColor);

        titleLabel=new JLabel("Title: " + content.getTitle());
        titleLabel.setForeground(textColor);
        contentInfoPanel.add(titleLabel);

        ratingLabel=new JLabel();
        ratingLabel.setForeground(textColor);
        ratingLabel.setText("Average Rating: " + content.getFormattedAverageRating());
        ratingLabel.setForeground(textColor);
        contentInfoPanel.add(ratingLabel);

        peopleInvolvedLabel=new JLabel("<html><body style='width: 300px;'>People involved in the making: " + content.getPeopleInvolved() + "</body></html>");
        peopleInvolvedLabel.setForeground(textColor);
        contentInfoPanel.add(peopleInvolvedLabel);

        relatedContentPanel =new JPanel();
        relatedContentPanel.setLayout(new BoxLayout(relatedContentPanel,BoxLayout.Y_AXIS));
        relatedContentPanel.setSize(getPreferredSize());
        relatedContentLabel =new JLabel("Related Content: ");
        relatedContentPanel.setBackground(Color.WHITE);
        relatedContentLabel.setForeground(textColor);
        relatedContentScrollPane=new JScrollPane(relatedContentPanel);

        contentInfoPanel.add(relatedContentLabel);
        contentInfoPanel.add(relatedContentScrollPane);

        buildRelatedContentPanel(!content.getRelatedContent().isEmpty());

        addReviewButton=new JButton("Leave a review");
        addReviewButton.setBackground(textColor);
        addReviewButton.setForeground(primaryColor);

        reviewsLabel=new JLabel("Reviews: ");
        reviewsLabel.setForeground(textColor);
        contentInfoPanel.add(reviewsLabel);
        //JScrollPane reviewsScrollPane=new JScrollPane(reviewsPanel=new ReviewsPanel1(content,user,ratingLabel,addReviewButton));
        //reviewsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        contentInfoPanel.add(reviewsPanel=new ReviewsPanel(dataBase,content,user,ratingLabel,addReviewButton));
    }

    /*This method and the corresponding relatedContentPanel display the related content
    (if there is any) of the given content if the boolean parameter is true, or removes it
    if the boolean parameter is false. This way, the panel can be
    properly updated after the related content is edited*/
    public void buildRelatedContentPanel(boolean build)
    {
        relatedContentPanel.add(new JLabel("Click on a title for more details\n\n"));
        if(build)
        {
            for (Content content1 : content.getRelatedContent())
            {
                JLabel relatedContentTitleLabel = new JLabel();
                relatedContentTitleLabel.setText("-" + content1.getTitle());

                relatedContentTitleLabel.setForeground(Color.BLACK);
                relatedContentTitleLabel.addMouseListener(new MouseAdapter()
                {
                    /*If the title label is clicked, a new frame for
                    the content with this title will pop up, to provide more details*/
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        if(!(content1 instanceof ViewableContent))
                        {
                            new ContentFrame(dataBase,content1, user);
                        }
                        else if(content1 instanceof Movie movie)
                        {
                            new MovieFrame(dataBase,movie,user);
                        }
                        else if(content1 instanceof Series series)
                        {
                            new SeriesFrame(dataBase,series, user);
                        }
                        else if(content1 instanceof ViewableContent viewableContent)
                        {
                            new ViewableContentFrame(dataBase,viewableContent,user);
                        }
                    }
                });
                relatedContentPanel.add(relatedContentTitleLabel);
            }

            if(!relatedContentLabel.isVisible())
            {
                relatedContentLabel.setVisible(true);
            }
            if(!relatedContentScrollPane.isVisible())
            {
                relatedContentScrollPane.setVisible(true);
            }
        }
        else
        {
            if(relatedContentLabel.isVisible())
            {
                relatedContentLabel.setVisible(false);
            }
            if(relatedContentScrollPane.isVisible())
            {
                relatedContentScrollPane.setVisible(false);
            }

            if(relatedContentPanel.getComponentCount()>0)
            {
                relatedContentPanel.removeAll();
            }
        }

        contentInfoPanel.revalidate();
        contentInfoPanel.repaint();
        relatedContentPanel.revalidate();
        relatedContentPanel.repaint();
        relatedContentScrollPane.revalidate();
        relatedContentScrollPane.repaint();
    }

    /*Setting up the components that will be visible exclusively to admins.
    Each subclass has a similar method that calls this one, but the function of the edit button
    changes each time*/
    public void buildAdminPanelForContent()
    {
        editContentButton =new JButton("Edit content details");
        editContentButton.setForeground(primaryColor);
        editContentButton.setBackground(textColor);

        /*This action listener of the edit button changes based on the  specific type of content,
         since the EditContentFrame is different for each type. This way, though this method is accessed
         by the subclasses, the only EditContentFrame that appears is the one that corresponds to the
         specific category*/
        if(!(content instanceof ViewableContent))
        {
            editContentButton.addActionListener(e -> {
                new EditContentFrame(dataBase,content,(Admin) user,ContentFrame.this);
                if(mainPageFrame!=null)
                {
                    mainPageFrame.buildAppearingContentPanel();
                }
            });
        }

        deleteContentButton =new JButton("Delete content");
        deleteContentButton.setBackground(textColor);
        deleteContentButton.setForeground(primaryColor);
        deleteContentButton.addActionListener(e -> {
            int confirmResult=JOptionPane.showConfirmDialog(ContentFrame.this,"Are you sure you want to delete this content?","Confirm deletion",JOptionPane.YES_NO_OPTION);

            if (confirmResult == JOptionPane.YES_OPTION)
            {
                dataBase.removeFromContent(content);
                if(isFoundFromSearchResults)
                {
                    mainPageFrame.buildSearchResultsPanel();
                }
                else if(mainPageFrame!=null)
                {
                    mainPageFrame.buildAppearingContentPanel();
                }
                dispose();
            }
        });

        adminPanel =new JPanel();
        adminPanel.setLayout(new FlowLayout());
        adminPanel.setBackground(primaryColor);

        if(!isAccessedFromAnotherContent)
        {
            adminPanel.add(editContentButton);
            adminPanel.add(deleteContentButton);
        }
        panel.add(adminPanel);
    }

    //Setting up the components that will be exclusive to subscribers
    private void buildSubscriberPanel(Content content)
    {
        Subscriber subscriber=(Subscriber) user;
        subscriberPanel =new JPanel();
        subscriberPanel.setLayout(new FlowLayout());
        subscriberPanel.setBackground(primaryColor);

        favoritesButton=new JButton();
        favoritesButton.setForeground(primaryColor);
        favoritesButton.setBackground(textColor);

        if (!subscriber.getFavorites().contains(content))
        {
            favoritesButton.setText("Add to favorites");
        }
        else {
            favoritesButton.setText("Remove from favorites");
        }


        subscriberPanel.add(addReviewButton);

        /*The subscriber won't be given the option to add a review
        if they have already submitted one.They can either edit the review, or delete it and
        submit a new one*/

        if(content.getReviews().containsKey(subscriber)) {
            addReviewButton.setVisible(false);
        }

        addReviewButton.addActionListener(e -> {
            new ReviewFrame(dataBase,content,subscriber,reviewsPanel);
        });

        favoritesButton.addActionListener(e ->
        {
            if(!subscriber.getFavorites().contains(content))
            {
                favoritesButton.setText("Add to favorites");
                subscriber.addToFavorites(content);
                if(subscriber.getFavorites().contains(content))
                {
                    favoritesButton.setText("Remove from favorites");
                }
            }
            else
            {
                favoritesButton.setText("Remove from favorites");
                subscriber.removeFromFavorites(content);
                if(!subscriber.getFavorites().contains(content))
                {
                    favoritesButton.setText("Add to favorites");
                }
            }
            dataBase.editFiles();

            if(userProfileFrame!=null)
            {
                userProfileFrame.updateSubscriberFavoritesPanel();
            }
        });
        subscriberPanel.add(favoritesButton);

        panel.add(subscriberPanel);
    }

    /*Updating the displayed info after an admin is done editing this content.
    This method is called by the EditContentFrame*/
    public void updateDisplayedContentInfo()
    {
        dataBase.editFiles();

        if (!content.getRelatedContent().isEmpty())
        {
            buildRelatedContentPanel(false);
            buildRelatedContentPanel(true); //If the related section isn't empty, the panel needs to be rebuilt (updated)
        }
        else
        {
            buildRelatedContentPanel(false); //If the section is empty, rebuilding the panel isn't necessary
        }

        titleLabel.setText("Title: " + content.getTitle());
        setTitle(content.getTitle());
        peopleInvolvedLabel.setText("<html><body style='width: 300px;'>People involved in the making: " + content.getPeopleInvolved() + "</body></html>");

        contentInfoPanel.revalidate();
        contentInfoPanel.repaint();

        if(mainPageFrame!=null)
        {
            if(isFoundFromSearchResults)
            {
                mainPageFrame.buildSearchResultsPanel();
            }
            else {
                mainPageFrame.buildAppearingContentPanel();
            }
        }
    }


}