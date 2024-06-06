package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

//This class modifies the content that appears in the related section while an admin is editing
public class RelatedContentEditPanel extends JPanel
{
    private JPanel relatedContentEditOptionsPanel, removeFromRelatedOptionsPanel;
    private JButton removeRelatedOptionsButton, cancelRemovalButton;
    private HashSet<Content> editedRelatedContent;
    private final Content content;
    private final DataBase dataBase;
    private final Admin admin;
    private Color primaryColor,textColor;

    public RelatedContentEditPanel(Content content, DataBase dataBase, Admin admin)
    {
        this.content = content;
        this.dataBase = dataBase;
        this.admin = admin;
        editedRelatedContent=new HashSet<>();
        if(!content.getRelatedContent().isEmpty())
        {
            editedRelatedContent.addAll(content.getRelatedContent());
        }
        buildPanel();
    }

    public RelatedContentEditPanel(DataBase dataBase,Admin admin)
    {
        this.content=null;
        this.dataBase=dataBase;
        this.admin=admin;
        editedRelatedContent=new HashSet<>();
        buildPanel();
    }

    public void buildPanel()
    {
        primaryColor=new Color(85,0,0);
        textColor=Color.WHITE;
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBackground(textColor);
        relatedContentEditOptionsPanel = new JPanel();
        relatedContentEditOptionsPanel.setBackground(textColor);
        relatedContentEditOptionsPanel.setLayout(new FlowLayout());
        JButton addToRelatedButton = new JButton("Add related content");
        addToRelatedButton.setBackground(textColor);
        addToRelatedButton.setForeground(primaryColor);
        relatedContentEditOptionsPanel.add(addToRelatedButton);

        addToRelatedButton.addActionListener(e -> new AddToRelatedContentFrame());

        if (!editedRelatedContent.isEmpty())
        {
            this.add(new JLabel("Click on a title to view content details!"));
            buildRemoveFromRelatedContentOptionsPanel();
        }

        this.add(relatedContentEditOptionsPanel);
    }

    /*This frame handles the display of all content that can be added to the related section
     as well as the addition of all chosen content to the section*/
    public class AddToRelatedContentFrame extends JFrame
    {
        private ArrayList<Content> relatedContentCandidates;
        private HashSet<Content> relatedToBeAdded;
        private JPanel relatedCandidatesPanel;
        private JPanel addToRelatedOptionsPanel;

        public AddToRelatedContentFrame()
        {
            /*This ArrayList stores all content that fit the criteria the admin
            determines for the addition of suitable content to the related section*/
            relatedContentCandidates=new ArrayList<>();

            //This HashSet stores all the content the admin wants to be added to the related section
            relatedToBeAdded = new HashSet<>();
            relatedContentCandidates.addAll(dataBase.getContent());

            if(content!=null)
            {
                relatedContentCandidates.remove(content);
            }

            buildAddToRelatedFrame();
        }

