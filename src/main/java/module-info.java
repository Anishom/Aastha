module com.example.team_veritas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.team_veritas to javafx.fxml;
    exports com.example.team_veritas;
}