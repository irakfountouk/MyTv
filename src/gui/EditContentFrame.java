package gui;


import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static api.Movie.durationIsValid;
import static api.Movie.yearOfReleaseIsValid;
import static api.ViewableContent.genreIsValid;
import static api.ViewableContent.suitableForMinorsIsValid;

//Class for both registering and editing all types of content
public class EditContentFrame extends JFrame
{
    private JPanel contentEditPanel,editSeasonsPanel;
    private RelatedContentEditPanel relatedContentEditPanel;
    private JTextField titleEditField, genreEditField, suitabilityForMinorsEditField, yearOfReleaseEditField, durationEditField;
    private JTextArea peopleInvolvedEditArea, synopsisEditArea;
    private JLabel peopleInvolvedEditLabel;
    private JLabel seasonsEditLabel;
    private ContentFrame contentFrame; /*The ContentFrame of the content being edited. It's passed as a parameter
    to the constructor in order to call the method that updates the displayed content info*/
    private ArrayList<Season> editedSeasons;
    private DataBase dataBase;
    private Content content; /*If a piece of content is being edited,
    this variable stores it. In a different case, the variable is
    initialized as null, and, if new content is registered, it's stored in it*/
    private final Admin admin;
    private Color primaryColor, textColor;
    private boolean addContent=false;
    private String category; /*This variable determines the type of content that
    will be registered/added, and based on that, the appropriate frame is built*/

    //This constructor is called when editing existing content
    public EditContentFrame(DataBase dataBase, Content content, Admin admin, ContentFrame contentFrame)
    {
        this.dataBase = dataBase;
        this.content = content;
        this.admin = admin;
        this.contentFrame = contentFrame;

        switch (content)
        {
            case Movie movie -> category = "movie";
            case Series series ->
            {
                category = "series";

            /*If a series is being edited,
            this ArrayList will store its current seasons and handle the
            addition of new ones and the removal of existing ones. If the changes made are saved,
            it will be passed to the series object, in order to update the seasons field*/
                editedSeasons = new ArrayList<>();
                editedSeasons.addAll(series.getSeasons());
            }
            case ViewableContent viewableContent -> category = "viewablecontent";
            default -> category = "content";
        }

        buildEditFrame();
    }
    //This constructor is called when registering new content
    public EditContentFrame(DataBase dataBase, Admin admin, String category)
    {
        this.dataBase=dataBase;
        this.admin = admin; //current admin
        this.content=null;
        addContent=true; //This field determines if content will be registered (added) or edited

        if(category.toLowerCase().contains("movie"))
        {
            this.category="movie";
        }
        else if(category.toLowerCase().contains("series"))
        {
            this.category="series";

            /*If a series is being registered, this ArrayList
            will store all the seasons that are registered/removed in the process.
            If the registration is concluded, it will be passed as a parameter to the
            new object's constructor, in order to set the seasons field*/
            editedSeasons=new ArrayList<>();
        }
        else if(category.toLowerCase().replaceAll("\\s", "").contains("viewablecontent"))
        {
            this.category="viewablecontent";
        }
        else
        {
            this.category="content";
        }

        buildEditFrame();
    }

