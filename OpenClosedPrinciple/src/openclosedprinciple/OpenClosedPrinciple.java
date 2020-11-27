package openclosedprinciple;

import java.io.IOException;
import java.util.List;

public class OpenClosedPrinciple {

    public static void main(String[] args) throws IOException {
        getAndWriteUolData();
        getAndWriteEstadaoData();
        getAndWritePelandoData();
    }

    public static void getAndWriteUolData() throws IOException {
        SimpleWebsiteExtractor uol = new SimpleWebsiteExtractor("https://www.uol.com.br/");
        uol.addField("Titulo", "h2.titulo", null, null, "text");
        uol.addField("Link", "h2.titulo", "a", null, "href");
        FileManagerCSV output = new FileManagerCSV("uol.csv", uol.scrapy(), uol.listFieldsNames(), ';');
        output.write();
    }

    public static void getAndWriteEstadaoData() throws IOException {
        SimpleWebsiteExtractor estadao = new SimpleWebsiteExtractor("https://www.estadao.com.br/");
        estadao.addField("Titulo", "h3.title", "a", null, "text");
        estadao.addField("Link", "h3.title", "a", null, "href");
        FileManagerCSV output = new FileManagerCSV("estadao.csv", estadao.scrapy(), estadao.listFieldsNames(), ';');
        output.write();
    }

    public static void getAndWritePelandoData() throws IOException {
        SimpleWebsiteExtractor pelando = new SimpleWebsiteExtractor("https://www.pelando.com.br/");
        pelando.addField("Produto", "a.thread-title--list", null, null, "title");
        pelando.addField("Link", "a.thread-title--list", null, null, "href");
        pelando.addField("Autor", "span.thread-username", null, null, "text");
        System.out.println(pelando.scrapy().toString());
        FileManagerCSV output = new FileManagerCSV("pelando.csv", pelando.scrapy(), pelando.listFieldsNames(), ';');
        output.write();
    }
}
