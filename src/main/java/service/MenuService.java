package service;

import domain.FileWithId;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import java.io.IOException;
import java.util.List;

public class MenuService {
    public Boolean isNeedStop = false;
    private final FileService fileService;
    private final DisplayService displayService;

    public MenuService(FileService fileService, DisplayService displayService) {
        this.fileService = fileService;
        this.displayService = displayService;
    }

    public void start(String dirPath) throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        List<FileWithId> fileWithIdList = fileService.prepareTrackListDisplay(dirPath);
        int choiceMainMenu = displayService.displayMainMenu();
        switch (choiceMainMenu) {
            case 1, 2 -> mainMenuOptionsChoice(choiceMainMenu, fileWithIdList);
            case 3 -> {
                int choiceSubMenu = displayService.displaySubMenuMetaInfo();
                if (choiceSubMenu == 7) {
                    start(dirPath);
                }
                subMenuOptionsChoice(choiceSubMenu, fileWithIdList);
            }
            case 4 -> isNeedStop = true;
        }
    }

    public void mainMenuOptionsChoice(int choice, List<FileWithId> fileWithIdList) throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        switch (choice) {
            //   case 1 -> fileService2.changeFileName(fileWithIdList);
            case 2 -> fileService.changePosition(fileWithIdList);
            case 3 -> displayService.displaySubMenuMetaInfo();
        }
    }

    public void subMenuOptionsChoice(int choice, List<FileWithId> fileWithIdList) throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        switch (choice) {
            case 1 -> fileService.changeMetaInfo(fileWithIdList, FieldKey.ARTIST);

            case 2 -> fileService.changeMetaInfo(fileWithIdList, FieldKey.TITLE);
            case 3 -> fileService.changeMetaInfo(fileWithIdList, FieldKey.ALBUM);
            case 4 -> fileService.changeMetaInfo(fileWithIdList, FieldKey.COMMENT);
            case 5 -> fileService.changeMetaInfo(fileWithIdList, FieldKey.GENRE);
            case 6 -> {
                int subMenuSubOptionsChoice = (displayService.displaySubMenuSubOptions());
                if (subMenuSubOptionsChoice == 1) {
                    fileService.eraseGarbageInAllTracks(fileWithIdList);

                } else {
                    fileService.eraseGarbage(fileWithIdList);
                }

            }
        }
    }
}





