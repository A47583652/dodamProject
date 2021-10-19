package com.dodam.persistence.members;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dodam.domain.members.MemberVo;;

public interface MemberDao {

	public int insertMember(MemberVo mem);
	
	public boolean send(String emailAddr, String confirmCode) throws MessagingException, AddressException;

	MemberVo loginMember(MemberVo mem);
	
	
	
}
