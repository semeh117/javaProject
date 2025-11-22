module Semeh {
	requires java.sql;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;

	opens com.hms.application to javafx.graphics, javafx.fxml;
	opens com.hms.presentation to javafx.fxml;
	opens com.hms.domain to javafx.base;
}
