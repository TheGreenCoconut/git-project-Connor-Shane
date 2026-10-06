import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Git {
    public static void init() {
        int count = 0;
        File gitDir = new File("git");
        if (!gitDir.exists()) {
            gitDir.mkdir();
            count++;
        }
        File INDEX = new File("git/INDEX");
        if (!INDEX.exists()) {
            try {
                INDEX.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            count++;
        }
        File objects = new File("git/objects");
        if (!objects.exists()) {
            objects.mkdir();
            count++;
        }

        File HEAD = new File("git/HEAD");
        if (!HEAD.exists()) {
            try {
                HEAD.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            count++;
        }
        if (count > 0) {
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
        FileWriter blobWriter = new FileWriter(blobFile);
        blobWriter.write(strContentOfFile);
        blobWriter.close();

        Path pathToIndex = Path.of("git/INDEX");
        BufferedReader indexReader = Files.newBufferedReader(pathToIndex);
        ArrayList<String> linesOfIndex = new ArrayList<>();
        String pathToString = FilePath.toString();
        boolean fileAlreadyIndexed = false;
        while (indexReader.ready()) {
            String line = indexReader.readLine();
            if (line.endsWith(pathToString)) {
                linesOfIndex.add(hashOfContents + " " + FilePath);
                fileAlreadyIndexed = true;
            } else {
                linesOfIndex.add(line);
            }
        }
        indexReader.close();

        

        if (fileAlreadyIndexed) {
            FileWriter indexWriter = new FileWriter("git/INDEX");
            for (String line : linesOfIndex) {
                indexWriter.write(line + "\n");
            }
            indexWriter.close();
        } else {
            FileWriter fwINDEX = new FileWriter("git/INDEX", true);
            // the true means that the data is being appended
            Path path = Paths.get(FilePath.toString());
            fwINDEX.write(hashOfContents + " " + path.toString() + "\n");
            fwINDEX.close();

        }
    }

    public static void add(Path FilePath) throws IOException {
        createBlob(FilePath);
    }

    public static void createTree(String input) throws IOException {
        Path path = Path.of(input);
        
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
