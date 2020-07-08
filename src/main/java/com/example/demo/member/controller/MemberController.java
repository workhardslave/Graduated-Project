package com.example.demo.member.controller;

import com.example.demo.config.auth.LogExecutionTime;
import com.example.demo.config.auth.LoginFindMember;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.security.Role;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.vo.Member;
import com.example.demo.member.vo.MemberResponseDto;
import com.example.demo.member.vo.MemberSaveRequestDto;
import com.example.demo.overlap.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
/**
 * 세션부분 추후 @Aspect 적용하기.
 * */
@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {


    private final MemberService memberService;

    @GetMapping("/")
    public String home(){

        return "home";
    }

    // 회원가입
    @GetMapping("/member/signup")
    public String createMember(Model model) {
        model.addAttribute("memberForm", new MemberForm());

        return "memberAuth/signUp";
    }

    // 회원가입 API
    @PostMapping(value = "/api/member/signup")
    public String createMemberApi(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "memberAuth/signUp";
        }

        Address address = new Address(form.getCity(),
                form.getZipcode(),form.getStreet());
        MemberSaveRequestDto member = new MemberSaveRequestDto();

        memberService.SignUp(member.builder()
                .name(form.getName())
                .address(address)
                .birth(form.getBirth())
                .email(form.getEmail())
                .password(form.getPassword())
                .phone(form.getPhone())
                .role(form.getRole())
                .build());

        return "memberAuth/signIn";
    }

    //회원정보 리스트
    @GetMapping(value = "/admin/members")
    @LogExecutionTime
    public String readAllMemberAdmin(Model model) {
        List<MemberResponseDto> members = memberService.findAllDesc();
        model.addAttribute("members", members);

        return "admin/memberList";
    }

    @GetMapping("/member/mypage")
    public String readMember(Model model, @LoginFindMember Member member) {

        if(member != null) {
            model.addAttribute("member", member);
        }

        return "memberAuth/myPage";
    }

    // 회원 정보수정 페이지
    @GetMapping("/member/settings/{id}")
    public String updateMember(@PathVariable Long id, Model model) {

        MemberResponseDto dto = memberService.findById(id);
        model.addAttribute("member", dto);

        return "memberAuth/settings";
    }

    // 관리자 회원정보 수정페이지
    @GetMapping("/admin/member/settings/{id}")
    public String updateMemberAdmin(@PathVariable Long id, Model model){

        MemberResponseDto dto = memberService.findById(id);

        model.addAttribute("member", dto);
        log.info(dto.getPassword());

        return "admin/settings";
    }

    // 로그인 페이지
    @GetMapping("/member/login")
    public String dispLogin() throws Exception
    {
        return "memberAuth/signIn";
    }

    // 관리자 정보조회
    @GetMapping("/admin/mypage")
    public String readAdmin(Model model, @LoginFindMember Member admin) {

        if(admin != null) {
            model.addAttribute("admin", admin);
        }
        return "adminAuth/admin_myPage";
    }

    // 관리자 정보수정
    @GetMapping("/admin/settings/{id}")
    public String updateAdmin(@PathVariable Long id, Model model) {

        MemberResponseDto adminDto = memberService.findById(id);
        model.addAttribute("admin", adminDto);

        return "adminAuth/admin_settings";
    }
}