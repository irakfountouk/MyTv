import api.*;
import gui.LoginFrame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Το πρόγραμμά σας πρέπει να έχει μόνο μία main, η οποία πρέπει να είναι η παρακάτω.
 * <p>
 * <p>
 * ************* ΜΗ ΣΒΗΣΕΤΕ ΑΥΤΗ ΤΗΝ ΚΛΑΣΗ ************
 */


/**
 * The main class of the program responsible for initializing data and launching the application.
 *
 * This class contains the main method, which creates an object of the LoginFrame class
 * so users can log in the app, and which initializes the data files for users and content,
 * if program is being run for the first time -and as such the file "file.txt"
 * doesn't exist.
 *
 * The program's data includes information about admins, subscribers, movies, series, seasons,
 * episodes, and reviews.
 *
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 24-01-10
 */
public class Main
{
    private static File file=new File("file.txt");
    private static DataBase dataBase=new DataBase("content.dat","users.dat");

     /** The main method checks if the data file exists, initializes data if not, and then launches the
     LoginFrame to start the application.*/
    public static void main(String[] args) throws IOException
    {
            if (!file.exists())
            {
                initializeData();
                file.createNewFile();
            }
            new LoginFrame(dataBase);
    }

     /**Initializes the user and content files.*/
    public static void initializeData()
    {
        Admin admin1=new Admin("admin1","password1","Bob","Vance,Vance Refrigeration");
        Admin admin2=new Admin("admin2","password2","Aaa","Ddd");
        Subscriber subscriber1=new Subscriber("subscriber1","password1","Prison","Mike");
        Subscriber subscriber2=new Subscriber("subscriber2","password2","Name","Last-Name");

        Movie poorThings=new Movie("Poor Things","The incredible tale about the fantastical evolution of Bella Baxter; a young woman brought back to life by the brilliant and unorthodox scientist, Dr. Godwin Baxter.","No","Comedy","Emma Stone, Mark Ruffalo, William Dafoe",2023,141);
        Movie eternalSunshine=new Movie("Eternal Sunshine of the Spotless Mind","When their relationship turns sour, a couple undergoes a medical procedure to have each other erased from their memories for ever.","Yes","Drama","Jim Carrey,Kate Winslet,Tom Wilkinson",2004,108);
        Movie spiderManIntoSpiderverse=new Movie("Spider-Man: Into the Spider-Verse","Teen Miles Morales becomes the Spider-Man of his universe and must join with five spider-powered individuals from other dimensions to stop a threat for all realities.","Yes","Action","Shameik Moore,Jake Johnson,Hailee Steinfeld",2018,117);
        Movie talkToMe=new Movie("Talk to Me","When a group of friends discover how to conjure spirits using an embalmed hand, they become hooked on the new thrill, until one of them goes too far and unleashes terrifying supernatural forces.","No","Horror","Ari McCarthy, Hamish Philips, Kit Erhart-Bruce",2022,95);
        Movie thirteenthFloor=new Movie("The Thirteenth Floor","A computer scientist running a virtual reality simulation of 1937 becomes the primary suspect when his colleague and mentor is murdered.","Yes","Science fiction","Craig Bierko, Gretchen Mol, Armin Mueller-Stahl",1999,100);
        Movie shutterIsland=new Movie("Shutter Island","Teddy Daniels and Chuck Aule, two US marshals, are sent to an asylum on a remote island in order to investigate the disappearance of a patient, where Teddy uncovers a shocking truth about the place.","Yes","Drama","Leonardo DiCaprio, Emily Mortimer, Mark Ruffalo",2010,138);
        Movie memento=new Movie("Memento","A man with short-term memory loss attempts to track down his wife's murderer.","Yes","Drama","Guy Pearce, Carrie-Anne Moss, Joe Pantoliano",2000,113,shutterIsland);
        Movie everythingEverywhere=new Movie("Everything Everywhere All at Once","A middle-aged Chinese immigrant is swept up into an insane adventure in which she alone can save existence by exploring other universes and connecting with the lives she could have led.","Yes","Comedy","Michelle Yeoh, Stephanie Hsu, Jamie Lee Curtis",2022,139,poorThings,spiderManIntoSpiderverse);
        spiderManIntoSpiderverse.addToRelatedContent(everythingEverywhere);
        poorThings.addToRelatedContent(everythingEverywhere);
        shutterIsland.addToRelatedContent(memento);

        Season breakingSeason1=new Season(1,2008,new Episode(50),new Episode(45),new Episode(55),new Episode(40));
        Season breakingSeason2=new Season(2,2009,new Episode(40),new Episode(50),new Episode(50),new Episode(45));
        Season breakingSeason3=new Season(3,2010,new Episode(55),new Episode(42),new Episode(45),new Episode(52));
        Season breakingSeason4=new Season(4,2011,new Episode(50),new Episode(57),new Episode(43),new Episode(50));
        Season breakingSeason5=new Season(5,2012,new Episode(55),new Episode(46),new Episode(58),new Episode(49));

        ArrayList<Season> breakingBadSeasons=new ArrayList<>();
        breakingBadSeasons.add(breakingSeason1);
        breakingBadSeasons.add(breakingSeason2);
        breakingBadSeasons.add(breakingSeason3);
        breakingBadSeasons.add(breakingSeason4);
        breakingBadSeasons.add(breakingSeason5);
        Series breakingBad=new Series("Breaking Bad","A chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine with a former student in order to secure his family's future.","Yes","Drama","Bryan Cranston, Aaron Paul, Anna Gunn",breakingBadSeasons);

        Season fleabagSeason1=new Season(1,2016,new Episode(21),new Episode(26),new Episode(24),new Episode(30));
        Season fleabagSeason2=new Season(2,2019,new Episode(20),new Episode(28),new Episode(22),new Episode(25));
        ArrayList<Season> fleabagSeasons=new ArrayList<>();
        fleabagSeasons.add(fleabagSeason1);
        fleabagSeasons.add(fleabagSeason2);
        Series fleabag=new Series("Fleabag","Series adapted from the award-winning play about a young woman trying to cope with life in London whilst coming to terms with a recent tragedy.","Yes","Comedy","Phoebe Waller-Bridge, Sian Clifford, Olivia Colman",fleabagSeasons);

        // Create Seasons for the new Series
        Season strangerThingsSeason1 = new Season(1, 2016, new Episode(8), new Episode(8), new Episode(9), new Episode(9));
        Season strangerThingsSeason2 = new Season(2, 2017, new Episode(9), new Episode(9), new Episode(9), new Episode(9));
        Season strangerThingsSeason3 = new Season(3, 2019, new Episode(8), new Episode(8), new Episode(8), new Episode(8));
        ArrayList<Season> strangerThingsSeasons = new ArrayList<>();
        strangerThingsSeasons.add(strangerThingsSeason1);
        strangerThingsSeasons.add(strangerThingsSeason2);
        strangerThingsSeasons.add(strangerThingsSeason3);

        Season theCrownSeason1 = new Season(1, 2016, new Episode(10), new Episode(10), new Episode(10), new Episode(10));
        Season theCrownSeason2 = new Season(2, 2017, new Episode(10), new Episode(10), new Episode(10), new Episode(10));
        Season theCrownSeason3 = new Season(3, 2019, new Episode(10), new Episode(10), new Episode(10), new Episode(10));
        ArrayList<Season> theCrownSeasons = new ArrayList<>();
        theCrownSeasons.add(theCrownSeason1);
        theCrownSeasons.add(theCrownSeason2);
        theCrownSeasons.add(theCrownSeason3);

        Season theMandalorianSeason1 = new Season(1, 2019, new Episode(8), new Episode(8), new Episode(8), new Episode(8));
        Season theMandalorianSeason2 = new Season(2, 2020, new Episode(8), new Episode(8), new Episode(8), new Episode(8));
        Season theMandalorianSeason3 = new Season(3, 2022, new Episode(8), new Episode(8), new Episode(8), new Episode(8));
        ArrayList<Season> theMandalorianSeasons = new ArrayList<>();
        theMandalorianSeasons.add(theMandalorianSeason1);
        theMandalorianSeasons.add(theMandalorianSeason2);
        theMandalorianSeasons.add(theMandalorianSeason3);

        Series strangerThings = new Series("Stranger Things",
                "In a small town where everyone knows everyone, strange things start happening after a young boy goes missing. A group of kids, a few friends and their families, uncover a government conspiracy and a parallel dimension.",
                "Yes", "Science Fiction",
                "Winona Ryder, David Harbour, Finn Wolfhard", strangerThingsSeasons);

        Series theCrown = new Series("The Crown",
                "Follows the political rivalries and romance of Queen Elizabeth II's reign and the events that shaped the second half of the 20th century.",
                "Yes", "Drama",
                "Claire Foy, Matt Smith, Olivia Colman", theCrownSeasons);

        Series theMandalorian = new Series("The Mandalorian",
                "The travels of a lone bounty hunter in the outer reaches of the galaxy, far from the authority of the New Republic.",
                "Yes", "Science Fiction",
                "Pedro Pascal, Gina Carano, Giancarlo Esposito", theMandalorianSeasons);

        strangerThings.addToReviews(new Review(4, "Manages to fully immerse you in its mystery, thanks to both the writers and the actors.", subscriber1));
        theCrown.addToReviews(new Review(3,"While the writing and acting are very solid, the series can't stop itself from redeeming the royals as an institution, by outlining only the personal shortcomings of the family's members.",subscriber2));
        theMandalorian.addToReviews(new Review(4,"The Mandalorian could very well capitalize solely on the legacy of Star Wars and bring nothing of its own to the table. Despite that, the writers chose to make something that expands upon the franchise while having a unique character of its own.",subscriber1));
        poorThings.addToReviews(new Review(4,"Lanthimos' arguably most Hollywood-appropriate film is also the one to flesh out the protagonists in the best way so far, without leaving behind entirely the signature style of the director. The cinematography is, by general consensus, breathtaking, with every scene and its colors leaving you in total awe.",subscriber2));
        eternalSunshine.addToReviews(new Review(5,"Jim Carrey proves once again that he can do drama just as exceptionally as comedy. This movie doesn't confine itself to telling a love story linearly, but it goes back and forth in time to impose on the viewers the mental turmoil of the protagonists. The thing that makes it stand out most compared to other romance films is that, instead of shying away from the characters' flaws, these flaws are what the story is built on.",subscriber1));
        talkToMe.addToReviews(new Review(4,"Horror films are very vulnerable to clichés, which can make a lot of people quite biased against them. Although this movie's goal isn't to bring something entirely new to the genre, it very much accomplishes to build its tone in the way that's best suited for it.",subscriber2));
        thirteenthFloor.addToReviews(new Review(4, "An unfairly overlooked sci-fi film, that manages to keep you on your toes the whole way thought the mystery's unfolding.",subscriber1));
        memento.addToReviews(new Review(4,"In standard Christopher Nolan fashion, Memento's convoluted unraveling of the plot requires the viewer's full attention. Building on the unreliable narrator trope, the story;s twist and turns make you question who's telling the truth and who's lying.", subscriber2));
        everythingEverywhere.addToReviews(new Review(5,"A story that travels across space and time to navigate the lines of nihilism and optimism, while having protagonists and viewers experience a wide range of emotions.",subscriber1));
        breakingBad.addToReviews(new Review(5,"Breaking Bad is a rare instance of a series that perfectly wraps action in the most realistic circumstances, which works wonders for character development. More than a story of how a drug empire is built, this is a story about how circumstances can make a person go from point A to catercorner point B.",subscriber2));
        fleabag.addToReviews(new Review(5,"Don't let the comedy genius of Phoebe Waller-Bridge fool you. Fleabag is, at its core, a story about love in all its forms -love for friends, love for family, romantic love- and all its manifestations, ranging from infatuation to grief. The very likeable but deeply flawed Fleabag goes down the path of the anti-hero protagonist trope to retell a very human story that can resonate with everyone.",subscriber1));
        spiderManIntoSpiderverse.addToReviews(new Review(4,"A nice take on the classic Spider-Man story, that is as fun as it is wholesome.",subscriber2));
        shutterIsland.addToReviews(new Review(4,"The mysterious atmosphere of Shutter Island makes for a very captivating thriller that turns out to be much more than it has you believe at first.",subscriber1));


        subscriber1.addToFavorites(fleabag);
        subscriber2.addToFavorites(everythingEverywhere);

        dataBase.addToContent(strangerThings, theCrown, theMandalorian,poorThings,eternalSunshine,talkToMe,thirteenthFloor,memento,everythingEverywhere,breakingBad,fleabag,spiderManIntoSpiderverse,shutterIsland);
        dataBase.addToUsers(admin1,admin2,subscriber1,subscriber2);
    }
}
