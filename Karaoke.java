import com.dlflann.KaraokeMachine;
import com.dlflann.model.Song;
import com.dlflann.model.SongBook;

public class Karaoke
{
    public static void main(String[] args)
    {
        SongBook songBook = new SongBook();
        KaraokeMachine machine = new KaraokeMachine(songBook);
        machine.run();
    }
}