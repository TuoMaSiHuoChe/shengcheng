import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ${changeTableName}Service {

	@Autowired
	private ${changeTableName}Dao ${changeTableName}Dao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<${changeTableName}> findAll() {
		return ${changeTableName}Dao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<${changeTableName}> findSearch(Map whereMap, int page, int size) {
		Specification<${changeTableName}> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return ${changeTableName}Dao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<${changeTableName}> findSearch(Map whereMap) {
		Specification<${changeTableName}> specification = createSpecification(whereMap);
		return ${changeTableName}Dao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public ${changeTableName} findById(String id) {
		return ${changeTableName}Dao.findOneById(id);
	}

	/**
	 * 增加
	 * @param ${changeTableName}
	 */
	public void add(${changeTableName} ${changeTableName}) {

		${changeTableName}Dao.save(${changeTableName});
	}

	/**
	 * 修改
	 * @param ${changeTableName}
	 */
	public void update(${changeTableName} ${changeTableName}) {
		${changeTableName}Dao.save(${changeTableName});
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		${changeTableName}Dao.deleteById(id);
	}
/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<${changeTableName}> createSpecification(Map searchMap) {

		return new Specification<${changeTableName}>() {

			@Override
			public Predicate toPredicate(Root<${changeTableName}> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
				 <#list columnClasses as x>

				if (searchMap.get("${x.changeColumnName}") != null && !"".equals(searchMap.get("${x.changeColumnName}"))) {
   					predicateList.add(cb.like(root.get("${x.changeColumnName}").as(String.class), "%" + (String) searchMap.get("${x.changeColumnName}") + "%"));
    			}


				 </#list>

        return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

        }
        };

        }

	

}