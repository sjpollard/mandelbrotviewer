package mandelbrot;

public class InfoItem implements Comparable<InfoItem> {

    private String title;
    private String explanation;

    public InfoItem(String title, String explanation) {

        this.title = title;
        this.explanation = explanation;

    }


    @Override
    public int compareTo(InfoItem item) {
        return -item.title.compareToIgnoreCase(this.title);
    }

    public String getBoldTitle() {

        return "<b>" + this.title + ":</b>";
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

}
