package com.SEP490_G9.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.service.TagService;

@RequestMapping("private/manageTag")
@RestController
public class TagController {
	@Autowired
	TagService manageTagService;
	
	@GetMapping("tags")
	public ResponseEntity<?> getAllTags(){
		List<Tag> tagList = manageTagService.getAllTags();
		return ResponseEntity.ok(tagList);
	}
	
	@PostMapping("addTag")
	public ResponseEntity<?> addTag(@RequestBody Tag tag){
		boolean canAdd = manageTagService.addTag(tag);
		return ResponseEntity.ok(canAdd);
	}
	
	@PutMapping("updateTag/{id}")
	public ResponseEntity<?> updateTag(@RequestBody Tag tag, @PathVariable("id") int id){
		boolean canUpdate = manageTagService.updateTag(tag, id);
		return ResponseEntity.ok(canUpdate);
	}
}
