module com.tv.tvschedule {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tv.tvschedule to javafx.fxml;
    exports com.tv.tvschedule;
}