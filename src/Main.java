import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import placeholders.Movie;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private Scene selectionScene;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final List<Movie> movies = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // Selection screen
        BorderPane selectionRoot = new BorderPane();
        selectionRoot.setId("root");

        // Top Text component
        Text titleText = new Text("All Time Top 8 Classics");
        titleText.setId("titleText");
        HBox topHBox = new HBox(titleText);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(10));
        selectionRoot.setTop(topHBox);

        // HBox with search button
        Button visitButton = new Button("Visit IMDb");
        visitButton.setId("visitButton");
        HBox searchHBox = new HBox(visitButton);
        searchHBox.setId("searchBox");
        searchHBox.setAlignment(Pos.CENTER);
        searchHBox.setSpacing(10);
        searchHBox.setPadding(new Insets(10));
        selectionRoot.setBottom(searchHBox);

        BorderPane.setMargin(searchHBox, new Insets(5));

        // Add functionality to search button
        visitButton.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.imdb.com/chart/top/"));
            } catch (IOException | URISyntaxException ignored) {
                //..
            }
        });

        // GridPane for movie thumbnails
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        selectionRoot.setCenter(gridPane);

        // store movie details
        storeMovies();

        // Add 8 ImageViews with movie thumbnails
        for (int row = 0, ctr = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                ImageView movieImageView = new ImageView(new Image(Main.class.getResourceAsStream(movies.get(ctr).getImgPath())));
                movieImageView.setFitWidth(150);
                movieImageView.setFitHeight(200);
                movieImageView.setId(ctr++ + "");
                movieImageView.setOnMouseEntered(e -> {
                    movieImageView.setFitWidth(160);
                    movieImageView.setFitHeight(210);
                });
                movieImageView.setOnMouseExited(e -> {
                    movieImageView.setFitWidth(150);
                    movieImageView.setFitHeight(200);
                });
                movieImageView.setOnMouseClicked(e -> openMovieDetailsStage(primaryStage, Integer.parseInt(((ImageView)e.getSource()).getId())));
                gridPane.add(movieImageView, col, row);
            }
        }

        // Set the initial scene
        selectionScene = new Scene(selectionRoot, WIDTH, HEIGHT);
        selectionScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(selectionScene);
        primaryStage.setTitle("Movie App");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void openMovieDetailsStage(Stage primaryStage, int id) {
        Movie movie = movies.get(id);

        BorderPane detailsRoot = new BorderPane();
        detailsRoot.setId("detailsRoot");

        // Movie title
        Text titleText = new Text(movie.getTitle());
        titleText.setId("titleText");
        titleText.setWrappingWidth(300);

        // Genre txt
        Text genreText = new Text(movie.getGenre());
        genreText.setId("genreText");
        genreText.setWrappingWidth(300);

        // description txt
        Text descText = new Text("");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(movie.getDescPath())))) {
            StringBuilder desc = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                desc.append(line);
            }
            descText.setText(desc.toString());
        } catch (IOException e) {
            descText.setText("Error reading movie description.");
        }
        descText.setId("descText");
        descText.setWrappingWidth(300);

        // ImageView for displaying detailed movie information
        ImageView movieImageView = new ImageView(new Image(Main.class.getResourceAsStream(movie.getImgPath())));
        movieImageView.setFitWidth(380);
        movieImageView.setFitHeight(500);

        VBox movieImageVBox = new VBox(movieImageView);
        movieImageVBox.setAlignment(Pos.TOP_CENTER);
        movieImageVBox.setPadding(new Insets(10));
        detailsRoot.setLeft(movieImageVBox);

        BorderPane.setMargin(movieImageVBox, new Insets(20, 0, 0, 20));

        VBox movieInfoVBox = new VBox();
        movieInfoVBox.setAlignment(Pos.TOP_CENTER);
        movieInfoVBox.setPadding(new Insets(10));
        movieInfoVBox.setSpacing(10);
        movieInfoVBox.getChildren().addAll(titleText, genreText, descText);
        detailsRoot.setCenter(movieInfoVBox);

        BorderPane.setMargin(movieInfoVBox, new Insets(20, 0, 0, 0));

        // Back button to return to the previous stage
        Button backButton = new Button("Back");
        backButton.setId("backButton");
        backButton.setOnAction(e -> primaryStage.setScene(selectionScene));
        detailsRoot.setTop(backButton);

        Scene scene = new Scene(detailsRoot, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    // store 8 popular movie titles
    private void storeMovies() {
        movies.add(new Movie("the_shawshank_redemption.jpg", "The Shawshank Redemption (1994)", "R | Drama", "the_shawshank_redemption_desc"));
        movies.add(new Movie("the_godfather.jpg", "The Godfather (1972)", "R | Crime, Drama", "the_godfather_desc"));
        movies.add(new Movie("the_dark_knight.jpg", "The Dark Knight (2008)", "PG-13 | Action, Crime, Drama, Thriller", "the_dark_knight_desc"));
        movies.add(new Movie("the_godfather_part_2.jpg", "The Godfather Part II (1974)", "R | Crime, Drama", "the_godfather_part_2_desc"));
        movies.add(new Movie("twelve_angry_man.jpg", "12 Angry Men (1957)", "Approved | Crime, Drama", "twelve_angry_man_desc"));
        movies.add(new Movie("shindlers_list.jpg", "Schindler's List (1993)", "R | Biography, Drama, History", "shindlers_list_desc"));
        movies.add(new Movie("lotr_rotk.jpg", "The Lord of the Rings: The Return of the King (2003)", "PG-13 | Action, Adventure, Drama, Fantasy", "the_rotk_desc"));
        movies.add(new Movie("pulp_fiction.jpg", "Pulp Fiction (1994)", "R | Crime, Drama", "pulp_fiction_desc"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

