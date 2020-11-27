package openclosedprinciple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SimpleWebsiteExtractor extends Extractor {

    private List<HTMLField> fields = new ArrayList<>();

    public SimpleWebsiteExtractor(String website) {
        super(website);
    }

    /* 
    * Add field to be extracted from website
    * - fieldName: the name to the field (to list in listFieldNames())
    * - docSelect: the component + css class to be the starting point of the search. e.g. "h1.classname"
    * - familyTagName: the parent or child tag to be searched. e.g. "a"
    *                   if familyTagName == null then the attr field will be extracted from docSelect tag
    * - familyClassName: the parent or child css class name to be searched. e.g. "classname"
    *                   if familyClassName == null then the attr field will be extracted from the first encountered parent/child tag {docSelect if familyTagName == null else familyTagName}
    * - attr: the attribute to be extracted. Can be "text" for the simple text or an attribute of the tag. e.g. "href" if tag == "a".
    *
    * Examples: 
    *   1. object.addField("Title", "h2.title", null, null, "text");
    *   2. object.addField("Link", "h5", "a", "link-class", "href");
    * */
    public void addField(String fieldName, String docSelect, String familyTagName, String familyClassName, String attr) {
        if (attr == null) {
            attr = "text";
        }
        fields.add(new HTMLField(fieldName, docSelect, familyTagName, familyClassName, attr));
    }

    /*  List fields that can be extracted from website */
    public List<String> listFieldsNames() {
        List<String> fieldNames = new ArrayList<>();
        for (HTMLField field : fields) {
            fieldNames.add(field.getFieldName());
        }
        return fieldNames;
    }

    /*
    * Implements scrapy function
    * Logic: for each field if parent/child != null go up searching until find and go down searching until find
    *                       else return attr of the field
    * */
    @Override
    public List<List<String>> scrapy() {
        try {
            List<List<String>> result = new ArrayList<>();
            // for each field
            for (HTMLField field : fields) {
                List<String> currentField = new ArrayList<>();
                Document doc = (Document) connection.get();
                Elements current = doc.select(field.getDocSelect());
                // System.out.println(current.toString());
                // if parent/child == null get the attribute of the docSelect tag
                if (field.getFamilyTagName() == null) {
                    current.forEach(element -> {
                        if (field.getAttr().equals("text")) {
                            currentField.add(element.text());
                        } else {
                            currentField.add(element.attr(field.getAttr()));
                        }
                    });
                } else {
                    current.forEach((var element) -> {
                        Element found = null;
                        Element parent = element.parent();
                        // search for the tag as a parent until find or pass all the tree
                        while (parent != null && !parent.tagName().equals(field.getFamilyTagName())
                                && (!parent.classNames().contains(field.getFamilyClassName())
                                || field.getFamilyClassName() == null)) {
                            parent = parent.parent();
                        }
                        // search for the tag as a child until find or pass all the tree
                        if (parent == null || (!parent.tagName().equals(field.getFamilyTagName())
                                && (!parent.classNames().contains(field.getFamilyClassName())))) {
                            Elements children = element.children();
                            for (Element child : children) {
                                if (child.tagName().equals(field.getFamilyTagName())
                                        && (child.classNames().contains(field.getFamilyClassName())
                                        || field.getFamilyClassName() == null)) {
                                    found = child;
                                    break;
                                }
                            }
                        } else {
                            found = parent;
                        }
                        if (found == null) {
                            currentField.add("NULL");
                        } else {
                            if (field.getAttr().equals("text")) {
                                currentField.add(found.text());
                            } else {
                                currentField.add(found.attr(field.getAttr()));
                            }
                        }
                    });
                }
                result.add(currentField);
            }

            // convert the data to the default format (teacher)
            List<List<String>> toReturn = new ArrayList<>();
            for (int i = 0; i < result.get(0).size(); i++) {
                List<String> aux = new ArrayList<>();
                for (int j = 0; j < result.size(); j++) {
                    System.out.println(result.toString());
                    aux.add(result.get(j).get(i));
                }
                toReturn.add(aux);
            }
            return toReturn;
        } catch (IOException e) {
            return null;
        }
    }
}
