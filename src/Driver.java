import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
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
import javafx.stage.FileChooser;
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
	private SubScene shapesSub;
	private Label messageLabel = new Label("");
	private Label shapeSelected = new Label("");
	private Slider xAxis = new Slider(-360.0, 360.0, 0.0);
	private Slider yAxis = new Slider(-360.0, 360.0, 0.0);
	private Slider xPos = new Slider(-500.0, 500.0, 0.0);
	private Slider yPos = new Slider(-500.0, 500.0, 0.0);
	private Slider scale = new Slider(1.0, 5.0, 0);
	private Stage primaryStage;

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
		shapesSub = new SubScene(shapesGroup, 500, 500,
				true, SceneAntialiasing. DISABLED);

		VBox.setVgrow(shapesSub, Priority.ALWAYS);
		shapesSub.setFill(Color. AZURE);
		rootNode.getChildren().add(shapesSub);

		border.setCenter(rootNode);
		border.setBottom(bottomNode);

		addShape();

		Scene myScene = new Scene(border,800,750);

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

		save.setOnAction(event ->{
			FileChooser fc = new FileChooser();
			File saveFile = fc.showSaveDialog(primaryStage);

			try {
				writeFile(saveFile.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		open.setOnAction(event ->{
			FileChooser fc = new FileChooser();
			File openFile = fc.showOpenDialog(primaryStage);

			try {
				getFileContent(openFile.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
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

		VBox shapeMenu = new VBox(10, messageLabel, gp, addShape);

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
					double x = Double.parseDouble(xInput.getText());
					double y = Double.parseDouble(yInput.getText());
					double radius = Double.parseDouble(radiusInput.getText());

					if(x > 0 && y > 0 && radius > 0) {
						addSphere(x,y,radius,1);
						messageLabel.setText("Shape added.");
					}
					else {
						messageLabel.setText("Shape cannot be added no negative values input.");
					}

				});
			}
			else if(chosenShape.equals("Cylinder")) {

				gp.addColumn(2, radiusLabel, heightLabel);
				gp.addColumn(3, radiusInput, heightInput);

				addShape.setOnAction(event ->{

					double x = Double.parseDouble(xInput.getText());
					double y = Double.parseDouble(yInput.getText());
					double radius = Double.parseDouble(radiusInput.getText());
					double height = Double.parseDouble(heightInput.getText());

					if(x > 0 && y > 0 && radius > 0 && height > 0) {
						addCylinder(x,y,radius,height,1);
						messageLabel.setText("Shape added.");
					}
					else {
						messageLabel.setText("Shape cannot be added no negative values input.");
					}

				});
			}
			else if(chosenShape.equals("Box")) {

				gp.addColumn(2, widthLabel, heightLabel, lengthLabel);
				gp.addColumn(3, widthInput, heightInput, lengthInput);

				addShape.setOnAction(event ->{
					double x = Double.parseDouble(xInput.getText());
					double y = Double.parseDouble(yInput.getText());
					double width = Double.parseDouble(widthInput.getText());
					double height = Double.parseDouble(heightInput.getText());
					double depth = Double.parseDouble(lengthInput.getText());

					if(x > 0 && y > 0 && width > 0 && height > 0 && depth > 0) {
						addBox(x,y,width,height,depth,1);
						messageLabel.setText("Shape added.");
					}
					else {
						messageLabel.setText("Shape cannot be added no negative values input.");
					}
				});

			}

		});
	}

	private void addSphere(double x, double y, double radius, double s) {
		Sphere sphere = new Sphere(radius);
		sphere.getTransforms().add(new Translate(x, y, 0));
		sphere.setScaleX(s);
		sphere.setScaleY(s);
		sphere.setScaleZ(s);


		sphere.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent ->{
			selectedShape = sphere;
			shapeSelected.setText("Sphere");
			xPos.setValue(selectedShape.getTranslateX());
			yPos.setValue(selectedShape.getTranslateY());
			scale.setValue(selectedShape.getScaleX());
		});

		sphere.layoutXProperty();
		sphere.layoutYProperty();

		shapesGroup.getChildren().add(sphere);
	}

	private void addCylinder(double x, double y, double radius, double height, double s) {
		Cylinder cylinder = new Cylinder(radius, height);
		cylinder.getTransforms().add(new Translate(x, y, 0));
		
		cylinder.setScaleX(s);
		cylinder.setScaleY(s);
		cylinder.setScaleZ(s);
		
		cylinder.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent ->{
			selectedShape = cylinder;
			shapeSelected.setText("Cylinder");
			xPos.setValue(selectedShape.getTranslateX());
			yPos.setValue(selectedShape.getTranslateY());
			scale.setValue(selectedShape.getScaleX());
		});

		cylinder.layoutXProperty();
		cylinder.layoutYProperty();

		shapesGroup.getChildren().add(cylinder);
	}

	private void addBox(double x, double y, double width, double height, double depth, double s) {
		Box box = new Box(width,height,depth);
		box.getTransforms().add(new Translate(x, y, 0));
		box.setScaleX(s);
		box.setScaleY(s);
		box.setScaleZ(s);

		box.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEvent ->{
			selectedShape = box;
			shapeSelected.setText("Box");
			xPos.setValue(selectedShape.getTranslateX());
			yPos.setValue(selectedShape.getTranslateY());
			scale.setValue(selectedShape.getScaleX());

		});

		box.layoutXProperty();
		box.layoutYProperty();

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
		xAxis.setShowTickMarks(true);
		xAxis.setShowTickLabels(true);
		yAxis.setShowTickMarks(true);
		yAxis.setShowTickLabels(true);

		//X and Y position slider
		xPos.setShowTickMarks(true);
		xPos.setShowTickLabels(true);
		yPos.setShowTickMarks(true);
		yPos.setShowTickLabels(true);

		//Scale slider
		scale.setShowTickMarks(true);
		scale.setShowTickLabels(true);

		GridPane gp = new GridPane();

		gp.addColumn(0, hRotateLabel, vRotateLabel, xLabel, yLabel, scaleLabel);
		gp.addColumn(1, xAxis, yAxis, xPos, yPos, scale);
		gp.setHgap(10);
		gp.setVgap(10);

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
				if((double)newVal <= shapesSub.getWidth() - 50 && (double)newVal >= -150)
					selectedShape.setTranslateX((double)newVal);
			}

		});

		yPos.valueProperty().addListener((o, oldVal, newVal) ->
		{
			if(selectedShape != null)
			{
				if((double)newVal <= shapesSub.getWidth() - 50 && (double)newVal >= -150)
					selectedShape.setTranslateY((double)newVal);
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

		VBox customizationMenu = new VBox(10,shapeSelected,gp);
		customizationMenu.setPadding(new Insets(10));
		customizationMenu.setAlignment(Pos.CENTER);

		border.setRight(customizationMenu);

	}

	private String writeSphere(Sphere shape) {
		return "Sphere," + (shape.getTransforms().get(0).getTx() + shape.getTranslateX()) + "," + (shape.getTransforms().get(0).getTy() + shape.getTranslateY()) + "," + shape.getRadius() + "," + shape.getScaleX(); 
	}
	private String writeCylinder(Cylinder shape) {
		return "Cylinder," + (shape.getTransforms().get(0).getTx() + shape.getTranslateX()) + "," + (shape.getTransforms().get(0).getTy() + shape.getTranslateY()) + "," + shape.getRadius() + "," + 0 + "," + shape.getHeight() + "," + shape.getScaleX(); 	
	}
	private String writeBox(Box shape) {
		return "Box," + (shape.getTransforms().get(0).getTx() + shape.getTranslateX()) + "," + (shape.getTransforms().get(0).getTy() + shape.getTranslateY()) + "," + 0 + "," + shape.getWidth() + "," + shape.getHeight() + "," + shape.getDepth() + "," + shape.getScaleX(); 		
	}

	private void writeFile(String fileName) throws IOException {
		CSV csvWriter = new CSV(fileName);

		for(Node x : shapesGroup.getChildren()) {

			if(x instanceof Sphere)
				csvWriter.write(writeSphere((Sphere) x));

			if(x instanceof Cylinder)
				csvWriter.write(writeCylinder((Cylinder)x));

			if(x instanceof Box)
				csvWriter.write(writeBox((Box)x));	
		}
	}

	private void getFileContent(String filePath) throws IOException {
		try
		(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				if(!line.isEmpty()) {
					String everything = line;

					String[] tokens = everything.split(",");

					if(tokens[0].equals("Sphere")) {
						double x = Double.parseDouble(tokens[1]);
						double y = Double.parseDouble(tokens[2]);
						double radius = Double.parseDouble(tokens[3]);
						double scale = Double.parseDouble(tokens[4]);

						addSphere(x,y,radius,scale);
					}

					if(tokens[0].equals("Cylinder")) {
						double x = Double.parseDouble(tokens[1]);
						double y = Double.parseDouble(tokens[2]);
						double radius = Double.parseDouble(tokens[3]);
						double height = Double.parseDouble(tokens[5]);
						double scale = Double.parseDouble(tokens[4]);

						addCylinder(x,y,radius, height, scale);	
					}

					if(tokens[0].equals("Box")) {
						double x = Double.parseDouble(tokens[1]);
						double y = Double.parseDouble(tokens[2]);
						double width = Double.parseDouble(tokens[4]);
						double height = Double.parseDouble(tokens[5]);
						double depth = Double.parseDouble(tokens[6]);
						double scale = Double.parseDouble(tokens[4]);

						addBox(x,y,width,height,depth, scale);
					}
				}
				
				line = br.readLine();

			}

		}
		catch(FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
		}
	}
}
