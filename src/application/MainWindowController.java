package application;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class MainWindowController
{
	@FXML private TextArea logText;
	@FXML private TableView<List<SimpleStringProperty>> table;
	
	private TableData data;
	private List<String> titles;
	private ObservableList<List<SimpleStringProperty>> data = FXCollections.observableArrayList();
	private int citiesCount = 0;
	private int lastCityNumber = 0;
	
	private ContextMenu cellContext;
	
	public MainWindowController()
	{
		cellContext = new ContextMenu();
		MenuItem remove = new MenuItem("Убрать город на вертикали");
		MenuItem toggle = new MenuItem("Сделать (не)активным");
		
		remove.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				removeAt(table.getFocusModel().getFocusedCell().getColumn());
			}
		});
		
		toggle.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				
			}
		});
		
		cellContext.getItems().addAll(remove, toggle);
	}
	
	@FXML public void initialize()
	{       
		table.setItems(data);
		table.setContextMenu(cellContext);
		table.getSelectionModel().setCellSelectionEnabled(true);
				
		titles = new ArrayList<>();
		titles.add("Город");		
		table.getColumns().add(createColumn(titles.get(0), 0));
		
		for (int i=0; i<3; i++)
			addItem();
	}
	
	private void addItem()
	{
		List<SimpleStringProperty> list = new ArrayList<>();
		list.add(new SimpleStringProperty("A" + (lastCityNumber+1)));
		
		for (int i=0; i<citiesCount; i++)
			list.add(new SimpleStringProperty("0"));
		
		data.add(list);
		
		addColumn();
	}
		
	private void addColumn()
	{
		for (int i=0; i<data.size(); i++)
		{
			if (i == data.size()-1)
				data.get(i).add(new SimpleStringProperty("M"));
			else
				data.get(i).add(new SimpleStringProperty("0"));
		}
				
		titles.add("A" + (lastCityNumber+1));
		lastCityNumber++;
		citiesCount ++;
				
		table.getColumns().add(createColumn(titles.get(citiesCount), citiesCount));
	}
	
	private TableColumn<List<SimpleStringProperty>, String> createColumn(String title, final int n)
	{
		TableColumn<List<SimpleStringProperty>, String> c = new TableColumn<>(title);
		
    	c.setCellValueFactory(cellData -> cellData.getValue().get(n));    	
    	c.setCellFactory(TextFieldTableCell.forTableColumn());
    	
    	c.setSortable(false);
		
		return c;
	}

	private void removeAt(int index)
	{
		if (index == 0)
			return;
		
		table.getColumns().remove(index);
	}
		
	private void tableToString()
	{
		for(List<SimpleStringProperty> list: data)
		{
			String row = "";
			for(SimpleStringProperty s: list)
				row = row + s.get() + " ";
			
			log(row);
		}
	}
	
	@FXML private void onAddClicked()
	{
		addItem();
		tableToString();
	}
	
	private void log(String text)
	{
		logText.setText(logText.getText() + "\n" + text);
	}
}
