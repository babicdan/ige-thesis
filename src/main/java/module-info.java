module babicdan.thesis {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens babicdan.thesis to javafx.fxml;
    exports babicdan.thesis;
    opens babicdan.thesis.ui to javafx.fxml;
    exports babicdan.thesis.ui;
}