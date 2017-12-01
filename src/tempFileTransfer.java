import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

public class tempFileTransfer
{
    public static void main(String args[]) throws IOException
    {
        String path = System.getProperty("user.dir");
        System.out.println(path);
        path += "\\tempFiles";

        File dir = new File(path + "\\newFile");

        boolean successful = dir.mkdir();
        if (successful)
        {
            // created the directories successfully
            System.out.println("directories were created successfully");
        }
        else
        {
            // something failed trying to create the directories
            System.out.println("failed trying to create the directories");
        }

        File file = new File(path + "\\MusicFile.mp3");
        Path Path = file.toPath();
        byte[] fileArray;
        fileArray = Files.readAllBytes(Path);
        if(fileArray.length != 10)//todo check config file for correct size
        {
            //todo if not correct size throw error
        }
        int pieceSize = 40000; //todo make this read the config
        int pieceArraySize = fileArray.length / pieceSize;
        if (fileArray.length % pieceSize != 0) pieceArraySize++;

        byte[][] pieceArray = new byte[pieceArraySize][pieceSize];

        for(int i=0; i<pieceArraySize; i++)
        {
            for(int j=i*pieceSize, k=0; j<(i+1)*pieceSize; j++, k++)
            {
                pieceArray[i][k] =  fileArray[j];
                if (j == fileArray.length-1) {
                    break;
                }
            }
        }
        byte[] temp = pieceArray[1];

        System.out.println("temp size: " + temp.length);
//        System.out.println(pieceArray[1].length);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path + "\\newFile\\filename.mp3");
            for (int i = 0; i < pieceArraySize; i++)
            {
                fileOutputStream.write(pieceArray[i]);
            }
        } finally {
            fileOutputStream.close();
        }

        System.out.println(fileArray.length);
        System.out.println(pieceArraySize);

        file = new File(path + "\\newFile\\filename.mp3");
        //Removing the last 2 bytes which are two NUL bytes
        RandomAccessFile raf = new RandomAccessFile(file,"rwd");
        raf.setLength(file.length()-2);

        Path = file.toPath();
        byte[] fileArray2;
        fileArray2 = Files.readAllBytes(Path);
        System.out.println(fileArray2.length);
    }
}
