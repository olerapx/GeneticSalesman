package models;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

public class TableRow
{
	public SimpleStringProperty title;
	public List<SimpleStringProperty> cells;
		
	public TableRow()
	{
		title = new SimpleStringProperty("");
		cells = new ArrayList<>();	
	}
}
