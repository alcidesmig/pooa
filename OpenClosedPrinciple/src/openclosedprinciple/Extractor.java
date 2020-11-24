package openclosedprinciple;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public abstract class Extractor {

    final String website;
    Connection connection;


    public Extractor(String website) {
        this.website = website;
        this.connection = Jsoup.connect(this.website);
    }
    
    public abstract <T> List<T> scrapy();
}
