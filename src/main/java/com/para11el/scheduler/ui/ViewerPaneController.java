package com.para11el.scheduler.ui;


import com.para11el.scheduler.algorithm.Task;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.layout.Eades84Layout;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.camera.Camera;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random;


public class ViewerPaneController {
	private static FxViewer _viewer;
	private static Camera _camera;
	private FxDefaultView viewPanel;

	private static AnimationTimer _timer;

	private static String _inputFile;
	private static String _outputFile;
	private static String _processors;
	private static String _cores;
	private static long _startTime;

	private int _cellWidth;
	private int _cellHeight = 20;

	@FXML
	private AnchorPane graphContainer;


	@FXML
	private Label timerLabel;

	@FXML
	private Text inputFileText;

	@FXML
	private Text processorsText;

	@FXML
	private Text coresText;

	@FXML
	private Text outputFileText;

	@FXML
	private TilePane colLabelTile;

	@FXML
	private TilePane tile;

	@FXML
	private ScrollPane scrollPane;


	/**
	 * Initialize some of the GUI's components to initial states
	 */
	 @FXML
	 public void initialize() {

		 scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		 setCellSize(Integer.parseInt(_processors));

		 initialisePane(30);
		 initialiseLabel(30);


		 // set cell: processor, start time, length of time, colour (string)
		 setCell("a", 2, 3, 2, generateColours());
		 setCell("b", 4, 0, 7, generateColours());

		 setCell("b2", 4, 7, 7, generateColours());

		 setCell("c", 3, 18, 12, generateColours());

		 setCell("b", 1, 0, 1, generateColours());

		 setCell("b", 1, 1, 1, generateColours());

		 setCell("b", 1, 7, 4, generateColours());


		 // testing more processors


		 // Embed GraphStream graph into the GUI
		 _viewer.addDefaultView(false, _viewer.newDefaultGraphRenderer());
		 _viewer.enableAutoLayout();
		 viewPanel = (FxDefaultView) _viewer.getDefaultView();
		 viewPanel.setFocusTraversable(true); // Allow the keyboard shortcuts
		 viewPanel.setMaxHeight(357); // So it fits
		 viewPanel.setMaxWidth(598);
		 viewPanel.requireFocus();
		 _camera = _viewer.getDefaultView().getCamera();
		 graphContainer.getChildren().add(viewPanel); // Add it to its container

		 inputFileText.setText(_inputFile);
		 coresText.setText(_cores);
		 processorsText.setText(_processors);
		 outputFileText.setText(_outputFile);


		 // Set the timer for elapsing the program run time
		 _timer = new AnimationTimer() {
			 @Override
			 public void handle(long now) {
				 long elapsedMillis = System.currentTimeMillis() - _startTime ;
				 String timeLabel = String.format("%02d:%02d:%02d",
						 (elapsedMillis / 60000),
						 ((elapsedMillis % 60000) / 1000),
						 ((elapsedMillis % 1000) / 10));
				 timerLabel.setText(timeLabel);
			 }
		 };

		 toggleTimer(true); // Start the timer
	 }

	 public static void setViewer(FxViewer viewer) {
		 _viewer = viewer;
	 }

	 /**
	  * Zoom out on the graph view
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 private void zoomInAction(ActionEvent event) {
		 Platform.runLater(() -> {
			 _camera.setViewPercent(Math.max(0.0001f,
					 _camera.getViewPercent() * 0.7f));
		 });
	 }

	 /**
	  * Zoom out on the graph view
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 private void zoomOutAction(ActionEvent event) {
		 Platform.runLater(() -> {
			 _camera.setViewPercent(_camera.getViewPercent() * 1.3f);
		 });
	 }

	 /**
	  * Recenter the camera to default on the graph view
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 private void resetViewAction(ActionEvent event) {
		 Platform.runLater(() -> {
			 _camera.resetView();
		 });
	 }

	 /**
	  * Pan the camera on the graph view up
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 private void panUpAction(ActionEvent event) {
		 Platform.runLater(() -> {
			 double delta = calculateDelta();

			 Point3 p = _camera.getViewCenter();
			 _camera.setViewCenter(p.x, p.y + delta, 0);
		 });
	 }

	 /**
	  * Pan the camera on the graph view down
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 private void panDownAction(ActionEvent event) {
		 Platform.runLater(() -> {
			 double delta = calculateDelta();

			 Point3 p = _camera.getViewCenter();
			 _camera.setViewCenter(p.x, p.y - delta, 0);
		 });
	 }

	 /**
	  * Pan the camera on the graph view right
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 private void panRightAction(ActionEvent event) {
		 Platform.runLater(() -> {
			 double delta = calculateDelta();

			 Point3 p = _camera.getViewCenter();
			 _camera.setViewCenter(p.x + delta, p.y, 0);
		 });
	 }

	 /**
	  * Pan the camera on the graph view left
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 private void panLeftAction(ActionEvent event) {
		 Platform.runLater(() -> {
			 double delta = calculateDelta();

			 Point3 p = _camera.getViewCenter();
			 _camera.setViewCenter(p.x - delta, p.y, 0);
		 });
	 }

	 /**
	  * Set the graph to be have a hierarchical layout
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 void setHierarchicalLayout(ActionEvent event) {
		 _viewer.disableAutoLayout();
		 _viewer.enableAutoLayout(new HierarchicalLayout());
	 }

	 /**
	  * Set the graph to be have a linear logarithm layout
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 void setLinLogLayout(ActionEvent event) {
		 _viewer.disableAutoLayout();
		 _viewer.enableAutoLayout(new LinLog());
	 }

	 /**
	  * Set the graph to be have a spring box layout
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 void setSpringBoxLayout(ActionEvent event) {
		 _viewer.disableAutoLayout();
		 _viewer.enableAutoLayout(new SpringBox());
	 }

	 /**
	  * Give graph view focus when clicked on i.e. allow it to be accessed by keyboard shortcuts
	  * @param event
	  * @Author Sean Oldfield
	  */
	 @FXML
	 void giveGraphFocus(MouseEvent event) {
		 viewPanel.requestFocus();
	 }

