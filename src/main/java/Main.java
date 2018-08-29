import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String folderPath = "reports";
    private static final String exportPath = "export";
    private static int num = 0;
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        objectMapper.configure( JsonParser.Feature.ALLOW_COMMENTS, true );
        main.checkFields();

     }
    private void checkFields() throws IOException {

        JsonNode node = objectMapper.readTree(this.getFileWithUtil("fields.json"));
        try (Stream<Path> paths = Files.walk(getFile(folderPath).toPath())) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach((file) ->{
                        try {
                            String content = getFileContent(file.toFile());
                            //save content to file

                            String entityName = file.getParent().getFileName().toString();
                            String reportType = FilenameUtils.getExtension(file.getFileName().toString());
                            String exportToFileName =  String.format("%s_%s.%s",entityName, reportType,"txt");
                            FileUtils.writeStringToFile(Paths.get(exportPath, exportToFileName).toFile(), content);
                            JsonNode entityFields = node.get(entityName);
                            for (Iterator<String> it = entityFields.fieldNames(); it.hasNext(); ) {
                                String label = it.next();
                                String value = entityFields.get(label).asText();

                                String regexLabelValue = String.format("(.*)%s\\s*%s(.*)",Pattern.quote(label), Pattern.quote(value));
                                String regexValueLabel = String.format("(.*)%s\\s*%s(.*)",Pattern.quote(value), Pattern.quote(label));

                                Pattern pLabelValue = Pattern.compile(regexLabelValue, Pattern.MULTILINE);
                                Pattern pValueLabel = Pattern.compile(regexValueLabel, Pattern.MULTILINE);
                                if (!pLabelValue.matcher(content).find() && !pValueLabel.matcher(content).find()){
                                    System.out.println("############# " + (++num) + "  #################");
                                    System.out.println("\tregexLabelValue :" + regexLabelValue);
                                    System.out.println("\tregexValueLabel :" + regexValueLabel);
                                    System.out.println(String.format("\tMissing %s_%s -> %s = %s", entityName, reportType, label , value));
                                    System.out.println("##############################");
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (TikaException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                    });
        }

    }
    private InputStream getFileWithUtil(String fileName) {
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
    private File getFile(String fileName) {
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
    private String getFileContent(File f) throws IOException, TikaException, SAXException {
        String fileExtension = FilenameUtils.getExtension(f.getName());
        switch (fileExtension){
            case "pdf":
                return getPdfFileContent(f);
            case "docx":
            case "pptx":
                return getMSOfficeFileContent(f);
            default:
                return null;
        }
    }

    private String  getPdfFileContent(File f) throws IOException, TikaException, SAXException {
        PDFParser pdfParser = new PDFParser();
        BodyContentHandler bodyContentHandler = new BodyContentHandler();
        pdfParser.parse(new FileInputStream(f), bodyContentHandler, new Metadata(),new ParseContext());
        return bodyContentHandler.toString();
    }
    private String getMSOfficeFileContent(File f) throws TikaException, SAXException, IOException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(f);
        ParseContext pcontext = new ParseContext();

        //OOXml parser
        OOXMLParser msofficeparser = new OOXMLParser ();
        msofficeparser.parse(inputstream, handler, metadata,pcontext);
        return handler.toString();
    }
}
