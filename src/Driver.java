import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/*
Your program should include a SubScene in which a user can add and edit Shape3D objects (only Spheres, Boxes, and Cylinders required).

A button at the bottom of the window titled "Add Shape" will open a form prompting the user to create a new Shape to add to the SubScene. The user should be asked for the required info (x, y) location, radius for spheres, width, height, and length for boxes, etc.

The user should also be allowed to change the background color of the subscene

When they click 'Submit', the shape should be added to the subscene.

On the right of the Screen should be a set of tools (sliders, textforms, dropdowns etc). These should perform transforms on the currently selected shape. When the user clicks a shape within the subscene, it becomes the selected shape. If we use the rotate control, only the selected shape should rotate. Users should be able to rotate, translate, scale, and change the color of a shape.

A menu system should be implemented allowing the user to save their current image. Determine a way to save the shapes in the subscene, as well as their locations and dimensions. The user can also open a valid file and the subscene should populate with the shapes specified by that file.

To be submitted: All source code files, as well as a brief report (~2 paragraphs) from each student discussing what challenges they encountered and how they were solved, as well as which part of the project they worked on.
BOUNDED WITHIN SUBSPACE 
NO NEGATIVE VALUE
 */

public class Driver extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	private BorderPane border;
	private Group shapesGroup;
	private Shape3D selectedShape;

	@Override
	public void start(Stage primaryStage) throws Exception {

		border = new BorderPane();

		menu();
		customizationMenu();

		VBox rootNode = new VBox(10);
		rootNode.setAlignment(Pos.CENTER);

		HBox bottomNode = new HBox(10);
		Button addShape = new Button("Add Shape");

		bottomNode.getChildren().add(addShape);
		bottomNode.setAlignment(Pos.CENTER);
		bottomNode.setPadding(new Insets(10));

		shapesGroup = new Group();
		SubScene shapesSub = new SubScene(shapesGroup, 500, 500,
				true, SceneAntialiasing. DISABLED);

		VBox.setVgrow(shapesSub, Priority.ALWAYS);
		shapesSub.setFill(Color. AZURE);
		rootNode.getChildren().add(shapesSub);

		border.setCenter(rootNode);
		border.setBottom(bottomNode);

		addShape();

		Scene myScene = new Scene(border,800,700);

		primaryStage.setScene(myScene);
		primaryStage.show();
	}

	private void menu() {
		MenuBar menu = new MenuBar();
		Menu file = new Menu("File");
		Menu edit = new Menu("Edit");

		MenuItem open = new MenuItem("Open");
		MenuItem save = new MenuItem("Save");
		MenuItem backgroundColor = new MenuItem("Background Color");

		file.getItems().addAll(open, new SeparatorMenuItem(), save);
		edit.getItems().add(backgroundColor);

		menu.getMenus().addAll(file,edit);

		border.setTop(menu);
	}

	private void addShape() {
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

		Button addShape = new Button("Add Shape");

		String[] shapes = {"Sphere","Cylinder","Box"};
		ChoiceBox<String> shapesList = new ChoiceBox<>(FXCollections.observableArrayList(shapes));

		GridPane gp = new GridPane();
		gp.addColumn(0, shapeLabel, xLabel, yLabel);
		gp.addColumn(1, shapesList, xInput, yInput);

		VBox shapeMenu = new VBox(10, gp, addShape);

		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
		shapeMenu.setPadding(new Insets(10));
		shapeMenu.setAlignment(Pos.CENTER);
		border.setBottom(shapeMenu);

		shapesList.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) ->{

			String chosenShape = shapes[newValue.intValue()];

			if(gp.getChildren().contains(heightLabel)) {
				gp.getChildren().remove(heightLabel);
				gp.getChildren().remove(heightInput);
			}
			if(gp.getChildren().contains(widthLabel)) {
				gp.getChildren().remove(widthLabel);
				gp.getChildren().remove(widthInput);
			}
			if(gp.getChildren().contains(lengthLabel)) {
				gp.getChildren().remove(lengthLabel);
				gp.getChildren().remove(lengthInput);
			}
			if(gp.getChildren().contains(radiusLabel)) {
				gp.getChildren().remove(radiusLabel);
				gp.getChildren().remove(radiusInput);
			}

			if(chosenShape.equals("Sphere")) {

				gp.addColumn(2, radiusLabel);
				gp.addColumn(3, radiusInput);

				addShape.setOnAction(event ->{
					int x = Integer.parseInt(xInput.getText());
					int y = Integer.parseInt(yInput.getText());
					int radius = Integer.parseInt(radiusInput.getText());

					addSphere(x,y,radius);
				});
			}
			else if(chosenShape.equals("Cylinder")) {

				gp.addColumn(2, radiusLabel, heightLabel);
				gp.addColumn(3, radiusInput, heightInput);

				addShape.setOnAction(event ->{
					int x = Integer.parseInt(xInput.getText());
					int y = Integer.parseInt(yInput.getText());
					int radius = Integer.parseInt(radiusInput.getText());
					int height = Integer.parseInt(heightInput.getText());

					addCylinder(x,y,radius,height);
				});
			}
			else if(chosenShape.equals("Box")) {

				gp.addColumn(2, widthLabel, heightLabel, lengthLabel);
				gp.addColumn(3, widthInput, heightInput, lengthInput);

				addShape.setOnAction(event ->{
					int x = Integer.parseInt(xInput.getText());
					int y = Integer.parseInt(yInput.getText());
					int width = Integer.parseInt(widthInput.getText());
					int height = Integer.parseInt(heightInput.getText());
					int depth = Integer.parseInt(lengthInput.getText());

					addBox(x,y,width,height,depth);
				});

			}

		});
	}

	private void addSphere(int x, int y, int radius) {
		Sphere sphere = new Sphere(radius);
		sphere.getTransforms().add(new Translate(x, y, 0));

		sphere.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent ->{
			selectedShape = sphere;
		});
		
		sphere.boundsInParentProperty();

		shapesGroup.getChildren().add(sphere);
	}

	private void addCylinder(int x, int y, int radius, int height) {
		Cylinder cylinder = new Cylinder(radius, height);
		cylinder.getTransforms().add(new Translate(x, y, 0));
		
		cylinder.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent ->{
			selectedShape = cylinder;
		});
		
		cylinder.boundsInParentProperty();

		shapesGroup.getChildren().add(cylinder);
	}

	private void addBox(int x, int y, int width, int height, int depth) {
		Box box = new Box(width,height,depth);
		box.getTransforms().add(new Translate(x, y, 0));

		box.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent ->{
			selectedShape = box;
		});

		box.boundsInParentProperty();
		
		shapesGroup.getChildren().add(box);
	}

	private void customizationMenu() {
		Label shapeSelected = new Label("");
		Label hRotateLabel = new Label("Horizontal Rotate");
		Label vRotateLabel = new Label("Vertical Rotate");
		Label xLabel = new Label("X Posistion");
		Label yLabel = new Label("Y Position");
		Label scaleLabel = new Label("Scale");

		//Horizontal and Vertical Slider
		Slider xAxis = new Slider(-360.0, 360.0, 0.0);
		xAxis.setShowTickMarks(true);
		xAxis.setShowTickLabels(true);
		Slider yAxis = new Slider(-360.0, 360.0, 0.0);
		yAxis.setShowTickMarks(true);
		yAxis.setShowTickLabels(true);

		//X and Y position slider
		Slider xPos = new Slider(-500.0, 500.0, 0);
		xPos.setShowTickMarks(true);
		xPos.setShowTickLabels(true);
		Slider yPos = new Slider(-500.0, 500.0, 0);
		yPos.setShowTickMarks(true);
		yPos.setShowTickLabels(true);

		//Scale slider
		Slider scale = new Slider(1.0, 5.0, 0);
		scale.setShowTickMarks(true);
		scale.setShowTickLabels(true);

		GridPane gp = new GridPane();


		gp.addColumn(0, hRotateLabel, vRotateLabel, xLabel, yLabel, scaleLabel);
		gp.addColumn(1, xAxis, yAxis, xPos, yPos, scale);
		gp.setHgap(10);
		gp.setVgap(10);
		
		xAxis.setValue(0);
		yAxis.setValue(0);
		xPos.setValue(0);
		yPos.setValue(0);
		scale.setValue(0);

		xAxis.valueProperty().addListener((o, oldVal, newVal) ->
		{        	
			if(selectedShape != null)
			{
				Rotate xRotate = new Rotate(0, Rotate.X_AXIS);

				xRotate.setAngle((double)newVal);
				selectedShape.getTransforms().add(xRotate);
			}
		});

		yAxis.valueProperty().addListener((o, oldVal, newVal) ->
		{        	
			if(selectedShape != null)
			{
				Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

				yRotate.setAngle((double)newVal);
				selectedShape.getTransforms().add(yRotate);
			}
		});

		xPos.valueProperty().addListener((o, oldVal, newVal) ->
		{
			if(selectedShape != null)
			{
				selectedShape.setLayoutX((double)newVal);
			}

		});
		
		yPos.valueProperty().addListener((o, oldVal, newVal) ->
		{
			if(selectedShape != null)
			{
				selectedShape.setLayoutY((double)newVal);
			}

		});

		scale.valueProperty().addListener((o, oldVal, newVal) ->{
			if(selectedShape != null)
			{	
				selectedShape.setScaleX((double)newVal);
				selectedShape.setScaleY((double)newVal);
				selectedShape.setScaleZ((double)newVal);
			}
		});
		
		shapeSelected.textProperty().addListener((observer, oldValue, newValue) ->{
			
			if(selectedShape != null) {
				
				shapeSelected.setText("Selected Shape");
			}
		});

		VBox customizationMenu = new VBox(10,shapeSelected,gp);
		customizationMenu.setPadding(new Insets(10));
		customizationMenu.setAlignment(Pos.CENTER);

		border.setRight(customizationMenu);

	}

	private void writeFile(String fileName) {

	}

	private void openFile(String fileName) {

	}
}
