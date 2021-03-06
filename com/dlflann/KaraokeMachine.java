package com.dlflann;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import com.dlflann.model.Song;
import com.dlflann.model.SongBook;

public class KaraokeMachine
{
    private SongBook mSongBook;
    private BufferedReader mReader;
    private Queue<Song> mSongQueue;
    private Map<String, String> mMenu;

    public KaraokeMachine(SongBook songBook)
    {
        mSongBook = songBook;
        mReader = new BufferedReader(new InputStreamReader(System.in));
        mSongQueue = new ArrayDeque<>();
        mMenu = new TreeMap<>();
        mMenu.put("Add", "Add a new song to the menu.");
        mMenu.put("Choose", "Choose a song to sing!");
        mMenu.put("Play", "Play next song in the queue");
        mMenu.put("Quit", "Exit the program.");
    }

    private String promptAction() throws IOException
    {
        System.out.printf("There are %d songs available and %d in the queue. Your options are: %n", 
                          mSongBook.getSongCount(), mSongQueue.size());
        for (Map.Entry<String, String> option : mMenu.entrySet())
        {
            System.out.printf("%s - %s %n", option.getKey(), option.getValue());
        }
        System.out.print("What do you want to do:  ");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    public void run()
    {
        String choice = "";
        do
        {
            try 
            {
                choice = promptAction();
                switch(choice)
                {
                    case "add":
                        Song song = promptNewSong();
                        mSongBook.addSong(song);
                        System.out.printf("%s added! %n", song);
                        break;
                    case "choose":
                        String artist = promptArtist();
                        Song artistSong = promptSongForArtists(artist);
                        mSongQueue.add(artistSong);
                        System.out.printf("You chose: %s %n", artistSong);
                        break;
                    case "play":
                        playNext();
                        break;
                    case "quit":
                        System.out.println("Thanks for playing!");
                        break;
                    default:
                        System.out.printf("Sorry, I'm not sure what that means. \"%s\" %n%n", choice);
                        break;
                }
            }
            catch (IOException ioe) 
            {
                System.out.println("Problem reading input");
                ioe.printStackTrace();
            }
        } while (!choice.equals("quit"));
    }

    private Song promptNewSong() throws IOException
    {
        System.out.print("Enter Artist Name:  ");
        String artist = mReader.readLine();
        System.out.print("Enter Song Title:  ");
        String title = mReader.readLine();
        System.out.print("Enter Video URL:  ");
        String videoUrl = mReader.readLine();
        return new Song(artist, title, videoUrl);
    }

    private Song promptSongForArtists(String artist) throws IOException
    {
        List<Song> songs = mSongBook.getSongsForArtist(artist);
        List<String> songTitles = new ArrayList<>();
        for (Song song : songs)
        {
            songTitles.add(song.getTitle());
        }
        System.out.printf("Available songs for %s: %n", artist);
        int index = promptForIndex(songTitles);
        return songs.get(index);
    }

    private int promptForIndex(List<String> options) throws IOException
    {
        int counter = 1;
        for (String option : options)
        {
            System.out.printf("%d.) %s %n", counter, option);
            counter++;
        }
        System.out.print("Your choice:  ");
        String optionAsString = mReader.readLine();
        int choice = Integer.parseInt(optionAsString.trim());
        return choice - 1;
    }

    private String promptArtist() throws IOException
    {
        System.out.println("Available artists: ");
        List<String> artist = new ArrayList<>(mSongBook.getArtists());
        int index = promptForIndex(artist);
        return artist.get(index);
    }

    public void playNext()
    {
        Song song = mSongQueue.poll();
        if (song == null)
        {
            System.out.println("Sorry there are no songs in the queue. Use choose from the menu to add one.");
        }
        else
        {
            try
            {
                if(Desktop.isDesktopSupported())
                {
                    Desktop.getDesktop().browse(new URI(song.getVideoUrl()));
                }
            }
            catch (URISyntaxException uri)
            {
                System.out.println("Problem opening the specified URI");
                uri.printStackTrace();
            }
            catch (IOException ioe)
            {
                System.out.println("Issue opening the browser instance");
                ioe.printStackTrace();
            }
        }
    }
}