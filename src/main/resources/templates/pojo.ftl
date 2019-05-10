package ${page}pojo;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import lombok.Data;
import java.util.Date;



import ${page}entity.*;
@Data
@Table(name="${tableClass.tbaleName}")
@Entity
public class ${tableClass.changeTableName} implements Serializable {

    <#list tableClass.columnClasses as x>

    /**
    *  ${x.columnComment}
    */
     private ${x.columnType} ${x.columnName};


    </#list>


}
