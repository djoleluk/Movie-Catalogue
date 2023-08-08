package placeholders;

public class Movie {
    private final String imgPath;
    private final String title;
    private final String genre;
    private final String descPath;

    public Movie(String imgPath, String title, String genre, String descPath) {
        this.imgPath = imgPath;
        this.title = title;
        this.genre = genre;
        this.descPath = descPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescPath() {
        return descPath;
    }
}
