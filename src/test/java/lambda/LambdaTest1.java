package lambda;

public class LambdaTest1 {

    public static void main(String[] args) {

        OOOOO ooooo = new OOOOO() {
            @Override
            public String getName(String str) {
                return null;
            }

        };

        OOOOO oooo1 = (String str) -> {
            return "";
        };
    }

}

@FunctionalInterface
interface OOOOO {
    String getName(String str);

}