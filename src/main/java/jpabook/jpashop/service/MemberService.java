package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //읽기 전용일땐 readOnly = true 해줘야 최적화 됨
//@AllArgsConstructor //밑에 생성자 만들어줌
@RequiredArgsConstructor //final있는 field만 가지고 생성자 만들어줌
public class MemberService {

    private final MemberRepository memberRepository; //변경할 일이 없기 때문에 final 해 줌

//    @Autowired //생성자 Injection (@Autowired 없어도 생성자 하나일 시 스프링이 자동으로 Injection 해줌)
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    @Autowired //스프링이 스프링빈에 등록되어있는 MemberRepository를 injection 해 줌 (Field Injection)
//    private MemberRepository memberRepository;

    //settter Injection
//    private MemberRepository memberRepository;
//
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId(); //em.persist(member) -> 영속성 컨텍스트에 Member 객체를 올릴 때 key(id), value
    }

    private void validateDuplicateMember(Member member) { //똑같은 이름 둘 이 동시에 DB에 insert 시 문제 될 수 있음 따라서, name을 unique 제약조건으로 하기
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }
}
