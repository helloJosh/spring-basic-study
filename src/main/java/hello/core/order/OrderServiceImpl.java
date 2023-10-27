package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
@Component
//@RequiredArgsConstructor // 생성자 자동생성
public class OrderServiceImpl implements OrderService{


    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    //생성자 의존관계주입 불변,필수
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, /*@Qualifier("mainDiscountPolicy")@MainDiscountPolicy*/ DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
        //this.discountPolicy = rateDiscountPolicy; -> 빈이름이 중복됐을때 필드명, 파라미터명으로 매칭하여 타입 매칭 시도 가능

    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId,itemName,itemPrice,discountPrice);
    }


    // 필드 의존관계 주입 : 문제점 순수자바코드로 테스트하기로힘듬 -> setter로 열어놔야함
    //@Autowired private MemberRepository memberRepository;
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //@Autowired private DiscountPolicy discountPolicy;


    /* Setter 의존관계 주입 선택,변경 가능
    @Autowired(required = false)
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository=memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy){
        this.discountPolicy=discountPolicy;
    }
    */

    //테스트용도
    public MemberRepository getMemberRepository() {return memberRepository;}
}
