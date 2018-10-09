package mandelbrot;

/**
 * Fairly simple object that is used within HelpFrame to create glossary items. Essentially, the
 * object allows for two related strings to be stored together and accessed systematically.
 */

public class InfoItem implements Comparable<InfoItem> {

    /**Title and explanation of the 'item'*/
    private String title;
    private String explanation;

    /**Constructor that passes in the input strings*/
    public InfoItem(String title, String explanation) {

        this.title = title;
        this.explanation = explanation;

    }

    /**Allows for InfoItem to be sorted into order via the Comparable interface*/
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