        public void buildAddToRelatedFrame()
        {
            setTitle("Add related content");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setSize(getPreferredSize());

            if (!editedRelatedContent.isEmpty())
            {
                relatedContentCandidates.removeAll(editedRelatedContent);
            }

            JPanel addToRelatedContentPanel = new JPanel();
            addToRelatedContentPanel.setBackground(primaryColor);
            addToRelatedContentPanel.setLayout(new BoxLayout(addToRelatedContentPanel, BoxLayout.Y_AXIS));
            add(addToRelatedContentPanel);

            JButton cancelAdditionButton = new JButton("Cancel");
            cancelAdditionButton.setForeground(primaryColor);
            cancelAdditionButton.setBackground(textColor);
            cancelAdditionButton.addActionListener(e ->
            {
                relatedContentCandidates.clear();
                relatedToBeAdded.clear();
                dispose();
            });

            if (relatedContentCandidates.isEmpty())
            {
                addToRelatedContentPanel.add(new JLabel("No candidates in this category to be added to the related section!"));
                addToRelatedContentPanel.add(cancelAdditionButton);
                cancelAdditionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                pack();
                setVisible(true);
                return;
            }

            relatedCandidatesPanel=new JPanel();
            relatedCandidatesPanel.setLayout(new BoxLayout(relatedCandidatesPanel, BoxLayout.Y_AXIS));
            relatedCandidatesPanel.setBackground(primaryColor);

            /*Gives the admin the options of adding selected content
            to the related section, or cancelling the addition process*/
            addToRelatedOptionsPanel=new JPanel();
            addToRelatedOptionsPanel.setBackground(primaryColor);
            addToRelatedOptionsPanel.setLayout(new FlowLayout());
            JButton addSelectedToRelatedButton = new JButton("Add selected to related content");
            addSelectedToRelatedButton.setBackground(textColor);
            addSelectedToRelatedButton.setForeground(primaryColor);
            addSelectedToRelatedButton.addActionListener(e -> {
                if (!relatedToBeAdded.isEmpty())
                {
                    editedRelatedContent.addAll(relatedToBeAdded);
                    relatedToBeAdded.clear();
                    updateRelatedContentEditPanel();
                    dispose();
                }
            });
            addToRelatedOptionsPanel.add(addSelectedToRelatedButton);
            addToRelatedOptionsPanel.add(cancelAdditionButton);

            updateRelatedCandidatesPanel();

            addToRelatedContentPanel.add(new SearchPanel(dataBase,AddToRelatedContentFrame.this,relatedContentCandidates));

            addToRelatedContentPanel.add(new JScrollPane(relatedCandidatesPanel));
            addToRelatedContentPanel.add(addToRelatedOptionsPanel);
            pack();
            setVisible(true);
        }

        /*This method is called to update the content that's available
        for being added to the related section. The update comes
        after an admin has set the criteria they want the content to fit,
        has removed content from the related section or has added content to the section*/
        public void updateRelatedCandidatesPanel()
        {
            if(!editedRelatedContent.isEmpty())
            {
                relatedContentCandidates.removeAll(editedRelatedContent);
            }
            if(relatedCandidatesPanel.getComponentCount()>0) {
                relatedCandidatesPanel.removeAll();
            }

            if(content!=null) {
                relatedContentCandidates.remove(content);
            }

            if (!relatedContentCandidates.isEmpty())
            {
                for (Content content1 : relatedContentCandidates)
                {
                    relatedCandidatesPanel.add(contentDetailsPanel(content1, false, relatedToBeAdded));
                    if (!addToRelatedOptionsPanel.isVisible())
                    {
                        addToRelatedOptionsPanel.setVisible(true);
                    }
                    relatedCandidatesPanel.add(new JLabel("  "));
                }
            }
            else
            {
                if (addToRelatedOptionsPanel.isVisible())
                {
                    addToRelatedOptionsPanel.setVisible(false);
                }
                JPanel noResultsPanel=new JPanel();
                noResultsPanel.setLayout(new FlowLayout());
                relatedCandidatesPanel.add(noResultsPanel);
                noResultsPanel.add(new JLabel("No results found"));
                JButton cancelButton=new JButton("Cancel");
                cancelButton.addActionListener(e -> dispose());
                relatedCandidatesPanel.add(cancelButton);
            }

            relatedCandidatesPanel.revalidate();
            relatedCandidatesPanel.repaint();
        }

