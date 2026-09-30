import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

//

public class Verify {
    public static void main(String[] args) throws IOException {
        Git.init();
        Path readmepath = Path.of("README.md");
        Git.createBlob(readmepath);
        Path emptypath = Path.of("Tester.java");
        Git.createBlob(emptypath);
        Path e2 = Path.of("asd");
        Git.createBlob(e2);
        FileWriter test = new FileWriter("asd", true);
        test.append("hello");
        test.close();
        Git.createBlob(e2);
        FileWriter test2 = new FileWriter("asd", true);
        test2.append("hello");
        test2.close();
        Git.createBlob(e2);
    }
}