	 /**
	  * Set useful parameters for viewing and manipulating in the GUI
	  * @param parameters String list of parameters
	  * @Author Sean Oldfield
	  */
	 public static void setParameters(List<String> parameters) {
		 _inputFile = parameters.get(0);
		 _processors = parameters.get(1);
		 if(parameters.get(2).equals("0")) {
			 _cores = "Not Set";
		 } else {
			 _cores = parameters.get(2);
		 }
		 _outputFile = parameters.get(3);
		 _startTime = Long.parseLong(parameters.get(4));

	 }

	 /**
	  * Calculate delta offset for zoom functions in the GUI
	  * @return The delta offset
	  * @Author Sean Oldfield
	  */
	 private double calculateDelta() {
		 return  _camera.getViewPercent() * _camera.getGraphDimension() * 0.1f;
	 }

	 /**
	  * Start or stop the program timer
	  * @param enable True if the timer is to be started, false to stop it
	  * @Author Sean Oldfield
	  */
	 public static void toggleTimer(boolean enable) {
		 if(enable) {
			 _timer.start();
		 } else {
			 _timer.stop();
		 }
	 }

	 // ====================================

	 private void initialisePane(int num) {
		 
		 Label processorLabel;
		 int processorNum = Integer.parseInt(_processors);

		 for (int i = 0; i < processorNum; i++) {
			 
			 if (processorNum < 11) {
				 processorLabel = new Label("P" + Integer.toString(i+1));
			 } else {
				 processorLabel = new Label(Integer.toString(i+1));
			 }

			 processorLabel.setPrefSize(_cellWidth, _cellHeight);
			 processorLabel.setAlignment(Pos.CENTER);

			 tile.getChildren().add(processorLabel);
		 }

		 for (int i = 0; i < (num)*processorNum; i++) {
			 Pane p = new Pane();
			 p.setPrefSize(_cellWidth, _cellHeight);
			 p.setStyle("-fx-background-color: #D3D3D3");
			 tile.getChildren().add(p);
		 }


	 }

	 private void initialiseLabel(int num) {

		 Label colLabel;

		 for (int i = 0; i < num+1; i++) {

			 if (i == 0 ) {
				 colLabel = new Label("");
			 } else {
				 colLabel = new Label(Integer.toString(i-1));
			 }
			 
			 colLabel.setPrefSize(30, _cellHeight);
			 colLabel.setAlignment(Pos.CENTER_RIGHT);

			 colLabelTile.getChildren().add(colLabel);
		 }

	 }

	 /*    public void updateSchedule(List<Task> schedule) {
        for(Task task: schedule) {
            setCell(task.getNode(), task.getProcessor(), task.getStartTime(), task.);
        }
    }*/


	 private void setCell(String label, int processor, int startTime, int length, String colour) {

		 int cell = (processor + Integer.parseInt(_processors) * startTime) - 1 + Integer.parseInt(_processors);

		 for (int i = 0; i < length; i++) {

			 if (i == 0) {

				 Label nodeLabel = new Label(label);
				 nodeLabel.setPrefSize(_cellWidth, _cellHeight);
				 nodeLabel.setAlignment(Pos.CENTER);
				 tile.getChildren().set(cell, nodeLabel);
				 tile.getChildren().get(cell).setStyle(colour);
			 }
			 tile.getChildren().get(cell).setStyle("-fx-background-color: " + colour);
			 cell = cell + Integer.parseInt(_processors);
		 }
	 }

	 private String generateColours() {

		 Random rand = new Random();

		 float r = rand.nextFloat();
		 float g = rand.nextFloat() / 2f;
		 float b = rand.nextFloat() / 2f;

		 java.awt.Color randomColor = new java.awt.Color(r, g, b);
		 String hex = Integer.toHexString(randomColor.getRGB() & 0xffffff);

		 if (hex.length() < 6) {
			 hex = "0" + hex;
		 }
		 hex = "#" + hex;

		 return hex;
	 }

	 private void setCellSize(int processors) {

		 int gapSize = (processors - 1)*2;
		 _cellWidth = (int)(Math.floor((300 - gapSize)/processors));
	 }


}

