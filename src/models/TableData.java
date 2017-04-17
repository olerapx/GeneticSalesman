package models;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TableData 
{
	private List<String> titles;
	private ObservableList<List<SimpleStringProperty>> cells;
	
	private SimpleIntegerProperty citiesCount;
	private SimpleIntegerProperty lastCityNumber;
	
	public TableData()
	{
		titles = new ArrayList<>();
		cells = FXCollections.observableArrayList();	
		
		citiesCount = new SimpleIntegerProperty(0);
		lastCityNumber = new SimpleIntegerProperty(0);
	}
}