    //This method adds the components that are needed for all types of content
    public void buildEditFrame()
    {
        if(content!=null) {
            setTitle("Edit Content Details");
        }
        else
        {
            setTitle("Register content");
        }

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        primaryColor = new Color(85, 0, 0);
        textColor = Color.WHITE;

        titleEditField=new JTextField("   ");
        peopleInvolvedEditArea=new JTextArea("   ");

        //If content is being edited, the fields display its current info
        if(!addContent)
        {
            titleEditField.setText(content.getTitle());
            peopleInvolvedEditArea.setText(content.getPeopleInvolved());
            relatedContentEditPanel = new RelatedContentEditPanel(content, dataBase, admin);
        }
        else //The panel for adding/removing related content is always built
        {
            relatedContentEditPanel=new RelatedContentEditPanel(dataBase,admin);
        }

        contentEditPanel = new JPanel(); //Main panel
        contentEditPanel.setLayout(new GridLayout(5, 2));
        contentEditPanel.setBackground(primaryColor);

        JLabel titleEditLabel = new JLabel("Title:");
        titleEditLabel.setForeground(textColor);

        peopleInvolvedEditLabel = new JLabel("People involved in the making:");
        peopleInvolvedEditLabel.setForeground(textColor);

        JLabel relatedContentEditLabel = new JLabel("Related Content:");
        relatedContentEditLabel.setForeground(textColor);

        contentEditPanel.add(titleEditLabel);
        contentEditPanel.add(titleEditField);
        contentEditPanel.add(peopleInvolvedEditLabel);
        contentEditPanel.add(new JScrollPane(peopleInvolvedEditArea));
        contentEditPanel.add(relatedContentEditLabel);
        contentEditPanel.add(new JScrollPane(relatedContentEditPanel));

        switch (category)
        {
            case "movie" -> addMovieSpecificElements();
            case "series" -> addSeriesSpecificElements();
            case "viewablecontent" -> addViewableContentSpecificElements();
        }


        JPanel fillerPanel = new JPanel(); //Panel for design-exclusive purposes
        fillerPanel.setBackground(primaryColor);

        contentEditPanel.add(fillerPanel);

        savePanel();

        add(contentEditPanel);

        pack();
        setVisible(true);
    }

    /*This method and the savePanel provide the admin with the
    options of concluding the registration/editing process or cancelling*/
    public void savePanel()
    {
        JPanel saveOptionsPanel = new JPanel();
        saveOptionsPanel.setLayout(new FlowLayout());
        contentEditPanel.add(saveOptionsPanel);

        JButton saveButton = new JButton(" ");
        saveButton.setForeground(primaryColor);
        saveButton.setBackground(textColor);

        if(addContent)
        {
            saveButton.setText("Register content");
        }
        else
        {
            saveButton.setText("Save changes");
        }

        JButton cancelButton = new JButton("Cancel"); //Button for cancelling edit/registration process
        cancelButton.setBackground(textColor);
        cancelButton.setForeground(primaryColor);
        saveOptionsPanel.add(saveButton);
        saveOptionsPanel.add(cancelButton);

        //Button for saving all changes/registering new content
        saveButton.addActionListener(e -> {
            if (editFieldsAreValid())
            {
                int confirmation;
                if(addContent)
                {
                    confirmation = JOptionPane.showConfirmDialog(EditContentFrame.this, "Register content?", "Confirm registration", JOptionPane.YES_NO_OPTION);
                }
                else {
                    confirmation = JOptionPane.showConfirmDialog(EditContentFrame.this, "Save changes?", "Confirm changes", JOptionPane.YES_NO_OPTION);
                }
                if (confirmation == JOptionPane.YES_OPTION)
                {
                    if(addContent)
                    {
                        registerContent();
                        dataBase.addToContent(content);
                        if(!dataBase.getContent().contains(content))
                        {
                            JOptionPane.showMessageDialog(contentEditPanel,"Content registration failed! Please try again and check for errors (e.g content already exists)");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(contentEditPanel,"Content registered successfully!");
                            switch (category) {
                                case "movie" -> new MovieFrame(dataBase,(Movie) content, admin);
                                case "series" -> new SeriesFrame(dataBase,(Series) content, admin);
                                case "viewablecontent" -> new ViewableContentFrame(dataBase,(ViewableContent) content, admin);
                                default -> new ContentFrame(dataBase,content, admin);
                            }
                        }
                    }
                    else //Content is edited
                    {
                        updateContentInfo();
                    }
                    dispose();
                }
            }
        });

        //Button for discarding all changes made/process of registration
        cancelButton.addActionListener(e -> {
            int confirmation;
            if(addContent)
            {
                confirmation = JOptionPane.showConfirmDialog(EditContentFrame.this, "Cancel registration?", "Confirm cancellation", JOptionPane.YES_NO_OPTION);
            }
            else {
                confirmation = JOptionPane.showConfirmDialog(EditContentFrame.this, "Cancel changes?", "Confirm cancellation", JOptionPane.YES_NO_OPTION);
            }
            if (confirmation == JOptionPane.YES_OPTION)
            {
                dispose();
            }
        });
    }

