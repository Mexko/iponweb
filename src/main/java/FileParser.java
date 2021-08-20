import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class FileParser implements Parser {

    private static final int FIRST_UNICODE_CHAR = 0x80;
    private final File file;

    FileParser(File f) {
        if (f != null && f.exists() && f.isFile() && f.canWrite()) {
            file = f;
        } else {
            throw new IllegalArgumentException("Input file is not valid");
        }
    }

    public File getFile() {
        return file;
    }

    public String getContent() throws IOException {
        StringBuilder output = new StringBuilder();
        try(Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            int data;
            while ((data = reader.read()) > 0) {
                output.append((char) data);
            }
        }
        return output.toString();
    }

    public String getContentWithoutUnicode() throws IOException {
        StringBuilder output = new StringBuilder();
        try(Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            int data;
            while ((data = reader.read()) > 0) {
                if (data < FIRST_UNICODE_CHAR) {
                    output.append((char) data);
                }
            }
        }
        return output.toString();
    }

    public void saveContent(String content) throws IOException {
        Objects.requireNonNull(content);
        try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < content.length(); i += 1) {
                writer.write(content.charAt(i));
            }
            writer.flush();
        }
    }
}