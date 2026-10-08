import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

//

public class Verify {
    public static void main(String[] args) throws IOException {
        Git.init();

        Path e2 = Path.of("asd");
        Git.createBlob(e2);
        Path a = Path.of("testfolder/hello");
        Git.createBlob(a);
        Path b = Path.of("testfolder/helo2");
        Git.createBlob(b);
        Path c = Path.of("testfolder/h/helo5");
        Git.createBlob(c);
        Path d = Path.of("testfolder/h/helo3");
        Git.createBlob(d);

        System.out.println(Git.createTreeFromIndex());

    }
}
