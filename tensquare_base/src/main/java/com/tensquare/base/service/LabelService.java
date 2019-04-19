package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {

    private final LabelDao labelDao;
    private final IdWorker idWorker;

    public LabelService(LabelDao labelDao, IdWorker idWorker) {
        this.labelDao = labelDao;
        this.idWorker = idWorker;
    }

    // 增
    public void save(Label label) {
        long id = idWorker.nextId();
        label.setId(id + "");
        labelDao.save(label);
    }

    // 删
    public void delete(String id) {
        labelDao.deleteById(id);
    }

    // 改
    public void update(String id, Label label) {
        label.setId(id + "");
        labelDao.save(label);
    }

    //根据id查询
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    //查询所有
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    // 条件查询
    public List<Label> findByCondition(Label label) {
        return labelDao.findAll(generateSpecification(label));
    }

    // 分页条件查询
    public PageResult<Label> findPageByCondition(Integer page, Integer size, Label label) {
        //调用JPA的分页查询
        Page<Label> labelPage = labelDao.findAll(generateSpecification(label), PageRequest.of(page, size));  //注意页码page从零开始
        return new PageResult<>(labelPage.getTotalElements(), labelPage.getContent());
    }

    //抽取的公共方法用于构造条件查询所需的Specification对象
    private Specification<Label> generateSpecification(Label label) {
        //lambda表达式重写构造方法
        return (Specification<Label>) (root, criteriaQuery, criteriaBuilder) -> {
            //保存后续拼装的条件
            List<Predicate> predicates = new ArrayList<>();
            //labelName条件的查询
            if (!StringUtils.isEmpty(label.getLabelname())) {
                //非空后拼装条件
                Predicate p = criteriaBuilder.like(root.get("labelname").as(String.class), label.getLabelname());
                predicates.add(p);
            }
            //state条件的查询
            if (!StringUtils.isEmpty(label.getState())) {
                //非空后拼装条件
                Predicate p = criteriaBuilder.equal(root.get("state").as(String.class), label.getState());
                predicates.add(p);
            }
            //recommend条件的查询
            if (!StringUtils.isEmpty(label.getRecommend())) {
                //非空后拼装条件
                Predicate p = criteriaBuilder.equal(root.get("recommend").as(String.class), label.getRecommend());
                predicates.add(p);
            }

            if (predicates.isEmpty()) {
                return null;
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
