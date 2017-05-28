package com.example.ekanban.dozer.converter;

import com.example.ekanban.entity.Section;
import com.example.ekanban.respository.SectionRepository;
import com.example.ekanban.util.ApplicationContextUtil;
import org.dozer.DozerConverter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by razamd on 4/12/2017.
 */
public class SectionConverter extends DozerConverter {

    public SectionConverter() {
        super(String.class, Set.class);
    }

    @Override
    public Object convertTo(Object o, Object o2) {
        if (o == null) return null;
        if (o instanceof String) {
            Set<Section> sections = new HashSet<>();
            String sec = (String)o;
            String[] secArray = sec.split(",");

            SectionRepository sectionRepository = ApplicationContextUtil.getApplicationContext().getBean(SectionRepository.class);
            Section section;
            for (String str: secArray){
                section =sectionRepository.findByNameIgnoreCase(str.trim());
                if (section != null) {
                    sections.add(section);
                }else {
                    sections.add(sectionRepository.save(new Section(str.trim())));
                }
            }
            return sections;
        }
        return null;
    }

    @Override
    public Object convertFrom(Object o, Object o2) {
        return null;
    }
}
