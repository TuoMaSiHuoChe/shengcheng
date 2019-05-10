
import lombok.Data;
import org.hibernate.annotations.Table;

@Data
@Table(name="${tbaleName}")
public class ${changeTableName} implements Serializable {

    <#list columnClasses as x>

    /**
    *  ${x.columnComment}
    */
     private ${x.columnType} ${x.columnName};


    </#list>


}
