package cn.xzh.travel.service;

import cn.xzh.travel.dao.CategoryDao;
import cn.xzh.travel.pojo.Category;
import cn.xzh.travel.utils.JedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CategoryService {

    private CategoryDao categoryDao = new CategoryDao();

    public String findAllCategory() throws Exception {
        Jedis jedis = JedisUtil.getJedis();
        String jsonData = jedis.get("categoryList");
        if(jsonData==null || "".equals(jsonData.trim())){
            List<Category> categoryList = categoryDao.findAllCategory();
            jsonData = new ObjectMapper().writeValueAsString(categoryList);
            jedis.set("categoryList",jsonData);
        }
        return jsonData;
    }
}
