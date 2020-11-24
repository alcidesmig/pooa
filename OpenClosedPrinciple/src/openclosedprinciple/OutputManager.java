package openclosedprinciple;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class OutputManager {

    PrintWriter writer;
    String fileName;

    public OutputManager(String fileName) throws IOException {
        this.fileName = fileName;
        this.writer = new PrintWriter(new FileWriter(new File(fileName)));
    }

    public abstract void write();

    public abstract void clearFile();
}
