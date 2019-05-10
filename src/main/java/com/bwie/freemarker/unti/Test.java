package com.bwie.freemarker.unti;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.*;

/**
 * @author: admin
 * @Date: 2019/5/10 11:03
 * @Description:
 */
public class Test {


    public static void main(String[] args) throws IOException, SQLException, TemplateException {
        //获取跟目录
        String path = ResourceUtils.getURL("").getPath();

        System.out.println("path:" + path);


//在打包成jar正式发布时，得到的地址为：{发布jar包目录}/static/images/upload/
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        String s = next.replace(".", "/");
        path = path +"src/main/";
        System.out.println(next);

        Map<String, List> tableNames = Connectio.getTableNames();
        List<String> newTableNames = tableNames.get("newTableNames");
        List<String> oldTableNames = tableNames.get("oldTableNames");

        List<TableClass> tableClasses = connectionMysql(newTableNames, oldTableNames);
        System.out.println(tableClasses);
        /*getExction(path,path+"java/"+s+"/",next+".");*/
        for (TableClass tableClass : tableClasses) {
            getPojo(tableClass,path, path+"java/"+s+"/",next+".","pojo");
        }
    }


    //生成pojo页面
    private static boolean getPojo(TableClass tableClass, String path, String s, String next,String ftl) throws IOException, TemplateException {
        Configuration configuration=new Configuration(Configuration.getVersion());
        //2.设置模板所在的目录
        configuration.setDirectoryForTemplateLoading(new File(path+"/resources/templates/"));
        //3.设置字符集
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate(ftl+".ftl");
        File f = new File(s + ftl+"/" );
        if(!f.exists()){

            f.mkdirs();//创建目录
        }
        Writer out = new FileWriter(s +ftl+ "/" + tableClass.getChangeTableName() + Character.toUpperCase(ftl.charAt(0))+ftl.substring(1)+".java");
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("tableClass",tableClass);
        objectObjectHashMap.put("page",next);
        template.process(objectObjectHashMap, out);
        out.close();
        return true;
    }


/*
    //生成异常爆出
    private static boolean getExction(String path, String s, String next) throws IOException, TemplateException {
        Configuration configuration=new Configuration(Configuration.getVersion());
        //2.设置模板所在的目录
        configuration.setDirectoryForTemplateLoading(new File(s+"/resources/templates/"));
        //3.设置字符集
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("ection.ftl");
        File f = new File(next + "controller/" );
        if(!f.exists()){

            f.mkdirs();//创建目录
        }


        Writer out = new FileWriter(next + "controller/" + "BaseExceptionHandler.java");
        template.process(null, out);
        out.close();
        return true;
    }*/

    //设置整个数据库所有的表
    private static List<TableClass> connectionMysql(List<String> newTableNames, List<String> oldTableNames) {
        List<TableClass> objects = new ArrayList<>();
        for (int i = 0; i < oldTableNames.size(); i++) {
            TableClass tableClass = new TableClass();

            Map<String, List> columnNames = Connectio.getColumnNames(oldTableNames.get(i));
            List<String> columnComments = Connectio.getColumnComments(oldTableNames.get(i));
            List<String> newColumnNames = columnNames.get("newColumnNames");
            List<String> oldColumnNames = columnNames.get("oldColumnNames");
            List<String> columnTypes = Connectio.getColumnTypes(oldTableNames.get(i));
            List<ColumnClass> columnClasses = new ArrayList<>();
            for (int i1 = 0; i1 < oldColumnNames.size(); i1++) {
                ColumnClass columnClass = new ColumnClass();
                columnClass.setColumnType(columnTypes.get(i1));
                columnClass.setChangeColumnName(newColumnNames.get(i1));
                columnClass.setColumnName(oldColumnNames.get(i1));
                columnClass.setColumnComment(columnComments.get(i1));
                columnClasses.add(columnClass);
            }
            tableClass.setTbaleName(oldTableNames.get(i));
            tableClass.setChangeTableName(newTableNames.get(i));
            tableClass.setColumnClasses(columnClasses);
            objects.add(tableClass);
        }
        return objects;
    }

}
