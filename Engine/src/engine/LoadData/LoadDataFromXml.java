package engine.LoadData;

import DTOS.Validators.xmlFileValidatorDTO;
import engine.enigma.*;
import engine.enigma.Machine.EnigmaMachine;
import engine.enigma.keyboard.Keyboard;
import engine.enigma.reflector.Reflector;
import engine.enigma.reflector.inputOutputPair;
import engine.enigma.rotors.RotatingRotor;
import engine.LoadData.jaxb.schema.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadDataFromXml implements LoadData {
    private final static String JAXB_XML_PACKAGE_NAME = "engine.LoadData.jaxb.schema.generated";
    public EnigmaMachine loadDataFromInput(String FilePath, xmlFileValidatorDTO validator){

        return loadDataFromXml(FilePath,validator);

    }
    private EnigmaMachine loadDataFromXml(String FilePath, xmlFileValidatorDTO validator) {
        CTEEnigma anigma;
        try {
            InputStream inputStream = new FileInputStream(new File(FilePath));
            anigma = deserializeFrom(inputStream);
        } catch (JAXBException e) {
            validator.addException(new RuntimeException("Error: invalid file were entered"));
            return null;
        } catch(FileNotFoundException e){
                validator.addException(new RuntimeException("Error: invalid file path were entered"));
                return null;
            }
        validator.setMachine(anigma.getCTEMachine());
        validator.isValidMachineInputFromFile();
        if(validator.getListOfExceptions().size()== 0)
            return deserializeMachineInput(anigma.getCTEMachine());
        else
            return null;

    }

    private EnigmaMachine deserializeMachineInput(CTEMachine MachineInput){
        Keyboard keyboard = getKeyBoard(MachineInput);
        List<RotatingRotor> rotors = getRotorsFromInput(MachineInput);
        int amoutOfRotorsToUse = MachineInput.getRotorsCount();
        List<CTEReflector> reflectorsToTransfer = MachineInput.getCTEReflectors().getCTEReflector();
        List<Reflector> reflectors = getReflectorsFromInput(reflectorsToTransfer);
        return new EnigmaMachine(keyboard, rotors,amoutOfRotorsToUse, reflectors);
    }
    private Keyboard getKeyBoard(CTEMachine MachineInput){
        String ABC = MachineInput.getABC();
        ABC = xmlFileValidatorDTO.getCleanAlphabet(ABC);
        return new Keyboard(ABC);
    }



    private List<Reflector> getReflectorsFromInput(List<CTEReflector> reflectorsInput){
        List<Reflector> reflectorsToReturn = new ArrayList<>();
        for(CTEReflector inputReflector: reflectorsInput){
            Reflector reflector = translateReflectorInput(inputReflector);
            reflectorsToReturn.add(reflector);
        }
        return reflectorsToReturn;
    }
    private Reflector translateReflectorInput(CTEReflector inputReflector){
        String id = inputReflector.getId();
        List<inputOutputPair> pairs = getPairsInputOutput(inputReflector.getCTEReflect());
        return new Reflector(id,pairs);
    }
    private List<inputOutputPair> getPairsInputOutput(List<CTEReflect> inputReflector){
        List<inputOutputPair> pairOfData = new ArrayList<>();
        for (int i = 0; i <inputReflector.size() ; i++) {
            int input = inputReflector.get(i).getInput() -1;
            int output = inputReflector.get(i).getOutput() -1;
            pairOfData.add(new inputOutputPair(input,output));
        }
        return pairOfData;

    }
    private List<RotatingRotor> getRotorsFromInput(CTEMachine MachineInput){
        List<CTERotor> rotorsInput =  MachineInput.getCTERotors().getCTERotor();
        List<RotatingRotor> rotorsToReturn = new ArrayList<RotatingRotor>();
        for (CTERotor inputRotor: rotorsInput) {
            RotatingRotor rotor = translateRotorInput(inputRotor);
            rotorsToReturn.add(rotor);
        }
        return rotorsToReturn;
    }
    private RotatingRotor translateRotorInput(CTERotor inputRotor){
        int notch = inputRotor.getNotch() -1;
        int tempId = inputRotor.getId();
        String id = String.valueOf(tempId);
        List<CTEPositioning> inputPosition = inputRotor.getCTEPositioning();
        List<pairOfData> pairOfData = getPairOfData(inputPosition);
        return new RotatingRotor(notch, id, pairOfData);
    }
    private List<pairOfData> getPairOfData(List<CTEPositioning> inputPosition)
    {
        List<pairOfData> pairOfData = new ArrayList<>();
        for (int i = 0; i <inputPosition.size() ; i++) {
            Character right = inputPosition.get(i).getRight().charAt(0);
            Character left = inputPosition.get(i).getLeft().charAt(0);
            pairOfData.add(new pairOfData(right,left));
        }
        return pairOfData;
    }
    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }
    private EnigmaMachine deserializeMachine(CTEEnigma enigmaMachineInput){




        return null;
    }

}
