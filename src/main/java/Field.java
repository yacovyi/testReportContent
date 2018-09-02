import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Field {
    //label to shouldBeFound
    @JsonProperty(required = false)
    private String label;
    //value to shouldBeFound
    @JsonProperty(required = false)
    private String value;
    //string before the value
    @JsonProperty(required = false)
    private String pre;
    //string after the value
    @JsonProperty(required = false)
    private String post;
    //determines if the field should be found
    @JsonProperty(required = false)
    private Boolean shouldBeFound;

    @JsonProperty(required = false)
    private Boolean replaceLabelValueOrder;
    @JsonProperty(required = false)
    private Boolean replacePrePostOrder;

    @JsonIgnoreProperties
    private Boolean found;
    @JsonIgnoreProperties
    private String pattern;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPre() {
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
    @JsonIgnoreProperties
    public Boolean getShouldBeFound() {
        return shouldBeFound;
    }
    @JsonIgnoreProperties
    public void setShouldBeFound(Boolean shouldBeFound) {
        this.shouldBeFound = shouldBeFound;
    }
    public Boolean getFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }
    @JsonIgnore
    public boolean isValid(){
        //if shouldBeFound is null then check if found else
        //return true when we found what should be found or when we didn't fount what shouldn't be found.
        return (shouldBeFound == null) ? found : (shouldBeFound == found);
    }

    public Boolean getReplaceLabelValueOrder() {
        return (replaceLabelValueOrder == null) ? false : replaceLabelValueOrder;
    }

    public void setReplaceLabelValueOrder(Boolean replaceLabelValueOrder) {
        this.replaceLabelValueOrder = replaceLabelValueOrder;
    }

    public Boolean getReplacePrePostOrder() {
        return (replacePrePostOrder == null) ? false : replacePrePostOrder;
    }

    public void setReplacePrePostOrder(Boolean replacePrePostOrder) {
        this.replacePrePostOrder = replacePrePostOrder;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Field{");
        sb.append("label='").append(label).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", pre='").append(pre).append('\'');
        sb.append(", post='").append(post).append('\'');
        sb.append(", shouldBeFound=").append(shouldBeFound);
        sb.append(", replaceLabelValueOrder=").append(replaceLabelValueOrder);
        sb.append(", replacePrePostOrder=").append(replacePrePostOrder);
        sb.append(", found=").append(found);
        sb.append(", pattern='").append(pattern).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
