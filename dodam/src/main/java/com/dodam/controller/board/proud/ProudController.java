package com.dodam.controller.board.proud;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dodam.domain.members.MypointVo;
import com.dodam.domain.proud.LikeHistory;
import com.dodam.domain.proud.PagingProud;
import com.dodam.domain.proud.ProudVo;
import com.dodam.domain.proud.ReplyVo;
import com.dodam.etc.proud.UploadFileProcess;
import com.dodam.etc.proud.UploadFiles;
import com.dodam.service.board.proud.ProudService;
import com.google.gson.JsonObject;

@Controller
@RequestMapping("/board/proud/*")
public class ProudController {

	@Inject
	private ProudService service;
	
	private static final Logger logger = LoggerFactory.getLogger(ProudController.class);

	@RequestMapping(value ="/listAll", method=RequestMethod.GET)
	public void listAll(Model model, @RequestParam(value="pageNo", required=false, defaultValue="1") String tmp,
			@RequestParam(value ="searchBy", required = false, defaultValue="title") String type,
			@RequestParam(value ="searchWord", required = false, defaultValue="") String word) throws Exception {
		int pageNo = 1;
		if(!tmp.equals("") || tmp != null) {
			pageNo = Integer.parseInt(tmp);
		}
		
		System.out.println("?????? ?????? : " + type + ", ?????? ?????? : " + word);

		Map<String, Object> map = service.readAllBoard(pageNo, type, word);

		List<ProudVo> lst = (List<ProudVo>)map.get("boardList");
		PagingProud pi = (PagingProud)map.get("pagingInfo");

		model.addAttribute("pagingInfo", pi); // ????????? ??????0
		model.addAttribute("proudlistBoard", lst); // ????????? ??? ?????????

	}
	
	@RequestMapping(value = "/readboard", method=RequestMethod.GET)
	public String readBoard(@RequestParam("no") String tmp, Model model, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes rttr) throws NamingException, SQLException {
		int no = Integer.parseInt(tmp);		

		Cookie[] cookies = request.getCookies();

		HttpSession ses = request.getSession();				
		String userid = (String)ses.getAttribute("userid");
		System.out.println("????????? ?????? ????????? : " + ses.getAttribute("userid"));

		int result2 = service.likehistory(no, userid);
		model.addAttribute("likehistory", result2);	

		boolean result3 = true;
		boolean result4 = true;

			if (ses.getAttribute("userid") == null) {
			for (int i = 0; i < cookies.length; i++) {		// ?????? ????????? ?????????
				if(!cookies[i].getName().equals("cookie" + no)) {	// ??????+id+?????? ??? ?????? ??????
					System.out.println("????????? ?????? ?????? ??????");
				}
				else {
					System.out.println(cookies[i].getName());
					System.out.println("????????? ????????? ??????????????? ?????? ???????????? -> ????????? ??????");
					result3 = false;
				}
			}

			if(result3) {	// ???????????? ??? ?????? ??? ??????
					service.readcount(no);		// ????????? 1 ??????
					Cookie newCookie = new Cookie("cookie"+no, "|" + no + "|");
					newCookie.setMaxAge(60*60*24);
					response.addCookie(newCookie);		// ?????? ??????
				}
			}

			else if(ses.getAttribute("userid") != null) {
				for (int i = 0; i < cookies.length; i++) {		// ?????? ????????? ?????????
					if(!cookies[i].getName().equals("cookie" + userid + no)) {
						System.out.println("????????? ?????? ?????? ??????");
					}
					else {
						System.out.println(cookies[i].getName());
						System.out.println("????????? ?????? ?????? ??????");
						result4 = false;
					}
				}

				if(result4) {
					service.readcount(no);		// ????????? 1 ??????
					Cookie newCookie = new Cookie("cookie" + userid + no, "|" + no + "|");
					newCookie.setMaxAge(60*60*24);
					response.addCookie(newCookie);		// ?????? ??????
				}
			}
		
		ProudVo vo = service.readBoard(no);
		model.addAttribute("board", vo);	
		
		return "board/proud/readboard";
		}
	
	@RequestMapping(value = "/updateboard", method=RequestMethod.GET)
	public void updateBoard(@RequestParam("no") String tmp, Model model) throws NamingException, SQLException {
		int no = Integer.parseInt(tmp);
		
		ProudVo vo = service.readBoard(no);
		
		model.addAttribute("board", vo);
		
	}
	
	
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerBoard() {
		return "/board/proud/registerboard";
	}
	
	
	
