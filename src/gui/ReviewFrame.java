package gui;

import api.Content;
import api.DataBase;
import api.Review;
import api.Subscriber;

import javax.swing.*;
import java.awt.*;

//Class for submitting a new review or editing an existing one
public class ReviewFrame extends JFrame
{
    private JTextArea reviewTextArea;
    private JSlider ratingSlider;
    private JScrollPane reviewTextScrollPane;
    private JPanel panel;
    private Color primaryColor,textColor;
    private DataBase dataBase;
    private final Content content;
    private final Subscriber subscriber; //The subscriber who submits the review
    private Review review; /*If a subscriber edits an existing review,
    it's passed as a parameter to the constructor and is stored in this variable.
    In a different case, the variable is initialized as null and, if the subscriber
    submits a review, the new review is stored in it*/
    private boolean addReview=false; /*Variable that determines whether
    a new review will be submitted(added) or an existing one will be edited*/
    private final ReviewsPanel reviewsPanel;

    //This constructor is called when submitting a new review

    public ReviewFrame(DataBase dataBase,Content content, Subscriber subscriber, ReviewsPanel reviewsPanel)
    {
        this.dataBase=dataBase;
        review=null;
        this.content=content;
        this.subscriber=subscriber;
        this.reviewsPanel = reviewsPanel;
        addReview=true;
        buildFrame();
    }

    //This constructor is called when editing a review
    public ReviewFrame(DataBase dataBase,Content content, Subscriber subscriber, Review review, ReviewsPanel reviewsPanel)
    {
        this.dataBase=dataBase;
        this.content=content;
        this.subscriber=subscriber;
        this.review=review;
        this.reviewsPanel = reviewsPanel;
        buildFrame();
    }

    public void buildFrame()
    {
        setTitle("Leave a Review");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        primaryColor=new Color(85,0,0);
        textColor=Color.WHITE;

        panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(primaryColor);

        JLabel reviewLabel = new JLabel("Review: ");
        reviewLabel.setForeground(textColor);

        JLabel ratingLabel = new JLabel("Rating: ");
        ratingLabel.setForeground(textColor);

        buildReviewTextArea();

        buildRatingSlider();

        //If a review is being edited, the fields will display the current review
        if(!addReview && review!=null)
        {
            reviewTextArea.setText(review.getText());
            ratingSlider.setValue(review.getRating());
        }

        JButton submitButton=new JButton("Submit");
        submitButton.setForeground(primaryColor);
        submitButton.setBackground(textColor);

        submitButton.addActionListener(e -> {
            if(reviewTextArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "All fields are mandatory");
            }
            else if(reviewTextArea.getText().length()>500) {
                JOptionPane.showMessageDialog(panel, "The text can't surpass 500 characters");
            }
            else
            {
                if(!addReview && review!=null) //An existing review is being edited
                {
                    review.edit(ratingSlider.getValue(), reviewTextArea.getText());
                }
                else //A new review is being submitted
                {
                    review=new Review(ratingSlider.getValue(), reviewTextArea.getText(), subscriber);
                    content.addToReviews(review);
                }
                dataBase.editFiles();
                reviewsPanel.updateSubscriberReviewPanel();
                dispose();
            }
        });

        panel.add(reviewLabel);
        panel.add(reviewTextScrollPane);
        panel.add(ratingLabel);
        panel.add(ratingSlider);
        panel.add(submitButton);

        add(panel);
        pack();
        setVisible(true);
    }

    //Field for the review text
    public void buildReviewTextArea()
    {
        reviewTextArea =new JTextArea(5, 20);
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setWrapStyleWord(true);
        reviewTextScrollPane =new JScrollPane(reviewTextArea);
        reviewTextScrollPane.setForeground(textColor);
    }

    //Field for the rating
    public void buildRatingSlider()
    {
        ratingSlider=new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
        ratingSlider.setBackground(Color.WHITE);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
    }
}

