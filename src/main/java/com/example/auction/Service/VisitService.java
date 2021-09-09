package com.example.auction.Service;

import com.example.auction.Dao.VisitDao;
import com.example.auction.Model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VisitService {
    @Autowired
    VisitDao visitDao;

    public Integer addVisit(Visit visit){
        return visitDao.addVisit(visit);
    }
    public List<Visit> list(Map map){
        return visitDao.list(map);
    }

    public List<Visit> getByUserId(Integer userid) {
        return visitDao.getByUserId(userid);
    }

    public List<Visit> getByUserIdAuctionIdCarId(Integer userid,Integer auctionid,Integer carid) {
        return visitDao.getByUserIdAuctionIdCarId(userid,auctionid,carid);
    }

    public Integer updateState(Integer userId) {
        return visitDao.updateState(userId,0);
    }
}
