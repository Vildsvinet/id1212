public class Test {

    public static void main(String[] args){
        System.out.println("hello world");
        parseRequestString("hej=1ölsnv4");
    }

    private static int parseRequestString(String str){
        int place = str.indexOf("=");
        String subStr = str.substring(place+1, place+4);
        int requestGuess = Integer.parseInt(subStr.replaceAll("[^0-9]", "")); //skicka till Game!!
        System.out.println(requestGuess);
        return 0;
    }

}


