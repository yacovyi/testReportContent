import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class ReportValidation {
    //Alejandro[.\s\n]*Tipo[\s\n]*Casa[.\s\n]
    //private static final String regex = "[.\\s\\n]*%s[.\\s\\n]*%s[\\s\\n]*%s[.\\s\\n]*%s[.\\s\\n]*";
    private static final String regex = ".*%s.*%s.*%s.*%s.*";
    private Report report;
    private String exportFolder;
    private String reportsFolder;
    private String content;

    ReportValidation(Report report, String reportsFolder,String exportFolder){
        this.report = report;
        this.reportsFolder = reportsFolder;
        this.exportFolder = exportFolder;
    }
    public void validateReport() throws TikaException, IOException, SAXException {
        String content = getFileContent();
        this.content = content;
        saveContent(content);
        report.getFields().forEach(field -> {
            validateField(field,content);
        });
    }

    public String getContent() {
        return content;
    }

    private void validateField(Field field, String content)
    {
        String pre = (field.getPre() == null) ? "" : field.getPre();
        String post = (field.getPost() == null) ? "" : field.getPost();
        String label = (field.getLabel() == null) ? "" : field.getLabel();
        String value = (field.getValue() == null) ? "" : field.getValue();


        if (field.getReplaceLabelValueOrder()){
            String tmp = label;
            label = value;
            value = tmp;
        }
        if (field.getReplacePrePostOrder()){
            String tmp = pre;
            pre = post;
            post = tmp;
        }
        String regexLabelValue = String.format(regex,Utils.forRegex(pre),
                                                     Utils.forRegex(label),
                                                     Utils.forRegex(value),
                                                     Utils.forRegex(post));
        //Pattern.DOTALL  When this mode is enabled, line terminators (\n or \r or \r\n) are treated as literal. The dot (.) in regex expression can match them as well .
        // By default the line terminators are the only ones dot doesn't match.
        //Pattern.UNICODE_CHARACTER_CLASS for matching spansh charcters
        Pattern fieldPattern = Pattern.compile(regexLabelValue, Pattern.DOTALL| Pattern.UNICODE_CHARACTER_CLASS);

        //set found and pattern
        field.setFound(fieldPattern.matcher(content).find());
        field.setPattern(regexLabelValue);
    }
    private void saveContent(String content) throws IOException {
        String exportToFileName =  String.format("%s_%s.%s",report.getEntityType(), report.getFormat(),"txt");
        FileUtils.writeStringToFile(Paths.get(exportFolder, exportToFileName).toFile(), content);

    }
    private String getFileContent() throws TikaException, IOException, SAXException {
        String fileName = Paths.get(reportsFolder, report.getFileName()).toString();
        File file =  Utils.getFile(fileName, this.getClass());
        return Utils.getFileContent(file);
    }

}

