package service;

import domain.FileWithId;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileService {
    private final StorageService storageService = new StorageService();
    private final DisplayService displayService = new DisplayService();

    public File[] getFilesFromDir(String dirPath) {
        File dir = new File(dirPath);
        return dir.listFiles();
    }

    public List<FileWithId> prepareTrackListDisplay(String dirPath) throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        File[] fileArr = getFilesFromDir(dirPath);
        assert fileArr != null;
        int fileCounter = 0;

        List<FileWithId> fileWithIdList = new LinkedList<>();
        String newFileNameWithId = "";

        for (File file : fileArr) {
            if (!file.getName().endsWith(".mp3")) {
                continue;
            }
            fileCounter = fileCounter + 1;
            FileWithId fileWithId = new FileWithId(fileCounter, file);
            fileWithIdList.add(fileWithId);
            MP3File mp3File = new MP3File(file);

            if (nameChecker(fileWithId)) {
                newFileNameWithId = ifMatches(newFileNameWithId, fileWithId);
            } else {
                newFileNameWithId = String
                        .valueOf((fileWithId.getId()))
                        .concat(". ")
                        .concat(fileWithId.getFile().getName());
            }
            displayService.displayFileStaff(newFileNameWithId, mp3File);
        }
        return fileWithIdList;
    }

    private String ifMatches(String newFileNameWithId, FileWithId fileWithId) {
        short charCounterForCycle = 0;
        newFileNameWithId = fileWithId.getFile().getName();
        char[] charArr = newFileNameWithId.toCharArray();
        for (char c : charArr) {
            if (c == '.') {
                break;
            }
            charCounterForCycle++;
        }
        return newFileNameWithId = String
                .valueOf((fileWithId.getId()))
                .concat(". " + fileWithId.getFile().getName().substring(charCounterForCycle + 2));
    }

    public void changePosition(List<FileWithId> fileWithIdList) throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {

        String idFromStr = displayService.inputIdFromForChangeFilePosition();
        int fileIdFrom = Integer.parseInt(idFromStr);
        String idToStr = displayService.inputIdToForChangeFilePosition();
        int fileIdTo = Integer.parseInt(idToStr);

        FileWithId fileWithIdFrom = new FileWithId(fileWithIdList.get(fileIdFrom - 1).getId(), fileWithIdList.get(fileIdFrom - 1).getFile());
        FileWithId fileWithIdTo = new FileWithId(fileWithIdList.get(fileIdTo - 1).getId(), fileWithIdList.get(fileIdTo - 1).getFile());
        if (fileIdFrom > fileIdTo) {
            fileWithIdList.add(fileWithIdTo.getId() - 1, fileWithIdFrom);
            fileWithIdList.remove(fileWithIdFrom.getId());
        } else {
            fileWithIdList.add(fileWithIdTo.getId(), fileWithIdFrom);
            fileWithIdList.remove(fileWithIdFrom.getId() - 1);
        }
        int counter = 0;
        String newNameWithId;
        for (FileWithId file : fileWithIdList) {
            counter++;
            file.setId(counter);
            String newIdStr = String.valueOf(file.getId());
            if (nameChecker(file)) {

                newNameWithId = String.valueOf(file.getId()).concat(". " + file.getFile().getName().substring(newIdStr.length() + 2));

            } else {
                newNameWithId = String.valueOf(file.getId()).concat(". " + file.getFile().getName());
            }
            storageService.changeFileId(file, newNameWithId);
        }
    }

    public void changeMetaInfo(List<FileWithId> fileWithIdList, FieldKey fieldKey) throws IOException, TagException, InvalidAudioFrameException, ReadOnlyFileException {
        String IdFromScanner = displayService.inputId();
        List<String> idFromScannerList = Arrays.asList(IdFromScanner.split(", "));

        String newName = displayService.inputName();
        List<String> nameFromScannerList = Arrays.asList(IdFromScanner.split(", "));
        if (idFromScannerList.size() != nameFromScannerList.size()) {
            System.out.println("Count of the id's not equals count of the names.");
            changeMetaInfo(fileWithIdList, fieldKey);
            return;
        }
        for (String str : idFromScannerList) {
            if (Integer.parseInt(str) > idFromScannerList.size()) {
                System.out.println("Some id is invalid");
                return;
            } else {
                File file = fileWithIdList.get(Integer.parseInt(str) - 1).getFile();
                MP3File mp3File = new MP3File(file);
                mp3File.getID3v2Tag().setField(fieldKey, newName);
                mp3File.save(file);
            }
        }
        displayService.displayDone();
    }


    public void eraseGarbage(List<FileWithId> fileWithIdList) throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        String garbageFromScanner = displayService.inputGarbageName();
        List<String> garbageFromScannerList = Arrays.asList(garbageFromScanner.split(", "));
        String tagsFromScanner = displayService.inputTag();
        List<String> tagsFromScannerList = Arrays.asList(tagsFromScanner.split(", "));
        String IdFromScanner = displayService.inputId();
        List<String> idFromScannerList = Arrays.asList(IdFromScanner.split(", "));

        for (String str : idFromScannerList) {
            File file = fileWithIdList.get(Integer.parseInt(str) - 1).getFile();
            MP3File mp3File = new MP3File(file);
            String oldTag = mp3File
                    .getID3v2TagAsv24()
                    .getFirst(FieldKey.valueOf(tagsFromScannerList.get(0)));
            String newTag = oldTag.replace(garbageFromScannerList.get(0), "");
            mp3File.getID3v2Tag().setField(FieldKey.valueOf(tagsFromScannerList.get(0)), newTag);
            mp3File.save(file);
        }
        displayService.displayDone();
    }

    private boolean nameChecker(FileWithId fileWithId) {
        Pattern pattern = Pattern.compile("^\\d+\\.\\s.+$");
        Matcher matcher = pattern.matcher(fileWithId.getFile().getName());
        return matcher.matches();
    }
 /*   public void changeFileName(List<FileWithId> fileWithIdList) throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        String oldNameWithId = subMenuService.inputId();
        String newName = subMenuService.inputName();
        FileWithId fileWithId = fileWithIdList.get(0);
        File fileWithNewName;

        Pattern pattern = Pattern.compile("^\\d+\\.\\s.+$");
        Matcher matcher = pattern.matcher(newName);

        if (matcher.matches()) {
            fileWithNewName = new File(fileWithIdList.get(0).getFile().getPath() + newName);
            fileWithId.getFile().renameTo(fileWithNewName);
        }else {
            fileWithNewName = new File(fileWithIdList.get(0).getFile().getPath() + fileWithId.getId() + newName);
            fileWithId.getFile().renameTo(fileWithNewName);
        }

        fileWithIdList.get(0).setFile(fileWithNewName);
        storageService.changeFileName(fileWithIdList.get(0));

    }*/




}