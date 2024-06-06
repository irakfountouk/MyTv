
package gui;

import api.Content;
import api.DataBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;

//This class is for searching content in the Database
public class SearchPanel extends JPanel
{
    private ArrayList<Content> searchResults;
    private DataBase dataBase;
    private MainPageFrame mainPageFrame;
    private RelatedContentEditPanel.AddToRelatedContentFrame addToRelatedContentFrame;

    /*The searchResults ArrayList that's passed as a parameter will be used to
    transfer the results to the frame that used the class*/

    /*The frame is passed as a parameter so that the appearing content on it can be updated after searching*/
    public SearchPanel(DataBase dataBase,JFrame frame,ArrayList<Content> searchResults)
    {
        this.addToRelatedContentFrame=null;
        this.mainPageFrame=null;
        this.dataBase=dataBase;
        if(frame instanceof MainPageFrame)
        {
            mainPageFrame=(MainPageFrame) frame;
        }
        else if(frame instanceof RelatedContentEditPanel.AddToRelatedContentFrame)
        {
            addToRelatedContentFrame=(RelatedContentEditPanel.AddToRelatedContentFrame) frame;
        }
        this.searchResults=new ArrayList<>();
        this.searchResults=searchResults;
        buildSearchPanel();
    }

    public void buildSearchPanel()
    {
        Color primaryColor=new Color(85,0,0);
        Color textColor=Color.WHITE;

        setBackground(primaryColor);

        JPanel searchPanel=new JPanel();
        searchPanel.setBackground(primaryColor);
        setLayout(new FlowLayout());

        add(searchPanel);

        /*Panel intended to let the user determine the criteria they're searching for.
         * That is, type of content, genre, minimum average rating, and whether it be suitable for minors*/
        JPanel searchFiltersPanel=new JPanel();
        searchFiltersPanel.setBackground(primaryColor);
        searchFiltersPanel.setLayout(new FlowLayout());
        searchPanel.add(searchFiltersPanel);

        JComboBox<String> categoryComboBox=new JComboBox<>();
        categoryComboBox.addItem("");
        categoryComboBox.addItem("Movie");
        categoryComboBox.addItem("Series");

        JPanel categoryComboBoxPanel=new JPanel();
        categoryComboBoxPanel.setBackground(primaryColor);
        categoryComboBoxPanel.setLayout(new BoxLayout(categoryComboBoxPanel,BoxLayout.Y_AXIS));
        JLabel categoryLabel=new JLabel("Category");
        categoryLabel.setForeground(textColor);
        categoryComboBoxPanel.add(categoryLabel);
        categoryComboBoxPanel.add(categoryComboBox);
        searchFiltersPanel.add(categoryComboBoxPanel);

        JComboBox<String> genreComboBox = new JComboBox<>();
        genreComboBox.addItem("");
        genreComboBox.addItem("Action");
        genreComboBox.addItem("Drama");
        genreComboBox.addItem("Comedy");
        genreComboBox.addItem("Science fiction");
        genreComboBox.addItem("Horror");

        JPanel genreComboBoxPanel = new JPanel();
        genreComboBoxPanel.setBackground(primaryColor);
        genreComboBoxPanel.setLayout(new BoxLayout(genreComboBoxPanel, BoxLayout.Y_AXIS));
        JLabel genreLabel=new JLabel("Genre");
        genreLabel.setForeground(textColor);
        genreComboBoxPanel.add(genreLabel);
        genreComboBoxPanel.add(genreComboBox);
        searchFiltersPanel.add(genreComboBoxPanel);

        JComboBox<String> minimumAverageRatingComboBox=new JComboBox<>();
        minimumAverageRatingComboBox.addItem("");
        minimumAverageRatingComboBox.addItem("1");
        minimumAverageRatingComboBox.addItem("2");
        minimumAverageRatingComboBox.addItem("3");
        minimumAverageRatingComboBox.addItem("4");
        minimumAverageRatingComboBox.addItem("5");

        JPanel minimumAverageRatingPanel = new JPanel();
        minimumAverageRatingPanel.setBackground(primaryColor);
        minimumAverageRatingPanel.setLayout(new BoxLayout(minimumAverageRatingPanel, BoxLayout.Y_AXIS));
        JLabel minAverageRatingLabel=new JLabel("Min average rating");
        minAverageRatingLabel.setForeground(textColor);
        minimumAverageRatingPanel.add(minAverageRatingLabel);
        minimumAverageRatingPanel.add(minimumAverageRatingComboBox);
        searchFiltersPanel.add(minimumAverageRatingPanel);

        JComboBox<String> suitableForMinorsComboBox = new JComboBox<>();
        suitableForMinorsComboBox.addItem("");
        suitableForMinorsComboBox.addItem("Yes");
        suitableForMinorsComboBox.addItem("No");

        JPanel suitableForMinorsComboBoxPanel = new JPanel();
        suitableForMinorsComboBoxPanel.setBackground(primaryColor);
        suitableForMinorsComboBoxPanel.setLayout(new BoxLayout(suitableForMinorsComboBoxPanel, BoxLayout.Y_AXIS));
        JLabel suitableForMinorsLabel=new JLabel("Suitable for minors");
        suitableForMinorsLabel.setForeground(textColor);
        suitableForMinorsComboBoxPanel.add(suitableForMinorsLabel);
        suitableForMinorsComboBoxPanel.add(suitableForMinorsComboBox);
        searchFiltersPanel.add(suitableForMinorsComboBoxPanel);

        /*The search bar that can be used to search for content
        that contains the string input of the user in either */
        JPanel searchBarPanel=new JPanel();
        searchBarPanel.setBackground(primaryColor);
        searchBarPanel.setLayout(new BoxLayout(searchBarPanel,BoxLayout.Y_AXIS));
        searchPanel.add(searchBarPanel);
        JLabel searchLabel=new JLabel("Search");
        searchLabel.setForeground(textColor);
        searchBarPanel.add(searchLabel);
        JTextField searchBarField=new JTextField("");
        searchBarField.setSize(300,100);
        searchBarPanel.add(searchBarField);

        JPanel searchButtonPanel=new JPanel();
        searchButtonPanel.setBackground(primaryColor);
        searchButtonPanel.setLayout(new BoxLayout(searchButtonPanel,BoxLayout.Y_AXIS));
        searchButtonPanel.add(new JLabel("\n"));
        JButton searchButton = new JButton("Search");
        searchButton.setForeground(primaryColor);
        searchButton.setBackground(textColor);
        searchButtonPanel.add(searchButton);
        searchPanel.add(searchButtonPanel);

        /*After the button is pressed, all values of the search fields
        are passed as parameters to the search method of DataBase*/
        searchButton.addActionListener(e -> {
            searchResults.clear();
            String[] userSearch=new String[]{"category", Objects.requireNonNull(categoryComboBox.getSelectedItem()).toString(),"genre",genreComboBox.getSelectedItem().toString(),
                    "suitable for minors", Objects.requireNonNull(suitableForMinorsComboBox.getSelectedItem()).toString(),
                    "min rating", Objects.requireNonNull(minimumAverageRatingComboBox.getSelectedItem()).toString(),searchBarField.getText()};

            for (Content content : dataBase.searchContent(userSearch))
            {
                if(!searchResults.contains(content))
                {
                    searchResults.add(content);
                }
            }

            if(addToRelatedContentFrame!=null)
            {
                addToRelatedContentFrame.updateRelatedCandidatesPanel();
            }
            else if(mainPageFrame!=null)
            {
                mainPageFrame.buildSearchResultsPanel();
            }
        });

         /*After the enter key is pressed, all values of the search fields
        are passed as parameters to the search method of DataBase*/
        searchBarField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    searchResults.clear();
                    String[] userSearch=new String[]{"category", Objects.requireNonNull(categoryComboBox.getSelectedItem()).toString(),"genre", Objects.requireNonNull(genreComboBox.getSelectedItem()).toString(),
                            "suitable for minors", Objects.requireNonNull(suitableForMinorsComboBox.getSelectedItem()).toString(),
                            "min rating", Objects.requireNonNull(minimumAverageRatingComboBox.getSelectedItem()).toString(),searchBarField.getText()};
                    for (Content content : dataBase.searchContent(userSearch))
                    {
                        if(!searchResults.contains(content))
                        {
                            searchResults.add(content);
                        }
                    }
                    if(addToRelatedContentFrame!=null)
                    {
                        addToRelatedContentFrame.updateRelatedCandidatesPanel();
                    }
                    else if(mainPageFrame!=null)
                    {
                        mainPageFrame.buildSearchResultsPanel();
                    }
                }
            }
        });
    }
}
