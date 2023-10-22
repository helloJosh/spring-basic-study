package hello.core.member;

public class MemberServiceImpl implements  MemberService{
    private final MemberRepository memberRepsoitory = new MemoryMemberRepository();
    @Override
    public void join(Member member) {
        memberRepsoitory.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepsoitory.findById(memberId);
    }
}
