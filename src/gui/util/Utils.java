package gui.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Consumer;

import application.Program;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Utils {

    public static Stage currentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Integer tryParseToInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double tryParseToDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static synchronized <T> void loadView(Object mainClass, String viewPath, Consumer<T> action) {
        FXMLLoader loader = new FXMLLoader(mainClass.getClass().getResource(viewPath));

        try {
            VBox vBox = loader.load();

            VBox mainVBox = (VBox) ((ScrollPane) Program.getMainScene().getRoot()).getContent();
            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(vBox.getChildren());

            T controller = loader.getController();
            action.accept(controller);

        } catch (IOException e) {
            Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
        }
    }

    public static <T> void formatDateTableColumn(TableColumn<T, Date> column, String dateFormat) {
        column.setCellFactory(param -> new TableCell<T, Date>() {

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            @Override
            protected void updateItem(Date date, boolean empty) {
                if (empty)
                    setText(null);
                else
                    setText(sdf.format(date));
            }
        });
    }

    public static <T> void formatDoubleTableColumn(TableColumn<T, Double> column) {
        column.setCellFactory(param -> new TableCell<T, Double>() {

            @Override
            protected void updateItem(Double value, boolean empty) {
                if (empty)
                    setText(null);
                else
                    setText(String.format("%.2f", value));
            }
        });
    }

    public static void formatDatePicker(DatePicker datePicker, String format) {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
            {
                datePicker.setPromptText(format.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    public static LocalDate convertDateToLocalDate(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        LocalDate lDate = null;
        try {
            Date nDate = sdf.parse(date.toString());
            lDate = LocalDate.ofInstant(nDate.toInstant(), ZoneId.systemDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lDate;
    }

    public static Date convertLocalDateToDate(LocalDate date) {
        Instant instant = Instant.from(date.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }

}
