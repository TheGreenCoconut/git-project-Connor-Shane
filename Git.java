import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.DigestUtils;

public class Git {
    public void init() {
        int num = 0;
        File gitNew = new File("gitNew");
        if (!gitNew.exists()) {
            gitNew.mkdir();
            num++;
        }
        File INDEX = new File("gitNew/INDEX");
        if (!INDEX.exists()) {
            try {
                INDEX.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            num++;
        }
        File objects = new File("gitNew/objects");
        if (!objects.exists()) {
            objects.mkdir();
            num++;
        }

        File HEAD = new File("git");
        if (!HEAD.exists()) {
            try {
                HEAD.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            num++;
        }
        if (num > 0) {
            System.out.println("Git Repository Created");
        } else {
            System.out.println("Git Repository Already Exists");
        }

    }

    public void createBlob(Path FilePath) throws IOException {
        // read file and turn to string
        StringBuilder contentOfFile = new StringBuilder();
        BufferedReader br = Files.newBufferedReader(FilePath);
        while (br.ready()) {
            String line = br.readLine();
            contentOfFile.append(line).append("\n");
        }
        br.close();
        String strContentOfFile = contentOfFile.toString();
        String hashOfContents = hashSHA1(strContentOfFile);
        File blobFile = new File("gitNew/objects", hashOfContents);
        FileWriter fw = new FileWriter(blobFile);
        fw.write(strContentOfFile);
        fw.close();
    }

    public static String hashSHA1(String input) {
        String hashed = DigestUtils.sha1Hex(input);
        return hashed;
    }

}
