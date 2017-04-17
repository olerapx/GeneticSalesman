package application;

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
import models.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class MainWindowController
{
	@FXML private TextArea logText;
	@FXML private TableView<TableRow> table;
	
	private ObservableList<TableRow> data;
	
	private int citiesCount = 0;
	private int lastCityNumber = 0;
	
	private ContextMenu cellContext;
	
	public MainWindowController()
	{		
		data = FXCollections.observableArrayList();
		
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
		
		TableColumn<TableRow, String> headerColumn = createColumn("Город", -1);
		headerColumn.setEditable(false);
		
		table.getColumns().add(headerColumn);
		
		for (int i=0; i<3; i++)
			addItem();
	}
	
	private void addItem()
	{
		TableRow row = new TableRow();
		row.title.set("A" + (lastCityNumber+1));
		
		for (int i=0; i<citiesCount; i++)
			row.cells.add(new SimpleStringProperty("0"));
		
		data.add(row);
		
		addColumn();
	}
		
	private void addColumn()
	{
		for (int i=0; i<data.size(); i++)
		{
			if (i == data.size()-1)
				data.get(i).cells.add(new SimpleStringProperty("M"));
			else
				data.get(i).cells.add(new SimpleStringProperty("0"));
		}
						
		table.getColumns().add(createColumn("A" + (lastCityNumber+1), citiesCount));
		
		lastCityNumber++;
		citiesCount ++;
	}
	
	private TableColumn<TableRow, String> createColumn(String title, final int n)
	{
		TableColumn<TableRow, String> c = new TableColumn<>(title);
		
    	c.setCellValueFactory(cellData -> (n == -1) ? cellData.getValue().title : cellData.getValue().cells.get(n));    	
    	c.setCellFactory(TextFieldTableCell.forTableColumn());
    	
    	c.setSortable(false);
		
		return c;
	}

	private void removeAt(int index)
	{
		if (index == 0)
			return;
		
		for (TableRow row: data)
		{
			row.cells.remove(index-1);
		}
		
		table.getColumns().remove(index);
	}
		
	private void tableToString()
	{
		for(TableRow row: data)
		{
			String res = row.title.get() + " ";
			for(SimpleStringProperty s: row.cells)
				res = res + s.get() + " ";
			
			log(res);
		}
	}
	
	private void toggleCell(int x, int y)
	{
		if (y == 0)
			return;
		
	}
	
	private boolean isCellDisabled(int x, int y)
	{
		return false;
	}
	
	@FXML private void onAddClick()
	{
		addItem();
	}
	
	@FXML private void onClearClick()
	{
		logText.clear();
	}
	
	private void log(String text)
	{
		logText.setText(logText.getText() + "\n" + text);
	}
}
