
package ${page}controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ${page}service.*;

import ${page}dao.*;
import ${page}pojo.*;
import ${page}entity.*;


/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/${tableClass.changeTableName}")
public class ${tableClass.changeTableName}Controller {

	@Autowired
	private ${tableClass.changeTableName}Service ${tableClass.changeTableName}Service;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",${tableClass.changeTableName}Service.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",${tableClass.changeTableName}Service.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<${tableClass.changeTableName}> pageList = ${tableClass.changeTableName}Service.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<${tableClass.changeTableName}>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",${tableClass.changeTableName}Service.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param ${tableClass.changeTableName}
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody ${tableClass.changeTableName} ${tableClass.changeTableName}  ){
		${tableClass.changeTableName}Service.add(${tableClass.changeTableName});
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param ${tableClass.changeTableName}
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody ${tableClass.changeTableName} ${tableClass.changeTableName}, @PathVariable String id ){
		${tableClass.changeTableName}.setId(id);
		${tableClass.changeTableName}Service.update(${tableClass.changeTableName});		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		${tableClass.changeTableName}Service.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
