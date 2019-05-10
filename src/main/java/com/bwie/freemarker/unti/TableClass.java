package com.bwie.freemarker.unti;

import lombok.Data;


import java.util.List;

/**
 * @author: admin
 * @Date: 2019/5/10 08:56
 * @Description:
 */
@Data
/**
 * 表
 */
public class TableClass {
    /** 数据库字段名称 **/
    private String tbaleName;
    /** 数据库字段首字母小写且去掉下划线字符串 **/
    private String changeTableName;
    /**
     * 每个表里面的字段
     */
    private List<ColumnClass> columnClasses;
}
