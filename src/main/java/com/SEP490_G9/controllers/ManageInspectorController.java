package com.SEP490_G9.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entity.User;
import com.SEP490_G9.service.ManageInspectorService;


@RequestMapping("private/manageInspector")
@RestController
public class ManageInspectorController {
	
	@Autowired
	ManageInspectorService manageInspectorService;
	
	@GetMapping("allInspectors")
	public ResponseEntity<?> getAllInspectors(){
		List<User> inspectorList = manageInspectorService.getAllInspectors();
		return ResponseEntity.ok(inspectorList);
	}
	
	@PostMapping("addInspector")
	public ResponseEntity<?> addInspector(@RequestBody User inspector){
		boolean canAdd = manageInspectorService.addInspector(inspector);
		return ResponseEntity.ok(canAdd);
	}
	
	@PutMapping("activateInspector")
	public ResponseEntity<?> activateInspector(@RequestParam(name = "inspectorId") Long id){
		boolean canActivate = manageInspectorService.activateInspector(id);
		return ResponseEntity.ok(canActivate);
	}
	
	@PutMapping("deactivateInspector")
	public ResponseEntity<?> deactivateInspector(@RequestParam(name = "inspectorId") Long id){
		boolean canDeactivate = manageInspectorService.deactivateInspector(id);
		return ResponseEntity.ok(canDeactivate);
	}
	
	@DeleteMapping("deleteInspector/{id}")
	public ResponseEntity<?> deleteInspector(@PathVariable("id") Long id){
		boolean canDelete = manageInspectorService.deleteInspector(id);
		return ResponseEntity.ok(canDelete);
	}
	
	@PutMapping("updateInspector")
	public ResponseEntity<?> updateInspector(@RequestBody User inspector){
		boolean canUpdate = manageInspectorService.updateInspector(inspector);
		return ResponseEntity.ok(canUpdate);
	}
}
