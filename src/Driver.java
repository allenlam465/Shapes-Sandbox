import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

/*
Your program should include a SubScene in which a user can add and edit Shape3D objects (only Spheres, Boxes, and Cylinders required). 

A button at the bottom of the window titled "Add Shape" will open a form prompting the user to create a new Shape to add to the SubScene. The user should be asked for the required info (x, y) location, radius for spheres, width, height, and length for boxes, etc. 

The user should also be allowed to change the background color of the subscene

When they click 'Submit', the shape should be added to the subscene. 

On the right of the Screen should be a set of tools (sliders, textforms, dropdowns etc). These should perform transforms on the currently selected shape. When the user clicks a shape within the subscene, it becomes the selected shape. If we use the rotate control, only the selected shape should rotate. Users should be able to rotate, translate, scale, and change the color of a shape.

A menu system should be implemented allowing the user to save their current image. Determine a way to save the shapes in the subscene, as well as their locations and dimensions. The user can also open a valid file and the subscene should populate with the shapes specified by that file.

To be submitted: All source code files, as well as a brief report (~2 paragraphs) from each student discussing what challenges they encountered and how they were solved, as well as which part of the project they worked on. 
 */

public class Driver extends Application{

	public static void main(String[] args) {

		launch(args);

	}

	private BorderPane border;
	private Group shapesGroup;

	@Override
	public void start(Stage primaryStage) throws Exception {

		border = new BorderPane();

		menu();

		// Application’s root node will be a vbox
		VBox rootNode = new VBox(10);
		rootNode.setAlignment(Pos.CENTER);

		HBox bottomNode = new HBox(10);
		Button addShape = new Button("Add Shape");

		bottomNode.getChildren().add(addShape);
		bottomNode.setAlignment(Pos.CENTER);
		bottomNode.setPadding(new Insets(10));

		// shapesGroup will eventually hold some 3D shapes
		shapesGroup = new Group();
		SubScene shapesSub = new SubScene(shapesGroup, 340, 340,
				true, SceneAntialiasing. DISABLED);

		// Change the SubScene’s background color
		shapesSub.setFill(Color. AZURE);
		rootNode.getChildren().add(shapesSub);

		border.setCenter(rootNode);
		border.setBottom(bottomNode);

		Scene myScene = new Scene(border, 380, 440);

		primaryStage.setScene(myScene);
		primaryStage.show();
	}

	protected void menu() {
		MenuBar menu = new MenuBar();
		Menu file = new Menu("File");

		MenuItem open = new MenuItem("Open");
		MenuItem save = new MenuItem("Save");

		file.getItems().addAll(open, new SeparatorMenuItem(), save);

		menu.getMenus().add(file);

		border.setTop(menu);	
	}

	protected void addShape() {

		Label shapeLabel = new Label("Shape");
		Label xLabel = new Label("X");
		Label yLabel = new Label("Y");

		Label radiusLabel = new Label("Radius");
		Label heightLabel = new Label("Height");
		Label widthLabel = new Label("Width");
		Label lengthLabel = new Label("Length");
		
		TextField xInput = new TextField();
		TextField yInput = new TextField();
		TextField radiusInput = new TextField();
		TextField heightInput = new TextField();
		TextField widthInput = new TextField();
		TextField lengthInput = new TextField();

		ChoiceBox<String> shapes = new ChoiceBox<>();
		shapes.getItems().addAll("Sphere","Cylinder","Box");
		
		GridPane gp = new GridPane();
		
		if(shapes.getValue().equals("Sphere")) {
			
			int radius = Integer.parseInt(radiusInput.getText());
			
			Sphere sphere = new Sphere(radius);
			
			shapesGroup.getChildren().add(sphere);
			
		}
		else if(shapes.getValue().equals("Cylinder")) {
			
			int radius = Integer.parseInt(radiusInput.getText());
			int height = Integer.parseInt(heightInput.getText());

			Cylinder cylinder = new Cylinder(radius, height);
			
			shapesGroup.getChildren().add(cylinder);

			
		}
		else if(shapes.getValue().equals("Box")) {
			int width = Integer.parseInt(heightInput.getText());
			int height = Integer.parseInt(heightInput.getText());
			int depth = Integer.parseInt(heightInput.getText());
			
			Box box = new Box(width,height,depth);
			
			shapesGroup.getChildren().add(box);

			
		}

	}
	
	protected void customizationMenu() {
		
	}



}
