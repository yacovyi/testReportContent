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
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Utils {
    public static String getFileContent(File f) throws IOException, TikaException, SAXException {
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

    public static String  getPdfFileContent(File f) throws IOException, TikaException, SAXException {
        PDFParser pdfParser = new PDFParser();
        BodyContentHandler bodyContentHandler = new BodyContentHandler();
        pdfParser.parse(new FileInputStream(f), bodyContentHandler, new Metadata(),new ParseContext());
        return bodyContentHandler.toString();
    }
    public static String getMSOfficeFileContent(File f) throws TikaException, SAXException, IOException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(f);
        ParseContext pcontext = new ParseContext();

        //OOXml parser
        OOXMLParser msofficeparser = new OOXMLParser ();
        msofficeparser.parse(inputstream, handler, metadata,pcontext);
        return handler.toString();
    }
    public static InputStream getFileWithUtil(String fileName, Class clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
    public static File getFile(String fileName, Class clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        //when the file name has space then the classLoader.getResource(fileName).getFile() replace space with %20
        //then we should replace again to space
        return Paths
                .get(classLoader
                .getResource(fileName)
                .getFile()
                .replaceAll("%20", " "))
                .toFile();
    }
    public static String forRegex(String aRegexFragment){
        final StringBuilder result = new StringBuilder();

        final StringCharacterIterator iterator =
                new StringCharacterIterator(aRegexFragment)
                ;
        char character =  iterator.current();
        while (character != CharacterIterator.DONE ){
      /*
       All literals need to have backslashes doubled.
      */
            if (character == '.') {
                result.append("\\.");
            }
            else if (character == '\\') {
                result.append("\\\\");
            }
            else if (character == '?') {
                result.append("\\?");
            }
            else if (character == '*') {
                result.append("\\*");
            }
            else if (character == '+') {
                result.append("\\+");
            }
            else if (character == '&') {
                result.append("\\&");
            }
            else if (character == ':') {
                result.append("\\:");
            }
            else if (character == '{') {
                result.append("\\{");
            }
            else if (character == '}') {
                result.append("\\}");
            }
            else if (character == '[') {
                result.append("\\[");
            }
            else if (character == ']') {
                result.append("\\]");
            }
            else if (character == '(') {
                result.append("\\(");
            }
            else if (character == ')') {
                result.append("\\)");
            }
            else if (character == '^') {
                result.append("\\^");
            }
            else if (character == '$') {
                result.append("\\$");
            }
            else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }
}
