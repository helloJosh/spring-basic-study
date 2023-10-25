package hello.core.singleton;

public class SingletonService {
    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance(){
        return instance;
    }

    private SingletonService(){}
    public void logic(){
        System.out.println("싱글톤 객체 로직 호출");
    }

    //무상태로 설계 : 값을 수정x, 가급적 읽기만
}
