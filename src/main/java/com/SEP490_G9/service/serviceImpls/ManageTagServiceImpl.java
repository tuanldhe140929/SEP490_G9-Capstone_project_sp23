package com.SEP490_G9.services.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.DuplicateFieldException;
import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.repositories.TagRepository;
import com.SEP490_G9.services.ManageTagService;

@Service
public class ManageTagServiceImpl implements ManageTagService{

	@Autowired
	TagRepository tagRepo;
	
	@Override
	public List<Tag> getAllTags() {
		List<Tag> allTags = tagRepo.findAll();
		return allTags;
	}

	@Override
	public boolean addTag(Tag tag) {
		if(tagRepo.existsByName(tag.getName())) {
			throw new DuplicateFieldException("name", tag.getName());
		}
		tagRepo.save(tag);
		return true;
	}

	@Override
	public boolean updateTag(Tag tag, int id) {
		Tag updatedTag = tagRepo.findById(id);
		if(!tag.getName().trim().equalsIgnoreCase(updatedTag.getName().trim())) {
			if(tagRepo.existsByName(tag.getName())) {
				throw new DuplicateFieldException("name", tag.getName());
			}
		}
		updatedTag.setName(tag.getName().trim());
		tagRepo.save(updatedTag);
		return true;
	}

}
