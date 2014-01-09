package prepare;


public class Property {
    public static String FILEPATH_OUT = "i:/tmp/";
    public static final String url = "jdbc:mysql://localhost:3306/";
    public static String FILEPATH_TEST = "i:/tmp/test";
    public static String FILENAME_DATASET = "dataset";

    public static String[] symbols = new String[] { "[", "]", "\\", "/", "(",
	    ")", "'", "_" };
    public static String[] deleteStrings = new String[] { "{@inheritDoc}",
	    "& nbsp", "& lt", "& gt" };
    public static String StringForWell = " # ";
    public static String StringForDash = " - ";

    public static String[] deleteSentenceString = { "org/omg" };

    public static final int PACKAGE = 0;
    public static final int CLASS = 1;
    public static final int METHOD = 2;
    public static final int CTOR = 3;
    public static final int FIELD = 4;
    public static final int METHOD_AND_CTOR = 5;
    public static int MAX_ITERATOR_CHANGE = 5;
    public static int MAX_ITERATOR_ADD = 4;
    public static int MAX_ITERATOR_DELETE = 5;
    public static int MAX_CLUSTERS_CHANGE = 80;
    public static int MIN_CLUSTERS_CHANGE = 2;
    public static int MAX_CLUSTERS_ADD = 80;
    public static int MIN_CLUSTERS_ADD = 10;
    public static int MAX_CLUSTERS_DELETE = 80;
    public static int MIN_CLUSTERS_DELETE = 2;
    public static String TABLE_FREQ_NAME = "freq_array";

    public static final double CHANGE_THRESHOLD = 1.0;

    public static final double TOTAL_WEIGHT = 0.2;
    public static final double DELELE_WEIGHT = 0.3;
    public static final double INSERT_WEIGHT = 0.5;

    public static final String v1 = "Version_1.2";
    public static final String v2 = "Version_1.3";
    public static final String v3 = "Version_1.4";
    public static final String v4 = "Version_5";
    public static final String v5 = "Version_6";

    public static final String TABLE_NAME = "stat_docdiff_";
    public static final String TABLE_API_NAME = "api";
    public static final String TABLE_MERGE_NAME = "multi_in_1";
    public static final String DELIM = " .,;:?!'(){}[]\"~@#$%^&*+=_-|\\<>/";
    public static final String DELIM2 = " .,;:?!'(){}[]\"~@#$%^&*+=_-|\\<>/\t\r\f\n";
    public static final String DELIM3 = " .,;:?!(){}[]\"'~@#$%^&*+=_-|\\<>/\n";

    public static final String SELECTEDPKG = "javax.swing";
    
    /**
     * same to build.xml jdiff old new
     */
    
    public static final String[] versions_jfreechart = new String[]{"jfreechart_1_0_9", "jfreechart_1_0_10", "jfreechart_1_0_11", "jfreechart_1_0_12", "jfreechart_1_0_13"};
    
    public static final String[] versions_log4j = new String[]{"log4j_1_2_12", "log4j_1_2_13", "log4j_1_2_14", "log4j_1_2_15", "log4j_1_2_16"};
    
    public static final String[] versions_struts = new String[] {"struts_2_0_14","struts_2_1_2","struts_2_1_6","struts_2_1_8","struts_2_1_8_1"};
    
    public static final String[] versions_lucene = new String[] { "lucene_2_9_0","lucene_2_9_1","lucene_2_9_2","lucene_3_0_0" ,"lucene_3_0_1"};
    
    public static final String[] versions_util = new String[] { "java_util_1_2",
	    "java_util_1_3", "java_util_1_4", "java_util_1_5", "java_util_1_6" };

    public static final String[] versions_active = new String[]{"activemq_5_0_0","activemq_5_1_0","activemq_5_2_0","activemq_5_3_0","activemq_5_3_1"};
    
    public static final String[] versions = new String[] { "Version_2",
	    "Version_3", "Version_4", "Version_5", "Version_6" };

    public static final String driverClass = "com.mysql.jdbc.Driver";
    
    //public static final String url_j2se = "jdbc:mysql://localhost:3306/api_doc_changes";
    public static final String user = "root";
    public static final String pwd = "123456";
    public static final int TOPNUM = 150;
    public static final boolean WRITE_ADD_DELETE_CHANGE_TO_DB = false;
    public static final double TOTAL_ELEMENTS = 209276;
    public static final boolean MULTIVERSION = true;
    public static final boolean DATABASE = true;
    public static final boolean STATISTIC = true;
    public static final int ADD = 0;
    public static final int DELELE = 1;
    public static final int CHANGE = 2;
    public static final float similarity =  0.85f;
    public static final String AMDTableName = "stat_AMD_versions";

}
