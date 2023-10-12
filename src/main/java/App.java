

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import service.DisplayService;
import service.FileService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import service.MenuService;

public class App {
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio");


    public static void main(String[] args) throws IOException, TagException, InvalidAudioFrameException, ReadOnlyFileException {
        logger.getParent().setLevel(Level.OFF);

        DisplayService displayService = new DisplayService();
        FileService fileService = new FileService(displayService);
        MenuService menuService = new MenuService(fileService, displayService);

        String dirPath = displayService.inputDirectory();

        while (!menuService.isNeedStop) {
             menuService.start(dirPath);
        }
    }
}
