package com.nmmc.hrms.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.uuid.Generators;
import com.google.gson.JsonObject;
import com.nmmc.hrms.Dao.TrnDocumentListDao;
import com.nmmc.hrms.Entity.TrnDocumentList;
import com.nmmc.hrms.Repository.TrnDocumentListRepository;
import com.nmmc.hrms.Service.TrnDocumentListService;

@Service
public class TrnDocumentListServiceImpl implements TrnDocumentListService {

    @Autowired
    private TrnDocumentListRepository trnDocumentListRepository;

    @Override
    public String save(TrnDocumentListDao trnDocumentListDao) {
        JsonObject ob = new JsonObject();
        TrnDocumentList trnDocumentList = null;
        try {
            if (trnDocumentListDao.getId() != null) {
                trnDocumentList = trnDocumentListRepository
                        .getReferenceById(trnDocumentListDao.getId());
                if (trnDocumentList == null) {
                    ob.addProperty("status", "Failed");
                    ob.addProperty("message", "Data not found");
                    return ob.toString();
                }
            }

            if (trnDocumentListDao.getId() == null) {
                trnDocumentList = new TrnDocumentList();

                UUID timebaseUUID = Generators.timeBasedEpochGenerator().generate();
                trnDocumentListDao.setId(timebaseUUID);

            } else {
                trnDocumentListDao.setId(trnDocumentList.getId());
            }
            BeanUtils.copyProperties(trnDocumentListDao, trnDocumentList);

            trnDocumentListRepository.save(trnDocumentList);
            ob.addProperty("status", "success");
            return ob.toString();
        } catch (Exception ex) {
            ob.addProperty("status", "failure");
            return ob.toString();
        }
    }

    @Override
    public List<TrnDocumentListDao> findAllDocumentListByTransactionIdAndTransactionKey(UUID transactionId,
            String transactionKey) {
        List<TrnDocumentList> trnDocumentList = trnDocumentListRepository
                .findAllDocumentListByTransactionIdAndTransactionKey(transactionId, transactionKey);

        List<TrnDocumentListDao> trnDocumentDaoList = new ArrayList<>();

        for (TrnDocumentList trnDocument : trnDocumentList) {
            TrnDocumentListDao trnDocumentListDao = new TrnDocumentListDao();
            BeanUtils.copyProperties(trnDocument, trnDocumentListDao);
            trnDocumentDaoList.add(trnDocumentListDao);
        }
        return trnDocumentDaoList;

    }

}
