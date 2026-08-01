package com.cesarfrade.ats.service;

import com.cesarfrade.ats.model.CV;

import java.util.List;

public interface ICVService {
    public CV findCV(Long id_CV);
    public List<CV> getCVs();
    public void saveCV(CV cv);
    public void deleteCV(Long id_cv);
    public void editCV(CV cv, Long id_cv);
}
