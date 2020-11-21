package com.edudev.daniloproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edudev.daniloproject.entity.Cellphone;
import com.edudev.daniloproject.entity.User;
import com.edudev.daniloproject.repository.CellphoneRepository;
import com.edudev.daniloproject.repository.UserRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Controller
public class CellphoneController {

	@Autowired
	private CellphoneRepository repo;

	@Autowired
	private UserRepository userRepo;


	@PostMapping("/c{id}")
	public String detailsUserPost(@PathVariable("id") Long id, User users) {
		
		try {
			Cellphone cellphone = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			users.setCellphone(cellphone);
			userRepo.save(users);
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/c{id}";
	}
	
	@GetMapping("/updateUser")
	public String formCellUpdate() {
		return "User/updateUsers";
	}
	
	@RequestMapping("/updateUser")
	public String updateUser(Long id, User user) {
		String cod = "";
		try {
			if(user.getName() != null && user.getEmail() != null && user.getPhone() != null) {
			User userToUpdate = userRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			userToUpdate.setName(user.getName());
			userToUpdate.setEmail(user.getEmail());
			userToUpdate.setPhone(user.getPhone());
			
			userRepo.save(userToUpdate);
			
			Cellphone cellphone = userToUpdate.getCellphone();
			long idLong = cellphone.getId();
			cod = "" + idLong;
			}
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/c" + cod;
	}

	@RequestMapping("/deleteUser")
	public String deleteUser(Long id) {
		String cod = "";
		try {
			User user = userRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			userRepo.delete(user);

			Cellphone cellphone = user.getCellphone();
			long idLong = cellphone.getId();
			cod = "" + idLong;
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/c" + cod;
	}

}
