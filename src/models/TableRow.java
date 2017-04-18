package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TableRow
{
	public SimpleStringProperty title;
	public ObservableList<SimpleStringProperty> cells;
		
	public TableRow()
	{
		title = new SimpleStringProperty("");
		cells = FXCollections.observableArrayList();	
	}
}
