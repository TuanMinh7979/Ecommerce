package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.entities.RootFilterEntity;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.repository.RootFilterRepo;
import com.tmt.tmdt.service.RootFilterEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RootFilterEntityServiceImpl implements RootFilterEntityService {
    private final RootFilterRepo rootFilterRepo;

    @Override
    public RootFilterEntity getFilterEntity(Integer id) {
        return rootFilterRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("FilterEntity have id " + id + " Not found"));
    }
}
