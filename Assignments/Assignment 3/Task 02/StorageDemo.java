import java.io.IOException;

class ExamException extends Exception {
    ExamException(String message) { super(message); }
    ExamException(String message, Throwable cause) { super(message, cause); }
}
class SubmissionStorageException extends ExamException {
    SubmissionStorageException(String message, Throwable cause) { super(message, cause); }
}
class ExamClosedException extends ExamException {
    ExamClosedException(String message) { super(message); }
}
class NotRegisteredException extends ExamException {
    NotRegisteredException(String message) { super(message); }
}
class FileTooLargeException extends ExamException {
    FileTooLargeException(String message) { super(message); }
}
class Storage {
    void save(String fileName, long sizeKb) throws SubmissionStorageException {
        try {
            if (sizeKb > 500) throw new IOException("Disk quota exceeded");
            System.out.println("Saved " + fileName);
        } catch (IOException e) {
            throw new SubmissionStorageException("Could not store " + fileName, e);
        }
    }
}
class SubmissionService {
    private final Storage storage = new Storage();
    void submit(String file, long size, boolean registered, boolean examOpen)
            throws ExamException {
        if (!examOpen) throw new ExamClosedException("Exam window has closed");
        if (!registered) throw new NotRegisteredException("Student is not registered");
        if (size > 500) throw new FileTooLargeException("Answer file exceeds 500 KB");
        storage.save(file, size);
    }
}
public class StorageDemo {
    public static void main(String[] args) {
        Storage storage = new Storage();
        try {
            storage.save("answer01.pdf", 120);
            storage.save("answer02.pdf", 800);
        } catch (ExamException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
            e.printStackTrace(System.out);
        }
        SubmissionService service = new SubmissionService();
        try {
            service.submit("answer03.pdf", 100, true, false);
        } catch (ExamException e) {
            System.out.println("Submission rejected: " + e.getMessage());
        }
    }
}