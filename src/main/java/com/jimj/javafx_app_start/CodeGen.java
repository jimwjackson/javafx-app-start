package com.jimj.javafx_app_start;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.ContextTool;

public class CodeGen
{

	private Document doc;

	public CodeGen(Document doc)
	{
		super();
		this.doc = doc;
	}

	public void gen() throws Exception
	{
		if ((doc.getProjectDirName() == null) || (doc.getProjectDirNameValue().length() == 0))
			throw new IllegalArgumentException(doc.getProjectDirName().getName() + " is empty !!!");
		FileUtils.deleteDirectory(new File(doc.getProjectDirNameValue()));
		FileUtils.forceMkdir(new File(doc.getProjectDirNameValue()));
		String srcMainJava = doc.getProjectDirNameValue() + File.separator + "src" + File.separator + "main"
		        + File.separator + "java";
		FileUtils.forceMkdir(new File(srcMainJava));
		String srcTestJava = doc.getProjectDirNameValue() + File.separator + "src" + File.separator + "test"
		        + File.separator + "java";
		FileUtils.forceMkdir(new File(srcTestJava));
		String srcMainResourcesImg = doc.getProjectDirNameValue() + File.separator + "src" + File.separator + "main"
		        + File.separator + "resources" + File.separator + "img";
		FileUtils.forceMkdir(new File(srcMainResourcesImg));

		String projectNameTitle = WordUtils.capitalize(doc.getProjectNameValue().replaceAll("[^a-zA-Z0-9]", "")
		        .toLowerCase());

		genPomXmlFile();
		genJavaFile(doc.getAppJavaFileNameValue(), null);
		genJavaFile(doc.getDocumentFileNameValue(), projectNameTitle + "Document");
		genJavaFile(doc.getDataFileNameValue(), projectNameTitle + "Data");
		genJavaFile(doc.getLoggerFileNameValue(), projectNameTitle + "Logger");
		genJavaFile(doc.getServiceFileNameValue(), projectNameTitle + "Service");
		genJavaFile(doc.getTaskFileNameValue(), projectNameTitle + "Task");
		genJavaFile(doc.getQueryFileNameValue(), projectNameTitle + "Query");
		genJavaFile(doc.getSettingsFileNameValue(), projectNameTitle + "Settings");
		genJavaFile(doc.getUtilsFileNameValue(), projectNameTitle + "Utils");

		File srcBuildPropertiesFile = new File("src" + File.separator + "main" + File.separator + "resources"
		        + File.separator + "build.properties");
		File destBuildPropertiesFile = new File(doc.getProjectDirNameValue() + File.separator + "src" + File.separator
		        + "main" + File.separator + "resources" + File.separator + "build.properties");
		FileUtils.copyFile(srcBuildPropertiesFile, destBuildPropertiesFile);

		FileUtils.copyFileToDirectory(new File("java-format.xml"), new File(doc.getProjectDirNameValue()));

		File appFile = new File(srcMainJava + File.separator
		        + FilenameUtils.getBaseName(WordUtils.capitalize(doc.getAppJavaFileNameValue())) + ".java");
		List<String> appTeplateLines = FileUtils.readLines(appFile, StandardCharsets.UTF_8);
		for (String line : appTeplateLines)
		{
			Matcher m = patternImageResource.matcher(line);
			if (m.find())
			{
				File srcImageFile = new File(doc.getImageRepoValue() + File.separator + m.group(1));
				File destImageDir = new File(doc.getProjectDirNameValue() + File.separator + "src" + File.separator
				        + "main" + File.separator + "resources" + File.separator + "img");
				FileUtils.copyFileToDirectory(srcImageFile, destImageDir);
			}
		}

	}

	/*
	 * Image imageOpen = new
	 * Image(getClass().getResourceAsStream("/img/page_edit.png"));
	 */

	private static Pattern patternImageResource = Pattern.compile("getResourceAsStream\\(\\\"/img/(.+)\\\"\\)");

