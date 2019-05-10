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
        //获取项目根目录   “classpath：”
        String path = ResourceUtils.getURL("").getPath();
        System.out.println("path:" + path);
        //获得项目真正的目录
        path = path +"src/main/";
        //控制台输入想生成位置的包名
        Scanner scanner = new Scanner(System.in);
        //获得输入的字符创
        String next = scanner.next();
        //取到包名  并转为路径类型
        String s = next.replace(".", "/");

        //获得所有的表名
        Map<String, List> tableNames = Connectio.getTableNames();
        //驼峰之后的表名
        List<String> newTableNames = tableNames.get("newTableNames");
        //没有驼峰的表名
        List<String> oldTableNames = tableNames.get("oldTableNames");
        //调用方法完成对所有表数据的封装
        List<TableClass> tableClasses = connectionMysql(newTableNames, oldTableNames);
        System.out.println(tableClasses);
        /*getExction(path,path+"java/"+s+"/",next+".");*/
        //单个表生成
        for (TableClass tableClass : tableClasses) {
            //生成实体类  参数为 1.表信息  2.项目根路径  3.java下面的路径   4.根据输入获得的路径  用于后期模板     5.生成的模板后缀等信息
            getPojo(tableClass,path, path+"java/"+s+"/",next+".","pojo");
        }
    }


    //生成pojo页面
    private static boolean getPojo(TableClass tableClass, String path, String s, String next,String ftl) throws IOException, TemplateException {
        Configuration configuration=new Configuration(Configuration.getVersion());
        //2.设置模板所在的目录  path+位置
        configuration.setDirectoryForTemplateLoading(new File(path+"/resources/templates/"));
        //3.设置字符集
        configuration.setDefaultEncoding("utf-8");
        //获得模板
        Template template = configuration.getTemplate(ftl+".ftl");
        //得到文件目录   src/mian/java/com/bwie
        File f = new File(s + ftl+"/" );
        //判断存在目录
        if(!f.exists()){
            //创建目录
            f.mkdirs();
        }
        Writer out =null;
        /**
         * 判断是否为pojo类型  文件名的转换
         */
        if("pojo".equals(ftl)){
            /**
             *      是的话                src/mian/java/com/bwie/User
             */
            out=new FileWriter(s +ftl+ "/" + tableClass.getChangeTableName()+".java");
        }else{
            /**
             *                                            src/mian/java/com/bwie/Usercontroller
             *                                            src/mian/java/com/bwie/UserController
             */
            out = new FileWriter(s +ftl+ "/" + tableClass.getChangeTableName() + Character.toUpperCase(ftl.charAt(0))+ftl.substring(1)+".java");
        }
        //组装数据   表信息+import 导入
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        //加入表信息
        objectObjectHashMap.put("tableClass",tableClass);
        //加入路径
        objectObjectHashMap.put("page",next);
        //生成
        template.process(objectObjectHashMap, out);
        //关流
        out.close();
        //返回
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
        //创建一个集合装所有表信息
        List<TableClass> objects = new ArrayList<>();

        //根据表名循环
        for (int i = 0; i < oldTableNames.size(); i++) {

            //新建表信息
            TableClass tableClass = new TableClass();
            //拿到搜有的注释
            List<String> columnComments = Connectio.getColumnComments(oldTableNames.get(i));
            //获得所有的表字段
            Map<String, List> columnNames = Connectio.getColumnNames(oldTableNames.get(i));
            //获得所有的字段类型
            List<String> columnTypes = Connectio.getColumnTypes(oldTableNames.get(i));
            //拿到驼峰之后的字段名称
            List<String> newColumnNames = columnNames.get("newColumnNames");
            //拿到驼峰之前的字段名称
            List<String> oldColumnNames = columnNames.get("oldColumnNames");
            //组装该表所有字段
            List<ColumnClass> columnClasses = new ArrayList<>();
            //根据该表的字段进行循环  放入表字段类型  原始字段名称，现有字段名称   字段注释
            for (int i1 = 0; i1 < oldColumnNames.size(); i1++) {
                //实例字段表信息
                ColumnClass columnClass = new ColumnClass();
                //设置该字段类型
                columnClass.setColumnType(columnTypes.get(i1));
                //设置字段新名词  驼峰之后的
                columnClass.setChangeColumnName(newColumnNames.get(i1));
                //设置原始字段名称
                columnClass.setColumnName(oldColumnNames.get(i1));
                //设置字段注释
                columnClass.setColumnComment(columnComments.get(i1));
                //添加该字段
                columnClasses.add(columnClass);
            }
            //现在columnClasses里面放的就是本次循环的表的所有字段的详细信息
            //设置该表的原始名称
            tableClass.setTbaleName(oldTableNames.get(i));

            //获得更改之后的名称
            tableClass.setChangeTableName(newTableNames.get(i));
            //填充该表的字段详细信息   List集合
            tableClass.setColumnClasses(columnClasses);
            //把这个表加入集合   每次循环加一个表的信息  结束了所有变信息都在里面
            objects.add(tableClass);
        }
        //返回所有表信息
        return objects;
    }

}
