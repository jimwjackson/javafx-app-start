package com.jimj.javafx_app_start;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javafx.beans.property.*;

public class Document implements Serializable
{
	private static final long serialVersionUID = 1L;
	private SimpleBooleanProperty dirty;
	private SimpleStringProperty projectName;
	private SimpleStringProperty projectDirName;
	private SimpleStringProperty packageName;
	private SimpleStringProperty pom_xml_fileName;
	private SimpleStringProperty app_java_fileName;
	private SimpleStringProperty propertiesFileName;
	private SimpleStringProperty documentFileName;
	private SimpleStringProperty dataFileName;
	private SimpleStringProperty taskFileName;
	private SimpleStringProperty serviceFileName;
	private SimpleStringProperty utilsFileName;
	private SimpleStringProperty loggerFileName;
	private SimpleStringProperty settingsFileName;
	private SimpleStringProperty queryFileName;
	private SimpleStringProperty imageRepo;
	private List<SimpleStringProperty> properties = new ArrayList<SimpleStringProperty>();

	public Document()
	{
		pom_xml_fileName = new SimpleStringProperty();
		app_java_fileName = new SimpleStringProperty();
		projectDirName = new SimpleStringProperty();
		projectName = new SimpleStringProperty();
		propertiesFileName = new SimpleStringProperty();
		dirty = new SimpleBooleanProperty();
		packageName = new SimpleStringProperty();
		documentFileName = new SimpleStringProperty();
		dataFileName = new SimpleStringProperty();
		taskFileName = new SimpleStringProperty();
		serviceFileName = new SimpleStringProperty();
		utilsFileName = new SimpleStringProperty();
		loggerFileName = new SimpleStringProperty();
		queryFileName = new SimpleStringProperty();
		settingsFileName = new SimpleStringProperty();

		imageRepo = new SimpleStringProperty();
		properties.addAll(Arrays.asList(projectName, projectDirName, packageName, pom_xml_fileName, app_java_fileName,
		        propertiesFileName, documentFileName, dataFileName, taskFileName, serviceFileName, utilsFileName,
		        loggerFileName, queryFileName, imageRepo, settingsFileName));
		for (SimpleStringProperty ssp : properties)
		{
			ssp.addListener((observable, oldValue, newValue) ->
			{
				dirty.setValue(true);
			});
		}
		clear();

		projectDirName.addListener((observable, oldValue, newValue) ->
		{
			projectName.setValue(FilenameUtils.getBaseName(newValue));
		});

		projectName.addListener((observable, oldValue, newValue) ->
		{
			String dirName = FilenameUtils.getBaseName(projectDirName.getValue());
			String firstPart = StringUtils.removeEnd(projectDirName.getValue(), dirName);
			String newName = firstPart + projectName.getValue();
			projectDirName.setValue(newName);
		});
	}

	public void clear()
	{
		projectName.setValue("");
		projectDirName.setValue("");
		propertiesFileName.setValue("");
		pom_xml_fileName.setValue("");
		app_java_fileName.setValue("");
		packageName.setValue("");
		documentFileName.setValue("");
		dataFileName.setValue("");
		taskFileName.setValue("");
		serviceFileName.setValue("");
		utilsFileName.setValue("");
		loggerFileName.setValue("");
		queryFileName.setValue("");
		imageRepo.setValue("");
		settingsFileName.setValue("");

		dirty.setValue(false);
	}

	public SimpleStringProperty getAppJavaFileName()
	{
		return app_java_fileName;
	}

	public String getAppJavaFileNameValue()
	{
		return app_java_fileName.getValue();
	}

	public SimpleBooleanProperty getDirty()
	{
		return dirty;
	}

	public Boolean getDirtyValue()
	{
		return dirty.getValue();
	}

	public SimpleStringProperty getPomXmFileName()
	{
		return pom_xml_fileName;
	}

	public String getPomXmlFileNameValue()
	{
		return pom_xml_fileName.getValue();
	}

	public SimpleStringProperty getProjectDirName()
	{
		return projectDirName;
	}

	public String getProjectDirNameValue()
	{
		return projectDirName.getValue();
	}

	public SimpleStringProperty getProjectName()
	{
		return projectName;
	}

	public String getProjectNameValue()
	{
		return projectName.getValue();
	}

	public SimpleStringProperty getPropertiesFileName()
	{
		return propertiesFileName;
	}

	public String getPropertiesFileNameValue()
	{
		return propertiesFileName.getValue();
	}

