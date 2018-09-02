import java.util.List;

public class Report {
     private String description;
     private String entityType;
     private String format;
     private String fileName;
     private List<Field> fields;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Report{");
        sb.append("description='").append(description).append('\'');
        sb.append(", entityType='").append(entityType).append('\'');
        sb.append(", format='").append(format).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
