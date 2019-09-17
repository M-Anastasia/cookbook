package com.nastya.cookbook.service;

import com.nastya.cookbook.model.Share;
import com.nastya.cookbook.repository.ShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    private ShareRepository shareRepository;

    @Override
    public List<Share> findByName(String name) {
        return shareRepository.findByName(name);
    }

    @Override
    public void save(Share share) {
        shareRepository.save(share);
    }
}
