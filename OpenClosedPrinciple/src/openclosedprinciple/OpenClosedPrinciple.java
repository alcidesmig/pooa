package openclosedprinciple;

import java.io.IOException;
import java.util.List;

public class OpenClosedPrinciple {

    public static void main(String[] args) throws IOException {
        SimpleWebsiteExtractor uol = new SimpleWebsiteExtractor("https://www.uol.com.br/");
        uol.addField("Titulo", "h2.titulo", null, null, "text");
        uol.addField("Link", "h2.titulo", "a", null, "href");
        FileManagerCSV output = new FileManagerCSV("test.csv", uol.scrapy(), uol.listFieldsNames(), ';');
        output.write();
    }
}
