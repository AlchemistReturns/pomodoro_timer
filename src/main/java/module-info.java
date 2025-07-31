module com.abrar.pomodoro_timer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.abrar.pomodoro_timer to javafx.fxml;
    exports com.abrar.pomodoro_timer;
}