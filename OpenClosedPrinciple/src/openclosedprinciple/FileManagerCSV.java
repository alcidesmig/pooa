package openclosedprinciple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManagerCSV extends OutputManager {

    List<List<String>> text;
    List<String> header;
    char sep;

    public FileManagerCSV(String fileName,
            List<List<String>> text,
            List<String> header,
            char sep) throws IOException {
        super(fileName);
        this.text = text;
        this.header = header;
        this.sep = sep;
    }

    @Override
    public void write() {
        writer.write(String.join(String.valueOf(sep), header));
        writer.write("\n");
        text.forEach(list -> {
            writer.write(String.join(String.valueOf(sep), list));
            writer.write("\n");
        });
    }

    @Override
    public void clearFile() {
        this.writer.close();
        try {
            this.writer = new PrintWriter(new FileWriter(new File(fileName)));
        } catch (IOException ex) {
            Logger.getLogger(FileManagerCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public <T> List<T> process(List<T> data) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
