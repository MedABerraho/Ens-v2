package com.group.ensprojectspringboot.serviceImpl;

import com.group.ensprojectspringboot.configuration.JwtFilter;
import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.Source;
import com.group.ensprojectspringboot.repository.SourceRepository;
import com.group.ensprojectspringboot.service.SourceService;
import com.group.ensprojectspringboot.utils.EnsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class SourceServiceImpl implements SourceService {

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> createSource(Map<String, String> request) {
        try {
            if (jwtFilter.isAdmin()) {
                if (isSourceValid(request.get("sourceName"))) {
                    sourceRepository.save(getSourceFromMap(request));
                    return EnsUtils.getResponseEntity("Source is added Successfully!", HttpStatus.OK);
                }
                return EnsUtils.getResponseEntity(EnsConsts.INVALID_DATA, HttpStatus.OK);
            } else {
                return EnsUtils.getResponseEntity(EnsConsts.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Source getSourceFromMap(Map<String, String> request) {
        Source source = new Source();
        source.setSourceName(request.get("sourceName"));
        source.setSourceDescription(request.get("sourceDescription"));
        return source;
    }

    private boolean isSourceValid(String sourceName) {
        if (sourceName == null || sourceName.isEmpty())
            return false;
        return true;
    }

    @Override
    public ResponseEntity<List<Source>> getSource() {
        try {
            return new ResponseEntity<List<Source>>(sourceRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<Source>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteSource(Long id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<?> optional = sourceRepository.findById(id);
                if (optional.isPresent()) {
                    sourceRepository.deleteById(id);
                    return EnsUtils.getResponseEntity("Source deleted successfully", HttpStatus.OK);
                } else {
                    return EnsUtils.getResponseEntity("Source id does not exist", HttpStatus.OK);
                }
            } else {
                return EnsUtils.getResponseEntity(EnsConsts.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateSource(Map<String, String> request) {
        try {
            if (jwtFilter.isAdmin()) {
                if (isSourceValid(request.get("sourceName"))) {
                    Long sourceId = Long.valueOf(request.get("id"));
                    Optional<Source> sourceOptional = sourceRepository.findById(sourceId);
                    if (sourceOptional.isPresent()) {
                        Source source = getSourceFromMap(request);
                        sourceRepository.updateSource(sourceId, source.getSourceName(), source.getSourceDescription());
                        return EnsUtils.getResponseEntity("Source updated successfully", HttpStatus.OK);
                    } else {
                        return EnsUtils.getResponseEntity("Source does not exist!", HttpStatus.OK);
                    }
                } else {
                    return EnsUtils.getResponseEntity(EnsConsts.INVALID_DATA, HttpStatus.OK);
                }
            } else {
                return EnsUtils.getResponseEntity(EnsConsts.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
