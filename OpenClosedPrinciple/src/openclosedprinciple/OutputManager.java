package openclosedprinciple;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public abstract class OutputManager {

    PrintWriter writer;
    String fileName;

    public OutputManager(String fileName) throws IOException {
        this.fileName = fileName;
        this.writer = new PrintWriter(new FileWriter(new File(fileName)));
    }
    
    /* process method to override */
    public abstract <T> List<T> process(List<T> data);
    
    /* write method to override */
    public abstract void write();

    /* clear file method to override */
    public abstract void clearFile();
}
