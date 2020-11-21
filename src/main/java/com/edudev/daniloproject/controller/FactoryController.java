package com.edudev.daniloproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.edudev.daniloproject.entity.Cellphone;
import com.edudev.daniloproject.entity.Factory;
import com.edudev.daniloproject.entity.User;
import com.edudev.daniloproject.repository.CellphoneRepository;
import com.edudev.daniloproject.repository.FactoryRepository;
import com.edudev.daniloproject.repository.UserRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Controller
public class FactoryController {

	@Autowired
	private FactoryRepository repo;

	@Autowired
	private CellphoneRepository cellRepo;
	
	@Autowired
	private UserRepository userRepo;

	@GetMapping("/form")
	public String form() {
		return "Factory/formFactory";
	}

	@PostMapping("/form")
	public String form(Factory factory) {
		repo.save(factory);
		return "redirect:/factories";
	}

	@GetMapping("/factories")
	public ModelAndView listFactories() {
		ModelAndView mv = new ModelAndView("Factory/listFactories");
		Iterable<Factory> factories = repo.findAll();
		mv.addObject("factories", factories);
		return mv;
	}

	@GetMapping("/{id}")
	public ModelAndView detailsFactory(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("Factory/detailsFactory");
		try {
			Factory factories = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			mv.addObject("factories", factories);

			Iterable<Cellphone> cellphones = cellRepo.findByFactory(factories);
			mv.addObject("cellphones", cellphones);
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return mv;
	}
	
	@GetMapping("/c{id}")
	public ModelAndView detailsCellphone(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("Cellphone/detailsCellphone");
		try {
			Cellphone cellphones = cellRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			mv.addObject("cellphones", cellphones);

			Iterable<User> users = userRepo.findByCellphone(cellphones);
			mv.addObject("users", users);
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return mv;
	}

	@PostMapping("/{id}")
	public String detailsFactoryPost(@PathVariable("id") Long id, Cellphone cellphone) {
		try {
			Factory factory = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			cellphone.setFactory(factory);
			cellRepo.save(cellphone);
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/{id}";
	}

	@RequestMapping("/delete")
	public String deleteFactory(Long id) {
		try {
			Factory factory = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			repo.delete(factory);
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/factories";
	}
	
	@GetMapping("/update")
	public String formUpdate() {
		return "Factory/updateFactories";
	}
	
	@RequestMapping("/update")
	public String updateFactory(Long id, Factory factory) {
		try {
			if(factory.getName() != null) {
			Factory factoryToUpdate = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			factoryToUpdate.setName(factory.getName());
			
			repo.save(factoryToUpdate);
			}
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/factories";
	}
	
	@RequestMapping("/deleteCellphone")
	public String deleteCellphone(Long id) {
		String cod = "";
		try {
			Cellphone cellphone = cellRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			cellRepo.delete(cellphone);

			Factory factory = cellphone.getFactory();
			long idLong = factory.getId();
			cod = "" + idLong;
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/" + cod;
	}
	
	@GetMapping("/updateCellphone")
	public String formCellUpdate() {
		return "Cellphone/updateCellphones";
	}
	
	@RequestMapping("/updateCellphone")
	public String updateCellphone(Long id, Cellphone cellphone) {
		String cod = "";
		try {
			if(cellphone.getName() != null && cellphone.getPrice() != null && cellphone.getDescription() != null) {
			Cellphone cellphoneToUpdate = cellRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id not exists"));
			cellphoneToUpdate.setName(cellphone.getName());
			cellphoneToUpdate.setPrice(cellphone.getPrice());
			cellphoneToUpdate.setDescription(cellphone.getDescription());
			
			cellRepo.save(cellphoneToUpdate);
			
			Factory factory = cellphoneToUpdate.getFactory();
			long idLong = factory.getId();
			cod = "" + idLong;
			}
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/" + cod;
	}
}
