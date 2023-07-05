package com.group.ensprojectspringboot.serviceImpl;

import com.group.ensprojectspringboot.configuration.JwtFilter;
import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.Bursarship;
import com.group.ensprojectspringboot.model.Source;
import com.group.ensprojectspringboot.repository.BursarshipRepository;
import com.group.ensprojectspringboot.repository.SourceRepository;
import com.group.ensprojectspringboot.service.BursarshipService;
import com.group.ensprojectspringboot.utils.EnsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class BursaryServiceImpl implements BursarshipService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BursarshipRepository bursarshipRepository;

    @Autowired
    SourceRepository sourceRepository;

    @Override
    public ResponseEntity<String> createBursary(Map<String, String> request) {
        try {
            if (jwtFilter.isAdmin()) {
                if (isBursaryValid(request.get("bursaryName"), Double.valueOf(request.get("amount")), Long.valueOf(request.get("sourceId")))) {
                    Optional<Source> sourceOptional = sourceRepository.findById(Long.valueOf(request.get("sourceId")));
                    if (sourceOptional.isPresent()){
                        bursarshipRepository.save(getBursaryFromMap(request));
                        return EnsUtils.getResponseEntity("Bursarship is added Successfully!", HttpStatus.OK);
                    } else {
                        return EnsUtils.getResponseEntity(EnsConsts.INVALID_ID + Long.valueOf(request.get("sourceId")), HttpStatus.OK);
                    }
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

    private Bursarship getBursaryFromMap(Map<String, String> request) {
        Bursarship bursarship = new Bursarship();
        bursarship.setBursaryName(request.get("bursaryName"));
        bursarship.setAmount(Double.valueOf(request.get("amount")));
        bursarship.setCreatedOn(LocalDate.now());
        Optional<Source> sourceOptional = sourceRepository.findById(Long.valueOf(request.get("sourceId")));
        Source source = sourceOptional.get();
        bursarship.setSourceId(source);
        return bursarship;
    }

    private boolean isBursaryValid(String bursaryName, Double amount, Long sourceId) {
        if (bursaryName == null || bursaryName.isEmpty())
            return false;
        if (amount <= 0)
            return false;
        if (sourceId == null)
            return false;
        return true;
    }

    @Override
    public ResponseEntity<List<Bursarship>> getBursary() {
        try {
            return new ResponseEntity<List<Bursarship>>(bursarshipRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<Bursarship>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteBursary(Long id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<?> optional = bursarshipRepository.findById(id);
                if (optional.isPresent()) {
                    bursarshipRepository.deleteById(id);
                    return EnsUtils.getResponseEntity("Bursarship deleted successfully", HttpStatus.OK);
                } else {
                    return EnsUtils.getResponseEntity("Bursarship id does not exist", HttpStatus.OK);
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
    public ResponseEntity<String> updateBursary(Map<String, String> request) {
        try {
            if (jwtFilter.isAdmin()) {
                if (isBursaryValid(request.get("bursaryName"), Double.valueOf(request.get("amount")), Long.valueOf(request.get("sourceId")))) {
                    Long bursarshipId = Long.valueOf(request.get("id"));
                    Optional<Bursarship> bursarshipOptional = bursarshipRepository.findById(bursarshipId);
                    if (bursarshipOptional.isPresent()) {
                        Bursarship bursarship = getBursaryFromMap(request);
                        bursarshipRepository.updateBursarship(bursarshipId, bursarship.getBursaryName(), bursarship.getAmount(), bursarship.getSourceId());
                        return EnsUtils.getResponseEntity("Bursarship updated successfully", HttpStatus.OK);
                    } else {
                        return EnsUtils.getResponseEntity("Bursarship does not exist!", HttpStatus.OK);
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
