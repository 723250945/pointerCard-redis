package cn.jbit.biz.bizImpl;

import cn.jbit.biz.CardsBiz;
import cn.jbit.dao.CardPcitrueDao;
import cn.jbit.dao.CardsDao;
import cn.jbit.dao.PraiseRateDao;
import cn.jbit.entity.CardPcitrue;
import cn.jbit.entity.Cards;
import cn.jbit.entity.PraiseRate;
import cn.jbit.util.Browse;
import cn.jbit.util.FastJSONUtil;
import cn.jbit.util.Pages;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("cardBiz")
public class CardsBizImpl implements CardsBiz {

    @Resource(name = "cardsDao")
    private CardsDao cardsDao;
    @Resource(name = "cardPcitrueDao")
    private CardPcitrueDao cardPcitrueDao;
    @Resource(name = "praiseRateDao")
    private PraiseRateDao praiseRateDao;


    /**
     * 分页查询点卡信息
     * @param typeId
     * @param index
     * @param pagesize
     * @return
     */
    public Pages<Cards> searchPage(int typeId,int gid,String searchInfo,int index,int pagesize) {
        Pages<Cards> cardsPages=new Pages<Cards>();
        cardsPages.setPageSzie(pagesize);
        cardsPages.setCurrPageNo(index);
        //获取总行数
        int coutsize=cardsDao.getcount(typeId,gid,searchInfo);
        cardsPages.setTotalCount(coutsize);
        //获取当前页数据起始点
        index=(index-1)*pagesize;
        cardsPages.setNewList(cardsDao.searchList(typeId,gid,searchInfo,index,pagesize));
        return cardsPages;
    }

    /**
     * 查询最新上架的点卡信息前十名
     * @return
     */
    public List<Cards> searchNewTime() {
        return cardsDao.searchNewTime(10);
    }

    /**
     * 根据点卡id查找点卡子集图标
     * @param cid
     * @return
     */
    public List<CardPcitrue> searchByCid(int cid) {
        return cardPcitrueDao.findByCid(cid);
    }

    /**
     * 根据点卡id查找评论信息
     * @param cid
     * @return
     */
    public List<PraiseRate> searchPraiseRate(int cid) {
        return praiseRateDao.findByCid(cid);
    }

    /**
     * 更新点卡信息
     * @param cards
     * @return
     */
    public int updateCards(Cards cards) {
        return cardsDao.updateCards(cards);
    }

    /**
     * 查找单个点卡信息必须要cid和gid两个参数才会返回唯一结果
     * @param cards
     * @return
     */
    public Cards searchCard(Cards cards) {
        return cardsDao.searchByInfo(cards);
    }

    /**
     * 查找用户浏览记录的点卡信息
     * @return
     */
    public List<Cards> searchBrowse() {
        List<Cards> cardsList=new ArrayList<Cards>();
        if(Browse.records.size()>0){
            for (Cards c:Browse.records) {
                if(null!=c){
                    Cards cards=cardsDao.searchByInfo(c);
                    cardsList.add(cards);
                }
            }
        }
        return cardsList;
    }


}
