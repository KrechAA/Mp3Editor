package service;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v24FieldKey;

import java.util.Scanner;

public class DisplayService {

    public String inputDirectory() {
        System.out.println("Input a directory: ");
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    public String inputId() {
        System.out.println("Input file id: ");
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    public String inputIdFromForChangeFilePosition() {
        System.out.println("Input file id from: ");
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    public String inputIdToForChangeFilePosition() {
        System.out.println("Input file id to: ");
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    public String inputName() {
        Scanner in = new Scanner(System.in);
        System.out.println("Input new name: ");
        return in.nextLine();
    }

    public String inputGarbageName() {
        Scanner in = new Scanner(System.in);
        System.out.println("Input the text you want to remove: ");
        return in.nextLine();
    }

    public String inputTag() {
        Scanner in = new Scanner(System.in);
        System.out.println("Input the tag you want to edit (ARTIST, TITLE, ALBUM, COMMENT, GENRE): ");
        return in.nextLine();
    }

    public String displayDone() {
        return "Done";
    }

    public void displayFileStaff(String newFileNameWithId, MP3File mp3File) {
        System.out.println(newFileNameWithId + ". " +
                mp3File.getID3v2TagAsv24().getFirst(ID3v24FieldKey.ARTIST) + ". " +
                mp3File.getID3v2TagAsv24().getFirst(ID3v24FieldKey.TITLE) + ". " +
                mp3File.getID3v2TagAsv24().getFirst(ID3v24FieldKey.ALBUM) + ". " +
                mp3File.getID3v2TagAsv24().getFirst(ID3v24FieldKey.COMMENT) + ". " +
                mp3File.getID3v2TagAsv24().getFirst(ID3v24FieldKey.GENRE) + ". " +
                mp3File.getID3v2TagAsv24().getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
    }

    public int displayMainMenu() {
        System.out.println("\nWhat do you want?\n");
        System.out.println("1. Change the file Name.");
        System.out.println("2. Change the track Position.");
        System.out.println("3. Change the track MetaInfo.");
        System.out.println("4. Exit.");
        Scanner in = new Scanner(System.in);
        String choiceStr = in.nextLine();

        return Integer.parseInt(choiceStr);
    }

    public int displaySubMenuMetaInfo() {
        System.out.println("\nWhat do you want?\n");
        System.out.println("\t1. Change the track Artist (ARTIST).");
        System.out.println("\t2. Change the track Name (TITLE).");
        System.out.println("\t3. Change the track Album (ALBUM).");
        System.out.println("\t4. Change the track Comment (COMMENT).");
        System.out.println("\t5. Change the track Genre (GENRE).");
        System.out.println("\t6. Erase some text from the tag. (ARTIST, TITLE, ALBUM, COMMENT, GENRE");
        System.out.println("\t7. Back.");
        Scanner in = new Scanner(System.in);
        String choiceStr = in.nextLine();

        return Integer.parseInt(choiceStr);
    }

    public  int displaySubMenuSubOptions(){
        System.out.println("\nInsert id's or \"*\" for select all track's");

        Scanner in = new Scanner(System.in);
        String choiceStr = in.nextLine();
        if(choiceStr.equals("*")){
            return 1;
        }

        return 2;
    }


}
