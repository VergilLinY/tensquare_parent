package com.tensquare.article.service;

import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private IdWorker idWorker;

/**
	 * 查询全部列表
	 * @return
	 */
	public List<Article> findAll() {
		return articleDao.findAll();
	}

	/**
	 * 分页查询
	 *
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Article> findPage(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page-1, size);
		return articleDao.findAll(pageRequest);
	}


	/**
	 * 条件查询
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Article> findSearch(Map whereMap, int page, int size) {
		Specification<Article> specification = where(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return articleDao.findAll(specification, pageRequest);
	}

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Article findById(String id) {
	//先从redis中查询
		Article article = (Article) redisTemplate.opsForValue().get("article_"+id);

		if(article==null){//缓存中没有
			//从dao中查询
			article =	articleDao.findById(id).get();
			//将查询结果放入缓存
			//参数3 : 缓存有效时间长度
			//参数4 : 缓存有效时间单位
			redisTemplate.opsForValue().set("article_"+id,article,1, TimeUnit.HOURS);
		}

		return article;
	}

	/**
	 * 增加
	 * @param article
	 */
	public void add(Article article) {
		article.setId( idWorker.nextId()+"" );
		articleDao.save(article);
	}

	/**
	 * 修改
	 * @param article
	 */
	public void update(Article article) {

		//从redis缓存中移除
		redisTemplate.delete("article_"+article.getId());

		articleDao.save(article);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		//从redis缓存中移除
		redisTemplate.delete("article_"+id);
		articleDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Article> where(Map searchMap) {

		return new Specification<Article>() {

			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 专栏ID
                if (searchMap.get("columnid")!=null && !"".equals(searchMap.get("columnid"))) {
                	predicateList.add(cb.like(root.get("columnid").as(String.class), "%"+(String)searchMap.get("columnid")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 文章正文
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 文章封面
                if (searchMap.get("image")!=null && !"".equals(searchMap.get("image"))) {
                	predicateList.add(cb.like(root.get("image").as(String.class), "%"+(String)searchMap.get("image")+"%"));
                }
                // 是否公开
                if (searchMap.get("ispublic")!=null && !"".equals(searchMap.get("ispublic"))) {
                	predicateList.add(cb.like(root.get("ispublic").as(String.class), "%"+(String)searchMap.get("ispublic")+"%"));
                }
                // 是否置顶
                if (searchMap.get("istop")!=null && !"".equals(searchMap.get("istop"))) {
                	predicateList.add(cb.like(root.get("istop").as(String.class), "%"+(String)searchMap.get("istop")+"%"));
                }
                // 审核状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // 所属频道
                if (searchMap.get("channelid")!=null && !"".equals(searchMap.get("channelid"))) {
                	predicateList.add(cb.like(root.get("channelid").as(String.class), "%"+(String)searchMap.get("channelid")+"%"));
                }
                // URL
                if (searchMap.get("url")!=null && !"".equals(searchMap.get("url"))) {
                	predicateList.add(cb.like(root.get("url").as(String.class), "%"+(String)searchMap.get("url")+"%"));
                }
                // 类型
                if (searchMap.get("type")!=null && !"".equals(searchMap.get("type"))) {
                	predicateList.add(cb.like(root.get("type").as(String.class), "%"+(String)searchMap.get("type")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
		* 审核文章
		* @param id 文章id
		*/
	public void exmine(String id){
		articleDao.examine(id);
	}
	/**
		* 点赞文章
		* @param id 文章id
		*/
	public void thumbup(String id){
		articleDao.thumbup(id);
	}
}