        /*This method ensures that the candidates for related content HashSet
        is cleared when the process finishes*/
        protected void processWindowEvent(WindowEvent e) {
            super.processWindowEvent(e);

            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                relatedContentCandidates.clear();
            }
        }
    }

    /*This method is used for providing the admin the ability
    to select the content that will be removed from the selected
    (remove selected, remove all or cancel removal)*/
    public void buildRemoveFromRelatedContentOptionsPanel()
    {
        //This HashSet stores all the content the admin wants to be removed from the related section
        HashSet<Content> relatedToBeRemoved = new HashSet<>();

        removeRelatedOptionsButton = new JButton("Remove related content");
        removeRelatedOptionsButton.setForeground(primaryColor);
        removeRelatedOptionsButton.setBackground(textColor);

        removeFromRelatedOptionsPanel = new JPanel();
        removeFromRelatedOptionsPanel.setBackground(textColor);
        removeFromRelatedOptionsPanel.setLayout(new FlowLayout());

        JButton removeAllRelatedButton = new JButton("Remove all current related content");
        removeAllRelatedButton.setBackground(textColor);
        removeAllRelatedButton.setForeground(primaryColor);
        JButton removeSelectedFromRelatedButton = new JButton("Remove selected from related content");
        removeSelectedFromRelatedButton.setForeground(primaryColor);
        removeSelectedFromRelatedButton.setBackground(textColor);
        cancelRemovalButton = new JButton("Cancel");
        cancelRemovalButton.setBackground(textColor);
        cancelRemovalButton.setForeground(primaryColor);

        removeFromRelatedOptionsPanel.add(removeSelectedFromRelatedButton);
        removeFromRelatedOptionsPanel.add(removeAllRelatedButton);
        removeFromRelatedOptionsPanel.add(cancelRemovalButton);

        cancelRemovalButton.addActionListener(e -> {
            removeFromRelatedOptionsPanel.setVisible(false);
            relatedToBeRemoved.clear();
        });

        removeAllRelatedButton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(RelatedContentEditPanel.this, "Are you sure you want to remove all current related content from the related content section?",
                    "Confirm removal", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION)
            {
                //relatedContentCandidates.addAll(editedRelatedContent);
                editedRelatedContent.clear();
                relatedToBeRemoved.clear();
                updateRelatedContentEditPanel();
            }
        });

        removeSelectedFromRelatedButton.addActionListener(e -> {
            if (!relatedToBeRemoved.isEmpty())
            {
                int confirmation = JOptionPane.showConfirmDialog(RelatedContentEditPanel.this, "Are you sure you want to remove all selected from the related content section?",
                        "Confirm removal", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION)
                {
                    editedRelatedContent.removeAll(relatedToBeRemoved);
                    //relatedContentCandidates.addAll(relatedToBeRemoved);
                    relatedToBeRemoved.clear();
                    updateRelatedContentEditPanel();
                }
            }
        });

        Iterator<Content> it=editedRelatedContent.iterator();
        while(it.hasNext())
        {
            Content content1=it.next();
            this.add(contentDetailsPanel(content1, true, relatedToBeRemoved));
        }

        relatedContentEditOptionsPanel.add(removeRelatedOptionsButton);

        this.add(removeFromRelatedOptionsPanel);
        removeFromRelatedOptionsPanel.setVisible(false);
    }

    public JPanel contentDetailsPanel(Content content, boolean remove, HashSet<Content> relatedHandler)
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(textColor);
        contentPanel.setLayout(new FlowLayout());
        JCheckBox relatedCheckBox = new JCheckBox();
        JLabel relatedContentTitleLabel = new JLabel(content.getTitle());
        contentPanel.add(relatedCheckBox);
        contentPanel.add(relatedContentTitleLabel);

        relatedContentTitleLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (!(content instanceof ViewableContent))
                {
                    new ContentFrame(dataBase,content, admin);
                }
                else if (content instanceof Movie movie)
                {
                    new MovieFrame(dataBase,movie,admin);
                }
                else if (content instanceof Series series)
                {
                    new SeriesFrame(dataBase,series,admin);
                } else if (content instanceof ViewableContent viewableContent)
                {
                    new ViewableContentFrame(dataBase,viewableContent, admin);
                }
            }
        });

        relatedCheckBox.addItemListener(e -> {
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED)
            {
                relatedHandler.add(content);
            } else if (state == ItemEvent.DESELECTED)
            {
                relatedHandler.remove(content);
            }
        });

        if (remove)
        {
            relatedCheckBox.setVisible(false);
            removeRelatedOptionsButton.addActionListener(e -> {
                relatedCheckBox.setVisible(true);
                removeFromRelatedOptionsPanel.setVisible(true);
            });

            cancelRemovalButton.addActionListener(e -> relatedCheckBox.setVisible(false));
        }
        return contentPanel;
    }

    public void updateRelatedContentEditPanel()
    {
        this.removeAll();
        this.revalidate();
        this.repaint();
        buildPanel();
    }

    public HashSet<Content> getEditedRelatedContent()
    {
        return  editedRelatedContent;
    }
}