    //If new content is registered, the content variable stores it
    public void registerContent()
    {
        HashSet<Content> editedRelatedContent = new HashSet<>();
        editedRelatedContent = relatedContentEditPanel.getEditedRelatedContent();
        switch (category) {
            case "movie" ->
                    content = new Movie(titleEditField.getText(), synopsisEditArea.getText(), suitabilityForMinorsEditField.getText(), genreEditField.getText(), peopleInvolvedEditArea.getText(), Integer.parseInt(yearOfReleaseEditField.getText().replaceAll("\\s", "")), Integer.parseInt(durationEditField.getText().replaceAll("\\s", "")), editedRelatedContent);
            case "series" ->
                    content = new Series(titleEditField.getText(), synopsisEditArea.getText(), suitabilityForMinorsEditField.getText(), genreEditField.getText(), peopleInvolvedEditArea.getText(), editedSeasons, editedRelatedContent);
            case "viewablecontent" ->
                    content = new ViewableContent(titleEditField.getText(), synopsisEditArea.getText(), suitabilityForMinorsEditField.getText(), genreEditField.getText(), peopleInvolvedEditArea.getText(), editedRelatedContent);
            default ->
                    content = new Content(titleEditField.getText(), synopsisEditArea.getText(), editedRelatedContent);
        }
    }

    //If content details were edited, the fields of the content object are updated
    public void updateContentInfo()
    {
        HashSet<Content> editedRelatedContent = new HashSet<>();
        editedRelatedContent = relatedContentEditPanel.getEditedRelatedContent();

        if (!(content instanceof ViewableContent))
        {
            content.edit(titleEditField.getText(), peopleInvolvedEditArea.getText(), editedRelatedContent);
            contentFrame.updateDisplayedContentInfo();
        }
        else if (!(content instanceof Movie || content instanceof Series))
        {
            ViewableContentFrame viewableContentFrame = (ViewableContentFrame) contentFrame;
            ViewableContent viewableContent = (ViewableContent) content;
            viewableContent.edit(titleEditField.getText(), synopsisEditArea.getText(), suitabilityForMinorsEditField.getText(), genreEditField.getText(), peopleInvolvedEditArea.getText(), editedRelatedContent);
            viewableContentFrame.updateDisplayedContentInfoForViewable();
        }
        else if (content instanceof Movie movie)
        {
            MovieFrame movieFrame = (MovieFrame) contentFrame;
            movie.edit(titleEditField.getText(), synopsisEditArea.getText(), suitabilityForMinorsEditField.getText(), genreEditField.getText(), peopleInvolvedEditArea.getText(), Integer.parseInt(yearOfReleaseEditField.getText().replaceAll("\\s", "")), Integer.parseInt(durationEditField.getText().replaceAll("\\s", "")), editedRelatedContent);
            movieFrame.updateDisplayedContentInfoForMovie();
        }
        else if (content instanceof Series series)
        {
            SeriesFrame seriesFrame = (SeriesFrame) contentFrame;
            series.edit(titleEditField.getText(),synopsisEditArea.getText(),suitabilityForMinorsEditField.getText(),genreEditField.getText(),peopleInvolvedEditArea.getText(),editedSeasons,editedRelatedContent);
            seriesFrame.updateDisplayedInfoForSeries();
        }
        dataBase.editFiles();
    }

