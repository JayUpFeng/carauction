package com.example.auction.Dao;

import com.example.auction.Model.CarCopy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @作者：zhanghe
 * @时间：2021-09-10 09:18:28
 * @注释：
 */
@Mapper
public interface CarCopyDao {
    int save(CarCopy carCopy);
    List<CarCopy> carCopyList(Map<String,Object> map);
    int getById(Integer id);
}
