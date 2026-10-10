import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;


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

        contentOfFile.delete(contentOfFile.length() - 1, contentOfFile.length()); // PEER-REVIEW:
                                                                                  // Removed the
                                                                                  // trailing
                                                                                  // whitespace

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
            // for (String line : linesOfIndex) {
            // indexWriter.write(line + "\n");
            // }
            for (int line = 0; line < linesOfIndex.size(); line++) {
                indexWriter.write(linesOfIndex.get(line));
                if (line != linesOfIndex.size() - 1) {
                    indexWriter.write("\n");
                }
            }
            indexWriter.close();
        } else {
            FileWriter fwINDEX = new FileWriter("git/INDEX", true);
            // the true means that the data is being appended
            Path path = Paths.get(FilePath.toString());
            FileReader frINDEX = new FileReader("git/INDEX");
            if (frINDEX.read() == -1) { // PEER-REVIEW: Handles trailing whitespace in INDEX
                fwINDEX.write(hashOfContents + " " + path.toString());
            } else {
                fwINDEX.write("\n" + hashOfContents + " " + path.toString());
            }
            frINDEX.close();
            fwINDEX.close();

        }
    }

    public static void add(Path FilePath) throws IOException {
        createBlob(FilePath);
    }



    public static String hashSHA1(String input) throws IOException {
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

    public static String createTree(ArrayList<String> workingList, String dirPath)
            throws IOException {
        StringBuilder bcontent = new StringBuilder();
        // put in parts, for loop

        // then put all together, probably stringbuilder
        for (String current : workingList) {
            String[] parts = current.split(" ");
            String type = parts[0];
            String hash = parts[1];
            String path = parts[2];
            // fix the checks later, add root check
            String whole = type + " " + hash + " " + path;
            if (whole.contains(" " + dirPath + "/")) {
                String name = path.substring(dirPath.length() + 1);
                String add = type + " " + hash + " " + name + "\n";
                bcontent.append(add);
            } else if (dirPath.equals("")) {
                if (path.contains("/") == false) {
                    String add = type + " " + hash + " " + path + "\n";
                    bcontent.append(add);
                }
            }


        }
        String content = bcontent.toString().substring(0, bcontent.length() - 1);
        FileWriter fw = new FileWriter("git/objects/" + hashSHA1(content));
        fw.write(content);
        fw.close();
        return hashSHA1(content);
    }
    // count slashes to do done

    // built list, blob for each done

    // loop:
    // sort list done
    // find deepest path
    // check if no slashes no slashes then done done
    // else go to the deepest folder done
    // and createtree
    // remove from all of the stuff from the workinglist
    // then add the tree to
    // wokrfjfrjoieefrevreifjrjoiefrjioferjioefojiefrijoferjoiferijoefriojefjoirjfioer
    public static String createTreeFromIndex() throws IOException {

        ArrayList<String> workingList = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("git/INDEX"));
        while (br.ready()) {
            String line = br.readLine().toString();
            workingList.add("blob " + line);
        }

        br.close();
        boolean done = false;
        while (done == false) {
            for (int i = 0; i < workingList.size(); i++) {
                String[] parts = workingList.get(i).split(" ");
                int min = i;
                for (int j = i + 1; j < workingList.size(); j++) {
                    String[] parts2 = workingList.get(j).split(" ");
                    String check = parts2[2];
                    String[] parts3 = workingList.get(min).split(" ");
                    String minp = parts3[2];
                    if (minp.compareTo(check) > 0) {
                        min = j;
                    }

                }
                String temp = workingList.get(i);
                workingList.set(i, workingList.get(min));
                workingList.set(min, temp);

            }

            int maxslash = 0;
            String deep = "";
            for (int i = 0; i < workingList.size(); i++) {
                String[] parts = workingList.get(i).split(" ");
                String path = parts[2];
                if (numSlashes(path) > maxslash) {
                    maxslash = numSlashes(path);
                    deep = path;
                }
            }

            if (maxslash == 0) {
                return createTree(workingList, "");
            } else {
                int last = 0;
                for (int j = 0; j < deep.length(); j++) {
                    if (deep.substring(j, j + 1).equals("/")) {
                        last = j;
                    }
                }
                String dir = deep.substring(0, last);
                String tree = createTree(workingList, dir);


                int temp = 0;
                for (int i = workingList.size() - 1; i > -1; i--) {
                    if (workingList.get(i).contains(" " + dir + "/")) {
                        workingList.remove(i);
                        temp = i;
                    }
                }


                workingList.add(temp, "tree " + tree + " " + dir);
            }

        }

        return "";
    }

    public static int numSlashes(String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.substring(i, i + 1).equals("/")) {
                count += 1;
            }
        }
        return count;
    }


}
