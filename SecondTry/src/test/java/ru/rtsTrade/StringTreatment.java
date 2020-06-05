package ru.rtsTrade;

public class StringTreatment {

    private static int getPos(String s){
        if (s.charAt(s.length()-1)=='.') return s.length()-4;
        else  return s.length()-3;

    }

    private static String spaceFree (String s){
        while (s.indexOf(" ")!=-1){
           s= s.substring(0, s.indexOf(" ")) + s.substring(s.indexOf(" ") + 1);
        }
        s=s.replace(',','.');
        return s;
    }


    public static  double getMoney(String s, Config conf){

        if ((s.equals("Не определена")))    return 0;

        String currency = s.substring(getPos(s));
        double money = Double.parseDouble(spaceFree(s.substring(0,getPos(s)-1)));

        if (currency.equals("EUR"))     return (money*conf.getEUR());
        if (currency.equals("USD"))     return  (money*conf.getUSD());
        else                            return  money;


    }//getMoney
}
