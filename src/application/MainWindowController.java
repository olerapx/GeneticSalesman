package application;

import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import models.TableRow;
import util.StringUtils;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainWindowController
{
	@FXML private TextArea logText;
	@FXML private TableView<TableRow> table;
	@FXML private TextField maxDistanceText;
	
	private ObservableList<TableRow> data;
	
	private int citiesCount = 0;
	private int lastCityNumber = 0;
	
	private ContextMenu cellContext;
	
	public MainWindowController()
	{		
		data = FXCollections.observableArrayList();
		
		cellContext = new ContextMenu();
		MenuItem removeVertical = new MenuItem("Убрать город по вертикали");
		MenuItem removeHorizontal = new MenuItem("Убрать город по горизонтали");
		
		removeVertical.setOnAction(e -> removeAtVertical(table.getFocusModel().getFocusedCell().getColumn()));
		removeHorizontal.setOnAction(e -> removeAtHorizontal(table.getFocusModel().getFocusedCell().getRow()));
				
		cellContext.getItems().addAll(removeVertical, removeHorizontal);
	}
	
	@FXML public void initialize()
	{       
		table.setItems(data);
		table.setContextMenu(cellContext);
		table.getSelectionModel().setCellSelectionEnabled(true);
		
		TableColumn<TableRow, String> headerColumn = createColumn("Город", 0);
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
						
		table.getColumns().add(createColumn("A" + (lastCityNumber+1), citiesCount+1));
		
		lastCityNumber++;
		citiesCount ++;
	}
	
	private TableColumn<TableRow, String> createColumn(String title, final int n)
	{
		TableColumn<TableRow, String> c = new TableColumn<>(title);
		setCellValueFactory(c, n);
	
        c.setCellFactory(new Callback<TableColumn<TableRow, String>, TableCell<TableRow, String>>() 
        {
			@Override
			public TableCell<TableRow, String> call(TableColumn<TableRow, String> param) 
			{
				return new TextFieldTableCell<TableRow, String>()
				{
					@Override
					public void updateItem(String item, boolean empty)
					{
						super.updateItem(item, empty);
						
						setConverter(new DefaultStringConverter());
						
						if (item == null || empty)
						{
					         setText(null);
					         setGraphic(null);
					         return;
						}
						
						if (!containsWay(item))
						{
							setStyle("-fx-background-color: #E0E0E2; -fx-border-color: #94BBDA; -fx-text-fill: #000000;");
							setText(item);
							return;
						}
							
						if (!StringUtils.isNumeric(item) && !table.getColumns().get(0).equals(param))
							item = "0";

						setStyle(null);

						setText(item);
					}
				};
			}
       });
    	
    	c.setSortable(false);
		
		return c;
	}
	
	private void setCellValueFactory(TableColumn<TableRow, String> c, int n)
	{
    	c.setCellValueFactory(cellData -> 
    	{
    		if (n == 0)
    			return cellData.getValue().title;
    		else if (n < cellData.getValue().cells.size()+1)
    			return cellData.getValue().cells.get(n-1);
    		else return new SimpleStringProperty("");
    	});
	}
		
	private boolean containsWay(String str)
	{
		if (str.equalsIgnoreCase("M") || str.equalsIgnoreCase("М"))
		{
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void removeAtVertical(int index)
	{
		if (index == 0)
			return;
		
		table.getItems().remove(index-1);	
		
		for(TableRow trow: data)
			trow.cells.remove(index-1);
		
		table.getColumns().remove(index);
		
		for (int i=index; i<table.getColumns().size(); i++)
		{			
			final int n = i;	
			setCellValueFactory((TableColumn<TableRow, String>) table.getColumns().get(i), n);
		}
		citiesCount --;
	}
	
	@SuppressWarnings("unchecked")
	private void removeAtHorizontal(int index)
	{		
		table.getItems().remove(index);	
		
	for(TableRow trow: data)
			trow.cells.remove(index);
		
		table.getColumns().remove(index+1);
		
		for (int i=index+1; i<table.getColumns().size(); i++)
		{			
			final int n = i;	
			setCellValueFactory((TableColumn<TableRow, String>) table.getColumns().get(i), n);
		}
		citiesCount --;
	}
				
	@FXML private void onAddClick()
	{
		addItem();
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
		
	@FXML private void onClearClick()
	{
		logText.clear();
	}
	
	@FXML private void onRandomFillClick()
	{
		randomFill(Integer.valueOf(maxDistanceText.getText()));
	}
	
	private void randomFill(int max)
	{
		Random rand = new Random();
		for(TableRow row: data)
			for(SimpleStringProperty s: row.cells)
			{
				if(containsWay(s.get()))
					s.set(String.valueOf(rand.nextInt(max)));
			}
	}
	
	private void log(String text)
	{
		logText.setText(logText.getText() + "\n" + text);
	}
}
