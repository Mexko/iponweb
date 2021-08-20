import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentFileParser extends FileParser {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock writeLock = lock.writeLock();
    private final Lock readLock = lock.readLock();

    public ConcurrentFileParser(File f) {
        super(f);
    }

    public String getContent() throws IOException {
        try {
            readLock.lock();
            return super.getContent();
        } finally {
            readLock.unlock();
        }
    }

    public String getContentWithoutUnicode() throws IOException {
        try {
            readLock.lock();
            return super.getContentWithoutUnicode();
        } finally {
            readLock.unlock();
        }
    }

    public void saveContent(String content) throws IOException {
        try {
            writeLock.lock();
            super.saveContent(content);
        } finally {
            writeLock.unlock();
        }
    }
}
