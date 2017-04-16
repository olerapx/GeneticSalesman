package application;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;

public class MainWindowController
{
	@FXML private TextArea logText;
	@FXML private TableView<List<String>> table;
	
	private List<String> titles;
	private ObservableList<List<String>> data = FXCollections.observableArrayList();
	private int citiesCount = 0;
	private int lastCityNumber = 0;
	
	@FXML public void initialize()
	{       
		table.setItems(data);
		
		titles = new ArrayList<>();
		titles.add("Город");		
		table.getColumns().add(createColumn(titles.get(0), 0));
		
		for (int i=0; i<3; i++)
			addItem();
	}
	
	private void addItem()
	{
		List<String> list = new ArrayList<>();
		list.add("A" + (lastCityNumber+1));
		
		for (int i=0; i<citiesCount; i++)
			list.add("0");
		
		data.add(list);
		
		addColumn();
	}
		
	private void addColumn()
	{
		for (int i=0; i<data.size(); i++)
		{
			if (i == data.size()-1)
				data.get(i).add("M");
			else
				data.get(i).add("0");
		}
				
		titles.add("A" + (lastCityNumber+1));
		lastCityNumber++;
		citiesCount ++;
				
		table.getColumns().add(createColumn(titles.get(citiesCount), citiesCount));
	}
	
	private TableColumn<List<String>, String> createColumn(String title, final int n)
	{
		TableColumn <List<String>, String> c = new TableColumn<>(title);
		
    	c.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>()
    	{
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> p) 
			{
				 return new SimpleStringProperty((p.getValue().get(n)));
			}        		
    	});
		
		return c;
	}
}
