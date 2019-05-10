package com.bwie.freemarker;

import com.bwie.freemarker.unti.ColumnClass;
import com.bwie.freemarker.unti.Connectio;
import com.bwie.freemarker.unti.TableClass;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FreemarkerApplicationTests {

    @Value("${pagedir}")
    private  String pagedir;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Test
    public void contextLoads() throws SQLException, IOException, TemplateException {

        Map<String, List> tableNames = Connectio.getTableNames();
        List<String> newTableNames = tableNames.get("newTableNames");
        List<String> oldTableNames = tableNames.get("oldTableNames");

        List<TableClass> tableClasses = connectionMysql(newTableNames, oldTableNames);
        System.out.println(tableClasses);

        for (TableClass tableClass : tableClasses) {
           /* getPojo(tableClass);
            getDao(tableClass);*/
           getService(tableClass);
        }
    }

//生成pojo页面
    private boolean getPojo(TableClass tableClass) throws IOException, TemplateException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("pojo.ftl");
        Writer out=new FileWriter(pagedir+tableClass.getChangeTableName()+".java");
        template.process(tableClass, out);
        out.close();
        return true;
    }
    //生产dao层
    private boolean getDao(TableClass tableClass) throws IOException, TemplateException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("dao.ftl");
        Writer out=new FileWriter(pagedir+tableClass.getChangeTableName()+"Dao.java");
        template.process(tableClass, out);
        out.close();
        return true;
    }
    //生产Service层
    private boolean getService(TableClass tableClass) throws IOException, TemplateException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("service.ftl");
        Writer out=new FileWriter(pagedir+tableClass.getChangeTableName()+"Service.java");
        template.process(tableClass, out);
        out.close();
        return true;
    }

    //设置整个数据库所有的表
    private static List<TableClass> connectionMysql(List<String> newTableNames, List<String> oldTableNames){
        List<TableClass> objects = new ArrayList<>();
        for (int i = 0; i < oldTableNames.size(); i++) {
            TableClass tableClass = new TableClass();

            Map<String, List> columnNames = Connectio.getColumnNames(oldTableNames.get(i));
            List<String> columnComments = Connectio.getColumnComments(oldTableNames.get(i));
            List<String> newColumnNames = columnNames.get("newColumnNames");
            List<String> oldColumnNames = columnNames.get("oldColumnNames");
            List<String> columnTypes = Connectio.getColumnTypes(oldTableNames.get(i));
            List<ColumnClass> columnClasses=new ArrayList<>();
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
