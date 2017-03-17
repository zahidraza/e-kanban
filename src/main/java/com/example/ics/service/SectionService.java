package com.example.ics.service;


import com.example.ics.entity.Section;
import com.example.ics.respository.SectionRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private final Logger logger = LoggerFactory.getLogger(SectionService.class);

    @Autowired SectionRepository sectionRepository;

    public Section findOne(Long id) {
        logger.debug("findOne(): id = {}",id);
        return sectionRepository.findOne(id);
    }

    public List<Section> findAll() {
        logger.debug("findAll()");
        return sectionRepository.findAll();
    }
    
    public Page<Section> findAllByPage(Pageable pageable){
        logger.debug("findAllByPage()");
        return sectionRepository.findAll(pageable);
    }
    
    public Section findByName(String name) {
        logger.debug("findByName(): name = " , name);
        return sectionRepository.findByName(name);
    }

    public Boolean exists(Long id) {
        logger.debug("exists(): id = ",id);
        return sectionRepository.exists(id);
    }
    
    public Long count(){
        logger.debug("count()");
        return sectionRepository.count();
    }

    @Transactional
    public Section save(Section section) {
        logger.debug("save()");
        section = sectionRepository.save(section);
        return section;
    }

    @Transactional
    public Section update(Section section) {
        logger.debug("update()");
        Section section2 = sectionRepository.findOne(section.getId());
        section2.setName(section.getName());
        return section2;
    }
    
    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        sectionRepository.delete(id);
    }
}
