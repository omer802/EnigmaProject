package EngineMachine;

import java.util.ArrayList;
import java.util.List;

public class main {
    //public machine(String alphabetInput, List<RotatingRotor> rotorsInput, Reflector reflectorInput, PlugBoard plugBoardInput)
    public static void main(String[] args) {
        ///*** alphabet
        String alphabet = "abcdef";
        List<RotatingRotor> rotors = makeRotors();
        char ca = (char) ('0');
        char cb = (char) ('1');
        char cc = (char) ('2');
        char cd = (char) ('3');
        char ce = (char) ('4');
        char cf = (char) ('5');


        List<pairOfData> pairsreflector = new ArrayList<>();

        pairsreflector.add(new pairOfData(ca, cd));
        pairsreflector.add(new pairOfData(cb, ce));
        pairsreflector.add(new pairOfData(cc, cf));
        Reflector toReflect = new Reflector(pairsreflector);
        String connections = "c|b";
        PlugBoard plugConnections = new PlugBoard(connections);
        PlugBoard plug = new PlugBoard();
        Machine enigmaMechine = new Machine(alphabet, rotors, toReflect, plugConnections);
        String str = enigmaMechine.encodeString("abcdeabcde");
        System.out.println(str);

    }

    static List<RotatingRotor> makeRotors() {
        List<RotatingRotor> rotorsToReturn = new ArrayList<>();
        List<pairOfData> pairsrotors = new ArrayList<>();
        List<pairOfData> pairsrotors2 = new ArrayList<>();

        char ca = (char) ('a');
        char cb = (char) ('b');
        char cc = (char) ('c');
        char cd = (char) ('d');
        char ce = (char) ('e');
        char cf = (char) ('f');
        //rotor 1
        pairsrotors.add(new pairOfData(ca, cf));
        pairsrotors.add(new pairOfData(cb, ce));
        pairsrotors.add(new pairOfData(cc, cd));
        pairsrotors.add(new pairOfData(cd, cc));
        pairsrotors.add(new pairOfData(ce, cb));
        pairsrotors.add(new pairOfData(cf, ca));
        RotatingRotor rotor = new RotatingRotor(3, 2, "3", pairsrotors);
        rotorsToReturn.add(rotor);
        //rotor 2
        pairsrotors2.add(new pairOfData(ca, ce));
        pairsrotors2.add(new pairOfData(cb, cb));
        pairsrotors2.add(new pairOfData(cc, cd));
        pairsrotors2.add(new pairOfData(cd, cf));
        pairsrotors2.add(new pairOfData(ce, cc));
        pairsrotors2.add(new pairOfData(cf, ca));
        RotatingRotor rotor2 = new RotatingRotor(0, 2, "4", pairsrotors2);
        rotorsToReturn.add(rotor2);
        return rotorsToReturn;
    }
}











    //******************check machine pisces******************
    //********Check plugBoard*************
    //String plugBoardstr = "a|b,c|d";
    // plugBoard inputPlag = new plugBoard(plugBoardstr);
    // System.out.println(inputPlag);
    //***** good for check rotor
     /*   List<pairOfData> pairs = new ArrayList<>();
        for (int i = 0; i <23 ; i++) {
            char c1 = (char) ('a' +i);
            char c2 = (char) ('b' +i);
            pairOfData pairsa = new pairOfData(c1,c2);
            pairs.add(pairsa);

        }*******check rotor****************
       /* List<pairOfData> pairs = new ArrayList<>();
        char ca = (char) ('a');
        char cb = (char) ('b');
        char cc = (char) ('c');
        char cd = (char) ('d');
        char ce = (char) ('e');
        char cf = (char) ('f');
        pairs.add(new pairOfData(ca,cf));
        pairs.add(new pairOfData(cb,ce));
        pairs.add(new pairOfData(cc,cd));
        pairs.add(new pairOfData(cd,cc));
        pairs.add(new pairOfData(ce,cb));
        pairs.add(new pairOfData(cf,ca));
        System.out.println(pairs);
        rotatingRotor rotor = new rotatingRotor(0,0,"0",pairs);
        int nextIndx = rotor.convertRightToLeft(3);
        int nextIndx2 = rotor.convertLeftToRight(1);
        System.out.println(nextIndx);
        System.out.println(nextIndx2);*/

    //***************check reflector***********
    // reflector toReflect = new reflector(pairs);
    //int nextIndex = toReflect.reflect(1);
    //System.out.println(nextIndex);

