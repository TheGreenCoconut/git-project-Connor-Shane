import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Git {
    public static void init() {
        int num = 0;
        File gitNew = new File("git");
        if (!gitNew.exists()) {
            gitNew.mkdir();
            num++;
        }
        File INDEX = new File("git/INDEX");
        if (!INDEX.exists()) {
            try {
                INDEX.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            num++;
        }
        File objects = new File("git/objects");
        if (!objects.exists()) {
            objects.mkdir();
            num++;
        }

        File HEAD = new File("git/HEAD");
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

    public static void createBlob(Path FilePath) throws IOException {
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
        File blobFile = new File("git/objects", hashOfContents);
        FileWriter fwObjects = new FileWriter(blobFile);
        fwObjects.write(strContentOfFile);
        fwObjects.close();

        Path pathToIndex = Path.of("git/INDEX");
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
            FileWriter fwINDEX = new FileWriter("git/INDEX");
            for (String line : linesOfIndex) {
                fwINDEX.write(line + "\n");
            }
            fwINDEX.close();
        } else {
            FileWriter fwINDEX = new FileWriter("git/INDEX", true);
            // the true means that the data is being appended
            fwINDEX.write(hashOfContents + " " + FilePath + "\n");
            fwINDEX.close();

        }
    }

    public static String hashSHA1(String input) throws IOException{
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    hexString.append(String.format("%02x", b));
                }
            return hexString.toString();
        } catch (Exception e) {
            System.out.println("cant");
            return null;
        }
    }

}
