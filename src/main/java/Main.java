import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfReader;
import com.sun.xml.internal.ws.policy.AssertionSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.jupiter.api.Assertions;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    //Alejandro[.\s\n]*Tipo[\s\n]*Casa[.\s\n]
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String folderPath = "reports";
    private static final String exportPath = "export";
    private static final String generatedReportsFolderPath = "generatedReports";
    private static int num = 0;
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        objectMapper.configure( JsonParser.Feature.ALLOW_COMMENTS, true );
        //main.checkFields();
        main.validateReports();
     }

     private void validateReports() throws IOException, TikaException, SAXException {
        InputStream is = Utils.getFileWithUtil("testMetaData.json",this.getClass());
         List<Report> reports = objectMapper.readValue(is, new TypeReference<List<Report>>(){});
         reports.forEach(report -> {
             ReportValidation reportValidation = new ReportValidation(report,generatedReportsFolderPath, exportPath);
             try {
                 reportValidation.validateReport();
                 report.getFields().forEach(field -> {
                     Assertions.assertTrue(field.isValid(),String.format("%s\n%s",report,field.toString()));
                 });

             } catch (IOException | TikaException | SAXException e) {
                 throw new RuntimeException(e);
             }

         });
     }
}
