module com.uos.schoollearningapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.uos.schoollearningapplication to javafx.fxml;
    exports com.uos.schoollearningapplication;
}