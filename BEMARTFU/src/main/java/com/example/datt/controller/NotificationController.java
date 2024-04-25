package com.example.datt.controller;
import com.example.datt.entity.Notification;
import com.example.datt.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/notification")
public class NotificationController {

	@Autowired
	NotificationRepository notificationRepository;

	@GetMapping
	public ResponseEntity<List<Notification>> getAll() {
		return ResponseEntity.ok(notificationRepository.findByOrderByIdDesc());
	}

	@PostMapping
	public ResponseEntity<Notification> post(@RequestBody Notification notification) {
		if (notificationRepository.existsById(notification.getId())) {
			return ResponseEntity.badRequest().build();
		}
		notification.setTime(new Date());
		notification.setStatus(false);
		return ResponseEntity.ok(notificationRepository.save(notification));
	}

	@GetMapping("/readed/{id}")
	public ResponseEntity<Notification> put(@PathVariable("id") Long id) {
		if (!notificationRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		Notification no = notificationRepository.getById(id);
		no.setStatus(true);
		return ResponseEntity.ok(notificationRepository.save(no));
	}

	@GetMapping("/read-all")
	public ResponseEntity<Void> readAll() {
		notificationRepository.readAll();
		return ResponseEntity.ok().build();
	}

}
