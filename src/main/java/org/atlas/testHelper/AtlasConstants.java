package org.atlas.testHelper;

public class AtlasConstants {

	public final static int PAGELOAD_TIMEOUT_THRESHOLD = 10;
	public final static int DRIVER_TIMEOUT = 30;
	
	public static long START_TIME;
	
//	public final static String UI_URL = "http://52.27.169.250:3172";
//	public final static String UI_URL = "http://52.27.169.250:3208";
//	public final static String UI_URL = "http://52.27.169.250:3228";
//	public final static String UI_URL = "http://162.212.133.190:3285";
//	public final static String UI_URL = "http://162.212.133.190:3228";
//	public final static String UI_URL = "http://162.212.133.190:3100/";
	public final static String UI_URL = "http://162.212.133.190:3263/";

	public final static String SEARCH_STRING = "searchDataFeed";
	public final static String SEARCH_TABLE_HEADERS = "searchTableHeaders";
	
	public final static String BUTTON_ATTRIBUTE_AS_DISBALED = "disabled";
	public final static String PWD = System.getProperty("user.dir");
	public final static String SCREENSHOTS_DIR = PWD + "\\test-output\\screenshots\\";
	public final static String REPORT_FILE_PATH = PWD + "\\test-output\\emailable-report.html";
	public final static String CHROME_DRIVER_PATH = PWD + "\\src\\test\\resource\\chromedriver.exe";
	
	public static final String LINK_CLICKED = "link clicked";
	public static final String BROWSER_TYPE = "firefox";
//	public static final String BROWSER_TYPE = "chrome";
	
	/*
	"Column+select+Column.name",
	"from+DB+select+DB.name",
	"from Table select Table.name",
	"from table select owner='joe'",
	"from hive_table select tableName, owner",
	"from hive_table select tableName"*/
	
	/*Column select Column.name 
	from DB select DB.name
	from Table select Table.name
	from Table select Table - 
	from Table select Table.name , Table.owner 
	from Table select Table.name, Table.owner, Table.creteTime -
	from DB select DB.name, DB.createTime
	from DB select DB.name=‘sales'
	from DB select DB.name=‘Sales’ 
	Table select name,owner,createTime
	from Table select Table.name, Table.owner 
	from table select owner='joe'*/
}
