import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {

    private static final ClassLoader CLASS_LOADER = FileParserTest.class.getClassLoader();

    @Test
    void invalidFileInput() {
        //given:
        String fileName = "NotExists";
        File file = new File(fileName);
        //expect:
        assertThrows(IllegalArgumentException.class,
                () -> new FileParser(file),
                "Input file is not valid"
                );
    }

    @Test
    void parseEmptyFile() throws IOException {
        //given:
        String fileName = "EmptyFile";
        File file = getFileByFileName(fileName);
        FileParser fileParser = new FileParser(file);
        //expect:
        assertEquals("", fileParser.getContent());
        assertEquals("", fileParser.getContentWithoutUnicode());
    }

    @Test
    void parseFileWithPlainTextOnly() throws IOException {
        //given:
        String fileName = "FileWithPlainText";
        File file = getFileByFileName(fileName);
        FileParser fileParser = new FileParser(file);
        String expectedContent = "Hello, IPONWEB!";
        //expect:
        assertEquals(expectedContent, fileParser.getContent());
        assertEquals(expectedContent, fileParser.getContentWithoutUnicode());
    }

    @Test
    void parseFileWithUnicodeAndPlainText() throws IOException {
        //given:
        String fileName = "FileWithUnicode";
        File file = getFileByFileName(fileName);
        FileParser fileParser = new FileParser(file);
        //expect:
        assertEquals("1\u00802\u01513", fileParser.getContent());
        assertEquals("123", fileParser.getContentWithoutUnicode());
    }

    @Test
    void parseFileWithUnicodeOnly() throws IOException {
        //given:
        String fileName = "FileWithUnicodeOnly";
        File file = getFileByFileName(fileName);
        FileParser fileParser = new FileParser(file);
        //expect:
        assertEquals("\u0080\u0151", fileParser.getContent());
        assertEquals("", fileParser.getContentWithoutUnicode());
    }

    @Test
    void saveContent() throws IOException {
        //given:
        String fileName = "FileToSaveContent";
        File file = getFileByFileName(fileName);
        FileParser fileParser = new FileParser(file);
        String context = "1\u00802\u01513";
        //when:
        fileParser.saveContent(context);
        //then:
        assertEquals(context, fileParser.getContent());
    }

    @Test
    void validateSaveContent() throws IOException {
        //given:
        String fileName = "FileToSaveContent";
        File file = getFileByFileName(fileName);
        FileParser fileParser = new FileParser(file);
        //expect:
        assertThrows(NullPointerException.class, () -> fileParser.saveContent(null));
    }

    private static File getFileByFileName(String fileName) throws IOException {
        URL url = CLASS_LOADER.getResource(fileName);
        if (url != null) {
            return new File(url.getFile());
        } else {
            throw new FileNotFoundException();
        }
    }
}