    //Checking if the fields contain values that are valid for the specific field
    public boolean editFieldsAreValid()
    {
        if (titleEditField.getText().trim().isEmpty() || peopleInvolvedEditArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(contentEditPanel, "All fields are mandatory (related content excluded)");
            return false;
        }
        else if (!category.equals("content"))
        {
            if (synopsisEditArea.getText().replaceAll("\\s", "").isEmpty() || genreEditField.getText().replaceAll("\\s", "").isEmpty() || suitabilityForMinorsEditField.getText().replaceAll("\\s", "").trim()
                    .isEmpty())
            {
                JOptionPane.showMessageDialog(contentEditPanel, "All fields are mandatory (related content excluded)");
                return false;
            }
            else if (!genreIsValid(genreEditField.getText().trim()))
            {
                JOptionPane.showMessageDialog(contentEditPanel, "Invalid genre typed! Acceptable genres are: Action,Drama,Horror,Science fiction,Comedy.Check for spelling errors and try again");
                return false;
            }
            else if (!suitableForMinorsIsValid(suitabilityForMinorsEditField.getText()))
            {
                JOptionPane.showMessageDialog(contentEditPanel, "Invalid text in the 'Suitable for minors' area! Acceptable values are: Yes,No.Try again and check for spelling errors");
                return false;
            }
            else
            {
                if (category.equals("movie"))
                {
                    if (yearOfReleaseEditField.getText().replaceAll("\\s", "").isEmpty() || durationEditField.getText().replaceAll("\\s", "").isEmpty()) {
                        JOptionPane.showMessageDialog(contentEditPanel, "All fields are mandatory (related content excluded)");
                        return false;
                    }
                    if (!yearOfReleaseIsValid(yearOfReleaseEditField.getText())) {
                        JOptionPane.showMessageDialog(contentEditPanel, "Invalid year of release typed!");
                        return false;
                    } else if (!durationIsValid(durationEditField.getText()))
                    {
                        JOptionPane.showMessageDialog(contentEditPanel, "Invalid duration typed!");
                        return false;
                    }
                }
                else if (category.equals("series"))
                {
                    if(editedSeasons.isEmpty())
                    {
                        JOptionPane.showMessageDialog(contentEditPanel,"Adding seasons is mandatory");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //Adding all extra components for registering/editing instances of ViewableContent
    public void addViewableContentSpecificElements()
    {
        peopleInvolvedEditLabel.setText("Actors:");
        peopleInvolvedEditLabel.setForeground(textColor);
        JLabel synopsisEditLabel = new JLabel("Synopsis:");
        synopsisEditLabel.setForeground(textColor);
        JLabel genreEditLabel = new JLabel("Genre:");
        genreEditLabel.setForeground(textColor);
        JLabel suitabilityForMinorsEditLabel = new JLabel("Suitable for minors:");
        suitabilityForMinorsEditLabel.setForeground(textColor);

        int pos = contentEditPanel.getComponentZOrder(titleEditField);

        synopsisEditArea = new JTextArea("  ");
        genreEditField = new JTextField("  ");
        suitabilityForMinorsEditField = new JTextField("  ");

        //If content is being edited, the fields display its current info
        if(!addContent)
        {
            ViewableContent viewableContent = (ViewableContent) content;
            synopsisEditArea.setText(viewableContent.getSynopsis());
            genreEditField.setText(viewableContent.getGenre());
            suitabilityForMinorsEditField.setText(viewableContent.getIsSuitableForMinors());
        }

        contentEditPanel.add(synopsisEditLabel, pos + 1);
        contentEditPanel.add(new JScrollPane(synopsisEditArea), pos + 2);
        contentEditPanel.add(genreEditLabel, pos + 3);
        contentEditPanel.add(genreEditField, pos + 4);
        contentEditPanel.add(suitabilityForMinorsEditLabel, pos + 5);
        contentEditPanel.add(suitabilityForMinorsEditField, pos + 6);

        contentEditPanel.setLayout(new GridLayout(7, 2));
    }

    //Adding all extra components for registering/editing a movie
    public void addMovieSpecificElements()
    {
        addViewableContentSpecificElements();

        contentEditPanel.setLayout(new GridLayout(9, 2));

        int pos = contentEditPanel.getComponentZOrder(suitabilityForMinorsEditField);

        JLabel yearOfReleaseEditLabel = new JLabel("Year of release: ");
        yearOfReleaseEditLabel.setForeground(textColor);
        yearOfReleaseEditField = new JTextField("");
        contentEditPanel.add(yearOfReleaseEditLabel, pos + 1);
        contentEditPanel.add(yearOfReleaseEditField, pos + 2);

        JLabel durationEditLabel = new JLabel("Duration: ");
        durationEditLabel.setForeground(textColor);
        durationEditField = new JTextField("");
        contentEditPanel.add(durationEditLabel, pos + 3);
        contentEditPanel.add(durationEditField, pos + 4);

        //If content is being edited, the fields display its current info
        if(!addContent)
        {
            Movie movie = (Movie) content;
            yearOfReleaseEditField.setText(String.valueOf(movie.getYearOfRelease()));
            durationEditField.setText(String.valueOf(movie.getDuration()));
        }

    }

    //Adding all extra components for registering/editing a series
    public void addSeriesSpecificElements()
    {
        addViewableContentSpecificElements();

        contentEditPanel.setLayout(new GridLayout(8, 2));
        seasonsEditLabel = new JLabel("Seasons: " + editedSeasons.size());

        seasonsEditLabel.setForeground(textColor);
        contentEditPanel.add(seasonsEditLabel);
        editSeasonsPanel = new JPanel();
        editSeasonsPanel.setLayout(new BoxLayout(editSeasonsPanel, BoxLayout.Y_AXIS));
        JScrollPane editSeasonsScrollPane=new JScrollPane(editSeasonsPanel);
        contentEditPanel.add(editSeasonsScrollPane);
        buildEditSeasonsPanel();
    }

    /*This method and the editSeasonsPanel are responsible for handling the display,
      addition and removal of a series' seasons*/
    public void buildEditSeasonsPanel()
    {
        seasonsEditLabel.setText("Seasons: " + editedSeasons.size());
        if(!editedSeasons.isEmpty())
        {
            editSeasonsPanel.add(new JLabel("Click on a season to view more details!"));
        }

        JButton addToSeasonsButton = new JButton("Add season");
        addToSeasonsButton.setBackground(textColor);
        addToSeasonsButton.setForeground(primaryColor);
        addToSeasonsButton.addActionListener(e ->
        {
            //Frame to register season
            new EditSeasonFrame(editedSeasons,admin, EditContentFrame.this);
        });

        JPanel handleSeasonsPanel=new JPanel(); //Panel for providing the options of adding/removing seasons
        handleSeasonsPanel.setLayout(new FlowLayout());
        handleSeasonsPanel.add(addToSeasonsButton);

        if(!editedSeasons.isEmpty())
        {
            buildRemoveSeasonsPanel(handleSeasonsPanel);
        }
        editSeasonsPanel.add(handleSeasonsPanel);
    }

    /*This method displays all seasons stored in editedSeasons and places
     a checkbox next to each one, so that admins can select the ones they want removed*/
    public void buildRemoveSeasonsPanel(JPanel handleSeasonsPanel)
    {
        JButton removeFromSeasonsButton = new JButton("Remove seasons");
        removeFromSeasonsButton.setForeground(primaryColor);
        removeFromSeasonsButton.setBackground(textColor);
        handleSeasonsPanel.add(removeFromSeasonsButton);

        JPanel removeSeasonsOptionsPanel = new JPanel(); //Panel that provides the options of removing all seasons,
        // removing the ones selected, or cancelling the removal process altogether*/

        removeSeasonsOptionsPanel.setLayout(new FlowLayout());
        removeSeasonsOptionsPanel.setVisible(false);

        ArrayList<Season> seasonsToBeRemoved = new ArrayList<>(); //This HashSet stores the seasons that are selected for removal

        JButton removeSelectedFromSeasonsButton = new JButton("Remove selected from seasons");
        removeSelectedFromSeasonsButton.setBackground(textColor);
        removeSelectedFromSeasonsButton.setForeground(primaryColor);
        JButton removeAllSeasonsButton = new JButton("Remove all seasons");
        removeAllSeasonsButton.setForeground(primaryColor);
        removeAllSeasonsButton.setBackground(textColor);
        JButton cancelRemovalButton = new JButton("Cancel");
        cancelRemovalButton.setBackground(textColor);
        cancelRemovalButton.setForeground(primaryColor);
        removeSeasonsOptionsPanel.add(removeSelectedFromSeasonsButton);
        removeSeasonsOptionsPanel.add(removeAllSeasonsButton);
        removeSeasonsOptionsPanel.add(cancelRemovalButton);

        removeFromSeasonsButton.addActionListener(e -> removeSeasonsOptionsPanel.setVisible(true));


        removeSelectedFromSeasonsButton.addActionListener(e -> {
            if (!seasonsToBeRemoved.isEmpty()) {
                int confirmation = JOptionPane.showConfirmDialog(editSeasonsPanel, "Are you sure you want to remove all selected from seasons?", "Confirm removal", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION)
                {
                    Iterator<Season> it=editedSeasons.iterator();
                    while(it.hasNext())
                    {
                        Season season=it.next();
                        if(seasonsToBeRemoved.contains(season))
                        {
                            it.remove();
                        }
                    }
                    updateEditSeasonsPanel();
                }
            }
        });

        removeAllSeasonsButton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(editSeasonsPanel, "Are you sure you want to remove all seasons?", "Confirm removal", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                int doubleConfirmation = JOptionPane.showConfirmDialog(editSeasonsPanel, "The season section can't be empty, so you will have to add at least one season before saving the changes you made", "Confirm removal", JOptionPane.OK_CANCEL_OPTION);
                if (doubleConfirmation == JOptionPane.OK_OPTION) {
                    editedSeasons.clear();
                    updateEditSeasonsPanel();
                }
            }
        });

        cancelRemovalButton.addActionListener(e -> {
            seasonsToBeRemoved.clear();
            removeSeasonsOptionsPanel.setVisible(false);
        });

        /*Iterating through all seasons that are currently in the seasons section
        of the edit panel, so that each seasons' details can be viewed, and
        check boxes for choosing the ones to be removed are added next to every one*/

        Iterator<Season> it=editedSeasons.iterator();
        while(it.hasNext())
        {
            Season season=it.next();
            JPanel seasonPanel = new JPanel();
            editSeasonsPanel.add(seasonPanel);
            seasonPanel.setLayout(new FlowLayout());
            JCheckBox removeSeasonCheckBox = new JCheckBox();
            removeSeasonCheckBox.setVisible(false);
            seasonPanel.add(removeSeasonCheckBox);
            JLabel seasonLabel = new JLabel("Season " + season.getSeasonNumber());
            seasonPanel.add(seasonLabel);

            seasonLabel.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new SeasonFrame(season, admin); //View season's details
                }
            });
            removeSeasonCheckBox.addItemListener(e -> {
                int state = e.getStateChange();

                if (state == ItemEvent.SELECTED) {
                    seasonsToBeRemoved.add(season);
                } else if (state == ItemEvent.DESELECTED) {
                    seasonsToBeRemoved.remove(season);
                }
            });

            removeFromSeasonsButton.addActionListener(e -> removeSeasonCheckBox.setVisible(true));

            cancelRemovalButton.addActionListener(e -> removeSeasonCheckBox.setVisible(false));
        }

        editSeasonsPanel.add(removeSeasonsOptionsPanel);
    }

    //If a season is added, removed or edited, the editSeasonsPanel is updated
    public void updateEditSeasonsPanel()
    {
        editSeasonsPanel.removeAll();
        buildEditSeasonsPanel();
        editSeasonsPanel.revalidate();
        editSeasonsPanel.repaint();
    }

}

