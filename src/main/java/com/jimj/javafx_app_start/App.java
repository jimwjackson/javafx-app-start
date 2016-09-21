package com.jimj.javafx_app_start;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.velocity.VelocityContext;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.StatusBar;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class App extends Application
{
	private StatusBar statusBar;
	private BorderPane borderPane;
	private GridPane gridPane;
	private Stage primaryStage;
	private Scene scene;
	private ScrollPane scrollPane;
	private SplitPane splitPane;
	private TextArea previewPane;
	private SimpleStringProperty appTitle;
	private SimpleStringProperty previewText;

	private Document doc;
	private File file;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		appTitle = new SimpleStringProperty("untitled");
		primaryStage = stage;
		primaryStage.titleProperty().bindBidirectional(appTitle);

		doc = new Document();
		Menu menuFile = new Menu("File");
		MenuItem menuFileItemNew = new MenuItem("New");
		menuFileItemNew.setOnAction(actionEvent ->
		{
			onFileNew(actionEvent);
		});
		MenuItem menuFileItemSave = new MenuItem("Save");
		menuFileItemSave.setOnAction(actionEvent ->
		{
			onFileSave(actionEvent);
		});
		MenuItem menuFileItemOpen = new MenuItem("Open...");
		menuFileItemOpen.setOnAction(actionEvent ->
		{
			onFileOpen(actionEvent);
		});
		MenuItem menuFileItemSaveAs = new MenuItem("Save As...");
		menuFileItemSaveAs.setOnAction(actionEvent ->
		{
			onFileSaveAs(actionEvent);
		});
		MenuItem menuFileGen = new MenuItem("Generate App");
		menuFileGen.setOnAction(actionEvent ->
		{
			onFileGen(actionEvent);
		});

		MenuItem menuFileItemExit = new MenuItem("Exit");
		menuFileItemExit.setOnAction(actionEvent ->
		{
			if (doc.getDirtyValue() == true)
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Save File ?");
				alert.setHeaderText("Save File ?");
				alert.setContentText("Save File?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK)
				{
					onFileSave(null);
				}
				else
				{
					Alert alert2 = new Alert(AlertType.INFORMATION);
					alert2.setTitle("File will not be saved");
					alert2.setHeaderText("File will not be saved");
					alert2.setContentText("File will not be saved");
					alert2.showAndWait();
				}
			}
			Platform.exit();
		});

		menuFile.getItems().addAll(menuFileItemNew, menuFileItemOpen, menuFileItemSave, menuFileItemSaveAs,
		        new SeparatorMenuItem(), menuFileGen, new SeparatorMenuItem(), menuFileItemExit);

		Menu menuHelp = new Menu("Help");
		MenuItem menuHelpItemAbout = new MenuItem("About...");
		menuHelpItemAbout.setOnAction(actionEvent ->
		{
			onHelpAbout(actionEvent);
		});
		menuHelp.getItems().addAll(menuHelpItemAbout);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menuFile, menuHelp);

		Image imageSave = new Image(getClass().getResourceAsStream("/img/page_save.png"));
		Button tbSave = new Button("Save", new ImageView(imageSave));
		tbSave.setOnAction(actionEvent ->
		{
			onFileSave(actionEvent);
		});

		Image imageOpen = new Image(getClass().getResourceAsStream("/img/page_edit.png"));
		Button tbOpen = new Button("Open", new ImageView(imageOpen));
		tbOpen.setOnAction(actionEvent ->
		{
			onFileOpen(actionEvent);
		});

		Image imageSaveAs = new Image(getClass().getResourceAsStream("/img/page_save.png"));
		Button tbSaveAs = new Button("Save As", new ImageView(imageSaveAs));
		tbSaveAs.setOnAction(actionEvent ->
		{
			onFileSaveAs(actionEvent);
		});

		Image imageGen = new Image(getClass().getResourceAsStream("/img/transmit.png"));
		Button tbGen = new Button("Generate App", new ImageView(imageGen));
		tbGen.setOnAction(actionEvent ->
		{
			onFileGen(actionEvent);
		});

		ToolBar toolBar = new ToolBar(tbOpen, tbSave, tbSaveAs, new Separator(), tbGen);

		VBox vbox = new VBox(8);
		vbox.getChildren().addAll(menuBar, toolBar);
		borderPane = new BorderPane();
		borderPane.setTop(vbox);
		statusBar = new StatusBar();
		borderPane.setBottom(statusBar);
		scrollPane = new ScrollPane();
		gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		projectName();
		projectDir();
		packageName();
		propertiesFileName();
		pomFileName();
		appFileName();
		documentFileName();
		dataFileName();
		queryFileName();
		settingsFileName();
		taskFileName();
		serviceFileName();
		utilsFileName();
		loggerFileName();
		imageRepoDirName();

		splitPane = new SplitPane();
		previewPane = new TextArea();
		previewText = new SimpleStringProperty();
		previewPane.textProperty().bindBidirectional(previewText);
		scrollPane.setContent(gridPane);
		splitPane.getItems().addAll(scrollPane, previewPane);

		borderPane.setCenter(splitPane);

		scene = new Scene(borderPane, 2000, 1000);
		primaryStage.setTitle("untitled");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/transmit.png")));
		primaryStage.show();
		ActionEvent onFileNewEvent = new ActionEvent(null, menuFileItemNew);
		primaryStage.fireEvent(onFileNewEvent);
	}

	private void onFileNew(ActionEvent event)
	{
		appTitle.setValue("untitled");
		doc.clear();
	}

	private void onFileGen(ActionEvent event)
	{
		CodeGen gen = new CodeGen(doc);
		try
		{
			gen.gen();
			statusBar.setText(doc.getProjectDirNameValue() + " created");
		}
		catch (Exception e)
		{
			Notifications.create().title(e.getClass().getName()).text(e.getMessage()).showError();
			e.printStackTrace();
		}
	}

	private void onFileOpen(ActionEvent event)
	{
		final FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("jas files (*.jas)", "*.jas");
		fileChooser.setSelectedExtensionFilter(extFilter);
		File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
		if (f != null)
		{
			try (FileInputStream fis = new FileInputStream(f); ObjectInputStream ois = new ObjectInputStream(fis))
			{
				doc.readDoc(ois);
				ois.close();
				file = f;
				statusBar.setText(file.getPath() + " opened");
				appTitle.setValue(file.getPath());
				doc.setDirtyValue(false);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Notifications.create().title(e.getClass().getName()).text(e.getMessage()).showError();
			}
		}
	}

	private void onFileSave(ActionEvent event)
	{
		if (file == null)
		{
			onFileSaveAs(event);
		}
		else
		{
			if (doc.getDirtyValue() == true)
			{
				try (FileOutputStream fos = new FileOutputStream(file);
				        ObjectOutputStream oos = new ObjectOutputStream(fos))
				{
					doc.writeDoc(oos);
					oos.close();
					appTitle.setValue(file.getPath());
					statusBar.setText(file.getName() + " saved");
				}
				catch (Exception e)
				{
					e.printStackTrace();
					Notifications.create().title(e.getClass().getName()).text(e.getMessage()).showError();
				}
				statusBar.setText(file.getPath() + " saved");
			}
		}
	}

	private void onFileSaveAs(ActionEvent event)
	{
		final FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("jas files (*.jas)", "*.jas");
		fileChooser.setSelectedExtensionFilter(extFilter);
		final File f = fileChooser.showSaveDialog(gridPane.getScene().getWindow());
		if (f != null)
			try (FileOutputStream fos = new FileOutputStream(f); ObjectOutputStream oos = new ObjectOutputStream(fos))
			{
				doc.writeDoc(oos);
				oos.close();
				file = f;
				appTitle.setValue(file.getPath());
				statusBar.setText(file.getPath() + " saved");
			}
			catch (Exception e)
			{
				Notifications.create().title(e.getClass().getName()).text(e.getMessage()).showError();
			}
	}

	private void onHelpAbout(ActionEvent event)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("sftp Help About");
		InputStream is = getClass().getResourceAsStream("/build.properties");
		try
		{
			List<String> lines = IOUtils.readLines(is, Charset.defaultCharset());
			StringBuilder buffer = new StringBuilder();
			for (String line : lines)
			{
				buffer.append(line);
				buffer.append('\n');
			}
			alert.setContentText(buffer.toString());
			is.close();
		}
		catch (IOException e)
		{
			Notifications notifications = Notifications.create();
			notifications.text(e.getMessage());
			notifications.showError();
		}
		alert.setHeaderText("Platform: " + org.controlsfx.tools.Platform.getCurrent().getPlatformId());
		alert.showAndWait();
	}

	private void appFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getAppJavaFileName());
		Button buttonLoad = new Button("app java file (vm): ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setAppJavaFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as app.java template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getAppJavaFileNameValue(), doc.getPropertiesFileNameValue(), writer);
				String string = writer.toString();
				previewText.setValue(string);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void pomFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getPomXmFileName());
		Button buttonLoad = new Button("pom.vm file: ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setPomXmlFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as pom.xml template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getPomXmlFileNameValue(), doc.getPropertiesFileNameValue(), writer);
				previewText.setValue(writer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void dataFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getDataFileName());
		Button buttonLoad = new Button("data java file (vm): ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setDataFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as data template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getDataFileNameValue(), doc.getPropertiesFileNameValue(), writer);
				previewText.setValue(writer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void taskFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getTaskFileName());
		Button buttonLoad = new Button("task java file (vm): ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setTaskFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as task template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getTaskFileNameValue(), doc.getPropertiesFileNameValue(), writer);
				previewText.setValue(writer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void serviceFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getServiceFileName());
		Button buttonLoad = new Button("service java file (vm): ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setServiceFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as service template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getServiceFileNameValue(), doc.getServiceFileNameValue(), writer);
				previewText.setValue(writer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void utilsFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getUtilsFileName());
		Button buttonLoad = new Button("utils java file (vm): ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setUtilsFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as utils template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getUtilsFileNameValue(), doc.getPropertiesFileNameValue(), writer);
				previewText.setValue(writer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void queryFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getQueryFileName());
		Button buttonLoad = new Button("query java file (vm): ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setQueryFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as query template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getQueryFileNameValue(), doc.getPropertiesFileNameValue(), writer);
				previewText.setValue(writer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void settingsFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getSettingsFileName());
		Button buttonLoad = new Button("settings java file (vm): ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setSettingsFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as settings template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getSettingsFileNameValue(), doc.getPropertiesFileNameValue(), writer);
				previewText.setValue(writer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private File imageDir;

	private void imageRepoDirName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getImageRepo());
		Button buttonLoad = new Button("image repo: ");
		buttonLoad.setOnAction((event) ->
		{
			final DirectoryChooser dirChooser = new DirectoryChooser();
			imageDir = dirChooser.showDialog(gridPane.getScene().getWindow());
			if (imageDir != null)
			{
				try
				{
					doc.setImageRepoValue(imageDir.getAbsolutePath());
					statusBar.setText(imageDir.getAbsolutePath() + " set as image repo");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringBuilder buffer = new StringBuilder();
			try
			{
				Collection<File> files = FileUtils.listFiles(new File(doc.getImageRepoValue()),
				        TrueFileFilter.INSTANCE, null);
				for (File f : files)
				{
					buffer.append(f.getName() + "\n");
				}
				previewText.setValue(buffer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void loggerFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getLoggerFileName());
		Button buttonLoad = new Button("logger java file (vm): ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setLoggerFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as logger template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getLoggerFileNameValue(), doc.getPropertiesFileNameValue(), writer);
				previewText.setValue(writer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void documentFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getDocumentFileName());
		Button buttonLoad = new Button("document java file (vm): ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setDocumentFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as document template");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				CodeGen gen = new CodeGen(doc);
				gen.applyTemplate(doc.getDocumentFileNameValue(), doc.getPropertiesFileNameValue(), writer);
				previewText.setValue(writer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	@SuppressWarnings("unchecked")
	private void propertiesFileName()
	{
		Label label = new Label("");
		label.textProperty().bindBidirectional(doc.getPropertiesFileName());
		Button buttonLoad = new Button("properties file: ");
		buttonLoad.setOnAction((event) ->
		{
			final FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("velocity files (*.vm)", "*.vm");
			fileChooser.setSelectedExtensionFilter(extFilter);
			File f = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setPropertiesFileNameValue(f.getName());
					statusBar.setText(f.getPath() + " set as properties file");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		Button buttonPreview = new Button("Preview");
		buttonPreview.setOnAction((event) ->
		{
			StringWriter writer = new StringWriter();
			try
			{
				StringBuilder buffer = new StringBuilder();
				CodeGen gen = new CodeGen(doc);
				VelocityContext context = gen.applyTemplate(doc.getLoggerFileNameValue(),
				        doc.getPropertiesFileNameValue(), writer);
				Object[] keys = context.getKeys();
				for (Object k : keys)
				{
					String key = (String) k;
					Object value = context.get(key);
					if (value instanceof Map)
					{
						buffer.append(key + " [\n ");
						Map<Object, Object> map = (Map<Object, Object>) value;
						for (Map.Entry<Object, Object> entry : map.entrySet())
						{
							if (entry.getValue() instanceof Map)
							{
								buffer.append("   " + entry.getKey() + " [\n ");
								Map<Object, Object> m = (Map<Object, Object>) entry.getValue();
								for (Map.Entry<Object, Object> e : m.entrySet())
								{
									buffer.append("      " + e.getKey() + " = " + e.getValue() + "\n");
								}
								buffer.append(" ]\n ");
							}
							else if (entry.getValue() instanceof List)
							{
								List<Object> l = (List<Object>) entry.getValue();
								for (Object obj : l)
								{
									if (obj instanceof Map)
									{
										buffer.append("   " + entry.getKey() + " [\n ");
										Map<Object, Object> m = (Map<Object, Object>) obj;
										for (Map.Entry<Object, Object> e : m.entrySet())
										{
											buffer.append("      " + e.getKey() + " = " + e.getValue() + "\n");
										}
										buffer.append(" ]\n ");
									}
									else
									{
										buffer.append(obj + "\n");
									}
								}
							}
							else
							{
								buffer.append(entry.getKey() + " = " + entry.getValue() + "\n");
							}
						}
						buffer.append(" ]\n ");
					}
					else
					{
						buffer.append(key + " = " + value + "\n");
					}
				}
				previewText.setValue(buffer.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(buttonLoad, 0, row);
		gridPane.add(label, 1, row);
		gridPane.add(buttonPreview, 2, row);
	}

	private void projectName()
	{
		Label label = new Label("Project Name:");
		TextField projectName = new TextField("");
		projectName.textProperty().bindBidirectional(doc.getProjectName());
		int row = gridPane.getChildren().size();
		gridPane.add(label, 0, row);
		gridPane.add(projectName, 1, row);
	}

	private void packageName()
	{
		Label label = new Label("Package Name:");
		TextField projectName = new TextField("");
		projectName.textProperty().bindBidirectional(doc.getPackageName());
		int row = gridPane.getChildren().size();
		gridPane.add(label, 0, row);
		gridPane.add(projectName, 1, row);
	}

	private void projectDir()
	{
		TextField projectName = new TextField("");
		projectName.setMinWidth(300);
		projectName.setPrefWidth(500);
		projectName.textProperty().bindBidirectional(doc.getProjectDirName());
		Button button = new Button("Project directory: ");
		button.setOnAction((event) ->
		{
			final DirectoryChooser dirChooser = new DirectoryChooser();
			File f = dirChooser.showDialog(gridPane.getScene().getWindow());
			if (f != null)
			{
				try
				{
					doc.setProjectDirNameValue(f.getPath());
					statusBar.setText(f.getPath() + " set as project directory");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		int row = gridPane.getChildren().size();
		gridPane.add(button, 0, row);
		gridPane.add(projectName, 1, row);
	}

}
