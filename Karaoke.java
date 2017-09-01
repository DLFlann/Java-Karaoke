import com.dlflann.KaraokeMachine;
import com.dlflann.model.Song;
import com.dlflann.model.SongBook;

public class Karaoke
{
    public static void main(String[] args)
    {
        SongBook songBook = new SongBook();
        songBook.importFrom("songs.txt");
        KaraokeMachine machine = new KaraokeMachine(songBook);
        machine.run();
        System.out.println("Saving book...");
        songBook.exportTo("songs.txt");
    }
}