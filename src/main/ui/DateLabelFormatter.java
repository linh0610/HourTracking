package ui;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//a date formatter for String and Date Object
class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    // EFFECTS: Converts a String to a Date object
    @Override
    public Object stringToValue(String text) {
        return null;
    }

    // EFFECTS: Converts a Date object to a String
    @Override
    public String valueToString(Object value) {
        if (value instanceof Date) {
            return dateFormatter.format((Date) value);
        } else if (value instanceof Calendar) {
            return dateFormatter.format(((Calendar) value).getTime());
        }
        return "";
    }
}