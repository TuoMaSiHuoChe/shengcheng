package ${page}dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



import ${page}pojo.*;
import ${page}entity.*;

import ${tableClass.changeTableName};
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ${tableClass.changeTableName}Dao extends JpaRepository<${tableClass.changeTableName},String>,JpaSpecificationExecutor<${tableClass.changeTableName}>{
    ${tableClass.changeTableName} findOneById(String id);
}