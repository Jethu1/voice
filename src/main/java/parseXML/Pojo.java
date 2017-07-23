package parseXML;

/**
 * Created by jet on 2017/7/18.
 */
public class Pojo {
    String FileName;
    String role;
    String begin;
    String end;
    String text;
    String time;

    @Override
    public String toString() {
        return "Pojo{" +
                "FileName='" + FileName + '\'' +
                ", role='" + role + '\'' +
                ", begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                ", text='" + text + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
