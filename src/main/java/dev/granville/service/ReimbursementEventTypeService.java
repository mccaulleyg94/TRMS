package dev.granville.service;

import dev.granville.beans.ReimbursementEventType;
import dev.granville.dao.ReimbursementEventTypeDAO;

public class ReimbursementEventTypeService {
    private final ReimbursementEventTypeDAO reimbursementEventTypeDao = new ReimbursementEventTypeDAO();
    public ReimbursementEventType getById(Integer id) {
        return reimbursementEventTypeDao.getById(id);
    }
    public ReimbursementEventType getRandom() {
        return reimbursementEventTypeDao.getRandom();
    }
}
