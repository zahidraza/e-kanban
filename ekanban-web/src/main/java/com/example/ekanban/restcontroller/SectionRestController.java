package com.example.ekanban.restcontroller;

import com.example.ekanban.assembler.SectionAssembler;
import com.example.ekanban.entity.Section;
import com.example.ekanban.service.SectionService;
import com.example.ekanban.util.ApiUrls;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrls.ROOT_URL_SECTIONS)
public class SectionRestController {    
    private final Logger logger = LoggerFactory.getLogger(SectionRestController.class);
    
    private final SectionService sectionService;    
    private final SectionAssembler sectionAssembler;

    @Autowired
    public SectionRestController(SectionService sectionService, SectionAssembler sectionAssembler) {
        this.sectionService = sectionService;
        this.sectionAssembler = sectionAssembler;
    }

//    @GetMapping
//    public ResponseEntity<?> loadAllSections(Pageable pageable, PagedResourcesAssembler assembler){
//        logger.debug("loadAllSections()");
//        Page<Section> page = sectionService.findAllByPage(pageable);
//        return new ResponseEntity<>(assembler.toResource(page,sectionAssembler),HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<?> loadAllSections(){
        logger.debug("loadAllSections()");
        List<Section> sections = sectionService.findAll();
        return new ResponseEntity<>(sectionAssembler.toResources(sections),HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_SECTIONS_SECTION)
    public ResponseEntity<?> loadSection(@PathVariable("secId") Long secId){
        logger.debug("loadSection(): secId = {}",secId);
        Section section = sectionService.findOne(secId);
        if (section == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sectionAssembler.toResource(section), HttpStatus.OK);
    }
    
    
    @PostMapping
    public ResponseEntity<?> createSection(@Valid @RequestBody Section section) {
        logger.debug("createSection():\n {}", section.toString());
        section = sectionService.save(section);
        Link selfLink = linkTo(SectionRestController.class).slash(section.getId()).withSelfRel();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(sectionAssembler.toResource(section),headers,HttpStatus.CREATED);
    }
 
    @PutMapping(ApiUrls.URL_SECTIONS_SECTION)
    public ResponseEntity<?> updateSection(@PathVariable("secId") long secId,@Valid @RequestBody Section section) {
        logger.debug("updateSection(): secId = {} \n {}",secId,section);
        if (!sectionService.exists(secId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        section.setId(secId);  
        section = sectionService.update(section);
        return new ResponseEntity<>(sectionAssembler.toResource(section), HttpStatus.OK);
    }
  
    @DeleteMapping(ApiUrls.URL_SECTIONS_SECTION)
    public ResponseEntity<Void> deleteSection(@PathVariable("secId") long secId) {
        logger.debug("deleteSection(): id = {}",secId);
        if (!sectionService.exists(secId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        sectionService.delete(secId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
