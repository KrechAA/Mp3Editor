package service;

import domain.FileWithId;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import java.io.File;
import java.io.IOException;

public class StorageService {
    DisplayService displayService = new DisplayService();

    public void changeFileId(FileWithId fileWithId,  String newNameWithId) throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
            File fileDest = new File(fileWithId.getFile().getParentFile().getPath() + "\\" + newNameWithId);
            fileWithId.getFile().renameTo(fileDest);
            MP3File mp3File = new MP3File(fileDest);
            mp3File.save(fileDest);

            displayService.displayDone();
        }
}
