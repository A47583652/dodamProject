package com.dodam.controller.board.missing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dodam.domain.missing.MissingReplyVo;
import com.dodam.service.board.missing.MissingReplyService;

@RestController
@RequestMapping("/board/missing")
public class MissingReplyController {
	
	@Inject
	private MissingReplyService service;
	
	@RequestMapping(value="/reply", method=RequestMethod.POST)
	public ResponseEntity<String> registerReply(@RequestBody MissingReplyVo mrv) {
		if (service.insertReply(mrv)) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		return new ResponseEntity<String>("fail", HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value="/replies/{pno}", method=RequestMethod.GET)
	public Map<String, Object> viewAllReply(@PathVariable("pno") int pno) {
		
		List<MissingReplyVo> lst = service.selectAllReply(pno);
		System.out.println(lst);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lst", lst);
		map.put("sizeOflst", lst.size());
		return map;
	}
	
	@RequestMapping(value="/reply/{no}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteReply(@PathVariable("no") int no) {
		
		System.out.println(no);
		if (service.deleteReply(no)) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		return new ResponseEntity<String>("fail", HttpStatus.OK);
	}
	
	@RequestMapping(value="/reply/{no}", method=RequestMethod.PUT)
	public ResponseEntity<String> updateReply(@PathVariable("no") int no, @RequestBody MissingReplyVo mrv) {
		
		if (service.updateReply(mrv)) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		return new ResponseEntity<String>("fail", HttpStatus.OK);
	}
}
