package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

//The class that loads the main page of the app
public class MainPageFrame extends JFrame
{
    private DataBase dataBase;
    private JPanel panel,appearingContentPanel;
    private User user;
    private Color primaryColor, textColor;
    private ArrayList<Content> searchResults;

    MainPageFrame(DataBase dataBase, User user)
    {
        //This HashSet is responsible for storing and displaying search results
        searchResults = new ArrayList<>();
        this.dataBase = dataBase;
        this.user = user;
        buildFrame();
    }

    public void buildFrame() {
        this.setTitle(" ");
        this.setSize(400,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        primaryColor = new Color(85, 0, 0);
        textColor = Color.WHITE;

        panel = new JPanel();
        panel.setBackground(primaryColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel handleFramePanel = new JPanel();
        handleFramePanel.setLayout(new FlowLayout());
        handleFramePanel.setBackground(primaryColor);
        SearchPanel searchPanel = new SearchPanel(dataBase, MainPageFrame.this, searchResults);
        searchPanel.setBackground(primaryColor);
        handleFramePanel.add(searchPanel);
        handleFramePanel.add(buildOptionsMenu());
        panel.add(handleFramePanel);

        appearingContentPanel=new JPanel();
        panel.add(new JScrollPane(appearingContentPanel));
        //panel.add(appearingContentPanel);
        appearingContentPanel.setBackground(primaryColor);
        appearingContentPanel.setLayout(new BoxLayout(appearingContentPanel,BoxLayout.Y_AXIS));
        buildAppearingContentPanel();

        if(user instanceof Admin)
        {
            buildAdminPanel();
        }

        JPanel decorativePanel=new JPanel();
        decorativePanel.setBackground(primaryColor);
        panel.add(decorativePanel);

        add(panel);
        pack();
        this.setVisible(true);
    }

    public JMenuBar buildOptionsMenu() {
        Color menuForegroundColor = new Color(130, 0, 0);
        JMenuBar optionsBar = new JMenuBar();
        optionsBar.setLayout((new BoxLayout(optionsBar, BoxLayout.Y_AXIS)));
        optionsBar.setBackground(Color.BLACK);
        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setForeground(menuForegroundColor);
        JMenuItem viewProfileMenuItem = new JMenuItem("View your profile");
        viewProfileMenuItem.setBackground(Color.BLACK);
        viewProfileMenuItem.setForeground(menuForegroundColor);
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.setBackground(Color.BLACK);
        logoutMenuItem.setForeground(menuForegroundColor);

        optionsMenu.add(viewProfileMenuItem);
        optionsMenu.addSeparator();
        optionsMenu.add(logoutMenuItem);
        optionsBar.add(optionsMenu);

        viewProfileMenuItem.addActionListener(e -> {
            new UserProfileFrame(dataBase, user);
            dispose();
        });


        logoutMenuItem.addActionListener(e -> {
            new LoginFrame(dataBase);
            dispose();
        });

        return optionsBar;
    }


    public void buildAppearingContentPanel()
    {
        if(appearingContentPanel.getComponentCount()>0)
        {
            appearingContentPanel.removeAll();
            appearingContentPanel.revalidate();
            appearingContentPanel.repaint();
        }

        buildSpecificGenrePanel("action","Want to pump up some adrenaline? These action films and series might do the trick");
        buildSpecificGenrePanel("drama","Drama pieces that might just make you run out of tissues");
        buildSpecificGenrePanel("horror","Planning a horror night with your friends? These will be great help");
        buildSpecificGenrePanel("science fiction","Sci-fi pieces guaranteed to help you escape dullness");
        buildSpecificGenrePanel("comedy","If you're looking for something to cheer you up, we have these suggestions to make");

        if(!dataBase.getMovies().isEmpty())
        {
            JPanel moviesOuterPanel=new JPanel();
            moviesOuterPanel.setLayout(new BoxLayout(moviesOuterPanel,BoxLayout.Y_AXIS));
            JPanel moviesIntroPanel=new JPanel();
            moviesIntroPanel.setLayout(new FlowLayout());
            moviesIntroPanel.setBackground(primaryColor);
            JLabel moviesIntroLabel=new JLabel("Some movies you might like");
            moviesIntroLabel.setForeground(textColor);
            moviesIntroPanel.add(moviesIntroLabel);
            moviesOuterPanel.add(moviesIntroPanel);

            int contentAdded=0;
            Iterator<Movie> it=dataBase.getMovies().iterator();
            JPanel moviesInnerPanel=new JPanel();
            moviesInnerPanel.setBackground(primaryColor);
            moviesInnerPanel.setLayout(new FlowLayout());
            moviesOuterPanel.add(moviesInnerPanel);
            while (it.hasNext() && contentAdded<10)
            {
                Movie movie=it.next();
                moviesInnerPanel.add(viewContentDetails(movie,false));

                contentAdded++;
            }

            appearingContentPanel.add(new JScrollPane(moviesOuterPanel));
        }

        if(!dataBase.getSeries().isEmpty())
        {
            JPanel seriesOuterPanel=new JPanel();
            seriesOuterPanel.setLayout(new BoxLayout(seriesOuterPanel,BoxLayout.Y_AXIS));
            JPanel seriesIntroPanel=new JPanel();
            seriesIntroPanel.setLayout(new FlowLayout());
            seriesIntroPanel.setBackground(primaryColor);
            JLabel seriesIntroLabel=new JLabel("Some series you might like");
            seriesIntroLabel.setForeground(textColor);
            seriesIntroPanel.add(seriesIntroLabel);
            seriesOuterPanel.add(seriesIntroPanel);

            int contentAdded=0;
            Iterator<Series> it=dataBase.getSeries().iterator();
            JPanel seriesInnerPanel=new JPanel();
            seriesInnerPanel.setLayout(new FlowLayout());
            seriesInnerPanel.setBackground(primaryColor);
            seriesOuterPanel.add(seriesInnerPanel);
            while(it.hasNext() && contentAdded<10)
            {
                Series series=it.next();
                seriesInnerPanel.add(viewContentDetails(series,false));
                contentAdded++;
            }
            appearingContentPanel.add(new JScrollPane(seriesOuterPanel));
        }
        appearingContentPanel.revalidate();
        appearingContentPanel.repaint();
    }

    public void buildSpecificGenrePanel(String genre,String messageLabel)
    {
        JPanel genreOuterPanel=new JPanel();
        genreOuterPanel.setLayout(new BoxLayout(genreOuterPanel,BoxLayout.Y_AXIS));

        JPanel introPanel = new JPanel();
        introPanel.setLayout(new FlowLayout());
        introPanel.setBackground(primaryColor);
        JLabel introLabel = new JLabel(messageLabel);
        introLabel.setForeground(textColor);
        introPanel.add(introLabel);
        genreOuterPanel.add(introPanel);

        Iterator<ViewableContent> it=dataBase.getViewableContent().iterator();
        int contentAdded=0;
        JPanel genreInnerPanel=new JPanel();
        genreInnerPanel.setBackground(primaryColor);
        genreInnerPanel.setLayout(new FlowLayout());
        genreOuterPanel.add(genreInnerPanel);
        while(it.hasNext() && contentAdded<10)
        {
            ViewableContent viewableContent=it.next();
            if(viewableContent.getGenre().toLowerCase().equals(genre))
            {
                contentAdded++;
                genreInnerPanel.add(viewContentDetails(viewableContent,false));
            }
        }

        if(contentAdded>0)
        {
            appearingContentPanel.add(new JScrollPane(genreOuterPanel));
        }
    }

    public void buildSearchResultsPanel()
    {
        if(appearingContentPanel.getComponentCount()>0)
        {
            appearingContentPanel.removeAll();
            appearingContentPanel.revalidate();
            appearingContentPanel.repaint();
        }
        if(searchResults.isEmpty())
        {
            JPanel noResultsPanel=new JPanel();
            noResultsPanel.setLayout(new FlowLayout());
            noResultsPanel.setBackground(primaryColor);
            JLabel noResultsLabel=new JLabel("No results found!");
            noResultsLabel.setForeground(Color.BLACK);
            noResultsPanel.add(noResultsLabel);
            appearingContentPanel.add(noResultsPanel);
        }
        for (Content searchResult : searchResults)
        {
            appearingContentPanel.add(viewContentDetails(searchResult,true));
        }

        JPanel returnPanel=new JPanel();
        returnPanel.setBackground(primaryColor);
        returnPanel.setLayout(new FlowLayout());
        JButton returnButton=new JButton("Return to main page");
        returnButton.setBackground(textColor);
        returnButton.setForeground(primaryColor);
        returnPanel.add(returnButton);

        appearingContentPanel.revalidate();
        appearingContentPanel.repaint();
        appearingContentPanel.add(returnPanel);
        returnButton.addActionListener(e -> buildAppearingContentPanel());
    }

    public JPanel viewContentDetails(Content content,boolean isFoundFromSearchResults)
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(primaryColor);
        contentPanel.setLayout(new FlowLayout());
        JLabel contentTitleLabel = new JLabel(content.getTitle());
        contentTitleLabel.setForeground(Color.BLACK);
        contentPanel.add(contentTitleLabel);

        contentTitleLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (content instanceof Movie movie)
                {
                    new MovieFrame(dataBase,movie,user,MainPageFrame.this,isFoundFromSearchResults);
                }
                else if (content instanceof Series series)
                {
                    new SeriesFrame(dataBase,series,user,MainPageFrame.this,isFoundFromSearchResults);
                }
            }
        });

        return contentPanel;
    }

    public void buildAdminPanel()
    {
        JPanel adminPanel=new JPanel();
        adminPanel.setLayout(new FlowLayout());
        adminPanel.setBackground(primaryColor);

        JButton registerMovieButton=new JButton("Register a new movie");
        registerMovieButton.setForeground(primaryColor);
        registerMovieButton.setBackground(textColor);
        adminPanel.add(registerMovieButton);

        JButton registerSeriesButton=new JButton("Register a new series");
        registerSeriesButton.setForeground(primaryColor);
        registerSeriesButton.setBackground(textColor);
        adminPanel.add(registerSeriesButton);

        registerMovieButton.addActionListener(e -> new EditContentFrame(dataBase,(Admin) user,"movie"));

        registerSeriesButton.addActionListener(e -> new EditContentFrame(dataBase,(Admin) user,"series"));

        panel.add(adminPanel);
    }
}