	@RequestMapping(value="/uploadFile", method=RequestMethod.POST, produces="text/plain; charset=utf-8")
	public ResponseEntity<String> upFile(MultipartFile upFile, HttpServletRequest request)  { //MultipartFile ?????????????? ???????????????????????? jsp???????????????????? ajax???????????? ???????????????? name?????? ?????????????? ????????.
		logger.info("????????? ??? ?????? ?????? ??????...");
		
		logger.info("???????????? ?????? ?????? : " + upFile.getOriginalFilename());
		logger.info("?????? ????????? : " + upFile.getSize());
		logger.info("???????????? ????????? ?????? : " + upFile.getContentType());
		logger.info("?????? separator : " + File.separator);
		
		String upPath = request.getSession().getServletContext().getRealPath("resources/uploads/lcj");
		logger.info("????????? ?????? ?????? ?????? : " + upPath);
		
		UploadFiles files;
		try {
			files = new UploadFileProcess().uploadFile(upPath, upFile.getOriginalFilename(), upFile.getBytes());
			String returnVal = null;
//			if (files.getThumbNailImgFileName() != null) {
				// ????????? ??????????????? ????????? ????????? ?????? ??????
//				returnVal = files.getThumbNailImgFileName();
//			}
//			else if ( files.getNotImgFileName() != null) {
				// ????????? ????????? ???????????? ?????? ??????
				returnVal = files.getOriginImgFileName();
//			}
			return new ResponseEntity<String>(returnVal, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/delFile", method=RequestMethod.POST, produces="text/plain; charset=utf-8")
	public ResponseEntity<String> delFile(@RequestParam("origin") String origin, @RequestParam("thumb") String thumb,
			@RequestParam("notImg") String notImg, HttpServletRequest request) {
		System.out.println("origin : " + origin + ", thumb : " + thumb);
		String path = request.getSession().getServletContext().getRealPath("resources");
		path += File.separator + "uploads/lcj";
		
		// ???????????? ?????? ????????? ??????
		if(!notImg.equals("") && origin.equals("")) {	// not???????????? ??????????????? ????????? origin??? ??????????????????.. ???????????? ?????????
			notImg = notImg.replace('/', File.separatorChar);
			File notImgF = new File(path + notImg); 
			if( notImgF.delete()) {
				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);	
			}
			
		}  else if(notImg.equals("") && !origin.equals("")){ // not???????????? ???????????? ???????????? ??????????????? ?????? ???.. ????????? ??????
			origin = origin.replace('/', File.separatorChar);
			File originFile = new File(path + origin);
				
			System.out.println("?????? ?????? :" + (path + origin));
			boolean oBoolean = originFile.delete();
			boolean tBoolean = false;
				
			thumb = thumb.replace('/', File.separatorChar);
			File thumbFile = new File(path + thumb);

			System.out.println("?????? ?????? :" + (path + origin));
			tBoolean = thumbFile.delete();
			tBoolean = true;
			
			if (oBoolean && tBoolean) {
			return new ResponseEntity<String>(HttpStatus.OK);
			}	else {
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);	
			}
		}
		else {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/createBoard", method=RequestMethod.POST)
	public String createBoard(ProudVo vo, RedirectAttributes rttr, HttpServletRequest request) throws NamingException, SQLException {
		System.out.println(vo.toString());

		HttpSession ses = request.getSession();				
		String userid = (String)ses.getAttribute("userid"); // ????????? ???????????????
		
		if (service.addBoard(vo)) {
			rttr.addFlashAttribute("result", "success");
			service.addpoint(new MypointVo(userid, null, 5, "????????? ??????"));
		} else {
			rttr.addFlashAttribute("result", "fail");
		}
		return "redirect:/board/proud/listAll?pageNo=1";
	}
	
	@ResponseBody
	@RequestMapping(value="/like", method=RequestMethod.POST)
	public ResponseEntity<String> like(@RequestBody LikeHistory vo, HttpServletRequest request) throws NamingException, SQLException {

		System.out.println("no");
		
		HttpSession ses = request.getSession();				
		String userid = (String)ses.getAttribute("userid"); // ????????? ???????????????
		
		ResponseEntity<String> result = null;
		
		if(ses.getAttribute("userid") != null) {
			try {
				 service.like(vo, userid);
		         service.likeup(vo);
		         result = new ResponseEntity<String>("success", HttpStatus.OK);
			} catch (Exception e) {
		         result = new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value="/dislike", method=RequestMethod.POST)
	public ResponseEntity<String> dislike(@RequestBody LikeHistory vo, HttpServletRequest request) throws NamingException, SQLException {

		System.out.println("no");
		
		HttpSession ses = request.getSession();				
		String userid = (String)ses.getAttribute("userid"); // ????????? ???????????????
		
		ResponseEntity<String> result = null;
		
		if(ses.getAttribute("userid") != null) {
			try {
				 service.dislike(vo, userid);
		         service.likedown(vo);
		         result = new ResponseEntity<String>("success", HttpStatus.OK);
			} catch (Exception e) {
		         result = new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		}
		return result;
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(ProudVo vo, RedirectAttributes rttr) throws NamingException, SQLException {
		System.out.println(vo.toString());
		
		if (service.updateBoard(vo)) {
			rttr.addFlashAttribute("result", "success");			
		} else {
			rttr.addFlashAttribute("result", "fail");
		}
		return "redirect:/board/proud/listAll?pageNo=1";
	}

	@RequestMapping(value = "/deleteboard", method=RequestMethod.GET)
	public String deleteBoard(@RequestParam("no") String tmp, RedirectAttributes rttr) throws NamingException, SQLException {
		int no = Integer.parseInt(tmp);
		
		if (service.deleteBoard(no)) {
			rttr.addFlashAttribute("result", "success");			
		} else {
			rttr.addFlashAttribute("result", "fail");
		}
		
		return "redirect:/board/proud/listAll?pageNo=1";
	}
	
	@RequestMapping(value = "/writetest", method=RequestMethod.GET)
	public String writetest() throws NamingException, SQLException {
		
		return "product_write";
	}
}
