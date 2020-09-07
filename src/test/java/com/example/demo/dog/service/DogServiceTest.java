package com.example.demo.dog.service;

import com.example.demo.config.security.Role;
import com.example.demo.dog.dto.DogResponseDto;
import com.example.demo.dog.dto.DogSaveRequestDto;
import com.example.demo.dog.dto.DogUpdateRequestDto;
import com.example.demo.dog.repository.DogRepository;
import com.example.demo.member.domain.Member;
import com.example.demo.member.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DogServiceTest {

    @Autowired
    private DogService dogService;
    @Autowired
    private DogRepository dogRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 반려견_등록() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder()
                .role(Role.GUEST)
                .build());

        // when
        Long savedId = dogService.dog_SignUp(DogSaveRequestDto.builder()
                .name("별이")
                .member(member)
                .build());

        // then
        List<DogResponseDto> dogs = dogService.findAllDesc(member);
        DogResponseDto dog = dogService.findById(savedId);
        System.out.println("dog.getName() = " + dog.getName());
        System.out.println("dogs.get(0).getName() = " + dogs.get(0).getName());
        assertEquals(dogs.get(0).getName(),"별이");
        assertEquals(dog.getName(),"별이");
    }
    
    @Test
    public void 반려견_정보수정() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder()
                .role(Role.GUEST)
                .build());

        Long savedId = dogService.dog_SignUp(DogSaveRequestDto.builder()
                .name("별이")
                .member(member)
                .build());
        
        // when
        Long updatedId = dogService.update(savedId, DogUpdateRequestDto.builder()
                .name("달이")
                .build());

        // then
        List<DogResponseDto> dogs = dogService.findAllDesc(member);
        DogResponseDto dog = dogService.findById(updatedId);
        System.out.println("dog.getName() = " + dog.getName());
        System.out.println("dogs.get(0).getName() = " + dogs.get(0).getName());
        assertEquals(dogs.get(0).getName(),"달이");
        assertEquals(dog.getName(),"달이");

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void 반려견_정보삭제() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder()
                .role(Role.GUEST)
                .build());

        Long savedId = dogService.dog_SignUp(DogSaveRequestDto.builder()
                .name("별이")
                .member(member)
                .build());

        // when
        dogService.delete(savedId);

        // then
        List<DogResponseDto> dogs = dogService.findAllDesc(member);
        assertEquals(dogs.get(0).getName(),"별이");
        fail("반려견이 없으니까 예외처리 발생");
    }
}