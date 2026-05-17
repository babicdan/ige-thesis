module babicdan.thesis {
    requires javafx.controls;
    requires javafx.fxml;

    opens babicdan.thesis.ui to javafx.fxml;
    exports babicdan.thesis.ui;
}