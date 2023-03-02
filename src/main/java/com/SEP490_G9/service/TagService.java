package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entity.Tag;

public interface TagService {
	public List<Tag> getAllTags();
	
	public boolean addTag(Tag tag);
	
	public boolean updateTag(Tag tag, int id);
}
