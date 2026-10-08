import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

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
        Path a = Path.of("testfolder/hello");
        Git.createBlob(a);
        Path b = Path.of("testfolder/helo2");
        Git.createBlob(b);




        ArrayList<String> list = new ArrayList<String>();
        list.add("blob 0a4d55a8d778e5022fab701977c5d840bbc486d0 myProgram/docs/Hello.txt");
        list.add("blob 483b5e082cf5502b303ba3dd4f3469a49fd3421f myProgram/docs/World.txt");
        list.add("blob 4377a91cdfd44db9a9bbf056849c7da0fc6cc7be myProgram/README.md");
        System.out.println(Git.createTree(list, "myProgram/docs"));
        System.out.println(Git.createTree(list, "myProgram/docs"));
    }
}
