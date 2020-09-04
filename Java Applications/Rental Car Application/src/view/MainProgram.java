package view;

import java.io.File;
import java.util.ArrayList;

import org.hsqldb.util.DatabaseManagerSwing;

import controller.DataController;
import controller.ImportExportController;
import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.RentalRecord;
import model.Van;
import model.Vehicle;
import model.VehicleStatus;
import util.DateTime;
// beans - for property management
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
// Events
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


// Controls
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.*;
// Layouts
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

// Scenes
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class MainProgram extends Application {

	private Stage primaryStage;
	private Scene homePageScene, detailPageScene;
	private static DataController DATA_LINK;	

	public MainProgram() {
		try {
			this.DATA_LINK = new DataController();
		} catch (Exception e) {
			CustomExceptionBox.displayCustomException(e);
		}
	}
	//private ObservableList<Vehicle> vehiclesList ;// FXCollections.observableArrayList(dc.getVehicles());

	public static void main(String[] args) {
		//		(new controller.ThriftyRentSystem()).runApplication();
		try {
			//DatabaseManagerSwing.main(new String[] {"--url", "jdbc:hsqldb:file:database/thriftyrentdb"});
			Application.launch(args);
			
		}
		catch( Exception e)
		{
			CustomExceptionBox.displayCustomException(e);
		}
	}

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		try {
			this.setPrimaryStage(primaryStage); 
			this.getPrimaryStage().setMinWidth(1200);
			this.getPrimaryStage().setMinHeight(800);
			this.createHomePage(FXCollections.observableArrayList(MainProgram.getDATA_LINK().getVehicles()));
			this.getHomePageScene().getStylesheets().add("style.css");
			this.primaryStage.setOnCloseRequest(e -> {
				e.consume();
				this.closeProgram();
			});

			primaryStage.setTitle("ThriftRentSystem"); // Set the stage title
			primaryStage.setScene(homePageScene); // Place the scene in the stage
			primaryStage.show(); // Display the stage

		} catch (Exception e) {
			CustomExceptionBox.displayCustomException(e);
		}

	}

	public void createHomePage(ObservableList<Vehicle> vehiclesList) {	
		try{
			VBox topMenu = menuFunctionality();
			VBox leftVbox = filterFunctionality();
			ScrollPane carDetails = carDetailFunctionality(vehiclesList); 
			BorderPane borderPane = new BorderPane();
			Scene scene = new Scene(borderPane, 1200, 800, Color.WHITE);

			// Set Content of Home Screen
			borderPane.setTop(topMenu);
			borderPane.setLeft(leftVbox);
			borderPane.setCenter(carDetails);

			this.setHomePageScene(scene);
			this.getPrimaryStage().setTitle("ThriftRentSystem"); // Set the stage title
			this.getPrimaryStage().setScene(homePageScene); // Place the scene in the stage
			this.getPrimaryStage().show(); // Display the stage
		}
		catch(Exception e) {
			CustomExceptionBox.displayCustomException(e);
		}
	}

	public VBox menuFunctionality() {

		VBox topVBox = new VBox();
		HBox topHBox = new HBox();
		topHBox.setMinHeight(120);

		// Menu bar
		Menu fileMenu = new Menu("File");
		Menu quit = new Menu("Quit");
		MenuItem importData = new MenuItem("Import Data...");
		MenuItem exportData = new MenuItem("Export Data...");
		MenuItem close = new MenuItem("Close Program");
		MenuBar menu = new MenuBar();
		fileMenu.getItems().add(importData);
		fileMenu.getItems().add(exportData);
		quit.getItems().add(close);

		// Logo image
		String imagePath = "images/logo.jpg";
		File imageFile = new File(imagePath);
		Image image = new Image(imageFile.toURI().toString());
		ImageView logo = new ImageView(image);
		logo.setFitHeight(120);
		logo.setFitWidth(500);	
		try{

			// Allow user to choose directory
			Alert a1 = new Alert(Alert.AlertType.ERROR); 
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setInitialDirectory(new File("src"));
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("src"));
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

			// Import data into database from a file
			importData.setOnAction(e -> {
				File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
				if(selectedFile == null){
					a1.setHeaderText("No File Selected");
					a1.showAndWait();
				}else{
					Boolean result;
					try {
						result = ImportExportController.importData(selectedFile.getAbsolutePath());
						if(result) {
							ObservableList<model.Vehicle> test = FXCollections.observableArrayList(MainProgram.getDATA_LINK().getVehicles());

							Alert a2 = new Alert(Alert.AlertType.INFORMATION);
							a2.setHeaderText("Data Imported Successfully");
							a2.showAndWait();

							this.createHomePage(test);
						}
						else {
							a1.setHeaderText("File not imported : Something Went Wrong!!!");
							a1.showAndWait();
						}
					} catch (Exception e1) {
						CustomExceptionBox.displayCustomException(e1);
					}
				}
			}
					);

			// Export data to a text file
			exportData.setOnAction(e -> {
				File selectDirectory = directoryChooser.showDialog(this.primaryStage);
				if(selectDirectory == null){
					//No Directory selected
				}else{
					new ImportExportController();
					//System.out.println(selectedDirectory.getAbsolutePath());
					Boolean result;
					try {
						result = ImportExportController.exportData(selectDirectory.getAbsolutePath());

						if(result) {
							Alert a2 = new Alert(Alert.AlertType.INFORMATION);
							a2.setHeaderText("Data Exported Successfully");
							a2.showAndWait();
						}
						else {
							a1.setHeaderText("File not exported : Something Went Wrong!!!");
							a1.showAndWait();
						}
					} catch (Exception e1) {
						CustomExceptionBox.displayCustomException(e1);
					}
				} 
			});

			close.setOnAction(e -> this.closeProgram());

			// Add to Layout
			menu.getMenus().addAll(fileMenu, quit);
			topHBox.setStyle("-fx-background-color: linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%); ");
			topHBox.getChildren().addAll(logo);
			topVBox.getChildren().addAll(menu,topHBox);
		}
		catch(Exception e)
		{
			CustomExceptionBox.displayCustomException(e);
		}
		return topVBox;
	}

	// Left Panel for Filter Functionalities
	public VBox filterFunctionality() {
		VBox vbox = new VBox(40);
		VBox innerOne = new VBox(10);
		VBox innerTwo = new VBox(10);		
		Separator separator = new Separator();

		Label filter = new Label("Filters");
		filter.setStyle("-fx-text-fill:#FFFFFF; -fx-font-size:20px; -fx-font-weight:bold;");
		Label filterVehicle = new Label("Vehicle Type");
		filterVehicle.setStyle("-fx-text-fill:#FFFFFF; -fx-font-size:15px; -fx-font-weight:bold;");
		Label filterSeat = new Label("Seat");
		filterSeat.setStyle("-fx-text-fill:#FFFFFF; -fx-font-size:15px; -fx-font-weight:bold;");
		Label filterStatus = new Label("Status");
		filterStatus.setStyle("-fx-text-fill:#FFFFFF; -fx-font-size:15px; -fx-font-weight:bold;");
		Label filterMake = new Label("Make");
		filterMake.setStyle("-fx-text-fill:#FFFFFF; -fx-font-size:15px; -fx-font-weight:bold;");

		ComboBox<String> vehicleChoice = new ComboBox();		
		ComboBox<String> seatChoice = new ComboBox<>();
		ComboBox<String> statusChoice = new ComboBox<>();
		ComboBox<String> makeChoice = new ComboBox<>();

		Button applyFilter = new Button("Apply Filter");	
		applyFilter.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
		Button addCar = new Button("Add Car");
		addCar.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
		Button addVan =  new Button("Add Van");
		addVan.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
		try {
			vehicleChoice.getItems().addAll("All","Car", "Van");
			seatChoice.getItems().addAll("All", "5", "7", "15");
			ArrayList<String> makes = MainProgram.getDATA_LINK().getUniqueMake();
			statusChoice.getItems().addAll("All","Available", "Rented", "UnderMaintenance");		
			makeChoice.getItems().addAll("All");
			makes.forEach(item -> {
				makeChoice.getItems().addAll(item);
			});	

			vehicleChoice.setPromptText("Choose a type of Vehicle");
			seatChoice.setPromptText("Choose number of Seats");
			statusChoice.setPromptText("Choose current status of Vehicle");
			makeChoice.setPromptText("Choose a make of Vehicle");

			vehicleChoice.getSelectionModel().selectFirst();
			seatChoice.getSelectionModel().selectFirst();
			statusChoice.getSelectionModel().selectFirst();
			makeChoice.getSelectionModel().selectFirst();

			applyFilter.setOnAction(e ->{
				ArrayList<Vehicle> vehicles;
				try {
					vehicles = MainProgram.getDATA_LINK().getVehicles();
					ArrayList<Vehicle> filteredList = new ArrayList<>();

					String vehicleType = vehicleChoice.getValue();
					String seatType = seatChoice.getValue();
					String statusType = statusChoice.getValue();
					String makeType = makeChoice.getValue();
					filteredList = vehicles;
					if(!vehicleType.equalsIgnoreCase("all"))
						filteredList = this.filterByType(vehicles, vehicleType);

					if(filteredList.size() != 0 && !seatType.equalsIgnoreCase("all"))
						filteredList = this.filterBySeat(filteredList, seatType);

					if(filteredList.size() != 0 && !statusType.equalsIgnoreCase("all"))
						filteredList = this.filterByStatus(filteredList, statusType);

					if(filteredList.size() != 0 && !makeType.equalsIgnoreCase("all"))
						filteredList = this.filterByMake(filteredList, makeType);

					this.createHomePage(FXCollections.observableArrayList(filteredList));

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			
			Button clearFilter = new Button("Clear Filter");
			clearFilter.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
			clearFilter.setOnAction(e ->{
				try {
					this.createHomePage(FXCollections.observableArrayList(MainProgram.getDATA_LINK().getVehicles()));
				} catch (Exception e1) {
					CustomExceptionBox.displayCustomException(e1);
				}
			});

			// Create a new car
			addCar.setOnAction(e ->{
				new AddCarView().addVehicleDisplay();
				try {
					this.createHomePage(FXCollections.observableArrayList(MainProgram.getDATA_LINK().getVehicles()));
				} catch (Exception e1) {
					CustomExceptionBox.displayCustomException(e1);
				}
			});

			// Create a new Van
			addVan.setOnAction(e ->{
				new AddVanView().addVehicleDisplay();
				try {
					this.createHomePage(FXCollections.observableArrayList(MainProgram.getDATA_LINK().getVehicles()));
				} catch (Exception e1) {
					CustomExceptionBox.displayCustomException(e1);
				}
			});
			addCar.setMinSize(20, 10);

			// Add to layout
			innerOne.getChildren().addAll(
					filter,
					filterVehicle, 
					vehicleChoice,
					filterSeat, seatChoice,
					filterStatus, statusChoice,
					filterMake, makeChoice, 
					applyFilter, clearFilter
					);

			innerTwo.getChildren().addAll(
					addCar,
					addVan
					);

			vbox.getChildren().addAll(innerOne, innerTwo);
			vbox.getChildren().add(1, separator);
			vbox.setPadding(new Insets(20,20,20,20));
			vbox.setStyle("-fx-background-color: linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%); ");
		}catch(Exception ex)
		{
			CustomExceptionBox.displayCustomException(ex);
		}
		return vbox;
	}

	// Filter Vehicle objects by Type
	public ArrayList<Vehicle> filterByType(ArrayList<Vehicle> vehicles, String vehicleType){
		ArrayList<Vehicle> filteredList = new ArrayList<>();	
		for(Vehicle v : vehicles) {
			if(v.getVehicleType().equalsIgnoreCase(vehicleType))
				filteredList.add(v);
		}
		return filteredList;
	}

	// Filter Vehicle objects by Seat
	public ArrayList<Vehicle> filterBySeat(ArrayList<Vehicle> vehicles, String seatType){
		ArrayList<Vehicle> filteredList = new ArrayList<>();
		for(Vehicle v : vehicles) {
			if(v.getNoOfSeats()  == Integer.parseInt(seatType))
				filteredList.add(v);					
		}
		return filteredList;
	}

	// Filter Vehicle objects by Status
	public ArrayList<Vehicle> filterByStatus(ArrayList<Vehicle> vehicles, String statusType){
		ArrayList<Vehicle> filteredList = new ArrayList<>();
		for(Vehicle v : vehicles) {
			if(v.getVehicleStatus().toString().equalsIgnoreCase(statusType))
				filteredList.add(v);					
		}
		return filteredList;
	}

	// Filter Vehicle objects by Make
	public ArrayList<Vehicle> filterByMake(ArrayList<Vehicle> vehicles, String makeType){
		ArrayList<Vehicle> filteredList = new ArrayList<>();
		for(Vehicle v : vehicles) {
			if(v.getMake().equalsIgnoreCase(makeType))
				filteredList.add(v);					
		}
		return filteredList;
	}

	// Home page car details Functionality
	public ScrollPane carDetailFunctionality(ObservableList<Vehicle> vehicles) {
		ScrollPane centreScrollPane = new ScrollPane();
		VBox vehicleVbox = new VBox(10);

		centreScrollPane.setStyle("-fx-background-color: #000000;");
		vehicleVbox.setPadding(new Insets(10,10,10,10));

		// Generate view for each vehicle in database
		for(int i = 0; i < vehicles.size() ; i++)
		{
			vehicleVbox.getChildren().add(generateVehicleView(vehicles.get(i)));
		}

		// Add to Layout
		centreScrollPane.setContent(vehicleVbox);	  
		centreScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		centreScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		centreScrollPane.setFitToWidth(true);
		return centreScrollPane;
	}

	// Home page view for each Vehicle
	public GridPane generateVehicleView(Vehicle vehicle) {
		HBox vehicleHbox = new HBox();
		GridPane imageGrid = new GridPane();
		GridPane vehicleGrid = new GridPane();
		GridPane bookGrid = new GridPane();
		String imgPath;
		File imageFile;
		Image image;

		imageGrid.setPadding(new Insets(10,10,10,10));
		imageGrid.setHgap(30);
		imageGrid.setVgap(5);
		vehicleHbox.setPadding(new Insets(10,10,10,10));
		vehicleHbox.setAlignment(Pos.CENTER);
		vehicleGrid.setPadding(new Insets(10, 10, 10, 10)); 
		vehicleGrid.setVgap(5);
		vehicleGrid.setHgap(10);
		vehicleGrid.setAlignment(Pos.CENTER);
		bookGrid.setPadding(new Insets(10,10,10,10));
		bookGrid.setVgap(5);
		bookGrid.setHgap(20);
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setHgrow( Priority.ALWAYS );
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setHgrow( Priority.ALWAYS );
		vehicleGrid.getColumnConstraints().addAll( new ColumnConstraints( 80 ), c1, new ColumnConstraints( 100 ), c2 );
		vehicleGrid.setStyle("-fx-background-color: rgba(186, 241, 252, 0.5); ");	

		// Set image of the car				
		if(vehicle.getImagePath() == null) {
			imgPath = "images/noimage.jpg"; // Image path doesn't exists then set default image 
		} else {	
			imgPath = "images/" + vehicle.getImagePath();
		}
		imageFile = new File(imgPath);
		image = new Image(imageFile.toURI().toString());
		ImageView carImage = new ImageView(image);
		carImage.setFitHeight(150);
		carImage.setFitWidth(250);

		// Set Vehicle details
		Text makeModel = new Text(vehicle.getMake() + " " + vehicle.getModel());
		makeModel.setStyle("-fx-font-size:25px; -fx-font-weight:300;");

		Label yearLabel = new Label("Make Year:");
		Label seatLabel = new Label("No. Of Seats:");
		Label statusLabel = new Label("Status:");
		Label vehicleType = new Label("VehicleType:");
		Text type = new Text(vehicle.getVehicleType());
		Text year = new Text(Integer.toString(vehicle.getYear()));
		Text seat = new Text(Integer.toString(vehicle.getNoOfSeats()));
		Text status= new Text(vehicle.getVehicleStatus().toString());
		Text lastMaintenanceDate;

		if(vehicle instanceof Van) {
			lastMaintenanceDate = new Text("Last Maintenance Date	" + ((Van)vehicle).getLastMaintenanceDate().toString());
			vehicleGrid.add(lastMaintenanceDate, 2 , 3);
		}

		// Set book panel
		Button book = new Button("Book Now");
		book.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
		Button showDetails = new Button("Show Details");
		showDetails.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
		
		showDetails.setTooltip(new Tooltip("Click here to see vehicle details"));

		// Disable book button according to the change in Vehicle Status
		if(vehicle.getVehicleStatus() == VehicleStatus.available) {
			book.setDisable(false);
			book.setTooltip(new Tooltip("Available to book"));
		} else {
			book.setTooltip(new Tooltip("Currently rented or undermaintenace"));
			book.setDisable(true);
		}

		// Button actions
		showDetails.setOnAction(e -> {
			this.setDetailPageScene(createDetailPage(vehicle));
			this.getPrimaryStage().setScene(detailPageScene); 
		});        
		book.setOnAction(e -> {
			new BookDialogBoxView().bookingDisplay(vehicle);
			try {
				this.createHomePage(FXCollections.observableArrayList(MainProgram.getDATA_LINK().getVehicles()));
			} catch (Exception e1) {
				CustomExceptionBox.displayCustomException(e1);
			}
		});

		// Add to Layout
		vehicleGrid.add(carImage, 0, 0, 1, 4);
		vehicleGrid.add(makeModel, 2, 0);
		vehicleGrid.add(yearLabel, 2, 1);
		vehicleGrid.add(year, 3, 1);
		vehicleGrid.add(seatLabel, 2, 2);
		vehicleGrid.add(seat, 3, 2);
		vehicleGrid.add(statusLabel, 4, 1);
		vehicleGrid.add(status, 5, 1);
		vehicleGrid.add(vehicleType, 4, 2);
		vehicleGrid.add(type, 5, 2);
		vehicleGrid.add(book, 6, 1);
		vehicleGrid.add(showDetails, 6, 2);        

		return vehicleGrid;
	}

	// Create Detailed page for a vehicle
	public Scene createDetailPage(Vehicle vehicle) {	
		VBox topMenu = menuFunctionality();
		ScrollPane detailsPage =  generateDetailsView(vehicle);
		BorderPane borderPane = new BorderPane();

		borderPane.setTop(topMenu);	
		borderPane.setCenter(detailsPage);
		Scene scene = new Scene(borderPane, 1200, 800);

		return scene;
	}

	// Generate Layout for Detail of a vehicle
	public ScrollPane generateDetailsView(Vehicle vehicle) {
		ScrollPane detailsPane = new ScrollPane();
		VBox detailsVbox= new VBox(10); 
		HBox detailsHbox = new HBox(10);
		TableView table = new TableView();
		Button goToHome = new Button("GO TO HOME");
		goToHome.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
		String imgPath;
		File imageFile;
		Image image;
		
		detailsHbox.setMinWidth(1200);
		
		try {
			detailsVbox.setPadding(new Insets(10,10,10,10));
			GridPane detailsGrid = new GridPane();
			detailsGrid.setPadding(new Insets(10, 10, 10, 10)); 
			detailsGrid.setVgap(15);
			detailsGrid.setHgap(40);
			detailsGrid.setAlignment(Pos.CENTER);

			// Direct to home page
			goToHome.setOnAction(e -> {
				try {
					this.createHomePage(FXCollections.observableArrayList(MainProgram.getDATA_LINK().getVehicles()));
					this.getPrimaryStage().setScene(homePageScene);
				} catch (Exception e1) {
					CustomExceptionBox.displayCustomException(e1);
				}

			});

			// Set image of the vehicle				
			if(vehicle.getImagePath() == null) {
				imgPath = "images/noimage.jpg";
			} else {
				imgPath = "images/" + vehicle.getImagePath();
			}
			imageFile = new File(imgPath);
			image = new Image(imageFile.toURI().toString());
			ImageView carImage = new ImageView(image);
			carImage.setFitWidth(600);
			carImage.setFitHeight(300);

			// Set Vehicle details
			Label yearLabel = new Label("Make Year");
			Label seatLabel = new Label("No. Of Seats");
			Label statusLabel = new Label("Status");
			Label lastMaintenanceDateLabel = new Label("Last Maintenance Date");

			Text makeModel = new Text(vehicle.getMake() + " " + vehicle.getModel());
			Text year = new Text(Integer.toString(vehicle.getYear()));
			Text seat = new Text(Integer.toString(vehicle.getNoOfSeats()));
			Text status= new Text(vehicle.getVehicleStatus().toString());
			Text lastMaintenanceDate;

			if(vehicle instanceof Van) {
				lastMaintenanceDate = new Text(((Van)vehicle).getLastMaintenanceDate().toString());
				detailsGrid.add(lastMaintenanceDateLabel, 0, 4);
				detailsGrid.add(lastMaintenanceDate, 1 , 4);
			}

			// Set book panel
			Button book = new Button("Book Now");
			book.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
			Button returnVehicle = new Button("Return");
			returnVehicle.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
			Button requestMaint = new Button("Request Maintenance");
			requestMaint.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");
			Button completeMaint = new Button("Complete Maintenance");
			completeMaint.setStyle("-fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22),radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white;-fx-effect:dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: \"Arial\";-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 12px;-fx-padding: 10 20 10 20;");

			// Disable operation based on Status of Vehicle
			if(vehicle.getVehicleStatus() == VehicleStatus.available) {
				requestMaint.setDisable(false);
				completeMaint.setDisable(true);
			} else if(vehicle.getVehicleStatus() == VehicleStatus.underMaintanance) {
				requestMaint.setDisable(true);
				completeMaint.setDisable(false);
			} else {
				requestMaint.setDisable(true);
				completeMaint.setDisable(true);
			}

			if(vehicle.getVehicleStatus() == VehicleStatus.available) {
				book.setDisable(false);
				returnVehicle.setDisable(true);
			} else if(vehicle.getVehicleStatus() == VehicleStatus.underMaintanance) {
				book.setDisable(true);
				returnVehicle.setDisable(true);        
			}else {
				book.setDisable(true);
				returnVehicle.setDisable(false);
			}

			// Request Maintenance Functionality
			requestMaint.setOnAction(e -> {
				try {
					vehicle.performMaintenance();
					this.setDetailPageScene(createDetailPage(vehicle));
					this.getPrimaryStage().setScene(detailPageScene); 
				} catch (Exception e1) {
					CustomExceptionBox.displayCustomException(e1);
				}
			});

			// Complete Maintenance Functionality
			completeMaint.setOnAction(e -> {
				try {
					if(vehicle instanceof Van)
						vehicle.completeMaintenance(new LastMaintPopup().display());
					else
						vehicle.completeMaintenance(new DateTime());
					this.setDetailPageScene(createDetailPage(vehicle));
					this.getPrimaryStage().setScene(detailPageScene); 
				} catch (Exception e1) {
					CustomExceptionBox.displayCustomException(e1);
				}
			});

			// Book Vehicle Functionality
			book.setOnAction(e -> {
				new BookDialogBoxView().bookingDisplay(vehicle);
				this.setDetailPageScene(createDetailPage(vehicle));
				this.getPrimaryStage().setScene(detailPageScene); 
			});

			// Return Vehicle Functionality
			returnVehicle.setOnAction(e -> {
				new ReturnVehicleView().display(vehicle);
				this.setDetailPageScene(createDetailPage(vehicle));
				this.getPrimaryStage().setScene(detailPageScene); 
			});
			
		

			// Add to Layout
			detailsGrid.add(makeModel, 0, 0);
			detailsGrid.add(statusLabel, 0, 1);
			detailsGrid.add(status, 1, 1);
			detailsGrid.add(seatLabel, 0, 2);
			detailsGrid.add(seat, 1, 2);
			detailsGrid.add(yearLabel, 0, 3);
			detailsGrid.add(year, 1, 3);
			detailsGrid.add(book, 0, 5);
			detailsGrid.add(returnVehicle, 1, 5);
			detailsGrid.add(requestMaint, 0, 6);
			detailsGrid.add(completeMaint, 1, 6);
			table = generateRecordTable(vehicle);
			table.setItems(FXCollections.observableArrayList(MainProgram.getDATA_LINK().getVehicleRentalRecords(vehicle.getVehicleId())));
			
			HBox imgHbox = new HBox();
			imgHbox.setMaxWidth(600);
			
			detailsHbox.getChildren().addAll(carImage, detailsGrid);
			detailsVbox.getChildren().addAll(goToHome, detailsHbox, table);
			detailsPane.setContent(detailsVbox);
		}
		catch(Exception ex) {
			CustomExceptionBox.displayCustomException(ex);
		}
		return detailsPane;
	}

	// Generate table for Vehicle rental records
	public TableView generateRecordTable(Vehicle vehicle) {
		TableView table = new TableView();

		TableColumn<RentalRecord, String> recordIdColumn = new TableColumn("RecordId");		
		recordIdColumn.setCellValueFactory(new PropertyValueFactory<RentalRecord,String>("recordId"));
		TableColumn<RentalRecord, String> rentDateColumn = new TableColumn("RentDate");	
		rentDateColumn.setCellValueFactory(new PropertyValueFactory<RentalRecord,String>("rentDate"));
		TableColumn<RentalRecord, String> estimatedDateColumn = new TableColumn("EstimatedRetunDate");
		estimatedDateColumn.setCellValueFactory(new PropertyValueFactory<RentalRecord,String>("estimatedReturnDate"));
		TableColumn<RentalRecord, String> actualDateColumn = new TableColumn("ActualRetunDate");
		actualDateColumn.setCellValueFactory(new PropertyValueFactory<RentalRecord,String>("actualReturnDate"));
		TableColumn<RentalRecord, String> rentalFeeColumn = new TableColumn("RentalFee");
		rentalFeeColumn.setCellValueFactory(new PropertyValueFactory<RentalRecord,String>("rentalFee"));
		TableColumn<RentalRecord, String> lateFeeColumn = new TableColumn("LateFee");
		lateFeeColumn.setCellValueFactory(new PropertyValueFactory<RentalRecord,String>("lateFee"));
		table.getColumns().addAll(recordIdColumn, rentDateColumn, estimatedDateColumn, actualDateColumn, rentalFeeColumn, lateFeeColumn);

		return table;
	}

	// Getters 
	public Scene getHomePageScene() {
		return homePageScene;
	}

	public static DataController getDATA_LINK() {
		return DATA_LINK;
	}

	public Scene getDetailPageScene() {
		return detailPageScene;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setHomePageScene(Scene homePageScene) {
		this.homePageScene = homePageScene;
	}

	public static void setDATA_LINK(DataController dATA_LINK) {
		DATA_LINK = dATA_LINK;
	}

	public void setDetailPageScene(Scene detailPageScene) {
		this.detailPageScene = detailPageScene;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	// Confirm close action
	private void closeProgram() {
		Boolean answer = CloseConfirmBox.displayConfirmBox();
		if(answer) {
			this.primaryStage.close();
		}
	}
}
