import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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

        File HEAD = new File("gitNew/HEAD");
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
        FileWriter fwObjects = new FileWriter(blobFile);
        fwObjects.write(strContentOfFile);
        fwObjects.close();

        Path pathToIndex = Path.of("gitNew/INDEX");
        BufferedReader brINDEX = Files.newBufferedReader(pathToIndex);
        ArrayList<String> linesOfIndex = new ArrayList<>();
        String pathToString = FilePath.toString();
        boolean rewriteINDEX = false;
        while (brINDEX.ready()) {
            String line = brINDEX.readLine();
            if (line.endsWith(pathToString)) {
                linesOfIndex.add(hashOfContents + " " + FilePath);
                rewriteINDEX = true;
            } else {
                linesOfIndex.add(line);
            }
        }
        brINDEX.close();

        if (rewriteINDEX) {
            FileWriter fwINDEX = new FileWriter("gitNew/INDEX");
            for (String line : linesOfIndex) {
                fwINDEX.write(line + "\n");
            }
            fwINDEX.close();
        } else {
            FileWriter fwINDEX = new FileWriter("gitNew/INDEX", true);
            // the true means that the data is being appended
            fwINDEX.write(hashOfContents + " " + FilePath + "\n");
            fwINDEX.close();

        }
    }

    public static String hashSHA1(String input) {
        String hashed = DigestUtils.sha1Hex(input);
        return hashed;
    }

}
