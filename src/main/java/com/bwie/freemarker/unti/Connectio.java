package com.bwie.freemarker.unti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: admin
 * @Date: 2019/5/10 07:41
 * @Description:
 */
public class Connectio {


    private final static Logger LOGGER = LoggerFactory.getLogger(Connectio.class);


    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/pinyougoudb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    /**
     * 数据库操作
     */

    private static final String SQL = "SELECT * FROM ";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            LOGGER.error("get connection failure", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
            }
        }
    }

    /**
     * 获取到所有的表名
     */

    public static Map<String, List> getTableNames() throws SQLException {
        List<String> newTableNames = new ArrayList<>();
        List<String> oldTableNames = new ArrayList<>();
        Map<String, List> objectObjectHashMap = new HashMap<>();
        Connection conn = getConnection();
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = null;

        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[]{"TABLE"});
            System.out.println(rs);
            while (rs.next()) {
                String s = underlineToCamel(rs.getString(3));
                newTableNames.add(s);
                oldTableNames.add(rs.getString(3));
                System.out.println("数据库所有表显示：");
                System.out.println(s);
            }
        } catch (SQLException e) {
            LOGGER.error("getTableNames failure", e);
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                LOGGER.error("close ResultSet failure", e);
            }
        }
        objectObjectHashMap.put("newTableNames", newTableNames);
        objectObjectHashMap.put("oldTableNames", oldTableNames);
        return objectObjectHashMap;

    }


    /**
     * 获取表中所有字段名称
     *
     * @param tableName 表名
     * @return
     */
    public static  Map<String, List> getColumnNames(String tableName) {
        List<String> newColumnNames = new ArrayList<>();
        List<String> oldColumnNames = new ArrayList<>();
        Map<String, List> ColumnNames = new HashMap<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                String columnName = rsmd.getColumnName(i + 1);
                String s = underlineToCamel(columnName);
                oldColumnNames.add( rsmd.getColumnName(i + 1));
                newColumnNames.add(s);
            }
        } catch (SQLException e) {
            LOGGER.error("getColumnNames failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnNames close pstem and connection failure", e);
                }
            }
        }
        ColumnNames.put("newColumnNames", newColumnNames);
        ColumnNames.put("oldColumnNames", oldColumnNames);
        return ColumnNames;
    }


    /**
     * 获取表中所有字段类型
     * @param tableName
     * @return
     */
    public static List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                String s = dbTypeChangeJavaType(rsmd.getColumnTypeName(i + 1));
                columnTypes.add(s);
            }
        } catch (SQLException e) {
            LOGGER.error("getColumnTypes failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnTypes close pstem and connection failure", e);
                }
            }
        }
        return columnTypes;
    }



    /**
     * 下划线串转为驼峰
     */
    private static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 获取表中字段的所有注释
     * @param tableName
     * @return
     */
    public static List<String> getColumnComments(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        List<String> columnComments = new ArrayList<>();//列名注释集合
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnComments close ResultSet and connection failure", e);
                }
            }
        }
        return columnComments;
    }

    /**
     * 讲数据库的字段转化成java字段
     * @param dbTypeName
     * @return
     */
    public static String dbTypeChangeJavaType(String dbTypeName){
        String javaType=null;
        switch(dbTypeName){
            case "VARCHAR" :javaType="String";break;
            case "BIGINT" :javaType="Long";break;
            case "INT" :javaType="Integer";break;
            case "DATETIME" :javaType="Date";break;
            default:javaType="String";break;
        }
        return javaType;
    }



}