	public void genPomXmlFile() throws Exception
	{
		FileWriter writer = new FileWriter(new File(doc.getProjectDirNameValue() + File.separator + "pom.xml"));
		try
		{
			CodeGen gen = new CodeGen(doc);
			gen.applyTemplate(doc.getPomXmlFileNameValue(), doc.getPropertiesFileNameValue(), writer);
			writer.flush();
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void genJavaFile(String fileName, String javaFileName) throws Exception
	{
		if (javaFileName == null)
			javaFileName = fileName;
		FileWriter writer = new FileWriter(new File(doc.getProjectDirNameValue() + File.separator + "src"
		        + File.separator + "main" + File.separator + "java" + File.separator
		        + WordUtils.capitalize(FilenameUtils.getBaseName(javaFileName)) + ".java"));
		try
		{
			CodeGen gen = new CodeGen(doc);
			gen.applyTemplate(fileName, doc.getPropertiesFileNameValue(), writer);
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static Pattern propertyListPattern = Pattern.compile("[_A-Za-z0-9]+_[1-9]+");

	public VelocityContext applyTemplate(String templateFileName, String propertiesFileName, Writer writer)
	        throws Exception
	{
		ContextTool contextTool = new ContextTool();
		contextTool.getKeys();
		Map<String, Object> result = new HashMap<String, Object>();
		ZonedDateTime now = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
		String formattedNow = now.format(formatter);
		Velocity.init();
		VelocityContext context = new VelocityContext();
		context.put("now", new String(formattedNow));
		context.put("template", templateFileName);
		context.put("properties", propertiesFileName);
		context.put("projectName", doc.getProjectNameValue());
		context.put("packageName", doc.getPackageNameValue());
		context.put("artifactId", doc.getProjectNameValue().replaceAll("[^a-zA-Z0-9]", "").toLowerCase()); // remove
		                                                                                                   // non
		                                                                                                   // alphabetic
		context.put("projectNameTitle",
		        WordUtils.capitalize(doc.getProjectNameValue().replaceAll("[^a-zA-Z0-9]", "").toLowerCase()));

		File tf = new File(templateFileName);
		if (tf.exists())
		{
			Instant lastModifiedInstant = Instant.ofEpochMilli(tf.lastModified());
			ZonedDateTime lastModifiedTime = ZonedDateTime.ofInstant(lastModifiedInstant, ZoneId.systemDefault());
			String formattedLastModified = lastModifiedTime.format(formatter);
			context.put("templateLastModified", new String(formattedLastModified));
		}
		context.put("properties", propertiesFileName);

		Map<String, ArrayList<Map<String, String>>> propertiesMap = new HashMap<String, ArrayList<Map<String, String>>>();
		// ArrayList<ArrayList<Pair<String, String>>> propertiesArray = new
		// ArrayList<ArrayList<Pair<String, String>>>();

		// inner map = keys of field_name , field_type , field_label,
		// field_required , field_description
		// array = maps of field properties
		// outer map = arrays of field properties maps

		Properties properties = new Properties();
		File propertiesFile = new File(propertiesFileName);
		FileInputStream fis = new FileInputStream(propertiesFile);
		properties.load(fis);
		List<PropertyEntry> propertiesList = new ArrayList<PropertyEntry>();
		for (Entry<Object, Object> entry : properties.entrySet())
		{
			String name = entry.getKey().toString();
			String value = entry.getValue().toString();
			context.put(name, value);
			result.put(name, value);
			Matcher matcherListProperty = propertyListPattern.matcher(name);
			if (matcherListProperty.find())
			{
				String outerMapKey = StringUtils.left(name, StringUtils.indexOf(name, "_"));
				String strip = name.substring(StringUtils.indexOf(name, "_") + 1, StringUtils.lastIndexOf(name, "_"));
				String innerMapKey = strip;
				int index = StringUtils.lastIndexOf(name, "_");
				index = Integer.parseInt(StringUtils.substring(name, index + 1));
				PropertyEntry e = new PropertyEntry(outerMapKey, innerMapKey, index, name, value);
				propertiesList.add(e);
			}
		}
		Collections.sort(propertiesList);
		int prevIndex = 0;
		String outerMapPrev = "";

		// Properties props = new Properties();
		for (PropertyEntry entry : propertiesList)
		{
			// String name = entry.getName();
			String value = entry.getValue();
			// props.put(name, value);
			String outerMapKey = entry.getCatName();
			String innerMapKey = entry.getFieldName();
			int index = entry.getIndex();
			ArrayList<Map<String, String>> array = null;
			if (!outerMapKey.equals(outerMapPrev))
			{
				array = new ArrayList<Map<String, String>>();
				propertiesMap.put(outerMapKey, array);
				outerMapPrev = outerMapKey;
				prevIndex = 0;

			}
			else if (propertiesMap.containsKey(outerMapKey))
			{
				array = propertiesMap.get(outerMapKey);
			}
			else
			{
				array = new ArrayList<Map<String, String>>();
				propertiesMap.put(outerMapKey, array);
			}
			Map<String, String> map = null;
			if (prevIndex == 0)
			{
				map = new HashMap<String, String>();
				array.add(map);
			}
			else if (index != prevIndex)
			{
				map = new HashMap<String, String>();
				array.add(map);
			}
			else
			{
				if (array == null)
				{
					throw new IllegalStateException("array is null !!" + " index:" + index + " prevIndex = "
					        + prevIndex + " entry = " + entry);
				}
				if (array.size() < index)
				{
					throw new IllegalStateException("array is smaller than index !!" + " index:" + index
					        + " prevIndex = " + prevIndex + " entry = " + entry);
				}
				map = array.get(index - 1);
			}
			prevIndex = index;
			map.put(innerMapKey, value);
		}

		// result.put("proprties", props);
		context.put("propertiesMap", propertiesMap);
		result.put("propertiesMap", propertiesMap);

		fis.close();

		Properties systemProperties = System.getProperties();
		Properties sys = new Properties();
		for (Entry<Object, Object> entry : systemProperties.entrySet())
		{
			String name = ((String) entry.getKey()).replaceAll("[^a-zA-Z0-9]", "").toLowerCase(); // remove
			                                                                                      // non
			                                                                                      // alphabetic;
			String value = systemProperties.getProperty(entry.getKey().toString());
			context.put(name, value);
			sys.put(name, value);
		}
		result.put("system", sys);
		Template template = Velocity.getTemplate(templateFileName);
		template.merge(context, writer);
		return context;
	}
}
