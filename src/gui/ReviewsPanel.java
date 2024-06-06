package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Class responsible for displaying the reviews of a piece of content
public class ReviewsPanel extends JPanel
{
    private JPanel innerPanel,subscriberReviewPanel;
    private final JLabel ratingLabel;
    private JLabel noReviewsLabel;
    private final JButton addReviewButton;
    private JScrollPane reviewsScrollPane;
    private DataBase dataBase;
    private Content content;
    private User user;
    private Review review;
    private Color textColor;

    /*The constructor has the components ratingLabel(used for displaying average rating) and addReviewButton
    that are contained in the ContentFrame class passed as parameters, so that
    the appropriate modifications can be made if a subscriber submits or edits a review*/
    public ReviewsPanel(DataBase dataBase,Content content, User user, JLabel ratingLabel, JButton addReviewButton)
    {
        this.content=content;
        this.user=user;
        this.ratingLabel=ratingLabel;
        this.addReviewButton=addReviewButton;
        this.dataBase=dataBase;
        buildPanel();
    }

    public void buildPanel()
    {
        textColor=Color.WHITE;
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.setSize(getPreferredSize());
        this.setBackground(textColor);

        noReviewsLabel =new JLabel("There are no reviews");
        noReviewsLabel.setOpaque(true);

        innerPanel=new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel,BoxLayout.Y_AXIS));
        innerPanel.setBackground(textColor);
        reviewsScrollPane=new JScrollPane(innerPanel);
        reviewsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        reviewsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        if(user instanceof Subscriber)
        {
                /*The subscriberReviewsPanel, that displays the current subscriber's review,
                is added first, even if the subscriber hasn't left a review,
                so that no matter the modifications made, it is always placed in the top of the panel*/
            subscriberReviewPanel=new JPanel();
            subscriberReviewPanel.setLayout(new BoxLayout(subscriberReviewPanel,BoxLayout.Y_AXIS));
            innerPanel.add(subscriberReviewPanel);
            if(content.getReviews().containsKey(user))
            {
                buildSubscriberReviewPanel();
            }
        }

        if(!content.getReviews().isEmpty())
        {
            if(!content.getReviews().containsKey(user))
            {
                    /*if the current user has left a review,
                    the reviewsScrollPane will have been added
                    after having called the buildSubscriberReviewPanel method*/
                add(reviewsScrollPane);
            }
            for (Review review : content.getReviews().values())
            {
                    /*The subscriberReviewPanel has already been added to the panel,
                    so displaying the subscriber's review more than once must be avoided*/
                if(!review.getReviewer().equals(user))
                {
                    innerPanel.add(new JLabel("Rating: " + review.getRating() + "/5"));
                    innerPanel.add(new JLabel("<html><body style='width: 300px;'>" + review.getText() + "</body></html>"));
                    innerPanel.add(new JLabel("Date: " + review.getFormattedDate()));
                    innerPanel.add(new JLabel("-" + review.getReviewer().getName()));
                    innerPanel.add(new JLabel("\n"));
                }
            }
        }
        else if(!content.getReviews().containsKey(user))
        {
                /*if the current user has left a review,
                the noReviewsLabel will have been added
                after having called the buildSubscriberReviewPanel method*/
            add(noReviewsLabel);
        }

        revalidate();
        repaint();
        setVisible(true);
    }

    /*This method and the corresponding subscriberReviewPanel
    handle if and how a subscriber's review is displayed after being added,edited,or deleted,
    as well as the options provided to the subscriber for the review*/
    public void buildSubscriberReviewPanel()
    {
        Color primaryColor=new Color(90,0,0);
        updatePanel();
        ratingLabel.setText("Average rating: " + content.getFormattedAverageRating());

        JLabel editReviewLabel=new JLabel("Edit review");
        editReviewLabel.setForeground(Color.BLACK);
        JLabel deleteReviewLabel=new JLabel("Delete review");
        deleteReviewLabel.setForeground(Color.BLACK);

        review=content.getReviews().get(user);

        subscriberReviewPanel.add(new JLabel("Rating: " + review.getRating() + "/5")).setForeground(primaryColor);
        subscriberReviewPanel.add(new JLabel("<html><body style='width: 300px;'>" + review.getText() + "</body></html>")).setForeground(primaryColor);
        subscriberReviewPanel.add(new JLabel("Date: " + review.getFormattedDate())).setForeground(primaryColor);
        subscriberReviewPanel.add(new JLabel("-" + review.getReviewer().getName())).setForeground(primaryColor);
        subscriberReviewPanel.add(editReviewLabel);
        subscriberReviewPanel.add(deleteReviewLabel);
        subscriberReviewPanel.add(new JLabel(" "));

        if(addReviewButton.isVisible())
        {
            addReviewButton.setVisible(false);
        }

        editReviewLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                new ReviewFrame(dataBase,content,(Subscriber) user,review, ReviewsPanel.this);
                updateSubscriberReviewPanel();
            }
        });

        deleteReviewLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int confirmation=JOptionPane.showConfirmDialog(ReviewsPanel.this,"Are you sure you want to delete this review?","Confirm deletion",JOptionPane.YES_NO_OPTION);
                if(confirmation==JOptionPane.YES_OPTION)
                {
                    content.removeFromReviews(review);
                    if(!content.getReviews().containsKey(user))
                    {
                            /*The subscriber will be given the option to submit a new review
                            after deleting one*/
                        addReviewButton.setVisible(true);
                        dataBase.editFiles();
                        updateSubscriberReviewPanel();
                    }
                }
            }
        });
    }

    /*If a subscriber deletes the sole review in piece of content,
    or adds the first one, the panel for the reviews must be properly updated*/
    public void updatePanel()
    {
        if(getComponentCount()>0)
        {
            removeAll();
        }

        if(content.getReviews().isEmpty())
        {
            add(noReviewsLabel); //Subscriber deleted sole review
        }
        else
        {
            add(reviewsScrollPane);
            //Subscriber added first review
        }

        revalidate();
        repaint();
    }

    /*If a subscriber submits, edits, or deletes a review,
    the label displaying the average rating and the labels displaying
    the review of the subscriber must be properly updated*/
    public void updateSubscriberReviewPanel()
    {
        ratingLabel.setText("Average rating: " + content.getFormattedAverageRating());

        if(subscriberReviewPanel.getComponentCount()>0)
        {
            subscriberReviewPanel.removeAll();
        }
        if(content.getReviews().containsKey(user)) //Subscriber edited or submitted review
        {
            buildSubscriberReviewPanel();
        }
        else
        {
            updatePanel();
        }
        subscriberReviewPanel.revalidate();
        subscriberReviewPanel.repaint();
        innerPanel.revalidate();
        innerPanel.repaint();
    }
}
