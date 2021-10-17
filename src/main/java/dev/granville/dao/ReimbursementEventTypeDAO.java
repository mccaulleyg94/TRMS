package dev.granville.dao;

import dev.granville.beans.ReimbursementEventType;
import dev.granville.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;

public class ReimbursementEventTypeDAO implements ReimbursementEventTypeDAOInterface {

    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();
    private final Logger log = Logger.getLogger(ReimbursementEventTypeDAO.class);

    @Override
    public ReimbursementEventType add(ReimbursementEventType reimbursementEventType) {
        return null;
    }

    @Override
    public ReimbursementEventType delete(ReimbursementEventType reimbursementEventType) {
        return null;
    }

    @Override
    public ReimbursementEventType update(ReimbursementEventType reimbursementEventType) {
        return null;
    }

    @Override
    public ReimbursementEventType getById(Integer id) {
        try {
            Session s = hu.getSession();
            ReimbursementEventType ret = s.get(ReimbursementEventType.class, id);
            s.close();
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public ReimbursementEventType getRandom() {
        try (Session s = hu.getSession()) {
            Random rand = new Random();
            String query = "FROM ReimbursementEventType";
            Query<ReimbursementEventType> q = s.createQuery(query, ReimbursementEventType.class);
            List<ReimbursementEventType> eventTypeList = q.getResultList();
            return eventTypeList.get(rand.nextInt(eventTypeList.size()));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<ReimbursementEventType> getAll() {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
