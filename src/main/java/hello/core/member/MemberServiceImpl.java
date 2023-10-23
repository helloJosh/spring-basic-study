package hello.core.member;

public class MemberServiceImpl implements  MemberService{
    private MemberRepository memberRepsoitory;

    public MemberServiceImpl(MemberRepository memberRepsoitory) {
        this.memberRepsoitory = memberRepsoitory;
    }

    @Override
    public void join(Member member) {
        memberRepsoitory.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepsoitory.findById(memberId);
    }
}