	public void readDoc(ObjectInputStream stream)
	{
		try
		{
			Boolean dirtyBool = (Boolean) stream.readObject();
			this.dirty.setValue(dirtyBool);
			String projectNameStr = (String) stream.readObject();
			this.projectName.setValue(projectNameStr);
			String packageNameStr = (String) stream.readObject();
			this.packageName.setValue(packageNameStr);
			String projectDirNameStr = (String) stream.readObject();
			this.projectDirName.setValue(projectDirNameStr);
			String pom_xml_fileNameStr = (String) stream.readObject();
			this.pom_xml_fileName.setValue(pom_xml_fileNameStr);
			String app_java_fileNameStr = (String) stream.readObject();
			this.app_java_fileName.setValue(app_java_fileNameStr);
			String propertiesFileNameStr = (String) stream.readObject();
			this.propertiesFileName.setValue(propertiesFileNameStr);
			String documentFileNameStr = (String) stream.readObject();
			this.documentFileName.setValue(documentFileNameStr);
			String dataFileNameStr = (String) stream.readObject();
			this.dataFileName.setValue(dataFileNameStr);
			String taskFileNameStr = (String) stream.readObject();
			this.taskFileName.setValue(taskFileNameStr);
			String serviceFileNameStr = (String) stream.readObject();
			this.serviceFileName.setValue(serviceFileNameStr);
			String utilsFileNameStr = (String) stream.readObject();
			this.utilsFileName.setValue(utilsFileNameStr);
			String loggerFileNameStr = (String) stream.readObject();
			this.loggerFileName.setValue(loggerFileNameStr);
			String queryStr = (String) stream.readObject();
			this.queryFileName.setValue(queryStr);
			String settingsFileNameStr = (String) stream.readObject();
			this.settingsFileName.setValue(settingsFileNameStr);
			String imageRepoStr = (String) stream.readObject();
			this.imageRepo.setValue(imageRepoStr);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void writeDoc(ObjectOutputStream stream)
	{
		try
		{
			stream.writeObject(dirty.getValue());
			stream.writeObject(projectName.getValue());
			stream.writeObject(packageName.getValue());
			stream.writeObject(projectDirName.getValue());
			stream.writeObject(pom_xml_fileName.getValue());
			stream.writeObject(app_java_fileName.getValue());
			stream.writeObject(propertiesFileName.getValue());
			stream.writeObject(documentFileName.getValue());
			stream.writeObject(dataFileName.getValue());
			stream.writeObject(taskFileName.getValue());
			stream.writeObject(serviceFileName.getValue());
			stream.writeObject(utilsFileName.getValue());
			stream.writeObject(loggerFileName.getValue());
			stream.writeObject(queryFileName.getValue());
			stream.writeObject(settingsFileName.getValue());
			stream.writeObject(imageRepo.getValue());

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setAppJavaFileName(SimpleStringProperty app_java_fileName)
	{
		this.app_java_fileName = app_java_fileName;
	}

	public void setAppJavaFileNameValue(String app_java_fileName)
	{
		this.app_java_fileName.setValue(app_java_fileName);
	}

	public void setDirty(SimpleBooleanProperty dirty)
	{
		this.dirty = dirty;
	}

	public void setDirtyValue(Boolean dirty)
	{
		this.dirty.setValue(dirty);
	}

	public void setPomXmlFileName(SimpleStringProperty pom_xml_fileName)
	{
		this.pom_xml_fileName = pom_xml_fileName;
		setDirtyValue(true);
	}

	public void setPomXmlFileNameValue(String pom_xml_fileName)
	{
		setDirtyValue(true);
		this.pom_xml_fileName.setValue(pom_xml_fileName);
	}

	public void setProjectDirName(SimpleStringProperty projectDirName)
	{
		this.projectDirName = projectDirName;
	}

	public void setProjectDirNameValue(String projectDirName)
	{
		this.projectDirName.setValue(projectDirName);
	}

	public void setProjectName(SimpleStringProperty projectName)
	{
		this.projectName = projectName;
	}

	public void setPropertiesFileName(SimpleStringProperty propertiesFileName)
	{
		this.propertiesFileName = propertiesFileName;
	}

	public void setPropertiesFileNameValue(String propertiesFileName)
	{
		this.propertiesFileName.setValue(propertiesFileName);
	}

	public SimpleStringProperty getPackageName()
	{
		return this.packageName;
	}

	// setters

	public void setPackageName(SimpleStringProperty p)
	{
		this.packageName = p;
	}

	public String getPackageNameValue()
	{
		return this.packageName.getValue();
	}

	public void setPackageName(String p)
	{
		this.packageName.setValue(p);
	}

	public SimpleStringProperty getDocumentFileName()
	{
		return this.documentFileName;
	}

	public SimpleStringProperty getDataFileName()
	{
		return this.dataFileName;
	}

	public SimpleStringProperty getTaskFileName()
	{
		return this.taskFileName;
	}

	public SimpleStringProperty getServiceFileName()
	{
		return this.serviceFileName;
	}

	public SimpleStringProperty getUtilsFileName()
	{
		return this.utilsFileName;
	}

	// setters

	public void setDocumentFileName(SimpleStringProperty p)
	{
		this.documentFileName = p;
	}

	public void setDataFileName(SimpleStringProperty p)
	{
		this.dataFileName = p;
	}

	public void setTaskFileName(SimpleStringProperty p)
	{
		this.taskFileName = p;
	}

	public void setServiceFileName(SimpleStringProperty p)
	{
		this.serviceFileName = p;
	}

	public void setUtilsFileName(SimpleStringProperty p)
	{
		this.utilsFileName = p;
	}

	public String getDocumentFileNameValue()
	{
		return this.documentFileName.getValue();
	}

	public String getDataFileNameValue()
	{
		return this.dataFileName.getValue();
	}

	public String getTaskFileNameValue()
	{
		return this.taskFileName.getValue();
	}

	public String getServiceFileNameValue()
	{
		return this.serviceFileName.getValue();
	}

	public String getUtilsFileNameValue()
	{
		return this.utilsFileName.getValue();
	}

	public void setDocumentFileNameValue(String p)
	{
		this.documentFileName.setValue(p);
	}

	public void setDataFileNameValue(String p)
	{
		this.dataFileName.setValue(p);
	}

	public void setTaskFileNameValue(String p)
	{
		this.taskFileName.setValue(p);
	}

	public void setServiceFileNameValue(String p)
	{
		this.serviceFileName.setValue(p);
	}

	public void setUtilsFileNameValue(String p)
	{
		this.utilsFileName.setValue(p);
	}

	public SimpleStringProperty getLoggerFileName()
	{
		return this.loggerFileName;
	}

	public void setLoggerFileName(SimpleStringProperty p)
	{
		this.loggerFileName = p;
	}

	public String getLoggerFileNameValue()
	{
		return this.loggerFileName.getValue();
	}

	public void setLoggerFileNameValue(String p)
	{
		this.loggerFileName.setValue(p);
	}

	public void setQueryFileName(SimpleStringProperty p)
	{
		this.queryFileName = p;
	}

	public void setImageRepo(SimpleStringProperty p)
	{
		this.imageRepo = p;
	}

	public String getQueryFileNameValue()
	{
		return this.queryFileName.getValue();
	}

	public String getImageRepoValue()
	{
		return this.imageRepo.getValue();
	}

	public SimpleStringProperty getQueryFileName()
	{
		return this.queryFileName;
	}

	public SimpleStringProperty getImageRepo()
	{
		return this.imageRepo;
	}

	public void setQueryFileNameValue(String p)
	{
		this.queryFileName.setValue(p);
	}

	public void setImageRepoValue(String p)
	{
		this.imageRepo.setValue(p);
	}

	public SimpleStringProperty getSettingsFileName()
	{
		return this.settingsFileName;
	}

	public void setSettingsFileName(SimpleStringProperty p)
	{
		this.settingsFileName = p;
	}

	public String getSettingsFileNameValue()
	{
		return this.settingsFileName.getValue();
	}

	public void setSettingsFileNameValue(String p)
	{
		this.settingsFileName.setValue(p);
	}

	@Override
	public String toString()
	{
		return "Document [dirty=" + dirty + ", projectName=" + projectName + ", projectDirName=" + projectDirName
		        + ", packageName=" + packageName + ", pom_xml_fileName=" + pom_xml_fileName + ", app_java_fileName="
		        + app_java_fileName + ", propertiesFileName=" + propertiesFileName + ", documentFileName="
		        + documentFileName + ", dataFileName=" + dataFileName + ", taskFileName=" + taskFileName
		        + ", serviceFileName=" + serviceFileName + ", utilsFileName=" + utilsFileName + ", loggerFileName="
		        + loggerFileName + ", queryFileName=" + queryFileName + ", imageRepo=" + imageRepo + ", properties="
		        + properties + "]";
	}

}
