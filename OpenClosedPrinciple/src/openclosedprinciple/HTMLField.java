package openclosedprinciple;

public class HTMLField {

    private final String fieldName;

    private final String docSelect;
    private final String familyTagName;
    private final String familyClassName;
    private final String attr;

    public HTMLField(String fieldName, String docSelect, String familyTagName, String familyClassName, String attr) {
        this.fieldName = fieldName;
        this.docSelect = docSelect;
        this.familyTagName = familyTagName;
        this.familyClassName = familyClassName;
        this.attr = attr;
    }

    public String getDocSelect() {
        return docSelect;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFamilyTagName() {
        return familyTagName;
    }

    public String getAttr() {
        return attr;
    }

    public String getFamilyClassName() {
        return familyClassName;
    }

}
