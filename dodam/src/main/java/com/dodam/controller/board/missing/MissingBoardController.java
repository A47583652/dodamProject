package com.dodam.controller.board.missing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dodam.domain.members.MemberVo;
import com.dodam.domain.missing.ListParamDTO;
import com.dodam.domain.missing.MissingBoardListDTO;
import com.dodam.domain.missing.MissingBoardVo;
import com.dodam.domain.missing.MissingWriteDTO;
import com.dodam.domain.missing.PagingInfoDTO;
import com.dodam.etc.missing.IPChecking;
import com.dodam.etc.missing.MissingUploadImgProcess;
import com.dodam.etc.missing.MissingUploadImgs;
import com.dodam.service.board.missing.MissingBoardService;

@Controller
@RequestMapping("/board/missing/*")
public class MissingBoardController {
	
	private static Logger logger = LoggerFactory.getLogger(MissingBoardController.class);
	
	@Inject
	private MissingBoardService service;
	
	@RequestMapping("/list")
	public String missingboardlist(@RequestParam(value="pageNo", defaultValue = "1") int pageNo,
								@RequestParam(value="searchWord", defaultValue = "") String searchWord,
								@RequestParam(value="location", defaultValue = "") String location,
								@RequestParam(value="animal", defaultValue = "") String animal,
								@RequestParam(value="category", defaultValue = "missing") String category,
								@RequestParam(value="itemsPerPage", defaultValue = "20") int itemsPerPage,
								Model model) {
		ListParamDTO lpd = new ListParamDTO(pageNo, location, animal, category, searchWord);
		
		Map<String, Object> map = null;
		
		// #### 예외처리 페이지 만들기!!!!! ####
		try {
			map = service.selectMissingBoardList(lpd, itemsPerPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// int numOflist = service.getTotalPost(lpd);
		
		List<MissingBoardListDTO> lst = (List<MissingBoardListDTO>)map.get("listMissingBoard");
		PagingInfoDTO pi = (PagingInfoDTO)map.get("pagingInfo");
		int numOflist = (Integer)map.get("numOflist");
		
		model.addAttribute("listMissingBoard", lst);
		model.addAttribute("pagingInfo", pi);
		model.addAttribute("numOflist", numOflist);
		
		System.out.println(map);
		
		return "/board/missing/missingboardlist";
	}
	
	@RequestMapping("/write")
	public String addmissinglist() {
		System.out.println("실종게시판 게시물 등록페이지 go");
		return "/board/missing/registermissing";
	}
	
	@RequestMapping(value="/uploadImgs", method=RequestMethod.POST, produces="text/plain; charset=utf-8")
	public ResponseEntity<String> upFile(MultipartFile[] uploadImg, HttpServletRequest request) {
		// MultipartFile 객체의 변수명은 jsp파일내에 ajax에서 첨부하는 name과 같아야 한다.
		
		// request객체에서 실제 서버에서 이미지가 저장되는 경로를 얻어옴
		String uploadFolder = request.getSession().getServletContext().getRealPath("resources/uploads/kmj/missing");
		logger.info("업로드되는 실제 경로 : " + uploadFolder);
		
		String returnVal = "";
		try {
			for(int i=0; i<uploadImg.length; i++) {
				MissingUploadImgs imgfiles;
				imgfiles = new MissingUploadImgProcess().uploadImgRename(uploadFolder, uploadImg[i].getOriginalFilename(), uploadImg[i].getBytes());
				returnVal += imgfiles.getThumbNailImgName() + ", ";
			}
			return new ResponseEntity<String>(returnVal, HttpStatus.OK);
		} catch (IOException e) {
			e.getStackTrace().toString();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/delImg", method=RequestMethod.POST)
	public ResponseEntity<String> delFile(@RequestParam("origin") String origin, @RequestParam("thumb") String thumb, HttpServletRequest request) {
		// request.getRealPath()는 삭제 예정
		String path = request.getSession().getServletContext().getRealPath("resources");

		// 이미지가 저장된 폴더의 상위경로 중 uploads/kmj 폴더까지의 경로 생성
		path += File.separator + "uploads/kmj/missing";
		
		// 이미지 파일 삭제
		origin = origin.replace("/", File.separator);
		thumb = thumb.replace("/", File.separator);
		System.out.println("지울 파일 : " + path + origin);
		File originFile = new File(path + origin);
		boolean oBoolean = originFile.delete();
		System.out.println("지울 파일 : " + path + thumb);
		File thumbFile = new File(path + thumb);
		boolean tBoolean = thumbFile.delete();
		if (oBoolean && tBoolean) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping("/detail")
	public String viewDetailPage(@RequestParam("no") int no, HttpServletRequest request,
								Model model) {
		MissingBoardVo mb = null;
		HttpSession ses = request.getSession();
		MemberVo loginmem = (MemberVo)ses.getAttribute("loginSession");
		String userid = "";
		
		try {
			if (loginmem == null) {
				IPChecking ipCheck = new IPChecking();
				userid = ipCheck.getIp(); // ip 주소
			} else {
				userid = loginmem.getUserid();
			}
	
			mb = service.getMissingBoard(no, userid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("MissingBoard", mb);
		return "/board/missing/viewBoard";
	}
	
	@ResponseBody
	@RequestMapping(value="/getOtherList", method=RequestMethod.POST)
	public List<MissingBoardListDTO> getOtherList(@RequestParam("no") int no, HttpServletRequest request) {
		System.out.println(no);
		
		HttpSession ses = request.getSession();
		MemberVo loginmember = (MemberVo)ses.getAttribute("loginSession");
		List<MissingBoardListDTO> lst = new ArrayList<MissingBoardListDTO>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (loginmember != null) {
			map.put("no", no);
			map.put("userid", loginmember.getUserid());
			map.put("howmany", 20);
			lst = service.getRecommendation(map);
		} else {
			map.put("no", no);
			map.put("howmany", 20);
			lst = service.getRandomAnimal(map);
		}
		System.out.println("컨트롤러 : " + lst + ", " + loginmember);
		return lst;
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerBoard(MissingWriteDTO mw, RedirectAttributes rttr) {
		try {
			if(service.insertBoard(mw)) {
				rttr.addFlashAttribute("result", "success");
			} else {
				rttr.addFlashAttribute("result", "fail");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:/board/missing/list";
	}
	
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public ResponseEntity<String> deleteBoard(@RequestParam("no") int no) {
		try {
			if (service.deleteBoard(no)) {
				return new ResponseEntity<String>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/modify")
	public String modfiyMissing(@RequestParam("no") int no,
								Model model) {
		MissingBoardVo mb = null;
		try {
			mb = service.getMissingBoard(no);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mb.setContents(mb.getContents().replaceAll("<br />", "\r\n"));
		model.addAttribute("MissingBoard", mb);
		
		return "/board/missing/modifymissing";
	}
	
	@RequestMapping(value="/update")
	public String updateBoard(MissingWriteDTO mw, HttpServletRequest request, RedirectAttributes rttr) {
		mw.setContents(mw.getContents().replaceAll("(\r\n|\r|\n|\n\r)", "<br />"));
		try {
			if(service.updateBoard(mw)) {
				rttr.addFlashAttribute("result", "success");
			} else {
				rttr.addFlashAttribute("result", "fail");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 세션에서 로그인한 유저 아이디를 가져옴
		HttpSession ses = request.getSession();
		MemberVo mem = (MemberVo)ses.getAttribute("loginSession");
		
		
		return "redirect:/board/missing/detail?no=" + mw.getNo() + "&userid=" + mem.getUserid();
	}
	
	@RequestMapping(value="/changeCategory", method=RequestMethod.POST)
	public ResponseEntity<String> changeCategory(@RequestParam("no") int no, @RequestParam("category") String category) {
		try {
			if (service.updateCategory(no, category)) {
				return new ResponseEntity<String>("success", HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value="/bookmark", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Integer>> updateBookmark(@RequestParam("no") int no,
											@RequestParam("userid") String userid) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (service.updateBookmark(no, userid)) {
			int bookmarkCount = service.selectBookmarkCount(no);
			result.put("result", 1);
			result.put("bookmarkCount", bookmarkCount);
			return new ResponseEntity<Map<String, Integer>>(result, HttpStatus.OK);
		}
		
		result.put("result", -1);
		return new ResponseEntity<Map<String, Integer>>(result, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value="/unbookmark", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Integer>> updateUnbookmark(@RequestParam("no") int no,
															@RequestParam("userid") String userid) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (service.updateUnbookmark(no, userid)) {
			int bookmarkCount = service.selectBookmarkCount(no);
			result.put("result", 1);
			result.put("bookmarkCount", bookmarkCount);
			return new ResponseEntity<Map<String, Integer>>(result, HttpStatus.OK);
		}
		
		result.put("result", -1);
		return new ResponseEntity<Map<String, Integer>>(result, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value="/bookmarkHistory", method=RequestMethod.POST)
	public ResponseEntity<String> selectBookmard(@RequestParam("no") int no,
												@RequestParam("userid") String userid) {
		if (service.selectBookmark(no, userid)) {
			return new ResponseEntity<String>("exist", HttpStatus.OK);
		}
		return new ResponseEntity<String>("none", HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public Map<String, Object> searchList(ListParamDTO lpd, @RequestParam(value="itemsPerPage", defaultValue="20") int itemsPerPage) {
		Map<String, Object> map = null;
		System.out.println(lpd);
		try {
			map = service.selectMissingBoardList(lpd, itemsPerPage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(map.get("pagingInfo"));
		
		return map;
	}
	
}

